package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookRelInfo;

public class BookRelInfoDAOImpl {
	
	java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
	
	public List getBookRelInfoList(Connection conn, String intake, String isbn)	throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select intake, isbn, coursecode, majorcode, grade, ce, booktype, actind from tbookrelinfo " +
					"where 1=1 ";
			if(intake != null && !"".equals(intake)){
				sql += "and intake = '" + intake + "' ";
			}
			if(isbn != null && !"".equals(isbn)){
				sql += "and isbn = '" + isbn + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookRelInfo info = new BookRelInfo();
				info.setIntake(rs.getString("intake"));
				info.setIsbn(rs.getString("isbn"));
				info.setCourseCode(rs.getString("coursecode"));
				info.setMajorCode(rs.getString("majorcode"));
				info.setGrade(rs.getString("grade"));
				info.setCe(rs.getString("ce"));
				info.setBookType(rs.getString("booktype"));
				info.setActInd(rs.getString("actind"));
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
	
	public BookRelInfo getBookRelInfo(Connection conn, String intake, String isbn, String courseCode, String majorCode) 
		throws Exception {
		BookRelInfo info = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select intake, isbn, coursecode, majorcode, grade, ce, booktype, actind from tbookrelinfo " +
					"where 1=1 ";
			if(intake != null && !"".equals(intake)){
				sql += "and intake = '" + intake + "' ";
			}
			if(isbn != null && !"".equals(isbn)){
				sql += "and isbn = '" + isbn + "' ";
			}
			if(courseCode != null && !"".equals(courseCode)){
				sql += "and coursecode = '" + courseCode + "' ";
			}
			if(majorCode != null && !"".equals(majorCode)){
				sql += "and majorcode = '" + majorCode + "' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				info = new BookRelInfo();
				info.setIntake(rs.getString("intake"));
				info.setIsbn(rs.getString("isbn"));
				info.setCourseCode(rs.getString("coursecode"));
				info.setMajorCode(rs.getString("majorcode"));
				info.setGrade(rs.getString("grade"));
				info.setCe(rs.getString("ce"));
				info.setBookType(rs.getString("booktype"));
				info.setActInd(rs.getString("actind"));
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

	/**
	 * 
	 * @param list
	 * @return 1 int if success
	 * @throws SQLException
	 */
	public int saveBookRelInfo(Connection conn, List list) throws Exception {
		int temp = 0;
		PreparedStatement checkPstmt = null;
		PreparedStatement insPstmt = null;
		PreparedStatement updPstmt = null;
		ResultSet rs = null;
		try {
			String checkSql = "select 1 from tbookrelinfo where intake=? and isbn=? and coursecode=? and majorcode=? ";
			checkPstmt = conn.prepareStatement(checkSql);
			String insSql = "insert into tbookrelinfo " 
				+ "(intake, isbn, courseCode, majorcode, grade, ce, booktype, actInd, creUid, creDate, updUid, updDate) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			insPstmt = conn.prepareStatement(insSql);
			String updSql = "update tbookrelinfo set grade=?, ce=?, booktype=?, actind=?, upduid=?, upddate=? "
				+ "where intake=? and isbn=? and courseCode=? and majorcode=? ";
			updPstmt = conn.prepareStatement(updSql);
			for (int i = 0; i < list.size(); i++) {
				BookRelInfo info = (BookRelInfo)list.get(i);
				checkPstmt.setString(1, info.getIntake());
				checkPstmt.setString(2, info.getIsbn());
				checkPstmt.setString(3, info.getCourseCode());				
				checkPstmt.setString(4, info.getMajorCode());
				rs = checkPstmt.executeQuery();
				if (!rs.next()) {
					insPstmt.setString(1, info.getIntake());
					insPstmt.setString(2, info.getIsbn());
					insPstmt.setString(3, info.getCourseCode());
					insPstmt.setString(4, info.getMajorCode());
					insPstmt.setString(5, info.getGrade());
					insPstmt.setString(6, info.getCe());
					insPstmt.setString(7, info.getBookType());
					insPstmt.setString(8, info.getActInd());
					insPstmt.setString(9, info.getCreUid());
					insPstmt.setTimestamp(10, ts);
					insPstmt.setString(11, info.getUpdUid());
					insPstmt.setTimestamp(12, ts);
					temp = insPstmt.executeUpdate();
					if (temp > 0) {
						temp = 1;
					} else {
						break;
					}
				} else {
					updPstmt.setString(1, info.getGrade());
					updPstmt.setString(2, info.getCe());
					updPstmt.setString(3, info.getBookType());
					updPstmt.setString(4, info.getActInd());
					updPstmt.setString(5, info.getUpdUid());
					updPstmt.setTimestamp(6, ts);
					updPstmt.setString(7, info.getIntake());
					updPstmt.setString(8, info.getIsbn());
					updPstmt.setString(9, info.getCourseCode().toUpperCase());					
					updPstmt.setString(10, info.getMajorCode().toUpperCase());
					temp = updPstmt.executeUpdate();
					if (temp > 0) {
						temp = 1;
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (checkPstmt != null)
				checkPstmt.close();
			if (insPstmt != null)
				insPstmt.close();
			if (updPstmt != null)
				updPstmt.close();
		}
		return temp;
	}

	public int saveBookRelInfo(Connection conn, BookRelInfo info) throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into tbookrelinfo "
				+ "(intake, isbn, courseCode, majorcode, grade, ce, booktype, actind, creuid, credate, upduid, upddate) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getIntake());
			pstmt.setString(2, info.getIsbn());
			pstmt.setString(3, info.getCourseCode().toUpperCase());
			pstmt.setString(4, info.getMajorCode());
			pstmt.setString(5, info.getGrade());
			pstmt.setString(6, info.getCe());
			pstmt.setString(7, info.getBookType());
			pstmt.setString(8, info.getActInd());
			pstmt.setString(9, info.getCreUid());
			pstmt.setTimestamp(10, ts);
			pstmt.setString(11, info.getUpdUid());
			pstmt.setTimestamp(12, ts);
			temp = pstmt.executeUpdate();
			if (temp > 0) {
				temp = 1;
			}
		} catch (Exception e) {
			temp = -1;
			e.printStackTrace();
		} finally {
			if(pstmt != null)
				pstmt.close();
		}
		return temp;
	}

}
