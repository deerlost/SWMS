package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizAllocateItem;

public interface BizAllocateItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizAllocateItem record);

    int insertSelective(BizAllocateItem record);

    BizAllocateItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizAllocateItem record);

    int updateByPrimaryKey(BizAllocateItem record);
    
    int updateIsDeleteByHeadId(Integer allocateId);
    
    List<Map<String, Object>> selectAllotItemsByCondition(Map<String, Object> map);
    
    List<Map<String, Object>> selectAllotItemsOnCreateDeliveryOnPaging(Map<String, Object> map);
    
    List<Map<String, Object>> selectAllotItemsOnCreateDBRKOnPagIng(Map<String, Object> map);
    
    Byte selectMinStatusByAllotId(Integer allocateId);
    
    int deleteByAllotId(Integer allocateId);
    
}