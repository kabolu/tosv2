package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

public interface SurveyRespDAO {

	public List getSurveyResp(Connection conn, int srSuRefno) throws Exception;
}
