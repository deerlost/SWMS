package com.inossem.wms.web.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.google.gson.JsonObject;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizDeliveryOrderHead;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumStockOutputReturnStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.model.vo.BizStockOutputReturnHeadVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IOutPutCWService;
import com.inossem.wms.service.biz.IPkgOperationService;
import com.inossem.wms.service.biz.IReturnService;
import com.inossem.wms.service.dic.IPackageTypeService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/return")
public class ReturnControllerCW {
	private static final Logger logger = LoggerFactory.getLogger(ReturnControllerCW.class);

	@Autowired
	private IReturnService returnService;
	
	@Autowired
	private IPackageTypeService packageTypeService;
	
	@Autowired
	private IPkgOperationService pkgService;

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
		boolean sortAscend = true;
		String sortColumn = "";
		if (json.containsKey("total")) {
			totalCount = json.getInt("total");
		}
		if (json.containsKey("sort_ascend")) {
			sortAscend = json.getBoolean("sort_ascend");
		}
		if (json.containsKey("sort_column")) {
			sortColumn = json.getString("sort_column");
		}
		param.put("paging", true);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		param.put("sortAscend", sortAscend);
		param.put("sortColumn", sortColumn);

		return param;
	}
	
	
	/**
	 * 获取产品线装置班次
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/sale/get_base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getBaseInfo(String condition, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject body = new JSONObject();
		try {
			Map<String, Object> productLineMap=pkgService.selectPkgClassLineInstallationList(cUser.getUserId());
			body.put("product_line_list", productLineMap.get("productLineList"));
			body.put("class_type_list",  productLineMap.get("classTypeList"));
			body.put("class_type_id",  productLineMap.get("class_type_id"));
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		}  catch (WMSException e) {
			error_code = e.getErrorCode();
			error_string = e.getMessage();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = "获取产品线装置班次";
			logger.error("获取产品线装置班次", e);
		}
		return UtilResult.getResultToJson(status, error_code, error_string, body);
		// return body;
	}
	
	
	

	/**
	 * 获取销售退库单列表
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/sale/get_return_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getReturnList(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;
		boolean sortAscend = true;
		String sortColumn = "";
		List<BizStockOutputReturnHeadVo> list = new ArrayList<BizStockOutputReturnHeadVo>();
		JSONArray body = new JSONArray();
		JSONObject obj;
		try {
			String selectStatus = json.getString("status");// 状态 0-未退库 10-已退库 20-已冲销 2 已作业 3已提交

			if (selectStatus != null && selectStatus.length() > 0) {

				pageIndex = json.getInt("page_index");
				pageSize = json.getInt("page_size");
				int totalCount = -1;
				if (json.containsKey("total")) {
					totalCount = json.getInt("total");
				}
				if (json.containsKey("sort_ascend")) {
					sortAscend = json.getBoolean("sort_ascend");
				}
				if (json.containsKey("sort_column")) {
					sortColumn = json.getString("sort_column");
				}

				String condition = json.getString("condition").trim();
				String statusStr = "(" + selectStatus + ")";

				BizStockOutputReturnHeadVo paramVo = new BizStockOutputReturnHeadVo();
				paramVo.setReceiptType(EnumReceiptType.STOCK_RETURN_SALE.getValue());// 62-销售退库
				paramVo.setCondition(condition);
				paramVo.setStatusStr(statusStr);

				paramVo.setPaging(true);
				paramVo.setPageIndex(pageIndex);
				paramVo.setPageSize(pageSize);
				paramVo.setTotalCount(totalCount);
				paramVo.setSortAscend(sortAscend);
				paramVo.setSortColumn(sortColumn);

				list = returnService.getReturnList(paramVo);

				if (list != null && list.size() > 0) {
					total = list.get(0).getTotalCount();
					for (BizStockOutputReturnHeadVo vo : list) {
						obj = new JSONObject();
						obj.put("return_id", vo.getReturnId()); // 退库单id
						obj.put("return_code", vo.getReturnCode()); // 销售退库单号
						obj.put("refer_receipt_code", vo.getReferReceiptCode()); // 交货单号
						obj.put("customer_code", vo.getCustomerCode());
						obj.put("customer_name", vo.getCustomerName()); // 客户名称
						obj.put("create_user", vo.getUserName()); // 创建人
						obj.put("create_time", UtilString.getLongStringForDate(vo.getCreateTime())); // 创建日期
						obj.put("status", vo.getStatus()); // 退库状态
						int statusValue = Integer.parseInt(vo.getStatus().toString());
						String statusName = EnumStockOutputReturnStatus.getNameByValue((byte) statusValue);
						obj.put("status_name", statusName); // 状态名称
						obj.put("mat_doc_code", UtilString.getStringOrEmptyForObject(vo.getMatDocCode())); // erp凭证
						obj.put("mes_doc_code", UtilString.getStringOrEmptyForObject(vo.getMesDocCode())); // mes凭证
						body.add(obj);
					}
				}
			}
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			error_string = e.getMessage();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("领料退库单列表查询", e);
		}

		return UtilResult.getResultToJson(status, error_code, error_string, pageIndex, pageSize, total, body);
	}

	// 交货单详情
	@RequestMapping(value = "/sale/get_delivery_order_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDeliveryOrderInfo(String condition, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject body = new JSONObject();
		BizDeliveryOrderHead head = new BizDeliveryOrderHead();
		// String s =
		// "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":20,\"total\":-1},\"body\":{\"class_type_list\":[{\"class_type_end\":\"16:00:00\",\"class_type_name\":\"白班\",\"class_name\":\"白班(08:00:00-16:00:00)\",\"class_type_id\":0,\"class_type_start\":\"08:00:00\"},{\"class_type_end\":\"00:00:00\",\"class_type_name\":\"中班\",\"class_name\":\"中班(16:00:00-00:00:00)\",\"class_type_id\":1,\"class_type_start\":\"16:00:00\"},{\"class_type_end\":\"08:00:00\",\"class_type_name\":\"夜班\",\"class_name\":\"夜班(00:00:00-08:00:00)\",\"class_type_id\":2,\"class_type_start\":\"00:00:00\"}],\"create_time\":\"2018-05-10\",\"create_user_id\":\"admin\",\"create_user_name\":\"admin\",\"customer_code\":\"2222\",\"customer_name\":\"国务院\",\"delivery_code\":\"0000001111\",\"delivery_id\":3,\"delivery_type\":\"交货单类型\",\"distribution_channels\":\"分销渠道\",\"item_list\":[{\"customer_mat_code\":\"60017822\",\"delivery_code\":\"0000001111\",\"delivery_qty\":60,\"delivery_rid\":\"1\",\"erp_batch\":\"11111\",\"erp_remark\":\"ERP备注\",\"fty_code\":\"2010\",\"fty_id\":4,\"fty_name\":\"销售工厂2\",\"have_shelves_qty\":0,\"item_id\":\"\",\"location_code\":\"0024\",\"location_id\":7,\"location_name\":\"凯达供应站\",\"mat_code\":\"60017822\",\"mat_id\":56274,\"mat_name\":\"换向开关/QC83-60\",\"refer_receipt_code\":\"444444\",\"refer_receipt_rid\":\"66666\",\"remark\":\"\",\"return_qty\":0,\"rid\":1,\"unit_code\":\"PC\",\"unit_id\":149,\"unit_name\":\"件\"},{\"customer_mat_code\":\"60017822\",\"delivery_code\":\"0000001111\",\"delivery_qty\":66,\"delivery_rid\":\"2\",\"erp_batch\":\"22222\",\"erp_remark\":\"ERP备注\",\"fty_code\":\"2010\",\"fty_id\":4,\"fty_name\":\"销售工厂2\",\"have_shelves_qty\":0,\"item_id\":\"\",\"location_code\":\"0024\",\"location_id\":7,\"location_name\":\"凯达供应站\",\"mat_code\":\"60017822\",\"mat_id\":56274,\"mat_name\":\"换向开关/QC83-60\",\"refer_receipt_code\":\"444444\",\"refer_receipt_rid\":\"99999\",\"remark\":\"\",\"return_qty\":0,\"rid\":2,\"unit_code\":\"PC\",\"unit_id\":149,\"unit_name\":\"件\"}],\"product_group\":\"32\",\"refer_purchase_code\":\"333332\",\"remark\":\"\",\"sale_document\":\"。。。\",\"sale_document_type\":\"内部销售\",\"sale_document_type_name\":\"类型描述\",\"sale_org_code\":\"销售组织\"}}";
		// body = JSONObject.fromObject(s);
		try {
			//校验交货单是否已退货
			boolean canUse = returnService.canUseForDelivery(condition);
			if (canUse) {
				Map<String, Object> map = new HashMap<>();
				map.put("condition", condition);
				map.put("cUserId", cUser.getUserId());
				map.put("cUserName", cUser.getUserName());
				head = returnService.getDeliveryOrderInfo(map);
				String saleDocumentType = head.getSaleDocumentType()==null?"":head.getSaleDocumentType();
				if (saleDocumentType.equals("ZCD2")||saleDocumentType.equals("ZM03")||saleDocumentType.equals("ZR02")||saleDocumentType.equals("ZR11")) {
					boolean flg = true;
				}else {
					head = new BizDeliveryOrderHead();
					throw new WMSException ("请检查交货单是否为退货交货单");
				}
			}
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		}  catch (WMSException e) {
			error_code = e.getErrorCode();
			error_string = e.getMessage();
			head = new BizDeliveryOrderHead();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {

			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = "获取交货单详情失败";
			head = new BizDeliveryOrderHead();
			logger.error("获取交货单详情", e);
		}
		return UtilResult.getResultToJson(status, error_code, error_string, head);
		// return body;
	}
	
	
	
	

	/**
	 * 1.1.3获取配货库存
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/sale/get_sale_output_position_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getSaleOutputPositionList(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		// String s =
		// "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":20,\"total\":-1},\"body\":[{\"rid\":\"1\",\"stock_id\":221,\"stock_sn_id\":1,\"stock_status\":\"1\",\"stock_status_name\":\"非限制库存\",\"product_batch\":\"11111\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC-20\",\"package_type_name\":\"PVC-20\",\"package_standard\":\"20\",\"package_standard_ch\":\"PVC-20\",\"output_qty\":\"48\",\"output_time\":\"2018-02-02\",\"qty\":\"0\"},{\"rid\":\"2\",\"stock_id\":222,\"stock_sn_id\":1,\"stock_status\":\"1\",\"stock_status_name\":\"非限制库存\",\"product_batch\":\"222222\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC-20\",\"package_type_name\":\"PVC-20\",\"package_standard\":\"20\",\"package_standard_ch\":\"PVC-20\",\"output_qty\":\"48\",\"output_time\":\"2018-02-02\",\"qty\":\"0\"}]}";
		// JSONObject body = JSONObject.fromObject(s);
		JSONArray returnAry = new JSONArray();
		JSONObject object = null;
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("erpbatch", json.get("erp_batch"));
			param.put("ftyId", json.get("fty_id"));
			param.put("matId", json.get("mat_id"));
			param.put("locationId", json.get("location_id"));
			//查42销售出库	
			param.put("receiptType", "("+EnumReceiptType.STOCK_OUTPUT_SALE.getValue()+")");
			param.put("referReceiptType", EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue());
			param.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
			List<Map<String, Object>> retrunList = returnService.getSaleOutputItemList(param);
			int i =1;
			List<DicPackageType> packageTypeList = packageTypeService.getPackageTypeAll();
			if (retrunList != null && retrunList.size() > 0) {
				for (Map<String, Object> map : retrunList) {
					object = new JSONObject();
					object.put("rid", i++);//
					object.put("stock_output_item_id", map.get("item_id"));//出库行项目itemId
					object.put("stock_task_position_item_id", map.get("item_position_id"));//下架作业单仓位主键
					object.put("stock_output_id", map.get("stock_output_id"));
					object.put("stock_output_rid", map.get("stock_output_rid"));
					object.put("stock_status_name", EnumStockType.STOCK_TYPE_ERP.getName());// 库存类型
					object.put("output_batch",  map.get("batch"));// 下架作业单中的出库批次
					object.put("erp_batch",  map.get("erp_batch"));// erp批次
					object.put("product_batch", map.get("production_batch"));// 生产批次
					object.put("quality_batch", map.get("quality_batch"));// 质检批次					
					object.put("package_type_id", map.get("package_type_id"));// 包装类型
					object.put("package_type_code", map.get("package_type_code"));//
					object.put("package_type_name", map.get("package_type_name"));//
					object.put("package_standard", map.get("package_standard"));// 包装物规格id
					object.put("package_standard_ch", map.get("package_standard_ch"));// 包装物规格code
					object.put("sn_used", map.get("sn_used"));// 1启用  2 不启用					
					object.put("output_qty", map.get("qty"));// 出库数量==下架数量
					object.put("create_time", map.get("create_time"));// 出库时间
					//object.put("package_type_list", packageTypeList);// 打包类型下拉					
					returnAry.add(object);
				}

			}

			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("交货单详情", e);
		}
		return UtilResult.getResultToJson(status, error_code, error_string, returnAry);
		
	}
	
	//获取包装类型下拉框
	@RequestMapping(value = "/sale/get_package_type_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getpackageTypeList() {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONArray returnAry = new JSONArray();
		JSONObject object = null;
		try {
			List<DicPackageType> packageTypeList = packageTypeService.getPackageTypeAll();
			if (packageTypeList != null && packageTypeList.size()>0) {
				for (DicPackageType dicPackageType : packageTypeList) {
					returnAry.add(dicPackageType);
				}
			}

			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("交货单详情", e);
		}
		return UtilResult.getResultToJson(status, error_code, error_string, returnAry);
		
	}
	
	

	private String getStockStatusName(int status) {
		String stockStatusName = "";
		if (status == 1) {
			stockStatusName = "非限制库存";
		} else if (status == 2) {
			stockStatusName = "在途库存";
		} else if (status == 3) {
			stockStatusName = "质量检验库存";
		} else if (status == 4) {
			stockStatusName = "冻结的库存";
		}
		return stockStatusName;
	}

	// 1.1.4销售退库单概览
	@RequestMapping(value = "/sale/get_return_order_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getReturnInfo(int return_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject body = new JSONObject();
		BizDeliveryOrderHead head = new BizDeliveryOrderHead();
		Map<String, Object> returnMap = new HashMap<>();
//		String s = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":20,\"total\":-1},\"body\":{\"return_id\":\"1\",\"return_code\":\"6000000059\",\"status\":\"0\",\"status_name\":\"未完成\",\"create_time\":\"2018-05-10\",\"create_user_id\":\"admin\",\"create_user_name\":\"admin\",\"customer_code\":\"2222\",\"customer_name\":\"国务院\",\"delivery_code\":\"0000001111\",\"delivery_id\":3,\"delivery_type\":\"交货单类型\",\"distribution_channels\":\"分销渠道\",\"product_group\":\"32\",\"refer_purchase_code\":\"333332\",\"sale_document\":\"。。。\",\"sale_document_type\":\"内部销售\",\"sale_document_type_name\":\"类型描述\",\"sale_org_code\":\"销售组织\",\"remark\":\"退库单备注\",\"class_name\":\"白班(08:00:00-16:00:00)\",\"item_list\":[{\"customer_mat_code\":\"60017822\",\"delivery_code\":\"0000001111\",\"delivery_qty\":60,\"delivery_rid\":\"1\",\"erp_batch\":\"11111\",\"fty_code\":\"2052\",\"fty_id\":33,\"fty_name\":\"凯达洗煤工厂\",\"have_shelves_qty\":0,\"item_id\":\"\",\"location_code\":\"0003\",\"location_id\":63,\"location_name\":\"备品备件库\",\"mat_code\":\"60017822\",\"mat_id\":56274,\"mat_name\":\"换向开关/QC83-60\",\"refer_receipt_code\":\"444444\",\"refer_receipt_rid\":\"66666\",\"remark\":\"haha\",\"erp_remark\":\"hah\",\"return_qty\":\"100\",\"unit_code\":\"PC\",\"unit_id\":149,\"unit_name\":\"件\",\"position_list\":[{\"rid\":\"1\",\"stock_id\":221,\"stock_sn_id\":1,\"stock_status\":\"1\",\"stock_status_name\":\"非限制库存\",\"product_batch\":\"H01222\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC-20\",\"package_type_name\":\"PVC-20\",\"package_standard\":\"20\",\"package_standard_ch\":\"PVC-20\",\"output_qty\":\"48\",\"output_time\":\"2018-02-02\",\"qty\":\"1\"}]},{\"customer_mat_code\":\"60017822\",\"delivery_code\":\"0000001111\",\"delivery_qty\":66,\"delivery_rid\":\"2\",\"erp_batch\":\"22222\",\"fty_code\":\"2052\",\"fty_id\":33,\"fty_name\":\"凯达洗煤工厂\",\"have_shelves_qty\":0,\"item_id\":\"\",\"location_code\":\"0003\",\"location_id\":63,\"location_name\":\"备品备件库\",\"mat_code\":\"60017822\",\"mat_id\":56274,\"mat_name\":\"换向开关/QC83-60\",\"refer_receipt_code\":\"444444\",\"refer_receipt_rid\":\"99999\",\"erp_remark\":\"hah\",\"remark\":\"eeee\",\"return_qty\":\"100\",\"unit_code\":\"PC\",\"unit_id\":149,\"unit_name\":\"件\",\"position_list\":[{\"rid\":\"1\",\"stock_id\":221,\"stock_sn_id\":1,\"stock_status\":\"1\",\"stock_status_name\":\"非限制库存\",\"product_batch\":\"H01222\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC-20\",\"package_type_name\":\"PVC-20\",\"package_standard\":\"20\",\"package_standard_ch\":\"PVC-20\",\"output_qty\":\"48\",\"output_time\":\"2018-02-02\",\"qty\":\"1\"},{\"rid\":\"2\",\"stock_id\":111,\"stock_sn_id\":1,\"stock_status\":\"1\",\"stock_status_name\":\"非限制库存\",\"product_batch\":\"H01111\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC-20\",\"package_type_name\":\"PVC-20\",\"package_standard\":\"20\",\"package_standard_ch\":\"PVC-20\",\"output_qty\":\"48\",\"output_time\":\"2018-02-01\",\"qty\":\"1\"}]}]}}";
//		body = JSONObject.fromObject(s);
		try {
			returnMap = returnService.getSaleReturnInfo(return_id);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("交货单详情", e);
		}
		
		body = UtilResult.getResultToJson(status, error_code, error_string, returnMap);
		UtilJSONConvert.setNullToEmpty(body);
		return body;
		//return body;
	}

	/**
	 * 1.1.5销售退库提交
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/sale/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveSaleReturnOrder(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject body = new JSONObject();
		BizDeliveryOrderHead head = new BizDeliveryOrderHead();
		try {
			json.put("cUserId", cUser.getUserId());
			body = returnService.saveSaleReturnOrder(json);

			// String s = "{\"return_id\":\"1\"}";
			// body = JSONObject.fromObject(s);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("销售退库提交", e);
		}
		return UtilResult.getResultToJson(status, error_code, error_string, body);

	}

	/**
	 * 1.1.6销售退库过账
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/sale/post", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject postSaleReturnOrder(int return_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject body = new JSONObject();
		 Map<String, Object> resultMap = new HashMap<>();
		try {
			resultMap = returnService.postOrderToSap(return_id, cUser.getUserId());//sap过账
			error_string = returnService.postOrderToFinish(resultMap, return_id, cUser.getUserId());//修改本地表
			returnService.saveAndwriteoffToMes(return_id, cUser.getUserId());//同步MES
			body.put("return_id", return_id);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (InterfaceCallException e) {
			error_code = e.getErrorCode();
			error_string = e.getMessage();
		}catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("销售退库过账", e);
		}
				
//		body = UtilResult.getResultToJson(status, error_code, error_string, resultMap);
//		UtilJSONConvert.setNullToEmpty(body);
		return UtilResult.getResultToJson(status, error_code, error_string, body);

	}
	
	
	//sap过账成功,mes失败后再次同步mes	mes冲销失败再次同步
	@RequestMapping(value = {"/sale/save_mes_again"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveMesAgain(int return_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			body = returnService.saveAndwriteoffToMes(return_id, cUser.getUserId());//同步MES
			error_string = body.getString("mesInfo");
			body.put("return_id", return_id);
		} catch (InterfaceCallException e) {
			status = false;
			error_code = e.getErrorCode();
			error_string = e.getMessage();
		} catch (WMSException e) {
			logger.error("重发mes同步 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = e.getMessage();
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
			logger.error("重发mes同步", e);
		}

		return UtilResult.getResultToJson(status, error_code, error_string, body);

	}
	

	/**
	 * 1.1.7销售退库冲销
	 * 
	 * @param
	 * @return
	 */

	@RequestMapping(value = "/sale/writeoff", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject writeoffReturnOrder(int return_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean status = false;
		JSONObject body = new JSONObject();
		BizDeliveryOrderHead head = new BizDeliveryOrderHead();
		Map<String, Object> resultMap = new HashMap<>();
		try {		
			resultMap = returnService.writeoffOrderToSap(return_id, cUser.getUserId());
			error_string = returnService.writeoffOrderToFinish(resultMap, return_id, cUser.getUserId());
			//同步mes
			returnService.saveAndwriteoffToMes(return_id, cUser.getUserId());
			body.put("return_id", return_id);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("销售退库冲销", e);
		}
		return UtilResult.getResultToJson(status, error_code, error_string, body);

	}
	
	/**
	 * 通用删除退库单
	 * @param stock_output_id
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/sale/delete", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteReturnOrder(int return_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "操作成功";
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			msg = returnService.deleteReturnOrder(return_id, cUser.getUserId());
			body.put("return_id", return_id);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			logger.error("删除退库单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error("删除退库单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "删除失败";
		}
		return UtilResult.getResultToJson(status, error_code, msg, body);
	}

}
