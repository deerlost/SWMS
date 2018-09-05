package com.inossem.wms.web.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.BizStockOutputHeadVo;
import com.inossem.wms.service.biz.IMatReqService;
import com.inossem.wms.service.biz.IOutPutService;
import com.inossem.wms.service.biz.IReturnService;
import com.inossem.wms.service.dic.IDicReceiverService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 出库单Controller
 * 
 * @author 高海涛
 * @date 2018年1月19日
 */
@Controller
public class OutPutController {

	private static final Logger logger = LoggerFactory.getLogger(OutPutController.class);

	@Resource
	private IOutPutService outPutService;
	@Resource
	private IDicReceiverService dicReceiverService;
	@Resource
	private IMatReqService matReqService;
	@Resource
	private IReturnService returnService;

	// MyTask 出库通用方法
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

	/**
	 * 获取出库单列表
	 * 
	 * @param keyWord
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = { "/biz/output/xsck/get_order_list", "/biz/output/cgth/get_order_list",
			"/biz/output/qtck/get_order_list",
			"/biz/output/ylck/get_order_list" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderList(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;

		List<Map<String, Object>> list = null;

		Map<String, Object> param = this.getParamMapToPageing(json);

		param.put("keyword", UtilObject.getStringOrEmpty(json.getString("keyword"))); // 关键字
		param.put("locationId", cUser.getLocationList());
		param.put("receiptType", json.get("receipt_type")); // 出库单类型

		try {
			list = outPutService.getOrderList(param);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
		} catch (Exception e) {
			logger.error("出库列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, list);
	}

	/**
	 * 获取出库明细
	 * 
	 * @param stock_output_id
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/xsck/get_order_view", "/biz/output/cgth/get_order_view",
			"/biz/output/qtck/get_order_view", "/biz/output/ylck/get_order_view",
			"/biz/myreceipt/sale_outpunt/get_order_view", "/biz/myreceipt/purchase_return/get_order_view",
			"/biz/myreceipt/other_output/get_order_view",
			"/biz/myreceipt/reserve_output/get_order_view" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderView(String stock_output_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		BizStockOutputHeadVo head = null;
		try {
			head = outPutService.getOrderView(stock_output_id);
		} catch (Exception e) {
			logger.error("出库概览页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, head);
	}

	/**
	 * 根据工厂、物料编码、库存地点、特殊库存类型、特殊库存编号查询仓位库存信息
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/llck/get_stock_data", "/biz/output/xsck/get_stock_data",
			"/biz/output/cgth/get_stock_data", "/biz/output/qtck/get_stock_data", "/biz/output/dbck/get_stock_data",
			"/biz/output/ylck/get_stock_data", }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getStockData(@RequestBody JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("matId", UtilObject.getStringOrEmpty(json.getString("mat_id"))); // 物料编码
		param.put("locationId", UtilObject.getStringOrEmpty(json.getString("location_id"))); // 库存地点
		param.put("specStock", UtilObject.getStringOrEmpty(json.get("spec_stock"))); // 特殊库存

		if (!"".equals(UtilObject.getStringOrEmpty(json.get("receipt_type")))) {
			param.put("receiptType", UtilObject.getStringOrEmpty(json.get("receipt_type")));
		}

		List<Map<String, Object>> result = null;

		if ("".equals(UtilObject.getStringOrEmpty(json.get("out_qty")))) {
			// 没有数量，则调用查询所有的接口
			result = outPutService.getStockData(param);
		} else {
			param.put("outQty", UtilObject.getStringOrEmpty(json.get("out_qty")));
			result = outPutService.getStockDataCommend(param);
		}

		return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), result);
	}

	/**
	 * 删除出库单
	 * 
	 * @param stock_output_id
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/llck/delete_order", "/biz/output/xsck/delete_order",
			"/biz/output/cgth/delete_order", "/biz/output/qtck/delete_order", "/biz/output/dbck/delete_order",
			"/biz/output/ylck/delete_order" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> deleteOutputOrder(int stock_output_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String msg = "";
		boolean status = true;

		try {
			msg = outPutService.deleteOutputOrder(stock_output_id, cUser.getUserId());

		} catch (Exception e) {
			logger.error("删除出库单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "删除失败";
		}

		return UtilResult.getResultToMap(status, error_code, msg, "");
	}

	/**
	 * 出库冲销
	 * 
	 * @date 2017年9月15日
	 * @author 高海涛
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/llck/write_off", "/biz/output/xsck/write_off", "/biz/output/cgth/write_off",
			"/biz/output/qtck/write_off", "/biz/output/dbck/write_off",
			"/biz/output/ylck/write_off", }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object writeOff(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String msg = "";
		boolean status = true;

		try {
			msg = outPutService.writeOff(jsonData, user);

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("冲销 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, "");
	}

	// MyTask 领料出库相关内容

	/**
	 * 获取领料出库列表
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/output/llck/get_matreq_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatReqList(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;
		List<Map<String, Object>> list = null;

		Map<String, Object> param = this.getParamMapToPageing(json);

		param.put("keyword", UtilObject.getStringOrEmpty(json.getString("keyword"))); // 关键字
		param.put("chooseType", UtilObject.getStringOrEmpty(json.getString("choose_type")));
		param.put("corpId", cUser.getCorpId());
		param.put("locationId", cUser.getLocationList());

		try {
			list = outPutService.getMatReqList(param);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
		} catch (Exception e) {
			logger.error("领料出库列表页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, list);
	}

	/**
	 * 根据领料单号获取领料单概览
	 * 
	 * @param mat_req_code
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/llck/get_matreq_view" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getMatReqViewByid(String mat_req_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("matReqId", mat_req_id);
		param.put("locationId", cUser.getLocationList());

		Map<String, Object> reslutMap = null;
		try {
			reslutMap = outPutService.getMatReqViewById(param);
			reslutMap.put("board_id", cUser.getBoardId());
		} catch (Exception e) {
			logger.error("领料单概览页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, reslutMap);
	}

	/**
	 * 根据出库单号获取领料出库单
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/llck/get_order_view_matreq",
			"/biz/myreceipt/matreq/get_order_view_matreq" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getOrderViewByMatReq(String stock_output_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stock_output_id);
		param.put("locationId", cUser.getLocationList());

		Map<String, Object> reslutMap = null;
		try {
			reslutMap = outPutService.getOrderViewByMatReq(param);
		} catch (Exception e) {
			logger.error("领料出库单概览页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, reslutMap);
	}

	/**
	 * 领料出库过账
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/llck/save_mr_order_sap" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveMROutOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();
		try {
			result = outPutService.saveOutputOrderData(jsonData, user, false,
					EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue());
			Map<String, Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn, result.getInt("stock_output_id"), user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("领料出库过账 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result.get("stock_output_id"));
	}

	// MyTask 销售出库
	/**
	 * 从SAP获取销售订单(概要)
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/xsck/get_sales_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getSalesOrderList(String sales_order_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> list = null;

		if (!"".equals(sales_order_code)) {
			while (sales_order_code.length() < 10) { // 不够十位数字，自动补0
				sales_order_code = "0" + sales_order_code;
			}

			try {
				list = outPutService.getSalesOrderList(sales_order_code, user.getUserId());
			} catch (Exception e) {
				logger.error("获取销售订单 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		} else {
			logger.error("获取销售订单 --", "请输入查询条件");
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, list);

	}

	/**
	 * 从SAP获取销售订单(详情)
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/xsck/get_sales_view" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getSalesViewBySap(String sales_order_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> body = null;

		if (!"".equals(sales_order_code)) {
			while (sales_order_code.length() < 10) { // 不够十位数字，自动补0
				sales_order_code = "0" + sales_order_code;
			}

			try {
				body = outPutService.getSalesViewBySap(sales_order_code, user);
			} catch (Exception e) {
				logger.error("获取销售订单 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		} else {
			logger.error("获取销售订单 --", "请输入查询条件");
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, body);

	}

	/**
	 * 销售出库过账
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/xsck/save_sales_order_sap" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveSalesOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();

		try {
			result = outPutService.saveOutputOrderData(jsonData, user, false,
					EnumReceiptType.STOCK_OUTPUT_SALE.getValue());
			Map<String, Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn, result.getInt("stock_output_id"), user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("销售出库过账 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result.get("stock_output_id"));
	}

	// MyTask 采购退货
	/**
	 * 从SAP获取采购订单(概要)
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/cgth/get_purchase_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getPurchaseOrderList(String purchase_order_code, String contract_code,
			String supplier_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> list = null;

		if (!UtilString.isNullOrEmpty(purchase_order_code) || !UtilString.isNullOrEmpty(contract_code)
				|| !UtilString.isNullOrEmpty(supplier_code)) {
			if (!UtilString.isNullOrEmpty(purchase_order_code)) {
				while (purchase_order_code.length() < 10) { // 不够十位数字，自动补0
					purchase_order_code = "0" + purchase_order_code;
				}
			}

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("purchaseOrderCode", purchase_order_code);
			param.put("contractCode", contract_code);
			param.put("supplierCode", supplier_code);
			param.put("userId", user.getUserId());
			try {
				list = outPutService.getPurchaseOrderList(param);
			} catch (Exception e) {
				logger.error("获取采购订单 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		} else {
			logger.error("获取采购订单 --", "请输入查询条件");
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, list);

	}

	/**
	 * 从SAP获取采购订单(详情)
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/cgth/get_purchase_view" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getPurchaseViewBySap(String purchase_order_code, String contract_code,
			String supplier_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> body = null;

		if (!UtilString.isNullOrEmpty(purchase_order_code)) {
			while (purchase_order_code.length() < 10) { // 不够十位数字，自动补0
				purchase_order_code = "0" + purchase_order_code;
			}

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("purchaseOrderCode", purchase_order_code);
			param.put("contractCode", contract_code);
			param.put("supplierCode", supplier_code);
			param.put("userId", user.getUserId());
			param.put("locationList", user.getLocationList());

			try {
				body = outPutService.getPurchaseViewBySap(param);
			} catch (Exception e) {
				logger.error("获取采购订单 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		} else {
			logger.error("获取采购订单 --", "请输入查询条件");
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, body);

	}

	/**
	 * 采购退货过账
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/cgth/save_purchase_order_sap" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object savePurchaseOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();

		try {
			result = outPutService.saveOutputOrderData(jsonData, user, false,
					EnumReceiptType.STOCK_OUTPUT_PURCHASE_RETURN.getValue());
			Map<String, Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn, result.getInt("stock_output_id"), user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("采购退货过账 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result.get("stock_output_id"));
	}

	// MyTask 其他出库

	/**
	 * 获取所有接收方
	 */
//	@RequestMapping(value = "/biz/output/qtck/get_jsf", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject listAllDicReceiver(CurrentUser user) {
//		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//		boolean status = true;
//		JSONArray body = new JSONArray();
//		try {
//			List<DicReceiver> receiverList = dicReceiverService.listAllDicReceiver();
//
//			for (DicReceiver receiver : receiverList) {
//				JSONObject receiverJSon = new JSONObject();
//				receiverJSon.put("id", receiver.getId());// 接收方表主键
//				receiverJSon.put("receive_code", receiver.getReceiveCode());// 接收方代码
//				receiverJSon.put("receive_name", receiver.getReceiveName());// 接收方描述
//				body.add(receiverJSon);
//			}
//		} catch (Exception e) {
//			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
//			status = false;
//			logger.error("获取接收方 --", e);
//		}
//		return UtilResult.getResultToJson(status, errorCode, 1, 50, 50, body);
//	}

