<%@ page language="java" contentType="text/html; charset=utf-8" %>

<form id="data_add_or_edit_form" class="form-horizontal" action="${ctx}/business/onestop/edit.html" method="post">
    <input type="hidden" name="id" value="${dds.id}">
    <div class="form-group">
        <label for="inputName" class="col-sm-2 control-label">驾校名称</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="inputName" placeholder="请输入驾校名称" name="name" value="${dds.name}">
        </div>
    </div>
    <div class="form-group">
        <label for="inputLinkTel" class="col-sm-2 control-label">联系电话</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="inputLinkTel" placeholder="请输入联系电话" name="linkTel" value="${dds.linkTel}" maxlength="11">
        </div>
    </div>
</form>

<script >
    $(function() {
        $("#data_add_or_edit_form").validate({
            <%-- rules中的loginname, username, pw等等均为输入框name值, 而不是id值 --%>
            rules: {
                name:{
                    required: true,
                    minlength: 2,
                    maxlength: 10
                },
                linkTel:{
                    required: true,
                    number: true,
                    phone: true //phone自定义方法扩展放在additional-methods.js, 在错误信息messages中, 不写phone,
                }
            },
            messages:{
                name:{
                    required: "驾校名称不能为空",
                    minlength: $.validator.format("驾校名称最小长度为{0}个字符"),
                    maxlength: $.validator.format("驾校名称最大长度为{0}个字符")
                },
                linkTel:{
                    required: "联系电话不能为空",
                    number: "联系电话必须为数字"
                    //phone: xxx, 不写错误信息, 错误信息为自定义法phone返回的内容
                }
            },
            highlight: function(element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            success: function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },
            errorPlacement: function(error, element) {
                element.parent('div').append(error);
            }
        });
    });

</script>