package com.iaknew.crm.settings.service.impl;

import com.iaknew.crm.settings.dao.DicTypeDao;
import com.iaknew.crm.settings.dao.DicValueDao;
import com.iaknew.crm.settings.domain.DicType;
import com.iaknew.crm.settings.domain.DicValue;
import com.iaknew.crm.settings.service.DicService;
import com.iaknew.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();

        // 获取所有类型（code）
        List<DicType> typeList = dicTypeDao.getTypeList();
        // 遍历list
        for (DicType dt : typeList){
            // 根据每个type 获取对应的值（value）
            String code = dt.getCode();
            List<DicValue> valueList = dicValueDao.getValueList(code);

            map.put(code, valueList);
        }

        return map;
    }
}
