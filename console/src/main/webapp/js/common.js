/**
 * 判断有没有权限以及登录超时以及错误信息
 * @param data
 * @returns {Boolean}
 */
function checkPermission(data) {
	if(typeof(data.errorMsg) != "undefined") {
		if(data.errorMsg == '登录超时或在别处登录，请重新登录。') {
			$.messager.alert("警告", data.errorMsg, "warning", function(){
				window.parent.location.href = parent.loginjsp;
				return false;
			});
		} else {
			$.messager.alert("警告",data.errorMsg,"warning");
			return false;
		}
	} else {
		return true;
	}
}

/**
 * 公用的右下角弹出框方法
 */
function easyuishow(content, time) {
    var times = 3;
    if(time) {
        times = time;
    }
	$.messager.show({
		title: "我的消息（窗口将在" + times + "秒后关闭）",
		msg: content,
		timeout: times * 1000,
		showType: 'show'
	});
}

/**
 * 去掉字符串里面的空格
 * @param str
 * @param is_global
 * @returns
 */
function trimSpace(str,is_global) {
	var result;
	result = str.replace(/(^\s+)|(\s+$)/g,"");
	if(is_global.toLowerCase()=="g") {
		result = result.replace(/\s/g,"");
	}
	return result;
}

/**
 * 数列状态字段转换函数
 * @param value
 * @param rowData
 * @param rowIndex
 * @returns {string}
 */
function statusFmt(value, rowData, rowIndex) {
    return (value == 1) ? "<span>正常</span>" : "<span style=\"color: red;\">禁用</span>";
}

function moreFmt(value,row,index){
    var showvalue = value;
    if(value.length > 30) {
        showvalue = value.substring(0, 30) + '...';
    }
    return "<a title=\"" + value + "\" class=\"easyui-tooltip\">" + showvalue + "</a>";
}

function parentFmt(value, row, index){
    return row.parentName;
}

/**
 * 判断当前datagrid是否有选中的一行
 * @param datagrid
 * @returns {Boolean}
 */
function datagridChecked(datagrid) {
	var rows = datagrid.datagrid('getChecked');
	if(rows.length == 0) {
		easyuishow("请选择一条数据");
		return false;
	} else if(rows.length > 1) {
		easyuishow("只能选择一条数据");
		return false;
	} else {
		return true;
	}
}

/**
 * 判断当前datagrid是否有选中的多行.
 * @param datagrid
 * @returns {Boolean}
 */
function datagridCheckedMore(datagrid) {
	var rows = datagrid.datagrid('getChecked');
	if(rows.length == 0) {
		easyuishow("请至少选择一条数据");
		return false;
	} else {
		return true;
	}
}

/**
 * 单击datagrid行的事件
 * @param datagrid
 * @param rowIndex
 */
function clickDatagridRow(datagrid, rowIndex) {
	datagrid.datagrid('clearSelections');
	datagrid.datagrid('clearChecked');
	datagrid.datagrid('selectRow',rowIndex);
	datagrid.datagrid('checkRow',rowIndex);
}

/**
 * 双击datagrid行的事件
 * @param datagrid
 * @param rowIndex
 */
function doublelClickDatagridRow(datagrid, rowIndex) {
	/*datagrid.datagrid('clearSelections');
	datagrid.datagrid('clearChecked');
	datagrid.datagrid('selectRow',rowIndex);
	datagrid.datagrid('checkRow',rowIndex);*/
}

/**
 * 行编辑模式下的增加或者修改
 * @param datagrid
 * @param rowData
 * @param addUrl
 * @param editUrl
 */
