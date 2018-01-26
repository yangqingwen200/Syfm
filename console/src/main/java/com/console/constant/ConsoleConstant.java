package com.console.constant;

import com.commons.constant.CommonConstant;

/**
 * @author Yang
 * @version v1.0
 * @date 2017/9/7
 */
public class ConsoleConstant extends CommonConstant {

    //用户详细信息
    public static final String USER_DETAIL = "user_detail_";

    //存放用户菜单redis key
    public static final String USER_MENU_PREFIX = "user_menu_";

    //存放用户权限redis key
    public static final String USER_PERMISSION_PREFIX = "user_permission_";

    //存放用户按钮redis key
    public static final String USER_ELEMENT_PREFIX = "user_element_";

    //存放用户角色redis key
    public static final String USER_ROLE_PREFIX = "user_role_";

    //存储用户详细信息过期时间
    public static final int USER_EXPIRE_TIME = 10 * 60;

}
