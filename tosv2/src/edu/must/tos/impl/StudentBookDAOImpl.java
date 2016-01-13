package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.BookTempl;
import edu.must.tos.bean.Course;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Price;
import edu.must.tos.dao.StudentBookDAO;
import edu.must.tos.util.*;

public class StudentBookDAOImpl implements StudentBookDAO {

	public List searchOrderedBookByRetailNo(Connection conn, int orderSeqNo,
			String curIntake) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List bookList = new ArrayList();
		List priceList = new ArrayList();
		List qtyList = new ArrayList();
		List orderedBookList = new ArrayList();
		Order order = new Order();

		String sql = null;

		sql = "select "
				+ "tb.isbn, tb.title, tb.author, tb.publisher, tb.publishYear, tb.edition, tb.language, tb.bookType, tb.remarks, "
				+ "mp.futurePrice, rp.futurePrice, "
				+ "mp.withdrawPrice, mp.netPrice, rp.netPrice, "
				+ "od.confirmQty, od.unconfirmQty, od.withdrawQty, tb.withdrawInd, od.notEnoughQty, od.withdrawQty2,"
				+ "o.orderseqno,o.paidcurrency,o.paidstatus,od.orderIntake "
				+ "from tordetail od "
				+ "inner join torder o on o.orderseqno=od.orderseqno and o.orderintake=od.orderintake "
				+ "inner join tbook tb on od.isbn = tb.isbn "
				+ "left outer join tprice mp on tb.isbn = mp.isbn and mp.currency = 'MOP' and mp.actind = 'Y' and mp.intake=od.orderintake "
				+ "left outer join tprice rp on tb.isbn = rp.isbn and rp.currency = 'RMB' and rp.actind = 'Y' and rp.intake=od.orderintake "
				+ "where od.orderIntake='" + curIntake + "' ";
		if (orderSeqNo != 0) {
			sql += "and od.orderseqno=" + orderSeqNo + " ";
		}
		sql += "and od.actInd = 'Y' and tb.actInd = 'Y' and o.actind='Y' ";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIsbn(rs.getString(1));
				book.setTitle(ToolsOfString.NulltoEmpty(rs.getString(2)));
				book.setAuthor(ToolsOfString.NulltoEmpty(rs.getString(3)));
				book.setPublisher(ToolsOfString.NulltoEmpty(rs.getString(4)));
				book.setPublishYear(ToolsOfString.NulltoEmpty(rs.getString(5)));
				book.setEdition(ToolsOfString.NulltoEmpty(rs.getString(6)));
				book.setLanguage(ToolsOfString.NulltoEmpty(rs.getString(7)));
				book.setBookType(ToolsOfString.NulltoEmpty(rs.getString(8)));
				book.setRemarks(ToolsOfString.NulltoEmpty(rs.getString(9)));

				Price tprice = new Price();
				tprice.setMopFuturePrice(rs.getDouble(10));
				tprice.setRmbFuturePrice(rs.getDouble(11));
				tprice.setWithdrawPrice(rs.getDouble(12));
				tprice.setMopNetPrice(rs.getDouble(13));
				tprice.setRmbNetPrice(rs.getDouble(14));

				priceList.add(tprice);

				OrDetail ordetail = new OrDetail();
				ordetail.setOrderIntake(rs.getString("orderIntake"));
				ordetail.setIsbn(rs.getString("isbn"));
				ordetail.setOrderSeqNo(rs.getInt("orderseqno"));
				ordetail.setConfirmQty(rs.getInt(15));
				ordetail.setUnconfirmQty(rs.getInt(16));
				ordetail.setWithdrawQty(rs.getInt(17));

				book.setWithdrawInd(rs.getString(18));
				if (rs.getObject(19) == null) {
					ordetail.setNotEnoughQty(-1);
				} else {
					ordetail.setNotEnoughQty(rs.getInt(19));
				}
				ordetail.setWithdrawQty2(rs.getInt(20));

				order.setPaidCurrency(rs.getString("paidcurrency"));
				order.setPaidStatus(rs.getString("paidstatus"));

