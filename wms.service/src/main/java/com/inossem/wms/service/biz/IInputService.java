package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputPackageItem;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.stock.StockPosition;

public interface IInputService {

	/**
	 * 获取入库单号
	 * 
	 * @return
	 * @throws Exception
	 */
	Long getStockInputCode() throws Exception;

	/**
	 * 获取采购订单列表
	 * 
	 * @param map
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<BizPurchaseOrderHead> getContractList(Map<String, Object> map, CurrentUser user) throws Exception;

	/**
	 * 获取采购订单详情
	 * 
	 * @param purchaseOrderCode
	 * @param user
	 * @return
	 * @throws Exception
	 */
	BizPurchaseOrderHead getContractInfo(String purchaseOrderCode, CurrentUser user) throws Exception;

	Map<String, Object> exemptInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user) throws Exception;

	Map<String, Object> exemptInputPosting(BizStockInputHead stockInputHead) throws Exception;

	Map<String, Object> exemptInputUpdateStock(BizMaterialDocHead mDocHead) throws Exception;

	Map<String, Object> platformInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user) throws Exception;

	Map<String, Object> platformInputPosting(BizStockInputHead stockInputHead) throws Exception;

	Map<String, Object> platformInputUpdateStock(BizMaterialDocHead mDocHead) throws Exception;

	Map<String, Object> otherInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user) throws Exception;

	Map<String, Object> otherInputPosting(BizStockInputHead stockInputHead) throws Exception;

	Map<String, Object> otherInputUpdateStock(BizMaterialDocHead mDocHead) throws Exception;

	Map<String, Object> allocateInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user) throws Exception;

	Map<String, Object> allocateInputPosting(BizStockInputHead stockInputHead) throws Exception;

	Map<String, Object> allocateInputUpdateStock(BizMaterialDocHead mDocHead, BizStockInputHead stockInputHead)
			throws Exception;

	/**
	 * 校验库存地点是否被冻结
	 * 
	 * @param stockInputHead
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> checkLocation(BizStockInputHead stockInputHead) throws Exception;

	/**
	 * 去除入库数量为空 或者为0的 项目
	 * 
	 * @return
	 * @throws Exception
	 */
	List<BizStockInputItem> trimZeroOrNullQtyObj(List<BizStockInputItem> stockInputItemList) throws Exception;

	/**
	 * 生成上架请求抬头
	 * 
	 * @param materialDocHead
	 * @throws Exception
	 */
	void addStockTaskReqHeadAndItem(BizMaterialDocHead materialDocHead) throws Exception;

	/**
	 * 生成上架请求行项目
	 * 
	 * @param headMap
	 * @param materialDocItem
	 * @param reqItem
	 * @throws Exception
	 */
	void addStockTaskReqItemFromMaterialDocItem(Map<String, Object> headMap, BizMaterialDocItem materialDocItem,
			BizStockTaskReqItem reqItem) throws Exception;

	/**
	 * 添加仓位库存
	 * 
	 * @param materialDocHead
	 * @throws Exception
	 */
	void addStockPosition(BizMaterialDocHead materialDocHead) throws Exception;

	/**
	 * 添加批次信息
	 * 
	 * @param materialDocHead
	 * @throws Exception
	 */
	void addBatchMaterialInfo(BizMaterialDocHead materialDocHead) throws Exception;

	/**
	 * 刪除入库单数据
	 * 
	 * @param stockInputHead
	 * @throws Exception
	 */
	void deleteRkInfo(BizStockInputHead stockInputHead) throws Exception;

	/**
	 * 查询物料
	 * 
	 * @param material
	 * @return
	 * @throws Exception
	 */
	List<DicMaterial> getMaterialList(DicMaterial material) throws Exception;

	/**
	 * 查询调拨单行项目 创建 调拨入库
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getAllotItemsOnCreateDBRK(Map<String, Object> map) throws Exception;

	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getDBRKItemByAllotIdAndDeliveryId(Map<String, Object> map) throws Exception;

	String getReceiptTypeNameByInPutCode(String stockInputCode);// 根据入库单code获得入库类型

	public void updateAllotItemAndDeliveryItem(BizStockInputHead stockInputHead, byte status, Integer allotId,
			Integer deliveryId) throws Exception;

	Map<String, Object> getAllocateItem(CurrentUser cuser, Integer allocate_id, Integer allocate_delivery_id)
			throws Exception;

	/**
	 * 查询入库抬头列表
	 * 
	 * @param map
	 * @return
	 */
	List<BizStockInputHead> getInputHeadList(Map<String, Object> map) throws Exception;

	/**
	 * 查询调拨入库抬头列表
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getAllocateInputHeadList(Map<String, Object> map) throws Exception;

	List<BizStockInputItem> getInputItemListByID(Integer stockInputId) throws Exception;

	List<Map<String, Object>> getInputAllocateItemById(Integer stockInputId) throws Exception;

	Map<String, Object> writeOff(BizStockInputHead stockInputHead, CurrentUser user) throws Exception;

	/**
	 * 更改入库单行项目物料凭证id和行项目rid
	 * 
	 * @param itemList
	 * @throws Exception
	 */
	void updateInputItemMatDocInfo(List<BizMaterialDocItem> mDocItemList) throws Exception;

	Map<String, Object> deleteUnFinishedInputInfo(Integer stockInputId) throws Exception;

	List<StockPosition> getStockPositionByItemId(Integer itemId) throws Exception;

	void saveOperatorList(BizStockInputHead stockInputhead) throws Exception;

	void saveFileList(BizStockInputHead stockInputhead) throws Exception;

	Map<String, Object> initInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception;

	Map<String, Object> addInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception;

	Map<String, Object> updateStock(BizMaterialDocHead mDocHead, BizStockInputHead stockInputHead) throws Exception;

	/**
	 * 根据入库单号查询入库单
	 * 
	 * @author 刘宇 2018.04.18
	 * @param stockInputId
	 * @return
	 * @throws Exception
	 */
	BizStockInputHead getBizStockInputHeadByStockInputId(int stockInputId) throws Exception;

	//---------------------川维接口---------------------------
	/**
	 * 新增其他入库
	 * @param stockInputHead
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addOtherInput(BizStockInputHead stockInputHead) throws Exception;

	
	/**
	 * 生成上架请求
	 * 
	 * @param materialDocHead
	 * @throws Exception
	 */
	void addStockTaskReqHeadAndItem(BizStockInputHead stockInputHead) throws Exception;
	
	/**
	 * 生成上架请求行项目
	 * @param headMap
	 * @param stockInputHead
	 * @param stockInputItem
	 * @param packageItem
	 * @param reqItem
	 * @throws Exception
	 */
	void addStockTaskReqItem(Map<String, Object> headMap,BizStockInputHead stockInputHead, BizStockInputItem stockInputItem,BizStockInputPackageItem packageItem,
			BizStockTaskReqItem reqItem) throws Exception;
	
	
	/**
	 * 添加仓位库存
	 * @param stockInputHead
	 * @throws Exception
	 */
	void addStockPosition(BizStockInputHead stockInputHead) throws Exception;
	
	
	/**
	 * 添加批次信息
	 * @param stockInputHead
	 * @throws Exception
	 */
	void addBatchMaterialInfo(BizStockInputHead stockInputHead) throws Exception;
	
	
	/**
	 * 其他入库过账
	 * @param stockInputId
	 * @param userId 
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> postOtherInput(Integer stockInputId,String userId) throws Exception;
	
	/**
	 * 更新批次信息
	 * @param stockInputHead
	 * @throws Exception
	 */
	public void modifyBatchMaterialInfo(BizStockInputHead stockInputHead) throws Exception;
	
	/**
	 * matId
	 * locationId
	 * batch
	 * productionBatch
	 * erpBatch
	 */
	List<Map<String, Object>> getStockLocationByFiveKeys() throws Exception;
	
}
