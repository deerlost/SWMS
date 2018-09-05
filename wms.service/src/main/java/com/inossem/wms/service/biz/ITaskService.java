package com.inossem.wms.service.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskHead;
import com.inossem.wms.model.biz.BizStockTaskHeadCw;
import com.inossem.wms.model.biz.BizStockTaskItem;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BizStockTaskReqPosition;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.key.DicWarehousePositionKey;
import com.inossem.wms.model.rel.RelTaskUserWarehouse;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;
import com.inossem.wms.model.vo.BizStockTaskItemVo;
import com.inossem.wms.model.vo.BizStockTaskReqHeadVo;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;
import com.inossem.wms.model.vo.StockPositionExpandVo;
import com.inossem.wms.model.vo.StockPositionVo;
import com.inossem.wms.model.vo.StockSnExpandVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface ITaskService {

	// 仓库管理--仓库整理列表
	List<BizStockTaskHeadVo> getBizStockTaskHeadList(BizStockTaskHeadVo paramVo);

	// 川维
	List<Map<String, Object>> getBizStockTaskHeadListForCW(Map<String, Object> map);

	// 仓库管理--上架作业列表
	List<BizStockTaskReqHeadVo> getBizStockTaskReqHeadList(BizStockTaskReqHeadVo paramVo);

	// 川维
	List<Map<String, Object>> getBizStockTaskReqHeadListForCW(Map<String, Object> map);

	List<DicWarehouseArea> selectByKey(Integer whId);

	DicWarehousePosition selectDicWarehousePositionByKey(DicWarehousePositionKey key);

	String getHavenMaterial(StockPosition record);

	// 作业单基于仓位整理
	// int positionCleanupOld(BizStockTaskHead bizStockTaskHead, int whId, int
	// sourceAreaId,int sourcePositionId,int targetAreaId,int targetPositionId);

	int positionCleanup(BizStockTaskHead bizStockTaskHead, BizStockTaskItem bizStockTaskItem) throws Exception;

	// 作业单基于物料整理
	// int materialCleanupOld(BizStockTaskHead bizStockTaskHead,
	// List<BizStockTaskItem> list);

	int materialCleanupNew(BizStockTaskHead bizStockTaskHead, List<BizStockTaskItem> list) throws Exception;

	List<StockPositionVo> getStockPositionList(int whId, String condition, String defaultArea, int pageIndex,
			int pageSize, int totalCount);

	BizStockTaskHeadCw getBizStockTaskHeadByPrimaryKey(Integer stockTaskId);

	List<BizStockTaskItemVo> getItemsByStockTaskId(BizStockTaskItemCw bizStockTaskItem);

	List<BizStockTaskReqItemVo> getReqItemsByTaskReqId(Integer stockTaskReqId);

	List<BizStockTaskHeadVo> getBizStockTaskHeadListInReq(BizStockTaskHeadVo paramVo);

	HashMap<String, Object> addBizStockTaskReqHead(JSONObject record);

	int shelve(BizStockTaskHead bizStockTaskHead, List<BizStockTaskItem> bizStockTaskItemList);

	List<StockPositionVo> getMaterialInPosition(StockPosition record);

	String getReferReceiptCodeInReq(Integer stockTaskId);// 查上架单对应的入库单

	BizStockTaskReqHead getReqHeadByPrimaryKey(Integer stockTaskReqId);

	JSONObject stockMoveForPosition(List<StockPositionExpandVo> spList) throws Exception;

	JSONObject stockMoveForSn(List<StockSnExpandVo> snList) throws Exception;

	StockPosition selectStockPositionByPrimaryKey(Integer id);

	StockSn queryStockSnByStockId(Integer stockId);

	// 生成作业单
	Map<String, Object> addBizStockTaskHead(BizStockTaskHeadCw stockTaskHead) throws Exception;

	
	// 修改作业单备注
	Map<String, Object> updateBizStockTaskRemark(BizStockTaskHeadCw stockTaskHead) throws Exception;
	
	// 上架校验
	JSONObject checkCodeTypeByShelves(JSONObject json) throws Exception;

	// 上架校验
	JSONObject checkCodeTypeByShelvesNew(JSONObject json) throws Exception;

	// 下架校验
	JSONObject checkCodeTypeByReomove(JSONObject json) throws Exception;

	// 下架校验
	JSONObject checkCodeTypeByReomoveNew(JSONObject json) throws Exception;

	// 下架校验 android
	JSONObject checkCodeTypeByReomovePortable(JSONObject json) throws Exception;

	// 生产转运校验
	JSONObject checkCodeTypeByProductionTransport(JSONObject json) throws Exception;

	// 生产转运校验
	JSONObject checkCodeTypeByProductionTransportNew(JSONObject json) throws Exception;

	JSONArray getAreaListByLocationId(Integer locationId) throws Exception;
	
	// 仓库整理获取存储区仓位
	JSONArray getAreaListByLocationIdForWarehouse(Integer locationId) throws Exception;

	// 下架查仓位 locationId matId batch
	JSONArray getAreaListByRemoval(Map<String, Object> map) throws Exception;

	// 下架建议仓位 locationId matId batch qty
	JSONArray getAdviseAreaListByRemoval(Map<String, Object> map) throws Exception;

	// 上架建议仓位 locationId production_batch
	JSONArray getAdviseAreaListByShelves(Map<String, Object> map) throws Exception;

	List<BizStockTaskReqPosition> getStockTaskReqPositionListByReqItemId(Integer itemId) throws Exception;

	List<BizStockTaskPositionCw> getStockTaskPositionListByReqItemId(BizStockTaskPositionCw record) throws Exception;

	List<BizStockTaskReqItemVo> getStockTaskReqItemByParams(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> getStockPosition(Map<String, Object> map) throws Exception;

	Map<String, Object> addStockTaskByGroup(BizStockTaskHeadCw head) throws Exception;

	Map<String, Object> saveTaskDataForPositionPallet(JSONObject json, boolean isPDA) throws Exception;

	Map<String, Object> saveTaskDataForPackageMaterial(JSONObject json, boolean isPDA) throws Exception;

	List<Map<String, Object>> getItemsByStockTaskIdForWarehouse(BizStockTaskItemCw bizStockTaskItem);

	void addStockTaskReqByTransport(BizStockTaskHeadCw stockTaskHead) throws Exception ;
	
	void addStockTaskHeadByReq(BizStockTaskHeadCw taskHead, BizStockTaskReqHead reqHead) throws Exception;

	void addStockTaskItemByReq(BizStockTaskHeadCw taskHead, BizStockTaskItemCw taskitem, BizStockTaskReqItem reqItem)
			throws Exception;
	
	public void transportByProduct(int stockTaskReqId) throws Exception ;

	List<RelTaskUserWarehouse> getWarehouseUserList(Map<String, Object> map);
	
	// 仓库整理校验
	JSONObject checkCodeTypeByWarehouse(JSONObject json) throws Exception;

	void deleteTaskById(Integer taskId) throws Exception;
	
	void deleteTaskReqAndTaskByReferIdAndType(Integer referReceiptId,Byte referReceiptType) throws Exception;
	
	void updateStockPostionByDate() throws Exception;
}
