package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import edu.must.tos.bean.BookTempl;

public class BookTemplDAOImpl {

	public int saveBookTemplInDB(Connection conn, List booktemplList) throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		PreparedStatement insPstmt = null;
		PreparedStatement updPstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select 1 from TBOOKTEMPL where facultyCode=? and intake=? and courseCode=? and year=? and majorCode=? ";
			pstmt = conn.prepareStatement(sql);
			String insSql = "insert into TBOOKTEMPL (facultyCode, intake, courseCode, year, actInd, creUid, creDate, updUid, updDate, stud_grp, majorCode) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			insPstmt = conn.prepareStatement(insSql);
			String updSql = "update TBOOKTEMPL set updUid=?, updDate=?, actind=?, stud_grp=? "
				+ "where facultyCode=? and intake=? and courseCode=? and year=? and majorCode=? ";
			updPstmt = conn.prepareStatement(updSql);
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			for (int i = 0; i < booktemplList.size(); i++) {
				BookTempl booktempl = (BookTempl) booktemplList.get(i);
				pstmt.setString(1, booktempl.getFacultyCode());
				pstmt.setString(2, booktempl.getIntake());
				pstmt.setString(3, booktempl.getCourseCode());
				pstmt.setInt(4, booktempl.getYear());
				pstmt.setString(5, booktempl.getMajorCode());
				rs = pstmt.executeQuery();
				if (!rs.next()) {
					insPstmt.setString(1, booktempl.getFacultyCode());
					insPstmt.setString(2, booktempl.getIntake());
					insPstmt.setString(3, booktempl.getCourseCode());
					insPstmt.setInt(4, booktempl.getYear());
					insPstmt.setString(5, booktempl.getActInd());
					insPstmt.setString(6, booktempl.getCreUid());
					insPstmt.setTimestamp(7, ts);
					insPstmt.setString(8, booktempl.getUpdUid());
					insPstmt.setTimestamp(9, ts);
					insPstmt.setString(10, booktempl.getStud_grp());
					insPstmt.setString(11, booktempl.getMajorCode());
					temp = insPstmt.executeUpdate();
					if (temp > 0) {
						temp = 1;
					} else {
						temp = 0;
						break;
					}
				} else {
					updPstmt.setString(1, booktempl.getUpdUid());
					updPstmt.setTimestamp(2, ts);
					updPstmt.setString(3, booktempl.getActInd());
					updPstmt.setString(4, booktempl.getStud_grp());
					updPstmt.setString(5, booktempl.getFacultyCode());
					updPstmt.setString(6, booktempl.getIntake());
					updPstmt.setString(7, booktempl.getCourseCode());
					updPstmt.setInt(8, booktempl.getYear());
					updPstmt.setString(9, booktempl.getMajorCode());
					temp = updPstmt.executeUpdate();
					if (temp > 0) {
						temp = 1;
					} else {
						temp = 0;
						break;
					}
				}
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
			if(insPstmt != null)
				insPstmt.close();
			if(updPstmt != null)
				updPstmt.close();
		}
		return temp;
	}
}
