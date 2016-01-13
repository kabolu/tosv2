package edu.must.tos.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.must.tos.bean.Order;

public interface OrderDAO {

	public int updateOrderActindList(Connection conn, Order order)
			throws Exception;

	public Order getOrderBySeqNo(Connection conn, int orderSeqNo)
			throws Exception;

	public boolean setPaidStatusN(Connection conn, int orderSeqNo,
			String curIntake) throws Exception;

	public List getAllOrderSeqNoInfo(Connection conn, String studParam,
			String intake, String actind) throws Exception;

	public int addOrderSeqNoInfo(Connection conn, Order order)
			throws Exception;

	public Order getPaidNOrderInfo(Connection conn, String studentNo,
			String intake) throws Exception;

	public boolean updateOrdNewStudNo(Connection conn, String intake,
			List updList) throws Exception;

	/**
	 * 
	 * @param conn
	 * @param orderSeqNo
	 * @param orderIntake
	 * @param paidCurrency
	 * @param paidAmount
	 * @param amercePrice
	 * @param updUid
	 * @param shippingFeem
	 * @param netPaidCurrency
	 * @param netpaidAmount
	 * @return
	 */
	public boolean updateOrder(Connection conn, int orderSeqNo,
			String orderIntake, String paidCurrency, float paidAmount,
			float amercePrice, String updUid, double shippingFeem, String netPaidCurrency, float netpaidAmount, double curRate);

	/**
	 * @param conn
	 * @param studentNo
	 * @param orderIntake
	 * @return
	 */
	public boolean isPaying(Connection conn, int orderSeqNo, String orderIntake);

	public Order getNewsOrderInfo(Connection conn, String studentNo,
			String orderIntake) throws Exception;

}
