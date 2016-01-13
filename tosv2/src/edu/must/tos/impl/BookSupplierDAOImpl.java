package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookSupplier;
import edu.must.tos.dao.BookSupplierDAO;
import edu.must.tos.util.UsePage;

public class BookSupplierDAOImpl implements BookSupplierDAO {

	public List getBookSuppliersList(Connection conn, BookSupplier supplier)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select supplierno, suppliercode, suppliername, supplierengname, contactname, "
				+ "suppliertel1, suppliertel2, supplierfax1, supplierfax2, supplieremail, address, "
				+ "resumeinfo, remarks, actind, io, inner "
				+ "from tbooksupplier where actind='Y' ";
			if(supplier!=null && supplier.getIo()!=null && !supplier.getIo().equals("")){
				sql += "and io = '"+supplier.getIo()+"' or (actind='Y' and io = 'IO') ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BookSupplier bookSupplier = new BookSupplier();
				bookSupplier.setSupplierNo(rs.getInt("supplierno"));
				bookSupplier.setSupplierCode(rs.getString("suppliercode"));
				bookSupplier.setSupplierName(rs.getString("suppliername"));
				bookSupplier.setSupplierEngName(rs.getString("supplierengname"));
				bookSupplier.setContactName(rs.getString("contactname"));
				bookSupplier.setSupplierTel_1(rs.getString("suppliertel1"));
				bookSupplier.setSupplierTel_2(rs.getString("suppliertel2"));
				bookSupplier.setSupplierFax_1(rs.getString("supplierfax1"));
				bookSupplier.setSupplierFax_2(rs.getString("supplierfax2"));
				bookSupplier.setSupplierEmail(rs.getString("supplieremail"));
				bookSupplier.setAddress(rs.getString("address"));
				bookSupplier.setResumeInfo(rs.getString("resumeinfo"));
				bookSupplier.setRemarks(rs.getString("remarks"));
				bookSupplier.setActInd(rs.getString("actind"));
				bookSupplier.setIo(rs.getString("io"));
				bookSupplier.setInner(rs.getString("inner"));
				list.add(bookSupplier);
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

	public boolean updateBookSupplierActN(Connection conn, int id)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "update tbooksupplier set actind=? where supplierno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "N");
			pstmt.setInt(2, id);
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public boolean updateBookSupplier(Connection conn, BookSupplier supplier)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "update tbooksupplier set " 
					+ "suppliercode=?, suppliername=?, supplierengname=?, contactname=?, "
					+ "suppliertel1=?, suppliertel2=?, supplierfax1=?, supplierfax2=?, supplieremail=?, address=?, "
					+ "resumeinfo=?, remarks=?, actind=?, upddate=?, upduid=?, io=?, inner=? "
					+ "where supplierno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, supplier.getSupplierCode());
			pstmt.setString(2, supplier.getSupplierName());
			pstmt.setString(3, supplier.getSupplierEngName());
			pstmt.setString(4, supplier.getContactName());
			pstmt.setString(5, supplier.getSupplierTel_1());
			pstmt.setString(6, supplier.getSupplierTel_2());
			pstmt.setString(7, supplier.getSupplierFax_1());
			pstmt.setString(8, supplier.getSupplierFax_2());
			pstmt.setString(9, supplier.getSupplierEmail());
			pstmt.setString(10, supplier.getAddress());
			pstmt.setString(11, supplier.getResumeInfo());
			pstmt.setString(12, supplier.getRemarks());
			pstmt.setString(13, supplier.getActInd());
			pstmt.setTimestamp(14, new java.sql.Timestamp(supplier.getUpdDate().getTime()));
			pstmt.setString(15, supplier.getUpdUid());
			pstmt.setString(16, supplier.getIo());
			pstmt.setString(17, supplier.getInner());
			pstmt.setInt(18, supplier.getSupplierNo());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public BookSupplier getBookSupplierByNo(Connection conn, int no)
			throws Exception {
		BookSupplier bookSupplier = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select supplierno, suppliercode, suppliername, supplierengname, contactname, "
					+ "suppliertel1, suppliertel2, supplierfax1, supplierfax2, supplieremail, address, "
					+ "resumeinfo, remarks, actind, io, inner "
					+ "from tbooksupplier where supplierno=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bookSupplier = new BookSupplier();
				bookSupplier.setSupplierNo(rs.getInt("supplierno"));
				bookSupplier.setSupplierCode(rs.getString("suppliercode"));
				bookSupplier.setSupplierName(rs.getString("suppliername"));
				bookSupplier.setSupplierEngName(rs.getString("supplierengname"));
				bookSupplier.setContactName(rs.getString("contactname"));
				bookSupplier.setSupplierTel_1(rs.getString("suppliertel1"));
				bookSupplier.setSupplierTel_2(rs.getString("suppliertel2"));
				bookSupplier.setSupplierFax_1(rs.getString("supplierfax1"));
				bookSupplier.setSupplierFax_2(rs.getString("supplierfax2"));
				bookSupplier.setSupplierEmail(rs.getString("supplieremail"));
				bookSupplier.setAddress(rs.getString("address"));
				bookSupplier.setResumeInfo(rs.getString("resumeinfo"));
				bookSupplier.setRemarks(rs.getString("remarks"));
				bookSupplier.setActInd(rs.getString("actind"));
				bookSupplier.setIo(rs.getString("io"));
				bookSupplier.setInner(rs.getString("inner"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return bookSupplier;
	}

	public boolean addBookSupplier(Connection conn, BookSupplier supplier)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into tbooksupplier (suppliercode, suppliername, supplierengname, contactname, "
					+ "suppliertel1, suppliertel2, supplierfax1, supplierfax2, supplieremail, address, "
					+ "resumeinfo, remarks, actind, credate, creuid, io, inner ) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, supplier.getSupplierCode());
			pstmt.setString(2, supplier.getSupplierName());
			pstmt.setString(3, supplier.getSupplierEngName());
			pstmt.setString(4, supplier.getContactName());
			pstmt.setString(5, supplier.getSupplierTel_1());
			pstmt.setString(6, supplier.getSupplierTel_2());
			pstmt.setString(7, supplier.getSupplierFax_1());
			pstmt.setString(8, supplier.getSupplierFax_2());
			pstmt.setString(9, supplier.getSupplierEmail());
			pstmt.setString(10, supplier.getAddress());
			pstmt.setString(11, supplier.getResumeInfo());
			pstmt.setString(12, supplier.getRemarks());
			pstmt.setString(13, supplier.getActInd());
			pstmt.setTimestamp(14, new java.sql.Timestamp(supplier.getCreDate().getTime()));
			pstmt.setString(15, supplier.getCreUid());
			pstmt.setString(16, supplier.getIo());
			pstmt.setString(17, supplier.getInner());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public List getBookSuppliersList(Connection conn, BookSupplier supplier,
			int start, int num) throws Exception {
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			sql = "select supplierno, suppliercode, suppliername, supplierengname, contactname, "
					+ "suppliertel1, suppliertel2, supplierfax1, supplierfax2, supplieremail, address, "
					+ "resumeinfo, remarks, actind, inner "
					+ "from tbooksupplier where actind='Y' ";
			if (supplier.getSupplierCode() != null && !supplier.getSupplierCode().equals("")) {
				sql += " and suppliercode like '%" + supplier.getSupplierCode() + "%'";
			}
			if (supplier.getSupplierName() != null && !supplier.getSupplierName().equals("")) {
				sql += " and suppliername like '%" + supplier.getSupplierName() + "%'";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = 0; i < start; i++) {
				rs.next();
			}
			int j = 1;
			while (rs.next() && j <= num) {
				BookSupplier bookSupplier = new BookSupplier();
				bookSupplier.setSupplierNo(rs.getInt("supplierno"));
				bookSupplier.setSupplierCode(rs.getString("suppliercode"));
				bookSupplier.setSupplierName(rs.getString("suppliername"));
				bookSupplier.setSupplierEngName(rs.getString("supplierengname"));
				bookSupplier.setContactName(rs.getString("contactname"));
				bookSupplier.setSupplierTel_1(rs.getString("suppliertel1"));
				bookSupplier.setSupplierTel_2(rs.getString("suppliertel2"));
				bookSupplier.setSupplierFax_1(rs.getString("supplierfax1"));
				bookSupplier.setSupplierFax_2(rs.getString("supplierfax2"));
				bookSupplier.setSupplierEmail(rs.getString("supplieremail"));
				bookSupplier.setAddress(rs.getString("address"));
				bookSupplier.setResumeInfo(rs.getString("resumeinfo"));
				bookSupplier.setRemarks(rs.getString("remarks"));
				bookSupplier.setActInd(rs.getString("actind"));
				bookSupplier.setInner(rs.getString("inner"));
				list.add(bookSupplier);
				j++;
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

	public static List getPage(Connection conn, BookSupplier supplier,
			String start, String num) {
		int count = getRsCount(conn, supplier);
		List page = UsePage.getPage(count, start, num);
		return page;
	}

	public static int getRsCount(Connection conn, BookSupplier supplier) {
		int i = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from tbooksupplier where actind='Y' ";
			if (supplier.getSupplierCode() != null && !supplier.getSupplierCode().equals("")) {
				sql += " and suppliercode like '%" + supplier.getSupplierCode() + "%'";
			}
			if (supplier.getSupplierName() != null && !supplier.getSupplierName().equals("")) {
				sql += " and suppliername like '%" + supplier.getSupplierName() + "%'";
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
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return i;
	}
}