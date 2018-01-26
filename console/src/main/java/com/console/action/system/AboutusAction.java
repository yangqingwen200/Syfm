package com.console.action.system;

import com.commons.bean.system.SysAboutus;
import com.commons.util.MaptoBean;
import com.console.action.BaseWebAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;

/**
 * 关于我们
 * auth: Yang
 * 2016年8月20日 上午9:45:02
 */
@Controller("system.web.action.aboutusAction")
@Scope("prototype")
public class AboutusAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;
	
	/**
	 * 获得关于我们文章内容
	 * auth: Yang
	 * 2016年8月20日 上午9:55:31
	 * @return
	 */
	public void getAboutus() {
		try {
			Map<String, Object> findBySqlMap = this.publicService.findSqlMap("select sa.id, sa.content from sys_aboutus sa limit 1");
			//request.setAttribute("content", findBySqlMap.get("content"));
			//request.setAttribute("id", findBySqlMap.get("id"));
			json.accumulate("content", findBySqlMap.get("content"));
			json.accumulate("id", findBySqlMap.get("id"));
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printString(json.toString());
		}
	}
	
	/**
	 * 更新关于我们内容
	 * auth: Yang
	 * 2016年8月20日 下午4:08:14
	 */
	public void updateAboutus() {
		try {
			SysAboutus saOld = this.publicService.load(SysAboutus.class, dto.getAsInteger("id"));
			SysAboutus sa = MaptoBean.mapToBeanBasic(saOld, dto);
			sa.setUpdateTime(new Date());
			this.publicService.save(sa);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
}
