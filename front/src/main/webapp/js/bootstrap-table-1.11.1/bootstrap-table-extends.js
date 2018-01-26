(function (win, $) {
    /**
     * 用来记录行的下标, 当行发生变更, 用来记录当前行号
     * 刷新之后, 友好的显示是哪行发生变更
     */
    if(typeof win.trIndex === "undefined") {
        win.trIndex = 0;
    }

    $.extend($.fn.bootstrapTable.defaults, {
        method: 'post',
        contentType: 'application/x-www-form-urlencoded',
        sortName: "id",  //初始排序字段
        sortOrder: "desc",  //初始排序类型: 正序 or 倒序
        idField: "id",  //指定主键列
        uniqueId: "id", //每一行的唯一标识，一般为主键列
        clickToSelect: true, //是否启用点击选中行
        striped: true, //是否显示行间隔色
        //toolbar: "toolbar",  //一个jQuery 选择器，指明自定义的toolbar（工具栏），将需要的功能放置在表格工具栏（默认）位置。
        sidePagination: "server",  //设置在哪里进行分页，可选值为 'client' 或者 'server'。设置 'server'时，必须设置 服务器数据地址（url）或者重写ajax方法
        pageNumber: 1,    //如果设置了分页，首页页码
        pageSize: 15,   //如果设置了分页，页面数据条数
        pageList: [
            10, 15, 20, 30, 40, 50   //如果设置了分页，设置可供选择的页面数据条数。设置为All 则显示所有记录。
        ],
        pagination: true,  //设置为 true 会在表格底部显示分页条
        detailView: true, //最前显示 + 号(父子表), 当点击+号, 触发onExpandRow事件
        queryParams: queryParams,
        onSort: function (name, order) { //Fires when user sort a column
            this.pageNumber = 1;
        },
        onClickRow: function (row, $element) {
            //单击行使用始终只能勾选一行, 如果想勾选多行, 点击checkbox
            //如果想只能选择一行,直接添加属性singleSelect:true即可
            $("#data_table").bootstrapTable("uncheckAll");
        },
        onLoadError: function (status) {
            BootstrapDialog.myAlert("错误", "哦豁, 与服务器失去联系...");
        },
        onExpandRow: function (index, row, $detail) { //父子表展开详情请求的url 以及 参数
            $detail.html('<div></div>').load(BootstrapDialog.myCreateURL(this.detailViewUrl, {id: row.id}), function(response,status,xhr){
                if("error" === status) {
                    BootstrapDialog.myAlert("错误", "请求的地址找不到...");
                }
            });
        },
        onAll: function (name, args) { //所有事件执行完毕之后都会执行这个方法
            if(name === "load-success.bs.table") { //执行成功(onLoadSuccess)事件后, 在执行判断后台返回的逻辑.
                if(args && args.length > 0) {
                    var arg = args[0];
                    if(arg.hasOwnProperty("errorMsg") && arg.hasOwnProperty("message") && !arg['message']) {
                        document.write(arg['errorMsg']); //打印登录超时或者没有权限信息
                        return;
                    }
                }
            }
            if(name === "post-body.bs.table") {
                if(win.trIndex || win.trIndex === 0) { //trIndex有值说明是修改了数据 (如: 下架/置顶等等操作)
                    $('#data_table').find("tr[data-index="+ win.trIndex + "]").addClass('info'); //当前行变色, 增加样式
                    win.trIndex = undefined;  //重新置为undefined, 避免翻页/下一页还会选中
                }
                if(this.detailView) { //检查是否查看详情的权限
                    if(this.detailViewPin !== 'y') {
                        //禁用父子表最前面的加号
                        $('#data_table').find('tbody tr td a[class="detail-icon"]').unbind().removeAttr('href').addClass('not-allowed');
                    }
                }
            }
        }
    });

    function queryParams(params) {
        var temp = {
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            sort: params.sort,
            order: params.order,
            _random: Date.now()
        };

        getToolbarValue(temp);

        return temp;
    }

    function getToolbarValue(temp) {
        var searchCondition = $("#toolbar input,select");
        for (var i = 0; i < searchCondition.length; i++) {
            var value = $(searchCondition[i]).val();
            var key = searchCondition[i].id;
            if (value !== "") {
                temp[key] = value;
            } else {
                delete temp[key];
            }
        }
    }

    /**
     * 点击选中行，改变选中行的背景颜色<br>
     * 会先执行onClickRow事件, 再执行这里的绑定的事件<br>
     * 以下代码块也可以放在bootstrapTable.defaults的 onClickRow事件中<br>
     *     但是如果别人重写了onClickRow, 单击行变色代码就会丢失(除非再写一遍), 所以使用click-row.bs.table来绑定
     *
     */
    $("#data_table").on('click-row.bs.table', function (e, row, element){
        $('.info').removeClass('info');//去除之前选中的行的，选中样式
        $(element).addClass('info');//添加当前选中的 success样式用于区别
        //var index = $('#formTempDetailTable_new').find('tr.success').data('index');//获得选中的行的id
    });
})(window, jQuery);
