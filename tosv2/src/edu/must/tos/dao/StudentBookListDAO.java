package edu.must.tos.dao;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;

import edu.must.tos.bean.StudentBookList;

public interface StudentBookListDAO{
	/**
	 * @param conn
	 * @param studNo
	 * @return
	 * @throws Exception
	 */
	public List queryBook(Connection conn, String studNo) throws Exception;
}
