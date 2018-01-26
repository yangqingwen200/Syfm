<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs all="true" />
	<script type="text/javascript">
        function moreFmt(value,row,index){
            var showvalue = value;
            if(value.length > 40) {
                showvalue = value.substring(0, 40) + '...';
            }
            var va = value.replace(/\\n/g, "\n");
            return "<a title=\"" + va + "\" class=\"easyui-tooltip\">" + showvalue + "</a>";
        }
	</script>
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div id="operlog_operlogView_Search" class='easyui-layout' data-options='fit:true, border:false'>
		<div id="north_frame" data-options="region:'north', border:false">
			<div id="north_frame_search">
				操作人:
				<input id="operName" class="easyui-textbox" />&nbsp;&nbsp;
				操作时间:
				<input id="opertime" class="easyui-datebox" data-options="currentText:'',closeText:'', editable:false" style="width:180px">
			</div>
		</div>
		<div data-options="region:'center'">
			<table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getOperlogList_operlog.do',sortName:'oparator_time',sortOrder:'desc'">
				<thead>
				<tr>
					<%--<th data-options="field:'id',width:10,align:'center',sortable:true">id</th>--%>
					<th data-options="field:'oparetor_name',width:30,align:'center'">操作人</th>
					<th data-options="field:'oparator_time',width:45,align:'center',sortable:true">操作时间</th>
					<th data-options="field:'ip_address',width:30,align:'center'">操作人IP</th>
					<th data-options="field:'access_class',width:70,align:'center'">访问的JAVA类</th>
					<th data-options="field:'access_method',width:40,align:'center'">访问的方法</th>
					<th data-options="field:'descprition',width:50,align:'center'">说明</th>
					<th data-options="field:'param',width:100,align:'center',formatter:moreFmt">变更内容</th>
				</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>


