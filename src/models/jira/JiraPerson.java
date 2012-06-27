package models.jira;

public class JiraPerson
{
	private String active;
	private String displayName;
	private String emailAddress;
	private String name;
	private String self;
	
	public JiraPerson() { }
	
	public String getActive()
	{
		return active;
	}
	public void setActive(String active)
	{
		this.active = active;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getEmailAddress()
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
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
