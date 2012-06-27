package models.jira;


public class JiraIssue
{
	private int id;
	private String key;
	private String self;
	private String expand;
	private JiraIssueField fields;

	public JiraIssue() { }

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getSelf()
	{
		return self;
	}

	public void setSelf(String self)
	{
		this.self = self;
	}

	public String getExpand()
	{
		return expand;
	}

	public void setExpand(String expand)
	{
		this.expand = expand;
	}

	public JiraIssueField getFields()
	{
		return fields;
	}

	public void setFields(JiraIssueField fields)
	{
		this.fields = fields;
	}
}
