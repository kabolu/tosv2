package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Student;

public interface StudentDAO {
	/**
	 * @param isbn,name,publisher
	 * @return list
	 */
	public List searchStudent(Connection con, Student student, int start,
			int num) throws Exception;

	public List getNewStudList(Connection conn, Student newStud)
			throws Exception;

	public boolean isNewStudent(Connection conn, Student stu) throws Exception;

	public List getUpdNewStudList(Connection conn, String intake)
			throws Exception;

	public List showStudentDetail(Connection conn, String psStudentNo)
			throws Exception;
}