package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.must.tos.bean.ContactBean;

public class ContactDAOImpl {

	public ContactBean getContact(Connection conn, String studentNo) throws Exception{
		ContactBean bean = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try{
			String sql = "select studentno, contactno, email, remarks from tcontact where actind = ? and studentno = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, studentNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				bean = new ContactBean();
				bean.setStudentNo(rs.getString("studentno"));
				bean.setContactNo(rs.getString("contactno"));
				bean.setEmail(rs.getString("email"));
				bean.setRemarks(rs.getString("remarks"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return bean;
	}
}
