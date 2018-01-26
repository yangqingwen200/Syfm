package com.console.taglib;

import com.commons.taglib.CommonTag;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionPageTag extends CommonTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String bodyMessage(HttpSession session) {
		StringBuffer sb = new StringBuffer("<script type=\"text/javascript\"> $(function(){");
		List<Map<String, Object>> listElment = publicService.findSqlListMap("select '' as id, '请选择' as text union all SELECT id, permission AS text FROM sys_permission WHERE parent=?", new Object[]{0});
		sb.append("$('#parent').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment)) + ");");

		listElment.remove(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 0);
		map.put("text", "顶级");
		listElment.add(0, map);
		sb.append("$('#editFormPermissionParent').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment)) + ");");
		sb.append("}); </script>");

		return sb.toString();
	}
}
