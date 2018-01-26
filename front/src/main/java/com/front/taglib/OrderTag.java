package com.front.taglib;

import com.commons.taglib.CommonTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 排序标签类
 *
 * auth：Yang
 * 2016年4月9日 下午7:43:39
 */
public class OrderTag extends CommonTag {
	private static final long serialVersionUID = 1L;
	private String col; //需要排序的数据库列(或者真正执行sql字段别名)

	@Override
	public String bodyMessage(HttpSession session) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		StringBuffer sb = new StringBuffer("<span style=\"cursor: pointer\" id='span_order_" + col + "' column='" + col + "' ");
		String orderAttr = request.getParameter("page.orderAttr");  //当前JSP页面中排序的列
		String orderType = request.getParameter("page.orderType"); //当前列排序的类型: 正序 or 倒序
		if(null != orderAttr && null != orderType) {
			if(col.equals(orderAttr)) {
				if("desc".equals(orderType)) {
					sb.append("ordertype='asc')\">↓");
				} else {
					sb.append("ordertype='desc')\">↑");
				}
			} else {
				sb.append("ordertype='desc')\">↑↓");
			}
		} else {
			sb.append("ordertype='desc')\">↑↓");
		}
		sb.append("</span>");
		return sb.toString();
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}
}
