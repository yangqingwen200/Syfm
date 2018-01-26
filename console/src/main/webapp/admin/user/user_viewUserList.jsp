<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
    <mytag:ordEasyuiJs all="true"/>
	<script type="text/javascript">
        var flagUrl = "";
        var flagDetailUrl = "${ctxsts}/user_detailUser_user.do";

        //导出用户列表
        function userExport(url) {
            var searchname = $("#loginname").textbox("getText");
            $("#user_userExport_exportForm").form("submit", {
                url: url + "?name=" + searchname,
                success: function(data){
                    data = $.parseJSON(data);
                    checkPermission(data);
                }
            });
        }

        function sexFmt(value, rowData, rowIndex) {
            return (value == 1) ? "<span>男</span>" : "<span>女</span>";
        }

        function distPermissionByUser(url) {
            $("#user_userPermission_addPermissionDialog").dialog("setTitle", "分配/收回权限");
            $("#overLabel").html("收回已分配的权限:");
            $("#readyLabel").html("授权未分配的权限:");
            flagUrl = "&flag=permission";
            oper(url);
        }

        function distMenuByUser(url) {
            $("#user_userPermission_addPermissionDialog").dialog("setTitle", "分配/收回菜单");
            $("#overLabel").html("收回已分配的菜单:");
            $("#readyLabel").html("授权未分配的菜单:");
            flagUrl = "&flag=menu";
            oper(url);
        }

        function distElementByUser(url) {
            $("#user_userPermission_addPermissionDialog").dialog("setTitle", "分配/收回按钮");
            $("#overLabel").html("收回已分配的按钮:");
            $("#readyLabel").html("授权未分配的按钮:");
            flagUrl = "&flag=element";
            oper(url);
        }

        function distRoleByUser(url) {
            $("#user_userPermission_addPermissionDialog").dialog("setTitle", "分配/收回角色");
            $("#overLabel").html("收回已分配的角色:");
            $("#readyLabel").html("授权未分配的角色:");
            flagUrl = "&flag=role";
            oper(url);
        }

        function oper(url) {
            if(datagridChecked($("#table_data_datagrid"))) {
                $("#user_userPermission_editPermissionForm").form("reset");
                var rows = $("#table_data_datagrid").datagrid("getChecked");
                var userId = rows[0].id;
                $("#user_userPermission_editPermissionForm").form("load", rows[0]);
                $("#viewPermissionUserCombotree").combotree("reload", url + "?param=add&userId=" + userId + flagUrl);
                $("#viewPermissionUserCombotrees").combotree("reload", url + "?param=revoke&userId=" + userId + flagUrl);
                $("#user_userPermission_addPermissionDialog").dialog("open");
                $("#viewPermissionUserCombotree").combotree('showPanel');
                $("#viewPermissionUserCombotrees").combotree('showPanel');
            }
        }

        function pwFmt(value,row,index) {
            return "<span title=" + value + ">******</span>";
        }

        $(function(){
            //分配/收回权限
            $("#user_userPermission_addPermissionDialog").dialog({
                height: 370,
                buttons:[{
                    iconCls:"icon-save",
                    text:"保存",
                    handler:function(){
                        var revoke = $("#viewPermissionUserCombotrees").combotree("getValues");
                        var add = $("#viewPermissionUserCombotree").combotree("getValues");
                        $("#user_userPermission_editPermissionForm").form("submit",{url: "${ctxweb}/addOrRevokePMEByUser_user.do?add=" + revoke + "&remove=" + add + flagUrl});
                    }
                }]
            });

            $("#user_userPermission_editPermissionForm").form({
                success : function(data) {
                    operFormSuccessOrFailShow(data, $("#user_userPermission_addPermissionDialog"));
                }
            });

        });

	</script>
</head>

