package com.front.action.login;

import com.front.action.BasePcAction;
import com.front.constant.FrontConstant;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("system.pc.action.login.loginAction")
@Scope("prototype")
public class LoginAction extends BasePcAction {

    private static final long serialVersionUID = 1081681641584848916L;
    private static final List<String> urlList = Arrays.asList("add", "edit", "delete", "detail", "forbidden");


    public String login() throws Exception {

        Map map = new HashMap();
        List<Object[]> menuParent = publicService.findSqlList("SELECT id, name, path, remark FROM sys_menu_1 WHERE disploy=1 AND parent=0 order by create_time");
        List<Object> menuChild = null;
        for (Object[] objects : menuParent) {
            menuChild = publicService.findSqlList("SELECT name, path, remark FROM sys_menu_1 WHERE disploy=1 AND parent=? order by create_time", objects[0]);
            map.put(objects[3], menuChild);
        }
        List<String> permission_1 = publicService.findSqlList("select code from sys_permission_1");

        session.setAttribute("navBarList", menuParent);
        session.setAttribute(FrontConstant.MENU_KEY, map);
        session.setAttribute(FrontConstant.PERMISSION_KEY, permission_1);

        //在哪个页面操作超时的, 登录成功之后跳回哪个页面
        String forwardUrl = dto.getAsString("_forward");
        if(null != forwardUrl && !"".equals(forwardUrl)) {
            response.sendRedirect(batUrl(forwardUrl));
            return NONE;
        }

        return SUCCESS;
    }

    /**
     * 把url中的部分字段(在urlList中指定)替换成list, 方便登录之后url跳转
     * @param url
     * @return
     */
    private static String batUrl(String url) {
        String result = url;
        int lastIndexOf = url.lastIndexOf("/");
        if(lastIndexOf != -1) {
            String substring = url.substring(lastIndexOf + 1);
            for (String s : urlList) {
                if(substring.contains(s)) {
                    substring = substring.replace(s, "list");
                    break;
                }
            }
            result = url.substring(0, lastIndexOf + 1) + substring;
        }

        return result;
    }
}
