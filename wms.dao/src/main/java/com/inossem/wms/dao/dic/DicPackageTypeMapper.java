package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.vo.DicPackageTypeViewVo;
import com.inossem.wms.model.vo.DicPackageTypeVo;

public interface DicPackageTypeMapper {
    int deleteByPrimaryKey(Integer packageTypeId);

    int insert(DicPackageType record);

    int insertSelective(DicPackageType record);

    DicPackageType selectByPrimaryKey(Integer packageTypeId);

    int updateByPrimaryKeySelective(DicPackageType record);

    int updateByPrimaryKey(DicPackageType record);
    
    List<DicPackageType> selectByMatId(Integer matId);
    
    List<DicPackageTypeVo> selectDicPackageTypeList(Map<String, Object> param);
    
    DicPackageTypeVo selectDicPackageTypeById(@Param("packageTypeId")Integer packageTypeId);
    
    int checkPkgIsUsed(@Param("packageId")Integer packageId,@Param("packageCode")String packageCode);
    
    List<DicPackageTypeViewVo> selectPackageOnpaging(Map<String, Object> param);
      
    int updateFreezeByPackIds(Integer[] array);


    int updateByPackId(Integer packageTypeId);
     
    List<DicPackageType> getPackageTypeAll();
    
    List<DicPackageType> selectListByClass(@Param("classificatId") Integer classificatId);
    
    
    List<Map<String,Object>>   getPackageTypeByMatUnitId(Integer matUnitId);
    
    
    List<DicPackageType>   getPackageTypeBySnKey(String snSerialKey);
    
      Integer getCountByCode(String code);
     
}