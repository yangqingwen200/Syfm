<%@ page language="java" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
    <mytag:ordEasyuiJs rules="true" pageform="true" />

    <script type="text/javascript">
        $(function() {
            $("#smt").click(function(){
                $("#appuser_addAppUser").form('submit');
            });
        });
    </script>
</head>

<body>

<form action="${ctxweb}/addAppUser_appUser.do" method="post" id="appuser_addAppUser">
    <input type="hidden" name="status" value="0"/>

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
        <tr>
            <td colspan="2" style="text-align: center">
                <input id="smt" type="button" class="easyui-linkbutton" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
            </td>
        </tr>
    </table>

</form>

</body>
</html>