<body>
    <%@include file="/admin/common/loadingDiv.jsp"%>
    <div id="user_userView_Search" class='easyui-layout' data-options='fit: true, border:false'>
        <div id="north_frame" data-options="region:'north', border:false">
            <div id="north_frame_search">
                登录名:
                <input id="loginname" class="easyui-textbox" />&nbsp;&nbsp;
                姓名:
                <input id="name" class="easyui-textbox" />&nbsp;&nbsp;
                性别:
                <select id="sex" class="easyui-combobox" data-options="width:75,panelHeight:75">
                    <option value="">请选择</option>
                    <option value="1">男</option>
                    <option value="0">女</option>
                </select>&nbsp;&nbsp;
                状态:
                <select id="status" class="easyui-combobox" data-options="width:75,panelHeight:75">
                    <option value="">请选择</option>
                    <option value="1">正常</option>
                    <option value="0">禁用</option>
                </select>
            </div>
        </div>
        <div data-options="region:'center', border:false">
            <table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getWebUserList_user.do'">
                <thead>
                <tr>
                    <th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
                    <th data-options="field:'loginname',width:80,align:'center'<mytag:show value="detailUser">,formatter:dtlFmtTabs</mytag:show>">登录名</th>
                    <th data-options="field:'name',width:80,align:'center'">姓名</th>
                    <th data-options="field:'sex',width:50,align:'center',formatter:sexFmt">性别</th>
                    <th data-options="field:'pw',width:150,align:'center',formatter:pwFmt">密码</th>
                    <th data-options="field:'telephone',width:100,align:'center'">手机号码</th>
                    <th data-options="field:'email',width:100,align:'center'">电子邮箱</th>
                    <th data-options="field:'status',width:50,align:'center',formatter:statusFmt">状态</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div id="add_edit_dialog" class="easyui-dialog">
        <form id="add_edit_form" method="post">
            <input type="hidden" id="editFormuserId" name="id">
            <table class="add-edit-form-table">
                <tr>
                    <th>登录名:</th>
                    <td><input id="editFormLoginName" type="text" name="loginname" class="easyui-textbox" data-options="validType:'middleLength[1,10]',required: true,missingMessage:'请输入登录名', width:300"/></td>
                </tr>
                <tr>
                    <th>姓名:</th>
                    <td><input id="editFormName" type="text" name="name" class="easyui-textbox" data-options="validType:'middleLength[1,10]',required: true,missingMessage:'请输入姓名', width:300"/></td>
                </tr>
                <tr>
                    <th>密码:</th>
                    <td><input id="editFormPw" name="pw" class="easyui-textbox" data-options="validType:'middleLength[1,32]',required: true,missingMessage:'请输入密码', width:300,type:'password'"/></td>
                </tr>
                <tr>
                    <th>手机号码:</th>
                    <td><input id="editFormTelephone" type="text" name="telephone" class="easyui-numberbox" data-options="validType:'checkMobile',required: true,missingMessage:'请输入手机号码', width:300, length:11"/></td>
                </tr>
                <tr>
                    <th>电子邮箱:</th>
                    <td><input id="editFormEmail" type="text" name="email" class="easyui-textbox" data-options="validType:'email', required: true,missingMessage:'请输入电子邮箱', invalidMessage:'电子邮箱填写不正确', width:300"/></td>
                </tr>
                <tr>
                    <th>性别:</th>
                    <td>
                        <select id="editFormSex" name="sex" class="easyui-combobox" data-options="width: 300, panelHeight: 50">
                            <option selected="selected" value="1">男</option>
                            <option value="0">女</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>状态:</th>
                    <td>
                        <select id="editFormStatus" name="status" class="easyui-combobox" data-options="width: 300, panelHeight: 50">
                            <option selected="selected" value="1">正常</option>
                            <option value="0">禁用</option>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <div id="user_userPermission_addPermissionDialog">
        <form id="user_userPermission_editPermissionForm" method="post">
            <input type="hidden" name="id">
            <table class="add-edit-form-table">
                <tr>
                    <th><label id="overLabel"></label></th>
                    <th><label id="readyLabel"></label></th>
                </tr>
                <tr>
                    <td><input id="viewPermissionUserCombotree" class="easyui-combotree" data-options="checkbox:true, width:280, multiple:true, panelHeight:228"></td>
                    <td><input id="viewPermissionUserCombotrees" class="easyui-combotree" data-options="checkbox:true, width:280, multiple:true, panelHeight:228"></td>
                </tr>
            </table>
        </form>
    </div>

    <div id="user_userExport_exportDialog">
        <form id="user_userExport_exportForm" method="post">
        </form>
    </div>

    <%--toolbar方式有两种:
        一是在自定义标签ordEasyuiJs中加上toolbar
        二是单独使用自定义标签toolbar, 使用name key-value.
        在这个页面采用第一种方式, 会出现jsp加载延迟, 出现页面渲染慢导致页面闪动现象, 采用第二种方式不会, 原因待查.
        其他页面采用第一种方式不会出现页面闪动现象.
    --%>
    <mytag:toolbar name="getWebUserList" />

    <%-- 不要使用jsp:include动态包含,否则后台管理系统页面渲染会出现闪动 --%>
    <%--<%@ include file="/admin/common/viewDetail.jsp"%>--%>

</body>
</html>

