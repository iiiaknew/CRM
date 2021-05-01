package com.iaknew.crm.web.listener;

import com.iaknew.crm.settings.domain.DicValue;
import com.iaknew.crm.settings.service.DicService;
import com.iaknew.crm.settings.service.impl.DicServiceImpl;
import com.iaknew.crm.utils.ServiceFactory;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.util.*;

@WebListener
public class SysInitListener implements ServletContextListener{

    public SysInitListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //System.out.println("下文对象创建了");
        // 服务器启动将数据字典保存到缓存中（application）
        ServletContext application = event.getServletContext();

        /*
            访问业务层 返回一个map
            {“类型”， “valueList”}
            {“类型”， “valueList”}
            {“类型”， “valueList”}
            ...
         */

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = ds.getAll();
        // 遍历map
        Set<String> set = map.keySet();
        for (String key : set){
            application.setAttribute(key, map.get(key));
        }

        //----------------------------------------------------------------------------------
        // 处理完数据字典 处理Stage2Possibility.properties文件
        // 解析Stage2Possibility.properties文件
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();
        Map<String, String> pMap = new HashMap<>();
        while (e.hasMoreElements()){
            // 阶段
            String key = e.nextElement();
            // 可能性
            String value = rb.getString(key);

            pMap.put(key, value);
        }
        application.setAttribute("pMap", pMap);
    }
}
