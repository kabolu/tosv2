package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.VBookFeeForApp;
import edu.must.tos.bean.VBookFeeForAppBean;
import edu.must.tos.dao.VBookFeeForAppDAO;

public class VBookFeeForAppDAOImpl implements VBookFeeForAppDAO {

	public List getNewGradStudAccount(Connection conn, String intake) throws Exception{
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select t.applicantno, v.studentno, v.paidintake, v.paidcurrency, v.prepaid, v.paidamount, v.duedate "+
					"from tstudent t "+
					"left outer join VTUITIONFEEFORAPP v on v.applicantno = t.applicantno and v.paidintake = '"+intake+"' and paidamount <> 0 "+ 
					"where t.applicantno like '"+intake+"%' and t.stud_grp in ('03', '04', '05', '06') "+
					"order by t.applicantno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				VBookFeeForApp vBookFeeForApp = new VBookFeeForApp();
				vBookFeeForApp.setApplicantNo(rs.getString("applicantno"));
				vBookFeeForApp.setStudentNo(rs.getString("studentno"));
				vBookFeeForApp.setPaidIntake(rs.getString("paidintake"));
				vBookFeeForApp.setPaidCurrency(rs.getString("paidcurrency"));
				vBookFeeForApp.setPrePaid(rs.getDouble("prepaid"));
				vBookFeeForApp.setPaidAmount(rs.getDouble("paidamount"));
				vBookFeeForApp.setDueDate(rs.getDate("duedate"));
				list.add(vBookFeeForApp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return list;
	}
	public List getUnDifferList(Connection conn, String intake)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select distinct applicantno,v.paidamount as vpaidamount," +
					"o.studentno,o.orderintake,o.orderseqno,o.paidcurrency,o.paidamount,o.paidstatus," +
					"o.amercemount,o.fineforlatepay,o.difference,o.shippingfee," +
					"od.isbn,od.confirmqty,od.withdrawqty,od.withdrawqty2,od.notenoughqty " +
					"from vbookfeeforapp v " +
					"left outer join torder o on o.studentno=v.applicantno and o.actind='Y' " +
					"left outer join tordetail od on od.orderseqno=o.orderseqno and od.actind='Y' " +
					"where v.paidintake=? and v.paidamount>0 order by applicantno";
			//and o.paidcurrency is not null and paidstatus<>? 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			//pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				VBookFeeForAppBean bean = new VBookFeeForAppBean();
				bean.setApplicantNo(rs.getString("applicantno"));
				bean.setvPaidAmount(rs.getDouble("vpaidamount"));
				bean.setStudentNo(rs.getString("studentno"));
				bean.setPaidIntake(rs.getString("orderintake"));
				bean.setOrderSeqNo(rs.getString("orderseqno"));
				bean.setPaidCurrency(rs.getString("paidcurrency"));
				bean.setPaidAmount(rs.getDouble("paidamount"));
				bean.setPaidstatus(rs.getString("paidstatus"));
				bean.setAmerceMount(rs.getDouble("amercemount"));
				bean.setFineForLatePay(rs.getDouble("fineforlatepay"));
				bean.setDifference(rs.getDouble("difference"));
				bean.setShippingFee(rs.getDouble("shippingfee"));
				bean.setIsbn(rs.getString("isbn"));
				bean.setConfirmQty(rs.getInt("confirmqty"));
				bean.setWithdrawQty(rs.getInt("withdrawqty"));
				bean.setWithdrawqty2(rs.getInt("withdrawqty2"));
				bean.setNotenoughqty(rs.getInt("notenoughqty"));
				resultList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return resultList;
	}

