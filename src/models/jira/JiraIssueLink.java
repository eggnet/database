package models.jira;

public class JiraIssueLink
{
	public class JiraInnerIssue {
		private String key;

		public String getKey()
		{
			return key;
		}

		public void setKey(String key)
		{
			this.key = key;
		}
	}
	
	private JiraInnerIssue inwardIssue;
	private JiraInnerIssue outwardIssue;
	
	public JiraInnerIssue getInwardIssue()
	{
		return inwardIssue;
	}
	
	public void setInwardIssue(JiraInnerIssue inwardIssue)
	{
		this.inwardIssue = inwardIssue;
	}
	
	public JiraInnerIssue getOutwardIssue()
	{
		return outwardIssue;
	}
	
	public void setOutwardIssue(JiraInnerIssue outwardIssue)
	{
		this.outwardIssue = outwardIssue;
	}
}
