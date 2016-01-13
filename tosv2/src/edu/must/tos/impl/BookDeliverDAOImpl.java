package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookDeliver;
import edu.must.tos.bean.DeliverSummary;
import edu.must.tos.bean.ViewStudOrdInfo;
import edu.must.tos.dao.BookDeliverDAO;

public class BookDeliverDAOImpl implements BookDeliverDAO {

	public List getBookDeliverList(Connection conn, String intake)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try{
			sql = "select td.orderintake, to_char(td.credate, 'yyyy/mm/dd') as deliverDate, td.orderseqno, td.studentno, st.chineseName, st.englishName, " +
					"td.creuid, sum(td.quantity) as quantity " +
					"from tbookdeliver td " +
					"inner join tstudent st on (st.studentno = td.studentno or st.applicantno = td.studentno ) " +
					"where td.actind=? and td.orderintake=? " +
					"group by td.orderintake, to_char(td.credate, 'yyyy/mm/dd'), td.orderseqno, td.studentno, st.chineseName, st.englishName, td.creuid " +
					"order by td.orderintake, to_char(td.credate, 'yyyy/mm/dd') ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDeliver bDeliver = new BookDeliver();
				bDeliver.setIntake(rs.getString("orderintake"));
				bDeliver.setDeliverDate(rs.getString("deliverDate"));
				bDeliver.setOrderSeqNo(rs.getInt("orderseqno"));
				bDeliver.setStudentNo(rs.getString("studentno"));
				bDeliver.setChineseName(rs.getString("chineseName"));
				bDeliver.setEnglishName(rs.getString("englishName"));
				bDeliver.setCreUid(rs.getString("creuid"));
				bDeliver.setQuantity(rs.getInt("quantity"));
				resultList.add(bDeliver);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return resultList;
	}
	
