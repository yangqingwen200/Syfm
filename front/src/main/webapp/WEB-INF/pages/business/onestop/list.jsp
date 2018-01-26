<%@ page language="java" contentType="text/html; charset=utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <%@include file="/WEB-INF/pages/common/head.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/pages/common/navbar.jsp" %>
<div class="container-fluid">
    <div class="panel panel-default">
        <div class="panel-heading panel-heading-custom">
            <%@include file="/WEB-INF/pages/common/menubar.jsp" %>
        </div>

        <div class="panel-body panel-body-custom">
            <form id="data_search_form" class="form-inline" role="form" action="${ctx}/business/onestop/list.html" method="post">
                <table style="width: 100%">
                    <tr>
                        <td>
                            <div class="form-group">
                                <label for="schoolId">驾校名称: </label>
                                <select id="schoolId" class="form-control" name="schoolId">
                                    <option value="">全部</option>
                                    <c:forEach items="${schoolList}" var="sc">
                                        <option value="${sc.id}" <c:if test="${sc.id eq dto.schoolId}">selected="selected"</c:if>>${sc.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="inputName">驾校名称:</label>
                                <input type="text" class="form-control" id="inputName" name="name" value="${dto.name}" placeholder="Jane Doe">
                            </div>
                            <div class="form-group">
                                <label for="inputLinkTel">联系电话:</label>
                                <input type="text" class="form-control" id="inputLinkTel" name="linkTel" value="${dto.linkTel}" placeholder="18888888888">
                            </div>

                            <div class="form-group">
                                <label for="inputStatus">状态: </label>
                                <select id="inputStatus" class="form-control" name="status">
                                    <option value="">全部</option>
                                    <option value="1" <c:if test="${dto.status eq 1}">selected="selected"</c:if>>审核</option>
                                    <option value="2" <c:if test="${dto.status eq 2}">selected="selected"</c:if>>屏蔽</option>
                                    <option value="3" <c:if test="${dto.status eq 3}">selected="selected"</c:if>>正常</option>
                                </select>
                            </div>
                            <div class="form-group" style="float: right">
                                <div class="btn-group" role="group" style="margin: 0">
                                    <button type="submit" class="btn btn-primary">
                                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span> 搜索
                                    </button>
                                    <button id="data_clear_opts" type="button" class="btn btn-info">
                                        <span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span> 重置
                                    </button>
                                    <button id="data_reload_opts" type="button" class="btn btn-success">
                                        <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> 刷新
                                    </button>
                                </div>
                                <div class="btn-group" role="group" style="margin: 0">
                                    <a <mytag:btn value="onestop/add">id="data_add_opts" class="btn btn-primary" url="${ctx}/business/onestop/add.html" dialogwidth="50%"</mytag:btn> class="btn btn-primary disabled">
                                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                                    </a>
                                    <a <mytag:btn value="onestop/delete">id="data_batdel_opts" class="btn btn-danger" url="${ctx}/business/onestop/delete.html"</mytag:btn> class="btn btn-danger disabled">
                                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 批量删除
                                    </a>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>

                <%-- 分页需要的隐藏数据 --%>
                <%@include file="/WEB-INF/pages/common/pagehidden.jsp" %>
            </form>
            <br>

            <div class="table-responsive">
                <table class="table table-hover table-bordered table-striped">
                    <thead>
                    <tr>
                        <th width="1%"></th>
                        <th width="1%"><input type="checkbox" id="all_choose_checkbox"></th>
                        <th width="3%">序号</th>
                        <th width="13%">驾校名称 <mytag:order col="name"/></th>
                        <th width="12%">联系人电话 <mytag:order col="link_tel"/></th>
                        <th width="10%">城市</th>
                        <th width="7%">好评率 <mytag:order col="star_num"/></th>
                        <th width="7%">有无考场</th>
                        <th width="5%">状态</th>
                        <th width="30%">地址</th>
                        <th width="7%">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${page.content.size() gt 0}">
                            <c:forEach items="${page.content}" var="content" varStatus="i">
                                <tr <c:if test="${content.id eq dto.updateId}">class="warning"</c:if>>
                                    <td>
                                        <span <mytag:btn value="keer/destail">id="data_detail_opts_${i.index + 1}" class="glyphicon glyphicon-plus span-plus"
                                           url="${ctx}/business/keer/detail.html" urldata="{id: ${content.id}}" title="查看详情"</mytag:btn> class="glyphicon glyphicon-plus span-plus-disable"></span>
                                    </td>
                                    <td><input type="checkbox" class="checkbox" value="${content.id}"/></td>
                                    <td>${(page.pageNow - 1) * page.pageSize + i.index + 1}</td>
                                    <td>${content.name}</td>
                                    <td>${content.link_tel}</td>
                                    <td>${content.city_name}</td>
                                    <td>${content.star_num}</td>
                                    <td style="vertical-align: middle">
                                        <c:if test="${content.status eq 1}"><span class="label label-warning">审核</span></c:if>
                                        <c:if test="${content.status eq 2}"><span class="label label-danger">屏蔽</span></c:if>
                                        <c:if test="${content.status eq 3}"><span class="label label-success">正常</span></c:if>
                                    </td>
                                    <td>${content.is_examroom}</td>
                                    <td><mytag:moreFmt value="${content.address}" subLength="20" fmtType="abbr"/></td>
                                    <td>
                                        <a <mytag:btn value="onestop/edit">id="data_edit_opts_${i.index + 1}" class="btn btn-primary btn-xs"
                                                url="${ctx}/business/onestop/edit.html" urldata="{id: ${content.id}}" dialogwidth="50%"</mytag:btn> class="btn btn-primary btn-xs disabled">
                                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 编辑
                                        </a>
                                        <a <mytag:btn value="onestop/deletes">id="data_del_opts_${i.index + 1}" class="btn btn-danger btn-xs"
                                                url="${ctx}/business/onestop/delete.html" urldata="{id: ${content.id}}"</mytag:btn> class="btn btn-danger btn-xs disabled">
                                            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 删除
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="11" style="text-align: center;">没有查询到数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                    </tbody>
                </table>
            </div>
        </div>

        <div class="panel-footer panel-footer-custom">
            <%--分页栏--%>
            <%@include file="/WEB-INF/pages/common/pagenav.jsp" %>
        </div>
    </div>
</div>

<%-- 编辑和删除对话框 --%>
<%@include file="/WEB-INF/pages/common/scriptall.jsp" %>
<script type="text/javascript" src="${ctx}/js/commonpc.js"></script>
</body>
</html>