package models;

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

	public String getCommitId()
	{
		return CommitId;
	}

	public void setCommitId(String commitId)
	{
		CommitId = commitId;
	}

	public String getOwnerId()
	{
		return OwnerId;
	}

	public void setOwnerId(String ownerId)
	{
		OwnerId = ownerId;
	}

	public String getFileId()
	{
		return FileId;
	}

	public void setFileId(String fileId)
	{
		FileId = fileId;
	}

	public int getCharStart()
	{
		return CharStart;
	}

	public void setCharStart(int charStart)
	{
		CharStart = charStart;
	}

	public int getCharEnd()
	{
		return CharEnd;
	}

	public void setCharEnd(int charEnd)
	{
		CharEnd = charEnd;
	}

	public Resources.ChangeType getChangeType()
	{
		return ChangeType;
	}

	public void setChangeType(Resources.ChangeType changeType)
	{
		ChangeType = changeType;
	}
}
