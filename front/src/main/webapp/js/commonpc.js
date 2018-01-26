/**
 * Array 原型扩展方法: removeValue
 * @param val 要删除的值
 */
Array.prototype.removeValue = function(val) {
    for(var i=0; i<this.length; i++) {
        if(this[i] === val) {
            this.splice(i, 1);
            break;
        }
    }
};

//用来保存选中的id
var idsArray = new Array();

/**
 * 点击分页栏指定页码调用的方法
 */
$("#page_forward").on("click", "a[id^='page_forward_']", function () {
    $("#page_now").val($(this).attr("value"));
    $("#data_search_form").submit();
});

/**
 * 改变每页的大小
 */
$("#page_forward_setpagesize").on("change", function () {
    $("#page_size").val($(this).val());
    $("#data_search_form").submit();
});

/**
 * 输入指定页码调用的方法, 绑定回车事件
 */
$("#page_forward_go_page").on("keydown", function(event){
    if(event.keyCode === 13){ //绑定回车
        var pageNow = $(this).val();
        var pageCount = $(this).attr("pageCount");
        if(isNaN(pageNow) || pageNow <= 0) {
            pageNow = 1;
        }
        $("#page_now").val(Number(pageNow) <= Number(pageCount) ? pageNow : pageCount);
        $("#data_search_form").submit();
    }
});

/**
 * 点击表头排序, 分页排序用, 与自定义标签OrderTag配合使用.
 */
$("table thead tr th").on('click', "span[id^='span_order_']", function () {
    $("#page_order_attr").val($(this).attr("column"));
    $("#page_order_type").val($(this).attr("ordertype"));
    $("#data_search_form").submit();
});

/**
 * 勾选表头的复选框
 */
$("#all_choose_checkbox").on('click', function () {
    //清空装有id的数组, 避免用户先勾选全部选中, 然后再取消勾选每行的复选框, 最后又点击勾选全部选中.
    idsArray.splice(0, idsArray.length);

    //attr获取checked是undefined, attr是获取自定义属性的值
    //prop修改和读取dom原生属性的值
    var status = $(this).prop("checked");
    var inchk = $("input.checkbox");
    inchk.prop("checked", status);
    if(status) {
        $.each(inchk, function (i, n) {
            idsArray.push($(this).val());
        });
    }
});

/**
 * 单击行, 改变行颜色
 */
$("table tbody").on('click', "tr", function (event) {
    //IE下,event对象有srcElement属性,但是没有target属性;Firefox下,event对象有target属性,但是没有srcElement属性.但他们的作用是相当的
    var evtTarget = event.target || event.srcElement;
    var tagName = evtTarget.tagName;
    if(tagName !== 'INPUT' && tagName !== 'SPAN') {
        $(this).siblings().removeClass("warning");
        $(this).addClass("warning");

        clearCheckboxAndIds();
        var input = $(this).find("input[type='checkbox']");
        $(input).prop("checked", true);
        idsArray.push($(input[0]).val());
    }
});

/**
 * 点击最前面的 + 或 - 号
 */
$("table tbody tr td").on("click", "span[id^='data_detail_opts_']", function () {
    var tr = $(this).parent().parent(); //得到当前行
    var cls = $(this).attr("class");
    if(cls.indexOf("glyphicon-plus") !== -1) {
        var url = $(this).attr("url");
        var urlData = stringToObject($(this).attr("urldata")); //url传递参数, 格式: {id:123}
        if(typeof(urlData) === "boolean") {
            return;
        }
        var nextAllSize = $(this).parent().nextAll().length + 1; //得到所有的td个数,方便跨列处理
        var newTr = $("<tr><td colspan=" + nextAllSize + "><div></div></td><tr>");
        $(tr).after(newTr); //在当前行下面添加一行
        newTr.find("div").load(BootstrapDialog.myCreateURL(url, urlData));
        $(this).removeClass("glyphicon-plus").addClass("glyphicon-minus");

    } else if(cls.indexOf("glyphicon-minus") !== -1) {
        tr.next().remove(); //移除当前行的下一行
        $(this).removeClass("glyphicon-minus").addClass("glyphicon-plus");
    }
});

/**
 * 单个勾选复选框
 */
$("table tbody tr td").on('click', "input[type='checkbox']",function () {
    //判断选中的元素 和 .checkbox总数是否相等
    var status = $("input.checkbox:checked").length === $("input.checkbox").length;
    $("#all_choose_checkbox").prop("checked", status);

    var isChecked = $(this).prop("checked");
    if(isChecked) {
        idsArray.push($(this).val());
    } else {
        idsArray.removeValue($(this).val());
    }
});

/**
 * 点击重置按钮
 */
$("#data_clear_opts").on("click", function () {
    var dataForm = $("#data_search_form");
    dataForm.find("input[type!='hidden'],select").val(""); //清空input select控件值
    $("#page_now").val(1);
    $("#update_id").val("");
    dataForm.submit();  //重新刷新当前页面
});

/**
 * 点击刷新按钮
 */
$("#data_reload_opts").on("click", function () {
    $("#page_now").val(calculationPageNow(false));
    $("#data_search_form").submit();  //重新刷新当前页面
});

