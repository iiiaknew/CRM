package com.iaknew.crm.workbench.service;

import com.iaknew.crm.workbench.domain.Clue;
import com.iaknew.crm.workbench.domain.Tran;

public interface ClueService {
    boolean saveClue(Clue clue);

    Clue detail(String id);

    boolean unbind(String id);

    boolean bund(String clueId, String[] activityIds);

    boolean convert(String clueId, Tran tran, String createBy);
}
