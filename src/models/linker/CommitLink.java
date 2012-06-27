package models.linker;

public class CommitLink
{
	private int ItemID;
	private String CommitID;
	private float Confidence;
	
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
