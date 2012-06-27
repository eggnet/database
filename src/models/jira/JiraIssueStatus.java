package models.jira;

public class JiraIssueStatus
{
	private String description;
	private String iconUrl;
	private String id;
	private String name;
	private String self;
	
	public JiraIssueStatus() { }
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getIconUrl()
	{
		return iconUrl;
	}
	public void setIconUrl(String iconUrl)
	{
		this.iconUrl = iconUrl;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getSelf()
	{
		return self;
	}
	public void setSelf(String self)
	{
		this.self = self;
	}
}
