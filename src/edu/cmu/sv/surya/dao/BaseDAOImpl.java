package edu.cmu.sv.surya.dao;

import edu.cmu.sv.surya.dao.util.DBUtils;

import java.sql.*;

public class BaseDAOImpl implements BaseDAO {
	private Connection conn;

	public Connection getConnection() throws ClassNotFoundException,
			SQLException {
		if(conn == null || conn.isClosed()) {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:" + DBUtils.getDBFilePath());
		}

		return conn;
	}

	public void commit() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.commit();
		}
	}

	public void rollback() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.rollback();
		}
	}

	public int executeUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
		conn = getConnection();
		
		Statement stmt = conn.createStatement();
		int status = stmt.executeUpdate(sqlStmt);

		return status;
	}
	
	public long countRowsInTable(String tableName) throws SQLException, ClassNotFoundException {
		String sqlStmt = "select COUNT(1) from " + tableName;
		long rowCount = 0;
		
		conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sqlStmt);
		while(rs.next()) {
			rowCount = rs.getLong(1);
		}
		
		return rowCount;
	}
}
