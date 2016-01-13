package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

public interface StudentenrolDAO {

	public List getStudentenrol(Connection conn, String intake, String faculty,
			String prog, String studentNo, int start, int num)
			throws Exception;
}