	public List getBookDeliverList(Connection conn, String intake, String isbn)	throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try{
			sql = "select deliverno, orderintake, orderseqno, studentno, quantity, credate, creuid, upddate, upduid, actind, isbn " +
					"from tbookdeliver " + 
					"where actind = ? and orderintake = ? and isbn = ? " ;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, intake);
			pstmt.setString(3, isbn);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDeliver bDeliver = new BookDeliver();
				bDeliver.setDeliverNo(rs.getInt("deliverno"));
				bDeliver.setIntake(rs.getString("orderintake"));
				bDeliver.setOrderSeqNo(rs.getInt("orderseqno"));
				bDeliver.setStudentNo(rs.getString("studentno"));
				bDeliver.setQuantity(rs.getInt("quantity"));
				bDeliver.setCreDate(rs.getTimestamp("credate"));
				bDeliver.setCreUid(rs.getString("creuid"));
				bDeliver.setUpdDate(rs.getTimestamp("upddate"));
				bDeliver.setUpdUid(rs.getString("upduid"));
				bDeliver.setActind(rs.getString("actind"));
				bDeliver.setIsbn(rs.getString("isbn"));
				resultList.add(bDeliver);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return resultList;
	}
	
	public boolean updateBookDeliverByNo(Connection conn, BookDeliver deliver)	throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		try{
			sql = "update tbookdeliver set orderintake = ?, orderseqno = ?, studentno = ?, quantity = ?, " + 
					"upddate = ?, upduid = ?, actind = ?, isbn = ? " +
					"where deliverno = ? " ;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deliver.getIntake());
			pstmt.setInt(2, deliver.getOrderSeqNo());
			pstmt.setString(3, deliver.getStudentNo());
			pstmt.setInt(4, deliver.getQuantity());
			pstmt.setTimestamp(5, ts);
			pstmt.setString(6, deliver.getUpdUid());
			pstmt.setString(7, deliver.getActind());
			pstmt.setString(8, deliver.getIsbn());
			pstmt.setInt(9, deliver.getDeliverNo());
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(pstmt!=null)
				pstmt.close();
		}
		return flag;
	}
	
	public List getDeliverReportList(Connection conn, String intake,
			String reportType) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			if (reportType != null && "A".equals(reportType)) {
				sql = "select orderintake, upddate, upduid, programcode, facultycode, num "
						+ "from CBPS_BOOK_DELIVER_SUMMARY "
						+ "where orderintake=? "
						+ "order by orderintake, upddate, upduid, programcode, facultycode ";
			} else if (reportType != null && "B".equals(reportType)) {
				//CBPS_BOOK_DELIVER_SUMMARY
				sql = "select od.orderintake, TO_CHAR (od.upddate, 'yyyy/mm/dd') as upddate, st.facultycode, st.programcode, pm.chinesename as programname, count( distinct od.orderseqno ) as num " 
						+ "from tordetail od " 
						+ "inner join tstudent st on st.studentno = od.studentno or st.applicantno = od.studentno "
						+ "inner join tprogram pm on pm.programcode = st.programcode "
						+ "where orderintake = ? and actind = 'Y'  "
						+ "GROUP BY OD.ORDERINTAKE, TO_CHAR (OD.UPDDATE, 'yyyy/mm/dd'), ST.FACULTYCODE, ST.PROGRAMCODE, PM.CHINESENAME " 
						+ "ORDER BY OD.ORDERINTAKE, TO_CHAR (OD.UPDDATE, 'yyyy/mm/dd'), ST.FACULTYCODE, ST.PROGRAMCODE ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DeliverSummary ds = new DeliverSummary();
				ds.setDate(rs.getString("upddate"));
				if (reportType != null && "A".equals(reportType)) {
					ds.setUpduid(rs.getString("upduid"));
				}
				ds.setProgramCode(rs.getString("programcode"));
				if (reportType != null && "B".equals(reportType)) {
					ds.setProgramName(rs.getString("programname"));
				}
				ds.setFacultyCode(rs.getString("facultycode"));
				ds.setOrderCount(rs.getInt("num"));
				resultList.add(ds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resultList;
	}
	
	public List getDeliverBookRecord(Connection conn, String intake,
			String faculty, String program, String academicYear, String status,
			String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		String sql = null;
		PreparedStatement pstmt = null;
		try {
			sql = "select t.chinesename,t.studentno,t.facultycode,t.fachnname,t.academicyear,"
					+ "t.programcode,t.progchnname,t.orderseqno,t.orderintake,t.notenoughqty,"
					+ "t.title,t.author,t.publisher,t.edition,t.publishyear,t.isbn,"
					+ "t.confirmqty,t.orderedqty,t.notenoughqty,t.receivedqty,t.futurepricemop,t.futurepricermb,t.netpricemop,t.netpricermb,"
					+ "t.paidcurrency,t.paidamount,t.paiddate,t.cashier,t.bookdeliverdate,t.bookdeliver,"
					+ "t.contactno,t.email,t.status, c.contactno as newcontact, c.email as newemail " 
					+ "from cbps_book_deliver t "
					+ "left outer join tcontact c on c.studentno = t.studentno "
					+ "where 1=1 ";
			if (intake != null && !intake.equals("")) {
				sql += "and orderintake='" + intake + "' ";
			}
			if (faculty != null && !faculty.equals("")) {
				sql += "and facultycode='" + faculty + "' ";
			}
			if (program != null && !program.equals("")) {
				sql += "and programcode='" + program + "' ";
			}
			if (academicYear != null && !academicYear.equals("")) {
				sql += "and academicyear='" + academicYear + "' ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null
					&& !toDate.equals("")) {
				sql += "and t.bookdeliverdate between to_date('" + fromDate
						+ "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('"
						+ toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			if (status != null && status.equals("1")) {// 已領書
				sql += "and notenoughqty='0' ";
			} else if (status != null && status.equals("2")) {// 未領書
				sql += "and notenoughqty is null and confirmqty>0 ";
			} else if (status != null && status.equals("3")) {// 缺書
				sql += "and notenoughqty>'0' ";
			}
			sql += "order by t.facultycode,t.academicyear,t.programcode,t.orderseqno,t.studentno,t.isbn asc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ViewStudOrdInfo info = new ViewStudOrdInfo();
				info.setChineseName(rs.getString("chinesename"));
				info.setStudentNo(rs.getString("studentno"));
				info.setFacultyName(rs.getString("fachnname"));
				info.setAcademicYear(rs.getInt("academicyear"));
				info.setProgram(rs.getString("progchnname"));
				info.setOrderSeqNo(rs.getString("orderseqno"));
				info.setTitle(rs.getString("title"));
				info.setAuthor(rs.getString("author"));
				info.setPublisher(rs.getString("publisher"));
				info.setEdition(rs.getString("edition"));
				info.setPublishYear(rs.getString("publishyear"));
				info.setIsbn(rs.getString("isbn"));
				info.setOrderEdQty(rs.getInt("orderedqty"));
				info.setNotEnoughQty(rs.getInt("notenoughqty"));
				info.setReceivedQty(rs.getInt("receivedqty"));
				info.setFuturePriceMOP(rs.getDouble("futurepricemop"));
				info.setFuturePriceRMB(rs.getDouble("futurepricermb"));
				info.setNetPriceMOP(rs.getDouble("netpricemop"));
				info.setNetPriceRMB(rs.getDouble("netpricermb"));
				info.setPaidAmount(rs.getDouble("paidamount"));
				info.setPaidCurrency(rs.getString("paidcurrency"));
				info.setPaidDate(rs.getTimestamp("paiddate"));
				info.setCashier(rs.getString("cashier"));
				info.setBookDeliverDate(rs.getTimestamp("bookdeliverdate"));
				info.setBookDeliver(rs.getString("bookdeliver"));
				info.setContactNo(rs.getString("contactno"));
				info.setEmail(rs.getString("email"));
				info.setStatus(rs.getString("status"));
				info.setNewContactNo(rs.getString("newcontact"));
				info.setNewEmail(rs.getString("newemail"));
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
	
	public List getDeliverBookRecord3(Connection conn, String intake,
			String faculty, String program, String academicYear, String status,
			String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		String sql = null;
		PreparedStatement pstmt = null;
		try {
			sql = "select t.chinesename,t.studentno,t.facultycode,t.fachnname,t.academicyear,"
					+ "t.programcode,t.progchnname,t.orderseqno,t.orderintake,t.notenoughqty,"
					+ "t.title,t.author,t.publisher,t.edition,t.publishyear,t.isbn,"
					+ "t.confirmqty,t.orderedqty,t.notenoughqty,t.receivedqty,t.futurepricemop,t.futurepricermb,t.netpricemop,t.netpricermb,"
					+ "t.paidcurrency,t.paidamount,t.paiddate,t.cashier,t.bookdeliverdate,t.bookdeliver,"
					+ "t.contactno,t.email,t.status, c.contactno as newcontact, c.email as newemail " 
					+ "from cbps_book_deliver_notenough t "
					+ "left outer join tcontact c on c.studentno = t.studentno "
					+ "where 1=1 ";
			if (intake != null && !intake.equals("")) {
				sql += "and orderintake='" + intake + "' ";
			}
			if (faculty != null && !faculty.equals("")) {
				sql += "and facultycode='" + faculty + "' ";
			}
			if (program != null && !program.equals("")) {
				sql += "and programcode='" + program + "' ";
			}
			if (academicYear != null && !academicYear.equals("")) {
				sql += "and academicyear='" + academicYear + "' ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and t.bookdeliverdate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') " + "and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			if (status != null && status.equals("3")) {// 缺書
				sql += "and notenoughqty > '0' ";
			}
			sql += "order by t.facultycode,t.academicyear,t.programcode,t.orderseqno,t.studentno,t.isbn asc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ViewStudOrdInfo info = new ViewStudOrdInfo();
				info.setChineseName(rs.getString("chinesename"));
				info.setStudentNo(rs.getString("studentno"));
				info.setFacultyName(rs.getString("fachnname"));
				info.setAcademicYear(rs.getInt("academicyear"));
				info.setProgram(rs.getString("progchnname"));
				info.setOrderSeqNo(rs.getString("orderseqno"));
				info.setTitle(rs.getString("title"));
				info.setAuthor(rs.getString("author"));
				info.setPublisher(rs.getString("publisher"));
				info.setEdition(rs.getString("edition"));
				info.setPublishYear(rs.getString("publishyear"));
				info.setIsbn(rs.getString("isbn"));
				info.setOrderEdQty(rs.getInt("orderedqty"));
				info.setNotEnoughQty(rs.getInt("notenoughqty"));
				info.setReceivedQty(rs.getInt("receivedqty"));
				info.setFuturePriceMOP(rs.getDouble("futurepricemop"));
				info.setFuturePriceRMB(rs.getDouble("futurepricermb"));
				info.setNetPriceMOP(rs.getDouble("netpricemop"));
				info.setNetPriceRMB(rs.getDouble("netpricermb"));
				info.setPaidAmount(rs.getDouble("paidamount"));
				info.setPaidCurrency(rs.getString("paidcurrency"));
				info.setPaidDate(rs.getTimestamp("paiddate"));
				info.setCashier(rs.getString("cashier"));
				info.setBookDeliverDate(rs.getTimestamp("bookdeliverdate"));
				info.setBookDeliver(rs.getString("bookdeliver"));
				info.setContactNo(rs.getString("contactno"));
				info.setEmail(rs.getString("email"));
				info.setStatus(rs.getString("status"));
				info.setNewContactNo(rs.getString("newcontact"));
				info.setNewEmail(rs.getString("newemail"));
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
	
	public List getDeliverBookRecord(Connection conn, String intake,
			String faculty, String program, String academicYear, String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		String sql = null;
		PreparedStatement pstmt = null;
		try {
			sql = "select st.chinesename, (case when st.studentno is null then st.applicantno else st.studentno end) as studentno, "+
			"st.facultycode, f.chinesename as fachnname, st.programcode, p.chinesename as progchnname, st.academicyear, st.status, st.contactno, st.email,"+
				"t.orderseqno,  t.quantity, "+
				"tc.contactno as newcontactno, tc.email as newemail, "+
				"o.upddate, o.chauid, tt.notenoughqty "+
				"from tstudent st "+
				"inner join tprogram p on st.programcode = p.programcode "+
				"inner join tfaculty f on st.facultycode = f.facultycode "+
				"inner join ( "+
				"            select orderintake, studentno, orderseqno, sum(quantity) as quantity "+
				"            from tbookdeliver "+
				"            where actind = 'Y' ";
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ";
			}
			sql += "group by orderintake, studentno, orderseqno "+
				"            ) t on (t.studentno = st.studentno or st.applicantno = t.studentno) "+
				"inner join torder o on o.orderseqno = t.orderseqno and o.actind = 'Y' "+
				"left outer join ( " +
				"                 select orderintake, orderseqno, sum(notenoughqty) as notenoughqty "+
				"                 from tordetail where actind = 'Y' and notenoughqty > 0 "+
				"                 group by orderintake, orderseqno "+
				"                ) tt on tt.orderseqno = t.orderseqno "+
				"left outer join tcontact tc on (tc.studentno = st.studentno or tc.studentno = st.applicantno) "+
				"where 1=1 ";
			if (intake != null && !intake.equals("")) {
				sql += "and t.orderintake = '" + intake + "' ";
			}
			if (faculty != null && !faculty.equals("")) {
				sql += "and st.facultycode = '" + faculty + "' ";
			}
			if (program != null && !program.equals("")) {
				sql += "and st.programcode = '" + program + "' ";
			}
			if (academicYear != null && !academicYear.equals("")) {
				sql += "and st.academicyear = '" + academicYear + "' ";
			}
			sql += "order by st.facultycode, st.academicyear, st.programcode, t.orderseqno asc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ViewStudOrdInfo info = new ViewStudOrdInfo();
				info.setChineseName(rs.getString("chinesename"));
				info.setStudentNo(rs.getString("studentno"));
				info.setFacultyName(rs.getString("fachnname"));
				info.setProgram(rs.getString("progchnname"));
				info.setAcademicYear(rs.getInt("academicyear"));				
				info.setOrderSeqNo(rs.getString("orderseqno"));
				info.setOrderEdQty(rs.getInt("quantity"));
				info.setPaidDate(rs.getTimestamp("upddate"));
				info.setCashier(rs.getString("chauid"));
				info.setNotEnoughQty(rs.getInt("notenoughqty"));
				info.setStatus(rs.getString("status"));
				info.setContactNo(rs.getString("contactno"));
				info.setEmail(rs.getString("email"));				
				info.setNewContactNo(rs.getString("newcontactno"));
				info.setNewEmail(rs.getString("newemail"));
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

}
