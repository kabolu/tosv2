package edu.must.tos.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

public class ToolsOfNumber {

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isPrice(String price) {
		if (price == null || price.length() == 0) {
			return false;
		} else {
			try {
				double priceDouble = new DecimalFormat().parse(price).doubleValue();
				String in = String.valueOf(priceDouble);
				int j = 0;
				for (int i = 0; i < in.length(); i++) {
					if (in.charAt(i) < '0' || in.charAt(i) > '9') {
						if (in.charAt(i) != '.') {
							return false;
						} else {
							String str = in.substring(0, in.indexOf("."));
							if (str.length() >= 6) {
								return false;
							} else {
								j++;
								if (j > 1 || (j == in.length() - 1))
									return false;
							}
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
