package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.BookStockOutBean;
import edu.must.tos.bean.BookStockOutOrder;
import edu.must.tos.dao.BookStockOutOrderDAO;

public class BookStockOutOrderDAOImpl implements BookStockOutOrderDAO {

	public BookStockOutOrder getStockOutOrderByPrNum(Connection conn,
			String prNum) throws Exception {
		BookStockOutOrder BookStockOutOrder = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select stockoutno, intake, prnum, supplierno, paidcurrency, outdate, actind, isp "
					+ "from tbookstockoutorder where prnum=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prNum);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				BookStockOutOrder = new BookStockOutOrder();
				BookStockOutOrder.setStockOutNo(rs.getInt("stockoutno"));
				BookStockOutOrder.setIntake(rs.getString("intake"));
				BookStockOutOrder.setPrnum(rs.getString("prnum"));
				BookStockOutOrder.setSupplierNo(rs.getInt("supplierno"));
				BookStockOutOrder.setPaidCurrency(rs.getString("paidcurrency"));
				BookStockOutOrder.setOutDate(rs.getString("outdate"));
				BookStockOutOrder.setActind(rs.getString("actind"));
				BookStockOutOrder.setIsp(rs.getString("isp"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return BookStockOutOrder;
	}

	public List getStockOutListByPrNum(Connection conn, String prnum)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select tbi.isbn, tb.title, tb.publisher, tb.publishyear, tb.edition, tb.author, t.prnum, t.outdate, tbi.remarks, tbi.purchaseprice,"
					+ "tbi.discount, tbi.costprice, tbi.adjnum, tbi.outno, tbi.coursecode, tbi.lectcode "
					+ "from tbookstockoutorder t "
					+ "inner join tbookstockout tbi on tbi.intake=t.intake and tbi.prnum=t.prnum and tbi.actind='Y' "
					+ "inner join tbook tb on tb.isbn=tbi.isbn "
					+ "where t.actind='Y' and tb.actind='Y' and t.prnum=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prnum);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockOutBean bStockOutBean = new BookStockOutBean();
				bStockOutBean.setIsbn(rs.getString("isbn"));
				bStockOutBean.getBook().setTitle(rs.getString("title"));
				bStockOutBean.getBook().setPublisher(rs.getString("publisher"));
				bStockOutBean.getBook().setPublishYear(rs.getString("publishyear"));
				bStockOutBean.getBook().setEdition(rs.getString("edition"));
				bStockOutBean.getBook().setAuthor(rs.getString("author"));
				bStockOutBean.setPrNum(rs.getString("prnum"));
				bStockOutBean.setOutDate(rs.getString("outdate"));
				bStockOutBean.setRemarks(rs.getString("remarks"));
				bStockOutBean.setPurchasePrice(rs.getDouble("purchaseprice"));
				bStockOutBean.setDisCount(rs.getDouble("discount"));
				bStockOutBean.setCostPrice(rs.getDouble("costprice"));
				bStockOutBean.setAdjNum(rs.getInt("adjnum"));
				bStockOutBean.setOutNo(rs.getInt("outno"));
				bStockOutBean.setCourseCode(rs.getString("coursecode"));
				bStockOutBean.setLectCode(rs.getString("lectcode"));
				list.add(bStockOutBean);
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

	public boolean addBookStockOutOrder(Connection conn, BookStockOutOrder o)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		PreparedStatement insertPstmt = null;
		PreparedStatement updatePstmt = null;
		String sql = "";
		ResultSet rs = null;
		try {
			sql = "select prnum from tbookstockoutorder where prnum=? and intake=? and actind=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, o.getPrnum());
			pstmt.setString(2, o.getIntake());
			pstmt.setString(3, o.getActind());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String updSql = "update tbookstockoutorder set upddate=?, upduid=?, supplierno=?, outdate=?, paidcurrency=?, isp=? "
						+ "where prnum=? and intake=? and actind=? ";
				updatePstmt = conn.prepareStatement(updSql);
				updatePstmt.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
				updatePstmt.setString(2, o.getCreUid());
				updatePstmt.setInt(3, o.getSupplierNo());
				updatePstmt.setString(4, o.getOutDate());
				updatePstmt.setString(5, o.getPaidCurrency());
				updatePstmt.setString(6, o.getIsp());
				updatePstmt.setString(7, o.getPrnum());
				updatePstmt.setString(8, o.getIntake());
				updatePstmt.setString(9, o.getActind());
				if (updatePstmt.executeUpdate() > 0) {
					flag = true;
				}
			} else {
				String insertSql = "insert into tbookstockoutorder "
						+ "(intake, prnum, supplierno, outdate, actind, credate, creuid, upddate, upduid, paidcurrency, isp) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?)";
				insertPstmt = conn.prepareStatement(insertSql);
				insertPstmt.setString(1, o.getIntake());
				insertPstmt.setString(2, o.getPrnum());
				insertPstmt.setInt(3, o.getSupplierNo());
				insertPstmt.setString(4, o.getOutDate());
				insertPstmt.setString(5, o.getActind());
				insertPstmt.setTimestamp(6, new java.sql.Timestamp(o.getCreDate().getTime()));
				insertPstmt.setString(7, o.getCreUid());
				insertPstmt.setTimestamp(8, new java.sql.Timestamp(o.getUpdDate().getTime()));
				insertPstmt.setString(9, o.getUpdUid());
				insertPstmt.setString(10, o.getPaidCurrency());
				insertPstmt.setString(11, o.getIsp());
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
	
	public List getStockOutDetailByPrNum(Connection conn, String intake, String prNum, String isp) throws Exception{
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select ts.outno,ts.intake,ts.prnum,ts.isbn,ts.adjnum,ts.purchaseprice,ts.discount,ts.costprice,ts.remarks," +
					"ts.ispdate, tb.title " +
					"from tbookstockoutorder t " +
					"inner join tbookstockout ts on ts.prnum = t.prnum " +
					"inner join tbook tb on tb.isbn = ts.isbn and tb.actind='Y' " +
					"where t.actind = 'Y' and ts.actind = 'Y' ";
			if(intake!=null && !intake.equals("")){
				sql += "and t.intake = '"+intake+"' ";
			}
			if(isp!=null && !isp.equals("")){
				sql += "and t.isp = '"+isp+"' ";
			}
			if(prNum!=null && !prNum.equals("")){
				sql += "and t.prnum = '"+prNum+"' ";
			}
			sql += " order by ts.intake desc, ts.ispdate desc, ts.credate desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BookStockOutBean bean = new BookStockOutBean();
				bean.setOutNo(rs.getInt("outno"));
				bean.setIntake(rs.getString("intake"));
				bean.setPrNum(rs.getString("prnum"));
				bean.setIsbn(rs.getString("isbn"));
				bean.setAdjNum(rs.getInt("adjnum"));
				bean.setPurchasePrice(rs.getDouble("purchaseprice"));
				bean.setDisCount(rs.getDouble("discount"));
				bean.setCostPrice(rs.getDouble("costprice"));
				bean.setRemarks(rs.getString("remarks"));
				bean.setIspDate(rs.getString("ispdate"));
				bean.getBook().setTitle(rs.getString("title"));
				resultList.add(bean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return resultList;
	}
	
	public List getStockOutOrderPrNum(Connection conn, String prNum, String intake)
		throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select stockoutno, intake, prnum, supplierno, paidcurrency, outdate, actind "
					+ "from tbookstockoutorder where actind='Y' "; 
					//edit by rainbow  + "from tbookstockoutorder t where t.actind='Y' and t.isp='N' ";
			if (prNum != null && !prNum.equals("")) {
				sql += "and prnum='" + prNum + "' ";
			}
			if (intake != null && !intake.equals("")) {
				sql += "and intake='" + intake + "' ";
			}
			sql += "order by intake desc, outdate desc, credate desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockOutOrder bookStockOutOrder = new BookStockOutOrder();
				bookStockOutOrder.setStockOutNo(rs.getInt("stockoutno"));
				bookStockOutOrder.setIntake(rs.getString("intake"));
				bookStockOutOrder.setPrnum(rs.getString("prnum"));
				bookStockOutOrder.setSupplierNo(rs.getInt("supplierno"));
				bookStockOutOrder.setPaidCurrency(rs.getString("paidcurrency"));
				bookStockOutOrder.setOutDate(rs.getString("outdate"));
				bookStockOutOrder.setActind(rs.getString("actind"));
				list.add(bookStockOutOrder);
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
	
	public boolean updateStockOutOrder(Connection conn, BookStockOutOrder o)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookstockoutorder set prnum=?, upddate=?, upduid=? where stockoutno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, o.getPrnum());
			pstmt.setTimestamp(2, new java.sql.Timestamp(o.getUpdDate().getTime()));
			pstmt.setString(3, o.getUpdUid());
			pstmt.setInt(4, o.getStockOutNo());
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

}
