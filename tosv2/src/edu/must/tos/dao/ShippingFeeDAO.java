package edu.must.tos.dao;

import java.sql.Connection;
import java.util.List;

import edu.must.tos.bean.ShippingFee;

public interface ShippingFeeDAO {
	
	public List getShippingFeeList(Connection conn, String intake, String startDate, String endDate) throws Exception;
	
	public ShippingFee getShippingFeeByFeeNo(Connection conn, int feeNo) throws Exception;
	
	public boolean updateShippingFee(Connection conn, ShippingFee shippingFee) throws Exception; 
	
	public boolean addShippingFee(Connection conn, ShippingFee shippingFee) throws Exception;

	public List getShippingFeeList(Connection conn, String intake, String fromDate, String toDate, int start, int num);
}
