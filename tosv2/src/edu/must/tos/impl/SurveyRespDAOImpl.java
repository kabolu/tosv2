package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.SurveyResp;
import edu.must.tos.dao.SurveyRespDAO;

public class SurveyRespDAOImpl implements SurveyRespDAO {

	public List getSurveyResp(Connection conn, int srSuRefno) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select sr_su_refno, sr_sq_refno, sr_value, sr_act_ind " +
					"from tsurveyresp where sr_act_ind = 'Y' and sr_su_refno = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, srSuRefno);
			rs = pstmt.executeQuery();
			while(rs.next()){
				SurveyResp resp = new SurveyResp();
				resp.setSrSuRefno(rs.getInt("sr_su_refno"));
				resp.setSrSqRefno(rs.getInt("sr_sq_refno"));
				resp.setSrValue(rs.getString("sr_value"));
				resp.setSrActInd(rs.getString("sr_act_ind"));
				list.add(resp);
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
