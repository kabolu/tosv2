package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.Difference;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Schedule2;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.ViewStudOrdInfo;
import edu.must.tos.dao.OrderDAO;

public class OrderDAOImpl implements OrderDAO {

	/**
	 * 尋找credate前還沒有付款的訂單記錄，並設置狀態為N
	 */
	public int updateOrderActindList(Connection conn, Order order)
			throws Exception {
		int status = 0, result = 0;
		boolean flag = false;
		Schedule2DAOImpl schedule2DAOImpl = new Schedule2DAOImpl();
		PreparedStatement pstmt = null, orderPstmt = null, detailPstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sql = "select orderseqno from torder "
					+ "where orderintake=? and actind=? "
					+ "and credate < to_date(?, 'yyyy-mm-dd hh24:mi:ss') and paidstatus=? ";

			String orderSql = "update torder set actind=?,upduid=?,upddate=? where orderseqno=? ";
			orderPstmt = conn.prepareStatement(orderSql);

			String detailSql = "update tordetail set actind=?,upduid=?,upddate=? where orderseqno=? ";
			detailPstmt = conn.prepareStatement(detailSql);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, order.getOrderIntake());
			pstmt.setString(2, order.getActInd());
			pstmt.setString(3, sdf.format(order.getCreDate()));
			pstmt.setString(4, order.getPaidStatus());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				status = 1;
				int orderSeqNo = rs.getInt("orderseqno");
				orderPstmt.setString(1, "N");
				orderPstmt.setString(2, order.getUpdUid());
				orderPstmt.setTimestamp(3, new java.sql.Timestamp(order.getUpdDate().getTime()));
				orderPstmt.setInt(4, orderSeqNo);
				int orderInt = orderPstmt.executeUpdate();

				detailPstmt.setString(1, "N");
				detailPstmt.setString(2, order.getUpdUid());
				detailPstmt.setTimestamp(3, new java.sql.Timestamp(order.getUpdDate().getTime()));
				detailPstmt.setInt(4, orderSeqNo);
				int detailInt = detailPstmt.executeUpdate();
				
				Schedule2 sc = new Schedule2();
				sc.setActInd("N");
				sc.setUpdDate(new Date());
				sc.setUpdUid(order.getUpdUid());
				sc.setOrderSeqNo(orderSeqNo);
				boolean schedule2Flag = schedule2DAOImpl.updSchedule2(conn, sc);
				
