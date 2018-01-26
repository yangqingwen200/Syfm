<%@ page language="java" pageEncoding="utf-8" %>

<div id='loadingDiv' style="position: absolute; z-index: 1000; top: 0px; left: 0px;
    width: 100%; height: 100%; background: white; text-align: center;">
    <h5 style="top: 48%; position: relative;font-family: 微软雅黑, 宋体;">
        <font color="#15428B">拼命加载中···</font>
    </h5>
</div>
<%--
    如果页面数据太多, easyui页面渲染的时候, 会出现闪动的现象, 用一个全局的遮罩来避免闪动.

    使用方法: 在需要使用的jsp页面中, <body>标签对中用 <%@include file="/admin/common/loadingDiv.jsp"%> 引入即可.
    注意: 使用$.parser.onComplete easyui的标签元素, 所以必须先引入easyui相关的js文件
 --%>
<script type="text/javascript">
    function closeLoading() {
        /*fadeOut淡入淡出效果, fast淡出最快*/
        $("#loadingDiv").fadeOut("normal", function () {
            $(this).remove();
        });
    }

    var no;
    $.parser.onComplete = function () {
        if (no) {
            clearTimeout(no);
        }
        no = setTimeout(closeLoading, 200);
    }
</script>

