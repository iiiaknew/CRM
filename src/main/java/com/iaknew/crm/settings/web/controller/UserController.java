package com.iaknew.crm.settings.web.controller;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.settings.service.UserService;
import com.iaknew.crm.settings.service.impl.UserServiceImpl;
import com.iaknew.crm.utils.MD5Util;
import com.iaknew.crm.utils.PrintJson;
import com.iaknew.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UserController", value = {"/settings/user/login.do"})
public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取请求路径
        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){
            login(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        // 获取浏览器ip
        String ip = request.getRemoteAddr();
        // 获取代理对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            User user = us.login(loginAct, loginPwd, ip);

            // 如果代码执行到这里 说明账号密码正确 给前段返回{“success”：true}
            request.getSession().setAttribute("user", user);
            PrintJson.printJsonFlag(response, true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();

            // 走到这里说明账号密码错误
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}
