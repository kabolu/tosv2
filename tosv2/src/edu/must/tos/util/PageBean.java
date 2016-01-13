package edu.must.tos.util;

import java.util.ArrayList;
import java.util.List;

public class PageBean {

	// 接收的数据
	String page; // 显示的页数
	// 当页的类型 如果是第一页 则前边不加上一页 prev 如果是正常页 pages 如果是当前页 cur 如果有下一页 next
	String pageType;
	String offset; // 第多少條記錄

	public PageBean() {}

	public PageBean(String page, String pageType, String offset) {
		super();
		this.page = page;
		this.pageType = pageType;
		this.offset = offset;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	// 显示分页 url=查找条件后的分页URL rsCount=记录数 offset=第几条记录 maxshow=显示多少条记录
	// maxpages=显示多少页
	public List ShowPage(int rsCount, int offset, int maxshow) {
		// 初始化页数
		List<PageBean> pl = new ArrayList<PageBean>();
		PageBean pb;

		// preoffset=上一页，从第几条记录开始取
		// pages=总页数
		// startp开始页
		// endp结束页
		int preoffset, pages, curp, startp, endp, newoffset, maxpages;
		maxpages = 9;
		// 如果少于第0条，则第一条为0 即是 limit 0 , 10 中的 0
		if (offset < 0) {
			offset = 0;
		}

		// 如果当前不是第一页，则在页数前加 上一页
		if (offset > 0) {
			preoffset = offset - maxshow;
			pb = new PageBean("上一頁", "prev", String.valueOf(preoffset));
			pl.add(pb);
		}

		// 总页数
		float page = ((float) rsCount / (float) maxshow);
		pages = (int) Math.ceil(page);

		// 如果总页数超过最大显示页数
		if (pages > maxpages) {
			curp = offset / maxshow;
			startp = curp - (maxpages - 2);
			if (startp < 1) {
				startp = 1;
			}
			endp = startp + maxpages;
			if (endp > pages) {
				endp = pages;
			}
		} else {
			startp = 1;
			endp = pages;
		}

		// 中間顯示頁碼
		for (int i = startp; i <= endp; i++) {
			newoffset = maxshow * (i - 1);
			if (newoffset == offset) {
				pb = new PageBean(String.valueOf(i), "cur", String.valueOf(newoffset));
				pl.add(pb);
			} else {
				pb = new PageBean(String.valueOf(i), "pages", String.valueOf(newoffset));
				pl.add(pb);
			}
		}
		// 顯示 "下一頁" 按鈕
		// if(offset<(pages*maxpages)-5)
		// if(endp<pages)
		if (offset / maxshow < (pages - 1)) {
			newoffset = offset + maxshow;
			pb = new PageBean(String.valueOf(newoffset), "next", String.valueOf(newoffset));
			pl.add(pb);
		}
		return pl;
	}
}