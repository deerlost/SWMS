package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelUserResources;

public interface RelUserResourcesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RelUserResources record);

    int insertSelective(RelUserResources record);

    RelUserResources selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RelUserResources record);

    int updateByPrimaryKey(RelUserResources record);
}