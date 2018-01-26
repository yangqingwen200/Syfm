<%@ page language="java" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<title>模板增删改查</title>
<head>
    <mytag:ordEasyuiJs all="true"/>
    <script type="text/javascript">

    </script>
</head>

<body>
	<div id="template_templateView_Search" class='easyui-layout' data-options='fit: true, border:false'>
	    <div id="north_frame" data-options="region:'north', border:false">
	        <div id="north_frame_search">
	        </div>
	    </div>
	    <div data-options="region:'center', border:false">
	        <table id="table_data_datagrid" class="easyui-datagrid" data-options="url:'${ctxweb}/getTemplateList_template.do'">
	            <thead>
	            <tr>
	                <th data-options="field:'id',width:20,align:'center',sortable:true">id</th>
	            </tr>
	            </thead>
	        </table>
	    </div>
	</div>
	
	<div id="add_edit_dialog" class="easyui-dialog">
	    <form id="add_edit_form" method="post">
	        <input type="hidden" id="editFormId" name="id">
	        <table class="add-edit-form-table">
	        </table>
	    </form>
	</div>
	
	<mytag:toolbar name="getTemplateList" />

</body>
</html>