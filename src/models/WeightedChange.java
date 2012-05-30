package models;

public class WeightedChange extends Change
{
	private float	Weight;

	public WeightedChange(Change other, float Weight)
	{
		CommitId = other.getCommitId();
		OwnerId = other.getOwnerId();
		FileId = other.getFileId();
		CharStart = other.getCharStart();
		CharEnd = other.getCharEnd();
		ChangeType = other.getChangeType();
		this.Weight = Weight;
	}

	public float getWeight()
	{
		return Weight;
	}

	public void setWeight(float Weight)
	{
		this.Weight = Weight;
	}
}