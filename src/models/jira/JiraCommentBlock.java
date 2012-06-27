package models.jira;

import java.util.List;

public class JiraCommentBlock{
	private JiraComment[] comments;
	private int total;
	
	public JiraComment[] getComments()
	{
		return comments;
	}
	public void setComments(JiraComment[] comments)
	{
		this.comments = comments;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
};
