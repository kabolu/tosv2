package edu.must.tos.util;

public class ToolsOfString {

	public static String NulltoEmpty(String str) {
		if (str == null) {
			str = "&nbsp;";
		}
		return str;
	}
	
	//2011-12-09 modified by jblu：若ISBN最後一位為'A'，截取剩餘前面部份；
	public static String getUniqueIsbn(String isbn) {
		if(isbn != null && !"".equals(isbn)){
			if(isbn.toUpperCase().endsWith("A")){
				isbn = isbn.substring(0, isbn.length()-1);
			}
		}
		return isbn;
	}
	
	public static boolean isNull(String src){
		if (src == null || src.equals("") || src.trim().length() == 0)
			return true;
		return false;
	}
	
	public static String join(String s1, String s2, String joiner) {
		if(!isNull(s1) && !isNull(s2))
			return chkNullString(s1) + chkNullString(joiner) + chkNullString(s2);
		else
			return chkNullString(s1) + chkNullString(s2);
	}
	
	public static String chkNullString(String src){
		if (isNull(src))return "";
		return src.trim();
	}
}
