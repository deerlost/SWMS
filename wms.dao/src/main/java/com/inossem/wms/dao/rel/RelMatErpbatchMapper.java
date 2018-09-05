package com.inossem.wms.dao.rel;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.rel.RelMatErpbatchKey;

public interface RelMatErpbatchMapper {
    int deleteByPrimaryKey(RelMatErpbatchKey key);

    int insert(RelMatErpbatchKey record);

    int insertList(List<Map<String, Object>> list);
    
    int insertSelective(RelMatErpbatchKey record);
    
    List<Map<String,Object>> selectErpBatchListByMatId(@Param("matId")Integer matId,@Param("ftyId")Integer ftyId);
}