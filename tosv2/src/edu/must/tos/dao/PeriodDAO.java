package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import edu.must.tos.bean.Period;

public interface PeriodDAO {

	public List getAllReceiptBookTime(Connection conn, String intake)
			throws Exception;

	public int addReceiptBookTime(Connection conn, Period period)
			throws Exception;

	public int checkReceiptBookTime(Connection conn, String intake,
			String fromDate, String toDate, String maxNo, String type,
			String periodNo, String oprType) throws Exception;

	public Period getPeriodByNo(Connection conn, int periodNo) throws Exception;

	public boolean updateReceiptBookTime(Connection conn, Period period,
			Period oldPeriod) throws Exception;

	public boolean updatePeriodIsN(Connection conn, int id, Period period,
			String userId) throws Exception;

	public List getSearchPeriod(Connection conn, String intake,
			String periodType) throws Exception;

	public List getStatisticStudReceiveTimeRecord(Connection conn,
			String intake, String faculty, String program, int year,
			String fromDate, String toDate, String periodType) throws Exception;

	public List getStatisticUnSelReceiveTimeRecord(Connection conn,
			String intake, String faculty, String program, int year,
			String unselperiodType) throws Exception;

	public List getStatisticReceivePeriod(Connection conn, String intake,
			String faculty, String fromDate, String toDate, String periodType)
			throws Exception;

	public List getReceivePeriodLeavings(Connection conn, String intake,
			String fromDate, String toDate, String periodTyep) throws Exception;

	public int getMaxNo(Connection conn, String periodNo) throws Exception;

	public Period getStudReceiveTime(Connection conn, String studNo,
			String intake) throws Exception;

	public Map getStudReceOrPaidPeriodMap(Connection conn, String intake)
			throws Exception;
}
