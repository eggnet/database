package models;

import java.sql.Timestamp;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

import db.Resources;

import models.jira.JiraIssue;

public class Issue
{
	private int itemID;
	private String status;
	private int assignedID = -1;
	private String assignee;
	private Timestamp creationTS;
	private Timestamp lastModifiedTS;
	private String title;
	private String description;
	private int creatorID;
	private String creator;
	private String keywords;
	private String issueNum;
	
	public Issue(JiraIssue issue) throws ParseException
	{
		status = issue.getFields().getStatus().getName();
		
		if (issue.getFields().getAssignee() != null)
			assignee = issue.getFields().getAssignee().getEmailAddress();
	
		java.util.Date createdDate = Resources.JiraDateFormat.parse(issue.getFields().getCreated().replace("T", " "));
		java.util.Date modifiedDate = Resources.JiraDateFormat.parse(issue.getFields().getCreated().replace("T", " "));
		
		creationTS = new Timestamp(createdDate.getTime());
		lastModifiedTS = new Timestamp(modifiedDate.getTime());
		
		title = issue.getFields().getSummary();
		description = issue.getFields().getDescription();
		creator = issue.getFields().getReporter().getEmailAddress();
		if (issue.getFields().getLabels() != null)
			keywords = StringUtils.join(issue.getFields().getLabels(), " , ");
		issueNum = issue.getKey();
	}	
	
	public Issue()
	{
		super();
	}

	public Issue(int itemID, String status, int assignedID,
			Timestamp creationTS, Timestamp lastModifiedTS, String title,
			String description, int creatorID, String keywords, String issueNum)
	{
		super();
		this.itemID = itemID;
		this.status = status;
		this.assignedID = assignedID;
		this.creationTS = creationTS;
		this.lastModifiedTS = lastModifiedTS;
		this.title = title;
		this.description = description;
		this.creatorID = creatorID;
		this.keywords = keywords;
		this.issueNum = issueNum;
	}
	
	

	public Issue(int itemID, String status, String assignee,
			Timestamp creationTS, Timestamp lastModifiedTS, String title,
			String description, String creator, String keywords, String issueNum)
	{
		super();
		this.itemID = itemID;
		this.status = status;
		this.assignee = assignee;
		this.creationTS = creationTS;
		this.lastModifiedTS = lastModifiedTS;
		this.title = title;
		this.description = description;
		this.creator = creator;
		this.keywords = keywords;
		this.issueNum = issueNum;
	}

	public int getItemID()
	{
		return itemID;
	}

	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public int getAssignedID()
	{
		return assignedID;
	}

	public void setAssignedID(int assignedID)
	{
		this.assignedID = assignedID;
	}

	public Timestamp getCreationTS()
	{
		return creationTS;
	}

	public void setCreationTS(Timestamp creationTS)
	{
		this.creationTS = creationTS;
	}

	public Timestamp getLastModifiedTS()
	{
		return lastModifiedTS;
	}

	public void setLastModifiedTS(Timestamp lastModifiedTS)
	{
		this.lastModifiedTS = lastModifiedTS;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getCreatorID()
	{
		return creatorID;
	}

	public void setCreatorID(int creatorID)
	{
		this.creatorID = creatorID;
	}

	public String getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}

	public String getIssueNum()
	{
		return issueNum;
	}

	public void setIssueNum(String issueNum)
	{
		this.issueNum = issueNum;
	}

	public String getAssignee()
	{
		return assignee;
	}

	public void setAssignee(String assignee)
	{
		this.assignee = assignee;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}
}
