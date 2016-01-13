package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookPurchasingCond;

public interface BookPurchasingCondDAO {
	
	public List getPurchasingCond(Connection conn, String intake, String BfromDate, String BtoDate, String supplierNo) throws Exception;

	public boolean addBookPurchasingCond(Connection conn, BookPurchasingCond cond) throws Exception;
}
