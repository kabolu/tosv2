package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.SysConfig;
import edu.must.tos.dao.SysConfigDAO;

public class SysConfigDAOImpl implements SysConfigDAO {

	public boolean updateSysConfig(Connection conn, SysConfig config)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "update tsysconfig set "
					+ "sctype=?, sckey=?, scvalue1=?, scvalue2=?, scchndesc=?, scengdesc=?, actind=?, upduid=?, upddate=?, scvalue3=? "
					+ "where sctype=? and sckey=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, config.getScType());
			pstmt.setString(2, config.getScKey());
			pstmt.setString(3, config.getScValue1());
			pstmt.setString(4, config.getScValue2());
			pstmt.setString(5, config.getScChnDesc());
			pstmt.setString(6, config.getScEngDesc());
			pstmt.setString(7, config.getActInd());
			pstmt.setString(8, config.getUpdUid());
			pstmt.setTimestamp(9, new java.sql.Timestamp(config.getUpdDate().getTime()));
			pstmt.setString(10, config.getScValue3());
			pstmt.setString(11, config.getScType());
			pstmt.setString(12, config.getScKey());
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

	public boolean addSysCnnfig(Connection conn, SysConfig config)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into tsysconfig "
					+ "(sctype, sckey, scvalue1, scvalue2, scchndesc, scengdesc, actind, creuid, credate, upduid, upddate, scvalue3) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, config.getScType());
			pstmt.setString(2, config.getScKey());
			pstmt.setString(3, config.getScValue1());
			pstmt.setString(4, config.getScValue2());
			pstmt.setString(5, config.getScChnDesc());
			pstmt.setString(6, config.getScEngDesc());
			pstmt.setString(7, config.getActInd());
			pstmt.setString(8, config.getCreUid());
			pstmt.setTimestamp(9, new java.sql.Timestamp(config.getCreDate().getTime()));
			pstmt.setString(10, config.getUpdUid());
			pstmt.setTimestamp(11, new java.sql.Timestamp(config.getUpdDate().getTime()));
			pstmt.setString(12, config.getScValue3());
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

	public List getSysConfigList(Connection conn, SysConfig config)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			String sql = "select sctype, sckey, scvalue1, scvalue2, scchndesc, scengdesc, actind, upduid, upddate, scvalue3 "
					+ "from tsysconfig where 1=1 ";
			if (config.getActInd() != null && !config.getActInd().equals("")) {
				sql += "and actind='" + config.getActInd() + "' ";
			}
			if (config.getScType() != null && !config.getScType().equals("")) {
				sql += "and sctype='" + config.getScType() + "' ";
			}
			sql += "order by sckey ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SysConfig conf = new SysConfig();
				conf.setScType(rs.getString("sctype"));
				conf.setScKey(rs.getString("sckey"));
				conf.setScValue1(rs.getString("scvalue1"));
				conf.setScValue2(rs.getString("scvalue2"));
				conf.setScChnDesc(rs.getString("scchndesc"));
				conf.setScEngDesc(rs.getString("scengdesc"));
				conf.setActInd(rs.getString("actind"));
				conf.setUpdDate(rs.getTimestamp("upddate"));
				conf.setUpdUid(rs.getString("upduid"));
				conf.setScValue3(rs.getString("scvalue3"));
				list.add(conf);
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

