package com.console.action;

import com.commons.action.BaseAction;
import com.commons.exception.LogicException;
import com.commons.util.Page;
import com.commons.util.core.BL3Utils;
import com.console.constant.ConsoleConstant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseWebAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	protected Logger LOG = LoggerFactory.getLogger(getClass());
	protected static final String OPERATION_FORWARD = "operationForward";
	protected int page; // 当前页数
	protected int rows; // 一页多少行
	protected String sort; // 排序字段
	protected String order; // 升序asc 或者 降序 desc
	protected Page<?> currentpage;
	protected boolean flag = true;
	protected String errorMsg = "";
	protected List<Object> fuzzySearch = new ArrayList<Object>();  //用来装精确查询的关键字
	protected JSONObject json = new JSONObject();

	protected void printJson() {
		try {
			json.accumulate("message", flag);
			if(!flag) { //只有发生错误了, 发返回错误信息, 否则不返回
				json.accumulate("errorMsg", errorMsg);
			}
			this.printString(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void printJson(JSONObject json) {
		try {
			this.json = json;
			this.json.accumulate("message", flag);
			if(!flag) {
				json.accumulate("errorMsg", errorMsg);
			}
			this.printString(this.json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void checkException(Exception e) {
		flag = false;
		if (e instanceof LogicException) {
			errorMsg = e.getMessage();
			LogicException ve = (LogicException)e;
			Object[] paramValue = ve.getParamValue();
			if(null != paramValue && paramValue.length > 0) {
				errorMsg = MessageFormat.format(errorMsg, paramValue);
			}
		} else {
			LOG.error(BL3Utils.getErrorMessage(3), e);
			errorMsg = ConsoleConstant.ERROR_MESSAGE;
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public com.commons.util.Page<?> getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(com.commons.util.Page<?> currentpage) {
		this.currentpage = currentpage;
	}
}
