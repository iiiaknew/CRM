package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {
    Customer getByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);
}
