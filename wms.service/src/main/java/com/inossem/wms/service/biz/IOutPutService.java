package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.vo.BizStockOutputHeadVo;

import net.sf.json.JSONObject;

/**
 * 出库单Service
 * 
 * @author 高海涛
 * @date 2018年1月19日
 */
public interface IOutPutService {

	List<Map<String, Object>> getOrderList(Map<String, Object> param);

	BizStockOutputHeadVo getOrderView(String stock_output_id) throws Exception;

	List<Map<String, Object>> getStockData(Map<String, Object> param);

	List<Map<String, Object>> getStockDataCommend(Map<String, Object> param);

	String deleteOutputOrder(int stockOutputId, String userId);

	String writeOff(JSONObject json, CurrentUser user) throws Exception;

	List<Map<String, Object>> getMatReqList(Map<String, Object> param);

	Map<String, Object> getMatReqViewById(Map<String, Object> param);

	Map<String, Object> getOrderViewByMatReq(Map<String, Object> param) throws Exception;

	List<Map<String, Object>> getSalesOrderList(String salesOrderCode, String userId) throws Exception;

	Map<String, Object> getSalesViewBySap(String salesOrderCode, CurrentUser user) throws Exception;

	List<Map<String, Object>> getPurchaseOrderList(Map<String, Object> param) throws Exception;

	Map<String, Object> getPurchaseViewBySap(Map<String, Object> param) throws Exception;

	Map<String, Object> saveOrderToSap(JSONObject result, String userId) throws Exception;

	String saveOrderToFinish(Map<String, Object> result, int stockOutputId, String userId) throws Exception;

	JSONObject saveOutputOrderData(JSONObject jsonData, CurrentUser user, boolean isPDA, byte orderType)
			throws Exception;

	List<Map<String, Object>> getAllocateList(Map<String, Object> param);

	Map<String, Object> getAllocateById(Map<String, Object> param);

	Map<String, Object> getDbckByStockOutputId(String stockOutputId) throws Exception;

	List<Map<String, Object>> getOrderListAllocate(Map<String, Object> param);

	JSONObject getReserveViewBySap(String referReserveCode, CurrentUser user) throws Exception;

	/**
	 * 
	 * 根据出库单id查询出库单
	 * 
	 * @author 刘宇 2018.04.18
	 * @param stockOutputId
	 * @return
	 * @throws Exception
	 */
	BizStockOutputHead getBizStockOutputHeadByStockOutPutId(int stockOutputId) throws Exception;
}
