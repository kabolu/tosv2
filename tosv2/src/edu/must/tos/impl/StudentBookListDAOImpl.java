package edu.must.tos.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.StudentBookList;
import edu.must.tos.dao.StudentBookListDAO;
import edu.must.tos.util.ToolsOfString;

public class StudentBookListDAOImpl implements StudentBookListDAO {
	/**
	 * @param conn
	 * @param studNo
	 * @return
	 * @throws Exception
	 */
	public List queryBook(Connection conn, String studNo) throws Exception{
		List list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;
		if (studNo != null && !studNo.equals("")) {
			sql = "select "
					+ " studentNo, isbn, bookTitle, author, publisher, publishYear, isEnrolled, " 
					+ " edition, language, bookType, remarks, courseCode, crsChineseName, crsEnglishName, " 
					+ " confirmQty, courseType, price, mop_price, mop_netprice, withdrawInd, supplement "
					+ " from V_BOOK_LIST_SEARCH"
					+ " where studentNo='" + studNo + "'";

		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				StudentBookList b = new StudentBookList();
				b.setStudentNo(rs.getString("studentNo"));
				b.setIsbn(rs.getString("isbn"));
				b.setBookTitle(ToolsOfString.NulltoEmpty(rs.getString("bookTitle")));
				b.setAuthor(ToolsOfString.NulltoEmpty(rs.getString("author")));
				b.setPublisher(ToolsOfString.NulltoEmpty(rs.getString("publisher")));
				b.setPublishYear(rs.getString("publishYear"));
				
				String isEnrolled = null;
				isEnrolled = rs.getString("isEnrolled");
				
				if(isEnrolled.equals("Y"))
					b.setIsEnrolled(true);
				else
					b.setIsEnrolled(false);
				
				b.setEdition(rs.getString("edition"));
				b.setLanguage(rs.getString("language"));
				b.setBookType(rs.getString("bookType"));
				b.setRemarks(ToolsOfString.NulltoEmpty(rs.getString("remarks")));
				b.setCourseCode(rs.getString("courseCode"));
				b.setCrsChineseName(rs.getString("crsChineseName"));
				b.setCrsEnglishName(rs.getString("crsEnglishName"));
				b.setConfirmQty(rs.getInt("confirmQty"));
				b.setCourseType(rs.getString("courseType"));
				b.setPrice(rs.getString("price"));
				b.setMopPrice(rs.getDouble("mop_price"));
				b.setNetMopPrice(rs.getDouble("mop_netprice"));
				b.setWithdrawInd(rs.getString("withdrawInd"));
				b.setSupplement(rs.getString("supplement"));
				list.add(b);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return list;
	}

	
}
