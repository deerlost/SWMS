package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputPackageItem;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.BizStockInputTransportItem;
import com.inossem.wms.model.biz.ErpReturn;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface VirtualTransportService {

	List<Map<String, Object>> selectVirtualDumplist(Map<String, Object> param);

	JSONArray getSynType();

	BizStockInputHead selectInputHeadByVirtualInputCode(String stockInputCode, byte receiptType);

	List<Map<String, Object>> selectInputItemByStockInputId(Integer stockInputId);

	int insertSelectiveStockInputTransHead(BizStockInputTransportHead recordHead);

	int insertSelectiveStockInputTransItem(BizStockInputTransportItem recordItem);

	int insertSelectiveStockInputPackItem(BizStockInputPackageItem record);

	Map<String, Object> selectStockTransportHeadbyID(Integer stockTransportId);

	List<Map<String, Object>> selectByStockTransportId(Integer stockTransportId);

	BizStockInputHead selectInputHeadByVirtualInputCode(Map<String, Object> param);

	ErpReturn takeSap(BizStockInputTransportHead transHead, JSONObject json, List<Map<String, Object>> inputItemList)
			throws Exception;

	void modifyNum(JSONObject json, List<Map<String, Object>> inputItemList, CurrentUser cUser,BizStockInputTransportHead transHead) throws Exception;

	



	void updateMatDocCode(ErpReturn er, BizStockInputTransportHead transHead);



	void setHistory(BizStockInputTransportHead transHead, CurrentUser cUser, JSONObject json,
			List<Map<String, Object>> inputItemList);

	BizStockInputTransportHead getTransHead(String string);

	void updateStockToWriteOff(Integer valueOf, CurrentUser cUser,BizStockInputTransportHead bizStockInputTransportHead, List<Map<String, Object>> list) throws Exception;

	ErpReturn takeSapWriteOff(BizStockInputTransportHead bizStockInputTransportHead, List<Map<String, Object>> list) throws Exception;

	void delete(String stock_input_id);

}