	/**
	 * 参考价格查询
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/biz/output/qtck/get_reference_price", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getReferencePrice(@RequestBody JSONObject json, CurrentUser user) {
		JSONArray body = new JSONArray();
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		try {
			// String zpiid = llglService.initProcess(Long.parseLong(zlldh));
			int ftyId = json.getInt("fty_id");
			String matCodes = json.getString("mat_codes");
			// JSONArray ckjAry = new JSONArray();
			body = matReqService.getReferencePrice(matCodes.split(","), ftyId, user.getUserId());
			// body.put("ckj", ckjAry);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	/**
	 * 其他出库过账
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/qtck/save_other_order_sap" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveOtherOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();

		try {
			result = outPutService.saveOutputOrderData(jsonData, user, false,
					EnumReceiptType.STOCK_OUTPUT_OTHER.getValue());
			Map<String, Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn, result.getInt("stock_output_id"), user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("其他出库过账 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result.get("stock_output_id"));
	}

	// MyTask 调拨出库
	/**
	 * 查询前置单据
	 * 
	 * @date 2017年12月4日
	 * @author 高海涛
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/biz/output/dbck/get_allocate_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAllocateList(String keyword) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("keyword", keyword); // 关键字

		List<Map<String, Object>> list = null;

		try {
			list = outPutService.getAllocateList(param);
		} catch (Exception e) {
			logger.error("获取调拨前置单据 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, list);
	}

	/**
	 * 获取前置单据明细
	 * 
	 * @date 2017年12月4日
	 * @author 高海涛
	 * @param allotId
	 * @param deliveryId
	 * @return
	 */
	@RequestMapping(value = "/biz/output/dbck/get_allocate_by_id", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAllocateById(String allocate_id, String allocate_delivery_id) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("allocateId", allocate_id);
		param.put("allocateDeliveryId", allocate_delivery_id);

		Map<String, Object> body = new HashMap<String, Object>();

		try {
			body = outPutService.getAllocateById(param);
		} catch (Exception e) {
			logger.error("获取调拨前置单据 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}

	/**
	 * 调拨出库过账
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/dbck/save_allocate_order_sap" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveAllocateOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();

		try {
			result = outPutService.saveOutputOrderData(jsonData, user, false,
					EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue());
			Map<String, Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn, result.getInt("stock_output_id"), user.getUserId());
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("调拨出库过账 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result.get("stock_output_id"));
	}

	/**
	 * 根据出库单号获取调拨出库单
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/dbck/get_order_view_allocate",
			"/biz/myreceipt/allocate_output/get_order_view_allocate" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getOrderViewByAllocate(String stock_output_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> reslutMap = null;
		try {
			reslutMap = outPutService.getDbckByStockOutputId(stock_output_id);
		} catch (Exception e) {
			logger.error("调拨出库单概览页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, reslutMap);
	}

	/**
	 * 获取调拨出库单列表
	 * 
	 * @param keyWord
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = {
			"/biz/output/dbck/get_order_list" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderListAllocate(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;

		List<Map<String, Object>> list = null;

		Map<String, Object> param = this.getParamMapToPageing(json);

		param.put("keyword", UtilObject.getStringOrEmpty(json.getString("keyword"))); // 关键字
		param.put("locationId", cUser.getLocationList());
		param.put("receiptType", json.get("receipt_type")); // 出库单类型

		try {
			list = outPutService.getOrderListAllocate(param);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
		} catch (Exception e) {
			logger.error("出库列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, list);
	}

	/**
	 * 预留单明细
	 * 
	 * @param refer_receipt_id
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/ylck/get_reserve_view" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getReserveView(String refer_receipt_code, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = null;

		if (!"".equals(refer_receipt_code)) {
			try {
				body = outPutService.getReserveViewBySap(refer_receipt_code, cUser);			
			} catch (Exception e) {
				logger.error("获取预留订单 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		} else {
			logger.error("获取预留订单 --", "请输入查询条件");
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}


	/**
	 * 获取预留单概览
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = { "/biz/return/reserve/get_reserve_order_info",
			"/biz/output/ylck/get_reserve_order_info" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getReserveInfo(String refer_receipt_code, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		// JSONObject obj = new JSONObject();
		Map<String, Object> reslutMap = new HashMap<String, Object>();
		try {
			reslutMap = returnService.getReserveOrderInfo(refer_receipt_code, cUser.getUsername());

			// String s
			// ="{\"refer_receipt_code\":\"XSDH111\",\"reserve_cost_obj_code\":\"成本对象\",\"reserve_cost_obj_name\":\"成本对象描述\",\"reserve_create_user\":\"创建人\",\"reserve_create_time\":\"创建日期\",\"item_list\":[{\"rid\":\"序号1\",\"reserve_rid\":\"预留行项目号\",\"mat_id\":\"物料id\",\"mat_code\":\"物料编码\",\"mat_name\":\"物料描述\",\"fty_id\":\"工厂id\",\"fty_code\":\"工厂代码\",\"fty_name\":\"工厂描述\",\"location_id\":\"库存地点id\",\"location_code\":\"库存地点\",\"location_name\":\"库存地点描述\",\"move_type_name\":\"移动类型\",\"demand_qty\":\"100\",\"unit_name\":\"单位\",\"batch_spec_list\":[{\"batch_spec_code\":\"ZSCRQ\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"1\",\"edit\":\"1\"}],\"batch_material_spec_list\":[{\"batch_spec_code\":\"production_time\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"1\",\"edit\":\"1\"},{\"batch_spec_code\":\"validity_time\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"2\",\"edit\":\"1\"},{\"batch_spec_code\":\"contract_code\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"1\",\"required\":\"0\",\"info\":[],\"display_index\":\"3\",\"edit\":\"0\"}]}]}";
			// obj = JSONObject.fromObject(s);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("获取预留退库单概览", e);
		}
		JSONObject object = UtilResult.getResultToJson(status, error_code, reslutMap);
		UtilJSONConvert.setNullToEmpty(object);
		return object;

	}

	/**
	 * 预留出库过账
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/output/ylck/save_reserve_order_sap" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveReserveOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();
		try {
			result = outPutService.saveOutputOrderData(jsonData, user, false,
					EnumReceiptType.STOCK_OUTPUT_RESERVE.getValue());
			Map<String, Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn, result.getInt("stock_output_id"), user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("预留出库过账 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result.get("stock_output_id"));
	}
}
