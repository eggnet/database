package models;

public class CommitFamily {
	private String parentId;
	private String childId;
	
	public CommitFamily(String parentId, String childId) {
		super();
		this.parentId = parentId;
		this.childId = childId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	
	
}
