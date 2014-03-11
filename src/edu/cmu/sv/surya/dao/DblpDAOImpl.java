package edu.cmu.sv.surya.dao;

import edu.cmu.sv.surya.dto.Literature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suryakiran on 3/10/14.
 */
public class DblpDAOImpl extends BaseDAOImpl implements DblpDAO {
    private static long literatureTableId = 0L;
    private static long literatureAuthorsTableId = 0L;

    @Override
    public void saveAll(List<Literature> litList) {
        try {
            Connection conn = getConnection();

            PreparedStatement litIns = conn.prepareStatement(INSERT_INTO_LITERATURE_STMT);
            PreparedStatement litAuthorsIns = conn.prepareStatement(INSERT_INTO_LITERATURE_AUTHORS_STMT);

            long literatureId = 0L;

            for(Literature lit: litList) {
                literatureId = getLiteratureTableId();

                litIns.setLong(1, literatureId);
                litIns.setString(2, lit.getKey());
                litIns.setString(3, lit.getmDate());
                litIns.setString(4, lit.getTitle());
                litIns.setString(5, lit.getYear());
                litIns.setString(6, lit.getUrl());

                // Save both authors and editors as authors in the table
                lit.getAuthors().addAll(lit.getEditors());
                for(String name: lit.getAuthors()) {
                    litAuthorsIns.setLong(1, getLiteratureAuthorsTableId());
                    litAuthorsIns.setLong(2, literatureId);
                    litAuthorsIns.setString(3, name);
                }

                litIns.addBatch();
            }

            int[] countLitRows = litIns.executeBatch();
            int[] countLitAuthRows = litAuthorsIns.executeBatch();

            conn.commit();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error when saving Literature info to DB: " + e.getMessage());
        }
    }

    private synchronized long getLiteratureTableId() {
        literatureTableId += 1;
        return literatureTableId;
    }

    private synchronized long getLiteratureAuthorsTableId() {
        literatureAuthorsTableId += 1;
        return literatureAuthorsTableId;
    }

    @Override
    public void createTables() throws SQLException, ClassNotFoundException {
        executeUpdate(CREATE_LITERATURE_TABLE_STMT);
        executeUpdate(CREATE_LITERATURE_AUTHORS_TABLE_STMT);
    }

    @Override
    public long countRowsInTable() throws SQLException, ClassNotFoundException {
        return countRowsInTable(LITERATURE_TABLE);
    }

    @Override
    public List<String[]> findLiteratureByAuthor(String authorName) {
        List<String[]> litList = new ArrayList<String[]>();

        try {
            Connection conn = getConnection();

            PreparedStatement ps = conn.prepareStatement(SELECT_LITERATURE_INFO_BY_AUTHOR_NAME);
            ps.setString(1, "%" + authorName.trim() + "%");

            ResultSet rs = ps.executeQuery();

            while(rs !=null && rs.next()) {
                List<String> result = new ArrayList<String>();
                result.add(rs.getLong(1) + "");
                result.add(rs.getString(2));
                result.add(rs.getString(3));
                result.add(rs.getString(4));
                result.add(rs.getString(5));
                result.add(rs.getString(6));

                litList.add((String[])result.toArray());
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error when saving Literature info to DB: " + e.getMessage());
        }


        return litList;
    }
}
