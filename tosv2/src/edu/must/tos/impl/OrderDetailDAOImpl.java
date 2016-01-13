package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.DelOrder;
import edu.must.tos.bean.DeliverSummary;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.OrderDetailPrice;
import edu.must.tos.bean.StudReceipt;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.ViewStudOrdInfo;
import edu.must.tos.dao.OrderDetailDAO;

public class OrderDetailDAOImpl implements OrderDetailDAO {

	public int getBefOrderQty(Connection conn, String intake, String isbn, String beforeTime) throws Exception{
		int confirmQty = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select o.orderintake, od.isbn, sum(od.confirmqty) as befConfirmQty "
					   + "from torder o "
					   + "inner join tordetail od on od.orderseqno=o.orderseqno and od.orderintake=o.orderintake and od.actind='Y' "
					   + "where o.actind='Y' and o.orderintake='"+intake+"' and od.isbn='"+isbn+"' and o.paidstatus = 'Y' "
					   + "and od.credate < to_date('"+beforeTime+"', 'yyyy-mm-dd hh24:mi:ss') "
  	   		 	  	   + "group by o.orderintake,od.isbn ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				confirmQty = rs.getInt("befConfirmQty");
			}	   
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null)
				pstmt.close();
		}
		return confirmQty;
	}
	
	public List getOrderDetailByDate(Connection conn, String fromDate,
			String toDate) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select o.orderintake,o.orderseqno,o.paidcurrency,o.paidamount,o.paidstatus,o.chauid,"
					+ "od.isbn,tb.title,od.confirmqty,od.notenoughqty,o.remarks "
					+ "from torder o "
					+ "inner join tordetail od on od.orderseqno=o.orderseqno "
					+ "inner join tbook tb on tb.isbn=od.isbn and tb.actind='Y' "
					+ "where o.actind='Y' ";
			if (fromDate != null && !fromDate.equals("") && toDate != null
					&& !toDate.equals("")) {
				sql += "and to_char(o.credate, 'yyyy-MM-dd') between '"
						+ fromDate + "' and '" + toDate + "' ";
			}
			sql += "and o.studentno='RETAIL' " + "order by o.orderseqno";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrDetail od = new OrDetail();
				od.setOrderIntake(rs.getString("orderintake"));
				od.setOrderSeqNo(rs.getInt("orderseqno"));
				od.setPaidCurrency(rs.getString("paidcurrency"));
				od.setPaidAmount(rs.getDouble("paidamount"));
				od.setPaidStatus(rs.getString("paidstatus"));
				od.setUpdUid(rs.getString("chauid"));
				od.setIsbn(rs.getString("isbn"));
				od.setBookTitle(rs.getString("title"));
				od.setConfirmQty(rs.getInt("confirmqty"));
				if (rs.getObject("notenoughqty") == null) {
					od.setNotEnoughQty(-1);
				} else {
					od.setNotEnoughQty(rs.getInt("notenoughqty"));
				}
				od.setRemarks(rs.getString("remarks"));
				resultList.add(od);
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

	public boolean updateOdBookPrice(Connection conn, List list)
			throws Exception {
		PreparedStatement ps = null;
		boolean flag = true;
		try {
			String sql = "update tordetail set paidamount=?, upduid=?, upddate=? where orderseqno=? and orderintake=? and isbn=? ";
			ps = conn.prepareStatement(sql);
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					OrDetail od = (OrDetail) list.get(i);
					ps.setDouble(1, od.getPaidAmount());
					ps.setString(2, od.getUpdUid());
					ps.setTimestamp(3, new java.sql.Timestamp(od.getUpdDate()
							.getTime()));
					ps.setInt(4, od.getOrderSeqNo());
					ps.setString(5, od.getOrderIntake());
					ps.setString(6, od.getIsbn());
					if (ps.executeUpdate() > 0) {
						flag = true;
					} else {
						flag = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null)
				ps.close();
		}
		return flag;
	}

	public List getRetailReceipt(Connection conn, int orderSeqNo)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select o.studentNo, o.orderintake, o.orderseqno, o.amercemount, tb.title, tb.isbn, "
					+ "tpmop.netprice as mopnetprice, tprmb.netprice as rmbnetprice, "
					+ "tpmop.futureprice as mopfutureprice, tprmb.futureprice as rmbfutureprice, "
					+ "tpmop.withdrawprice as mopwithprice, tprmb.withdrawprice as rmbwithprice, "
					+ "confirmqty, withdrawqty, "
					+ "(case when o.paidcurrency is null then 0 else o.paidamount end) as prepaidmop, "
					+ "(case when o.paidcurrency is null then 0 else o.paidamount end) as prepaidrmb, "
					+ "o.paidcurrency, o.paidstatus, od.withdrawqty2, od.paidamount, o.netpaidamount "
					+ "from torder o "
					+ "inner join tordetail od on o.orderseqno = od.orderseqno and o.orderIntake = od.orderIntake "
					+ "inner join tbook tb on od.isbn = tb.isbn "
					+ "inner join tprice tpmop on tb.isbn = tpmop.isbn and tpmop.currency = 'MOP' and tpmop.intake=o.orderintake "
					+ "inner join tprice tprmb on tb.isbn = tprmb.isbn and tprmb.currency = 'RMB' and tprmb.intake=o.orderintake "
					+ "where o.actInd = 'Y' and od.actInd = 'Y' and tb.actInd = 'Y' ";
			if (orderSeqNo != 0) {
				sql += "and o.orderseqno = " + orderSeqNo + " ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				StudReceipt sr = new StudReceipt();
				sr.setStudentNo(rs.getString("studentNo"));
				sr.setChineseName("RETAIL");
				sr.setEnglishName("RETAIL");
				sr.setProgramName("N/A");
				sr.setOrderIntake(rs.getString("orderIntake"));
				sr.setOrderSeqno(rs.getInt("orderSeqno"));
				sr.setAmercemount(rs.getDouble("amercemount"));
				sr.setTitle(rs.getString("title"));
				sr.setIsbn(rs.getString("isbn"));
				sr.setMopNetPrice(rs.getDouble("mopNetPrice"));
				sr.setRmbNetPrice(rs.getDouble("rmbNetPrice"));
				sr.setMopFuturePrice(rs.getDouble("mopfutureprice"));
				sr.setRmbFuturePrice(rs.getDouble("rmbfutureprice"));
				sr.setMopWithPrice(rs.getDouble("mopWithPrice"));
				sr.setRmbWithPrice(rs.getDouble("rmbWithPrice"));
				sr.setConfirmQty(rs.getInt("confirmQty"));
				sr.setWithDrawQty(rs.getInt("withDrawQty"));
				sr.setPrePaidMop(rs.getDouble("prePaidMop"));
				sr.setPrePaidRmb(rs.getDouble("prePaidRmb"));
				sr.setPaidCurrency(rs.getString("paidcurrency"));
				sr.setPaidStatus(rs.getString("paidstatus"));
				sr.setWithDrawQty2(rs.getInt("withDrawQty2"));
				sr.setPaidAmount(rs.getDouble("paidamount"));
				sr.setNetPaidAmount(rs.getDouble("netpaidamount"));
				resultList.add(sr);
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

	public List getOrDetailInfo(Connection conn, OrDetail od) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select studentno, orderintake, isbn, confirmqty, unconfirmqty, withdrawqty, "
					+ "actind, creuid, credate, upduid, upddate, notenoughqty, coursecode, withdrawqty2, orderseqno, paidamount "
					+ "from tordetail where orderseqno = ? ";
			if(od != null && od.getActInd() != null){
				sql += "and actind = '" + od.getActInd() + "' ";
			}
			if(od != null && od.getIsbn() != null){
				sql += "and isbn = '" + od.getIsbn() + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, od.getOrderSeqNo());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrDetail orDetail = new OrDetail();
				orDetail.setStudentNo(rs.getString("studentno"));
				orDetail.setOrderIntake(rs.getString("orderintake"));
				orDetail.setIsbn(rs.getString("isbn"));
				orDetail.setConfirmQty(rs.getInt("confirmqty"));
				orDetail.setUnconfirmQty(rs.getInt("unconfirmqty"));
				orDetail.setWithdrawQty(rs.getInt("withdrawqty"));
				orDetail.setActInd(rs.getString("actind"));
				orDetail.setCreUid(rs.getString("creuid"));
				orDetail.setCreDate(rs.getTimestamp("credate"));
				orDetail.setUpdUid(rs.getString("upduid"));
				orDetail.setUpdDate(rs.getTimestamp("upddate"));
				if (rs.getObject("notenoughqty") == null) {
					orDetail.setNotEnoughQty(-1);
				} else {
					orDetail.setNotEnoughQty(rs.getInt("notenoughqty"));
				}
				orDetail.setCourseCode(rs.getString("coursecode"));
				orDetail.setWithdrawQty2(rs.getInt("withdrawqty2"));
				orDetail.setOrderSeqNo(rs.getInt("orderseqno"));
				if(rs.getObject("paidamount") != null){
					orDetail.setPaidAmount(rs.getDouble("paidamount"));
				} else {
					orDetail.setPaidAmount(0);
				}
				list.add(orDetail);
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

	public boolean updateWithdrawInfo(Connection conn, List<OrDetail> detailList)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tordetail set confirmqty=?, withdrawqty=?, withdrawqty2=?, notenoughqty=?, upddate=?, upduid=? "
					+ "where orderseqno=? and isbn=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			for (OrDetail od : detailList) {
				pstmt.setInt(1, od.getConfirmQty());
				pstmt.setInt(2, od.getWithdrawQty());
				pstmt.setInt(3, od.getWithdrawQty2());
				if (od.getNotEnoughQty() < 0) {
					pstmt.setObject(4, "");
				} else {
					pstmt.setInt(4, od.getNotEnoughQty());
				}
				pstmt.setTimestamp(5, new java.sql.Timestamp(od.getUpdDate().getTime()));
				pstmt.setString(6, od.getUpdUid());
				pstmt.setInt(7, od.getOrderSeqNo());
				pstmt.setString(8, od.getIsbn());
				pstmt.setString(9, od.getActInd());
				if (pstmt.executeUpdate() > 0) {
					flag = true;
				} else {
					flag = false;
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

	public boolean addDetailSeqNoInfo(Connection conn, List detailList)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null, updatePstmt = null, insertPstmt = null;
		ResultSet rs = null;
		String sql = null, updateSql = null, insertSql = null;
		try {
			sql = "select orderseqno from tordetail where orderseqno=? and isbn=? ";

			updateSql = "update tordetail "
					+ "set confirmqty=?,withdrawqty=?,actind=?,upduid=?,upddate=?,notenoughqty=?,coursecode=?,withdrawqty2=?,majorcode=? "
					+ "where orderseqno=? and isbn=? ";

			insertSql = "insert into tordetail "
					+ "(studentno,orderintake,isbn,confirmqty,actind,creuid,credate,upduid,upddate,coursecode,orderseqno,majorcode) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";

			for (int i = 0; i < detailList.size(); i++) {
				OrDetail detail = (OrDetail) detailList.get(i);
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, detail.getOrderSeqNo());
				pstmt.setString(2, detail.getIsbn());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					updatePstmt = conn.prepareStatement(updateSql);
					updatePstmt.setInt(1, detail.getConfirmQty());
					updatePstmt.setInt(2, detail.getWithdrawQty());
					updatePstmt.setString(3, detail.getActInd());
					updatePstmt.setString(4, detail.getUpdUid());
					updatePstmt.setTimestamp(5, new java.sql.Timestamp(detail.getUpdDate().getTime()));
					updatePstmt.setObject(6, "");
					updatePstmt.setString(7, detail.getCourseCode());
					updatePstmt.setInt(8, detail.getWithdrawQty2());
					updatePstmt.setString(9, detail.getMajorCode());
					updatePstmt.setInt(10, detail.getOrderSeqNo());
					updatePstmt.setString(11, detail.getIsbn());
					int result = updatePstmt.executeUpdate();
					updatePstmt.close();
					if (result > 0) {
						flag = true;
					} else {
						flag = false;
						break;
					}
				} else {
					insertPstmt = conn.prepareStatement(insertSql);
					insertPstmt.setString(1, detail.getStudentNo());
					insertPstmt.setString(2, detail.getOrderIntake());
					insertPstmt.setString(3, detail.getIsbn());
					insertPstmt.setInt(4, detail.getConfirmQty());
					insertPstmt.setString(5, detail.getActInd());
					insertPstmt.setString(6, detail.getCreUid());
					insertPstmt.setTimestamp(7, new java.sql.Timestamp(detail.getCreDate().getTime()));
					insertPstmt.setString(8, detail.getUpdUid());
					insertPstmt.setTimestamp(9, new java.sql.Timestamp(detail.getUpdDate().getTime()));
					insertPstmt.setString(10, detail.getCourseCode());
					insertPstmt.setInt(11, detail.getOrderSeqNo());
					insertPstmt.setString(12, detail.getMajorCode());
					int result = insertPstmt.executeUpdate();
					insertPstmt.close();
					if (result > 0) {
						flag = true;
					} else {
						flag = false;
						break;
					}
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

	public boolean getDetailSeqNoByIsbn(Connection conn, OrDetail detail)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select orderseqno from tordetail where orderseqno=? and isbn=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, detail.getOrderSeqNo());
			pstmt.setString(2, detail.getIsbn());
			rs = pstmt.executeQuery();
			if (rs.next()) {
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

	public boolean insertDetailInfo(Connection conn, OrDetail detail)
			throws Exception {
		boolean flag = false;
		PreparedStatement insertPstmt = null;
		String insertSql = null;
		try {
			insertSql = "insert into tordetail "
					+ "(studentno,orderintake,isbn,confirmqty,actind,creuid,credate,upduid,upddate,coursecode,orderseqno) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?)";

			insertPstmt = conn.prepareStatement(insertSql);
			insertPstmt.setString(1, detail.getStudentNo());
			insertPstmt.setString(2, detail.getOrderIntake());
			insertPstmt.setString(3, detail.getIsbn());
			insertPstmt.setInt(4, detail.getConfirmQty());
			insertPstmt.setString(5, detail.getActInd());
			insertPstmt.setString(6, detail.getCreUid());
			insertPstmt.setTimestamp(7, new java.sql.Timestamp(detail.getCreDate().getTime()));
			insertPstmt.setString(8, detail.getUpdUid());
			insertPstmt.setTimestamp(9, new java.sql.Timestamp(detail.getUpdDate().getTime()));
			insertPstmt.setString(10, detail.getCourseCode());
			insertPstmt.setInt(11, detail.getOrderSeqNo());
			int result = insertPstmt.executeUpdate();
			if (result > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (insertPstmt != null)
				insertPstmt.close();
		}
		return flag;
	}

	public boolean updateDetailInfo(Connection conn, OrDetail detail)
			throws Exception {
		boolean flag = false;
		PreparedStatement updatePstmt = null;
		String updateSql = null;
		try {
			updateSql = "update tordetail "
					+ "set confirmqty=?,withdrawqty=?,actind=?,upduid=?,upddate=?,notenoughqty=?,coursecode=?,withdrawqty2=? "
					+ "where orderseqno=? and isbn=? ";

			updatePstmt = conn.prepareStatement(updateSql);
			updatePstmt.setInt(1, detail.getConfirmQty());
			updatePstmt.setInt(2, detail.getWithdrawQty());
			updatePstmt.setString(3, detail.getActInd());
			updatePstmt.setString(4, detail.getUpdUid());
			updatePstmt.setTimestamp(5, new java.sql.Timestamp(detail.getUpdDate().getTime()));
			updatePstmt.setObject(6, "");
			updatePstmt.setString(7, detail.getCourseCode());
			updatePstmt.setInt(8, detail.getWithdrawQty2());
			updatePstmt.setInt(9, detail.getOrderSeqNo());
			updatePstmt.setString(10, detail.getIsbn());
			int result = updatePstmt.executeUpdate();
			if (result > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (updatePstmt != null)
				updatePstmt.close();
		}
		return flag;
	}

	public boolean updateOrddetailNewStudNo(Connection conn, String intake,
			List updList) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tordetail set studentno=?,upduid=?,upddate=? where studentno=? and orderintake=? ";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < updList.size(); i++) {
				Student stud = (Student) updList.get(i);
				pstmt.setString(1, stud.getStudentNo());
				pstmt.setString(2, "SYSCH");
				pstmt.setTimestamp(3, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.setString(4, stud.getApplicantNo());
				pstmt.setString(5, intake);
				int temp = pstmt.executeUpdate();
				if (temp > 0) {
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

	public int getReceivedBookStatus(Connection conn, OrDetail orDetail)
			throws Exception {
		int notEnoughQty = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select notenoughqty from tordetail "
					+ "where orderseqno=? and orderintake=? and isbn=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orDetail.getOrderSeqNo());
			pstmt.setString(2, orDetail.getOrderIntake());
			pstmt.setString(3, orDetail.getIsbn());
			pstmt.setString(4, orDetail.getActInd());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getObject("notenoughqty") == null) {
					notEnoughQty = -1;
				} else {
					notEnoughQty = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return notEnoughQty;
	}

	public boolean delOrderDetailInfo(Connection conn, OrDetail orDetail)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			sql = "update tordetail set actind=?,upduid=?,upddate=? where orderseqno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orDetail.getActInd());
			pstmt.setString(2, orDetail.getUpdUid());
			pstmt.setTimestamp(3, new java.sql.Timestamp(orDetail.getUpdDate().getTime()));
			pstmt.setInt(4, orDetail.getOrderSeqNo());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}
	
	/**
	 * 更新訂單中圖書的狀態*/
	public boolean updateOrderDetailInfo(Connection conn, OrDetail orDetail)throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			sql = "update tordetail set actind=?,upduid=?,upddate=? where orderseqno=? and isbn = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orDetail.getActInd());
			pstmt.setString(2, orDetail.getUpdUid());
			if(orDetail.getUpdDate()==null){
				orDetail.setUpdDate(new Date());
			}
			pstmt.setTimestamp(3, new java.sql.Timestamp(orDetail.getUpdDate().getTime()));
			pstmt.setInt(4, orDetail.getOrderSeqNo());
			pstmt.setString(5, orDetail.getIsbn());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public boolean changeOrDetailIsbn(Connection conn, OrDetail od, String isbn, String remarks)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		try {
			sql = "update tordetail set isbn=?, remarks=?, upddate=?, upduid=? where isbn=? and orderseqno=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, isbn);
			pstmt.setString(2, remarks);
			pstmt.setTimestamp(3, ts);
			pstmt.setString(4, od.getUpdUid());
			pstmt.setString(5, od.getIsbn());
			pstmt.setInt(6, od.getOrderSeqNo());
			pstmt.setString(7, od.getActInd());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}	

	/**
	 * @param studentNo
	 * @param isbn
	 * @param orderIntake
	 * @param userId
	 * @param con
	 * @return
	 */
	public boolean deleteOrderDetail(int orderSeqNo, String isbn,
			String orderIntake, String userId, Connection con) {
		boolean totalResult = false;
		Statement stmt = null;
		String sql = "update tordetail "
				+ "set confirmqty=0,unconfirmqty=0,actind='N',upduid='" + userId + "',upddate=sysdate " 
				+ "where orderseqno=" + orderSeqNo + " and orderintake ='" + orderIntake + "' and isbn='" + isbn + "' ";
		try {
			stmt = con.createStatement();
			int result = stmt.executeUpdate(sql);
			if (result > 0) {
				totalResult = true;
			}
		} catch (Exception e) {
			totalResult = false;
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalResult;

	}

	/**
	 * 
	 * @param isbn
	 * @return true or false
	 * @throws SQLException
	 */
	public boolean getIsbnInOrDetail(Connection conn, String isbn) {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select 1 from TORDETAIL where isbn=? and actInd=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, isbn);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 
	 * @param isbn
	 * @return ordetail List
	 * @throws SQLException
	 */
	public List getOrDetail(Connection conn, String intake, String isbn, String actind) {
		List ordetailList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select studentno,orderintake,isbn,"
					+ "confirmqty,unconfirmqty,withdrawqty,actind,notenoughqty,coursecode,withdrawqty2,orderseqno,paidamount,upduid "
					+ "from tordetail "
					+ "where orderintake=? and isbn=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, isbn);
			pstmt.setString(3, actind);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrDetail od = new OrDetail();
				od.setStudentNo(rs.getString("studentno"));
				od.setOrderIntake(rs.getString("orderintake"));
				od.setIsbn(rs.getString("isbn"));
				od.setConfirmQty(rs.getInt("confirmqty"));
				od.setUnconfirmQty(rs.getInt("unconfirmqty"));
				od.setWithdrawQty(rs.getInt("withdrawqty"));
				od.setWithdrawQty2(rs.getInt("withdrawqty2"));
				od.setActInd(rs.getString("actind"));
				if (rs.getObject("notenoughqty") == null) {
					od.setNotEnoughQty(-1);
				} else {
					od.setNotEnoughQty(rs.getInt("notenoughqty"));
				}
				od.setCourseCode(rs.getString("coursecode"));
				od.setOrderSeqNo(rs.getInt("orderseqno"));
				od.setPaidAmount(rs.getDouble("paidamount"));
				od.setUpdUid(rs.getString("upduid"));
				ordetailList.add(od);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ordetailList;
	}

	/**
	 * 
	 * @param od
	 * @return number int
	 * @throws SQLException
	 */
	public int getCountOrDetail(Connection conn, OrDetail od) {
		int temp = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = " select count(studentNo) from TORDETAIL "
					+ " where studentNo=? and orderIntake=? and actInd=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, od.getStudentNo());
			pstmt.setString(2, od.getOrderIntake());
			pstmt.setString(3, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				temp = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	/**
	 * 
	 * @param odt
	 * @return 1 int if success
	 * @throws SQLException
	 */
	public int getDelOrdetail(Connection conn, OrDetail odt) {
		int temp = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "update TORDETAIL set actInd=?,updUid=?,updDate=? "
					+ "where studentNo=? and orderIntake=? and isbn=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odt.getActInd());
			pstmt.setString(2, odt.getUpdUid());
			pstmt.setDate(3, new java.sql.Date(odt.getUpdDate().getTime()));
			pstmt.setString(4, odt.getStudentNo());
			pstmt.setString(5, odt.getOrderIntake());
			pstmt.setString(6, odt.getIsbn());
			temp = pstmt.executeUpdate();
			if (temp > 0) {
				temp = 1;
			} else {
				temp = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}
	//之前要更新的ISBN,沒有考慮到tordetail的actind='N'的情況   by xfke
	public List getChangeNoActindBookList(Connection conn, String curIntake,
			String isbn, int confrimQty) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select o.orderseqno, o.studentno, od.isbn, b.title from torder o "
					+ "inner join tordetail od on od.orderseqno=o.orderseqno "
					+ "inner join tbook b on b.isbn=od.isbn and b.actind='Y' "
					+ "where o.actind='Y' and o.studentno<>'RETAIL' and od.notenoughqty is NULL "
					+ "and o.orderintake='" + curIntake + "' ";
			if (isbn != null && !isbn.equals("")) {
				sql += "and od.isbn='" + isbn + "' ";
			}
			if (confrimQty == 1) {
				sql += "and od.confirmqty>0  ";
			}
			sql += "order by o.orderseqno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrDetail od = new OrDetail();
				od.setOrderSeqNo(rs.getInt("orderseqno"));
				od.setStudentNo(rs.getString("studentno"));
				od.setIsbn(rs.getString("isbn"));
				od.setBookTitle(rs.getString("title"));
				list.add(od);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List getChangeBookList(Connection conn, String curIntake, String isbn, 
			String fromDate, String toDate, int confrimQty) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select o.orderseqno, o.studentno, od.isbn, b.title from torder o "
					+ "inner join tordetail od on od.orderseqno=o.orderseqno and od.actind='Y' "
					+ "inner join tbook b on b.isbn=od.isbn and b.actind='Y' "
					+ "where o.actind='Y' and o.studentno<>'RETAIL' and od.notenoughqty is NULL "
					+ "and o.orderintake='" + curIntake + "' ";
			if (isbn != null && !isbn.equals("")) {
				sql += "and od.isbn='" + isbn + "' ";
			}
			if (confrimQty == 1) {
				sql += "and od.confirmqty > 0  ";
			}
			if(fromDate != null && toDate != null){
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				sql += "and o.credate between to_date('"+ fromDate +"', 'yyyy-MM-dd hh24:mi:ss') " +
						"and to_date('"+ toDate +"', 'yyyy-MM-dd hh24:mi:ss') ";
			}
			sql += "order by o.orderseqno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrDetail od = new OrDetail();
				od.setOrderSeqNo(rs.getInt("orderseqno"));
				od.setStudentNo(rs.getString("studentno"));
				od.setIsbn(rs.getString("isbn"));
				od.setBookTitle(rs.getString("title"));
				list.add(od);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * add by jiabo at 2008-12-05 04:11
	 * 
	 * @param conn
	 * @param isbn
	 * @param courseCode
	 * @param curIntake
	 * @return
	 */
	public List getDelOrderList(Connection conn, String isbn,
			String courseCode, String curIntake) {
		Statement st = null;
		ResultSet rs = null;
		List delOrderList = new ArrayList();
		try {
			String sql = " select o.studentNo, b.isbn, b.title, o.orderIntake, o.orderseqno, count(1) "
					+ "from torder o "
					+ "inner join tordetail od on o.orderseqno=od.orderseqno and o.orderIntake=od.orderIntake "
					+ "inner join tbook b on b.isbn = od.isbn "
					+ "where o.orderIntake = '" + curIntake + "' " 
					+ "and o.actind='Y' and od.actind='Y' and b.actind='Y' ";
			if (isbn != null && !isbn.equals("")) {
				sql += "and b.isbn = '" + isbn + "' ";
			}
			if (courseCode != null && !courseCode.equals("")) {
				sql += "and od.courseCode = '" + courseCode + "' ";
			}
			sql += "group by o.studentNo, b.isbn, b.title, o.orderIntake, o.orderseqno order by o.orderseqno ";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				DelOrder delOrder = new DelOrder();
				delOrder.setStudentNo(rs.getString(1));
				delOrder.setIsbn(rs.getString(2));
				delOrder.setTitle(rs.getString(3));
				delOrder.setOrderIntake(rs.getString(4));
				delOrder.setOrderSeqNo(rs.getInt(5));
				delOrder.setCourseBookCount(rs.getInt(6));
				delOrderList.add(delOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return delOrderList;
	}

	/**
	 * add by jiabo at 2008-12-05 04:11
	 * 
	 * @param conn
	 * @param confirmDelOrderList
	 * @param orderIntake
	 * @param userId
	 * @return
	 */
	public boolean deleteOrderDetailList(Connection conn,
			List confirmDelOrderList, String orderIntake, String userId) {
		boolean delResult = false;
		for (int i = 0; i < confirmDelOrderList.size(); i++) {
			DelOrder delOrder = (DelOrder) confirmDelOrderList.get(i);
			delResult = deleteOrderDetail(delOrder.getOrderSeqNo(), delOrder.getIsbn(), orderIntake, userId, conn);
			if (!delResult) {
				break;
			}
		}
		return delResult;
	}

	public boolean getCheckCourseCode(Connection conn, BookRel br)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select o.orderseqno, od.isbn, od.coursecode "
					+ "from torder o "
					+ "inner join tordetail od on od.orderseqno=o.orderseqno and o.orderintake=od.orderintake and od.actind='Y' "
					+ "where o.orderintake='" + br.getIntake()
					+ "' and o.actind='Y' " + "and od.isbn='" + br.getIsbn()
					+ "' and od.coursecode='" + br.getCourseCode() + "'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
		}
		return flag;
	}

	public List getDetailPrice(Connection conn, String intake, int orderSeqNo)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select o.studentno,o.orderseqno,td.isbn,tb.title,td.confirmqty,td.withdrawqty,td.withdrawqty2,td.paidamount,"
					+ "o.paidcurrency,tp.futureprice,tp.netprice,tp.withdrawprice "
					+ "from tordetail td "
					+ "inner join torder o on o.orderseqno=td.orderseqno "
					+ "inner join tbook tb on tb.isbn=td.isbn and tb.actind='Y' "
					+ "inner join tprice tp on tp.isbn=tb.isbn and tp.actind='Y' and tp.intake=td.orderintake "
					+ "where td.orderintake='" + intake + "' and td.actind='Y' "
					+ "and tp.currency='MOP' ";
			if (orderSeqNo != 0) {
				sql += "and o.orderseqno='" + orderSeqNo + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderDetailPrice odp = new OrderDetailPrice();
				odp.setStudentno(rs.getString("studentno"));
				odp.setOrderseqno(rs.getInt("orderseqno"));
				odp.setIsbn(rs.getString("isbn"));
				odp.setTitle(rs.getString("title"));
				odp.setConfirmqty(rs.getInt("confirmqty"));
				odp.setWithdrawqty(rs.getInt("withdrawqty"));
				odp.setWithdrawqty2(rs.getInt("withdrawqty2"));
				odp.setPaidamount(rs.getDouble("paidamount"));
				odp.setPaidcurrency(rs.getString("paidcurrency"));
				odp.setFutureprice(rs.getDouble("futureprice"));
				odp.setNetprice(rs.getDouble("netprice"));
				odp.setWithdrawprice(rs.getDouble("withdrawprice"));
				list.add(odp);
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

	public List getReceiptCount(Connection conn, String studentNo, String intake)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String str[] = studentNo.split(",");
			String studNo = str[0];
			String appNo = str[1];
			String sql = "select orderseqno from torder "
					+ "where (studentNo = ? or studentNo = ?) and orderIntake = ? and paidstatus in (?,?) and actind=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studNo);
			pstmt.setString(2, appNo);
			pstmt.setString(3, intake);
			pstmt.setString(4, "R");
			pstmt.setString(5, "N");
			pstmt.setString(6, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				resultList.add(rs.getInt("orderseqno"));
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

	public List getStudReceiptTest(Connection conn, int orderSeqNo,
			String paidStatus) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select studentNo, orderIntake, orderSeqno, amercemount, title, isbn, mopNetPrice, rmbNetPrice, "
					+ "mopfutureprice, rmbfutureprice, mopWithPrice, rmbWithPrice, confirmQty, withDrawQty,"
					+ "prePaidMop, prePaidRmb, paidmop, currate, paidcurrency, paidstatus, withDrawQty2, bookprice "
					+ "from V_STUDRECEIPTTEST " 
					+ "where orderSeqno = ? ";
			if (paidStatus != null && "Y".equals(paidStatus)) {
				sql += "and paidstatus = ? ";
			} else if (paidStatus != null && "R".equals(paidStatus)) {
				sql += "and paidstatus in (?,?) ";
			}
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setInt(1, orderSeqNo);
			if (paidStatus != null && "Y".equals(paidStatus)) {
				pstmt.setString(2, "Y");
			} else if (paidStatus != null && "R".equals(paidStatus)) {
				pstmt.setString(2, "R");
				pstmt.setString(3, "N");
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				StudReceipt sr = new StudReceipt();
				sr.setStudentNo(rs.getString("studentNo"));
				sr.setOrderIntake(rs.getString("orderIntake"));
				sr.setOrderSeqno(rs.getInt("orderSeqno"));
				sr.setAmercemount(rs.getDouble("amercemount"));
				sr.setTitle(rs.getString("title"));
				sr.setIsbn(rs.getString("isbn"));
				sr.setMopNetPrice(rs.getDouble("mopNetPrice"));
				sr.setRmbNetPrice(rs.getDouble("rmbNetPrice"));
				sr.setMopFuturePrice(rs.getDouble("mopfutureprice"));
				sr.setRmbFuturePrice(rs.getDouble("rmbfutureprice"));
				sr.setMopWithPrice(rs.getDouble("mopWithPrice"));
				sr.setRmbWithPrice(rs.getDouble("rmbWithPrice"));
				sr.setConfirmQty(rs.getInt("confirmQty"));
				sr.setWithDrawQty(rs.getInt("withDrawQty"));
				sr.setPrePaidMop(rs.getDouble("prePaidMop"));
				sr.setPrePaidRmb(rs.getDouble("prePaidRmb"));
				sr.setNetPaidAmount(rs.getDouble("paidmop"));
				sr.setCurRate(rs.getDouble("currate"));
				sr.setPaidCurrency(rs.getString("paidcurrency"));
				sr.setPaidStatus(rs.getString("paidstatus"));
				sr.setWithDrawQty2(rs.getInt("withDrawQty2"));
				sr.setBookPrice(rs.getDouble("bookprice"));
				resultList.add(sr);
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

	public List getStudReceipt(Connection conn, String studentNo,
			String intake, int orderSeqNo) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String str[] = studentNo.split(",");
			String studNo = str[0];
			String appNo = str[1];
			String sql = "select studentNo,chineseName,englishName,status,"
					+ "programname,orderIntake,orderSeqno,amercemount,title,isbn,mopNetPrice,rmbNetPrice,"
					+ "mopfutureprice,rmbfutureprice,mopWithPrice,rmbWithPrice,confirmQty,withDrawQty,"
					+ "prePaidMop,prePaidRmb,paidcurrency,paidstatus,withDrawQty2,bookprice "
					+ "from V_STUDRECEIPT where ";
			if (orderSeqNo == 0) {
				sql += "(studentNo = ? or studentNo = ? ) and orderIntake = ? and paidstatus in (?,?) ";
			} else {
				sql += "orderSeqno = ? and (studentNo = ? or studentNo = ? ) ";
			}
			pstmt = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (orderSeqNo == 0) {
				pstmt.setString(1, studNo);
				pstmt.setString(2, appNo);
				pstmt.setString(3, intake);
				pstmt.setString(4, "R");
				pstmt.setString(5, "N");
			} else {
				pstmt.setInt(1, orderSeqNo);
				pstmt.setString(2, studNo);
				pstmt.setString(3, appNo);
			}
			rs = pstmt.executeQuery();
			if (rs.last()) {
				int rowCount = rs.getRow();
				if (rowCount == 1) {
					rs.beforeFirst();
					while (rs.next()) {
						StudReceipt sr = new StudReceipt();
						sr.setStudentNo(rs.getString("studentNo"));
						sr.setChineseName(rs.getString("chineseName"));
						sr.setEnglishName(rs.getString("englishName"));
						sr.setProgramName(rs.getString("programname"));
						sr.setOrderIntake(rs.getString("orderIntake"));
						sr.setOrderSeqno(rs.getInt("orderSeqno"));
						sr.setAmercemount(rs.getDouble("amercemount"));
						sr.setTitle(rs.getString("title"));
						sr.setIsbn(rs.getString("isbn"));
						sr.setMopNetPrice(rs.getDouble("mopNetPrice"));
						sr.setRmbNetPrice(rs.getDouble("rmbNetPrice"));
						sr.setMopFuturePrice(rs.getDouble("mopfutureprice"));
						sr.setRmbFuturePrice(rs.getDouble("rmbfutureprice"));
						sr.setMopWithPrice(rs.getDouble("mopWithPrice"));
						sr.setRmbWithPrice(rs.getDouble("rmbWithPrice"));
						sr.setConfirmQty(rs.getInt("confirmQty"));
						sr.setWithDrawQty(rs.getInt("withDrawQty"));
						sr.setPrePaidMop(rs.getDouble("prePaidMop"));
						sr.setPrePaidRmb(rs.getDouble("prePaidRmb"));
						sr.setPaidCurrency(rs.getString("paidcurrency"));
						sr.setPaidStatus(rs.getString("paidstatus"));
						sr.setWithDrawQty2(rs.getInt("withDrawQty2"));
						sr.setBookPrice(rs.getDouble("bookprice"));
						resultList.add(sr);
					}
				} else {
					rs.beforeFirst();
					while (rs.next()) {
						StudReceipt sr = new StudReceipt();
						sr.setStudentNo(rs.getString("studentNo"));
						sr.setChineseName(rs.getString("chineseName"));
						sr.setEnglishName(rs.getString("englishName"));
						sr.setProgramName(rs.getString("programname"));
						sr.setOrderIntake(rs.getString("orderIntake"));
						sr.setOrderSeqno(rs.getInt("orderSeqno"));
						sr.setAmercemount(rs.getDouble("amercemount"));
						sr.setTitle(rs.getString("title"));
						sr.setIsbn(rs.getString("isbn"));
						sr.setMopNetPrice(rs.getDouble("mopNetPrice"));
						sr.setRmbNetPrice(rs.getDouble("rmbNetPrice"));
						sr.setMopFuturePrice(rs.getDouble("mopfutureprice"));
						sr.setRmbFuturePrice(rs.getDouble("rmbfutureprice"));
						sr.setMopWithPrice(rs.getDouble("mopWithPrice"));
						sr.setRmbWithPrice(rs.getDouble("rmbWithPrice"));
						sr.setConfirmQty(rs.getInt("confirmQty"));
						sr.setWithDrawQty(rs.getInt("withDrawQty"));
						sr.setPrePaidMop(rs.getDouble("prePaidMop"));
						sr.setPrePaidRmb(rs.getDouble("prePaidRmb"));
						sr.setPaidCurrency(rs.getString("paidcurrency"));
						sr.setPaidStatus(rs.getString("paidstatus"));
						sr.setWithDrawQty2(rs.getInt("withDrawQty2"));
						sr.setBookPrice(rs.getDouble("bookprice"));
						// 防止新生轉學院後出現重複的記錄
						if (!rs.getString("status").equals("C")) {
							resultList.add(sr);
						}
					}
				}
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
	 * add by jiabo for update notEnough Books param
	 * String,String,String[],String[],Connection return boolean
	 */
	public String updateNotEnQty(Connection conn, String curIntake,
			String[] orderSeqNo, String[] isbn, String[] notEnoughQty,
			String userId) {
		PreparedStatement ps = null;
		int result = 0;
		StringBuffer errMsg = new StringBuffer(200);
		errMsg.append("");
		try {
			String sql = " update torDetail set notEnoughQty = ?,updUid = ?,updDate = sysdate "
					+ "where orderseqno = ? and orderIntake = ? and isbn = ? ";
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < isbn.length; i++) {
				try {
					String value = notEnoughQty[i];
					if (value != "") {
						int notEnBooksQty = Integer.parseInt(notEnoughQty[i]);
						ps.setInt(1, notEnBooksQty);
					} else {
						ps.setObject(1, "");
					}
					ps.setString(2, userId);
					ps.setInt(3, Integer.parseInt(orderSeqNo[i]));
					ps.setString(4, curIntake);
					ps.setString(5, isbn[i]);
					result = ps.executeUpdate();
					if (result == 0) {
						errMsg.append("更新失敗");
						break;
					}
				} catch (NumberFormatException n) {
					errMsg.append("輸入的欠書數量格式有誤,必須為整數!");
				}
			}
		} catch (Exception ex) {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					errMsg.append("關閉數據庫資源出錯!");
				}
			}
		}
		return errMsg.toString();
	}
	
	/**
	 * add by xfkey
	 * 通過status，得到已經領1，未2，缺書3訂單
	 * */
	public List getOrderDetailByStatus(Connection conn, String intake, String isbn, String status,
			String fromDate, String toDate) {
		List ordetailList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select td.studentno, td.orderintake, td.isbn, td.confirmqty, td.unconfirmqty, td.withdrawqty, " 
					+ "td.actind, td.notenoughqty, td.coursecode, td.withdrawqty2, td.orderseqno, td.paidamount, o.paidstatus "
					+ "from tordetail td " 
					+ "inner join torder o on td.orderseqno = o.orderseqno and o.actind = 'Y' "
					+ "where td.orderintake=? and td.actind=? ";
			if ( isbn != null && !"".equals(isbn) ){
				sql += "and td.isbn = '" + isbn + "' " ;
			}
			if (status != null && status.equals("1")) {// 已領書
				sql += "and td.notenoughqty = '0' " ;
			} else if (status != null && status.equals("2")) {// 未領書
				sql += "and td.notenoughqty is null and confirmqty > 0 ";
			} else if (status != null && status.equals("3")) {// 缺書
				sql += "and td.notenoughqty > '0' ";
			}
			
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and td.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			sql += "order by studentno ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrDetail od = new OrDetail();
				od.setStudentNo(rs.getString("studentno"));
				od.setOrderIntake(rs.getString("orderintake"));
				od.setIsbn(rs.getString("isbn"));
				od.setConfirmQty(rs.getInt("confirmqty"));
				od.setUnconfirmQty(rs.getInt("unconfirmqty"));
				od.setWithdrawQty(rs.getInt("withdrawqty"));
				od.setWithdrawQty2(rs.getInt("withdrawqty2"));
				od.setActInd(rs.getString("actind"));
				if (rs.getObject("notenoughqty") == null) {
					od.setNotEnoughQty(-1);
				} else {
					od.setNotEnoughQty(rs.getInt("notenoughqty"));
				}
				od.setCourseCode(rs.getString("coursecode"));
				od.setOrderSeqNo(rs.getInt("orderseqno"));
				od.setPaidAmount(rs.getDouble("paidamount"));
				od.setPaidStatus(rs.getString("paidstatus"));
				ordetailList.add(od);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ordetailList;
	}
}
