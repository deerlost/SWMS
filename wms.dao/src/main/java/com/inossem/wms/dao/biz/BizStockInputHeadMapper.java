package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;
import com.inossem.wms.model.biz.BizStockInputHead;

public interface BizStockInputHeadMapper {
	int deleteByPrimaryKey(Integer stockInputId);

	int insert(BizStockInputHead record);

	int insertSelective(BizStockInputHead record);

	BizStockInputHead selectByPrimaryKey(Integer stockInputId);

	BizStockInputHead selectByInputCode(String stockInputCode);

	List<BizStockInputHead> selectByConditionOnPagIng(Map<String, Object> map);

	List<Map<String, Object>> selectAllocateInputByConditionOnPagIng(Map<String, Object> map);

	int updateByPrimaryKeySelective(BizStockInputHead record);

	int updateByPrimaryKey(BizStockInputHead record);

	int deleteByStockInputId(Integer stockInputId);

	BizStockInputHead selectByInputCodeUrgence(String stockInputCode);

	BizStockInputHead selectByVirtualInputCode(Map<String, Object> param);

	String selectMatNameBycode(String mtrlCode);

	String getUnitRelSapAndWms(Map<String, Object> param);

	String getUnitCodeByName(String string);

	String getUnitRelMesAndWms(Map<String, Object> param);



	

}