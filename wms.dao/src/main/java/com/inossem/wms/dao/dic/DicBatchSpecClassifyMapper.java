package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpecClassify;

public interface DicBatchSpecClassifyMapper {
    int deleteByPrimaryKey(Integer batchSpecClassifyId);

    int insert(DicBatchSpecClassify record);

    int insertSelective(DicBatchSpecClassify record);

    DicBatchSpecClassify selectByPrimaryKey(Integer batchSpecClassifyId);

    int updateByPrimaryKeySelective(DicBatchSpecClassify record);

    int updateByPrimaryKey(DicBatchSpecClassify record);
    
    List<DicBatchSpecClassify> selectAll();
    
}