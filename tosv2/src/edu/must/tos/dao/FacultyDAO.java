package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Faculty;

public interface FacultyDAO {
	/**
	 * @param
	 * @return list
	 */
	public List searchFaculty(Connection conn, Faculty faculty);

	public List getStudToFaculty(Connection conn, String studentNo,
			String intake) throws Exception;

	public List getFacultyList(Connection conn) throws Exception;

	public List getAutoFaculty(Connection conn, String studentNo)
			throws Exception;

}