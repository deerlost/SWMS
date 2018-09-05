package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.BizUrgenceResVo;
import com.inossem.wms.service.biz.IUrgenceService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class BizDocController {

	@Autowired
	private IUrgenceService urgenceInAndOutStockService;

	private static final Logger logger = LoggerFactory.getLogger(BizDocController.class);

	/**
	 * 获取紧急出入库列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/bizdoc/get_urgence_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocUrgenceList(@RequestBody JSONObject json, CurrentUser cUser) {

		List<BizUrgenceResVo> list = new ArrayList<BizUrgenceResVo>();
		List<Integer> typelist = new ArrayList<Integer>();
		;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;

		int total = 0;

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String relateReceiptCode = json.getString("relate_receipt_code");
			String urgenceCode = json.getString("urgence_code");
			String state = json.getString("status");
			String operationTime = json.getString("operation_time");
			String demandPerson = json.getString("demand_person");
			String demandDept = json.getString("demand_dept");

			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String operator = json.getString("operator");

			JSONArray receiptType = json.getJSONArray("receipt_type");

			String matCodes = json.getString("mat_codes");

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("relateReceiptCode", relateReceiptCode);
			paramMap.put("urgenceCode", urgenceCode);
			paramMap.put("state", state);
			paramMap.put("operationTime", operationTime);
			paramMap.put("demandPerson", demandPerson);
			paramMap.put("demandDept", demandDept);
			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("operator", operator);
			if (receiptType.size() > 0) {
				paramMap.put("receiptTypeArray", receiptType);
			}
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);

			paramMap.put("matnrMap", getMatnrArray(matCodes));
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
//			PageAggregate aggregate = new PageAggregate();
//			aggregate.setExpression("GROUP_CONCAT(DISTINCT bh.receipt_type)");
//			aggregate.setAlias("receiptTypes");
//			List<PageAggregate> aggregateList = new ArrayList<PageAggregate>();
//			aggregateList.add(aggregate);
//			paramMap.put("aggregateColumns", aggregateList);

			// if(matCodes!=null&&!"".equals(matCodes)) {
			// String[] matArray = matCodes.split(",");
			// paramMap.put("matArray", matArray);
			// }

			list = urgenceInAndOutStockService.listBizDocUrgenceHead(paramMap);
			typelist = urgenceInAndOutStockService.listBizDocUrgenceType(paramMap);
			if (list != null && list.size() > 0) {
				total = UtilObject.getIntOrZero(list.get(0).getTotalCount());
			}
//			if (list != null && list.size() > 0) {
//				total = list.get(0).getTotalCount();
//				String receiptTypes = (String) list.get(0).getAggregateColumns().get(0).getValue();
//				String[] receiptTypeAry = receiptTypes.split(",");
//				for (String receiptTypeStr : receiptTypeAry) {
//					typelist.add(UtilString.getIntForString(receiptTypeStr));
//				}
//			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("紧急出入库列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	/**
	 * 获取入库列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/bizdoc/get_instock_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocinstockList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// List<Map<String, Object>> resultlist = new ArrayList<Map<String,
		// Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");

			String ftyId = json.getString("fty_id");
			String locationId = json.getString("location_id");
			String state = json.getString("status");
			String purchaseOrderCode = json.getString("purchase_order_code");
			String contractCode = json.getString("contract_code");
			String stockInputCode = json.getString("stock_input_code");
			String supplierName = json.getString("supplier_name");
			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String operator = json.getString("operator");
			JSONArray receiptType = json.getJSONArray("receipt_type");
			String matCodes = json.getString("mat_codes");

			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (ftyId != null && !"".equals(ftyId)) {
				paramMap.put("ftyId", Integer.parseInt(ftyId));
			}

			if (locationId != null && !"".equals(locationId)) {
				paramMap.put("locationId", Integer.parseInt(locationId));
			}

			paramMap.put("state", state);
			paramMap.put("purchaseOrderCode", purchaseOrderCode);
			paramMap.put("contractCode", contractCode);
			paramMap.put("stockInputCode", stockInputCode);
			paramMap.put("supplierName", supplierName);
			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("operator", operator);
			if (receiptType.size() > 0) {
				paramMap.put("receiptTypeArray", receiptType);
			}
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);

			paramMap.put("matnrMap", getMatnrArray(matCodes));

			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);

			// PageAggregate aggregate = new PageAggregate();
			// aggregate.setExpression("GROUP_CONCAT(DISTINCT
			// bh.receipt_type)");
			// aggregate.setAlias("receiptTypes");
			// List<PageAggregate> aggregateList = new
			// ArrayList<PageAggregate>();
			// aggregateList.add(aggregate);
			// paramMap.put("aggregateColumns", aggregateList);
			list = urgenceInAndOutStockService.getDocinstockList(paramMap);
			typelist = urgenceInAndOutStockService.listBizDocInstockType(paramMap);
			if (list != null && list.size() > 0) {
				total = UtilObject.getIntOrZero(list.get(0).get("totalCount"));
			}
			// if (list != null && list.size() > 0) {
			// Long totalCountLong = (Long) list.get(0).get("totalCount");
			// total = totalCountLong.intValue();
			// String receiptTypes = (String)
			// list.get(0).get(aggregate.getAlias());
			// String[] receiptTypeAry = receiptTypes.split(",");
			// for (String receiptTypeStr : receiptTypeAry) {
			// typelist.add(UtilString.getIntForString(receiptTypeStr));
			// }
			// }

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("入库列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	/**
	 * 获取出库列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/bizdoc/get_outstock_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocoutstockList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String ftyId = json.getString("fty_id");
			String locationId = json.getString("location_id");
			String state = json.getString("status");

			String purchaseCode = json.getString("purchase_code");
			String saleCode = json.getString("sale_code");
			String matReqCode = json.getString("mat_req_code");
			String allocateCode = json.getString("allocate_code");
			String contractCode = json.getString("contract_code");
			String stockOutputCode = json.getString("stock_output_code");
			String supplierName = json.getString("supplier_name");
			String receiveName = json.getString("receive_name");
			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String operator = json.getString("operator");
			JSONArray receiptType = json.getJSONArray("receipt_type");
			String matCodes = json.getString("mat_codes");

			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (ftyId != null && !"".equals(ftyId)) {
				paramMap.put("ftyId", Integer.parseInt(ftyId));
			}

			if (locationId != null && !"".equals(locationId)) {
				paramMap.put("locationId", Integer.parseInt(locationId));
			}
			paramMap.put("state", state);

			paramMap.put("purchaseCode", purchaseCode);
			paramMap.put("saleCode", saleCode);
			paramMap.put("matReqCode", matReqCode);
			paramMap.put("allocateCode", allocateCode);
			paramMap.put("contractCode", contractCode);

			paramMap.put("stockOutputCode", stockOutputCode);
			paramMap.put("supplierName", supplierName);
			paramMap.put("receiveName", receiveName);
			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("operator", operator);
			if (receiptType.size() > 0) {
				paramMap.put("receiptTypeArray", receiptType);
			}
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);

			paramMap.put("matnrMap", getMatnrArray(matCodes));
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
			list = urgenceInAndOutStockService.getDocOutstockList(paramMap);
			typelist = urgenceInAndOutStockService.listBizDocOutstockType(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("出库列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	/**
	 * 获取出库列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/bizdoc/get_stocktask_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocStockTaskList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String locationId = json.getString("location_id");
			String stockTaskCode = json.getString("stock_task_code");
			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String operator = json.getString("operator");
			String matCodes = json.getString("mat_codes");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			if (locationId != null && !"".equals(locationId)) {
				paramMap.put("locationId", Integer.parseInt(locationId));
			}

			paramMap.put("stockTaskCode", stockTaskCode);

			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("operator", operator);
			paramMap.put("matnrMap", getMatnrArray(matCodes));

			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
			list = urgenceInAndOutStockService.getDocStockTaskList(paramMap);
			// typelist =
			// urgenceInAndOutStockService.listBizDocOutstockType(paramMap);
			if (list != null && list.size() > 0) {
				total = UtilObject.getIntOrZero(list.get(0).get("totalCount"));
			}
			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("出库列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	@RequestMapping(value = "/biz/bizdoc/get_taskres_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocStockTaskResList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String matCodes = json.getString("mat_codes");
			String operator = json.getString("operator");

			String locationId = json.getString("location_id");
			String stockTaskReqCode = json.getString("stock_task_req_code");
			String matDocType = json.getString("mat_doc_type");
			String state = json.getString("status");

			Map<String, Object> paramMap = new HashMap<String, Object>();
			if (locationId != null && !"".equals(locationId)) {
				paramMap.put("locationId", Integer.parseInt(locationId));
			}

			paramMap.put("stockTaskReqCode", stockTaskReqCode);
			paramMap.put("matDocType", matDocType);
			paramMap.put("status", state);

			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("operator", operator);
			paramMap.put("matnrMap", getMatnrArray(matCodes));

			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
			list = urgenceInAndOutStockService.getDocStockTaskReqList(paramMap);

			for (Map<String, Object> map : list) {
				// 作业类型添加描述

				map.put("matDocTypeName",
						EnumReceiptType.getNameByValue(Byte.parseByte(String.valueOf(map.get("matDocType")))));
			}
			// typelist =
			// urgenceInAndOutStockService.listBizDocOutstockType(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("出库列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	@RequestMapping(value = "/biz/bizdoc/get_stockzl_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocStockZlList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;
		boolean sortAscend = true;
		String sortColumn = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
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
			String locationId = json.getString("location_id");

			String sourceAreaId = json.getString("source_area_id");
			String sourcePositionId = json.getString("source_position_id");
			String targetAreaId = json.getString("target_area_id");
			String targetPositionId = json.getString("target_position_id");
			String stockTaskCode = json.getString("stock_task_code");
			String receiptType = json.getString("receipt_type");

			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String matCodes = json.getString("mat_codes");
			String operator = json.getString("operator");

			Map<String, Object> paramMap = new HashMap<String, Object>();
			if (locationId != null && !"".equals(locationId)) {
				paramMap.put("locationId", Integer.parseInt(locationId));
			}
			if (sourceAreaId != null && !"".equals(sourceAreaId)) {
				paramMap.put("sourceAreaId", Integer.parseInt(sourceAreaId));
			}
			if (sourcePositionId != null && !"".equals(sourcePositionId)) {
				paramMap.put("sourcePositionId", Integer.parseInt(sourcePositionId));
			}
			if (targetAreaId != null && !"".equals(targetAreaId)) {
				paramMap.put("targetAreaId", Integer.parseInt(targetAreaId));
			}
			if (targetPositionId != null && !"".equals(targetPositionId)) {
				paramMap.put("targetPositionId", Integer.parseInt(targetPositionId));
			}

			paramMap.put("stockTaskCode", stockTaskCode);
			paramMap.put("receiptType", receiptType);

			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("operator", operator);
			paramMap.put("matnrMap", getMatnrArray(matCodes));

			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
			list = urgenceInAndOutStockService.getDocStockZlList(paramMap);
			// typelist =
			// urgenceInAndOutStockService.listBizDocOutstockType(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("出库列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	/**
	 * 获取转储列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/bizdoc/get_transport_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocTransportList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String ftyOutId = json.getString("fty_out_id");
			String locationOutId = json.getString("location_out_id");
			String ftyInputId = json.getString("fty_input_id");
			String locationInputId = json.getString("location_input_id");
			String state = json.getString("status");

			String stockTransportCode = json.getString("stock_transport_code");
			JSONArray receiptType = json.getJSONArray("receipt_type");
			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String operator = json.getString("operator");

			String matCodes = json.getString("mat_codes");

			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (ftyOutId != null && !"".equals(ftyOutId)) {
				paramMap.put("ftyOutId", Integer.parseInt(ftyOutId));
			}

			if (locationOutId != null && !"".equals(locationOutId)) {
				paramMap.put("locationOutId", Integer.parseInt(locationOutId));
			}
			if (ftyInputId != null && !"".equals(ftyInputId)) {
				paramMap.put("ftyInputId", Integer.parseInt(ftyInputId));
			}

			if (locationInputId != null && !"".equals(locationInputId)) {
				paramMap.put("locationInputId", Integer.parseInt(locationInputId));
			}
			paramMap.put("state", state);

			paramMap.put("stockTransportCode", stockTransportCode);
			if (receiptType.size() > 0) {
				paramMap.put("receiptTypeArray", receiptType);
			}

			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("operator", operator);

			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("matnrMap", getMatnrArray(matCodes));
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
			list = urgenceInAndOutStockService.listBizDocstockTransportOnPaging(paramMap);

			for (Map<String, Object> map : list) {
				// 作业类型添加描述

				map.put("receiptTypeName",
						EnumReceiptType.getNameByValue(Byte.parseByte(String.valueOf(map.get("receiptType")))));
			}

			typelist = urgenceInAndOutStockService.listBizDocTransportType(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("转储列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	/**
	 * 获取退库列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/bizdoc/get_return_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocReturnList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			JSONArray receiptType = json.getJSONArray("receipt_type");
			String operator = json.getString("operator");
			String matCodes = json.getString("mat_codes");

			String ftyId = json.getString("fty_id");
			String locationId = json.getString("location_id");
			String state = json.getString("status");

			String returnCode = json.getString("return_code");

			String saleCode = json.getString("sale_code");
			String referReceiptCode = json.getString("refer_receipt_code");
			String reserveCode = json.getString("reserve_code");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			if (receiptType.size() > 0) {
				paramMap.put("receiptTypeArray", receiptType);
			}
			paramMap.put("operator", operator);
			paramMap.put("matnrMap", getMatnrArray(matCodes));
			if (ftyId != null && !"".equals(ftyId)) {
				paramMap.put("ftyId", Integer.parseInt(ftyId));
			}

			if (locationId != null && !"".equals(locationId)) {
				paramMap.put("locationId", Integer.parseInt(locationId));
			}
			paramMap.put("state", state);
			paramMap.put("returnCode", returnCode);
			paramMap.put("saleCode", saleCode);
			paramMap.put("referReceiptCode", referReceiptCode);
			paramMap.put("reserveCode", reserveCode);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);

			list = urgenceInAndOutStockService.listBizDocstockReturnOnPaging(paramMap);
			typelist = urgenceInAndOutStockService.listBizDocReturnType(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("转储列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	/**
	 * 获取调拨列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/bizdoc/get_allocate_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDocAllocateList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> typelist = new ArrayList<Integer>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String ftyOutId = json.getString("fty_out_id");
			String locationOutId = json.getString("location_out_id");
			String ftyInputId = json.getString("fty_input_id");
			String locationInputId = json.getString("location_input_id");
			String state = json.getString("status");
			String allocateCode = json.getString("allocate_code");
			String allocateDeliveryCode = json.getString("allocate_delivery_code");
			String startdate = json.getString("start_date");
			String enddate = json.getString("end_date");
			String demandDate = json.getString("demand_date");
			String operator = json.getString("operator");
			String matCodes = json.getString("mat_codes");

			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (ftyOutId != null && !"".equals(ftyOutId)) {
				paramMap.put("ftyOutId", Integer.parseInt(ftyOutId));
			}

			if (locationOutId != null && !"".equals(locationOutId)) {
				paramMap.put("locationOutId", Integer.parseInt(locationOutId));
			}
			if (ftyInputId != null && !"".equals(ftyInputId)) {
				paramMap.put("ftyInputId", Integer.parseInt(ftyInputId));
			}

			if (locationInputId != null && !"".equals(locationInputId)) {
				paramMap.put("locationInputId", Integer.parseInt(locationInputId));
			}
			paramMap.put("state", state);
			paramMap.put("allocateCode", allocateCode);
			paramMap.put("allocateDeliveryCode", allocateDeliveryCode);
			paramMap.put("startdate", startdate);
			paramMap.put("enddate", enddate);
			paramMap.put("demandDate", demandDate);
			paramMap.put("operator", operator);
			paramMap.put("matnrMap", getMatnrArray(matCodes));

			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);

			list = urgenceInAndOutStockService.listBizDocAllocateOnPaging(paramMap);
			// typelist =
			// urgenceInAndOutStockService.listBizDocReturnType(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("转储列表", e);
		}

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		obj.put("receipt_type_list", typelist);

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, obj);
	}

	public HashMap<String, Object> getMatnrArray(String matnr) {

		HashMap<String, Object> returnValue = null;
		String[] matnrs = null;
		if (StringUtils.hasText(matnr)) {
			returnValue = new HashMap<String, Object>();
			matnr.replaceAll("，", ",");
			matnrs = matnr.split(",");
			ArrayList<HashMap<String, Object>> matnr_one_List = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> matnr_interval_List = new ArrayList<HashMap<String, Object>>();
			if (matnrs != null && matnrs.length > 0) {
				for (int i = 0; i < matnrs.length; i++) {
					if (StringUtils.hasText(matnrs[i])) {
						if (matnrs[i].contains("-")) {
							HashMap<String, Object> mantrMap = new HashMap<String, Object>();
							String[] matnrsInner = matnrs[i].split("-");
							mantrMap.put("matnr_begin", matnrsInner[0]);
							mantrMap.put("matnr_end", matnrsInner[1]);
							matnr_interval_List.add(mantrMap);
						} else {
							HashMap<String, Object> mantrMap = new HashMap<String, Object>();
							mantrMap.put("matnr", matnrs[i]);
							matnr_one_List.add(mantrMap);
						}

					}
				}
			}
			returnValue.put("matnr_interval_List", matnr_interval_List);
			returnValue.put("matnr_one_List", matnr_one_List);
		}

		return returnValue;

	}
}
