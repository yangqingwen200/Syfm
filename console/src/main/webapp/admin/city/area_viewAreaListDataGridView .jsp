<%@ page language="java" pageEncoding="utf-8"%>

<mytag:ordEasyuiJs all="true" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqueryextensions/jquery-easyui-datagridview/datagrid-detailview.js"></script>
<script type="text/javascript">
	$(function() {
		var dg = $('#area_areaView_datagrid').datagrid();
		dg.datagrid({
			view: detailview,
			onExpandRow: function(index,row) {
				$("#ddv-" + index).panel({    
		            border:false,    
		            cache:false,    
		            href:"${ctxajax}/getAreaDetail.do?areaid=" +row.id,
		            onLoad:function(){    
		            	dg.datagrid("fixDetailRowHeight",index);    
		            }    
		        });    
				dg.datagrid("fixDetailRowHeight",index);    
			},
			detailFormatter: function(rowIndex, rowData){
				return "<div id='ddv-" + rowIndex + "' style='padding:5px 0'></div>";
			}
		});
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
		        </tr>   
		    </thead>   
		</table>
	</div>
</div>
