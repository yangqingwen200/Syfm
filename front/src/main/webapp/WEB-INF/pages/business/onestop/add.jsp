<%@ page language="java" contentType="text/html; charset=utf-8" %>

<form id="data_add_or_edit_form" class="form-horizontal" action="${ctx}/business/onestop/add.html" method="post">
    <div class="form-group">
        <label for="inputName" class="col-sm-2 control-label">驾校名称</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="inputName" placeholder="请输入驾校名称" name="name">
        </div>
    </div>
    <div class="form-group">
        <label for="inputLinkTel" class="col-sm-2 control-label">联系电话</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="inputLinkTel" placeholder="请输入联系电话" name="linkTel" maxlength="11">
        </div>
    </div>
    <div class="form-group">
        <label for="inputBelong" class="col-sm-2 control-label">所在地</label>
        <div class="col-sm-10">
            <select id="inputBelong" name="belong" class="form-control" name="belong">
                <option value="">请选择</option>
                <option value="001">北京</option>
                <option value="002">上海</option>
                <option value="003">深圳</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="inputExamRoom" class="col-sm-2 control-label">有无考场</label>
        <div class="col-sm-10">
            <label class="radio-inline">
                <input type="radio" name="examRoom" id="inputExamRoom" value="option1" checked="checked"> 有考场
            </label>
            <label class="radio-inline">
                <input type="radio" name="examRoom" id="inputExamRoom1" value="option2"> 无考场
            </label>
        </div>
    </div>
    <div class="form-group">
        <label for="inlineCheckbox1" class="col-sm-2 control-label">学员评价</label>
        <div class="col-sm-10" style="float: left;margin-top: 6px;">
            <%-- 由于没有使用:class="form-control", 错误信息 "请选择评价" 会显示在右边, 因为form-control会独占一行 --%>
            <input type="checkbox" id="inlineCheckbox1" name="pingjia" value="1"> 热情&nbsp;&nbsp;&nbsp;
            <input type="checkbox" id="inlineCheckbox1" name="pingjia" value="2"> 教学好&nbsp;&nbsp;
            <input type="checkbox" id="inlineCheckbox1" name="pingjia" value="3"> 拿证快&nbsp;&nbsp;
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
                    minlength: 2
                },
                linkTel:{
                    required: true,
                    number: true,
                    phone: true //phone自定义方法扩展放在additional-methods.js, 在错误信息messages中, 不写phone,
                },
                belong:{
                    required: true
                },
                pingjia:{
                    required: true
                }
            },
            messages:{
                name:{
                    required: "驾校名称不能为空",
                    minlength: $.validator.format("驾校名称最小长度为{0}个字符")
                },
                linkTel:{
                    required: "联系电话不能为空",
                    number: "联系电话必须为数字"
                    //phone: xxx, 不写错误信息, 错误信息为自定义法phone返回的内容
                },
                belong:{
                    required: "请选择所在地"
                },
                pingjia:{
                    required: "请选择评价"
                }
            },
            highlight: function(element) {
                //closest: 选择器的第一个祖先元素，从当前元素开始沿 DOM 树向上
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