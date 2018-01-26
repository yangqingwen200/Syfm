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
                        <label for="linkMan">联系人: </label>
                        <input type="text" class="form-control" id="linkMan" placeholder="张三">
                    </div>
                    <div class="form-group">
                        <label for="linkTel">联系电话: </label>
                        <input type="text" class="form-control" id="linkTel" placeholder="18888888888">
                    </div>
                    <div class="form-group">
                        <label for="address">联系地址: </label>
                        <input type="text" class="form-control" id="address" placeholder="哈尔滨市南岗区">
                    </div>
                    <div class="form-group">
                        <label for="status">状态: </label>
                        <select id="status" class="form-control">
                            <option value="">全部</option>
                            <option value="3">正常</option>
                            <option value="1">审核</option>
                            <option value="2">屏蔽</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="isExamroom">考场: </label>
                        <select id="isExamroom" class="form-control">
                            <option value="">全部</option>
                            <option value="1">有考场</option>
                            <option value="2">无考场</option>
                        </select>
                    </div>
                </form>

                <div class="toolbar-crud">
                    <button <mytag:btn value="onestop/add">id="toolbar_add_opts" class="btn btn-info btn-sm" url="${ctx}/business/onestop/add.html" title="新增" dialogwidth="60%"</mytag:btn> class="btn btn-info btn-sm disabled">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button <mytag:btn value="onestop/edit">id="toolbar_edit_opts" class="btn btn-warning btn-sm" url="${ctx}/business/onestop/edit.html" title="编辑" dialogwidth="60%"</mytag:btn> class="btn btn-warning btn-sm disabled">
                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 编辑
                    </button>
                    <button <mytag:btn value="onestop/delete">id="toolbar_delete_opts" class="btn btn-danger btn-sm" url="${ctx}/business/onestop/delete.html" title="删除"</mytag:btn> class="btn btn-danger btn-sm disabled">
                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 删除
                    </button>
                    <div class="btn-group">
                        <button <mytag:btn value="onestop/forbidden">id="toolbar_forbidden_opts_forbidden" class="btn btn-default btn-sm" url="${ctx}/business/onestop/forbidden.html" urldata="{status: 2}" title="屏蔽"</mytag:btn> class="btn btn-default btn-sm disabled">
                            <span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span> 屏蔽
                        </button>
                        <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="height: 30px;padding-right: 3px;padding-left: 3px;">
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li>
                                <a ${mytag:chkpistwo("onestop/forbidden", "href='#' id='toolbar_forbidden_opts_normal' urldata='{status: 3}' title='正常'", "style='cursor: not-allowed'")} url="${ctx}/business/onestop/forbidden.html">
                                    <span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span> 正常
                                </a>
                            </li>
                            <li>
                                <a ${mytag:chkpistwo("onestop/forbidden", "href='#' id='toolbar_forbidden_opts_check' urldata='{status: 1}' title='审核'", "style='cursor: not-allowed'")} url="${ctx}/business/onestop/forbidden.html">
                                    <span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span> 审核
                                </a>
                            </li>
                        </ul>
                    </div>
                    <%@include file="/WEB-INF/pages/common/toolbarScr.jsp"%>
                </div>
            </div>
        </div>
    </div>
    <table id="data_table"></table>
</div>

<%@include file="/WEB-INF/pages/common/scriptall.jsp"%>

<script type="text/javascript">
    $("#data_table").bootstrapTable({
        url: "${ctx}/business/kesan/list.html",
        sortOrder: "asc",
        columns: [{
            checkbox: true, width: '2%', valign: 'middle'
        }, {
            title: '序号', width: '2%',
            formatter: function (value, row, index) {return index + 1;}
        }, {
            field: 'id', visible: false
        }, {
            field: 'name', title: '名称', width: '9%'
        }, {
            field: 'link_tel', title: '手机电话', width: '8%'
        }, {
            field: 'tel', title: '座机', width: '8%'
        }, {
            field: 'link_man', title: '联系人', width: '8%'
        }, {
            field: 'city_name', title: '城市', width: '5%'
        }, {
            field: 'star_num', title: '评分', sortable: true, width: '3%'
        }, {
            field: 'address', title: '地址', width: '25%',
            formatter: function (value, row, index) {
                return '<abbr title="' + value + '">' + value + '</abbr>';
            }
        }, {
            field: 'is_examroom', title: '考场', width: '5%',
            formatter: function (value, row, index) {
                return value === 1 ? '有考场' : '无考场';
            }
        }, {
            field: 'lng', title: '经度', width: '5%'
        }, {
            field: 'lat', title: '维度', width: '5%'
        }, {
            field: 'status', title: '状态', sortable: true, formatter: statusFmt, width: '3%', valign: 'middle'
        }, {
            field: 'create_date', title: '创建时间', width: '10%'
        }],
        detailViewPin: "${mytag:chkpis('keer/detail')}", //check view detail permission
        detailViewUrl: "${ctx}/business/keer/detail.html" //detail request url  onExpandRow
    });

    function statusFmt(value, row, index) {
        if(value === 1) {
            return "<span class='label label-warning'>审核</span>";
        }
        if(value === 2) {
            return "<span class='label label-danger'>屏蔽</span>";
        }
        if(value === 3) {
            return "<span class='label label-success'>正常</span>";
        }
    }

    $("#toolbar_forbidden_opts_forbidden,#toolbar_forbidden_opts_normal,#toolbar_forbidden_opts_check").on('click', function (event) {
        reallyDo(this, jumpNoTrChange);
    });

</script>
</body>
</html>
