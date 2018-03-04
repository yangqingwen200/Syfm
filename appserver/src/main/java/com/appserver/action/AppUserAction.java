package com.appserver.action;

import com.appserver.constant.AppConstant;
import com.appserver.service.AppUserService;
import com.commons.annotation.Login;
import com.commons.annotation.Version;
import com.commons.exception.ValidateException;
import com.commons.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.Map;

/**
 * 所有方法都有使用try catch捕获异常.<br/>
 *
 * 也可以不捕获, 继续往外抛出, 全部在{@link com.appserver.interceptor.AccessInterceptor AccessInterceptor类}拦截器中处理
 */
@Controller("system.app.action.appUserAction")
@Scope("prototype")
public class AppUserAction extends BaseAppAction {

	private static final long serialVersionUID = 1L;
	private AppUserService appUserService;
	private File imgFile;
	private String imgFileFileName;

	/**
	 * 用户登录
	 * @author Yang
	 * @version v1.0
	 * @date 2017/2/22
	 */
	public void login() {
		try {
			this.checkRequestParam("phone", "password");
			Map<String, Object> sqlMap = this.appUserService.login(dto);
			String userId = String.valueOf(sqlMap.get("id"));
			String token = UUIDUtils.uuid2();
			json.put("userId", userId);
			json.put("token", token);

			//把token和用户id对应信息, 存入redis服务器中
			cacheRedis.del(AppConstant.USER_TOKEN + userId);
			cacheRedis.set(AppConstant.USER_TOKEN + userId, token, AppConstant.USER_TOKEN_EXPIRE_TIME);

		} catch (ValidateException e) {
			this.checkException(e);
		}
		this.renderJson();
	}

	/**
	 * 得到APP用户详细信息
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月21日
	 */
	@Login
	@Version(version = "1.1")
	public void getAppUserInfo() {
		try {
			Long asLong = dto.getAsLong("userId");
			Map<String, Object> userInfo = this.appUserService.getUserInfo(asLong);
			json.put("userInfo", userInfo);
		} catch (Exception e) {
			this.checkException(e);
		}
		this.renderJson();
	}

	/**
	 * App退出登录
	 * @author Yang
	 * @version v1.0
	 * @date 2017/4/25
	 */
	@Login
	public void logout() {
		try {
			String userId = dto.getAsString("userId");
			cacheRedis.del(AppConstant.USER_TOKEN + userId);
		} catch (Exception e) {
			this.checkException(e);
		}
		this.renderJson();
	}

	public void getSchool() {
		try {
			this.checkRequestParam("pageNow");
			Integer pageNow = dto.getAsInteger("pageNow");
			Map<String, Object> school = this.appUserService.getSchool(pageNow);
			json.putAll(school);
		} catch (Exception e) {
			this.checkException(e);
		}
		this.renderJson();
	}

	public void schoolDetail() {
		try {
			this.checkRequestParam("id");
			Integer id = dto.getAsInteger("id");
			Map<String, Object> school = this.appUserService.getSchoolDetail(id);
			json.put("detail", school);
		} catch (Exception e) {
			this.checkException(e);
		}
		this.renderJson();
	}

	public void aroundFriend() {
		try {
			this.checkRequestParam("pageNow");
			Integer pageNow = dto.getAsInteger("pageNow");
			String name = dto.getAsStringTrim("name");
			String state = dto.getAsStringTrim("state");
			Map<String, Object> school = this.appUserService.getAroundFriend(pageNow, name, state);
			json.putAll(school);
		} catch (Exception e) {
			this.checkException(e);
		}
		this.renderJson();
	}

	@Login
	public void friendDetail() {
		try {
			this.checkRequestParam("friendId");
			Integer userId = dto.getAsInteger("friendId");
			Map<String, Object> school = this.appUserService.getFriendDetail(userId);
			json.put("detail", school);
		} catch (Exception e) {
			this.checkException(e);
		}
		this.renderJson();
	}

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileFileName() {
		return imgFileFileName;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public AppUserService getAppUserService() {
		return appUserService;
	}

	@Autowired
	@Qualifier("appUserService")
	public void setAppUserService(AppUserService appUserService) {
		this.appUserService = appUserService;
	}
}
