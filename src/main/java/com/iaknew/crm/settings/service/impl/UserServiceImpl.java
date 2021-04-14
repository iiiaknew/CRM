package com.iaknew.crm.settings.service.impl;

import com.iaknew.crm.settings.dao.UserDao;
import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.settings.exception.LoginException;
import com.iaknew.crm.settings.service.UserService;
import com.iaknew.crm.utils.DateTimeUtil;
import com.iaknew.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("账号密码错误");
        }

        // 如果代码执行到这里说明账号密码正确
        // 继续判断其他三项
        // 验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号已失效");
        }

        // 验证是否被锁定
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号被锁定");
        }

        // 验证ip是否被锁定
        String allowIp = user.getAllowIps();
        if (!allowIp.contains(ip)){
            throw new LoginException("ip地址受限");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        return userDao.getUserList();
    }
}
