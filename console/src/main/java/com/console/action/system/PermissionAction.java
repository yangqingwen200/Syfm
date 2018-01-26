package com.console.action.system;

import com.commons.annotation.OpernationLog;
import com.commons.bean.system.SysPermission;
import com.commons.exception.LogicException;
import com.commons.util.MaptoBean;
import com.console.action.BaseWebAction;
import com.console.enums.ConsoleSqlEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 权限管理增删改查
 * @author Yang
 * @version v1.0
 * Date 2017/3/22
 */
@Controller("system.web.action.permissionAction")
@Scope("prototype")
public class PermissionAction extends BaseWebAction {

    private static final long serialVersionUID = 1L;

    /**
     * 获得权限列表
     * <p>
     * auth: Yang 2016年2月16日 上午9:52:59
     */
    @SuppressWarnings("unchecked")
    public void getPermissionList() {
        try {
            currentpage = this.publicService.pagedQuerySqlFreemarker(ConsoleSqlEnum.FIND_PERMISSION_WEB, dto);

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
     * 增加权限
     * <p>
     * auth: Yang
     * 2016年3月22日 上午8:46:32
     */

    @OpernationLog(cls = SysPermission.class, value = "增加权限")
    public void addPermission() {
        try {
            this.checkCodeReply();
            this.checkPermissionNameReply();
            SysPermission permission = MaptoBean.mapToBeanBasic(new SysPermission(), dto);
            this.publicService.save(permission);
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 编辑权限
     * <p>
     * auth: Yang
     * 2016年3月22日 上午8:48:27
     */
    @OpernationLog(cls = SysPermission.class, value = "编辑权限")
    public void editPermission() {
        try {
            if (dto.getAsInteger("id").intValue() == dto.getAsInteger("parent").intValue()) {
                throw new LogicException("权限不能当爹又当儿子。");
            }

            SysPermission permission = this.publicService.load(SysPermission.class, dto.getAsInteger("id"));
            if (!permission.getCode().equals(dto.getAsStringTrim("code"))) {
                this.checkCodeReply();
            }
            if (!permission.getPermission().equals(dto.getAsStringTrim("permission"))) {
                this.checkPermissionNameReply();
            }

            //当前节点含有子节点, 不允许当前节点成为子节点.
            if (dto.getAsInteger("parent").intValue() != 0 && permission.getParent().intValue() == 0) {
                int countChild = this.publicService.findSqlCount("select count(*) from sys_menu where parent=?", dto.getAsInteger("id"));
                if (countChild > 0) {
                    throw new LogicException("当前权限含有子权限<br>请先删除或者移除子权限。");
                }
            }

            SysPermission p = MaptoBean.mapToBeanBasic(new SysPermission(), dto);
            this.publicService.update(p);
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 删除权限
     * <p>
     * auth: Yang
     * 2016年3月22日 上午8:50:50
     */
    @OpernationLog(cls = SysPermission.class, value = "删除权限")
    public void deletePermission() {
        try {
            String ids = dto.getAsStringTrim("id");
            String[] id = ids.split(",");
            for (String string : id) {
                int count = this.publicService.findSqlCount("select count(id) from sys_permission where parent=?", Integer.parseInt(string));
                if (count > 0) {
                    throw new LogicException("请先删除子权限。");
                }
                //删除用户对应的权限
                this.publicService.executeUpdateSql("delete from sys_user_permission where permissionId=?", Integer.parseInt(string));
                //删除页面按钮对应的权限
                this.publicService.executeUpdateSql("update sys_element set permission_id=0 where permission_id=?", Integer.parseInt(string));
                //删除角色对应的权限
                this.publicService.executeUpdateSql("delete from sys_role_permission where permission_id=?", Integer.parseInt(string));
                //删除权限
                this.publicService.executeUpdateSql("delete from sys_permission where id=?", Integer.parseInt(string));
            }
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 检查权限名是否重复
     * <p>
     * auth: Yang
     * 2016年3月22日 上午9:51:59
     */
    public void checkPermissionNameReply() {
        int count = this.publicService.findSqlCount("select count(id) from sys_permission where permission=?", dto.getAsStringTrim("permission"));
        if (count > 0) {
            throw new LogicException("权限名重复，请更换。");
        }
    }

    /**
     * 检查code码是否重复
     * <p>
     * 2016年5月8日 下午10:14:50
     */
    public void checkCodeReply() {
        if (!"#".equals(dto.getAsStringTrim("code"))) {
            int count = this.publicService.findSqlCount("select count(id) from sys_permission where code=?", dto.getAsStringTrim("code"));
            if (count > 0) {
                throw new LogicException("code码重复，请更换。");
            }
        }
    }
}
