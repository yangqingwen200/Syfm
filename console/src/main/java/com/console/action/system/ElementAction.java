package com.console.action.system;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.commons.exception.LogicException;
import com.commons.util.MaptoBean;
import com.commons.bean.system.SysElement;
import com.console.action.BaseWebAction;

import java.util.Date;

/**
 * 页面按钮管理增删改查
 * 
 * auth：Yang 2016年2月16日 上午9:40:24
 */
@Controller("system.web.action.elementAction")
@Scope("prototype")
public class ElementAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;

	/**
	 * 1. 获得页面按钮列表
	 * 
	 * auth: Yang 2016年2月12日 下午10:36:48
	 */
	public void getElementList() {
		try {
			StringBuffer sb = new StringBuffer("select m.id as id, m.code as code, m.function_name as functionName, m.description as description, m.disabled as disabled, m.icon as icon, m.buttom_name as buttomName, "
					+ "(SELECT mu.description FROM sys_element mu WHERE mu.id=m.parent) as parentName, m.parent as parent, p.id as permissionId, p.remark as permissionremark "
					+ " from sys_element m left join sys_permission p on m.permission_id=p.id where 1=1 ");
			if (dto.getAsInteger("parent") != null) {
				sb.append("and (m.parent = ? or m.id = ?) ");
				fuzzySearch.add(dto.getAsInteger("parent"));
				fuzzySearch.add(dto.getAsInteger("parent"));
			}
			if (dto.getAsInteger("disabled") != null) {
				sb.append("and m.disabled=? ");
				fuzzySearch.add(dto.getAsInteger("disabled"));
			}
			if (dto.getAsStringTrim("jsname") != null) {
				sb.append("and m.function_name like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("jsname") + "%");
			}
			if (dto.getAsStringTrim("name") != null) {
				sb.append("and m.buttom_name like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("name") + "%");
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
	 * 2. 增加页面按钮
	 *
	 * auth: Yang
	 * 2016年3月19日 下午5:53:22
	 */
	public void addElement() {
		try {
			if(!"#".equals(dto.getAsStringTrim("code"))) {
				int countCode = this.publicService.findSqlCount("select count(id) from sys_element where code=?", dto.getAsStringTrim("code"));
				if (countCode > 0) {
					throw new LogicException("按钮ID重复，请更换。");
				}
			}
			
			String jsName = dto.getAsStringTrim("functionName");
			if(!"#".equals(jsName) && !"commonDelete()".equals(jsName) && !"commonAdd()".equals(jsName) && !"commonEdit()".equals(jsName)) {
				int countJSName = this.publicService.findSqlCount("select count(id) from sys_element where function_name=?", dto.getAsStringTrim("functionName"));
				if (countJSName > 0) {
					throw new LogicException("JS方法名重复，请更换。");
				}
			}

			SysElement element = MaptoBean.mapToBeanBasic(new SysElement(), dto);
			element.setCreateTime(new Date());
			this.publicService.save(element);
			
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 3. 编辑页面按钮
	 *
	 * auth: Yang
	 * 2016年3月19日 下午5:52:55
	 */
	public void editElement() {
		try {
			
			if(dto.getAsInteger("id").intValue() == dto.getAsInteger("parent").intValue()) {
				throw new LogicException("按钮不能当爹又当儿子。");
			}
			
			SysElement m = this.publicService.load(SysElement.class, dto.getAsInteger("id")); //加载修改之前的数据
			if(!"#".equals(dto.getAsStringTrim("code"))) {
				if(!m.getCode().equals(dto.getAsStringTrim("code"))) {
					int countCode = this.publicService.findSqlCount("select count(id) from sys_element where code=?", dto.getAsStringTrim("code"));
					if (countCode > 0) {
						throw new LogicException("按钮ID重复，请更换。");
					}
				}
			}
			String jsName = dto.getAsStringTrim("functionName");
			if(!m.getFunctionName().equals(jsName)) {
				if(!"#".equals(jsName) && !"commonDelete()".equals(jsName) && !"commonAdd()".equals(jsName) && !"commonEdit()".equals(jsName)) {
					int countJSName = this.publicService.findSqlCount("select count(id) from sys_element where function_name=?", jsName);
					if (countJSName > 0) {
						throw new LogicException("JS方法名重复，请更换。");
					}
				}
			}
			//当前节点含有子节点, 不允许当前节点成为子节点.
			if(dto.getAsInteger("parent").intValue() != 0 &&  m.getParent().intValue() == 0) {
				int countChild = this.publicService.findSqlCount("select count(*) from sys_element where parent=?", dto.getAsInteger("id"));
				if(countChild > 0) {
					throw new LogicException("当前按钮含有子按钮<br>请先删除或者移除子按钮。");
				}
			}

			//如果禁用的是父节点, 则禁用下面的子节点
			if(m.getDisabled().intValue() != dto.getAsInteger("disabled").intValue()) {
				this.publicService.executeUpdateSql("update sys_element set disabled=? where parent=?", dto.getAsInteger("disabled"), dto.getAsInteger("id"));
			}

			SysElement sysElement = MaptoBean.mapToBeanBasic(m, dto); //修改之后, 反射成对象
			this.publicService.update(sysElement);
	} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 4. 删除页面按钮
	 *
	 * auth: Yang
	 * 2016年3月21日 下午1:34:43
	 */
	public void deleteElement() {
		try {
			String asString = dto.getAsStringTrim("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				int count = this.publicService.findSqlCount("select count(id) from sys_element where parent=?", Integer.parseInt(string));
				if(count > 0) {
					throw new LogicException("请先删除子按钮。");
				}
				//删除用户对应的页面按钮信息
				this.publicService.executeUpdateSql("delete from sys_user_element where element_id=?", Integer.parseInt(string));
				//删除角色对应的按钮
				this.publicService.executeUpdateSql("delete from sys_role_element where element_id=?", Integer.parseInt(string));
				//删除页面按钮信息
				this.publicService.executeUpdateSql("delete from sys_element where id=?", Integer.parseInt(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

}
