package com.inossem.wms.dao.rel;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.rel.RelTaskUserWarehouse;

public interface RelTaskUserWarehouseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RelTaskUserWarehouse record);

    int insertSelective(RelTaskUserWarehouse record);

    RelTaskUserWarehouse selectByPrimaryKey(Integer id);
    
    
    List<RelTaskUserWarehouse> selectByWhIdAndType(Map<String, Object> map);

    int updateByPrimaryKeySelective(RelTaskUserWarehouse record);

    int updateByPrimaryKey(RelTaskUserWarehouse record);
    
    List<Map<String, Object>> selectByTypeOnPaging(Map<String, Object> param);
    
    
}