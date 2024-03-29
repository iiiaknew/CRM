package com.iaknew.crm.vo;

import java.util.List;

public class PaginationVO<T> {
    private int total;
    private List<T> dataList;

    public PaginationVO() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public PaginationVO(int total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "PaginationVO{" +
                "total=" + total +
                ", dataList=" + dataList +
                '}';
    }
}
