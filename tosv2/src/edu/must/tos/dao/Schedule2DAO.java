package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Schedule2;

public interface Schedule2DAO {
	
	public List getSchedule2(Connection conn, Schedule2 sch2) throws Exception;

	public boolean checkPeriodNo(Connection conn, String type, String periodNo)
			throws Exception;

}
