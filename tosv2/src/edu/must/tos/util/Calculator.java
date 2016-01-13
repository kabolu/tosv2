package edu.must.tos.util;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Calculator {

	/**
	 * @param date1
	 *            需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12
	 * @param date2
	 *            被比较的时间 为空(null)则为当前时间
	 * @param stype
	 *            返回值类型 0为多少天，1为多少个月，2为多少年
	 * @return 举例： compareDate("2009-09-12", null, 0);//比较天
	 *         compareDate("2009-09-12", null, 1);//比较月
	 *         compareDate("2009-09-12", null, 2);//比较年
	 */
	public static int compareDate(String startDay, String endDay, int stype, List<String> vacationList) {
		int n = 0;
		String[] u = { "天", "月", "年" };
		String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

		endDay = endDay == null ? getCurrentDate("yyyy-MM-dd") : endDay;

		DateFormat df = new SimpleDateFormat(formatStyle);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(startDay));
			c2.setTime(df.parse(endDay));
		} catch (Exception e3) {
			System.out.println("wrong occured");
		}
		List list = new ArrayList();
		while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
			list.add(df.format(c1.getTime())); // 这里可以把间隔的日期存到数组中 打印出来
			
			n++;
			if (stype == 1) {
				c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
			} else if(stype == 0) {
				int week = c1.get(Calendar.DAY_OF_WEEK); //不計算週六和週日
                if(week == 7 || week ==1){
                    n--;
                } else {
                	String dateStr = df.format(c1.getTime());
    				if(vacationList.contains(dateStr)){
    					int index = vacationList.indexOf(dateStr);
    					vacationList.remove(index);
    					n--;
    				}
    				System.out.println(df.format(c1.getTime()));
                }                
                c1.add(Calendar.DATE, 1); // 比较天数，日期+1
			} else {
				c1.add(Calendar.DATE, 1);
			}
		}
		if(stype != 0)
            n = n - 1;
		if (stype == 2) {
			n = (int) n / 365;
		}
		System.out.println(startDay + " -- " + endDay + " 相差多少" + u[stype] + ":" + n);
		return n;
	}

	public static int compareYear(String startDay, String endDay) {
		int n = 0;
		endDay = endDay == null ? getCurrentDate("yyyy-MM-dd") : endDay;
		try{
			Integer sYear = Integer.parseInt(startDay.substring(0, 4));
			Integer sMonth = Integer.parseInt(startDay.substring(5, 7));
			Integer sDay = Integer.parseInt(startDay.substring(8, 10));
			Integer eYear = Integer.parseInt(endDay.substring(0, 4));
			Integer eMonth = Integer.parseInt(endDay.substring(5, 7));
			Integer eDay = Integer.parseInt(endDay.substring(8, 10));
			if(eYear > sYear){
				if(eMonth > sMonth){
					n = eYear - sYear;
				} else if(eMonth == sMonth){
					if(eDay >= sDay){
						n = eYear - sYear;
					} else {
						n = eYear - sYear - 1;
					}
				} else {
					n = eYear - sYear - 1;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return n;
	}
	
	public static int compareMonth(String startDay, String endDay) {
        int n = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //endDay = endDay == null ? ToolsOfDateTime.TimestampToString(now, "yyyy-MM-dd") : endDay;
        try{
            Integer sYear = Integer.parseInt(startDay.substring(0, 4));
            Integer sMonth = Integer.parseInt(startDay.substring(5, 7));
            Integer sDay = Integer.parseInt(startDay.substring(8, 10));
            Integer eYear = Integer.parseInt(endDay.substring(0, 4));
            Integer eMonth = Integer.parseInt(endDay.substring(5, 7));
            Integer eDay = Integer.parseInt(endDay.substring(8, 10));
            if(eYear > sYear){
            	if(eDay > sDay){
                	n = (eYear - sYear) * 12 + eMonth - sMonth;
                } else {
                	n = (eYear - sYear) * 12 + (eMonth - 1) - sMonth;
                }
            } else if(String.valueOf(eYear).equals(String.valueOf(sYear))){
            	if(eMonth > sMonth){
            		int days = getMonthLastDay(eYear, eMonth);
            		if(eDay > sDay){
            			if(eDay - sDay == 1){
            				n = 1;
            			} else {
            				if(eDay == days){
            					n = eMonth - sMonth + 1;
            				} else {
            					n = eMonth - sMonth;
            				}
            			}
            		} else if(eDay < sDay){
            			if(sDay - eDay == 1){
            				n = eMonth - sMonth;
            			} else {
            				n = eMonth - sMonth - 1;
            			}
            		} else {
            			n = eMonth - sMonth ;
            		}
            	} else if (String.valueOf(eMonth).equals(String.valueOf(sMonth))){
            		int days = getMonthLastDay(eYear, eMonth);
            		if(sDay == 1 && eDay == days){
            			n = 1;
            		}            		
            	}
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return n;
    }
	
	public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1); //把日期\u8BBE置\u4E3A\u5F53月第一天
        a.roll(Calendar.DATE, -1); //日期回\u6EDA一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
	
	public static String getCurrentDate(String format) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(day.getTime());
		return date;
	}

	public static void main(String[] args) {
		List list = Arrays.asList("2012-06-06", "2012-06-16", "2012-06-17");
		List arrayList = new ArrayList(list);
		System.out.println(compareDate("2013-08-01", "2013-09-30", 1, null));
		System.out.println(compareMonth("2013-07-25", "2013-09-24"));	
	}

}
