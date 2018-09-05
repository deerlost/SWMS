package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicDeliveryDriver;

public interface DicDeliveryDriverMapper {
    int deleteByPrimaryKey(Integer deliveryDriverId);

    int insert(DicDeliveryDriver record);

    int insertSelective(DicDeliveryDriver record);

    DicDeliveryDriver selectByPrimaryKey(Integer deliveryDriverId);

    int updateByPrimaryKeySelective(DicDeliveryDriver record);

    int updateByPrimaryKey(DicDeliveryDriver record);
    
    List<Map<String,Object>>  selectDriverByProductlineId(Integer productLineId);
    
    List<Map<String,Object>>  queryAllDriverOnPaging(Map<String, Object> param);
    
}