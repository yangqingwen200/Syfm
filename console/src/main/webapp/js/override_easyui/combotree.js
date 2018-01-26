$.extend($.fn.combotree.defaults, {
	onLoadSuccess: function(node, data) {
		checkPermission(data);
	}
});