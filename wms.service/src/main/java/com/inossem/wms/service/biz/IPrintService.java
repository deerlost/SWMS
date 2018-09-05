package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePosition;

import net.sf.json.JSONObject;

public interface IPrintService {

	String printInputBatch(Integer stockInputId) throws Exception;
	
	String printStockBatchForBatch(JSONObject params) throws Exception;
	
	String printForPosition(JSONObject params) throws Exception;
	
	String printStockPosition(JSONObject params) throws Exception;
	
	String printUrgence(JSONObject params) throws Exception;
	
	String printCGDDForMatnr(JSONObject params) throws Exception;
	
	List<DicWarehousePosition> selectPositionForPrint(Map<String, Object> map) throws Exception;
	
	String printProductTrans(Integer stockTransportId,String userId) throws Exception;
	
	String printErpStock(String fty_id,String location_id,CurrentUser cUser) throws Exception;
	
	String printOutput(Integer allocate_cargo_id ) throws Exception;
	
	String printLims(String sampName,String batchName,String specification) throws Exception;
	
	String printTask(int stockTaskId ,byte receiptType,String userId) throws Exception;
	
	JSONObject printZpl(JSONObject params,String userId)throws Exception;

	String printTransport(Integer stockTransportId ) throws Exception;
}
