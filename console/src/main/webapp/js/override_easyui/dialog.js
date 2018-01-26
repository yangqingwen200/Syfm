$.extend($.fn.dialog.defaults, {
	closed: true,
	width: 600,
	height: 350,
//	title: "填写信息",
	iconCls: "icon-edit",
	resizable: false,
	modal: true,
	buttons:[{
		iconCls:"icon-clear", 
		text:"重置",
		handler:function(){
			var textbox = $("#add_edit_form .add-edit-form-table .easyui-textbox,.easyui-numberbox,.easyui-filebox,.easyui-datebox,.easyui-datetimebox");
			for(var i=0; i<textbox.length; i++) {
				$(textbox[i]).textbox("setValue", "");
			}
			var combobox = $("#add_edit_form .add-edit-form-table .easyui-combobox");
			for(var i=0; i<combobox.length; i++) {
				var value = $(combobox[i]).combobox("getData");
				 if (value.length > 0) {
					 $(combobox[i]).combobox("reset");
	              } 
			}
		}
	},{
		iconCls:"icon-ok",
		text:"保存",
		handler:function(){
			//有点脱裤子放屁
			//form.js中已经配置novalidate: true, 此处应当不需要再判断输入的是否有效
			//为了显示进度条才输入值是否有效的判断, 如果不做判断, 输入框值无效, 也会显示进度条
			var flag = $("#add_edit_form").form("validate");
			if(flag) {
				$.messager.progress({
					text: '正在处理中...',
					interval: 500
				})
				$("#add_edit_form").form("submit", {url: parent.addOrEditUrl});
			}
		}
	}
	]
});