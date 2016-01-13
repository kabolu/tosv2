package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.Course;

public interface BookDAO {

	/**
	 * @return
	 * @throws SQLException
	 */
	public List getIsbn(Connection conn) throws Exception;

	/**
	 * @param book
	 * @return
	 * @throws Exception
	 */
	public int saveInDB(Connection conn, Book book) throws Exception;

	/**
	 * @param conn
	 * @param isbn
	 * @return
	 */
	public Book getBookByPK(Connection conn, String isbn) throws Exception;

	/**
	 * @param conn
	 * @param book
	 * @param course
	 * @return
	 * @throws Exception
	 */
	public List queryBook(Connection conn, Book book, Course course, int start,	int num) throws Exception;
}
