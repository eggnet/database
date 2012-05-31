package models;

import java.util.ArrayList;
import java.util.List;

import models.DiffEntry.diff_types;

public class FileDiff {
	private String file_id;
	private List<DiffEntry> diffEntries;
	
	public FileDiff()
	{
		diffEntries = new ArrayList<DiffEntry>();
	}
	
	public FileDiff(String file_id, List<DiffEntry> diffEntries) {
		super();
		this.file_id = file_id;
		this.diffEntries = diffEntries;
	}
	
	public void addDiffEntry(DiffEntry entry)
	{
		if(diffEntries != null)
			diffEntries.add(entry);
	}
	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public List<DiffEntry> getDiffEntries() {
		return diffEntries;
	}

	public void setDiffEntries(List<DiffEntry> diffEntries) {
		this.diffEntries = diffEntries;
	}
	
	public boolean isAddCommit()
	{
		for(DiffEntry de : diffEntries)
			if(de.getDiff_type() == diff_types.DIFF_ADD)
				return true;
		return false;
	}
	
	public boolean isDeleteCommit()
	{
		for(DiffEntry de : diffEntries)
			if(de.getDiff_type() == diff_types.DIFF_DELETE)
				return true;
		return false;
	}
}
