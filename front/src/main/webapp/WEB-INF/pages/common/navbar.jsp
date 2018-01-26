<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=utf-8" %>

<nav class="navbar navbar-default navbar-static-top navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" style="cursor: default">Welcome</a>
        </div>

        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <c:forEach items="${navBarList}" var="nb">
                    <li <c:if test="${nb[3] eq navBarIndex}">class="active"</c:if>><a href="${ctx}${nb[2]}">${nb[1]}</a></li>
                </c:forEach>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">更多 <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li ><a href="#">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
