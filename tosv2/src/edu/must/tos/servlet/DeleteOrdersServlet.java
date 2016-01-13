package edu.must.tos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.must.tos.bean.DelOrder;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Schedule2;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.Schedule2DAOImpl;

public class DeleteOrdersServlet extends HttpServlet {

	public DeleteOrdersServlet() {
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
			String orderIntake = (String)session.getAttribute("curIntake");
			String userId = (String)session.getAttribute("userId");
			//mark status 
			userId = "DELETEBY"+userId;
			List delOrderList = (List)session.getAttribute("delOrderList");
			String ids[] = request.getParameterValues("delbox");
			
			List confirmDelOrderList = new ArrayList();
			for(int i = 0 ; i < ids.length; i++) {	
				confirmDelOrderList.add(delOrderList.get(Integer.parseInt(ids[i])));
			}
			OrderDetailDAOImpl impl = new OrderDetailDAOImpl();
			boolean delDetail = impl.deleteOrderDetailList(conn, confirmDelOrderList, orderIntake, userId);
			
			OrderDAOImpl orderDaoImpl = new OrderDAOImpl();
			boolean delOrd = true;
			List schedule2List = new ArrayList();
			for (int i = 0; i < confirmDelOrderList.size(); i++) {
				DelOrder delOrder = (DelOrder) confirmDelOrderList.get(i);
				OrDetail od = new OrDetail();
				od.setOrderSeqNo(delOrder.getOrderSeqNo());
				od.setActInd("Y");
				
				List list = impl.getOrDetailInfo(conn, od);
				if(list == null || list.isEmpty()){
					schedule2List.add(delOrder.getOrderSeqNo());
					Order order = new Order();
					order.setActInd("N");
					order.setUpdDate(new Date());
					order.setUpdUid(userId);
					order.setOrderSeqNo(delOrder.getOrderSeqNo());
					order.setStudentNo(delOrder.getStudentNo());
					order.setOrderIntake(delOrder.getOrderIntake());
					int oprTemp = orderDaoImpl.getDelOrder(conn, order);
					if(oprTemp != 1)
						delOrd = false;
				}
				if(delOrd == false)
					break;
			}
			
			boolean delSchedule = true;
			Schedule2DAOImpl schedule2DAOImpl = new Schedule2DAOImpl();
			if(schedule2List != null && !schedule2List.isEmpty()){
				for(int i=0; i<schedule2List.size(); i++){
					int orderSeqNo = (Integer)schedule2List.get(i);
					Schedule2 sc = new Schedule2();
					sc.setActInd("N");
					sc.setUpdDate(new Date());
					sc.setUpdUid(userId);
					sc.setOrderSeqNo(orderSeqNo);
					List existList = schedule2DAOImpl.getSchedule2(conn, sc);
					if(existList != null && !existList.isEmpty()){
						delSchedule = schedule2DAOImpl.updSchedule2(conn, sc);
						if(delSchedule == false)
							break;
					}
				}
			}
			
			if(delDetail && delOrd && delSchedule) {
				conn.commit();
				request.setAttribute("oprMessage", "刪除成功!");
				request.getRequestDispatcher("deleteOrdersResult.jsp").forward(request, response);
			} else {
				conn.rollback();
				request.setAttribute("oprMessage", "刪除失敗!");
				request.getRequestDispatcher("deleteOrdersResult.jsp").forward(request, response);
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServletException (e);
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
