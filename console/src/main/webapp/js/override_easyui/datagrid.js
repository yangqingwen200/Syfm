$.extend($.fn.datagrid.defaults, {
	idField: "id",
	fit: true,
	border: false,
	striped: true, //奇数和偶数行颜色不一样
	fitColumns: true,
	rownumbers: false,  //显示行号,不建议使用.会拖慢渲染速度.
	nowrap: true, //当一个单元格显示不下时,强制在同一个单元格一行显示,可以用formatter自定义显示
	remoteSort: true,  //一进网页,就按照这个字段进行排序.
	sortName: "id",  //排序字段
	sortOrder: "asc",  //排序方式
	checkOnSelect: false, //如果为false，当用户仅在点击该复选框的时候才会被选中或取消
	selectOnCheck: true, //如果为true，单击复选框将永远选择行
	frozenColumns: [[{ 
		field : "ck",   //做一个复选框的效果,注意关闭singleSelect: true,否则只能选择一行
		width : 10,		//此字段没有任何意义,只是显示一个复选框而已.
		checkbox: true
	}
	]],
	toolbar: "#toolbar",  //toolbar放在pagination分页栏上面
	pagePosition: "bottom", //设置分页栏的位置,'both'上下都有
	pagination: true,
	pageList: [10,20,30,50,70,90],
	pageSize: 20, //一去页面就显示是十条数据
	onLoadSuccess:function(data) {
		if(checkPermission(data)) {
			if(data.rows < 1) {
				easyuishow("没有找到相关数据");
			}
		}
	},
	onClickRow: function(rowIndex, rowData) {
		clickDatagridRow($("#table_data_datagrid"), rowIndex);
	},
	onDblClickRow:function(rowIndex, rowData) {
		doublelClickDatagridRow($("#table_data_datagrid"), rowIndex);
	},
	onLoadError: function() {
		$.messager.alert('错误','数据加载失败，请联系管理员。', 'error');
	}
});
