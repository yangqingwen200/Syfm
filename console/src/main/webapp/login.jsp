<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv=Content-Type content="text/html; charset=utf-8">
	<title>平台管理系统</title>
	<link rel="shortcut icon" href="${ctx}/image/delicious.png">
	<link rel="shortcut icon" href="${ctx}/image/delicious.png" type="image/vnd.microsoft.icon">
	<link rel="icon" href="${ctx}/image/delicious.png" type="image/vnd.microsoft.icon">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.4.4/themes/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-easyui-1.4.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/css/common.css" />
	<script type='text/javascript' src='${ctx}/js/util/md5.js'></script>
	<script type="text/javascript" src="${ctx}/js/jquery-easyui-1.4.4/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js"></script>
	<script type='text/javascript' src='${ctx}/js/login.js'></script>

</head>
<body style="background-color: #f3f3f4" >
	 <div style="text-align:center;vertical-align: middle; margin-top: 10%">
		 <div>
			 <h2>平台管理系统</h2>
		 </div>
		 <div style="background-color: white">
			 <form id="user_login_loginForm" method="post" action="${ctx}/login/login.do">
				 <input type="hidden" name="pw" id="pw">
				 <table cellspacing="8" style="margin-left: auto; margin-right: auto; ">
					 <tr>
						 <td>
							 <input id="loginname" name="name" class="easyui-textbox" data-options="iconCls:'icon-man',required:true,missingMessage:'请输入登录名',type:'text', width:272, height:40, length:20, value:'admin'" >
						 </td>
					 </tr>
					 <tr>
						 <td>
							 <input id="pw1" class="easyui-textbox" data-options="iconCls:'icon-lock',required:true,missingMessage:'请输入密码',type:'password',width:272, height:40, value:'123456'">
						 </td>
					 </tr>
					 <tr>
						 <td>
							 <input id="btnLogin" type="submit" class="easyui-linkbutton index-form-botton" value="登录" />
						 </td>
					 </tr>
					 <tr>
						 <td>
							 <input id="reset" type="button" class="easyui-linkbutton index-form-botton" value="重填" />
						 </td>
					 </tr>
				 </table>
			 </form>
		 </div>
		 <div>
			 <h4>2017 © Yang.</h4>
		 </div>
	 </div>
</body>
</html>
