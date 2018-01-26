$.extend($.fn.form.defaults, {
    novalidate: true,
    onSubmit: function(){
        var isValid = $(this).form("validate");
        if(!isValid) {
            return false;
        }
        $.messager.progress({text:'正在处理中...', interval:600, title:"注意: 如果操作成功, 该窗口会自动关闭..."});
    },
    success : function(data) {
        var data1 = $.parseJSON(data);
        if(checkPermission(data1)) {
            colseTab(true);
        }else {
            $.messager.progress('close');
        }
    }
});