function rowDatagridAddOrEdit(datagrid, rowData, addUrl, editUrl) {
	var insert = datagrid.datagrid('getChanges', 'inserted');
	var update = datagrid.datagrid('getChanges', 'updated');
	if(insert.length > 0) {
		addeditleveUrl = addUrl;
	}
	if(update.length > 0) {
		addeditleveUrl = editUrl;
	}
	 $.ajax({
		   url: addeditleveUrl,
		   type: 'post',
		   data: rowData,
		   dataType: 'json',
		   success: function(data) {
				 if(!checkPermission(data)) {
					 datagrid.datagrid('rejectChanges');
				  } else {
					  if(showMessage(data)) {
						  datagrid.datagrid('reload');
					  } else {
						//失败时回滚当前的操作.
				        datagrid.datagrid('rejectChanges');
					  }

				  }
		   }
	 });
}

/**
 * 对datagrid(添加行数据)操作form操作成功/失败时弹出的提示框
 * 采用dialog形式
 * @param data
 * @param datagrid
 * @param dialog
 */
function operDatagridSuccessOrFailShow(data, datagrid, dialog) {
	if(operFormSuccessOrFailShow(data, dialog)) {
		datagrid.datagrid('reload');
	}
}

/**
 * 操作普通的form弹出失败或者成功信息
 * @param data
 * @param dialog
 * @returns {Boolean}
 */
function operFormSuccessOrFailShow(data, dialog) {
	data = $.parseJSON(data);
	$.messager.progress('close');
	if(checkPermission(data)) {
        if(showMessage(data)) {
            dialog.dialog('close');
            return true;
        } else {
            return false;
        }
  	} else {
        return false;
  	}
}

/**
 * 操作成功/失败时弹出的提示框
 * @param data
 * @returns {Boolean}
 */
function showMessage(data) {
	if(data.message) {
		easyuishow("操作成功。");
		return true;
	} else {
		easyuishow("操作失败。<br>" + data.errorMsg);
		return false;
	}
}

/**
 * 精确查找的公用方法
 */
function preciseSearch() {
	var match = "";
	var queryParams = $("#table_data_datagrid").datagrid('options').queryParams;
	var textbox = $("#north_frame_search .easyui-textbox,.easyui-numberbox,.easyui-searchbox,.easyui-datebox,.easyui-datetimebox");
	for(var i=0; i<textbox.length; i++) {
		var value = $(textbox[i]).textbox("getValue");
		match += value;
		var name = textbox[i].id;
		if(value != "") {
			queryParams[name]=value;
		} else {
			delete queryParams[name];
		}
	}

	//textbox和combobox分开写, 是为了Jboss7不报错: java.lang.IllegalStateException:Parameters processing failed
	var combobox = $("#north_frame_search .easyui-combobox");
	for(var i=0; i<combobox.length; i++) {
		var value = $(combobox[i]).combobox("getValue");
		var name = combobox[i].id;
		match += value;
		if(value != "") {
			queryParams[name]=value;
		} else {
			delete queryParams[name];
		}
	}

	$("#table_data_datagrid").datagrid('options').queryParams = queryParams;
	$("#table_data_datagrid").datagrid("clearChecked");
	$("#table_data_datagrid").datagrid("load");
}

/**
 * 清空搜索条件的公共的js方法
 */
function emptySearch() {
	$("#north_frame_search .easyui-textbox,.easyui-searchbox,.easyui-datebox,.easyui-datetimebox,.easyui-numberbox").textbox("clear");
	$("#north_frame_search .easyui-combobox").combobox("reset");
	$("#table_data_datagrid").datagrid("clearChecked");
	$("#table_data_datagrid").datagrid("load", {});
}

/**
 * 公用的添加方法
 */
function commonAdd(url) {
    parent.addOrEditUrl = url;
	//var v = $("form input[name=id]").val();
	$("#add_edit_form input[type=hidden][name=id]").val("");
	$("#add_edit_form").form("reset");

	$("#add_edit_dialog").dialog({
		iconCls: "icon-add"
	});
	$("#add_edit_dialog").dialog("setTitle", "新增");
	$("#add_edit_dialog").dialog("open");
}

/**
 * 公用的编辑方法
 */
