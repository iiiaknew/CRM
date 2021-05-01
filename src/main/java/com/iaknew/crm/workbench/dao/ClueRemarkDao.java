package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(String clueId);
}
