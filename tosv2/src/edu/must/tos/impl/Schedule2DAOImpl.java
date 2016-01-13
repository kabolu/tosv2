package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Schedule2;
import edu.must.tos.dao.Schedule2DAO;

public class Schedule2DAOImpl implements Schedule2DAO {

	public List getSchedule2(Connection conn, Schedule2 sch2)
			throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "";
		List list = new ArrayList();
		try{
			sql = "select studentno,intake,periodno,actind,type,orderseqno from TSchedule2 where 1=1 ";
			if(sch2.getOrderSeqNo()!=0){
				sql += "and orderseqno="+sch2.getOrderSeqNo()+" ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Schedule2 schedule2 = new Schedule2();
				schedule2.setStudentNo(rs.getString("studentno"));
				schedule2.setIntake(rs.getString("intake"));
				schedule2.setPeriodNo(rs.getString("periodno"));
				schedule2.setActInd(rs.getString("actind"));
				schedule2.setType(rs.getString("type"));
				schedule2.setOrderSeqNo(rs.getInt("orderseqno"));
				list.add(schedule2);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public boolean checkPeriodNo(Connection conn, String type, String periodNo)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select 1 from tschedule2 sch2 "
					+ "where sch2.periodno=? and sch2.actind=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(periodNo));
			pstmt.setString(2, "Y");
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
	
	public boolean updSchedule2(Connection conn, Schedule2 sc) throws Exception{
		boolean flag = true;
		PreparedStatement pstmt = null;
		String sql = null;
		try{
			sql = "update tschedule2 set actind=?, upduid=?, upddate=? where orderseqno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sc.getActInd());
			pstmt.setString(2, sc.getUpdUid());
			pstmt.setDate(3, new java.sql.Date(sc.getUpdDate().getTime()));
			pstmt.setInt(4, sc.getOrderSeqNo());
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}

}
