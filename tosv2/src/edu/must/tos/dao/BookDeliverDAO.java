package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

public interface BookDeliverDAO {

	public List getBookDeliverList(Connection conn, String intake) throws Exception;
}
