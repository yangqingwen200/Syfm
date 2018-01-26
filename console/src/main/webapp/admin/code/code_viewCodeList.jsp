<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        function descriptionFmt(value, row, index){
            return "<span title=" + value + ">" + value + "</span>";
        }
	</script>
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="code_codeView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				Code:
				<input id="code" class="easyui-textbox" />&nbsp;&nbsp;
				编码名称:
				<input id="code_name" class="easyui-textbox" />
			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getCodeList_code.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'code',width:80,align:'center'">Code</th>
						<th data-options="field:'codeName',width:100,align:'center'" >编码名称</th>
						<th data-options="field:'value',width:100,align:'center',formatter:descriptionFmt">编码值</th>
						<th data-options="field:'description',width:150,align:'center',formatter:descriptionFmt">描述</th>
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
					<th>Code:</th>
					<td><input id="editFormCode" name="code" class="easyui-textbox" data-options="validType:'middleLength[1,50]', required: true,missingMessage:'请输入Code', width:300"/></td>
				</tr>
				<tr>
					<th>编码名称:</th>
					<td><input id="editFormCodeName" name="codeName" class="easyui-textbox" data-options="validType:'middleLength[1,50]', required: true,missingMessage:'请输入编码名称', width:300"/></td>
				</tr>
				<tr>
					<th>编码值:</th>
					<td><input id="editFormCodeValue" name="value" class="easyui-textbox" data-options="validType:'middleLength[1,200]', required: true,missingMessage:'请输入编码值', width:300"/></td>
				</tr>
				<tr>
					<th>描述:</th>
					<td><input id="editFormCodeDescription" name="description" class="easyui-textbox" data-options="validType:'middleLength[1,1000]', required: true,missingMessage:'请输入描述', width:300, multiline: true, height:100"/></td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getCodeList" />

</body>
</html>