package com.front.action.business;

import com.front.action.BasePcAction;

/**
 * Created by Yang on 2017/9/9.
 */
public abstract class BusinessAction extends BasePcAction {

    private String navBarIndex = "business";

    public String getNavBarIndex() {
        return navBarIndex;
    }

    public void setNavBarIndex(String navBarIndex) {
        this.navBarIndex = navBarIndex;
    }
}
