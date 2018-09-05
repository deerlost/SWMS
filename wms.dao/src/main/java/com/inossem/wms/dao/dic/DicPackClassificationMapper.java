package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicPackClassification;

public interface DicPackClassificationMapper {
   
    int insert(DicPackClassification record);

    int insertSelective(DicPackClassification record);

    DicPackClassification selectByPrimaryKey(Integer classificatId);

    int updateByPrimaryKeySelective(DicPackClassification record);

    int updateByPrimaryKey(DicPackClassification record);
     
    List<DicPackClassification> selectAll();
}