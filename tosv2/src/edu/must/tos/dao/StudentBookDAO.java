package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.BookRel;

public interface StudentBookDAO {

	public List searchAvailableBookByIsbn(Connection conn, BookRel tbr,
			String studParam) throws Exception;

	public List searchAvailableBook(Connection conn, String studentNo,
			String orderIntake, String facultyCode, int year);// inquiry all the book that a student can order

	public List searchOrderedBook(Connection con, String studentNo,
			String orderIntake, String paidStatus, int searchOrderedBook);

	public List searchOrderedBookByRetailNo(Connection conn, int orderSeqNo,
			String curIntake) throws Exception;
}
