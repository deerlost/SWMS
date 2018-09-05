package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizAllocateHead;
import com.inossem.wms.model.vo.BizAllocateHeadVo;

public interface BizAllocateHeadMapper {
    int deleteByPrimaryKey(Integer allocateId);

    int insert(BizAllocateHead record);

    int insertSelective(BizAllocateHead record);

    BizAllocateHead selectByPrimaryKey(Integer allocateId);

    int updateByPrimaryKeySelective(BizAllocateHead record);

    int updateByPrimaryKey(BizAllocateHead record);
    
    List<BizAllocateHeadVo>  selectlistByConditionOnPaging(Map<String, Object> map);

    List<String> getAllocateDeliveryCodeById(Integer allocateId);
}