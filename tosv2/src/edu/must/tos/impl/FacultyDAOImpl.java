package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Faculty;
import edu.must.tos.dao.FacultyDAO;

public class FacultyDAOImpl implements FacultyDAO {

	public List getAutoFaculty(Connection conn, String studentNo)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String str[] = studentNo.split(",");
			String studNo = str[0];
			String appNo = str[1];

			sql = "select fac.facultycode,fac.chinesename as facName "
					+ "from tstudent stu "
					+ "inner join tfaculty fac on stu.facultycode=fac.facultycode "
					+ "where stu.studentno=? or stu.applicantno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studNo);
			pstmt.setString(2, appNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Faculty faculty = new Faculty();
				faculty.setFacultyCode(rs.getString("facultycode"));
				faculty.setChineseName(rs.getString("facName"));
				list.add(faculty);
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

	public List getFacultyList(Connection conn) throws Exception {
		List facultyList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select facultycode, chinesename, englishname from tfaculty ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Faculty faculty = new Faculty();
				faculty.setFacultyCode(rs.getString("facultycode"));
				faculty.setChineseName(rs.getString("chinesename"));
				faculty.setEnglishName(rs.getString("englishname"));
				facultyList.add(faculty);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return facultyList;
	}

	public List getStudToFaculty(Connection conn, String studentNo,
			String intake) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String str[] = studentNo.split(",");
			String studNo = str[0];
			String appNo = str[1];

			sql = "select distinct fac.facultycode,fac.chinesename from tfaculty fac "
					+ "inner join tcourse crs on crs.facultycode=fac.facultycode "
					+ "inner join studentenrol enrol on crs.coursecode=enrol.coursecode "
					+ "where (enrol.studentno=? or enrol.studentno=?) and enrol.courseintake=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studNo);
			pstmt.setString(2, appNo);
			pstmt.setString(3, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Faculty fac = new Faculty();
				fac.setFacultyCode(rs.getString("facultycode"));
				fac.setChineseName(rs.getString("chinesename"));
				list.add(fac);
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

	public List searchFaculty(Connection conn, Faculty faculty) {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select facultyCode,chineseName,englishName from Tfaculty where 1=1 ";

		if (faculty.getFacultyCode() != null && (!faculty.getFacultyCode().equals(""))) {
			sql += " and facultyCode='" + faculty.getFacultyCode() + "'";
		}
		if (faculty.getChineseName() != null && (!faculty.getChineseName().equals(""))) {
			sql += " and chineseName like '%" + faculty.getChineseName() + "%'";
		}
		if (faculty.getEnglishName() != null && (!faculty.getEnglishName().equals(""))) {
			sql += " and englishName like '%" + faculty.getEnglishName() + "%'";
		}
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Faculty fa = new Faculty();
				fa.setFacultyCode(rs.getString("facultyCode"));
				fa.setChineseName(rs.getString("chineseName"));
				fa.setEnglishName(rs.getString("englishName"));
				list.add(fa);
			}
		} catch (SQLException e) {
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
		return list;
	}

}
