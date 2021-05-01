package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.Clue;

public interface ClueDao {
    int saveClue(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
