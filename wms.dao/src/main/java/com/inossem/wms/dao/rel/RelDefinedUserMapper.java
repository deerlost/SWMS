package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelDefinedUser;

public interface RelDefinedUserMapper {
    int deleteByPrimaryKey(Integer id);



    int insert(RelDefinedUser record);

    int insertSelective(RelDefinedUser record);

    RelDefinedUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RelDefinedUser record);

    int updateByPrimaryKey(RelDefinedUser record);
}