function commonEdit(url) {
	if(datagridChecked($("#table_data_datagrid"))) {
        parent.addOrEditUrl = url;
		var rows = $("#table_data_datagrid").datagrid("getChecked");
		$("#add_edit_form").form("load", rows[0]);
		$("#add_edit_dialog").dialog({
			iconCls: "icon-edit"
		});
		$("#add_edit_dialog").dialog("setTitle", "修改");
		$("#add_edit_dialog").dialog("open");
	}
}

/**
 * 公用的删除方法
 */
function commonDelete(url) {
	if(datagridCheckedMore($("#table_data_datagrid"))) {
		$.messager.confirm("确认框", "您想要删除选中的信息吗？", function(r){
			if(r) {
				var rows = $("#table_data_datagrid").datagrid("getChecked");
				var ids = [];
				for(var i=0;i<rows.length;i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url: url,
					type: "post",
                    dataType: "json",
					data: "id=" + ids,
					async: false,
					cache: false,
					success: function(data) {
						if(checkPermission(data)) {
							if(showMessage(data)) {
								$("#table_data_datagrid").datagrid("clearChecked");
								//不建议使用重新加载,增加服务器的压力
								$("#table_data_datagrid").datagrid("reload");
							}
						}
					}
				});
			}
		});
	}
}

/**
 * 公用的添加方法, 使用跳转到新页面(新tabs)方式
 */
function commonAddNewTabs(url) {
    addTabInFrame("新增数据", url);
}

/**
 * 公用的编辑方法, 使用跳转到新页面(新tabs)方式
 */
function commonEditNewTabs(url) {
    if(datagridChecked($("#table_data_datagrid"))) {
        var rows = $("#table_data_datagrid").datagrid("getChecked");
        var id = rows[0].id;
        addTabInFrame("编辑数据", url + "?id=" + id);
    }
}

function ajaxRequest(url, data) {
	var reuslt = false;
    $.ajax({
        url: url,
		data: data,
        dataType: "json",
		type: "post",
        async: false,
        cache: false,
        success: function(data) {
            if(checkPermission(data)) {
                reuslt = data;
            }
        }
    });
    return reuslt;
}

/**
 * 增加tabs方法, 在layout中使用
 * @param titles
 * @param href
 * @param id
 */
function addTab(titles, href, id) {
    $("#" + id).addClass("default").siblings().removeClass("default");

	var t = $("#layout_index_centerTabs");
	if (t.tabs("exists", titles)) {
		t.tabs("select", titles);
	} else {
		var content = "<iframe scrolling='yes' frameborder='0' src='" + href + "' style='height:100%; width:100%; marginwidth:0; marginheight:0;'></iframe>";
		t.tabs("add", {
			title: titles,
			content:content,
			closable: true,
			 tools:[{
		        iconCls:"icon-mini-refresh",
		        handler:function(){
		        	t.tabs("select", titles); //先切换到要刷新的tabs, 避免在A tabs时, 去刷新B tabs, 会出现 A和B两个 tabs标题,内容相同的bug
		        	var tab = t.tabs("getSelected");
		        	$("#layout_index_centerTabs").tabs("update", {
		        		 tab:tab,
		        		 options: {
		        			 title: titles,
		        			 content: content
		        		 }
		        	});
		        }
		    }]
		});
	}
}

/**
 * 增加tabs方法,在iframe中使用
 * @param titles
 * @param href
 * @returns {boolean}
 */
function addTabInFrame(titles, href) {
    var pmap = parent.map; //得到父容器中的map, 在index.jsp中定义过
	var tparent = parent.$("#layout_index_centerTabs"); //得到父容器中的 tabs

	if(pmap.containsKey(titles)) {  //判断当前tabs中是否含有将要跳转相同名称的title
        if (tparent.tabs("exists", titles)) {
            tparent.tabs("select", titles);
        }
       return false;
	}

    var currTab = tparent.tabs("getSelected"); //得到跳转之前  当前选中的tabs
    var title = currTab.panel("options").title; //得到跳转之前  当前选中的tabs的标题
	pmap.put(titles, title); //把跳转之前的页面title值存入map中, 跳转之后的title为key, 跳转之前的tabs标题为value

	var content = "<iframe scrolling='yes' frameborder='0' src='" + href + "' style='height:100%; width:100%; marginwidth:0; marginheight:0;'></iframe>";
	tparent.tabs("add", {
		title: titles,
		content:content,
		closable: true
	});
}

