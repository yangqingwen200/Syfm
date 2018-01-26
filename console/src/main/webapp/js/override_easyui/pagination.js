$.extend($.fn.pagination.defaults, {
	//buttons: "#toolbar", //toolbar放在pagination分页栏上面
	nav : {
		first : {
			iconCls : "pagination-first",
			handler : function() {
				var _ce = $(this).pagination("options");
				if (_ce.pageNumber > 1) {
					$("#table_data_datagrid").datagrid("clearChecked");
					$(this).pagination("select", 1);
				}
			}
		},
		prev : {
			iconCls : "pagination-prev",
			handler : function() {
				var _cf = $(this).pagination("options");
				if (_cf.pageNumber > 1) {
					$("#table_data_datagrid").datagrid("clearChecked");
					$(this).pagination("select", _cf.pageNumber - 1);
				}
			}
		},
		next : {
			iconCls : "pagination-next",
			handler : function() {
				var _d0 = $(this).pagination("options");
				var _d1 = Math.ceil(_d0.total / _d0.pageSize);
				if (_d0.pageNumber < _d1) {
					$("#table_data_datagrid").datagrid("clearChecked");
					$(this).pagination("select", _d0.pageNumber + 1);
				}
			}
		},
		last : {
			iconCls : "pagination-last",
			handler : function() {
				var _d2 = $(this).pagination("options");
				var _d3 = Math.ceil(_d2.total / _d2.pageSize);
				if (_d2.pageNumber < _d3) {
					$("#table_data_datagrid").datagrid("clearChecked");
					$(this).pagination("select", _d3);
				}
			}
		},
		refresh : {
			iconCls : "pagination-refresh",
			handler : function() {
				var _d4 = $(this).pagination("options");
				if (_d4.onBeforeRefresh.call(this, _d4.pageNumber,
						_d4.pageSize) != false) {
					$("#table_data_datagrid").datagrid("clearChecked");
					$(this).pagination("select", _d4.pageNumber);
					_d4.onRefresh.call(this, _d4.pageNumber, _d4.pageSize);
				}
			}
		}
	}
});
