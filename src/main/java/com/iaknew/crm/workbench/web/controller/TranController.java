package com.iaknew.crm.workbench.web.controller;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.workbench.service.CustomerService;
import com.iaknew.crm.workbench.service.TranService;
import com.iaknew.crm.settings.service.UserService;
import com.iaknew.crm.workbench.service.impl.CustomerServiceImpl;
import com.iaknew.crm.workbench.service.impl.TranServiceImpl;
import com.iaknew.crm.settings.service.impl.UserServiceImpl;
import com.iaknew.crm.utils.DateTimeUtil;
import com.iaknew.crm.utils.PrintJson;
import com.iaknew.crm.utils.ServiceFactory;
import com.iaknew.crm.utils.UUIDUtil;
import com.iaknew.crm.workbench.domain.Tran;
import com.iaknew.crm.workbench.domain.TranHistory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TranController", value = {
        "/workbench/transaction/create.do", "/workbench/transaction/getCustomerName.do", "/workbench/transaction/save.do",
        "/workbench/transaction/detail.do", "/workbench/transaction/getHistoryListBiTranId.do"
})
public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/transaction/create.do".equals(path)){
            create(request, response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request, response);
        }else if ("/workbench/transaction/save.do".equals(path)){
            save(request, response);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request, response);
        }else if ("/workbench/transaction/getHistoryListBiTranId.do".equals(path)){
            getHistoryListBiTranId(request, response);
        }
    }

    private void getHistoryListBiTranId(HttpServletRequest request, HttpServletResponse response) {
        // 根据交易id获取 交易历史
        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList = ts.getHistoryListBiTranId(tranId);

        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");

        for (TranHistory th : thList){
            // 没去每一个对象的阶段
            String stage = th.getStage();

            // 根据阶段获取可能性
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }

        PrintJson.printJsonObj(response, thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 跳转到详细信息页
        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran = ts.detail(id);

        String stage = tran.getStage();
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        tran.setPossibility(possibility);

        request.setAttribute("tran", tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 创建交易
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran tran = new Tran();
        tran.setActivityId(activityId);
        tran.setExpectedDate(expectedDate);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        tran.setType(type);
        tran.setSource(source);
        tran.setOwner(owner);
        tran.setNextContactTime(nextContactTime);
        tran.setContactSummary(contactSummary);
        tran.setDescription(description);
        tran.setContactsId(contactsId);
        tran.setMoney(money);
        tran.setStage(stage);
        tran.setId(id);
        tran.setName(name);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(tran, customerName);
        if (flag){
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        // 获取客户名称 通过名字模糊查询
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> names = cs.getCustomerName(name);
        PrintJson.printJsonObj(response, names);
    }

    private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 跳转创建交易页面
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> users = us.getUserList();

        request.setAttribute("users", users);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}
