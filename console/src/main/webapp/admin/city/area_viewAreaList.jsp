<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        $(function() {
            $("#province").combobox({
                onChange: function(newValue, oldValue) {
                    $("#city").combobox("clear");
                    if(newValue) {
                        $("#city").combobox("reload", "${ctxajax}/getCityByProvince.do?flag=view&code=" + newValue + "");
                    } else {
                        $("#city").combobox("reload", "${ctxajax}/getCityByProvince.do?flag=view");
                    }
                }
            });

           $("#editFormProvince").combobox({
			   onSelect: function(record) {
			       var pc = record.id;
                   $("#editFormCity").combobox("clear");
                   $("#editFormCity").combobox("reload", "${ctxajax}/getCityByProvince.do?flag=edit&code=" + pc + "");
			   }
            });
        });
        function areaEdit(url) {
            if(datagridChecked($("#table_data_datagrid"))) {
                var rows = $("#table_data_datagrid").datagrid("getChecked");
                $("#editFormCity").combobox("clear");
                $("#editFormCity").combobox("reload", "${ctxajax}/getCityByProvince.do?flag=edit&code=" + rows[0].provinceCode + "");
            }
            commonEdit(url);
        }

        function provinceFmt(value, row, index){
            return row.provinceName;
        }

        function cityFmt(value, row, index){
            return row.cityName;
        }

	</script>

	<%--初始化数据太多, 页面会出现闪屏现象--%>
	<mytag:areaInitJSData />
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="area_areaView_Search" class='easyui-layout' data-options='fit:true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				县/区名称:
				<input id="area" class="easyui-textbox" />&nbsp;&nbsp;
				县/区邮编:
				<input id="code" class="easyui-numberbox" data-options="length:6" />&nbsp;&nbsp;
				所属省份:
				<select id="province" class="easyui-combobox" data-options="editable: true"></select>&nbsp;&nbsp;
				所属城市:
				<select id="city" class="easyui-combobox"></select>
			</div>
		</div>
		<div data-options="region:'center', border:false">

			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getAreaList_area.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'area',width:80,align:'center',sortable:true">县/区名称</th>
						<th data-options="field:'code',width:100,align:'center',sortable:true" >县/区邮编</th>
						<th data-options="field:'provinceCode',width:100,align:'center',formatter:provinceFmt" >所属省份</th>
						<th data-options="field:'cityCode',width:100,align:'center',formatter:cityFmt" >所属城市</th>
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
					<th>县/区名称: </th>
					<td><input id="editFormAreaName" name="area" class="easyui-textbox" data-options="validType:'middleLength[1,20]', required: true,missingMessage:'请输入县/区名称', width:300"/></td>
				</tr>
				<tr>
					<th>县/区邮编: </th>
					<td><input id="editFormCodeName" name="code" class="easyui-numberbox" data-options="validType:'middleLength[1,6]', required: true,missingMessage:'请输入县/区邮编', width:300, length:6"/></td>
				</tr>
				<tr>
					<th>所属省份: </th>
					<td><input id="editFormProvince" name="provinceCode" class="easyui-combobox" data-options="required: true, width: 300, panelHeight: 148, missingMessage:'请选择所属省份'" /></td>
				</tr>
				<tr>
					<th>所属城市: </th>
					<td><input id="editFormCity" name="cityCode" class="easyui-combobox" data-options="required: true, width: 300, panelHeight: 148, missingMessage:'请选择所属城市'" /></td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getAreaList" />

</body>
</html>