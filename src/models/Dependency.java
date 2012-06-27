package models;

public class Dependency
{
	private int ItemID;
	private int DependsOnID;
	
	public Dependency() { }
	
	public Dependency(int itemID, int dependsID)
	{
		ItemID = itemID;
		DependsOnID = dependsID;
	}
	public int getItemID()
	{
		return ItemID;
	}
	public void setItemID(int itemID)
	{
		ItemID = itemID;
	}
	public int getDependsOnID()
	{
		return DependsOnID;
	}
	public void setDependsOnID(int dependsOnID)
	{
		DependsOnID = dependsOnID;
	}
}
