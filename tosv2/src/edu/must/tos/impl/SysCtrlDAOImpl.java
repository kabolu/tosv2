package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.SysCtrl;
import edu.must.tos.dao.SysCtrlDAO;

public class SysCtrlDAOImpl implements SysCtrlDAO {

	public List getDistinctValue1(Connection conn, SysCtrl ctrl)
			throws Exception {
		List strList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select distinct value1 from tsysctrl where type=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ctrl.getType());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String value1 = rs.getString("value1");
				strList.add(value1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return strList;
	}

	public List getSysCtrlInfo(Connection conn, SysCtrl ctrl)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select type, key, ch_desc, en_desc, value1, value2, value3, value4 "
					+ "from tsysctrl ";
			if (ctrl.getType().equals("APPACCEPT")) {
				sql += "where type=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ctrl.getType());
			} else if (ctrl.getType().equals("AREA")) {
				sql += "where type=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ctrl.getType());
			} else {

			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SysCtrl sysCtrl = new SysCtrl();
				sysCtrl.setType(rs.getString("type"));
				sysCtrl.setKey(rs.getString("key"));
				sysCtrl.setCh_Desc(rs.getString("ch_desc"));
				sysCtrl.setEn_Desc(rs.getString("en_desc"));
				sysCtrl.setValue1(rs.getString("value1"));
				sysCtrl.setValue2(rs.getString("value2"));
				sysCtrl.setValue3(rs.getString("value3"));
				sysCtrl.setValue4(rs.getString("value4"));
				list.add(sysCtrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return list;
	}

}
