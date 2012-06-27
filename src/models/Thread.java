package models;

public class Thread
{
	private int ItemID;
	private int ThreadID;
	
	public Thread()
	{
		super();
	}

	public Thread(int itemID, int threadID)
	{
		super();
		ItemID = itemID;
		ThreadID = threadID;
	}

	public int getItemID()
	{
		return ItemID;
	}

	public void setItemID(int itemID)
	{
		ItemID = itemID;
	}

	public int getThreadID()
	{
		return ThreadID;
	}

	public void setThreadID(int threadID)
	{
		ThreadID = threadID;
	}
}
