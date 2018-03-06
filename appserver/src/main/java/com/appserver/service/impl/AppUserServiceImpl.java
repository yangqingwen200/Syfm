package com.appserver.service.impl;

import com.appserver.enums.AppExcEnum;
import com.appserver.service.AppUserService;
import com.commons.exception.ValidateException;
import com.commons.service.GenericServiceImpl;
import com.commons.util.Page;
import com.commons.util.core.Dto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("appUserService")
public class AppUserServiceImpl extends GenericServiceImpl implements AppUserService {

    @Override
    public Map<String, Object> login(Dto dto) {
        Map<String, Object> sqlMap = this.publicDao.findSqlMap("select id from appuser where telephone=? and pw=? limit 1", dto.getAsString("phone"), dto.getAsString("password"));
        if (sqlMap.isEmpty()) {
            throw new ValidateException(AppExcEnum.APPUSER_ACCOUNT_PASSWORD_ERR, dto.getAsString("phone"));
        }
        return sqlMap;
    }

    @Override
    public Map<String, Object> getUserInfo(Long id) {
        Map<String, Object> findSqlMap = this.publicDao.findSqlMap("select id, name, logourl, telephone, date_format(bir, '%Y-%m-%d') as bir from appuser where id=?", id);
        if (findSqlMap.isEmpty()) {
            throw new ValidateException(AppExcEnum.APPUSER_ISNOT_EXISTS, id);
        }
        return findSqlMap;
    }

    @Override
    public Map<String, Object> getSchool(Integer pageNow) {
        String sql = "SELECT id, `name`, logo, city_name, star_num, address, DATE_FORMAT(create_date, '%Y-%m-%d %H:%i:%s') AS create_date from `ds_driving_school`";
        Page page = this.publicDao.pagedQueryParamSql(pageNow, 5, sql, new Object[]{});
        Map map = new HashMap();
        map.put("list", page.getContent());
        return map;
    }

    @Override
    public Map<String, Object> getAroundFriend(Integer pageNow, String name, String state) {
        List<Object> param = new ArrayList<Object>();
        String sql = "SELECT * from `s61_bank_deposit_manage` where 1=1";
        if (null != name) {
            param.add("%" + name + "%");
            sql += " and acName like ?";
        }
        if(null != state && !"all".equals(state)) {
            param.add(state);
            sql += " and state = ?";
        }
        Page page = this.publicDao.pagedQueryParamSql(pageNow, 10, sql, param.toArray());
        Map map = new HashMap();
        map.put("total", page.getTotalNum());
        map.put("list", page.getContent());
        map.put("pageCount", page.getPageCount());
        map.put("pageNow", pageNow);
        return map;
    }

    @Override
    public Map<String, Object> getIndex(Integer pageNow, String category) {
        List<Object> param = new ArrayList<Object>();
        String sql = "SELECT *, CASE WHEN TIMESTAMPDIFF(MINUTE, behot_time, NOW())<60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, behot_time, NOW()), '分钟前') WHEN TIMESTAMPDIFF(HOUR, behot_time, NOW())<24 THEN CONCAT(TIMESTAMPDIFF(HOUR, behot_time, NOW()), '小时前') WHEN TIMESTAMPDIFF(HOUR, behot_time, NOW())>=24 THEN DATE_FORMAT(behot_time, '%m-%d %H:%i') END AS publish from jin_ri_tou_tiao where 1=1";
        if(null != category) {
            param.add("news_" + category);
            sql += " and category = ?";
        }
        Page page = this.publicDao.pagedQueryParamSql(pageNow, 10, sql, param.toArray());
        Map map = new HashMap();
        map.put("total", page.getTotalNum());
        map.put("list", page.getContent());
        map.put("pageCount", page.getPageCount());
        map.put("pageNow", pageNow);
        return map;
    }

    @Override
    public Map<String, Object> getSchoolDetail(Integer id) {
        String sql = "SELECT id, `name`, logo, city_name, star_num, address, " +
                "DATE_FORMAT(create_date, '%Y-%m-%d %H:%i:%s') AS create_date, introduction, link_man, link_tel,is_examroom FROM `ds_driving_school` where id=?";
        Map<String, Object> sqlMap = this.publicDao.findSqlMap(sql, id);
        return sqlMap;
    }

    @Override
    public Map<String, Object> getFriendDetail(Integer userId) {
        String sql = "SELECT * from `s61_bank_deposit_manage` where userId = ?";
        Map<String, Object> sqlMap = this.publicDao.findSqlMap(sql, userId);
        return sqlMap;
    }
}
