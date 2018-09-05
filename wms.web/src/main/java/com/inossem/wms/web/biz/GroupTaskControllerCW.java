package com.inossem.wms.web.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumTaskUserType;
import com.inossem.wms.model.rel.RelTaskUserWarehouse;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.BizStockTaskItemVo;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class GroupTaskControllerCW {

	private static final Logger logger = LoggerFactory.getLogger(GroupTaskControllerCW.class);

	@Autowired
	private ICommonService commonService;

	@Autowired
	private ITaskService taskServiceImpl;

	@Autowired
	private IFileService fileService;

	/**
	 * 库存地点list
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {
			"/biz/group_task/base_info" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getBaseInfo(CurrentUser user) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<Map<String, Object>> classTypeList = new ArrayList<Map<String, Object>>();
		JSONArray locationList = new JSONArray();
		Integer classTypeId = 0;
		List<RelTaskUserWarehouse> removerList = new ArrayList<RelTaskUserWarehouse>();
		List<RelTaskUserWarehouse> forkliftWorkerList = new ArrayList<RelTaskUserWarehouse>();
		List<RelTaskUserWarehouse> tallyClerkList = new ArrayList<RelTaskUserWarehouse>();

		try {
			locationList = commonService.listLocationForUser(user.getUserId(), "2");
			classTypeList = commonService.getClassTypeList();
			classTypeId = commonService.selectNowClassType();
			if (classTypeId == null) {
				classTypeId = 0;
			}
			Map<Integer, Map<String, Object>> workerMap = new HashMap<Integer, Map<String, Object>>();
			if (locationList != null) {
				for (int i = 0; i < locationList.size(); i++) {
					JSONObject lobj = locationList.getJSONObject(i);
					Integer whId = lobj.getInt("wh_id");
					if (workerMap.containsKey(whId)) {
						Map<String, Object> work = workerMap.get(whId);
						removerList = (List<RelTaskUserWarehouse>) work.get("removerList");
						forkliftWorkerList = (List<RelTaskUserWarehouse>) work.get("forkliftWorkerList");
						tallyClerkList = (List<RelTaskUserWarehouse>) work.get("tallyClerkList");

					} else {
						Map<String, Object> findMap = new HashMap<String, Object>();
						findMap.put("whId", whId);
						findMap.put("type", EnumTaskUserType.REMOVER.getValue());
						removerList = taskServiceImpl.getWarehouseUserList(findMap);
						findMap.put("type", EnumTaskUserType.FORKLIFT_WORKER.getValue());
						forkliftWorkerList = taskServiceImpl.getWarehouseUserList(findMap);
						findMap.put("type", EnumTaskUserType.TALLY_CLERK.getValue());
						tallyClerkList = taskServiceImpl.getWarehouseUserList(findMap);
						Map<String, Object> work = new HashMap<String, Object>();
						work.put("removerList", removerList);
						work.put("forkliftWorkerList", forkliftWorkerList);
						work.put("tallyClerkList", tallyClerkList);
						workerMap.put(whId, work);
					}

					lobj.put("removerList", removerList);
					lobj.put("forkliftWorkerList", removerList);
					lobj.put("tallyClerkList", removerList);
				}
			}

		} catch (Exception e) {
			logger.error("库存地点list", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		JSONObject body = new JSONObject();
		body.put("locationList", locationList);
		body.put("classTypeList", classTypeList);
		body.put("classTypeId", classTypeId);
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
		return obj;
	}

	// 上架作业-1.1.3 根据库存地点获取存储区
	@RequestMapping(value = {
			"/biz/group_task/area_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAreaList(Integer location_id, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONArray body = new JSONArray();
		try {
			body = taskServiceImpl.getAreaListByLocationId(location_id);

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

	// 组盘 上架作业--列表

	@RequestMapping(value = "/biz/group_task/head_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getHeadList(CurrentUser cUser, @RequestBody JSONObject json) {
		JSONArray headAry = new JSONArray();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		int total = 1;

		List<Map<String, Object>> list = new ArrayList<>();
		try {

			Map<String, Object> param = this.getParamMapToPageing(json);

			param.put("condition", json.getString("condition").trim());// 查询条件
			param.put("currentUser", cUser.getUserId());
			param.put("receiptType", "(" + EnumReceiptType.STOCK_TASK_GROUP.getValue() + ")");

			list = taskServiceImpl.getBizStockTaskHeadListForCW(param);
			if (list != null && list.size() > 0) {
				UtilObject.getIntOrZero(list.get(0).get("totalCount"));
				for (Map<String, Object> map : list) {
					int referReceiptType = UtilObject.getIntOrZero(map.get("refer_receipt_type"));// 参考单据类型
					map.put("receipt_type", referReceiptType);
					map.put("type_name", EnumReceiptType.getNameByValue((byte) referReceiptType));
					map.put("create_time", UtilString.getLongStringForDate((Date) map.get("create_time")));
					map.put("status", UtilObject.getByteOrNull(map.get("status")));
					map.put("status_name",
							EnumReceiptStatus.getNameByValue(UtilObject.getByteOrNull(map.get("status"))));
					headAry.add(map);
				}
			}

			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("上架作业列表", e);
		}

		JSONObject obj = UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, headAry);
		UtilJSONConvert.setNullToEmpty(obj);
		return obj;
	}

	// 上架单详情

	@RequestMapping(value = "/biz/group_task/details/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDetails(@PathVariable("stock_task_id") int stockTaskId, CurrentUser cUser) {
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
					body.putAll(resultMap);
					body.put("create_time", UtilString.getLongStringForDate((Date) resultMap.get("create_time")));
					body.put("status_name",
							EnumReceiptStatus.getNameByValue(UtilObject.getByteOrNull(resultMap.get("status"))));
					body.put("create_user", resultMap.get("create_user"));

					body.put("remover", resultMap.get("remover"));
					body.put("forklift_worker", resultMap.get("forklift_worker"));
					body.put("tally_clerk", resultMap.get("tally_clerk"));
					body.put("remark", resultMap.get("remark"));
					List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(
							body.getInt("stock_task_id"), UtilObject.getByteOrNull(resultMap.get("receipt_type")));
					body.put("fileList", fileList);

				}
				BizStockTaskItemCw bizStockTaskItem = new BizStockTaskItemCw();
				bizStockTaskItem.setStockTaskId(stockTaskId);
				itemList = taskServiceImpl.getItemsByStockTaskId(bizStockTaskItem);

				if (list != null && list.size() > 0) {
					for (BizStockTaskItemVo itemVo : itemList) {
						JSONObject item = new JSONObject();
						item.put("stock_task_id", itemVo.getStockTaskId());// 作业单
						item.put("stock_task_rid", itemVo.getStockTaskRid());// 作业单行号
						item.put("stock_task_req_id", itemVo.getStockTaskReqId());// 作业单
						item.put("stock_task_req_rid", itemVo.getStockTaskReqRid());// 作业单行号
						item.put("stock_task_req_code", itemVo.getStockTaskReqCode());// 作业单行号
						item.put("mat_code", itemVo.getMatCode());// 物料编码
						item.put("mat_id", itemVo.getMatId());// 物料编码
						item.put("mat_name", itemVo.getMatName());// 物料描述
						item.put("name_zh", itemVo.getNameZh());// 单位
						item.put("location_id", itemVo.getLocationId());
						item.put("location_code", itemVo.getLocationCode());
						item.put("location_name", itemVo.getLocationName());// 库存地点
						item.put("qty", itemVo.getQty());// 本次上架数量
						item.put("un_stock_task_qty", itemVo.getUnstockTaskQty());// 本次上架数量
						item.put("production_batch", itemVo.getProductionBatch());// 生产批次
						item.put("erp_batch", itemVo.getErpBatch());// 生产批次
						item.put("package_type_id", itemVo.getPackageTypeId());// 包装类型
						item.put("package_type_code", itemVo.getPackageTypeCode());
						item.put("package_type_name", itemVo.getPackageTypeName());
						item.put("package_standard", itemVo.getPackageStandard());//
						item.put("mat_store_type", itemVo.getMatStoreType());
						item.put("work_model", itemVo.getWorkModel());
						item.put("remark", itemVo.getRemark());// 计量单位浮点数
						item.put("refer_receipt_code", itemVo.getReferReceiptCode());
						item.put("refer_receipt_id", itemVo.getReferReceiptId());
						item.put("refer_receipt_type", itemVo.getReferReceiptType());
						item.put("refer_receipt_type_name",
								EnumReceiptType.getNameByValue(itemVo.getReferReceiptType()));
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
								p.setAreaId(p.getTargetAreaId());
								p.setAreaCode(p.getTargetAreaCode());
								p.setPositionId(p.getTargetPositionId());
								p.setPositionCode(p.getTargetPositionCode());

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
		JSONObject json = UtilResult.getResultToJson(returnStatus, error_code, body);
		UtilJSONConvert.setNullToEmpty(json);
		return json;
	}

	// 上架作业-校验code类型
	@RequestMapping(value = "/biz/group_task/check_code_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			body = taskServiceImpl.checkCodeTypeByShelvesNew(json);
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

	// 未完成上架请求列表-申请单详情
	@RequestMapping(value = "/biz/group_task/biz_stock_task_req_item_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getStockTaskReqItemList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		int total = 1;

		List<BizStockTaskReqItemVo> list = new ArrayList<BizStockTaskReqItemVo>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();

			param.put("matCondition", json.getString("mat_condition").trim());// 查询条件
			param.put("locationId", json.getInt("location_id"));
			param.put("productionBatch", json.getString("production_batch").trim());
			//param.put("referReceiptCode", json.getString("refer_receipt_code").trim());
			//param.put("stockTaskReqCode", json.getString("refer_receipt_code").trim());
			param.put("code", json.getString("refer_receipt_code").trim());
			param.put("receiptType", EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
			if(json.containsKey("refer_receipt_type")){
				param.put("referReceiptType", json.getInt("refer_receipt_type"));
			}
			
			
			ArrayList<Byte> referReceiptTypeList = new ArrayList<Byte>();
			referReceiptTypeList.add(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue());
			referReceiptTypeList.add(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue());
			param.put("referReceiptTypeList", referReceiptTypeList);
			param.put("status", EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			// 未完成上架请求行项目
			list = taskServiceImpl.getStockTaskReqItemByParams(param);

			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("上架作业列表", e);
		}
		JSONObject obj = UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, list);
		UtilJSONConvert.setNullToEmpty(obj);
		return obj;

	}

	// 上架作业--上架
	@RequestMapping(value = "/biz/group_task/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject submit(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		BizStockTaskHeadCw stockTaskHead = new BizStockTaskHeadCw();
		try {

			stockTaskHead = UtilJSONConvert.fromJsonToHump(json, BizStockTaskHeadCw.class);
			stockTaskHead.setReceiptType(EnumReceiptType.STOCK_TASK_GROUP.getValue());
			stockTaskHead.setCreateUser(user.getUserId());

			stockTaskHead.setRequestSource(EnumRequestSource.WEB.getValue());
			taskServiceImpl.addStockTaskByGroup(stockTaskHead);

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
		body.put("stockTaskId", UtilString.getStringOrEmptyForObject(stockTaskHead.getStockTaskId()));
		body.put("stockTaskCode", UtilString.getStringOrEmptyForObject(stockTaskHead.getStockTaskCode()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

	@RequestMapping(value = "/biz/group_task/delete/{stock_task_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
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
