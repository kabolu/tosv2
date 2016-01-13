package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.BookPurchasing;
import edu.must.tos.bean.BookPurchasingBean;
import edu.must.tos.dao.BookPurchasingDAO;

public class BookPurchasingDAOImpl implements BookPurchasingDAO {

	public int getbefPurchaseQty(Connection conn, String intake, String isbn, String beforeTime) throws Exception{
		int befQuantity = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "select intake, isbn, sum(quantity) as pQuantity from tbookpurchasing "
					   + "where actind='Y' " 
					   + "and intake='"+intake+"' and isbn='"+isbn+"' "
					   + "and orderdate < '"+beforeTime+"' "
				       + "group by intake, isbn ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				befQuantity = rs.getInt("pQuantity");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs != null)
				rs.close();
			if(pstmt != null)
				pstmt.close();
		}
		return befQuantity;
	}
	
	public List getBookPurchasingList(Connection conn, String curIntake,
			String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select t.orderno,t.isbn,tb.title,t.quantity,t.orderpp,t.orderby,"
					+ "(case t.orderby when 'OTHER' then t.orderpp else "
					+ "(select tbs.suppliername from tbooksupplier tbs where tbs.actind='Y' and to_char(tbs.supplierno)=t.orderby) "
					+ "end) as suppliername,"
					+ "t.remarks,t.issued,t.orderdate,t.intake,t.costprice, t.creuid "
					+ "from tbookpurchasing t "
					+ "inner join tbook tb on tb.isbn = t.isbn and tb.actind='Y' "
					+ "where t.actind='Y' ";
			if (curIntake != null) {
				sql += "and t.intake='" + curIntake + "' ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and t.orderdate between '" + fromDate + "' and '" + toDate + "' ";
			}
			sql += "order by t.orderdate desc, t.orderno ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasingBean bean = new BookPurchasingBean();
				bean.setOrderNo(rs.getString("orderno"));
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBookPurchase().setQuantity(rs.getInt("quantity"));
				bean.getBookPurchase().setOrderBy(rs.getString("suppliername"));
				bean.getBookPurchase().setRemarks(rs.getString("remarks"));
				bean.getBookPurchase().setIssued(rs.getString("issued"));
				bean.getBookPurchase().setOrderDate(rs.getString("orderdate"));
				bean.getBookPurchase().setIntake(rs.getString("intake"));
				bean.getBookPurchase().setCostPrice(rs.getDouble("costprice"));
				bean.getBookPurchase().setCreUid(rs.getString("creuid"));
				list.add(bean);
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

	public List getBookPurchasingSummByIntake(Connection conn, String intake,
			String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select tbp.intake,tbp.isbn,tb.title,tb.author,tb.publisher,tb.publishYear,tb.edition,tb.language,tb.bookType,tb.remarks,"
					+ "(select tbs.suppliername from tbooksupplier tbs where tbs.actind='Y' and tbs.suppliercode=tb.suppliercode1) as suppliercode1,"
					+ "(select tbs.suppliername from tbooksupplier tbs where tbs.actind='Y' and tbs.suppliercode=tb.suppliercode2) as suppliercode2,"
					+ "tb.unitprice, tb.currency, tb.discount, tb.favourableprice, "
					+ "sum(quantity) as quantity "
					+ "from tbookpurchasing tbp "
					+ "inner join tbook tb on tb.isbn=tbp.isbn and tb.actind='Y' "
					+ "where tbp.actind='Y' ";
			if (intake != null && !intake.equals("")) {
				sql += "and tbp.intake='" + intake + "' ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null
					&& !toDate.equals("")) {
				sql += "and tbp.orderdate between '" + fromDate + "' and '" + toDate + "' ";
			}
			sql += "group by tbp.intake,tbp.isbn,tb.title,tb.author,tb.publisher,tb.publishYear,tb.edition,tb.language,"
					+ "tb.bookType,tb.remarks,suppliercode1,suppliercode2, tb.unitprice, tb.currency, tb.discount, tb.favourableprice ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasingBean bean = new BookPurchasingBean();
				bean.getBookPurchase().setIntake(rs.getString("intake"));
				bean.getBookPurchase().setRemarks(rs.getString("remarks"));
				bean.getBookPurchase().setQuantity(rs.getInt("quantity"));
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishYear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.getBook().setLanguage(rs.getString("language"));
				bean.getBook().setBookType(rs.getString("bookType"));
				bean.getBook().setSupplierCode1(rs.getString("suppliercode1"));
				bean.getBook().setSupplierCode2(rs.getString("suppliercode2"));
				bean.getBook().setUnitPrice(rs.getDouble("unitprice"));
				bean.getBook().setCurrency(rs.getString("currency"));
				bean.getBook().setDisCount(rs.getDouble("discount"));
				bean.getBook().setFavourablePrice(rs.getDouble("favourableprice"));
				list.add(bean);
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

	public List getBookPurchasingBeanByOrderNo(Connection conn, String orderNo)
			throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select tp.id,tp.orderno,tp.isbn,tp.quantity,tp.orderby,tp.remarks,tp.issued,tp.orderdate,tp.actind,tp.costprice,"
					+ "tp.credate,tp.creuid,tp.upddate,tp.upduid,tb.title,tb.author,tb.edition,tb.publisher,tb.publishyear,"
					+ "tp.orderpp,tp.intake,tp.leave "
					+ "from tbookpurchasing tp "
					+ "inner join tbook tb on tb.isbn=tp.isbn and tb.actind='Y' "
					+ "where tp.actind='Y' ";
			if (orderNo != null && !orderNo.equals("")) {
				sql += "and tp.orderno='" + orderNo + "' ";
			}
			sql += "order by tp.orderno,tp.orderdate ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasingBean bean = new BookPurchasingBean();
				bean.setId(rs.getInt("id"));
				bean.setOrderNo(rs.getString("orderno"));
				bean.getBookPurchase().setOrderBy(rs.getString("orderby"));
				bean.getBookPurchase().setIssued(rs.getString("issued"));
				bean.getBookPurchase().setOrderDate(rs.getString("orderdate"));
				bean.getBookPurchase().setOrderPp(rs.getString("orderpp"));
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishyear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.getBookPurchase().setQuantity(rs.getInt("quantity"));
				bean.getBookPurchase().setRemarks(rs.getString("remarks"));
				bean.getBookPurchase().setIntake(rs.getString("intake"));
				bean.getBookPurchase().setLeave(rs.getInt("leave"));
				bean.getBookPurchase().setCostPrice(rs.getDouble("costprice"));
				resultList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resultList;
	}

	public List getBookPurchasingBeanBySupplierCode(Connection conn,
			String supplierCode, String intake) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select tp.id, tp.orderno, tp.isbn, tp.quantity, tp.orderby, tp.remarks, tp.issued, tp.orderdate, tp.actind, "
					+ "tp.credate, tp.creuid, tp.upddate, tp.upduid, tb.title, tb.author, tb.edition, tb.publisher, tb.publishyear, "
					+ "tp.orderpp, tp.intake, tp.leave, tp.costprice "
					+ "from tbookpurchasing tp "
					+ "inner join tbook tb on tb.isbn=tp.isbn and tb.actind='Y' "
					+ "where tp.actind='Y' ";
			if (supplierCode != null && !supplierCode.equals("")) {
				sql += "and tp.orderno like '%" + supplierCode + "%' ";
			}
			sql += "order by tp.orderno,tp.orderdate ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasingBean bean = new BookPurchasingBean();
				bean.setId(rs.getInt("id"));
				bean.setOrderNo(rs.getString("orderno"));
				bean.getBookPurchase().setOrderBy(rs.getString("orderby"));
				bean.getBookPurchase().setIssued(rs.getString("issued"));
				bean.getBookPurchase().setOrderDate(rs.getString("orderdate"));
				bean.getBookPurchase().setOrderPp(rs.getString("orderpp"));
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishyear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.getBookPurchase().setQuantity(rs.getInt("quantity"));
				bean.getBookPurchase().setRemarks(rs.getString("remarks"));
				bean.getBookPurchase().setIntake(rs.getString("intake"));
				bean.getBookPurchase().setLeave(rs.getInt("leave"));
				bean.getBookPurchase().setCostPrice(rs.getDouble("costprice"));
				resultList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resultList;
	}

	public List getBookPurchasingByOrderNo(Connection conn, String orderNo,
			String fromDate, String toDate) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select id,orderno,isbn,quantity,orderby,remarks,issued,costprice,orderdate,actind,credate,creuid,upddate,upduid,"
					+ "intake,"
					+ "(case when orderby='OTHER' then orderpp else (select suppliername from tbooksupplier where actind='Y' and to_char(supplierno)=orderby) end) as orderpp "
					+ "from tbookpurchasing " 
					+ "where actind='Y' ";
			if (orderNo != null && !orderNo.equals("")) {
				sql += "and orderno='" + orderNo + "' ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null
					&& !toDate.equals("")) {
				sql += "and orderdate between '" + fromDate + "' and '"
						+ toDate + "' ";
			}
			sql += "order by orderno,orderdate ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasing bean = new BookPurchasing();
				bean.setId(rs.getInt("id"));
				bean.setOrderNo(rs.getString("orderno"));
				bean.setIsbn(rs.getString("isbn"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.setOrderBy(rs.getString("orderby"));
				bean.setOrderPp(rs.getString("orderpp"));
				bean.setRemarks(rs.getString("remarks"));
				bean.setIssued(rs.getString("issued"));
				bean.setOrderDate(rs.getString("orderdate"));
				bean.setActind(rs.getString("actind"));
				bean.setCreDate(rs.getTimestamp("credate"));
				bean.setCreUid(rs.getString("creuid"));
				bean.setUpdDate(rs.getTimestamp("upddate"));
				bean.setUpdUid(rs.getString("upduid"));
				bean.setIntake(rs.getString("intake"));
				bean.setCostPrice(rs.getDouble("costprice"));
				resultList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resultList;
	}

	public List getBookPurchasingInfo(Connection conn, String intake,
			String fromDate, String toDate, String supplierNo) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select distinct tp.isbn,tb.title,tb.author,tb.publisher,tb.publishyear,tb.edition,"
					+ "tmp.quantity,tmp1.confirmqty "
					+ "from tbookpurchasing tp "
					+ "inner join tbook tb on tb.isbn=tp.isbn "
					+ "inner join (select intake,isbn,sum(quantity) as quantity "
					+ "            from tbookpurchasing "
					+ "            where actind='Y' ";
			if (!supplierNo.equals("0")) {
				sql += "and orderby='" + supplierNo + "' ";
			}
			sql += "            group by intake, isbn) tmp on tmp.isbn=tp.isbn and tmp.intake = tp.intake "
					+ "left outer join (select td.isbn, sum(td.confirmqty) as confirmqty "
					+ "                 from torder o "
					+ "                 inner join tordetail td on td.orderseqno=o.orderseqno and td.actind='Y' "
					+ "                 where o.orderintake=? and o.actind='Y' and o.studentno<>'RETAIL' "
					+ "                 and o.paidstatus='Y' and o.creuid<>'NEWSTUD' "
					+ "                 group by td.isbn) tmp1 on tmp1.isbn=tp.isbn "
					+ "where tp.actind='Y' and tp.intake = '"+intake+"' ";
			if (!supplierNo.equals("0")) {
				sql += "and tp.orderby=? ";
			}
			if (fromDate != null && !fromDate.equals("") && toDate != null
					&& !toDate.equals("")) {
				sql += "and tp.orderdate between ? and ? ";
			}
			sql += "order by tp.isbn";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, intake);
			pstmt.setString(2, String.valueOf(supplierNo));
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				pstmt.setString(3, fromDate);
				pstmt.setString(4, toDate);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasingBean bean = new BookPurchasingBean();
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishyear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.setTotalQuantitySum(rs.getInt("quantity"));
				bean.setTotalConfirmSum(rs.getInt("confirmqty"));
				resultList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resultList;
	}

	public boolean addBookPurchasing(Connection conn, BookPurchasing bean)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into tbookpurchasing "
					+ "(id, orderno, isbn, quantity, orderpp, orderby, remarks, issued, orderdate, actind, creuid, credate, upduid, upddate, intake, leave, costprice) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bean.getId());
			pstmt.setString(2, bean.getOrderNo());
			pstmt.setString(3, bean.getIsbn());
			pstmt.setInt(4, bean.getQuantity());
			pstmt.setString(5, bean.getOrderPp());
			pstmt.setString(6, bean.getOrderBy());
			pstmt.setString(7, bean.getRemarks());
			pstmt.setString(8, bean.getIssued());
			pstmt.setString(9, bean.getOrderDate());
			pstmt.setString(10, bean.getActind());
			pstmt.setString(11, bean.getCreUid());
			pstmt.setTimestamp(12, new java.sql.Timestamp(bean.getCreDate().getTime()));
			pstmt.setString(13, bean.getUpdUid());
			pstmt.setTimestamp(14, new java.sql.Timestamp(bean.getUpdDate().getTime()));
			pstmt.setString(15, bean.getIntake());
			pstmt.setInt(16, bean.getQuantity());
			pstmt.setDouble(17, bean.getCostPrice());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public boolean updateBookPurchasing(Connection conn, BookPurchasing bean)
			throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			String sql = "update tbookpurchasing set "
					+ "quantity=?, orderpp=?, orderby=?, remarks=?, issued=?, orderdate=?, actind=?, upduid=?, upddate=?, intake=?, leave=?, costprice=? "
					+ "where id=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bean.getQuantity());
			pstmt.setString(2, bean.getOrderPp());
			pstmt.setString(3, bean.getOrderBy());
			pstmt.setString(4, bean.getRemarks());
			pstmt.setString(5, bean.getIssued());
			pstmt.setString(6, bean.getOrderDate());
			pstmt.setString(7, bean.getActind());
			pstmt.setString(8, bean.getUpdUid());
			pstmt.setTimestamp(9, new java.sql.Timestamp(bean.getUpdDate().getTime()));
			pstmt.setString(10, bean.getIntake());
			pstmt.setInt(11, bean.getLeave());
			pstmt.setDouble(12, bean.getCostPrice());
			pstmt.setInt(13, bean.getId());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return flag;
	}

	public boolean updateBookPurchasingByOrderNo(Connection conn,
			BookPurchasing bean) throws Exception {
		boolean flag = true;
		PreparedStatement pstmt = null;
		try {
			String sql = "update tbookpurchasing set orderpp=?, orderby=?, orderdate=?, issued=?, upduid=?, upddate=?, intake=? "
					+ "where orderno=? and actind=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getOrderPp());
			pstmt.setString(2, bean.getOrderBy());
			pstmt.setString(3, bean.getOrderDate());
			pstmt.setString(4, bean.getIssued());
			pstmt.setString(5, bean.getUpdUid());
			pstmt.setTimestamp(6, new java.sql.Timestamp(bean.getUpdDate().getTime()));
			pstmt.setString(7, bean.getIntake());
			pstmt.setString(8, bean.getOrderNo());
			pstmt.setString(9, bean.getActind());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public boolean updateBookPurchasingBySupplierNo(Connection conn,
			String userId, String supplierNo, String fromDate, String toDate,
			String intake) throws Exception {
		boolean flag = true;
		PreparedStatement pstmt = null;
		try {
			String sql = "update tbookpurchasing set issued=?,upduid=?,upddate=? "
					+ "where orderby=? and actind=? and intake=? ";
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				sql += "and orderdate between ? and ? ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setString(2, userId);
			pstmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
			pstmt.setString(4, supplierNo);
			pstmt.setString(5, "Y");
			pstmt.setString(6, intake);
			if (fromDate != null && !fromDate.equals("") && toDate != null && !toDate.equals("")) {
				pstmt.setString(7, fromDate);
				pstmt.setString(8, toDate);
			}
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public List getBookPurchasingReport(Connection conn, String intake,
			String fromDate, String toDate) throws Exception {
		List list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select tbp.intake,(select tbs.suppliername from tbooksupplier tbs "
					+ "where tbs.actind='Y' and to_char(tbs.supplierno)=tbp.orderby) as suppliername,"
					+ "sum(quantity) as quantity,"
					+ "tbp.isbn, tb.title, tb.author, tb.publisher, tb.publishyear, tb.edition, tbp.creuid "
					+ "from tbookpurchasing tbp "
					+ "inner join tbook tb on tb.isbn=tbp.isbn "
					+ "where tbp.actind='Y' and tbp.orderby<>'OTHER' ";
			if (fromDate != null && !fromDate.equals("") && toDate != null
					&& !toDate.equals("")) {
				sql += "and orderdate between '" + fromDate + "' and '"
						+ toDate + "' ";
			}
			sql += "group by tbp.intake,tbp.orderby,tbp.isbn,tb.title,tb.author,tb.publisher,tb.publishyear,tb.edition,tbp.creuid ";
			sql += "union ";
			sql += "select tbp.intake,tbp.orderpp,"
					+ "sum(quantity) as quantity,"
					+ "tbp.isbn, tb.title, tb.author, tb.publisher, tb.publishyear, tb.edition, tbp.creuid "
					+ "from tbookpurchasing tbp "
					+ "inner join tbook tb on tb.isbn=tbp.isbn "
					+ "where tbp.actind='Y' and tbp.orderby='OTHER' ";
			if (fromDate != null && !fromDate.equals("") && toDate != null
					&& !toDate.equals("")) {
				sql += "and orderdate between '" + fromDate + "' and '"
						+ toDate + "' ";
			}
			sql += "group by tbp.intake,tbp.orderpp,tbp.isbn,tb.title,tb.author,tb.publisher,tb.publishyear,tb.edition,tbp.creuid ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasingBean bean = new BookPurchasingBean();
				bean.getBookPurchase().setOrderBy(rs.getString("suppliername"));
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishyear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.getBookPurchase().setQuantity(rs.getInt("quantity"));
				bean.getBookPurchase().setCreUid(rs.getString("creuid"));
				list.add(bean);
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

	public List getOrderNoList(Connection conn, String orderNo) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select orderno from tbookpurchasing where orderno like ? order by orderno desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + orderNo + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				resultList.add(rs.getString("orderno"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public BookPurchasing getBookPurchasingById(Connection conn, int id)
			throws Exception {
		BookPurchasing bookPurchasing = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select id,orderno,isbn,quantity,orderby,remarks,issued,orderdate,actind,credate,creuid,upddate,upduid,"
					+ "intake,leave,costprice "
					+ "from tbookpurchasing "
					+ "where actind='Y' and id=" + id + " ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bookPurchasing = new BookPurchasing();
				bookPurchasing.setId(rs.getInt("id"));
				bookPurchasing.setOrderNo(rs.getString("orderno"));
				bookPurchasing.setIsbn(rs.getString("isbn"));
				bookPurchasing.setQuantity(rs.getInt("quantity"));
				bookPurchasing.setOrderBy(rs.getString("orderby"));
				bookPurchasing.setRemarks(rs.getString("remarks"));
				bookPurchasing.setIssued(rs.getString("issued"));
				bookPurchasing.setOrderDate(rs.getString("orderdate"));
				bookPurchasing.setActind(rs.getString("actind"));
				bookPurchasing.setCreDate(rs.getTimestamp("credate"));
				bookPurchasing.setCreUid(rs.getString("creuid"));
				bookPurchasing.setUpdDate(rs.getTimestamp("upddate"));
				bookPurchasing.setUpdUid(rs.getString("upduid"));
				bookPurchasing.setIntake(rs.getString("intake"));
				bookPurchasing.setLeave(rs.getInt("leave"));
				bookPurchasing.setCostPrice(rs.getDouble("costprice"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return bookPurchasing;
	}
	
	public List getBookPurchasingBeanByIsbn(Connection conn, String isbn, String curIntake, 
			String fromDate, String toDate, boolean actFlag) throws Exception {
		List resultList = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select tp.id,tp.orderno,tp.isbn,tp.quantity,tp.orderby,tp.remarks,tp.issued,tp.orderdate,tp.actind,"
					+ "tp.credate,tp.creuid,tp.upddate,tp.upduid,tp.costprice,tb.title,tb.author,tb.edition,tb.publisher,tb.publishyear,"
					+ "tp.orderpp,tp.intake,tp.leave "
					+ "from tbookpurchasing tp "
					+ "inner join tbook tb on tb.isbn=tp.isbn and tb.actind='Y' "
					+ "where tp.actind='Y' ";
			if (isbn != null && !isbn.equals("")) {
				sql += "and tp.isbn = '" + isbn + "' ";
			}
			if (curIntake != null && !curIntake.equals("")) {
				sql += "and tp.intake = '" + curIntake + "' ";
			}
			if(fromDate != null && toDate != null){
				sql += "and tp.orderdate >= '"+ fromDate +"' and tp.orderdate <= '"+ toDate +"' ";
			}
			if (actFlag){
				sql += "and tp.quantity = tp.leave ";
			}
			sql += "order by tp.orderno,tp.orderdate ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookPurchasingBean bean = new BookPurchasingBean();
				bean.setId(rs.getInt("id"));
				bean.setOrderNo(rs.getString("orderno"));
				bean.getBookPurchase().setOrderBy(rs.getString("orderby"));
				bean.getBookPurchase().setIssued(rs.getString("issued"));
				bean.getBookPurchase().setOrderDate(rs.getString("orderdate"));
				bean.getBookPurchase().setOrderPp(rs.getString("orderpp"));
				bean.getBook().setIsbn(rs.getString("isbn"));
				bean.getBook().setTitle(rs.getString("title"));
				bean.getBook().setAuthor(rs.getString("author"));
				bean.getBook().setPublisher(rs.getString("publisher"));
				bean.getBook().setPublishYear(rs.getString("publishyear"));
				bean.getBook().setEdition(rs.getString("edition"));
				bean.getBookPurchase().setQuantity(rs.getInt("quantity"));
				bean.getBookPurchase().setRemarks(rs.getString("remarks"));
				bean.getBookPurchase().setIntake(rs.getString("intake"));
				bean.getBookPurchase().setLeave(rs.getInt("leave"));
				bean.getBookPurchase().setCostPrice(rs.getDouble("costprice"));
				resultList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resultList;
	}
	
	public boolean changeIsbnList(Connection conn, BookPurchasingBean bean)throws Exception {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = null;
		java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		try {
			sql = "update tbookpurchasing set isbn = ?, upddate = ?, upduid = ?, changeRemarks = ? where id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getBook().getIsbn());
			pstmt.setTimestamp(2, ts);
			pstmt.setString(3, bean.getBookPurchase().getUpdUid());
			pstmt.setString(4, bean.getChangeRemarks());
			pstmt.setInt(5, bean.getId());			
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		throw new Exception(e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
}
