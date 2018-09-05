package com.inossem.wms.portable.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterialSpec;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.biz.BizSaleOrderHead;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IReturnService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class ReturnController {

	private static final Logger logger = LoggerFactory.getLogger(ReturnController.class);

	@Autowired
	private IReturnService returnService;

	/**
	 * 创建退库单--根据出库单或领料单查出库单列表
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/matreq/get_output_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOutputList(String condition) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray body = new JSONArray();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("condition", condition.trim());
			list = returnService.getOutputListByOrderCode(paramMap);
			if (list != null && list.size() > 0) {
				JSONObject obj = new JSONObject();
				for (Map map : list) {
					obj = new JSONObject();
					obj.put("stock_output_id", map.get("stockOutputId")); // 出库单id
					obj.put("stock_output_code", map.get("stockOutputCode")); // 出库单号
					obj.put("mat_req_code", map.get("matReqCode")); // 领料单号
					obj.put("mat_req_fty_name", map.get("ftyName")); // 领料工厂
					obj.put("mat_req_mat_type_name", map.get("matTypeName")); // 物料类型
					obj.put("create_time", sdf.format(map.get("createTime"))); // 出库时间
					body.add(obj);
				}
			}
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("出库单列表", e);
		}

		// return UtilResult.getResultToJson(status,error_code, body);
		JSONObject object = UtilResult.getResultToJson(status, error_code, body);
		UtilJSONConvert.setNullToEmpty(object);
		return object;
	}

	/**
	 * 根据出库单号获取领料出库单概览
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/matreq/get_output_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOutputInfo(String stock_output_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		Map<String, Object> param = new HashMap<String, Object>();
		int stockOutputId = Integer.parseInt(stock_output_id);
		param.put("stockOutputId", stockOutputId);
		param.put("locationId", cUser.getLocationList());

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> reslutMap = null;
		JSONObject obj = new JSONObject();
		try {
			reslutMap = returnService.getOutputInfoByOutputId(param);
			reslutMap.put("return_create_user", cUser.getUserName());
			// String s =
			// "{\"stock_output_code\":\"3200000075\",\"apply_fty_name\":\"伊泰股份工厂\",\"return_create_user\":\"佳佳\",\"mat_req_code\":\"3700000001\",\"user_name\":\"佳佳\",\"create_time\":\"2018-01-22\",\"mat_req_fty_name\":\"伊泰股份工厂\",\"mat_type_name\":\"备件、材料（除支护矿务工程）\",\"biz_type_name\":\"其他\",\"receive_name\":\"大地二号井\",\"internal_order_code\":\"\",\"dept_name\":\"\",\"use_dept_name\":\"工会2\",\"is_building_project\":\"\",\"remark\":\"备注备注1\",\"stock_output_id\":\"92\",\"refer_receipt_code\":\"1\",\"receive_code\":\"1010\",\"receipt_type\":\"41\",\"user_list\":[{\"role_name\":\"共通权限\",\"user_id\":\"a123457\",\"role_id\":0,\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"user_name\":\"佳佳\",\"phone\":\"13720092677\",\"org_name\":\"煤炭生产事业部\"}],\"item_list\":[{\"rid\":\"1\",\"stock_output_rid\":\"1\",\"mat_id\":\"1\",\"mat_code\":\"60000001\",\"mat_name\":\"热轧碳素结构钢板/δ3*1500*6000/Q235B\",\"move_type_id\":\"260094\",\"move_type_code\":\"101\",\"move_type_name\":\"采购订单收货\",\"unit_id\":\"63\",\"unit_name\":\"KG\",\"location_id\":\"20000004\",\"location_code\":\"0004\",\"location_name\":\"大地中心供应站\",\"refer_price\":0,\"cost_obj_code\":\"2000010006\",\"cost_obj_name\":\"财务部机关\",\"demand_qty\":10,\"send_qty\":3,\"reserve_id\":\"0000018179\",\"reserve_rid\":\"0001\",\"purchase_order_code\":\"4600029274\",\"purchase_order_rid\":\"00010\",\"output_qty\":10,\"have_return_qty\":1,\"can_return_qty\":9,\"decimal_place\":\"3\",\"batch_list\":[{\"batch\":\"1000002713\",\"batch_spec_list\":[{\"batch_spec_name\":\"名称1\",\"batch_spec_code\":\"值1\"},{\"batch_spec_name\":\"名称2\",\"batch_spec_code\":\"值2\"}]},{\"batch\":\"1000002714\",\"batch_spec_list\":[{\"batch_spec_name\":\"名称3\",\"batch_spec_code\":\"值3\"},{\"batch_spec_name\":\"名称4\",\"batch_spec_code\":\"值4\"}]}],\"fty_id\":\"2000\",\"fty_name\":\"伊泰股份工厂\",\"fty_code\":\"2000\",\"is_focus_batch\":0,\"item_id\":\"79\",\"remark\":\"\",\"write_off\":0}]}";
			// obj = JSONObject.fromObject(s);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("获得领料出库单详情 ", e);
		}

		// return UtilResult.getResultToJson(status, error_code, reslutMap);
		JSONObject object = UtilResult.getResultToJson(status, error_code, reslutMap);
		UtilJSONConvert.setNullToEmpty(object);
		return object;
	}

	/**
	 * 领料退库过账
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/matreq/posting", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject matReqPosting(@RequestBody JSONObject json, CurrentUser cUser) {
		// int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		// boolean status = false;
		// JSONObject obj = new JSONObject();
		// try {
		//
		// //String
		// s="{\"stock_output_id\":\"出库单号\",\"return_id\":\"退库单号\",\"doc_time\":\"2017-07-17\",\"posting_date\":\"2017-07-17\",\"remark\":\"备注信息\",\"user_list\":[{\"user_id\":\"111\",\"role_id\":\"14\"},{\"user_id\":\"222\",\"role_id\":\"14\"}],\"item_list\":[{\"rid\":\"序号1\",\"stock_output_rid\":\"出库单行项目1\",\"return_qty\":\"11\",\"batch\":\"111\"},{\"rid\":\"序号2\",\"return_rid\":\"行项目2\",\"return_qty\":\"22\",\"batch\":\"222\"}]}";
		// obj = JSONObject.fromObject(s);
		// status = true;
		// error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		// } catch (Exception e) {
		// logger.error("领料退库过账", e);
		// }
		//
		// return UtilResult.getResultToJson(status,error_code,obj);
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject obj = new JSONObject();
		JSONObject returnObj = new JSONObject();
		json.put("cUserId", cUser.getUserId());
		json.put("locationId", cUser.getLocationList());
		json.put("createName", cUser.getUserName());
		json.put("status", 10);

		try {
			returnObj = returnService.matreqSaveAndPost(json);
			errorCode = returnObj.getInt("errorCode");
			errorString = returnObj.getString("errorString");
			if (errorCode == EnumErrorCode.ERROR_CODE_SUCESS.getValue()) {
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
				status = true;
			} else {
				obj = (JSONObject) returnObj.get("body");
				status = false;
			}

			// String
			// s="{\"stock_output_id\":\"出库单号\",\"return_id\":\"退库单号\",\"doc_time\":\"2017-07-17\",\"posting_date\":\"2017-07-17\",\"remark\":\"备注信息\",\"user_list\":[{\"user_id\":\"111\",\"role_id\":\"14\"},{\"user_id\":\"222\",\"role_id\":\"14\"}],\"item_list\":[{\"rid\":\"序号1\",\"return_rid\":\"行项目1\",\"return_qty\":\"11\",\"batch\":\"111\"},{\"rid\":\"序号2\",\"return_rid\":\"行项目2\",\"return_qty\":\"22\",\"batch\":\"222\"}]}";
			// obj = JSONObject.fromObject(s);

		} catch (Exception e) {
			logger.error("领料退库暂存或过账", e);
			status = false;
		}

		return UtilResult.getResultToJson(status, errorCode, errorString, obj);
	}

	/**
	 * 获取销售订单列表
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/sale/get_sale_order_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getSaleOrderList(String refer_receipt_code, String preorder_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject obj = new JSONObject();
		List<BizSaleOrderHead> list = new ArrayList<BizSaleOrderHead>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("referReceiptCode", refer_receipt_code);
			map.put("preorderId", preorder_id);

			list = returnService.getSaleOrderList(map);

			// String
			// s="[{\"refer_receipt_code\":\"XSDH111\",\"preorder_id\":\"HTBH111\",\"supplier\":\"南京英诺森软件科技有限公司\",\"customer_name\":\"汪帮宇\",\"create_time\":\"2018-01-01\"},{\"refer_receipt_code\":\"XSDH222\",\"preorder_id\":\"HTBH222\",\"supplier\":\"北京京东软件科技有限公司\",\"customer_name\":\"汪帮宇\",\"create_time\":\"2018-01-01\"}]";
			// obj = JSONObject.fromObject(s);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("获取销售订单列表", e);
		}

		// return UtilResult.getResultToJson(status,error_code,list);
		JSONObject object = UtilResult.getResultToJson(status, error_code, list);
		UtilJSONConvert.setNullToEmpty(object);
		return object;
	}

	/**
	 * 获取销售订单概览
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/sale/get_sale_order_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getSaleOrderInfo(String refer_receipt_code, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject obj = new JSONObject();
		Map<String, Object> reslutMap = new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("referReceiptCode", refer_receipt_code);
			param.put("preorderId", "");

			reslutMap = returnService.getSaleOrderInfo(param);

			// String s =
			// "{\"refer_receipt_code\":\"XSDH111\",\"preorder_id\":\"HTBH111\",\"customer_name\":\"客户名称\",\"order_type_name\":\"销售订单类型\",\"sale_org_name\":\"销售组织\",\"sale_group_name\":\"销售组\",\"user_list\":[{\"role_name\":\"共通权限\",\"user_id\":\"a123457\",\"role_id\":0,\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"user_name\":\"佳佳\",\"phone\":\"13720092677\",\"org_name\":\"煤炭生产事业部\"}],\"item_list\":[{\"rid\":\"序号1\",\"sale_rid\":\"销售单行项目\",\"mat_id\":\"物料id\",\"mat_code\":\"物料编码\",\"mat_name\":\"物料描述\",\"fty_id\":\"工厂id\",\"fty_code\":\"工厂代码\",\"fty_name\":\"工厂描述\",\"location_code\":\"库存地点\",\"location_name\":\"库存地点描述\",\"order_qty\":\"100\",\"have_return_qty\":\"20\",\"unit_name\":\"单位\",\"batch_spec_list\":[{\"batch_spec_code\":\"ZSCRQ\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"1\",\"edit\":\"1\"}],\"batch_material_spec_list\":[{\"batch_spec_code\":\"production_time\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"1\",\"edit\":\"1\"},{\"batch_spec_code\":\"validity_time\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"2\",\"edit\":\"1\"},{\"batch_spec_code\":\"contract_code\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"1\",\"required\":\"0\",\"info\":[],\"display_index\":\"3\",\"edit\":\"0\"}]}]}";
			// obj = JSONObject.fromObject(s);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("获取销售退库单概览", e);
		}
		JSONObject object = UtilResult.getResultToJson(status, error_code, reslutMap);
		UtilJSONConvert.setNullToEmpty(object);
		return object;
	}

	/**
	 * 销售退库过账
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/sale/posting", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject salePosting(@RequestBody JSONObject json, CurrentUser cUser) {
		// int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		// boolean status = false;
		// JSONObject obj = new JSONObject();
		// try {
		// //String
		// s="{\"stock_output_id\":\"出库单号\",\"return_id\":\"退库单号\",\"doc_time\":\"2017-07-17\",\"posting_date\":\"2017-07-17\",\"remark\":\"备注信息\",\"user_list\":[{\"user_id\":\"111\",\"role_id\":\"14\"},{\"user_id\":\"222\",\"role_id\":\"14\"}],\"item_list\":[{\"rid\":\"序号1\",\"return_rid\":\"行项目1\",\"return_qty\":\"11\",\"batch\":\"111\"},{\"rid\":\"序号2\",\"return_rid\":\"行项目2\",\"return_qty\":\"22\",\"batch\":\"222\"}]}";
		// //obj = JSONObject.fromObject(s);
		// status = true;
		// error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		// } catch (Exception e) {
		// logger.error("销售退库过账", e);
		// }
		//
		// return UtilResult.getResultToJson(status,error_code,obj);
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject obj = new JSONObject();
		JSONObject returnObj = new JSONObject();
		try {
			json.put("cUserId", cUser.getUserId());
			returnObj = returnService.salePost(json);

			errorCode = returnObj.getInt("errorCode");
			errorString = returnObj.getString("errorString");
			if (errorCode == EnumErrorCode.ERROR_CODE_SUCESS.getValue()) {
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			} else {
				obj = (JSONObject) returnObj.get("body");
				status = false;
			}

			// String
			// s="{\"stock_output_id\":\"出库单号\",\"return_id\":\"退库单号\",\"doc_time\":\"2017-07-17\",\"posting_date\":\"2017-07-17\",\"remark\":\"备注信息\",\"user_list\":[{\"user_id\":\"111\",\"role_id\":\"14\"},{\"user_id\":\"222\",\"role_id\":\"14\"}],\"item_list\":[{\"rid\":\"序号1\",\"return_rid\":\"行项目1\",\"return_qty\":\"11\",\"batch\":\"111\"},{\"rid\":\"序号2\",\"return_rid\":\"行项目2\",\"return_qty\":\"22\",\"batch\":\"222\"}]}";
			// obj = JSONObject.fromObject(s);

		} catch (Exception e) {
			logger.error("销售退库过账", e);
		}

		return UtilResult.getResultToJson(status, errorCode, errorString, obj);
	}

	/**
	 * 获取预留单概览
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/reserve/get_reserve_order_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getReserveInfo(String refer_receipt_code, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
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
	 * 预留退库过账
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = "/biz/return/reserve/posting", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject reservePosting(@RequestBody JSONObject json, CurrentUser cUser) {
		// int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		// boolean status = false;
		// JSONObject obj = new JSONObject();
		// try {
		// //String
		// s="{\"stock_output_id\":\"出库单号\",\"return_id\":\"退库单号\",\"doc_time\":\"2017-07-17\",\"posting_date\":\"2017-07-17\",\"remark\":\"备注信息\",\"user_list\":[{\"user_id\":\"111\",\"role_id\":\"14\"},{\"user_id\":\"222\",\"role_id\":\"14\"}],\"item_list\":[{\"rid\":\"序号1\",\"return_rid\":\"行项目1\",\"return_qty\":\"11\",\"batch\":\"111\"},{\"rid\":\"序号2\",\"return_rid\":\"行项目2\",\"return_qty\":\"22\",\"batch\":\"222\"}]}";
		// //obj = JSONObject.fromObject(s);
		// status = true;
		// error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		// } catch (Exception e) {
		// logger.error("预留退库过账", e);
		// }
		//
		// return UtilResult.getResultToJson(status,error_code,obj);
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject obj = new JSONObject();
		JSONObject returnObj = new JSONObject();
		try {
			json.put("cUserId", cUser.getUserId());
			returnObj = returnService.reservePost(json);

			errorCode = returnObj.getInt("errorCode");
			errorString = returnObj.getString("errorString");
			if (errorCode == EnumErrorCode.ERROR_CODE_SUCESS.getValue()) {
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			} else {
				obj = (JSONObject) returnObj.get("body");
				status = false;
			}

			// String
			// s="{\"stock_output_id\":\"出库单号\",\"return_id\":\"退库单号\",\"doc_time\":\"2017-07-17\",\"posting_date\":\"2017-07-17\",\"remark\":\"备注信息\",\"user_list\":[{\"user_id\":\"111\",\"role_id\":\"14\"},{\"user_id\":\"222\",\"role_id\":\"14\"}],\"item_list\":[{\"rid\":\"序号1\",\"return_rid\":\"行项目1\",\"return_qty\":\"11\",\"batch\":\"111\"},{\"rid\":\"序号2\",\"return_rid\":\"行项目2\",\"return_qty\":\"22\",\"batch\":\"222\"}]}";
			// obj = JSONObject.fromObject(s);

		} catch (Exception e) {
			logger.error("预留退库过账", e);
		}

		return UtilResult.getResultToJson(status, errorCode, errorString, obj);
	}

	// 获取最近150条批次信息
	@RequestMapping(value = { "/biz/return/sale/getRecentBatchList",
			"/biz/return/reserve/getRecentBatchList" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getRecentBatchList(String mat_id, String fty_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONArray obj = new JSONArray();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> batchList = new ArrayList<Map<String, Object>>();
		JSONArray resultAry = new JSONArray();
		try {
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Integer ftyId = Integer.parseInt(fty_id);
			Integer matId = Integer.parseInt(mat_id);
			paramMap.put("matId", matId);
			paramMap.put("ftyId", ftyId);
			List<BatchMaterialSpec> list = returnService.getRecentBatchList(paramMap);
			for (BatchMaterialSpec batchMaterialSpec : list) {
				Map<String, Object> innerMap = new HashMap<String, Object>();
				innerMap.put("batch", batchMaterialSpec.getBatch());
				innerMap.put("mat_id", matId);
				itemList.add(innerMap);
			}

			returnList = returnService.addBatchMaterialSpecForItemList(ftyId, itemList);
			for (Map map : returnList) {
				batchList = (List<Map<String, Object>>) map.get("batch_list");
			}

			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

			// String s =
			// "[{\"batch\":\"1000002713\",\"supplier_name\":\"1000002713\",\"create_time\":\"1000002713\",\"batch_spec_list\":[{\"batch_spec_name\":\"名称1\",\"batch_spec_value\":\"值1\"},{\"batch_spec_name\":\"名称2\",\"batch_spec_value\":\"值2\"}]},{\"batch\":\"1000002714\",\"supplier_name\":\"1000002713\",\"create_time\":\"1000002713\",\"batch_spec_list\":[{\"batch_spec_name\":\"名称3\",\"batch_spec_value\":\"值3\"},{\"batch_spec_name\":\"名称4\",\"batch_spec_value\":\"值4\"}]}]";
			// String s =
			// "[{\"create_time\":\"\",\"batch\":1000000083,\"batch_spec_list\":[{\"batch_spec_name\":\"原厂设备编号\",\"batch_spec_value\":\"1\"},{\"batch_spec_name\":\"考核完成标识\",\"batch_spec_value\":\"1\"}],\"supplier_name\":\"内蒙古双达机电设备有限公司\"},{\"create_time\":\"\",\"batch\":1000000071,\"batch_spec_list\":[{\"batch_spec_name\":\"原厂设备编号\",\"batch_spec_value\":\"1\"},{\"batch_spec_name\":\"考核完成标识\",\"batch_spec_value\":\"1\"}],\"supplier_name\":\"内蒙古双达机电设备有限公司\"}]";
			// obj = JSONArray.fromObject(s);
		} catch (Exception e) {
			logger.error("获取最近一段时间的批次", e);
		}

		// JSONObject object = UtilResult.getResultToJson(status, error_code,
		// resultAry);
		// UtilJSONConvert.setNullToEmpty(object);
		// return UtilResult.getResultToJson(status, error_code, batchList);
		JSONObject object = UtilResult.getResultToJson(status, error_code, batchList);
		UtilJSONConvert.setNullToEmpty(object);
		return object;
	}

}
