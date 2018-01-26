package com.console.action.system;

import com.commons.annotation.OpernationLog;
import com.commons.constant.InitDBConstant;
import com.commons.exception.LogicException;
import com.commons.util.MaptoBean;
import com.commons.bean.system.SysCode;
import com.console.action.BaseWebAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;

/**
 * code码增删改
 * auth: Yang
 * 2016年7月1日 下午12:30:23
 */
@Controller("system.web.action.codeAction")
@Scope("prototype")
public class CodeAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;
	
	/**
	 * 获得code列表
	 * 
	 * auth: Yang 2016年2月12日 下午10:36:48
	 */
	public void getCodeList() {
		try {
			StringBuffer sb = new StringBuffer("select id, code, code_name as codeName, value, description from sys_code m where 1=1 ");
			if (dto.getAsStringTrim("code") != null) {
				sb.append("and m.code like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("code") + "%");
			}
			
			if (dto.getAsStringTrim("code_name") != null) {
				sb.append("and m.code_name like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("code_name") + "%");
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
	 * 增加code
	 * 
	 * auth: Yang 2016年2月15日 下午2:46:28
	 */
	@OpernationLog(cls = SysCode.class, value = "增加code")
	public void addCode() {
		try {
			int count = this.publicService.findSqlCount("select count(*) from sys_code where code=?", dto.getAsStringTrim("code"));
			if(count > 0) {
				throw new LogicException("code值重复，请更换。");
			}
			SysCode sbc = MaptoBean.mapToBeanBasic(new SysCode(), dto);
			sbc.setCode(sbc.getCode().toUpperCase());
			this.publicService.save(sbc);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 编辑code
	 * 
	 * auth: Yang 2016年2月15日 下午5:18:39
	 */
	@OpernationLog(cls = SysCode.class, value = "编辑code")
	public void editCode() {
		try {
			SysCode sbc = this.publicService.load(SysCode.class, dto.getAsInteger("id"));
			SysCode sbc1 = MaptoBean.mapToBeanBasic(new SysCode(), dto);
			sbc1.setCode(sbc1.getCode().toUpperCase());
			if(sbc.equals(sbc1)) {
				throw new LogicException("Code码信息没有任何改变。");
			}
			
			if(!sbc.getCode().equals(sbc1.getCode())) {
				throw new LogicException("code严禁修改，修改请通知开发人员。");
			}
			
			//code码相对应的值改变, 更新InitDBConstant java类中的静态变量的值
			if(!sbc.getValue().equals(sbc1.getValue())) { 
				//利用反射, 给InitDBConstant类中的属性赋值
				Class<?> cla = InitDBConstant.class; 
				Field f = cla.getField(sbc.getCode());
				if(null != f) {
					f.set(cla, sbc1.getValue()); //重新赋值
				}
			}
			
			this.publicService.update(sbc1);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 删除code
	 *
	 * auth: Yang
	 * 2016年3月21日 下午2:32:39
	 */
	@OpernationLog(cls = SysCode.class, value = "删除code")
	public void deleteCode() {
		try {
			String asString = dto.getAsStringTrim("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				this.publicService.executeUpdateSql("delete from sys_code where id=?", Integer.parseInt(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

}
