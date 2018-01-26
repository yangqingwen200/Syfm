$.extend($.fn.combobox.defaults, {
    textField: 'text',
    valueField: 'id',
    type: 'text',
    editable: false,
    width: 150,
    panelHeight: 150
});

$.extend($.fn.combotree.defaults, {
    onLoadSuccess: function(node, data) {
        checkPermission(data);
    }
});

$.extend($.fn.datagrid.defaults, {
    idField: "id",
    fit: true,
    border: false,
    striped: true,
    fitColumns: true,
    rownumbers: false,
    nowrap: true,
    remoteSort: true,
    sortName: "id",
    sortOrder: "asc",
    checkOnSelect: false,
    selectOnCheck: true,
    frozenColumns: [[{
        field : "ck",
        width : 10,
        checkbox: true
    }
    ]],
    toolbar: "#toolbar",
    pagePosition: "bottom",
    pagination: true,
    pageList: [10,20,30,50,70,90],
    pageSize: 20,
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

$.extend($.fn.dialog.defaults, {
    closed: true,
    width: 600,
    height: 350,
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
            var flag = $("#add_edit_form").form("validate");
            if(flag) {
                $.messager.progress({
                    text: '正在处理中...',
                    interval: 500
                });
                $("#add_edit_form").form("submit", {url: parent.addOrEditUrl});
            }
        }
    }
    ]
});

$.extend($.fn.form.defaults, {
    novalidate: true,
    success : function(data) {
        operDatagridSuccessOrFailShow(data, $("#table_data_datagrid"), $("#add_edit_dialog"));
    }
});

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

$.extend($.fn.validatebox.defaults.rules, {
    minLength : {
        validator : function(value, param) {
            return value.length >= param[0];
        },
        message : '请至少输入{0}字符'
    },
    middleLength : {
        validator : function(value, param) {
            return value.trim().length >= param[0]
                && value.trim().length <= param[1];
        },
        message : '请输入大于{0}小于{1}个字符'
    },
    maxLength : {
        validator : function(value, param) {
            return  value.trim().length < param[0];
        },
        message : '最多输入{0}字符'
    },
    equalLength : {
        validator : function(value, param) {
            return value.length == param[0];
        },
        message : '请输入{0}位数字'
    },
    equalLengthValidate : {
        validator : function(value, param) {
            return value.length == param[0];
        },
        message : '请输入{0}位验证码'
    },
    checkMobile : {
        validator : function(value, param) {
            var partten = /^1[3,4,5,6,7,8,9]\d{9}$/;
            var fl = false;
            if(partten.test(value) && value.length == 11) {
                fl = true;
            }
            return fl;
        },
        message : '请输入11位数字手机号码'
    },
    checkEuqals : {
        validator : function(value, param) {
            return value == $(param[0]).val();
        },
        message : '两次输入密码不一致'
    },
    forbbinChinese : {
        validator : function(value, param) {
            var flag = value.trim().length >= param[0] && value.trim().length <= param[1];
            var reg = /[\u4E00-\u9FA5]/i;
            if(!reg.test(value) && flag){
                return true;
            }
            return false;
        },
        message : '请输入大于{0}小于{1}个无中文字符'
    },
    checkImage : {
        validator : function(value, param) {
            var index = value.lastIndexOf(".");
            if(index == -1) {
                return false;
            }
            var perfix = value.substring(index+1, value.length).toLowerCase();
            if(perfix != "jpeg" && perfix != "jpg" && perfix != "png" && perfix != "bmp") {
                return false;
            } else {
                return true;
            }
        },
        message : "只能选择jpeg,jpg,png,bmp文件"
    }
});

$.extend($.fn.searchbox.defaults, {
    type: 'text',
    width: 180,
});