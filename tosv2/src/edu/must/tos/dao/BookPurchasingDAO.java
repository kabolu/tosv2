package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookPurchasing;

public interface BookPurchasingDAO {
	
	public List getOrderNoList(Connection conn, String orderNo) throws Exception;
	
	public List getBookPurchasingBeanByOrderNo(Connection conn, String orderNo) throws Exception;
	
	public List getBookPurchasingByOrderNo(Connection conn, String orderNo, String fromDate, String toDate) throws Exception;
	
	public List getBookPurchasingInfo(Connection conn, String intake, String fromDate, String toDate, String supplierNo) throws Exception;

	public boolean addBookPurchasing(Connection conn, BookPurchasing bean) throws Exception;
	
	public boolean updateBookPurchasing(Connection conn, BookPurchasing bean) throws Exception;
	
	public boolean updateBookPurchasingByOrderNo(Connection conn, BookPurchasing bean) throws Exception;
	
	public boolean updateBookPurchasingBySupplierNo(Connection conn, String userId, String supplierNo, String fromDate, String toDate, String intake) throws Exception;
}
