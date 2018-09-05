package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelDefinedField;

public interface RelDefinedFieldMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RelDefinedField record);

    int insertSelective(RelDefinedField record);

    RelDefinedField selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RelDefinedField record);

    int updateByPrimaryKey(RelDefinedField record);
}