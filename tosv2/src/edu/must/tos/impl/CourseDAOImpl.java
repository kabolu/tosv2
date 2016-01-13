package edu.must.tos.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Course;
import edu.must.tos.dao.CourseDAO;

import edu.must.tos.util.UsePage;

public class CourseDAOImpl implements CourseDAO {

	public List searchCourse(Connection conn, Course course, int start, int num) {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select courseCode, chineseName, englishName, facultyCode from TCOURSE where 1=1 ";

		if (course.getCourseCode() != null && (!course.getCourseCode().equals(""))) {
			sql += " and courseCode like '%" + course.getCourseCode() + "%'";
		}
		if (course.getChineseName() != null && (!course.getChineseName().equals(""))) {
			sql += " and chineseName like '%" + course.getChineseName() + "%'";
		}
		if (course.getEnglishName() != null && (!course.getEnglishName().equals(""))) {
			sql += " and englishName like '%" + course.getEnglishName() + "%'";
		}
		if (course.getFacultyCode() != null && (!course.getFacultyCode().equals(""))) {
			sql += " and facultyCode='" + course.getFacultyCode() + "'";
		}
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// 分頁，從指定位置開始取記錄
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			while (rs.next() && j <= num) {
				Course co = new Course();
				co.setCourseCode(rs.getString("courseCode"));
				co.setChineseName(rs.getString("chineseName"));
				co.setEnglishName(rs.getString("englishName"));
				co.setFacultyCode(rs.getString("facultyCode"));
				list.add(co);
				j++;
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

	// 分頁
	public static List getpage(Connection conn, Course course, String start,
			String num) {
		int count = getRsCount(conn, course);
		List page = UsePage.getPage(count, start, num);
		return page;
	}

	// 獲取记录总数方法
	public static int getRsCount(Connection conn, Course course) {
		int i = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from TCourse where 1=1 ";
			if (course.getCourseCode() != null && (!course.getCourseCode().equals(""))) {
				sql += " and courseCode like '%" + course.getCourseCode() + "%'";
			}
			if (course.getChineseName() != null && (!course.getChineseName().equals(""))) {
				sql += " and chineseName like '%" + course.getChineseName() + "%'";
			}
			if (course.getEnglishName() != null && (!course.getEnglishName().equals(""))) {
				sql += " and englishName like '%" + course.getEnglishName() + "%'";
			}
			if (course.getFacultyCode() != null && (!course.getFacultyCode().equals(""))) {
				sql += " and facultyCode='" + course.getFacultyCode() + "'";
			}
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
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
		return i;
	}

	public List getCourseCode(Connection conn, Course course) throws Exception {
		List list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select coursecode, facultycode, chinesename, englishname " +
					"from tcourse " +
					"where 1=1 ";
			if(course != null && course.getFacultyCode() != null){
				sql += "and facultycode = '" + course.getFacultyCode() + "' ";
			}
			if(course != null && course.getCourseCode() != null){
				sql += "and coursecode = '" + course.getCourseCode() + "' ";
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Course crs = new Course();
				crs.setCourseCode(rs.getString("coursecode"));
				crs.setFacultyCode(rs.getString("facultycode"));
				crs.setChineseName(rs.getString("chinesename"));
				crs.setEnglishName(rs.getString("englishname"));
				list.add(crs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 
	 * @param cond
	 * @return courseCode List
	 * @throws SQLException
	 */
	public List getCourseCodeLike(Connection conn, String like)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select coursecode,chinesename from tcourse where coursecode like ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, like + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course courseCode = new Course();
				courseCode.setCourseCode(rs.getString("coursecode"));
				courseCode.setChineseName(rs.getString("chinesename"));
				resultList.add(courseCode);
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
		return resultList;
	}

	/**
	 * 
	 * @param coursecode
	 * @return chineseNamw String
	 * @throws SQLException
	 */
	public Course getACourse(Connection conn, String coursecode)
			throws Exception {
		String courseName = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Course cos = null;
		try {
			String sql = "select facultycode, chinesename, englishname from tcourse where coursecode=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, coursecode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cos = new Course();
				cos.setFacultyCode(rs.getString("facultycode"));
				cos.setChineseName(rs.getString("chinesename"));
				cos.setEnglishName(rs.getString("englishname"));
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
		return cos;
	}
}
