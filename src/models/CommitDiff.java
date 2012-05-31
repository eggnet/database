package models;

import java.util.ArrayList;
import java.util.List;

public class CommitDiff{
	private String new_commit_id;
	private String old_commit_id;
	private List<FileDiff> fileDiffs;
	
	public CommitDiff()
	{
		fileDiffs = new ArrayList<FileDiff>();
	}
	public CommitDiff(String new_commit_id, String old_commit_id,
			List<FileDiff> fileDiffs) {
		super();
		this.new_commit_id = new_commit_id;
		this.old_commit_id = old_commit_id;
		this.fileDiffs = fileDiffs;
	}

	public String getNew_commit_id() {
		return new_commit_id;
	}

	public void setNew_commit_id(String new_commit_id) {
		this.new_commit_id = new_commit_id;
	}

	public String getOld_commit_id() {
		return old_commit_id;
	}

	public void setOld_commit_id(String old_commit_id) {
		this.old_commit_id = old_commit_id;
	}

	public List<FileDiff> getFileDiffs() {
		return fileDiffs;
	}

	public void setFileDiffs(List<FileDiff> fileDiffs) {
		this.fileDiffs = fileDiffs;
	}
	
	public void addFileDiff(FileDiff filediff)
	{
		if(fileDiffs != null)
			fileDiffs.add(filediff);
	}
}

