package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookStockOut;
import edu.must.tos.bean.BookStockOutBean;
import edu.must.tos.dao.BookStockOutDAO;

public class BookStockOutDAOImpl implements BookStockOutDAO {

	public List getBookStockOurReport(Connection conn, String intake, String beginDate, String toDate, String prnum, String supplier)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select t3.supplierno,t3.suppliername,t3.supplierengname,tb.isbn,title,author,publisher,publishyear,edition," +
					"sum(adjnum) as quantity,sum(adjnum*costprice) as amount " +
					"from tbookstockoutorder t1 " +
					"inner join tbookstockout t2 on t2.prnum=t1.prnum and t2.actind='Y' " +
					"inner join tbooksupplier t3 on t3.supplierno = t1.supplierno and t3.actind='Y' " +
					"inner join tbook tb on tb.isbn=t2.isbn and tb.actind='Y' " +
					"where t1.actind = 'Y' " ;
					if(intake != null && !"".equals(intake)){
						sql += "and t1.intake = '"+intake+"' ";
					}
					if(beginDate!=null && !beginDate.equals("") && toDate!=null && !toDate.equals("")){
						sql += "and (t1.outdate between '"+beginDate+"' and '"+toDate+"' " +
								"or t2.ispdate between '"+beginDate+"' and '"+toDate+"' )";
					}
					if(prnum!=null && !prnum.equals("")){
						sql += "and t1.prnum = '"+prnum+"' ";
					}
					if(supplier!=null && !supplier.equals("")){
						sql += "and t1.supplierno = '"+supplier+"' ";
					}
					sql += "group by t3.supplierno,t3.suppliername,t3.supplierengname,tb.isbn,title,author,publisher,publishyear,edition ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BookStockOutBean bean = new BookStockOutBean();
				bean.setSupplierNo(rs.getInt("supplierno"));
				bean.setSupplierName(rs.getString("suppliername"));
				bean.setSupplierEngName(rs.getString("supplierengname"));
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishyear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setAmount(rs.getDouble("amount"));
				list.add(bean);
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

