package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.Book;
import edu.must.tos.bean.BookRel;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Price;
import edu.must.tos.impl.BookDAOImpl;
import edu.must.tos.impl.BookRelDAOImpl;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.PriceDAOImpl;

public class DeleteBookServlet extends HttpServlet {

	public DeleteBookServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;    
		try{
			//使用連接池獲取連接
			DataSource ds = (DataSource)getServletConfig().getServletContext().getAttribute("dbpool");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
		
			HttpSession session = request.getSession();
	    	String userId = (String)session.getAttribute("userId");
			String intake = (String)session.getAttribute("curIntake");
			
			
			String isbn = request.getParameter("isbn");
			OrderDetailDAOImpl ordetailImpl = new OrderDetailDAOImpl();
			OrderDAOImpl orderImpl = new OrderDAOImpl();
			BookRelDAOImpl bookrelImpl = new BookRelDAOImpl();
			PriceDAOImpl priceImpl = new PriceDAOImpl();
			BookDAOImpl bookImpl = new BookDAOImpl();
			Date now = new Date();
			//search isbn in order detail
			boolean flag = ordetailImpl.getIsbnInOrDetail(conn, isbn);
			
			if(flag){
				boolean result = false;
				List odList = ordetailImpl.getOrDetail(conn, intake, isbn, "Y");
				
				for(int i=0; i<odList.size(); i++){
					OrDetail odt = (OrDetail)odList.get(i);
					odt.setActInd("N");
					odt.setUpdUid(userId);
					odt.setUpdDate(now);
					
					Order od = new Order();
					od.setStudentNo(odt.getStudentNo());
					od.setOrderIntake(odt.getOrderIntake());
					od.setActInd("N");
					od.setUpdUid(userId);
					od.setUpdDate(now);
					
					BookRel br = new BookRel();
					br.setIsbn(isbn);
					br.setActInd("N");
					br.setUpdUid(userId);
					br.setUpdDate(now);
					br.setIntake(intake);
					
					Price p = new Price();
					p.setIsbn(isbn);
					p.setUpdUid(userId);
					p.setUpdDate(now);
					p.setActInd("N");
					p.setIntake(intake);
					
					Book book = new Book();
					book.setIsbn(isbn);
					book.setUpdUid(userId);
					book.setUpdDate(now);
					book.setActInd("N");
					
					int num = ordetailImpl.getCountOrDetail(conn, odt);
					boolean delBookFlag = false;
					if(num == 1){
						int ordetail = 0, order = 0, bookrel = 0, price = 0;
						
						ordetail = ordetailImpl.getDelOrdetail(conn, odt);
						order = orderImpl.getDelOrder(conn, od);
						bookrel = bookrelImpl.deleteBookRel(conn, br);
						price = priceImpl.getDelPrice(conn, p);
						delBookFlag = bookImpl.delBook(conn, book);
						if(ordetail>0 && order>0 && bookrel>0 && price>0 && delBookFlag){
							result = true;
						}else{
							result = false;
							break;
						}
					}else{
						int ordetail = 0, bookrel = 0, price = 0;
						ordetail = ordetailImpl.getDelOrdetail(conn, odt);
						bookrel = bookrelImpl.deleteBookRel(conn, br);
						price = priceImpl.getDelPrice(conn, p);
						delBookFlag = bookImpl.delBook(conn, book);
						if(ordetail>0 && bookrel>0 && price>0 && delBookFlag){
							result = true;
						}else{
							result = false;
							break;
						}
					}
				}
				if(result){
					conn.commit();
					request.setAttribute("msg", "Delete success!");
					request.setAttribute("type", "delBook");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}else{
					conn.rollback();
					request.setAttribute("msg", "Delete failed!");
					request.setAttribute("type", "delBook");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}
			}else{
				BookRel br = new BookRel();
				br.setIsbn(isbn);
				br.setActInd("N");
				br.setUpdUid(userId);
				br.setUpdDate(now);
				br.setIntake(intake);
				
				Price p = new Price();
				p.setIsbn(isbn);
				p.setUpdUid(userId);
				p.setUpdDate(now);
				p.setActInd("N");
				p.setIntake(intake);
				
				Book b = new Book();
				b.setIsbn(isbn);
				b.setUpdUid(userId);
				b.setUpdDate(now);
				b.setActInd("N");
				
				int bookrel = 0, price = 0;
				bookrel = bookrelImpl.deleteBookRel(conn,br);
				price = priceImpl.getDelPrice(conn,p);
				boolean delBookFlag = bookImpl.delBook(conn,b);
				if(bookrel>0 && price>0 && delBookFlag){
					conn.commit();
					request.setAttribute("msg", "Delete success!");
					request.setAttribute("type", "delBook");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}else{
					conn.rollback();
					request.setAttribute("msg", "Delete failed!");
					request.setAttribute("type", "delBook");
					request.getRequestDispatcher("msg.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
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
