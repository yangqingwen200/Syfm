<%@ page language="java" contentType="text/html; charset=utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <%@include file="/WEB-INF/pages/common/head.jsp"%>
</head>
<body>
<%@include file="/WEB-INF/pages/common/navbar.jsp"%>

<div class="jumbotron">
    <div class="container">
        <h1>Hello, world!</h1>
        <p>This is a template for a simple marketing or informational website. It includes a large callout called a jumbotron and three supporting pieces of content. Use it as a starting point to create something more unique.</p>
        <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn more &raquo;</a></p>
    </div>
</div>

<div class="container">

    <h1 class="page-header">布局
        <small> 使用Bootstrap网格系统布局网页</small>
    </h1>

    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nibh est, sagittis sit amet consectetur a, rhoncus dignissim ligula. Curabitur at neque eget quam accumsan vestibulum. Maecenas
        facilisis tellus quis nisl facilisis eget mollis lectus feugiat. Etiam pharetra mattis ultrices. In ac mi metus, ac pharetra ipsum. Aenean imperdiet sem purus. Suspendisse quis odio eu neque
        varius posuere. Fusce tincidunt tincidunt arcu non viverra. Vivamus dui eros, rhoncus cursus porta quis, sollicitudin a ante. Aliquam porta euismod sollicitudin.</p>

    <div class="row">
        <div class="col-lg-4">
            <h2 class="page-header">区块一</h2>
            <p>consectetur adipiscing elit,Lorem ipsum dolor sit amet. Nulla nibh est, sagittis sit amet consectetur a, rhoncus dignissim ligula. Curabitur at neque eget quam accumsan vestibulum. </p>
        </div>

        <div class="col-lg-4">
            <h2 class="page-header">区块二</h2>
            <p>Lorem ipsum dolor sit amet, quam accumsan vestibulum，consectetur adipiscing elit. Nulla nibh est, sagittis sit amet consectetur a, rhoncus dignissim ligula. Curabitur at neque eget
                . </p>
        </div>

        <div class="col-lg-4">
            <h2 class="page-header">区块三</h2>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nibh est, sagittis sit amet consectetur a, rhoncus dignissim ligula. Curabitur at neque eget quam accumsan
                vestibulum. </p>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/pages/common/scriptall.jsp"%>

</body>
</html>