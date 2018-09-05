package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTransportHeadCw;


public interface IProductionTransportService {

	Map<String, Object> addProductionTransport(BizStockTransportHeadCw stockTransportHead) throws Exception;
	
	List<BizStockTransportHeadCw> getHeadList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getItemListByHeadId(Integer stockTransportId) throws Exception;
	
	List<Map<String, Object>> getPositionItemListByParams(Map<String, Object> map) throws Exception;
	
	void deleteById(Integer stockTransportId,Byte receiptType) throws Exception;
	
}
