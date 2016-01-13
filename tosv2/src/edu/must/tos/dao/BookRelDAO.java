package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookRel;

public interface BookRelDAO {

	public int saveBookRelInDB(Connection conn, List list) throws Exception;

	public List getBookRelList(Connection conn, BookRel bookRel) throws Exception;
}
