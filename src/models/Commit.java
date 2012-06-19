package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import db.util.PreparedStatementExecutionItem;

public class Commit {
	
	private int id;
	private String commit_id;
	private String author;
	private String author_email;
	private String comment;
	private Timestamp commit_date;

	private String branch_id;
	private PreparedStatementExecutionItem executionItem;
	private boolean wasRetrieved = false;
	
	public Commit() { }

	public Commit(int id, String commit_id, String author, String author_email,
			String comment, Timestamp commit_date, String branch_id)
	{
		this.id = id;
		this.commit_id = commit_id;
		this.author = author;
		this.author_email = author_email;
		this.comment = comment;
		this.commit_date = commit_date;
		this.branch_id = branch_id;
	}

	public Commit(String commit_id, String author, String author_email,
			String comment, Timestamp commit_date, String branch_id) {
		super();
		this.commit_id = commit_id;
		this.author = author;
		this.author_email = author_email;
		this.comment = comment;
		this.commit_date = commit_date;
		this.branch_id = branch_id;
	}

	public Commit(PreparedStatementExecutionItem ei) {
		this.executionItem = ei;
	}
	
	public String getBranch_id() {
		retrieve();
		return branch_id;
	}
	
	public void setBranch_id(String branchId) {
		branch_id = branchId;
	}

	public int getId() {
		retrieve();
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommit_id() {
		retrieve();
		return commit_id;
	}

	public void setCommit_id(String commit_id) {
		this.commit_id = commit_id;
	}

	public String getAuthor() {
		retrieve();
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor_email() {
		retrieve();
		return author_email;
	}

	public void setAuthor_email(String author_email) {
		this.author_email = author_email;
	}

	public String getComment() {
		retrieve();
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getCommit_date() {
		retrieve();
		return commit_date;
	}

	public void setCommit_date(Timestamp commit_date) {
		this.commit_date = commit_date;
	}
	
	private void retrieve() {
		if (!wasRetrieved ) {
			this.executionItem.waitUntilExecuted();
			wasRetrieved = true;
			ResultSet rs = this.executionItem.getResult();
			
			try {
				if (rs.next()) {
					this.commit_id = rs.getString("commit_id");
					this.author = rs.getString("author");
					this.author_email = rs.getString("author_email");
					this.comment = rs.getString("comments");
					this.commit_date = rs.getTimestamp("commit_date");
					this.branch_id = rs.getString("branch_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
