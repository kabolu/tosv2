package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookRel;
import edu.must.tos.dao.BookRelDAO;

public class BookRelDAOImpl implements BookRelDAO {	
	
	public List getBookRelList(Connection conn, BookRel bookRel) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select isbn, coursecode, intake, majorcode from tbookrel " +
					"where 1=1 ";
			if(bookRel.getActInd() != null){
				sql += "and actind = '"+bookRel.getActInd()+"' ";
			}
			if(bookRel.getIntake() != null){
				sql += "and intake = '"+bookRel.getIntake()+"' ";
			}
			if(bookRel.getIsbn() != null){
				sql += "and isbn = '"+bookRel.getIsbn()+"' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookRel bRel = new BookRel();
				bRel.setIsbn(rs.getString("isbn"));
				bRel.setCourseCode(rs.getString("coursecode"));
				bRel.setIntake(rs.getString("intake"));
				bRel.setMajorCode(rs.getString("majorcode"));
				list.add(bRel);
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
	 * 
	 * @param list
	 * @return 1 int if success
	 * @throws SQLException
	 */
	public int saveBookRelInDB(Connection conn, List list) throws Exception {
		int temp = 0;
		PreparedStatement checkPstmt = null;
		PreparedStatement insPstmt = null;
		PreparedStatement updPstmt = null;
		ResultSet rs = null;
		try {
			String checkSql = "select 1 from tbookrel where isbn=? and courseCode=? and intake=? and majorcode=? ";
			checkPstmt = conn.prepareStatement(checkSql);
			String insSql = "insert into tbookrel (isbn, courseCode, actInd, creUid, creDate, updUid, updDate, intake, majorcode) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			insPstmt = conn.prepareStatement(insSql);
			String updSql = "update tbookrel set actInd=?, updUid=?, updDate=? "
				+ "where isbn=? and courseCode=? and intake=? and majorcode=? ";
			updPstmt = conn.prepareStatement(updSql);
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			for (int i = 0; i < list.size(); i++) {
				BookRel br = (BookRel)list.get(i);
				checkPstmt.setString(1, br.getIsbn());
				checkPstmt.setString(2, br.getCourseCode());
				checkPstmt.setString(3, br.getIntake());
				checkPstmt.setString(4, br.getMajorCode());
				rs = checkPstmt.executeQuery();
				if (!rs.next()) {
					insPstmt.setString(1, br.getIsbn());
					insPstmt.setString(2, br.getCourseCode());
					insPstmt.setString(3, br.getActInd());
					insPstmt.setString(4, br.getCreUid());
					insPstmt.setTimestamp(5, ts);
					insPstmt.setString(6, br.getUpdUid());
					insPstmt.setTimestamp(7, ts);
					insPstmt.setString(8, br.getIntake());
					insPstmt.setString(9, br.getMajorCode());
					temp = insPstmt.executeUpdate();
					if (temp > 0) {
						temp = 1;
					} else {
						break;
					}
				} else {
					updPstmt.setString(1, br.getActInd());
					updPstmt.setString(2, br.getUpdUid());
					updPstmt.setTimestamp(3, ts);
					updPstmt.setString(4, br.getIsbn());
					updPstmt.setString(5, br.getCourseCode().toUpperCase());
					updPstmt.setString(6, br.getIntake());
					updPstmt.setString(7, br.getMajorCode());
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

	public int deleteBookRelList(Connection conn, List list) throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "update TBOOKREL set actInd=?, updUid=?, updDate=? "
				+ "where isbn=? and courseCode=? and intake=? and (majorcode=? or majorcode is null) ";
			pstmt = conn.prepareStatement(sql);
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			for (int i = 0; i < list.size(); i++) {
				BookRel br = new BookRel();
				br = (BookRel) list.get(i);
				pstmt.setString(1, br.getActInd());
				pstmt.setString(2, br.getUpdUid());
				pstmt.setTimestamp(3, ts);
				pstmt.setString(4, br.getIsbn());
				pstmt.setString(5, br.getCourseCode());
				pstmt.setString(6, br.getIntake());
				pstmt.setString(7, br.getMajorCode());
				temp = pstmt.executeUpdate();
				if (temp > 0) {
					temp = 1;
				} else {
					temp = 0;
					break;
				}
			}
		} catch (Exception e) {
			temp = -1;
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return temp;
	}

	/**
	 * 刪除ISBN時，需要將tbookrel的科目關係設定為N
	 * @param conn
	 * @param br
	 * @return
	 * @throws Exception
	 */
	public int deleteBookRel(Connection conn, BookRel br) throws Exception {
		int temp = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "update TBOOKREL set actInd=?, updUid=?, updDate=? "
					+ "where isbn=? and intake=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, br.getActInd());
			pstmt.setString(2, br.getUpdUid());
			pstmt.setTimestamp(3, new java.sql.Timestamp(br.getUpdDate().getTime()));
			pstmt.setString(4, br.getIsbn());
			pstmt.setString(5, br.getIntake());
			temp = pstmt.executeUpdate();
			if (temp > 0) {
				temp = 1;
			} else {
				temp = 0;
			}
		} catch (Exception e) {
			temp = -1;
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return temp;
	}

	public int saveBookRel(Connection conn, List bookRelList) throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into TBOOKREL (isbn, courseCode, actInd, creUid, creDate, updUid, updDate, intake, majorcode)"
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			for (int i=0; i < bookRelList.size(); i++) {
				BookRel br = (BookRel) bookRelList.get(i);
				pstmt.setString(1, br.getIsbn());
				pstmt.setString(2, br.getCourseCode().toUpperCase());
				pstmt.setString(3, br.getActInd());
				pstmt.setString(4, br.getCreUid());
				pstmt.setTimestamp(5, ts);
				pstmt.setString(6, br.getUpdUid());
				pstmt.setTimestamp(7, ts);
				pstmt.setString(8, br.getIntake());
				pstmt.setString(9, br.getMajorCode());
				temp = pstmt.executeUpdate();
				if (temp > 0) {
					temp = 1;
				} else {
					break;
				}
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
