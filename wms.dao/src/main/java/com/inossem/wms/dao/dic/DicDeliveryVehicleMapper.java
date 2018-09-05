package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicDeliveryVehicle;

public interface DicDeliveryVehicleMapper {
    int deleteByPrimaryKey(Integer deliveryVehicleId);

    int insert(DicDeliveryVehicle record);

    int insertSelective(DicDeliveryVehicle record);

    DicDeliveryVehicle selectByPrimaryKey(Integer deliveryVehicleId);

    int updateByPrimaryKeySelective(DicDeliveryVehicle record);

    int updateByPrimaryKey(DicDeliveryVehicle record);
    
    List<Map<String,Object>>   selectVehicleByProductId(Integer productId);
    
    List<Map<String,Object>>   queryAllVehicleOnPaging(Map<String, Object> param);
}