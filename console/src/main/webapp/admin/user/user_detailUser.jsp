<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs/>
	<script type="text/javascript">

		$(function() {
            $('#permission').tree({
                data: ${permission}
            });
            $('#menu').tree({
                data: ${menu}
            });
            $('#element').tree({
                data: ${element}
            });
            $('#role').tree({
                data: ${role}
            });
        });
	</script>
</head>

<body>
	<div id="p" style="padding:10px; font-size: 12px; font-family: 微软雅黑, 宋体">
		<table border="1">
			<tr>
				<th style="min-width: 200px">
					基本信息:
				</th>
				<th style="min-width: 200px">
					拥有权限:
				</th>
				<th style="min-width: 200px">
					拥有按钮:
				</th >
				<th style="min-width: 200px">
					拥有菜单:
				</th>
				<th style="min-width: 200px">
					拥有角色:
				</th>
			</tr>
			<tr>
				<td>
					登录名: ${detailUser.loginname }<br>
					姓名: ${detailUser.name }<br>
					性别: <c:if test="${detailUser.sex eq 0 }">女</c:if><c:if test="${detailUser.sex eq 1 }">男</c:if><br>
					手机号码: ${detailUser.telephone }<br>
					电子邮箱: ${detailUser.email }<br>
				</td>
				<td>
					<ul id="permission"></ul>
				</td>
				<td>
					<ul id="element"></ul>
				</td>
				<td>
					<ul id="menu"></ul>
				</td>
				<td>
					<ul id="role"></ul>
				</td>
			</tr>
		</table>

	</div>
</body>
</html>
