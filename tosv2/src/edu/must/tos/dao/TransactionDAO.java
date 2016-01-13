package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Transaction;

public interface TransactionDAO {
	
	public List getOrderPaySummList(Connection conn, String intake) throws Exception;

	public List getTransactionInfo(Connection conn, String fromDate,
			String endDate) throws Exception;

	public boolean addTransactionInfo(Connection conn, Transaction t)
			throws Exception;

	public List getTransactionList(Connection conn, String fromDate, String toDate, String orderSeqNo, String items)
			throws Exception;

	public List getTransactionByItem(Connection conn, List seqNoList, List item, String status)
			throws Exception;
}
