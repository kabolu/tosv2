package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookStockIn;

public interface BookStockInDAO {

	public BookStockIn getBookStockIn(Connection conn, BookStockIn b) throws Exception;

	public boolean updateBookStockInList(Connection conn, List<BookStockIn> list) throws Exception;

	public List getBookStockInList(Connection conn, BookStockIn bookStockIn) throws Exception;

	public List getBookStockInByIsbn(Connection conn, String intake, String isbn) throws Exception;
	
	public List getBookStockInInfoList(Connection conn, String prNum,
			String fromDate, String toDate, String paidStatus, int supplierNo)
			throws Exception;
}
