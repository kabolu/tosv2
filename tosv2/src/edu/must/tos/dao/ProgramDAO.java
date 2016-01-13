package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Program;

public interface ProgramDAO {
	/**
	 * @param conn
	 * @param program
	 * @param start
	 * @param num
	 * @return
	 */
	public List searchProgram(Connection conn, Program program, int start,
			int num);

	public List getProgramInfo(Connection conn) throws Exception;

}
