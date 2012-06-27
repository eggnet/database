package models.jira;

import java.util.List;

public class JiraIssueQuery
{
	public JiraIssueQuery() { }
	
	private String expand;
	private int maxResults;
	private int startAt;
	private int total;
	private JiraIssue[] issues;
	
	public String getExpand()
	{
		return expand;
	}
	public void setExpand(String expand)
	{
		this.expand = expand;
	}
	public int getMaxResults()
	{
		return maxResults;
	}
	public void setMaxResults(int maxResults)
	{
		this.maxResults = maxResults;
	}
	public int getStartAt()
	{
		return startAt;
	}
	public void setStartAt(int startAt)
	{
		this.startAt = startAt;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	public JiraIssue[] getIssues()
	{
		return issues;
	}
	public void setIssues(JiraIssue[] issues)
	{
		this.issues = issues;
	}
}
