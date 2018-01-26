package com.console.action.system;

import com.commons.exception.LogicException;
import com.commons.bean.system.SysUser;
import com.console.action.BaseWebAction;
import net.sf.json.JSONArray;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("system.web.action.ajaxAction")
@Scope("prototype")
public class AjaxAction extends BaseWebAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 检查登录名是否存在, 只能返回true 或者 false
	 * 前台 jquery-validate 中使用
	 * @author Yang
	 * @version v1.0
	 * @date 2017/7/18
	 */
	public void checkLoginExists() {
		String loginname = dto.getAsStringTrim("loginname");
		int sqlCount = this.publicService.findSqlCount("select count(id) from sys_user where loginname=?", loginname);
		if(sqlCount > 0) {
			this.printString("false");
		} else {
			this.printString("true");

		}
	}

	/**
	 * 得到登录用户的登录信息
	 * @author Yang
	 * @version v1.0
	 * @date 2017/7/14
	 */
	public void getLoginUserInfo() {
		try {
			Integer user = ((SysUser) session.getAttribute("user")).getId();
			Map<String, Object> sqlMap = this.publicService.findSqlMap("select DATE_FORMAT(login_time,'%Y-%m-%d %H:%i:%S') as lastlogintime, ip_address as ip " +
					"from sys_log_login where user_id=? order by id desc limit 1,1", user);

			Map<String, Object> sqlMap1 = this.publicService.findSqlMap("SELECT GROUP_CONCAT(re.name) AS role FROM " +
					"(SELECT r.name FROM `sys_user_role` ur JOIN `sys_role` r ON r.`id`=ur.`role_id` WHERE ur.`user_id`=?) AS re", user);

			json.accumulate("lastLongiTime", sqlMap.get("lastlogintime") != null ? sqlMap.get("lastlogintime") : "您是第一次登录系统。");
			json.accumulate("ip", sqlMap.get("ip") != null ? sqlMap.get("ip") : "您是第一次登录系统。");
			json.accumulate("role", sqlMap1.get("role") != null ? sqlMap1.get("role") : "root用户");

		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson();
		}

	}


	/**
	 * @version v1.0
	 * @date 2017/2/16 0016
	 * @author Yang
	 */
	public void getPermissionId() {
		try {
			Integer parentid = dto.getAsInteger("parentid");
			Map<String, Object> sqlMap = this.publicService.findSqlMap("select permission_id as perssmionid from sys_element where id=?", parentid);
			json.accumulate("perssmionid", sqlMap.get("perssmionid"));

		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson();
		}
	}
	
	
	/**
	 * 根据省份code, 获得所有的城市
	 * auth: Yang
	 * 2016年7月10日 下午12:00:20
	 */
	public void getCityByProvince() {
		List<Map<String, Object>> listCity = new ArrayList<Map<String,Object>>();
		try {
			String flag = dto.getAsStringTrim("flag");
			String provinceCode = dto.getAsStringTrim("code");
			if(provinceCode == null) {
				listCity = this.publicService.findSqlListMap("SELECT '' AS id, '请选择' AS text UNION ALL SELECT CODE AS id, city AS TEXT FROM sys_base_city");
			} else {
				if("edit".equals(flag)) {
					listCity = this.publicService.findSqlListMap("SELECT CODE AS id, city AS text FROM sys_base_city where province_code=?", new Object[]{provinceCode});
				} else if("view".equals(flag)) {
					listCity = this.publicService.findSqlListMap("SELECT '' AS id, '请选择' AS text UNION ALL SELECT CODE AS id, city AS text FROM sys_base_city where province_code=?", new Object[]{provinceCode});
				}
			}
			
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printString(JSONArray.fromObject(listCity).toString());
		}
		
	}
	
	/**
	 * 首页用户修改密码
	 * auth: Yang
	 * 2016年7月10日 下午9:12:52
	 */
	public void mofidyPwdByUser() {
		try {
			SysUser user = (SysUser)session.getAttribute("user");
			Map<String, Object> findBySqlMap = this.publicService.findSqlMap("select pw from sys_user where id=?", user.getId());
			String oldPwd = String.valueOf(findBySqlMap.get("pw"));
			if(oldPwd.equals(dto.getAsStringTrim("oldPwd"))) {
				this.publicService.executeUpdateSql("update sys_user set pw=? where id=?", dto.getAsStringTrim("newPwd"), user.getId());
			} else {
				throw new LogicException("旧密码不正确，请检查。");
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 获得区域详情
	 * auth: Yang
	 * 2016年7月26日 下午12:33:56
	 */
	public void getAreaDetail() {
		String result = "加载数据失败";
		try {
			Integer areaid = dto.getAsInteger("areaid");
			StringBuffer sb = new StringBuffer("SELECT m.area, m.code, city.city, city.code as citycode, pro.province, pro.code as procode from sys_base_area m "
					+ "join sys_base_city city on m.city_code=city.code join sys_base_province pro on pro.code=city.province_code "
					+ "where m.id = ?");
			
			Map<String, Object> findBySqlMap = this.publicService.findSqlMap(sb.toString(), areaid);
			
			result= "<table class='dv-table' border='0' style='width:100%;'>"
					+ "<tr><td>所属省份：" + findBySqlMap.get("province") + "， 所属省份区号： " + findBySqlMap.get("procode") + "</td>"
					+ "<tr><td>所属城市：" + findBySqlMap.get("city") +"， 所属城市区号： " + findBySqlMap.get("citycode") + "</td>"
					+ "</tr></table>";
			
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printString(result);
		}
	}
	
	public String viewAppUserListjump() {
		try {
			List<Map<String, Object>> listElment = this.publicService.findSqlListMap("select '' as id, '请选择' as text, TRUE AS 'selected' union all SELECT id, NAME AS text, FALSE AS 'selected' FROM sys_menu WHERE disploy=? AND parent=?", new Object[]{1, 0});
			this.request.setAttribute("mueuData", String.valueOf(JSONArray.fromObject(listElment)));
		} catch (Exception e) {
			this.checkException(e);
		}
		return "success";
	}
	
	/**
	 * 得到用户的登录情况
	 * auth: Yang
	 * 2016年9月2日 下午3:37:23
	 */
	public void getUserLoginPublish() {
		List<Object> listName = new ArrayList<Object>();
		List<Object> listValue = new ArrayList<Object>();
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		try {
			List<Object[]> lists = this.publicService.findSqlList("SELECT COUNT(sll.id), su.loginname FROM sys_user su LEFT JOIN sys_log_login sll ON sll.user_id=su.id GROUP BY su.id");
			for (Object[] objects : lists) {
				listName.add(objects[1]);
				listValue.add(objects[0]);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "登录次数");
			map.put("data", listValue);
			listMap.add(map);
			json.accumulate("names", listName);
			json.accumulate("datas", listMap);
			
			listMap.clear();
			List<String> listtime = this.publicService.findSqlList("SELECT DATE_FORMAT(sll.login_time, '%Y-%m-%d') AS TIME FROM sys_user su JOIN sys_log_login sll ON sll.user_id=su.id GROUP BY DATE_FORMAT(sll.login_time, '%Y-%m-%d')");
			json.accumulate("xlogintime", listtime);
			List<Object> listuser = this.publicService.findSqlList("SELECT su.loginname FROM sys_user su");
			for (Object object : listuser) {
				Map<String, Object> mapdata = new HashMap<String, Object>();
				mapdata.put("name", object);
				List<Object> listLoginCount = new ArrayList<Object>();
				for(String str : listtime) {
					int count = this.publicService.findSqlCount("select count(*) FROM sys_user su JOIN sys_log_login sll ON sll.user_id=su.id where su.loginname=? and DATE_FORMAT(sll.login_time, '%Y-%m-%d')=?", object, str);
					listLoginCount.add(count);
				}
				mapdata.put("data", listLoginCount);
				listMap.add(mapdata);
			}
			json.accumulate("loginpub", listMap);
			
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printString(json.toString());
		}
	}

}
