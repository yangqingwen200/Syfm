package com.console.action.system;

import com.commons.exception.LogicException;
import com.commons.util.MaptoBean;
import com.commons.bean.system.SysBaseArea;
import com.console.action.BaseWebAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 县/区增删改
 * auth: Yang
 * 2016年7月1日 下午12:30:23
 */
@Controller("system.web.action.areaAction")
@Scope("prototype")
public class AreaAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;
	
	/**
	 * 获得县/区列表
	 * 
	 * auth: Yang 2016年2月12日 下午10:36:48
	 */
	public void getAreaList() {
		try {
			StringBuffer sb = new StringBuffer("SELECT m.id, m.area, m.code, pc.provinceName, pc.cityName, pc.cityCode, pc.provinceCode from sys_base_area m "
					+ "LEFT JOIN (SELECT p.province as provinceName, c.city as cityName, c.code as cityCode, p.code as provinceCode FROM sys_base_province p JOIN sys_base_city c ON c.province_code=p.code) AS pc ON m.city_code=pc.cityCode "
					+ "where 1=1 ");
			if (dto.getAsStringTrim("code") != null) {
				sb.append("and m.code like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("code") + "%");
			}
			
			if (dto.getAsStringTrim("area") != null) {
				sb.append("and m.area like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("area") + "%");
			}
			
			if (dto.getAsStringTrim("province") != null) {
				sb.append("and pc.provinceCode like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("province") + "%");
			}
			
			if (dto.getAsStringTrim("city") != null) {
				sb.append("and pc.cityCode like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("city") + "%");
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

	/**
	 * 增加县/区
	 * 
	 * auth: Yang 2016年2月15日 下午2:46:28
	 */
	public void addArea() {
		try {
			this.checkAreaCodeReply();
			SysBaseArea sbp = MaptoBean.mapToBeanBasic(new SysBaseArea(), dto);
			this.publicService.save(sbp);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 编辑县/区
	 * 
	 * auth: Yang 2016年2月15日 下午5:18:39
	 */
	public void editArea() {
		try {
			SysBaseArea sbp = this.publicService.load(SysBaseArea.class, dto.getAsInteger("id"));
			SysBaseArea sbp1 = MaptoBean.mapToBeanBasic(new SysBaseArea(), dto);
			if(sbp.equals(sbp1)) {
				throw new LogicException("县/区信息没有任何改变。");
			}
			
			if(!sbp.getCode().equals(sbp1.getCode())) {
				this.checkAreaCodeReply();
			}

			this.publicService.update(sbp1);
		} catch (Exception e) { 
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 删除县/区
	 *
	 * auth: Yang
	 * 2016年3月21日 下午2:32:39
	 */
	public void deleteArea() {
		try {
			String asString = dto.getAsStringTrim("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				//删除县/区
				this.publicService.executeUpdateSql("delete from sys_base_area where id=?", Integer.parseInt(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	public void checkAreaCodeReply() {
		int count = this.publicService.findSqlCount("select count(*) from sys_base_area where code=?", dto.getAsStringTrim("code"));
		if(count > 0) {
			throw new LogicException("县/区邮编重复，请更换。");
		}
	}
}
