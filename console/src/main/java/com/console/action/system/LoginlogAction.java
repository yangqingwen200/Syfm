package com.console.action.system;

import com.console.action.BaseWebAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 用户登录日志
 * @author Administrator
 *
 */
@Controller("system.web.action.loginlogAction")
@Scope("prototype")
public class LoginlogAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;

	/**
	 * 获得用户登录日志列表
	 */
	public void getloginlogList() {
		try {
			StringBuffer footer = new StringBuffer("SELECT '累计时间' AS logout_time, IFNULL(SUM(onlinetime), 0) as onlinetime FROM sys_log_login log where 1=1 ");
			StringBuffer sb = new StringBuffer("select log.id, log.login_name, log.ip_address, DATE_FORMAT(log.login_time,'%Y-%m-%d %H:%i:%S') AS login_time, " +
					"DATE_FORMAT(log.logout_time,'%Y-%m-%d %H:%i:%S') AS logout_time, log.onlinetime, log.session_id, log.logout_type as logout_type from sys_log_login log where 1=1 ");
			if(dto.getAsStringTrim("logintime") != null) {
				sb.append("and DATE_FORMAT(login_time, '%Y-%m-%d')= ? ");
				footer.append("and DATE_FORMAT(login_time, '%Y-%m-%d')= ? ");
				fuzzySearch.add(dto.getAsStringTrim("logintime"));
				
			}
			if (dto.getAsStringTrim("loginName") != null) {
				sb.append("and log.login_name like ? ");
				footer.append("and log.login_name like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("loginName") + "%");
			}
			sb.append("order by " + sort + " " + order);
			currentpage = this.publicService.pagedQuerySql(page, rows, sb.toString(), fuzzySearch.toArray());
			List<Object> sqlListMap = this.publicService.findSqlListMap(footer.toString(), fuzzySearch.toArray());

			json.accumulate("total", currentpage.getTotalNum());
			json.accumulate("rows", currentpage.getContent());
			json.accumulate("footer", sqlListMap);
		} catch (Exception e) {
			json.accumulate("total", 0);
			json.accumulate("rows", 0);
			this.checkException(e);
		} finally {
			this.printString(json.toString());
		}
	}
	
}
