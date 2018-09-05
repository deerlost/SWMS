package com.inossem.wms.service.intfc;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;

/**
 * 出库过账接口
 * 
 * @author 高海涛
 * @date 2018年1月23日
 */
public interface IStockOutput {

	Map<String, Object> postingForMatReqOrder(int stockOutputId, String userId) throws Exception;

	Map<String, Object> postingForReserveOrder(int stockOutputId, String userId) throws Exception;

	Map<String, Object> postingForSalesOrder(int stockOutputId, String userId) throws Exception;

	Map<String, Object> postingForPurchaseOrder(int stockOutputId, String userId) throws Exception;

	Map<String, Object> postingForOtherOrder(int stockOutputId, String userId) throws Exception;

	Map<String, Object> postingForHdzm(int stockOutputId) throws Exception;

	Map<String, Object> postingForAllocateOrder(int stockOutputId, String userId) throws Exception;

	List<Map<String, Object>> getSalesOrderList(String salesOrderCode, String userId) throws Exception;

	Map<String, Object> getSalesViewBySap(String salesOrderCode, CurrentUser user) throws Exception;

	List<Map<String, Object>> getPurchaseOrderList(Map<String, Object> searchMap) throws Exception;

	Map<String, Object> getPurchaseViewBySap(Map<String, Object> searchMap) throws Exception;

	Map<String, Object> writeOff(Map<String, Object> param, List<Integer> ridList, CurrentUser user) throws Exception;

	List<Map<String, Object>> getReserveViewBySap(String refer_reserve_code, CurrentUser user) throws Exception;
}
