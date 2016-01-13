package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Withdraw;
import edu.must.tos.bean.WithdrawBean;
import edu.must.tos.dao.WithdrawDAO;

public class WithdrawDAOImpl implements WithdrawDAO {

	public List getWithdrawList(Connection conn, String intake, String from,
			String to, String isbn, String cause) throws Exception {
		String sql = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		List list = new ArrayList();
		try {
			sql = "select t.withdrawno,t.orderseqno,t.studentno,st.chinesename,t.isbn,tb.title,t.withdrawqty," +
					"t.cause,t.orderintake,t.credate,t.creuid "
					+ "from twithdraw t "
					+ "inner join tstudent st on (st.studentno=t.studentno or st.applicantno=t.studentno) "
					+ "inner join tbook tb on tb.isbn=t.isbn "
					+ "where tb.actind='Y' ";
			if (intake != null) {
				sql += "and t.orderintake = '" + intake + "' ";
			}
			if (from != null && !from.equals("") && to != null && !to.equals("")) {
				sql += "and t.credate between to_date('" + from + "', 'yyyy-MM-dd hh24:mi:ss') " +
						"and to_date('" + to + "', 'yyyy-MM-dd hh24:mi:ss') ";
			}
			if (isbn != null && !isbn.equals("")) {
				sql += "and t.isbn = '" + isbn + "' ";
			}
			if (cause != null && !cause.equals("")) {
				sql += "and t.cause = '" + cause + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				WithdrawBean withdraw = new WithdrawBean();
				withdraw.setWhithdrawNo(rs.getInt("withdrawno"));
				withdraw.setOrderSeqNo(rs.getInt("orderseqno"));
				withdraw.setOrderIntake(rs.getString("orderintake"));
				withdraw.setStudentNo(rs.getString("studentno"));
				withdraw.setStudentName(rs.getString("chinesename"));
				withdraw.setIsbn(rs.getString("isbn"));
				withdraw.setBookName(rs.getString("title"));
				withdraw.setWithdrawQty(rs.getInt("withdrawqty"));
				withdraw.setCause(rs.getString("cause"));
				withdraw.setCreDate(rs.getTimestamp("credate"));
				withdraw.setCreUid(rs.getString("creuid"));
				list.add(withdraw);
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

	public boolean addWithdraw(Connection conn, Withdraw withdraw)
			throws Exception {
		String sql = null;
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			sql = "insert into twithdraw (orderintake,orderseqno,studentno,isbn,withdrawqty,cause,credate,creuid) "
					+ "values (?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, withdraw.getOrderIntake());
			pstmt.setInt(2, withdraw.getOrderSeqNo());
			pstmt.setString(3, withdraw.getStudentNo());
			pstmt.setString(4, withdraw.getIsbn());
			pstmt.setInt(5, withdraw.getWithdrawQty());
			pstmt.setString(6, withdraw.getCause());
			pstmt.setTimestamp(7, new java.sql.Timestamp(withdraw.getCreDate().getTime()));
			pstmt.setString(8, withdraw.getCreUid());
			if (pstmt.executeUpdate() > 0) {
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

}
