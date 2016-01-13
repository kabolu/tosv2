package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import edu.must.tos.bean.Period;
import edu.must.tos.bean.StudReceivePeriodRecord;
import edu.must.tos.dao.PeriodDAO;

public class PeriodDAOImpl implements PeriodDAO {

	java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());

	public LinkedHashMap getStudReceOrPaidPeriodMap(Connection conn, String intake)
			throws Exception {
		LinkedHashMap map = new LinkedHashMap();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			sql = "select sch2.studentno,sch2.intake,sch2.periodno,sch2.type,per.starttime,per.endtime "
					+ "from tschedule2 sch2 "
					+ "inner join tperiod per on per.periodno=sch2.periodno "
					+ "where sch2.actind=? and sch2.intake=? " +
					"order by sch2.studentno,sch2.intake,sch2.type ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("studentno")+"_"+rs.getString("type");
				String value = format.format(rs.getTimestamp("starttime")) + "-" + format.format(rs.getTimestamp("endtime")).substring(10, 19);
				map.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return map;
	}

	public Period getStudReceiveTime(Connection conn, String studNo,
			String intake) throws Exception {
		Period period = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select sch2.studentno,per.periodno,per.intake,per.starttime,per.endtime,per.type "
					+ "from tschedule2 sch2 "
					+ "inner join tperiod per on per.periodno=sch2.periodno "
					+ "where sch2.actind=? and sch2.studentno=? and sch2.intake=? and per.type=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, studNo);
			pstmt.setString(3, intake);
			pstmt.setString(4, "R");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				period = new Period();
				period.setPeriodNo(rs.getInt("periodno"));
				period.setIntake(rs.getString("intake"));
				period.setStartTime(rs.getTimestamp("starttime"));
				period.setEndTime(rs.getTimestamp("endtime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return period;
	}

	public int getMaxNo(Connection conn, String periodNo) throws Exception {
		int maxNo = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select count(per.periodno) as stud_number,"
					+ "per.periodno,per.starttime,per.endtime,per.type,per.maxno,"
					+ "per.maxno-count(per.periodno) as leaving_number "
					+ "from tperiod per "
					+ "inner join tschedule2 sch2 on sch2.periodno = per.periodno "
					+ "inner join tstudent stud on stud.studentno=sch2.studentno "
					+ "inner join tfaculty fac  on fac.facultycode=stud.facultycode "
					+ "where per.periodno=? "
					+ "group by per.periodno,per.starttime,per.endtime,per.type,per.maxno order by per.starttime";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, periodNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxNo = rs.getInt("stud_number");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return maxNo;
	}

	public List getReceivePeriodLeavings(Connection conn, String intake,
			String fromDate, String toDate, String periodType) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		String sql = null;
		List list = new ArrayList();
		try {
			sql = "select sch2.receivenum as stud_number,"
					+ "per.periodno, per.starttime, per.endtime, per.type,per.maxno, sch2.receivenum as received_number,"
					+ "per.maxno - sch2.receivenum as leaving_number "
					+ "from tperiod per "
					+ "left outer join ( select count(periodno) as receivenum, periodno, intake, type from tschedule2 " 
					+ "where actind = 'Y' "
					+ "group by periodno, intake, type ) sch2 on sch2.periodno = per.periodno and sch2.type = per.type and sch2.intake = per.intake "
					+ "where 1=1 ";
			if (periodType != null && !periodType.equals("")) {
				sql += "and per.type='" + periodType + "' ";
			}
			if (intake != null && !intake.equals("")) {
				sql += "and per.intake='" + intake + "' ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and per.starttime between to_date('" + fromDate + "','yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "','yyyy-mm-dd hh24:mi:ss') " +
						"and per.endtime between " + "to_date('" + fromDate + "','yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "','yyyy-mm-dd hh24:mi:ss') ";
			}
			sql += "order by per.starttime ";
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				StudReceivePeriodRecord record = new StudReceivePeriodRecord();
				record.setCount(rs.getInt("stud_number"));
				record.setPeriodNo(rs.getString("periodno"));
				record.setStarttime(rs.getTimestamp("starttime"));
				record.setEndtime(rs.getTimestamp("endtime"));
				record.setType(rs.getString("type"));
				record.setMaxNo(rs.getInt("maxno"));
				record.setReceivedNumber(rs.getInt("received_number"));
				//record.setLeavingNumber(rs.getInt("leaving_number"));
				record.setLeavingNumber(record.getMaxNo() - record.getReceivedNumber());
				list.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
		}
		return list;
	}

	public List getStatisticReceivePeriod(Connection conn, String intake,
			String faculty, String fromDate, String toDate, String periodType)
			throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		String sql = null;
		List list = new ArrayList();
		try {
			sql = "select count(per.periodno) as stud_number,"
					+ "per.periodno,per.starttime,per.endtime,fac.facultycode,fac.chinesename as facchnname,per.type "
					+ "from tperiod per "
					+ "inner join tschedule2 sch2 on sch2.periodno = per.periodno "
					+ "inner join tstudent stud on stud.studentno=sch2.studentno "
					+ "inner join tfaculty fac on fac.facultycode=stud.facultycode "
					+ "where 1=1 and sch2.actind='Y' ";
			if (periodType != null && !periodType.equals("")) {
				sql += "and per.type='" + periodType + "' ";
			}
			if (intake != null && !intake.equals("")) {
				sql += "and per.intake='" + intake + "' ";
			}
			if (faculty != null && !faculty.equals("")) {
				sql += "and fac.facultycode='" + faculty + "' ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and per.starttime between to_date('" + fromDate + "','yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "','yyyy-mm-dd hh24:mi:ss') " +
						"and per.endtime between " + "to_date('" + fromDate + "','yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "','yyyy-mm-dd hh24:mi:ss') ";
			}
			sql += "group by per.periodno,per.starttime,per.endtime,fac.facultycode,fac.chinesename,per.type "
					+ "order by per.starttime,fac.facultycode";
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				
				StudReceivePeriodRecord record = new StudReceivePeriodRecord();
				record.setCount(rs.getInt("stud_number"));
				record.setPeriodNo(rs.getString("periodno"));
				record.setStarttime(rs.getTimestamp("starttime"));
				record.setEndtime(rs.getTimestamp("endtime"));
				record.setFacultyCode(rs.getString("facultycode"));
				record.setFacChnName(rs.getString("facchnname"));
				record.setType(rs.getString("type"));
				list.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
		}
		return list;
	}

	public List getStatisticUnSelReceiveTimeRecord(Connection conn,
			String intake, String faculty, String program, int year,
			String unselperiodType) throws Exception {
		List recordList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select ord.orderseqno,ord.orderintake,stud.chinesename,stud.studentno,"
					+ "stud.facultycode,fac.chinesename as facchnname,"
					+ "stud.programcode,pro.chinesename as prochnname,stud.academicyear "
					+ "from tstudent stud "
					+ "inner join torder ord on ord.studentno=stud.studentno "
					+ "inner join tfaculty fac on fac.facultycode=stud.facultycode "
					+ "inner join tprogram pro on pro.programcode=stud.programcode "
					+ "where ord.actind='Y' ";
			if (intake != null && !intake.equals("")) {
				sql += "and ord.orderintake= '" + intake + "' ";
			}
			if (faculty != null && !faculty.equals("")) {
				sql += "and stud.facultycode='" + faculty + "' ";
			}
			if (program != null && !program.equals("")) {
				sql += "and stud.programcode='" + program + "' ";
			}
			if (year != 0) {
				if (year == 1) {
					sql += "and stud.academicyear<=" + year
							+ " or stud.academicyear is null ";
				} else if (year == 5) {
					sql += "and stud.academicyear>=" + year + " ";
				} else {
					sql += "and stud.academicyear=" + year + " ";
				}
			}
			sql += "and stud.studentno not in (select sch2.studentno from tperiod per "
					+ "inner join tschedule2 sch2 on sch2.periodno=per.periodno "
					+ "where per.type='" + unselperiodType + "' and per.intake=ord.orderintake and sch2.actind='Y') "
					+ "order by stud.facultycode,stud.programcode,stud.academicyear,ord.orderseqno";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				StudReceivePeriodRecord record = new StudReceivePeriodRecord();
				record.setOrderseqNo(rs.getString("orderseqno"));
				record.setChineseName(rs.getString("chinesename"));
				record.setStudentNo(rs.getString("studentno"));
				record.setFacChnName(rs.getString("facchnname"));
				record.setProChnName(rs.getString("prochnname"));
				if (rs.getObject("academicyear") == null) {
					record.setAcademicyear(-1);
				} else {
					record.setAcademicyear(rs.getInt("academicyear"));
				}
				recordList.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return recordList;
	}

	public List getStatisticStudReceiveTimeRecord(Connection conn,
			String intake, String faculty, String program, int year,
			String fromDate, String toDate, String periodType) throws Exception {
		List recordList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select ord.orderintake,ord.orderseqno,stud.studentno,stud.chinesename,stud.academicyear,"
					+ "stud.facultycode,fac.chinesename as facchnname,stud.programcode,"
					+ "pro.chinesename as prochnname,t.periodno,t.starttime,t.endtime,t.type "
					+ "from tstudent stud "
					+ "inner join torder ord on ord.studentno=stud.studentno "
					+ "inner join tfaculty fac on fac.facultycode=stud.facultycode "
					+ "inner join tprogram pro on pro.programcode=stud.programcode "
					+ "inner join ("
					+ "select per.periodno,per.type,per.starttime,per.endtime,per.intake,sch2.studentno "
					+ "from tschedule2 sch2 "
					+ "inner join tperiod per on per.periodno=sch2.periodno "
					+ "where per.type='" + periodType + "' and sch2.actind='Y') t on t.studentno=stud.studentno "
					+ "where ord.actind='Y' ";
			if (intake != null && !intake.equals("")) {
				sql += "and ord.orderintake= '" + intake + "' ";
			}
			if (faculty != null && !faculty.equals("")) {
				sql += "and stud.facultycode='" + faculty + "' ";
			}
			if (program != null && !program.equals("")) {
				sql += "and stud.programcode='" + program + "' ";
			}
			if (year != 0) {
				if (year == 1) {
					sql += "and stud.academicyear<=" + year
							+ " or stud.academicyear is null ";
				} else if (year == 5) {
					sql += "and stud.academicyear>=" + year + " ";
				} else {
					sql += "and stud.academicyear=" + year + " ";
				}
			}
			if ((fromDate != null || !fromDate.equals("")) && (toDate == null || toDate.equals(""))) {
				sql += "and t.starttime > to_date('" + fromDate + "','yyyy-MM-dd hh24:mi:ss') ";
			}
			if ((fromDate == null || fromDate.equals("")) && (toDate != null || !toDate.equals(""))) {
				sql += "and t.endtime < to_date('" + toDate + "','yyyy-MM-dd hh24:mi:ss') ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and t.starttime between to_date('" + fromDate + "','yyyy-MM-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "','yyyy-MM-dd hh24:mi:ss') " +
						"and t.endtime between to_date('" + fromDate + "','yyyy-MM-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "','yyyy-MM-dd hh24:mi:ss') ";
			}
			sql += "order by t.starttime,stud.facultycode,stud.programcode,stud.academicyear,ord.orderseqno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				StudReceivePeriodRecord record = new StudReceivePeriodRecord();
				record.setOrderseqNo(rs.getString("orderseqno"));
				record.setChineseName(rs.getString("chinesename"));
				record.setStudentNo(rs.getString("studentno"));
				record.setStarttime(rs.getTimestamp("starttime"));
				record.setEndtime(rs.getTimestamp("endtime"));
				record.setFacChnName(rs.getString("facchnname"));
				record.setProChnName(rs.getString("prochnname"));
				if (rs.getObject("academicyear") == null) {
					record.setAcademicyear(-1);
				} else {
					record.setAcademicyear(rs.getInt("academicyear"));
				}
				recordList.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return recordList;
	}

	public List getSearchPeriod(Connection conn, String intake,
			String periodType) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.periodno,t.intake,t.starttime,t.endtime,t.maxno,t.type "
					+ "from tperiod t where t.intake=? ";
			if (periodType != null && !periodType.equals("")) {
				sql += " and t.type=? ";
			}
			sql += "order by t.type,t.starttime ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			if (periodType != null && !periodType.equals("")) {
				pstmt.setString(2, periodType);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Period period = new Period();
				period.setPeriodNo(rs.getInt("periodno"));
				period.setIntake(rs.getString("intake"));
				period.setStartTime(rs.getTimestamp("starttime"));
				period.setEndTime(rs.getTimestamp("endtime"));
				period.setMaxNo(rs.getInt("maxno"));
				period.setType(rs.getString("type"));
				list.add(period);
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

	public boolean updatePeriodIsN(Connection conn, int id, Period period,
			String userId) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "delete from tperiod where periodno=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				sql = "insert into tperiod_audit (periodno,intake,starttime,endtime,maxno,actind,upduid,upddate) "
						+ "values (?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, period.getPeriodNo());
				pstmt.setString(2, period.getIntake());
				pstmt.setTimestamp(3, new java.sql.Timestamp(period.getStartTime().getTime()));
				pstmt.setTimestamp(4, new java.sql.Timestamp(period.getEndTime().getTime()));
				pstmt.setInt(5, period.getMaxNo());
				pstmt.setString(6, period.getActInd());
				pstmt.setString(7, userId);
				pstmt.setTimestamp(8, ts);
				int rel = pstmt.executeUpdate();
				if (rel > 0) {
					flag = true;
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

	public boolean updateReceiptBookTime(Connection conn, Period period,
			Period oldPeriod) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tperiod set intake=?,starttime=?,endtime=?,maxno=?,type=?,upddate=?,upduid=?,actind=? "
					+ "where periodno=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, period.getIntake());
			pstmt.setTimestamp(2, new java.sql.Timestamp(period.getStartTime().getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(period.getEndTime().getTime()));
			pstmt.setInt(4, period.getMaxNo());
			pstmt.setString(5, period.getType());
			pstmt.setTimestamp(6, ts);
			pstmt.setString(7, period.getUpdUid());
			pstmt.setString(8, period.getActInd());
			pstmt.setInt(9, period.getPeriodNo());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				sql = "insert into tperiod_audit (periodno,intake,starttime,endtime,maxno,actind,upduid,upddate) "
						+ "values (?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, oldPeriod.getPeriodNo());
				pstmt.setString(2, oldPeriod.getIntake());
				pstmt.setTimestamp(3, new java.sql.Timestamp(oldPeriod.getStartTime().getTime()));
				pstmt.setTimestamp(4, new java.sql.Timestamp(oldPeriod.getEndTime().getTime()));
				pstmt.setInt(5, oldPeriod.getMaxNo());
				pstmt.setString(6, oldPeriod.getActInd());
				pstmt.setString(7, period.getUpdUid());
				pstmt.setTimestamp(8, ts);
				int rel = pstmt.executeUpdate();
				if (rel > 0) {
					flag = true;
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

	public Period getPeriodByNo(Connection conn, int periodNo) throws Exception {
		Period period = new Period();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.periodno,t.intake,t.starttime,t.endtime,t.maxno,t.type,t.actind "
					+ "from tperiod t where t.periodno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, periodNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				period.setPeriodNo(rs.getInt("periodno"));
				period.setIntake(rs.getString("intake"));
				period.setStartTime(rs.getTimestamp("starttime"));
				period.setEndTime(rs.getTimestamp("endtime"));
				period.setMaxNo(rs.getInt("maxno"));
				period.setType(rs.getString("type"));
				period.setActInd(rs.getString("actind"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return period;
	}

	public int checkReceiptBookTime(Connection conn, String intake,
			String fromDate, String toDate, String maxNo, String type,
			String periodNo, String oprType) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select count(*) as checkDate from tperiod t "
					+ "where t.intake=? and t.actInd=? and t.type=? "
					+ "and (t.starttime between to_date(?,'yyyy-mm-dd hh24:mi:ss') "
					+ "and to_date(?,'yyyy-mm-dd hh24:mi:ss') "
					+ "or t.endtime between to_date(?,'yyyy-mm-dd hh24:mi:ss') "
					+ "and to_date(?,'yyyy-mm-dd hh24:mi:ss'))";

			if (oprType != null && oprType.equals("edit")) {
				sql += "and t.periodNo <> ? ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, "Y");
			pstmt.setString(3, type);
			pstmt.setString(4, fromDate);
			pstmt.setString(5, toDate);
			pstmt.setString(6, fromDate);
			pstmt.setString(7, toDate);
			if (oprType != null && oprType.equals("edit")) {
				pstmt.setString(8, periodNo);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt("checkDate");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return result;
	}

	public int addReceiptBookTime(Connection conn, Period period)
			throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "insert into tperiod (intake,starttime,endtime,maxno,actInd,creuid,credate,upduid,upddate,type) "
					+ "values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, period.getIntake());
			pstmt.setTimestamp(2, new java.sql.Timestamp(period.getStartTime().getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(period.getEndTime().getTime()));
			pstmt.setInt(4, period.getMaxNo());
			pstmt.setString(5, period.getActInd());
			pstmt.setString(6, period.getCreUid());
			pstmt.setTimestamp(7, ts);
			pstmt.setString(8, period.getUpdUid());
			pstmt.setTimestamp(9, ts);
			pstmt.setString(10, period.getType());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return result;
	}

	public List getAllReceiptBookTime(Connection conn, String intake)
			throws Exception {
		List resultList = new ArrayList();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "select t.periodno,t.intake,t.starttime,t.endtime,t.maxno,t.type "
					+ "from tperiod t where t.intake=? order by t.type,t.starttime";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Period period = new Period();
				period.setPeriodNo(rs.getInt("periodno"));
				period.setIntake(rs.getString("intake"));
				period.setStartTime(rs.getTimestamp("starttime"));
				period.setEndTime(rs.getTimestamp("endtime"));
				period.setMaxNo(rs.getInt("maxno"));
				period.setType(rs.getString("type"));
				resultList.add(period);
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
}
