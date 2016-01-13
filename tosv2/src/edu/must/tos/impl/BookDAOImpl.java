package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookSummaryBean;
import edu.must.tos.bean.Course;
import edu.must.tos.dao.BookDAO;
import edu.must.tos.util.UsePage;
import edu.must.tos.util.ToolsOfString;

public class BookDAOImpl implements BookDAO {

	public List getBooksByCos(Connection conn, String intake, String courseCode) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List resultList = new ArrayList();
		try{
			String sql = "select tb.isbn, tb.title, tb.author, tb.publisher, tb.edition, tb.language, tb.grade, tb.core from tbook tb " +
					     "inner join tbookrel tr on tr.isbn = tb.isbn " +
					     "where tb.actind = 'Y' and tr.actind = 'Y' and tr.intake = ? and tr.coursecode = ? " +
					     "order by tb.isbn ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, courseCode);
			rs = pstmt.executeQuery();
			while ( rs.next() ){
				Book book = new Book();
				book.setIsbn(rs.getString("isbn"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPublisher(rs.getString("publisher"));
				book.setEdition(rs.getString("edition"));
				book.setLanguage(rs.getString("language"));
				book.setGrade(rs.getString("grade"));
				book.setCore(rs.getString("core"));
				resultList.add(book);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return resultList;
	}
	/**
	 * @param conn
	 * @param isbn
	 * @return
	 * @throws SQLException
	 */
	public Book getBookByPK(Connection conn, String isbn) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Book book = new Book();
		try {
			String sql = "select isbn, title, author, publisher, edition, language, bookType, publishYear, remarks, withdrawInd, " 
					+ "supplement, currency, unitPrice, disCount, favourablePrice, grade, core, "
					+ "suppliercode1, suppliercode2, actind, credate, creuid, upddate, upduid "
					+ "from TBOOK where isbn=? and actInd =? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, isbn);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				book.setIsbn(rs.getString("isbn"));
				book.setTitle(ToolsOfString.NulltoEmpty(rs.getString("title")));
				book.setAuthor(ToolsOfString.NulltoEmpty(rs.getString("author")));
				book.setPublisher(ToolsOfString.NulltoEmpty(rs.getString("publisher")));
				book.setPublishYear(rs.getString("publishYear"));
				book.setEdition(ToolsOfString.NulltoEmpty(rs.getString("edition")));
				book.setLanguage(rs.getString("language"));
				book.setBookType(rs.getString("bookType"));
				book.setRemarks(ToolsOfString.NulltoEmpty(rs.getString("remarks")));
				book.setWithdrawInd(ToolsOfString.NulltoEmpty(rs.getString("withdrawInd")));
				book.setSupplierCode1(ToolsOfString.NulltoEmpty(rs.getString("suppliercode1")));
				book.setSupplierCode2(ToolsOfString.NulltoEmpty(rs.getString("suppliercode2")));
				book.setActInd(rs.getString("actind"));
				book.setCreDate(rs.getTimestamp("credate"));
				book.setCreUid(rs.getString("creuid"));
				book.setUpdDate(rs.getTimestamp("upddate"));
				book.setUpdUid(rs.getString("upduid"));
				book.setSupplement(ToolsOfString.NulltoEmpty(rs.getString("supplement")));
				book.setCurrency(rs.getString("currency"));
				book.setUnitPrice(rs.getDouble("unitPrice"));
				book.setDisCount(rs.getDouble("disCount"));
				book.setFavourablePrice(rs.getDouble("favourablePrice"));
				book.setGrade(rs.getString("grade"));
				book.setCore(rs.getString("core"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return book;
	}

	/**
	 * 
	 * @param conn
	 * @return all book list List
	 * @throws SQLException
	 */
	public List getAllBook(Connection conn) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select isbn, title, author, publisher, publishYear, edition, language, bookType, remarks, "
					+ "withdrawInd, suppliercode1, suppliercode2, actind, credate, creuid, upddate, upduid, "
					+ "supplement, currency, unitPrice, disCount, favourablePrice, grade, core "
					+ "from tbook where actInd='Y' ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIsbn(rs.getString("isbn"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPublisher(rs.getString("publisher"));
				book.setPublishYear(rs.getString("publishYear"));
				book.setEdition(rs.getString("edition"));
				book.setLanguage(rs.getString("language"));
				book.setBookType(rs.getString("bookType"));
				book.setRemarks(rs.getString("remarks"));
				book.setWithdrawInd(rs.getString("withdrawInd"));
				book.setSupplierCode1(rs.getString("suppliercode1"));
				book.setSupplierCode2(rs.getString("suppliercode2"));
				book.setActInd(rs.getString("actind"));
				book.setCreDate(rs.getTimestamp("credate"));
				book.setCreUid(rs.getString("creuid"));
				book.setUpdDate(rs.getTimestamp("upddate"));
				book.setUpdUid(rs.getString("upduid"));
				book.setSupplement(rs.getString("supplement"));
				book.setCurrency(rs.getString("currency"));
				book.setUnitPrice(rs.getDouble("unitPrice"));
				book.setDisCount(rs.getDouble("disCount"));
				book.setFavourablePrice(rs.getDouble("favourablePrice"));
				book.setGrade(rs.getString("grade"));
				book.setCore(rs.getString("core"));
				resultList.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resultList;
	}

	/**
	 * 
	 * @param conn
	 * @param book
	 * @param course
	 * @return
	 * @throws Exception
	 */
	public List queryBook(Connection conn, Book book, Course course, int start, int num) throws Exception {
		List list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;
		if ((course.getFacultyCode() != null && !course.getFacultyCode().equals(""))
				|| (course.getCourseCode() != null && !course.getCourseCode().equals(""))) {
			sql = "select isbn, title, author, publisher, publishYear from tbook " 
				+ "where tbook.actInd='Y' " 
				+ "and isbn in (select isbn from tbookrel where tbookrel.actInd='Y' and (courseCode='OTHER' or courseCode in "
				+ "(select tcourse.courseCode from tcourse, tfaculty where 1=1 ";
			if (course.getFacultyCode() != null && !course.getFacultyCode().equals("")) {
				sql += " and tcourse.facultyCode='" + course.getFacultyCode() + "'";
			}
			if (course.getCourseCode() != null && !course.getCourseCode().equals("")) {
				sql += " and tcourse.courseCode like '%" + course.getCourseCode() + "%'";
			}
			sql += ")))";

			if (book.getIsbn() != null && !book.getIsbn().equals("")) {
				sql += " and tbook.isbn like '%" + book.getIsbn() + "%'";
			}
			if (book.getTitle() != null && !book.getTitle().equals("")) {
				sql += " and tbook.title like '%" + book.getTitle() + "%'";
			}
			if (book.getAuthor() != null && !book.getAuthor().equals("")) {
				sql += " and tbook.author like '%" + book.getAuthor() + "%'";
			}
			if (book.getPublisher() != null && !book.getPublisher().equals("")) {
				sql += " and tbook.publisher like '%" + book.getPublisher() + "%'";
			}
		} else {
			sql = "select isbn, title, author, publisher, publishYear from tbook where actInd ='Y' ";
			if (book.getIsbn() != null && !book.getIsbn().equals("")) {
				sql += " and isbn like '%" + book.getIsbn() + "%'";
			}
			if (book.getTitle() != null && !book.getTitle().equals("")) {
				sql += " and title like '%" + book.getTitle() + "%'";
			}
			if (book.getAuthor() != null && !book.getAuthor().equals("")) {
				sql += " and author like '%" + book.getAuthor() + "%'";
			}
			if (book.getPublisher() != null && !book.getPublisher().equals("")) {
				sql += " and publisher like '%" + book.getPublisher() + "%'";
			}
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			while (rs.next() && j <= num) {
				Book b = new Book();
				b.setIsbn(rs.getString("isbn"));
				b.setTitle(ToolsOfString.NulltoEmpty(rs.getString("title")));
				b.setAuthor(ToolsOfString.NulltoEmpty(rs.getString("author")));
				b.setPublisher(ToolsOfString.NulltoEmpty(rs.getString("publisher")));
				b.setPublishYear(rs.getString("publishYear"));
				list.add(b);
				j++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List getIsbn(Connection conn) throws Exception {
		List list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select isbn from tbook where actind='Y' ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("isbn"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	/**
	 * 
	 * @param conn
	 * @param book
	 * @return
	 * @throws Exception
	 */
	public int saveInDB(Connection conn, Book book) throws Exception {
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		int temp = 0;
		String sql = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "select isbn from tbook where isbn = ?";
			pstmt1 = conn.prepareStatement(sql);
			pstmt1.setString(1, book.getIsbn());
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				sql = "select isbn from tbook where isbn = ? and actInd = ?";
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setString(1, book.getIsbn());
				pstmt2.setString(2, "N");
				rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
					temp = updateOneBook(conn, book);
					if (temp > 0) {
						temp = 1;
					}
				} else {
					temp = updateOneBook(conn, book);
					if (temp > 0) {
						temp = 1;
					}
				}
			} else {
				sql = "insert into tbook "
						+ "(isbn, title, author, publisher, publishYear, edition, language, bookType, remarks, updUid, updDate, actInd, " 
						+ "withdrawInd, suppliercode1, suppliercode2, supplement, currency, unitPrice, disCount, favourablePrice, grade, core, class ) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt3 = conn.prepareStatement(sql);
				pstmt3.setString(1, book.getIsbn());
				pstmt3.setString(2, book.getTitle());
				pstmt3.setString(3, book.getAuthor());
				pstmt3.setString(4, book.getPublisher());
				pstmt3.setString(5, book.getPublishYear());
				pstmt3.setString(6, book.getEdition());
				pstmt3.setString(7, book.getLanguage());
				pstmt3.setString(8, book.getBookType());
				pstmt3.setString(9, book.getRemarks());
				pstmt3.setString(10, book.getUpdUid());
				pstmt3.setTimestamp(11, ts);
				pstmt3.setString(12, book.getActInd());
				pstmt3.setString(13, book.getWithdrawInd());
				pstmt3.setString(14, book.getSupplierCode1());
				pstmt3.setString(15, book.getSupplierCode2());
				pstmt3.setString(16, book.getSupplement());
				pstmt3.setString(17, book.getCurrency());
				pstmt3.setDouble(18, book.getUnitPrice());
				pstmt3.setDouble(19, book.getDisCount());
				pstmt3.setDouble(20, book.getFavourablePrice());
				pstmt3.setString(21, book.getGrade());
				pstmt3.setString(22, book.getCore());
				pstmt3.setString(23, book.getCls());
				temp = pstmt3.executeUpdate();
				if (temp > 0) {
					temp = 1;
				} else {
					temp = 0;
				}
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if (rs1 != null)
				rs1.close();
			if (rs2 != null)
				rs2.close();
			if (pstmt1 != null)
				pstmt1.close();
			if (pstmt2 != null)
				pstmt2.close();
			if (pstmt3 != null)
				pstmt3.close();
		}
		return temp;
	}

	// 分頁
	public static List getpage(Connection con, Book book, Course course,
			String start, String num, int count) {
		//int count = getRsCount(con, book, course);
		List page = UsePage.getPage(count, start, num);
		return page;
	}

	// 獲取记录总数方法
	public static int getRsCount(Connection conn, Book book, Course course) {
		int i = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;
		if ((course.getFacultyCode() != null && !course.getFacultyCode().equals(""))
				|| (course.getCourseCode() != null && !course.getCourseCode().equals(""))) {
			sql = "select count(*) from tbook where tbook.actind='Y' and isbn in "
					+ "(select isbn from tbookrel where tbookrel.actind='Y' and (courseCode='OTHER' or courseCode in "
					+ "(select tcourse.coursecode from tcourse, tfaculty where 1=1 ";
			if (course.getFacultyCode() != null && !course.getFacultyCode().equals("")) {
				sql += " and tcourse.facultycode='" + course.getFacultyCode() + "'";
			}
			if (course.getCourseCode() != null && !course.getCourseCode().equals("")) {
				sql += " and tcourse.coursecode like '%" + course.getCourseCode() + "%'";
			}
			sql += ")))";

			if (book.getIsbn() != null && !book.getIsbn().equals("")) {
				sql += " and tbook.isbn like '%" + book.getIsbn() + "%'";
			}
			if (book.getTitle() != null && !book.getTitle().equals("")) {
				sql += " and tbook.title like '%" + book.getTitle() + "%'";
			}
			if (book.getAuthor() != null && !book.getAuthor().equals("")) {
				sql += " and tbook.author like '%" + book.getAuthor() + "%'";
			}
			if (book.getPublisher() != null && !book.getPublisher().equals("")) {
				sql += " and tbook.publisher like '%" + book.getPublisher() + "%'";
			}

		} else {
			sql = "select count(*) from tbook where actind ='Y' ";
			if (book.getIsbn() != null && !book.getIsbn().equals("")) {
				sql += " and isbn like '%" + book.getIsbn() + "%'";
			}
			if (book.getTitle() != null && !book.getTitle().equals("")) {
				sql += " and title like '%" + book.getTitle() + "%'";
			}
			if (book.getAuthor() != null && !book.getAuthor().equals("")) {
				sql += " and author like '%" + book.getAuthor() + "%'";
			}
			if (book.getPublisher() != null && !book.getPublisher().equals("")) {
				sql += " and publisher like '%" + book.getPublisher() + "%'";
			}
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return i;
	}

	/**
	 * 
	 * @param book
	 * @return update result 1 int if success
	 * @throws Exception
	 */
	public int updateOneBook(Connection conn, Book book) throws Exception {
		PreparedStatement pstmt = null;
		int temp;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			String sql = "update tbook set "
					+ "title=?, author=?, publisher=?, publishYear=?, edition=?, language=?, bookType=?, "
					+ "remarks=?, updUid=?, updDate=?, withdrawInd=?, suppliercode1=?, suppliercode2=?, actInd=?, supplement=?, " 
					+ "currency=?, unitPrice=?, disCount=?, favourablePrice=?, grade=?, core=?, class=? "
					+ "where isbn = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, book.getTitle());
			pstmt.setString(2, book.getAuthor());
			pstmt.setString(3, book.getPublisher());
			pstmt.setString(4, book.getPublishYear());
			pstmt.setString(5, book.getEdition());
			pstmt.setString(6, book.getLanguage());
			pstmt.setString(7, book.getBookType());
			pstmt.setString(8, book.getRemarks());
			pstmt.setString(9, book.getUpdUid());
			pstmt.setTimestamp(10, ts);
			pstmt.setString(11, book.getWithdrawInd());
			pstmt.setString(12, book.getSupplierCode1());
			pstmt.setString(13, book.getSupplierCode2());
			pstmt.setString(14, book.getActInd());
			pstmt.setString(15, book.getSupplement());
			pstmt.setString(16, book.getCurrency());
			pstmt.setDouble(17, book.getUnitPrice());
			pstmt.setDouble(18, book.getDisCount());
			pstmt.setDouble(19, book.getFavourablePrice());
			pstmt.setString(20, book.getGrade());
			pstmt.setString(21, book.getCore());
			pstmt.setString(22, book.getCls());
			pstmt.setString(23, book.getIsbn());		
			temp = pstmt.executeUpdate();
			if (temp > 0) {
				temp = 1;
			} else {
				temp = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return temp = -1;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return temp;
	}

	/**
	 * 
	 * @param b
	 * @return
	 * @throws SQLException
	 */
	public boolean delBook(Connection conn, Book book) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "update tbook set actind=?, upduid=?, upddate=? where isbn=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, book.getActInd());
			pstmt.setString(2, book.getUpdUid());
			pstmt.setTimestamp(3, new java.sql.Timestamp(book.getUpdDate().getTime()));
			pstmt.setString(4, book.getIsbn());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public Vector getStatisticByBook(Connection conn, String intake,
			String fromDate, String toDate) throws Exception {
		Vector outter = new Vector();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select tb.isbn,tb.title,tb.author,tb.publisher,tb.publishYear,tb.edition,tb.language,tb.bookType,tb.remarks,"
					+ "(select tbs.suppliername from tbooksupplier tbs where tbs.actind='Y' and tbs.suppliercode=tb.suppliercode1) as suppliercode1,"
					+ "(select tbs.suppliername from tbooksupplier tbs where tbs.actind='Y' and tbs.suppliercode=tb.suppliercode2) as suppliercode2,"
					+ "ord.orderIntake,ord.confirmQty, ord.unconfirmQty, ord.withdrawQty,"
					+ "mp.futurePrice as mop_futureprice,"
					+ "rp.futurePrice as rmb_futureprice,"
					+ "mp.netPrice as mop_netprice,"
					+ "rp.netPrice as rmb_netprice,"
					+ "mp.withdrawPrice as mop_withdrawprice,"
					+ "rp.withdrawPrice as rmb_withdrawprice,"
					+ "(ord.confirmQty * mp.futurePrice) as totalConfirmMopFutAmt,"
					+ "(ord.unconfirmQty * mp.futurePrice) as totalUnconfirmMopFutAmt,"
					+ "(ord.confirmQty * rp.futurePrice) as totalConfirmRmbFutAmt,"
					+ "(ord.unconfirmQty * rp.futurePrice) as totalUnconfirmRmbFutAmt,"
					+ "(ord.confirmQty * mp.netPrice) as totalConfirmMopNetAmt,"
					+ "(ord.unconfirmQty * mp.netPrice) as totalUnconfirmMopNetAmt,"
					+ "(ord.confirmQty * rp.netPrice ) as totalConfirmRmbNetAmt,"
					+ "(ord.unconfirmQty * rp.netPrice ) as totalUnconfirmRmbNetAmt,"
					+ "(ord.withdrawQty * mp.withdrawPrice) as totalWithdrawMopAmt,"
					+ "(ord.withdrawQty * rp.withdrawPrice) as totalWithdrawRmbAmt "
					+ "from  (select o.orderIntake, od.isbn, sum(od.confirmQty) as confirmQty, "
					+ "sum(od.unconfirmQty) as unconfirmQty, sum(od.withdrawQty) as withdrawQty "
					+ "from  torder o "
					+ "inner join tordetail od  on o.orderseqno = od.orderseqno and o.orderIntake = od.orderIntake and od.actInd = 'Y' "
					+ "where  o.actInd = 'Y' ";
			if (!fromDate.equals("") && !toDate.equals("")) {
				sql += "and od.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			sql += "group by o.orderIntake, od.isbn) ord "
					+ "inner join tbook tb on ord.isbn = tb.isbn and tb.actInd = 'Y' "
					+ "left outer join tprice mp on tb.isbn = mp.isbn and mp.currency = 'MOP' and mp.intake = ord.orderintake and mp.actind = 'Y' "
					+ "left outer join tprice rp on tb.isbn = rp.isbn and rp.currency = 'RMB' and rp.intake = ord.orderintake and rp.actind = 'Y' "
					+ "where ord.orderIntake = ? order by tb.isbn ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Vector inner = new Vector();
				inner.add(rs.getString("isbn"));
				inner.add(rs.getString("title"));
				inner.add(rs.getString("author"));
				inner.add(rs.getString("publisher"));
				inner.add(rs.getString("publishYear"));
				inner.add(rs.getString("edition"));
				inner.add(rs.getString("language"));
				//inner.add(rs.getString("bookType"));
				inner.add(rs.getString("remarks"));
				inner.add(rs.getString("suppliercode1"));
				inner.add(rs.getString("suppliercode2"));
				inner.add(rs.getString("orderIntake"));
				inner.add(rs.getInt("confirmQty"));
				inner.add(rs.getInt("withdrawQty"));
				inner.add(rs.getDouble("mop_futureprice"));
				inner.add(rs.getDouble("rmb_futureprice"));
				inner.add(rs.getDouble("mop_netprice"));
				inner.add(rs.getDouble("rmb_netprice"));
				inner.add(rs.getDouble("mop_withdrawprice"));
				inner.add(rs.getDouble("rmb_withdrawprice"));
				inner.add(rs.getDouble("totalConfirmMopFutAmt"));
				inner.add(rs.getDouble("totalConfirmRmbFutAmt"));
				inner.add(rs.getDouble("totalConfirmMopNetAmt"));
				inner.add(rs.getDouble("totalConfirmRmbNetAmt"));
				inner.add(rs.getDouble("totalWithdrawMopAmt"));
				inner.add(rs.getDouble("totalWithdrawRmbAmt"));
				outter.add(inner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return outter;
	}

	public List getStatisticSummaryByBook(Connection conn, String intake,
			String fromDate, String toDate, String paidFromDate, String paidToDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String query = "select tb.isbn,tb.title,tb.author,tb.publisher,tb.publishYear,tb.edition,tb.language,tb.bookType,tb.remarks,"
					+ "(select tbs.suppliername from tbooksupplier tbs where tbs.actind='Y' and tbs.suppliercode=tb.suppliercode1) as suppliercode1,"
					+ "(select tbs.suppliername from tbooksupplier tbs where tbs.actind='Y' and tbs.suppliercode=tb.suppliercode2) as suppliercode2,"
					+ "tb.unitprice, tb.currency, tb.discount, tb.favourableprice, "
					+ "ord.orderIntake, ord.confirmQty, ord.unconfirmQty, ord.withdrawQty, un.unconfirmTake "
					+ "from (select o.orderIntake, od.isbn, "
					+ "sum(od.confirmQty) as confirmQty, sum(od.unconfirmQty) as unconfirmQty, "
					+ "sum(od.withdrawQty) as withdrawQty from torder o "
					+ "inner join tordetail od on o.orderseqno = od.orderseqno and o.orderIntake = od.orderIntake and od.actInd = 'Y' "
					+ "left outer join ttransaction tt on tt.orderseqno = to_char(o.orderseqno) and tt.paymenttype='收費' "
					+ "where o.actInd = 'Y' ";
			if (!fromDate.equals("") && !toDate.equals("")) {
				query += "and od.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			if(!"".equals(paidFromDate) && !"".equals(paidToDate)){
				query += "and tt.paiddate between to_date('"+ paidFromDate +"', 'yyyy-mm-dd hh24:mi:ss') and to_date('"+ paidToDate +"', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			query += "group by o.orderIntake, od.isbn) ord "
					+ "inner join tbook tb on ord.isbn = tb.isbn and tb.actInd = 'Y' "
					+ "left outer join (select o.orderintake,od.isbn,"
					+ "sum(case when od.notenoughqty>0 then od.notenoughqty when od.notenoughqty is null then od.confirmqty end) as unconfirmTake "
					+ "from torder o "
					+ "inner join tordetail od on od.orderseqno = o.orderseqno and od.orderintake = o.orderintake and od.actind = 'Y' "
					+ "left outer join ttransaction tt on tt.orderseqno = to_char(o.orderseqno) and tt.paymenttype='收費' "
					+ "where (od.notenoughqty is null or od.notenoughqty>0) and o.actind='Y' ";
			if (!fromDate.equals("") && !toDate.equals("")) {
				query += "and od.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			if(!"".equals(paidFromDate) && !"".equals(paidToDate)){
				query += "and tt.paiddate between to_date('"+ paidFromDate +"', 'yyyy-mm-dd hh24:mi:ss') and to_date('"+ paidToDate +"', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			query += "group by o.orderintake,od.isbn) "
					+ "un on un.orderintake=ord.orderintake and un.isbn=ord.isbn ";
			query += "where ord.orderIntake = ? order by tb.isbn";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookSummaryBean bean = new BookSummaryBean();
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishYear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.getBook().setLanguage(rs.getString("language"));
				bean.getBook().setBookType(rs.getString("bookType"));
				bean.getBook().setRemarks(rs.getString("remarks"));
				bean.getBook().setSupplierCode1(rs.getString("suppliercode1"));
				bean.getBook().setSupplierCode2(rs.getString("suppliercode2"));
				bean.getBook().setUnitPrice(rs.getDouble("unitprice"));
				bean.getBook().setCurrency(rs.getString("currency"));
				bean.getBook().setDisCount(rs.getDouble("discount"));
				bean.getBook().setFavourablePrice(rs.getDouble("favourableprice"));
				bean.setOrderIntake(rs.getString("orderIntake"));
				bean.setConfirmQty(rs.getInt("confirmQty"));
				if (rs.getObject("unconfirmTake") != null) {
					bean.setUnconfirmTake(rs.getInt("unconfirmTake"));
				}
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return list;
	}
}
