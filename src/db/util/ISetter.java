package db.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface ISetter {
	public PreparedStatement set(PreparedStatement ps) throws SQLException;
	public CallableStatement set(CallableStatement ps) throws SQLException;
	
	public class StringSetter implements ISetter {
		private int position;
		private String value;

		public StringSetter(int position, String value) {
			this.position = position;
			this.value = value;
		}
		
		@Override
		public PreparedStatement set(PreparedStatement ps) throws SQLException {
			ps.setString(position, value);
			return ps;
		}

		@Override
		public CallableStatement set(CallableStatement ps) throws SQLException {
			ps.setString(position, value);
			return ps;
		}
	}
	
	public class IntSetter implements ISetter {
		private int value;
		private int position;
		
		public IntSetter(int position, int value) {
			this.value = value;
			this.position = position;
		}
		
		@Override
		public PreparedStatement set(PreparedStatement ps) throws SQLException {
			ps.setInt(position, value);
			return ps;
		}
		
		@Override
		public CallableStatement set(CallableStatement ps) throws SQLException {
			ps.setInt(position, value);
			return ps;
		}
	}
	
	public class TimestampSetter implements ISetter {
		private Timestamp value;
		private int position;

		public TimestampSetter(int position, Timestamp value) {
			this.value = value;
			this.position = position;
		}
		
		@Override
		public PreparedStatement set(PreparedStatement ps) throws SQLException {
			ps.setTimestamp(position, value);
			return ps;
		}
		
		@Override
		public CallableStatement set(CallableStatement ps) throws SQLException {
			ps.setTimestamp(position, value);
			return ps;
		}
	}
}
