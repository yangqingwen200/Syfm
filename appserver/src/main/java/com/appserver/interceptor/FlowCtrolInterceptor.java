package com.appserver.interceptor;

import com.appserver.cache.IpOutOfFlowCache;
import com.appserver.enums.AppExcEnum;
import com.commons.exception.ValidateException;
import com.commons.util.core.WebUtils;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 同一个ip在规定时间内访问次数拦截器
 */
public class FlowCtrolInterceptor extends MethodFilterInterceptor {
	
	private static final long serialVersionUID = 4185130583194819455L;
	private final static ConcurrentMap<String, FlowControl> FLOW_CONTROLS = Maps.newConcurrentMap();
	private final static IpOutOfFlowCache OUT_FLOW_IP_CACHE = IpOutOfFlowCache.getInstance();

	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String ip = WebUtils.getClientIp(request); //得到操作者的IP地址
		if(isFlowOut(ip)) {
			throw new ValidateException(AppExcEnum.BLACK_IP);
		}
		return actionInvocation.invoke();
	}

	public boolean isFlowOut(String ip) {
		/* 列入黑名单的ip，当天停止访问, 需要写定时任务清空黑名单, 或者重启服务器 */
		if (OUT_FLOW_IP_CACHE.contains(ip)) {
			return true;
		}
		FlowControl flowControl = new FlowControl();
		FlowControl flowControl0 = FLOW_CONTROLS.putIfAbsent(ip, flowControl);
		if (flowControl0 != null && flowControl0.isMoreThen()) {
			OUT_FLOW_IP_CACHE.add(ip);
			return true;
		}
		return false;
	}

	private final static class FlowControl {

		private long nano = System.nanoTime();
		private AtomicInteger reqCount = new AtomicInteger(1);
		private final static long MAX_INTERVAL = TimeUnit.SECONDS.toNanos(1);  //规定时间间隔: 1秒
		private final static int MAX_COUNT = 5; //在MAX_INTERVAL时间内访问次数

		public boolean isMoreThen() {
			long now = System.nanoTime();
			if (now - nano < MAX_INTERVAL) {
				int tmpReqCount = reqCount.incrementAndGet();
				return tmpReqCount > MAX_COUNT;
			} else {
				nano = now;
				reqCount.set(1);
			}
			return false;
		}
	}

}
