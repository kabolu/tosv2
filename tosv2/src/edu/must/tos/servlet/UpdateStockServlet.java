package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.FileItem;

import edu.must.tos.bean.BookInventory;
import edu.must.tos.bean.BookStockIn;
import edu.must.tos.bean.Stocktake;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookInventoryDAOImpl;
import edu.must.tos.impl.BookStockInDAOImpl;
import edu.must.tos.impl.StocktakeDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.util.UploadFile;

public class UpdateStockServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UpdateStockServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		HttpSession session = request.getSession();
		try{
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
			BookStockInDAOImpl bookStockInDAOImpl = new BookStockInDAOImpl();
			StocktakeDAOImpl stocktakeDAOImpl = new StocktakeDAOImpl();
			SysConfigDAOImpl sysConfigDAOImpl = new SysConfigDAOImpl();
			
			String key = "CURRINTAKE";
			String intake = sysConfigDAOImpl.getCurIntake(conn, key);
			
			String userId = (String)session.getAttribute("userId");
			
			int size = 3;	//設置上傳文件大小
			List list = UploadFile.upload(request, size);
			List elementsList = null;
			String sizeInfo = "";
			FileItem fileItem = null;
			if(list!=null && list.size()==3){
				fileItem = (FileItem)list.get(0);
				elementsList = (List)list.get(1);
				sizeInfo = (String)list.get(2);
			}
			if(sizeInfo.equals("wrong")){
				request.setAttribute("type", "updatestock");
				request.setAttribute("msg", "上傳文件超過"+size+"M！");
				request.getRequestDispatcher("msg.jsp").forward(request, response);
			}else{
				if(fileItem!=null){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String prNum = df.format(new Date());
					List dataList = getExcelDate(conn, fileItem, request);
					Map map = (HashMap)dataList.get(0);
					if(map.isEmpty()){
						List<Stocktake> detailList = (List)dataList.get(1);
						if(!detailList.isEmpty()){
							List<BookInventory> bookInventoryList = new ArrayList();
							List<BookStockIn> bookStockInList = new ArrayList();							
							for(Stocktake s : detailList){
								BookStockIn stockIn = new BookStockIn();
								stockIn.setIntake(intake);
								stockIn.setPRnum(prNum);
								stockIn.setIsbn(s.getIsbn());
								stockIn.setCredate(new Date());
								stockIn.setCreuid(userId);
								stockIn.setRemarks("導入調整");
								stockIn.setAdjnum(-s.getAdjusted());
								stockIn.setActind("Y");
								if(stockIn.getAdjnum() != 0){
									bookStockInList.add(stockIn);
								}
								
								BookInventory bi = new BookInventory();
								bi.setIsbn(s.getIsbn());
								bi.setStock(stockIn.getAdjnum());
								bi.setUpddate(new Date());
								bi.setUpduid(userId);
								bookInventoryList.add(bi);
							}
							
							boolean bookStockInFlag = true;
							if(!bookStockInList.isEmpty()){
								bookStockInFlag = bookStockInDAOImpl.updateBookStockInList(conn, bookStockInList);
							}
							
							boolean bookInventoryFlag = true;
							if(!bookInventoryList.isEmpty()){
								bookInventoryFlag = bookInventoryDAOImpl.updateBookInventory(conn, bookInventoryList);
							}
							boolean stocktakeFlag = stocktakeDAOImpl.insertStocktake(conn, detailList);
							
							if(bookStockInFlag && bookInventoryFlag && stocktakeFlag){
								conn.commit();
								request.setAttribute("msg", "盤點資料導入成功！");
							}else{
								conn.rollback();
								request.setAttribute("msg", "盤點資料導入失敗！");
							}
							request.setAttribute("type", "updateStock");
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}else{
							request.setAttribute("type", "updateStock");
							request.setAttribute("msg", "該導入文件沒有信息！");
							request.getRequestDispatcher("msg.jsp").forward(request, response);
						}
					}else{
						request.setAttribute("type", "updateStock");
						request.setAttribute("map", map);
						request.getRequestDispatcher("errExcel.jsp").forward(request, response);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (conn != null && !conn.isClosed())
				conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	private List getExcelDate(Connection conn, FileItem fi, HttpServletRequest request){
		List list = new ArrayList();
		Workbook workbook = null;
		Map map = new HashMap();
		BookDAOImpl bookDAOImpl = new BookDAOImpl();
		BookInventoryDAOImpl bookInventoryDAOImpl = new BookInventoryDAOImpl();
		try{
			HttpSession session = request.getSession();
			String userId = (String)session.getAttribute("userId");
			
			List isbnList = bookDAOImpl.getIsbn(conn);
			
			List<BookInventory> bookInventoryList = bookInventoryDAOImpl.getBookInventoryList(conn);
			
			workbook = Workbook.getWorkbook(fi.getInputStream());
			Sheet sheet = workbook.getSheet(0);
			List detailList = new ArrayList();
			Set isbnSet = new HashSet();
			for(int i=1;i<sheet.getRows();i++){
				Map err = new HashMap();
				Stocktake s = new Stocktake();
				String isbn = sheet.getCell(0, i).getContents().trim().replace("\\n\\r", "");
				if(isbn.equals("")){
					err.put(1, "第一列圖書isbn不能為空！");
				}else{
					if(isbn.length()>30){
						err.put(1, "第一列圖書isbn編號長度不能超過30位！");
					}else{
						if(!isbnSet.contains(isbn)){
							isbnSet.add(isbn);
							
							if(!isbnList.contains(isbn)){
								err.put(1, "第一列圖書isbn不存在系統內！");
							}else{
								boolean bookFlag = false;
								for(BookInventory bi : bookInventoryList){
									if(bi.getIsbn().equals(isbn)){
										bookFlag = true;
										s.setOriginal_stock(bi.getStock());
									}
								}
								if(bookFlag == false){
									s.setOriginal_stock(0);
								}
							}
						}else{
							err.put(1, "第一列圖書isbn編號出現重複值！");
						}
					}
				}
				
				s.setIsbn(isbn);
				
				String now_stock = sheet.getCell(1, i).getContents().trim().replace("\\n\\r", "");
				String real_stock = sheet.getCell(2, i).getContents().trim().replace("\\n\\r", "");
				int after_stocktake = 0;
				if(real_stock.equals("")){
					err.put(3, "第三列實際存量不能為空！");
				}else{
					if(!this.isNumeric(real_stock)){
						err.put(3, "第三列實際存量所填數值不正確！");
					}else{
						after_stocktake = Integer.parseInt(real_stock);
					}
				}
				s.setAfter_stocktake(after_stocktake);
				s.setAdjusted(s.getOriginal_stock() - s.getAfter_stocktake());
				s.setUpduid(userId);
				detailList.add(s);
				
				if(isbn.equals("") && now_stock.equals("") && real_stock.equals("")){
					break;
				}
				
				if(!err.isEmpty()){
					map.put(i+1, err);
				}
				
			}
			list.add(map);
			list.add(detailList);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(workbook!=null){
				workbook.close();
			}
		}
		return list;
	}
	
	private boolean isNumeric(String str){
		for (int i = str.length();--i>=0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
		    }
		}
		return true;
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
