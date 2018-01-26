package com.front.taglib;

import com.front.constant.FrontConstant;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.List;

/**
 * 根据不同的权限, 是否显示按钮
 * <p>
 * auth：Yang
 * 2016年4月9日 下午7:38:48
 */
public class ButtonTag extends BodyTagSupport {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String value;
    private String content;

/*	@SuppressWarnings("unchecked")
    public int doStartTag() throws JspTagException {
		HttpSession session = pageContext.getSession();
		if(session != null) {
			List<String> permission = (List<String>) session.getAttribute(FrontConstant.PERMISSION_KEY);
			//String body = bodyContent.getString().trim();
			if (null != permission && permission.contains(value)) {
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}*/

    @Override
    public int doAfterBody() throws JspException {
        content = bodyContent.getString();
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        if (null == value) {
            value = content;
        }
        try {
            JspWriter out = pageContext.getOut();
            HttpSession session = pageContext.getSession();
            if(session != null) {
                List<String> permission = (List<String>) session.getAttribute(FrontConstant.PERMISSION_KEY);
                if (null == permission || !permission.contains(value)) {
                    content = "";
                }
            } else {
                content = "";
            }
            out.print(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
