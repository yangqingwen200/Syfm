package com.commons.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * @author Yang
 * @version v1.0
 * @date 2017/8/31
 */

/**
 * 用法:
 <mytag:if >
     <a class="btn btn-primary btn-xs" url="${ctx}/business/edit_onestop.html" urldata="{id: ${content.id}}" dialogwidth="40%" id="data_edit_opts_${i.index + 1}" >
     <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 编辑
     </a>
 <mytag:elseif />
     <a class="btn btn-primary btn-xs disabled" >
     <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 编辑
     </a>
 <mytag:else />
     <a class="btn btn-primary btn-xs disabled" >
     <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 编辑
     </a>
 </mytag:if>
 */
public class IfTag extends BodyTagSupport {

    private String body = null;  //用于存放成功条件后的内容
    private boolean subtagSucceeded; //判断if 或者 子 else if是否提交成功
    private String value;

    private boolean test;

    public IfTag() {
        super();
        init();
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBody() {
        if (body == null) {
            body = bodyContent.getString().trim();
        }
    }

    private String getBody() {
        if (body == null)
            return bodyContent.getString().trim();
        else
            return body;
    }

    public void succeeded() {
        subtagSucceeded = true;
    }

    public boolean isSucceeded() {
        return subtagSucceeded;
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspException {
        if(value.equals("1") || test) {
            this.succeeded();
        }
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            if (subtagSucceeded) {
                pageContext.getOut().write(getBody());
            }
        } catch (IOException e) {
            throw new JspException("IOError while writing the body: " + e.getMessage(), e);
        }
        init();
        return super.doEndTag();
    }

    private void init() {
        subtagSucceeded = false;
        body = null;
        value = null;
        test = false;
    }
}
