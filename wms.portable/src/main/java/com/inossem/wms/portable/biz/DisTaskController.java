package com.inossem.wms.portable.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockTaskHeadCw;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqPosition;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumTaskUserType;
import com.inossem.wms.model.rel.RelTaskUserWarehouse;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;
import com.inossem.wms.model.vo.BizStockTaskItemVo;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IOutPutService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/biz/task")
public class DisTaskController {
	private static final Logger logger = LoggerFactory.getLogger(DisTaskController.class);

	// @Autowired
	// private IInputService rkglService;

	@Autowired
	private ITaskService taskServiceImpl;

	@Autowired
	private ICommonService commonService;

	// @Autowired
	// private DicWarehousePositionMapper positionMapper;

	@Resource
	private IOutPutService outPutService;

	/**
	 * 分页参数
	 * 
	 * @param json
	 * @return
	 */
	private Map<String, Object> getParamMapToPageing(JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();

		int pageIndex = json.getInt("page_index");
		int pageSize = json.getInt("page_size");
		int totalCount = EnumPage.TOTAL_COUNT.getValue();
		if (json.containsKey("total")) {
			totalCount = json.getInt("total");
		}
		boolean sortAscend = json.getBoolean("sort_ascend");
		String sortColumn = json.getString("sort_column");
		param.put("paging", true);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		param.put("sortAscend", sortAscend);
		param.put("sortColumn", sortColumn);

		return param;
	}

	private String getReqStatusName(byte receiptType, byte status) {
		String statusName = "";
		if ((receiptType == EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue()
				|| receiptType == EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue())
				&& status == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
			statusName = "未完成";
		} else if ((receiptType == EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue()
				|| receiptType == EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue())
				&& status == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
			statusName = "已完成";
		} else if (receiptType == EnumReceiptType.STOCK_TASK_LISTING.getValue()
				&& status == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
			statusName = "未记账上架作业";
		} else if (receiptType == EnumReceiptType.STOCK_TASK_LISTING.getValue()
				&& status == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
			statusName = "已记账上架作业";
		} else if (receiptType == EnumReceiptType.STOCK_TASK_REMOVAL.getValue()
				&& status == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
			statusName = "未记账下架作业";
		} else if (receiptType == EnumReceiptType.STOCK_TASK_REMOVAL.getValue()
				&& status == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
			statusName = "已记账下架作业";
		}
		return statusName;
	}

	// 川维 下架作业--列表
	@RequestMapping(value = "/removal/biz_stock_task_req_head_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqHeadList(CurrentUser cUser, String condition) {
		JSONArray headAry = new JSONArray();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		int total = 1;

		List<Map<String, Object>> list = new ArrayList<>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if (StringUtils.hasText(condition)) {
				param.put("condition", condition.trim());// 查询条件
			}

			param.put("currentUser", cUser.getUserId());

			param.put("receiptType", EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
			param.put("status", EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			list = taskServiceImpl.getBizStockTaskReqHeadListForCW(param);
			if (list != null && list.size() > 0) {
				total = UtilObject.getIntOrZero(list.get(0).get("totalCount"));
				for (Map<String, Object> map : list) {
					int referReceiptType = UtilObject.getIntOrZero(map.get("refer_receipt_type"));// 参考单据类型
					map.put("receipt_type", referReceiptType);
					map.put("type_name", EnumReceiptType.getNameByValue((byte) referReceiptType));
					map.put("create_time", UtilString.getLongStringForDate((Date) map.get("create_time")));
					int status = UtilObject.getIntOrZero(map.get("status"));
					map.put("status_name",
							getReqStatusName(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue(), (byte) status));
					headAry.add(map);
				}
			}

			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("上架作业列表", e);
		}

		return UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, headAry);
	}