				if (orderInt > 0 && detailInt > 0 && schedule2Flag) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
			if (status != 0) {
				if (flag) {
					result = 1;
				} else {
					result = 2;
				}
			} else {
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (orderPstmt != null)
				orderPstmt.close();
			if (detailPstmt != null)
				detailPstmt.close();
		}
		return result;
	}

	public List getOrderSumm(Connection conn, String curIntake,
			String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select count(orderseqno) as ordernum,count(distinct studentno) as studnum from v_studpaidsumm "
					+ "where academicyear <=1 and orderintake=? "
					//+ "and credate between to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, 'yyyy-mm-dd hh24:mi:ss') " +
					+ "union "
					+ "select count(orderseqno) as ordernum,count(distinct studentno) as studnum from v_studpaidsumm "
					+ "where orderintake=? "; 
					//+ "and credate between to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, 'yyyy-mm-dd hh24:mi:ss') ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, curIntake);
			pstmt.setString(2, curIntake);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int[] num = new int[2];
				num[0] = rs.getInt("ordernum");
				num[1] = rs.getInt("studnum");
				list.add(num);
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
	
	/**
	 * 根據訂單序號和日期範圍條件，搜尋有關零售訂書的記錄
	 */
	public List getRetailOrderList(Connection conn, int orderSeqNo, String fromDate, String toDate) throws Exception{
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select t.studentno,t.orderintake,t.orderseqno,t.paidcurrency,t.paidamount,t.paidstatus," +
					"t.amercemount,t.fineforlatepay,t.difference,t.upddate,t.chauid,t.creuid,t.actind,t.shippingfee,t.netpaidcurrency,t.netpaidamount,t.currate " +
					"from torder t " +
					"where t.actind='Y' ";
			if(orderSeqNo!=0){
				sql += "and t.orderseqno="+orderSeqNo+" ";
			}
			if(fromDate!=null && !fromDate.equals("") && toDate!=null && !toDate.equals("")){
				sql += "and to_char(t.credate, 'yyyy-MM-dd') between '"+fromDate+"' and '"+toDate+"' ";
			}
			sql += "order by t.credate desc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Order order = new Order();
				order.setStudentNo(rs.getString("studentno"));
				order.setOrderIntake(rs.getString("orderintake"));
				order.setOrderSeqNo(rs.getInt("orderseqno"));
				order.setPaidCurrency(rs.getString("paidcurrency"));
				order.setPaidAmount(rs.getDouble("paidamount"));
				order.setPaidStatus(rs.getString("paidstatus"));
				order.setAmerceMount(rs.getDouble("amercemount"));
				order.setFineforlatepay(rs.getDouble("fineforlatepay"));
				order.setDifference(rs.getDouble("difference"));
				order.setUpdDate(rs.getTimestamp("upddate"));
				order.setChaUid(rs.getString("chauid"));
				order.setCreUid(rs.getString("creuid"));
				order.setActInd(rs.getString("actind"));
				order.setShippingFee(rs.getDouble("shippingfee"));
				order.setNetpaidcurrency(rs.getString("netpaidcurrency"));
				order.setNetpaidamount(rs.getDouble("netpaidamount"));
				order.setCurrate(rs.getDouble("currate"));
				resultList.add(order);
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

	/**
	 * 根據訂單序號OrderSeqNo搜尋訂單記錄
	 */
	public Order getOrderBySeqNo(Connection conn, int orderSeqNo)
			throws Exception {
		Order order = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.studentno,t.orderintake,t.orderseqno,t.paidcurrency,t.paidamount,t.paidstatus,"
					+ "t.amercemount,t.fineforlatepay,t.difference,t.upddate,t.chauid,t.creuid,t.actind,t.shippingfee,t.netpaidcurrency,t.netpaidamount,t.currate "
					+ "from torder t where t.orderseqno=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orderSeqNo);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				order = new Order();
				order.setStudentNo(rs.getString("studentno"));
				order.setOrderIntake(rs.getString("orderintake"));
				order.setOrderSeqNo(rs.getInt("orderseqno"));
				order.setPaidCurrency(rs.getString("paidcurrency"));
				order.setPaidAmount(rs.getDouble("paidamount"));
				order.setPaidStatus(rs.getString("paidstatus"));
				order.setAmerceMount(rs.getDouble("amercemount"));
				order.setFineforlatepay(rs.getDouble("fineforlatepay"));
				order.setDifference(rs.getDouble("difference"));
				order.setUpdDate(rs.getTimestamp("upddate"));
				order.setChaUid(rs.getString("chauid"));
				order.setCreUid(rs.getString("creuid"));
				order.setActInd(rs.getString("actind"));
				order.setShippingFee(rs.getDouble("shippingfee"));
				order.setNetpaidcurrency(rs.getString("netpaidcurrency"));
				order.setNetpaidamount(rs.getDouble("netpaidamount"));
				order.setCurrate(rs.getDouble("currate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return order;
	}

	/**
	 * 根據studParam，包括學生編號和申請編號搜尋符合條件的訂單記錄
	 */
	public List<Order> getAllOrderSeqNoInfo(Connection conn, String studParam,
			String intake, String actind) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String studNo = null;
			String appNo = null;
			if (studParam != null) {
				String no[] = studParam.split(",");
				studNo = no[0];
				appNo = no[1];
			}
			sql = "select studentno, orderintake, orderseqno, paidcurrency, paidamount, paidstatus, actind, "
					+ "amercemount, chauid, fineforlatepay, difference, credate, shippingfee, netpaidcurrency, netpaidamount, currate "
					+ "from torder where orderintake=? and actind=? ";
			if(studParam != null){
				sql += "and (studentno=? or studentno=?) ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, actind);
			if(studParam != null){
				pstmt.setString(3, studNo);
				pstmt.setString(4, appNo);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Order o = new Order();
				o.setStudentNo(rs.getString("studentno"));
				o.setOrderIntake(rs.getString("orderintake"));
				o.setOrderSeqNo(rs.getInt("orderseqno"));
				o.setPaidCurrency(rs.getString("paidcurrency"));
				o.setPaidAmount(rs.getDouble("paidamount"));
				o.setPaidStatus(rs.getString("paidstatus"));
				o.setActInd(rs.getString("actind"));
				o.setAmerceMount(rs.getDouble("amercemount"));
				o.setChaUid(rs.getString("chauid"));
				o.setFineforlatepay(rs.getDouble("fineforlatepay"));
				o.setDifference(rs.getDouble("difference"));
				o.setCreDate(rs.getTimestamp("credate"));
				o.setShippingFee(rs.getDouble("shippingfee"));
				o.setNetpaidcurrency(rs.getString("netpaidcurrency"));
				o.setNetpaidamount(rs.getDouble("netpaidamount"));
				o.setCurrate(rs.getDouble("currate"));
				resultList.add(o);
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
	 * 增加訂書記錄
	 */
	public synchronized int addOrderSeqNoInfo(Connection conn, Order order)
			throws Exception {
		int result = 0;
		PreparedStatement pstmt = null, queryPstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "insert into torder "
					+ "(studentno,orderintake,paidcurrency,paidamount,paidstatus,actind,creuid,credate,upduid,upddate,amercemount,fineforlatepay," 
					+ "remarks,netpaidcurrency,netpaidamount,currate) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, order.getStudentNo());
			pstmt.setString(2, order.getOrderIntake());
			pstmt.setString(3, order.getPaidCurrency());
			pstmt.setDouble(4, order.getPaidAmount());
			pstmt.setString(5, order.getPaidStatus());
			pstmt.setString(6, order.getActInd());
			pstmt.setString(7, order.getCreUid());
			pstmt.setTimestamp(8, new java.sql.Timestamp(order.getCreDate().getTime()));
			pstmt.setString(9, order.getUpdUid());
			pstmt.setTimestamp(10, new java.sql.Timestamp(order.getUpdDate().getTime()));
			pstmt.setDouble(11, order.getAmerceMount());
			pstmt.setDouble(12, order.getFineforlatepay());
			pstmt.setString(13, order.getRemarks());
			pstmt.setString(14, order.getNetpaidcurrency());
			pstmt.setDouble(15, order.getNetpaidamount());
			pstmt.setDouble(16, order.getCurrate());
			if (pstmt.executeUpdate() > 0) {
				sql = "select qorderno.currval as orderseqno from dual";
				queryPstmt = conn.prepareStatement(sql);
				rs = queryPstmt.executeQuery();
				if (rs.next()) {
					result = rs.getInt("orderseqno");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (queryPstmt != null)
				queryPstmt.close();
		}
		return result;
	}

	public Order getPaidNOrder(Connection conn, String studentNo, String intake)
			throws Exception {
		Order order = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.studentno,t.orderintake,t.orderseqno,t.paidcurrency,t.paidamount,t.paidstatus,"
					+ "t.amercemount,t.fineforlatepay,t.difference,t.upddate,t.chauid,t.creuid,t.actind "
					+ "from torder t "
					+ "where t.studentno=? and t.paidstatus=? and t.orderintake=? and t.actind=? "
					//+ "and t.paidcurrency is not null 
					+ "";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studentNo);
			pstmt.setString(2, "N");
			pstmt.setString(3, intake);
			pstmt.setString(4, "Y");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				order = new Order();
				order.setStudentNo(rs.getString("studentno"));
				order.setOrderIntake(rs.getString("orderintake"));
				order.setOrderSeqNo(rs.getInt("orderseqno"));
				order.setPaidCurrency(rs.getString("paidcurrency"));
				order.setPaidAmount(rs.getDouble("paidamount"));
				order.setPaidStatus(rs.getString("paidstatus"));
				order.setAmerceMount(rs.getDouble("amercemount"));
				order.setFineforlatepay(rs.getDouble("fineforlatepay"));
				order.setDifference(rs.getDouble("difference"));
				order.setUpdDate(rs.getTimestamp("upddate"));
				order.setChaUid(rs.getString("chauid"));
				order.setCreUid(rs.getString("creuid"));
				order.setActInd(rs.getString("actind"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return order;
	}

	public Order getPaidNOrderInfo(Connection conn, String studentNo,
			String intake) throws Exception {
		Order order = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.studentno,t.orderintake,t.orderseqno,t.paidcurrency,t.paidamount,t.paidstatus,"
					+ "t.amercemount,t.fineforlatepay,t.difference,t.upddate,t.chauid,t.creuid,t.actind "
					+ "from torder t "
					+ "where t.studentno=? and t.paidstatus=? and t.orderintake=? and t.actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studentNo);
			pstmt.setString(2, "N");
			pstmt.setString(3, intake);
			pstmt.setString(4, "Y");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				order = new Order();
				order.setStudentNo(rs.getString("studentno"));
				order.setOrderIntake(rs.getString("orderintake"));
				order.setOrderSeqNo(rs.getInt("orderseqno"));
				order.setPaidCurrency(rs.getString("paidcurrency"));
				order.setPaidAmount(rs.getDouble("paidamount"));
				order.setPaidStatus(rs.getString("paidstatus"));
				order.setAmerceMount(rs.getDouble("amercemount"));
				order.setFineforlatepay(rs.getDouble("fineforlatepay"));
				order.setDifference(rs.getDouble("difference"));
				order.setUpdDate(rs.getTimestamp("upddate"));
				order.setChaUid(rs.getString("chauid"));
				order.setCreUid(rs.getString("creuid"));
				order.setActInd(rs.getString("actind"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return order;
	}

	public boolean updateOrdNewStudNo(Connection conn, String intake,
			List updList) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update torder set studentno=?,upduid=?,upddate=? where studentno=? and orderintake=? ";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < updList.size(); i++) {
				Student stud = (Student) updList.get(i);
				pstmt.setString(1, stud.getStudentNo());
				pstmt.setString(2, "SYSCH");
				pstmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
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

	public boolean updateOrderBySeqNo(Connection conn, Order order)
			throws Exception {
		boolean flag = true;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			sql = "update torder set "
					+ "paidamount=?,paidcurrency=?,paidstatus=?,upduid=?,upddate=?,"
					+ "amercemount=?,chauid=?,fineforlatepay=?,difference=?,actind=?,shippingfee=?,netpaidcurrency=?,netpaidamount=?,currate=? "
					+ "where orderseqno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, order.getPaidAmount());
			pstmt.setString(2, order.getPaidCurrency());
			pstmt.setString(3, order.getPaidStatus());
			pstmt.setString(4, order.getUpdUid());
			pstmt.setTimestamp(5, new java.sql.Timestamp(order.getUpdDate().getTime()));
			pstmt.setDouble(6, order.getAmerceMount());
			pstmt.setString(7, order.getChaUid());
			pstmt.setDouble(8, order.getFineforlatepay());
			pstmt.setDouble(9, order.getDifference());			
			pstmt.setString(10, order.getActInd());
			pstmt.setDouble(11, order.getShippingFee());
			pstmt.setString(12, order.getNetpaidcurrency());
			pstmt.setDouble(13, order.getNetpaidamount());
			pstmt.setDouble(14, order.getCurrate());
			pstmt.setInt(15, order.getOrderSeqNo());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				flag = true;
			}else{
				flag = false;
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

	public Order getNewsOrderInfo(Connection conn, String studentNo,
			String orderIntake) throws Exception {
		Order order = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.studentno,t.orderintake,t.orderseqno,t.paidcurrency,t.paidamount,t.paidstatus,"
					+ "t.amercemount,t.fineforlatepay,t.difference,t.upddate,t.chauid,t.creuid,t.credate,t.actind "
					+ "from torder t "
					+ "where t.studentno=? and t.orderintake=? and actind=? and creuid=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studentNo);
			pstmt.setString(2, orderIntake);
			pstmt.setString(3, "Y");
			pstmt.setString(4, "NEWSTUD");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				order = new Order();
				order.setStudentNo(rs.getString("studentno"));
				order.setOrderIntake(rs.getString("orderintake"));
				order.setOrderSeqNo(rs.getInt("orderseqno"));
				order.setPaidCurrency(rs.getString("paidcurrency"));
				order.setPaidAmount(rs.getDouble("paidamount"));
				order.setPaidStatus(rs.getString("paidstatus"));
				order.setAmerceMount(rs.getDouble("amercemount"));
				order.setFineforlatepay(rs.getDouble("fineforlatepay"));
				order.setDifference(rs.getDouble("difference"));
				order.setUpdDate(rs.getTimestamp("upddate"));
				order.setChaUid(rs.getString("chauid"));
				order.setCreUid(rs.getString("creuid"));
				order.setCreDate(rs.getTimestamp("credate"));
				order.setActInd(rs.getString("actind"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return order;
	}

	/**
	 * 
	 * @param od
	 * @return
	 * @throws SQLException
	 */
	public int getDelOrder(Connection conn, Order od) throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "update TORDER set actInd=?,updUid=?,updDate=? "
					+ "where studentNo=? and orderIntake=? ";
			if(od!=null && od.getOrderSeqNo()!=0){
				sql += "and orderseqno = "+od.getOrderSeqNo()+" ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, od.getActInd());
			pstmt.setString(2, od.getUpdUid());
			pstmt.setDate(3, new java.sql.Date(od.getUpdDate().getTime()));
			pstmt.setString(4, od.getStudentNo());
			pstmt.setString(5, od.getOrderIntake());
			temp = pstmt.executeUpdate();
			if (temp > 0) {
				temp = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return temp;
	}

	/**
	 * @param conn
	 * @param studentNo
	 * @param orderIntake
	 * @param paidCurrency
	 * @param paidAmount
	 * @param updUid
	 * @return update result boolean
	 */
	public boolean updateOrder(Connection conn, int orderSeqNo,
			String orderIntake, String paidCurrency, float paidAmount,
			float amercePrice, String updUid, double shippingFee, String netPaidCurrency, float netPaidAmount, double curRate) {
		boolean totalResult = true;
		PreparedStatement ps = null;
		try {
			String sql = "update torder "
					+ "set paidCurrency=?, paidStatus='Y', paidAmount=?, fineforlatepay=?, chaUid=?, updUid=?, "
					+ "updDate=sysdate, shippingFee=?, netPaidCurrency=?, netPaidAmount=?, curRate=? "
					+ "where orderseqno=? and orderIntake = ? and actInd = 'Y' ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, paidCurrency);
			ps.setFloat(2, paidAmount);
			ps.setFloat(3, amercePrice);
			ps.setString(4, updUid);
			ps.setString(5, updUid);
			ps.setDouble(6, shippingFee);			
			ps.setString(7, netPaidCurrency);
			ps.setFloat(8, netPaidAmount);
			ps.setDouble(9, curRate);
			ps.setInt(10, orderSeqNo);
			ps.setString(11, orderIntake);
			int result = ps.executeUpdate();
			if (result == 0) {
				totalResult = false;
			}
		} catch (Exception e) {
			totalResult = false;
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return totalResult;
	}

	/**
	 * @param conn
	 * @param studentNo
	 * @param orderIntake
	 * @return
	 */
	public boolean isPaying(Connection conn, int orderSeqNo, String orderIntake) {
		boolean totalResult = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = " select paidstatus from torder "
					+ "where orderSeqNo=? and orderIntake = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, orderSeqNo);
			ps.setString(2, orderIntake);
			rs = ps.executeQuery();
			if (rs.next()) {
				String paidStatus = rs.getString("paidstatus");
				if (paidStatus.equals("Y")) {
					totalResult = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalResult;
	}

	public List statisticStudOrdInfo(Connection conn, String intakeList,
			String faculty, String program, String academicYear,
			String studType, String reportType, String fromDate, String toDate,
			String fromReceiveDate, String toReceiveDate, String fromOrderDate,
			String toOrderDate) throws Exception {
		List list = new ArrayList();
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			if (reportType != null && reportType.equals("summary")) {
				sql = "select ts.email,tc.email as newEmail,ts.contactno,tc.contactno as newContactno, t.chineseName,t.studentNo,t.applicantNo,t.facultyCode,t.faChnName,t.programCode,t.progChnName,t.academicYear,t.orderSeqNo,"
						+ "t.orderIntake,t.amercemount,t.fineforlatepay,t.difference,t.totalQty,t.totalFutPriceMOP,t.totalFutPriceRMB,t.totalNetPriceMOP,t.totalNetPriceRMB,"
						+ "t.totalWithdrawPriceMop,t.totalWithdrawPriceRmb,"
						+ "t.paidCurrency,t.paidAmount,t.paidDate,t.cashier,t.totalNotEngQty,t.totalNotEngPriceMOP,t.totalNotEngPriceRMB,t.shippingFee,t.credate "
						+ "from cbps_order_summary t "
						+ "left join tstudent ts on ts.studentno=t.studentno "
						+ "left join tcontact tc on tc.studentno=t.studentno "
						+ " where 1=1 ";
				if (intakeList != null && !intakeList.equals("")) {
					sql += "and t.orderIntake='" + intakeList + "' ";
				}
				if (faculty != null && !faculty.equals("")) {
					sql += "and t.facultyCode='" + faculty + "' ";
				}
				if (program != null && !program.equals("")) {
					sql += "and t.programCode='" + program + "' ";
				}
				if (studType != null && studType.equals("new")) {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					} else {
						sql += "and t.academicYear<'2' ";
					}
				} else if (studType != null && studType.equals("old")) {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					} else {
						sql += "and t.academicYear>='2' ";
					}
				} else if (studType != null && studType.equals("all")) {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					}
				} else {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					}
				}
				if (!fromDate.equals("") && toDate.equals("")) {
					sql += "and t.paiddate > to_date('" + fromDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (fromDate.equals("") && !toDate.equals("")) {
					sql += "and t.paiddate < to_date('" + toDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (!fromDate.equals("") && !toDate.equals("")) {
					sql += "and t.paiddate " + "between to_date('" + fromDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('"
							+ toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (!fromOrderDate.equals("") && !toOrderDate.equals("")) {
					sql += "and t.credate between to_date('" + fromOrderDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('"
							+ toOrderDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				sql += "order by t.facultycode,t.academicyear,t.programcode,t.orderseqno asc";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ViewStudOrdInfo info = new ViewStudOrdInfo();
					info.setChineseName(rs.getString("chineseName"));
					info.setEmail(rs.getString("email"));
					info.setNewEmail(rs.getString("newEmail"));
					info.setContactNo(rs.getString("contactno"));
					info.setNewContactNo(rs.getString("newContactno"));
					info.setStudentNo(rs.getString("studentNo"));
					info.setApplicantNo(rs.getString("applicantNo"));
					info.setFacultyName(rs.getString("faChnName"));
					info.setProgram(rs.getString("progChnName"));
					info.setAcademicYear(rs.getInt("academicYear"));
					info.setOrderSeqNo(rs.getString("orderSeqNo"));
					info.setAmercemount(rs.getDouble("amercemount"));
					info.setFineforlatepay(rs.getDouble("fineforlatepay"));
					info.setDifference(rs.getDouble("difference"));
					info.setTotalQty(rs.getInt("totalQty"));
					info.setTotalFutPriceMOP(rs.getDouble("totalFutPriceMOP"));
					info.setTotalFutPriceRMB(rs.getDouble("totalFutPriceRMB"));
					info.setTotalNetPriceMOP(rs.getDouble("totalNetPriceMOP"));
					info.setTotalNetPriceRMB(rs.getDouble("totalNetPriceRMB"));
					info.setTotalWithdrawPriceMop(rs.getDouble("totalWithdrawPriceMop"));
					info.setTotalWithdrawPriceRmb(rs.getDouble("totalWithdrawPriceRmb"));
					info.setPaidCurrency(rs.getString("paidCurrency"));
					info.setPaidAmount(rs.getDouble("paidAmount"));
					info.setPaidDate(rs.getTimestamp("paidDate"));
					info.setCashier(rs.getString("cashier"));
					info.setTotalNotEngQty(rs.getInt("totalNotEngQty"));
					info.setTotalNotEngPriceMOP(rs.getDouble("totalNotEngPriceMOP"));
					info.setTotalNotEngPriceRMB(rs.getDouble("totalNotEngPriceRMB"));
					info.setShippingFee(rs.getDouble("shippingFee"));
					resultList.add(info);
				}
			} else if (reportType != null && reportType.equals("detail")) {
				sql = "select t.email,tc.email as newEmail,t.contactno,tc.contactno as newContactno, t.chinesename,t.studentno,t.applicantno,t.facultycode,t.fachnname,"
						+ "t.programcode,t.progchnname,t.academicyear,t.orderseqno,t.orderintake,"
						+ "t.title,t.author,t.publisher,t.edition,t.publishyear,t.isbn,t.orderedqty,"
						+ "t.notenoughqty,t.futurepricemop,t.futurepricermb,t.withdrawpricemop,t.withdrawpricermb, "
						+ "t.payfuturepricemop,t.payfuturepricermb,t.paynetpricemop,t.paynetpricermb,t.netpricemop,t.netpricermb,"
						+ "t.paidcurrency,t.paidamount,t.paiddate,t.cashier,t.upddate,t.upduid,t.withdrawqty2,t.credate "
						+ "from cbps_order_detail t " 
						+ "left join tcontact tc on tc.studentno=t.studentno "
						+ "where 1=1 ";
				if (intakeList != null && !intakeList.equals("")) {
					sql += "and t.orderIntake='" + intakeList + "' ";
				}
				if (faculty != null && !faculty.equals("")) {
					sql += "and t.facultyCode='" + faculty + "' ";
				}
				if (program != null && !program.equals("")) {
					sql += "and t.programCode='" + program + "' ";
				}
				if (studType != null && studType.equals("new")) {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					} else {
						sql += "and t.academicYear<'2' ";
					}
				} else if (studType != null && studType.equals("old")) {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					} else {
						sql += "and t.academicYear>='2' ";
					}
				} else if (studType != null && studType.equals("all")) {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					}
				} else {
					if (academicYear != null && !academicYear.equals("")) {
						sql += "and t.academicYear='" + academicYear + "' ";
					}
				}
				if (!fromDate.equals("") && toDate.equals("")) {
					sql += "and t.paiddate > to_date('" + fromDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (fromDate.equals("") && !toDate.equals("")) {
					sql += "and t.paiddate < to_date('" + toDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (!fromDate.equals("") && !toDate.equals("")) {
					sql += "and t.paiddate " + "between to_date('" + fromDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('"
							+ toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (!fromReceiveDate.equals("") && !toReceiveDate.equals("")) {
					sql += "and t.upddate between to_date('" + fromReceiveDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('"
							+ toReceiveDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
					sql += "and t.notenoughqty is not null ";
				}
				if (!fromOrderDate.equals("") && !toOrderDate.equals("")) {
					sql += "and t.credate between to_date('" + fromOrderDate
							+ "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('"
							+ toOrderDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				sql += "order by t.facultycode,t.academicyear,t.programcode,t.orderseqno asc";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ViewStudOrdInfo info = new ViewStudOrdInfo();
					info.setChineseName(rs.getString("chinesename"));
					info.setEmail(rs.getString("email"));
					info.setNewEmail(rs.getString("newEmail"));
					info.setContactNo(rs.getString("contactno"));
					info.setNewContactNo(rs.getString("newContactno"));
					info.setStudentNo(rs.getString("studentno"));
					info.setApplicantNo(rs.getString("applicantno"));
					info.setFacultyName(rs.getString("fachnname"));
					info.setProgram(rs.getString("progchnname"));
					info.setAcademicYear(rs.getInt("academicyear"));
					info.setOrderSeqNo(rs.getString("orderseqno"));
					info.setTitle(rs.getString("title"));
					info.setAuthor(rs.getString("author"));
					info.setPublisher(rs.getString("publisher"));
					info.setEdition(rs.getString("edition"));
					info.setPublishYear(rs.getString("publishyear"));
					info.setIsbn(rs.getString("isbn"));
					info.setOrderEdQty(rs.getInt("orderedqty"));
					if (rs.getObject("notenoughqty") == null) {
						info.setNotEnoughQty(-1);
					} else {
						info.setNotEnoughQty(rs.getInt("notenoughqty"));
					}
					info.setFuturePriceMOP(rs.getDouble("futurepricemop"));
					info.setFuturePriceRMB(rs.getDouble("futurepricermb"));
					info.setWithdrawpriceMOP(rs.getDouble("withdrawpricemop"));
					info.setWithdrawpriceRMB(rs.getDouble("withdrawpricermb"));
					info.setPayfuturepricemop(rs.getDouble("payfuturepricemop"));
					info.setPayfuturepricermb(rs.getDouble("payfuturepricermb"));
					info.setPaynetpricemop(rs.getDouble("paynetpricemop"));
					info.setPaynetpricermb(rs.getDouble("paynetpricermb"));
					info.setNetPriceMOP(rs.getDouble("netpricemop"));
					info.setNetPriceRMB(rs.getDouble("netpricermb"));
					info.setPaidCurrency(rs.getString("paidcurrency"));
					info.setPaidAmount(rs.getDouble("paidamount"));
					info.setPaidDate(rs.getTimestamp("paiddate"));
					info.setCashier(rs.getString("cashier"));
					info.setUpddate(rs.getTimestamp("upddate"));
					info.setUpduid(rs.getString("upduid"));
					info.setWithdrawqty2(rs.getInt("withdrawqty2"));
					resultList.add(info);
				}
			} else if (reportType != null && reportType.equals("summary_only")) {
				sql = "select orderseqno, orderintake, amercemount, fineforlatepay, difference, totalqty, totalfutpricemop, "
						+ "totalfutpricermb, totalnetpricemop, totalnetpricermb, totalwithdrawpricemop, totalwithdrawpricermb, "
						+ "paidcurrency, paidamount, paiddate, cashier, totalnotengqty, totalnotengpricemop, totalnotengpricermb, shippingfee "
						+ "from CBPS_ORDER_SUMMARY_ONLY where 1=1 ";
				if (intakeList != null && !intakeList.equals("")) {
					sql += "and orderintake='" + intakeList + "' ";
				}
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ViewStudOrdInfo info = new ViewStudOrdInfo();
					info.setOrderSeqNo(rs.getString("orderseqno"));
					info.setAmercemount(rs.getDouble("amercemount"));
					info.setFineforlatepay(rs.getDouble("fineforlatepay"));
					info.setDifference(rs.getDouble("difference"));
					info.setTotalQty(rs.getInt("totalqty"));
					info.setTotalFutPriceMOP(rs.getDouble("totalfutpricemop"));
					info.setTotalFutPriceRMB(rs.getDouble("totalfutpricermb"));
					info.setTotalNetPriceMOP(rs.getDouble("totalnetpricemop"));
					info.setTotalNetPriceRMB(rs.getDouble("totalnetpricermb"));
					info.setTotalWithdrawPriceMop(rs.getDouble("totalwithdrawpricemop"));
					info.setTotalWithdrawPriceRmb(rs.getDouble("totalwithdrawpricermb"));
					info.setPaidCurrency(rs.getString("paidcurrency"));
					info.setPaidAmount(rs.getDouble("paidamount"));
					info.setPaidDate(rs.getTimestamp("paiddate"));
					info.setCashier(rs.getString("cashier"));
					info.setTotalNotEngQty(rs.getInt("totalnotengqty"));
					info.setTotalNotEngPriceMOP(rs.getDouble("totalnotengpricemop"));
					info.setTotalNotEngPriceRMB(rs.getDouble("totalnotengpricermb"));
					info.setShippingFee(rs.getDouble("shippingfee"));
					resultList.add(info);
				}
			}
			list.add(resultList);
			list.add(sql);
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

	public ViewStudOrdInfo getCbpsOrderInfo(Connection conn, String studNo,
			String orderIntake) throws Exception {
		ViewStudOrdInfo info = null;
		ResultSet rs = null;
		String sql = null;
		PreparedStatement pstmt = null;
		try {
			sql = "select chineseName,studentNo,applicantNo,facultyCode,faChnName,programCode,progChnName,academicYear,orderSeqNo,"
					+ "orderIntake,amercemount,fineforlatepay,totalQty,totalFutPriceMOP,totalFutPriceRMB,totalNetPriceMOP,totalNetPriceRMB,"
					+ "totalWithdrawPriceMop,totalWithdrawPriceRmb,"
					+ "paidCurrency,paidAmount,paidDate,cashier,totalNotEngQty,totalNotEngPriceMOP,totalNotEngPriceRMB "
					+ "from cbps_order_summary t where (studentNo=? or applicantno=?) and orderIntake=? "
					+ "order by t.facultycode,t.academicyear,t.programcode,t.orderseqno asc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studNo);
			pstmt.setString(2, studNo);
			pstmt.setString(3, orderIntake);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				info = new ViewStudOrdInfo();
				info.setChineseName(rs.getString("chineseName"));
				info.setStudentNo(rs.getString("studentNo"));
				info.setApplicantNo(rs.getString("applicantNo"));
				info.setFacultyName(rs.getString("faChnName"));
				info.setProgram(rs.getString("progChnName"));
				info.setAcademicYear(rs.getInt("academicYear"));
				info.setOrderSeqNo(rs.getString("orderSeqNo"));
				info.setAmercemount(rs.getDouble("amercemount"));
				info.setFineforlatepay(rs.getDouble("fineforlatepay"));
				info.setTotalQty(rs.getInt("totalQty"));
				info.setTotalFutPriceMOP(rs.getDouble("totalFutPriceMOP"));
				info.setTotalFutPriceRMB(rs.getDouble("totalFutPriceRMB"));
				info.setTotalNetPriceMOP(rs.getDouble("totalNetPriceMOP"));
				info.setTotalNetPriceRMB(rs.getDouble("totalNetPriceRMB"));
				info.setTotalWithdrawPriceMop(rs.getDouble("totalWithdrawPriceMop"));
				info.setTotalWithdrawPriceRmb(rs.getDouble("totalWithdrawPriceRmb"));
				info.setPaidCurrency(rs.getString("paidCurrency"));
				info.setPaidAmount(rs.getDouble("paidAmount"));
				info.setPaidDate(rs.getTimestamp("paidDate"));
				info.setCashier(rs.getString("cashier"));
				info.setTotalNotEngQty(rs.getInt("totalNotEngQty"));
				info.setTotalNotEngPriceMOP(rs.getDouble("totalNotEngPriceMOP"));
				info.setTotalNotEngPriceRMB(rs.getDouble("totalNotEngPriceRMB"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return info;
	}

	public boolean updateOrderDifference(Connection conn, String orderIntake,
			List<Difference> dealList, String userId) throws Exception {
		boolean flag = true;
		String sql = null;
		PreparedStatement pstmt = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "update torder set "
					+ "paidamount = ?, upddate = ?, upduid = ?, chauid = ?, difference = ?, paidstatus = ?, netpaidamount = ? "
					+ "where orderseqno = ? and orderintake = ? ";
			pstmt = conn.prepareStatement(sql);
			for (Difference d : dealList) {
				if (d.getDifference() != 0 
						|| ("R".equals(d.getPaidStatus()) && d.getDifference()!=0)
						|| ("Y".equals(d.getPaidStatus()) && d.getDifference()!=0) ) {
					pstmt.setDouble(1, d.getShoulePayAmount());
					pstmt.setTimestamp(2, ts);
					pstmt.setString(3, userId);
					pstmt.setString(4, userId);
					pstmt.setDouble(5, d.getDifferenceMop());
					pstmt.setString(6, "Y");
					pstmt.setDouble(7, d.getShouldPayMopAmount());
					pstmt.setInt(8, d.getOrderSeqNo());
					pstmt.setString(9, orderIntake);
					int temp = pstmt.executeUpdate();
					if (temp > 0) {
						flag = true;
					} else {
						flag = false;
						break;
					}
				} else if (d.getDifference() == 0 && "N".equals(d.getPaidStatus())){
					pstmt.setDouble(1, d.getShoulePayAmount());
					pstmt.setTimestamp(2, ts);
					pstmt.setString(3, userId);
					pstmt.setString(4, userId);
					pstmt.setDouble(5, d.getDifferenceMop());
					pstmt.setString(6, "Y");
					pstmt.setDouble(7, d.getShouldPayMopAmount());
					pstmt.setInt(8, d.getOrderSeqNo());
					pstmt.setString(9, orderIntake);
					int temp = pstmt.executeUpdate();
					if (temp > 0) {
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
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public boolean setPaidStatusN(Connection conn, int orderSeqNo,
			String curIntake) throws Exception {
		boolean flag = false;
		String sql = null;
		PreparedStatement pstmt = null;
		try {

			boolean isPaying = this.isPaying(conn, orderSeqNo, curIntake);
			if (isPaying) {
				sql = "update torder set paidstatus=? "
						+ "where orderSeqNo=? and orderintake=? and actind=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "R");
				pstmt.setInt(2, orderSeqNo);
				pstmt.setString(3, curIntake);
				pstmt.setString(4, "Y");
				int temp = pstmt.executeUpdate();
				if (temp > 0) {
					flag = true;
				}
			} else {
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
