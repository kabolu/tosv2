package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.NewCourseInfo;
import edu.must.tos.bean.V_NewStudOrder;
import edu.must.tos.dao.NewStudOrderDAO;

public class NewStudOrderDAOImpl implements NewStudOrderDAO {

	public List getNewStudCourseInfo(Connection conn, String intake) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select intake, facultycode, programcode, status, isbn, coursecode, region, awardtype "
					+ "from tnewcosinfo where intake='"+intake+"' ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				NewCourseInfo info = new NewCourseInfo();
				info.setIntake(rs.getString("intake"));
				info.setFacultyCode(rs.getString("facultycode"));
				info.setProgramCode(rs.getString("programcode"));
				info.setStatus(rs.getString("status"));
				info.setIntake(rs.getString("isbn"));
				info.setCourseCode(rs.getString("coursecode"));
				info.setRegion(rs.getString("region"));
				info.setAwardType(rs.getString("awardtype"));
				list.add(info);
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

	public boolean deleteNewStudCourseInfo(Connection conn) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "delete from tnewcosinfo";
			pstmt = conn.prepareStatement(sql);
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

	public List getNewStudOrderInfoAndAcct(Connection conn, V_NewStudOrder v)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.intake,t.studentno,t.studentname,t.studengname,t.facultyname,t.facultycode," +
					"t.programname,t.programcode,t.majorcode," +
					"t.academicyear,t.applicantno,t.acceptance,t.region,t.isbn,t.coursecode,t.confirmQty," +
					"v.paidcurrency, v.paidamount, td.credate, v.credate bookFeeCreDate, v.upddate bookFeeUpdDate  " +
					"from cbps_new_stud_order t " +
					"inner join vbookfeeforapp v on v.paidintake=t.intake and t.applicantno=v.applicantno " +
					"left outer join tordetail td on td.actind='Y' and td.isbn=t.isbn and td.studentno=t.applicantno and td.orderintake=t.intake " +
					"where t.intake=? " +
					"order by t.studentno,t.applicantno";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, v.getIntake());
			//pstmt.setString(2, v.getIntake());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				V_NewStudOrder info = new V_NewStudOrder();
				info.setIntake(rs.getString("intake"));
				info.setStudentNo(rs.getString("studentno"));
				info.setStudentName(rs.getString("studentname"));
				info.setStudEngName(rs.getString("studengname"));
				info.setFacultyName(rs.getString("facultycode"));
				info.setProgramName(rs.getString("programcode"));
				info.setMajorCode(rs.getString("majorcode"));
				info.setApplicantNo(rs.getString("applicantno"));
				info.setAcceptance(rs.getString("acceptance"));
				info.setRegion(rs.getString("region"));
				info.setIsbn(rs.getString("isbn"));
				info.setCourseCode(rs.getString("coursecode"));
				info.setConfirmQty(rs.getInt("confirmQty"));
				info.setPaidCurrency(rs.getString("paidcurrency"));
				info.setPaidAmount(rs.getDouble("paidamount"));
				info.setCreDate(rs.getTimestamp("credate"));
				//info.setTranDate(rs.getTimestamp("TRANDATE"));
				info.setBookFeeCreDate(rs.getTimestamp("bookFeeCreDate"));
				info.setBookFeeUpdDate(rs.getTimestamp("bookFeeUpdDate"));
				list.add(info);
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

	public List getNewStudOrderInfo(Connection conn, V_NewStudOrder v)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.intake,t.studentno,t.studentname,t.studengname,t.facultyname,t.facultycode,"
					+ "t.programname,t.programcode,t.majorcode,"
					+ "t.academicyear,t.applicantno,t.acceptance,t.region,t.isbn,t.mopprice,t.coursecode,t.confirmQty,td.credate "
					+ ", vb.creDate bookFeeCreDate, vb.updDate bookFeeUpdDate "
					+ "from cbps_new_stud_order t " 
					+ "left outer join VBOOKFEEFORAPP vb on vb.APPLICANTNO = t.applicantno and vb.PAIDINTAKE = t.intake "
					+" left outer join tordetail td on td.actind='Y' and td.isbn=t.isbn and td.studentno=t.applicantno and td.orderintake=t.intake " 
					+ "where t.intake='" + v.getIntake() + "' and t.applicantno like '" + v.getIntake() + "%' " 
					+ "order by t.studentno,t.applicantno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				V_NewStudOrder info = new V_NewStudOrder();
				info.setIntake(rs.getString("intake"));
				info.setStudentNo(rs.getString("studentno"));
				info.setStudentName(rs.getString("studentname"));
				info.setStudEngName(rs.getString("studengname"));
				info.setFacultyName(rs.getString("facultycode"));
				info.setProgramName(rs.getString("programcode"));
				info.setMajorCode(rs.getString("majorcode"));
				info.setApplicantNo(rs.getString("applicantno"));
				info.setAcceptance(rs.getString("acceptance"));
				info.setRegion(rs.getString("region"));
				info.setIsbn(rs.getString("isbn"));
				info.setMopPrice(rs.getDouble("mopprice"));
				info.setCourseCode(rs.getString("coursecode"));
				info.setConfirmQty(rs.getInt("confirmQty"));
				info.setCreDate(rs.getTimestamp("credate"));
				info.setBookFeeCreDate(rs.getTimestamp("bookFeeCreDate"));
				info.setBookFeeUpdDate(rs.getTimestamp("bookFeeUpdDate"));
				list.add(info);
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

	public boolean importNewStudCourseInfo(Connection conn, List list)
			throws Exception {
		boolean flag = false;
		PreparedStatement searchPstmt = null, insertPstmt = null;
		ResultSet rs = null;
		String sql = null;
		String insertSQL = null;
		try {
			sql = "select 1 from tnewcosinfo "
					+ "where intake=? and facultycode=? and programcode=? and majorcode=? "
					+ "and status=? and isbn=? and coursecode=? and region=? and awardtype=? ";
			insertSQL = "insert into tnewcosinfo "
					+ "(intake, facultycode, programcode, majorcode, status, isbn, coursecode, region, awardtype) "
					+ "values (?,?,?,?,?,?,?,?,?)";
			searchPstmt = conn.prepareStatement(sql);
			insertPstmt = conn.prepareStatement(insertSQL);

			for (int i = 0; i < list.size(); i++) {
				NewCourseInfo info = new NewCourseInfo();
				info = (NewCourseInfo) list.get(i);
				searchPstmt.setString(1, info.getIntake());
				searchPstmt.setString(2, info.getFacultyCode());
				searchPstmt.setString(3, info.getProgramCode());
				searchPstmt.setString(4, info.getMajorCode());
				searchPstmt.setString(5, info.getStatus());
				searchPstmt.setString(6, info.getIsbn());
				searchPstmt.setString(7, info.getCourseCode());
				searchPstmt.setString(8, info.getRegion());
				searchPstmt.setString(9, info.getAwardType());
				rs = searchPstmt.executeQuery();
				if (!rs.next()) {
					insertPstmt.setString(1, info.getIntake());
					insertPstmt.setString(2, info.getFacultyCode());
					insertPstmt.setString(3, info.getProgramCode());
					insertPstmt.setString(4, info.getMajorCode());
					insertPstmt.setString(5, info.getStatus());
					insertPstmt.setString(6, info.getIsbn());
					insertPstmt.setString(7, info.getCourseCode());
					insertPstmt.setString(8, info.getRegion());
					insertPstmt.setString(9, info.getAwardType());
					int temp = insertPstmt.executeUpdate();
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
			if(rs != null)
				rs.close();
			if (searchPstmt != null)
				searchPstmt.close();
			if (insertPstmt != null)
				insertPstmt.close();
		}
		return flag;
	}

}
