package com.commons.util.core;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 和Web层相关的实用工具类
 *
 * @since 2008-09-22
 */
public class WebUtils {

    /**
     * 获取一个Session属性对象
     *
     * @param request
     * @return
     */
    public static Object getSessionAttribute(HttpServletRequest request,
                                             String sessionKey) {
        Object objSessionAttribute = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            objSessionAttribute = session.getAttribute(sessionKey);
        }
        return objSessionAttribute;
    }

    /**
     * 设置一个Session属性对象
     *
     * @param request
     * @return
     */
    public static void setSessionAttribute(HttpServletRequest request,
                                           String sessionKey, Object objSessionAttribute) {
        HttpSession session = request.getSession();
        if (session != null)
            session.setAttribute(sessionKey, objSessionAttribute);
    }

    /**
     * 移除Session对象属性值
     *
     * @param request
     * @return
     */
    public static void removeSessionAttribute(HttpServletRequest request,
                                              String sessionKey) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute(sessionKey);
        }
    }

    /**
     * 将请求参数封装为Dto
     *
     * @param request
     * @return
     */
    public static Dto getParamAsDto(HttpServletRequest request) {
        Dto dto = new BaseDto();
        Map map = request.getParameterMap();
        Iterator keyIterator = map.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = (String) keyIterator.next();
            String value = ((String[]) (map.get(key)))[0];
            dto.put(key, value);
        }
        return dto;
    }

    /**
     * 获取代码对照值
     *
     * @param request
     * @return
     */
    public static String getCodeDesc(String pField, String pCode,
                                     HttpServletRequest request) {
        List codeList = (List) request.getSession().getServletContext()
                .getAttribute("EACODELIST");
        String codedesc = null;
        for (int i = 0; i < codeList.size(); i++) {
            Dto codeDto = (BaseDto) codeList.get(i);
            if (pField.equalsIgnoreCase(codeDto.getAsString("field"))
                    && pCode.equalsIgnoreCase(codeDto.getAsString("codeid")))
                codedesc = codeDto.getAsString("codedesc");
        }
        return codedesc;
    }

    /**
     * 根据代码类别获取代码表列表
     *
     * @param request
     * @return
     */
    public static List getCodeListByField(String pField,
                                          HttpServletRequest request) {
        List codeList = (List) request.getSession().getServletContext()
                .getAttribute("EACODELIST");
        List lst = new ArrayList();
        for (int i = 0; i < codeList.size(); i++) {
            Dto codeDto = (BaseDto) codeList.get(i);
            if (codeDto.getAsString("field").equalsIgnoreCase(pField)) {
                lst.add(codeDto);
            }
        }
        return lst;
    }

    /**
     * 获取全局参数值
     *
     * @param pParamKey 参数键名
     * @return
     */
    public static String getParamValue(String pParamKey,
                                       HttpServletRequest request) {
        String paramValue = "";
        ServletContext context = request.getSession().getServletContext();
        if (BL3Utils.isEmpty(context)) {
            return "";
        }
        List paramList = (List) context.getAttribute("EAPARAMLIST");
        for (int i = 0; i < paramList.size(); i++) {
            Dto paramDto = (BaseDto) paramList.get(i);
            if (pParamKey.equals(paramDto.getAsString("paramkey"))) {
                paramValue = paramDto.getAsString("paramvalue");
            }
        }
        return paramValue;
    }

    /**
     * 判断是否为get请求
     * @param request
     * @return
     */
    public static Boolean isGetRequest(HttpServletRequest request) {
        String method = request.getMethod();
        return "GET".equals(method);
    }

    /**
     * 获取全局参数
     *
     * @return
     */
    public static List getParamList(HttpServletRequest request) {
        ServletContext context = request.getSession().getServletContext();
        if (BL3Utils.isEmpty(context)) {
            return new ArrayList();
        }
        return (List) context.getAttribute("EAPARAMLIST");
    }

    /**
     * 获取指定Cookie的值
     *
     * @param cookies      cookie集合
     * @param cookieName   cookie名字
     * @param defaultValue 缺省值
     * @return
     */
    public static String getCookieValue(Cookie[] cookies, String cookieName,
                                        String defaultValue) {
        if (cookies == null) {
            return defaultValue;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName()))
                return (cookie.getValue());
        }
        return defaultValue;
    }

    /**
     * 获得客户端ip
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

}
