package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IInputUrgentTransportService {




	BizStockInputHead getBizStockInputHeadByStockInputId(int stockInputId) throws Exception;
	
	String selectNowTime() throws Exception;

	List<Map<String, Object>> getTransportInputList(Map<String, Object> map);

	Map<String, Object> getTransportInputInfo2(Map<String, Object> map);

	List<Map<String, Object>> getInputTransportItemListByID(Integer stock_input_id);

	List<Map<String, Object>> selectAllclassType();

	Integer selectNowClassType();

	List<Map<String, Object>> select_stock_task(Map<String, Object> map);

	List<Map<String, Object>> getStockMatListByTaskId(Map<String, Object> map);

	BizStockInputTransportHead insertStock(List<Map<String,Object>> itemList,CurrentUser user,JSONObject json);

	void updateStockToWriteOff(Integer stock_input_id,CurrentUser cuser) throws Exception;

	List<Map<String, Object>> getMatListByID(JSONArray myJsonArray);

	ErpReturn takeSap(BizStockInputTransportHead bizStockInputTransportHead,List<Map<String, Object>> itemList)throws Exception;

	void modifyNum(List<Map<String,Object>> itemList,CurrentUser user,BizStockInputTransportHead bizStockInputTransportHead) throws Exception ;

	void updateStockHeandStatus(List<Map<String,Object>> itemList,Integer stock_input_id);

	void updateTransportStatus(List<Map<String, Object>> itemList, CurrentUser user);

	ErpReturn  takeSapWriteOff(JSONArray item_id,JSONArray doc_yea) throws Exception;

	ErpReturn takeSapWriteOff(BizStockInputTransportHead bizStockInputTransportHead,
			List<Map<String, Object>> itemList) throws Exception ;

	void setHistory(BizStockInputTransportHead bizStockInputTransportHead, CurrentUser user, JSONObject json,List<Map<String,Object>> itemList);

	BizStockInputTransportHead selectEntity(BizStockInputTransportHead bizStockInputTransportHead);

	void updateMatDocCode(ErpReturn er, BizStockInputTransportHead bizStockInputTransportHead);

	void delete(String stock_input_id);

	void modifyNum2(List<Map<String, Object>> itemList, CurrentUser user,BizStockInputTransportHead bizStockInputTransportHead) throws Exception;

	WmIopRetVal takeMes(BizStockInputTransportHead bizStockInputTransportHead, List<Map<String, Object>> itemList,CurrentUser user)  throws Exception;

	void dealBeforeStatus(String stock_input_id);

	WmIopRetVal takeMesAgain(BizStockInputTransportHead bizStockInputTransportHead,CurrentUser user) throws Exception;

	void selectIsWriteOff(Integer stock_input);


	BizStockInputTransportHead submitTransport(JSONObject json , CurrentUser cUser , BizStockInputTransportHead head)throws Exception;


	

	

	
	
}
