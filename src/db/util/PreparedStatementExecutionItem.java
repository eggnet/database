package db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.util.ISetter;

public class PreparedStatementExecutionItem extends AExecutionItem {		
	private String query = null;
	private ISetter[] params = null;
	private ResultSet resultSet = null;
	private boolean wasExecuted = false;
	
	public PreparedStatementExecutionItem(String query, ISetter[] params) {
		this.query = query;
		this.params = params;
	}
	
	public void execute(Connection conn) {
		try {
			PreparedStatement s = conn.prepareStatement(query);
			if (params != null) {
				for (ISetter setter : params) {
					s = setter.set(s);
				}
			}
			if (query.toLowerCase().startsWith("select")) {
				resultSet = s.executeQuery();
			} else {
				s.execute();
			}
		}
		catch (SQLException e) {
			PreparedStatement s = null;
			try {
				s = conn.prepareCall(query);
				if (params != null) {
					for (ISetter setter : params) {
						s = setter.set(s);
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.err.println(s.toString());
			e.printStackTrace();
		}
		wasExecuted = true;
	}
	
	public boolean wasExecuted() {
		return wasExecuted;
	}
	
	public ResultSet getResult() {
		return this.resultSet;
	}
}
