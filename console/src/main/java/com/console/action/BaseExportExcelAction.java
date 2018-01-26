package com.console.action;

import com.commons.util.core.BL3Utils;
import com.commons.util.core.Dto;
import com.commons.util.core.WebUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 导出excel文件的基类
 * @author Yang
 * @version v1.0
 * @date 2016年9月14日
 */
public abstract class BaseExportExcelAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = 562172221263984463L;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected OutputStream outputStream;
    protected Dto pDto;

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
		this.pDto = WebUtils.getParamAsDto(request);
		BL3Utils.handleNullStr(pDto);
	}
	
	/**
	 * 下载文件的公用方法
	 * @param wb excel文件
	 * @param fileName 文件名
	 * @author Yang
	 * @version v1.0
	 * @throws Exception 
	 * @date 2016年9月14日
	 */
	protected void renderFile(Workbook wb, String fileName) throws Exception {
		this.response.setContentType("application/x-download");
		String fileTitle = new String(fileName.getBytes("GBK"),"ISO8859-1"); 
		this.response.setHeader("Content-disposition","attachment;filename=\""+ fileTitle + "\"");
		outputStream = this.response.getOutputStream();
		wb.write(this.outputStream);
		this.outputStream.flush();
		this.outputStream.close();
	}
	
}
