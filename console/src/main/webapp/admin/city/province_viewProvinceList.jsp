<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">

	</script>
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="province_provinceView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				省份名称:
				<input id="province" class="easyui-textbox" />&nbsp;&nbsp;
				省份邮编:
				<input id="code" class="easyui-numberbox" data-options="length:6" />
			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getProvinceList_province.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'province',width:80,align:'center',sortable:true">省份名称</th>
						<th data-options="field:'code',width:100,align:'center',sortable:true" >省份邮编</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog">
		<form id="add_edit_form" method="post">
			<input type="hidden" id="editFormCodeId" name="id">
			<table class="add-edit-form-table">
				<tr>
					<th>省份名称: </th>
					<td><input id="editFormCodeName" name="province" class="easyui-textbox" data-options="validType:'middleLength[1,20]', required: true,missingMessage:'请输入省份名称', width:300"/></td>
				</tr>
				<tr>
					<th>省份邮编: </th>
					<td><input id="editFormCode" name="code" class="easyui-numberbox" data-options="validType:'middleLength[1,6]', required: true,missingMessage:'请输入邮编', width:300, length:6"/></td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getProvinceList" />

</body>
</html>