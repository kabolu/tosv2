package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.Faculty;
import edu.must.tos.bean.Major;
import edu.must.tos.bean.Program;
import edu.must.tos.bean.Student;
import edu.must.tos.dao.StudentDAO;
import edu.must.tos.util.ToolsOfString;
import edu.must.tos.util.UsePage;

public class StudentDAOImpl implements StudentDAO {

	public List getUpdNewStudList(Connection conn, String intake)
			throws Exception {
		List newStudList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select stu.studentno as studNo,stu.applicantno,o.studentno as ordStudNo "
					+ "from torder o "
					+ "inner join tstudent stu on stu.applicantno=o.studentno "
					+ "where o.creuid='SYSTEM' and o.actind='Y' and o.orderintake=? "
					+ "and stu.studentno is not null and (stu.status is null or stu.status in ('UR','A')) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String studNo = rs.getString("studNo");
				String ordStudNo = rs.getString("ordStudNo");
				if (!studNo.equals(ordStudNo)) {
					Student stud = new Student();
					stud.setStudentNo(studNo);
					stud.setApplicantNo(rs.getString("applicantno"));
					newStudList.add(stud);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return newStudList;
	}

	public boolean isNewStudent(Connection conn, Student stu) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select 1 from tstudent stu "
					+ "where (stu.academicyear=0 or stu.academicyear is null) "
					+ "and (stu.status is null or stu.status in ('A','D','UR')) "
					+ "and (stu.studentno=? or stu.applicantno=?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, stu.getStudentNo());
			pstmt.setString(2, stu.getApplicantNo());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}

