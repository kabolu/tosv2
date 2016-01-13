package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.V_NewStudOrder;

public interface NewStudOrderDAO {

	public List getNewStudCourseInfo(Connection conn, String intake) throws Exception;

	public boolean importNewStudCourseInfo(Connection conn, List list)
			throws Exception;

	public List getNewStudOrderInfo(Connection conn, V_NewStudOrder v)
			throws Exception;

	public boolean deleteNewStudCourseInfo(Connection conn) throws Exception;
}
