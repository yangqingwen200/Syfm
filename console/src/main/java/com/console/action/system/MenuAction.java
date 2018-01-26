package com.console.action.system;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.commons.annotation.OpernationLog;
import com.commons.exception.LogicException;
import com.commons.util.MaptoBean;
import com.commons.bean.system.SysMenu;
import com.console.action.BaseWebAction;

import java.util.Date;

/**
 * 菜单管理增删改查
 * 
 * auth：Yang 2016年2月16日 上午9:40:24
 */
@Controller("system.web.action.menuAction")
@Scope("prototype")
public class MenuAction extends BaseWebAction {
	private static final long serialVersionUID = 562172221263984463L;
	
	/**
	 * 获得菜单列表
	 * 
	 * auth: Yang 2016年2月12日 下午10:36:48
	 */
	public void getMenuList() {
		try {
			StringBuffer sb = new StringBuffer("select id, name, path, remark, disploy, "
					+ "(SELECT mu.name FROM sys_menu mu WHERE mu.id=m.parent) as parentName, parent from sys_menu m where 1=1 ");
			if (dto.getAsInteger("parent") != null) {
				sb.append("and (parent=? or id=?) ");
				fuzzySearch.add(dto.getAsInteger("parent"));
				fuzzySearch.add(dto.getAsInteger("parent"));
			}
			if (dto.getAsInteger("disploy") != null) {
				sb.append("and disploy = ? ");
				fuzzySearch.add(dto.getAsInteger("disploy"));
			}
			if (dto.getAsStringTrim("name") != null) {
				sb.append("and name like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("name") + "%");
			}
			if (dto.getAsStringTrim("path") != null) {
				sb.append("and path like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("path") + "%");
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
	 * 增加菜单
	 * 
	 * auth: Yang 2016年2月15日 下午2:46:28
	 */
	@OpernationLog(cls = SysMenu.class, value = "新增菜单")
	public void addMenu() {
		try {
			this.checkMenuNameReply();
			this.checkMenuPathReply();
			SysMenu menu = MaptoBean.mapToBeanBasic(new SysMenu(), dto);
			menu.setCreateTime(new Date());
			this.publicService.save(menu);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 编辑菜单
	 * 
	 * auth: Yang 2016年2月15日 下午5:18:39
	 */
	@OpernationLog(cls = SysMenu.class, value = "编辑菜单")
	public void editMenu() {
		try {
			if(dto.getAsInteger("id").intValue() == dto.getAsInteger("parent").intValue()) {
				throw new LogicException("菜单不能当爹又当儿子。");
			}
			SysMenu m = this.publicService.load(SysMenu.class, dto.getAsInteger("id"));
			if (!dto.getAsStringTrim("name").equals(m.getName())) {
				this.checkMenuNameReply();
			}
			if (!dto.getAsStringTrim("path").equals(m.getPath())) {
				this.checkMenuPathReply();
			}

			//当前节点含有子节点, 不允许当前节点成为子节点.
			if(dto.getAsInteger("parent").intValue() != 0 && m.getParent().intValue() == 0) {
				int countChild = this.publicService.findSqlCount("select count(*) from sys_menu where parent=?", dto.getAsInteger("id"));
				if(countChild > 0) {
					throw new LogicException("当前菜单含有子菜单<br>请先删除或者移除子菜单。");
				}
			}

			SysMenu menu = MaptoBean.mapToBeanBasic(m, dto);
			this.publicService.update(menu);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 删除菜单信息
	 *
	 * auth: Yang
	 * 2016年3月21日 下午2:32:39
	 */
	@OpernationLog(cls = SysMenu.class, value = "删除菜单")
	public void deleteMenu() {
		try {
			String asString = dto.getAsStringTrim("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				int count = this.publicService.findSqlCount("select count(id) from sys_menu where parent=?", Integer.parseInt(string));
				if(count > 0) {
					throw new LogicException("请先删除子菜单。");
				}
				//删除用户对应菜单信息
				this.publicService.executeUpdateSql("delete from sys_user_menu where menuId=?", Integer.parseInt(string));
				//删除角色对应的菜单
				this.publicService.executeUpdateSql("delete from sys_role_menu where menu_id=?", Integer.parseInt(string));
				
				//删除菜单信息
				this.publicService.executeUpdateSql("delete from sys_menu where id=?", Integer.parseInt(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 *  检查菜单是否重复
	 * 
	 *  2016年5月8日 下午10:23:25
	 */
	public void checkMenuNameReply() {
		int countName = this.publicService.findSqlCount("select count(id) from sys_menu where name=?", dto.getAsStringTrim("name"));
		if (countName > 0) {
			throw new LogicException("菜单名重复，请更换。");
		}
	}
	
	/**
	 * 检查菜单路径是否重复
	 * 
	 *  2016年5月8日 下午10:23:40
	 */
	public void checkMenuPathReply() {
		if(!"#".equals(dto.getAsStringTrim("path"))) {
			int countPath = this.publicService.findSqlCount("select count(id) from sys_menu where path=?", dto.getAsStringTrim("path"));
			if (countPath > 0) {
				throw new LogicException("菜单路径重复，请更换。");
			}
		}
	}

}
