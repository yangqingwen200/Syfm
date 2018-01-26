package com.front.action.business;

import com.front.constant.SQLConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller("system.pc.action.business.kesanAction")
@Scope("prototype")
public class KesanAction extends BusinessAction {

    private static final long serialVersionUID = 1081681641584848916L;
    private static final Logger LOG = LoggerFactory.getLogger(KesanAction.class);
    private String menuIndex = "kesan";

    public String list() {
        long total = 0;
        List<?> rowsList = new ArrayList<Object>();
        try {
            if (isGetRequest) {
                try {
                    return SUCCESS;
                } catch (Exception e) {
                    this.getErrorMessage(e);
                    return ERROR;
                }
            }
            page = this.publicService.pagedQuerySqlFreemarker(SQLConstant.PC_FIND_KESAN_DRIVINGSCHOOL, dto);
            total = page.getTotalNum();
            rowsList = page.getContent();
        } catch (Exception e) {
            this.checkException(e);
        }
        json.accumulate("total", total);
        json.accumulate("rows", rowsList);
        this.printString(json.toString());
        return NONE;
    }

    public String getMenuIndex() {
        return menuIndex;
    }

    public void setMenuIndex(String menuIndex) {
        this.menuIndex = menuIndex;
    }
}
