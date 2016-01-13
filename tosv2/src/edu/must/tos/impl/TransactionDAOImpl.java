package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Transaction;
import edu.must.tos.bean.TransactionOrderSumm;
import edu.must.tos.bean.TransactionSumm;
import edu.must.tos.dao.TransactionDAO;

public class TransactionDAOImpl implements TransactionDAO {

	public List getOrderPaySummList(Connection conn, String intake)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select o.studentno,o.orderseqno, " +
					"sum(decode(t.paidcurrency, 'MOP', t.pay, '')) as MOP, " +
					"sum(decode(t.paidcurrency, 'RMB', t.pay, '')) as RMB " +
					"from torder o " +
					"inner join ttransaction t on t.orderseqno = to_char(o.orderseqno) and t.status='Y' " +
					"where o.orderintake=? and o.actind=? and t.studentno<>'RETAIL' " +
					"group by o.studentno,o.orderseqno ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TransactionOrderSumm ts = new TransactionOrderSumm();
				ts.setStudentNo(rs.getString("studentno"));
				ts.setOrderSeqNo(rs.getString("orderseqno"));
				ts.setMopAmount(rs.getDouble("MOP"));
				ts.setRmbAmount(rs.getDouble("RMB"));
				list.add(ts);
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

	public List getTransactionByItem(Connection conn, List seqNoList, List item, String status)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "select transactionno, orderseqno, cashier, paiddate, paymenttype, pay, paidcurrency, remarks, status, studentno "
					+ "from ttransaction where paymenttype=? and orderseqno=? ";
			if(status != null && !"".equals(status)){
				sql += "and status = '" + status + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			for(int j=0; j<item.size(); j++){
				String itemStr = (String)item.get(j);
				pstmt.setString(1, itemStr);
				for (int i = 0; i < seqNoList.size(); i++) {
					int seqNo = (Integer) seqNoList.get(i);
					pstmt.setString(2, String.valueOf(seqNo));
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Transaction transaction = new Transaction();
						transaction.setTransactionNo(rs.getInt("transactionno"));
						transaction.setOrderSeqNo(rs.getString("orderseqno"));
						transaction.setCashier(rs.getString("cashier"));
						transaction.setPaidDate(rs.getTimestamp("paiddate"));
						transaction.setPaidMentType(rs.getString("paymenttype"));
						transaction.setPay(rs.getDouble("pay"));
						transaction.setPaidCurrency(rs.getString("paidcurrency"));
						transaction.setRemarks(rs.getString("remarks"));
						transaction.setStatus(rs.getString("status"));
						transaction.setStudentNo(rs.getString("studentno"));
						list.add(transaction);
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
		return list;
	}

	public List getTransactionList(Connection conn, String fromDate, String toDate, String orderSeqNo, String items)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "select transactionno, orderseqno, cashier, paiddate, paymenttype, pay, paidcurrency, remarks " +
					"from ttransaction " +
					"where status='Y' and paymenttype <> '收費' ";
			if (fromDate != null && toDate != null) {
				sql += "and paiddate between to_date('"+ fromDate +" 00:00:00', 'yyyy-MM-dd hh24:mi:ss') and to_date('"+ toDate +" 23:59:59', 'yyyy-MM-dd hh24:mi:ss') ";
			}
			if (orderSeqNo != null && !"".equals(orderSeqNo)) {
				sql += "and orderseqno = '"+ orderSeqNo +"' ";
			}
			if(items != null && !"".equals(items)){
				sql += "and paymenttype = '" + items + "' ";
			}
			sql += "order by orderseqno, paiddate desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionNo(rs.getInt("transactionno"));
				transaction.setOrderSeqNo(rs.getString("orderseqno"));
				transaction.setCashier(rs.getString("cashier"));
				transaction.setPaidDate(rs.getTimestamp("paiddate"));
				transaction.setPaidMentType(rs.getString("paymenttype"));
				transaction.setPay(rs.getDouble("pay"));
				transaction.setPaidCurrency(rs.getString("paidcurrency"));
				transaction.setRemarks(rs.getString("remarks"));
				list.add(transaction);
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

	public List getTransSumm(Connection conn, String fromDate, String endDate)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select paidcurrency, sum(pay) as pay from ttransaction "
					+ "where status='Y' " 
					+ "and paiddate between to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, 'yyyy-mm-dd hh24:mi:ss') "
					+ "group by paidcurrency";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TransactionSumm t = new TransactionSumm();
				t.setPaidCurrency(rs.getString("paidcurrency"));
				t.setPay(rs.getDouble("pay"));
				list.add(t);
			}
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

	public List getTransStudPaySumm(Connection conn, String fromDate,
			String endDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.studentno,"
					+ "(select chinesename from tstudent st where (st.studentno=t.studentno or st.applicantno=t.studentno)) as studentname,"
					+ "paidcurrency,sum(pay) as pay "
					+ "from ttransaction t "
					+ "where t.status='Y' and t.paiddate between to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, 'yyyy-mm-dd hh24:mi:ss') "
					+ "group by t.studentno,t.paidcurrency";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TransactionSumm t = new TransactionSumm();
				t.setStudentNo(rs.getString("studentno"));
				t.setStudentName(rs.getString("studentname"));
				t.setPaidCurrency(rs.getString("paidcurrency"));
				t.setPay(rs.getDouble("pay"));
				list.add(t);
			}
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

	public List getTransactionSumm(Connection conn, String fromDate,
			String endDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select to_char(paiddate, 'yyyy-mm-dd') as paiddate, cashier, paidcurrency, sum(pay) as pay, count(paidcurrency) as countNum "
					+ "from ttransaction "
					+ "where status='Y' and paiddate between to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, 'yyyy-mm-dd hh24:mi:ss') "
					+ "group by to_char(paiddate, 'yyyy-mm-dd'),cashier,paidcurrency ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TransactionSumm t = new TransactionSumm();
				t.setPaidDate(rs.getString("paiddate"));
				t.setPaidCurrency(rs.getString("paidcurrency"));
				t.setPay(rs.getDouble("pay"));
				t.setCashier(rs.getString("cashier"));
				t.setCountNum(rs.getInt("countNum"));
				list.add(t);
			}
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

	public List getTransactionInfo(Connection conn, String fromDate,
			String endDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select transactionno, orderseqno, cashier, paiddate, paymenttype, pay, paidcurrency, remarks, studentno, studentname, confirmqty, withdrawqty "
					+ "from v_transactioninfo "
					+ "where paiddate between to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, 'yyyy-mm-dd hh24:mi:ss') ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionNo(rs.getInt("transactionno"));
				transaction.setOrderSeqNo(rs.getString("orderseqno"));
				transaction.setCashier(rs.getString("cashier"));
				transaction.setPaidDate(rs.getTimestamp("paiddate"));
				transaction.setPaidMentType(rs.getString("paymenttype"));
				transaction.setPay(rs.getDouble("pay"));
				transaction.setPaidCurrency(rs.getString("paidcurrency"));
				transaction.setRemarks(rs.getString("remarks"));
				transaction.setStudentNo(rs.getString("studentno"));
				transaction.setChineseName(rs.getString("studentname"));
				transaction.setConfirmQty(rs.getInt("confirmqty"));
				transaction.setWithdrawQty(rs.getInt("withdrawqty"));
				list.add(transaction);
			}
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
	
	public Transaction getTransaction(Connection conn, Integer transactionNo) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Transaction transaction = null;
		try {
			String sql = "select transactionno, orderseqno, cashier, paiddate, paymenttype, pay, paidcurrency, remarks, studentno, status "
					+ "from ttransaction "
					+ "where transactionno = "+ transactionNo +" ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				transaction = new Transaction();
				transaction.setTransactionNo(rs.getInt("transactionno"));
				transaction.setOrderSeqNo(rs.getString("orderseqno"));
				transaction.setCashier(rs.getString("cashier"));
				transaction.setPaidDate(rs.getTimestamp("paiddate"));
				transaction.setPaidMentType(rs.getString("paymenttype"));
				transaction.setPay(rs.getDouble("pay"));
				transaction.setPaidCurrency(rs.getString("paidcurrency"));
				transaction.setRemarks(rs.getString("remarks"));
				transaction.setStudentNo(rs.getString("studentno"));
				transaction.setStatus(rs.getString("status"));
			}
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
		return transaction;
	}

