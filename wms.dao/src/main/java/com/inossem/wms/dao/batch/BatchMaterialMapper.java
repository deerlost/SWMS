package com.inossem.wms.dao.batch;


import java.util.List;
import java.util.Map;

import com.inossem.wms.model.batch.BatchMaterial;

public interface BatchMaterialMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(BatchMaterial record);

	int insertSelective(BatchMaterial record);

	BatchMaterial selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(BatchMaterial record);
	
	int updateByUniqueKeySelective(BatchMaterial record);

	int updateByPrimaryKey(BatchMaterial record);
	
	int deleteByStockInputId(Integer stockInputId);
	
	BatchMaterial selectByUniqueKey(BatchMaterial record);
	
	int insertBatchMaterialForNewBatch(Map<String, Object> map);
	
	int deleteByUniqueKey(BatchMaterial record);

	List<BatchMaterial> selectByStockPositionIds(List<Integer> ids);
	
	List<BatchMaterial> selectByStockBatchIds(List<Integer> ids);
}