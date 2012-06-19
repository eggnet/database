package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.DbConnection.PreparedStatementExecutionItem;
import db.Resources;
import db.Resources.ChangeType;

public class Change
{	
	protected String				CommitId;
	protected String				OwnerId;
	protected String				FileId;
	protected int					CharStart;
	protected int					CharEnd;
	protected Resources.ChangeType	ChangeType;
	private PreparedStatementExecutionItem executionItem;
	private boolean wasRetrieved = false;

	public Change()
	{ }
	
	public Change(Change other)
	{
		CommitId = other.getCommitId();
		OwnerId = other.getOwnerId();
		FileId = other.getFileId();
		CharStart = other.getCharStart();
		CharEnd = other.getCharEnd();
		ChangeType = other.getChangeType();
	}
	
	public Change(String ownerId, String commitId, ChangeType changeType, String fileId, int charStart, int charEnd)
	{
		super();
		CommitId = commitId;
		OwnerId = ownerId;
		FileId = fileId;
		CharStart = charStart;
		CharEnd = charEnd;
		ChangeType = changeType;
	}

	public Change(PreparedStatementExecutionItem ei) {
		this.executionItem = ei;
	}
		
	public String getCommitId()
	{
		retrieve();
		return CommitId;
	}

	public void setCommitId(String commitId)
	{
		CommitId = commitId;
	}

	public String getOwnerId()
	{
		retrieve();
		return OwnerId;
	}

	public void setOwnerId(String ownerId)
	{
		OwnerId = ownerId;
	}

	public String getFileId()
	{
		retrieve();
		return FileId;
	}

	public void setFileId(String fileId)
	{
		FileId = fileId;
	}

	public int getCharStart()
	{
		retrieve();
		return CharStart;
	}

	public void setCharStart(int charStart)
	{
		CharStart = charStart;
	}

	public int getCharEnd()
	{
		retrieve();
		return CharEnd;
	}

	public void setCharEnd(int charEnd)
	{
		CharEnd = charEnd;
	}

	public Resources.ChangeType getChangeType()
	{
		retrieve();
		return ChangeType;
	}

	public void setChangeType(Resources.ChangeType changeType)
	{
		ChangeType = changeType;
	}
	
	private void retrieve() {
		if (!wasRetrieved) {
			this.executionItem.waitUntilExecuted();
			wasRetrieved = true;
			ResultSet rs = this.executionItem.getResult();
			try {
				if (rs.next()) {
					CommitId = rs.getString("commit_id");
					OwnerId = rs.getString("owner_id");
					FileId = rs.getString("file_id");
					CharStart = rs.getInt("char_start");
					CharEnd = rs.getInt("char_end");
					ChangeType = Resources.ChangeType.valueOf(rs.getString("change_type"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
