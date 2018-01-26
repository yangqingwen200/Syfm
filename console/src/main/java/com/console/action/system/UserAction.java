package com.console.action.system;

import com.commons.annotation.OpernationLog;
import com.commons.bean.system.SysLogLogin;
import com.commons.bean.system.SysMenu;
import com.commons.bean.system.SysUser;
import com.commons.exception.LogicException;
import com.commons.util.Asyncs;
import com.commons.util.MD5Encoder;
import com.commons.util.MaptoBean;
import com.commons.util.core.WebUtils;
import com.console.action.BaseWebAction;
import com.console.constant.HQLConstant;
import com.console.constant.SQLConstant;
import com.console.constant.ConsoleConstant;
import com.console.enums.ConsoleSqlEnum;
import com.console.listener.SessionListener;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * 用户的登陆功能，用户的权限分配，用户的菜单分配功能模块
 *
 * @version 1.0, 2014-7-1/上午09:24:39
 */
@Controller("system.web.action.userAction")
@Scope("prototype")
public class UserAction extends BaseWebAction {

    private static final long serialVersionUID = 1081681641584848916L;

    public static final ConcurrentMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();

    /**
     * 网页端系统退出登录
     *
     * @author Yang
     * @version v1.0
     * @date 2016年11月17日
     */
    public void loginout() {
        try {
            this.removeSessionAttr();
            map.put(session.getId(), 0);
            session.invalidate(); //使当前用户session无效
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 后台管理系统登录
     * <p>
     * Yang
     * 2016年2月5日 下午3:57:31
     */
    public void login() {
        try {
            if (map.containsKey(session.getId())) {
                throw new LogicException("当前窗口已有用户登录，不能再登。<br>请关闭浏览器后重新登录。");
            }
            List<SysUser> u_list = publicService.findHqlList("select new SysUser(id, loginname, pw, status, name) from SysUser where loginname=? and pw=?", new Object[]{dto.getAsStringTrim("name"), dto.getAsStringTrim("pw")});
            if (u_list.size() == 1) {
                SysUser su = u_list.get(0);
                if ("1".equals(su.getStatus())) {
                    Map<String, Object> userPermission = this.getUserPermission(su.getId());
                    Map<String, Object> userPermissionSession = new LinkedHashMap<String, Object>();
                    List<Object[]> listElment = new ArrayList<Object[]>();
                    this.getUserPermissionMsg(userPermission, userPermissionSession, listElment);

                    //先移除原有的session中用户,菜单,权限信息
                    this.removeSessionAttr();
                    session.setAttribute("onlineUserBindingListener", new SessionListener(su.getId(), publicService));
                    session.setAttribute("user", su); //放入用户信息
                    session.setAttribute("upp", userPermissionSession); //放入用户对应的权限信息
                    session.setAttribute("menus", this.getUserMenus(su.getId()));  //放入用户对应的菜单信息
                    Asyncs.submit(() -> {
                        session.setAttribute("eles", this.getUserPermissonElement(listElment, su.getId()));  //放入用户对应的按钮信息
                    });

                    //保存登录日志
                    String ip = WebUtils.getClientIp(this.request);
                    Asyncs.submit(() -> {
                        SysLogLogin sll = new SysLogLogin();
                        sll.setUserId(su.getId());
                        sll.setIpAddress(ip);
                        sll.setLoginName(su.getLoginname());
                        sll.setLoginTime(new Date());
                        sll.setSessionId(session.getId());
                        sll.setLogoutType("在线当中");
                        this.publicService.save(sll);
                    });

                    map.put(session.getId(), 1);
                } else {
                    throw new LogicException("此用户已被禁用，请解禁后再登录！");
                }
            } else {
                throw new LogicException("登录名或密码错误，请检查。");
            }
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson();
        }
    }

    /**
     * 获得后台管理用户列表
     * <p>
     * Yang
     * 2016年2月5日 下午8:31:12
     */
    @SuppressWarnings("unchecked")
    public void getWebUserList() {
        try {
            //如果不是admin用户登录. 用户列表里面不显示admin超级管理员用户
            SysUser su = (SysUser) session.getAttribute("user");
            if (!"admin".equals(su.getLoginname())) {
                dto.put("admin", "admin");
            }
            currentpage = this.publicService.pagedQuerySqlFreemarker(ConsoleSqlEnum.FIND_WEB_USER_WEB, dto);
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
     * 新增Web用户
     * <p>
     * Yang
     * 2016年2月6日 下午10:08:52
     */
    @OpernationLog(cls = SysUser.class, value = "新增Web用户")
    public void addWebUser() {
        try {
            int count = this.publicService.findHqlCount("select count(id) from SysUser where loginname=?", dto.getAsStringTrim("loginname"));
            if (count > 0) {
                throw new LogicException("用户登录名重复，请更换。");
            }

            SysUser su = MaptoBean.mapToBeanBasic(new SysUser(), dto);
            su.setPw(MD5Encoder.getMD5(su.getPw()));
            this.publicService.save(su);
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 编辑后台管理用户
     * Yang
     * 2016年2月7日 上午11:23:12
     */
    @OpernationLog(cls = SysUser.class, value = "编辑Web用户")
    public void editWebUser() {
        try {
            SysUser su = MaptoBean.mapToBeanBasic(new SysUser(), dto);
            SysUser u = this.publicService.load(SysUser.class, su.getId());
            if (su.equals(u)) {
                throw new LogicException("用户信息没有任何改变。");
            }
            if (u != null) {
                if (!su.getLoginname().equals(u.getLoginname())) {
                    int count = this.publicService.findHqlCount("select count(id) from SysUser where loginname=?", dto.getAsStringTrim("loginname"));
                    if (count > 0) {
                        throw new LogicException("用户登录名重复，请更换。");
                    }
                }
                if (!u.getPw().equals(su.getPw())) {
                    su.setPw(MD5Encoder.getMD5(su.getPw()));
                }
            }
            this.publicService.update(su);
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 删除后台管理用户
     * <p>
     * auth: Yang
     * 2016年3月21日 下午2:32:07
     */
    @OpernationLog(cls = SysUser.class, value = "删除Web用户")
    public void deleteWebUser() {
        try {
            String asString = dto.getAsStringTrim("id");
            String[] ids = asString.split(",");
            for (String string : ids) {
                //删除用户对应的菜单信息
                this.publicService.executeUpdateSql("delete from sys_user_menu where userId=?", Integer.parseInt(string));
                //删除用户对应权限信息
                this.publicService.executeUpdateSql("delete from sys_user_permission where userId=?", Integer.parseInt(string));
                //删除用户对应页面按钮信息
                this.publicService.executeUpdateSql("delete from sys_user_element where user_id=?", Integer.parseInt(string));
                //删除登录日志
                this.publicService.executeUpdateSql("delete from sys_log_login where user_id=?", Integer.parseInt(string));
                //删除操作日志
                this.publicService.executeUpdateSql("delete from sys_log_operation where oparetor_id=?", Integer.parseInt(string));
                //删除删除用户对应的角色
                this.publicService.executeUpdateSql("delete from sys_user_role where user_id=?", Integer.parseInt(string));
                //删除用户
                this.publicService.executeUpdateSql("delete from sys_user where id=?", Integer.parseInt(string));
            }
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 获得用户拥有的权限, 菜单, 页面按钮
     * <p>
     * auth: Yang
     * 2016年3月22日 下午3:57:20
     */
    public void getPMEByUser() {
        List<Map<String, Object>> listElment = new ArrayList<Map<String, Object>>();
        try {
            String flag = dto.getAsStringTrim("flag");
            String param = dto.getAsStringTrim("param");
            if ("permission".equals(flag)) {
                if ("add".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select p.id, p.permission as text, "
                            + "if((select count(pp.id) from sys_permission pp where pp.parent=p.id)>0, '1', '0') as child from sys_permission p where p.id in (select distinct p.parent from sys_permission p where p.id in (select up.permissionid from sys_user_permission up where up.userid=? and up.permissionid=p.id) and p.parent !=0)", new Object[]{dto.getAsInteger("userId")});
                    for (Map<String, Object> map : listElment) {
                        if ("1".equals(String.valueOf(map.get("child")))) {
                            map.put("children", this.publicService.findSqlListMap("select p.id, p.permission as text from sys_permission p join sys_user_permission up on up.permissionid=p.id where p.parent=? and up.userid=?", map.get("id"), dto.getAsInteger("userId")));
                        }
                    }
                } else if ("revoke".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select p.id, p.permission as text, "
                            + "if((select count(pp.id) from sys_permission pp where pp.parent=p.id)>0, '1', '0') as child from sys_permission p where p.id in (select distinct p.parent from sys_permission p where not exists (select 1 from sys_user_permission up where up.userid=? and up.permissionid=p.id) and p.parent !=0)", new Object[]{dto.getAsInteger("userId")});
                    for (Map<String, Object> map : listElment) {
                        if ("1".equals(String.valueOf(map.get("child")))) {
                            map.put("children", this.publicService.findSqlListMap("select p.id, p.permission as text from sys_permission p where p.parent=? and not exists (select 1 from sys_user_permission up where p.id=up.permissionid and up.userid=?)", map.get("id"), dto.getAsInteger("userId")));
                        }
                    }
                }
            } else if ("menu".equals(flag)) {
                if ("add".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select p.id, p.name as text, "
                            + "if((select count(pp.id) from sys_menu pp where pp.parent=p.id)>0, '1', '0') as child from sys_menu p where p.id in (select distinct p.parent from sys_menu p where p.id in (select up.menuid from sys_user_menu up where up.userid=? and up.menuid=p.id) and p.parent !=0 and p.disploy=1) order by p.create_time", new Object[]{dto.getAsInteger("userId")});
                    for (Map<String, Object> map : listElment) {
                        if ("1".equals(String.valueOf(map.get("child")))) {
                            map.put("children", this.publicService.findSqlListMap("select p.id, p.name as text from sys_menu p join sys_user_menu up on up.menuid=p.id where p.parent=? and up.userid=? and p.disploy=1 order by p.create_time", map.get("id"), dto.getAsInteger("userId")));
                        }
                    }
                } else if ("revoke".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select p.id, p.name as text, "
                            + "if((select count(pp.id) from sys_menu pp where pp.parent=p.id)>0, '1', '0') as child from sys_menu p where p.id in (select distinct p.parent from sys_menu p where not exists (select 1 from sys_user_menu up where up.userid=? and up.menuid=p.id) and p.parent !=0 and p.disploy=1) order by p.create_time", new Object[]{dto.getAsInteger("userId")});
                    for (Map<String, Object> map : listElment) {
                        if ("1".equals(String.valueOf(map.get("child")))) {
                            map.put("children", this.publicService.findSqlListMap("select p.id, p.name as text from sys_menu p where p.parent=? and p.disploy=1 and not exists (select 1 from sys_user_menu up where p.id=up.menuid and up.userid=? and p.disploy=1) order by p.create_time", map.get("id"), dto.getAsInteger("userId")));
                        }
                    }
                }
            } else if ("element".equals(flag)) {
                if ("add".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select p.id, p.description as text, "
                            + "if((select count(pp.id) from sys_element pp where pp.parent=p.id)>0, '1', '0') as child from sys_element p where p.id in (select distinct p.parent from sys_element p where p.id in (select up.element_id from sys_user_element up where up.user_id=? and up.element_id=p.id) and p.parent !=0 and p.disabled=1) order by p.create_time", new Object[]{dto.getAsInteger("userId")});
                    for (Map<String, Object> map : listElment) {
                        if ("1".equals(String.valueOf(map.get("child")))) {
                            map.put("children", this.publicService.findSqlListMap("select p.id, p.description as text from sys_element p join sys_user_element up on up.element_id=p.id where p.disabled=1 and p.parent=? and up.user_id=? order by p.create_time", map.get("id"), dto.getAsInteger("userId")));
                        }
                    }
                } else if ("revoke".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select p.id, p.description as text, "
                            + "if((select count(pp.id) from sys_element pp where pp.parent=p.id)>0, '1', '0') as child from sys_element p where p.id in (select distinct p.parent from sys_element p where not exists (select 1 from sys_user_element up where up.user_id=? and up.element_id=p.id) and p.parent !=0 and p.disabled=1) order by p.create_time", new Object[]{dto.getAsInteger("userId")});
                    for (Map<String, Object> map : listElment) {
                        if ("1".equals(String.valueOf(map.get("child")))) {
                            map.put("children", this.publicService.findSqlListMap("select p.id, p.description as text from sys_element p where p.parent=? and p.disabled=1 and not exists (select 1 from sys_user_element up where p.id = up.element_id and up.user_id=?) order by p.create_time", map.get("id"), dto.getAsInteger("userId")));
                        }
                    }
                }
            } else if ("role".equals(flag)) {
                if ("add".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select role.id, role.name as text from sys_role role join sys_user_role sur on sur.role_id=role.id join sys_user user on user.id=sur.user_id where user.id=? ", new Object[]{dto.getAsInteger("userId")});
                } else if ("revoke".equals(param)) {
                    listElment = this.publicService.findSqlListMap("select role.id, role.name as text from sys_role role where not exists (select 1 from sys_user_role up where role.id = up.role_id and up.user_id=?)", new Object[]{dto.getAsInteger("userId")});
                }
            }
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printString(String.valueOf(JSONArray.fromObject(listElment)));
        }
    }

    /**
     * 后台管理系统用户分配/收回权限,菜单,页面按钮, 角色
     * <p>
     * auth: Yang
     * 2016年3月23日 下午3:36:23
     */
    public void addOrRevokePMEByUser() {
        try {
            int userId = dto.getAsInteger("id");
            String flagUrl = dto.getAsStringTrim("flag");
            String removes = dto.getAsStringTrim("remove");
            String adds = dto.getAsStringTrim("add");
            if ("permission".equals(flagUrl)) {
                Asyncs.execute(() -> {
                    if (removes != null) {
                        String[] removeIds = removes.split(",");
                        this.deletePermissionByUser(removeIds, userId, null);
                    }

                    if (adds != null) {
                        String[] addIds = adds.split(",");
                        this.insertPermissionByUser(addIds, userId);
                    }
                });

            } else if ("menu".equals(flagUrl)) {
                Asyncs.execute(() -> {

                    if (removes != null) {
                        String[] removeIds = removes.split(",");
                        this.deleteMenuByUser(userId, removeIds, null);
                    }

                    if (adds != null) {
                        String[] addIds = adds.split(",");
                        this.insertMenuByUser(userId, addIds);
                    }
                });
            } else if ("element".equals(flagUrl)) {
                Asyncs.execute(() -> {
                    if (removes != null) {
                        String[] removeIds = removes.split(",");
                        this.deleteElementByUser(userId, removeIds, null);
                    }

                    if (adds != null) {
                        String[] addIds = adds.split(",");
                        this.insertElementByUser(userId, addIds);
                    }
                });
            } else if ("role".equals(flagUrl)) {
                Asyncs.execute(() -> {
                    if (removes != null) {
                        String[] removeIds = removes.split(",");
                        for (String string : removeIds) {
                            int pId = Integer.parseInt(string);
                            String sql = "SELECT srpm.permission_id FROM sys_role_permission srpm WHERE "
                                    + "NOT EXISTS (SELECT 1 FROM sys_role_permission srp JOIN sys_permission sp ON sp.id=srp.permission_id JOIN sys_user_role sur ON sur.role_id=srp.role_id WHERE sur.user_id=? AND srpm.permission_id=srp.permission_id AND srp.role_id!=?) "
                                    + "AND srpm.role_id=?";
                            List<Object> findBySqlList = this.publicService.findSqlList(sql, userId, pId, pId);
                            this.deletePermissionByUser(findBySqlList.toArray(), userId, pId);

                            sql = "SELECT srpm.menu_id FROM sys_role_menu srpm WHERE "
                                    + "NOT EXISTS (SELECT 1 FROM sys_role_menu srp JOIN sys_menu sp ON sp.id=srp.menu_id JOIN sys_user_role sur ON sur.role_id=srp.role_id WHERE sur.user_id=? AND srpm.menu_id=srp.menu_id AND srp.role_id!=?) "
                                    + "AND srpm.role_id=?";
                            findBySqlList = this.publicService.findSqlList(sql, userId, pId, pId);
                            this.deleteMenuByUser(userId, findBySqlList.toArray(), pId);

                            sql = "SELECT srpm.element_id FROM sys_role_element srpm WHERE "
                                    + "NOT EXISTS (SELECT 1 FROM sys_role_element srp JOIN sys_element sp ON sp.id=srp.element_id JOIN sys_user_role sur ON sur.role_id=srp.role_id WHERE sur.user_id=? AND srpm.element_id=srp.element_id AND srp.role_id!=?) "
                                    + "AND srpm.role_id=?";
                            findBySqlList = this.publicService.findSqlList(sql, userId, pId, pId);
                            this.deleteElementByUser(userId, findBySqlList.toArray(), pId);
                            //删除用户对应的角色
                            this.publicService.executeUpdateSql("delete from sys_user_role where user_id=? and role_id=?", userId, pId);
                        }
                    }

                    if (adds != null) {
                        String[] addIds = adds.split(",");
                        for (String string : addIds) {
                            int pId = Integer.parseInt(string);
                            //找到该角色拥有的权限, 并全部插入用户对应权限的中间表
                            List<Object> findBySqlList = this.publicService.findSqlList("select permission_id from sys_role_permission where role_id=?", pId);
                            this.insertPermissionByUser(findBySqlList.toArray(), userId);

                            //找到该角色拥有的菜单, 并全部插入用户对应菜单的中间表
                            findBySqlList = this.publicService.findSqlList("select menu_id from sys_role_menu where role_id=?", pId);
                            this.insertMenuByUser(userId, findBySqlList.toArray());

                            //找到该角色拥有的按钮, 并全部插入用户对应按钮的中间表
                            findBySqlList = this.publicService.findSqlList("select element_id from sys_role_element where role_id=?", pId);
                            this.insertElementByUser(userId, findBySqlList.toArray());

                            //插入用户对应的角色
                            this.publicService.executeUpdateSql("insert into sys_user_role (user_id, role_id) values (?, ?)", userId, pId);
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
     * 查看后台管理用户详情信息, 树形结构.
     * @author Yang
     * @version v1.0
     * @date 2017/7/19
     */
    public String detailUser() {
        try {
            Integer userId = dto.getAsInteger("id");

            CountDownLatch begin = new CountDownLatch(5);

            Asyncs.submit(() -> {
                SysUser user = null;
                try {
                    String string = cacheRedis.get(ConsoleConstant.USER_DETAIL + userId);
                    if (null != string) {
                        user = (SysUser) JSONObject.toBean(JSONObject.fromObject(string), SysUser.class);
                    } else {
                        user = this.publicService.load(SysUser.class, userId);
                        cacheRedis.set(ConsoleConstant.USER_DETAIL + userId, JSONObject.fromObject(user).toString(), ConsoleConstant.USER_EXPIRE_TIME);
                    }
                    this.request.setAttribute("detailUser", user);
                } finally {
                    begin.countDown();
                }
            });

            Asyncs.submit(() -> {
                try {
                    //得到权限
                    String userP = cacheRedis.get(ConsoleConstant.USER_PERMISSION_PREFIX + userId);
                    if(null == userP) {
                        String permission = "SELECT sp.`id` as id, sp.`permission` as text FROM `sys_permission` sp JOIN `sys_user_permission` sup ON sup.`permissionId`=sp.`id` WHERE sup.`userId`=? AND sp.`parent`=?";
                        List<Object> userDetailP = this.getUserDetailPME(permission, userId);
                        userP = JSONArray.fromObject(userDetailP).toString();
                        cacheRedis.set(ConsoleConstant.USER_PERMISSION_PREFIX + userId, userP, ConsoleConstant.USER_EXPIRE_TIME);
                    }
                    this.request.setAttribute("permission", userP);
                } finally {
                    begin.countDown();
                }
            });

            Asyncs.submit(() -> {
                try {
                    //得到菜单
                    String userM = cacheRedis.get(ConsoleConstant.USER_MENU_PREFIX + userId);
                    if(null == userM) {
                        String menu = "SELECT sm.`id` as id, sm.`name` as text FROM `sys_menu` sm JOIN `sys_user_menu` sume ON sume.`menuId`=sm.`id` WHERE sume.`userId`=? AND sm.`parent`=?";
                        List<Object> userDetailM = this.getUserDetailPME(menu, userId);
                        userM = JSONArray.fromObject(userDetailM).toString();
                        cacheRedis.set(ConsoleConstant.USER_MENU_PREFIX + userId, userM, ConsoleConstant.USER_EXPIRE_TIME);
                    }
                    this.request.setAttribute("menu", userM);
                } finally {
                    begin.countDown();
                }
            });

            Asyncs.submit(() -> {
                try {
                    //得到按钮
                    String userE = cacheRedis.get(ConsoleConstant.USER_ELEMENT_PREFIX + userId);
                    if(null == userE) {
                        String element = "SELECT se.`id`, se.description AS text FROM `sys_element` se JOIN `sys_user_element` sue ON sue.`element_id`=se.`id` WHERE sue.`user_id`=? AND se.`parent`=?";
                        List<Object> userDetailE = this.getUserDetailPME(element, userId);
                        userE = JSONArray.fromObject(userDetailE).toString();
                        cacheRedis.set(ConsoleConstant.USER_ELEMENT_PREFIX + userId, userE, ConsoleConstant.USER_EXPIRE_TIME);
                    }
                    this.request.setAttribute("element", userE);
                } finally {
                    begin.countDown();
                }
            });

            Asyncs.submit(() -> {
                try {
                    //得到角色
                    String userR =  cacheRedis.get(ConsoleConstant.USER_ROLE_PREFIX + userId);
                    if(null == userR) {
                        List<Object> userDetailR = this.publicService.findSqlListMap("SELECT sr.`id`, sr.`name` as text FROM `sys_role` sr JOIN `sys_user_role` sur ON sur.`role_id`=sr.`id` WHERE sur.`user_id`=?", userId);
                        userR = JSONArray.fromObject(userDetailR).toString();
                        cacheRedis.set(ConsoleConstant.USER_ROLE_PREFIX + userId, userR, ConsoleConstant.USER_EXPIRE_TIME);
                    }
                    this.request.setAttribute("role", userR);
                } finally {
                    begin.countDown();
                }
            });

            begin.await();
            return SUCCESS;
        } catch (Exception e) {
            this.checkException(e);
            return ERROR;
        }
    }

    /**
     *
     * @author Yang
     * @version v1.0
     * @date 2017/7/19
     */
    public List<Object> getUserDetailPME(String sql, Object userId) {
        List<Object> child = new ArrayList<Object>();  //用来存放空节点对象
        List<Object> mapParent = this.publicService.findSqlListMap(sql, userId, 0);  //获得父节点
        for (Object obj :  mapParent) {
            Map map = (Map) obj;
            List<Object> mapChildren = this.publicService.findSqlListMap(sql, userId, map.get("id")); //获得子节点
            if(mapChildren.size() == 0) {
                // mapParent.remove(obj);  //无法直接移除, 先存到child中, 最后迭代移除
                child.add(obj);
            } else {
                map.put("children", mapChildren);
            }
        }

        //移动掉没有子节点的对象, 避免前台tree显示一个空的标签
        for(Object obj :  child) {
            mapParent.remove(obj);
        }
        return mapParent;
    }

    /**
     * 插入用户按钮
     *
     * @param userId 2016年8月2日 下午5:19:03
     */
    private void insertElementByUser(int userId, Object[] addIds) {
        for (Object string : addIds) {
            int pId = Integer.parseInt(String.valueOf(string));
            List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_element where id=?)!=0, "
                    + "if((select count(up.id) from sys_user_element up join sys_element p on up.element_id=p.id where p.id=(select parent from sys_element where id=?) and up.user_id=?)!=0, -1, (SELECT parent FROM sys_element WHERE id=?)),"
                    + "if((select count(up.id) from sys_user_element up join sys_element p on up.element_id=p.id where p.id=? and up.user_id=?)!=0, 0, -1)) as result", new Object[]{pId, pId, userId, pId, pId, userId});
            Map<String, Object> map = findBySqlMap.get(0);
            int object = ((BigInteger) map.get("result")).intValue();
            if (object > 0) {
                int count = this.publicService.findSqlCount("select count(*) from sys_user_element where user_id=? and element_id=?", userId, object);
                if (count == 0) {
                    this.publicService.executeUpdateSql("insert into sys_user_element (user_id, element_id) values (?, ?)", userId, object);
                }
            }
            int count = this.publicService.findSqlCount("select count(*) from sys_user_element where user_id=? and element_id=?", userId, pId);
            if (count == 0) {
                this.publicService.executeUpdateSql("insert into sys_user_element (user_id, element_id) values (?, ?)", userId, pId);
            }
        }
    }

    /**
     * 删除用户按钮
     *
     * @param userId 2016年8月2日 下午5:18:23
     */
    private void deleteElementByUser(int userId, Object[] removeIds, Object roleId) {
        for (Object string : removeIds) {
            int pId = Integer.parseInt(String.valueOf(string));
            List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_element where id=?)!=0, 1, 0) as result", new Object[]{pId});
            int object = ((BigInteger) findBySqlMap.get(0).get("result")).intValue();
            if (object == 1) {
                this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", userId, pId);
            } else if (object == 0) {

                String sql = "";
                List<Object> childs = new ArrayList<Object>();
                if (null != roleId) {
                    sql = "select p.id from sys_element p join sys_user_element up on up.element_id=p.id join sys_role_element sre on sre.element_id=p.id where p.parent=? and up.user_id=? and sre.role_id=?";
                    childs = this.publicService.findSqlList(sql, pId, userId, roleId);
                } else {
                    sql = "select p.id from sys_element p join sys_user_element up on up.element_id=p.id where p.parent=? and up.user_id=?";
                    childs = this.publicService.findSqlList(sql, new Object[]{pId, userId});
                }
                for (Object object2 : childs) {
                    this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", userId, object2);
                }
                //this.publicService.executeUpdateSql("delete from sys_user_element where user_id=? and element_id=?", new Object[]{userId, pId});
            }
        }
    }

    /**
     * 插入用户菜单
     *
     * @param userId 2016年8月2日 下午5:17:26
     */
    private void insertMenuByUser(int userId, Object[] addIds) {
        for (Object string : addIds) {
            int pId = Integer.parseInt(String.valueOf(string));
            List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_menu where id=?)!=0, "
                    + "if((select count(up.id) from sys_user_menu up join sys_menu p on up.menuId=p.id where p.id=(select parent from sys_menu where id=?) and up.userid=?)!=0, -1, (SELECT parent FROM sys_menu WHERE id=?)),"
                    + "if((select count(up.id) from sys_user_menu up join sys_menu p on up.menuId=p.id where p.id=? and up.userId=?)!=0, 0, -1)) as result", new Object[]{pId, pId, userId, pId, pId, userId});
            Map<String, Object> map = findBySqlMap.get(0);
            int object = ((BigInteger) map.get("result")).intValue();
            if (object > 0) {
                int count = this.publicService.findSqlCount("select count(*) from sys_user_menu where userid=? and menuid=?", userId, object);
                if (count == 0) {
                    this.publicService.executeUpdateSql("insert into sys_user_menu (userid, menuid) values (?, ?)", userId, object);
                }
            }

            int count = this.publicService.findSqlCount("select count(*) from sys_user_menu where userid=? and menuid=?", userId, pId);
            if (count == 0) {
                this.publicService.executeUpdateSql("insert into sys_user_menu (userid, menuid) values (?, ?)", userId, pId);
            }
        }
    }

    /**
     * 删除用户菜单
     *
     * @param userId 2016年8月2日 下午5:12:38
     */
    private void deleteMenuByUser(int userId, Object[] removeIds, Object id) {
        for (Object string : removeIds) {
            int pId = Integer.parseInt(String.valueOf(string));
            Map<String, Object> findBySqlMap = this.publicService.findSqlMap("select if((select parent from sys_menu where id=?)!=0, 1, 0) as result", pId);
            int object = ((BigInteger) findBySqlMap.get("result")).intValue();
            if (object == 1) {
                this.publicService.executeUpdateSql("delete from sys_user_menu where userId=? and menuId=?", userId, pId);
            } else if (object == 0) {
                String sql = "";
                List<Object> childs = new ArrayList<Object>();
                if (null != id) {
                    /*
					 * 找到该用户拥有的菜单, 并且该菜单有分配给某一个角色
					 */
                    sql = "select p.id from sys_menu p join sys_user_menu up on up.menuId=p.id join sys_role_menu srm on srm.menu_id=p.id where p.parent=? and up.userId=? and srm.role_id=?";
                    childs = this.publicService.findSqlList(sql, pId, userId, id);
                } else {
                    sql = "select p.id from sys_menu p join sys_user_menu up on up.menuId=p.id where p.parent=? and up.userId=?";
                    childs = this.publicService.findSqlList(sql, new Object[]{pId, userId});
                }
                for (Object object2 : childs) {
                    this.publicService.executeUpdateSql("delete from sys_user_menu where userId=? and menuId=?", userId, object2);
                }
                //this.publicService.executeUpdateSql("delete from sys_user_menu where userId=? and menuId=?", new Object[]{userId, pId});
            }
        }
    }

    /**
     * 删除用户权限
     *
     * @param userId 2016年8月2日 下午5:12:38
     */
    private void deletePermissionByUser(Object[] removeIds, Object userId, Object roleId) {
        for (Object string : removeIds) {
            int pId = Integer.parseInt(String.valueOf(string));
            //如果当前权限的没有父节点, 则返回0(本身就是父节点)
            //有父节点则返回1(只考虑两层父子关系), 本身是子节点
            Map<String, Object> findBySqlMap = this.publicService.findSqlMap("select if((select parent from sys_permission where id=?)!=0, 1, 0) as result", pId);
            int object = ((BigInteger) findBySqlMap.get("result")).intValue();
            if (object == 1) {
                //不是父节点, 删除本身就可以
                this.publicService.executeUpdateSql("delete from sys_user_permission where userId=? and permissionId=?", userId, pId);
            } else if (object == 0) {

                String sql = "";
                List<Object> childs = new ArrayList<Object>();
                if (null != roleId) {
                    //当前节点是父节点, 找到所有的子节点, 并且该权限在指定的角色中
                    sql = "select p.id from sys_permission p join sys_user_permission up on up.permissionId=p.id join sys_role_permission srp on srp.permission_id=p.id where p.parent=? and up.userId=? and srp.role_id=?";
                    childs = this.publicService.findSqlList(sql, pId, userId, roleId);
                } else {
                    //当前节点是父节点, 找到所有的子节点
                    sql = "select p.id from sys_permission p join sys_user_permission up on up.permissionId=p.id where p.parent=? and up.userId=?";
                    childs = this.publicService.findSqlList(sql, pId, userId);
                }
                for (Object object2 : childs) {
                    //逐一删除子节点
                    this.publicService.executeUpdateSql("delete from sys_user_permission where userId=? and permissionId=?", userId, object2);
                }
                //删除父节点 (父节点可以保留)
                //this.publicService.executeUpdateSql("delete from sys_user_permission where userId=? and permissionId=?", new Object[]{userId, pId});
            }
        }
    }

    /**
     * 插入用户权限
     *
     * @param userId 2016年8月2日 下午5:12:51
     */
    private void insertPermissionByUser(Object[] addIds, Object userId) {
        for (Object string : addIds) {
            int pId = Integer.parseInt(String.valueOf(string));
			/*
			 * 1.找到该节点的父节点
			 * 2.如果父节点不为0(说明是子节点)
			 * 		a.先到中间表查询该节点的父节点是否存在,
			 * 			如果存在, 返回-1, 后续操作只要向中间表中插入自身即可
			 * 			如果不存在, 返回该节点的父节点, 后续操作, 先向中间表中插入父节点, 然后在插入子节点.
			 * 3.如果父节点为0(说明是本身父节点)
			 * 		a.先到中间表中查询该节点是否存在,
			 * 			如果存在,返回0, 后续不做任何操作.
			 *			如果不存在, 返回-1, 后续操作只要向中间表中插入自身即可
			 *
			 *说明: 前台页面不可能只会传一个父节点过来, 肯定会有一个父节点带一个子节点
			 *		easyui combotree特性所在, 如果tree只有一个子节点, 选中子节点, 会把父节点也会选中.
			 * */
            List<Map<String, Object>> findBySqlMap = this.publicService.findSqlListMap("select if((select parent from sys_permission where id=?)!=0, "
                    + "if((select count(up.id) from sys_user_permission up join sys_permission p on up.permissionId=p.id where p.id=(select parent from sys_permission where id=?) and up.userid=?)!=0, -1, (SELECT parent FROM sys_permission WHERE id=?)),"
                    + "if((select count(up.id) from sys_user_permission up join sys_permission p on up.permissionId=p.id where p.id=? and up.userId=?)!=0, 0, -1)) as result", new Object[]{pId, pId, userId, pId, pId, userId});
            Map<String, Object> map = findBySqlMap.get(0);
            int object = ((BigInteger) map.get("result")).intValue();
            if (object > 0) {
                int count = this.publicService.findSqlCount("select count(*) from sys_user_permission where userid=? and permissionid=?", userId, object);
                if (count == 0) {
                    this.publicService.executeUpdateSql("insert into sys_user_permission (userid, permissionid) values (?, ?)", userId, object);
                }
            }
            int count = this.publicService.findSqlCount("select count(*) from sys_user_permission where userid=? and permissionid=?", userId, pId);
            if (count == 0) {
                this.publicService.executeUpdateSql("insert into sys_user_permission (userid, permissionid) values (?, ?)", userId, pId);
            }
        }
    }

    /**
     * 根据用户的id,得到用户的权限
     *
     * @param userId
     * @return 2015-5-9 下午07:03:15
     */
    public Map<String, Object> getUserPermission(int userId) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        List<Object[]> up = publicService.findHqlList(HQLConstant.FIND_USER_PERMISSION, new Object[]{userId, 0});
        for (Object[] objects : up) {
            map.put(objects[0].toString(), objects);
        }
        return map;
    }

    /**
     * 根据用户的id,得到用户的菜单
     *
     * @param userId
     * @return 2015-5-9 下午07:03:15
     */
    public Map<Integer, List<SysMenu>> getUserMenus(int userId) {
        Map<Integer, List<SysMenu>> map = new LinkedHashMap<Integer, List<SysMenu>>();

        //找到所有的父菜单
        List<Integer> up = publicService.findHqlList(HQLConstant.FIND_USER_MENU, new Object[]{userId});
        for (int i = 0; i < up.size(); i++) {
            List<SysMenu> listMenu = new ArrayList<SysMenu>();
            Integer integer = up.get(i);
            SysMenu m = this.publicService.load(SysMenu.class, integer);
            listMenu.add(m);
            List<SysMenu> listMenuChild = this.publicService.findHqlList("select m from SysMenu m, SysUserMenu um where um.menuId=m.id and parent=? and disploy=? and um.userId=? order by m.createTime", new Object[]{integer, 1, userId});
            for (SysMenu objects : listMenuChild) {
                listMenu.add(objects);
                map.put(i, listMenu);
            }
        }
        return map;
    }

    /**
     * 根据用户的权限, 获得权限对应的页面按钮
     *
     * @param mapPermission
     * @return Yang
     * 2016年2月6日 下午12:06:30
     */
    public Map<String, List<Object[]>> getUserPermissonElement(List<Object[]> mapPermission, int userId) {
        Map<String, List<Object[]>> map = new LinkedHashMap<String, List<Object[]>>();

        for (Object[] objects : mapPermission) {
            List<Object[]> list = this.publicService.findSqlList(SQLConstant.WEB_FIND_PERMISSION_ELEMENT, new Object[]{objects[0], 1, userId});
            map.put(String.valueOf(objects[1]), list);
        }
        return map;
    }

    /**
     * 移除session中用户,菜单,权限, 按钮信息
     */
    private void removeSessionAttr() {
        session.removeAttribute("user");
        session.removeAttribute("upp");
        session.removeAttribute("menus");
        session.removeAttribute("eles");
    }

    /**
     * 获得用户权限对应的页面信息
     *
     * @param userPermission
     * @param userPermissionSession
     * @param listElment
     */
    private void getUserPermissionMsg(Map<String, Object> userPermission, Map<String, Object> userPermissionSession, List<Object[]> listElment) {
        Iterator<Entry<String, Object>> it = userPermission.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> e = it.next();
            Object[] list = (Object[]) e.getValue();
            userPermissionSession.put(e.getKey(), list[1]);
            if ((Integer) list[2] == 1) {
                Object[] obj = new Object[]{list[0], list[1]};
                listElment.add(obj);
            }
        }
    }
}
