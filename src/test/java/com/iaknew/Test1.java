package com.iaknew;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.utils.DateTimeUtil;
import com.iaknew.crm.utils.ServiceFactory;
import com.iaknew.crm.utils.UUIDUtil;
import com.iaknew.crm.workbench.domain.Activity;
import com.iaknew.crm.workbench.service.ActivityService;
import com.iaknew.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Test;

public class Test1 {
    @Test
    public void test1(){
        Activity a = new Activity();
        String id = UUIDUtil.getUUID();
        String owner = "owner";
        String name = "name";
        String startDate = "startDate";
        String endDate = "endDate";
        String cost = "cost";
        String description = "description";
        String createTime = DateTimeUtil.getSysTime();
        String createBy = "user";

        a.setId(id);
        a.setCost(cost);
        a.setCreateTime(createTime);
        a.setDescription(description);
        a.setCreateBy(createBy);
        a.setName(name);
        a.setOwner(owner);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        System.out.println(a);


        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.save(a);
        System.out.println(flag);
    }
}
