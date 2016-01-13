package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.BookStockInBean;
import edu.must.tos.bean.BookStockInOrder;
import edu.must.tos.dao.BookStockInOrderDAO;

public class BookStockInOrderDAOImpl implements BookStockInOrderDAO {

	public List getStockInOrderListByPrNum(Connection conn, String prNum,
			String intake) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select stockinno, intake, prnum, supplierno, paidstatus, paidcurrency,"
					+ " paiddate, indate, actind, invoicedate "
					+ "from tbookstockinorder where actind='Y' ";
			if (prNum != null && !prNum.equals("")) {
				sql += "and prnum='" + prNum + "' ";
			}
			if (intake != null && !intake.equals("")) {
				sql += "and intake='" + intake + "' ";
			}
			sql += "order by intake desc, indate desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockInOrder bookStockInOrder = new BookStockInOrder();
				bookStockInOrder.setStockInNo(rs.getInt("stockinno"));
				bookStockInOrder.setIntake(rs.getString("intake"));
				bookStockInOrder.setPrnum(rs.getString("prnum"));
				bookStockInOrder.setSupplierNo(rs.getInt("supplierno"));
				bookStockInOrder.setPaidstatus(rs.getString("paidstatus"));
				bookStockInOrder.setPaidcurrency(rs.getString("paidcurrency"));
				bookStockInOrder.setPaidDate(rs.getString("paiddate"));
				bookStockInOrder.setInDate(rs.getString("indate"));
				bookStockInOrder.setActind(rs.getString("actind"));
				bookStockInOrder.setInvoiceDate(rs.getString("invoicedate"));
				list.add(bookStockInOrder);
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