/**
 * 关闭当前的tabs, 并刷新跳转之前的tabs中的datagrid, 在iframe中使用
 * @param refresh 是否需要刷新datagrid
 */
function colseTab(refresh) {
	var pmap = parent.map;
	var tparent = parent.$("#layout_index_centerTabs");
	var tab = tparent.tabs("getSelected"); //获得即将关闭的tabs
	var title = tab.panel("options").title; //得到即将关闭tabs的标题
	var value = pmap.get(title); //根据当前页面的title, 从map中取出跳转之前的tabs的title, 打开页面的时候以key-value形式方式map中

	/*
	 * 刷新跳转之前tabs中的datagrid数据
	 */
	if (tparent.tabs("exists", value)) {
		tparent.tabs("select", value); //切换回跳转之前的tabs

		if(refresh) {
            var tab1 = tparent.tabs("getTab", value); //根据标题. 获得之前的tabs
            //tparent.tabs("update", {tab: tab1, options: {}}); //刷新整个tabs. 不建议使用, jsp中含有自定义标签, 刷新整个页面, 消耗性能
            var _refresh_ifram = tab1.find('iframe')[0];//获取到选定的tab下的iframe
            var refresh_doc = _refresh_ifram.contentWindow;//获取到选定的tab下的iframe中的内容
            var datagrid = refresh_doc.$("#table_data_datagrid"); //获得tabs中的datagrid
            if(datagrid) {
                datagrid.datagrid("reload"); //刷新datagrid数据
            }
        }
	}

	pmap.remove(title); //从map移除掉跳转之前的tabs
	tparent.tabs("close", title); //关闭当前tabs
}

/**
 * 转换需要查看详情的转换器, datagrid单元格转换器
 */
function dtlFmtTabs(value,row,index) {
	var result = '<a onclick="viewDetialTabs( \'' + row.id + '\',\'' + value + '\')" style="cursor: pointer;color: blue;text-decoration: underline" title="查看详情">' + value + '</a>';
	return result;
}

/**
 * 查看详情, 使用跳转到新页面(新tabs)方式
 * @param id
 * @param value
 */
function viewDetialTabs(id, value) {
    addTabInFrame("浏览详情_" + value, flagDetailUrl + "?id=" + id);
}

/**
 * 转换需要查看详情的转换器, datagrid单元格转换器
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function dtlFmtDialog(value,row,index) {
    var result = '<a onclick="viewDetialDialog( \'' + row.id + '\')" style="cursor: pointer;color: blue;text-decoration: underline" title="查看详情">' + value + '</a>';
    return result;
}

/**
 * 查看详情的逻辑函数, 使用dialog方式
 * @param id
 */
function viewDetialDialog(id) {
    $("#viewDetail_detailDialog").dialog({
        href: flagDetailUrl + "?id=" + id
    });
    $("#viewDetail_detailDialog").dialog("open");
}

/**
 * 初始化搜索栏的按钮
 */
$(function() {
    var search = "&nbsp;&nbsp;<a id=\"search\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-search',plain:true\" onclick=\"preciseSearch()\">查询&nbsp;&nbsp;</a>";
    var emptySearch = "&nbsp;&nbsp;<a id=\"emptySearch\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload',plain:true\" onclick=\"emptySearch()\">重置&nbsp;&nbsp;</a>";
    $("#north_frame_search").append(search);
    $("#north_frame_search").append(emptySearch);
    $.parser.parse($("#north_frame_search"));
});