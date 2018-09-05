package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.batch.BatchMaterialSpec;
import com.inossem.wms.model.biz.BizDeliveryOrderHead;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.biz.BizSaleOrderHead;
import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.biz.BizStockOutputReturnItem;
import com.inossem.wms.model.vo.BizStockOutputReturnHeadVo;

import net.sf.json.JSONObject;

public interface IReturnService {
	List<BizStockOutputReturnHeadVo> getReturnList(BizStockOutputReturnHeadVo paramVo);

	List<Map<String, Object>> getOutputListByOrderCode(Map<String, Object> param);

	Map<String, Object> getOutputInfoByOutputId(Map<String, Object> param);

	// 获取领料退库明细
	Map<String, Object> getMatreqReturnInfo(Map<String, Object> param) throws Exception;

	// 获取销售或预留退库明细
	Map<String, Object> getSaleOrReserveReturnInfo(Integer returnId) throws Exception;
	//川维销售退库
	Map<String, Object> getSaleReturnInfo(Integer returnId) throws Exception;

	// 根据物料明细List,转成带批次特性的物料明细List
	List<Map<String, Object>> addBatchMaterialSpecForItemList(Integer ftyId, List<Map<String, Object>> itemList);

	// 从sap获取销售合同订单列表
	List<BizSaleOrderHead> getSaleOrderList(Map<String, Object> map) throws Exception;

	// 从sap获取销售合同订单概览
	Map<String, Object> getSaleOrderInfo(Map<String, Object> map) throws Exception;

	// 从sap获预留订单列表
	List<BizReserveOrderHead> getReserveOrderList(String receiptCode, String createUser) throws Exception;

	// 从sap获取预留订单概览
	Map<String, Object> getReserveOrderInfo(String receiptCode, String createUser) throws Exception;

	// 获取该工厂，该物料最近的批次号
	List<BatchMaterialSpec> getRecentBatchList(Map<String, Object> param);

	// 领料退库单暂存/过账
	JSONObject matreqSaveAndPost(JSONObject json) throws Exception;

	// 销售退库过账
	JSONObject salePost(JSONObject json) throws Exception;
	
	//川维 销售退库保存
	JSONObject saveSaleReturnOrder(JSONObject json) throws Exception;

	// 预留退库过账
	JSONObject reservePost(JSONObject json) throws Exception;

	// 为退库单明细添加批次信息，分新建批次和获取新批次两种情况，适用于三种退库
	void addeBatchMaterialInfoForReturnOrder(BizMaterialDocHead materialDocHead) throws Exception;

	// 向数据库中插入退库单抬头和明细 ，返回自增的return_id
	int insertReturnHeadAndItems(BizStockOutputReturnHead returnHead, List<BizStockOutputReturnItem> returnItems);

	/**
	 * 通过退库单id查询退库单头信息
	 * 
	 * @author 刘宇 2018.04.185cm
	 * @param returnId
	 * @return
	 * @throws Exception
	 */
	BizStockOutputReturnHead getBizStockOutputReturnHeadByReturnId(int returnId) throws Exception;
	
	//川维 交货单详情	
	BizDeliveryOrderHead getDeliveryOrderInfo (Map<String, Object> map) throws Exception;
	
	//川维退库配货
	List<Map<String, Object>> getSaleOutputItemList (Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> selectReturnPositionList(Map<String, Object> map) throws Exception;
	
	Map<String, Object> postOrderToSap(Integer returnId,String userId) throws Exception;
	
	String postOrderToFinish(Map<String, Object> sapResult, int returnId, String userId) throws Exception;
	
	Map<String, Object> writeoffOrderToSap(Integer returnId,String userId) throws Exception;
	
	String writeoffOrderToFinish(Map<String, Object> sapResult, int returnId, String userId) throws Exception;
		
	boolean canUseForDelivery(String condition) throws Exception;
	
	/**
	 * (通用)退库mes同步信息
	 * @param sapReturn
	 * @param userId
	 */
	void saveReturnToMes(Map<String,Object> sapReturn,String userId);
	
	String writeoffReturnToMes(int returnId,String userId);
	
	JSONObject saveAndwriteoffToMes(int returnId,String userId);
	
	/**
	 * 删除退库单
	 * @param returnId
	 * @param userId
	 * @return
	 */
	String deleteReturnOrder(int returnId, String userId)throws Exception;
}
