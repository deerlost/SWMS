package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IInputUrgentProduceService {

	List<Map<String, Object>> production_input_list(Map<String, Object> map);

	Map<String, Object> production_input_info(String stock_input_id, String userId);

	List<Map<String, Object>> selectItemList(String stock_input_id, String userId);

//	List<Map<String, Object>> selectErpList(String mat_id);

	List<Map<String, Object>> selectPackageTypeList(String mat_id);

//	List<Map<String, Object>> selectLocationList(int fty_id);

	List<Map<String,Object>> selectPkgList(Map<String, Object> param);
	
	List<Map<String, Object>> selectProduceLineList();

	Map<String,Object> insertProduction(CurrentUser user, JSONObject json) throws Exception;

	Map<String, Object> getMatInfoByCode(String mat_code);

	DicFactory selectFtyNameById(String string);

	Map<String, Object> getInputHeadMap(Map<String, Object> map);

	List<Map<String, Object>> getItemMap(Map<String, Object> map);

	List<Map<String, Object>> getPackageList(List<String> list);

	ErpReturn takeSap(BizStockInputHead bizStockInputHead, JSONObject json)throws Exception;

	Map<String,Object> getUnitIdByCode(String string);

	void modifyNum(BizStockInputHead bizStockInputHead,CurrentUser user, JSONObject json,JSONArray batchArray) throws Exception;

	Map<String,Object> getstockInputHeadAndBatch(String string);

	void updateMatDocCode(ErpReturn er, JSONArray itemArray);

	ErpReturn takeSapWriteOff(JSONArray item_id, JSONArray doc_year) throws Exception;

	void updateStockToWriteOff(int stock_input_id, CurrentUser cuser) throws Exception;

	void updateWriteOffStatus(ErpReturn er,JSONArray item_id, int stock_input_id);

	String selectPackageId(String name);

	void modifyNum2(BizStockInputHead stockInputHead, CurrentUser user, JSONObject json, JSONArray fromObject,JSONArray itemArray) throws Exception;

	List<Map<String, Object>> selectAllErpList();

	List<Map<String, Object>> selectAllPackageTypeList();

	List<Map<String, Object>> selectFactoryList(CurrentUser cuser);

	Map<String, Object> select_mat_info(String mat_code);

	void delete(String stock_input_id,CurrentUser user) throws Exception;

	WmIopRetVal takeMes2(BizStockInputHead stockInputHead, JSONObject json, CurrentUser user, JSONArray itemArray) throws Exception;

	WmIopRetVal takeMesAgain(BizStockInputHead stockInputHead, JSONObject json, CurrentUser user, JSONArray itemArray) throws Exception;

	
	

	
	
}