	public SysConfig getSysConfig(Connection conn, SysConfig config)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		SysConfig sc = null;
		try {
			sql = "select sctype, sckey, scvalue1, scvalue2, scvalue3, scchndesc, scengdesc, upduid, upddate, actind, scvalue3 "
					+ "from tsysconfig where 1=1 ";
			if (config.getScType() != null && !config.getScType().equals("")) {
				sql += "and sctype='" + config.getScType() + "' ";
			}
			if (config.getScKey() != null && !config.getScKey().equals("")) {
				sql += "and sckey='" + config.getScKey() + "' ";
			}
			if (config.getActInd() != null && !config.getActInd().equals("")) {
				sql += "and actind='" + config.getActInd() + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sc = new SysConfig();
				sc.setScType(rs.getString("sctype"));
				sc.setScKey(rs.getString("sckey"));
				sc.setScValue1(rs.getString("scvalue1"));
				sc.setScValue2(rs.getString("scvalue2"));
				sc.setScValue3(rs.getString("scvalue3"));
				sc.setScChnDesc(rs.getString("scchndesc"));
				sc.setScEngDesc(rs.getString("scengdesc"));
				sc.setUpdUid(rs.getString("upduid"));
				sc.setUpdDate(rs.getTimestamp("upddate"));
				sc.setActInd(rs.getString("actind"));
				sc.setScValue3(rs.getString("scvalue3"));
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
		return sc;
	}

	public List getSysScType(Connection conn) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select distinct t1.sctype,t2.scchndesc,t2.scengdesc from tsysconfig t1 "
					+ "left outer join tsysconfig t2 on t2.sctype=t1.sctype "
					+ "where t1.sctype in ('ONSALE','SELRECEIVE','SELPAY') "
					+ "and t1.actind = ? order by t1.sctype";
			// "where t1.sctype in
			// ('RECEIVED','WITHDRAW','SELRECEIVE','SELPAY','PAID','PAID2')
			// " +
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SysConfig sc = new SysConfig();
				sc.setScType(rs.getString("sctype"));
				sc.setScChnDesc(rs.getString("scchndesc"));
				sc.setScEngDesc(rs.getString("scengdesc"));
				list.add(sc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.next();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return list;
	}

	public boolean updateCurrIntake(Connection conn, String newIntake,
			String oldIntake, String userId) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "update tsysconfig set sckey=?,actind=?,upduid=?,upddate=? "
					+ "where sctype=? and scvalue1=? and actind=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oldIntake);
			pstmt.setString(2, "Y");
			pstmt.setString(3, userId);
			pstmt.setTimestamp(4, ts);
			pstmt.setString(5, "INTAKE");
			pstmt.setString(6, oldIntake);
			pstmt.setString(7, "Y");
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				sql = "update tsysconfig set sckey=?,actind=?,upduid=?,upddate=? "
						+ "where sctype=? and scvalue1=? and actind=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "CURRINTAKE");
				pstmt.setString(2, "Y");
				pstmt.setString(3, userId);
				pstmt.setTimestamp(4, ts);
				pstmt.setString(5, "INTAKE");
				pstmt.setString(6, newIntake);
				pstmt.setString(7, "Y");
				int rel = pstmt.executeUpdate();
				if (rel > 0) {
					flag = true;
				}
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

	public boolean addSysIntake(Connection conn, String intake, String userId)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "select 1 from tsysconfig where sctype='INTAKE' and scvalue1='" + intake + "' and actind='Y' ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				sql = "insert into tsysconfig (sctype, sckey, scvalue1, actind, creuid, credate, upduid, upddate) "
						+ "values (?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "INTAKE");
				pstmt.setString(2, intake);
				pstmt.setString(3, intake);
				pstmt.setString(4, "Y");
				pstmt.setString(5, userId);
				pstmt.setTimestamp(6, ts);
				pstmt.setString(7, userId);
				pstmt.setTimestamp(8, ts);
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
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}

