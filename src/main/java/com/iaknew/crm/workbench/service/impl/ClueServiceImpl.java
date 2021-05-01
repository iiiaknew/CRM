package com.iaknew.crm.workbench.service.impl;

import com.iaknew.crm.utils.DateTimeUtil;
import com.iaknew.crm.utils.SqlSessionUtil;
import com.iaknew.crm.utils.UUIDUtil;
import com.iaknew.crm.workbench.dao.*;
import com.iaknew.crm.workbench.domain.*;
import com.iaknew.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {
    // 线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    // 客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    // 联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    // 交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean saveClue(Clue clue) {
        boolean flag = true;

        int count = clueDao.saveClue(clue);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public boolean unbind(String id) {
        boolean flag = true;

        int count = clueActivityRelationDao.unbind(id);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bund(String clueId, String[] activityIds) {
        boolean flag = true;

        // 遍历数组 获取每一个id和activityId
        for (String activityId : activityIds){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(activityId);

            int count = clueActivityRelationDao.bund(car);
            if (count != 1){
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;

        // (1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);

        // (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getByName(company);

        if (customer == null){
            // 如果没有此客户
            customer = new Customer();
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setId(UUIDUtil.getUUID());
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);

            // 添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }
        }


        /*
        *   第2步之后 客户对象已经创建
         */


        // (3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());

        int count2 = contactsDao.save(contacts);
        if (count2 != 1){
            flag = false;
        }

        // (4) 线索备注转换到客户备注以及联系人备注
        // 获取和线索关联的备注信息
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);

        // 列出每一条备注
        for (ClueRemark clueRemark : clueRemarkList){
            // 获取备注信息
            String noteContent = clueRemark.getNoteContent();

            // 创建客户备注对象 创建客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setEditFlag("0");
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);

            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1){
                flag = false;
            }

            // 创建联系人备注对象 创建联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setEditFlag("0");
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setContactsId(contacts.getId());

            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1){
                flag = false;
            }
        }

        // (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        // 获取线索和市场活动关联的对象
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListById(clueId);

        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();

            // 创建联系人和市场活动关联对象
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setId(UUIDUtil.getUUID());

            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1){
                flag = false;
            }
        }

        // (6) 如果有创建交易需求，创建一条交易
        if (tran != null){
            // tran已经封装好的信息：
            // id,money,name,expectedDate,stage,activityId,createTime,createBy
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setDescription(clue.getDescription());
            tran.setContactsId(contacts.getId());

            int count6 = tranDao.save(tran);
            if (count6 != 1){
                flag = false;
            }

            // (7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setId(UUIDUtil.getUUID());

            int count7 = tranHistoryDao.save(tranHistory);
            if (count7 != 1){
                flag = false;
            }
        }

        // (8) 删除线索备注
        int count8 = clueRemarkDao.delete(clueId);
        if (count8 != clueRemarkList.size()){
            flag = false;
        }

        // (9) 删除线索和市场活动的关系
        int count9 = clueActivityRelationDao.delete(clueId);
        if (count9 != clueActivityRelationList.size()){
            flag = false;
        }

        // (10) 删除线索
        int count10 = clueDao.delete(clueId);
        if (count10 != 1){
            flag = false;
        }

        return flag;
    }
}
