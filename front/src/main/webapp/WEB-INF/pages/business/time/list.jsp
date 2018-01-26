<%@ page language="java" contentType="text/html; charset=utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <link href="${ctx}/js/bootstrap-table-1.11.1/bootstrap-editable.css" rel="stylesheet" />
    <%@include file="/WEB-INF/pages/common/head.jsp"%>
</head>
<body>
<%@include file="/WEB-INF/pages/common/navbar.jsp"%>
<div class="container-fluid">
    <div class="panel panel-default">
        <div class="panel-heading panel-heading-custom">
            <%@include file="/WEB-INF/pages/common/menubar.jsp"%>
        </div>

        <div class="panel-body" style="padding-bottom: 15px;">
            <div id="toolbar">
                <form id="data_search_form" class="form-inline" role="form">
                    <div class="form-group">
                        <label for="name">驾校名称:</label>
                        <input type="text" class="form-control" id="name" placeholder="Jane Doe">
                    </div>
                    <div class="form-group">
                        <label for="linkTel">联系电话:</label>
                        <input type="text" class="form-control" id="linkTel" placeholder="18888888888">
                    </div>
                    <div class="form-group">
                        <label for="schoolId">驾校名称: </label>
                        <select id="schoolId" class="form-control">
                            <option value="">全部</option>
                            <c:forEach items="${schoolList}" var="sc">
                                <option value="${sc.id}">${sc.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group" style="float: right">
                        <%@include file="/WEB-INF/pages/common/toolbarScr.jsp"%>

                        <div class="btn-group" role="group">
                            <mytag:btn value="1">
                                <a id="add_opts" class="btn btn-primary" url="${ctx}/business/time/add.html">
                                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                                </a>
                            </mytag:btn>
                        </div>
                    </div>
                </form>
            </div>
            <table id="data_table"></table>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/pages/common/scriptall.jsp"%>
<script type="text/javascript" src="${ctx}/js/bootstrap-table-1.11.1/bootstrap-editable.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap-table-1.11.1/bootstrap-table-editable.js"></script>

<script type="text/javascript">
    $("#data_table").bootstrapTable({
        url: "${ctx}/business/time/list.html",
        sortOrder: "asc",
        columns: [{
            title: '序号', width: '2%',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: 'id', visible: false
        }, {
            field: 'name', title: '名称', width: '15%',
            editable: {
                type: 'text',
                title: '驾校名',
                validate: function (v) {
                    if (!v) {
                        return '驾校名不能为空';
                    }
                }
            }
        }, {
            field: 'link_tel', title: '联系电话', width: '10%',
            editable: {
                type: 'text', mode: 'inline',
                validate: function (v) {
                    if (!v) {
                        return '联系电话不能为空';
                    }
                    if(isNaN(v) || v.length != 11) {
                        return '联系电话只能是11位数字';
                    }
                }
            }
        }, {
            field: 'address', title: '地址', width: '40%',
            editable: {
                type: 'text', mode: 'popup', //default value
                validate: function (v) {
                    if (!v) {
                        return '地址不能为空';
                    }
                    if(v.length > 20) {
                        return '地址长度不能大于20字符';
                    }
                }
            }
        }, {
            field: 'is_examroom', title: '考场', width: '10%',
            editable: {
                type: 'select',
                source:[{value:"1",text:"有考场"},{value:"2",text:"无考场"}]
            }

        }, {
            field: 'status', title: '状态', sortable: true,
            editable: {
                type: 'select',
                source:[{value:"1",text:"审核"},{value:"2",text:"屏蔽"},{value:"3",text:"正常"}]
            }

        }, {
            field: 'opt', title: '操作', width: '5%',
            formatter: function(value, row, index) {
                return ['<mytag:btn value="1"><span id="delete_opts" class="btn btn-danger btn-xs glyphicon glyphicon-trash" aria-hidden="true" title="删除" url="${ctx}/business/onestop/delete.html"></span></mytag:btn>',
                        '<mytag:btn value="1"><span id="forbidden_opts" class="btn btn-warning btn-xs glyphicon glyphicon-wrench" aria-hidden="true" title="屏蔽" url="${ctx}/business/onestop/forbidden.html"></span></mytag:btn>'
                        ].join(' ');
            },
            events: {
                'click #delete_opts': function(e, value, row, index) {
                    var url = $(e.target).attr("url");
                   // BootstrapDialog.myConfirm(url, {id: row.id}, "您确定要删除--" + row.name + "吗? <mark>且删除后无法恢复, 请谨慎操作.</mark>", jumpTrChange);
                },
                'click #forbidden_opts': function(e, value, row, index) {
                    var url = $(e.target).attr("url");
                   // BootstrapDialog.myConfirm(url, {id: row.id}, "您确定要屏蔽--" + row.name + "吗? 请确认...", jumpNoTrChange);
                }
            }
        }],
        onExpandRow: function (index, row, $detail) {
            var dv = $detail.html('<div></div>');
            dv.load(BootstrapDialog.myCreateURL("${ctx}/business/keer/detail.html", {id: row.id}));
        },
        onEditableSave: function (field, row, oldValue, $el) {
            trIndex = $('#data_table').find('tr.warning').data('index'); //记录发生变更的行下标
            if(row.id > 0) { //编辑才有id, 新增没有id, 初始id为-1
                var results = BootstrapDialog.myAjaxReq("${ctx}/business/time/edit.html", row);
                if(BootstrapDialog.mySEShow(results, 2000, true)) {
                    $('#data_table').bootstrapTable('refresh');
                }
            }
        }
    });

    $("#add_opts").on('click', function () {
        var url = $(this).attr("url");
        var getRowByUniqueId = $("#data_table").bootstrapTable('getRowByUniqueId', -1);
        if(getRowByUniqueId) {
            var results = BootstrapDialog.myAjaxReq(url, getRowByUniqueId);
            if(BootstrapDialog.mySEShow(results)) {
                $(this).html('<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增');
                $('#data_table').bootstrapTable('refresh');
            }
        } else {
            $(this).html('<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> 保存');
            $("#data_table").bootstrapTable('insertRow', {
                index: 0,
                row: {
                    id: -1,
                    name: '请填写',
                    link_tel: "18888888888",
                    address: "请填写",
                    is_examroom: 1,
                    status: 2
                }
            });
        }
    });

</script>
</body>
</html>
