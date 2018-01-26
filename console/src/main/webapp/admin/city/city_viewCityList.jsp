<%@ page language="java" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
    <mytag:ordEasyuiJs all="true"/>
    <script type="text/javascript">

    </script>
    <mytag:cityInitJSData />
</head>

<body>
    <%@include file="/admin/common/loadingDiv.jsp"%>
    <div id="city_cityView_Search" class='easyui-layout' data-options='fit: true, border:false'>
        <div id="north_frame" data-options="region:'north', border:false">
            <div id="north_frame_search">
                城市名称:
                <input id="city" class="easyui-textbox" />&nbsp;&nbsp;
                城市邮编:
                <input id="code" class="easyui-numberbox" data-options="length:6" />&nbsp;&nbsp;
                首字母:
                <input id="letter" class="easyui-textbox" />&nbsp;&nbsp;
                所属省份:
                <select id="province" class="easyui-combobox"></select>
            </div>
        </div>
        <div data-options="region:'center', border:false">
            <table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getCityList_city.do'">
                <thead>
                <tr>
                    <th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
                    <th data-options="field:'city',width:80,align:'center',sortable:true">城市名称</th>
                    <th data-options="field:'code',width:100,align:'center',sortable:true">城市邮编</th>
                    <th data-options="field:'letter',width:100,align:'center',sortable:true">首字母</th>
                    <th data-options="field:'provinceCode',width:100,align:'center',sortable:true,formatter:parentFmt">所属省份</th>
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
                    <th>城市名称:</th>
                    <td><input id="editFormCityName" name="city" class="easyui-textbox" data-options="validType:'middleLength[1,20]', required: true,missingMessage:'请输入省份名称', width:300"/></td>
                </tr>
                <tr>
                    <th>城市邮编:</th>
                    <td><input id="editFormCodeName" name="code" class="easyui-numberbox" data-options="validType:'middleLength[1,6]', required: true,missingMessage:'请输入邮编', width:300, length:6"/></td>
                </tr>
                <tr>
                    <th>首字母:</th>
                    <td><input id="editFormLetter" name="letter" class="easyui-combobox" data-options="required: true, width: 300, panelHeight: 148, missingMessage:'请输入首字母'"/></td>
                </tr>
                <tr>
                    <th>所属省份:</th>
                    <td><input id="editFormProvinceCode" name="provinceCode" class="easyui-combobox" data-options="required: true, width: 300, panelHeight: 148, missingMessage:'请选择所属省份'"/></td>
                </tr>
            </table>
        </form>
    </div>

    <mytag:toolbar name="getCityList" />

</body>
</html>