package com.console.constant;

public class HQLConstant {

	public static final String FIND_USER_PERMISSION = "select p.id, p.code, p.iselement from SysUserPermission up, SysPermission p, SysUser u where up.userId=u.id and up.permissionId=p.id and up.userId=? and p.parent!=?";

	public static final String FIND_USER_MENU = "select m.id from SysUserMenu um, SysMenu m, SysUser u where um.userId=u.id and um.menuId=m.id and um.userId=? and m.parent=0 and m.disploy=1 order by m.createTime";

}
