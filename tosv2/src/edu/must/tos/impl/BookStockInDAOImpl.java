package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookStockInBean;
import edu.must.tos.bean.BookStockIn;
import edu.must.tos.dao.BookStockInDAO;

public class BookStockInDAOImpl implements BookStockInDAO {

	public BookStockIn getBookStockIn(Connection conn, BookStockIn b)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BookStockIn bookStockIn = null;
		try {
			String sql = "select no, intake, prnum, isbn, credate, creuid, adjnum, purchaseprice, discount, costprice, remarks "
					+ "from tbookstockin where actind='Y' ";
			if (b.getIntake() != null) {
				sql += "and intake='" + b.getIntake() + "' ";
			}
			if (b.getIsbn() != null) {
				sql += "and isbn='" + b.getIsbn() + "' ";
			}
			if (b.getPRnum() != null) {
				sql += "and prnum='" + b.getPRnum() + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bookStockIn = new BookStockIn();
				bookStockIn.setNo(rs.getInt("no"));
				bookStockIn.setIntake(rs.getString("intake"));
				bookStockIn.setPRnum(rs.getString("prnum"));
				bookStockIn.setIsbn(rs.getString("isbn"));
				bookStockIn.setCredate(rs.getTimestamp("credate"));
				bookStockIn.setCreuid(rs.getString("creuid"));
				bookStockIn.setAdjnum(rs.getInt("adjnum"));
				bookStockIn.setPurchasePrice(rs.getDouble("purchaseprice"));
				bookStockIn.setDiscount(rs.getDouble("discount"));
				bookStockIn.setCostPrice(rs.getDouble("costprice"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return bookStockIn;
	}

	public List getBookStockInByIsbn(Connection conn, String intake,
			String isbn) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select " +
					"(case when ts.suppliername is null then ts.supplierengname else ts.suppliername end) as supplierName, " +
					"t.intake, t.prnum, t.indate, " +
					"t.paidstatus, t.paiddate, t.paidcurrency, t.invoicedate, " +
					"tbi.no, tbi.isbn, tbi.purchaseprice, tbi.discount, tbi.costprice, tbi.adjnum,tbi.remarks " +
					"from tbookstockinorder t " +
					"inner join tbookstockin tbi on tbi.intake = t.intake and tbi.prnum = t.prnum and tbi.actind = 'Y' " +
					"left outer join tbooksupplier ts on ts.supplierno = t.supplierno and ts.actind='Y' " +
					"where t.actind='Y' ";
			if(intake != null && !intake.equals("")){
				sql += "and t.intake = '" + intake + "' ";
			}
			if(isbn != null && !isbn.equals("")){
				sql += "and tbi.isbn = '" + isbn + "' ";
			}
			sql += "order by t.intake, ts.supplierno, t.prnum, tbi.isbn, t.indate ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockInBean bookStockInBean = new BookStockInBean();
				bookStockInBean.setSupplierName(rs.getString("supplierName"));
				bookStockInBean.setIntake(rs.getString("intake"));
				bookStockInBean.setPrNum(rs.getString("prnum"));
				bookStockInBean.setIndate(rs.getString("indate"));
				bookStockInBean.setPaidStatus(rs.getString("paidstatus"));
				bookStockInBean.setPaiddate(rs.getString("paiddate"));
				bookStockInBean.setPaidCurrency(rs.getString("paidcurrency"));
				bookStockInBean.setInvoiceDate(rs.getString("invoicedate"));
				bookStockInBean.setNo(rs.getInt("no"));
				bookStockInBean.setIsbn(rs.getString("isbn"));
				bookStockInBean.setPurchasePrice(rs.getDouble("purchaseprice"));
				bookStockInBean.setDiscount(rs.getDouble("discount"));
				bookStockInBean.setCostPrice(rs.getDouble("costprice"));
				bookStockInBean.setAdjNum(rs.getInt("adjnum"));
				bookStockInBean.setRemarks(rs.getString("remarks"));
				list.add(bookStockInBean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return list;
	}

	public List getBookStockInInfoList(Connection conn, String prNum,
			String fromDate, String toDate, String paidStatus, int supplierNo)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select (case when ts.suppliername is null then ts.supplierengname else ts.suppliername end) as supplierName, "
					+ "t.intake, t.prnum, t.indate, "
					+ "t.paidstatus, t.paiddate, t.paidcurrency, t.invoicedate, "
					+ "tbi.isbn, tb.title, tb.author, tb.publisher, tb.publishyear, tb.edition, "
					+ "tbi.purchaseprice, tbi.discount, tbi.costprice, tbi.adjnum, "
					+ "(tbi.purchaseprice * tbi.discount * tbi.adjnum) as totalPrice, tbi.remarks "
					+ "from tbookstockinorder t "
					+ "inner join tbookstockin tbi on tbi.intake=t.intake and tbi.prnum=t.prnum and tbi.actind='Y' "
					+ "inner join tbook tb on tb.isbn=tbi.isbn "
					+ "left outer join tbooksupplier ts on ts.supplierno = t.supplierno and ts.actind='Y' "
					+ "where t.actind='Y' and tb.actind='Y' ";
			if (prNum != null) {
				sql += "and t.prnum = '" + prNum + "' ";
			}
			if (fromDate != null && toDate != null) {
				sql += "and t.indate between '" + fromDate + "' and '" + toDate + "' ";
			}
			if (paidStatus != null) {
				sql += "and t.paidstatus = '" + paidStatus + "' ";
			}
			if (supplierNo != 0) {
				sql += "and ts.supplierno = " + supplierNo + " ";
			}
			sql += "order by t.intake,ts.supplierno,t.prnum,tbi.isbn,t.indate ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockInBean bookStockInBean = new BookStockInBean();
				bookStockInBean.setSupplierName(rs.getString("supplierName"));
				bookStockInBean.setIntake(rs.getString("intake"));
				bookStockInBean.setPrNum(rs.getString("prnum"));
				bookStockInBean.setIndate(rs.getString("indate"));
				bookStockInBean.setPaidStatus(rs.getString("paidstatus"));
				bookStockInBean.setPaiddate(rs.getString("paiddate"));
				bookStockInBean.setPaidCurrency(rs.getString("paidcurrency"));
				bookStockInBean.setInvoiceDate(rs.getString("invoicedate"));
				bookStockInBean.setIsbn(rs.getString("isbn"));
				bookStockInBean.getBook().setTitle(rs.getString("title"));
				bookStockInBean.getBook().setAuthor(rs.getString("author"));
				bookStockInBean.getBook().setPublisher(rs.getString("publisher"));
				bookStockInBean.getBook().setPublishYear(rs.getString("publishYear"));
				bookStockInBean.getBook().setEdition(rs.getString("edition"));
				bookStockInBean.setPurchasePrice(rs.getDouble("purchaseprice"));
				bookStockInBean.setDiscount(rs.getDouble("discount"));
				bookStockInBean.setCostPrice(rs.getDouble("costprice"));
				bookStockInBean.setAdjNum(rs.getInt("adjnum"));
				bookStockInBean.setTotalPrice(rs.getDouble("totalPrice"));
				bookStockInBean.setRemarks(rs.getString("remarks"));
				list.add(bookStockInBean);
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

	public boolean updateBookStockIn(Connection conn, BookStockIn bi)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookstockin set purchaseprice=?, discount=?, costprice=?, adjnum=?, remarks=?, actind=? "
					+ "where prnum=? and isbn=? and no=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, bi.getPurchasePrice());
			pstmt.setDouble(2, bi.getDiscount());
			pstmt.setDouble(3, bi.getCostPrice());
			pstmt.setInt(4, bi.getAdjnum());
			pstmt.setString(5, bi.getRemarks());
			pstmt.setString(6, bi.getActind());
			pstmt.setString(7, bi.getPRnum());
			pstmt.setString(8, bi.getIsbn());
			pstmt.setInt(9, bi.getNo());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}
	
	public boolean updateBookStockInByNo(Connection conn, BookStockInBean bi) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookstockin set prnum=?, isbn=?, purchaseprice=?, discount=?, costprice=?, adjnum=?, remarks=?, actind=? "
				+ "where no = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bi.getPrNum());
			pstmt.setString(2, bi.getIsbn());
			pstmt.setDouble(3, bi.getPurchasePrice());
			pstmt.setDouble(4, bi.getDiscount());
			pstmt.setDouble(5, bi.getCostPrice());
			pstmt.setInt(6, bi.getAdjNum());
			pstmt.setString(7, bi.getRemarks());
			pstmt.setString(8, bi.getActind());
			pstmt.setInt(9, bi.getNo());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public boolean updateBookStockInList(Connection conn, List<BookStockIn> list)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "insert into tbookstockin (intake, prnum, isbn, credate, creuid, adjnum, remarks, no, purchaseprice, discount, costprice, actind) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			for (BookStockIn bookStockIn : list) {
				pstmt.setString(1, bookStockIn.getIntake());
				pstmt.setString(2, bookStockIn.getPRnum());
				pstmt.setString(3, bookStockIn.getIsbn());
				pstmt.setTimestamp(4, new java.sql.Timestamp(bookStockIn.getCredate().getTime()));
				pstmt.setString(5, bookStockIn.getCreuid());
				pstmt.setInt(6, bookStockIn.getAdjnum());
				pstmt.setString(7, bookStockIn.getRemarks());
				pstmt.setInt(8, 0);
				pstmt.setDouble(9, bookStockIn.getPurchasePrice());
				pstmt.setDouble(10, bookStockIn.getDiscount());
				pstmt.setDouble(11, bookStockIn.getCostPrice());
				pstmt.setString(12, bookStockIn.getActind());
				if (pstmt.executeUpdate() > 0) {
					flag = true;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public boolean updateStockInPrNum(Connection conn, String prNum,
			String oldPrNum) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookstockin set prNum=? where prnum=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prNum);
			pstmt.setString(2, oldPrNum);
			pstmt.setString(3, "Y");
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public List getBookStockInList(Connection conn, BookStockIn bookStockIn)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select intake, prnum, isbn, credate, creuid, adjnum, remarks, purchaseprice, discount, costprice " +
					"from tbookstockin where actind='Y' ";
			if (bookStockIn.getIntake() != null && !bookStockIn.getIntake().equals("")) {
				sql += "and intake='" + bookStockIn.getIntake() + "' ";
			}
			if (bookStockIn.getPRnum() != null && !bookStockIn.getPRnum().equals("")) {
				sql += "and prnum='" + bookStockIn.getPRnum() + "' ";
			}
			if (bookStockIn.getIsbn() != null && !bookStockIn.getIsbn().equals("")) {
				sql += "and isbn='" + bookStockIn.getIsbn() + "' ";
			}
			sql += "order by credate desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockIn bsi = new BookStockIn();
				bsi.setIntake(rs.getString("intake"));
				bsi.setPRnum(rs.getString("prnum"));
				bsi.setIsbn(rs.getString("isbn"));
				bsi.setCredate(rs.getTimestamp("credate"));
				bsi.setCreuid(rs.getString("creuid"));
				bsi.setAdjnum(rs.getInt("adjnum"));
				bsi.setRemarks(rs.getString("remarks"));
				bsi.setPurchasePrice(rs.getDouble("purchaseprice"));
				bsi.setDiscount(rs.getDouble("discount"));
				bsi.setCostPrice(rs.getDouble("costprice"));
				list.add(bsi);
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
