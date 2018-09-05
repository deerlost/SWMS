package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpec;

public interface DicBatchSpecMapper {
    int deleteByPrimaryKey(Integer batchSpecId);

    int insert(DicBatchSpec record);

    int insertSelective(DicBatchSpec record);

    DicBatchSpec selectByPrimaryKey(Integer batchSpecId);

    int updateByPrimaryKeySelective(DicBatchSpec record);

    int updateByPrimaryKey(DicBatchSpec record);
    
    List<DicBatchSpec> selectByMatId(Integer matId);
}