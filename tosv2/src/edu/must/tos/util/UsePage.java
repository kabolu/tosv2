package edu.must.tos.util;

import java.util.List;

public class UsePage {

	// 取页码 所有记录的总数 rsCount start 从多少行开始取 num每頁顯示取多少行？
	public static List getPage(int rsCount, String start, String num) {
		PageBean pb = new PageBean();
		List pagelist = pb.ShowPage(rsCount, Integer.parseInt(start), Integer.parseInt(num));
		return pagelist;
	}
}
