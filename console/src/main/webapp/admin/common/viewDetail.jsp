<%@ page language="java" pageEncoding="utf-8"%>

<div id="viewDetail_detailDialog" class="easyui-dialog" style="width:65%;height:50%;padding:10px;"
     data-options="title: '浏览详情',loadingMessage: '正在加载数据，请等待...',border: false,iconCls: 'icon-search',
                    resizable: true,maximizable: true,buttons: null,
                    onLoad : function() {
                        <%-- 这里代码主要是检查有没有权限或者登陆超时的情况, 如果不需要检查, 可以把onLoad这段代码去掉 --%>
                        var bo = $(this).panel('body');
                        var content = bo[0].innerText; //获得dialog里面的内容
                        try {
                            var jcontent = $.parseJSON(content);
                            if (!checkPermission(jcontent)) {
                                $(this).panel('close');
                            }
                        } catch (e) {
                            //console.info('is not json');
                        }
                    },
                    onClose: function() {
                        $('#viewDetail_detailDialog').dialog('resize');
                    }">
<%-- 这里会包含一个jsp页面进来, 经过struts forward过来jsp --%>
</div>
