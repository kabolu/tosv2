package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

public interface SurveyQuestDAO {

	public List getSurveyQuest(Connection conn, int refNo) throws Exception;
}
