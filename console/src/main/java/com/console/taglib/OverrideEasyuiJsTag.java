package com.console.taglib;

import com.commons.taglib.CommonTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 工具栏div显示控制标签类
 * <p>
 * auth：Yang
 * 2016年4月9日 下午7:43:39
 */
public class OverrideEasyuiJsTag extends CommonTag {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean all;
    private boolean datagrid;
    private boolean form;
    private boolean dialog;
    private boolean combotree;
    private boolean pagination;
    private boolean rules;
    private boolean searchbox;
    private boolean combobox;
    private boolean pageform;  //注意: 属性pageform和属性form是相互斥, 两个同时引入的话, 后引入的会覆盖先引入的.

    @Override
    public String bodyMessage(HttpSession session) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String ctx = request.getContextPath();

        StringBuffer sb = new StringBuffer();
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + ctx + "/js/jquery-easyui-1.4.4/themes/bootstrap/easyui.css\"/>\n");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + ctx + "/js/jquery-easyui-1.4.4/themes/icon.css\">\n");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + ctx + "/css/common.css\" />\n");
        sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/jquery-easyui-1.4.4/jquery-1.7.2.min.js\"></script>\n");
        sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/jquery-easyui-1.4.4/jquery.easyui.min.js\"></script>\n");
        sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js\"></script>\n");
        //sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/common-min.js\"></script>\n");
        sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/common.js\"></script>\n");

        if (all) {
            //sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/all-min.js\"></script>\n");
            sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/all.js\"></script>\n");
        } else {
            if (rules) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/rules.js\"></script>\n");
            }
            if (datagrid) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/datagrid.js\"></script>\n");
            }
            if (form) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/form.js\"></script>\n");
            }
            if (dialog) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/dialog.js\"></script>\n");
            }
            if (combotree) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/combotree.js\"></script>\n");
            }
            if (pagination) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/pagination.js\"></script>\n");
            }
            if (searchbox) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/searchbox.js\"></script>\n");
            }
            if (combobox) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/combobox.js\"></script>\n");
            }
            if(pageform) {
                sb.append("<script type=\"text/javascript\" src=\"" + ctx + "/js/override_easyui/pageform.js\"></script>\n");
            }
        }
        return sb.toString();
    }


    public boolean isDatagrid() {
        return datagrid;
    }


    public void setDatagrid(boolean datagrid) {
        this.datagrid = datagrid;
    }


    public boolean isForm() {
        return form;
    }


    public void setForm(boolean form) {
        this.form = form;
    }


    public boolean isDialog() {
        return dialog;
    }


    public void setDialog(boolean dialog) {
        this.dialog = dialog;
    }


    public boolean isCombotree() {
        return combotree;
    }


    public void setCombotree(boolean combotree) {
        this.combotree = combotree;
    }


    public boolean isPagination() {
        return pagination;
    }


    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }


    public boolean isRules() {
        return rules;
    }


    public void setRules(boolean rules) {
        this.rules = rules;
    }


    public boolean isAll() {
        return all;
    }


    public void setAll(boolean all) {
        this.all = all;
    }


    public boolean isSearchbox() {
        return searchbox;
    }


    public void setSearchbox(boolean searchbox) {
        this.searchbox = searchbox;
    }


    public boolean isCombobox() {
        return combobox;
    }


    public void setCombobox(boolean combobox) {
        this.combobox = combobox;
    }

    public boolean isPageform() {
        return pageform;
    }

    public void setPageform(boolean pageform) {
        this.pageform = pageform;
    }
}
