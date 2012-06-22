package db.util;

import java.sql.Connection;

public abstract class AExecutionItem {
	public abstract void execute(Connection conn);
	
	/**
	 * blocking
	 */
	public synchronized void waitUntilExecuted() {
		while (!wasExecuted()) {
			try {
				this.wait(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public abstract boolean wasExecuted();
	
	public abstract boolean combine(AExecutionItem itemToAdd);
}
