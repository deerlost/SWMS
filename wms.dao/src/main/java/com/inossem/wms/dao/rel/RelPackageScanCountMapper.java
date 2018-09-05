package com.inossem.wms.dao.rel;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.rel.RelPackageScanCountKey;

public interface RelPackageScanCountMapper {
    int deleteByPrimaryKey(RelPackageScanCountKey key);

    int insert(RelPackageScanCountKey record);

    int insertSelective(RelPackageScanCountKey record);
    
    RelPackageScanCountKey selectByPkgId(@Param("packageId")Integer packageId);
    
    int update(RelPackageScanCountKey record);
}