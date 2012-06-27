package models;

public class Silent
{
	private int pID;
	private int itemID;
	
	public Silent()
	{
		super();
	}

	public Silent(int pID, int itemID)
	{
		super();
		this.pID = pID;
		this.itemID = itemID;
	}

	public int getpID()
	{
		return pID;
	}

	public void setpID(int pID)
	{
		this.pID = pID;
	}

	public int getItemID()
	{
		return itemID;
	}

	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}
}
