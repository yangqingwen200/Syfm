package com.console.action.system;

import com.commons.bean.system.SysBaseProvince;
import com.commons.exception.LogicException;
import com.commons.util.MaptoBean;
import com.console.action.BaseWebAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 省份增删改
 * auth: Yang
 * 2016年7月1日 下午12:30:23
 */
@Controller("system.web.action.provinceAction")
@Scope("prototype")
public class ProvinceAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;
	
	/**
	 * 获得省份列表
	 * 
	 * auth: Yang 2016年2月12日 下午10:36:48
	 */
	public void getProvinceList() {
		try {
			StringBuffer sb = new StringBuffer("select id, code, province from sys_base_province m where 1=1 ");
			if (dto.getAsStringTrim("code") != null) {
				sb.append("and m.code like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("code") + "%");
			}
			
			if (dto.getAsStringTrim("province") != null) {
				sb.append("and m.province like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("province") + "%");
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
	 * 增加省份
	 * 
	 * auth: Yang 2016年2月15日 下午2:46:28
	 */
	public void addProvince() {
		try {
			this.checkProvinceCodeReply();
			this.checkProvinceNameReply();
			SysBaseProvince sbp = MaptoBean.mapToBeanBasic(new SysBaseProvince(), dto);
			this.publicService.save(sbp);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 编辑省份
	 * 
	 * auth: Yang 2016年2月15日 下午5:18:39
	 */
	public void editProvince() {
		try {
			SysBaseProvince sbp = this.publicService.load(SysBaseProvince.class, dto.getAsInteger("id"));
			SysBaseProvince sbp1 = MaptoBean.mapToBeanBasic(new SysBaseProvince(), dto);
			if(sbp.equals(sbp1)) {
				throw new LogicException("省份信息没有任何改变。");
			}
			
			if(!sbp.getCode().equals(sbp1.getCode())) {
				this.checkProvinceCodeReply();
			}
			
			if(!sbp.getProvince().equals(sbp1.getProvince())) {
				this.checkProvinceNameReply();
			}
			
			this.publicService.update(sbp1);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 删除省份
	 *
	 * auth: Yang
	 * 2016年3月21日 下午2:32:39
	 */
	public void deleteProvince() {
		try {
			String asString = dto.getAsStringTrim("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				SysBaseProvince sbp = this.publicService.load(SysBaseProvince.class, Integer.parseInt(string));
				String code = sbp.getCode();
				//找到省份下面所有的城市
				List<String> findBySqlList = this.publicService.findSqlList("select code from sys_base_city where province_code=?", new Object[]{code}); 
				for (String string2 : findBySqlList) {
					//删除城市下面所有的县级
					this.publicService.executeUpdateSql("delete from sys_base_area where city_code=?", string2);
				}
				//删除省份下面所有的城市
				this.publicService.executeUpdateSql("delete from sys_base_city where province_code=?", code);
				//删除省份
				this.publicService.executeUpdateSql("delete from sys_base_province where id=?", Integer.parseInt(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	public void checkProvinceCodeReply() {
		int count = this.publicService.findSqlCount("select count(*) from sys_base_province where code=?", dto.getAsStringTrim("code"));
		if(count > 0) {
			throw new LogicException("省份邮编重复，请更换。");
		}
	}
	
	public void checkProvinceNameReply() {
		int count = this.publicService.findSqlCount("select count(*) from sys_base_province where province=?", dto.getAsStringTrim("province"));
		if(count > 0) {
			throw new LogicException("省份重复，请更换。");
		}
	}
}
