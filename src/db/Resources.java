package db;

import java.text.SimpleDateFormat;

public abstract class Resources {
	public static final String dbUser = "bradens";
	public static final String dbPassword = "bradens";
	public static final String dbUrl = "jdbc:postgresql://localhost:10000/";
	public static final String EGGNET_DB_NAME = "eggnet";
	public static final SimpleDateFormat DBDateFormat = new SimpleDateFormat("yyyy-mm-dd kk:mm:ss Z");
	
	public enum ChangeType {
		MODIFYINSERT, MODIFYDELETE, DELETE, ADD, MODIFY, MOVE, RENAME
	}
	
	public static int convertLineEndToCharEnd(int lineEnd, String file) {
		int index = -1;
		int lineCount = 1;
		
		for(char c: file.toCharArray()) {
			if(c == '\n' && lineCount == lineEnd) {
				index++;
				break;
			}
			if(c == '\n')
				lineCount++;
			index++;
		}
		
		return index;
	}
	
	public static int convertCharToLine(int charNum, String file) {
		int index = -1;
		int lineCount = 1;
		
		for(char c: file.toCharArray()) {
			index++;
			if(index == charNum) 
				break;
			if(c == '\n')
				lineCount++;
		}
		
		return lineCount;
	}
	
	public static int convertLineStartToCharStart(int lineStart, String file) {
		int index = -1;
		int lineCount = 1;
		if (lineStart == 1) return 0;
		for(char c: file.toCharArray()) {
			if(lineCount == lineStart) {
				index++;
				break;
			}
			else if(c == '\n')
				lineCount++;
			index++;
		}
		
		return index;
	}
}