package com.front.taglib;

import com.commons.util.Servlets;
import com.front.constant.FrontConstant;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * auth：Yang
 * 2016年4月9日 下午7:38:48
 */
public class CommonsFunction {

    public static String checkPermission(String value) throws Exception {
        return checkPermission(Servlets.getSession(), value, "y", "n");
    }
    public static String checkPermission(Map session, String value) throws Exception {
        return checkPermission(session, value, "y", "n");
    }

    public static String checkPermission(String value, String yes, String no) throws Exception {
        return checkPermission(Servlets.getSession(), value, yes, no);
    }

    public static String checkPermission(Object session, String value, String yes, String no) throws Exception {
        List<Object> permission = null;
        if(null != session) {
            if(session instanceof Map) {
                Map temp = (Map) session;
                permission = (List<Object>) temp.get(FrontConstant.PERMISSION_KEY);
            } else if(session instanceof List) {
                permission = (List<Object>) session;
            } else if(session instanceof HttpSession) {
                HttpSession temp = (HttpSession) session;
                permission = (List<Object>) temp.getAttribute(FrontConstant.PERMISSION_KEY);
            }

            if (null != permission && permission.contains(value)) {
                return yes;
            } else {
                return no;
            }
        }
        return "";
    }
}
