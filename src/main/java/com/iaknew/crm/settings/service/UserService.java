package com.iaknew.crm.settings.service;

import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.settings.exception.LoginException;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
}
