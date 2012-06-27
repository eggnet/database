package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

import models.jira.JiraComment;
import models.jira.JiraIssue;

import db.Resources;
import db.Resources.CommType;

/**
 * Stores the record of an Item of communication
 * {@link Thread}
 * {@link Email}
 * {@link Commit}
 * {@link IM}
 * {@link IRC}
 * etc.
 * @author braden
 */
public class Item
{
	private int PID;
	private String person;
	private Timestamp ItemDate;
	private int ItemID;
	private String Body;
	private String Title;
	private CommType CommunicationType;

	public Item(JiraIssue issue) throws ParseException
	{
		Resources.JiraDateFormat.setLenient(false);
		person = issue.getFields().getReporter().getEmailAddress();
		java.util.Date d = Resources.JiraDateFormat.parse(issue.getFields().getCreated().replace("T", " "));
		ItemDate = new Timestamp(d.getTime());
		Body = issue.getFields().getDescription();
		Title = issue.getFields().getSummary();
		CommunicationType = Resources.CommType.ISSUE;
	}
	
	public Item(JiraComment comment) throws ParseException
	{
		person = comment.getAuthor().getEmailAddress();
		java.util.Date createdDate = Resources.JiraDateFormat.parse(comment.getCreated().replace("T", " "));
		ItemDate = new Timestamp(createdDate.getTime());
		Body = comment.getBody();
		Title = null;
		CommunicationType = CommunicationType.JIRA;
	}
	
	/**
	 * @param pId
	 * @param timestamp
	 * @param itemId
	 * @param body
	 * @param title
	 * @param communicationType
	 */
	public Item(int pId, Timestamp timestamp, int itemId, String body, String title, CommType communicationType)
	{
		super();
		PID = pId;
		ItemDate = timestamp;
		ItemID = itemId;
		Body = body;
		Title = title;
		CommunicationType = communicationType;
	}
	
	public Item(String person, Timestamp itemDate, int itemID, String body,
			String title, CommType communicationType)
	{
		super();
		this.person = person;
		ItemDate = itemDate;
		ItemID = itemID;
		Body = body;
		Title = title;
		CommunicationType = communicationType;
	}

	public Item() { }

	public int getPId()
	{
		return PID;
	}
	public void setPId(int pId)
	{
		PID = pId;
	}
	public Timestamp getItemDate()
	{
		return ItemDate;
	}
	public void setItemDate(Timestamp timestamp)
	{
		ItemDate = timestamp;
	}
	public int getItemId()
	{
		return ItemID;
	}
	public void setItemId(int itemId)
	{
		ItemID = itemId;
	}
	public String getBody()
	{
		return Body;
	}
	public void setBody(String body)
	{
		Body = body;
	}
	public String getTitle()
	{
		return Title;
	}
	public void setTitle(String title)
	{
		Title = title;
	}
	public CommType getCommunicationType()
	{
		return CommunicationType;
	}
	public void setCommunicationType(CommType communicationType)
	{
		CommunicationType = communicationType;
	}

	public String getPerson()
	{
		return person;
	}

	public void setPerson(String person)
	{
		this.person = person;
	}
	
	
}
