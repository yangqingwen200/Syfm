<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<mytag:menuInitJSData />
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="menu_menuView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				菜单名:
				<input id="name" class="easyui-textbox" />&nbsp;&nbsp;
				路径:
				<input id="path" class="easyui-textbox" />&nbsp;&nbsp;
				父级:
				<select class="easyui-combobox" id="parent" ></select>&nbsp;&nbsp;
				状态:
				<select class="easyui-combobox" id="disploy" data-options="width: 75, panelHeight: 75">
					<option value="">请选择</option>
					<option value="1">启用</option>
					<option value="0">禁用</option>
				</select>
			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getMenuList_menu.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'name',width:80,align:'center'">菜单名</th>
						<th data-options="field:'path',width:150,align:'center'">路径</th>
						<th data-options="field:'disploy',width:50,align:'center',formatter:statusFmt">状态</th>
						<th data-options="field:'parent',width:50,align:'center',formatter:parentFmt">父级</th>
						<th data-options="field:'remark',width:80,align:'center'">备注</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog">
		<form id="add_edit_form" method="post">
			<input type="hidden" id="editFormMenuId" name="id">
			<table class="add-edit-form-table">
				<tr>
					<th>菜单名:</th>
					<td><input id="editFormMenuName" name="name" class="easyui-textbox" data-options="validType:'middleLength[1,20]', required: true,missingMessage:'请输入菜单名', width:300"/></td>
				</tr>
				<tr>
					<th>路径:</th>
					<td><input id="editFormMenuPath" name="path" class="easyui-textbox" data-options="validType:'middleLength[1,60]', required: true,missingMessage:'请输入路径', width:300"/></td>
				</tr>
				<tr>
					<th>状态:</th>
					<td>
						<select id="editFormMenuDisploy" name="disploy" class="easyui-combobox" data-options="width: 300,panelHeight: 50">
							<option selected="selected" value="1">正常</option>
							<option value="0">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>父级:</th>
					<td><input id="editFormMenuParent" name="parent" class="easyui-combobox" data-options="required: true, width: 300, panelHeight: 148, missingMessage:'请选择父级'"/></td>
				</tr>
				<tr>
					<th>备注:</th>
					<td><input id="editFormMenuRemark" name="remark" class="easyui-textbox" data-options="validType:'middleLength[1,50]', required: true,missingMessage:'请输入备注', width:300, multiline: true, height:100"/></td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getMenuList" />

</body>
</html>