package edu.must.tos.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.must.tos.bean.SysCtrl;

public interface SysCtrlDAO {

	public List getSysCtrlInfo(Connection conn, SysCtrl ctrl)
			throws Exception;

	public List getDistinctValue1(Connection conn, SysCtrl ctrl)
			throws Exception;
}
