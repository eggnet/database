package db.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import db.util.ISetter;

public class PreparedCallExecutionItem extends AExecutionItem {
	private String query;
	private List<ISetter[]> params = new ArrayList<ISetter[]>();
	private boolean wasExecuted = false;

	public PreparedCallExecutionItem(String query, ISetter[] params) {
		this.query = query;
		this.params.add(params);
	}
	
	@Override
	public void execute(Connection conn) {
		try {
			CallableStatement s = conn.prepareCall(query);
			Iterator<ISetter[]> it = this.params.iterator();
			while (it.hasNext()) {
				ISetter[] params = it.next();
				for (ISetter setter : params) s = setter.set(s);
				if (it.hasNext()) s.addBatch();
			}
			s.execute();
		}
		catch (SQLException e) {
			CallableStatement s = null;
			try {
				for (ISetter[] params : this.params) {
					s = conn.prepareCall(query);
					if (params != null) {
						for (ISetter setter : params) {
							s = setter.set(s);
						}
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

	@Override
	public boolean combine(AExecutionItem itemToAdd) {
		if (itemToAdd != null && itemToAdd.getClass() == PreparedCallExecutionItem.class) {
			PreparedCallExecutionItem otherItem = (PreparedCallExecutionItem) itemToAdd;
			if (otherItem.query.matches(this.query)) {
				this.params.addAll(otherItem.params);
				return true;
			}
		}		
		return false;
	}
}
