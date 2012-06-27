package models;

public class Attachment
{
	private int	itemID;
	private String title;
	private String body;
	
	public Attachment()
	{
		super();
	}

	public Attachment(int itemID, String title, String body)
	{
		super();
		this.itemID = itemID;
		this.title = title;
		this.body = body;
	}

	public int getItemID()
	{
		return itemID;
	}

	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}
}
