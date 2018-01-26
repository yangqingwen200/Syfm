<%@ page language="java" pageEncoding="utf-8"%>

<mytag:ordEasyuiJs all="true" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqueryextensions/jquery-easyui-datagridfilter/datagrid-filter.js"></script>
<script type="text/javascript">
	$(function() {
		var dg = $('#area_areaView_datagrid').datagrid();
		dg.datagrid('enableFilter', []);
	});
</script>

<div id="area_areaView_Search" class='easyui-layout' data-options='fit: true, border:false'>
	<div data-options="region:'center', border:false">
		<table id="area_areaView_datagrid" data-options="url:'${ctxweb}/getAreaList_area.do',remoteFilter:true">
			 <thead>   
		        <tr>   
		            <th data-options="field:'id',width:20,align:'center',sortable:true">id</th>   
		            <th data-options="field:'area',width:80,align:'center',sortable:true">县/区名称</th>   
		            <th data-options="field:'code',width:100,align:'center',sortable:true" >县/区邮编</th>   
		            <th data-options="field:'provinceCode',width:100,align:'center'" >所属省份</th>   
		            <th data-options="field:'pcode',width:100,align:'center'" >省份邮编</th>   
		            <th data-options="field:'cityCode',width:100,align:'center'" >所属城市</th>   
		            <th data-options="field:'ccode',width:100,align:'center'" >城市邮编</th>   
		        </tr>   
		    </thead>   
		</table>
	</div>
</div>
