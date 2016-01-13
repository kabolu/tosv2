package edu.must.tos.dao;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @author Administrator
 * 
 */
public interface OrdBooksReportDAO {

	public ResultSet getOrdBooks(Connection con, String facultyCode,
			String programCode, String year, String intake, String studType,
			String region, String paidStatus, String fromDate, String toDate);

	public ResultSet getNewsOrdBooksBySeqNo(Connection conn,
			String facultyCode, String programCode, String year, String intake,
			String studType, String region, String paidStatus, String isDiffer,
			String fromDate, String toDate);

	public ResultSet getOrdBooksBySeqNo(Connection conn, String facultyCode,
			String programCode, String year, String intake, String studType,
			String region, String paidStatus, String fromDate, String toDate);
}
