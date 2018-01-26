<%@ page language="java" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
    <mytag:ordEasyuiJs all="true"/>
</head>

<body>
    <%@include file="/admin/common/loadingDiv.jsp"%>
    <div id="loginlog_loginlogView_Search" class='easyui-layout' data-options='fit: true, border:false'>
        <div id="north_frame" data-options="region:'north', border:false">
            <div id="north_frame_search">
                登录人:
                <input id="loginName" class="easyui-textbox" />&nbsp;&nbsp;
                登录时间:
                <input id="logintime" class="easyui-datebox" data-options="currentText:'',closeText:'', editable: false" style="width:180px">
            </div>
        </div>
        <div data-options="region:'center'">
            <table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getloginlogList_loginlog.do',sortName:'login_time',sortOrder:'desc',showFooter:true">
                <thead>
                <tr>
                    <%--<th data-options="field:'id',width:20,align:'center',sortable:true">id</th>--%>
                    <th data-options="field:'login_name',width:50,align:'center'">登录人</th>
                    <th data-options="field:'ip_address',width:100,align:'center'">登录ip</th>
                    <th data-options="field:'login_time',width:100,align:'center',sortable:true">登录时间</th>
                    <th data-options="field:'logout_time',width:100,align:'center',sortable:true">退出时间</th>
                    <th data-options="field:'onlinetime',width:50,align:'center'">在线时长(分钟)</th>
                    <th data-options="field:'session_id',width:100,align:'center'">session</th>
                    <th data-options="field:'logout_type',width:50,align:'center'">下线方式</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</body>
</html>

