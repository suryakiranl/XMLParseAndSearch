package edu.cmu.sv.surya.dao;

import edu.cmu.sv.surya.dto.Literature;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by suryakiran on 3/10/14.
 */
public interface DblpDAO extends BaseDAO {
    String LITERATURE_TABLE = " LITERATURE ";
    String LITERATURE_AUTHORS_TABLE = " LIT_AUTHORS ";
    String CREATE_LITERATURE_TABLE_STMT = "create table " + LITERATURE_TABLE + " (id BIGINT, key VARCHAR(4000), mDate VARCHAR(20), title VARCHAR(4000), year VARCHAR(4), url VARCHAR(4000))";
    String INSERT_INTO_LITERATURE_STMT = "insert into " + LITERATURE_TABLE + " (id, key, mDate, title, year, url) values (?, ?, ?, ?, ?, ?)";
    String CREATE_LITERATURE_AUTHORS_TABLE_STMT = "create table " + LITERATURE_AUTHORS_TABLE + " (id BIGINT, lit_id BIGINT, name VARCHAR(4000))";
    String INSERT_INTO_LITERATURE_AUTHORS_STMT = "insert into " + LITERATURE_AUTHORS_TABLE + " (id, lit_id, name) values (?, ?, ?)";
    String SELECT_LITERATURE_INFO_BY_AUTHOR_NAME = " SELECT l.id, l.key, l.title, l.year, l.url, la2.name " +
            " FROM " + LITERATURE_TABLE + " l, " + LITERATURE_AUTHORS_TABLE + " la1, " + LITERATURE_AUTHORS_TABLE + " la2 " +
            " WHERE l.id = la1.lit_id AND l.id = la2.lit_id " +
            " AND la1.name like ?  AND la2.id != la1.id ";

    void saveAll(List<Literature> litList);
    void createTables() throws SQLException, ClassNotFoundException;
    long countRowsInTable() throws SQLException, ClassNotFoundException;
    List<String[]> findLiteratureByAuthor(String authorName);
}
