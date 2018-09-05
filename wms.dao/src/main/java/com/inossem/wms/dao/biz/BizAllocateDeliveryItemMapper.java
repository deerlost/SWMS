package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizAllocateDeliveryItem;

public interface BizAllocateDeliveryItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizAllocateDeliveryItem record);

    int insertSelective(BizAllocateDeliveryItem record);

    BizAllocateDeliveryItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizAllocateDeliveryItem record);

    int updateByPrimaryKey(BizAllocateDeliveryItem record);
    
    int updateByAllotItemIdSelective(BizAllocateDeliveryItem record);
    
    List<Map<String, Object>> selectByCondition(Map<String, Object> map);
   
    Byte selectMinStatusByDeliveryId(Integer allocateDeliveryId);
}