package models;

public class User {
	private String UserEmail;
	private String UserName;
	
	public User() { }
	
	public User(String userEmail)
	{
		super();
		UserEmail = userEmail;
	}
	
	public User(String userEmail, String userName)
	{
		super();
		UserEmail = userEmail;
		UserName = userName;
	}

	public String getUserEmail() {
		return UserEmail;
	}
	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
}
