package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

public interface SurveyUserDao {

	public List getSurveyUsers(Connection conn, String currIntake) throws Exception;
}
