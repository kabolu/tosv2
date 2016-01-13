package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.must.tos.bean.Price;
import edu.must.tos.dao.PriceDAO;

public class PriceDAOImpl implements PriceDAO {

	public boolean updateWithdrawPrice(Connection conn, String sckey,
			String scvalue1, String intake) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tprice tp set tp.withdrawprice=? where tp.intake=? and tp.actind=? "
					+ "and tp.isbn in (select tb.isbn from tbook tb where tb.language=? and tb.actind=?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, scvalue1);
			pstmt.setString(2, intake);
			pstmt.setString(3, "Y");
			pstmt.setString(4, sckey);
			pstmt.setString(5, "Y");
			int temp = pstmt.executeUpdate();
			if (temp > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	/**
	 * 
	 * @param conn
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	public int savePriceInDB(Connection conn, List list) throws Exception {
		int temp = 0;
		PreparedStatement selectPstmt1 = null;
		PreparedStatement selectPstmt2 = null;
		PreparedStatement insertPstmt = null;
		PreparedStatement updatePstmt1 = null;
		PreparedStatement updatePstmt2 = null;
		ResultSet rs = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			String sql1 = "select 1 from tprice where isbn = ? and currency = ? and intake = ?";
			selectPstmt1 = conn.prepareStatement(sql1);
			String sql2 = "select 1 from tprice where isbn = ? and currency = ? and actInd = ? and intake = ?";
			selectPstmt2 = conn.prepareStatement(sql2);
			String sql3 = "update tprice "
					+ "set futurePrice = ?, netPrice = ?, withdrawPrice = ?, updUid = ?, updDate = ?, actInd = ? "
					+ "where isbn = ? and currency = ? and intake = ? ";
			updatePstmt1 = conn.prepareStatement(sql3);
			String sql4 = "update tprice "
					+ "set futurePrice = ?, netPrice = ?, withdrawPrice = ?, updUid = ?, updDate = ? "
					+ "where isbn = ? and currency = ? and intake = ? and actInd = ? ";
			updatePstmt2 = conn.prepareStatement(sql4);
			String sql5 = "insert into tprice "
					+ "(isbn, currency, futurePrice, netPrice, withdrawPrice, creUid, creDate, updUid, updDate, actInd, intake) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			insertPstmt = conn.prepareStatement(sql5);
			for (int i = 0; i < list.size(); i++) {
				Price price = (Price)list.get(i);
				selectPstmt1.setString(1, price.getIsbn());
				selectPstmt1.setString(2, price.getCurrency());
				selectPstmt1.setString(3, price.getIntake());
				rs = selectPstmt1.executeQuery();
				if (rs.next()) {
					selectPstmt2.setString(1, price.getIsbn());
					selectPstmt2.setString(2, price.getCurrency());
					selectPstmt2.setString(3, "N");
					selectPstmt2.setString(4, price.getIntake());
					rs = selectPstmt2.executeQuery();
					if (rs.next()) {
						updatePstmt1.setDouble(1, price.getFuturePrice());
						updatePstmt1.setDouble(2, price.getNetPrice());
						updatePstmt1.setDouble(3, price.getWithdrawPrice());
						updatePstmt1.setString(4, price.getUpdUid());
						updatePstmt1.setTimestamp(5, ts);
						updatePstmt1.setString(6, price.getActInd());
						updatePstmt1.setString(7, price.getIsbn());
						updatePstmt1.setString(8, price.getCurrency());
						updatePstmt1.setString(9, price.getIntake());
						temp = updatePstmt1.executeUpdate();
						if (temp == 0) {
							break;
						}
					} else {
						updatePstmt2.setDouble(1, price.getFuturePrice());
						updatePstmt2.setDouble(2, price.getNetPrice());
						updatePstmt2.setDouble(3, price.getWithdrawPrice());
						updatePstmt2.setString(4, price.getUpdUid());
						updatePstmt2.setTimestamp(5, ts);
						updatePstmt2.setString(6, price.getIsbn());
						updatePstmt2.setString(7, price.getCurrency());
						updatePstmt2.setString(8, price.getIntake());
						updatePstmt2.setString(9, price.getActInd());
						temp = updatePstmt2.executeUpdate();
						if (temp == 0) {
							break;
						}
					}
				} else {
					insertPstmt.setString(1, price.getIsbn());
					insertPstmt.setString(2, price.getCurrency());
					insertPstmt.setDouble(3, price.getFuturePrice());
					insertPstmt.setDouble(4, price.getNetPrice());
					insertPstmt.setDouble(5, price.getWithdrawPrice());
					insertPstmt.setString(6, price.getCreUid());
					insertPstmt.setTimestamp(7, ts);
					insertPstmt.setString(8, price.getUpdUid());
					insertPstmt.setTimestamp(9, ts);
					insertPstmt.setString(10, price.getActInd());
					insertPstmt.setString(11, price.getIntake());
					temp = insertPstmt.executeUpdate();
					if (temp == 0) {
						break;
					}
				}
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (selectPstmt1 != null)
				selectPstmt1.close();
			if (selectPstmt2 != null)
				selectPstmt2.close();
			if (insertPstmt != null)
				insertPstmt.close();
			if (updatePstmt1 != null)
				updatePstmt1.close();
			if (updatePstmt2 != null)
				updatePstmt2.close();
		}
		return temp;
	}

	/**
	 * @param conn
	 * @param isbn
	 * @return
	 * @throws SQLException
	 */

	public List getBookPrice(Connection conn, String isbn, String intake)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List priceList = new ArrayList();
		String sql = null;
		try {
			sql = "select isbn, currency, futurePrice, netPrice, withdrawPrice from tprice "
					+ "where isbn = ? and actInd = ? and intake = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, isbn);
			pstmt.setString(2, "Y");
			pstmt.setString(3, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Price price = new Price();
				price.setIsbn(rs.getString("isbn"));
				price.setCurrency(rs.getString("currency"));
				price.setFuturePrice(rs.getDouble("futurePrice"));
				price.setNetPrice(rs.getDouble("netPrice"));
				price.setWithdrawPrice(rs.getDouble("withdrawPrice"));
				priceList.add(price);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return priceList;
	}

	/**
	 * 
	 * @param p
	 * @return
	 * @throws SQLException
	 */
	public int getDelPrice(Connection conn, Price p) throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			String sql = "update tprice set actInd = ?, updUid = ?, updDate = ? "
					+ "where isbn = ? and intake = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getActInd());
			pstmt.setString(2, p.getUpdUid());
			pstmt.setTimestamp(3, ts);
			pstmt.setString(4, p.getIsbn());
			pstmt.setString(5, p.getIntake());
			temp = pstmt.executeUpdate();
			if (temp > 0) {
				temp = 1;
			} else {
				temp = 0;
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return temp;
	}

	public int getUpdateNetPrice(Connection conn, List priceList)
			throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "update tprice set netPrice = ?, updUid = ?, updDate = ? "
					+ "where isbn = ? and currency = ? and actInd = ? and intake = ? ";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < priceList.size(); i++) {
				Price price = (Price) priceList.get(i);
				pstmt.setDouble(1, price.getNetPrice());
				pstmt.setString(2, price.getUpdUid());
				pstmt.setTimestamp(3, ts);
				pstmt.setString(4, price.getIsbn());
				pstmt.setString(5, price.getCurrency());
				pstmt.setString(6, price.getActInd());
				pstmt.setString(7, price.getIntake());
				temp = pstmt.executeUpdate();
				if (temp > 0) {
					temp = 1;
				} else {
					temp = 0;
					break;
				}
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return temp;
	}

	public int getUpdateFuturePrice(Connection conn, List priceList)
			throws Exception {
		int temp = 0;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			sql = "update tprice set futurePrice = ?, updUid = ?, updDate = ? "
					+ "where isbn = ? and currency = ? and actInd = ? and intake = ? ";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < priceList.size(); i++) {
				Price price = (Price) priceList.get(i);
				pstmt.setDouble(1, price.getFuturePrice());
				pstmt.setString(2, price.getUpdUid());
				pstmt.setTimestamp(3, ts);
				pstmt.setString(4, price.getIsbn());
				pstmt.setString(5, price.getCurrency());
				pstmt.setString(6, price.getActInd());
				pstmt.setString(7, price.getIntake());
				temp = pstmt.executeUpdate();
				if (temp > 0) {
					temp = 1;
				} else {
					temp = 0;
					break;
				}
			}
		} catch (Exception e) {
			temp = 0;
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return temp;
	}