	public BookStockOut getBookStockOut(Connection conn, BookStockOut bStockOut)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BookStockOut bookStockOut = null;
		try {
			String sql = "select outno, intake, prnum, isbn, credate, creuid, adjnum, purchaseprice, discount, costprice, remarks, " +
					"upddate, upduid, ispdate, purchaseid, coursecode, lectcode "
					+ "from tbookstockout where actind='Y' ";
			if(bStockOut.getOutNo() != null){
				sql += "and outno = "+ bStockOut.getOutNo() +" ";
			}
			if (bStockOut.getIntake() != null) {
				sql += "and intake='" + bStockOut.getIntake() + "' ";
			}
			if (bStockOut.getIsbn() != null) {
				sql += "and isbn='" + bStockOut.getIsbn() + "' ";
			}
			if (bStockOut.getPRnum() != null) {
				sql += "and prnum='" + bStockOut.getPRnum() + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bookStockOut = new BookStockOut();
				bookStockOut.setOutNo(rs.getInt("outno"));
				bookStockOut.setIntake(rs.getString("intake"));
				bookStockOut.setPRnum(rs.getString("prnum"));
				bookStockOut.setIsbn(rs.getString("isbn"));
				bookStockOut.setCredate(rs.getTimestamp("credate"));
				bookStockOut.setCreuid(rs.getString("creuid"));
				bookStockOut.setAdjnum(rs.getInt("adjnum"));
				bookStockOut.setPurchasePrice(rs.getDouble("purchaseprice"));
				bookStockOut.setDiscount(rs.getDouble("discount"));
				bookStockOut.setCostPrice(rs.getDouble("costprice"));
				bookStockOut.setUpddate(rs.getTimestamp("upddate"));
				bookStockOut.setUpduid(rs.getString("upduid"));
				bookStockOut.setIspDate(rs.getString("ispdate"));
				bookStockOut.setPurchaseId(rs.getInt("purchaseid"));
				bookStockOut.setCourseCode(rs.getString("coursecode"));
				bookStockOut.setLectCode(rs.getString("lectcode"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return bookStockOut;
	}

	public List getStockOutInfoList(Connection conn, String intake, String prNum,
			String fromDate, String toDate, String paidStatus, int supplierNo)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select (case when ts.suppliername is null then ts.supplierengname else ts.suppliername end) as supplierName, "
					+ "t.intake, t.prnum, t.outdate, t.paidcurrency, "
					+ "tbi.isbn, tb.title, tb.author, tb.publisher, tb.publishyear, tb.edition, "
					+ "tbi.purchaseprice, tbi.discount, tbi.costprice, tbi.adjnum, tbi.remarks, t.isp, tbi.ispdate, tbi.coursecode, tbi.lectcode "
					+ "from tbookstockoutorder t "
					+ "inner join tbookstockout tbi on tbi.intake=t.intake and tbi.prnum=t.prnum and tbi.actind='Y' "
					+ "inner join tbook tb on tb.isbn=tbi.isbn "
					+ "left outer join tbooksupplier ts on ts.supplierno = t.supplierno and ts.actind='Y' "
					+ "where t.actind='Y' and tb.actind='Y' ";
			if (intake != null && !"".equals(intake)) {
				sql += "and t.intake = '" + intake + "' ";
			}
			if (prNum != null && !"".equals(prNum)) {
				sql += "and t.prnum = '" + prNum + "' ";
			}
			if (fromDate != null && toDate != null) {
				sql += "and (t.outdate between '" + fromDate + "' and '" + toDate + "' " +
						"or tbi.ispdate between '" + fromDate + "' and '" + toDate + "' )";
			}
			if (paidStatus != null) {
				sql += "and t.paidstatus = '" + paidStatus + "' ";
			}
			if (supplierNo != 0) {
				sql += "and ts.supplierno = " + supplierNo + " ";
			}
			sql += "order by t.intake,ts.supplierno,t.prnum,tbi.isbn,t.outdate ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockOutBean bookStockOutBean = new BookStockOutBean();
				bookStockOutBean.setSupplierName(rs.getString("supplierName"));
				bookStockOutBean.setIntake(rs.getString("intake"));
				bookStockOutBean.setPrNum(rs.getString("prnum"));
				bookStockOutBean.setOutDate(rs.getString("outdate"));
				bookStockOutBean.setPaidCurrency(rs.getString("paidcurrency"));
				bookStockOutBean.setIsbn(rs.getString("isbn"));
				bookStockOutBean.getBook().setTitle(rs.getString("title"));
				bookStockOutBean.getBook().setAuthor(rs.getString("author"));
				bookStockOutBean.getBook().setPublisher(rs.getString("publisher"));
				bookStockOutBean.getBook().setPublishYear(rs.getString("publishYear"));
				bookStockOutBean.getBook().setEdition(rs.getString("edition"));
				bookStockOutBean.setPurchasePrice(rs.getDouble("purchaseprice"));
				bookStockOutBean.setDisCount(rs.getDouble("discount"));
				bookStockOutBean.setCostPrice(rs.getDouble("costprice"));
				bookStockOutBean.setAdjNum(rs.getInt("adjnum"));
				bookStockOutBean.setRemarks(rs.getString("remarks"));
				bookStockOutBean.setIsp(rs.getString("isp"));
				bookStockOutBean.setIspDate(rs.getString("ispdate"));
				bookStockOutBean.setCourseCode(rs.getString("coursecode"));
				bookStockOutBean.setLectCode(rs.getString("lectcode"));
				list.add(bookStockOutBean);
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

	public boolean updateBookStockOut(Connection conn, BookStockOut bStockOut)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookstockout set purchaseprice=?, discount=?, costprice=?, adjnum=?, remarks=?, actind=?, upddate=?, upduid=?, coursecode=?, lectcode=? "
					+ "where prnum=? and isbn=? and outno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, bStockOut.getPurchasePrice());
			pstmt.setDouble(2, bStockOut.getDiscount());
			pstmt.setDouble(3, bStockOut.getCostPrice());
			pstmt.setInt(4, bStockOut.getAdjnum());
			pstmt.setString(5, bStockOut.getRemarks());
			pstmt.setString(6, bStockOut.getActind());
			pstmt.setTimestamp(7, new java.sql.Timestamp(bStockOut.getUpddate().getTime()));
			pstmt.setString(8, bStockOut.getUpduid());
			pstmt.setString(9, bStockOut.getCourseCode());
			pstmt.setString(10, bStockOut.getLectCode());
			pstmt.setString(11, bStockOut.getPRnum());
			pstmt.setString(12, bStockOut.getIsbn());
			pstmt.setInt(13, bStockOut.getOutNo());
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

	public boolean updateBookStockOutByNo(Connection conn, BookStockOut bStockOut)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		try {
			sql = "update tbookstockout set prnum = ?, isbn = ?, purchaseprice = ?, discount = ?, costprice = ?, adjnum = ?, remarks = ?, actind = ?, "+
					"upddate = ?, upduid = ?, coursecode = ?, lectcode = ? " +
					"where outno = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bStockOut.getPRnum());
			pstmt.setString(2, bStockOut.getIsbn());
			pstmt.setDouble(3, bStockOut.getPurchasePrice());
			pstmt.setDouble(4, bStockOut.getDiscount());
			pstmt.setDouble(5, bStockOut.getCostPrice());
			pstmt.setInt(6, bStockOut.getAdjnum());
			pstmt.setString(7, bStockOut.getRemarks());
			pstmt.setString(8, bStockOut.getActind());
			pstmt.setTimestamp(9, ts);
			pstmt.setString(10, bStockOut.getUpduid());
			pstmt.setString(11, bStockOut.getCourseCode());
			pstmt.setString(12, bStockOut.getLectCode());
			pstmt.setInt(13, bStockOut.getOutNo());
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
	
	public boolean insertBookStockOutList(Connection conn, List<BookStockOut> list)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "insert into tbookstockout " +
					"(intake,prnum,isbn,adjnum,remarks,outno,purchaseprice,discount,costprice,actind,upddate,upduid,ispdate,purchaseid,coursecode,lectcode) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			for (BookStockOut stockOut : list) {
				pstmt.setString(1, stockOut.getIntake());
				pstmt.setString(2, stockOut.getPRnum());
				pstmt.setString(3, stockOut.getIsbn());
				pstmt.setInt(4, stockOut.getAdjnum());
				pstmt.setString(5, stockOut.getRemarks());
				pstmt.setInt(6, 0);
				pstmt.setDouble(7, stockOut.getPurchasePrice());
				pstmt.setDouble(8, stockOut.getDiscount());
				pstmt.setDouble(9, stockOut.getCostPrice());
				pstmt.setString(10, stockOut.getActind());
				pstmt.setTimestamp(11, new java.sql.Timestamp(stockOut.getUpddate().getTime()));
				pstmt.setString(12, stockOut.getUpduid());
				pstmt.setString(13, stockOut.getIspDate());
				pstmt.setInt(14, stockOut.getPurchaseId());
				pstmt.setString(15, stockOut.getCourseCode());
				pstmt.setString(16, stockOut.getLectCode());
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

	public boolean updateStockOutPrNum(Connection conn, String prNum,
			String oldPrNum) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookstockout set prNum=? where prnum=? and actind=? ";
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

	public List getStockOutList(Connection conn, BookStockOut bookStockOut)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select outno, intake, prnum, isbn, credate, creuid, adjnum, remarks, purchaseprice, discount, costprice, upddate, upduid, coursecode, lectcode " +
					"from tbookstockout where actind = 'Y' ";
			if (bookStockOut.getIntake() != null && !bookStockOut.getIntake().equals("")) {
				sql += "and intake = '" + bookStockOut.getIntake() + "' ";
			}
			if (bookStockOut.getPRnum() != null && !bookStockOut.getPRnum().equals("")) {
				sql += "and prnum = '" + bookStockOut.getPRnum() + "' ";
			}
			if (bookStockOut.getIsbn() != null && !bookStockOut.getIsbn().equals("")) {
				sql += "and isbn = '" + bookStockOut.getIsbn() + "' ";
			}
			sql += "order by credate desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookStockOut bStockOut = new BookStockOut();
				bStockOut.setOutNo(rs.getInt("outno"));
				bStockOut.setIntake(rs.getString("intake"));
				bStockOut.setPRnum(rs.getString("prnum"));
				bStockOut.setIsbn(rs.getString("isbn"));
				bStockOut.setCredate(rs.getTimestamp("credate"));
				bStockOut.setCreuid(rs.getString("creuid"));
				bStockOut.setAdjnum(rs.getInt("adjnum"));
				bStockOut.setRemarks(rs.getString("remarks"));
				bStockOut.setPurchasePrice(rs.getDouble("purchaseprice"));
				bStockOut.setDiscount(rs.getDouble("discount"));
				bStockOut.setCostPrice(rs.getDouble("costprice"));
				bStockOut.setUpddate(rs.getTimestamp("upddate"));
				bStockOut.setUpduid(rs.getString("upduid"));
				bStockOut.setCourseCode(rs.getString("coursecode"));
				bStockOut.setLectCode(rs.getString("lectcode"));
				list.add(bStockOut);
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
	
	public List getStockOutbySupplierCode(Connection conn, String supplierCode)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select prnum from tbookstockout where actind='Y' ";
			if (supplierCode != null && !supplierCode.equals("")) {
				sql += "and prnum like '" + supplierCode + "%' ";
			}			
			sql += "order by prnum desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {				
				list.add(rs.getString("prnum"));
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
