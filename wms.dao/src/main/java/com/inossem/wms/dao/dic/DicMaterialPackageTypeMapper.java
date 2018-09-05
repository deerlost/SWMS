package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicMaterialPackageType;
import com.inossem.wms.model.vo.DicMaterialPackageTypeVo;

public interface DicMaterialPackageTypeMapper {
    int deleteByPrimaryKey(Integer dicMatPackageTypeId);

    int insert(DicMaterialPackageType record);

    int insertSelective(DicMaterialPackageType record);

    DicMaterialPackageType selectByPrimaryKey(Integer dicMatPackageTypeId);

    int updateByPrimaryKeySelective(DicMaterialPackageType record);

    int updateByPrimaryKey(DicMaterialPackageType record);
    
    List<DicMaterialPackageTypeVo> selectPackageTypeOnpaging(Map<String ,Object> map);
    
    int updateFreezeById(Integer[] ids);
    
    Map<String,Object> searchMatCodeOrName(String search);
    
    Integer  getMatPackageFreezeCount(Map<String,Object> map);
}