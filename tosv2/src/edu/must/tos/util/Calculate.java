package edu.must.tos.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import edu.must.tos.bean.Difference;
import edu.must.tos.bean.OrDetail;
import edu.must.tos.bean.Order;
import edu.must.tos.bean.StudReceipt;
import edu.must.tos.bean.SysConfig;
import edu.must.tos.impl.OrderDAOImpl;
import edu.must.tos.impl.OrderDetailDAOImpl;
import edu.must.tos.impl.SysConfigDAOImpl;
import edu.must.tos.receipt.StudReceiptInfo;

public class Calculate {

	public static List getDifferAmount(Connection conn, String studValue,
			String orderIntake, double paidAmount) {
		List differAmountList = new ArrayList();
		OrderDAOImpl orderDAOImpl = new OrderDAOImpl();
		OrderDetailDAOImpl orderDetailDAOImpl = new OrderDetailDAOImpl();
		SysConfigDAOImpl sysconfigImpl = new SysConfigDAOImpl();
		try {
			List<Order> inactiveOrderList = orderDAOImpl.getAllOrderSeqNoInfo(conn, studValue, orderIntake, "N");
			for(Order o : inactiveOrderList){
				double amount = o.getPaidAmount();
				String currency = o.getPaidCurrency();
				if(o.getPaidCurrency() != null){
					if(o.getPaidStatus().equals("Y")){
						OrDetail od = new OrDetail();
						od.setActInd("N");
						od.setOrderSeqNo(o.getOrderSeqNo());
						List onlyList = new ArrayList();
						List<OrDetail> inactiveOrdetailList = orderDetailDAOImpl.getOrDetailInfo(conn, od);
						if(inactiveOrdetailList != null){
							for(OrDetail ordetail : inactiveOrdetailList){
								if(!"STUDENT".equals(ordetail.getUpdUid()))
									onlyList.add(ordetail);
							}
						}
						if(onlyList.size() == 1){
							od = (OrDetail)inactiveOrdetailList.get(0);
							Difference value = new Difference();
							value.setPaidStatus(o.getPaidStatus());
							value.setOrderSeqNo(o.getOrderSeqNo());
							value.setPaidAmount(amount); // 已付金額總數
							value.setPaidcurrency(currency);
							value.setAmercemount(0); // 滯留金
							value.setFineforlatepay(0); // 逾期付款罰款金額
							BigDecimal differ = new BigDecimal(Double.toString(value.getShoulePayAmount())).subtract(new BigDecimal(Double.toString(value.getPaidAmount())));
							value.setDifference(differ.doubleValue());
							value.setDifferenceMop(-o.getNetpaidamount());
							List isbnList = new ArrayList();
							isbnList.add(od.getIsbn());
							value.setIsbnList(isbnList);
							List bookPriceList = new ArrayList();
							bookPriceList.add(od.getPaidAmount());
							value.setBookPriceList(bookPriceList);
							value.setStudentNo(o.getStudentNo());
							differAmountList.add(value);
						}
					}
				}				
			}
			
			List<Order> orderList = orderDAOImpl.getAllOrderSeqNoInfo(conn, studValue, orderIntake, "Y");
			for (Order o : orderList) {
				int orderSeqNo = o.getOrderSeqNo();
				//實際所收取的價錢
				double amount = o.getPaidAmount();
				//葡紙價錢
				double mopAmount = o.getNetpaidamount();
				String currency = o.getPaidCurrency();
				//記錄的匯率
				Double rate = o.getCurrate(); 
				List isbnList = new ArrayList();
				List bookPriceList = new ArrayList();
				
				if (!o.getPaidStatus().equals("N") || (o.getPaidStatus().equals("N") && o.getPaidCurrency() != null)) {
					Difference value = new Difference();
					value.setPaidStatus(o.getPaidStatus());
					if (o.getPaidStatus().equals("N") && o.getPaidCurrency() != null) {
						amount = paidAmount;
					}
					double latePay = o.getFineforlatepay();
					double amerceMount = o.getAmerceMount();
					// 獲取原補訂書費百分比
					double shippingFee = o.getShippingFee();
					String studentNo = o.getStudentNo();
					
					//List<StudReceipt> receiptList = orderDetailImpl.getStudReceipt(conn, studentNo + "," + studentNo, orderIntake, orderSeqNo);
					List<StudReceipt> receiptList = StudReceiptInfo.getStudReceiptList(conn, orderIntake, studValue, orderSeqNo, null);
					
					SysConfig config = new SysConfig();
					config.setScType("FINERATE");
					config.setScKey("SHIPPING");
					config.setActInd("Y");
					SysConfig withdrawForCarry = sysconfigImpl.getSysConfig(conn, config);
					double feePercent = 0;
					if (withdrawForCarry != null && withdrawForCarry.getScValue1() != null) {
						feePercent = Double.parseDouble(withdrawForCarry.getScValue1());
					}

					double sumMop = 0, sumRmb = 0;
					double bookMopPrice = 0, bookRmbPrice = 0;
					double bookMopWithPrice = 0, bookRmbWithPrice = 0;
					
					for (StudReceipt receipt : receiptList) {
						double mopPrice = 0, rmbPrice = 0;
						if (receipt.getMopNetPrice() == 0) {
							mopPrice = receipt.getMopFuturePrice();
						} else {
							mopPrice = receipt.getMopNetPrice();
						}

						if (receipt.getConfirmQty() > 0) {
							isbnList.add(receipt.getIsbn());
							bookPriceList.add(mopPrice);
						}
						// 退書費
						BigDecimal withdrawFee = new BigDecimal(Integer.toString(receipt.getWithDrawQty())).multiply(new BigDecimal(Double.toString(receipt.getMopWithPrice())));
						// 退運費
						BigDecimal withdrawCarryFeeMop = new BigDecimal(Integer.toString(receipt.getWithDrawQty2())).multiply(new BigDecimal(Double.toString(mopPrice))).multiply(new BigDecimal(Double.toString(feePercent))).multiply(new BigDecimal(Double.toString(0.01)));
						if (0 < withdrawCarryFeeMop.doubleValue() && withdrawCarryFeeMop.doubleValue() < 1) {
							withdrawCarryFeeMop = new BigDecimal(1);
						} else {
							withdrawCarryFeeMop = withdrawCarryFeeMop.setScale(0, BigDecimal.ROUND_HALF_UP);
						}
						// 分別計算各個幣種的總數
						sumMop = ((new BigDecimal(Double.toString(mopPrice)).multiply(new BigDecimal(Integer.toString(receipt.getConfirmQty())))).add(withdrawFee).add(withdrawCarryFeeMop)).add(new BigDecimal(Double.toString(sumMop))).doubleValue();
						
						bookMopPrice = (new BigDecimal(Double.toString(mopPrice)).multiply(new BigDecimal(Integer.toString(receipt.getConfirmQty())))).add(new BigDecimal(Double.toString(bookMopPrice))).doubleValue();
						bookMopWithPrice = (new BigDecimal(Double.toString(mopPrice)).multiply(new BigDecimal(Integer.toString(receipt.getWithDrawQty())))).add(new BigDecimal(Double.toString(bookMopWithPrice))).doubleValue();
					}
					
					// 補訂運費計算=(確定數 + 退書數) * 書價
					bookMopPrice = (new BigDecimal(Double.toString(bookMopPrice)).add(new BigDecimal(Double.toString(bookMopWithPrice)))).multiply(new BigDecimal(Double.toString(shippingFee))).doubleValue();
					if (0 < bookMopPrice && bookMopPrice < 1) {
						bookMopPrice = 1;
					} else {
						bookMopPrice = new BigDecimal(Double.toString(bookMopPrice)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					
					value.setOrderSeqNo(orderSeqNo);
					value.setPaidAmount(amount); // 已付金額總數
					value.setPaidcurrency(currency);
					
					double finalMopAmount = new BigDecimal(sumMop).add(new BigDecimal(latePay)).add(new BigDecimal(amerceMount)).add(new BigDecimal(bookMopPrice)).doubleValue();
					value.setShouldPayMopAmount(finalMopAmount);
					double finalAmount = new BigDecimal(finalMopAmount).multiply(new BigDecimal(rate)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
					value.setShoulePayAmount(finalAmount); // 需實收金額總數
					
					value.setAmercemount(0); // 滯留金
					value.setFineforlatepay(0); // 逾期付款罰款金額
					BigDecimal differ = new BigDecimal(Double.toString(value.getShoulePayAmount())).subtract(new BigDecimal(Double.toString(value.getPaidAmount())));
					BigDecimal differMop = new BigDecimal(value.getShouldPayMopAmount()).subtract(new BigDecimal(mopAmount));
					value.setDifference(differ.doubleValue());
					value.setDifferenceMop(differMop.doubleValue());
					value.setIsbnList(isbnList);
					value.setBookPriceList(bookPriceList);
					value.setStudentNo(studentNo);
					differAmountList.add(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			differAmountList = null;
		}
		return differAmountList;
	}
}
