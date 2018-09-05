package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelUserDept;

public interface RelUserDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RelUserDept record);

    int insertSelective(RelUserDept record);

    RelUserDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RelUserDept record);

    int updateByPrimaryKey(RelUserDept record);
}