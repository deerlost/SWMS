package com.inossem.wms.portable.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.inossem.wms.exception.InventoryException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.FreezeCheck;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockTaskHead;
import com.inossem.wms.model.biz.BizStockTaskHeadCw;
import com.inossem.wms.model.biz.BizStockTaskItem;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.key.DicWarehousePositionKey;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;
import com.inossem.wms.model.vo.BizStockTaskItemVo;
import com.inossem.wms.model.vo.BizStockTaskReqHeadVo;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;
import com.inossem.wms.model.vo.StockPositionVo;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/task/22")
public class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private IInputService rkglService;

	@Autowired
	private ITaskService taskServiceImpl;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IDictionaryService dictionaryService;
	
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
		if ((receiptType==EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue() ||
				receiptType==EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue())
				&& status == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
			statusName = "未完成";
		}else if ((receiptType==EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue() ||
				receiptType==EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue())
				&& status == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
			statusName = "已完成";
		}else if (receiptType==EnumReceiptType.STOCK_TASK_LISTING.getValue()
				&& status == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
			statusName = "未入库";
		}else if (receiptType==EnumReceiptType.STOCK_TASK_LISTING.getValue()
				&& status == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
			statusName = "已入库";
		}else if (receiptType==EnumReceiptType.STOCK_TASK_REMOVAL.getValue()
				&& status == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
			statusName = "未出库";
		}else if (receiptType==EnumReceiptType.STOCK_TASK_REMOVAL.getValue()
				&& status == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
			statusName = "已出库";
		}
		return statusName;
	}


	// 仓库整理列表