/**
 * 点击删除按钮
 */
$("table tbody tr td a[id^='data_del_opts_']").on("click", function (event) {
    clearCheckboxAndIds();
    var delUrl = $(this).attr("url");
    var urlData = stringToObject($(this).attr("urldata"));
    if(typeof(urlData) === "boolean") {
        return;
    }
    runDoSomething(delUrl, urlData, "您确认要删除选中的信息吗? <mark>且删除后无法恢复, 请谨慎操作</mark>", true);
});

/**
 * 点击批量删除按钮
 */
$("#data_batdel_opts").on("click", function () {
    var delUrl = $(this).attr("url");
    if(idsArray.length <= 0) {
        PNotify.myTips("提示", "请至少选择一条数据", "", 1000);
        return;
    }
    runDoSomething(delUrl, {id: idsArray.join(",")}, "您确认要删除选中的信息吗? <mark>且删除后无法恢复, 请谨慎操作</mark>", true);
});

/**
 * 新增/编辑对话框
 */
$("#data_add_opts,table tbody tr td a[id^='data_edit_opts_']").on("click", function (event) {
    var addUrl = $(this).attr("url");
    var width = $(this).attr("dialogwidth"); //改变弹出编辑框的大小: 60px or 60%
    var data = stringToObject($(this).attr("urldata")); //url传递参数, 格式: {id:123}
    if(typeof(data) === "boolean") {
        return;
    }
    BootstrapDialog.myAddEditShow({
        url: addUrl,
        param: data,
        title: $(this).html().trim(),
        dialogSize: width,
        showTips: false,
        doSomething: function () {
            $("#update_id").val(data.id);
            $("#page_now").val(calculationPageNow(false));
            $("#data_search_form").submit();  //重新刷新当前页面
        }
    });
});

/**
 * 清除选中的复选框和装有id的数组
 * 这种情况是勾选了复选框, 但是点击的是行内编辑 or 删除按钮
 */
function clearCheckboxAndIds() {
    $("#all_choose_checkbox").prop("checked", false); //去掉全选复选框的勾
    $("td input.checkbox").prop("checked", false); //去掉单个复选框的勾
    idsArray.splice(0, idsArray.length); //清空装有id的数组
}

/**
 * 拼接URL
 * @param url
 * @param param
 * @returns 拼接之后URL
 */
function createURL(url, param){
    //加上随机数, 避免IE浏览器使用缓存
    url = url += "?random=" + Date.now() + "&";
    for(var name in param){
        if(param.hasOwnProperty(name)) {
            url += (name + "=" + param[name] + "&");
        }
    }
    return url.substr(0, url.length - 1);
}

/**
 * 同步提交请求信息
 *
 * @param url 请求地址
 * @param data 请求提交的数据
 * @returns {*}
 */
function ajaxRequestPost(url, data) {
    var result;
    $.ajax({
        url: url,
        data: data,
        dataType: "json",
        type: "post",
        async: false,
        cache: false,
        success: function(data) {
            result = data;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            result = {message: false, errorMsg: '请求出错.'};
        }
    });
    return result;
}

/**
 * 计算当前页
 * @param flag 是否有行数发生变化, 如: 删除就有, 编辑或者禁用就没有
 * @param pageNow
 * @returns {jQuery|*}
 */
function calculationPageNow(flag, pageNow) {
    pageNow = pageNow || $("ul.pagination li.active a").attr("value");
    if(flag) {
        var pageSize = $("#page_size").val();
        var totalNum = $("#page_totalNum").val();
        var lastPage = $("#page_lastPage").val();
        var arrLen = idsArray.length; //得到数据长度
        if((pageNow === lastPage && totalNum % pageSize === 1) ||
            (arrLen !== 0 && (arrLen === Number(pageSize) || totalNum % pageSize === arrLen))) {
            pageNow = pageNow - 1; //当前位置在最后一页 && 当前页只有一条数据, 删除这条数据应该跳到上一页
            if(pageNow <= 0) {
                pageNow = 1;
            }
        }
    }
    return pageNow;
}

/**
 *
 * @param url 请求url
 * @param param 参数
 * @param message 提示内容
 * @param flag
 */
function runDoSomething(url, param, message, flag) {
    BootstrapDialog.myConfirm({
        url: url,
        param: param,
        message: message,
        showTips: false,
        doSomething: function () {
            $("#page_now").val(calculationPageNow(flag));
            $("#data_search_form").submit();  //重新刷新当前页面
        }

    });
}

/**
 * string 转js 对象
 * @param data
 * @returns {*}
 */
function stringToObject(data) {
    try {
        if(data) {
            data = eval("("+data+")");
        }
        return data;
    } catch (e) {
        BootstrapDialog.myShow({
            title: "<span class='glyphicon glyphicon-remove' aria-hidden='true'> 错误",
            message: "JS eval解析: " + data + "出现错误了...",
            type: BootstrapDialog.TYPE_DANGER,
            size: BootstrapDialog.SIZE_SMALL
        });
        return false;
    }
}
