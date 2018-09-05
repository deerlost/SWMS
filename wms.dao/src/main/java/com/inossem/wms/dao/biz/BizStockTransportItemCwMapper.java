package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;

import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizStockTransportHeadCw;
import java.util.List;

import com.inossem.wms.model.biz.BizStockTransportItemCw;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.vo.BizStockTransportItemVo;

public interface BizStockTransportItemCwMapper {
	int deleteByPrimaryKey(Integer itemId);

	int insert(BizStockTransportItemCw record);

	int insertSelective(BizStockTransportItemCw record);

	BizStockTransportItemCw selectByPrimaryKey(Integer itemId);

	int updateByPrimaryKeySelective(BizStockTransportItemCw record);

	int updateDeleteByPrimaryKey(BizStockTransportItemCw record);

	int updateByPrimaryKey(BizStockTransportItemCw record);

	List<Map<String, Object>> getMaterial(@PathVariable("matcode") String matcode);

	BatchMaterial getMaterialbyBatchOrID(Long batch, Integer matid);

	StockPosition getStockPositionBymatid(Integer matid);

	DicPackageType getPackageTypeByID(Integer package_type_id);

	// DicStockType getStockTypeByID(Integer stock_type_id);

	BizStockTransportHeadCw getDumpByTransportID(Integer stock_transport_id);

	DicUnit getUnitByID(int unitid);

	List<Map<String, Object>> selectMatListByParam(Map<String, Object> map);

	void InsertStockTransportItemCw(BizStockTransportItemCw stItemCw);

	List<Map<String, Object>> selectItemById(Integer stock_transport_id);

	List<Map<String, Object>> selectItemByIdForPrint(Integer stock_transport_id);

	List<BizStockTransportItemCw> selectItemVoById(Integer stock_transport_id);

	List<BizStockTransportItemCw> selectByStockTaskId(Integer stockTaskId);

	List<BizStockTransportItemCw> selectByStockTaskIdWriteOff(Integer stockTaskId);

	List<Map<String, Object>> queryOutput(Map<String, Object> param);

	List<Map<String, Object>> queryTransStockCwOutAndInOnPaging(Map<String, Object> param);
	
	void deleteDumpItem(Map<String, Object> param);
}