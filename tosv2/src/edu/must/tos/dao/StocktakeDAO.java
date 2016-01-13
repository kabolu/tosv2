package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Stocktake;

public interface StocktakeDAO {

	public List getStocktakeRecord(Connection conn, String fromDate,
			String toDate, String intake) throws Exception;

	public List getStocktakeList(Connection conn, Stocktake stocktake)
			throws Exception;

	public boolean insertStocktake(Connection conn, List list)
			throws Exception;
}