	public List getNewStudList(Connection conn, Student newStud)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select studentno, applicantno, region, acceptance, facultycode, programcode "
					+ "from tstudent "
					+ "where (status in ('A','D','UR') or status is null) " ;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Student stud = new Student();
				stud.setStudentNo(rs.getString("studentno"));
				stud.setApplicantNo(rs.getString("applicantno"));
				stud.setRegion(rs.getString("region"));
				stud.setAcceptance(rs.getString("acceptance"));
				stud.setFacultyCode(rs.getString("facultycode"));
				stud.setProgramCode(rs.getString("programcode"));
				list.add(stud);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return list;
	}

	public List searchStudent(Connection con, Student student, int start,
			int num) throws Exception {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select " +
				"ts.studentNo,ts.chineseName,ts.englishName,ts.idNo,ts.facultyCode,ts.email,ts.contactNo,ts.status,ts.programCode,ts.applicantno,tc.email as email2,tc.contactNo as contactNo2 " +
				"from " +
				"tstudent ts " +
				"left join " +
				"tcontact tc on tc.studentno=ts.studentno " +
				"where 1=1 ";
		if (student.getStudentNo() != null && (!student.getStudentNo().equals(""))) {
			sql+=" and ts.studentNo like '"+student.getStudentNo()+"%' ";
			//sql += " and studentNo = '" + student.getStudentNo() + "' ";
		}
		if (student.getApplicantNo() != null && (!student.getApplicantNo().equals(""))) {
			sql+=" and ts.applicantno like '"+student.getApplicantNo()+"%' ";
			//sql += " and applicantno ='" + student.getApplicantNo() + "' ";
		}
		if (student.getChineseName() != null && (!student.getChineseName().equals(""))) {
			sql += " and ts.chineseName like '%" + student.getChineseName() + "%' ";
		}
		if (student.getEnglishName() != null && (!student.getEnglishName().equals(""))) {
			sql += " and ts.englishName like '%" + student.getEnglishName() + "%' ";
		}
		if (student.getIdNo() != null && (!student.getIdNo().equals(""))) {
			sql += " and ts.idNo like '%" + student.getIdNo() + "%' ";
		}
		if (student.getFacultyCode() != null && (!student.getFacultyCode().equals(""))) {
			sql += " and ts.facultyCode = '" + student.getFacultyCode() + "' ";
		}
		if (student.getEmail() != null && (!student.getEmail().equals(""))) {
			sql += " and ts.email like '%" + student.getEmail() + "%' ";
		}
		if (student.getContactNo() != null && (!student.getContactNo().equals(""))) {
			sql += " and ts.contactNo like '%" + student.getContactNo() + "%' ";
		}
		if (student.getProgramCode() != null && (!student.getProgramCode().equals(""))) {
			sql += " and ts.programCode='" + student.getProgramCode() + "' ";
		}

		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			while (rs.next() && j <= num) {
				Student stu = new Student();
				stu.setStudentNo(rs.getString("studentNo"));
				stu.setChineseName(ToolsOfString.NulltoEmpty(rs.getString("chineseName")));
				stu.setEnglishName(ToolsOfString.NulltoEmpty(rs.getString("englishName")));
				stu.setIdNo(ToolsOfString.NulltoEmpty(rs.getString("idNo")));
				stu.setFacultyCode(rs.getString("facultyCode"));
				stu.setEmail(ToolsOfString.NulltoEmpty(rs.getString("email")));
				stu.setEmail2(ToolsOfString.NulltoEmpty(rs.getString("email2")));
				stu.setContactNo(ToolsOfString.NulltoEmpty(rs.getString("contactNo")));
				stu.setContactNo2(ToolsOfString.NulltoEmpty(rs.getString("contactNo2")));
				stu.setStatus(ToolsOfString.NulltoEmpty(rs.getString("status")));
				stu.setProgramCode(rs.getString("programCode"));
				stu.setApplicantNo(rs.getString("applicantno"));
				list.add(stu);
				j++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List searchStudentLackBook(Connection con, Book book, String intake, String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select st.studentNo,st.chineseName,st.englishName,st.idNo,st.facultyCode,st.email,st.contactNo,st.status,st.programCode,st.applicantno,"+
			"tc.email as email2,tc.contactNo as contactNo2,o.orderseqno "+
			"from torder o "+
			"inner join tordetail td on td.orderseqno = o.orderseqno and td.actind = 'Y' "+
			"inner join tbook tb on tb.isbn = td.isbn and tb.actind = 'Y' "+
			"inner join tstudent st on (st.studentno = o.studentno or st.applicantno = o.studentno) "+
			"left outer join tcontact tc on tc.studentno = o.studentno "+
			"where o.orderintake = '" + intake + "' and o.actind = 'Y' and o.studentno <> 'RETAIL' and td.notenoughqty > 0 ";
			if(book != null && book.getIsbn() != null){
				sql += "and td.isbn = '" + book.getIsbn() + "' ";
			}
			if(fromDate != null && !"".equals(fromDate) && toDate != null && !"".equals(toDate)){
				sql += "and o.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') "+
					"and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			sql += "group by st.studentNo,st.chineseName,st.englishName,st.idNo,st.facultyCode,st.email,st.contactNo,st.status,st.programCode,st.applicantno,tc.email,tc.contactNo,o.orderseqno";
		
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Student stu = new Student();
				stu.setStudentNo(rs.getString("studentNo"));
				stu.setChineseName(ToolsOfString.NulltoEmpty(rs.getString("chineseName")));
				stu.setEnglishName(ToolsOfString.NulltoEmpty(rs.getString("englishName")));
				stu.setIdNo(ToolsOfString.NulltoEmpty(rs.getString("idNo")));
				stu.setFacultyCode(rs.getString("facultyCode"));
				stu.setEmail(ToolsOfString.NulltoEmpty(rs.getString("email")));
				stu.setEmail2(ToolsOfString.NulltoEmpty(rs.getString("email2")));
				stu.setContactNo(ToolsOfString.NulltoEmpty(rs.getString("contactNo")));
				stu.setContactNo2(ToolsOfString.NulltoEmpty(rs.getString("contactNo2")));
				stu.setStatus(ToolsOfString.NulltoEmpty(rs.getString("status")));
				stu.setProgramCode(rs.getString("programCode"));
				stu.setApplicantNo(rs.getString("applicantno"));
				stu.getOrder().setOrderSeqNo(rs.getInt("orderseqno"));
				list.add(stu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List searchNoticeBookInfo(Connection con, String studentNo,Book book, String intake,int start,
			int num) throws Exception {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select " +
		"TB.ISBN, TB.TITLE, TB.AUTHOR, TB.PUBLISHER, T.NOTENOUGHQTY " +
		" from " +
		" tstudent ts " +
		" left join tcontact tc " +
		" on tc.studentno=ts.studentno " +
		" inner join tordetail t " +
		" on ts.studentNo = t.studentNo and t.actind='Y' and t.ORDERINTAKE= "+intake+
		" left join tbook tb" +
		" on TB.ISBN = T.ISBN "+
		" where t.notenoughqty > 0 and ts.studentNo = " +
		"'"+studentNo+"'";
		
		if(book.getIsbn()!=null){
			sql = sql + " and tb.isbn = '" +book.getIsbn()+"'";
		}
		
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			while (rs.next() && j <= num) {
				Book b = new Book();
				b.setIsbn(ToolsOfString.NulltoEmpty(rs.getString("isbn")));
				b.setTitle(ToolsOfString.NulltoEmpty(rs.getString("title")));
				b.setAuthor(ToolsOfString.NulltoEmpty(rs.getString("author")));
				b.setPublisher(ToolsOfString.NulltoEmpty(rs.getString("publisher")));
				b.setRemarks(ToolsOfString.NulltoEmpty(rs.getString("NOTENOUGHQTY")));
				list.add(b);
				j++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 分頁
	public static List getpage(Connection con, Student student, String start,
			String num, int count) {
		//int count = getRsCount(con, student);
		List page = UsePage.getPage(count, start, num);
		return page;
	}

	// 獲取记录总数方法
	public static int getRsCount(Connection con, Student student) {
		int i = 0;
		Statement st = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from tstudent where 1=1";
			if (student.getStudentNo() != null && (!student.getStudentNo().equals(""))) {
				sql+=" and studentNo like '"+student.getStudentNo()+"%' ";
				//sql += " and studentNo = '" + student.getStudentNo() + "' ";
			}
			if (student.getApplicantNo() != null && (!student.getApplicantNo().equals(""))) {
				sql+=" and applicantNo like '"+student.getApplicantNo()+"%' ";
				//sql+=" and applicantNo = '"+student.getApplicantNo()+"' ";
			}
			if (student.getChineseName() != null && (!student.getChineseName().equals(""))) {
				sql += " and chineseName like '%" + student.getChineseName() + "%' ";
			}
			if (student.getEnglishName() != null && (!student.getEnglishName().equals(""))) {
				sql += " and englishName like '%" + student.getEnglishName() + "%' ";
			}
			if (student.getIdNo() != null && (!student.getIdNo().equals(""))) {
				sql += " and idNo like '%" + student.getIdNo() + "%' ";
			}
			if (student.getFacultyCode() != null && (!student.getFacultyCode().equals(""))) {
				sql += " and facultyCode = '" + student.getFacultyCode() + "' ";
			}
			if (student.getEmail() != null && (!student.getEmail().equals(""))) {
				sql += " and email like '%" + student.getEmail() + "%' ";
			}
			if (student.getContactNo() != null && (!student.getContactNo().equals(""))) {
				sql += " and contactNo like '%" + student.getContactNo() + "%' ";
			}
			if (student.getProgramCode() != null && (!student.getProgramCode().equals(""))) {
				sql += " and programCode='" + student.getProgramCode() + "' ";
			}
			//System.out.println("getRsCount sql = "+sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
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
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	/**
	 * 獲取有關學生的基本信息，並不包括status為C的記錄
	 */
	public List showStudentDetail(Connection conn, String psStudentNo)
			throws Exception {
		List list = new ArrayList();
		Statement st = null;
		ResultSet rs = null;
		String str[] = psStudentNo.split(",");
		String studNo = str[0];
		String appNo = str[1];
		String sql = "select "
				+ "ts.studentNo, ts.chineseName, ts.englishName, ts.idNo, ts.facultyCode, ts.email, ts.contactNo, " 
				+ "ts.status, ts.programCode, ts.applicantNo, ts.majorcode, ts.stud_grp, "
				+ "tf.chineseName as faChineseName, tf.englishName as faEnglishName, "
				+ "tp.chineseName as proChineseName, tp.englishName as prEnglishName, "
				+ "tm.chineseName as majChineseName, tm.englishName as majEnglishName "
				+ "from tprogram tp "
				+ "inner join tstudent ts on tp.programCode = ts.programCode "
				+ "inner join tfaculty tf on ts.facultyCode = tf.facultyCode "
				+ "inner join tmajor tm on tm.majorcode = ts.majorcode ";
		if (studNo != null && !studNo.equals("null") && appNo != null && !appNo.equals("null")) {
			sql += " where ts.studentNo='" + studNo + "' ";
		} else {
			sql += " where (ts.studentNo='" + studNo + "' or ts.applicantNo='" + appNo + "') " +
					"and (ts.status is null or ts.status <> 'C') ";
		}
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				Student stu = new Student();
				stu.setStudentNo(rs.getString("studentNo"));
				stu.setChineseName(rs.getString("chineseName"));
				stu.setEnglishName(rs.getString("englishName"));
				stu.setIdNo(rs.getString("idNo"));
				stu.setFacultyCode(rs.getString("facultyCode"));
				stu.setEmail(rs.getString("email"));
				stu.setContactNo(rs.getString("contactNo"));
				stu.setStatus(rs.getString("status"));
				stu.setProgramCode(rs.getString("programCode"));
				stu.setApplicantNo(rs.getString("applicantNo"));
				stu.setMajorCode(rs.getString("majorcode"));
				stu.setStud_grp(rs.getString("stud_grp"));
				list.add(stu);

				Faculty fa = new Faculty();
				fa.setChineseName(rs.getString("faChineseName"));
				fa.setEnglishName(rs.getString("faEnglishName"));
				list.add(fa);

				Program pro = new Program();
				pro.setChineseName(rs.getString("proChineseName"));
				pro.setEnglishName(rs.getString("prEnglishName"));
				list.add(pro);
				
				Major major = new Major();
				major.setChineseName(rs.getString("majChineseName"));
				major.setEnglishName(rs.getString("majEnglishName"));
				list.add(major);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public Vector getStatisticStudentInfo(Connection conn, String fromDate,
			String toDate, String intake) throws Exception {
		Vector outter = new Vector();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select studentNo, chineseName, englishName, " 
				+ "isbn, title, author, publisher, publishYear, edition, language, bookType, remarks, "
				+ "orderIntake, upddate, confirmQty, unconfirmQty, withdrawQty, "
				+ "mop_futurePrice, mopFutureAmt, " 
			    + "rmb_futurePrice, rmbFutureAmt, "
				+ "mop_netprice, mopNetAmt, rmb_netprice, rmbNetAmt, "
				+ "mop_withprice, mopWithdrawAmt, rmb_withprice, rmbWithdrawAmt "
				+ "from cbps_stud_order " 
				+ "where 1=1 ";
			if(intake!=null && !intake.equals("")){
				sql += "and orderIntake = '" + intake + "' ";
			}
			if ((fromDate != null && !fromDate.equals("")) && (toDate != null && !toDate.equals(""))){
				String from = fromDate + " 00:00:00";
				String to = toDate + " 23:59:59";
				sql += "and upddate between to_date('" + from + "','yyyy-mm-dd HH24:mi:ss') " 
						+ "and to_date('" + to + "','yyyy-mm-dd HH24:mi:ss') ";
			}
			sql += "order by studentNo,isbn,upddate ";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Vector inner = new Vector();
				inner.add(rs.getString("studentNo"));
				inner.add(rs.getString("chineseName"));
				inner.add(rs.getString("englishName"));
				inner.add(rs.getString("isbn"));
				inner.add(rs.getString("title"));
				inner.add(rs.getString("author"));
				inner.add(rs.getString("publisher"));
				inner.add(rs.getString("publishYear"));
				inner.add(rs.getString("edition"));
				inner.add(rs.getString("language"));
				inner.add(rs.getString("bookType"));
				inner.add(rs.getString("remarks"));
				inner.add(rs.getString("orderIntake"));
				inner.add(rs.getTimestamp("upddate"));
				inner.add(rs.getInt("confirmQty"));
				inner.add(rs.getInt("unconfirmQty"));
				inner.add(rs.getInt("withdrawQty"));
				inner.add(rs.getDouble("mop_futurePrice"));
				inner.add(rs.getDouble("mopFutureAmt"));
				inner.add(rs.getDouble("rmb_futurePrice"));
				inner.add(rs.getDouble("rmbFutureAmt"));
				inner.add(rs.getDouble("mop_netprice"));
				inner.add(rs.getDouble("mopNetAmt"));
				inner.add(rs.getDouble("rmb_netprice"));
				inner.add(rs.getDouble("rmbNetAmt"));
				inner.add(rs.getDouble("mop_withprice"));
				inner.add(rs.getDouble("mopWithdrawAmt"));
				inner.add(rs.getDouble("rmb_withprice"));
				inner.add(rs.getDouble("rmbWithdrawAmt"));
				outter.add(inner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return outter;
	}

	public List getReceiveTime(Connection conn, String studentno, String intake)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select starttime, endtime from tschedule "
					+ "where studentno = ? and intake = ? and actind = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studentno);
			pstmt.setString(2, intake);
			pstmt.setString(3, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Date starttime = rs.getTimestamp("starttime");
				Date endtime = rs.getTimestamp("endtime");
				list.add(starttime);
				list.add(endtime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
