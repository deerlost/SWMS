package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizStockTransportHeadCw;
import com.inossem.wms.model.biz.BizStockTransportItemCw;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.key.StockPositionKey;
import com.inossem.wms.model.stock.StockBatch;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicStockLocationVo;

import net.sf.json.JSONObject;

public interface MaterialTransportService {

	List<Map<String, Object>> getDumpList(Map<String, Object> param);

	List<DicMoveType> getMoveList()throws Exception;

	List<DicStockLocationVo> locListByFty(Integer ftyid);

	List<DicFactoryVo> selectFtylist();

	List<Map<String, Object>> getMaterial(String matcode);

	BatchMaterial getMaterialbyBatchOrID(Long batch, Integer matid);

	String getNowTime();

	List<Map<String, Object>> getClassMap();

	StockPositionKey getStockPositionBymatid(Integer matid);

	DicPackageType getPackageTypeByID(Integer package_type_id);

	// DicStockType getStockTypeByID(Integer stock_type_id);

	Map<String, Object> getDumpByTransportID(Integer stock_transport_id) throws Exception;

	void InsertDump(BizStockTransportHeadCw st);

	String getMaterialTransCode();

	// BizStockTransportHeadCw getDumpItemByTransportID(int stock_transport_id)
	// throws Exception;

	DicUnit getUnitByID(int unitid);

	List<Map<String, Object>> getMatListByParam(Map<String, Object> map) throws Exception;

	int InsertStockTransportHeadCw(JSONObject json, String userId) throws Exception;

	int InsertBizStockTaskReqHead(BizStockTransportHeadCw rerecord);

	void InsertBizStockTasReqkItem(BizStockTransportItemCw rerecord, int stock_task_req_id, int i,
			BizStockTransportHeadCw record);

	int selectReceipt(Integer moveTypeId);

	void deleteDump(Integer stockTransportId) throws Exception;

	List<Map<String, Object>> selectERP(String mat_id, int fty_id,int package_id) throws Exception;
}
