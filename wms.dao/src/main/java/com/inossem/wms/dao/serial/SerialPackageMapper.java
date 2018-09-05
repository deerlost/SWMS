package com.inossem.wms.dao.serial;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.serial.SerialPackage;
import com.inossem.wms.model.vo.SerialPackageVo;

public interface SerialPackageMapper {
	
    int deleteByPrimaryKey(Integer packageId);

    int insert(SerialPackage record);

    int insertSelective(SerialPackage record);

    SerialPackage selectByPrimaryKey(Integer packageId);

    int updateByPrimaryKeySelective(SerialPackage record);

    int updateByPrimaryKey(SerialPackage record);
    
    SerialPackage selectByCode(@Param("packageCode")String packageCode);
    
    List<SerialPackageVo> selectListOnPaging(Map<String, Object> param);
}