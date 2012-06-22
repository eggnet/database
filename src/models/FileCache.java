package models;

public class FileCache {
	private String file_id;
	private String commit_id;
	private String raw_file;

	public FileCache() { }
	public FileCache(String file_id, String commit_id, String raw_file) {
		super();
		this.file_id = file_id;
		this.commit_id = commit_id;
		this.raw_file = raw_file;
	}

	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public String getCommit_id() {
		return commit_id;
	}

	public void setCommit_id(String commit_id) {
		this.commit_id = commit_id;
	}

	public String getRaw_file() {
		return raw_file;
	}

	public void setRaw_file(String raw_file) {
		this.raw_file = raw_file;
	}
}
