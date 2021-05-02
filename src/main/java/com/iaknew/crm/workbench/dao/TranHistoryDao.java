package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListBiTranId(String tranId);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
