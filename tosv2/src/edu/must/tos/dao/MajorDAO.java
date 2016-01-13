package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Major;

public interface MajorDAO {
	
	public List getMajByProg(Connection conn, Major major) throws Exception;

	public List getMajorList(Connection conn) throws Exception;

}
