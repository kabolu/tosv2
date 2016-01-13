package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookStockInOrder;
import edu.must.tos.bean.BookStockOutOrder;

public interface BookStockOutOrderDAO {

	public boolean addBookStockOutOrder(Connection conn, BookStockOutOrder o)
			throws Exception;

	public List getStockOutListByPrNum(Connection conn, String prnum)
			throws Exception;

	public BookStockOutOrder getStockOutOrderByPrNum(Connection conn,
			String prNum) throws Exception;
}
