package models;

public class Person
{
	private int 	PID = -1;
	private String 	name;
	private String	email;
	
	public Person()
	{
		super();
	}

	public Person(int pID, String name, String email)
	{
		super();
		PID = pID;
		this.name = name;
		this.email = email;
	}

	public int getPID()
	{
		return PID;
	}

	public void setPID(int pID)
	{
		PID = pID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	@Override
	public int hashCode()
	{
		return email.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		Person p = (Person) o;
		if(this.email.equals(p.email) && this.name.equals(p.name) && this.PID == p.PID)
			return true;
		else
			return false;
	}
}
