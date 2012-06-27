package models;

public class Link
{
	private int 	ItemID;
	private String	CommitID;
	private float	Confidence;
	
	public Link(int itemID, String commitID, float confidence)
	{
		super();
		ItemID = itemID;
		CommitID = commitID;
		Confidence = confidence;
	}

	public Link()
	{
		super();
	}

	public int getItemID()
	{
		return ItemID;
	}

	public void setItemID(int itemID)
	{
		ItemID = itemID;
	}

	public String getCommitID()
	{
		return CommitID;
	}

	public void setCommitID(String commitID)
	{
		CommitID = commitID;
	}

	public float getConfidence()
	{
		return Confidence;
	}

	public void setConfidence(float confidence)
	{
		Confidence = confidence;
	}
}
