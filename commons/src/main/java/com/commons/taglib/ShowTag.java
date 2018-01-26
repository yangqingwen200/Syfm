package com.commons.taglib;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Map;

/**
 * 根据不同的权限, 显示不同的数据列表
 *
 * auth：Yang
 * 2016年4月9日 下午7:38:48
 */
public class ShowTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String value;

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspTagException {
		HttpSession session = pageContext.getSession();
		if(session != null) {
			Map<String, Object> permission_l = (Map<String, Object>) session.getAttribute("upp");
			if (null != permission_l && permission_l.containsValue(value)) {
				//return EVAL_BODY_INCLUDE; //测试需要, 移到外面去了
			}
			if(("1".equals(value))) {
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
