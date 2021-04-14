package com.iaknew.crm.workbench.service.impl;

import com.iaknew.crm.utils.SqlSessionUtil;
import com.iaknew.crm.workbench.dao.ActivityDao;
import com.iaknew.crm.workbench.domain.Activity;
import com.iaknew.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Activity a) {
        System.out.println("3");
        boolean flag = true;
        int count = dao.save(a);
        if (count != 1){
            flag = false;
        }
        return flag;
    }
}
