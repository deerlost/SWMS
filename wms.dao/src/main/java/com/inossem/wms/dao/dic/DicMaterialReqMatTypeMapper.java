package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicMaterialReqMatType;

public interface DicMaterialReqMatTypeMapper {
	int deleteByPrimaryKey(Integer matReqMatTypeId);

	int insert(DicMaterialReqMatType record);

	int insertSelective(DicMaterialReqMatType record);

	DicMaterialReqMatType selectByPrimaryKey(Integer matReqMatTypeId);

	int updateByPrimaryKeySelective(DicMaterialReqMatType record);

	int updateByPrimaryKey(DicMaterialReqMatType record);

	List<DicMaterialReqMatType> selectByReceiptTypeAndBoardId(Map<String, Object> map);
	
	List<Map<String,Object>> selectPackageTypeListByMatId(@Param("matId")Integer matId);
}