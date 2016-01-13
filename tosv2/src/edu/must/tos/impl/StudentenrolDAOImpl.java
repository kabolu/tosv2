package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Course;
import edu.must.tos.bean.Student;
import edu.must.tos.bean.Studentenrol;
import edu.must.tos.dao.StudentenrolDAO;
import edu.must.tos.util.UsePage;

public class StudentenrolDAOImpl implements StudentenrolDAO {

	public List getCrsMajList(Connection conn, String intake){
		List  result = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select coursecode , majorcode  from studentenrol where courseintake = ? "+
				"group by coursecode , majorcode order by coursecode , majorcode";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Studentenrol enrol = new Studentenrol();
				enrol.setCourseCode(rs.getString("coursecode"));
				enrol.setMajorCode(rs.getString("majorcode"));
				result.add(enrol);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	// 分頁
	public static List getpage(Connection conn, String intake, String faculty,
			String prog, String studentNo, String start, String num) {
		int count = getRsCount(conn, intake, faculty, prog, studentNo);
		List page = UsePage.getPage(count, start, num);
		return page;
	}

	public static int getRsCount(Connection conn, String intake,
			String faculty, String prog, String studentNo) {
		int i = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from studentenrol enl "
					+ "inner join tcourse cos on cos.coursecode=enl.coursecode "
					+ "inner join tstudent stu on stu.studentno=enl.studentno "
					+ "inner join tfaculty fac on fac.facultycode=stu.facultycode where 1=1 ";
			if (intake != null && !intake.equals("")) {
				sql += "and enl.courseintake='" + intake + "' ";
			}
			if (faculty != null && !faculty.equals("")) {
				sql += "and stu.facultycode='" + faculty + "' ";
			}
			if (prog != null && !prog.equals("")) {
				sql += "and stu.programcode='" + prog + "' ";
			}
			if (studentNo != null && !studentNo.equals("")) {
				sql += "and enl.studentno='" + studentNo + "' ";
			}
			sql += "order by stu.facultycode,enl.coursecode,enl.studentno ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	public List getStudentenrol(Connection conn, String intake, String faculty,
			String prog, String studentNo, int start, int num)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select enl.studentno,enl.coursecode,cos.chinesename as coursename,"
				+ "stu.programcode,stu.chinesename,stu.englishname,stu.facultycode,"
				+ "enl.courseintake,enl.classcode,enl.credit,enl.credittype "
				+ "from studentenrol enl "
				+ "inner join tcourse cos on cos.coursecode=enl.coursecode "
				+ "inner join tstudent stu on stu.studentno=enl.studentno "
				+ "where 1=1 ";
		if (intake != null && !intake.equals("")) {
			sql += "and enl.courseintake='" + intake + "' ";
		}
		if (faculty != null && !faculty.equals("")) {
			sql += "and stu.facultycode='" + faculty + "' ";
		}
		if (prog != null && !prog.equals("")) {
			sql += "and stu.programcode='" + prog + "' ";
		}
		if (studentNo != null && !studentNo.equals("")) {
			sql += "and enl.studentno='" + studentNo + "' ";
		}
		sql += "order by stu.facultycode,enl.coursecode,enl.studentno ";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			List enlList = new ArrayList();
			List stuList = new ArrayList();
			List cosList = new ArrayList();
			while (rs.next() && j <= num) {
				Studentenrol enl = new Studentenrol();
				enl.setStudentNO(rs.getString("studentno"));
				enl.setClassCode(rs.getString("classcode"));
				enl.setCreditType(rs.getString("credittype"));
				enlList.add(enl);

				Student stu = new Student();
				stu.setChineseName(rs.getString("chinesename"));
				stu.setEnglishName(rs.getString("englishname"));
				stuList.add(stu);

				Course cos = new Course();
				cos.setCourseCode(rs.getString("coursecode"));
				cos.setChineseName(rs.getString("coursename"));
				cosList.add(cos);

				j++;
			}
			list.add(enlList);
			list.add(stuList);
			list.add(cosList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
