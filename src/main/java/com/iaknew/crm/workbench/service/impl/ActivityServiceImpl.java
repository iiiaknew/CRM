package com.iaknew.crm.workbench.service.impl;

import com.iaknew.crm.settings.dao.UserDao;
import com.iaknew.crm.settings.domain.User;
import com.iaknew.crm.utils.SqlSessionUtil;
import com.iaknew.crm.vo.PaginationVO;
import com.iaknew.crm.workbench.dao.ActivityDao;
import com.iaknew.crm.workbench.dao.ActivityRemarkDao;
import com.iaknew.crm.workbench.domain.Activity;
import com.iaknew.crm.workbench.domain.ActivityRemark;
import com.iaknew.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public boolean save(Activity a) {
        System.out.println("3");
        boolean flag = true;
        int count = activityDao.save(a);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        PaginationVO<Activity> vo = new PaginationVO<>();
        int total = activityDao.getTotalByCondition(map);
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;

        // 获取应该删除备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        // 获取实际删除的备注数量
        int count2 = activityRemarkDao.deleteCountByAids(ids);

        // 如果数量不相同删除失败
        if (count1 != count2){
            flag = false;
        }

        // 删除市场活动
        int count3 = activityDao.deleteActivity(ids);

        if (count3 != ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        Map<String, Object> map = new HashMap<>();

        List<User> users = userDao.getUserList();
        Activity activity = activityDao.getActivity(id);

        map.put("users", users);
        map.put("activity", activity);

        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;

        int count = activityDao.update(activity);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {
        return activityDao.detail(id);
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> remarks = activityRemarkDao.getRemarkListByAid(activityId);
        return remarks;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean editRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.editRemark(ar);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        return activityDao.getActivityListByClueId(clueId);
    }

    @Override
    public List<Activity> getActivityNoRelation(String clueId) {
        return activityDao.getActivityNoRelation(clueId);
    }

    @Override
    public List<Activity> searchActivityByNameNoRelation(Map<String, Object> map) {
        return activityDao.searchActivityByNameNoRelation(map);
    }

    @Override
    public List<Activity> getActivityRelation(String clueId) {
        return activityDao.getActivityRelation(clueId);
    }

    @Override
    public List<Activity> getActivityRelationByActivityName(Map<String, String> map) {
        return activityDao.getActivityRelationByActivityName(map);
    }
}
