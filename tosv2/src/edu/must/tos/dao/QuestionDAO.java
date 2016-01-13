package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Question;

public interface QuestionDAO {

	public Question getQuestionByPK(Connection conn, Question question) throws Exception;
	
	public boolean insertQuestion(Connection conn, Question question) throws Exception;
	
	public List getQuestionList(Connection conn, Question question) throws Exception;
	
	public boolean updateQuestion(Connection conn, Question question) throws Exception;
	
}
