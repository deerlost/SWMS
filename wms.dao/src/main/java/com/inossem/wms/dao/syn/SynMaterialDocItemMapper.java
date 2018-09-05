package com.inossem.wms.dao.syn;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.syn.SynMaterialDocItem;

public interface SynMaterialDocItemMapper {

    SynMaterialDocItem selectByPrimaryKey(Map<String,Object> param);

    List<Map<String,Object>> selectListById(Map<String,Object> param);
    
    List<Map<String,Object>> selectMatDocInfoByParams(Map<String,Object> param);
    
    int updateMatDocSapToPolymeric();
    
    int insertSelective(SynMaterialDocItem record);
    
    int updateByPrimaryKeySelective(SynMaterialDocItem record);
    
    List<Map<String,Object>> selectDataForHdzmStorageNum(Map<String,Object> param);
    
}