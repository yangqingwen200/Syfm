<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%
    String forwardUrl = request.getParameter("_forward");
    request.setAttribute("_forward", forwardUrl);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
</head>
<body style="text-align: center; margin-top: 20%; font-size: 14px; font-family: 微软雅黑;">
    <p><span id="time">1</span>s后跳入首页... <a href="${ctx}/login/login.html?_forward=${_forward}">立即进入!!!</a></p>
</body>
<script type="text/javascript">
    setTimeout(function () {
        window.location.href = "${ctx}/login/login.html?_forward=${_forward}";
    }, 1000);

    setInterval(function () {
        var time = document.getElementById("time").innerHTML;
        document.getElementById("time").innerHTML = (Number(time) - 1);
    }, 1000);
</script>
</html>
