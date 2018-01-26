package com.commons.taglib;

import com.commons.context.AppContext;
import com.commons.service.GenericService;
import com.commons.util.core.BL3Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public abstract class CommonTag extends BodyTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CommonTag.class);
	protected static GenericService publicService = null;
	
	@Override
	public int doStartTag() throws JspException {
		if(publicService == null) {
			publicService = AppContext.getBean("publicService", GenericService.class);
		}
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspTagException {

		HttpSession session = pageContext.getSession();
		if(session == null) {
			return SKIP_PAGE;
		}

		JspWriter out = pageContext.getOut();
		String result = "";
		try {
			result = this.bodyMessage(session);

		} catch (Exception e) {
			LOG.error(BL3Utils.getErrorMessage(2), e);
			result = "<script type=\"text/javascript\" charset=\"utf-8\">alert(\"" + this.getClass().getSimpleName() + " 类自定义标签初始化错误, 请检查!\");</script>";
		} finally {
			try {
				out.print(result);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/* doEndTag有两个返回值
		   SKIP_PAGE : 不再显示后面的页面部分。
		   EVAL_PAGE : 显示后面的page部分。
		 */
		return EVAL_PAGE;
	}

	public abstract String bodyMessage(HttpSession session);
}
