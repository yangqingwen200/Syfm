<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>平台管理系统</title>
	<link rel="shortcut icon" href="${ctx}/image/delicious.png">
	<link rel="shortcut icon" href="${ctx}/image/delicious.png" type="image/vnd.microsoft.icon">
	<link rel="icon" href="${ctx}/image/delicious.png" type="image/vnd.microsoft.icon">
	<mytag:ordEasyuiJs dialog="true" form="true" rules="true" />
	<script type='text/javascript' src='${ctx}/js/util/md5.js'></script>
	<script type='text/javascript' src='${ctx}/js/util/map.js'></script>
	<script type="text/javascript">
        var map = new Map();
        var addOrEditUrl = "";
        var flagAddOrEditUrl = "${ctxajax}/mofidyPwdByUser.do";
        var loginjsp = "${ctx}/";

        $.extend($.fn.form.defaults, {
            onSubmit: function() {
                var isValid = $(this).form("validate");
                if(!isValid) {
                    return false;
                }
                var op = $("#oldPwd").textbox("getValue");
                $("#op").val(hex_md5(op));

                var np = $("#newPwd").textbox("getValue");
                $("#np").val(hex_md5(np));
            },
            onLoadError: function() {
                easyuishow("抱歉，操作失败。");
            }
        });

        function exitwin() {
            $.messager.confirm("确认框", "您确定要退出系统吗？", function(r){
                if (r){
                    var result = ajaxRequest("${ctx}/login/loginout.do");
                    if(result.message) {
                        window.location.href = loginjsp;
                    }
                }
            });
        }

        function help() {
            var result = ajaxRequest("${ctxajax}/getLoginUserInfo.do");
            if(result.message) {
                <%-- alert中不写"info 或者 warning", 弹出的确认框没有info或者waring图标, 可以当做普通的对话框 --%>
                $.messager.alert("个人信息","角色: " +　result.role + "<br>上次登录IP: " + result.ip + "<br>上次登录时间: " + result.lastLongiTime);
            }
        }

        function modifyPwd() {
            $("#add_edit_form").form("clear");
            $("#add_edit_dialog").dialog({
                title: "修改密码（下次登录生效）",
                width: 410,
                height: 180,
                buttons:[{
                    iconCls:"icon-ok",
                    text:"保存",
                    handler:function(){
                        var flag = $("#add_edit_form").form("validate");
                        if(flag) {
                            $.messager.progress({
                                text: '正在处理中...',
                                interval: 500
                            })
                            $("#add_edit_form").form("submit",{url: flagAddOrEditUrl});
                        }
                    }
                }
                ]
            });
            $("#add_edit_dialog").dialog("center");
            $("#add_edit_dialog").dialog("open");
        }

        $(function() {
            //初始化完成之后, 让accordion全部折叠起来
            $("#accordion").accordion("getSelected").panel("collapse");
        });

	</script>
</head>

<body>
	<!-- 指定fit="true",设定浏览器窗口大小变化的时候,div的大小也随之变化 -->
	<div id="cc" class="easyui-layout" data-options="fit: true">
		<div data-options="region:'north',split:false" style="height:65px;">
			<div class="welcome-button">
				<a id="helpbutton" class="easyui-linkbutton" data-options="iconCls:'icon-man',plain:true" onclick="help()">${session.user.name}</a>|
				<a id="modifyPwd" class="easyui-linkbutton" data-options="iconCls:'icon-key-go',plain:true" onclick="modifyPwd()">修改密码</a>|
				<a id="exitbutton" class="easyui-linkbutton" data-options="iconCls:'icon-exit',plain:true" onclick="exitwin()">注销</a>
			</div>
			<h2 style="padding-left: 10px">平台管理系统</h2>
		</div>

		<%-- 嵌套panel, data-options没有title属性, 让west布局没有折叠收起效果 --%>
		<div data-options="region:'west', split:true, border:false" title="菜单导航" style="width:200px;">
			<jsp:include page="admin/layout/west.jsp"></jsp:include>
		</div>
		<div data-options="region:'center'" style="padding:1px;background:#eee;overflow: hidden;">
			<jsp:include page="admin/layout/center.jsp"></jsp:include>
		</div>
		<div data-options="region:'south'" style="height:25px;text-align: center;line-height:20px;font-weight: bold;">
			感谢您的访问，建议使用<a href="https://www.baidu.com/s?wd=chrome" target="_blank">Chrome浏览器</a> 或者 <a href="https://www.baidu.com/s?wd=火狐浏览器" target="_blank">火狐浏览器</a>。
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog">
		<form id="add_edit_form" method="post">
			<table class="add-edit-form-table">
				<tr>
					<th>旧密码:</th>
					<td>
						<input type="hidden" name="oldPwd" id="op"/>
						<input id="oldPwd" class="easyui-textbox" data-options="validType:'middleLength[1,20]', required: true,missingMessage:'请输入旧密码', width:200, type:'password'"/>
					</td>
				</tr>
				<tr>
					<th>新密码:</th>
					<td>
						<input type="hidden" name="newPwd" id="np"/>
						<input id="newPwd" class="easyui-textbox" data-options="validType:'minLength[6]', required: true,missingMessage:'请输入新密码', width:200, type:'password'"/>
					</td>
				</tr>
				<tr>
					<th>确认密码:</th>
					<td><input id="confirePwd" class="easyui-textbox" data-options="validType:'checkEuqals[newPwd]', required: true,missingMessage:'请再次输入新密码', width:200, type:'password'"/></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>