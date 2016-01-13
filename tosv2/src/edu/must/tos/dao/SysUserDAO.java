package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.SysUser;

/**
 * @author Wangjiabo
 * @version 1.0
 * @date 2008-11-07
 */
public interface SysUserDAO {

	/**
	 * check user is exist or not
	 */
	public boolean checkLogin(Connection conn, String userId);

	/**
	 * get all the user info ,return list;
	 */
	public List getAllUserInfo(Connection conn) throws Exception;

	/**
	 * add user info
	 */
	public boolean addUserInfo(Connection conn, SysUser user)
			throws Exception;

	/**
	 * userId is unique
	 */
	public boolean checkUserId(Connection conn, SysUser user)
			throws Exception;

	/**
	 * get user info by userId
	 */
	public SysUser getUserInfoById(Connection conn, SysUser user)
			throws Exception;

	/**
	 * update user info
	 */
	public boolean updateUserInfo(Connection conn, SysUser user)
			throws Exception;

	/**
	 * get user info by somes condition
	 */
	public List getUserInfoByCond(Connection conn, SysUser user)
			throws Exception;
}
