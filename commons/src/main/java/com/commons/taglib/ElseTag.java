package com.commons.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Yang
 * @version v1.0
 * @date 2017/8/31
 */
public class ElseTag extends BodyTagSupport {

    public void release() {
        super.release();
    }

    public int doStartTag() throws JspException {
        Tag parent = getParent();

        if (parent == null || !(parent instanceof IfTag)) {
            throw new JspTagException("else tag must inside if tag");
        }

        IfTag ifTag = (IfTag) parent;
        if (ifTag.isSucceeded()) {
            // 已经有执行成功的条件，保存之前的html
            ifTag.setBody();
        } else {
            // 之前没有的判断没有成功条件,则清除之前的输出
            ifTag.getBodyContent().clearBody();
            ifTag.succeeded();
        }

        return EVAL_BODY_BUFFERED;
    }

}