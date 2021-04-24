package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity a);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    int deleteActivity(String[] ids);

    Activity getActivity(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityNoRelation(String clueId);

    List<Activity> searchActivityByNameNoRelation(Map<String, Object> map);

    List<Activity> getActivityRelation(String clueId);

    List<Activity> getActivityRelationByActivityName(Map<String, String> map);
}
