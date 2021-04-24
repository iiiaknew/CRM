package com.iaknew.crm.workbench.dao;

import com.iaknew.crm.workbench.domain.Customer;

public interface CustomerDao {
    Customer getByName(String company);

    int save(Customer cus);
}
