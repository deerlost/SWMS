package com.inossem.wms.dao.unit;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.unit.UnitConv;
	
public interface UnitConvMapper {
    int deleteByPrimaryKey(Integer unitConvId);

    int insert(UnitConv record);

    int insertSelective(UnitConv record);

    UnitConv selectByPrimaryKey(Integer unitConvId);

    int updateByPrimaryKeySelective(UnitConv record);

    int updateByPrimaryKey(UnitConv record);
    
    List<Map<String, Object>> selectUnitConvOnPaging(Map<String, Object> paramMap);
    
    UnitConv selectObjByGroupAndMat(Map<String, Object> paramMap);
    
    
    int deleteUnitByArray(Map<String, Object> paramMap);
    
    int deleteUnitVarByArray(Map<String, Object> paramMap);
    
    
    List<Map<String, Object>> selectMatByCode(String matCode);
    
    
    UnitConv selectConvByPrimaryKey(Integer unitConvId);
}