package edu.must.tos.receipt;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Major;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.Program;
import edu.must.tos.bean.StudReceipt;
import edu.must.tos.bean.Student;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.StudentDAOImpl;

public class StudReceiptInfo {

	public static List getStudReceiptList(Connection conn, String intake, String studParam, 
			int orderSeqNo, String paidStatus) throws Exception{
		List resultList = new ArrayList();
		StudentDAOImpl studentDAOImpl = new StudentDAOImpl();
		OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		
		try{
			List studentList = studentDAOImpl.showStudentDetail(conn, studParam);
			Student student = (Student)studentList.get(0);
			Program program = (Program)studentList.get(2);
			Major major = (Major)studentList.get(3);
			StudReceipt studReceipt = null;
			if(student != null){
				studReceipt = new StudReceipt();
				studReceipt.setStudentNo(student.getStudentNo());
				studReceipt.setChineseName(student.getChineseName());
				studReceipt.setEnglishName(student.getEnglishName());
				studReceipt.setProgramName(program.getChineseName());
				studReceipt.setMajorName(major.getChineseName()+" ("+student.getMajorCode()+")");
				if(orderSeqNo == 0){
					paidStatus = "R";
					List orderList = orderDAOImpl.getAllOrderSeqNoInfo(conn, studParam, intake, "Y");
					if(orderList!=null && !orderList.isEmpty()){
						for(int i=0; i<orderList.size(); i++){
							Order order = (Order)orderList.get(i);
							studReceipt.setAmercemount(order.getAmerceMount());
							studReceipt.setPaidCurrency(order.getPaidCurrency());
							studReceipt.setPaidStatus(order.getPaidStatus());
							studReceipt.setNetPaidCurrency(order.getNetpaidcurrency());
							studReceipt.setNetPaidAmount(order.getNetpaidamount());
							studReceipt.setCurRate(order.getCurrate());
							int seqNo = order.getOrderSeqNo();
							List<StudReceipt> receiptList = orderDetailDAOImpl.getStudReceiptTest(conn, seqNo, paidStatus);
							if(receiptList!=null && !receiptList.isEmpty()){
								for(StudReceipt s : receiptList){
									s.setChineseName(studReceipt.getChineseName());
									s.setEnglishName(studReceipt.getEnglishName());
									s.setProgramName(studReceipt.getProgramName());
									s.setMajorName(studReceipt.getMajorName());
									resultList.add(s);
								}
							}
						}
					}
				}else{
					List<StudReceipt> receiptList = orderDetailDAOImpl.getStudReceiptTest(conn, orderSeqNo, null);
					if(receiptList!=null && !receiptList.isEmpty()){
						for(StudReceipt s : receiptList){
							s.setChineseName(studReceipt.getChineseName());
							s.setEnglishName(studReceipt.getEnglishName());
							s.setProgramName(studReceipt.getProgramName());
							s.setMajorName(studReceipt.getMajorName());
							resultList.add(s);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultList;
	}
}
