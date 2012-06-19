package db.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import db.util.ISetter;

public class PreparedCallExecutionItem extends AExecutionItem {
	private String query;
	private ISetter[] params;
	private boolean wasExecuted = false;

	PreparedCallExecutionItem(String query, ISetter[] params) {
		this.query = query;
		this.params = params;
	}
	
	@Override
	public void execute(Connection conn) {
		try {
			CallableStatement s = conn.prepareCall(query);
			for (ISetter setter : params) s = setter.set(s);
			s.execute();
		}
		catch (SQLException e) {
			CallableStatement s = null;
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

	@Override
	public boolean wasExecuted() {
		return wasExecuted;
	}
}
