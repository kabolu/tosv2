package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.SysUser;
import edu.must.tos.dao.SysUserDAO;

/**
 * @author Wangjiabo
 * @version 1.0
 * @date 2008-11-07
 */
public class SysUserDAOImpl implements SysUserDAO {

	public List getUserInfoByCond(Connection conn, SysUser user)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select userid, passwd, username, department, status, role, times, remarks "
					+ "from tsysuser where 1=1 ";
			if (user.getUserId() != null && !user.getUserId().equals("")) {
				sql += "and userid = '" + user.getUserId() + "' ";
			}
			if (user.getUserName() != null && !user.getUserName().equals("")) {
				sql += "and username = '" + user.getUserName() + "' ";
			}
			if (user.getDepartment() != null && !user.getDepartment().equals("")) {
				sql += "and department = '" + user.getDepartment() + "' ";
			}
			if (user.getStatus() != null && !user.getStatus().equals("")) {
				sql += "and status = '" + user.getStatus() + "' ";
			}
			if (user.getRole() != null && !user.getRole().equals("")) {
				sql += "and role = '" + user.getRole() + "' ";
			}
			sql += "order by userid ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SysUser userInfo = new SysUser();
				userInfo.setUserId(rs.getString("userid"));
				userInfo.setPasswd(rs.getString("passwd"));
				userInfo.setUserName(rs.getString("username"));
				userInfo.setDepartment(rs.getString("department"));
				userInfo.setStatus(rs.getString("status"));
				userInfo.setRole(rs.getString("role"));
				userInfo.setTimes(rs.getInt("times"));
				userInfo.setRemarks(rs.getString("remarks"));
				list.add(userInfo);
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

	public boolean updateUserInfo(Connection conn, SysUser user)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tsysuser set passwd=?,username=?,department=?,status=?,role=?,remarks=?,upddate=?,upduid=?,email=?,address=?,contactno=?,faxno=? "
					+ "where userid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getPasswd());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getDepartment());
			pstmt.setString(4, user.getStatus());
			pstmt.setString(5, user.getRole());
			pstmt.setString(6, user.getRemarks());
			pstmt.setTimestamp(7, new java.sql.Timestamp(user.getUpddate().getTime()));
			pstmt.setString(8, user.getUpduid());
			pstmt.setString(9, user.getEmail());
			pstmt.setString(10, user.getAddress());
			pstmt.setString(11, user.getContactNo());
			pstmt.setString(12, user.getFaxNo());
			pstmt.setString(13, user.getUserId());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}

	public SysUser getUserInfoById(Connection conn, SysUser user)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select userid,passwd,username,department,status,role,times,remarks,email,address,contactno,faxno "
					+ "from tsysuser where userid=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user.setPasswd(rs.getString("passwd"));
				user.setUserName(rs.getString("username"));
				user.setDepartment(rs.getString("department"));
				user.setStatus(rs.getString("status"));
				user.setRole(rs.getString("role"));
				user.setTimes(rs.getInt("times"));
				user.setRemarks(rs.getString("remarks"));
				user.setEmail(rs.getString("email"));
				user.setAddress(rs.getString("address"));
				user.setContactNo(rs.getString("contactno"));
				user.setFaxNo(rs.getString("faxno"));
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
		return user;
	}

	public boolean checkUserId(Connection conn, SysUser user)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select userId from tsysuser";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String value = rs.getString("userId");
				if (value.equals(user.getUserId())) {
					flag = true;
					break;
				}
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
		return flag;
	}

	public boolean addUserInfo(Connection conn, SysUser user)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "insert into tsysuser "
					+ "(userid,passwd,username,department,status,role,remarks,credate,creuid,actind,email,address,contactno,faxno,upddate,upduid) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getPasswd());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getDepartment());
			pstmt.setString(5, user.getStatus());
			pstmt.setString(6, user.getRole());
			pstmt.setString(7, user.getRemarks());
			pstmt.setTimestamp(8, new java.sql.Timestamp(user.getCredate().getTime()));
			pstmt.setString(9, user.getCreuid());
			pstmt.setString(10, user.getActInd());
			pstmt.setString(11, user.getEmail());
			pstmt.setString(12, user.getAddress());
			pstmt.setString(13, user.getContactNo());
			pstmt.setString(14, user.getFaxNo());
			pstmt.setTimestamp(15, new java.sql.Timestamp(user.getUpddate().getTime()));
			pstmt.setString(16, user.getUpduid());
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}

	public List getAllUserInfo(Connection conn) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select userid,passwd,username,department,status,role,times,remarks from tsysuser where status in ('A', 'L') order by status ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SysUser user = new SysUser();
				user.setUserId(rs.getString("userid"));
				user.setPasswd(rs.getString("passwd"));
				user.setUserName(rs.getString("username"));
				user.setDepartment(rs.getString("department"));
				user.setStatus(rs.getString("status"));
				user.setRole(rs.getString("role"));
				user.setTimes(rs.getInt("times"));
				user.setRemarks(rs.getString("remarks"));
				list.add(user);
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

	/**
	 * check user is exist or not
	 */
	public boolean checkLogin(Connection conn, String psUserId) {
		boolean result = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select userid from tsysuser where userid = ? and status = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, psUserId);
			ps.setString(2, "A");
			rs = ps.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
