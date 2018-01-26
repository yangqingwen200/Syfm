<%@ page language="java" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>

    <link rel="stylesheet" type="text/css" href="${ctx}/js/jquery-validation-1.14.0/labeltip.css">
    <script src="${ctx}/js/jquery/jquery-1.11.1-min.js"></script>
    <script src="${ctx}/js/jquery-validation-1.14.0/jquery.validate.min.js"></script>
    <script src="${ctx}/js/jquery-validation-1.14.0/additional-methods.js"></script>
    <script src="${ctx}/js/jquery-validation-1.14.0/jquery.validate.extend.js"></script>
    <script type="text/javascript">

        $(function() {
            $("#appuser_addAppUser").validate({
                <%-- rules中的loginname, username, pw等等均为输入框name值, 而不是id值 --%>
                rules: {
                    name:{
                        required: true,
                        minlength: 2
                    },
                    loginname:{
                        required: true,
                        minlength: 5,
                        <%-- 异步请求服务器: 自动会把输入值传递到服务器(loginname=xxx) --%>
                        remote:{
                            url:"${ctxajax}/checkLoginExists.do",
                            type:"post"
                        }
                    },
                    pw:{
                        required: true,
                        minlength: 6,
                        maxlength: 16
                    },
                    repassword:{
                        required: true,
                        equalTo: "#password" /*id为password值相同*/
                    },
                    telephone: {
                        required: true,
                        /* phone方法扩展放在additional-methods.js文件中*/
                        phone: true
                    },
                    amt: {
                        required: true,
                        /* amtCheck方法扩展放在additional-methods.js文件中*/
                        amtCheck: true
                    },
                    email: {
                        required: true,
                        email:true
                    },
                    address: {
                        required: true,
                        rangelength:[6,20]
                    },
                    like:{
                        required:true,
                    },
                    belong:{
                        required: true,
                    },
                    image: {
                        required: true,
                        extension: 'jpg|jpeg|png'
                    }
                },
                messages:{
                    name:{
                        required: "用户名不能为空",
                        minlength: $.validator.format("用户名的最小长度为{0}个字符")
                    },
                    loginname:{
                        required: "登录名不能为空(会异步判断)",
                        minlength: $.validator.format("用户名的最小长度为{0}个字符"),
                        remote:"该用户名已存在！"
                    },
                    pw:{
                        required: "密码不能为空",
                        minlength: $.validator.format("密码长度不能少于{0}个字符"),
                        maxlength: $.validator.format("密码长度不能超过{1}个字符")
                    },
                    repassword:{
                        required: "确认密码不能为空",
                        equalTo: "确认密码和密码不一致"
                    },
                    telephone:{
                        required: "手机号码不能为空",
                        phone: "手机号码格式不正确"
                    },
                    amt: {
                        required: "金额不能为空"
                    },
                    email:{
                        required: "Email不能为空",
                        email: "Email格式不正确"
                    },
                    address:{
                        required: "家庭住址不能为空",
                        rangelength: $.validator.format("家庭住址的长度在{0}与{1}个字符之间")
                    },
                    like:{
                        required:"请选择爱好"
                    },
                    belong:{
                        required: "请选择所在地",
                    },
                    image: {
                        required: '请选择要上传的头像',
                        extension: "文件后缀名必须为jpg,jpeg,png"
                    }
                }
            });

        });

    </script>
</head>

<body>

<form action="${ctxsts}/appuser_addAppUser_appUser.do" method="post" id="appuser_addAppUser">
    <input type="hidden" name="status" value="0"/>

    <%--for的值与id的值一致--%>
    <fieldset>
        <legend style="font-size: 15px">jquery-validate表单校验验证</legend>
        <div class="item">
            <label for="name" class="item-label">用户名:</label>
            <input type="text" id="name" name="name" class="item-text" placeholder="设置用户名" tip="请输入用户名">
        </div>
        <div class="item">
            <label for="loginname" class="item-label">登录名:</label>
            <input type="text" id="loginname" name="loginname" class="item-text" placeholder="设置登录名" tip="请输入登录名(会异步判断)">
        </div>
        <div class="item">
            <label for="password" class="item-label">密码:</label>
            <input type="password" id="password" name="pw" class="item-text" placeholder="设置密码" tip="长度为6-16个字符">
        </div>
        <div class="item">
            <label for="password" class="item-label">确认密码:</label>
            <input type="password" name="repassword" class="item-text" placeholder="设置确认密码">
        </div>
        <div class="item">
            <label for="telephone" class="item-label">手机号码:</label>
            <input type="text" id="telephone" name="telephone" maxlength="11" class="item-text" placeholder="设置手机号码" tip="长度为11个数字">
        </div>
        <div class="item">
            <label for="amt" class="item-label">金额:</label>
            <input type="text" id="amt" name="amt" class="item-text" placeholder="输入交易金额" tip="交易金额必须大于0，且最多有两位小数">
        </div>
        <div class="item">
            <label for="email" class="item-label">Email:</label>
            <input type="text" id="email" name="email" class="item-text" placeholder="设置Email" tip="请输入Email">
        </div>
        <div class="item">
            <label for="address" class="item-label">家庭住址:</label>
            <input type="text" id="address" name="address" class="item-text" placeholder="设置家庭住址" tip="请输入家庭住址">
        </div>
        <div class="item">
            <label for="like" class="item-label">爱好:</label>
            上网：<input type="checkbox" name="like" value="上网"/>
            唱歌：<input type="checkbox" name="like" value="唱歌"/>
            编程：<input type="checkbox" name="like" value="编程"/>
            书法：<input type="checkbox" name="like" value="书法"/><br/>
        </div>
        <div class="item">
            <label for="belong" class="item-label">所在地:</label>
            <select id="belong" name="belong" class="item-select" tip="请选择所在地">
                <option value="">---请选择---</option>
                <option value="北京">北京</option>
                <option value="上海">上海</option>
                <option value="深圳">深圳</option>
            </select>
        </div>
        <div class="item">
            <label for="image" class="item-label">头像:</label>
            <input type="file" id="image" name="image" class="item-file" placeholder="设置头像" tip="请选择头像文件(jpg|jpeg|png)">
        </div>

        <div class="item">
            <input type="submit" value="提交" class="item-submit">
        </div>
    </fieldset>
</form>

</body>
</html>