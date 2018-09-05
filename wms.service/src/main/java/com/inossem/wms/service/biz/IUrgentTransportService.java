package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.ErpReturn;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IUrgentTransportService {

	public JSONArray getSynType();
	
	List<Map<String, Object>> getMoveList();

	void modifyNum(JSONObject json, JSONArray itemList, CurrentUser cUser,BizStockInputTransportHead transHead) throws Exception;

	void updateMatDocCode(ErpReturn er, BizStockInputTransportHead transHead) throws Exception;

	void setHistory(BizStockInputTransportHead transHead, CurrentUser cUser, JSONObject json,
			JSONArray itemList) throws Exception;

	ErpReturn takeSap(BizStockInputTransportHead transHead, JSONObject json, JSONArray itemList) throws Exception;

	ErpReturn takeSapWriteOff(BizStockInputTransportHead bizStockInputTransportHead,
			List<Map<String, Object>> list)  throws Exception;

	 void updateStockToWriteOff(Integer valueOf,BizStockInputTransportHead bizStockInputTransportHead,
				List<Map<String, Object>> list,CurrentUser cUser)  throws Exception;

	 void delete(String stock_input_id);

	

	

	


}
