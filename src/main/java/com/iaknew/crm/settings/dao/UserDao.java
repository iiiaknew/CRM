package com.iaknew.crm.settings.dao;

import com.iaknew.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
}
