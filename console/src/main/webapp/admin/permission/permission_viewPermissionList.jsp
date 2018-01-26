<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        function iselementFmt(value, row, index) {
            return (value == 1) ? "<span style='color: green;'>是</span>" : "<span style='color: red;'>否</span>";
        }
	</script>
	<mytag:permissionInitJSData />
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="permission_permissionView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				权限名:
				<input id="permission" class="easyui-textbox" />&nbsp;&nbsp;
				code:
				<input id="code" class="easyui-textbox" />&nbsp;&nbsp;
				父级:
				<select class="easyui-combobox" id="parent"></select>&nbsp;&nbsp;
				包含按钮:
				<select class="easyui-combobox" id="iselement" data-options="width: 75, panelHeight: 75">
					<option value="">请选择</option>
					<option value="1">包含</option>
					<option value="0">不包含</option>
				</select>
			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getPermissionList_permission.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'permission',width:70,align:'center'">权限名</th>
						<th data-options="field:'code',width:70,align:'center'">code</th>
						<th data-options="field:'remark',width:150,align:'center'">备注</th>
						<th data-options="field:'parent',width:100,align:'center',formatter:parentFmt">父级</th>
						<th data-options="field:'iselement',width:100,align:'center',formatter:iselementFmt">包含页面按钮</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog">
		<form id="add_edit_form" method="post">
			<input type="hidden" id="editFormPermissionId" name="id">
			<table class="add-edit-form-table">
				<tr>
					<th>权限名:</th>
					<td><input id="editFormPermissionName" type="text" class="easyui-textbox" name="permission" data-options="validType:'middleLength[1,20]',required: true,missingMessage:'请输入权限名', width:300"/></td>
				</tr>
				<tr>
					<th>code:</th>
					<td><input id="editFormPermissionPath" type="text" class="easyui-textbox" name="code" data-options="validType:'middleLength[1,30]',required: true,missingMessage:'请输入code', width:300"/></td>
				</tr>
				<tr>
					<th>包含按钮:</th>
					<td>
						<select id="editFormPermissionIselement" name="iselement" class="easyui-combobox" data-options="width: 300,panelWidth: 300,panelHeight: 50,editable: false">
							<option selected="selected" value="0">否</option>
							<option value="1">是</option>
						</select>
					 </td>
				</tr>
				<tr>
					<th>父级:</th>
					<td><input id="editFormPermissionParent" type="text" name="parent" class="easyui-combobox" data-options="required: true, valueField: 'id', textField: 'text', width: 300, panelHeight: 147, missingMessage:'请选择父级',editable: false" /></td>
				</tr>
				<tr>
					<th>备注:</th>
					<td><input id="editFormPermissionRemark" type="text" class="easyui-textbox" name="remark" data-options="validType:'middleLength[1,50]', required: true,missingMessage:'请输入备注', width:300, multiline: true, height: 100"/></td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getPermissionList" />

</body>
</html>