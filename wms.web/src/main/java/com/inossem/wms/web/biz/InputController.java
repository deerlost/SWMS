package com.inossem.wms.web.biz;

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
import com.inossem.wms.model.biz.BizAllocateDeliveryHead;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.dic.DicMoveReason;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.EnumAllocateStatus;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumUnCheckReason;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.service.biz.IAllocService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/33")
public class InputController {

	private static final Logger logger = LoggerFactory.getLogger(InputController.class);

	@Autowired
	private IInputService rkglService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IAllocService allocService;

	@Autowired
	private IFileService fileService;

	/**
	 * 查询合同列表
	 * 
	 * @param zhtbh
	 * @param ebeln
	 * @return
	 */
	@RequestMapping(value = "/biz/input/exempt/contract_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getContractList(String contract_code, String purchase_order_code, CurrentUser user) {
		logger.info("根据送货单号获取验收单方法");
		logger.info("传进参数-----" + contract_code);
		logger.info("传进参数-----" + purchase_order_code);
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.hasText(contract_code)) {
			map.put("contractCode", contract_code);
		}
		if (StringUtils.hasText(purchase_order_code)) {
			map.put("purchaseOrderCode", purchase_order_code);
		}
		List<BizPurchaseOrderHead> headList = new ArrayList<BizPurchaseOrderHead>();
		try {
			if (!map.isEmpty()) {
				headList = rkglService.getContractList(map, user);

			}
		} catch (Exception e) {
			logger.error("rkgl查询合同列表", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), headList);
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
		boolean status = true;
		try {
			purchaseOrderHead = rkglService.getContractInfo(purchase_order_code, cUser);

		} catch (Exception e) {
			logger.error("获取采购订单详情", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), purchaseOrderHead);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	/**
	 * 其他入库移动原因,库存地点
	 * 
	 * @param condition
	 *            查询条件（采购订单 验收单）
	 * @return
	 */
	@RequestMapping(value = "/biz/input/exempt/base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object exemptBaseInfo(CurrentUser cUser) {

		HashMap<String, Object> body = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONArray ftyList = new JSONArray();
		List<Map<String, String>> unCheckReason = new ArrayList<Map<String, String>>();
		try {
			ftyList = commonService.listLocationForUser(cUser.getUserId(),"");
			unCheckReason = EnumUnCheckReason.toList();

		} catch (Exception e) {
			logger.error("免检入库免检原因", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		body.put("fty_list", ftyList);
		body.put("un_check_reason_list", unCheckReason);
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
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
	@RequestMapping(value = "/biz/input/platform/base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object platformBaseInfo(CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONArray ftyList = new JSONArray();
		try {
			ftyList = commonService.listLocationForUser(cUser.getUserId(),"");

		} catch (Exception e) {
			logger.error("免检入库免检原因", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyList);
		return obj;

	}

	/**
	 * 免检原因list
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/get_un_check_reason_list", "/biz/rkgl/zcrk/getUnCheckReasonList",
			"/biz/wdsp/mjrk/getUnCheckReasonList",
			"/biz/jjcrk/mjrk/getUnCheckReasonList" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getUnCheckReasonList() {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<Map<String, String>> unCheckReason = EnumUnCheckReason.toList();

		boolean status = true;
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), unCheckReason);
		return obj;
	}

	/**
	 * 新增免检入库单
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/exempt/exempt", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateExemptInput(@RequestBody JSONObject json, CurrentUser user) {

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
			stockInputHead.setRequestSource(EnumRequestSource.WEB.getValue());
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
	 * 新增招采入库单
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/platform/platform", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdatePlatformInput(@RequestBody JSONObject json, CurrentUser user) {

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
			stockInputHead.setRequestSource(EnumRequestSource.WEB.getValue());
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
			logger.error("新增招采入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		body.put("stockInputId", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
		body.put("stockInputCode", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputCode()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

	/**
	 * 新增其他入库单
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/other", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateOtherInput(@RequestBody JSONObject json, CurrentUser user) {

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
			stockInputHead.setRequestSource(EnumRequestSource.WEB.getValue());
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
	public @ResponseBody Object addOrUpdateAllocateInput(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			stockInputHead = UtilJSONConvert.fromJsonToHump(json, BizStockInputHead.class);
			stockInputHead.setReceiptType(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue());
			stockInputHead.setRequestSource(EnumRequestSource.WEB.getValue());
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
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
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
	 * 移动类型列表(其他入库)
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/move_type_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMoveTypeList() {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		List<DicMoveType> list = new ArrayList<DicMoveType>();
		try {
			list = commonService.getMoveTypeByReceiptType(EnumReceiptType.STOCK_INPUT_OTHER.getValue());
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("移动类型列表", e);
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, list);
		return obj;
	}

	/**
	 * (根据移动类型)移动原因列表
	 * 
	 * @param reason_id
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/move_reason/{receipt_type_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMoveReasonList(@PathVariable("receipt_type_id") Integer receipt_type_id) {

		List<DicMoveReason> list = new ArrayList<DicMoveReason>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		try {
			list = commonService.getReasonByMoveTypeId(receipt_type_id);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("(根据移动类型)移动原因列表", e);
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, list);
		return obj;
	}

	// 下面方法移动至MultController类。 刘宇 2018.04.03

	// /**
	// * 供应商列表
	// *
	// * @param id
	// * @param userName
	// * @return
	// */
	// @RequestMapping(value = "/biz/input/other/get_supplier_list", method =
	// RequestMethod.GET, produces = "application/json;charset=UTF-8")
	// public @ResponseBody Object getSupplierList(String supplier_code, String
	// supplier_name) {
	//
	// List<SupplierVo> list = new ArrayList<SupplierVo>();
	// Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
	// Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
	// Integer pageSize = EnumPage.PAGE_SIZE.getValue();
	// Integer total = EnumPage.TOTAL_COUNT.getValue();
	// try {
	// SupplierVo find = new SupplierVo();
	// find.setSupplierCode(supplier_code);
	// find.setSupplierName(supplier_name);
	//
	// list = commonService.getSupplierList(find);
	//
	// } catch (Exception e) {
	// errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
	// logger.error("供应商列表", e);
	// }
	//
	// JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex,
	// pageSize, total, list);
	// return obj;
	// }

	/**
	 * 查询批次特性
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/get_material", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMaterialBatchInfo(Integer mat_id) {

		Map<String, Object> body = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		try {
			body = commonService.getBatchSpecList(null, null, mat_id);

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("供应商列表", e);
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, body);
		UtilJSONConvert.deleteNull(obj);
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

	/**
	 * 免检入库列表
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/exempt/exempt_input_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputExemptList(CurrentUser cuser, @RequestBody JSONObject json) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		try {

			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			Integer totalCount = -1;

			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String condition = json.getString("condition");
			JSONArray statusList = json.getJSONArray("status_list");

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("statusList", statusList);
			map.put("condition", UtilString.getStringOrEmptyForObject(condition).trim());
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("totalCount", totalCount);
			map.put("receiptType", EnumReceiptType.STOCK_INPUT_EXEMPT_INSPECT.getValue());
			map.put("createUser", cuser.getUserId());
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);
			List<BizStockInputHead> shList = rkglService.getInputHeadList(map);
			if (shList != null && shList.size() > 0) {
				total = shList.get(0).getTotalCount();
				for (BizStockInputHead sh : shList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
					itemMap.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
					itemMap.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
					itemMap.put("receiptTypeName",
							UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
					itemMap.put("purchaseOrderCode", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderCode()));
					itemMap.put("supplierName", UtilString.getStringOrEmptyForObject(sh.getSupplierName()));
					itemMap.put("createTime", UtilString.getShortStringForDate(sh.getCreateTime()));
					itemMap.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
					itemMap.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
					itemMap.put("statusName",
							UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
					returnList.add(itemMap);
				}
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}

	/**
	 * 招采入库列表
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/platform/platform_input_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputPlatformList(CurrentUser cuser, @RequestBody JSONObject json) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		try {

			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			Integer totalCount = -1;

			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String condition = json.getString("condition");
			JSONArray statusList = json.getJSONArray("status_list");

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("statusList", statusList);
			map.put("condition", UtilString.getStringOrEmptyForObject(condition).trim());
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("totalCount", totalCount);
			map.put("receiptType", EnumReceiptType.STOCK_INPUT_PLATFORM.getValue());
			map.put("createUser", cuser.getUserId());
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);
			List<BizStockInputHead> shList = rkglService.getInputHeadList(map);
			if (shList != null && shList.size() > 0) {
				total = shList.get(0).getTotalCount();
				for (BizStockInputHead sh : shList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
					itemMap.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
					itemMap.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
					itemMap.put("receiptTypeName",
							UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
					itemMap.put("purchaseOrderCode", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderCode()));
					itemMap.put("supplierName", UtilString.getStringOrEmptyForObject(sh.getSupplierName()));
					itemMap.put("createTime", UtilString.getShortStringForDate(sh.getCreateTime()));
					itemMap.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
					itemMap.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
					itemMap.put("statusName",
							UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
					returnList.add(itemMap);
				}
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}

	/**
	 * 其他入库列表
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/other/other_input_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputOtherList(CurrentUser cuser, @RequestBody JSONObject json) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		try {

			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			Integer totalCount = -1;

			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");

			String condition = json.getString("condition");
			JSONArray statusList = json.getJSONArray("status_list");

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("statusList", statusList);
			map.put("condition", UtilString.getStringOrEmptyForObject(condition).trim());
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("totalCount", totalCount);
			map.put("receiptType", EnumReceiptType.STOCK_INPUT_OTHER.getValue());
			map.put("createUser", cuser.getUserId());
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);
			List<BizStockInputHead> shList = rkglService.getInputHeadList(map);
			if (shList != null && shList.size() > 0) {
				total = shList.get(0).getTotalCount();
				for (BizStockInputHead sh : shList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
					itemMap.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
					itemMap.put("moveTypeName", UtilString.getStringOrEmptyForObject(sh.getMoveTypeName()));
					itemMap.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
					itemMap.put("receiptTypeName",
							UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
					itemMap.put("purchaseOrderCode", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderCode()));
					itemMap.put("supplierName", UtilString.getStringOrEmptyForObject(sh.getSupplierName()));
					itemMap.put("createTime", UtilString.getShortStringForDate(sh.getCreateTime()));
					itemMap.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
					itemMap.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
					itemMap.put("statusName",
							UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
					returnList.add(itemMap);
				}
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}

	/**
	 * 调拨入库列表
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/biz/input/allocate/allocate_input_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputAllocateList(CurrentUser cuser, @RequestBody JSONObject json) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		try {

			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			Integer totalCount = -1;

			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String condition = json.getString("condition");
			JSONArray statusList = json.getJSONArray("status_list");

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("statusList", statusList);
			map.put("condition", UtilString.getStringOrEmptyForObject(condition).trim());
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("totalCount", totalCount);
			map.put("receiptType", EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue());
			map.put("createUser", cuser.getUserId());
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);
			List<Map<String, Object>> shList = rkglService.getAllocateInputHeadList(map);
			if (shList != null && shList.size() > 0) {
				Long totalLong = (Long) shList.get(0).get("totalCount");
				total = totalLong.intValue();
				for (Map<String, Object> sh : shList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("stock_input_id", UtilString.getStringOrEmptyForObject(sh.get("stock_input_id")));
					itemMap.put("stock_input_code", UtilString.getStringOrEmptyForObject(sh.get("stock_input_code")));
					itemMap.put("allocate_code", UtilString.getStringOrEmptyForObject(sh.get("allocate_code")));
					itemMap.put("allocate_delivery_code",
							UtilString.getStringOrEmptyForObject(sh.get("allocate_delivery_code")));
					itemMap.put("fty_output_name", UtilString.getStringOrEmptyForObject(sh.get("fty_output_name")));
					itemMap.put("location_output_name",
							UtilString.getStringOrEmptyForObject(sh.get("location_output_name")));
					itemMap.put("fty_input_name", UtilString.getStringOrEmptyForObject(sh.get("fty_input_name")));
					itemMap.put("location_input_name",
							UtilString.getStringOrEmptyForObject(sh.get("location_input_name")));
					itemMap.put("demand_date", UtilString.getShortStringForDate((Date) sh.get("demand_date")));
					itemMap.put("status", UtilString.getStringOrEmptyForObject(sh.get("status")));
					itemMap.put("status_name", UtilString.getStringOrEmptyForObject(
							EnumReceiptStatus.getNameByValue(UtilObject.getByteOrNull(sh.get("status")))));
					returnList.add(itemMap);
				}
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}

	/**
	 * 免检入库详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/exempt_input_info/{stock_input_id}",
			"/biz/myreceipt/exempt/exempt_input_info/{stock_input_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputExemptInfo(CurrentUser cuser,
			@PathVariable("stock_input_id") Integer stock_input_id) {

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> input = new HashMap<String, Object>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("stockInputId", stock_input_id);

			List<BizStockInputHead> shList = rkglService.getInputHeadList(map);
			if (shList != null && shList.size() == 1) {
				BizStockInputHead sh = shList.get(0);

				input.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
				input.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
				input.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
				input.put("receiptTypeName",
						UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
				input.put("purchaseOrderCode", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderCode()));
				input.put("supplierName", UtilString.getStringOrEmptyForObject(sh.getSupplierName()));
				input.put("createTime", UtilString.getShortStringForDate(sh.getCreateTime()));
				input.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
				input.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
				input.put("statusName",
						UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
				input.put("remark", UtilString.getStringOrEmptyForObject(sh.getRemark()));
				input.put("exemptCheck", UtilString.getStringOrEmptyForObject(sh.getExemptCheck()));
				input.put("exemptCheckName", UtilString.getStringOrEmptyForObject(
						EnumUnCheckReason.getNameByValue(UtilObject.getIntegerOrNull(sh.getExemptCheck()))));
				input.put("purchaseOrderType", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderType()));
				input.put("purchaseOrderTypeName", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderTypeName()));
				input.put("contractCode", UtilString.getStringOrEmptyForObject(sh.getContractCode()));
				input.put("corpName", UtilString.getStringOrEmptyForObject(sh.getCorpName()));
				input.put("purchaseGroupName", UtilString.getStringOrEmptyForObject(sh.getPurchaseGroupName()));
				input.put("purchaseOrgName", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrgName()));
				List<BizStockInputItem> itemList = rkglService.getInputItemListByID(stock_input_id);
				input.put("itemList", itemList);
				List<ApproveUserVo> userList = commonService.getReceiptUser(stock_input_id, sh.getReceiptType());
				List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(stock_input_id,
						sh.getReceiptType());
				input.put("userList", userList);
				input.put("fileList", fileList);
			} else {
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				status = false;
			}

		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, input);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	/**
	 * 招采入库详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/platform/platform_input_info/{stock_input_id}",
			"/biz/myreceipt/platform/platform_input_info/{stock_input_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputPlatformInfo(CurrentUser cuser,
			@PathVariable("stock_input_id") Integer stock_input_id) {

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> input = new HashMap<String, Object>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("stockInputId", stock_input_id);

			List<BizStockInputHead> shList = rkglService.getInputHeadList(map);
			if (shList != null && shList.size() == 1) {
				BizStockInputHead sh = shList.get(0);

				input.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
				input.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
				input.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
				input.put("receiptTypeName",
						UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
				input.put("purchaseOrderCode", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderCode()));
				input.put("supplierName", UtilString.getStringOrEmptyForObject(sh.getSupplierName()));
				input.put("createTime", UtilString.getShortStringForDate(sh.getCreateTime()));
				input.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
				input.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
				input.put("statusName",
						UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
				input.put("remark", UtilString.getStringOrEmptyForObject(sh.getRemark()));
				input.put("purchaseOrderType", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderType()));
				input.put("purchaseOrderTypeName", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderTypeName()));
				input.put("contractCode", UtilString.getStringOrEmptyForObject(sh.getContractCode()));
				input.put("corpName", UtilString.getStringOrEmptyForObject(sh.getCorpName()));
				input.put("purchaseGroupName", UtilString.getStringOrEmptyForObject(sh.getPurchaseGroupName()));
				input.put("purchaseOrgName", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrgName()));
				List<BizStockInputItem> itemList = rkglService.getInputItemListByID(stock_input_id);
				input.put("itemList", itemList);
				List<ApproveUserVo> userList = commonService.getReceiptUser(stock_input_id, sh.getReceiptType());
				List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(stock_input_id,
						sh.getReceiptType());
				input.put("userList", userList);
				input.put("fileList", fileList);
			} else {
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				status = false;
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, input);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	/**
	 * 其他入库详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/other/other_input_info/{stock_input_id}",
			"/biz/myreceipt/other/other_input_info/{stock_input_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputOtherInfo(CurrentUser cuser,
			@PathVariable("stock_input_id") Integer stock_input_id) {

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> input = new HashMap<String, Object>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("stockInputId", stock_input_id);

			List<BizStockInputHead> shList = rkglService.getInputHeadList(map);
			if (shList != null && shList.size() == 1) {
				BizStockInputHead sh = shList.get(0);
				input.put("checkAccount", UtilString.getStringOrEmptyForObject(sh.getCheckAccount()));
				input.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
				input.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
				input.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
				input.put("receiptTypeName",
						UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
				input.put("purchaseOrderCode", UtilString.getStringOrEmptyForObject(sh.getPurchaseOrderCode()));
				input.put("supplierName", UtilString.getStringOrEmptyForObject(sh.getSupplierName()));
				input.put("supplierCode", UtilString.getStringOrEmptyForObject(sh.getSupplierCode()));
				input.put("createTime", UtilString.getShortStringForDate(sh.getCreateTime()));
				input.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
				input.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
				input.put("statusName",
						UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
				input.put("remark", UtilString.getStringOrEmptyForObject(sh.getRemark()));
				input.put("moveTypeName", UtilString.getStringOrEmptyForObject(sh.getMoveTypeName()));
				input.put("moveTypeCode", UtilString.getStringOrEmptyForObject(sh.getMoveTypeCode()));
				input.put("specStock", UtilString.getStringOrEmptyForObject(sh.getSpecStock()));

				input.put("moveTypeId", UtilString.getStringOrEmptyForObject(sh.getMoveTypeId()));
				input.put("reason_id", UtilString.getStringOrEmptyForObject(sh.getReasonId()));
				input.put("reason_code", UtilString.getStringOrEmptyForObject(sh.getReasonCode()));
				input.put("reason_name", UtilString.getStringOrEmptyForObject(sh.getReasonName()));
				input.put("specStockCode", UtilString.getStringOrEmptyForObject(sh.getSpecStockCode()));
				input.put("ftyId", UtilString.getStringOrEmptyForObject(sh.getFtyId()));
				input.put("ftyCode", UtilString.getStringOrEmptyForObject(sh.getFtyCode()));
				input.put("ftyName", UtilString.getStringOrEmptyForObject(sh.getFtyName()));
				input.put("locationId", UtilString.getStringOrEmptyForObject(sh.getLocationId()));
				input.put("locationCode", UtilString.getStringOrEmptyForObject(sh.getLocationCode()));
				input.put("locationName", UtilString.getStringOrEmptyForObject(sh.getLocationName()));
				List<BizStockInputItem> itemList = rkglService.getInputItemListByID(stock_input_id);
				input.put("itemList", itemList);
				List<ApproveUserVo> userList = commonService.getReceiptUser(stock_input_id, sh.getReceiptType());
				List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(stock_input_id,
						sh.getReceiptType());
				input.put("userList", userList);
				input.put("fileList", fileList);
			} else {
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				status = false;
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, input);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	/**
	 * 调拨入库详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/allocate/allocate_input_info/{stock_input_id}",
			"/biz/myreceipt/allocate/allocate_input_info/{stock_input_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputAllocateInfo(CurrentUser cuser,
			@PathVariable("stock_input_id") Integer stock_input_id) {

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> input = new HashMap<String, Object>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("stockInputId", stock_input_id);

			List<Map<String, Object>> shList = rkglService.getAllocateInputHeadList(map);
			if (shList != null && shList.size() == 1) {
				Map<String, Object> sh = shList.get(0);

				input.put("stock_input_id", UtilString.getStringOrEmptyForObject(sh.get("stock_input_id")));
				input.put("stock_input_code", UtilString.getStringOrEmptyForObject(sh.get("stock_input_code")));
				input.put("allocate_code", UtilString.getStringOrEmptyForObject(sh.get("allocate_code")));
				input.put("allocate_id", UtilString.getStringOrEmptyForObject(sh.get("allocate_id")));
				input.put("allocate_delivery_code",
						UtilString.getStringOrEmptyForObject(sh.get("allocate_delivery_code")));
				input.put("allocate_delivery_id", UtilString.getStringOrEmptyForObject(sh.get("allocate_delivery_id")));
				input.put("fty_output_name", UtilString.getStringOrEmptyForObject(sh.get("fty_output_name")));
				input.put("location_output_name", UtilString.getStringOrEmptyForObject(sh.get("location_output_name")));
				input.put("fty_input_name", UtilString.getStringOrEmptyForObject(sh.get("fty_input_name")));
				input.put("location_input_name", UtilString.getStringOrEmptyForObject(sh.get("location_input_name")));
				input.put("demand_date", UtilString.getShortStringForDate((Date) sh.get("demand_date")));
				input.put("status", UtilString.getStringOrEmptyForObject(sh.get("status")));
				input.put("status_name", UtilString.getStringOrEmptyForObject(
						EnumReceiptStatus.getNameByValue(UtilObject.getByteOrNull(sh.get("status")))));
				input.put("applicant", UtilString.getStringOrEmptyForObject(sh.get("applicant")));
				input.put("org_name", UtilString.getStringOrEmptyForObject(sh.get("org_name")));

				Integer allocate_delivery_id = UtilObject.getIntegerOrNull(sh.get("allocate_delivery_id"));

				Map<String, Object> delivery_info = new HashMap<String, Object>();

				if (allocate_delivery_id != null) {
					// 配送单
					BizAllocateDeliveryHead delivery = allocService.getDeliveryById(allocate_delivery_id);
					if (delivery != null) {
						delivery_info.put("is_show", 1);
						delivery_info.put("delivery_date",
								UtilString.getShortStringForDate(delivery.getDeliveryDate()));
						delivery_info.put("delivery_vehicle", delivery.getDeliveryVehicle());
						delivery_info.put("delivery_driver", delivery.getDeliveryDriver());
						delivery_info.put("delivery_phone", delivery.getDeliveryPhone());
						delivery_info.put("remark", delivery.getRemark());
						input.put("allocate_delivery_code", delivery.getAllocateDeliveryCode());
						input.put("delivery_info", delivery_info);
					} else {
						delivery_info.put("is_show", 0);
					}

				} else {

					delivery_info.put("is_show", 0);
				}
				input.put("delivery_info", delivery_info);
				List<Map<String, Object>> itemList = rkglService.getInputAllocateItemById(stock_input_id);
				input.put("itemList", itemList);
				List<ApproveUserVo> userList = commonService.getReceiptUser(stock_input_id,
						EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue());
				List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(stock_input_id,
						EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue());
				input.put("userList", userList);
				input.put("fileList", fileList);
			} else {
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				status = false;
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, input);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	/**
	 * 删除未入库的入库单
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/exempt/{stock_input_id}",
			"/biz/input/platform/platform/{stock_input_id}", "/biz/input/other/other/{stock_input_id}",
			"/biz/input/allocate/allocate/{stock_input_id}" }, method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object deleteInputInfo(CurrentUser cuser,
			@PathVariable("stock_input_id") Integer stock_input_id) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = rkglService.deleteUnFinishedInputInfo(stock_input_id);
			errorCode = (Integer) result.get("errorCode");
			errorString = (String) result.get("errorString");
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("删除入库单", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, new JSONObject());
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	/**
	 * 删除未入库的入库单
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/write_off", "/biz/input/platform/write_off",
			"/biz/input/other/write_off",
			"/biz/input/allocate/write_off" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object writeOffInputInfo(CurrentUser cuser, @RequestBody JSONObject json) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		Map<String, Object> result = new HashMap<String, Object>();
		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			stockInputHead = UtilJSONConvert.fromJsonToHump(json, BizStockInputHead.class);
			result = rkglService.writeOff(stockInputHead, cuser);

			errorCode = (Integer) result.get("errorCode");
			errorString = (String) result.get("errorString");
			if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
				status = true;
			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();

			logger.error("", e);
		} catch (Exception e) {
			logger.error("入库冲销", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, new JSONObject());

		return obj;
	}

	/**
	 * 仓位库存
	 * 
	 * @param zdjbh
	 * @param zdjxm
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/stock_position", "/biz/input/platform/stock_position",
			"/biz/input/other/stock_position",
			"/biz/input/allocate/stock_position" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getItemStockPosition(Integer item_id) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		List<StockPosition> spList = new ArrayList<StockPosition>();

		if (item_id != null) {

			try {
				spList = rkglService.getStockPositionByItemId(item_id);

			} catch (Exception e) {
				status = false;
				errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
				logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
			}
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_NO_PARAM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_NO_PARAM.getName();
			status = false;
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, spList);
		UtilJSONConvert.setNullToEmpty(obj);
		return obj;
	}

}
