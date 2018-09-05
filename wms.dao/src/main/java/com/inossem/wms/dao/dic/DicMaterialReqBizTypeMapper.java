package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicMaterialReqBizType;

public interface DicMaterialReqBizTypeMapper {
	int deleteByPrimaryKey(Integer matReqBizTypeId);

	int insert(DicMaterialReqBizType record);

	int insertSelective(DicMaterialReqBizType record);

	DicMaterialReqBizType selectByPrimaryKey(Integer matReqBizTypeId);

	int updateByPrimaryKeySelective(DicMaterialReqBizType record);

	int updateByPrimaryKey(DicMaterialReqBizType record);

	List<DicMaterialReqBizType> selectByReceiptTypeAndBoardId(Map<String, Object> map);
}