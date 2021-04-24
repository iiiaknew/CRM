package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {
    int unbind(String id);

    int bund(ClueActivityRelation car);
}
