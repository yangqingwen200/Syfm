<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<mytag:ordEasyuiJs />

	<script type="text/javascript" src="${ctx}/js/kindeditor/kindeditor-all.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/kindeditor/lang/zh-CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/kindeditor/plugins/code/prettify.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/js/kindeditor/themes/default/default.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/kindeditor/plugins/code/prettify.css">
	<script type="text/javascript">
		<%--
			后台接收上传图片方法
			public void testkindeditor() {
			try {
				//文件类型
				String dirName = request.getParameter("dir");
				String uploadFile = FileUtil.uploadFile(this.getImgFile(), this.getImgFileFileName(),  "/" + dirName, false);
				json.put("error", 0);
				json.put("url", uploadFile);
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.printString(json.toString());
			}
		}
		--%>
        var editor;
        KindEditor.ready(function(K) {
            editor = K.create('textarea[name="content"]', {
                //height : "500px", //编辑器的高度，只能设置px，比textarea输入框样式表高度优先度高。
                //resizeType: 0, //2或1或0，2时可以拖动改变宽度和高度，1时只能改变高度，0时不能拖动。
                useContextmenu: false, //true时使用右键菜单，false时屏蔽右键菜单。
                uploadJson : 'app/testkindeditor_appUser.do',
                fileManagerJson : 'js/kindeditor/jsp/file_manager_json.jsp',
                allowFileManager : true,
                afterCreate : function() {
                    var self = this;
                    //同步数据到服务器（图片，视频，音频什么的）如果没有这一步对不起，获取不到数据
                    self.sync();
                }
            });
            prettyPrint();
        });

        $(function(){

            $.post("${ctxweb}/getAboutus_aboutus.do",function(result){
                result = $.parseJSON(result);
                if(checkPermission(result)) {
                    $("#id").val(result.id);
                    editor.html(result.content);
                }
            });

            $("#aboutusForm").form({
                url : "${ctxweb}/updateAboutus_aboutus.do",
                onSubmit : function() {
                    //var content = KindEditor.instances[0].html();也可以拿到第一个富文本的值
                    editor.sync(); //先同步，才能拿到值
                    var content = $("#content").val();
                    if($.trim(content) == '') {
                        alert("请填写内容");
                        return false;
                    }
                },
                success : function(data) {
                    data = $.parseJSON(data);
                    if(checkPermission(data)) {
                        showMessage(data);
                    }
                }
            });
        });
	</script>
</head>

<body>
	<%@include file="/admin/common/loadingDiv.jsp"%>
	<div>
	  <form id="aboutusForm" method="post">
			<input type="hidden" name="id" id="id">
			<textarea id="content" name="content" style="width:100%;height:90%;">
			</textarea>
			<input type="submit" name="button" value="保存" />
	  </form>
	</div>

</body>
</html>