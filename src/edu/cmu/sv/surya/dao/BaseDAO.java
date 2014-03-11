package edu.cmu.sv.surya.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseDAO {
	Connection getConnection() throws ClassNotFoundException, SQLException;
	void commit() throws SQLException;
	void rollback() throws SQLException;
	int executeUpdate(String sqlStmt) throws SQLException, ClassNotFoundException;
	long countRowsInTable(String tableName) throws SQLException, ClassNotFoundException;
}
