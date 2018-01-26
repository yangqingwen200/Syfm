package com.system.web.system.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.generic.annotation.OpernationLog;
import com.generic.enums.SqlParserInfo;
import com.generic.util.MaptoBean;
import com.system.bean.system.Template;
import com.system.common.BaseWebAction;

/**
 * 模板 增删改查
 * @author Yang
 * @version v1.0
 */
@Controller("system.web.action.templateAction")
@Scope("prototype")
public class TemplateAction extends BaseWebAction {

    private static final long serialVersionUID = 1L;

    /**
     * 获得模板列表
     * 
     * auth: Yang 
     */
    @SuppressWarnings("unchecked")
    public void getTemplateList() {
        try {
            currentpage = this.publicService.pagedQueryParamSqlFreemarker(SqlParserInfo.FIND_Template, dto);
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
     * 增加模板
     * 
     * auth: Yang
     */
    @OpernationLog(cls = Template.class, value = "增加模板")
    public void addTemplate() {
        try {
            Template template = MaptoBean.mapToBeanBasic(new Template(), dto);
            this.publicService.save(template);
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 编辑模板
     * 
     * auth: Yang
     */
    @OpernationLog(cls = Template.class, value = "编辑模板")
    public void editTemplate() {
        try {
        	Template templateOld = this.publicService.load(Template.class, dto.getAsInteger("id"));
        	Template template = MaptoBean.mapToBeanBasic(templateOld, dto);
            this.publicService.update(template);
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }

    /**
     * 删除模板
     * 
     * auth: Yang
     */
    @OpernationLog(cls = Template.class, value = "删除模板")
    public void deleteTemplate() {
        try {
            String ids = dto.getAsStringTrim("id");
            String[] id = ids.split(",");
            for (String string : id) {
                this.publicService.executeUpdateSql("delete from template where id=?", Integer.parseInt(string));
            }
        } catch (Exception e) {
            this.checkException(e);
        } finally {
            this.printJson(json);
        }
    }
}
