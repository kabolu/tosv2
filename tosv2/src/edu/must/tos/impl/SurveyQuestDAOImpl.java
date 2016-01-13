package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.SurveyQuest;
import edu.must.tos.dao.SurveyQuestDAO;

public class SurveyQuestDAOImpl implements SurveyQuestDAO {

	public List getSurveyQuest(Connection conn, int refNo) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select sq_ref_no, sq_sm_refno, sq_name, sq_chn_title, sq_eng_title, sq_chn_ext, sq_eng_ext, " +
					"sq_type, sq_size, sq_act_ind, sq_align, sq_multi_line " +
					"from tsurveyquest where sq_act_ind = 'Y' and sq_sm_refno = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, refNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				SurveyQuest quest = new SurveyQuest();
				quest.setSqRefNo(rs.getInt("sq_ref_no"));
				quest.setSqSmRefNo(rs.getInt("sq_sm_refno"));
				quest.setSqName(rs.getString("sq_name"));
				quest.setSqChnTitle(rs.getString("sq_chn_title"));
				quest.setSqEngTitle(rs.getString("sq_eng_title"));
				quest.setSqChnExt(rs.getString("sq_chn_ext"));
				quest.setSqEngExt(rs.getString("sq_eng_ext"));
				quest.setSqType(rs.getString("sq_type"));
				quest.setSqSize(rs.getString("sq_size"));
				quest.setSqActInd(rs.getString("sq_act_ind"));
				quest.setSqAlign(rs.getString("sq_align"));
				quest.setSqMultiLine(rs.getString("sq_multi_line"));
				list.add(quest);
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
