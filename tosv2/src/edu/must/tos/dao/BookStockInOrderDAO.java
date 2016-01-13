package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookStockInOrder;

public interface BookStockInOrderDAO {

	public boolean addStockInOrder(Connection conn, BookStockInOrder o)throws Exception;

	public List getStockInListByPrNum(Connection conn, String prnum) throws Exception;

	public BookStockInOrder getStockInOrderByPrNum(Connection conn,
			String prNum) throws Exception;
}
