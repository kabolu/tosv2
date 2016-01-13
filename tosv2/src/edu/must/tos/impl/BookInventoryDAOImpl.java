package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.BookInventory;
import edu.must.tos.bean.BookInventoryRecord;
import edu.must.tos.dao.BookInventoryDAO;

public class BookInventoryDAOImpl implements BookInventoryDAO {

	public List getBookInventoryList(Connection conn) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select isbn, stock, upddate, upduid from tbookinventory ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookInventory bi = new BookInventory();
				bi.setIsbn(rs.getString("isbn"));
				bi.setStock(rs.getInt("stock"));
				bi.setUpddate(rs.getTimestamp("upddate"));
				bi.setUpduid(rs.getString("upduid"));
				list.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) 
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return list;
	}

	public BookInventory getBookInventoryByPK(Connection conn, String isbn)
			throws Exception {
		BookInventory inventory = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select isbn, stock, upddate, upduid from tbookinventory where isbn=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, isbn);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				inventory = new BookInventory();
				inventory.setIsbn(rs.getString("isbn"));
				inventory.setStock(rs.getInt("stock"));
				inventory.setUpddate(rs.getTimestamp("upddate"));
				inventory.setUpduid(rs.getString("upduid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return inventory;
	}

	public List getBookInventoryRecord(Connection conn, String intake, String fromDate)
			throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select tin.intake, tb.isbn, tb.title, sup.suppliername, "
					+ "ysum.confirmqty as yconfirmqty, nsum.confirmqty as nconfirmqty, "
					+ "tin.adjnumsum, tine.adjnumExportSum, tby.stock, rece.receiveqty, receA.receiveAqty, tout.outsum, tb.currency, tb.unitprice, tb.discount, tb.favourableprice, ";
					if(fromDate!=null && !fromDate.equals("")){
						sql += "tt.after_stocktake ";
					}else{
						sql += "'0' as after_stocktake ";
					}
					sql += "from tbook tb "
					+ "inner join tbookinventory tby on tby.isbn=tb.isbn "
					+ "left outer join (select intake, (case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end) as isbn,sum(adjnum) as adjnumsum "
					+ "from tbookstockin where actind='Y' " 
					+ "group by intake, (case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end)) tin on tin.isbn=tb.isbn "
					+ "left outer join (select intake, (case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end) as isbn,sum(adjnum) as adjnumExportSum "
					+ "from tbookstockin where actind='Y' and remarks='導入調整' " 
					+ "group by intake, (case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end)) tine on tine.isbn=tb.isbn and tine.intake=tin.intake "
					+ "left outer join (select (case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) as isbn,"
					+ "td.orderintake,sum(td.confirmqty) as confirmqty from torder o "
					+ "inner join tordetail td "
					+ "on td.orderseqno=o.orderseqno and td.orderintake=o.orderintake and o.paidstatus='Y' "
					+ "where o.actind='Y' and td.actind='Y' "
					+ "group by td.orderintake,(case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end)) ysum on ysum.isbn=tb.isbn and ysum.orderintake=tin.intake "
					+ "left outer join (select (case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) as isbn,td.orderintake,sum(td.confirmqty) as confirmqty from torder o "
					+ "inner join tordetail td "
					+ "on td.orderseqno=o.orderseqno and td.orderintake=o.orderintake and o.paidstatus='N' "
					+ "where o.actind='Y' and td.actind='Y' "
					+ "group by td.orderintake,(case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end)) nsum on nsum.isbn=tb.isbn and nsum.orderintake=tin.intake "
					+ "left outer join (select td.isbn as isbn, " 
					+ "td.orderintake,sum(case when td.notenoughqty>0 then (td.confirmqty-td.notenoughqty) else td.confirmqty end) as receiveqty " 
					+ "from torder o "
					+ "inner join tordetail td on td.orderseqno=o.orderseqno and td.confirmqty>0 and (td.notenoughqty=0 or td.notenoughqty>0) "
					+ "where o.actind='Y' and td.actind='Y' and o.paidstatus='Y' "
					+ "group by td.orderintake, td.isbn ) rece on rece.isbn=tb.isbn and rece.orderintake=tin.intake " 
					
					+ "left outer join ("
					+ "select distinct ti.isbn , o.indate, o.supplierno, bs.suppliername from tbookstockinorder o "
					+ "inner join tbookstockin ti on ti.prnum = o.prnum and ti.actind = 'Y' " 
					+ "inner join ( select ti.isbn , max (o.indate ) as indate from tbookstockinorder o " 
					+ "inner join tbookstockin ti on ti.prnum = o.prnum and ti.actind = 'Y' " 
					+ "where o.intake = '" + intake +"' and o.actind = 'Y' group by ti.isbn ) " 
					+ "t on t.isbn = ti.isbn and t.indate = o.indate inner join tbooksupplier bs on BS.supplierno = o.supplierno " 
					+ "where o.intake = '"+ intake +"' and o.actind = 'Y' "
					+ ") sup on sup.isbn = tb.isbn "
					
					+ "left outer join (select (case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) as isbn, td.orderintake," 
					+ "sum(case when td.notenoughqty>0 then (td.confirmqty-td.notenoughqty) else td.confirmqty end) as receiveAqty " 
					+ "from torder o "
					+ "inner join tordetail td on td.orderseqno=o.orderseqno and td.confirmqty>0 and (td.notenoughqty=0 or td.notenoughqty>0) "
					+ "where o.actind='Y' and td.actind='Y' and o.paidstatus='Y' and upper(td.isbn) like '%A' "
					+ "group by td.orderintake,(case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end)) receA on receA.isbn=tb.isbn and receA.orderintake=tin.intake "
					
					+ "left outer join (select intake,(case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end) as isbn,"
					+ "sum(adjnum) as outsum from tbookstockout where actind='Y' " 
					+ "group by intake, (case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end) ) tout on tout.isbn=tb.isbn and tout.intake=tin.intake ";
					if(fromDate!=null && !fromDate.equals("")){
						sql += "left outer join (select isbn,after_stocktake,stocktake_date from tstocktake ) tt on tt.isbn=tb.isbn and tt.stocktake_date=to_date('"+fromDate+"', 'yyyy-mm-dd hh24:mi:ss') ";
					}
					sql += "where tb.actind='Y' and tin.intake=? ";
					sql += "union " 
					+ "select '"+intake+"' as intake, tb.isbn, tb.title, sup.suppliername, ysum.confirmqty as yconfirmqty, nsum.confirmqty as nconfirmqty,"
					+ "0 as adjnumsum, 0 as adjnumExportSum, tby.stock, rece.receiveqty, receA.receiveAqty, tout.outsum, tb.currency, tb.unitprice, tb.discount, tb.favourableprice, ";
					if(fromDate!=null && !fromDate.equals("")){
						sql += "tt.after_stocktake ";
					}else{
						sql += "'0' as after_stocktake ";
					}
					sql += "from tbook tb "
					+ "inner join tbookinventory tby on tby.isbn=tb.isbn "
					+ "left outer join (select (case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) as isbn,"
					+ "td.orderintake,sum(td.confirmqty) as confirmqty "
					+ "from torder o "
					+ "inner join tordetail td on td.orderseqno=o.orderseqno and td.orderintake=o.orderintake and o.paidstatus='Y' "
					+ "where o.actind='Y' and td.actind='Y' "
					+ "group by td.orderintake,(case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) ) "
					+ "ysum on ysum.isbn=tb.isbn and ysum.orderintake='"+intake+"' "
					+ "left outer join (select (case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) as isbn,"
					+ "td.orderintake,sum(td.confirmqty) as confirmqty "
					+ "from torder o "
					+ "inner join tordetail td on td.orderseqno=o.orderseqno and td.orderintake=o.orderintake and o.paidstatus='N' "
					+ "where o.actind='Y' and td.actind='Y' "
					+ "group by td.orderintake,(case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end)) nsum on nsum.isbn=tb.isbn and nsum.orderintake='"+intake+"' "
					
					+ "left outer join (select td.isbn as isbn, td.orderintake, " 
					+ "sum(case when td.notenoughqty>0 then (td.confirmqty-td.notenoughqty) else td.confirmqty end) as receiveqty "
					+ "from torder o " 
					+ "inner join tordetail td on td.orderseqno=o.orderseqno and td.confirmqty>0 and (td.notenoughqty=0 or td.notenoughqty>0) "
					+ "where o.actind='Y' and td.actind='Y' and o.paidstatus='Y' "
					+ "group by td.orderintake, td.isbn "
					+ ") rece on rece.isbn=tb.isbn and rece.orderintake='"+intake+"' "
					
					+ "left outer join ("
					+ "select distinct ti.isbn , o.indate, o.supplierno, bs.suppliername from tbookstockinorder o "
					+ "inner join tbookstockin ti on ti.prnum = o.prnum and ti.actind = 'Y' " 
					+ "inner join ( select ti.isbn , max (o.indate ) as indate from tbookstockinorder o " 
					+ "inner join tbookstockin ti on ti.prnum = o.prnum and ti.actind = 'Y' " 
					+ "where o.intake = '" + intake +"' and o.actind = 'Y' group by ti.isbn ) " 
					+ "t on t.isbn = ti.isbn and t.indate = o.indate inner join tbooksupplier bs on BS.supplierno = o.supplierno " 
					+ "where o.intake = '"+ intake +"' and o.actind = 'Y' "
					+ ") sup on sup.isbn = tb.isbn "
					
					+ "left outer join (select (case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) as isbn, td.orderintake," 
					+ "sum(case when td.notenoughqty>0 then (td.confirmqty-td.notenoughqty) else td.confirmqty end) as receiveAqty "
					+ "from torder o " 
					+ "inner join tordetail td on td.orderseqno=o.orderseqno and td.confirmqty>0 and (td.notenoughqty=0 or td.notenoughqty>0) "
					+ "where o.actind='Y' and td.actind='Y' and o.paidstatus='Y' and upper(td.isbn) like '%A' "
					+ "group by td.orderintake,(case when (instr(upper(td.isbn), 'A')-1) >= 0 then substr(td.isbn, 0, (instr(upper(td.isbn), 'A')-1)) else td.isbn end) "
					+ ") receA on receA.isbn=tb.isbn and receA.orderintake='"+intake+"' "
					
					+ "left outer join (select intake,(case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end) as isbn,sum(adjnum) as outsum "
					+ "from tbookstockout where actind='Y' "
					+ "group by intake,(case when (instr(upper(isbn), 'A')-1) >= 0 then substr(isbn, 0, (instr(upper(isbn), 'A')-1)) else isbn end) "
					+ ") tout on tout.isbn=tb.isbn and tout.intake='"+intake+"' ";
					if(fromDate!=null && !fromDate.equals("")){
						sql += "left outer join (select isbn,after_stocktake,stocktake_date from tstocktake ) tt on tt.isbn=tb.isbn and tt.stocktake_date=to_date('"+fromDate+"', 'yyyy-mm-dd hh24:mi:ss') ";
					}
					sql += "where tb.actind='Y' order by isbn";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookInventoryRecord record = new BookInventoryRecord();
				record.setIntake(rs.getString("intake"));
				record.setIsbn(rs.getString("isbn"));
				record.setTitle(rs.getString("title"));
				record.setSupplierName(rs.getString("suppliername"));
				record.setAdjnumSum(rs.getInt("adjnumsum") - rs.getInt("adjnumExportSum")); //總入貨數 減去 導入調整的數量
				record.setStock(rs.getInt("stock"));
				record.setPaidYSum(rs.getInt("yconfirmqty"));
				record.setPaidNSum(rs.getInt("nconfirmqty"));
				record.setReceiveNum(rs.getInt("receiveqty"));
				record.setReceiveANum(rs.getInt("receiveAqty"));
				record.setOutSum(rs.getInt("outsum"));
				if(fromDate!=null && !fromDate.equals("")){
					record.setStocktake(rs.getInt("after_stocktake"));
				}
				record.setCurrency(rs.getString("currency"));
				record.setUnitPrice(rs.getDouble("unitprice"));
				record.setDisCount(rs.getDouble("discount"));
				record.setFavourablePrice(rs.getDouble("favourableprice"));
				list.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return list;
	}

	public boolean updateBookInventory(Connection conn, List<BookInventory> list)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null, insertPstmt = null, updatePstmt = null;
		ResultSet rs = null;
		String sql = null, insertSql = null, updateSql = null;
		try {
			sql = "select isbn from tbookinventory where isbn=? ";
			pstmt = conn.prepareStatement(sql);
			insertSql = "insert into tbookinventory (isbn, stock, upddate, upduid) values (?, ?, ?, ?) ";
			insertPstmt = conn.prepareStatement(insertSql);

			updateSql = "update tbookinventory set stock=stock+?, upddate=?, upduid=? where isbn=? ";
			updatePstmt = conn.prepareStatement(updateSql);
			for (BookInventory b : list) {
				pstmt.setString(1, b.getIsbn());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					updatePstmt.setInt(1, b.getStock());
					updatePstmt.setTimestamp(2, new java.sql.Timestamp(b.getUpddate().getTime()));
					updatePstmt.setString(3, b.getUpduid());
					updatePstmt.setString(4, b.getIsbn());
					if (updatePstmt.executeUpdate() > 0) {
						flag = true;
					} else {
						break;
					}
				} else {
					insertPstmt.setString(1, b.getIsbn());
					insertPstmt.setInt(2, b.getStock());
					insertPstmt.setTimestamp(3, new java.sql.Timestamp(b.getUpddate().getTime()));
					insertPstmt.setString(4, b.getUpduid());
					if (insertPstmt.executeUpdate() > 0) {
						flag = true;
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (insertPstmt != null)
				insertPstmt.close();
			if (updatePstmt != null)
				updatePstmt.close();
		}
		return flag;
	}

	public boolean updateBookInventoryByIsbn(Connection conn, BookInventory bookInventory) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookinventory set stock=stock+?, upddate=?, upduid=? where isbn=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookInventory.getStock());
			pstmt.setTimestamp(2, new java.sql.Timestamp(bookInventory.getUpddate().getTime()));
			pstmt.setString(3, bookInventory.getUpduid());
			pstmt.setString(4, bookInventory.getIsbn());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}
	
	public boolean updateBookInventoryByIsbn(Connection conn, BookInventory bookInventory, String isbn) throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update tbookinventory set isbn=?, upddate=?, upduid=? where isbn=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookInventory.getIsbn());
			pstmt.setTimestamp(2, new java.sql.Timestamp(bookInventory.getUpddate().getTime()));
			pstmt.setString(3, bookInventory.getUpduid());
			pstmt.setString(4, isbn);
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

}
