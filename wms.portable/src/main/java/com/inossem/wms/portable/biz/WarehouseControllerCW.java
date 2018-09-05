package com.inossem.wms.portable.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.bpmn.model.BooleanDataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumFtyType;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumTaskUserType;
import com.inossem.wms.model.rel.RelTaskUserWarehouse;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/task")
public class WarehouseControllerCW {
	private static final Logger logger = LoggerFactory.getLogger(WarehouseControllerCW.class);
	
	@Autowired
	private ITaskService taskServiceImpl;

	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	// 基于仓位 基于托盘 基于包装 基于物料 获取当前登陆人的工厂-库存地点-仓库号 理货员、叉车工、搬运工下拉框 通用方法
	@RequestMapping(value = "/get_base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listLocationForUser(CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		JSONArray locAry = new JSONArray();
		try {						
			locAry = commonService.listLocationForUser(cUser.getUserId(),"");
			for (Object object : locAry) {
				JSONObject locObj = (JSONObject)object;
				int whId = UtilObject.getIntOrZero(locObj.get("wh_id"));
				List<RelTaskUserWarehouse> removerList = new ArrayList<RelTaskUserWarehouse>();
				List<RelTaskUserWarehouse> forkliftWorkerList = new ArrayList<RelTaskUserWarehouse>();
				List<RelTaskUserWarehouse> tallyClerkList = new ArrayList<RelTaskUserWarehouse>();
				Map<String, Object> findMap = new HashMap<String, Object>();
				findMap.put("whId", whId);
				findMap.put("type",EnumTaskUserType.REMOVER.getValue());
				removerList = taskServiceImpl.getWarehouseUserList(findMap);
				findMap.put("type",EnumTaskUserType.FORKLIFT_WORKER.getValue());
				forkliftWorkerList = taskServiceImpl.getWarehouseUserList(findMap);
				findMap.put("type",EnumTaskUserType.TALLY_CLERK.getValue());
				tallyClerkList = taskServiceImpl.getWarehouseUserList(findMap);
				locObj.put("removerList", removerList);
				locObj.put("forkliftWorkerList", forkliftWorkerList);
				locObj.put("tallyClerkList", tallyClerkList);
			}			

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, locAry);
		return obj;

	}
	
	private String getFtyTypeByLocationId(Integer locationId) {
		String isDefaultWarehouse = "";		
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		Integer whId = locationIdMap.get(locationId).getWhId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("whId", whId);
		if (locationIdMap.get(locationId).getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
			isDefaultWarehouse = "(0,1)"; // 生产工厂可移动默认仓位
		} else {
			isDefaultWarehouse = "(0)"; // 销售工厂不可移动默认仓位
		}	
		return isDefaultWarehouse;
	}
	
