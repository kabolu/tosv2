package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.Withdraw;

public interface WithdrawDAO {

	public boolean addWithdraw(Connection conn, Withdraw with)
			throws Exception;

	public List getWithdrawList(Connection conn, String intake, String from,
			String to, String isbn, String cause) throws Exception;
}
