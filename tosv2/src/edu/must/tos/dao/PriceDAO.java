package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

public interface PriceDAO {

	public int savePriceInDB(Connection conn, List list) throws Exception;
}