	public Vector getPrice(Connection conn, String intake) throws Exception {
		Vector outter = new Vector();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select tb.isbn,tb.title,tb.author,tb.publisher,tb.publishYear,tb.edition,"
					//rainbow add
					+ "tb.language, tb.booktype, tb.remarks, tb.actind as withdrawInd, tb.suppliercode1, tb.suppliercode2, tb.supplement, "
					+ "tb.currency as bookcurrency, tb.unitprice, tb.discount, tb.favourableprice, "
					+ "t1.currency, t1.futureprice as mopfutureprice, t2.futureprice as rmbfutureprice,"
					+ "t2.currency, t1.netprice as mopnetprice, t2.netprice as rmbnetprice "
					+ "from tbook tb " 
					+ "inner join tprice t1 on tb.isbn = t1.isbn and t1.currency = 'MOP' " 
					+ "inner join tprice t2 on tb.isbn = t2.isbn and t2.currency = 'RMB' "
					+ "where tb.actind ='Y' and t1.actind = 'Y' and t2.actind = 'Y' and t1.intake = t2.intake "
					+ "and t1.intake = ? order by tb.isbn";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Vector inner = new Vector();
				inner.add(rs.getString("isbn"));
				inner.add(rs.getString("title"));
				inner.add(rs.getString("author"));
				inner.add(rs.getString("publisher"));
				inner.add(rs.getString("publishYear"));
				inner.add(rs.getString("edition"));
				
				//rainbow add
				inner.add(rs.getString("language"));
				inner.add(rs.getString("remarks"));
				inner.add(rs.getString("withdrawInd"));
				
				inner.add(rs.getString("suppliercode1"));
				inner.add(rs.getString("suppliercode2"));
				inner.add(rs.getString("supplement"));
				
				
				inner.add(rs.getDouble("mopfutureprice"));
				inner.add(rs.getDouble("rmbfutureprice"));
				inner.add(rs.getDouble("mopnetprice"));
				inner.add(rs.getDouble("rmbnetprice"));
				
				inner.add(rs.getString("bookcurrency"));
				inner.add(rs.getDouble("unitprice"));
				inner.add(rs.getDouble("discount"));
				inner.add(rs.getDouble("favourableprice"));
				outter.add(inner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return outter;
	}
}
