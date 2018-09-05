package com.inossem.wms.dao.batch;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.batch.BatchMaterialSpec;
import com.inossem.wms.model.dic.DicBatchSpec;
import com.inossem.wms.model.key.BatchMaterialSpecKey;

public interface BatchMaterialSpecMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(BatchMaterialSpec record);

	int insertSelective(BatchMaterialSpec record);

	BatchMaterialSpec selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(BatchMaterialSpec record);

	int updateByPrimaryKey(BatchMaterialSpec record);
	
	int updateByUniqueKeySelective(BatchMaterialSpec record);

	List<BatchMaterialSpec> selectByUniqueKey(BatchMaterialSpecKey key);
	
	int insertList(List<BatchMaterialSpec> bmsList);
	
	int deleteByStockInputId(Integer stockInputId);
	
	List<DicBatchSpec> selectBatchSpecByUniqueKey(BatchMaterialSpecKey key);
	int deleteByFtyIdAndMatIdAndBatch(Map<String, Object> param) throws Exception;
	
	List<BatchMaterialSpec> selectRecentBatchList(Map<String, Object> param);

	
	int insertBatchMaterialSpecForNewBatch(Map<String, Object> map);

	
	/**
	 * 批量插入批次特性
	 * @param mch1List
	 * @return
	 */
	int insertMCH1List(List<BatchMaterialSpec> mch1List);
	/**
	 * 根据 ftyId matId batch 删除批次特性值
	 * @param mch1
	 * @return
	 */
	int deleteByftyIdMatIdBatch(BatchMaterialSpec record);

}