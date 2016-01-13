package edu.must.tos.util;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.must.tos.bean.BookSupplier;
import edu.must.tos.impl.BookPurchasingDAOImpl;
import edu.must.tos.impl.BookSupplierDAOImpl;

public class AutoPurchaseNumber {

//	public static void main(String[] args) {
//		String temp = "U11_1";
//		String str1 = temp.substring(0, temp.indexOf("_"));
//		String str2 = temp.substring(temp.indexOf("_")+1, temp.length());
//		System.out.println(str1);
//		System.out.println(str2);
//		
//		String yearStr = "16/09/2011";
//		System.out.println(yearStr.substring(yearStr.length()-2, yearStr.length()));
//		
//		
//		//System.out.printf具体的使用方法有： 
//		float d = (float) 10.2;
//		System.out.printf("%f", d);      //"f"表示格式化输出浮点数。 
//		System.out.println();
//		System.out.printf("%9.2f", d);   //"9.2"中的9表示输出的长度，2表示小数点后的位数。
//		System.out.println();
//		System.out.printf("%+9.2f", d);  //"+"表示输出的数带正负号。
//		System.out.println();
//		System.out.printf("%-9.4f", d);  //"-"表示输出的数左对齐（默认为右对齐）。
//		System.out.println();
//		System.out.printf("%+-9.3f", d); //"+-"表示输出的数带正负号且左对齐。
//		System.out.println();
//		int i = 3;
//		System.out.printf("%d", i);      //"d"表示输出十进制整数。
//		System.out.println();
//		System.out.printf("%6d", i);     //表示输入十进制整数，并且至少补齐6位。
//		System.out.println();
//		System.out.printf("%o", i);      //"o"表示输出八进制整数。
//		System.out.println();
//		System.out.printf("%x", i);      //"x"表示输出十六进制整数。
//		System.out.println();
//		System.out.printf("%#x", i);     //"x"表示输出带有十六进制标志的整数。
//		System.out.println();
//		String s = "ABC";
//		System.out.printf("%s", s);      //"s"表示输出字符串。 
//		System.out.println();
//		
//		System.out.println(String.format("%03d",2));
//	}
	
	public static String getPurchaseNumber(Connection conn, int supplierNo, String intake){
		String result = null;
		try{
			BookPurchasingDAOImpl bookPurchasingDAOImpl = new BookPurchasingDAOImpl();
			BookSupplierDAOImpl bookSupplierDAOImpl = new BookSupplierDAOImpl();
			if(supplierNo!=0){
				BookSupplier bookSupplier = bookSupplierDAOImpl.getBookSupplierByNo(conn, supplierNo);
				if(bookSupplier!=null){
					result = "PO"+bookSupplier.getSupplierCode() + intake;
				}
			}else{
				result = "POOTHER" + intake;
			}
			
			List tempList = bookPurchasingDAOImpl.getOrderNoList(conn, result);
			if(tempList!=null && !tempList.isEmpty()){
				String temp = (String)tempList.get(0);
				int seq = Integer.parseInt(temp.substring(temp.length()-3, temp.length()));
				seq++;
				if(String.valueOf(seq).length() == 1)
					result += "00"+String.valueOf(seq);
				else if(String.valueOf(seq).length() == 2)
					result += "0"+String.valueOf(seq);
				else
					result += String.valueOf(seq);
			}else{
				result = result + "001";
			}
		}catch(Exception e){
			e.printStackTrace();
			result = null;
		}
		return result;
	}
}
