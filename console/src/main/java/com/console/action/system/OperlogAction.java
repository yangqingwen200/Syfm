package com.console.action.system;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.console.action.BaseWebAction;

/**
 * 用户操作日志
 * @author Administrator
 *
 */
@Controller("system.web.action.operlogAction")
@Scope("prototype")
public class OperlogAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;

	/**
	 * 获得用户操作日志列表
	 */
	public void getOperlogList() {
		try {
			StringBuffer sb = new StringBuffer("select log.id, log.oparetor_name, log.ip_address, DATE_FORMAT(log.oparator_time,'%Y-%m-%d %H:%i:%S') AS oparator_time, " +
					"log.access_class, log.access_method, log.descprition, log.param from sys_log_operation log where 1=1 ");
			if(dto.getAsStringTrim("opertime") != null) {
				sb.append("and DATE_FORMAT(oparator_time, '%Y-%m-%d')= ? ");
				fuzzySearch.add(dto.getAsStringTrim("opertime"));
				
			}
			if (dto.getAsStringTrim("operName") != null) {
				sb.append("and log.oparetor_name like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("operName") + "%");
			}
			sb.append("order by " + sort + " " + order);
			currentpage = this.publicService.pagedQuerySql(page, rows, sb.toString(), fuzzySearch.toArray());

			json.accumulate("total", currentpage.getTotalNum());
			json.accumulate("rows", currentpage.getContent());
		} catch (Exception e) {
			json.accumulate("total", 0);
			json.accumulate("rows", 0);
			this.checkException(e);
		} finally {
			this.printString(json.toString());
		}
	}
	
}
