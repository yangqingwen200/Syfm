<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        var flagDetailUrl = "${ctxweb}/detailAppUser_appUser.do";

        function logourlFmt(value,row,index) {
            if(value) {
                return "<a onclick=\"viewUserImage('" + value + "')\" style=\"cursor: pointer;\"><img style='width: 16px;height: 16px;' alt='加载失败' title='点击查看大图' src='" + value + "'></a>";
            } else {
                return "暂无";
                /**/
            }
        }

        function appUserEdit(url) {
            if(datagridChecked($(".easyui-datagrid"))) {
                var rows = $(".easyui-datagrid").datagrid("getChecked");
                var obj = rows[0];
                obj.logourl = null;
                commonEdit(url);
            }
        }

        function viewUserImage(url) {
            var con = "<img id=\"viewUserHeadImae_image\" style=\"max-width: 100%;max-height: 100%\" alt=\"图片链接地址失效...\"  src=" + url + ">";
            $("#viewUserHeadImage").dialog({
                content: con
            });
            $("#viewUserHeadImage").dialog("open");
        }

        function pwFmt(value,row,index) {
            return "<span title=" + value + ">******</span>";
        }

	</script>
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="user_userAppView_Search" class='easyui-layout' data-options='fit: true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				昵称:
				<input id="name" class="easyui-textbox" />&nbsp;&nbsp;
				手机号码:
				<input id="telephone" class="easyui-numberbox" data-options="length:11" />

			</div>
		</div>
		<div data-options="region:'center', border:false">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getAppUserList_appUser.do'">
				 <thead>
					<tr>
						<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
						<th data-options="field:'name',width:100,align:'center',sortable:true<mytag:show value="detailAppUser">,formatter:dtlFmtDialog</mytag:show>">昵称</th>
						<th data-options="field:'pw',width:100,align:'center',formatter:pwFmt">密码</th>
						<th data-options="field:'pw1',width:100,align:'center',hidden:true" ></th>
						<th data-options="field:'status',width:100,align:'center',hidden:true" ></th>
						<th data-options="field:'telephone',width:100,align:'center',sortable:true">手机号码</th>
						<th data-options="field:'sessionId',width:100,align:'center'">sessionId</th>
						<th data-options="field:'logourl',width:30,align:'center',formatter:logourlFmt">用户头像</th>
					</tr>
				</thead>

			</table>
		</div>
	</div>

	<div id="add_edit_dialog" class="easyui-dialog">
		<form id="add_edit_form" method="post" enctype="multipart/form-data">
			<input type="hidden" id="editFormAppuserId" name="id">
			<input type="hidden" id="editFormAppuserPw1" name="pw1">
			<input type="hidden" id="editFormAppuserStatus" name="status">
			<table class="add-edit-form-table">
				<tr>
					<th>昵称:</th>
					<td><input name="name" class="easyui-textbox" data-options="validType:'middleLength[1,10]', required: true,missingMessage:'请输入昵称', width:300"/></td>
				</tr>
				<tr>
					<th>密码:</th>
					<td><input name="pw" class="easyui-textbox" data-options="validType:'middleLength[1,32]',required: true,missingMessage:'请输入密码', width:300, type:'password'"/></td>
				</tr>
				<tr>
					<th>手机号码:</th>
					<td><input name="telephone" class="easyui-numberbox" data-options="validType:'checkMobile', required: true,missingMessage:'请输入11位数字的手机号码', width:300, length:11"/></td>
				</tr>
				<tr>
					<th>头像:</th>
					<td><input name="logourl" class="easyui-filebox" data-options="validType:'checkImage', buttonText:'选择图片', buttonIcon:'icon-search'" style="width:300px"></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="viewUserHeadImage" class="easyui-dialog" data-options="title:'浏览用户头像', border:false, iconCls:'icon-search', buttons: null" style="text-align:center">
	</div>

	<mytag:toolbar name="getAppUserList" />
	<%-- 不要使用jsp:include动态包含,否则后台管理系统页面渲染会出现闪动 --%>
	<%@ include file="/admin/common/viewDetail.jsp"%>

</body>
</html>