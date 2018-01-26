package com.console.action.system;

import com.commons.exception.LogicException;
import com.commons.service.GenericService;
import com.commons.util.MaptoBean;
import com.commons.bean.system.SysBaseCity;
import com.console.action.BaseWebAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 城市增删改
 * auth: Yang
 * 2016年7月1日 下午12:30:23
 */
@Controller("system.web.action.cityAction")
@Scope("prototype")
public class CityAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;
	
	/**
	 * 获得城市列表
	 * 
	 * auth: Yang 2016年2月12日 下午10:36:48
	 */
	public void getCityList() {
		try {
			StringBuffer sb = new StringBuffer("select m.id, m.code, m.city, m.province_code as provinceCode, "
					+ "(select p.province FROM sys_base_province p where p.code=m.province_code) as parentName, m.letter from sys_base_city m where 1=1 ");
			if (dto.getAsStringTrim("code") != null) {
				sb.append("and m.code like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("code") + "%");
			}
			
			if (dto.getAsStringTrim("city") != null) {
				sb.append("and m.city like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("city") + "%");
			}
			
			if (dto.getAsStringTrim("province") != null) {
				sb.append("and m.province_code like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("province") + "%");
			}
			if (dto.getAsStringTrim("letter") != null) {
				sb.append("and m.letter like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("letter") + "%");
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
	 * 增加城市
	 * 
	 * auth: Yang 2016年2月15日 下午2:46:28
	 */
	public void addCity() {
		try {
			this.checkCityCodeReply();
			this.checkCityNameReply();
			SysBaseCity sbp = MaptoBean.mapToBeanBasic(new SysBaseCity(), dto);
			this.publicService.save(sbp);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 编辑城市
	 * 
	 * auth: Yang 2016年2月15日 下午5:18:39
	 */
	public void editCity() {
		try {
			SysBaseCity sbp = this.publicService.load(SysBaseCity.class, dto.getAsInteger("id"));
			SysBaseCity sbp1 = MaptoBean.mapToBeanBasic(new SysBaseCity(), dto);

			if(!sbp.getCode().equals(sbp1.getCode())) {
				this.checkCityCodeReply();
			}
			
			if(!sbp.getCity().equals(sbp1.getCity())) {
				this.checkCityNameReply();
			}
			
			this.publicService.update(sbp1);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 删除城市
	 *
	 * auth: Yang
	 * 2016年3月21日 下午2:32:39
	 */
	public void deleteCity() {
		try {
			String asString = dto.getAsStringTrim("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				SysBaseCity sbp = this.publicService.load(SysBaseCity.class, Integer.parseInt(string));
				String code = sbp.getCode();
				//删除城市下面所有的县级
				this.publicService.executeUpdateSql("delete from sys_base_area where city_code=?", code);
				//删除城市
				this.publicService.executeUpdateSql("delete from sys_base_city where id=?", Integer.parseInt(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	public void checkCityCodeReply() {
		int count = this.publicService.findSqlCount("select count(*) from sys_base_city where code=?", dto.getAsStringTrim("code"));
		if(count > 0) {
			throw new LogicException("城市邮编重复，请更换。");
		}
	}
	
	public void checkCityNameReply() {
		int count = this.publicService.findSqlCount("select count(*) from sys_base_city where city=?", dto.getAsStringTrim("city"));
		if(count > 0) {
			throw new LogicException("城市名称重复，请更换。");
		}
	}

	@Autowired
	public void setPublicService(@Qualifier("publicService") GenericService publicService) {
		this.publicService = publicService;
	}

	public GenericService getPublicService() {
		return publicService;
	}
	
}
