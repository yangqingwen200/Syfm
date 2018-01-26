/**
 * BootstrapDialog 自定义方法
 * Created by Yang on 2017/9/12.
 */
(function ($, bd, pnt) {
    /**
     * 弹出框
     * @param title 弹出框标题
     * @param content 弹出框内容
     */
    bd.myAlert = function(title, content) {
        bd.alert({
            title: "<span class='glyphicon glyphicon-flash' aria-hidden='true'></span><span class='dialog-title'>" + (title || '警告') + "</span>",
            message: content,
            type: bd.TYPE_DEFAULT,
            draggable: true,
            buttonLabel: '<span class="glyphicon glyphicon-check" aria-hidden="true"></span> 明白了'
        });
    };

    /**
     * 提示框
     * @param title 提示框标题
     * @param content 提示框内容
     * @param type 主题颜色
     * @param size 框大小
     * @param timeout 超时时间关闭窗口
     */
    bd.myShow = function(options) {
        options.timeout = options.timeout || 60000;
        bd.show({
            title: options.title + " (窗口" + options.timeout/1000 + "秒后消失)",
            message: options.message,
            type: options.type || bd.TYPE_PRIMARY,
            size: options.size || bd.SIZE_NORMAL,
            onshow: function(dialog) {
                setTimeout(function(){
                    dialog.close();
                }, options.timeout);
            }
        });
    };

    /**
     * 从服务器加载数据到显示框中
     * @param url 请求的url
     * @param param 请求url附加的数据
     * @param title 显示框的标题
     * @param dialogSize 改变弹出的框的大小: 90% 90px
     */
    bd.myAjaxShow = function(options) {
        bd.show({
            title: options.title || "<span class='glyphicon glyphicon-search' aria-hidden='true'></span><span class='dialog-title'>浏览</span>",
            type: bd.TYPE_DEFAULT,
            size: bd.SIZE_WIDE,
            message: $('<div></div>').load(bd.myCreateURL(options.url, options.param)),
            onshow: function(dialog) {
                if(options.dialogSize) {
                    var moadlDialog = dialog.$modalDialog[0]; //得到弹出框, 方便改变大小
                    $(moadlDialog).css("width", options.dialogSize);
                }
            },
            buttons: [{
                icon: 'glyphicon glyphicon-ok',
                label: '看完了',
                cssClass: 'btn-info',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
        });
    };

    /**
     * 新增或者编辑对话框
     * @param url 进入到编辑或者新增请求的url
     * @param param 请求url附加的数据
     * @param title 弹出框标题
     * @param doSomething form提交成功后执行的方法
     * @param dialogSize 改变弹出的框的大小: 90% 90px
     * @param showTips 是否弹出提示信息
     */
    bd.myAddEditShow = function(options) {
        bd.show({
            title: options.title || "<span class='glyphicon glyphicon-edit' aria-hidden='true'></span><span class='dialog-title'>信息填写</span>",
            type: bd.TYPE_DEFAULT,
            size: bd.SIZE_WIDE,
            onshow: function(dialog) {
                if(options.dialogSize) {
                    var moadlDialog = dialog.$modalDialog[0]; //得到弹出框, 方便改变大小
                    $(moadlDialog).css("width", options.dialogSize);
                }
            },
            message: $('<div></div>').load(bd.myCreateURL(options.url, options.param)),
            buttons: [{
                icon: 'glyphicon glyphicon-remove',
                label: '取消',
                cssClass: 'btn-default',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }, {
                icon: 'glyphicon glyphicon-check',
                label: '保存',
                cssClass: 'btn-primary',
                action: function(dialogItself){
                    var form = dialogItself.getModalBody().find('form'); //找到弹出框里面的form表单
                    if(form.length > 0) { //页面错误, 并没有加载正常加载对应页面
                        if($(form).valid()) {
                            dialogItself.enableButtons(false); //禁用当前所有按钮, 避免网络慢 用户使劲狂点...
                            var $button = this; //得到点击的按钮
                            $button.spin(); //开始小齿轮转动效果
                            $(form).ajaxSubmit({
                                data: {
                                    _random: Date.now()
                                },
                                type: "post",
                                success: function (responseText, statusText) {
                                    var result = $.parseJSON(responseText);
                                    if(statusText === "success" && bd.mySEShow(result, 1000, options.showTips)) {
                                        dialogItself.close();
                                        if(options.doSomething) {
                                            options.doSomething.call();
                                        }
                                    } else {
                                        $button.stopSpin(); //停止小齿轮转动
                                        dialogItself.enableButtons(true); //开启按钮可以点击功能
                                    }
                                }
                            });
                        }
                    } else {
                        dialogItself.close(); //出了错误点击保存按钮, 直接关闭dialog
                    }
                }
            }]
        });
    };

    /**
     * 确认框操作 (如: 删除/下架/置顶)
     * @param url 请求的url
     * @param data 请求提交的数据
     * @param message 确认框的提示语内容
     * @param dosomething 服务器返回成功后需要执行调用方法
     * @param showTips 是否弹出提示信息
     */
    bd.myConfirm  = function(options) {
        var dialog = bd.confirm({
            title: options.title || '<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span><span class="dialog-title">确认框</span>',
            message: options.message || "请确认您当前操作?",
            type: options.type || bd.TYPE_DEFAULT,
            closable: true,
            draggable: true,
            btnCancelLabel: '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 取消',
            btnOKLabel: '<span class="glyphicon glyphicon-check" aria-hidden="true"></span> 确认',
            btnOKClass: 'btn-danger',
            callback: function(result) {
                if(result) {
                    dialog.enableButtons(false); //禁用当前所有按钮, 避免网络慢 用户使劲狂点...
                    var results = bd.myAjaxReq(options.url, options.param); //请求后台
                    if(bd.mySEShow(results, 1000, options.showTips)) {
                        if(options.doSomething) {
                            options.doSomething.call();
                        }
                    }
                }
            }
        });
    };

    /**
     * 数据请求
     * @param url 请求的url
     * @param data 提交的数据
     * @returns 服务器返回的结果
     */
    bd.myAjaxReq = function (url, data) {
        var result;
        data._random = Date.now();
        $.ajax({
            url: url,
            data: data,
            dataType: "json",
            type: "post",
            async: false,
            cache: false,
            /*beforeSend:function(jqXHR, options){
                jqXHR.setRequestHeader("custom-header", "custom-info") ;  // 增加一个自定义请求头
            },*/
            success: function(data) {
                result = data;
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                result = {message: false, errorMsg: '请求出错'};
            }
        });
        return result;
    };

    /**
     * 拼接参数到url后面
     * @param url 请求url
     * @param param 参数对象: {id: 1, name: 'wen'}
     * @returns {string}
     */
    bd.myCreateURL = function(url, param){
        //加上随机数, 避免IE浏览器使用缓存
        url = url += "?random=" + Date.now() + "&";
        for(var pro in param){
            if(param.hasOwnProperty(pro)) {
                url += (pro + "=" + param[pro] + "&");
            }
        }
        return url.substr(0, url.length - 1);
    };

    /**
     * 操作成功或者失败的提示: Success or Error
     * @param results 服务器返回的结果, 格式: {message: true, errorMsg: '服务器异常'}
     * @param timeout 弹出框的显示时间
     * @param showTips 是否弹出提示框
     */
    bd.mySEShow = function (results, timeout, showTips) {
        timeout =  timeout || 1000;
        if(results.message) {
            if(showTips) {
                pnt.myTips("提示", "操作成功", "success", timeout);
            }
        } else{
            bd.myShow({
                title: "<span class='glyphicon glyphicon-remove' aria-hidden='true'></span><span class='dialog-title'>错误</span>",
                message: results.errorMsg,
                type: bd.TYPE_DANGER,
                size: bd.SIZE_SMALL
            });
        }
        return results.message;
    };


})(jQuery, BootstrapDialog, PNotify);