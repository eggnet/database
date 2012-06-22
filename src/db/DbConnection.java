package db;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import models.Change;
import models.Commit;
import models.CommitDiff;
import models.CommitFamily;
import models.DiffEntry;
import models.DiffEntry.diff_types;
import models.FileCache;
import models.FileDiff;
import db.Resources.ChangeType;
import db.util.AExecutionItem;
import db.util.ISetter;
import db.util.ISetter.StringSetter;
import db.util.ISetter.TimestampSetter;
import db.util.PreparedStatementExecutionItem;

public abstract class DbConnection {
	protected String branchName = null;
	protected String branchID = null;
	
	private Queue<AExecutionItem> executionQueue = new ConcurrentLinkedQueue<AExecutionItem>();
	private String dbName;
	private int queueSize = 4;
	private boolean stopWorkers = false;
	private List<QueueWorker> queueWorkers = new ArrayList<QueueWorker>();
	private int	queueLimit = 10000;
	
	protected DbConnection() 
	{
		try 
		{
			Class.forName("org.postgresql.Driver").newInstance();
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	public boolean connect(String dbName) {
		this.dbName = dbName;
		this.stopWorkers = false;
		this.startWorkers(queueSize);
		return true;
	}
	
	public boolean connect(String dbName, int numQueueWorkers) {
		this.queueSize = numQueueWorkers;
		return this.connect(dbName);
	}
	
	public boolean connect(String dbNAme, int numQueueWorkers, int queueLimit) {
		this.queueLimit = queueLimit;
		return this.connect(dbNAme, numQueueWorkers);
	}
	
	public boolean close() {
		try {
			this.stopWorkers();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void runScript(Reader isr) throws IOException, SQLException {
		Connection conn = getConnection(this.dbName);
		ScriptRunner sr = new ScriptRunner(conn, false, true);
		sr.setLogWriter(null);
		sr.runScript(isr);
		conn.close();
	}
	
	public String getBranchID() {
		return branchID;
	}
	
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public String getBranchName() {
		return branchName;
	}

	public void execBatch() {}
	
	/**
	 * blocking
	 * 
	 * Should be called AFTER @see {@link #connect(String)}, as it also does 
	 * a lookup on the branchID and sets it behind the scenes.
	 * Also does a lookup in the branches table for 
	 * @param branchName
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
		try
		{
			String query = "SELECT branch_id from branches where branch_name ~ ? LIMIT 1";
			ISetter[] params = {new StringSetter(1,branchName)};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(query, params);
			this.addExecutionItem(ei);
			ei.waitUntilExecuted();
			ei.getResult().next();
			setBranchID(ei.getResult().getString(1));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * blocking
	 * 
	 * Executes the given query with escaped values in String[] params in place of
	 * ? characters in sql.
	 * @param sql ex. "SELECT * FROM something where my_column=?"
	 * @param params ex. {"braden's work"}
	 * @return Query ResultSet on success, null otherwise
	 */
	@Deprecated
	public ResultSet execPreparedQuery(String sql, String[] params)
	{
		ISetter[] ps = null;
		if (params != null && params.length > 0) {
			ps = new ISetter[params.length];
			for (int i = 0; i < params.length; ++i) {
				ps[i] = new StringSetter(i+1,params[i]);
			}
		}
		PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, ps);
		this.addExecutionItem(ei);
		ei.waitUntilExecuted();
		return ei.getResult();
	}

	public String getTimeStamp(String commit_id)
	{
		try {
			String[] params = {commit_id, this.branchID};
			ResultSet rs = execPreparedQuery("SELECT commit_date from commits where commit_id =? and (branch_id=? or branch_id is NULL);", params);
			if(rs.next())
				return rs.getString(1);
			else
				return "";
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public FileDiff getFileDiffForCommitFamily(CommitFamily comFam, List<CommitDiff> comDiffs, String fileName)
	{
		for(CommitDiff cd: comDiffs)
		{
			if(cd.getNew_commit_id().equals(comFam.getChildId()) && 
			   cd.getOld_commit_id().equals(comFam.getParentId()))
			{
				for(FileDiff fd : cd.getFileDiffs())
				{
					if(fd.getFile_id().equals(fileName))
						return fd;
				}
			}
		}
		
		return null;
	}
	
	public DiffEntry getEarliestInsert(int lastEndChar, List<DiffEntry> insertList)
	{
		DiffEntry entry = null;
		for(DiffEntry cur : insertList)
		{
			if(cur.getChar_start() >= lastEndChar)
			{
				if(entry!=null)
				{
					if(entry.getChar_start() >= cur.getChar_start())
						entry = cur;
				}
				else
				{
					entry = cur;
				}
			}
		}
		
		return entry;
	}
	
	public DiffEntry getLatestDelete(int lastStartChar, List<DiffEntry> deleteList)
	{
		DiffEntry entry = null;
		for(DiffEntry cur : deleteList)
		{
			if(cur.getChar_end() <= lastStartChar)
			{
				if(entry!=null)
				{
					if(entry.getChar_start() <= cur.getChar_start())
						entry = cur;
				}
				else
				{
					entry = cur;
				}
			}
		}
		
		return entry;
	}
	
	/**
	 * Construct raw file from diffs object
	 * @param fileID
	 * @param commitID
	 * @return
	 * @throws Exception 
	 */
	public String getRawFileFromDiffTree(String fileID, String commitID, List<CommitFamily> commitPath)
	{
		try
		{
			String rawFile = "";
			
			List<CommitDiff> commitDiffs = getDiffTreeFromFirstCommit(fileID, commitID);
			List<CommitFamily> shortestCommitPath = new ArrayList<CommitFamily>();
			Map<String, FileCache> fileCaches = getFileCachesFromCommit(fileID, commitID);
			
			// Rebuild the commit path that has the latest Add entry in it.
			for(CommitFamily cf: commitPath)
			{
				// this commit Family has the lastest Add for the file, start from here
				FileDiff fd = getFileDiffForCommitFamily(cf, commitDiffs, fileID);
				if(fd != null)
				{
					// If the commit has a raw file for this, just get the raw file
					if(fileCaches.containsKey(cf.getChildId()))
					{
						rawFile = fileCaches.get(cf.getChildId()).getRaw_file();
						break;
					}
					else
					if(fd.isAddCommit())
					{
						shortestCommitPath.add(cf);
						break;
					}
					else
					{
						shortestCommitPath.add(cf);
					}
				}
			}
	
			// Create raw file from the beginning of shortest path
			for(int i =shortestCommitPath.size() - 1; i >= 0; i--)
			{
				// get commitDiff
				FileDiff file = getFileDiffForCommitFamily(shortestCommitPath.get(i), commitDiffs, fileID);
				if(file == null)
					continue;
				
				if(file.isAddCommit())
				{
					//Should have only single DiffEntry - DIFF_ADD
					if(file.getDiffEntries().size()>0)
						rawFile = file.getDiffEntries().get(0).getDiff_text();
					
					continue;
				}
				else if(file.isDeleteCommit())
				{
					rawFile = "";
				}
				else //create the next version of the file
				{
					List<DiffEntry> deleteList = new ArrayList<DiffEntry>();
					List<DiffEntry> insertList = new ArrayList<DiffEntry>();
				
					// Store list of Delete Entry backward
					for(DiffEntry entry: file.getDiffEntries())
					{
						if(entry.getDiff_type() == diff_types.DIFF_MODIFYDELETE)
							deleteList.add(entry);
						else if(entry.getDiff_type() == diff_types.DIFF_MODIFYINSERT)
							insertList.add(entry);
					}		
					
					//Get Original Equal in reverse order
					int lastStart = rawFile.length();
					for(int j =deleteList.size() - 1; j >= 0; j--)
					{
						DiffEntry entry = getLatestDelete(lastStart, deleteList);
						if(entry == null)
							continue;
						
						if(entry.getChar_end() <= lastStart)
						{
							lastStart = entry.getChar_start();
							
							//Remove the delete entry
							int firstEnd    = entry.getChar_start();
							int secondStart = entry.getChar_end();
							if(firstEnd < 0)
								firstEnd = 0;
							if(secondStart > rawFile.length() - 1)
								secondStart = rawFile.length() - 1;
							if(secondStart <0)
								secondStart =0;
							
							// Encounter exception, ignore for now
							if(firstEnd > rawFile.length())
							{
								String error = "Delete Raw file error: " + file.getFile_id() + " for " + commitID + "\n";
								error += entry.getNewCommit_id() +"-"+ entry.getOldCommit_id() + "\n";
								error += "from:" + entry.getChar_start() +" to "+ entry.getChar_end() + "\n";
								throw new Exception("Building Rawfile messed up, we have a problem guys:" + error); 
							}
							
							// Merge back rawfile
							String firstPart  = rawFile.substring(0, firstEnd);
							String secondPart = rawFile.substring(secondStart);
							rawFile = firstPart + secondPart;
						}
					}
					
					//Have to ensure the the order 
					//Create new version of the file
					int lastEnd = 0;
					for(int k=0; k< insertList.size(); k++)
					{
						DiffEntry entry = getEarliestInsert(lastEnd, insertList);
						if(entry == null)
							continue;
						
						// store the last line, need to find the min
						if(entry.getChar_start() >= lastEnd)
						{
							lastEnd = entry.getChar_end();
		
							// Split up the Rawfile for insert
							int firstEnd    = entry.getChar_start();
							int secondStart = entry.getChar_start();
							if(firstEnd < 0)
								firstEnd = 0;
							if(secondStart > rawFile.length() - 1)
								secondStart = rawFile.length() - 1;
							if(secondStart < 0)
								secondStart =0;
							
							// Encounter exception, ignore for now
							if(firstEnd > rawFile.length())
							{
								String error = "Insert Raw file error: " + file.getFile_id() + " for " + commitID + "\n";
								error += entry.getNewCommit_id() +"-"+ entry.getOldCommit_id() + "\n";
								error += "from:" + entry.getChar_start() +" to "+ entry.getChar_end() + "\n";
								throw new Exception("Building Rawfile messed up, we have a problem guys:" + error); 
							}
							// insert new change
							String firstPart  = rawFile.substring(0, firstEnd);
							String secondPart = rawFile.substring(secondStart);
							rawFile = firstPart + entry.getDiff_text() + secondPart;
						}
					}
				}
			}
			return rawFile;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.print(e.getMessage());
			return null;
		}
		
	}
	
	/**
	 * Return a random path from a commit to the Root.
	 * @param fileID
	 * @param commitID
	 * @return
	 */
	public List<CommitFamily> getCommitPathToRoot(String commitID)
	{
		try {
			String sql = "SELECT parent, child from commit_family natural join commits where " +
					"(branch_id=? or branch_id is NULL) and " +
					"commit_date <= " +
					"(SELECT commit_date from commits where commit_id=? and (branch_id=? OR branch_id is NULL) limit 1) AND commit_id=child ORDER BY commit_date desc;";

			List<CommitFamily> rawFamilyList = new ArrayList<CommitFamily>();
			List<CommitFamily> familyList 	 = new ArrayList<CommitFamily>();
			String[] parms = {this.branchID,commitID, this.branchID};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				String parentId = rs.getString("parent");
				String childId  = rs.getString("child");
				rawFamilyList.add(new CommitFamily(parentId, childId));
			}
			
			// Get a random path from this commit to Root
			// List must be ordered from newest to oldest
			String currentChild = commitID;
			// MAGICAL LOOP OF GOODNESS - DO NOT DELETE ADRIAN!!!!
			// IT'S POWERS ARE KNOWN FAR AND WIDE
			for(CommitFamily family : rawFamilyList)
			{
				// Look for its parent
				for(CommitFamily secondFamily : rawFamilyList)
				{
					if(secondFamily.getChildId().equals(currentChild))
					{
						familyList.add(new CommitFamily(secondFamily.getParentId(), secondFamily.getChildId()));
						currentChild = secondFamily.getParentId();
						break;
					}
				}
			}
			
			return familyList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Just return the first 3 file caches found. Just to minimize the number of caches return
	 * @param fileID
	 * @param commitID
	 * @return
	 */
	public Map<String, FileCache> getFileCachesFromCommit(String fileID, String commitID)
	{
		try{
			Map<String, FileCache> cacheList = new HashMap<String, FileCache>();
			String sql = "SELECT file_id, commit_id, raw_file from commits natural join file_caches where " +
					"file_id=? and " +
					"(branch_id=? or branch_id is NULL) and commit_date<= " + 
					"(select commit_date from commits where commit_id=? and " +
					"(branch_id=? OR branch_id is NULL) limit 1) ORDER BY commit_date DESC limit 3";

			String[] params = {fileID, this.branchID, commitID, this.branchID};
			ResultSet rs = execPreparedQuery(sql, params);
			
			while(rs.next())
			{
				String commitId  = rs.getString("commit_id");
				String fileId 	    = rs.getString("file_id");
				String rawFile      = rs.getString("raw_file");
				cacheList.put(commitId, new FileCache(fileId, commitId, rawFile));
			}
			
			return cacheList;	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Return the Diff tree for a file start from root to commitID.
	 * @param fileID
	 * @param commitID
	 * @return Lis<CommitDiff> list of all the diffs from Root to Current Commit
	 */
	public List<CommitDiff> getDiffTreeFromFirstCommit(String fileID, String commitID)
	{
		try{
			// For each CommitDiff, store a list of FileDiff. For each FileDiff, store a list of DiffEntry
			List<CommitDiff> CommitList = new ArrayList<CommitDiff>();
			String sql = "SELECT file_id, new_commit_id, old_commit_id, diff_text, char_start, char_end, diff_type from commits natural join file_diffs where " +
					"file_id=? and " +
					"(branch_id=? or branch_id is NULL) and commit_date<= " + 
					"(select commit_date from commits where commit_id=? and " +
					"(branch_id=? OR branch_id is NULL) limit 1) AND new_commit_id= commit_id ORDER BY old_commit_id, new_commit_id";

			String[] params = {fileID, this.branchID, commitID, this.branchID};
			ResultSet rs = execPreparedQuery(sql, params);
			
			// Get first CommitDiff
			if (!rs.next())
				return CommitList;
			
			String currentNewCommitId = rs.getString("new_commit_id");
			String currentOldCommitId = rs.getString("old_commit_id");
			String currentFileId 	  = rs.getString("file_id");
			String currentDiffTxt     = rs.getString("diff_text");
			String currentDiffType		  = rs.getString("diff_type");
			int currentCharStart = rs.getInt("char_start");
			int currentCharEnd = rs.getInt("char_end");
			
			// Group CommitDiff by old,new commit id
			List<FileDiff> currentFileDiffList = new ArrayList<FileDiff>();
			CommitDiff currentCommitDiff = new CommitDiff(currentNewCommitId, currentOldCommitId, currentFileDiffList);
			
			// Group FileDiff by file_id
			DiffEntry de = new DiffEntry(currentFileId, currentNewCommitId, currentOldCommitId, currentDiffTxt, currentCharStart, currentCharEnd, currentDiffType);
			FileDiff currentFileDiff = new FileDiff(currentFileId, new ArrayList<DiffEntry>());
			currentFileDiff.addDiffEntry(de);
			
			while(rs.next())
			{
				// Group all CommitDiff by old and new commit id
				String newCommitId  = rs.getString("new_commit_id");
				String oldCommitId  = rs.getString("old_commit_id");
				String fileId 	    = rs.getString("file_id");
				String diffTxt      = rs.getString("diff_text");
				String diffType		= rs.getString("diff_type");
				int charStart 		= rs.getInt("char_start");
				int charEnd 		= rs.getInt("char_end");
				
				// same CommitDiff
				if (newCommitId.equals(currentNewCommitId) && oldCommitId.equals(currentOldCommitId))
				{
					// same FileDiff
					if(fileId.equals(currentFileId))
					{
						currentFileDiff.addDiffEntry(new DiffEntry(fileId, newCommitId, oldCommitId, diffTxt, charStart, charEnd, diffType));
					}
					else
					{
						// add the current FileDiff to the CommitDiff and start new fileDiff
						currentCommitDiff.addFileDiff(currentFileDiff);
						currentFileDiff = new FileDiff(fileId, new ArrayList<DiffEntry>());
						currentFileId = fileId;
					}
				}
				else
				{
					// add current CommitDiff and start new CommitDiff
					currentCommitDiff.addFileDiff(currentFileDiff);
					CommitList.add(currentCommitDiff);
					currentCommitDiff = new CommitDiff(newCommitId, oldCommitId, new ArrayList<FileDiff>()); 
					currentNewCommitId = newCommitId;
					currentOldCommitId = oldCommitId;
					
					// start new File
					currentFileDiff = new FileDiff(fileId, new ArrayList<DiffEntry>());
					currentFileId = fileId;
					
					// add new diff entry
					currentFileDiff.addDiffEntry(new DiffEntry(fileId, newCommitId, oldCommitId, diffTxt, charStart, charEnd, diffType));
				}
			}
			
			// Added last commit diff
			currentCommitDiff.addFileDiff(currentFileDiff);
			CommitList.add(currentCommitDiff);
			
			return CommitList;	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get diff entries for 2 consecutive commits
	 * @param fileID
	 * @param oldCommitID
	 * @param newCommitID
	 * @return List<DiffEntry> list of the diffs, empty if there is none
	 */
	public List<DiffEntry> getDiffsFromTwoConsecutiveCommits(String fileID, String oldCommitID, String newCommitID)
	{
		try{
			List<DiffEntry> diffList = new ArrayList<DiffEntry>();
			String sql = "SELECT file_id, new_commit_id, old_commit_id, diff_text, char_start, char_end, diff_type from file_diffs where " +
						"file_id=? and old_commit_id=? and new_commit_id=?";
			ISetter[] params = {new StringSetter(1, fileID), new StringSetter(2, oldCommitID), new StringSetter(3, newCommitID)};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			this.addExecutionItem(ei);
			ei.waitUntilExecuted();
			
			ResultSet rs = ei.getResult();
			while(rs.next())
			{
				String newCommitId  = rs.getString("new_commit_id");
				String oldCommitId  = rs.getString("old_commit_id");
				String fileId 	    = rs.getString("file_id");
				String diffTxt      = rs.getString("diff_text");
				String diffType		= rs.getString("diff_type");
				int charStart 		= rs.getInt("char_start");
				int charEnd 		= rs.getInt("char_end");
				
				diffList.add(new DiffEntry(fileId, newCommitId, oldCommitId, diffTxt, charStart, charEnd, diffType));
			}
			
			return diffList;	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Checks whether or not a commit is included in the owners table
	 * @param CommitId
	 * @return
	 */
	public boolean isCommitInOwners(String CommitId) 
	{
		try
		{
			String sql = "SELECT commit_id from owners where commit_id=?;";
			String[] params = {CommitId};
			ResultSet rs = execPreparedQuery(sql, params);
			if (rs.next())
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Gets the latest commit in the owners table.
	 * @return CommitID
	 */
	public String getLastOwnerCommit() 
	{
		try 
		{
			String sql = "Select commit_id from owners natural join commits order by id desc;";
			String[] parms = {};
			ResultSet rs = execPreparedQuery(sql, parms);
			if (rs.next())
				return rs.getString(1);
			else
				return null;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Gets the latest commit in the commits table
	 * @return CommitID 
	 */
	public String getLastCommit() 
	{
		try 
		{
			String sql = "Select commit_id from commits order by id desc;";
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, null);
			this.addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			if (rs.next())
				return rs.getString(1);
			else
				return null;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public Commit getCommit(String CommitId)
	{
		String sql = "SELECT commit_id, author, author_email, comments, commit_date, branch_id FROM Commits where commit_id=? and (branch_id=? or branch_id is NULL);";
		ISetter[] params = {new StringSetter(1,CommitId), new StringSetter(2,this.branchID)};
		PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
		this.addExecutionItem(ei);
		return new Commit(ei);
	}
	
	public void insertOwnerRecord(String CommitId, String Author, String FileId, int ChangeStart, int ChangeEnd, ChangeType changeType)
	{
		String sql = "INSERT INTO owners values (?,?,?,'" + ChangeStart + "','" + ChangeEnd + "', ?)";
		ISetter[] params = {new StringSetter(1,CommitId), new StringSetter(2, Author), new StringSetter(3, FileId), new StringSetter(4, changeType.toString())};
		this.addExecutionItem(new PreparedStatementExecutionItem(sql, params));
	}
	
	public Change getOwnerChangeBefore(String FileId, int CharStart, Timestamp CommitDate)
	{
		String sql = "SELECT commit_id, file_id, owner_id, char_start, char_end, change_type FROM owners natural join commits where file_id=? and commit_id=?" +
				"and char_start='" + CharStart + "' and (branch_id=? OR branch_id is NULL) and commit_date < "+ CommitDate + " order by id desc";
		ISetter[] params = {new StringSetter(1,FileId), new StringSetter(2,branchID)};
		PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
		this.addExecutionItem(ei);
		return new Change(ei);
	}
		
	public Change getLatestOwnerChange(String fileId, int start, int end, Timestamp commitDate)
	{
		String sql = "SELECT commit_id, file_id, owner_id, char_start, char_end, change_type FROM owners natural join commits where file_id=? AND commit_date < ? AND "
				+ "(branch_id=? OR branch_id is NULL) order by id desc";
		ISetter[] params = {new StringSetter(1,fileId), new TimestampSetter(2, commitDate), new StringSetter(3, branchID)};
		PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
		this.addExecutionItem(ei);
		return new Change(ei);
	}
	
	public void setConnectionString(String dbName) {
		this.dbName = dbName;
	}
	
	private synchronized void waiting(long ms) {
		try
		{
			this.wait(ms);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean addExecutionItem(AExecutionItem ei) {
		while (this.queueLimit  < this.executionQueue.size()) this.waiting(1);
		return this.executionQueue.add(ei);
	}
	
	private Connection getConnection(String dbName) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(Resources.dbUrl + dbName.toLowerCase(), Resources.dbUser, Resources.dbPassword);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	public void startWorkers(int numberOfThreads) {
		for (int i = 0; i < numberOfThreads; ++i) {
			QueueWorker qw = new QueueWorker(getConnection(this.dbName));
			qw.start();
			this.queueWorkers.add(qw);
		}
	}
	
	private class QueueWorker extends Thread {
		private Connection conn = null;
		
		public QueueWorker(Connection connection) {
			this.conn = connection;
		}
		
		private synchronized void waiting(long ms) {
			try {
				this.wait(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			AExecutionItem itemToBeExecuted = null;
			while (!executionQueue.isEmpty() || !stopWorkers) {
				if (itemToBeExecuted != null) itemToBeExecuted = executionQueue.poll();
				if (itemToBeExecuted != null) {
					AExecutionItem nextItem = executionQueue.poll();
					while (itemToBeExecuted.combine(nextItem)) {
						nextItem = executionQueue.poll();
					}
					itemToBeExecuted.execute(this.conn);
					itemToBeExecuted = nextItem;
				}
				if (executionQueue.isEmpty() && itemToBeExecuted == null) this.waiting(1);
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
	public void stopWorkers() throws InterruptedException {
		this.stopWorkers = true;
		for (QueueWorker qWorker : queueWorkers) {
			qWorker.join();
		}
	}
}
