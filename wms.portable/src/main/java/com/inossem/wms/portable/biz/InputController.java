package com.inossem.wms.portable.biz;

import java.util.ArrayList;
import java.util.Date;
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

import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.dic.DicBatchSpec;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.EnumAllocateStatus;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumUnCheckReason;
import com.inossem.wms.model.vo.SupplierVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class InputController {

	private static final Logger logger = LoggerFactory.getLogger(InputController.class);

	@Autowired
	private IInputService rkglService;

	@Autowired
	private ICommonService commonService;

	/**
	 * 查询合同列表
	 * 
	 * @param zhtbh
	 * @param ebeln
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/contract_list",
			"/biz/input/platform/contract_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getContractList(String contract_code, String purchase_order_code, CurrentUser user) {
		logger.info("根据送货单号获取验收单方法");
		logger.info("传进参数-----" + contract_code);
		logger.info("传进参数-----" + purchase_order_code);
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.hasText(contract_code)) {
			map.put("contractCode", contract_code);
		}
		if (StringUtils.hasText(purchase_order_code)) {
			map.put("purchaseOrderCode", purchase_order_code);
		}

		List<BizPurchaseOrderHead> headList = new ArrayList<BizPurchaseOrderHead>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			if (!map.isEmpty()) {
				headList = rkglService.getContractList(map, user);
				if (headList != null && headList.size() > 0) {
					for (BizPurchaseOrderHead head : headList) {
						Map<String, Object> returnObj = new HashMap<String, Object>();
						returnObj.put("contractCode", head.getContractCode());
						returnObj.put("purchaseOrderCode", head.getPurchaseOrderCode());
						returnObj.put("supplierName", head.getSupplierName());
						returnList.add(returnObj);
					}

				}
			}
		} catch (Exception e) {
			logger.error("rkgl查询合同列表", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), returnList);
		return obj;
	}

	/**
	 * 获取合同信息（详情）
	 * 
	 * @param htbh
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/contract_info/{purchase_order_code}",
			"/biz/input/platform/contract_info/{purchase_order_code}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getContractInfo(@PathVariable("purchase_order_code") String purchase_order_code,
			CurrentUser cUser) {
		logger.info("获取合同信息");
		logger.info("传进参数-----" + purchase_order_code);
		BizPurchaseOrderHead purchaseOrderHead = new BizPurchaseOrderHead();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		try {
			purchaseOrderHead = rkglService.getContractInfo(purchase_order_code, cUser);

		} catch (Exception e) {
			logger.error("获取采购订单详情", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), purchaseOrderHead);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	/**
	 * 免检原因,库存地点list
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/input/platform/fty_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getPlatformBaseInfo(CurrentUser user) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		JSONArray ftyList = new JSONArray();
		try {
			ftyList = commonService.listFtyLocationForUser(user.getUserId(),"");
		} catch (Exception e) {
			logger.error("免检原因,库存地点list", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyList);
		return obj;
	}

	/**
	 * 免检原因,库存地点list
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/input/exempt/base_info" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getExemptBaseInfo(CurrentUser user) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<Map<String, String>> unCheckReason = EnumUnCheckReason.toList();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("unCheckReasonList", unCheckReason);
		JSONArray ftyList = new JSONArray();
		try {
			ftyList = commonService.listFtyLocationForUser(user.getUserId(),"");
		} catch (Exception e) {
			logger.error("免检原因,库存地点list", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		body.put("ftyList", ftyList);
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
		return obj;
	}

	/**
	 * 新增招采入库单
	 * 
	 * @param zmkpf
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/platform/platform", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdatePlatform(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			stockInputHead = UtilJSONConvert.fromJsonToHump(json, BizStockInputHead.class);
			stockInputHead.setReceiptType(EnumReceiptType.STOCK_INPUT_PLATFORM.getValue());
			List<BizStockInputItem> itemList = rkglService.trimZeroOrNullQtyObj(stockInputHead.getItemList());
			stockInputHead.setItemList(itemList);
			stockInputHead.setRequestSource(EnumRequestSource.PDA.getValue());
			Map<String, Object> addMap = rkglService.addInput(stockInputHead, user);
			errorCode = (Integer) addMap.get("errorCode");
			errorString = (String) addMap.get("errorString");
			if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
				Map<String, Object> postMap = rkglService.platformInputPosting(stockInputHead);
				errorCode = (Integer) postMap.get("errorCode");
				errorString = (String) postMap.get("errorString");
				if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
					BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
					rkglService.updateStock(mDocHead, stockInputHead);
					status = true;
				} else {
					throw new InterfaceCallException(errorString);
				}

			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增免检入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		body.put("stockInputId", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
		body.put("stockInputCode", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputCode()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

	/**
	 * 新增免检入库单
	 * 
	 * @param zmkpf
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/exempt/exempt", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateExempt(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			stockInputHead = UtilJSONConvert.fromJsonToHump(json, BizStockInputHead.class);
			stockInputHead.setReceiptType(EnumReceiptType.STOCK_INPUT_EXEMPT_INSPECT.getValue());
			List<BizStockInputItem> itemList = rkglService.trimZeroOrNullQtyObj(stockInputHead.getItemList());
			stockInputHead.setItemList(itemList);
			stockInputHead.setRequestSource(EnumRequestSource.PDA.getValue());
			Map<String, Object> addMap = rkglService.addInput(stockInputHead, user);
			errorCode = (Integer) addMap.get("errorCode");
			errorString = (String) addMap.get("errorString");
			if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
				Map<String, Object> postMap = rkglService.exemptInputPosting(stockInputHead);
				errorCode = (Integer) postMap.get("errorCode");
				errorString = (String) postMap.get("errorString");
				if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
					BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
					rkglService.updateStock(mDocHead, stockInputHead);
					status = true;
				} else {
					throw new InterfaceCallException(errorString);
				}

			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增免检入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		body.put("stockInputId", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
		body.put("stockInputCode", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputCode()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

	/**
	 * 新增调拨入库单
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/allocate/allocate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateAllocate(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			stockInputHead = UtilJSONConvert.fromJsonToHump(json, BizStockInputHead.class);
			stockInputHead.setReceiptType(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue());
			stockInputHead.setRequestSource(EnumRequestSource.PDA.getValue());
			Map<String, Object> addMap = rkglService.addInput(stockInputHead, user);
			errorCode = (Integer) addMap.get("errorCode");
			errorString = (String) addMap.get("errorString");
			if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
				Map<String, Object> postMap = rkglService.allocateInputPosting(stockInputHead);
				errorCode = (Integer) postMap.get("errorCode");
				errorString = (String) postMap.get("errorString");
				if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
					BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
					rkglService.updateStock(mDocHead, stockInputHead);
					status = true;
				} else if (errorCode.equals(EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue())) {
					rkglService.updateAllotItemAndDeliveryItem(stockInputHead, EnumAllocateStatus.WAIT_INPUT.getValue(),
							stockInputHead.getAllocateId(), stockInputHead.getAllocateDeliveryId());
					throw new InterfaceCallException(errorString);
				}

			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增免检入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		body.put("stockInputId", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
		body.put("stockInputCode", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputCode()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
		return obj;
	}

	/**
	 * 其他入库移动原因,库存地点
	 * 
	 * @param condition
	 *            查询条件（采购订单 验收单）
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/other_base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object otherBaseInfo(CurrentUser cUser) {

		HashMap<String, Object> body = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONArray ftyList = new JSONArray();
		List<DicMoveType> moveList = new ArrayList<DicMoveType>();
		try {
			ftyList = commonService.listFtyLocationForUser(cUser.getUserId(),"");
			moveList = commonService.listMovTypeAndReason(EnumReceiptType.STOCK_INPUT_OTHER.getValue());

		} catch (Exception e) {
			logger.error("其他入库免检原因", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		body.put("ftyList", ftyList);
		body.put("moveList", moveList);
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
		return obj;

	}

	/**
	 * 供应商列表
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/get_supplier_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getSupplierList(String supplier_code, String supplier_name) {

		List<SupplierVo> list = new ArrayList<SupplierVo>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		try {
			SupplierVo find = new SupplierVo();
			find.setSupplierCode(supplier_code);
			find.setSupplierName(supplier_name);

			list = commonService.getSupplierList(find);

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("供应商列表", e);
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, list);
		return obj;
	}

	/**
	 * 特殊库存代码
	 * 
	 * @param condition
	 *            查询条件（特殊库存代码）
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/spec_stock_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getSpecialInventoryList(String condition, CurrentUser cUser) {

		ArrayList<HashMap<String, Object>> specialInventoryList = new ArrayList<HashMap<String, Object>>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		for (int j = 0; j < 2; j++) {
			HashMap<String, Object> factory = new HashMap<String, Object>();
			factory.put("spec_stock_code", "11" + j);
			factory.put("spec_stock_name", "特殊库存" + j);
			specialInventoryList.add(factory);
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, specialInventoryList);
		return obj;

	}

	/**
	 * 物料描述列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/other/get_material_list",
			"/biz/jjcrk/getInMat" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMaterialList(String mat_code) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<DicMaterial> list = new ArrayList<DicMaterial>();
		try {
			DicMaterial material = new DicMaterial();
			material.setMatCode(mat_code);

			list = rkglService.getMaterialList(material);

			if (list != null) {
				for (DicMaterial item : list) {
					Map<String, Object> batchSpecMap = commonService.getBatchSpecList(null, null, item.getMatId());
					@SuppressWarnings("unchecked")
					List<DicBatchSpec> batchSpecList = (List<DicBatchSpec>) batchSpecMap.get("batchSpecList");

					@SuppressWarnings("unchecked")
					List<DicBatchSpec> batchMaterialSpecList = (List<DicBatchSpec>) batchSpecMap
							.get("batchMaterialSpecList");
					item.setBatchSpecList(batchSpecList);
					item.setBatchMaterialSpecList(batchMaterialSpecList);

				}
			}

		} catch (Exception e) {
			logger.error("物料描述列表", e);
		}
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, 0, 10, 5, list);
		UtilJSONConvert.deleteNull(obj);
		return obj;

	}

	/**
	 * 新增其他入库单
	 * 
	 * @param zmkpf
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/other", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateOther(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;

		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			stockInputHead = UtilJSONConvert.fromJsonToHump(json, BizStockInputHead.class);
			stockInputHead.setReceiptType(EnumReceiptType.STOCK_INPUT_OTHER.getValue());
			List<BizStockInputItem> itemList = rkglService.trimZeroOrNullQtyObj(stockInputHead.getItemList());
			stockInputHead.setItemList(itemList);
			stockInputHead.setRequestSource(EnumRequestSource.PDA.getValue());
			Map<String, Object> addMap = rkglService.addInput(stockInputHead, user);
			errorCode = (Integer) addMap.get("errorCode");
			errorString = (String) addMap.get("errorString");
			if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {

				Map<String, Object> postMap = rkglService.otherInputPosting(stockInputHead);
				errorCode = (Integer) postMap.get("errorCode");
				errorString = (String) postMap.get("errorString");
				if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
					BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
					rkglService.updateStock(mDocHead, stockInputHead);
					status = true;
				} else {
					throw new InterfaceCallException(errorString);
				}

			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增其他入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		body.put("stockInputId", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
		body.put("stockInputCode", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

	/**
	 * 6.3.3 调拨单配送单列表（创建调拨入库单时查询列表）
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/allocate/allocate_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getAllocateList(CurrentUser cuser, @RequestBody JSONObject json) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		boolean status = true;

		List<Map<String, Object>> allotItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			String condition = json.getString("condition");

			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;

			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			if (StringUtils.hasText(condition)) {
				map.put("condition", condition);
			}
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("totalCount", totalCount);
			map.put("ftyLocationIn", cuser.getLocationList());
			map.put("status", EnumAllocateStatus.SENDING.getValue());

			allotItems = rkglService.getAllotItemsOnCreateDBRK(map);
			if (allotItems != null && allotItems.size() > 0) {
				total = UtilObject.getIntOrZero(allotItems.get(0).get("totalCount"));
				for (Map<String, Object> allotItem : allotItems) {
					String dateStr = UtilString.getShortStringForDate((Date) allotItem.get("create_time"));
					allotItem.put("create_time", dateStr);

				}
			}

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, allotItems);
		return obj;
	}

	/**
	 * 15.1.2 调拨单详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/allocate/get_allocate_item", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getAllocateItem(CurrentUser cuser, Integer allocate_id, Integer allocate_delivery_id) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> input = new HashMap<String, Object>();
		if (allocate_id != null) {
			try {
				input = rkglService.getAllocateItem(cuser, allocate_id, allocate_delivery_id);
			} catch (Exception e) {
				status = false;
				errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
				logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
			}
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_ALLOCATEID_EMPTY.getValue();
			status = false;
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), input);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

}