//	@RequestMapping(value = "/warehouse/biz_stock_task_head_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject bizStockTaskHeadList(CurrentUser cUser, @RequestBody JSONObject json) {
//		JSONArray bizStockTaskHeadAry = new JSONArray();
//		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
//		int pageIndex = EnumPage.PAGE_INDEX.getValue();
//		int pageSize = EnumPage.PAGE_SIZE.getValue();
//		boolean returnStatus = false;
//		int total = 0;
//		try {
//
//			String condition = json.getString("condition").trim();
//			pageIndex = json.getInt("page_index");
//			pageSize = json.getInt("page_size");
//			int totalCount = -1;
//			if (json.containsKey("total")) {
//				totalCount = json.getInt("total");
//			}
//			boolean sortAscend = json.getBoolean("sort_ascend");
//			String sortColumn = json.getString("sort_column");
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			BizStockTaskHeadVo paramVo = new BizStockTaskHeadVo();
//
//			paramVo.setCondition(condition);
//			paramVo.setShippingType("X");
//			paramVo.setCreateUser(cUser.getUserId());
//			paramVo.setPaging(true);
//			paramVo.setPageIndex(pageIndex);
//			paramVo.setPageSize(pageSize);
//			paramVo.setTotalCount(totalCount);
//			paramVo.setSortAscend(sortAscend);
//			paramVo.setSortColumn(sortColumn);
//
//			List<BizStockTaskHeadVo> list = taskServiceImpl.getBizStockTaskHeadList(paramVo);
//			JSONObject obj;
//			if (list != null && list.size() > 0) {
//				total = list.get(0).getTotalCount();
//				for (BizStockTaskHeadVo vo : list) {
//					obj = new JSONObject();
//					obj.put("stock_task_id", vo.getStockTaskId());// 作业单号id
//					obj.put("stock_task_code", vo.getStockTaskCode());// 作业单号
//					obj.put("location_name", vo.getLocationName());// 库存地点
//					obj.put("receipt_type", vo.getReceiptType());// 业务类型
//					obj.put("receipt_type_name", vo.getReceiptTypeName());// 业务类型名称
//					obj.put("user_name", vo.getUserName());// 操作人
//					obj.put("create_time", sdf.format(vo.getCreateTime()));// 创建日期
//
//					obj.put("wh_code", UtilString.getStringOrEmptyForObject(vo.getWhCode()));// 仓库code
//					obj.put("wh_name", UtilString.getStringOrEmptyForObject(vo.getWhName()));// 仓库名称
//					obj.put("fty_code", vo.getFtyCode());// 工厂code
//					obj.put("fty_name", vo.getFtyName());// 工厂名称
//					bizStockTaskHeadAry.add(obj);
//				}
//			}
//			returnStatus = true;
//			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//		} catch (Exception e) {
//			logger.error("仓库整理列表", e);
//		}
//
//		return UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, bizStockTaskHeadAry);
//	}

	// 上架作业--列表

	@RequestMapping(value = "/shelves/biz_stock_task_req_head_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqHeadList(CurrentUser cUser, @RequestBody JSONObject json) {
		JSONArray headAry = new JSONArray();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		int total = 1;
		
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			
			Map<String, Object> param = this.getParamMapToPageing(json);
			
			param.put("condition", json.getString("condition").trim());//查询条件
			param.put("currentUser", cUser.getUserId());
			int chooseType = json.getInt("choose_type");//1未完成上架请求 2已完成上架请求 3未入库上架作业 4已入库上架作业
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (chooseType ==1 || chooseType==2) {								
				param.put("receiptType", EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
				if (chooseType ==1) {
					param.put("status", EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				}else {
					param.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
				}
				list = taskServiceImpl.getBizStockTaskReqHeadListForCW(param);
				if (list!=null && list.size()>0) {
					total = Integer.parseInt(UtilString.getStringOrEmptyForObject(list.get(0).get("totalCount")));
					for (Map<String, Object> map : list) {
						int referReceiptType = UtilObject.getIntOrZero(map.get("refer_receipt_type"));//参考单据类型
						map.put("receipt_type", referReceiptType);
						map.put("type_name", EnumReceiptType.getNameByValue((byte)referReceiptType));
						map.put("create_time", sdf.format(map.get("create_time")));
						int status = UtilObject.getIntOrZero(map.get("status"));
						map.put("status_name", getReqStatusName(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue(),(byte)status));
						headAry.add(map);
					}
				}
			
			}else if (chooseType==3 || chooseType==4) {
				//param.put("shippingType", "X");
				param.put("receiptType", "("+EnumReceiptType.STOCK_TASK_LISTING.getValue()+")");
				if (chooseType==3) {
					param.put("status", EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				}else {
					param.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
				}
				list = taskServiceImpl.getBizStockTaskHeadListForCW(param);
				if (list!=null && list.size()>0) {
					total = Integer.parseInt(UtilString.getStringOrEmptyForObject(list.get(0).get("totalCount")));
					for (Map<String, Object> map : list) {
						int referReceiptType = UtilObject.getIntOrZero(map.get("refer_receipt_type"));//参考单据类型
						map.put("receipt_type", referReceiptType);
						map.put("type_name", EnumReceiptType.getNameByValue((byte)referReceiptType));
						map.put("create_time", sdf.format(map.get("create_time")));
						int status = UtilObject.getIntOrZero(map.get("status"));
						map.put("status_name", getReqStatusName(EnumReceiptType.STOCK_TASK_LISTING.getValue(),(byte)status));
						headAry.add(map);
					}
				}
			}
			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("上架作业列表", e);
		}

		return UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, headAry);
	}

	// 上架作业-申请单详情
	@RequestMapping(value = "/shelves/biz_stock_task_req_details/{stock_task_req_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqDetails(@PathVariable("stock_task_req_id") int stockTaskReqId,
			CurrentUser cUser) {
		JSONArray bizStockTaskReqItemsAry = new JSONArray();// 申请单明细
		JSONArray bizStockTaskHeadAry = new JSONArray();// 作业单抬头
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean returnStatus = false;

		if (StringUtils.hasText(stockTaskReqId + "")) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Map<String, Object> map = new HashMap<>();
				map.put("stockTaskReqId", stockTaskReqId);
				// 获取作业申请单抬头
				List<Map<String, Object>> reqHeadList = taskServiceImpl.getBizStockTaskReqHeadListForCW(map);
				
				if (reqHeadList != null && reqHeadList.size() > 0) {
					Map<String, Object> resultMap = reqHeadList.get(0);								
					int referReceiptType = UtilObject.getIntOrZero(resultMap.get("refer_receipt_type"));//参考单据类型
					body.put("receipt_type", referReceiptType);//单据类型
					body.put("type_name", EnumReceiptType.getNameByValue((byte)referReceiptType));//单据类型描述					
					body.put("create_time", sdf.format(resultMap.get("create_time")));
					int status = UtilObject.getIntOrZero(resultMap.get("status"));
					body.put("status_name", getReqStatusName(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue(),(byte)status));
					body.put("stock_task_req_id", resultMap.get("stock_task_req_id"));//作业申请号
					body.put("stock_task_req_code", resultMap.get("stock_task_req_code"));//作业申请号
					body.put("refer_receipt_code", resultMap.get("refer_receipt_code"));//单据类型					
					body.put("create_user_name", resultMap.get("create_user"));//创建人
					body.put("delivery_vehicle", resultMap.get("delivery_vehicle"));// 创建人
					body.put("delivery_driver", resultMap.get("delivery_driver"));// 创建人
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
							item.put("stock_task_req_rid", reqItemVo.getStockTaskReqRid());// 作业单行号
							item.put("mat_code", reqItemVo.getMatCode());// 物料编码
							item.put("mat_name", reqItemVo.getMatName());// 物料描述
							item.put("unit_id",reqItemVo.getUnitId());
							item.put("unit_zh", reqItemVo.getNameZh());// 单位
							item.put("location_id",reqItemVo.getLocationId());
							item.put("location_name", reqItemVo.getLocationName());// 库存地点
							item.put("location_code",reqItemVo.getLocationCode());
							item.put("spec_stock", reqItemVo.getSpecStockCode());// 特殊库存标识
							item.put("spec_stock_code", reqItemVo.getSpecStockCode());// 特殊库存编码
							item.put("spec_stock_name", reqItemVo.getSpecStockName());// 特殊库存描述
							item.put("production_batch", reqItemVo.getProductionBatch());//生产批次
							item.put("package_type_id", reqItemVo.getPackageTypeId());//
							item.put("package_type_code", reqItemVo.getPackageTypeCode());//包装类型
							item.put("package_type_name", reqItemVo.getPackageTypeName());//
							item.put("unstock_task_qty", unStockTaskQty);// 未上架数量
							item.put("task_qty", 0);// 本次上架数量默认是0
							// item.put("stock_task_qty", stockTaskQty);// 已上架数量
							item.put("decimal_place", reqItemVo.getDecimalPlace());// 计量单位浮点数
							bizStockTaskReqItemsAry.add(item);
						}
					}
				}
				body.put("item_list", bizStockTaskReqItemsAry);

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
						Obj.put("create_time", sdf.format(innerVo.getCreateTime()));// 上架日期
						String locationcode = innerVo.getLocationCode();
						String locationname = innerVo.getLocationName();
						Obj.put("location_name", locationcode + "-" + locationname);// 库存地点
						Obj.put("user_name", innerVo.getUserName());// 作业人
						Obj.put("status", innerVo.getStatus());// 作业状态
						Obj.put("status_name", innerVo.getStatusNameForListing());// 作业状态：是否入库
						bizStockTaskHeadAry.add(Obj);
					}
				}
				body.put("task_list", bizStockTaskHeadAry);
				returnStatus = true;
				error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			} catch (Exception e) {
				logger.error("上架作业明细", e);
			}
		}
		return UtilResult.getResultToJson(returnStatus, error_code, body);
	}

	// 上架单详情

	@RequestMapping(value = "/shelves/get_inner_task_details/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getInnerTaskDetails(@PathVariable("stock_task_id") int stockTaskId,
			CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean returnStatus = false;
		List<BizStockTaskHeadVo> headList = new ArrayList<BizStockTaskHeadVo>();
		List<BizStockTaskItemVo> itemList = new ArrayList<BizStockTaskItemVo>();
		JSONObject body = new JSONObject();
		JSONArray bizStockTaskItemAry = new JSONArray();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			if (stockTaskId > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				BizStockTaskHeadCw bizStockTaskHead = taskServiceImpl.getBizStockTaskHeadByPrimaryKey(stockTaskId);
				
				Map<String, Object> map = new HashMap<>();
				map.put("stockTaskId", bizStockTaskHead.getStockTaskId());
				map.put("currentUser", cUser.getUserId());
				// 获取作业单抬头
				list = taskServiceImpl.getBizStockTaskHeadListForCW(map);				
				if (list != null && list.size() > 0) {
					Map<String, Object> resultMap = list.get(0);	
					int referReceiptType = UtilObject.getIntOrZero(resultMap.get("refer_receipt_type"));//参考单据类型
					body.put("receipt_type", referReceiptType);
					body.put("type_name", EnumReceiptType.getNameByValue((byte)referReceiptType));
					body.put("create_time", sdf.format(resultMap.get("create_time")));
					int status = UtilObject.getIntOrZero(resultMap.get("status"));
					body.put("status_name", getReqStatusName(EnumReceiptType.STOCK_TASK_LISTING.getValue(),(byte)status));		
					body.put("status", status);
					body.put("stock_task_req_id", resultMap.get("stock_task_req_id"));
					body.put("stock_task_req_code", resultMap.get("stock_task_req_code"));
					body.put("stock_task_id", resultMap.get("stock_task_id"));
					body.put("stock_task_code", resultMap.get("stock_task_code"));
					body.put("create_user", resultMap.get("create_user"));
					body.put("refer_receipt_code", resultMap.get("refer_receipt_code"));
					body.put("remark", resultMap.get("remark"));
				}
				BizStockTaskItemCw bizStockTaskItem = new BizStockTaskItemCw();
				bizStockTaskItem.setStockTaskId(stockTaskId);
				itemList = taskServiceImpl.getItemsByStockTaskId(bizStockTaskItem);
				
				if (headList != null && headList.size() > 0) {
					for (BizStockTaskItemVo itemVo : itemList) {
						JSONObject item = new JSONObject();
						item.put("stockTaskRid", itemVo.getStockTaskRid());// 作业单行号
						item.put("mat_code", itemVo.getMatCode());// 物料编码
						item.put("mat_name", itemVo.getMatName());// 物料描述
						item.put("unit_zh", itemVo.getNameZh());// 单位
						item.put("location_id", itemVo.getLocationId());
						item.put("location_code", itemVo.getLocationCode());
						item.put("location_name", itemVo.getLocationName());// 库存地点
						item.put("qty", itemVo.getQty());// 本次上架数量
						item.put("unstock_task_qty", itemVo.getUnstockTaskQty());// 本次上架数量
						
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

		return UtilResult.getResultToJson(returnStatus, error_code, body);
	}

	// 仓库整理--基于仓位调整提交
	@RequestMapping(value = "/warehouse/position_submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject positionSubmit(@RequestBody JSONObject json, CurrentUser cUser) {
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		String errorString = "";
		int total = 0;
		try {
			int ftyId = json.getInt("fty_id");
			int locationId = json.getInt("location_id");
			int whId = json.getInt("wh_id");
			String whCode = dictionaryService.getWhCodeByWhId(whId);// 仓库号

			// 根据发出 存储区code-仓位code 获取 存储区id-仓位id
			String sourceAreaPositionCode = json.getString("s_area_position_code"); // 源-存储区code-仓位code
			String[] sourceAreaPositionCodeAry = sourceAreaPositionCode.split("-", 2);
			String sourceAreaCode = sourceAreaPositionCodeAry[0];// 存储区code
			int sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, sourceAreaCode);
			String sourcePositionCode = sourceAreaPositionCodeAry[1];// 仓位code
			DicWarehousePositionKey sourceKey = new DicWarehousePositionKey();
			sourceKey.setAreaId(sourceAreaId);
			sourceKey.setWhId(whId);
			sourceKey.setPositionCode(sourcePositionCode);
			DicWarehousePosition sourcePosition = taskServiceImpl.selectDicWarehousePositionByKey(sourceKey);
			int sourcePositionId = sourcePosition.getPositionId();

			// 根据目标 存储区code-仓位code 获取 存储区id-仓位id
			String targetAreaPositionCode = json.getString("t_area_position_code"); // 目标-存储区code-仓位code
			String[] targetAreaPositionCodeAry = targetAreaPositionCode.split("-", 2);
			String targetAreaCode = targetAreaPositionCodeAry[0];// 存储区code
			int targetAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, targetAreaCode);
			String targetPositionCode = targetAreaPositionCodeAry[1];// 仓位code
			DicWarehousePositionKey targetKey = new DicWarehousePositionKey();
			targetKey.setAreaId(targetAreaId);
			targetKey.setWhId(whId);
			targetKey.setPositionCode(targetPositionCode);
			DicWarehousePosition targetPosition = taskServiceImpl.selectDicWarehousePositionByKey(targetKey);
			int targetPositionId = targetPosition.getPositionId();

			if (sourceAreaId == targetAreaId && sourcePositionId == targetPositionId) {
				throw new WMSException("发出仓位与接受仓位不能相同");
			}
			
			BizStockTaskHead bizStockTaskHead = new BizStockTaskHead();
			bizStockTaskHead.setWhId(whId);
			bizStockTaskHead.setMoveTypeId(0);
			bizStockTaskHead.setShippingType("X");
			bizStockTaskHead.setCreateUser(cUser.getUserId());
			bizStockTaskHead.setFtyId(ftyId);
			bizStockTaskHead.setLocationId(locationId);
			bizStockTaskHead.setReceiptType(EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue());
			bizStockTaskHead.setStockTaskReqId(0);
			bizStockTaskHead.setRemark("");
			
			BizStockTaskItem bizStockTaskItem = new BizStockTaskItem();
			bizStockTaskItem.setWhId(whId);
			bizStockTaskItem.setFtyId(ftyId);
			bizStockTaskItem.setLocationId(locationId);
			bizStockTaskItem.setSourceAreaId(sourceAreaId);
			bizStockTaskItem.setSourcePositionId(sourcePositionId);
			bizStockTaskItem.setTargetAreaId(targetAreaId);
			bizStockTaskItem.setTargetPositionId(targetPositionId);

			List<Map<String, String>> checkOutMaps = new ArrayList<Map<String, String>>();
			List<Map<String, String>> checkInMaps = new ArrayList<Map<String, String>>();

			HashMap<String, String> checkOutMap = new HashMap<String, String>();
			checkOutMap.put("position_id", sourcePositionId + "");
			checkOutMaps.add(checkOutMap);
			HashMap<String, String> checkInMap = new HashMap<String, String>();
			checkInMap.put("position_id", targetPositionId + "");
			checkInMaps.add(checkInMap);

			FreezeCheck fOutCheck = commonService.checkWhPosAndStockLoc(checkOutMaps);
			FreezeCheck fInCheck = commonService.checkWhPosAndStockLoc(checkInMaps);
			if (fOutCheck != null && !fOutCheck.isLockedOutput() && fInCheck != null && !fInCheck.isLockedInput()) {
				taskServiceImpl.positionCleanup(bizStockTaskHead, bizStockTaskItem);
				returnStatus = true;
				error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			} else {
				if (fOutCheck != null && StringUtils.hasText(fOutCheck.getResultMsgOutput())) {
					errorString += fOutCheck.getResultMsgOutput();
				}
				if (fInCheck != null && StringUtils.hasText(fInCheck.getResultMsgOutput())) {
					errorString += fInCheck.getResultMsgOutput();
				}
			}

		} catch (InventoryException e) {
			logger.error("", e);
			errorString = e.getMessage();
		} catch (WMSException e) {
			logger.error("", e);
			errorString = e.getMessage();
		}  catch (Exception e) {
			logger.error("基于仓位提交", e);
		}

		return UtilResult.getResultToJson(returnStatus, error_code, errorString, pageIndex, pageSize, total, body);
	}

	// 仓库整理--基于物料调整提交
	@RequestMapping(value = "/warehouse/material_submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject materialSubmit(@RequestBody JSONObject json, CurrentUser cUser) {
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		String errorString = "";
		int total = 0;
		try {

			int ftyId = json.getInt("fty_id");// 工厂
			int locationId = json.getInt("location_id");// 库存地点
			int whId = json.getInt("wh_id");// 仓库号
			String whCode = dictionaryService.getWhCodeByWhId(whId);// 仓库号

			BizStockTaskHead bizStockTaskHead = new BizStockTaskHead();
			bizStockTaskHead.setWhId(whId);
			bizStockTaskHead.setMoveTypeId(0);
			bizStockTaskHead.setShippingType("X");
			bizStockTaskHead.setCreateUser(cUser.getUserId());
			bizStockTaskHead.setFtyId(ftyId);
			bizStockTaskHead.setLocationId(locationId);
			bizStockTaskHead.setReceiptType(EnumReceiptType.STOCK_TASK_MATERIAL_CLEANUP.getValue());
			bizStockTaskHead.setStockTaskReqId(0);
			bizStockTaskHead.setRemark("");

			List<BizStockTaskItem> itemList = new ArrayList<BizStockTaskItem>();
			List<Map<String, String>> checkOutMaps = new ArrayList<Map<String, String>>();
			List<Map<String, String>> checkInMaps = new ArrayList<Map<String, String>>();

			JSONArray materialItem = json.getJSONArray("item_list");
			for (Object object : materialItem) {
				JSONObject material = (JSONObject) object;

				int stockPositionId = material.getInt("id");// 仓位库存id
				//查出对应sn库存
				StockSn sn = taskServiceImpl.queryStockSnByStockId(stockPositionId);
				int snId = 0 ;
				if (sn!= null) {
					snId = sn.getSnId();// sn库存
				}				
				int matId = material.getInt("mat_id");// 物料
				int sourceAreaId = material.getInt("s_area_id");// 源存储区
				int sourcePositionId = material.getInt("s_position_id");// 源仓位

//				StockPosition stockPosition = new StockPosition();
//				stockPosition.setWhId(whId);
//				stockPosition.setAreaId(sourceAareaId);
//				stockPosition.setPositionId(sourcePositionIid);
//				stockPosition.setMatId(matId);
//
//				List<StockPositionVo> materialInfoList = taskServiceImpl.getMaterialInPosition(stockPosition);
//				StockPositionVo stockPositionVo = materialInfoList.get(0);
				StockPosition stockPosition = taskServiceImpl.selectStockPositionByPrimaryKey(stockPositionId);
				JSONArray targetCodeArray = material.getJSONArray("target_position_list");// 目标-存储区code-仓位code
				for (Object innerObject : targetCodeArray) {
					BizStockTaskItem bizStockTaskItem = new BizStockTaskItem();
					bizStockTaskItem.setStockPositionId(stockPositionId);
					bizStockTaskItem.setBatch(stockPosition.getBatch());
					bizStockTaskItem.setFtyId(ftyId);
					bizStockTaskItem.setIsDelete((byte) 0);
					bizStockTaskItem.setWhId(whId);
					bizStockTaskItem.setLocationId(locationId);
					bizStockTaskItem.setMatId(matId);
					bizStockTaskItem.setSourceAreaId(sourceAreaId);
					bizStockTaskItem.setSourcePositionId(sourcePositionId);
					bizStockTaskItem.setSourceStockType(0);
					bizStockTaskItem.setSpecStock(UtilString.getStringOrEmptyForObject(stockPosition.getSpecStock()));
					bizStockTaskItem
							.setSpecStockCode(UtilString.getStringOrEmptyForObject(stockPosition.getSpecStockCode()));
					bizStockTaskItem
							.setSpecStockName(UtilString.getStringOrEmptyForObject(stockPosition.getSpecStockName()));
					bizStockTaskItem.setTargetStockType(0);
					bizStockTaskItem.setUnitId(stockPosition.getUnitId());
					bizStockTaskItem.setUnitWeight(stockPosition.getUnitWeight());

					JSONObject targetPositionJson = (JSONObject) innerObject;
					BigDecimal qty = new BigDecimal(targetPositionJson.getString("qty"));// 调整数量
					String targetAreaPositionCode = targetPositionJson.getString("t_area_position_code"); // 源-存储区code-仓位code
					String[] targetAreaPositionCodeAry = targetAreaPositionCode.split("-", 2);
					String targetAreaCode = targetAreaPositionCodeAry[0];// 存储区code
					int targetAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, targetAreaCode);
					String targetPositionCode = targetAreaPositionCodeAry[1];// 仓位code
					DicWarehousePositionKey targetKey = new DicWarehousePositionKey();
					targetKey.setAreaId(targetAreaId);
					targetKey.setWhId(whId);
					targetKey.setPositionCode(targetPositionCode);
					DicWarehousePosition targetPosition = taskServiceImpl.selectDicWarehousePositionByKey(targetKey);
					int targetPositionId = targetPosition.getPositionId();

					if (sourceAreaId == targetAreaId && sourcePositionId == targetPositionId) {
						throw new WMSException("发出仓位与接受仓位不能相同");
					}
					
					bizStockTaskItem.setQty(qty);
					bizStockTaskItem.setTargetAreaId(targetAreaId);
					bizStockTaskItem.setTargetPositionId(targetPositionId);
					bizStockTaskItem.setTargetPackageId(0);
					bizStockTaskItem.setTargetPalletId(0);
					bizStockTaskItem.setSnId(snId);
					itemList.add(bizStockTaskItem);

					HashMap<String, String> checkOutMap = new HashMap<String, String>();
					checkOutMap.put("position_id", sourcePositionId + "");
					checkOutMaps.add(checkOutMap);
					HashMap<String, String> checkInMap = new HashMap<String, String>();
					checkInMap.put("position_id", targetPositionId + "");
					checkInMaps.add(checkInMap);

				}

			}

			FreezeCheck fOutCheck = commonService.checkWhPosAndStockLoc(checkOutMaps);
			FreezeCheck fInCheck = commonService.checkWhPosAndStockLoc(checkInMaps);

			if (fOutCheck != null && !fOutCheck.isLockedOutput() && fInCheck != null && !fInCheck.isLockedInput()) {
				taskServiceImpl.materialCleanupNew(bizStockTaskHead, itemList);
				returnStatus = true;
				error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			} else {
				if (fOutCheck != null && StringUtils.hasText(fOutCheck.getResultMsgOutput())) {
					errorString += fOutCheck.getResultMsgOutput();
				}
				if (fInCheck != null && StringUtils.hasText(fInCheck.getResultMsgOutput())) {
					errorString += fInCheck.getResultMsgOutput();
				}
			}
		}	catch (InventoryException e) {
			logger.error("", e);
			errorString = e.getMessage();
		}  catch (WMSException e) {
			logger.error("", e);
			errorString = e.getMessage();
		} catch (Exception e) {
			logger.error("基于物料提交", e);
		}

		return UtilResult.getResultToJson(returnStatus, error_code, errorString, pageIndex, pageSize, total, body);
	}

	@RequestMapping(value = "/warehouse/add_material", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addMaterial(@RequestBody JSONObject json, CurrentUser cUser) {
		JSONArray returnAry = new JSONArray();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		int total = 0;
		List<StockPositionVo> list = null;
		try {
			// pageIndex = json.getInt("page_index");
			// pageSize = json.getInt("page_size");
			// int totalCount = -1;
			// if (json.containsKey("total")) {
			// totalCount = json.getInt("total");
			// }

			int whId = json.getInt("wh_id");
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			String condition = json.getString("condition").trim();
			list = taskServiceImpl.getStockPositionList(whId, condition, UtilProperties.getInstance().getDefaultCCQ(),
					pageIndex, pageSize, -1);

			JSONObject innerObject;
			if (list != null && list.size() > 0) {
				total = list.get(0).getTotalCount();

				for (StockPositionVo stockPositionVo : list) {
					innerObject = new JSONObject();
					innerObject.put("id", stockPositionVo.getId());// 仓位库存id
					innerObject.put("sn_id", stockPositionVo.getSnId());// sn库存sn_id
					
					innerObject.put("package_id", stockPositionVo.getPackageId());//包
					innerObject.put("pallet_id", stockPositionVo.getPalletId());//托盘
					innerObject.put("location_id", stockPositionVo.getLocationId());//库存地点
					innerObject.put("wh_id", whId);
					innerObject.put("wh_code", whCode);
					innerObject.put("mat_id", stockPositionVo.getMatId());// 物料id
					innerObject.put("mat_code", stockPositionVo.getMatCode());// 物料编号
					innerObject.put("mat_name", stockPositionVo.getMatName());// 物料描述
					innerObject.put("spec_stock", stockPositionVo.getSpecStock());// 特殊库存标识
					innerObject.put("spec_stock_code", stockPositionVo.getSpecStockCode());// 特殊库存编号
					innerObject.put("spec_stock_name", stockPositionVo.getSpecStockName());// 特殊库存描述
					innerObject.put("s_area_id", stockPositionVo.getAreaId());// 源存储区id
					innerObject.put("s_area_code", stockPositionVo.getAreaCode());// 源存储区code
					innerObject.put("s_position_id", stockPositionVo.getPositionId());// 源仓位号id
					innerObject.put("s_position_code", stockPositionVo.getPositionCode());// 源仓位号code
					innerObject.put("batch", stockPositionVo.getBatch());// 批次号
					innerObject.put("qty", stockPositionVo.getQty());// 批次数量
					innerObject.put("unit_name", stockPositionVo.getNameZh());// 单位
					innerObject.put("unit_id", stockPositionVo.getUnitId());// 单位id
					innerObject.put("unit_weight", stockPositionVo.getUnitWeight());// 重量单位
					innerObject.put("decimal_place", 1);// 小数位 假的，一会儿换成真的
					returnAry.add(innerObject);
				}
			}
			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("添加物料", e);
		}
		return UtilResult.getResultToJson(returnStatus, error_code, pageIndex, pageSize, total, returnAry);
	}

	// 仓库整理--获取作业单明细
//	@RequestMapping(value = "/warehouse/biz_stock_task_details/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject bizStockTaskDetails(@PathVariable("stock_task_id") int stockTaskId,
//			CurrentUser cUser) {
//		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
//		boolean returnStatus = false;
//		JSONObject returnObj = new JSONObject();
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//			BizStockTaskHead bizStockTaskHead = taskServiceImpl.getBizStockTaskHeadByPrimaryKey(stockTaskId);
//			if (bizStockTaskHead != null) {
//
//				int ftyId = bizStockTaskHead.getFtyId();
//				int locationId = bizStockTaskHead.getLocationId();
//				int whId = bizStockTaskHead.getWhId();
//				byte receiptType = bizStockTaskHead.getReceiptType();
//				String userId = bizStockTaskHead.getCreateUser();
//
//				returnObj.put("stockTaskId", bizStockTaskHead.getStockTaskId());// 作业单id
//				returnObj.put("stockTaskCode", bizStockTaskHead.getStockTaskCode());// 作业单code
//				if (receiptType == EnumReceiptType.STOCK_TASK_MATERIAL_CLEANUP.getValue()) {
//					returnObj.put("receiptTypeName", "基于物料调整");
//				} else if (receiptType == EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue()) {
//					returnObj.put("receiptTypeName", "基于仓位调整");
//				} else {
//					returnObj.put("receiptTypeName", "上架请求类型");
//				} // 业务类型
//
//				returnObj.put("ftyName", dictionaryService.getFtyIdMap().get(ftyId).getFtyName());// 工厂
//				returnObj.put("locationName", dictionaryService.getLocationIdMap().get(locationId).getLocationName());// 库存地点
//				returnObj.put("whName", dictionaryService.getWarehouseIdMap().get(whId).getWhName());// 仓库
//				returnObj.put("createUser", userService.getUserById(userId).getUserName());// 创建人
//				returnObj.put("createTime", sdf.format(bizStockTaskHead.getCreateTime()));// 创建时间
//				returnObj.put("remark", bizStockTaskHead.getRemark());// 备注
//
//				JSONArray bizStockTaskItemAry = new JSONArray();
//				BizStockTaskItem bizStockTaskItem = new BizStockTaskItem();
//				bizStockTaskItem.setStockTaskId(stockTaskId);
//				List<BizStockTaskItemVo> list = taskServiceImpl.getItemsByStockTaskId(bizStockTaskItem);
//				if (list != null && list.size() > 0) {
//					for (BizStockTaskItemVo vo : list) {
//						JSONObject item = new JSONObject();
//						item.put("stockTaskRid", vo.getStockTaskRid());// 作业单行号
//						item.put("matId", vo.getMatId());// 物料id
//						item.put("matCode", vo.getMatCode());// 物料编码
//						item.put("matName", vo.getMatName());// 物料描述
//						item.put("sourceAreaCode",
//								dictionaryService.getAreaByAreaId(vo.getSourceAreaId()).getAreaCode());// 源存储区
//						item.put("sourcePositionCode",
//								dictionaryService.getPositionByPositionId(vo.getSourcePositionId()).getPositionCode());// 源仓位
//						item.put("batch", vo.getBatch());// 批次
//						if (vo.getBatchqty() == null) {
//							item.put("batchQty", 0);// 批次数量
//						} else {
//							item.put("batchQty", vo.getBatchqty());// 批次数量
//						}
//						item.put("targetAreaCode",
//								dictionaryService.getAreaByAreaId(vo.getTargetAreaId()).getAreaCode());// 接收存储区
//						item.put("targetPositionCode",
//								dictionaryService.getPositionByPositionId(vo.getTargetPositionId()).getPositionCode());// 接收仓位
//						item.put("qty", vo.getQty());// 调整数量
//						item.put("spec_stock", vo.getSpecStock());
//						item.put("spec_stock_code", vo.getSpecStockCode());
//						item.put("spec_stock_name", vo.getSpecStockName());
//						bizStockTaskItemAry.add(item);
//					}
//					returnObj.put("item_list", bizStockTaskItemAry);
//				}
//			}
//			returnStatus = true;
//			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//		} catch (Exception e) {
//			logger.error("仓库整理明细", e);
//		}
//		return UtilResult.getResultToJson(returnStatus, error_code, returnObj);
//	}

	private String getTypeName(Object status) {
		if (status == null) {
			return "";
		}
		int status_int = 0;
		String return_str = "";
		try {
			status_int = Integer.parseInt(status.toString());
		} catch (Exception e) {

		}
		switch (status_int) {
		case 0:
			return_str = "未完成";
			break;
		case 1:
			return_str = "已完成";
			break;
		default:
			break;
		}

		return return_str;
	}

	// 上架作业--上架
	@RequestMapping(value = "/shelves/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject submit(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean returnStatus = false;
		String errorString = "";
		int total = 0;
		JSONObject body = new JSONObject();
		json.put("user_id", cUser.getUserId());
		Map<String, Object> returnValue = new HashMap<String, Object>();
		try {
			returnValue = taskServiceImpl.addBizStockTaskReqHead(json);
			if (returnValue != null) {
				String code = (String) returnValue.get("code");
				if ("1".equals(code)) {
					returnStatus = true;
					error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					errorString = "上架成功";
				} else {
					errorString = returnValue.get("msg").toString();
				}
			} else {
				errorString = "上架失败";
			}
		} catch (InventoryException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("上架作业上架", e);
		}

		return UtilResult.getResultToJson(returnStatus, error_code, errorString, pageIndex, pageSize, total,
				returnValue);
	}

	// 仓位的物料信息
	@RequestMapping(value = "/warehouse/get_material_in_position", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialInPosition(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean returnStatus = false;
		JSONArray body = new JSONArray();
		try {

			int whId = json.getInt("wh_id");
			int areaId = json.getInt("area_id");
			int positionId = json.getInt("position_id");

			StockPosition stockPosition = new StockPosition();
			stockPosition.setWhId(whId);
			stockPosition.setAreaId(areaId);
			stockPosition.setPositionId(positionId);

			List<StockPositionVo> list = taskServiceImpl.getMaterialInPosition(stockPosition);
			JSONObject material;
			if (list != null && list.size() > 0) {
				for (StockPositionVo stockPositionVo : list) {
					material = new JSONObject();
					material.put("mat_code", stockPositionVo.getMatCode());// 物料编码
					material.put("mat_name", stockPositionVo.getMatName());// 物料描述
					material.put("unit_zh", stockPositionVo.getNameZh());// 单位
					material.put("batch", stockPositionVo.getBatch());// 批次单位
					material.put("qty", stockPositionVo.getQty());// 批次数量
					material.put("spec_stock", UtilString.getStringOrEmptyForObject(stockPositionVo.getSpecStock()));// 特殊库存标识
					material.put("spec_stock_code",
							UtilString.getStringOrEmptyForObject(stockPositionVo.getSpecStockCode()));// 特殊库存编号
					material.put("spec_stock_name",
							UtilString.getStringOrEmptyForObject(stockPositionVo.getSpecStockName()));// 特殊库存描述
					material.put("s_position_id", positionId);
					body.add(material);
				}
			}
			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("获得仓位物料", e);
		}
		return UtilResult.getResultToJson(returnStatus, error_code, body);
	}

	// 校验目标仓位信息
	@RequestMapping(value = "/warehouse/position_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject positionList(@RequestBody JSONObject json) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		JSONObject body = new JSONObject();
		try {
			int whId = json.getInt("wh_id");// 仓库号
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			String areaPositionCode = json.getString("area_position_code"); // 存储区code-仓位code
			String[] areaPositionCodeAry = areaPositionCode.split("-", 2);
			String areaCode = areaPositionCodeAry[0];// 存储区code
			int areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
			String positionCode = areaPositionCodeAry[1];// 仓位code

			DicWarehousePositionKey key = new DicWarehousePositionKey();
			key.setAreaId(areaId);
			key.setWhId(whId);
			key.setPositionCode(positionCode);

			DicWarehousePosition position = taskServiceImpl.selectDicWarehousePositionByKey(key);
			if (position == null) {
				body.put("position", false);
				body.put("havenMaterial", "");
			} else {
				int positionId = position.getPositionId();
				StockPosition stockPosition = new StockPosition();
				stockPosition.setWhId(whId);
				stockPosition.setAreaId(areaId);
				stockPosition.setPositionId(positionId);

				String havenMaterial = taskServiceImpl.getHavenMaterial(stockPosition);
				body.put("position", true);
				body.put("positionObj", position);
				body.put("havenMaterial", UtilString.getStringOrEmptyForObject(havenMaterial));
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("仓位列表", e);
			body.put("position", false);
			body.put("havenMaterial", "");
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, 5, body);
	}

}
