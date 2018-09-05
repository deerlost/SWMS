package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelMoveTypeKey;

public interface RelMoveTypeMapper {
    int deleteByPrimaryKey(RelMoveTypeKey key);

    int insert(RelMoveTypeKey record);

    int insertSelective(RelMoveTypeKey record);
}