<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        function getPermissionId(parentid) {
            var result;
            $.ajax({
                url: "${ctxajax}/getPermissionId.do",
                data: {"parentid": parentid},
                cache: false,
                async: false,
                success: function (data) {
                    var da = $.parseJSON(data);
                    result = da.perssmionid;
                }
            });

            return result;
        }

	</script>
	<mytag:elementInitJSData />
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="element_elementView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				按钮名称:
				<input id="name" class="easyui-textbox" />&nbsp;&nbsp;
				JS方法名:
				<input id="jsname" class="easyui-textbox" />&nbsp;&nbsp;
				父级:
				<select id="parent" class="easyui-combobox">
				</select>&nbsp;&nbsp;
				状态:
				<select class="easyui-combobox" id="disabled" data-options="width: 75, panelHeight: 75">
					<option value="">请选择</option>
					<option value="1">启用</option>
					<option value="0">禁用</option>
				</select>
			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getElementList_element.do'">
				<thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'code',width:100,align:'center'">按钮ID</th>
						<th data-options="field:'buttomName',width:50,align:'center'">按钮名称</th>
						<th data-options="field:'functionName',width:150,align:'center'">JS方法名(参数)</th>
						<th data-options="field:'icon',width:40,align:'center'">图标</th>
						<th data-options="field:'description',width:60,align:'center'">描述</th>
						<th data-options="field:'parentName',width:60,align:'center'">父级</th>
						<th data-options="field:'parent',width:10,align:'center',hidden:true">父级id</th>
						<th data-options="field:'permissionId',width:60,align:'center',hidden:true"></th>
						<th data-options="field:'permissionremark',width:60,align:'center'">所属页面</th>
						<th data-options="field:'disabled',width:20,align:'center',formatter:statusFmt">状态</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog">
		<form id="add_edit_form" method="post">
			<input type="hidden" id="editFormElementId" name="id">
			<table class="add-edit-form-table">
				<tr>
					<th>按钮ID:</th>
					<td><input name="code" class="easyui-textbox" data-options="validType:'middleLength[1,100]',required: true,missingMessage:'请输入按钮ID', width:300"/></td>
				</tr>
				<tr>
					<th>按钮名称:</th>
					<td><input name="buttomName" class="easyui-textbox" data-options="validType:'middleLength[1,20]',required: true,missingMessage:'请输入按钮名称', width:300"/></td>
				</tr>
				<tr>
					<th>方法(参数):</th>
					<td><input name="functionName" class="easyui-textbox" data-options="validType:'middleLength[1,500]',required: true,missingMessage:'请输入JS方法名', width:300"/></td>
				</tr>
				<tr>
					<th>按钮图标:</th>
					<td><input name="icon" class="easyui-textbox" data-options="validType:'middleLength[1,200]',required: true,missingMessage:'请输入按钮图标', width:300"/></td>
				</tr>
				<tr>
					<th>按钮描述:</th>
					<td><input name="description" class="easyui-textbox" data-options="validType:'middleLength[1,100]',required: true,missingMessage:'请输入按钮描述', width:300"/></td>
				</tr>
				<tr>
					<th>父级:</th>
					<td>
						<select id="parentId" name="parent" class="easyui-combobox" style="width:300px;"
							data-options="valueField:'id', textField:'text', panelHeight:120, editable:true,
											onSelect: function(record) {
												if(record.id != 0) {
													var result = getPermissionId(record.id);
													$('#permissionId').combobox('select', result);
												}
											}">
						</select>
					</td>
				</tr>
				<tr>
					<th>所属页面:</th>
					<td>
						<select id="permissionId" name="permissionId" class="easyui-combobox" style="width:300px;"
							data-options="valueField:'id', textField:'text', panelHeight:93, editable:false">
						</select>
					</td>
				</tr>
				<tr>
					<th>状态:</th>
					<td>
						<select name="disabled" class="easyui-combobox" style="width:300px;" data-options="panelHeight: 50,editable: false">
							<option value="1">正常</option>
							<option value="0">禁用</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getElementList" />

</body>
</html>