package com.inossem.wms.dao.dic;

import com.inossem.wms.model.dic.DicMaterialGroup;

public interface DicMaterialGroupMapper {
    int deleteByPrimaryKey(Integer matGroupId);

    int insert(DicMaterialGroup record);

    int insertSelective(DicMaterialGroup record);

    DicMaterialGroup selectByPrimaryKey(Integer matGroupId);

    int updateByPrimaryKeySelective(DicMaterialGroup record);

    int updateByPrimaryKey(DicMaterialGroup record);
    
    Integer selectIdByCode(String matGroupCode);
    
}