package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Program;
import edu.must.tos.dao.ProgramDAO;

import edu.must.tos.util.UsePage;

public class ProgramDAOImpl implements ProgramDAO {

	public List getProgramInfo(Connection conn) throws Exception {
		List programList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select t.facultycode,t.programcode,t.chinesename,t.englishname,t.awardtype "
					+ "from tprogram t order by t.facultycode ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Program program = new Program();
				program.setFacultyCode(rs.getString("facultycode"));
				program.setProgramCode(rs.getString("programcode"));
				program.setChineseName(rs.getString("chinesename"));
				program.setEnglishName(rs.getString("englishname"));
				program.setAwardType(rs.getString("awardtype"));
				programList.add(program);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return programList;
	}

	public List findByFacultyCode(Connection conn, String facultyCode) {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = " select programCode,facultyCode,chineseName,englishName from TProgram where facultyCode = '" + facultyCode + "' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Program pro = new Program();
				pro.setProgramCode(rs.getString(1));
				pro.setFacultyCode(rs.getString(2));
				pro.setChineseName(rs.getString(3));
				pro.setEnglishName(rs.getString(4));
				list.add(pro);
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
		return list;
	}

	public List searchProgram(Connection conn, Program program, int start,
			int num) {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select programCode,facultyCode,chineseName,englishName from Tprogram where 1=1";
		if (program.getProgramCode() != null && (!program.getProgramCode().equals(""))) {
			sql += " and programCode like '%" + program.getProgramCode() + "%'";
		}
		if (program.getFacultyCode() != null && (!program.getFacultyCode().equals(""))) {
			sql += " and facultyCode like '%" + program.getFacultyCode() + "%'";
		}
		if (program.getChineseName() != null && (!program.getChineseName().equals(""))) {
			sql += " and chineseName like '%" + program.getChineseName() + "%'";
		}
		if (program.getEnglishName() != null && (!program.getEnglishName().equals(""))) {
			sql += " and englishName like '%" + program.getEnglishName() + "%'";
		}

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			while (rs.next() && j <= num) {
				Program pro = new Program();
				pro.setFacultyCode(rs.getString("facultyCode"));
				pro.setChineseName(rs.getString("chineseName"));
				pro.setEnglishName(rs.getString("englishName"));
				pro.setProgramCode(rs.getString("programCode"));
				list.add(pro);
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
	public static List getpage(Connection conn, Program program, String start,
			String num) {
		int count = getRsCount(conn, program);
		List page = UsePage.getPage(count, start, num);
		return page;
	}

	// 獲取记录总数方法
	public static int getRsCount(Connection conn, Program program) {
		int i = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from Tprogram where 1=1";
			if (program.getProgramCode() != null && (!program.getProgramCode().equals(""))) {
				sql += " and programCode like '%" + program.getProgramCode() + "%'";
			}
			if (program.getFacultyCode() != null && (!program.getFacultyCode().equals(""))) {
				sql += " and facultyCode like '%" + program.getFacultyCode() + "%'";
			}
			if (program.getChineseName() != null && (!program.getChineseName().equals(""))) {
				sql += " and chineseName like '%" + program.getChineseName() + "%'";
			}
			if (program.getEnglishName() != null && (!program.getEnglishName().equals(""))) {
				sql += " and englishName like '%" + program.getEnglishName() + "%'";
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

}
