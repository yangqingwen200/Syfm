package com.console.action.system;

import com.commons.bean.drivingschool.DsDrivingSchool;
import com.commons.util.MaptoBean;
import com.console.action.BaseWebAction;
import com.console.enums.ConsoleSqlEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller("system.web.action.drivingSchoolAction")
@Scope("prototype")
public class DrivingSchoolAction extends BaseWebAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 获得驾校列表
	 * @author Yang
	 * @version v1.0
	 * @date 2017/3/22
	 */
	public void getDrivingSchoolList() {
		long total = 0;
		List<?> rowsList = new ArrayList<Object>();
		try {
			currentpage = this.publicService.pagedQuerySqlFreemarker(ConsoleSqlEnum.FIND_DRIVINGSCHOOL_WEB, dto);
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
	 * 新增驾校
	 * @author Yang
	 * @version v1.0
	 * @date 2017/3/22
	 */
	public void addDrivingSchool() {
		try {
			DsDrivingSchool dds = MaptoBean.mapToBeanBasic(new DsDrivingSchool(), dto);
			//主键id为Long类型, 在MaptoBean类中没有指定Long类类型的id(只指定为Integer), 会返回0, 所以手动置为null
			dds.setId(null);
			dds.setDisabled(1);
			dds.setStarNum(5.00);
			dds.setIsExamroom(1);
			dds.setCreateDate(new Date());
			this.publicService.save(dds);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 编辑驾校信息
	 * @author Yang
	 * @version v1.0
	 * @date 2017/3/22
	 */
	public void editDrivingSchool() {
		try {
			DsDrivingSchool ddsOld = this.publicService.load(DsDrivingSchool.class, dto.getAsLong("id"));
			DsDrivingSchool dds = MaptoBean.mapToBeanBasic(ddsOld, dto);

			this.publicService.update(dds);
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}

	/**
	 * 删除驾校
	 * @author Yang
	 * @version v1.0
	 * @date 2017/3/22
	 */
	public void deleteDrivingSchool() {
		try {
			String asString = dto.getAsStringTrim("id");
			String[] ids = asString.split(",");
			for (String string : ids) {
				this.publicService.executeUpdateSql("UPDATE ds_driving_school SET disabled=0 WHERE id=?", Long.parseLong(string));
			}
		} catch (Exception e) {
			this.checkException(e);
		} finally {
			this.printJson(json);
		}
	}
}