	public List getMoreRecordList(Connection conn, String intake)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select applicantno,studentno,paidintake,paidcurrency,prepaid,paidamount,duedate " +
					"from vbookfeeforapp " +
					"where paidintake=? and applicantno in " +
					"( select applicantno " +
					"from vbookfeeforapp where paidintake=? group by applicantno having count(applicantno) > 1 " +
					") order by applicantno,studentno,duedate";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, intake);
			rs = pstmt.executeQuery();
			while(rs.next()){
				VBookFeeForApp vBookFeeForApp = new VBookFeeForApp();
				vBookFeeForApp.setApplicantNo(rs.getString("applicantno"));
				vBookFeeForApp.setStudentNo(rs.getString("studentno"));
				vBookFeeForApp.setPaidIntake(rs.getString("paidintake"));
				vBookFeeForApp.setPaidCurrency(rs.getString("paidcurrency"));
				vBookFeeForApp.setPrePaid(rs.getDouble("prepaid"));
				vBookFeeForApp.setPaidAmount(rs.getDouble("paidamount"));
				vBookFeeForApp.setDueDate(rs.getDate("duedate"));
				resultList.add(vBookFeeForApp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				pstmt.close();
			}
		}
		return resultList;
	}

	public List getVBookFeeForAppList(Connection conn, String paidStatus, String date,
			String tranDate, String intake) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select t.applicantno,t.studentno,t.paidintake,t.paidcurrency,t.paidamount,t.prepaid,t.duedate, "
					+ "st.chinesename,st.englishname,st.facultycode,st.programcode,st.majorcode,st.acceptance,st.region "
					+ "from vbookfeeforapp t, tstudent st "
					+ "where st.applicantno = t.applicantno ";
			if (tranDate != null && !tranDate.equals("")) {
				sql += "and t.trandate < to_date('" + tranDate + " 23:59:59', 'yyyy-MM-dd hh24:mi:ss') ";
				if (intake != null && !intake.equals("")) {
					sql += "and t.paidintake='" + intake + "' ";
				}
			} else {
				if (intake != null && !intake.equals("")) {
					// and t.applicantno like '"+intake+"%'澳門那邊需要這個，本機測試可以不用
					//sql += "and t.paidintake='" + intake + "' and t.applicantno like '" + intake + "%' ";
					sql += "and t.paidintake='" + intake + "' ";
				}
			}
			if (paidStatus != null && paidStatus.equals("Y")) {
				sql += "and t.paidamount>0 ";
			}
			if (date != null && !date.equals("")) {
				sql += "and t.duedate < to_date('" + date + " 23:59:59', 'yyyy-MM-dd hh24:mi:ss') ";
			}
			
			if (intake != null && !intake.equals("")) {
				// and t.applicantno like '"+intake+"%'澳門那邊需要這個，本機測試可以不用
				//sql += "and t.paidintake='" + intake + "' and t.applicantno like '" + intake + "%' ";
				sql += "and t.paidintake='" + intake + "' ";
			}
			sql += "order by t.duedate, t.applicantno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				VBookFeeForAppBean fee = new VBookFeeForAppBean();
				if (rs.getString("studentno") == null) {
					fee.setStudentNo("");
				} else {
					fee.setStudentNo(rs.getString("studentno"));
				}
				if (rs.getString("applicantno") == null) {
					fee.setApplicantNo("");
				} else {
					fee.setApplicantNo(rs.getString("applicantno"));
				}
				fee.setPaidAmount(rs.getDouble("paidamount"));
				fee.setPaidCurrency(rs.getString("paidcurrency"));
				fee.setPaidIntake(rs.getString("paidintake"));
				fee.setPrePaid(rs.getDouble("prepaid"));
				fee.setDueDate(rs.getDate("duedate"));

				if (rs.getString("chinesename") == null) {
					fee.setChinesename("");
				} else {
					fee.setChinesename(rs.getString("chinesename"));
				}
				if (rs.getString("englishname") == null) {
					fee.setEnglishname("");
				} else {
					fee.setEnglishname(rs.getString("englishname"));
				}
				if (rs.getString("facultycode") == null) {
					fee.setFacultycode("");
				} else {
					fee.setFacultycode(rs.getString("facultycode"));
				}
				if (rs.getString("programcode") == null) {
					fee.setProgramcode("");
				} else {
					fee.setProgramcode(rs.getString("programcode"));
				}
				if (rs.getString("majorcode") == null) {
					fee.setMajorcode("");
				} else {
					fee.setMajorcode(rs.getString("majorcode"));
				}
				if (rs.getString("acceptance") == null) {
					fee.setAcceptance("");
				} else {
					fee.setAcceptance(rs.getString("acceptance"));
				}
				if (rs.getString("region") == null) {
					fee.setRegion("");
				} else {
					fee.setRegion(rs.getString("region"));
				}
				resultList.add(fee);
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
		return resultList;
	}

	public List getVBookFeeForAppList(Connection conn, VBookFeeForApp v)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select t.applicantno,t.studentno,t.paidintake,t.paidcurrency,t.paidamount,t.prepaid "
					+ "from vbookfeeforapp t where 1=1 ";
			if (v.getPaidIntake() != null && !v.getPaidIntake().equals("")) {
				sql += "and t.paidintake = '" + v.getPaidIntake() + "' ";
			}
			if (!v.getStudentNo().equals("") && !v.getApplicantNo().equals("")) {
				sql += "and (t.studentno ='" + v.getStudentNo() + "' or t.applicantno = '" + v.getApplicantNo() + "') ";
			} else if (!v.getStudentNo().equals("") && v.getApplicantNo().equals("")) {
				sql += "and t.studentno = '" + v.getStudentNo() + "' ";
			} else if (v.getStudentNo().equals("") && !v.getApplicantNo().equals("")) {
				sql += "and t.applicantno = '" + v.getApplicantNo() + "' ";
			}
			sql += "order by t.studentno,t.applicantno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				VBookFeeForApp fee = new VBookFeeForApp();
				if (rs.getString("studentno") == null) {
					fee.setStudentNo("");
				} else {
					fee.setStudentNo(rs.getString("studentno"));
				}
				if (rs.getString("applicantno") == null) {
					fee.setApplicantNo("");
				} else {
					fee.setApplicantNo(rs.getString("applicantno"));
				}
				fee.setPaidAmount(rs.getDouble("paidamount"));
				fee.setPaidCurrency(rs.getString("paidcurrency"));
				fee.setPaidIntake(rs.getString("paidintake"));
				fee.setPrePaid(rs.getDouble("prepaid"));
				resultList.add(fee);
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
		return resultList;
	}

	public List getVBookFeeForAppAccuSumm(Connection conn, String intake)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select paidcurrency, sum(paidamount) as totalamount from vbookfeeforapp "
					+ "where paidintake=? " 
					+ "group by paidcurrency";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				VBookFeeForAppBean bean = new VBookFeeForAppBean();
				bean.setPaidCurrency(rs.getString("paidcurrency"));
				bean.setPaidAmount(rs.getDouble("totalamount"));
				list.add(bean);
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

	public VBookFeeForApp getVBookFeeForAppInfo(Connection conn,
			VBookFeeForApp v) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select applicantno, studentno, paidintake, paidcurrency, paidamount, prepaid "
					+ "from vbookfeeforapp where (applicantno=? or studentno=?) and paidintake=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, v.getApplicantNo());
			pstmt.setString(2, v.getStudentNo());
			pstmt.setString(3, v.getPaidIntake());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				v.setApplicantNo(rs.getString("applicantno"));
				v.setStudentNo(rs.getString("studentno"));
				v.setPaidIntake(rs.getString("paidintake"));
				v.setPaidCurrency(rs.getString("paidcurrency"));
				v.setPaidAmount(rs.getDouble("paidamount"));
				v.setPrePaid(rs.getDouble("prepaid"));
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
		return v;
	}

}
