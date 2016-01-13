package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Major;
import edu.must.tos.bean.Program;
import edu.must.tos.dao.MajorDAO;
import edu.must.tos.dao.ProgramDAO;

import edu.must.tos.util.UsePage;

public class MajorDAOImpl implements MajorDAO {

	public List getMajByProg(Connection conn, Major major) throws Exception {
		List majorList = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select programCode, majorCode, chineseName, englishName " 
					   + "from tmajor " 
					   + "where programCode = '" + major.getProgramCode() + "' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Major bean = new Major();
				bean.setProgramCode(rs.getString("programCode"));
				bean.setMajorCode(rs.getString("majorCode"));
				bean.setChineseName(rs.getString("chineseName"));
				bean.setEnglishName(rs.getString("englishName"));
				majorList.add(bean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
		return majorList;
	}
	
	public List getMajByFac(Connection conn, String facultyCode) throws Exception {
		List majorList = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select m.majorcode, m.programcode, m.chinesename, m.englishname "
				+ "from tfaculty f " 
				+ "inner join tprogram p on p.facultycode = f.facultycode "
				+ "inner join tmajor m on p.programcode = m.programcode " ;
			if(facultyCode != null && !"".equals(facultyCode)){
				sql += "where f.facultycode = '"+facultyCode+"' ";
			}
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Major bean = new Major();
				bean.setProgramCode(rs.getString("programcode"));
				bean.setMajorCode(rs.getString("majorcode"));
				bean.setChineseName(rs.getString("chinesename"));
				bean.setEnglishName(rs.getString("englishname"));
				majorList.add(bean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
		return majorList;
	}

	public List getMajorList(Connection conn) throws Exception {
		List majorList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select programcode, majorcode, chinesename, englishname "
					+ "from tmajor " 
					+ "order by majorcode ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Major major = new Major();
				major.setMajorCode(rs.getString("majorcode"));
				major.setProgramCode(rs.getString("programcode"));
				major.setChineseName(rs.getString("chinesename"));
				major.setEnglishName(rs.getString("englishname"));
				majorList.add(major);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return majorList;
	}

}