	public boolean checkReceiptTime(Connection conn, String intake,
			String fromDate, String toDate, String periodType) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select 1 from tsysconfig t " 
					+ "where sckey=? and sctype=? and actind=? "
					+ "and ? between scvalue1 and scvalue2 and ? between scvalue1 and scvalue2 ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			if (periodType != null && periodType.equals("R")) {
				pstmt.setString(2, "RECEIVED");
			} else if (periodType != null && periodType.equals("P")) {
				pstmt.setString(2, "PAID");
			}
			pstmt.setString(3, "Y");
			pstmt.setString(4, fromDate);
			pstmt.setString(5, toDate);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
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
		return flag;
	}

	/**
	 * check period
	 */
	public String checkPeriod(Connection conn, java.util.Date psDate) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String key = "CURRINTAKE";
		String intake = getCurIntake(conn, key);
		String periodType = "forbid";
		Date date[] = new Date[2];
		try {
			sql = "select scType, scValue1, scValue2 from TSysConfig "
					+ "where scType in ('ONSALE', 'RECEIVED', 'PAID', 'PAID2') and scKey = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, intake);
			rs = ps.executeQuery();
			while (rs.next()) {
				String period = rs.getString("scType");
				date[0] = format.parse(rs.getString("scValue1"));
				date[1] = format.parse(rs.getString("scValue2"));
				if (psDate.after(date[0]) && psDate.before(date[1])) {
					periodType = period;
					break;
				} else if (period.equals("RECEIVED") && psDate.after(date[1])) {
					periodType = "OVERRECEIVED";
					break;
				}
			}
			if (periodType.equals("PAID")) {
				sql = "select sctype, scvalue1, scvalue2 from TSysConfig "
						+ "where sctype = 'RECEIVED' and sckey = ? "
						+ "and ? between to_date(scvalue1, 'yyyy-mm-dd hh24:mi:ss') and to_date(scvalue2, 'yyyy-mm-dd hh24:mi:ss') ";
				ps = conn.prepareStatement(sql);
				ps.setString(1, intake);
				ps.setTimestamp(2, new java.sql.Timestamp(psDate.getTime()));
				rs = ps.executeQuery();
				if (rs.next()) {
					periodType = "RECEIVED_PAID";
				}
			} else if (periodType.equals("PAID2")) {
				sql = "select sctype, scvalue1, scvalue2 from TSysConfig "
						+ "where sctype = 'RECEIVED' and sckey = ? "
						+ "and ? between to_date(scvalue1, 'yyyy-mm-dd hh24:mi:ss') and to_date(scvalue2, 'yyyy-mm-dd hh24:mi:ss') ";
				ps = conn.prepareStatement(sql);
				ps.setString(1, intake);
				ps.setTimestamp(2, new java.sql.Timestamp(psDate.getTime()));
				rs = ps.executeQuery();
				if (rs.next()) {
					periodType = "RECEIVED_PAID2";
				} else {
					sql = "select sctype, scvalue1, scvalue2 from TSysConfig "
							+ "where sctype = 'RECEIVED' and sckey = ? "
							+ "and ? > to_date(scvalue2, 'yyyy-mm-dd hh24:mi:ss') ";
					ps = conn.prepareStatement(sql);
					ps.setString(1, intake);
					ps
							.setTimestamp(2, new java.sql.Timestamp(psDate
									.getTime()));
					rs = ps.executeQuery();
					if (rs.next()) {
						periodType = "OVERRECEIVED_PAID2";
					} else {
						sql = "select sctype, scvalue1, scvalue2 from TSysConfig "
								+ "where sctype = 'ONSALE' and sckey = ? "
								+ "and ? between to_date(scvalue1, 'yyyy-mm-dd hh24:mi:ss') and to_date(scvalue2, 'yyyy-mm-dd hh24:mi:ss') ";
						ps = conn.prepareStatement(sql);
						ps.setString(1, intake);
						ps.setTimestamp(2, new java.sql.Timestamp(psDate
								.getTime()));
						rs = ps.executeQuery();
						if (rs.next()) {
							periodType = "ONSALE_PAID2";
						}
					}
				}
			} else if (periodType.equals("OVERRECEIVED")) {
				sql = "select sctype, scvalue1, scvalue2 from TSysConfig "
						+ "where sctype = 'PAID2' and sckey = ? "
						+ "and ? between to_date(scvalue1, 'yyyy-mm-dd hh24:mi:ss') and to_date(scvalue2, 'yyyy-mm-dd hh24:mi:ss') ";
				ps = conn.prepareStatement(sql);
				ps.setString(1, intake);
				ps.setTimestamp(2, new java.sql.Timestamp(psDate.getTime()));
				rs = ps.executeQuery();
				if (rs.next()) {
					periodType = "OVERRECEIVED_PAID2";
				}
			} else if (periodType.equals("ONSALE")) {
				sql = "select sctype, scvalue1, scvalue2 from tsysconfig "
						+ "where sctype = 'PAID' and sckey = ? "
						+ "and ? between to_date(scvalue1, 'yyyy-mm-dd hh24:mi:ss') and to_date(scvalue2 , 'yyyy-mm-dd hh24:mi:ss')";
				ps = conn.prepareStatement(sql);
				ps.setString(1, intake);
				ps.setTimestamp(2, new java.sql.Timestamp(psDate.getTime()));
				rs = ps.executeQuery();
				if (rs.next()) {
					periodType = "ONSALE_PAID";
				} else {
					sql = "select sctype, scvalue1, scvalue2 from TSysConfig "
							+ "where sctype = 'PAID2' and sckey = ? "
							+ "and ? between to_date(scvalue1, 'yyyy-mm-dd hh24:mi:ss') and to_date(scvalue2, 'yyyy-mm-dd hh24:mi:ss') ";
					ps = conn.prepareStatement(sql);
					ps.setString(1, intake);
					ps.setTimestamp(2, new java.sql.Timestamp(psDate.getTime()));
					rs = ps.executeQuery();
					if (rs.next()) {
						periodType = "ONSALE_PAID2";
					}
				}
			}
		} catch (Exception e) {
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
		return periodType;
	}

	public boolean checkWithdraw(Connection conn, String intake, Date date) throws Exception {
		boolean flag = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date value[] = new Date[2];
			String sql = " select scType, scValue1, scValue2 from TSysConfig " +
					"where scType ='WITHDRAW' and scKey = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, intake);
			rs = ps.executeQuery();
			if (rs.next()) {
				value[0] = format.parse(rs.getString("scValue1"));
				value[1] = format.parse(rs.getString("scValue2"));
				if (date.after(value[0]) && date.before(value[1])) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
		return flag;
	}

	/**
	 * return curIntake
	 */
	public String getCurIntake(Connection conn, String key) {
		String curIntake = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			String sql = "select scValue1 from TSysConfig where scType ='INTAKE' and scKey = '" + key + "' ";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				curIntake = rs.getString("scValue1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return curIntake;
	}

	/**
	 * 
	 * @return language list
	 * @throws SQLException
	 */
	public List getLanguage(Connection conn) {
		List language = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select scKey from TSYSCONFIG where scType=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "LANG");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				language.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return language;
	}

	/**
	 * 
	 * @param lang
	 * @return withdrawPrice
	 * @throws SQLException
	 */
	public Double getWithDrawPrice(Connection conn, String lang)
			throws Exception {
		Double withDrawPrice = 0.0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select scValue1 from TSYSCONFIG where scType=? and scKey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "WITHDRAWFEE");
			pstmt.setString(2, lang);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				withDrawPrice = Double.parseDouble(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return withDrawPrice;
	}

	/**
	 * @param lang
	 * @return
	 * @throws SQLException
	 */
	public boolean getLangResult(Connection conn, String lang) {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select scType, scKey from TSYSCONFIG where scType=? and scKey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "LANG");
			pstmt.setString(2, lang);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public List getSysTime(Connection conn, String intake) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select SCTYPE, SCKEY, SCVALUE1, SCVALUE2, SCCHNDESC, SCENGDESC, CREUID, CREDATE, SCVALUE3 "
					+ "from TSYSCONFIG "
					// "where SCTYPE in
					// ('RECEIVED','WITHDRAW','SELRECEIVE','SELPAY','PAID','PAID2')
					// " +
					+ "where SCTYPE in ('ONSALE', 'SELRECEIVE', 'SELPAY') "
					+ "and SCKEY = ? and ACTIND =? order by SCVALUE1 ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SysConfig sc = new SysConfig();
				sc.setScType(rs.getString("SCTYPE"));
				sc.setScKey(rs.getString("SCKEY"));
				sc.setScValue1(rs.getString("SCVALUE1"));
				sc.setScValue2(rs.getString("SCVALUE2"));
				sc.setScChnDesc(rs.getString("SCCHNDESC"));
				sc.setScEngDesc(rs.getString("SCENGDESC"));
				sc.setUpdUid(rs.getString("CREUID"));
				sc.setCreDate(rs.getDate("CREDATE"));
				sc.setScValue3(rs.getString("SCVALUE3"));
				resultList.add(sc);
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
		return resultList;
	}

	public int getAddConfTime(Connection conn, SysConfig sc) throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		PreparedStatement rateps = null;
		ResultSet rs = null;
		String sql = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "select 1 from TSYSCONFIG where SCTYPE=? and SCKEY=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sc.getScType());
			pstmt.setString(2, sc.getScKey());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				temp = -1;
			} else {
				
				
				sql = "insert into TSYSCONFIG "
						+ "(SCTYPE, SCKEY, SCVALUE1, SCVALUE2, SCCHNDESC, SCENGDESC, ACTIND, CREUID, CREDATE, UPDUID, UPDDATE, SCVALUE3)"
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, sc.getScType());
				pstmt.setString(2, sc.getScKey());
				pstmt.setString(3, sc.getScValue1());
				pstmt.setString(4, sc.getScValue2());
				pstmt.setString(5, sc.getScChnDesc());
				pstmt.setString(6, sc.getScEngDesc());
				pstmt.setString(7, sc.getActInd());
				pstmt.setString(8, sc.getCreUid());
				pstmt.setTimestamp(9, ts);
				pstmt.setString(10, sc.getUpdUid());
				pstmt.setTimestamp(11, ts);
				pstmt.setString(12, sc.getScValue3());
				temp = pstmt.executeUpdate();
				if (temp > 0) {
					temp = 1;
					
					//新增學期，新增初始學期匯率
					/*
					String []rateKey = {"HKD","RMB"};
					String []ratevalue={"0.97","0.76"};
					String []ratedesc = {"MOP 兌換 HKD","MOP 兌換 RMB"};
					for(int i=0;i<rateKey.length;i++){
						rateps = conn.prepareStatement(sql);
						rateps.setString(1, sc.getScKey());
						rateps.setString(2, rateKey[i]);
						rateps.setString(3, ratevalue[i]);
						rateps.setString(4, null);
						rateps.setString(5, ratedesc[i]);
						rateps.setString(6, null);
						rateps.setString(7, sc.getActInd());
						rateps.setString(8, sc.getCreUid());
						rateps.setTimestamp(9, ts);
						rateps.setString(10, sc.getUpdUid());
						rateps.setTimestamp(11, ts);
						rateps.setString(12, sc.getScValue3());
						rateps.executeUpdate();
					}
					*/
				} else {
					temp = 0;
				}
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rateps != null){
				rateps.close();
			}
		}
		return temp;
	}

	public boolean getCheckSysTime(Connection conn, SysConfig sc, String opType)
			throws Exception {
		boolean flag = true;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			List list = null;
			if (opType != null && opType.equals("edit")) {
				//list = getSysTimeButScType(conn, sc);
			} else if (opType != null && opType.equals("add")) {
				list = getSysTime(conn, sc.getScKey());
			}
			if (sc.getScType().equals("ONSALE")) { // onsale period
				if (list!=null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SysConfig dbsc = new SysConfig();
						dbsc = (SysConfig) list.get(i);
						if (dbsc.getScType().equals("RECEIVED")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='RECEIVED' "
									+ "and scvalue1 > '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("SELRECEIVE")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='SELRECEIVE' "
									+ "and scvalue1 >= '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("SELPAY")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='SELPAY' "
									+ "and scvalue1 >= '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("PAID")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='PAID' "
									+ "and scvalue1 >= '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						}
						/**
						 * 
						 * else if(dbsc.getScType().equals("PAID2")){ sql =
						 * "select 1 from tsysconfig t where t.actind='Y' and
						 * t.sckey='"+sc.getScKey()+"' and t.sctype='PAID2' " +
						 * "and t.scvalue1 > '"+sc.getScValue2()+"'"; pstmt =
						 * conn.prepareStatement(sql); rs =
						 * pstmt.executeQuery(); if(!rs.next()){ flag = false;
						 * break; } }
						 */
					}
				}
			} else if (sc.getScType().equals("RECEIVED")) { // received period
				if (list!=null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SysConfig dbsc = new SysConfig();
						dbsc = (SysConfig) list.get(i);
						if (dbsc.getScType().equals("ONSALE")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='ONSALE' "
									+ "and scvalue2 < '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("SELRECEIVE")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='SELRECEIVE' "
									+ "and scvalue2 <= '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("PAID")) {
							sql = "select 1 from tsysconfig where t.actind='Y' and sckey='" + sc.getScKey() + "' and sctype='PAID' "
									+ "and scvalue2 <= '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						}
					}
				}
			} else if (sc.getScType().equals("SELRECEIVE")) { // select received period
				if (list!=null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SysConfig dbsc = new SysConfig();
						dbsc = (SysConfig) list.get(i);
						if (dbsc.getScType().equals("ONSALE")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='ONSALE' "
									+ "and scvalue1 <= '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("RECEIVED")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='RECEIVED' "
									+ "and scvalue2 >= '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						}
					}
				}
			} else if (sc.getScType().equals("SELPAY")) { // select paid period
				if (list !=null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SysConfig dbsc = new SysConfig();
						dbsc = (SysConfig) list.get(i);
						if (dbsc.getScType().equals("ONSALE")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='ONSALE' "
									+ "and scvalue1 <= '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("PAID")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='PAID' "
									+ "and scvalue2 >= '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("RECEIVED")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='RECEIVED' "
									+ "and scvalue2 >= '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("PAID2")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='PAID2' "
									+ "and scvalue1 > '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						}
					}
				}
			} else if (sc.getScType().equals("PAID")) { // paid period
				if (list!=null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SysConfig dbsc = new SysConfig();
						dbsc = (SysConfig) list.get(i);
						if (dbsc.getScType().equals("ONSALE")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='ONSALE' "
									+ "and scvalue1 <= '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("RECEIVED")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='RECEIVED' "
									+ "and scvalue2 >= '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("SELPAY")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='SELPAY' "
									+ "and scvalue2 <= '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("PAID2")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='PAID2' "
									+ "and scvalue1 > '" + sc.getScValue2() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						}
					}
				}
			} else if (sc.getScType().equals("PAID2")) {
				if (list!=null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SysConfig dbsc = new SysConfig();
						dbsc = (SysConfig) list.get(i);
						/**
						 * 
						 * if(dbsc.getScType().equals("ONSALE")){
						 * //System.out.println("ONSALE ===="); sql = "select 1
						 * from tsysconfig t where t.actind='Y' and
						 * t.sckey='"+sc.getScKey()+"' and t.sctype='ONSALE' " +
						 * "and t.scvalue2 < '"+sc.getScValue1()+"'"; pstmt =
						 * conn.prepareStatement(sql); rs =
						 * pstmt.executeQuery(); if(!rs.next()){ flag = false;
						 * break; } }else
						 */
						if (dbsc.getScType().equals("SELPAY")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='SELPAY' "
									+ "and scvalue2 < '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						} else if (dbsc.getScType().equals("PAID")) {
							sql = "select 1 from tsysconfig where actind='Y' and sckey='" + sc.getScKey() + "' and sctype='PAID' "
									+ "and scvalue2 < '" + sc.getScValue1() + "'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							if (!rs.next()) {
								flag = false;
								break;
							}
						}
					}
				}
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
		return flag;
	}

	public List getSysIntake(Connection conn) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "select sctype, scvalue1, scchndesc from tsysconfig "
					+ "where sctype='INTAKE' and actind='Y' " 
					+ "order by scvalue1 ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SysConfig sc = new SysConfig();
				sc.setScType(rs.getString("sctype"));
				sc.setScValue1(rs.getString("scvalue1"));
				sc.setScChnDesc(rs.getString("scchndesc"));
				list.add(sc);
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

}