				qtyList.add(ordetail);
				bookList.add(book);
			}
			orderedBookList.add(bookList);
			orderedBookList.add(priceList);
			orderedBookList.add(qtyList);
			orderedBookList.add(order);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orderedBookList;
	}

	public List searchAvailableBookByIsbn(Connection conn, BookRel tbr,
			String studParam) throws Exception {
		List list = new ArrayList();
		List bookList = new ArrayList();
		List priceList = new ArrayList();
		List courseList = new ArrayList();
		List bookTemplList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String studNo = "";
			String appNo = "";
			if (studParam != null) {
				String[] no = studParam.split(",");
				studNo = no[0];
				appNo = no[1];
			}

			sql = "select tb.isbn, tb.title, tb.author, tb.publisher, tb.publishyear, tb.edition, "
					+ "tb.language, tb.booktype, tb.remarks, "
					+ "tb.supplement, "
					+ "t1.futureprice as mopfutureprice, t2.futureprice as rmbfutureprice, "
					+ "t1.netprice as mopnetprice, t2.netprice as rmbnetprice, "
					+ "t1.withdrawprice, tbr.coursecode, tbr.majorcode " 
				    + "from tbook tb "
					+ "inner join tbookrel tbr on tbr.isbn = tb.isbn "
					+ "inner join tprice t1 on t1.currency='MOP' and t1.isbn=tb.isbn and t1.intake=tbr.intake "
					+ "inner join tprice t2 on t2.currency='RMB' and t2.isbn=tb.isbn and t2.intake=tbr.intake "
					+ "where tbr.intake=? and tb.isbn like ? "
					+ "and tb.actind=? and tbr.actind=? and t1.actind=? and t2.actind=? "
					+ " ";
			sql += "and tb.isbn not in (select tordetail.isbn from tordetail "
					+ "inner join torder on torder.orderseqno=tordetail.orderseqno and torder.orderintake=tordetail.orderintake "
					+ "where (tordetail.studentno=? or tordetail.studentno=?) "
					+ "and torder.actind=? and tordetail.actind='Y' and torder.paidstatus='N' and torder.paidcurrency is null )";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tbr.getIntake());
			pstmt.setString(2, '%' + tbr.getIsbn() + '%');
			pstmt.setString(3, tbr.getActInd());
			pstmt.setString(4, tbr.getActInd());
			pstmt.setString(5, tbr.getActInd());
			pstmt.setString(6, tbr.getActInd());
			pstmt.setString(7, studNo);
			pstmt.setString(8, appNo);
			pstmt.setString(9, tbr.getActInd());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIsbn(rs.getString(1));
				book.setTitle(ToolsOfString.NulltoEmpty(rs.getString(2)));
				book.setAuthor(ToolsOfString.NulltoEmpty(rs.getString(3)));
				book.setPublisher(ToolsOfString.NulltoEmpty(rs.getString(4)));
				book.setPublishYear(ToolsOfString.NulltoEmpty(rs.getString(5)));
				book.setEdition(rs.getString(6));
				book.setLanguage(rs.getString(7));
				book.setBookType(rs.getString(8));
				book.setRemarks(ToolsOfString.NulltoEmpty(rs.getString(9)));
				
				book.setSupplement(ToolsOfString.NulltoEmpty(rs.getString(10)));

				bookList.add(book);

				Price tprice = new Price();
				tprice.setMopFuturePrice(rs.getDouble(11));
				tprice.setRmbFuturePrice(rs.getDouble(12));
				tprice.setMopNetPrice(rs.getDouble(13));
				tprice.setRmbNetPrice(rs.getDouble(14));
				tprice.setWithdrawPrice(rs.getDouble(15));

				priceList.add(tprice);

				Course course = new Course();
				course.setCourseCode(rs.getString(16));
				courseList.add(course);

				BookTempl tBookTempl = new BookTempl();
				tBookTempl.setYear(0);
				tBookTempl.setMajorCode(rs.getString("majorcode"));
				tBookTempl.setFacultyCode("");
				bookTemplList.add(tBookTempl);
			}
			list.add(bookList);
			list.add(priceList);
			list.add(courseList);
			list.add(bookTemplList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return list;
	}

	/**
	 * 
	 */
	public List searchAvailableBook(Connection conn, String studentNo,
			String orderIntake, String facultyCode, int year) {
		// inquiry the books that a student can order exclude the books he has ordered
		List bookList = new ArrayList();
		List priceList = new ArrayList();
		List courseList = new ArrayList();
		List bookTemplList = new ArrayList();
		List availableStudentBookList = new ArrayList();

		String sql = null;
		ResultSet rs = null;

		if (studentNo != null && !studentNo.equals("") && orderIntake != null && !orderIntake.equals("")) {
			sql = "select ISBN, BOOKTITLE, AUTHOR, PUBLISHER, PUBLISHYEAR, EDITION, LANGUAGE, BOOKTYPE, REMARKS, " 
				+ "MOP_PRICE, MOP_NETPRICE, COURSECODE "
				+ "FROM v_book_list_search where studentno = '" +studentNo+ "'";
			sql += "AND ISBN NOT IN ( " +
					"SELECT TORDETAIL.ISBN FROM TORDETAIL " +
					"INNER JOIN TORDER ON TORDER.ORDERSEQNO=TORDETAIL.ORDERSEQNO AND TORDER.ORDERINTAKE=TORDETAIL.ORDERINTAKE " +
					"INNER JOIN STUDENTENROL ON STUDENTENROL.STUDENTNO=TORDETAIL.STUDENTNO AND STUDENTENROL.COURSEINTAKE=TORDETAIL.ORDERINTAKE " +
					"WHERE TORDER.ACTIND='Y' AND TORDETAIL.ACTIND='Y' AND TORDER.PAIDSTATUS='N' AND TORDER.PAIDCURRENCY IS NULL " +
					"AND STUDENTENROL.STUDENTNO = '"+ studentNo +"') ";
			/*
			sql = "SELECT TBOOKREL.ISBN, TBOOK.TITLE, TBOOK.AUTHOR, TBOOK.PUBLISHER, TBOOK.PUBLISHYEAR, "
					+ "TBOOK.EDITION, TBOOK.LANGUAGE, INFO.BOOKTYPE, TBOOK.REMARKS, "
					+ "T1.FUTUREPRICE AS MOPFUTUREPRICE, T2.FUTUREPRICE AS RMBFUTUREPRICE, "
					+ "T1.NETPRICE AS MOPNETPRICE, T2.NETPRICE AS RMBNETPRICE, "
					+ "T1.WITHDRAWPRICE, STUDENTENROL.COURSECODE, TCOURSE.FACULTYCODE "
					+ "FROM STUDENTENROL "
					+ "INNER JOIN TBOOKREL ON STUDENTENROL.COURSECODE = TBOOKREL.COURSECODE AND STUDENTENROL.MAJORCODE = TBOOKREL.MAJORCODE "
					+ "INNER JOIN TCOURSE ON TCOURSE.COURSECODE = STUDENTENROL.COURSECODE "
					+ "INNER JOIN TBOOK ON TBOOKREL.ISBN = TBOOK.ISBN "
					+ "INNER JOIN TPRICE T1 ON TBOOKREL.ISBN = T1.ISBN "
					+ "INNER JOIN TPRICE T2 ON TBOOKREL.ISBN = T2.ISBN "
					+ "LEFT OUTER JOIN TBOOKRELINFO INFO ON INFO.INTAKE = TBOOKREL.INTAKE AND INFO.ISBN = TBOOKREL.ISBN AND INFO.COURSECODE = TBOOKREL.COURSECODE AND INFO.MAJORCODE = TBOOKREL.MAJORCODE "
					+ "WHERE STUDENTENROL.COURSEINTAKE = TBOOKREL.INTAKE AND STUDENTENROL.STUDENTNO = '" + studentNo + "' "
					+ "AND TBOOKREL.INTAKE = '" + orderIntake + "' "
					+ "AND TBOOK.ACTIND = 'Y' AND INFO.BOOKTYPE <> 'TB' AND TBOOKREL.ACTIND = 'Y' "
					+ "AND T1.INTAKE = TBOOKREL.INTAKE AND T1.ACTIND = 'Y' "
					+ "AND T2.INTAKE = TBOOKREL.INTAKE AND T2.ACTIND = 'Y' "
					+ "AND T1.CURRENCY = 'MOP' AND T2.CURRENCY = 'RMB' " ;
			/*
			if (!facultyCode.equals("")) {
				sql += " AND TCOURSE.FACULTYCODE = '" + facultyCode + "' ";
			}
			*/
			/*
			sql += "AND TBOOKREL.ISBN NOT IN (SELECT TORDETAIL.ISBN FROM TORDETAIL "
					+ "INNER JOIN TORDER ON TORDER.ORDERSEQNO=TORDETAIL.ORDERSEQNO AND TORDER.ORDERINTAKE=TORDETAIL.ORDERINTAKE "
					+ "INNER JOIN STUDENTENROL ON STUDENTENROL.STUDENTNO=TORDETAIL.STUDENTNO AND STUDENTENROL.COURSEINTAKE=TORDETAIL.ORDERINTAKE "
					+ "WHERE TORDER.ACTIND='Y' AND TORDETAIL.ACTIND='Y' AND TORDER.PAIDSTATUS='N' AND TORDER.PAIDCURRENCY IS NULL "
					+ "AND STUDENTENROL.STUDENTNO = '"+ studentNo +"' ) ";
			*/
		}
		try {
			PreparedStatement ps;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIsbn(rs.getString("ISBN"));
				book.setTitle(ToolsOfString.NulltoEmpty(rs.getString("BOOKTITLE")));
				book.setAuthor(ToolsOfString.NulltoEmpty(rs.getString("AUTHOR")));
				book.setPublisher(ToolsOfString.NulltoEmpty(rs.getString("PUBLISHER")));
				book.setPublishYear(ToolsOfString.NulltoEmpty(rs.getString("PUBLISHYEAR")));
				book.setEdition(rs.getString("EDITION"));
				book.setLanguage(rs.getString("LANGUAGE"));
				book.setBookType(rs.getString("BOOKTYPE"));
				book.setRemarks(ToolsOfString.NulltoEmpty(rs.getString("REMARKS")));
				bookList.add(book);

				Price tprice = new Price();
				tprice.setMopFuturePrice(rs.getDouble("MOP_PRICE"));
				tprice.setRmbFuturePrice(0);
				tprice.setMopNetPrice(rs.getDouble("MOP_NETPRICE"));
				tprice.setRmbNetPrice(0);
				tprice.setWithdrawPrice(0);
				priceList.add(tprice);

				Course course = new Course();
				course.setCourseCode(rs.getString("COURSECODE"));
				courseList.add(course);

				BookTempl tBookTempl = new BookTempl();
				tBookTempl.setYear(0);
				tBookTempl.setFacultyCode("facultyCode");
				bookTemplList.add(tBookTempl);
			}
			availableStudentBookList.add(bookList);
			availableStudentBookList.add(priceList);
			availableStudentBookList.add(courseList);
			availableStudentBookList.add(bookTemplList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					rs.close();
				}
			} catch (SQLException sqle) { }
		}
		return availableStudentBookList;
	}

	public List searchOrderedBook(Connection con, String studentNo,
			String orderIntake, String paidStatus, int orderSeqNoParam) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List bookList = new ArrayList();
		List priceList = new ArrayList();
		List qtyList = new ArrayList();
		List oList = new ArrayList();
		List orderedBookList = new ArrayList();

		String sql = null;

		String str[] = studentNo.split(",");
		String studNo = null;
		String appNo = null;
		if (str.length != 0) {
			studNo = str[0];
			appNo = str[1];
		}
		sql = "select "
				+ "tb.isbn, tb.title, tb.author, tb.publisher, tb.publishYear, tb.edition, tb.language, tb.bookType, tb.remarks, "
				+ "mp.futurePrice, rp.futurePrice, "
				+ "mp.withdrawPrice, mp.netPrice, rp.netPrice, "
				+ "od.confirmQty, od.unconfirmQty, od.withdrawQty, tb.withdrawInd, od.notEnoughQty, od.withdrawQty2,"
				+ "o.orderseqno,o.paidcurrency,o.paidstatus,od.orderIntake,od.paidamount "
				+ "from tordetail od "
				+ "inner join torder o on o.orderseqno=od.orderseqno and o.orderintake=od.orderintake "
				+ "inner join tbook tb on od.isbn = tb.isbn "
				+ "left outer join tprice mp on tb.isbn = mp.isbn and mp.currency = 'MOP' and mp.actind = 'Y' and mp.intake=od.orderintake "
				+ "left outer join tprice rp on tb.isbn = rp.isbn and rp.currency = 'RMB' and rp.actind = 'Y' and rp.intake=od.orderintake "
				+ "where od.actInd = 'Y' and tb.actInd = 'Y' and o.actind='Y' ";
		if (studNo != null && appNo != null && !studNo.equals("") && !appNo.equals("")) {
			sql += "and (od.studentNo='" + studNo + "' or od.studentNo='" + appNo + "') ";
		}
		if (!paidStatus.equals("") && paidStatus.equals("Y")) {
			sql += "and (o.paidstatus='" + paidStatus + "' or o.paidstatus='R') and od.confirmQty>0 ";
		} else if (!paidStatus.equals("") && paidStatus.equals("N")) {
			sql += "and o.paidstatus='" + paidStatus + "' ";
		}
		if(orderIntake!=null && !orderIntake.equals("")){
			sql += "and od.orderIntake='" + orderIntake + "' ";
		}
		if (orderSeqNoParam != 0) {
			sql += "and od.orderseqno=" + orderSeqNoParam + " ";
		}
		sql += "and od.actInd = 'Y' and tb.actInd = 'Y' and o.actind='Y' ";
		if (!paidStatus.equals("") && paidStatus.equals("Y")) {
			sql += "order by od.orderseqno, od.notEnoughQty, tb.isbn ";
		}
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIsbn(rs.getString(1));
				book.setTitle(ToolsOfString.NulltoEmpty(rs.getString(2)));
				book.setAuthor(ToolsOfString.NulltoEmpty(rs.getString(3)));
				book.setPublisher(ToolsOfString.NulltoEmpty(rs.getString(4)));
				book.setPublishYear(ToolsOfString.NulltoEmpty(rs.getString(5)));
				book.setEdition(ToolsOfString.NulltoEmpty(rs.getString(6)));
				book.setLanguage(ToolsOfString.NulltoEmpty(rs.getString(7)));
				book.setBookType(ToolsOfString.NulltoEmpty(rs.getString(8)));
				book.setRemarks(ToolsOfString.NulltoEmpty(rs.getString(9)));

				Price tprice = new Price();
				tprice.setMopFuturePrice(rs.getDouble(10));
				tprice.setRmbFuturePrice(rs.getDouble(11));
				tprice.setWithdrawPrice(rs.getDouble(12));
				tprice.setMopNetPrice(rs.getDouble(13));
				tprice.setRmbNetPrice(rs.getDouble(14));

				priceList.add(tprice);

				OrDetail ordetail = new OrDetail();
				ordetail.setOrderIntake(rs.getString("orderIntake"));
				ordetail.setIsbn(rs.getString("isbn"));
				ordetail.setOrderSeqNo(rs.getInt("orderseqno"));
				ordetail.setConfirmQty(rs.getInt(15));
				ordetail.setUnconfirmQty(rs.getInt(16));
				ordetail.setWithdrawQty(rs.getInt(17));

				book.setWithdrawInd(rs.getString(18));
				if (rs.getObject(19) == null) {
					ordetail.setNotEnoughQty(-1);
				} else {
					ordetail.setNotEnoughQty(rs.getInt(19));
				}
				ordetail.setWithdrawQty2(rs.getInt(20));
				ordetail.setPaidAmount(rs.getDouble("paidamount"));
				Order order = new Order();
				order.setOrderSeqNo(rs.getInt("orderseqno"));
				order.setPaidCurrency(rs.getString("paidcurrency"));
				order.setPaidStatus(rs.getString("paidstatus"));

				qtyList.add(ordetail);
				bookList.add(book);
				oList.add(order);
			}
			orderedBookList.add(bookList);
			orderedBookList.add(priceList);
			orderedBookList.add(qtyList);
			orderedBookList.add(oList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orderedBookList;
	}

}
