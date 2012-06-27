package models.jira;

public class JiraComment
{
	private JiraPerson author;
	private String body;
	private String created;
	private String id;
	private String self;
	private String updated;
	
	public JiraComment() { }
	
	public JiraPerson getAuthor()
	{
		return author;
	}
	public void setAuthor(JiraPerson author)
	{
		this.author = author;
	}
	public String getBody()
	{
		return body;
	}
	public void setBody(String body)
	{
		this.body = body;
	}
	public String getCreated()
	{
		return created;
	}
	public void setCreated(String created)
	{
		this.created = created;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getSelf()
	{
		return self;
	}
	public void setSelf(String self)
	{
		this.self = self;
	}
	public String getUpdated()
	{
		return updated;
	}
	public void setUpdated(String updated)
	{
		this.updated = updated;
	}
	
}
