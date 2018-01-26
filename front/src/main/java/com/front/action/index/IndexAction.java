package com.front.action.index;

import com.front.action.BasePcAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("system.pc.action.index.indexAction")
@Scope("prototype")
public class IndexAction extends BasePcAction {

    private static final long serialVersionUID = 1081681641584848916L;
    private String navBarIndex = "index";

    public String index() {
        return "index";
    }

    public String getNavBarIndex() {
        return navBarIndex;
    }

    public void setNavBarIndex(String navBarIndex) {
        this.navBarIndex = navBarIndex;
    }
}
