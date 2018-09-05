package com.inossem.wms.dao.unit;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.unit.UnitConvVar;

public interface UnitConvVarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnitConvVar record);

    int insertSelective(UnitConvVar record);

    UnitConvVar selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnitConvVar record);

    int updateByPrimaryKey(UnitConvVar record);
    
    int deleteByPrimaryConvId(Integer id);
    
    
    List<Map<String, Object>> queryUnitVarByConvId(Integer unitConvId);
}