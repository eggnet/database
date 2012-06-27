package models.jira;


public class JiraIssueField
{
	private JiraIssueStatus status;
	private JiraPerson assignee;
	private String created;
	private String updated;
	private String summary;
	private String description;
	private String[] labels;
	private JiraIssueLink[] issuelinks;
	private JiraPerson reporter;
	private JiraCommentBlock comment;
	
	public JiraIssueStatus getStatus()
	{
		return status;
	}
	public void setStatus(JiraIssueStatus status)
	{
		this.status = status;
	}
	public JiraPerson getAssignee()
	{
		return assignee;
	}
	public void setAssignee(JiraPerson assignee)
	{
		this.assignee = assignee;
	}
	public String getCreated()
	{
		return created;
	}
	public void setCreated(String created)
	{
		this.created = created;
	}
	public String getUpdated()
	{
		return updated;
	}
	public void setUpdated(String updated)
	{
		this.updated = updated;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public JiraPerson getReporter()
	{
		return reporter;
	}
	public void setReporter(JiraPerson reporter)
	{
		this.reporter = reporter;
	}
	public JiraCommentBlock getComment()
	{
		return comment;
	}
	public void setComment(JiraCommentBlock comment)
	{
		this.comment = comment;
	}
	public String[] getLabels()
	{
		return labels;
	}
	public void setLabels(String[] labels)
	{
		this.labels = labels;
	}
	public JiraIssueLink[] getIssueLinks()
	{
		return issuelinks;
	}
	public void setIssueLinks(JiraIssueLink[] issueLinks)
	{
		this.issuelinks = issueLinks;
	}
}
