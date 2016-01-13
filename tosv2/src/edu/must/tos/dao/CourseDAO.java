package edu.must.tos.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import edu.must.tos.bean.Course;

public interface CourseDAO {
	
	/**
	 * @param couseCode;chineseName;englishName;facultyCode;
	 * @return list
	 */
	public List searchCourse(Connection conn, Course course, int start, int num);

	/**
	 * 
	 * @return List
	 * @throws SQLException
	 */
	public List getCourseCode(Connection conn, Course course) throws Exception;

}