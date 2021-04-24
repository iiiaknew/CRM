package com.iaknew;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.utils.DateTimeUtil;
import com.iaknew.crm.utils.PrintJson;
import com.iaknew.crm.utils.ServiceFactory;
import com.iaknew.crm.utils.UUIDUtil;
import com.iaknew.crm.workbench.domain.Activity;
import com.iaknew.crm.workbench.service.ActivityService;
import com.iaknew.crm.workbench.service.ClueService;
import com.iaknew.crm.workbench.service.impl.ActivityServiceImpl;
import com.iaknew.crm.workbench.service.impl.ClueServiceImpl;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void test2(){
        String id = "41968dbba0c04982a40f25a0d06411cf";
        String owner = "owner";
        String name = "name";
        String startDate = "startDate";
        String endDate = "endDate";
        String cost = "cost";
        String description = "description";

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEditTime(endDate);
        activity.setCost(cost);
        activity.setDescription(description);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(activity);

        System.out.println(flag);
    }

    @Test
    public void test3(){
        String clueId = "8fba5e4ba3654b4d99c17917535ab1d0";
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities = as.getActivityListByClueId(clueId);
    }

    @Test
    public void test4(){
        String clueId = "123";
        String[] activityIds = {"111"};

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(clueId, activityIds);
    }
}