	public boolean updateStockInOrder(Connection conn, BookStockInOrder o)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookstockinorder set prnum=?,upddate=?,upduid=? where stockinno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, o.getPrnum());
			pstmt.setTimestamp(2, new java.sql.Timestamp(o.getUpdDate().getTime()));
			pstmt.setString(3, o.getUpdUid());
			pstmt.setInt(4, o.getStockInNo());
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

	public BookStockInOrder getStockInOrderByPrNum(Connection conn,
			String prNum) throws Exception {
		BookStockInOrder bookStockInOrder = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select stockinno, intake, prnum, supplierno, paidstatus, paidcurrency,"
					+ " paiddate, indate, actind, invoicedate "
					+ "from tbookstockinorder where prnum=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prNum);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bookStockInOrder = new BookStockInOrder();
				bookStockInOrder.setStockInNo(rs.getInt("stockinno"));
				bookStockInOrder.setIntake(rs.getString("intake"));
				bookStockInOrder.setPrnum(rs.getString("prnum"));
				bookStockInOrder.setSupplierNo(rs.getInt("supplierno"));
				bookStockInOrder.setPaidstatus(rs.getString("paidstatus"));
				bookStockInOrder.setPaidcurrency(rs.getString("paidcurrency"));
				bookStockInOrder.setPaidDate(rs.getString("paiddate"));
				bookStockInOrder.setInDate(rs.getString("indate"));
				bookStockInOrder.setActind(rs.getString("actind"));
				bookStockInOrder.setInvoiceDate(rs.getString("invoicedate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return bookStockInOrder;
	}

	public List getStockInListByPrNum(Connection conn, String prnum) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select tbi.isbn,tb.title,t.prnum,t.indate,t.invoicedate,tbi.remarks,tbi.purchaseprice,"
					+ "tbi.discount,tbi.costprice,tbi.adjnum,tbi.no "
					+ "from tbookstockinorder t "
					+ "inner join tbookstockin tbi on tbi.intake=t.intake and tbi.prnum=t.prnum and tbi.actind='Y' "
					+ "inner join tbook tb on tb.isbn=tbi.isbn "
					+ "where t.actind='Y' and tb.actind='Y' and t.prnum=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prnum);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockInBean bean = new BookStockInBean();
				bean.setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.setPrNum(rs.getString("prnum"));
				bean.setIndate(rs.getString("indate"));
				bean.setRemarks(rs.getString("remarks"));
				bean.setPurchasePrice(rs.getDouble("purchaseprice"));
				bean.setDiscount(rs.getDouble("discount"));
				bean.setCostPrice(rs.getDouble("costprice"));
				bean.setInvoiceDate(rs.getString("invoicedate"));
				bean.getBookStockIn().setNo(rs.getInt("no"));
				bean.setAdjNum(rs.getInt("adjnum"));
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

	
	public List getStockInOrderListByCond(Connection conn, String prNum,
			String intake, String paidStatus) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select stockinno, intake, prnum, supplierno, paidstatus, paidcurrency,"
					+ " paiddate, indate, actind, invoicedate "
					+ "from tbookstockinorder where actind='Y' ";
			if (prNum != null && !prNum.equals("")) {
				sql += "and prnum='" + prNum + "' ";
			}
			if (intake != null && !intake.equals("")) {
				sql += "and intake='" + intake + "' ";
			}
			if (paidStatus != null && !paidStatus.equals("")) {
				sql += "and paidStatus='" + paidStatus + "' ";
			}
			sql += "order by intake desc, indate desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockInOrder bookStockInOrder = new BookStockInOrder();
				bookStockInOrder.setStockInNo(rs.getInt("stockinno"));
				bookStockInOrder.setIntake(rs.getString("intake"));
				bookStockInOrder.setPrnum(rs.getString("prnum"));
				bookStockInOrder.setSupplierNo(rs.getInt("supplierno"));
				bookStockInOrder.setPaidstatus(rs.getString("paidstatus"));
				bookStockInOrder.setPaidcurrency(rs.getString("paidcurrency"));
				bookStockInOrder.setPaidDate(rs.getString("paiddate"));
				bookStockInOrder.setInDate(rs.getString("indate"));
				bookStockInOrder.setActind(rs.getString("actind"));
				bookStockInOrder.setInvoiceDate(rs.getString("invoicedate"));
				list.add(bookStockInOrder);
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
	
	public boolean addStockInOrder(Connection conn, BookStockInOrder o) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		PreparedStatement insertPstmt = null;
		PreparedStatement updatePstmt = null;
		String sql = "";
		ResultSet rs = null;
		try {
			sql = "select prnum from tbookstockinorder where prnum=? and intake=? and actind=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, o.getPrnum());
			pstmt.setString(2, o.getIntake());
			pstmt.setString(3, o.getActind());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String updSql = "update tbookstockinorder set upddate=?, upduid=?, supplierno=?, paidstatus=?, "
						+ "paidcurrency=?, paiddate=?, indate=?, invoicedate=? "
						+ "where prnum=? and intake=? and actind=? ";
				updatePstmt = conn.prepareStatement(updSql);
				updatePstmt.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
				updatePstmt.setString(2, o.getCreUid());
				updatePstmt.setInt(3, o.getSupplierNo());
				updatePstmt.setString(4, o.getPaidstatus());
				updatePstmt.setString(5, o.getPaidcurrency());
				updatePstmt.setString(6, o.getPaidDate());
				updatePstmt.setString(7, o.getInDate());
				updatePstmt.setString(8, o.getInvoiceDate());
				updatePstmt.setString(9, o.getPrnum());
				updatePstmt.setString(10, o.getIntake());
				updatePstmt.setString(11, o.getActind());
				if (updatePstmt.executeUpdate() > 0) {
					flag = true;
				}
			} else {
				String insertSql = "insert into tbookstockinorder "
						+ "(intake,prnum,supplierno,paidstatus,paidcurrency,paiddate,indate,actind,credate,creuid,invoicedate) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?)";
				insertPstmt = conn.prepareStatement(insertSql);
				insertPstmt.setString(1, o.getIntake());
				insertPstmt.setString(2, o.getPrnum());
				insertPstmt.setInt(3, o.getSupplierNo());
				insertPstmt.setString(4, o.getPaidstatus());
				insertPstmt.setString(5, o.getPaidcurrency());
				insertPstmt.setString(6, o.getPaidDate());
				insertPstmt.setString(7, o.getInDate());
				insertPstmt.setString(8, o.getActind());
				insertPstmt.setTimestamp(9, new java.sql.Timestamp(o.getCreDate().getTime()));
				insertPstmt.setString(10, o.getCreUid());
				insertPstmt.setString(11, o.getInvoiceDate());
				if (insertPstmt.executeUpdate() > 0) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (updatePstmt != null)
				updatePstmt.close();
			if (insertPstmt != null)
				insertPstmt.close();
		}
		return flag;
	}

}
