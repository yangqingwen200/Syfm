package com.commons.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BasicHibernateDaoSupport extends HibernateDaoSupport {
	
	@Autowired
	public void setSuperSessionFactory(@Qualifier("sessionFactory")SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }
	
}
