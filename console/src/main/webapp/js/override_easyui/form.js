$.extend($.fn.form.defaults, {
	novalidate: true,
	success : function(data) {
		operDatagridSuccessOrFailShow(data, $("#table_data_datagrid"), $("#add_edit_dialog"));
	}
});

