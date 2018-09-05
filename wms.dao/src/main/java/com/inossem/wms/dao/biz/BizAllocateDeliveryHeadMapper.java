package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizAllocateDeliveryHead;

public interface BizAllocateDeliveryHeadMapper {
    int deleteByPrimaryKey(Integer allocateDeliveryId);

    int insert(BizAllocateDeliveryHead record);

    int insertSelective(BizAllocateDeliveryHead record);

    BizAllocateDeliveryHead selectByPrimaryKey(Integer allocateDeliveryId);

    int updateByPrimaryKeySelective(BizAllocateDeliveryHead record);

    int updateByPrimaryKey(BizAllocateDeliveryHead record);
    
    List<Map<String, Object>> selectByConditionOnPagIng(Map<String, Object> map);
}