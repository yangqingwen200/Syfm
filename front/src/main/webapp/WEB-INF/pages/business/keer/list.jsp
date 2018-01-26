<%@ page language="java" contentType="text/html; charset=utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <%@include file="/WEB-INF/pages/common/head.jsp"%>
</head>
<body>
<%@include file="/WEB-INF/pages/common/navbar.jsp"%>
<div class="container-fluid">
    <div class="panel panel-custom">
        <div class="panel-heading panel-heading-custom">
            <%@include file="/WEB-INF/pages/common/menubar.jsp"%>
        </div>

        <div class="panel-body panel-body-custom">
            <div id="toolbar">
                <form id="data_search_form" class="form-inline" role="form">
                    <div class="form-group">
                        <label for="name">驾校名称: </label>
                        <input type="text" class="form-control" id="name" placeholder="Jane Doe">
                    </div>
                    <div class="form-group">
                        <label for="linkTel">联系电话: </label>
                        <input type="text" class="form-control" id="linkTel" placeholder="18888888888">
                    </div>
                </form>

                <div class="toolbar-crud">
                    <button <mytag:btn value="onestop/add">id="toolbar_add_opts" class="btn btn-info btn-sm" url="${ctx}/business/onestop/add.html" title="新增" dialogwidth="60%"</mytag:btn> class="btn btn-default btn-sm disabled">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button <mytag:btn value="onestop/delete">id="toolbar_delete_opts" class="btn btn-danger btn-sm" url="${ctx}/business/onestop/delete.html" title="删除"</mytag:btn> class="btn btn-danger btn-sm disabled">
                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 批量删除
                    </button>
                    <%@include file="/WEB-INF/pages/common/toolbarScr.jsp"%>
                </div>
            </div>
        </div>
    </div>
    <table id="data_table"></table>
</div>

<%@include file="/WEB-INF/pages/common/scriptall.jsp"%>
<script type="text/javascript">

    var optColumn = function () {
        return {
            field: 'opt', title: '操作', width: '5%',
            formatter: function(value, row, index) {
                var detail = '<span <mytag:btn value="keer/detail">id="detail_opts" class="glyphicon glyphicon-search pointer" title="浏览" dialogSize="80%" url="${ctx}/business/keer/detail.html"</mytag:btn> class="glyphicon glyphicon-search not-allowed"></span>';
                var edit = '<span <mytag:btn value="onestop/edit">id="edit_opts" class="glyphicon glyphicon-pencil pointer" title="编辑" dialogSize="50%" url="${ctx}/business/onestop/edit.html"</mytag:btn> class="glyphicon glyphicon-pencil not-allowed"></span>';
                var del = '<span <mytag:btn value="onestop/deletes">id="delete_opts" class="glyphicon glyphicon-trash pointer" title="删除" url="${ctx}/business/onestop/delete.html"</mytag:btn> class="glyphicon glyphicon-trash not-allowed"></span>';

                return [detail,edit,del].join('&nbsp;&nbsp;');
            },
            events: {
                'click #detail_opts': function(e, value, row, index) {
                    var url = $(this).attr("url");
                    var dialogSize = $(this).attr("dialogSize");
                    BootstrapDialog.myAjaxShow({url: url, param: {id: row.id}, dialogSize: dialogSize});
                },
                'click #edit_opts': function(e, value, row, index) {
                    var url = $(this).attr("url");
                    var dialogSize = $(this).attr("dialogSize");
                    BootstrapDialog.myAddEditShow({url:url, param: {id: row.id}, doSomething: jumpNoTrChange, dialogSize: dialogSize});
                },
                'click #delete_opts': function(e, value, row, index) {
                    var url = $(this).attr("url");
                    BootstrapDialog.myConfirm({url:url, param:{id: row.id}, message:"您确定要删除<mark>" + row.name + "</mark>吗? <mark>且删除后无法恢复, 请谨慎操作.</mark>", doSomething: jumpTrChange});
                }
            }
        }
    };

    $("#data_table").bootstrapTable({
        url: "${ctx}/business/keer/list.html",
        sortOrder: "asc",
        columns: [{
            checkbox: true, width: '2%', valign: 'middle'
        }, {
            title: '序号', width: '2%',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: 'id', visible: false
        }, {
            field: 'name', title: '名称', width: '15%'
        }, {
            field: 'link_tel', title: '联系电话', width: '10%'
        }, {
            field: 'address', title: '地址', width: '40%',
            formatter: function (value, row, index) {
                return '<abbr title="' + value + '">' + value + '</abbr>';
            }
        }, {
            field: 'is_examroom', title: '考场', width: '10%'
        }, {
            field: 'status', title: '状态', sortable: true, formatter: statusFmt
        }, optColumn()]
    });

    function statusFmt(value, row, index) {
        if(value === 1) {
            return "<span style='color: blue'>审核</span>";
        }
        if(value === 2) {
            return "<span style='color: red'>屏蔽</span>";
        }
        if(value === 3) {
            return "<span style='color: green'>正常</span>";
        }
    }
</script>
</body>
</html>
