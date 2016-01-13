package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.VBookFeeForApp;

public interface VBookFeeForAppDAO {
	
	public List getUnDifferList(Connection conn, String intake) throws Exception;
	
	public List getMoreRecordList(Connection conn, String intake) throws Exception;

	public List getVBookFeeForAppList(Connection conn, VBookFeeForApp v)
			throws Exception;

	public VBookFeeForApp getVBookFeeForAppInfo(Connection conn,
			VBookFeeForApp v) throws Exception;

	public List getVBookFeeForAppList(Connection conn, String paidStatus, String date,
			String tranDate, String intake) throws Exception;
}