	// 基于仓位   基于托盘  基于包装   基于物料  根据库存地点 获取存储区 -仓位  通用方法
	@RequestMapping(value = "/position/area_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAreaList(Integer location_id, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONArray body = new JSONArray();
		try {
			body = taskServiceImpl.getAreaListByLocationIdForWarehouse(location_id);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;

	}
	
	//基于仓位  查询源仓位物料明细
	@RequestMapping(value = "/position/get_material_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialListByPosition(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONArray body = new JSONArray();
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("ftyId", json.get("fty_id"));
			param.put("locationId", json.get("location_id"));
			param.put("whId", json.get("wh_id"));			
			param.put("areaId", json.get("area_id"));
			param.put("positionId", json.get("position_id"));
			param.put("isDefault", 0);
			
			result = commonService.getTaskMatListByPosition(param);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
		return obj;

	}
	
	//基于仓位  提交
	@RequestMapping(value = "/position/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveForPosition(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		Map<String, Object> result = null;
		try {
			json.put("cUserId", cUser.getUserId());
			json.put("receiptType", EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue());	
			result = taskServiceImpl.saveTaskDataForPositionPallet(json,true);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
		return obj;

	}
	
	
	//基于托盘   通过托盘号查询物料 
	@RequestMapping(value = "/pallet/get_material_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialListByPallet(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONArray body = new JSONArray();
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("ftyId", json.get("fty_id"));
			param.put("locationId", json.get("location_id"));
			param.put("whId", json.get("wh_id"));		
			param.put("palletCode", json.get("pallet_code"));
			param.put("isDefault", 0);
			
			result = commonService.getTaskMatListByPosition(param);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
		return obj;

	}
	
	//基于托盘  提交  新增托盘
	@RequestMapping(value = "/pallet/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveForPallet(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		Map<String, Object> result = new HashMap<>();
		try {
			json.put("cUserId", cUser.getUserId());
			json.put("receiptType", EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue());			
			result = taskServiceImpl.saveTaskDataForPositionPallet(json,true);
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
		return obj;

	}
	
	
	//基于包装   通过包装物序号/生产批次/ERP批次精确查询物料 
	@RequestMapping(value = "/package/get_material_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialListByPackage(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONArray body = new JSONArray();
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("ftyId", json.getInt("fty_id"));
			param.put("locationId", json.getInt("location_id"));
			param.put("whId", json.getInt("wh_id"));		
			param.put("keyword3", json.getString("keyword"));//包装物序号/生产批次/ERP批次
			param.put("isDefault", 0);		
			param.put("includePackage", 0);
			result = commonService.getTaskMatListByPosition(param);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
		return obj;

	}
	
	
	//基于包装 提交  
	@RequestMapping(value = "/package/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveForPackage(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		Map<String, Object> result = new HashMap<>();
		try {
			json.put("cUserId", cUser.getUserId());
			json.put("receiptType", EnumReceiptType.STOCK_TASK_PCKAGE_CLEANUP.getValue());			
			result = taskServiceImpl.saveTaskDataForPackageMaterial(json,true);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
		return obj;

	}
	
	//基于物料   物料编码/物料描述/生产批次/ERP批次精确查询物料 
		@RequestMapping(value = "/material/get_material_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
		public @ResponseBody JSONObject getMaterialListByMaterial(@RequestBody JSONObject json, CurrentUser cUser) {

			Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
			boolean status = true;
			JSONArray body = new JSONArray();
			List<Map<String, Object>> result = new ArrayList<>();
			try {
				Map<String, Object> param = new HashMap<>();
				param.put("ftyId", json.get("fty_id"));
				param.put("locationId", json.get("location_id"));
				param.put("whId", json.get("wh_id"));		
				param.put("keyword4", json.get("keyword"));//物料编码/物料描述/生产批次/ERP批次
				param.put("includePackage", 0);
				param.put("isDefault", 0);			
				result = commonService.getTaskMatListByPosition(param);

			} catch (WMSException e) {
				errorCode = e.getErrorCode();
				errorString = e.getMessage();
				status = false;
				logger.error("", e);
			} catch (Exception e) {
				errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
				errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
				status = false;
			}

			JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
			return obj;

		}
	
	//基于物料  提交  
	@RequestMapping(value = "/material/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveForMaterial(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		Map<String, Object> result = new HashMap<>();
		try {
			json.put("cUserId", cUser.getUserId());
			json.put("receiptType", EnumReceiptType.STOCK_TASK_MATERIAL_CLEANUP.getValue());			
			result = taskServiceImpl.saveTaskDataForPackageMaterial(json,true);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, result);
		return obj;

	}
	
	// 列表页 手持端只查草稿状态
	@RequestMapping(value = "/get_order_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderList(@RequestBody JSONObject json, CurrentUser cUser) {
		JSONArray bizStockTaskHeadAry = new JSONArray();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		int total = 0;
		try {

			String statusString = json.getString("status").trim();
			if (statusString.length() > 0) {
//				int receiptType = json.getInt("receipt_type");
				String condition = json.getString("condition").trim();
//				pageIndex = json.getInt("page_index");
//				pageSize = json.getInt("page_size");
//				int totalCount = -1;
//				if (json.containsKey("total")) {
//					totalCount = json.getInt("total");
//				}
//				boolean sortAscend = json.getBoolean("sort_ascend");
//				String sortColumn = json.getString("sort_column");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				BizStockTaskHeadVo paramVo = new BizStockTaskHeadVo();

				paramVo.setCondition(condition);
				paramVo.setStatusString("(" + statusString + ")");
				paramVo.setCreateUser(cUser.getUserId());
//				paramVo.setReceiptType((byte)receiptType);
//				paramVo.setPaging(true);
//				paramVo.setPageIndex(pageIndex);
//				paramVo.setPageSize(pageSize);
//				paramVo.setTotalCount(totalCount);
				paramVo.setReceiptTypeString("(" + EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue() + ","
						+ EnumReceiptType.STOCK_TASK_PCKAGE_CLEANUP.getValue() + ","
						+ EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue() + ","
						+ EnumReceiptType.STOCK_TASK_MATERIAL_CLEANUP.getValue() + ")");
//				paramVo.setSortAscend(sortAscend);
//				paramVo.setSortColumn(sortColumn);

				List<BizStockTaskHeadVo> list = taskServiceImpl.getBizStockTaskHeadList(paramVo);
				JSONObject obj;
				if (list != null && list.size() > 0) {
					total = list.get(0).getTotalCount();
					for (BizStockTaskHeadVo vo : list) {
						obj = new JSONObject();
						obj.put("stock_task_id", vo.getStockTaskId());// 作业单号id
						obj.put("stock_task_code", vo.getStockTaskCode());// 作业单号
						obj.put("location_id", vo.getLocationId());// 库存地点
						obj.put("location_code", vo.getLocationCode());// 库存地点
						obj.put("location_name", vo.getLocationName());// 库存地点
						obj.put("receipt_type", vo.getReceiptType());// 业务类型
						obj.put("receipt_type_name", vo.getReceiptTypeName(vo.getReceiptType()));// 业务类型名称
						obj.put("user_name", vo.getUserName());// 操作人
						obj.put("create_time", UtilString.getLongStringForDate(vo.getCreateTime()));// 创建日期
						obj.put("status", vo.getStatus());// 状态
						obj.put("status_name", vo.getStatusName(vo.getStatus()));// 状态描述
						bizStockTaskHeadAry.add(obj);
					}
				}
			}
			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		} catch (Exception e) {
			logger.error("仓库整理列表", e);
		}

		return UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, bizStockTaskHeadAry);

	}

	// 详情页
	@RequestMapping(value = "/get_order_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderInfo(int stock_task_id, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			BizStockTaskHeadVo paramVo = new BizStockTaskHeadVo();
			paramVo.setStockTaskId(stock_task_id);
			List<BizStockTaskHeadVo> list = taskServiceImpl.getBizStockTaskHeadList(paramVo);
			BizStockTaskHeadVo taskHead = list.get(0);
			body.put("stock_task_id", taskHead.getStockTaskId());
			body.put("stock_task_code", taskHead.getStockTaskCode());
			body.put("receipt_type", taskHead.getReceiptType());
			body.put("receipt_type_name", taskHead.getReceiptTypeName());
			body.put("fty_id", taskHead.getFtyId());
			body.put("fty_code", taskHead.getFtyCode());
			body.put("fty_name", taskHead.getFtyName());
			body.put("location_id", taskHead.getLocationId());
			body.put("location_code", taskHead.getLocationCode());
			body.put("location_name", taskHead.getLocationName());
			body.put("wh_id", taskHead.getWhId());
			body.put("wh_code", taskHead.getWhCode());
			body.put("wh_name", taskHead.getWhName());
			body.put("create_user", taskHead.getCreateUser());
			body.put("user_name", taskHead.getUserName());
			body.put("create_time", UtilString.getLongStringForDate(taskHead.getCreateTime()));
			body.put("status", taskHead.getStatus());
			body.put("status_name", taskHead.getStatusName(taskHead.getStatus()));
			body.put("remover", taskHead.getRemover());
			body.put("forklift_worker", taskHead.getForkliftWorker());
			body.put("tally_clerk", taskHead.getTallyClerk());
			body.put("remark", taskHead.getRemark());
			body.put("remover_id", taskHead.getRemoverId());
			body.put("forklift_worker_id", taskHead.getForkliftWorkerId());
			body.put("tally_clerk_id", taskHead.getTallyClerkId());

			BizStockTaskItemCw bizStockTaskItem = new BizStockTaskItemCw();
			bizStockTaskItem.setStockTaskId(stock_task_id);
			List<Map<String, Object>> itemList = taskServiceImpl.getItemsByStockTaskIdForWarehouse(bizStockTaskItem);
			List<Map<String, Object>> returnList = new ArrayList<>();
			if (itemList != null && itemList.size() > 0) {
				for (Map<String, Object> map : itemList) {
					Map<String, Object> innerMap = new HashMap<>();
					innerMap.put("item_id", UtilString.getStringOrEmptyForObject(map.get("item_id")));
					innerMap.put("stock_task_id", UtilString.getStringOrEmptyForObject(map.get("stock_task_id")));
					innerMap.put("stock_task_rid", UtilString.getStringOrEmptyForObject(map.get("stock_task_rid")));
					innerMap.put("status", UtilString.getStringOrEmptyForObject(map.get("status")));
					innerMap.put("mat_id", UtilString.getStringOrEmptyForObject(map.get("mat_id")));
					innerMap.put("mat_code", UtilString.getStringOrEmptyForObject(map.get("mat_code")));
					innerMap.put("mat_name", UtilString.getStringOrEmptyForObject(map.get("mat_name")));
					innerMap.put("unit_id", UtilString.getStringOrEmptyForObject(map.get("unit_id")));
					innerMap.put("unit_code", UtilString.getStringOrEmptyForObject(map.get("unit_code")));
					innerMap.put("unit_name", UtilString.getStringOrEmptyForObject(map.get("unit_name")));
					innerMap.put("batch", UtilString.getStringOrEmptyForObject(map.get("batch")));
					innerMap.put("qty", UtilString.getStringOrEmptyForObject(map.get("qty")));
					innerMap.put("pack_num", UtilString.getStringOrEmptyForObject(map.get("pack_num")));
					innerMap.put("product_batch", UtilString.getStringOrEmptyForObject(map.get("production_batch")));
					innerMap.put("quality_batch", UtilString.getStringOrEmptyForObject(map.get("quality_batch")));
					innerMap.put("erp_batch", UtilString.getStringOrEmptyForObject(map.get("erp_batch")));
					innerMap.put("package_type_id", UtilString.getStringOrEmptyForObject(map.get("package_type_id")));
					innerMap.put("package_type_code",
							UtilString.getStringOrEmptyForObject(map.get("package_type_code")));
					innerMap.put("package_standard_ch",
							UtilString.getStringOrEmptyForObject(map.get("package_standard_ch")));
					innerMap.put("package_type_name",
							UtilString.getStringOrEmptyForObject(map.get("package_type_name")));
					innerMap.put("sn_used",
							UtilString.getStringOrEmptyForObject(map.get("sn_used")));
					innerMap.put("work_model", UtilString.getStringOrEmptyForObject(map.get("work_model")));
					innerMap.put("mat_store_type", UtilString.getStringOrEmptyForObject(map.get("mat_store_type")));
					innerMap.put("remark", UtilString.getStringOrEmptyForObject(map.get("remark")));
					innerMap.put("area_id", UtilString.getStringOrEmptyForObject(map.get("source_area_id")));
					innerMap.put("area_code", UtilString.getStringOrEmptyForObject(map.get("source_area_code")));
					innerMap.put("area_name", UtilString.getStringOrEmptyForObject(map.get("source_area_name")));
					innerMap.put("position_id", UtilString.getStringOrEmptyForObject(map.get("source_position_id")));
					innerMap.put("position_code",
							UtilString.getStringOrEmptyForObject(map.get("source_position_code")));
					innerMap.put("pallet_id", UtilString.getStringOrEmptyForObject(map.get("source_pallet_id")));
					innerMap.put("pallet_code", UtilString.getStringOrEmptyForObject(map.get("source_pallet_code")));
					innerMap.put("pallet_name", UtilString.getStringOrEmptyForObject(map.get("source_pallet_name")));
					innerMap.put("target_area_id", UtilString.getStringOrEmptyForObject(map.get("target_area_id")));
					innerMap.put("target_area_code", UtilString.getStringOrEmptyForObject(map.get("target_area_code")));
					innerMap.put("target_area_name", UtilString.getStringOrEmptyForObject(map.get("target_area_name")));
					innerMap.put("target_position_id",
							UtilString.getStringOrEmptyForObject(map.get("target_position_id")));
					innerMap.put("target_position_code",
							UtilString.getStringOrEmptyForObject(map.get("target_position_code")));
					innerMap.put("target_pallet_id", UtilString.getStringOrEmptyForObject(map.get("pallet_id")));
					innerMap.put("target_pallet_code",
							UtilString.getStringOrEmptyForObject(map.get("target_pallet_code")));
					innerMap.put("target_pallet_name",
							UtilString.getStringOrEmptyForObject(map.get("target_pallet_name")));
					innerMap.put("stock_qty", UtilString.getStringOrEmptyForObject(map.get("stock_qty")));
					innerMap.put("stock_id", UtilString.getStringOrEmptyForObject(map.get("stock_id")));
					innerMap.put("package_code", UtilString.getStringOrEmptyForObject(map.get("package_code")));

					returnList.add(innerMap);
				}
			}

			body.put("target_area_id", returnList.get(0).get("target_area_id"));
			body.put("target_area_code", returnList.get(0).get("target_area_code"));
			body.put("target_area_name", returnList.get(0).get("target_area_name"));
			body.put("target_position_id", returnList.get(0).get("target_position_id"));
			body.put("target_position_code", returnList.get(0).get("target_position_code"));
			body.put("target_pallet_id", returnList.get(0).get("target_pallet_id"));
			body.put("target_pallet_code", returnList.get(0).get("target_pallet_code"));
			body.put("target_pallet_name", returnList.get(0).get("target_pallet_name"));
			
			body.put("item_list", returnList);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;

	}
	
	
	
	//基于托盘/基于包装-校验仓位/托盘
	@RequestMapping(value = "/check_code_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {
	Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
	String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
	boolean status = true;
	JSONObject body = new JSONObject();
	try {
		json.put("requestSource", EnumRequestSource.PDA.getValue());
	
		body = taskServiceImpl.checkCodeTypeByWarehouse(json);
	} catch (WMSException e) {
		errorCode = e.getErrorCode();
		errorString = e.getMessage();
		status = false;
		logger.error("", e);
	} catch (Exception e) {
		errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		status = false;
		logger.error("", e);
	}
	
	JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
	return obj;
	
	}
	
}
