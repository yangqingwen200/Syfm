(function (win, $, pn, bd) {

    /**
     * toolbar 精确搜索
     */
    $("#toolbar_search_table").on("click", function () {
        //在查询按钮刷新table发出数据请求时，将页码刷新回到1
        //$('#data_table').bootstrapTable('refreshOptions',{pageNumber:1}); //结合行编辑模式(Bootstrap Editable插件), 会有问题
        $('#data_table').bootstrapTable('refresh', {pageNumber: 1});
    });

    /**
     * toolbar 清除搜索框中的值
     */
    $("#toolbar_clear_table").on("click", function () {
        $("#toolbar input,select").val("");
        //在查询按钮刷新table发出数据请求时，将页码刷新回到1
        //$('#data_table').bootstrapTable('refreshOptions',{pageNumber:1});  //结合行编辑模式(Bootstrap Editable插件), 会有问题
        $('#data_table').bootstrapTable('refresh', {pageNumber: 1});
    });

    /**
     * toolbar 刷新
     */
    $("#toolbar_refresh_table").on("click", function () {
        $('#data_table').bootstrapTable('refresh');
    });

    /**
     * toolbar 新增
     */
    $("#toolbar_add_opts").on('click', function () {
        var url = $(this).attr("url");
        var dialogwidth = $(this).attr("dialogwidth");
        var init = addInitData(); //初始化需要传递到后台的数据
        bd.myAddEditShow({
            url: url,
            param: init,
            title: "<span class='glyphicon glyphicon-plus' aria-hidden='true'></span><span class='dialog-title'>新增</span>",
            dialogSize: dialogwidth,
            doSomething: jumpTrChange,
            showTips: true
        });
    });

    /**
     * toolbar 编辑
     */
    $("#toolbar_edit_opts").on('click', function () {
        var url = $(this).attr("url");
        var dialogwidth = $(this).attr("dialogwidth");
        var $dataTable = $("#data_table");
        var sel = $dataTable.bootstrapTable("getSelections");
        if(sel.length !== 1) {
            pn.myTips("提示", "请选择一条数据, 已选择" + sel.length + "条", "", 1000);
            return;
        }
        var tableOptions = $dataTable.bootstrapTable("getOptions");
        var idField = tableOptions.idField;
        var init = editInitData(sel[0], idField);
        if(!init) {
            bd.myAlert("错误", "操作请求到后台数据为空, 请开发人员检查!");
            return;
        }
        if(typeof init !== 'object') {
            bd.myAlert("错误", "操作请求到后台数据不是对象, 请开发人员检查!");
            return;
        }
        bd.myAddEditShow({
            url: url,
            param: init,
            title: "<span class='glyphicon glyphicon-edit' aria-hidden='true'></span><span class='dialog-title'>编辑</span>",
            doSomething: jumpNoTrChange,
            dialogSize: dialogwidth,
            showTips: true
        });
    });

    /**
     * toolbar 删除/禁用
     */
    $("#toolbar_delete_opts").on('click', function (event) {
        reallyDo(this, jumpTrChange);
    });

    win.reallyDo = function (targetDom, dosomething) {
        var $2 = $(targetDom);
        var url = $2.attr("url");
        var title = $2.attr("title");
        var urlData = $2.attr("urlData");

        title = title || "操作";
        if(typeof url === "undefined") {
            bd.myAlert("错误", "操作请求的url未定义, 请开发人员检查.");
            return;
        }
        try {
            urlData = eval("(" + urlData + ")") || {};
        } catch (e) {
            bd.myAlert("错误", "JS eval解析: " + urlData + "出错, 请开发人员检查.");
            return;
        }
        var $dataTable = $("#data_table");
        var sel = $dataTable.bootstrapTable("getSelections");
        if(sel.length === 0) {
            pn.myTips("提示", "请至少选择一条数据", "", 1000);
            return;
        }
        var tableOptions = $dataTable.bootstrapTable("getOptions");
        var idField = tableOptions.idField;
        var ids = [];
        var names = [];
        for (var i=0; i<sel.length; i++) {
            var obj = sel[i];
            var primary = getInitDataId(obj, idField);
            if(primary) {
                ids.push(primary);
            }
            var tip = getInitDataTips(obj);
            if(tip) {
                names.push(tip);
            }
        }
        if(ids.length === 0) {
            bd.myAlert("错误", "传递到后台主键数组为空, 请开发人员检查.");
            return;
        }
        bd.myConfirm({
            url: url,
            param: $.extend({id: ids.join(",")}, urlData),
            message: "您确定要" + title + " " + names.join(", ") + "吗?",
            doSomething: dosomething,
            showTips: true
        });
    };

    /**
     * 显示列表行有数据删减的刷新表格, 如: 删除
     */
    win.jumpTrChange = function () {
        var $dataTable = $("#data_table");
        var getSelections = $dataTable.bootstrapTable("getSelections");
        var tableOptions = $dataTable.bootstrapTable("getOptions");
        var pageNumber = tableOptions.pageNumber; //当前页数
        var pageSize = tableOptions.pageSize; //页面大小
        var totalPages = tableOptions.totalPages; //总页数
        var totalRows = tableOptions.totalRows; //总条数
        if(pageNumber === totalPages && (totalRows % pageSize === 1 || totalRows % pageSize === getSelections.length)) {
            $dataTable.bootstrapTable('refresh',{pageNumber: pageNumber > 1 ? pageNumber - 1 : 1});
        } else {
            $dataTable.bootstrapTable('refresh');  //刷新表格
        }
    };

    /**
     * 显示列表行没有数据删减的刷新表格, 如: 下架/置顶
     */
    win.jumpNoTrChange = function () {
        var $dataTable = $('#data_table');
        win.trIndex = $dataTable.find('tr.info').data('index'); //记录发生变更的行下标
        $dataTable.bootstrapTable('refresh');  //刷新表格, 停留在当前页
    };

    /**
     * 新增需要传递到后台的数据
     *     可以根据每个页面的需求不同, 在jsp定义相同方法即可(重写/覆盖)
     * @returns {{}} 需要传递到后台数据: {id: 123, name: '456'}
     */
    win.addInitData = function () {
        return {};
    };

    /**
     * 编辑需要传递到后台的数据
     * 可以根据每个页面的需求不同, 在jsp定义相同方法即可(重写/覆盖)
     * 有些数据库字段主键可能不是id, 传递到后台就不一样, 重写该方法即可
     * @param sel 行对象
     * @param idField 主键
     * @returns {{id}} 需要传递到后台数据: {id: 123, name: '456'}
     */
    win.editInitData = function (sel, idField) {
        if(sel.hasOwnProperty(idField)) {
            return {id: sel[idField]};
        }
    };

    /**
     * 删除/禁用取得当前行主键
     * 如果主键不是id, 重写该方法即可
     * @param sel 行记录
     * @param idField 主键
     */
    win.getInitDataId = function (sel, idField) {
        if(sel.hasOwnProperty(idField)) {
            return sel[idField];
        }
    };

    /**
     * 删除/禁用取得当前行 提示信息: 姓名 / 手机号码
     * 如果提示信息不是name, 重写该方法即可.
     * @param sel
     */
    win.getInitDataTips = function (sel) {
        if(sel.hasOwnProperty("name")) {
            return sel.name;
        }
    };

})(window, jQuery, PNotify, BootstrapDialog);