package com.iaknew.crm.workbench.service;

import com.iaknew.crm.vo.PaginationVO;
import com.iaknew.crm.workbench.domain.Activity;
import com.iaknew.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity a);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean editRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityNoRelation(String clueId);

    List<Activity> searchActivityByNameNoRelation(Map<String, Object> map);

    List<Activity> getActivityRelation(String clueId);

    List<Activity> getActivityRelationByActivityName(Map<String, String> map);
}
