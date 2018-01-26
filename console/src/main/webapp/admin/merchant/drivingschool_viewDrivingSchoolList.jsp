<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        var flagDetailUrl = "${ctxsts}/appuser_detailDrivingSchool_drivingSchool.do";

        function statusFmt(value, rowData, rowIndex) {
            if(value == 1) {
                return "<span style=\"color: blue;\">审核</span>";
            } else if(value == 2) {
                return "<span style=\"color: red;\">屏蔽</span>";
            } else if(value == 3) {
                return "<span>正常</span>";
            }
        }

	</script>
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="drivingschool_drivingschoolView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				驾校名称:
				<input id="name" class="easyui-textbox" />&nbsp;&nbsp;
				手机号码:
				<input id="link_tel" class="easyui-numberbox" data-options="length:11" />&nbsp;&nbsp;
				状态:
				<select id="status" class="easyui-combobox" data-options="width:75,panelHeight:95">
					<option value="">请选择</option>
					<option value="3">正常</option>
					<option value="1">审核</option>
					<option value="2">屏蔽</option>
				</select>
			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getDrivingSchoolList_drivingSchool.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'name',width:60,align:'center'<mytag:show value="detailDrivingSchool">,formatter:dtlFmtTabs</mytag:show>">驾校名称</th>
						<th data-options="field:'linkMan',width:30,align:'center'">联系人</th>
						<th data-options="field:'linkTel',width:50,align:'center'">手机号码</th>
						<th data-options="field:'address',width:150,align:'center'">详细地址</th>
						<th data-options="field:'tel',width:50,align:'center'">热线电话</th>
						<th data-options="field:'createDate',width:50,align:'center'">创建时间</th>
						<th data-options="field:'introduction',width:150,align:'center',formatter:moreFmt">简介</th>
						<th data-options="field:'status',width:20,align:'center',formatter:statusFmt">状态</th>
					</tr>
				</thead>

			</table>
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog" data-options="height:450">
		<form id="add_edit_form" method="post" enctype="multipart/form-data">
			<input type="hidden" id="editFormAppuserId" name="id">
			<table class="add-edit-form-table">
				<tr>
					<th>驾校名称:</th>
					<td><input name="name" class="easyui-textbox" data-options="validType:'middleLength[1,10]', required: true,missingMessage:'请输入驾校名称', width:300"/></td>
				</tr>
				<tr>
					<th>联系人:</th>
					<td><input name="linkMan" class="easyui-textbox" data-options="validType:'middleLength[1,32]',required: true,missingMessage:'请输入联系人', width:300"/></td>
				</tr>
				<tr>
					<th>手机号码:</th>
					<td><input name="linkTel" class="easyui-numberbox" data-options="validType:'checkMobile',required: true,missingMessage:'请输入手机号码', width:300, length:11"/></td>
				</tr>
				<tr>
					<th>详细地址:</th>
					<td><input name="address" class="easyui-textbox" data-options="validType:'middleLength[1,100]',required: true,missingMessage:'请输入详细地址', width:300"/></td>
				</tr>
				<tr>
					<th>热线电话:</th>
					<td><input name="tel" class="easyui-textbox" data-options="validType:'middleLength[1,13]',required: true,missingMessage:'请输入热线电话', width:300"/></td>
				</tr>
				<tr>
					<th>状态:</th>
					<td>
						<select name="status" class="easyui-combobox" data-options="width: 300,panelHeight: 75">
							<option selected="selected" value="3">正常</option>
							<option value="1">审核</option>
							<option value="2">屏蔽</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>简介:</th>
					<td><input name="introduction" class="easyui-textbox" data-options="validType:'middleLength[1,5000]', required: true,missingMessage:'请输入备注', width:300, multiline: true, height:150"/></td>
				</tr>
			</table>
		</form>
	</div>

	<mytag:toolbar name="getDrivingSchoolList" />

</body>
</html>