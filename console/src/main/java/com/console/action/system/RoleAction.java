package com.console.action.system;

import com.commons.exception.LogicException;
import com.commons.util.Asyncs;
import com.commons.util.MaptoBean;
import com.commons.bean.system.SysRole;
import com.console.action.BaseWebAction;
import net.sf.json.JSONArray;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色管理增删改查
 * 
 * auth：Yang 2016年2月16日 上午9:40:55
 */
@Controller("system.web.action.roleAction")
@Scope("prototype")
public class RoleAction extends BaseWebAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 获得角色列表
	 * 
	 * auth: Yang 2016年2月16日 上午9:52:59
	 */
	public void getRoleList() {
		try {
			StringBuffer sb = new StringBuffer(
					"SELECT role.id, role.name, role.remark, users.username from sys_role role LEFT JOIN (SELECT sur.role_id, CONCAT(GROUP_CONCAT(su.name)) AS username FROM sys_user su JOIN sys_user_role sur ON sur.user_id = su.id GROUP BY sur.role_id ) AS users ON users.role_id = role.id where 1=1 ");
			if (dto.getAsStringTrim("loginname") != null) {
				sb.append("and users.username like ? ");
				fuzzySearch.add("%" + dto.getAsStringTrim("loginname") + "%");
			}
			if (dto.getAsStringTrim("name") != null) {
				sb.append("and role.name like ? ");
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
	 * 增加角色
	 *
	 * auth: Yang 2016年3月22日 上午8:46:32
	 */
	public void addRole() {
		try {
			this.checkRoleNameReply();
			SysRole role = MaptoBean.mapToBeanBasic(new SysRole(), dto);
			this.publicService.save(role);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 编辑角色
	 *
	 * auth: Yang 2016年3月22日 上午8:48:27
	 */
	public void editRole() {
		try {
			SysRole role = this.publicService.load(SysRole.class, dto.getAsInteger("id"));
			SysRole p = MaptoBean.mapToBeanBasic(new SysRole(), dto);
			if (role.equals(p)) {
				throw new LogicException("角色信息没有任何改变。");
			}
			if (!role.getName().equals(dto.getAsStringTrim("name"))) {
				this.checkRoleNameReply();
			}

			this.publicService.update(p);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 删除角色
	 *
	 * auth: Yang 2016年3月22日 上午8:50:50
	 */
	public void deleteRole() {
		try {
			String ids = dto.getAsStringTrim("id");
			String[] id = ids.split(",");
			Asyncs.execute(() -> {
				for (String string : id) {
					int roledid = Integer.parseInt(string);
					List<Object> user_id = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", roledid);
					// 删除用户分配角色时所有分配给用户的按钮, 权限, 菜单
					for (Object object : user_id) {
						String sql = "SELECT srpm.permission_id FROM sys_role_permission srpm WHERE "
								+ "NOT EXISTS (SELECT 1 FROM sys_role_permission srp JOIN sys_permission sp ON sp.id=srp.permission_id JOIN sys_user_role sur ON sur.role_id=srp.role_id WHERE sur.user_id=? AND srpm.permission_id=srp.permission_id AND srp.role_id!=?) "
								+ "AND srpm.role_id=?";
						List<Object> findBySqlList = this.publicService.findSqlList(sql, object, roledid, roledid);
						this.deletePermissionByUser(findBySqlList.toArray(), object);
						
						sql = "SELECT srpm.menu_id FROM sys_role_menu srpm WHERE "
								+ "NOT EXISTS (SELECT 1 FROM sys_role_menu srp JOIN sys_menu sp ON sp.id=srp.menu_id JOIN sys_user_role sur ON sur.role_id=srp.role_id WHERE sur.user_id=? AND srpm.menu_id=srp.menu_id AND srp.role_id!=?) "
								+ "AND srpm.role_id=?";
						findBySqlList = this.publicService.findSqlList(sql, object, roledid, roledid);
						this.deleteMenuByUser((Integer) object, findBySqlList.toArray());
						
						sql = "SELECT srpm.element_id FROM sys_role_element srpm WHERE "
								+ "NOT EXISTS (SELECT 1 FROM sys_role_element srp JOIN sys_element sp ON sp.id=srp.element_id JOIN sys_user_role sur ON sur.role_id=srp.role_id WHERE sur.user_id=? AND srpm.element_id=srp.element_id AND srp.role_id!=?) "
								+ "AND srpm.role_id=?";
						findBySqlList = this.publicService.findSqlList(sql, object, roledid, roledid);
						this.deleteElementByUser((Integer) object, findBySqlList.toArray());
					}
					
					// 删除角色-按钮
					this.publicService.executeUpdateSql("delete from sys_role_element where role_id=?", roledid);
					// 删除角色-菜单
					this.publicService.executeUpdateSql("delete from sys_role_menu where role_id=?", roledid);
					// 删除角色-权限
					this.publicService.executeUpdateSql("delete from sys_role_permission where role_id=?", roledid);
					// 删除角色-用户
					this.publicService.executeUpdateSql("delete from sys_user_role where role_id=?", roledid);
					// 删除角色
					this.publicService.executeUpdateSql("delete from sys_role where id=?", roledid);
				}
			});
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	private void deleteMenuByUser(int userId, Object[] removeIds) {
		for (Object string : removeIds) {
			int pId = Integer.parseInt(String.valueOf(string));
			List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_menu where id=?)!=0, 1, 0) as result", new Object[] { pId });
			int object = ((BigInteger) findBySqlMap.get(0).get("result")).intValue();
			if (object == 1) {
				this.publicService.executeUpdateSql("delete from sys_user_menu where userId=? and menuId=?", userId, pId);
			} else if (object == 0) {
				List<Object> childs = this.publicService.findSqlList("select p.id from sys_menu p join sys_user_menu up on up.menuId=p.id where p.parent=? and up.userId=?",
						new Object[] { pId, userId });
				for (Object object2 : childs) {
					this.publicService.executeUpdateSql("delete from sys_user_menu where userId=? and menuId=?", userId, object2);
				}
				// this.publicService.executeUpdateBySql("delete from sys_user_menu where userId=? and menuId=?", new Object[]{userId, pId});
			}
		}
	}

	private void deletePermissionByUser(Object[] removeIds, Object userId) {
		for (Object string : removeIds) {
			int pId = Integer.parseInt(String.valueOf(string));
			// 如果当前权限的没有父节点, 则返回0(本身就是父节点)
			// 有父节点则返回1(只考虑两层父子关系), 本身是子节点
			List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_permission where id=?)!=0, 1, 0) as result", new Object[] { pId });
			int object = ((BigInteger) findBySqlMap.get(0).get("result")).intValue();
			if (object == 1) {
				// 不是父节点, 删除本身就可以
				this.publicService.executeUpdateSql("delete from sys_user_permission where userId=? and permissionId=?", userId, pId);
			} else if (object == 0) {
				// 当前节点是父节点, 找到所有的子节点
				List<Object> childs = this.publicService.findSqlList("select p.id from sys_permission p join sys_user_permission up on up.permissionId=p.id where p.parent=? and up.userId=?",
                        pId, userId);
				for (Object object2 : childs) {
					// 逐一删除子节点
					this.publicService.executeUpdateSql("delete from sys_user_permission where userId=? and permissionId=?", userId, object2);
				}
				// 删除父节点 (父节点可以保留)
				// this.publicService.executeUpdateBySql("delete from sys_user_permission where userId=? and permissionId=?", new Object[]{userId, pId});
			}
		}
	}

	private void deleteElementByUser(int userId, Object[] removeIds) {
		for (Object string : removeIds) {
			int pId = Integer.parseInt(String.valueOf(string));
			List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_element where id=?)!=0, 1, 0) as result", new Object[] { pId });
			int object = ((BigInteger) findBySqlMap.get(0).get("result")).intValue();
			if (object == 1) {
				this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", userId, pId);
			} else if (object == 0) {
				List<Object> childs = this.publicService.findSqlList("select p.id from sys_element p join sys_user_element up on up.element_id=p.id where p.parent=? and up.user_id=?",
						new Object[] { pId, userId });
				for (Object object2 : childs) {
					this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", userId, object2);
				}
				// this.publicService.executeUpdateBySql("delete from sys_user_element where user_id=? and element_id=?", new Object[]{userId, pId});
			}
		}
	}

	/**
	 * 获得角色拥有的权限, 菜单, 页面按钮
	 *
	 * auth: Yang 2016年3月22日 下午3:57:20
	 */
	public void getPMEByRole() {
		List<Map<String, Object>> listElment = new ArrayList<Map<String, Object>>();
		try {
			String flag = dto.getAsStringTrim("flag");
			String param = dto.getAsStringTrim("param");
			if ("permission".equals(flag)) {
				if ("add".equals(param)) {
					listElment = this.publicService.findSqlListMap(
							"select p.id, p.permission as text, "
									+ "if((select count(pp.id) from sys_permission pp where pp.parent=p.id)>0, '1', '0') as child from sys_permission p where p.id in (select distinct p.parent from sys_permission p where p.id in (select up.permission_id from sys_role_permission up where up.role_id=? and up.permission_id=p.id) and p.parent !=0)",
							new Object[] { dto.getAsInteger("userId") });
					for (Map<String, Object> map : listElment) {
						if ("1".equals(String.valueOf(map.get("child")))) {
							map.put("children",
									this.publicService.findSqlListMap(
											"select p.id, p.permission as text from sys_permission p join sys_role_permission up on up.permission_id=p.id where p.parent=? and up.role_id=?",
                                            map.get("id"), dto.getAsInteger("userId")));
						}
					}
				} else if ("revoke".equals(param)) {
					listElment = this.publicService.findSqlListMap(
							"select p.id, p.permission as text, "
									+ "if((select count(pp.id) from sys_permission pp where pp.parent=p.id)>0, '1', '0') as child from sys_permission p where p.id in (select distinct p.parent from sys_permission p where not exists (select 1 from sys_role_permission up where up.role_id=? and up.permission_id=p.id) and p.parent !=0)",
							new Object[] { dto.getAsInteger("userId") });
					for (Map<String, Object> map : listElment) {
						if ("1".equals(String.valueOf(map.get("child")))) {
							map.put("children",
									this.publicService.findSqlListMap(
											"select p.id, p.permission as text from sys_permission p where p.parent=? and not exists (select 1 from sys_role_permission up where p.id=up.permission_id and up.role_id=?)",
                                            map.get("id"), dto.getAsInteger("userId")));
						}
					}
				}
			} else if ("menu".equals(flag)) {
				if ("add".equals(param)) {
					listElment = this.publicService.findSqlListMap(
							"select p.id, p.name as text, "
									+ "if((select count(pp.id) from sys_menu pp where pp.parent=p.id)>0, '1', '0') as child from sys_menu p where p.id in (select distinct p.parent from sys_menu p where p.id in (select up.menu_id from sys_role_menu up where up.role_id=? and up.menu_id=p.id) and p.parent !=0 and p.disploy=1) order by p.create_time",
							new Object[] { dto.getAsInteger("userId") });
					for (Map<String, Object> map : listElment) {
						if ("1".equals(String.valueOf(map.get("child")))) {
							map.put("children",
									this.publicService.findSqlListMap(
											"select p.id, p.name as text from sys_menu p join sys_role_menu up on up.menu_id=p.id where p.parent=? and up.role_id=? and p.disploy=1 order by p.create_time",
                                            map.get("id"), dto.getAsInteger("userId")));
						}
					}
				} else if ("revoke".equals(param)) {
					listElment = this.publicService.findSqlListMap(
							"select p.id, p.name as text, "
									+ "if((select count(pp.id) from sys_menu pp where pp.parent=p.id)>0, '1', '0') as child from sys_menu p where p.id in (select distinct p.parent from sys_menu p where not exists (select 1 from sys_role_menu up where up.role_id=? and up.menu_id=p.id) and p.parent !=0 and p.disploy=1) order by p.create_time",
							new Object[] { dto.getAsInteger("userId") });
					for (Map<String, Object> map : listElment) {
						if ("1".equals(String.valueOf(map.get("child")))) {
							map.put("children",
									this.publicService.findSqlListMap(
											"select p.id, p.name as text from sys_menu p where p.parent=? and p.disploy=1 and not exists (select 1 from sys_role_menu up where p.id=up.menu_id and up.role_id=? and p.disploy=1) order by p.create_time",
                                            map.get("id"), dto.getAsInteger("userId")));
						}
					}
				}
			} else if ("element".equals(flag)) {
				if ("add".equals(param)) {
					listElment = this.publicService.findSqlListMap(
							"select p.id, p.description as text, "
									+ "if((select count(pp.id) from sys_element pp where pp.parent=p.id)>0, '1', '0') as child from sys_element p where p.id in (select distinct p.parent from sys_element p where p.id in (select up.element_id from sys_role_element up where up.role_id=? and up.element_id=p.id) and p.parent !=0 and p.disabled=1) order by p.create_time",
							new Object[] { dto.getAsInteger("userId") });
					for (Map<String, Object> map : listElment) {
						if ("1".equals(String.valueOf(map.get("child")))) {
							map.put("children",
									this.publicService.findSqlListMap(
											"select p.id, p.description as text from sys_element p join sys_role_element up on up.element_id=p.id where p.disabled=1 and p.parent=? and up.role_id=? order by p.create_time",
                                            map.get("id"), dto.getAsInteger("userId")));
						}
					}
				} else if ("revoke".equals(param)) {
					listElment = this.publicService.findSqlListMap(
							"select p.id, p.description as text, "
									+ "if((select count(pp.id) from sys_element pp where pp.parent=p.id)>0, '1', '0') as child from sys_element p where p.id in (select distinct p.parent from sys_element p where not exists (select 1 from sys_role_element up where up.role_id=? and up.element_id=p.id) and p.parent !=0 and p.disabled=1) order by p.create_time",
							new Object[] { dto.getAsInteger("userId") });
					for (Map<String, Object> map : listElment) {
						if ("1".equals(String.valueOf(map.get("child")))) {
							map.put("children",
									this.publicService.findSqlListMap(
											"select p.id, p.description as text from sys_element p where p.parent=? and p.disabled=1 and not exists (select 1 from sys_role_element up where p.id = up.element_id and up.role_id=?) order by p.create_time",
                                            map.get("id"), dto.getAsInteger("userId")));
						}
					}
				}
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printString(String.valueOf(JSONArray.fromObject(listElment)));
		}
	}

	/**
	 * 给角色分配/收回权限,菜单,页面按钮
	 * 
	 * <p><strong>注意: 思路一样的, 代码有点累赘, 没有去封装</strong></p>
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月26日
	 */
	public void addOrRevokePMEByRole() {
		try {
			int userId = dto.getAsInteger("id");
			String flagUrl = dto.getAsStringTrim("flag");
			String removes = dto.getAsStringTrim("remove");
			String adds = dto.getAsStringTrim("add");
			// 找到该角色的所有用户
			List<Object> user_id = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", userId);
			if ("permission".equals(flagUrl)) {
				Asyncs.execute(() -> {
					if (removes != null) {
						String[] removeIds = removes.split(",");
						for (String string : removeIds) {
							int pId = Integer.parseInt(string);
							
							// 如果当前权限的没有父节点, 则返回0(本身就是父节点)
							// 有父节点则返回1(只考虑两层父子关系), 本身是子节点
							List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_permission where id=?)!=0, 1, 0) as result",
									new Object[] { pId });
							int object = ((BigInteger) findBySqlMap.get(0).get("result")).intValue();
							if (object == 1) {
								this.revokePermissionByRole(userId, pId);
							} else if (object == 0) {
								// 当前节点是父节点, 找到所有的子节点
								List<Object> childs = this.publicService.findSqlList(
										"select p.id from sys_permission p join sys_role_permission up on up.permission_id=p.id where p.parent=? and up.role_id=?", new Object[] { pId, userId });
								for (Object object2 : childs) {
									this.revokePermissionByRole(userId, object2);
								}
							}
						}
					}
					
					if (adds != null) {
						String[] addIds = adds.split(",");
						for (String string : addIds) {
							int pId = Integer.parseInt(string);
							/*
							 * 1.找到该节点的父节点 2.如果父节点不为0(说明是子节点) a.先到中间表查询该节点的父节点是否存在, 如果存在, 返回-1, 后续操作只要向中间表中插入自身即可 如果不存在, 返回该节点的父节点, 后续操作, 先向中间表中插入父节点, 然后在插入子节点. 3.如果父节点为0(说明是本身父节点) a.先到中间表中查询该节点是否存在,
							 * 如果存在,返回0, 后续不做任何操作. 如果不存在, 返回-1, 后续操作只要向中间表中插入自身即可
							 *
							 * 说明: 前台页面不可能只会传一个父节点过来, 肯定会有一个父节点带一个子节点 easyui combotree特性所在, 如果tree只有一个子节点, 选中子节点, 会把父节点也会选中.
							 */
							List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap(
									"select if((select parent from sys_permission where id=?)!=0, "
											+ "if((select count(up.id) from sys_role_permission up join sys_permission p on up.permission_id=p.id where p.id=(select parent from sys_permission where id=?) and up.role_id=?)!=0, -1, (SELECT parent FROM sys_permission WHERE id=?)),"
											+ "if((select count(up.id) from sys_role_permission up join sys_permission p on up.permission_id=p.id where p.id=? and up.role_id=?)!=0, 0, -1)) as result",
											new Object[] { pId, pId, userId, pId, pId, userId });
							Map<String, Object> map = findBySqlMap.get(0);
							int object = ((BigInteger) map.get("result")).intValue();
							if (object > 0) {
								int count1 = this.publicService.findSqlCount("select count(*) from sys_role_permission where role_id=? and permission_id=?", userId, object);
								if (count1 == 0) {
									this.publicService.executeUpdateSql("insert into sys_role_permission (role_id, permission_id) values (?, ?)", userId, object);
								}
								for (Object object3 : user_id) {
									// 插入权限父节点
									int count = this.publicService.findSqlCount("select count(*) from sys_user_permission where userid=? and permissionid=?", object3, object);
									if (count == 0) {
										this.publicService.executeUpdateSql("insert into sys_user_permission (userid, permissionid) values (? ,?)", object3, object);
									}
								}
							}
							int count1 = this.publicService.findSqlCount("select count(*) from sys_role_permission where role_id=? and permission_id=?", userId, pId);
							if (count1 == 0) {
								this.publicService.executeUpdateSql("insert into sys_role_permission (role_id, permission_id) values (?, ?)", userId, pId);
							}
							
							for (Object object3 : user_id) {
								count1 = this.publicService.findSqlCount("select count(*) from sys_user_permission where userid=? and permissionid=?", object3, pId);
								if (count1 == 0) {
									this.publicService.executeUpdateSql("insert into sys_user_permission (userid, permissionid) values (? ,?)", object3, pId);
								}
							}
						}
					}
				});

			} else if ("menu".equals(flagUrl)) {
				Asyncs.execute(() -> {
					if (removes != null) {
						String[] removeIds = removes.split(",");
						for (String string : removeIds) {
							int pId = Integer.parseInt(string);
							List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_menu where id=?)!=0, 1, 0) as result", new Object[] { pId });
							int object = ((BigInteger) findBySqlMap.get(0).get("result")).intValue();
							if (object == 1) {
								this.revokeMenuByRole(userId, pId);
							} else if (object == 0) {
								List<Object> childs = this.publicService.findSqlList("select p.id from sys_menu p join sys_role_menu up on up.menu_id=p.id where p.parent=? and up.role_id=?",
										new Object[] { pId, userId });
								for (Object object2 : childs) {
									this.revokeMenuByRole(userId, object2);
								}
							}
						}
					}
					
					if (adds != null) {
						String[] addIds = adds.split(",");
						for (String string : addIds) {
							int pId = Integer.parseInt(string);
							List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap(
									"select if((select parent from sys_menu where id=?)!=0, "
											+ "if((select count(up.id) from sys_role_menu up join sys_menu p on up.menu_id=p.id where p.id=(select parent from sys_menu where id=?) and up.role_id=?)!=0, -1, (SELECT parent FROM sys_menu WHERE id=?)),"
											+ "if((select count(up.id) from sys_role_menu up join sys_menu p on up.menu_id=p.id where p.id=? and up.role_id=?)!=0, 0, -1)) as result",
											new Object[] { pId, pId, userId, pId, pId, userId });
							Map<String, Object> map = findBySqlMap.get(0);
							int object = ((BigInteger) map.get("result")).intValue();
							if (object > 0) {
								int count1 = this.publicService.findSqlCount("select count(*) from sys_role_menu where role_id=? and menu_id=?", userId, object);
								if (count1 == 0) {
									this.publicService.executeUpdateSql("insert into sys_role_menu (role_id, menu_id) values (?, ?)", userId, object);
								}
								for (Object object3 : user_id) {
									// 插入菜单父节点
									int count = this.publicService.findSqlCount("select count(*) from sys_user_menu where userid=? and menuid=?", object3, object);
									if (count == 0) {
										this.publicService.executeUpdateSql("insert into sys_user_menu (userid, menuid) values (?, ?)", object3, object);
									}
								}
							}
							int count1 = this.publicService.findSqlCount("select count(*) from sys_role_menu where role_id=? and menu_id=?", userId, pId);
							if (count1 == 0) {
								this.publicService.executeUpdateSql("insert into sys_role_menu (role_id, menu_id) values (?, ?)", userId, pId);
							}
							for (Object object3 : user_id) {
								count1 = this.publicService.findSqlCount("select count(*) from sys_user_menu where userid=? and menuid=?", object3, pId);
								if (count1 == 0) {
									this.publicService.executeUpdateSql("insert into sys_user_menu (userid, menuid) values (? ,?)", object3, pId);
								}
							}
						}
					}
				});
			} else if ("element".equals(flagUrl)) {
				Asyncs.execute(() -> {
					if (removes != null) {
						String[] removeIds = removes.split(",");
						for (String string : removeIds) {
							int pId = Integer.parseInt(string);
							Map<String, Object> findBySqlMap = this.publicService.findSqlMap("select if((select parent from sys_element where id=?)!=0, 1, 0) as result", pId);
							int object = ((BigInteger) findBySqlMap.get("result")).intValue();
							if (object == 1) {
								this.revokeElementByRole(userId, pId);
							} else if (object == 0) {
								List<Object> childs = this.publicService.findSqlList("select p.id from sys_element p join sys_role_element up on up.element_id=p.id where p.parent=? and up.role_id=?",
										new Object[] { pId, userId });
								for (Object object2 : childs) {
									this.revokeMenuByRole(userId, object2);
								}
							}
						}
					}
					
					if (adds != null) {
						String[] addIds = adds.split(",");
						for (String string : addIds) {
							int pId = Integer.parseInt(string);
							List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap(
									"select if((select parent from sys_element where id=?)!=0, "
											+ "if((select count(up.id) from sys_role_element up join sys_element p on up.element_id=p.id where p.id=(select parent from sys_element where id=?) and up.role_id=?)!=0, -1, (SELECT parent FROM sys_element WHERE id=?)),"
											+ "if((select count(up.id) from sys_role_element up join sys_element p on up.element_id=p.id where p.id=? and up.role_id=?)!=0, 0, -1)) as result",
											new Object[] { pId, pId, userId, pId, pId, userId });
							Map<String, Object> map = findBySqlMap.get(0);
							int object = ((BigInteger) map.get("result")).intValue();
							if (object > 0) {
								int count = this.publicService.findSqlCount("select count(*) from sys_role_element where role_id=? and element_id=?", userId, object);
								if (count == 0) {
									this.publicService.executeUpdateSql("insert into sys_role_element (role_id, element_id) values (?, ?)", userId, object);
								}
								for (Object object3 : user_id) {
									// 插入菜单父节点
									int count1 = this.publicService.findSqlCount("select count(*) from sys_user_element where user_id=? and element_id=?", object3, object);
									if (count1 == 0) {
										this.publicService.executeUpdateSql("insert into sys_user_element (user_id, element_id) values (? ,?)", object3, object);
									}
								}
							}
							int count = this.publicService.findSqlCount("select count(*) from sys_role_element where role_id=? and element_id=?", userId, pId);
							if (count == 0) {
								this.publicService.executeUpdateSql("insert into sys_role_element (role_id, element_id) values (?, ?)", userId, pId);
							}
							for (Object object3 : user_id) {
								count = this.publicService.findSqlCount("select count(*) from sys_user_element where user_id=? and element_id=?", object3, pId);
								if (count == 0) {
									this.publicService.executeUpdateSql("insert into sys_user_element (user_id, element_id) values (? ,?)", object3, pId);
								}
							}
						}
						
					}
				});
			}

		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * auth: Yang 2016年8月4日 下午8:33:45
	 * 
	 * @param userId
	 * @param pId
	 */
	private void revokePermissionByRole(Object userId, Object pId) {
		// 查出该菜单是否还在其他角色当中
		int count = this.publicService.findSqlCount("SELECT COUNT(*) FROM sys_role_permission srp WHERE srp.role_id!=? AND permission_id=?", userId, pId);
		if (count > 0) {
			// 根据菜单, 找到其他所有角色id
			List<Object> findSqlListMap = this.publicService.findSqlList("SELECT srp.role_id FROM sys_role_permission srp WHERE srp.role_id!=? AND permission_id=?", userId, pId);
			for (Object object : findSqlListMap) {
				// 找到其他角色是否有分配其他用户
				int count1 = this.publicService.findSqlCount("select count(*) from sys_user_role where role_id=?", object);
				if (count1 > 0) {

					// 如果有分配,
					// 判断要修改的角色和其他角色是不是分配给同一个用户
					String sql = "SELECT COUNT(*) FROM (SELECT user_id FROM `sys_user_role`  WHERE role_id=? OR role_id=? GROUP BY user_id) AS result";
					int countResult = this.publicService.findSqlCount(sql, object, userId);
					// 如果不是同一个用户
					// 直接到找到拥有要修改角色 用户id, 直接从用户菜单中间表删除
					if (countResult > 1) {
						// 根据要修改的角色id,找到所有已分配用户id, 剔除掉同时拥有修改的角色和其他角色的用户id
						String sql2 = "SELECT sur.`user_id` FROM `sys_user_role` sur WHERE sur.role_id=? AND NOT EXISTS (SELECT 1 FROM sys_user_role su WHERE su.role_id=? AND su.`user_id`=sur.user_id)";
						List<Object> findSqlList = this.publicService.findSqlList(sql2, userId, object);
						for (Object object1 : findSqlList) {
							// 依次从用户页面菜单中间中删除菜单
							this.publicService.executeUpdateSql("delete from sys_user_permission where userid=? and permissionid=?", object1, pId);
						}
					} else {
						// 如果是同一个用户, 不做任何处理
						// 最后直接从要修改的角色里面删除菜单即可
					}

					this.publicService.executeUpdateSql("delete from sys_role_permission where permission_id=? and role_id=?", pId, userId);

				} else {
					// 如果没有分配
					// 根据要修改的角色id,找到所有已分配用户id
					List<Object> findSqlList = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", userId);
					for (Object object1 : findSqlList) {
						// 依次从用户页面菜单中间中删除菜单
						this.publicService.executeUpdateSql("delete from sys_user_permission where userid=? and permissionid=?", object1, pId);
					}
					// 最后直接从想要删除的角色里面删除菜单, 其他角色不删除, 保持不变
					this.publicService.executeUpdateSql("delete from sys_role_permission where permission_id=? and role_id=?", pId, userId);
				}
			}

		} else {
			/*
			 * 若不在其他角色当中 判断该角色有没有分配给用户
			 */
			int count1 = this.publicService.findSqlCount("select count(*) from sys_user_role where role_id=?", userId);
			if (count1 > 0) {

				// 有分配给其他用户
				// 根据角色id,找到所有已分配用户id
				List<Object> findSqlList = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", userId);
				for (Object object : findSqlList) {
					// 依次从用户页面菜单中间中删除菜单
					this.publicService.executeUpdateSql("delete from sys_user_permission where userid=? and permissionid=?", object, pId);
				}
				// 最后把菜单从角色里面删除
				this.publicService.executeUpdateSql("delete from sys_role_permission where permission_id=? and role_id=?", pId, userId);
			} else {

				// 没有分配给用户直接把菜单从角色里面删除
				this.publicService.executeUpdateSql("delete from sys_role_permission where permission_id=? and role_id=?", pId, userId);
			}
		}
	}

	/**
	 * auth: Yang 2016年8月4日 下午8:33:45
	 * 
	 * @param userId
	 * @param pId
	 */
	private void revokeMenuByRole(Object userId, Object pId) {
		// 查出该菜单是否还在其他角色当中
		int count = this.publicService.findSqlCount("SELECT COUNT(*) FROM sys_role_menu srp WHERE srp.role_id!=? AND menu_id=?", userId, pId);
		if (count > 0) {
			// 根据菜单, 找到其他所有角色id
			List<Object> findSqlListMap = this.publicService.findSqlList("SELECT srp.role_id FROM sys_role_menu srp WHERE srp.role_id!=? AND menu_id=?", userId, pId);
			for (Object object : findSqlListMap) {
				// 找到其他角色是否有分配其他用户
				int count1 = this.publicService.findSqlCount("select count(*) from sys_user_role where role_id=?", object);
				if (count1 > 0) {

					// 如果有分配,
					// 判断要修改的角色和其他角色是不是分配给同一个用户
					String sql = "SELECT COUNT(*) FROM (SELECT user_id FROM `sys_user_role`  WHERE role_id=? OR role_id=? GROUP BY user_id) AS result";
					int countResult = this.publicService.findSqlCount(sql, object, userId);
					// 如果不是同一个用户
					// 直接到找到拥有要修改角色 用户id, 直接从用户菜单中间表删除
					if (countResult > 1) {
						// 根据要修改的角色id,找到所有已分配用户id, 剔除掉同时拥有修改的角色和其他角色的用户id
						String sql2 = "SELECT sur.`user_id` FROM `sys_user_role` sur WHERE sur.role_id=? AND NOT EXISTS (SELECT 1 FROM sys_user_role su WHERE su.role_id=? AND su.`user_id`=sur.user_id)";
						List<Object> findSqlList = this.publicService.findSqlList(sql2, userId, object);
						for (Object object1 : findSqlList) {
							// 依次从用户页面菜单中间中删除菜单
							this.publicService.executeUpdateSql("delete from sys_user_menu where userid=? and menuid=?", object1, pId);
						}
					} else {
						// 如果是同一个用户, 不做任何处理
						// 最后直接从要修改的角色里面删除菜单即可
					}

					this.publicService.executeUpdateSql("delete from sys_role_menu where menu_id=? and role_id=?", pId, userId);

				} else {
					// 如果没有分配
					// 根据要修改的角色id,找到所有已分配用户id
					List<Object> findSqlList = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", userId);
					for (Object object1 : findSqlList) {
						// 依次从用户页面菜单中间中删除菜单
						this.publicService.executeUpdateSql("delete from sys_user_menu where userid=? and menuid=?", object1, pId);
					}
					// 最后直接从想要删除的角色里面删除菜单, 其他角色不删除, 保持不变
					this.publicService.executeUpdateSql("delete from sys_role_menu where menu_id=? and role_id=?", pId, userId);
				}
			}

		} else {
			/*
			 * 若不在其他角色当中 判断该角色有没有分配给用户
			 */
			int count1 = this.publicService.findSqlCount("select count(*) from sys_user_role where role_id=?", userId);
			if (count1 > 0) {

				// 有分配给其他用户
				// 根据角色id,找到所有已分配用户id
				List<Object> findSqlList = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", userId);
				for (Object object : findSqlList) {
					// 依次从用户页面菜单中间中删除菜单
					this.publicService.executeUpdateSql("delete from sys_user_menu where userid=? and menuid=?", object, pId);
				}
				// 最后把菜单从角色里面删除
				this.publicService.executeUpdateSql("delete from sys_role_menu where menu_id=? and role_id=?", pId, userId);
			} else {

				// 没有分配给用户直接把菜单从角色里面删除
				this.publicService.executeUpdateSql("delete from sys_role_menu where menu_id=? and role_id=?", pId, userId);
			}
		}
	}

	/**
	 * auth: Yang 2016年8月4日 下午8:33:45
	 * 
	 * @param userId
	 *            角色id
	 * @param pId
	 */
	private void revokeElementByRole(Object userId, Object pId) {
		// 查出该元素是否还在其他角色当中
		int count = this.publicService.findSqlCount("SELECT COUNT(*) FROM sys_role_element srp WHERE srp.role_id!=? AND element_id=?", userId, pId);
		if (count > 0) {
			// 根据元素, 找到其他所有角色id
			List<Object> findSqlListMap = this.publicService.findSqlList("SELECT srp.role_id FROM sys_role_element srp WHERE srp.role_id!=? AND element_id=?", userId, pId);
			for (Object object : findSqlListMap) {
				// 找到其他角色是否有分配其他用户
				int count1 = this.publicService.findSqlCount("select count(*) from sys_user_role where role_id=?", object);
				if (count1 > 0) {

					// 如果有分配,
					// 判断要修改的角色和其他角色是不是分配给同一个用户
					String sql = "SELECT COUNT(*) FROM (SELECT user_id FROM `sys_user_role`  WHERE role_id=? OR role_id=? GROUP BY user_id) AS result";
					int countResult = this.publicService.findSqlCount(sql, object, userId);
					// 如果不是同一个用户
					// 直接到找到拥有要修改角色 用户id, 直接从用户菜单中间表删除
					if (countResult > 1) {
						// 根据要修改的角色id,找到所有已分配用户id, 剔除掉同时拥有修改的角色和其他角色的用户id
						String sql2 = "SELECT sur.`user_id` FROM `sys_user_role` sur WHERE sur.role_id=? AND NOT EXISTS (SELECT 1 FROM sys_user_role su WHERE su.role_id=? AND su.`user_id`=sur.user_id)";
						List<Object> findSqlList = this.publicService.findSqlList(sql2, userId, object);
						for (Object object1 : findSqlList) {
							// 依次从用户页面菜单中间中删除菜单
							this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", object1, pId);
						}
					} else {
						// 如果是同一个用户, 不做任何处理
						// 最后直接从要修改的角色里面删除菜单即可
					}

					this.publicService.executeUpdateSql("delete from sys_role_element where element_id=? and role_id=?", pId, userId);

				} else {
					// 如果没有分配
					// 根据要修改的角色id,找到所有已分配用户id
					List<Object> findSqlList = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", userId);
					for (Object object1 : findSqlList) {
						// 依次从用户页面元素中间中删除元素
						this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", object1, pId);
					}
					// 最后直接从想要删除的角色里面删除元素, 其他角色不删除, 保持不变
					this.publicService.executeUpdateSql("delete from sys_role_element where element_id=? and role_id=?", pId, userId);
				}
			}

		} else {
			/*
			 * 若不在其他角色当中 判断该角色有没有分配给用户
			 */
			int count1 = this.publicService.findSqlCount("select count(*) from sys_user_role where role_id=?", userId);
			if (count1 > 0) {

				// 有分配给其他用户
				// 根据角色id,找到所有已分配用户id
				List<Object> findSqlList = this.publicService.findSqlList("select user_id from sys_user_role where role_id=?", userId);
				for (Object object : findSqlList) {
					// 依次从用户页面元素中间中删除元素
					this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", object, pId);
				}
				// 最后把元素从角色里面删除
				this.publicService.executeUpdateSql("delete from sys_role_element where element_id=? and role_id=?", pId, userId);
			} else {

				// 没有分配给用户直接把元素从角色里面删除
				this.publicService.executeUpdateSql("delete from sys_role_element where element_id=? and role_id=?", pId, userId);
			}
		}
	}

	/**
	 * 检查角色名是否重复
	 */
	public void checkRoleNameReply() {
		int count = this.publicService.findSqlCount("select count(id) from sys_role where name=?", dto.getAsStringTrim("name"));
		if (count > 0) {
			throw new LogicException("角色名重复，请更换。");
		}
	}
}
