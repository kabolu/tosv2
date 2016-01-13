package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookStockOut;

public interface BookStockOutDAO {
	
	public List getBookStockOurReport(Connection conn, String intake, String beginDate, String toDate, String prnum, String supplier) throws Exception;

	public BookStockOut getBookStockOut(Connection conn, BookStockOut b) throws Exception;

	public boolean insertBookStockOutList(Connection conn, List<BookStockOut> list) throws Exception;

	public List getStockOutList(Connection conn, BookStockOut bookStockOut) throws Exception;

	public List getStockOutInfoList(Connection conn, String intake, String prNum,
			String fromDate, String toDate, String paidStatus, int supplierNo)
			throws Exception;
}
