package com.console.action.system;

import com.commons.util.DateUtil;
import com.commons.util.POIExcel;
import com.commons.util.core.BL3Utils;
import com.console.action.BaseExportExcelAction;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller("system.web.action.exportExcelAction")
@Scope("prototype")
public class ExportExcelAction extends BaseExportExcelAction {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger("com.wt.action");    

    /**
     * 导出Web管理用户列表
     * @author Yang
     * @version v1.0
     * @date 2016年9月14日
     */
    public void exportWebUserExcel() {
        try {
            String[] excelHeader = {"序号", "用户名", "登录名", "密码", "性别", "手机号码", "电子邮箱"};
            StringBuffer sb = new StringBuffer("SELECT id, name, loginname, pw, CASE sex WHEN 0 THEN '女' WHEN 1 THEN '男' WHEN NULL THEN '未填写' END, telephone, email FROM sys_user where 1=1 ");
            List<String> param = new ArrayList<String>();
            
            if(pDto.getAsStringTrim("name") != null) {
            	sb.append("and loginname like ? ");
            	param.add("%" + pDto.getAsStringTrim("name") + "%");
            }
            List<Object[]> listOne = this.publicService.findSqlList(sb.toString(), param.toArray());
            HSSFWorkbook wb = POIExcel.exportExcel("后台管理用户", "后台管理用户列表", excelHeader, listOne);
            this.renderFile(wb, "后台管理用户列表_"+ DateUtil.formatDate(new Date())+".xls");
        } catch(Exception e) {
        	log.error(BL3Utils.getErrorMessage(2), e);
        } 
    }

}
