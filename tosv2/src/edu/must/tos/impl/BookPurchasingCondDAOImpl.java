package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookPurchasingCond;
import edu.must.tos.dao.BookPurchasingCondDAO;

public class BookPurchasingCondDAOImpl implements BookPurchasingCondDAO {

	public List getPurchasingCond(Connection conn, String intake, String BfromDate,
			String BtoDate, String supplierNo) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		try{
			String sql = "select credate,creuid,fromdate,todate,orderby " +
					"from tbookpurchasingcond " +
					"where orderby=? and intake=? " ;
			if(BfromDate!=null && !BfromDate.equals("ALL") && BtoDate!=null && !BtoDate.equals("ALL")){
				sql += "and (((fromdate between ? and ?) or (todate between ? and ?))" +
					"or ((? between fromdate and todate) or (? between fromdate and todate))" +
					"or (fromdate=? and todate=? )) ";
			}
			sql += "order by credate desc ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, supplierNo);
			pstmt.setString(2, intake);
			if(BfromDate!=null && !BfromDate.equals("ALL") && BtoDate!=null && !BtoDate.equals("ALL")){
				pstmt.setString(3, BfromDate);
				pstmt.setString(4, BtoDate);
				pstmt.setString(5, BfromDate);
				pstmt.setString(6, BtoDate);
				pstmt.setString(7, BfromDate);
				pstmt.setString(8, BtoDate);
				pstmt.setString(9, BfromDate);
				pstmt.setString(10, BtoDate);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				BookPurchasingCond cond = new BookPurchasingCond();
				cond.setCreDate(rs.getTimestamp("credate"));
				cond.setCreUid(rs.getString("creuid"));
				cond.setFromDate(rs.getString("fromdate"));
				cond.setToDate(rs.getString("todate"));
				cond.setOrderBy(rs.getString("orderby"));
				list.add(cond);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return list;
	}

	public boolean addBookPurchasingCond(Connection conn, BookPurchasingCond cond) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try{
			String sql = "insert into tbookpurchasingcond (credate, creuid, fromdate, todate, orderby, intake) " +
					"values (?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new java.sql.Timestamp(cond.getCreDate().getTime()));
			pstmt.setString(2, cond.getCreUid());
			pstmt.setString(3, cond.getFromDate());
			pstmt.setString(4, cond.getToDate());
			pstmt.setString(5, cond.getOrderBy());
			pstmt.setString(6, cond.getIntake());
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}finally{
			if(pstmt!=null)
				pstmt.close();
		}
		return flag;
	}

}
