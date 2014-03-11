package edu.cmu.sv.surya.dao;

import edu.cmu.sv.surya.dto.Tag;

import java.sql.SQLException;
import java.util.List;

public interface TagDAO extends BaseDAO {
	String TABLE_NAME = " TAG ";
	String CREATE_TABLE_STMT = "create table " + TABLE_NAME + " (key VARCHAR(4000), value VARCHAR(4000))";
	String INSERT_STMT = "insert into " + TABLE_NAME + " (key, value) values (?, ?)";

	void saveAll(List<Tag> tags);
	int createTable() throws SQLException, ClassNotFoundException;
	long countRowsInTable() throws SQLException, ClassNotFoundException;
}
