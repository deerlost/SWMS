package com.inossem.wms.portable.biz;

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
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicReceiver;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IOutPutService;
import com.inossem.wms.service.dic.IDicMoveTypeService;
import com.inossem.wms.service.dic.IDicReceiverService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 出库单Controller
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
	private ICommonService commonService;
	@Resource
	private IDicMoveTypeService moveTypeService;
	
	// MyTask 出库通用方法
	
	/**
	 * 非强制添加库存
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/llck/get_stock_data",
							  "/biz/output/xsck/get_stock_data",
							  "/biz/output/cgth/get_stock_data",
							  "/biz/output/qtck/get_stock_data",
							  "/biz/output/dbck/get_stock_data",
							  "/biz/output/ylck/get_stock_data",}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getStockData(@RequestBody JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("matId", UtilObject.getStringOrEmpty(json.getString("mat_id"))); // 物料编码
		//param.put("ftyId", UtilObject.getStringOrEmpty(json.getString("fty_id"))); // 工厂
		param.put("locationId", UtilObject.getStringOrEmpty(json.getString("location_id"))); // 库存地点
		param.put("specStock",UtilObject.getStringOrEmpty(json.get("spec_stock"))); //特殊库存
		param.put("batch",UtilObject.getStringOrEmpty(json.get("batch"))); //批次
		
		if(!"".equals(UtilObject.getStringOrEmpty(json.get("receipt_type")))){
			param.put("receiptType", UtilObject.getStringOrEmpty(json.get("receipt_type")));
		}
		
		List<Map<String, Object>> result = null;

		// 没有数量，则调用查询所有的接口
		result = outPutService.getStockData(param);

		return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), result);
	}
	
	/**
	 * 强制添加库存
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/llck/get_fcous_stock_data",
							  "/biz/output/xsck/get_fcous_stock_data",
							  "/biz/output/cgth/get_fcous_stock_data",
							  "/biz/output/qtck/get_fcous_stock_data",
							  "/biz/output/dbck/get_fcous_stock_data",
							  "/biz/output/ylck/get_fcous_stock_data",}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getFcousStockData(@RequestBody JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("matId", UtilObject.getStringOrEmpty(json.getString("mat_id"))); // 物料编码
		//param.put("ftyId", UtilObject.getStringOrEmpty(json.getString("fty_id"))); // 工厂
		param.put("locationId", UtilObject.getStringOrEmpty(json.getString("location_id"))); // 库存地点
		param.put("specStock",UtilObject.getStringOrEmpty(json.get("spec_stock"))); //特殊库存
		
		if(!"".equals(UtilObject.getStringOrEmpty(json.get("receipt_type")))){
			param.put("receiptType", UtilObject.getStringOrEmpty(json.get("receipt_type")));
		}
		
		List<Map<String, Object>> result = null;

		param.put("outQty", UtilObject.getStringOrEmpty(json.get("out_qty")));
		result = outPutService.getStockDataCommend(param);

		return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), result);
	}
	
	// MyTask 领料出库相关内容

	/**
	 * 获取领料出库列表
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/output/llck/get_matreq_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatReqList(String mat_req_code,String create_user, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> list = null;
		
		Map<String, Object> param = new HashMap<String,Object>();
		
		param.put("matReqCode", mat_req_code); // 关键字
		param.put("createUser", create_user);
		param.put("chooseType","0");
		param.put("corpId", cUser.getCorpId());
		param.put("locationId", cUser.getLocationList());
		
		try {
			list = outPutService.getMatReqList(param);
			
		} catch (Exception e) {
			logger.error("领料出库列表页查询 --", e);
			status=false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		
		return UtilResult.getResultToJson(status,error_code, list);
	}
	
	/**
	 * 根据领料单号获取领料单概览
	 * @param mat_req_code
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/llck/get_matreq_view"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getMatReqViewByid(String mat_req_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status=true;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("matReqId", mat_req_id);
		param.put("locationId", cUser.getLocationList());
		
		Map<String, Object> reslutMap = null;
		try {
			reslutMap = outPutService.getMatReqViewById(param);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> itemList = (List<Map<String, Object>>) reslutMap.get("item_list");
			reslutMap.put("move_type_code", itemList.get(0).get("move_type_code"));
			reslutMap.put("move_type_name", itemList.get(0).get("move_type_name"));
			reslutMap.put("cost_obj_code", itemList.get(0).get("cost_obj_code"));
			reslutMap.put("cost_obj_name", itemList.get(0).get("cost_obj_name"));
			
		} catch (Exception e) {
			logger.error("领料单概览页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		

		return UtilResult.getResultToMap(status,error_code, reslutMap);
	}
	
	/**
	 * 领料出库过账
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/llck/save_mr_order_sap"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveMROutOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.mrOrderPost(jsonData, user);
	}
	@RequestMapping(value = { "/biz/output/llck/save_mr_order_sap"}, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object updateMROutOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.mrOrderPost(jsonData, user);
	}
	private JSONObject mrOrderPost(JSONObject jsonData, CurrentUser user){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String msg = "";
		boolean status =true;
		
		JSONObject result = new JSONObject();
		
		try {
			result = outPutService.saveOutputOrderData(jsonData, user, true, EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue());
			Map<String,Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn,result.getInt("stock_output_id"), user.getUserId());

			
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
		
		return UtilResult.getResultToJson(status,error_code, msg, result.get("stock_output_id"));
	}
	
	// MyTask 销售出库
	/**
	 * 从SAP获取销售订单(概要)
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/xsck/get_sales_list"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getSalesOrderList(String refer_receipt_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> list = null;
		
		if (!"".equals(refer_receipt_code)) {
			while (refer_receipt_code.length() < 10) { // 不够十位数字，自动补0
				refer_receipt_code = "0" + refer_receipt_code;
			}

			try {
				list = outPutService.getSalesOrderList(refer_receipt_code,user.getUserId());
			} catch (Exception e) {
				logger.error("获取销售订单 --", e);
				status=false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		} else {
			logger.error("获取销售订单 --", "请输入查询条件");
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		}

		return UtilResult.getResultToMap(status,error_code, list);
		
	}
	
	/**
	 * 从SAP获取销售订单(详情)
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/xsck/get_sales_view"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getSalesViewBySap(String refer_receipt_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status =true;
		Map<String,Object> body = null;
		
		if (!"".equals(refer_receipt_code)) {
			while (refer_receipt_code.length() < 10) { // 不够十位数字，自动补0
				refer_receipt_code = "0" + refer_receipt_code;
			}

			try {
				body = outPutService.getSalesViewBySap(refer_receipt_code,user);
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

		return UtilResult.getResultToMap(status,error_code, body);
		
	}
	
	/**
	 * 销售出库过账新增
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/xsck/save_sales_order_sap"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveSalesOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.salesOrderPost(jsonData, user);
	}
	/**
	 * 销售出库过账修改
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/xsck/save_sales_order_sap"}, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateSalesOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.salesOrderPost(jsonData, user);
	}
	
	private JSONObject salesOrderPost(JSONObject jsonData, CurrentUser user){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg="";
		JSONObject result = new JSONObject();
		
		try {
			result = outPutService.saveOutputOrderData(jsonData, user, true, EnumReceiptType.STOCK_OUTPUT_SALE.getValue());
			Map<String,Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn,result.getInt("stock_output_id"), user.getUserId());

			
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
		
		return UtilResult.getResultToJson(status,error_code, msg, result.get("stock_output_id"));
	}
	
	// MyTask 采购退货
	/**
	 * 从SAP获取采购订单(概要)
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/cgth/get_purchase_list"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getPurchaseOrderList(String refer_receipt_code,String contract_code,String supplier_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> list = null;
		
		if (!"".equals(refer_receipt_code)) {
			while (refer_receipt_code.length() < 10) { // 不够十位数字，自动补0
				refer_receipt_code = "0" + refer_receipt_code;
			}
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("purchaseOrderCode", refer_receipt_code);
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

		return UtilResult.getResultToMap(status,error_code, list);
		
	}
	
	/**
	 * 从SAP获取采购订单(详情)
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/cgth/get_purchase_view"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getPurchaseViewBySap(String refer_receipt_code,String contract_code,String supplier_code, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String,Object> body = null;
		
		if (!"".equals(refer_receipt_code)) {
			while (refer_receipt_code.length() < 10) { // 不够十位数字，自动补0
				refer_receipt_code = "0" + refer_receipt_code;
			}
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("purchaseOrderCode", refer_receipt_code);
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

		return UtilResult.getResultToMap(status,error_code,body);
		
	}
	
	/**
	 * 采购退货过账新增
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/cgth/save_purchase_order_sap"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject savePurchaseOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.purchaseOrderPost(jsonData, user);
	}
	/**
	 * 采购退货过账修改
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/cgth/save_purchase_order_sap"}, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updatePurchaseOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.purchaseOrderPost(jsonData, user);
	}
	
	private JSONObject purchaseOrderPost(JSONObject jsonData, CurrentUser user){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();
		
		try {
			result = outPutService.saveOutputOrderData(jsonData, user, true, EnumReceiptType.STOCK_OUTPUT_PURCHASE_RETURN.getValue());
			Map<String,Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn,result.getInt("stock_output_id"), user.getUserId());

			
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
		
		return UtilResult.getResultToJson(status,error_code, msg, result.get("stock_output_id"));
	}
	// MyTask 其他出库
	
	/**
	 * 获取基础信息
	 */
	@RequestMapping(value = "/biz/output/qtck/get_baseinfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listAllDicReceiver(CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		JSONObject body = new JSONObject();
		
		try {
			List<DicReceiver> rList = dicReceiverService.listAllDicReceiver();
			JSONArray receiverList = new JSONArray();
			
			for (DicReceiver receiver : rList) {
				JSONObject receiverJSon = new JSONObject();
				receiverJSon.put("id", receiver.getId());// 接收方表主键
				receiverJSon.put("receive_code", receiver.getReceiveCode());// 接收方代码
				receiverJSon.put("receive_name", receiver.getReceiveName());// 接收方描述
				receiverList.add(receiverJSon);
			}
			
			JSONArray fty_list = commonService.listFtyLocationForUser(user.getUserId(),"");
			
			List<DicMoveType> locationList = moveTypeService.getMoveTypeByReceiptType(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue());
			JSONArray moveTypeAry = new JSONArray();
			for (DicMoveType moveType : locationList) {
				JSONObject moveTypeObj = new JSONObject();
				moveTypeObj.put("move_type_id", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeId()));
				moveTypeObj.put("move_type_code", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeCode()));
				moveTypeObj.put("move_type_name", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeName()));
				moveTypeObj.put("spec_stock", UtilString.getStringOrEmptyForObject(moveType.getSpecStock()));
				moveTypeAry.add(moveTypeObj);
			}
			
			body.put("fty_list", fty_list);
			body.put("receiver_list", receiverList);
			body.put("move_list", moveTypeAry);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false ;
			logger.error("获取接收方 --", e);
		}
		return UtilResult.getResultToJson(status,errorCode, 1, 50, 50, body);
	}
	
	
	/**
	 * 其他出库过账
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/qtck/save_other_order_sap"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveOtherOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.otherOrderPost(jsonData, user);
	}
	@RequestMapping(value = { "/biz/output/qtck/save_other_order_sap"}, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object updateOtherOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.otherOrderPost(jsonData, user);
	}
	private JSONObject otherOrderPost(JSONObject jsonData, CurrentUser user){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status=true;
		String msg="";
		JSONObject result = new JSONObject();
		
		try {
			result = outPutService.saveOutputOrderData(jsonData, user, true, EnumReceiptType.STOCK_OUTPUT_OTHER.getValue());
			Map<String,Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn,result.getInt("stock_output_id"), user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("其他出库过账 --", e);
			status=false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		
		return UtilResult.getResultToJson(status,error_code,msg, result.get("stock_output_id"));
	}
	
	// MyTask 调拨出库
	/**
	 * 查询前置单据
	 * @date 2017年12月4日
	 * @author 高海涛
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/biz/output/dbck/get_allocate_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAllocateList(String condition) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("keyword",condition); // 关键字

		List<Map<String, Object>> list = null;
		
		try {
			list = outPutService.getAllocateList(param);
		} catch (Exception e) {
			logger.error("获取调拨前置单据 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status,error_code, list);	
	}
	
	/**
	 * 获取前置单据明细
	 * @date 2017年12月4日
	 * @author 高海涛
	 * @param allotId
	 * @param deliveryId
	 * @return
	 */
	@RequestMapping(value = "/biz/output/dbck/get_allocate_by_id", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAllocateById(String allocate_id,String allocate_delivery_id) {
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
		
		return UtilResult.getResultToJson(status,error_code, body);
	}
	
	/**
	 * 调拨出库过账
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/dbck/save_allocate_order_sap"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveAllocateOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.allocatePost(jsonData,user);
	}
	@RequestMapping(value = { "/biz/output/dbck/save_allocate_order_sap"}, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object updateAllocateOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.allocatePost(jsonData,user);
	}
	private JSONObject allocatePost(JSONObject jsonData, CurrentUser user){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg="";
		JSONObject result = new JSONObject();
		
		try {
			result = outPutService.saveOutputOrderData(jsonData, user, true, EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue());
			Map<String,Object> sapReturn = outPutService.saveOrderToSap(result, user.getUserId());
			msg = outPutService.saveOrderToFinish(sapReturn,result.getInt("stock_output_id"), user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("调拨出库过账 --", e);
			status=false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		
		return UtilResult.getResultToJson(status,error_code, msg, result.get("stock_output_id"));
	}
	
	// TODO 预留单
	
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
			result = outPutService.saveOutputOrderData(jsonData, user, true,
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
