package com.appserver.service.impl;

import com.appserver.enums.AppExcEnum;
import com.appserver.service.AppUserService;
import com.commons.exception.ValidateException;
import com.commons.service.GenericServiceImpl;
import com.commons.util.core.Dto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("appUserService")
public class AppUserServiceImpl extends GenericServiceImpl implements AppUserService {

	@Override
	public Map<String, Object> login(Dto dto) {
		Map<String, Object> sqlMap = this.publicDao.findSqlMap("select id from appuser where telephone=? and pw=? limit 1", dto.getAsString("phone"), dto.getAsString("password"));
		if(sqlMap.isEmpty()) {
			throw new ValidateException(AppExcEnum.APPUSER_ACCOUNT_PASSWORD_ERR, dto.getAsString("phone"));
		}
		return sqlMap;
	}

	@Override
	public Map<String, Object> getUserInfo(Long id) {
		Map<String, Object> findSqlMap = this.publicDao.findSqlMap("select id, name, logourl, telephone from appuser where id=?", id);
		if(findSqlMap.isEmpty()) {
			throw new ValidateException(AppExcEnum.APPUSER_ISNOT_EXISTS, id);
		}
		return findSqlMap;
	}
}
