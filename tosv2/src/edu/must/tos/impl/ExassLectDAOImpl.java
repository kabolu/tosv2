package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.ExassLect;
import edu.must.tos.dao.ExassLectDAO;

public class ExassLectDAOImpl implements ExassLectDAO {

	public List<ExassLect> getLectByCrs(Connection conn, ExassLect lect)
			throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		List resultList = new ArrayList();
		try{
			String sql = "select intake, courseCode, lecturer, lectChnName, lectEngName, facultyCode " +
					"from texasslect " +
					"where 1=1 ";
			if(lect.getIntake() != null){
				sql += "and intake = '"+lect.getIntake()+"' ";
			}
			if(lect.getCourseCode() != null){
				sql += "and courseCode = '"+lect.getCourseCode()+"' ";
			}
			if(lect.getLecturer() != null){
				sql += "and lecturer = '"+lect.getLecturer()+"' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ExassLect lecturer = new ExassLect();
				lecturer.setIntake(rs.getString("intake"));
				lecturer.setCourseCode(rs.getString("courseCode"));
				lecturer.setLecturer(rs.getString("lecturer"));
				lecturer.setLectChnName(rs.getString("lectChnName"));
				lecturer.setLectEngName(rs.getString("lectEngName"));
				lecturer.setFacultyCode(rs.getString("facultyCode"));
				resultList.add(lecturer);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return resultList;
	}

}
