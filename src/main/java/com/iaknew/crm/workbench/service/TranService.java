package com.iaknew.crm.workbench.service;

import com.iaknew.crm.workbench.domain.Tran;
import com.iaknew.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListBiTranId(String tranId);

    boolean changeStage(Tran tran, TranHistory th);

    Map<String, Object> getCharts();
}
