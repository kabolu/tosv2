package edu.must.tos.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.SysConfig;

/**
 * @author Wangjiabo
 * @version 1.0
 * @date 2008-11-07
 */
public interface SysConfigDAO {
	
	public boolean updateSysConfig(Connection conn, SysConfig config)
			throws Exception;
	
	public boolean addSysCnnfig(Connection conn, SysConfig config) 
			throws Exception;
	
	public List getSysConfigList(Connection conn, SysConfig config)
			throws Exception;
	
	public SysConfig getSysConfig(Connection conn, SysConfig config)
			throws Exception;
	
	public List getSysScType(Connection conn) throws Exception;
	
	public boolean updateCurrIntake(Connection conn, String newIntake,
			String oldIntake, String userId) throws Exception;
	
	public boolean addSysIntake(Connection conn, String intake, String userId)
			throws Exception;
	
	public boolean checkReceiptTime(Connection conn, String intake,
			String fromDate, String toDate, String periodType) throws Exception;
	
	/**
	 * check period
	 */
	public String checkPeriod(Connection conn, Date date);
	
	public boolean checkWithdraw(Connection conn, String intake, Date date) throws Exception;
	
	/**
	 * return curIntake
	 */
	public String getCurIntake(Connection conn, String key);

	/**
	 * 
	 * @return language list
	 * @throws SQLException
	 */
	public List getLanguage(Connection conn);
	
	/**
	 * 
	 * @param lang
	 * @return withdrawPrice
	 * @throws SQLException
	 */
	public Double getWithDrawPrice(Connection conn, String lang)
			throws Exception;

	/**
	 * @param lang
	 * @return
	 * @throws SQLException
	 */
	public boolean getLangResult(Connection conn, String lang);
	
	public List getSysTime(Connection conn, String intake) throws Exception;
	
	public int getAddConfTime(Connection conn, SysConfig sc) throws Exception;
	
	public boolean getCheckSysTime(Connection conn, SysConfig sc, String opType)
			throws Exception;
	
	public List getSysIntake(Connection conn) throws Exception;
}