	public boolean addTransactionInfo(Connection conn, Transaction t)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "insert into ttransaction (transactionno,orderseqno,cashier,paiddate,paymenttype,pay,paidcurrency,remarks,studentno,status) "
					+ "values (?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, t.getTransactionNo());
			pstmt.setString(2, t.getOrderSeqNo());
			pstmt.setString(3, t.getCashier());
			pstmt.setTimestamp(4, new java.sql.Timestamp(t.getPaidDate().getTime()));
			pstmt.setString(5, t.getPaidMentType());
			pstmt.setDouble(6, t.getPay());
			pstmt.setString(7, t.getPaidCurrency());
			pstmt.setString(8, t.getRemarks());
			pstmt.setString(9, t.getStudentNo());
			pstmt.setString(10, t.getStatus());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}
	
	public boolean updTransactionInfo(Connection conn, Transaction t)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update ttransaction set " +
					"orderseqno=?, cashier=?, paiddate=?, paymenttype=?, pay=?, paidcurrency=?, remarks=?, studentno=?, status=? " +
					"where transactionno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getOrderSeqNo());
			pstmt.setString(2, t.getCashier());
			pstmt.setTimestamp(3, new java.sql.Timestamp(t.getPaidDate().getTime()));
			pstmt.setString(4, t.getPaidMentType());
			pstmt.setDouble(5, t.getPay());
			pstmt.setString(6, t.getPaidCurrency());
			pstmt.setString(7, t.getRemarks());
			pstmt.setString(8, t.getStudentNo());
			pstmt.setString(9, t.getStatus());
			pstmt.setInt(10, t.getTransactionNo());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}

	public boolean updRecdByOrderSeqNo(Connection conn, Transaction t) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "update ttransaction set " +
					"cashier=?, paiddate=?, pay=?, paidcurrency=?, remarks=?, status=? " +
					"where orderseqno=? and paymenttype=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getCashier());
			pstmt.setTimestamp(2, new java.sql.Timestamp(t.getPaidDate().getTime()));
			pstmt.setDouble(3, t.getPay());
			pstmt.setString(4, t.getPaidCurrency());
			pstmt.setString(5, t.getRemarks());
			pstmt.setString(6, t.getStatus());
			pstmt.setString(7, t.getOrderSeqNo());
			pstmt.setString(8, t.getPaidMentType());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}
	
}
