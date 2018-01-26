<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.commons.bean.system.SysMenu"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>

<%-- fit:false 会根据菜单栏多少, 自动调节高度, 为true就屏幕高度 --%>
<div id="accordion" class="easyui-accordion" data-options="fit:true,border:false">
	<%
		Map<Integer, List<SysMenu>> menu_l = (Map<Integer, List<SysMenu>>) session.getAttribute("menus");
		if(menu_l != null) {
			Iterator<?> it = menu_l.keySet().iterator();
			while (it.hasNext()) {
				Integer i = (Integer) it.next();
				List<SysMenu> menus = menu_l.get(i);
				 for (SysMenu menu : menus) {
					 if (menu.getParent() == 0) {
						%>
						<div title="<%=menu.getName() %>" data-options="selected:true,iconCls:'icon-filter'" style="overflow:auto;padding:2px;">
						<%
						} else {%>
						<a id="<%=menu.getId() %>" class="west-nav" onclick="addTab('<%=menu.getName() %>','admin/<%=menu.getPath() %>','<%=menu.getId() %>')"
						   onmouseover="this.style.background='#e6e6e6';this.style.border='1px outset'" onmouseout="this.style.background='';this.style.border=''">
							<span>->&nbsp;<%=menu.getName() %></span>
						</a>
					<%}
				}%>
				</div>
				<%
			}
		} else {
			%>
			<script type="text/javascript">
				$.messager.alert("警告", "登录超时或在别处登录，请重新登录。", "warning", function(){
					window.parent.location.href = parent.loginjsp;
		});
			</script>
	<%}
	%>
</div>