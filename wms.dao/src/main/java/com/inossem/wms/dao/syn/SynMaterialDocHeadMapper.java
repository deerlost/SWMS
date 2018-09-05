package com.inossem.wms.dao.syn;

import com.inossem.wms.model.syn.SynMaterialDocHead;

public interface SynMaterialDocHeadMapper {

	SynMaterialDocHead selectByPrimaryKey(Integer matDocId);
	
    int insertSelective(SynMaterialDocHead record);

    int updateByPrimaryKeySelective(SynMaterialDocHead record);

}