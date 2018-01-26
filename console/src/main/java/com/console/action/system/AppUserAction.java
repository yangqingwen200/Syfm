package com.console.action.system;

import com.commons.bean.appcenter.Appuser;
import com.commons.util.FileUtil;
import com.commons.util.MD5Encoder;
import com.commons.util.MaptoBean;
import com.console.action.BaseWebAction;
import com.console.enums.ConsoleSqlEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Yang
 * @version v1.0
 * @date 2016年11月9日
 */
@Controller("system.web.action.appUserAction")
@Scope("prototype")
public class AppUserAction extends BaseWebAction {

	private static final long serialVersionUID = 1L;
	private File logourl;
	private String logourlFileName;

	/**
	 * 1. 获得App用户列表
	 *
	 * auth: Yang
	 * 2016年3月21日 下午2:33:15
	 */
	@SuppressWarnings("unchecked")
	public void getAppUserList() {
		long total = 0;
		List<?> rowsList = new ArrayList<Object>();
		try {
			currentpage = this.publicService.pagedQuerySqlFreemarker(ConsoleSqlEnum.FIND_APP_USER_WEB, dto);
			total = currentpage.getTotalNum();
			rowsList = currentpage.getContent();
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			json.accumulate("total", total);
			json.accumulate("rows", rowsList);
			this.printString(json.toString());
		}
	}

	/**
	 * 2. 编辑APP用户
	 *
	 * auth: Yang
	 * 2016年3月21日 下午4:07:23
	 */
	public void editAppUser() {
		try {
			Appuser appuserOld = this.publicService.load(Appuser.class, dto.getAsInteger("id"));
			Appuser appuser = MaptoBean.mapToBeanBasic(appuserOld, dto);
			if(!appuser.getPw().equals(dto.getAsString("pw1"))) {
				appuser.setPw(MD5Encoder.getMD5(appuser.getPw()));
			}
			if(logourl != null && logourl.exists()) {
				String uploadFile = FileUtil.uploadFile(this.getLogourl(), this.getLogourlFileName(), "appuserlogo", true);
				appuser.setLogourl(uploadFile);
			}
			this.publicService.update(appuser);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 3. 删除App用户
	 *
	 * auth: Yang
	 * 2016年3月21日 下午4:08:08
	 */
	public void deleteAppUser() {
		try {
			String asString = dto.getAsString("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				this.publicService.executeUpdateSql("update appuser set status=? where id=?", 1, Integer.parseInt(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
	
	/**
	 * 4. 查看App用户详情
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月9日
	 */
	public void detailAppUser() {
		try {
			Integer userId = dto.getAsInteger("id");
			Appuser appuserOld = this.publicService.load(Appuser.class, userId);
			this.request.setAttribute("appuser", appuserOld);
			this.request.getRequestDispatcher("/admin/appuser/appuser_detailAppUser.jsp").forward(request, response);
		} catch (Exception e) {
			this.checkException(e);
			this.printJson();
		}
	}

	/**
	 * 5. 新增App用户
	 * @author Yang
	 * @version v1.0
	 * @date 2017/7/17
	 */
	public String addAppUser() {
		try {
			if(isGetRequest) {
				/**
				 * 为新的页面, 所以在这里可以初始化内容, 放入request中,
				 * 在页面就可以初始化控件, 如comobbox等等, 避免在页面中再使用异步形式获取
				 */
				return OPERATION_FORWARD;
			}

			Appuser appuser = MaptoBean.mapToBeanBasic(new Appuser(), dto);
			this.publicService.save(appuser);
		} catch (Exception e) {
			this.checkException(e);
		}
		this.printJson();
		return NONE;
	}
	
	public File getLogourl() {
		return logourl;
	}

	public void setLogourl(File logourl) {
		this.logourl = logourl;
	}

	public String getLogourlFileName() {
		return logourlFileName;
	}

	public void setLogourlFileName(String logourlFileName) {
		this.logourlFileName = logourlFileName;
	}
	
}
