package com.iaknew.crm.workbench.service.impl;

import com.iaknew.crm.workbench.service.CustomerService;
import com.iaknew.crm.utils.SqlSessionUtil;
import com.iaknew.crm.workbench.dao.CustomerDao;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }
}
