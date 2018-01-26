package com.front.action.business;

import com.commons.redis.RedisUtil;
import com.front.constant.SQLConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller("system.pc.action.business.timeAction")
@Scope("prototype")
public class TimeAction extends BusinessAction {

    private static final long serialVersionUID = 1081681641584848916L;
    private static final Logger LOG = LoggerFactory.getLogger(TimeAction.class);
    private String menuIndex = "time";

    public String list() {
        long total = 0;
        List<?> rowsList = new ArrayList<Object>();
        try {
            if (isGetRequest) {
                try {
                    /**
                     * 这里页面跳转, 不是弹出框, 出现错误了, 应该跳入错误页面, 而不是打印错误json串, 所以try下
                     */
                    //搜索栏下拉框数据
                    String findSchool = "SELECT id, name FROM `ds_driving_school` WHERE disabled=? AND STATUS =?";
                    List<Object> sqlListMap = RedisUtil.getListByKey("schoolList", findSchool, new Object[]{1, 3}, 3 * 3600);
                    this.request.setAttribute("schoolList", sqlListMap);
                    return SUCCESS;
                } catch (Exception e) {
                    this.getErrorMessage(e);
                    return ERROR;
                }
            }
            page = this.publicService.pagedQuerySqlFreemarker(SQLConstant.PC_FIND_TIME_DRIVINGSCHOOL, dto);
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

    public String edit() {
        try {
            Integer id = dto.getAsInteger("id");
            String name = dto.getAsStringTrim("name");
            String link_tel = dto.getAsStringTrim("link_tel");
            String address = dto.getAsStringTrim("address");
            String status = dto.getAsStringTrim("status");
            Integer is_examroom = dto.getAsInteger("is_examroom");
            this.publicService.executeUpdateSql("update ds_driving_school set name=?, link_tel=?, address=?, status=?, is_examroom = ? where id=?", name, link_tel, address, status, is_examroom, id);
        } catch (Exception e) {
            this.checkException(e);
        }
        this.printJson();
        return NONE;
    }

    public String add() {
        try {
            Integer id = dto.getAsInteger("id");
            String name = dto.getAsStringTrim("name");
            String link_tel = dto.getAsStringTrim("link_tel");
            String address = dto.getAsStringTrim("address");
            String status = dto.getAsStringTrim("status");
            Integer is_examroom = dto.getAsInteger("is_examroom");
        } catch (Exception e) {
            this.checkException(e);
        }
        this.printJson();
        return NONE;
    }

    public String getMenuIndex() {
        return menuIndex;
    }

    public void setMenuIndex(String menuIndex) {
        this.menuIndex = menuIndex;
    }
}
