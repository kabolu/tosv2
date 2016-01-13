package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.ExassLect;

public interface ExassLectDAO {

	public List<ExassLect> getLectByCrs(Connection conn, ExassLect lect) throws Exception;
}
