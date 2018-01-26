<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        var flagUrl = "";

        $(function(){
            //分配/收回权限
            $("#role_rolePermission_addPermissionDialog").dialog({
                buttons:[{
                    iconCls:"icon-save",
                    text:"保存",
                    handler:function(){
                        var revoke = $("#viewPermissionRoleCombotrees").combotree("getValues");
                        var add = $("#viewPermissionRoleCombotree").combotree("getValues");
                        $("#role_rolePermission_editPermissionForm").form("submit",{url: "${ctxweb}/addOrRevokePMEByRole_role.do?add=" + revoke + "&remove=" + add + flagUrl});
                    }
                }]
            });

            $("#role_rolePermission_editPermissionForm").form({
                success : function(data) {
                    operFormSuccessOrFailShow(data, $("#role_rolePermission_addPermissionDialog"));
                }
            });

        });

        function distPermissionByRole(url) {
            $("#role_rolePermission_addPermissionDialog").dialog("setTitle", "分配/收回权限");
            $("#overLabel").html("收回已分配的权限:");
            $("#readyLabel").html("授权未分配的权限:");
            flagUrl = "&flag=permission";
            oper(url);
        }

        function distMenuByRole(url) {
            $("#role_rolePermission_addPermissionDialog").dialog("setTitle", "分配/收回菜单");
            $("#overLabel").html("收回已分配的菜单:");
            $("#readyLabel").html("授权未分配的菜单:");
            flagUrl = "&flag=menu";
            oper(url);
        }

        function distElementByRole(url) {
            $("#role_rolePermission_addPermissionDialog").dialog("setTitle", "分配/收回按钮");
            $("#overLabel").html("收回已分配的按钮:");
            $("#readyLabel").html("授权未分配的按钮:");
            flagUrl = "&flag=element";
            oper(url);
        }

        function oper(url) {
            if(datagridChecked($(".easyui-datagrid"))) {
                $("#role_rolePermission_editPermissionForm").form("reset");
                var rows = $(".easyui-datagrid").datagrid("getChecked");
                var userId = rows[0].id;
                $("#role_rolePermission_editPermissionForm").form("load", rows[0]);
                $("#viewPermissionRoleCombotree").combotree("reload", url + "?param=add&userId=" + userId + flagUrl);
                $("#viewPermissionRoleCombotrees").combotree("reload", url + "?param=revoke&userId=" + userId + flagUrl);
                $("#role_rolePermission_addPermissionDialog").dialog("open");
                $("#viewPermissionRoleCombotree").combotree('showPanel');
                $("#viewPermissionRoleCombotrees").combotree('showPanel');
            }
        }

	</script>
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="role_roleView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				角色名:
				<input id="name" class="easyui-textbox" />&nbsp;&nbsp;
				用户名:
				<input id="loginname" class="easyui-textbox" />
			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getRoleList_role.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'name',width:70,align:'center'">角色名</th>
						<th data-options="field:'remark',width:100,align:'center'">备注</th>
						<th data-options="field:'username',width:150,align:'center'">拥有该角色的用户</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog">
		<form id="add_edit_form" method="post">
			<input type="hidden" id="editFormRoleId" name="id">
			<table class="add-edit-form-table">
				<tr>
					<th>角色名:</th>
					<td><input id="editFormRoleName" type="text" class="easyui-textbox" name="name" data-options="validType:'middleLength[1,20]',required: true,missingMessage:'请输入角色名', width:300"/></td>
				</tr>
				<tr>
					<th>备注:</th>
					<td><input id="editFormRoleRemark" type="text" class="easyui-textbox" name="remark" data-options="validType:'middleLength[1,100]', required: true,missingMessage:'请输入备注', width:300, multiline: true, height: 100"/></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="role_rolePermission_addPermissionDialog">
		<form id="role_rolePermission_editPermissionForm" method="post">
			<input type="hidden" name="id">
			<table class="add-edit-form-table">
				<tr>
					<th><label id="overLabel"></label></th>
					<th><label id="readyLabel"></label></th>
				</tr>
				<tr>
					<td><input id="viewPermissionRoleCombotree" class="easyui-combotree" data-options="checkbox:true, width:280, multiple:true, panelHeight:228"></td>
					<td><input id="viewPermissionRoleCombotrees" class="easyui-combotree" data-options="checkbox:true, width:280, multiple:true, panelHeight:228"></td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getRoleList" />
</body>
</html>