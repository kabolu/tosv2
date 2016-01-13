package edu.must.tos.impl;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.must.tos.bean.Email;

public class EmailDAOImpl {

	public Email getEmail(Connection conn, String mailType) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Email mail = null;
		String sql = "select em_mail_type, em_sender, em_subject, em_cc, em_bcc, " +
                      "em_act_ind, em_upd_uid, em_upd_date, em_cre_uid, em_cre_date " +
                      "from temail where em_act_ind = ? and em_mail_type = ? ";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, mailType);
			rs = pstmt.executeQuery();
			if(rs.next()){
				mail = new Email();
				mail.setEmMailType(rs.getString("em_mail_type"));
				mail.setEmSender(rs.getString("em_sender"));
				mail.setEmSubject(rs.getString("em_subject"));
				mail.setEmCc(rs.getString("em_cc"));
				mail.setEmBcc(rs.getString("em_bcc"));
				mail.setEmContent(getBlobAsString(conn, mailType));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return mail;
	}
	
	private String getBlobAsString(Connection conn, String mailType) {
        String str = null;
        String sql;
        sql = "SELECT em_content FROM TEmail WHERE em_mail_type= '" + mailType + "' and em_act_ind = 'Y'";
        str = getBlobAsStringBySql(conn, sql);
        return str;
    }
	
	@SuppressWarnings("finally")
	protected final String getBlobAsStringBySql(Connection conn, String sql) {
        String str = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Blob blob = rs.getBlob(1);
                byte[] bdata = blob.getBytes(1, (int)blob.length());
                str = new String(bdata, "UTF-8");
            }
        }
        catch(SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            try {
                if(pstmt != null) {
                    pstmt.close();
                }
            }
            catch(final SQLException sqle) {
                System.out.println("err.db.sql.exception");
            }

            return str;
        }
    }
}
