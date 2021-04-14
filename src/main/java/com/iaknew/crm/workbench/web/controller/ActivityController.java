package com.iaknew.crm.workbench.web.controller;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.settings.service.UserService;
import com.iaknew.crm.settings.service.impl.UserServiceImpl;
import com.iaknew.crm.utils.*;
import com.iaknew.crm.workbench.domain.Activity;
import com.iaknew.crm.workbench.service.ActivityService;
import com.iaknew.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ActivityController", value = {
        "/workbench/activity/getUserList.do", "/workbench/activity/save.do", "/workbench/activity/pageList.do"
})
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("1");
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request, response);
        }else if ("/workbench/activity/save.do".equals(path)){
            save(request, response);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("2");
        Activity a = new Activity();
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

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
        System.out.println(3);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> users = us.getUserList();

        PrintJson.printJsonObj(response, users);
    }
}
