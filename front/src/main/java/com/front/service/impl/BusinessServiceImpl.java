package com.front.service.impl;

import com.commons.service.GenericServiceImpl;
import com.front.service.BusinessService;

public class BusinessServiceImpl extends GenericServiceImpl implements BusinessService {

    @Override
    public void update() {
        System.out.println(123);
    }
}
