package edu.must.tos.util;

public class FilterStr {

	public String toChinese(String str) {
		try {
			if (str == null) {
				str = "";
			} else {
				str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
				str = str.trim();
			}
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

	public String filterStr(String str) {
		str = str.replace("'", "");
		str = str.replace(",", " ");
		return str;
	}

	public String s(String str) {
		String[] data = null;
		data = str.split(",");
		String d = "";
		for (int i = 0; i < data.length; i++) {

			if (!data[i].equals("") || data[i].length() != 0) {
				d = d + data[i] + ",";
			}
		}
		return d;
	}

	/*
	 * public static void main(String args[]){ FilterStr str=new FilterStr();
	 * String aa=str.s(",cob201,cob302,,cob301,,cob402,,,"); String[]
	 * c=aa.split(","); System.out.println(c.length); System.out.print(aa); }
	 */
}
