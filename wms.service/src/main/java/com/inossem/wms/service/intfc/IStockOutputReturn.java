package com.inossem.wms.service.intfc;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizDeliveryOrderHead;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.biz.BizReserveOrderItem;
import com.inossem.wms.model.biz.BizSaleOrderHead;
import com.inossem.wms.model.biz.BizSaleOrderItem;
import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.biz.BizStockOutputReturnItem;

import net.sf.json.JSONObject;

/**
 * 退库过账接口
 * 
 * @author 兰英爽
 * @date 2018年2月27日
 */
public interface IStockOutputReturn {

	/**
	 * 从SAP 查询销售合同订单列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	JSONObject getSaleOrderListFromSAP(Map<String, Object> map) throws Exception;

	// sap销售单抬头
	List<BizSaleOrderHead> getSaleHeadListFromSAP(Map<String, Object> map) throws Exception;

	// sap销售单明细
	List<BizSaleOrderItem> getSaleItemListFromSAP(Map<String, Object> map) throws Exception;

	/**
	 * 从SAP 查询预留单列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	JSONObject getReserveOrderListFromSap(String receiptCode, String createUser) throws Exception;

	// 解析sap销售单抬头
	List<BizReserveOrderHead> getReserveHeadListFromSapReturnObj(JSONObject returnObj) throws Exception;

	// 解析sap销售单明细
	List<BizReserveOrderItem> getReserveItemListFromSapReturnObj(JSONObject returnObj) throws Exception;

	// SAP过账 领料退库
	Map<String, Object> submitToSapForMatreqForOneCorp(BizStockOutputReturnHead returnHead,
			List<BizStockOutputReturnItem> returnItems) throws Exception;

	Map<String, Object> submitToSapForMatreqForTwoCorp(BizStockOutputReturnHead returnHead,
			List<BizStockOutputReturnItem> returnItems) throws Exception;

	// SAP过账 销售退库
	Map<String, Object> submitToSapForSale(BizStockOutputReturnHead returnHead,
			List<BizStockOutputReturnItem> returnItems) throws Exception;
	
	//川维 交货单详情	
	BizDeliveryOrderHead getDeliveryOrderInfo (Map<String, Object> map) throws Exception;

}
