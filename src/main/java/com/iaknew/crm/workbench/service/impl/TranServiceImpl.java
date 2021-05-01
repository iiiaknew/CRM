package com.iaknew.crm.workbench.service.impl;

import com.iaknew.crm.workbench.service.TranService;
import com.iaknew.crm.utils.DateTimeUtil;
import com.iaknew.crm.utils.SqlSessionUtil;
import com.iaknew.crm.utils.UUIDUtil;
import com.iaknew.crm.workbench.dao.CustomerDao;
import com.iaknew.crm.workbench.dao.TranDao;
import com.iaknew.crm.workbench.dao.TranHistoryDao;
import com.iaknew.crm.workbench.domain.Customer;
import com.iaknew.crm.workbench.domain.Tran;
import com.iaknew.crm.workbench.domain.TranHistory;

import java.util.List;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Tran tran, String customerName) {
        // 创建交易操作：
        /*
            1. 根据customerName查询该客户是否存在
                    若存在 取其id 赋值customerId
                    若不存在 创建新的客户 取其id 赋值customerId
            2. 创建交易
            3. 创建交易历史
         */
        boolean flag = true;

        Customer customer = customerDao.getByName(customerName);
        // 如果客户为空 （没有该客户）创建客户
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setDescription(tran.getDescription());
            customer.setContactSummary(tran.getContactSummary());
            customer.setName(customerName);
            customer.setOwner(tran.getOwner());
            customer.setNextContactTime(tran.getNextContactTime());

            int count = customerDao.save(customer);
            if (count != 1){
                flag = false;
            }
        }
        // 不为空
        tran.setCustomerId(customer.getId());

        // 添加交易
        int count = tranDao.save(tran);
        if (count != 1){
            flag = false;
        }

        // 创建交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setMoney(tran.getMoney());
        th.setStage(tran.getStage());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getCreateBy());
        th.setTranId(tran.getId());
        th.setCreateTime(DateTimeUtil.getSysTime());
        int count2 = tranHistoryDao.save(th);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        return tranDao.detail(id);
    }

    @Override
    public List<TranHistory> getHistoryListBiTranId(String tranId) {
        return tranHistoryDao.getHistoryListBiTranId(tranId);
    }
}
