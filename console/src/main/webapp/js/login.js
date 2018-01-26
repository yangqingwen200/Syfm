$(function() {
    $("#user_login_loginForm").form({
        onSubmit: function() {
            var isValid = $(this).form('validate');
            if(!isValid) {
                return false;
            }
            var pw = $("#pw1").textbox("getValue");
            $("#pw").val(hex_md5(pw));
            $("#btnLogin").linkbutton({disabled: true});
        },
        success : function(data) {
            var res = $.parseJSON(data);
            if(res.message) {
				/*跳入主页面*/
                window.location.href="main.jsp";
            } else {
                var errorMsg = res.errorMsg;
                $("#btnLogin").linkbutton({disabled: false});
                easyuishow(errorMsg);
                $("#loginname").textbox("textbox").focus();
            }
        }
    });

    $("#reset").click(function(){
        $("#user_login_loginForm").form("clear");
    });

    $("#loginname").textbox("textbox").focus();

});

function easyuishow(content) {
    $.messager.show({
        title: "我的消息（窗口将在3秒后关闭）",
        msg: content,
        timeout: 3000,
        showType: 'show'
    });
}
