package com.front.action;

import com.commons.action.BaseAction;
import com.commons.exception.LogicException;
import com.commons.util.Page;
import com.commons.util.core.BL3Utils;
import com.front.constant.FrontConstant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePcAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOG = LoggerFactory.getLogger(BasePcAction.class);
	protected Page<?> page = new Page<>(1,15);
	protected boolean flag = true;
	protected String errorMessage = "ok";
	protected JSONObject json = new JSONObject();
	protected List<Object> fuzzySearch = new ArrayList<Object>();

	protected void printJson() {
		try {
			if(isGetRequest) {
				this.printString(errorMessage);
				return;
			}
			json.accumulate("message", flag);
			json.accumulate("errorMsg", errorMessage);
			this.printString(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以json形式把错误信息返回给前台
	 * @param e 异常
	 */
	protected void checkException(Exception e) {
		json.clear();
		flag = false;
		errorMessage = getErrorMessage(e, 4);
	}

	/**
	 * 把错误信放在域中返回给前台
	 * @param e 异常
	 */
	protected void checkActionError(Exception e) {
		addActionError(getErrorMessage(e, 4));
	}

	protected String getErrorMessage(Exception e) {
		return this.getErrorMessage(e, 4);
	}

	protected String getErrorMessage(Exception e , Integer level) {
		String message;
		if (e instanceof LogicException) {
			LogicException ve  = (LogicException) e;
			message = ve.getErrorMessage();
			Object[] paramValue = ve.getParamValue();
			if(null != paramValue && paramValue.length > 0) {
				message = MessageFormat.format(message, paramValue);
			}
		} else {
			LOG.error(BL3Utils.getErrorMessage(level), e);
			message = FrontConstant.ERROR_MESSAGE;
		}
		return message;
	}

	public Page<?> getPage() {
		return page;
	}

	public void setPage(Page<?> page) {
		this.page = page;
	}
}
