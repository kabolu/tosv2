package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookInventory;

public interface BookInventoryDAO {

	public List getBookInventoryList(Connection conn) throws Exception;

	public BookInventory getBookInventoryByPK(Connection conn, String isbn) throws Exception;

	public List getBookInventoryRecord(Connection conn, String intake, String fromDate) throws Exception;

	public boolean updateBookInventory(Connection conn, List<BookInventory> list) throws Exception;
}
