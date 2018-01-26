package com.console.taglib;

import com.commons.taglib.CommonTag;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class AreaPageTag extends CommonTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String bodyMessage(HttpSession session) {
		StringBuffer sb = new StringBuffer("<script type=\"text/javascript\"> $(function(){");
		//初始化省份下拉框
		List<Map<String, Object>> listProvince = publicService.findSqlListMap("select '' as id, '请选择' as text union all SELECT code as id, province AS text FROM sys_base_province");
		sb.append("$('#province').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listProvince)) + ");");
		listProvince.remove(0);
		sb.append("$('#editFormProvince').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listProvince)) + ");");

		//初始化城市下拉框
		List<Map<String, Object>> listCity = publicService.findSqlListMap("SELECT '' as id, '请选择' AS text UNION ALL SELECT code AS id, city AS text FROM sys_base_city");
		sb.append("$('#city').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listCity)) + ");");

		listCity.remove(0);
		sb.append("$('#editFormCity').combobox('loadData'," + String.valueOf(JSONArray.fromObject(listCity)) + ");");
		sb.append("}); </script>");

		return sb.toString();
	}
}
