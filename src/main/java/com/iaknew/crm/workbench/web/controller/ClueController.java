package com.iaknew.crm.workbench.web.controller;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.settings.service.UserService;
import com.iaknew.crm.settings.service.impl.DicServiceImpl;
import com.iaknew.crm.settings.service.impl.UserServiceImpl;
import com.iaknew.crm.utils.DateTimeUtil;
import com.iaknew.crm.utils.PrintJson;
import com.iaknew.crm.utils.ServiceFactory;
import com.iaknew.crm.utils.UUIDUtil;
import com.iaknew.crm.workbench.domain.Activity;
import com.iaknew.crm.workbench.domain.Clue;
import com.iaknew.crm.workbench.domain.Tran;
import com.iaknew.crm.workbench.service.ActivityService;
import com.iaknew.crm.workbench.service.ClueService;
import com.iaknew.crm.workbench.service.impl.ActivityServiceImpl;
import com.iaknew.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ClueController", value = {
        "/workbench/clue/getUserList.do", "/workbench/clue/saveClue.do", "/workbench/clue/detail.do",
        "/workbench/clue/getActivityListByClueId.do", "/workbench/clue/unbind.do", "/workbench/clue/getActivityNoRelation.do",
        "/workbench/clue/searchActivityByNameNoRelation.do", "/workbench/clue/bund.do", "/workbench/clue/getActivityRelation.do",
        "/workbench/clue/getActivityRelationByActivityName.do", "/workbench/clue/convert.do"
})
public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request, response);
        }else if ("/workbench/clue/saveClue.do".equals(path)){
            saveClue(request, response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request, response);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request, response);
        }else if ("/workbench/clue/unbind.do".equals(path)){
            unbind(request, response);
        }else if ("/workbench/clue/getActivityNoRelation.do".equals(path)){
            getActivityNoRelation(request, response);
        }else if ("/workbench/clue/searchActivityByNameNoRelation.do".equals(path)){
            searchActivityByNameNoRelation(request, response);
        }else if ("/workbench/clue/bund.do".equals(path)){
            bund(request, response);
        }else if ("/workbench/clue/getActivityRelation.do".equals(path)){
            getActivityRelation(request, response);
        }else if ("/workbench/clue/getActivityRelationByActivityName.do".equals(path)){
            getActivityRelationByActivityName(request, response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request, response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 线索转换为客户
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran tran = null;

        // 如果需要创建交易
        if ("a".equals(flag)){
            // 接收交易数据
            tran = new Tran();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            tran.setActivityId(activityId);
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setId(id);
            tran.setCreateTime(createTime);
            tran.setCreateBy(createBy);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        /*
        *   必须传的参数
        *       clueId 判断转换的是那一条线索
        *       tran 因为转换过程中可能要创建一条交易
         */
        boolean flag1 = cs.convert(clueId, tran, createBy);

        if (flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void getActivityRelationByActivityName(HttpServletRequest request, HttpServletResponse response) {
        // 根据activityName模糊查询已关联的市场活动
        String clueId = request.getParameter("clueId");
        String activityName = request.getParameter("activityName");

        Map<String, String> map = new HashMap<>();
        map.put("clueId", clueId);
        map.put("activityName", activityName);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activities = as.getActivityRelationByActivityName(map);
        PrintJson.printJsonObj(response, activities);
    }


    private void getActivityRelation(HttpServletRequest request, HttpServletResponse response) {
        // 查询已关联的市场活动
        String clueId = request.getParameter("clueId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities = as.getActivityRelation(clueId);
        PrintJson.printJsonObj(response, activities);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        // 关联市场活动（批量）
        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(clueId, activityIds);
        PrintJson.printJsonFlag(response, flag);
    }

    private void searchActivityByNameNoRelation(HttpServletRequest request, HttpServletResponse response) {
        // 通过name查询没有与该线索关联的市场活动
        String activityName = request.getParameter("activityName");
        String clueId = request.getParameter("clueId");
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities = as.searchActivityByNameNoRelation(map);

        PrintJson.printJsonObj(response, activities);
    }

    private void getActivityNoRelation(HttpServletRequest request, HttpServletResponse response) {
        // 获取没有与该线索关联的市场活动
        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities = as.getActivityNoRelation(clueId);
        PrintJson.printJsonObj(response, activities);
    }

    private void unbind(HttpServletRequest request, HttpServletResponse response) {
        // 线索市场活动解除绑定
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unbind(id);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        // 通过线索id获取绑定的市场活动列表
        String clueId = request.getParameter("clueId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities = as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response, activities);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 跳转到详细页面
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = cs.detail(id);

        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        // 创建线索
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.saveClue(clue);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(2);
        // 获取用户列表
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }
}
