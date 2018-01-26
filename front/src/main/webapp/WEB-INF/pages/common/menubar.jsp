<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.front.constant.FrontConstant" %>
<%@ page language="java" contentType="text/html; charset=utf-8" %>

<ul class="nav nav-tabs">
    <%-- 得到navBarIndex的值, 找到所有的child, 然后进行循环, 登录时候把对应关系从数据库中取出, 放在session中 --%>
    <%
        String navBar = String.valueOf(request.getAttribute("navBarIndex"));
        Map<String, List<Object[]>> menuList = (Map) session.getAttribute(FrontConstant.MENU_KEY);
        if(null != menuList) {
            List<Object[]> currentMenuList = menuList.get(navBar);
            request.setAttribute("currentMenuList", currentMenuList);
        }
    %>
    <c:forEach items="${currentMenuList}" var="list">
        <li role="presentation" <c:if test="${list[2] eq menuIndex}">class="active"</c:if>><a href="${ctx}${list[1]}">${list[0]}</a></li>
    </c:forEach>

</ul>
