package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookSupplier;

public interface BookSupplierDAO {
	
	public List getBookSuppliersList(Connection conn, BookSupplier supplier) throws Exception;

	public List getBookSuppliersList(Connection conn, BookSupplier supplier,
			int start, int num) throws Exception;

	public boolean addBookSupplier(Connection conn, BookSupplier supplier)
			throws Exception;

	public BookSupplier getBookSupplierByNo(Connection conn, int no)
			throws Exception;

	public boolean updateBookSupplier(Connection conn, BookSupplier supplier)
			throws Exception;

	public boolean updateBookSupplierActN(Connection conn, int id)
			throws Exception;
}
