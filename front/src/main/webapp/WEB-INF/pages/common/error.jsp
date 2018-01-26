<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <%@include file="/WEB-INF/pages/common/head.jsp"%>
</head>
<body>
<%@include file="/WEB-INF/pages/common/navbar.jsp"%>

<div class="jumbotron">
    <div class="container">
        <h1>404 Error! Page is Missing...</h1>
        <p>This is a template for a simple marketing or informational website. It includes a large callout called a jumbotron and three supporting pieces of content. Use it as a starting point to create something more unique.</p>
        <p><a class="btn btn-primary btn-lg" href="#" role="button">Connect Me &raquo;</a></p>
    </div>
</div>

<div class="container">

    <h1 class="page-header">T~T...
        <small> 发生点小事情........</small>
    </h1>

    <p>
        <%--这个错误信息是在aciton方法中, 使用addActionError(xx)--%>
        <s:actionerror/>
        <%--这个错误信息是拦截器中, 使用request.setAttribute("errorMessage", xxx);--%>
        ${errorMessage}
    </p>

</div>

<%@include file="/WEB-INF/pages/common/scriptall.jsp"%>

</body>
</html>