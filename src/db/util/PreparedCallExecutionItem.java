package db.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedCallExecutionItem extends AExecutionItem {
	private String query;
	private ISetter[] params = null;
	private boolean wasExecuted = false;
	private List<PreparedCallExecutionItem> executionItems = new ArrayList<PreparedCallExecutionItem>();

	public PreparedCallExecutionItem(String query, ISetter[] params) {
		this.query = query;
		this.params = params;
	}
	
	@Override
	public void execute(Connection conn) {
		try {
			CallableStatement s = conn.prepareCall(query);
			if (params != null) {
				for (ISetter setter : params) {
					s = setter.set(s);
				}
			}
			for (PreparedCallExecutionItem ei : executionItems)
				s = ei.addToBatch(s);
			if (executionItems.isEmpty())
				s.execute();
			else
				s.executeBatch();
			s.close();
		}
		catch (SQLException e) {
			System.err.println("===> Batch start");
			this.print();
			for (PreparedCallExecutionItem ei : this.executionItems)
				ei.print();
			System.err.println("===> Batch end");
			e.printStackTrace();
		}
		for (PreparedCallExecutionItem ei : executionItems)
			ei.wasExecuted = true;
		wasExecuted = true;
	}

	@Override
	public boolean wasExecuted() {
		return wasExecuted;
	}

	@Override
	public boolean combine(AExecutionItem itemToAdd) {
		if (itemToAdd != null && itemToAdd.getClass() == PreparedCallExecutionItem.class) {
			PreparedCallExecutionItem otherItem = (PreparedCallExecutionItem) itemToAdd;
			if (otherItem.query.toLowerCase().equals(query.toLowerCase())) {
				executionItems.add((PreparedCallExecutionItem) itemToAdd);
				return true;
			}
		}		
		return false;
	}	
	
	@Override
	public void print() {
		System.err.println(this.query);
		for (ISetter s : this.params) s.print();
	}
	
	private CallableStatement addToBatch(CallableStatement statement) throws SQLException {
		statement.addBatch();
		if (params != null) {
			for (ISetter s : params) {
				s.set(statement);
			}
		}
		return statement;
	}
}
