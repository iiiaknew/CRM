package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {
    int unbind(String id);

    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> getListById(String clueId);

    int delete(String clueId);
}
