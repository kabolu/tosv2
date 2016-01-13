package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Stocktake;
import edu.must.tos.bean.StocktakeRecord;
import edu.must.tos.dao.StocktakeDAO;

public class StocktakeDAOImpl implements StocktakeDAO {

	public List getStocktakeByUpdata(Connection conn, String stockFormDate) throws Exception{
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select batch,stocktake_date,isbn,original_stock,after_stocktake,adjusted,upduid,upddate " +
					"from tstocktake " ;
			if(stockFormDate!=null && !stockFormDate.equals("")){
				sql += "where stocktake_date=to_date('"+stockFormDate+"', 'yyyy/MM/dd hh24:mi:ss') " ;
			}
			sql += "order by isbn,upddate desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			List containList = new ArrayList();
			while(rs.next()){
				if(!containList.contains(rs.getString("isbn"))){
					containList.add(rs.getString("isbn"));
					Stocktake st = new Stocktake();
					st.setBatch(rs.getInt("batch"));
					st.setStock_date(rs.getTimestamp("stocktake_date"));
					st.setIsbn(rs.getString("isbn"));
					st.setOriginal_stock(rs.getInt("original_stock"));
					st.setAfter_stocktake(rs.getInt("after_stocktake"));
					st.setAdjusted(rs.getInt("adjusted"));
					st.setUpduid(rs.getString("upduid"));
					st.setUpddate(rs.getTimestamp("upddate"));
					list.add(st);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return list;
	}
	
	public List getStocktakeRecord(Connection conn, String fromDate,
			String toDate, String intake) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select tbi.isbn, tb.title, tb.author, tb.edition, tb.publishyear, tb.publisher,"
					+ "t1.after_stocktake as after_stocktake1,t2.adjnumsum,t3.original_stock,"
					+ "t4.after_stocktake as after_stocktake2,tbi.stock "
					+ "from tbookinventory tbi "
					+ "inner join tbook tb on tb.isbn=tbi.isbn and tb.actind='Y' "
					+ "left outer join (select tst.isbn,tst.after_stocktake "
					+ "from tstocktake tst "
					+ "where tst.upddate = to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss')) t1 on t1.isbn=tbi.isbn "
					+ "left outer join (select tbsi.intake,tbsi.isbn,sum(tbsi.adjnum) as adjnumsum "
					+ "from tbookstockin tbsi "
					+ "where tbsi.credate between to_date('" + fromDate + "', 'yyyy-mm-dd hh24:mi:ss') "
					+ "and to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss') " 
					+ "and tbsi.intake='" + intake + "' "
					+ "group by tbsi.intake,tbsi.isbn ) t2 on t2.isbn=tbi.isbn "
					+ "left outer join (select tst.isbn,tst.original_stock "
					+ "from tstocktake tst "
					+ "where tst.upddate = to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss')) t3 on t3.isbn=tbi.isbn "
					+ "left outer join (select tst.isbn,tst.after_stocktake "
					+ "from tstocktake tst "
					+ "where tst.upddate = to_date('" + toDate + "', 'yyyy-mm-dd hh24:mi:ss')) t4 on t4.isbn=tbi.isbn "
					+ "order by isbn";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				StocktakeRecord record = new StocktakeRecord();
				record.setIsbn(rs.getString("isbn"));
				record.setTitle(rs.getString("title"));
				record.setAuthor(rs.getString("author"));
				record.setEdition(rs.getString("edition"));
				record.setPublisher(rs.getString("publisher"));
				record.setPublishYear(rs.getString("publishyear"));
				record.setAfter_stocktake1(rs.getInt("after_stocktake1"));
				record.setAdjnumsum(rs.getInt("adjnumsum"));
				record.setOriginal_stock(rs.getInt("original_stock"));
				record.setAfter_stocktake2(rs.getInt("after_stocktake2"));
				record.setStock(rs.getInt("stock"));
				list.add(record);
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

	public List getStocktakeList(Connection conn, Stocktake stocktake)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select batch,stocktake_date,isbn,original_stock,after_stocktake,adjusted,upduid,upddate "
					+ "from tstocktake where 1=1 ";
			if (stocktake.getIsbn() != null) {
				sql += "and isbn='" + stocktake.getIsbn() + "' ";
			}
			sql += "order by stocktake_date desc,isbn ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Stocktake s = new Stocktake();
				s.setBatch(rs.getInt("batch"));
				s.setStock_date(rs.getTimestamp("stocktake_date"));
				s.setIsbn(rs.getString("isbn"));
				s.setOriginal_stock(rs.getInt("original_stock"));
				s.setAfter_stocktake(rs.getInt("after_stocktake"));
				s.setAdjusted(rs.getInt("adjusted"));
				s.setUpduid(rs.getString("upduid"));
				s.setUpddate(rs.getTimestamp("upddate"));
				list.add(s);
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

	public boolean insertStocktake(Connection conn, List list)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		try {
			sql = "insert into tstocktake "
					+ "(batch, stocktake_date, isbn, original_stock, after_stocktake, adjusted, upduid, upddate) "
					+ "values (?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				Stocktake s = (Stocktake) list.get(i);
				pstmt.setInt(1, 0);
				pstmt.setTimestamp(2, ts);
				pstmt.setString(3, s.getIsbn());
				pstmt.setInt(4, s.getOriginal_stock());
				pstmt.setInt(5, s.getAfter_stocktake());
				pstmt.setInt(6, s.getAdjusted());
				pstmt.setString(7, s.getUpduid());
				pstmt.setTimestamp(8, ts);
				if (pstmt.executeUpdate() > 0) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}
	
	public boolean updateStocktake(Connection conn, Stocktake stocktake) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		try {
			sql = "update tstocktake "
					+ "set stocktake_date=?, isbn=?, original_stock=?, after_stocktake=?, adjusted=?, upduid=?, upddate=? "
					+ "where batch = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, stocktake.getStock_date());
			pstmt.setString(2, stocktake.getIsbn());
			pstmt.setInt(3, stocktake.getOriginal_stock());
			pstmt.setInt(4, stocktake.getAfter_stocktake());
			pstmt.setInt(5, stocktake.getAdjusted());
			pstmt.setString(6, stocktake.getUpduid());
			pstmt.setTimestamp(7, ts);
			pstmt.setInt(8, stocktake.getBatch());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return flag;
	}
	
	public Timestamp getMaxStocktakeDate(Connection conn) throws Exception {
		Timestamp stockDate = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select max(stocktake_date) as stocktakeDate from tstocktake ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				stockDate = rs.getTimestamp("stocktakeDate");
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return stockDate;
	}
	
	public Stocktake getStocktakeBean(Connection conn, Timestamp maxDate, String isbn) throws Exception {
		Stocktake stocktake = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select batch, stocktake_date, isbn, original_stock, after_stocktake, adjusted from tstocktake "+
				"where isbn=? and stocktake_date=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, isbn);
			pstmt.setTimestamp(2, maxDate);
			rs = pstmt.executeQuery();
			if(rs.next()){
				stocktake = new Stocktake();
				stocktake.setBatch(rs.getInt("batch"));
				stocktake.setStock_date(rs.getTimestamp("stocktake_date"));
				stocktake.setIsbn(rs.getString("isbn"));
				stocktake.setOriginal_stock(rs.getInt("original_stock"));
				stocktake.setAfter_stocktake(rs.getInt("after_stocktake"));
				stocktake.setAdjusted(rs.getInt("adjusted"));
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return stocktake;
	}

}