	// 下架作业-申请单详情
	@RequestMapping(value = "/removal/biz_stock_task_req_details/{stock_task_req_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqDetails(@PathVariable("stock_task_req_id") int stockTaskReqId,
			CurrentUser cUser) {
		JSONArray bizStockTaskReqItemsAry = new JSONArray();// 申请单明细
		JSONArray bizStockTaskHeadAry = new JSONArray();// 作业单抬头
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean returnStatus = false;

		if (StringUtils.hasText(stockTaskReqId + "")) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("stockTaskReqId", stockTaskReqId);
				// 获取作业申请单抬头
				List<Map<String, Object>> reqHeadList = taskServiceImpl.getBizStockTaskReqHeadListForCW(map);

				if (reqHeadList != null && reqHeadList.size() > 0) {
					Map<String, Object> resultMap = reqHeadList.get(0);
					int referReceiptType = UtilObject.getIntOrZero(resultMap.get("refer_receipt_type"));// 参考单据类型
					body.put("receipt_type", referReceiptType);// 单据类型
					body.put("type_name", EnumReceiptType.getNameByValue((byte) referReceiptType));// 单据类型描述
					body.put("create_time", UtilString.getLongStringForDate((Date) resultMap.get("create_time")));
					int status = UtilObject.getIntOrZero(resultMap.get("status"));
					body.put("status_name",
							getReqStatusName(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue(), (byte) status));
					body.put("stock_task_req_id", resultMap.get("stock_task_req_id"));// 作业申请号
					body.put("stock_task_req_code", resultMap.get("stock_task_req_code"));// 作业申请号
					body.put("refer_receipt_code", resultMap.get("refer_receipt_code"));// 单据类型
					body.put("create_user", resultMap.get("create_user"));// 创建人
					body.put("delivery_vehicle", resultMap.get("delivery_vehicle"));// 创建人
					body.put("delivery_driver", resultMap.get("delivery_driver"));// 创建人
					body.put("remark", resultMap.get("remark"));// 创建人
				}
				// 获取作业申请单明细
				List<BizStockTaskReqItemVo> reqItemList = taskServiceImpl.getReqItemsByTaskReqId(stockTaskReqId);
				if (reqItemList != null && reqItemList.size() > 0) {
					for (int i = 0; i < reqItemList.size(); i++) {
						BizStockTaskReqItemVo reqItemVo = reqItemList.get(i);
						JSONObject item = new JSONObject();
						BigDecimal stockTaskQty = reqItemVo.getStockTaskQty();
						BigDecimal unStockTaskQty = reqItemVo.getQty().subtract(stockTaskQty);
						if (unStockTaskQty.compareTo(BigDecimal.ZERO) == 1) {
							item.put("item_id", reqItemVo.getItemId());// 作业单行号
							item.put("stock_task_req_rid", reqItemVo.getStockTaskReqRid());// 作业单行号
							item.put("mat_id", reqItemVo.getMatId());// 物料编码
							item.put("mat_code", reqItemVo.getMatCode());// 物料编码
							item.put("mat_name", reqItemVo.getMatName());// 物料描述
							item.put("mat_store_type", reqItemVo.getMatStoreType());// 启用类型
							item.put("unit_id", reqItemVo.getUnitId());
							item.put("unit_zh", reqItemVo.getNameZh());// 单位
							item.put("location_id", reqItemVo.getLocationId());
							item.put("location_name", reqItemVo.getLocationName());// 库存地点
							item.put("location_code", reqItemVo.getLocationCode());
							item.put("production_batch", reqItemVo.getProductionBatch());// 生产批次
							item.put("erp_batch", reqItemVo.getErpBatch());// 生产批次
							item.put("batch", reqItemVo.getBatch());// 生产批次
							item.put("package_type_id", reqItemVo.getPackageTypeId());//
							item.put("package_type_code", reqItemVo.getPackageTypeCode());// 包装类型
							item.put("package_type_name", reqItemVo.getPackageTypeName());//
							item.put("package_standard", reqItemVo.getPackageStandard());//
							item.put("package_standard_ch", reqItemVo.getPackageStandardCh());//
							item.put("unstock_task_qty", unStockTaskQty);// 未上架数量
							item.put("task_qty", 0);// 本次上架数量默认是0
							// item.put("stock_task_qty", stockTaskQty);// 已上架数量
							item.put("decimal_place", reqItemVo.getDecimalPlace());// 计量单位浮点数
							item.put("remark", reqItemVo.getRemark());// 计量单位浮点数
							List<BizStockTaskReqPosition> positionList = taskServiceImpl
									.getStockTaskReqPositionListByReqItemId(reqItemVo.getItemId());
							if (positionList != null && positionList.size() > 0) {
								BigDecimal havePayload = new BigDecimal(0);
								int packageNum = 0;
								for (BizStockTaskReqPosition p : positionList) {
									if (p.getPalletId() != null && p.getPalletId() > 0) {
										// 有托盘
										havePayload = havePayload.add(p.getQty());
										packageNum++;
										p.setMaxPayload(p.getMaxWeight());
										p.setHavePayload(havePayload);
										p.setPackageNum(packageNum);
									}
								}
								item.put("work_model", "1");
							} else {
								positionList = new ArrayList<BizStockTaskReqPosition>();
								item.put("work_model", "3");
							}
							item.put("pallet_package_list", positionList);

							bizStockTaskReqItemsAry.add(item);
						}
					}
				}
				body.put("item_list", bizStockTaskReqItemsAry);
				body.put("class_type_list", commonService.getClassTypeList());
				body.put("shipping_type_list", commonService.getShippingTypeList());
				Integer classTypeId = commonService.selectNowClassType();
				if (classTypeId == null) {
					classTypeId = 0;
				}
				body.put("classTypeId", classTypeId);
				
				BizStockTaskHeadVo param = new BizStockTaskHeadVo();
				param.setStockTaskReqId(stockTaskReqId);
				List<BizStockTaskHeadVo> innerHeadList = taskServiceImpl.getBizStockTaskHeadListInReq(param);

				// 申请单对应的作业单
				if (innerHeadList != null && innerHeadList.size() > 0) {
					for (int i = 0; i < innerHeadList.size(); i++) {
						JSONObject Obj = new JSONObject();
						BizStockTaskHeadVo innerVo = innerHeadList.get(i);
						Obj.put("stock_task_id", innerVo.getStockTaskId());// 作业单号id
						Obj.put("stock_task_code", innerVo.getStockTaskCode());// 作业单号
						Obj.put("create_time", UtilString.getLongStringForDate(innerVo.getCreateTime()));// 上架日期
						String locationcode = innerVo.getLocationCode();
						String locationname = innerVo.getLocationName();
						Obj.put("location_name", locationcode + "-" + locationname);// 库存地点
						Obj.put("user_name", innerVo.getUserName());// 作业人
						Obj.put("status", innerVo.getStatus());// 作业状态
						Obj.put("status_name", innerVo.getStatusNameForRemoval());// 作业状态：是否出库
						bizStockTaskHeadAry.add(Obj);
					}
				}
				body.put("task_list", bizStockTaskHeadAry);

				List<RelTaskUserWarehouse> removerList = new ArrayList<RelTaskUserWarehouse>();
				List<RelTaskUserWarehouse> forkliftWorkerList = new ArrayList<RelTaskUserWarehouse>();
				List<RelTaskUserWarehouse> tallyClerkList = new ArrayList<RelTaskUserWarehouse>();
				Map<String, Object> findMap = new HashMap<String, Object>();
				findMap.put("whId", reqHeadList.get(0).get("wh_id"));
				findMap.put("type", EnumTaskUserType.REMOVER.getValue());
				removerList = taskServiceImpl.getWarehouseUserList(findMap);
				findMap.put("type", EnumTaskUserType.FORKLIFT_WORKER.getValue());
				forkliftWorkerList = taskServiceImpl.getWarehouseUserList(findMap);
				findMap.put("type", EnumTaskUserType.TALLY_CLERK.getValue());
				tallyClerkList = taskServiceImpl.getWarehouseUserList(findMap);
				body.put("removerList", removerList);
				body.put("forkliftWorkerList", forkliftWorkerList);
				body.put("tallyClerkList", tallyClerkList);

				List<Map<String, Object>> vehicleList = new ArrayList<Map<String,Object>>();
				vehicleList = commonService.selectVehicleByProductId(cUser.getUserId());
				List<Map<String, Object>> driverList = new ArrayList<Map<String,Object>>();
				driverList = commonService.selectDriverByProductlineId(cUser.getUserId());
				body.put("vehicleList", vehicleList);
				body.put("driverList", driverList);
				returnStatus = true;
				error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			} catch (Exception e) {
				logger.error("下架作业明细", e);
			}
		}

		JSONObject json = UtilResult.getResultToJson(returnStatus, error_code, body);
		UtilJSONConvert.setNullToEmpty(json);
		return json;
	}

	// 下架作业-申请单详情
	@RequestMapping(value = {
			"/removal/check_code_type" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			if (json.containsKey("refer_receipt_type")) {
				Byte referReceipt = UtilObject.getByteOrNull(json.get("refer_receipt_type"));
				if (referReceipt.equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
						|| referReceipt.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
					json.put("isUrgent", 1);
				}
			}

			json.put("requestSource", EnumRequestSource.PDA.getValue());
			body = taskServiceImpl.checkCodeTypeByReomoveNew(json);
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

	// 上架作业-1.1.3 根据库存地点获取存储区
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {
			"/removal/stock_position" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getStockPosition(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {

			if (json.containsKey("refer_receipt_type")) {
				Byte referReceipt = UtilObject.getByteOrNull(json.get("refer_receipt_type"));
				if (referReceipt.equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
						|| referReceipt.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
					json.put("is_urgent", 1);
				}
			}

			UtilJSONConvert.convertToHump(json);

			Map<String, Object> map = JSONObject.fromObject(json, new JsonConfig());
			returnList = taskServiceImpl.getStockPosition(map);

			if(returnList!=null&&returnList.size()>0){
				for(Map<String, Object> innerMap:returnList){
					String wh_code = UtilObject.getStringOrEmpty(innerMap.get("wh_code"));
					String area_code = UtilObject.getStringOrEmpty(innerMap.get("area_code"));
					innerMap.put("area_code", wh_code+"-"+area_code);
				}
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

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, returnList);
		return obj;

	}

	/**
	 * 获取建议仓位
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {
			"/removal/advise_area_list" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAdviseAreaList(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONArray body = new JSONArray();
		try {

			if (json.containsKey("refer_receipt_type")) {
				Byte referReceipt = UtilObject.getByteOrNull(json.get("refer_receipt_type"));
				if (referReceipt.equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
						|| referReceipt.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
					json.put("is_urgent", 1);
				}
			}

			UtilJSONConvert.convertToHump(json);

			Map<String, Object> map = JSONObject.fromObject(json, new JsonConfig());
			body = taskServiceImpl.getAdviseAreaListByRemoval(map);

			if(body!=null&&!body.isEmpty()){
				for(int i=0;i<body.size();i++){
					JSONObject innerObj = body.getJSONObject(i);
					String wh_code = UtilObject.getStringOrEmpty(innerObj.get("wh_code"));
					String area_code = UtilObject.getStringOrEmpty(innerObj.get("area_code"));
					innerObj.put("area_code", wh_code+"-"+area_code);
				}
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
			logger.error("", e);
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;

	}

	// 上架作业--上架
	@RequestMapping(value = "/removal/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject submit(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		BizStockTaskHeadCw stockTaskHead = new BizStockTaskHeadCw();
		try {

			stockTaskHead = UtilJSONConvert.fromJsonToHump(json, BizStockTaskHeadCw.class);
			stockTaskHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL.getValue());
			stockTaskHead.setCreateUser(user.getUserId());

			stockTaskHead.setRequestSource(EnumRequestSource.WEB.getValue());
			taskServiceImpl.addBizStockTaskHead(stockTaskHead);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增其他入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

	// /**
	// * 根据工厂、物料编码、库存地点、特殊库存类型、特殊库存编号查询仓位库存信息
	// *
	// * @return
	// */
	// @RequestMapping(value = "/biz/task/removal/add_position_list", method =
	// RequestMethod.POST, produces = "application/json;charset=UTF-8")
	// public @ResponseBody Map<String, Object> getStockData(@RequestBody
	// JSONObject json) {
	// Map<String, Object> param = new HashMap<String, Object>();
	//
	// param.put("matId",
	// UtilObject.getStringOrEmpty(json.getString("mat_id"))); // 物料编码
	// param.put("locationId",
	// UtilObject.getStringOrEmpty(json.getString("location_id"))); // 库存地点
	// param.put("specStock",
	// UtilObject.getStringOrEmpty(json.get("spec_stock"))); // 特殊库存
	//
	// if (!"".equals(UtilObject.getStringOrEmpty(json.get("receipt_type")))) {
	// param.put("receiptType",
	// UtilObject.getStringOrEmpty(json.get("receipt_type")));
	// }
	//
	// List<Map<String, Object>> result = null;
	//
	// if ("".equals(UtilObject.getStringOrEmpty(json.get("out_qty")))) {
	// // 没有数量，则调用查询所有的接口
	// result = outPutService.getStockData(param);
	// } else {
	// param.put("outQty", UtilObject.getStringOrEmpty(json.get("out_qty")));
	// result = outPutService.getStockDataCommend(param);
	// }
	//
	// return UtilResult.getResultToMap(true,
	// EnumErrorCode.ERROR_CODE_SUCESS.getValue(), result);
	// }

	// 下架单详情

	// 川维 下架作业--列表
	@RequestMapping(value = "/removal/biz_stock_task_head_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getInnerTaskList(CurrentUser cUser, String condition) {
		JSONArray headAry = new JSONArray();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		int total = 1;

		List<Map<String, Object>> list = new ArrayList<>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			if (StringUtils.hasText(condition)) {
				param.put("condition", condition.trim());// 查询条件
			}

			param.put("currentUser", cUser.getUserId());

			param.put("receiptType","(" + EnumReceiptType.STOCK_TASK_REMOVAL.getValue()+")");

			list = taskServiceImpl.getBizStockTaskHeadListForCW(param);
			if (list != null && list.size() > 0) {
				total = UtilObject.getIntOrZero(list.get(0).get("totalCount"));
				for (Map<String, Object> map : list) {
					int referReceiptType = UtilObject.getIntOrZero(map.get("refer_receipt_type"));// 参考单据类型
					map.put("receipt_type", referReceiptType);
					map.put("type_name", EnumReceiptType.getNameByValue((byte) referReceiptType));
					map.put("create_time", UtilString.getLongStringForDate((Date) map.get("create_time")));
					int status = UtilObject.getIntOrZero(map.get("status"));
					map.put("status_name",
							getReqStatusName(EnumReceiptType.STOCK_TASK_REMOVAL.getValue(), (byte) status));
					headAry.add(map);
				}
			}

			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("上架作业列表", e);
		}

		return UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, headAry);
	}

	@RequestMapping(value = "/removal/get_inner_task_details/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getInnerTaskDetails(@PathVariable("stock_task_id") int stockTaskId,
			CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean returnStatus = false;
		// List<BizStockTaskHeadVo> headList = new
		// ArrayList<BizStockTaskHeadVo>();
		List<BizStockTaskItemVo> itemList = new ArrayList<BizStockTaskItemVo>();
		JSONObject body = new JSONObject();
		JSONArray bizStockTaskItemAry = new JSONArray();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			if (stockTaskId > 0) {
				BizStockTaskHeadCw bizStockTaskHead = taskServiceImpl.getBizStockTaskHeadByPrimaryKey(stockTaskId);

				Map<String, Object> map = new HashMap<>();
				map.put("stockTaskId", bizStockTaskHead.getStockTaskId());
				map.put("currentUser", cUser.getUserId());
				// 获取作业单抬头
				list = taskServiceImpl.getBizStockTaskHeadListForCW(map);
				if (list != null && list.size() > 0) {
					Map<String, Object> resultMap = list.get(0);
					int referReceiptType = UtilObject.getIntOrZero(resultMap.get("refer_receipt_type"));// 参考单据类型
					body.put("receipt_type", referReceiptType);
					body.put("type_name", EnumReceiptType.getNameByValue((byte) referReceiptType));
					body.put("create_time", UtilString.getLongStringForDate((Date)resultMap.get("create_time")));
					body.put("class_type_id", resultMap.get("class_type_id"));
					body.put("class_type_name", resultMap.get("class_type_name"));
					int status = UtilObject.getIntOrZero(resultMap.get("status"));
					body.put("status_name",
							getReqStatusName(EnumReceiptType.STOCK_TASK_REMOVAL.getValue(), (byte) status));
					body.put("status", status);
					body.put("stock_task_req_id", resultMap.get("stock_task_req_id"));
					body.put("stock_task_req_code", resultMap.get("stock_task_req_code"));
					body.put("stock_task_id", resultMap.get("stock_task_id"));
					body.put("stock_task_code", resultMap.get("stock_task_code"));
					body.put("create_user", resultMap.get("create_user"));
					body.put("remover", resultMap.get("remover"));
					body.put("forklift_worker", resultMap.get("forklift_worker"));
					body.put("tally_clerk", resultMap.get("tally_clerk"));
					body.put("refer_receipt_code", resultMap.get("refer_receipt_code"));
					body.put("remark", resultMap.get("remark"));
					// List<BizReceiptAttachmentVo> fileList =
					// fileService.getReceiptAttachments(body.getInt("stock_task_id"),
					// UtilObject.getByteOrNull(resultMap.get("receipt_type")));
					// body.put("fileList", fileList);
				}
				BizStockTaskItemCw bizStockTaskItem = new BizStockTaskItemCw();
				bizStockTaskItem.setStockTaskId(stockTaskId);
				itemList = taskServiceImpl.getItemsByStockTaskId(bizStockTaskItem);

				if (list != null && list.size() > 0) {
					for (BizStockTaskItemVo itemVo : itemList) {
						JSONObject item = new JSONObject();
						item.put("item_id", itemVo.getItemId());// 作业单
						item.put("stock_task_id", itemVo.getStockTaskId());// 作业单
						item.put("stock_task_rid", itemVo.getStockTaskRid());// 作业单行号
						item.put("mat_code", itemVo.getMatCode());// 物料编码
						item.put("mat_name", itemVo.getMatName());// 物料描述
						item.put("unit_zh", itemVo.getNameZh());// 单位
						item.put("location_id", itemVo.getLocationId());
						item.put("location_code", itemVo.getLocationCode());
						item.put("location_name", itemVo.getLocationName());// 库存地点
						item.put("qty", itemVo.getQty());// 本次上架数量
						item.put("unstock_task_qty", itemVo.getUnstockTaskQty());// 本次上架数量
						item.put("production_batch", itemVo.getProductionBatch());// 生产批次
						item.put("package_type_id", itemVo.getPackageTypeId());// 包装类型
						item.put("package_type_code", itemVo.getPackageTypeCode());
						item.put("package_type_name", itemVo.getPackageTypeName());
						item.put("package_standard", itemVo.getPackageStandard());//
						item.put("work_model", itemVo.getWorkModel());
						item.put("remark", itemVo.getRemark());
						item.put("unstock_task_qty", 0);// 未上架数量
						BizStockTaskPositionCw record = new BizStockTaskPositionCw();
						record.setStockTaskId(itemVo.getStockTaskId());
						record.setStockTaskRid(itemVo.getStockTaskRid());
						List<BizStockTaskPositionCw> positionList = taskServiceImpl
								.getStockTaskPositionListByReqItemId(record);
						if (positionList != null && positionList.size() > 0) {
							BigDecimal havePayload = new BigDecimal(0);
							int packageNum = 0;
							for (BizStockTaskPositionCw p : positionList) {
								if (p.getPalletId() != null && p.getPalletId() > 0) {
									// 有托盘
									havePayload = havePayload.add(p.getQty());
									packageNum++;
									p.setMaxPayload(p.getMaxWeight());
									p.setHavePayload(havePayload);
									p.setPackageNum(packageNum);
								}
								p.setAreaId(p.getSourceAreaId());
								p.setAreaCode(p.getSourceAreaCode());
								p.setPositionId(p.getSourcePositionId());
								p.setPositionCode(p.getSourcePositionCode());

							}
						} else {
							positionList = new ArrayList<BizStockTaskPositionCw>();
						}
						item.put("pallet_package_list", positionList);

						bizStockTaskItemAry.add(item);
					}
					body.put("item_list", bizStockTaskItemAry);
				}
			}
			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("", e);
		}
		JSONObject obj = UtilResult.getResultToJson(returnStatus, error_code, body);
		UtilJSONConvert.setNullToEmpty(obj);
		return obj;
	}

	@RequestMapping(value = "/removal/delete/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject delete(@PathVariable("stock_task_id") int stock_task_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean returnStatus = true;
		JSONObject body = new JSONObject();
		try {
			taskServiceImpl.deleteTaskById(stock_task_id);

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			error_string = e.getMessage();
			returnStatus = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增其他入库", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			returnStatus = false;
		}
		JSONObject json = UtilResult.getResultToJson(returnStatus, error_code, error_string, body);
		UtilJSONConvert.setNullToEmpty(json);
		return json;
	}

}
