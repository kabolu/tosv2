package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.ShippingFee;
import edu.must.tos.dao.ShippingFeeDAO;
import edu.must.tos.util.UsePage;

public class ShippingFeeDAOImpl implements ShippingFeeDAO {

	public List getShippingFeeList(Connection conn, String intake, String startDate, String endDate)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select feeno,intake,companyname,prnum,feetype,rmbprice,mopprice,tomopprice,invoicedate,remarks," +
					"actind,credate,creuid,upddate,upduid " +
					"from tshippingfee where actind='Y' ";
			if(intake != null){
				sql += "and intake = '"+intake+"' ";
			}
			if(startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)){
				sql += "and invoicedate between '"+startDate+"' and '"+endDate+"' ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ShippingFee fee = new ShippingFee();
				fee.setFeeNo(rs.getInt("feeno"));
				fee.setIntake(rs.getString("intake"));
				fee.setCompanyName(rs.getString("companyname"));
				fee.setPrNum(rs.getString("prnum"));
				fee.setFeeType(rs.getString("feetype"));
				fee.setRmbPrice(rs.getDouble("rmbprice"));
				fee.setMopPrice(rs.getDouble("mopprice"));
				fee.setToMopPrice(rs.getDouble("tomopprice"));
				fee.setInvoiceDate(rs.getString("invoicedate"));
				fee.setRemarks(rs.getString("remarks"));
				fee.setActind(rs.getString("actind"));
				fee.setCreDate(rs.getTimestamp("credate"));
				fee.setCreUid(rs.getString("creuid"));
				fee.setUpdDate(rs.getTimestamp("upddate"));
				fee.setUpdUid(rs.getString("upduid"));
				list.add(fee);
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

	public ShippingFee getShippingFeeByFeeNo(Connection conn, int feeNo)
			throws Exception {
		ShippingFee shippingFee = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select feeno,intake,companyname,prnum,feetype,rmbprice,mopprice,tomopprice,invoicedate,remarks," +
					"actind,credate,creuid,upddate,upduid " +
					"from tshippingfee where feeNo=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, feeNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				shippingFee = new ShippingFee();
				shippingFee.setFeeNo(rs.getInt("feeno"));
				shippingFee.setIntake(rs.getString("intake"));
				shippingFee.setCompanyName(rs.getString("companyname"));
				shippingFee.setPrNum(rs.getString("prnum"));
				shippingFee.setFeeType(rs.getString("feetype"));
				shippingFee.setRmbPrice(rs.getDouble("rmbprice"));
				shippingFee.setMopPrice(rs.getDouble("mopprice"));
				shippingFee.setToMopPrice(rs.getDouble("tomopprice"));
				shippingFee.setInvoiceDate(rs.getString("invoicedate"));
				shippingFee.setRemarks(rs.getString("remarks"));
				shippingFee.setActind(rs.getString("actind"));
				shippingFee.setCreDate(rs.getTimestamp("credate"));
				shippingFee.setCreUid(rs.getString("creuid"));
				shippingFee.setUpdDate(rs.getTimestamp("upddate"));
				shippingFee.setUpdUid(rs.getString("upduid"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return shippingFee;
	}

	public boolean addShippingFee(Connection conn, ShippingFee shippingFee)
			throws Exception {
		boolean flag = true;
		PreparedStatement pstmt = null;
		try{
			String sql = "insert into tshippingfee (feeno,intake,companyname,prnum,feetype,rmbprice,mopprice,tomopprice,invoicedate,remarks," +
				"actind,credate,creuid,upddate,upduid) " +
				"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, shippingFee.getFeeNo());
			pstmt.setString(2, shippingFee.getIntake());
			pstmt.setString(3, shippingFee.getCompanyName());
			pstmt.setString(4, shippingFee.getPrNum());
			pstmt.setString(5, shippingFee.getFeeType());
			pstmt.setDouble(6, shippingFee.getRmbPrice());
			pstmt.setDouble(7, shippingFee.getMopPrice());
			pstmt.setDouble(8, shippingFee.getToMopPrice());
			pstmt.setString(9, shippingFee.getInvoiceDate());
			pstmt.setString(10, shippingFee.getRemarks());
			pstmt.setString(11, shippingFee.getActind());
			pstmt.setTimestamp(12, new java.sql.Timestamp(shippingFee.getCreDate().getTime()));
			pstmt.setString(13, shippingFee.getCreUid());
			pstmt.setTimestamp(14, new java.sql.Timestamp(shippingFee.getUpdDate().getTime()));
			pstmt.setString(15, shippingFee.getUpdUid());
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}else{
				flag = false;
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

	public boolean updateShippingFee(Connection conn, ShippingFee shippingFee)
			throws Exception {
		boolean flag = true;
		PreparedStatement pstmt = null;
		try{
			String sql = "update tshippingfee set intake=?,companyname=?,prnum=?,feetype=?,rmbprice=?,mopprice=?,tomopprice=?,invoicedate=?,remarks=?," +
				"actind=?,credate=?,creuid=?,upddate=?,upduid=? " +
				"where feeno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, shippingFee.getIntake());
			pstmt.setString(2, shippingFee.getCompanyName());
			pstmt.setString(3, shippingFee.getPrNum());
			pstmt.setString(4, shippingFee.getFeeType());
			pstmt.setDouble(5, shippingFee.getRmbPrice());
			pstmt.setDouble(6, shippingFee.getMopPrice());
			pstmt.setDouble(7, shippingFee.getToMopPrice());
			pstmt.setString(8, shippingFee.getInvoiceDate());
			pstmt.setString(9, shippingFee.getRemarks());
			pstmt.setString(10, shippingFee.getActind());
			pstmt.setTimestamp(11, new java.sql.Timestamp(shippingFee.getCreDate().getTime()));
			pstmt.setString(12, shippingFee.getCreUid());
			pstmt.setTimestamp(13, new java.sql.Timestamp(shippingFee.getUpdDate().getTime()));
			pstmt.setString(14, shippingFee.getUpdUid());
			pstmt.setInt(15, shippingFee.getFeeNo());
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}else{
				flag = false;
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public List getShippingFeeList(Connection conn, String intake, String fromDate,
			String toDate, int start, int num) {
		List list = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select feeno,intake,companyname,prnum,feetype,rmbprice,mopprice,tomopprice,invoicedate,remarks," +
				"actind,credate,creuid,upddate,upduid " +
				"from tshippingfee where actind='Y' ";
		if(intake!=null && !intake.equals("")){
			sql += "and intake='"+intake +"' ";
		}
		if(fromDate!=null && !fromDate.equals("") && toDate!=null && !toDate.equals("")){
			sql += "and invoicedate between '"+fromDate+"' and '"+toDate+"' ";
		}
		sql += "order by feeno desc ";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// 分頁，從指定位置開始取記錄
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			while (rs.next() && j <= num) {
				ShippingFee fee = new ShippingFee();
				fee.setFeeNo(rs.getInt("feeno"));
				fee.setIntake(rs.getString("intake"));
				fee.setCompanyName(rs.getString("companyname"));
				fee.setPrNum(rs.getString("prnum"));
				fee.setFeeType(rs.getString("feetype"));
				fee.setRmbPrice(rs.getDouble("rmbprice"));
				fee.setMopPrice(rs.getDouble("mopprice"));
				fee.setToMopPrice(rs.getDouble("tomopprice"));
				fee.setInvoiceDate(rs.getString("invoicedate"));
				fee.setRemarks(rs.getString("remarks"));
				fee.setActind(rs.getString("actind"));
				fee.setCreDate(rs.getTimestamp("credate"));
				fee.setCreUid(rs.getString("creuid"));
				fee.setUpdDate(rs.getTimestamp("upddate"));
				fee.setUpdUid(rs.getString("upduid"));
				list.add(fee);
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
	public static List getpage(Connection conn, String intake, String fromDate, String toDate, String start,
			String num) {
		int count = getRsCount(conn, intake, fromDate, toDate);
		List page = UsePage.getPage(count, start, num);
		return page;
	}
	
	// 獲取记录总数方法
	public static int getRsCount(Connection conn, String intake, String fromDate, String toDate) {
		int i = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from tshippingfee where actind='Y' ";
			if(fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")){
				sql += "and invoicedate between '"+fromDate+"' and '"+toDate+"' ";
			}
			if(intake != null){
				sql += "and intake = '"+intake+"' ";				
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
		return i;
	}
}
