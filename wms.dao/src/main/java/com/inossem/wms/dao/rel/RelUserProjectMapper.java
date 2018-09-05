package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelUserProject;

public interface RelUserProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RelUserProject record);

    int insertSelective(RelUserProject record);

    RelUserProject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RelUserProject record);

    int updateByPrimaryKey(RelUserProject record);
}