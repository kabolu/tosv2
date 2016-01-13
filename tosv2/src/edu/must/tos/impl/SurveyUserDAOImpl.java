package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.SurveyUser;
import edu.must.tos.dao.SurveyUserDao;

public class SurveyUserDAOImpl implements SurveyUserDao {

	public List getSurveyUsers(Connection conn, String currIntake)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select su_ref_no, su_sm_refno, su_type, su_user, su_intake, su_is_finished, su_act_ind " +
					"from tsurveyuser where su_act_ind = ? and su_intake = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, currIntake);
			rs = pstmt.executeQuery();
			while(rs.next()){
				SurveyUser user = new SurveyUser();
				user.setSuRefNo(rs.getInt("su_ref_no"));
				user.setSuSmRefNo(rs.getInt("su_sm_refno"));
				user.setSuType(rs.getString("su_type"));
				user.setSuUser(rs.getString("su_user"));
				user.setSuIntake(rs.getString("su_intake"));
				user.setSuIsFinished(rs.getString("su_is_finished"));
				user.setSuActInd(rs.getString("su_act_ind"));
				list.add(user);
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

}
