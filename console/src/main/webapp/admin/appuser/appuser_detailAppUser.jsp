<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>  
<div id="p" style="padding:10px; font-size: 12px; font-family: 微软雅黑, 宋体">
	id: <label>${appuser.id }</label><br>
	昵称: <label>${appuser.name }</label><br>
	密码: <label>${appuser.pw }</label><br>
	手机号码: <label>${appuser.telephone }</label><br>
	状态: <label>
			<c:if test="${appuser.status eq 0 }">
				正常
			</c:if>
			<c:if test="${appuser.status eq 1 }">
				删除
			</c:if>
		</label><br>
	sessionId: <label>${appuser.sessionId }</label><br>
</div>
