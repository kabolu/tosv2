package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.must.tos.dao.OrdBooksReportDAO;

public class OrdBooksReportDAOImpl implements OrdBooksReportDAO {

	public ResultSet getNewsOrdBooksBySeqNo(Connection conn,
			String facultyCode, String programCode, String year, String intake,
			String studType, String region, String paidStatus, String isDiffer,
			String fromDate, String toDate) {
		Statement st = null;
		ResultSet rs = null;
		try {
			String strSql = "select tb.isbn, tb.title, tb.author, tb.publishYear, tb.publisher, "
					+ "o.studentno, st.chinesename, st.englishname, o.orderintake, "
					+ "tp.chineseName as programChineseName, st.facultyCode, st.programCode, st.academicYear, "
					+ "od.confirmQty, o.orderseqno "
					+ "from  torder o "
					+ "inner join tordetail od on o.orderseqno=od.orderseqno and od.orderintake=o.orderintake and od.actInd='Y' and od.confirmQty>0 and od.notenoughqty is null "
					+ "inner join tbook tb on od.isbn = tb.isbn and tb.actInd = 'Y' "
					+ "inner join tstudent st on (o.studentno = st.applicantno ) "; //or o.studentno = st.studentno
			if (facultyCode != null && !facultyCode.equals("")) {
				strSql += "and st.facultyCode = '" + facultyCode + "' ";
			}
			if (programCode != null && !programCode.equals("")) {
				strSql += " and st.programCode = '" + programCode + "' ";
			}
			if (studType.equals("new")) {
				if (region != null && !region.equals("all")) {
					strSql += " and st.region='" + region + "' and st.academicyear=0 ";
				} else {
					strSql += " and st.academicyear=0 ";
				}
			}
			strSql += "inner join tprogram tp on st.programCode = tp.programCode "
					+
					// inner join vbookfeeforapp v on
					// o.studentno=nvl2(v.studentno, v.studentno, v.applicantno)
					// and v.paidintake=o.orderintake
					"where o.actInd = 'Y' ";
			StringBuffer sql = new StringBuffer(1000);
			sql.append(strSql);
			if (intake != null && !intake.equals("")) {
				sql.append("and o.orderIntake = '" + intake + "' ");
			}

			/*
			 * if(paidStatus!=null && paidStatus.equals("Y")){ sql.append(" and
			 * v.paidamount is not null and v.paidamount>0 "); }else
			 * if(paidStatus!=null && paidStatus.equals("N")){ sql.append(" and
			 * v.paidamount=0 "); }
			 */
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql.append("and o.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			sql.append("order by st.facultyCode, st.programCode, st.academicYear, o.orderseqno, tb.sortby asc, tb.isbn ");
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rs;
	}

	public ResultSet getOrdBooksBySeqNo(Connection conn, String facultyCode, String programCode, String year, String intake, String studType,
			String region, String paidStatus, String fromDate, String toDate) {
		Statement st = null;
		ResultSet rs = null;
		try {
			String strSql = "select tb.isbn, tb.title, tb.author, tb.publishYear, tb.publisher, "
					+ "o.studentno, st.chinesename, st.englishname, o.orderintake, "
					+ "tp.chineseName as programChineseName, st.facultyCode, st.programCode, st.academicYear, "
					+ "od.confirmQty, o.orderseqno "
					+ "from torder o "
					+ "inner join tordetail od on  o.orderseqno=od.orderseqno and od.actInd = 'Y' and od.confirmQty > 0 and od.notenoughqty is null "
					+ "inner join tbook tb on od.isbn = tb.isbn and tb.actInd = 'Y' "
					+ "inner join tstudent st on st.studentno is not NULL and o.studentno = st.studentno ";
			if (studType != null && studType.equals("old")) {
				if (year != null && !year.equals("")) {
					strSql += " and st.academicYear = " + Integer.parseInt(year) + " ";
				} else {
					strSql += " and st.academicYear > 0 ";
				}
			}
			strSql += "inner join tprogram tp on st.programCode = tp.programCode where o.actInd = 'Y' ";

			StringBuffer sql = new StringBuffer(1000);
			sql.append(strSql);
			if (facultyCode != null && !facultyCode.equals("")) {
				sql.append(" and st.facultyCode = '" + facultyCode + "' ");
			}
			if (programCode != null && !programCode.equals("")) {
				sql.append(" and st.programCode = '" + programCode + "' ");
			}
			if (intake != null && !intake.equals("")) {
				sql.append(" and o.orderIntake = '" + intake + "' ");
			}
			if (paidStatus != null && !paidStatus.equals("")) {
				sql.append(" and o.paidstatus<>'" + paidStatus + "' ");
			}

			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql.append(" and o.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			sql.append("order by st.facultyCode,st.programCode,st.academicYear,o.orderseqno, tb.sortby asc, tb.isbn ");
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rs;
	}

	public ResultSet getOrdBooks(Connection con, String facultyCode, String programCode, String year, String intake, String studType,
			String region, String paidStatus, String fromDate, String toDate) {
		Statement st = null;
		ResultSet rs = null;
		try {
			String strSql = "select tb.isbn, tb.title, tb.author, tb.publishYear, tb.publisher, tb.sortby, "
					+ "o.studentno, st.chinesename, st.englishname, o.orderintake, "
					+ "tp.chineseName as programChineseName, st.facultyCode, st.programCode, st.academicYear, "
					+ "sum(od.confirmQty) as confirmQty "
					+ "from torder o "
					+ "inner join tordetail od on o.orderseqno=od.orderseqno and od.actInd = 'Y' and od.confirmQty > 0 and od.notenoughqty is null ";
			strSql += "inner join tbook tb on od.isbn = tb.isbn and tb.actInd = 'Y' "
					+ "inner join tstudent st on st.studentno is not NULL and o.studentno = st.studentno ";
			if (facultyCode != null && !facultyCode.equals("")) {
				strSql += " and st.facultyCode = '" + facultyCode + "' ";
			}
			if (programCode != null && !programCode.equals("")) {
				strSql += " and st.programCode = '" + programCode + "' ";
			}
			if (studType != null && studType.equals("old")) {
				if (year != null && !year.equals("")) {
					strSql += " and st.academicYear = " + Integer.parseInt(year) + " ";
				} else {
					strSql += " and st.academicYear > 0 ";
				}
			}
			strSql += "inner join tprogram tp on st.programCode = tp.programCode "
					+ "where o.actInd = 'Y' ";
			StringBuffer sql = new StringBuffer(1000);
			sql.append(strSql);
			if (intake != null && !intake.equals("")) {
				sql.append(" and o.orderIntake = '" + intake + "' ");
			}
			if (paidStatus != null && !paidStatus.equals("")) {
				sql.append(" and o.paidstatus<>'" + paidStatus + "' ");
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql.append(" and o.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			sql.append(" group by tb.isbn,tb.title, tb.author, tb.publishYear, tb.publisher, tb.sortby, "
							+ "o.studentno, st.chinesename, st.englishname, o.orderintake, tp.chineseName, "
							+ "st.facultyCode, st.programCode, st.academicYear "
							+ "order by st.facultyCode, st.programCode, st.academicYear, studentno, tb.sortby asc, tb.isbn ");
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rs;
	}

	public ResultSet getNewsOrdBooks(Connection con, String facultyCode, String programCode, String year, String intake, String studType,
			String region, String paidStatus, String isDiffer, String fromDate, String toDate) {
		Statement st = null;
		ResultSet rs = null;
		try {
			String strSql = "select tb.isbn, tb.title, tb.author, tb.publishYear, tb.publisher, tb.sortby, "
					+ "o.studentno, st.chinesename, st.englishname, o.orderintake, "
					+ "tp.chineseName as programChineseName, st.facultyCode, st.programCode, st.academicYear, "
					+ "sum(od.confirmQty) as confirmQty "
					+ "from  torder o "
					+ "inner join tordetail od on o.orderseqno=od.orderseqno and od.actInd = 'Y' and od.confirmQty > 0 and od.notenoughqty is null ";
			strSql += "inner join tbook tb on od.isbn = tb.isbn and tb.actInd = 'Y' "
					+ "inner join tstudent st on o.studentno = st.studentNo ";
			if (facultyCode != null && !facultyCode.equals("")) {
				strSql += " and st.facultyCode = '" + facultyCode + "' ";
			}
			if (programCode != null && !programCode.equals("")) {
				strSql += " and st.programCode = '" + programCode + "' ";
			}
			if (studType.equals("new")) {
				if (region != null && !region.equals("all")) {
					strSql += " and st.region='" + region + "' and st.academicyear=0 ";
				} else {
					strSql += " and st.academicyear=0 ";
				}
			}

			strSql += "inner join tprogram tp on st.programCode = tp.programCode ";
			/*
			 * "inner join vbookfeeforapp v on o.studentno=nvl2(v.studentno, v.studentno, v.applicantno) " ; 
			 * if(intake != null && !intake.equals("")) { strSql += " and v.paidintake = '"+intake+"' "; }
			 * if(paidStatus!=null && paidStatus.equals("Y")){ strSql += " and v.paidamount is not null and v.paidamount>0 "; }
			 * else if(paidStatus!=null && paidStatus.equals("N")){ strSql += " and v.paidamount=0 "; }
			 */
			strSql += "where o.actInd = 'Y' ";
			StringBuffer sql = new StringBuffer(1000);
			sql.append(strSql);

			if (intake != null && !intake.equals("")) {
				sql.append(" and o.orderIntake = '" + intake + "' ");
			}

			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql.append(" and o.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') " +
						"and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			sql.append(" group by tb.isbn,tb.title, tb.author, tb.publishYear, tb.publisher, tb.sortby, "
							+ "o.studentno, st.chinesename, st.englishname, o.orderintake, tp.chineseName, "
							+ "st.facultyCode, st.programCode, st.academicYear "
							+ "order by st.facultyCode, st.programCode, st.academicYear, studentno, tb.sortby asc, tb.isbn ");
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rs;
	}

	/**
	 * @param con
	 * @param studentNo
	 * @param intake
	 * @return
	 */
	public ResultSet isExistStuOrdBooks(Connection conn, String studentNo,	String intake, String paidStatus) {
		boolean result = false;
		Statement st = null;
		ResultSet rs = null;
		try {
			String strSql = "select tb.isbn, tb.title, tb.author, tb.publishYear, tb.publisher, tb.sortby, "
					+ "nvl(st.studentno, st.applicantno) as studentno, st.chinesename, st.englishname, o.orderintake, "
					+ "tp.chineseName as programChineseName, st.facultyCode, st.programCode, st.academicYear, "
					+ "sum(od.confirmQty) as confirmQty "
					+ "from  torder o "
					+ "inner join tordetail od on o.orderseqno=od.orderseqno and od.actInd = 'Y' and od.confirmQty > 0 and od.notenoughqty is null "
					+ "inner join tbook tb on od.isbn = tb.isbn and tb.actInd = 'Y' "
					+ "inner join tstudent st on ((st.studentNo is not null and st.studentNo = o.studentno) or st.applicantno = o.studentno) "
					+ "and ((st.studentNo is not null and st.studentNo='" + studentNo + "') or st.applicantNo='" + studentNo + "') "
					+ "inner join tprogram tp on st.programCode = tp.programCode "
					+ "where o.actInd = 'Y' and o.paidstatus<>'" + paidStatus + "' " + "";
			StringBuffer sql = new StringBuffer(1000);
			sql.append(strSql);
			sql.append(" and o.orderIntake='" + intake + "' ");
			sql.append(" group by tb.isbn,tb.title, tb.author, tb.publishYear, tb.publisher, tb.sortby, ");
			sql.append(" nvl(st.studentno, st.applicantno), st.chinesename, st.englishname, o.orderintake, tp.chineseName, "
							+ "st.facultyCode, st.programCode, st.academicYear "
							+ "order by st.facultyCode, st.programCode, st.academicYear, studentno, tb.sortby, tb.isbn ");
			
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return rs;
	}

	public boolean isExistNewStuOrdBooks(Connection con, String studentNo,
			String intake) {
		boolean result = false;
		Statement st = null;
		ResultSet rs = null;
		try {
			String strSql = "select tp.chineseName as programChineseName, "
					+ "(case when st.studentNo is null then st.applicantNo else st.studentNo end) as studentNo, "
					+ "st.chinesename, st.englishname, "
					+ "o.orderintake, o.orderseqno, o.paidstatus, "
					+ "tb.title,tb.isbn, tb.author, tb.publishYear, tb.publisher, "
					+ "od.confirmQty "
					+ "from torder o "
					+ "inner join tordetail od on  o.studentNo = od.studentNo and o.orderIntake = od.orderIntake "
					+ "inner join tbook tb on od.isbn = tb.isbn "
					+ "inner join tstudent st on (st.studentNo = o.studentno or st.applicantno = o.studentno) "
					+ "inner join tprogram tp on st.programCode = tp.programCode "
					+ "where  o.actInd = 'Y' and od.actInd = 'Y' and tb.actInd = 'Y' "
					+ "and od.confirmQty > 0 and od.notenoughqty is null " + "";
			StringBuffer sql = new StringBuffer(1000);
			sql.append(strSql);
			sql.append(" and (st.studentNo='" + studentNo + "' or st.applicantno='" + studentNo + "') and o.orderIntake='" + intake + "' ");
			sql.append(" order by st.facultyCode ,st.academicYear,st.programCode,o.orderseqno,od.isbn ");
			st = con.createStatement();
			rs = st.executeQuery(sql.toString());
			if (rs.next()) {
				result = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
