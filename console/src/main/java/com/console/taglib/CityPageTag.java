package com.console.taglib;

import com.commons.taglib.CommonTag;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityPageTag extends CommonTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String bodyMessage(HttpSession session) {
		StringBuffer sb = new StringBuffer("<script type=\"text/javascript\"> $(function(){");
		List<Map<String, Object>> listElment = publicService.findSqlListMap("select '' as id, '请选择' as text union all SELECT code as id, province AS text FROM sys_base_province");
		sb.append("$('#province').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment)) + ");");

		listElment.remove(0);
		sb.append("$('#editFormProvinceCode').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment)) + ");");

		List<Map<String, Object>> listElment1=  new ArrayList<Map<String, Object>>();
		for(int i=65; i<=90; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			char c = (char)i;
			map.put("id", c);
			map.put("text", c);
			listElment1.add(map);
		}
		sb.append("$('#editFormLetter').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listElment1)) + ");");
		sb.append("}); </script>");

		return sb.toString();
	}
}
