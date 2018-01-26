package com.console.taglib;

import com.commons.taglib.CommonTag;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementPageTag extends CommonTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String bodyMessage(HttpSession session) {
		StringBuffer sb = new StringBuffer("<script type=\"text/javascript\"> $(function(){");
		List<Map<String, Object>> listElment1 = publicService.findSqlListMap("select '' as id, '请选择' as text union all SELECT id, description AS text FROM sys_element WHERE disabled=? AND parent=?", new Object[]{1, 0});
		sb.append("$('#parent').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment1)) + ");");

		listElment1.remove(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 0);
		map.put("text", "顶级");
		listElment1.add(0, map);
		sb.append("$('#parentId').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment1)) + ");");

		List<Map<String, Object>> listElment2 = publicService.findSqlListMap("select 0 as id, '暂不分配' as text union all SELECT id, remark AS text FROM sys_permission WHERE iselement=?", new Object[]{1});
		sb.append("$('#permissionId').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment2)) + ");");
		sb.append("}); </script>");

		return sb.toString();
	}
}
