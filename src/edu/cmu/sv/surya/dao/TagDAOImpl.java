package edu.cmu.sv.surya.dao;

import edu.cmu.sv.surya.dto.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TagDAOImpl extends BaseDAOImpl implements TagDAO {
	@Override
	public void saveAll(List<Tag> tags) {
		try {
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
			
			for(Tag tag: tags) {
				ps.setString(1, tag.getKey());
				ps.setString(2, tag.getValue());
				ps.addBatch();
			}
			
			int[] count = ps.executeBatch();
			if(count.length != tags.size()) {
				throw new RuntimeException("Error when saving Tags info to DB. Row(s) not created successfully.");
			}

            commit();
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException("Error when saving Tag info to DB: " + e.getMessage());
		}
	}
	
	@Override
	public int createTable() throws SQLException, ClassNotFoundException {
		int status = executeUpdate(CREATE_TABLE_STMT);
        commit();
		
		return status;
	}
	
	@Override
	public long countRowsInTable() throws SQLException, ClassNotFoundException {
		return countRowsInTable(TABLE_NAME);
	}
}
