package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.OrDetail;

public interface OrderDetailDAO {
	
	public List getOrDetailInfo(Connection conn, OrDetail od) throws Exception;

	public boolean updateWithdrawInfo(Connection conn, List<OrDetail> detailList)
			throws Exception;

	public boolean addDetailSeqNoInfo(Connection conn, List detailList)
			throws Exception;
	
	public boolean updateOrddetailNewStudNo(Connection conn, String intake,
			List updList) throws Exception;

	public boolean deleteOrderDetail(int orderSeqNo, String isbn,
			String orderIntake, String userId, Connection con);

	public List getDelOrderList(Connection conn, String isbn,
			String courseCode, String curIntake);

	public boolean deleteOrderDetailList(Connection conn,
			List confirmDelOrderList, String orderIntake, String userId);

	public boolean delOrderDetailInfo(Connection conn, OrDetail orDetail)
			throws Exception;

	public int getReceivedBookStatus(Connection conn, OrDetail orDetail)
			throws Exception;

	public List getRetailReceipt(Connection conn, int orderSeqNo) throws Exception;
}
