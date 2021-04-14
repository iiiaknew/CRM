package com.iaknew.crm.settings.service;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.settings.exception.LoginException;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
