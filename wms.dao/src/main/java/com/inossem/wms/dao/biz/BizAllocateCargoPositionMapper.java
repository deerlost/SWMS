package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizAllocateCargoPosition;

public interface BizAllocateCargoPositionMapper {
    int deleteByPrimaryKey(Integer itemPositionId);

    int insert(BizAllocateCargoPosition record);

    int insertSelective(BizAllocateCargoPosition record);

    BizAllocateCargoPosition selectByPrimaryKey(Integer itemPositionId);

    int updateByPrimaryKeySelective(BizAllocateCargoPosition record);

    int updateByPrimaryKey(BizAllocateCargoPosition record);
    
    List<Map<String,String>> selectByCondition(Map<String,String> map);
    //根据配货单id删除配货数量信息
    int updateByAllocateCargoId(Map<String ,Object> map);
    
    List<Map<String,String>> selectPositionBySuperId(Map<String ,Object> map);
    
    Integer deleteAllocatePositionByHeadId(Integer id);
    
    List<Map<String,Object>> selectPositionByCargoId(Integer allocateCargoId);

    List<Map<String,Object>> selectPositionByCargoIdForPrint(Integer allocateCargoId);
}