package com.console.taglib;

import com.commons.taglib.CommonTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 工具栏div显示控制标签类
 *
 * auth：Yang
 * 2016年4月9日 下午7:43:39
 */
public class ToolBarTag extends CommonTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;

	@Override
	public String bodyMessage(HttpSession session) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		StringBuffer sb = new StringBuffer("<div id=\"toolbar\">\n\t\t");  //style="text-align:-webkit-right;" 按钮右对齐
		sb.append("<table cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tr>");
		Map<String, List<Object[]>> eles = (Map<String, List<Object[]>>) session.getAttribute("eles"); // 得到权限对应的页面元素信息
		if(eles != null) {
			if (eles.containsKey(name)) {
				List<Object[]> list = eles.get(name);
				for (Object[] objects : list) {
					sb.append("<td><a id=\"" + objects[0] + "\" class=\"easyui-linkbutton\" data-options=\"iconCls:'" + objects[2] + "',plain:true\" onclick=\""
							+ objects[1].toString().replace("${ctxweb}", contextPath + "/web").replace("${ctxajax}", contextPath + "/ajax").replace("${ctxsts}", contextPath + "/sts")
							+ "\">" + objects[3] + "</a></td>");
					sb.append("<td><div class=\"datagrid-btn-separator\"></div></td>");
				}
			}
		}
		sb.append("</tr>");
		sb.append("</table>\n\t");
		sb.append("</div>\n");
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
