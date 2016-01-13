package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Question;
import edu.must.tos.dao.QuestionDAO;

public class QuestionDAOImpl implements QuestionDAO {

	public Question getQuestionByPK(Connection conn, Question question)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Question ques = null;
		try{
			String sql = "select id, question, answer, actind, creuid, credate, upduid, upddate " +
					"from tquestion " +
					"where 1=1 ";
			if(question != null && question.getId() != 0){
				sql += "and id = "+question.getId()+" ";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ques = new Question();
				ques.setId(rs.getInt("id"));
				ques.setQuestion(rs.getString("question"));
				ques.setAnswer(rs.getString("answer"));
				ques.setActInd(rs.getString("actind"));
				ques.setCreUid(rs.getString("creuid"));
				ques.setCreDate(rs.getTimestamp("credate"));
				ques.setUpdUid(rs.getString("upduid"));
				ques.setUpdDate(rs.getTimestamp("upddate"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return ques;
	}

	public boolean insertQuestion(Connection conn, Question question)
			throws Exception {
		PreparedStatement pstmt = null;
		boolean flag = false;
		try{
			String sql = "insert into tquestion (question, answer, actind, creuid, credate, upduid, upddate) " +
					"values (?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, question.getQuestion());
			pstmt.setString(2, question.getAnswer());
			pstmt.setString(3, question.getActInd());
			pstmt.setString(4, question.getCreUid());
			pstmt.setTimestamp(5, new java.sql.Timestamp(question.getCreDate().getTime()));
			pstmt.setString(6, question.getUpdUid());
			pstmt.setTimestamp(7, new java.sql.Timestamp(question.getUpdDate().getTime()));
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){
				pstmt.close();
			}
		}
		return flag;
	}

	public List getQuestionList(Connection conn, Question question)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List resultList = new ArrayList();
		try{
			String sql = "select id, question, answer, actind, creuid, credate, upduid, upddate " +
					"from tquestion ";			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Question ques = new Question();
				ques.setId(rs.getInt("id"));
				ques.setQuestion(rs.getString("question"));
				ques.setAnswer(rs.getString("answer"));
				ques.setActInd(rs.getString("actind"));
				ques.setCreUid(rs.getString("creuid"));
				ques.setCreDate(rs.getTimestamp("credate"));
				ques.setUpdUid(rs.getString("upduid"));
				ques.setUpdDate(rs.getTimestamp("upddate"));
				resultList.add(ques);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return resultList;
	}

	public boolean updateQuestion(Connection conn, Question question)
			throws Exception {
		PreparedStatement pstmt = null;
		boolean flag = false;
		try{
			String sql = "update tquestion set question=?, answer=?, actind=?, upduid=?, upddate=? " +
					"where id=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, question.getQuestion());
			pstmt.setString(2, question.getAnswer());
			pstmt.setString(3, question.getActInd());
			pstmt.setString(4, question.getUpdUid());
			pstmt.setTimestamp(5, new java.sql.Timestamp(question.getUpdDate().getTime()));
			pstmt.setInt(6, question.getId());
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){
				pstmt.close();
			}
		}
		return flag;
	}

}
