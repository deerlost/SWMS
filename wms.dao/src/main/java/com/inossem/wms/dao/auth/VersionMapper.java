package com.inossem.wms.dao.auth;

import com.inossem.wms.model.auth.Version;

public interface VersionMapper {
    int deleteByPrimaryKey(Integer versionCode);

    int insert(Version record);

    int insertSelective(Version record);

    Version selectByPrimaryKey(Integer versionCode);

    int updateByPrimaryKeySelective(Version record);

    int updateByPrimaryKey(Version record);
    
    Version selectForMaxVersion();
}