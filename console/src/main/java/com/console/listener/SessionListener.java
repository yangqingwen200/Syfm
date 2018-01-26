package com.console.listener;

import com.commons.service.GenericService;
import com.console.action.system.UserAction;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SessionListener implements HttpSessionBindingListener {
	
	private static GenericService publicService = null;
	public static ConcurrentMap<Integer, HttpSession> mapSession = new ConcurrentHashMap<Integer, HttpSession>();
	private int userId;
	
	public SessionListener() {
		
	}
	
	public SessionListener(int userid, GenericService service) {
		this.userId = userid;
		if(publicService == null) {
			publicService = service;
		}
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		if(mapSession.containsKey(userId)) {
			HttpSession sessionlod = mapSession.get(userId);
			UserAction.map.put(sessionlod.getId(), 3);
			sessionlod.invalidate();
		}
		mapSession.put(userId, session);
	}

	/* 
	 * valueUnbound的触发条件是以下三种情况：
	 * 1.执行session.invalidate()时。
	 * 2.session超时，自动销毁时。
	 * 3.执行session.setAttribute("onlineUserListener", "其他对象");或session.removeAttribute("onlineUserListener");将listener从session中删除时。
	 * 
	 * 用户退出登陆,调用session.invalidate()即可触发此方法
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		if(mapSession.containsValue(session)) {
			 Iterator<Entry<Integer, HttpSession>> it = mapSession.entrySet().iterator();      
		        while (it.hasNext()) {       
		        	Entry<Integer, HttpSession> e = it.next(); 
		        	if(session.equals(e.getValue())) {
		        		mapSession.remove(e.getKey());
		        		
		        		Map<String, Integer> map = UserAction.map;
		        		int flag = map.get(session.getId());
		        		if(flag == 1) {
		        			publicService.executeUpdateSql("update sys_log_login set logout_time=?, onlinetime=((UNIX_TIMESTAMP(?)- UNIX_TIMESTAMP(login_time))/60)-20, logout_type=? where session_id=?",
		        					new Object[]{new Date(), new Date(), "登录超时", session.getId()});
		        		} else if(flag == 0) {
		        			publicService.executeUpdateSql("update sys_log_login set logout_time=?, onlinetime=(UNIX_TIMESTAMP(?)- UNIX_TIMESTAMP(login_time))/60, logout_type=? where session_id=?",
		        					new Object[]{new Date(), new Date(), "主动下线", session.getId()});
		        		} else if(flag == 3) {
		        			publicService.executeUpdateSql("update sys_log_login set logout_time=?, onlinetime=(UNIX_TIMESTAMP(?)- UNIX_TIMESTAMP(login_time))/60, logout_type=? where session_id=?",
		        					new Object[]{new Date(), new Date(), "重新登录", session.getId()});
		        		}
		        		UserAction.map.remove(session.getId());
		        		break;
		        	}
		        }
		}
	}
	
}
