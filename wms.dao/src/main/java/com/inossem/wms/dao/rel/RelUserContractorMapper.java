package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelUserContractor;

public interface RelUserContractorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RelUserContractor record);

    int insertSelective(RelUserContractor record);

    RelUserContractor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RelUserContractor record);

    int updateByPrimaryKey(RelUserContractor record);
}