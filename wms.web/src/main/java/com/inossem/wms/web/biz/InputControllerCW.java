package com.inossem.wms.web.biz;

import java.util.ArrayList;
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
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputProduceTransportService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.service.biz.IPkgOperationService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class InputControllerCW {

	private static final Logger logger = LoggerFactory.getLogger(InputControllerCW.class);

	@Autowired
	private IInputService rkglService;

	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private IPkgOperationService pkgService;

	@Autowired
	private IInputProduceTransportService iInputProduceTransportService;

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
		List<Map<String, Object>> classTypeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> productLineMap = new HashMap<String, Object>();
		Integer classTypeId = 0;
		try {
			ftyList = commonService.listFtyLocationForUser(cUser.getUserId(), "");
			moveList = commonService.listMovTypeAndReason(EnumReceiptType.STOCK_INPUT_OTHER.getValue());
			classTypeList = commonService.getClassTypeList();
			classTypeId = commonService.selectNowClassType();
			if(classTypeId==null){
				classTypeId = 0;
			}
			productLineMap = pkgService.selectPkgClassLineInstallationList(cUser.getUserId());
			
		} catch (Exception e) {
			logger.error("其他入库免检原因", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		body.put("ftyList", ftyList);
		body.put("moveList", moveList);
		body.put("classTypeList", classTypeList);
		body.put("product_line_list", productLineMap.get("productLineList"));
		body.put("classTypeId", classTypeId);
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
		return obj;

	}
	
	@RequestMapping(value = "/biz/input/other/buffPrice", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object buffPrice(CurrentUser cUser) {

		HashMap<String, Object> body = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONArray ftyList = new JSONArray();
		List<DicMoveType> moveList = new ArrayList<DicMoveType>();
		List<Map<String, Object>> classTypeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> productLineMap = new HashMap<String, Object>();
		Integer classTypeId = 0;
		try {
			commonService.bufferPrice();
		} catch (Exception e) 	{
			logger.error("其他入库免检原因", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		body.put("ftyList", ftyList);
		body.put("moveList", moveList);
		body.put("classTypeList", classTypeList);
		body.put("product_line_list", productLineMap.get("productLineList"));
		body.put("classTypeId", classTypeId);
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
		return obj;

	}
	

	/**
	 * 物料描述列表
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/input/other/get_material_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMaterialList(String condition) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<DicMaterial> list = new ArrayList<DicMaterial>();
		try {
			DicMaterial material = new DicMaterial();
			material.setCondition(condition);

			list = rkglService.getMaterialList(material);
		} catch (Exception e) {
			logger.error("物料描述列表", e);
		}
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, 0, 10, 5, list);
		return obj;

	}

	// 其他入库-1.4 根据物料获取包装类型
	@RequestMapping(value = "/biz/input/other/get_package_type_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getPackageTypeList(Integer mat_id, Integer fty_id,CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		List<DicPackageType> list = new ArrayList<DicPackageType>();
		List<Map<String, Object>> erp_batch_list = new ArrayList<Map<String, Object>>();
		boolean status = false;
		try {
			list = commonService.listPackageTypeByMatId(mat_id);
			erp_batch_list = iInputProduceTransportService.selectErpList(mat_id+"",fty_id);
			if(list!=null&&list.size()>0){
				for(DicPackageType d:list){
					List<Map<String, Object>> erpList = new ArrayList<Map<String,Object>>();
					String erpKey = UtilObject.getStringOrEmpty(d.getErpBatchKey());
					if(erp_batch_list!=null){
						for(Map<String, Object> map:erp_batch_list){
							String erpCode =UtilObject.getStringOrEmpty(map.get("erp_batch_code"));
							if(erpCode.contains(erpKey)){
								erpList.add(map);
							}
						}
					}
					d.setErpBatchList(erpList);
				}
			}
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("包类型", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, list);
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
			stockInputHead.setCreateUser(user.getUserId());
			List<BizStockInputItem> itemList = rkglService.trimZeroOrNullQtyObj(stockInputHead.getItemList());
			stockInputHead.setItemList(itemList);
			stockInputHead.setRequestSource(EnumRequestSource.WEB.getValue());
			rkglService.addOtherInput(stockInputHead);
			status = true;
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

	// 其他入库-1.8 其他入库过账
	@RequestMapping(value = "/biz/input/other/post/{stock_input_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject otherInputPost(@PathVariable("stock_input_id") Integer stock_input_id,
			CurrentUser cUser) {

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> input = new HashMap<String, Object>();
		try {

			rkglService.postOtherInput(stock_input_id, cUser.getUserId());

		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("其他入库过账", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, input);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}

	// 其他入库-1.7 其他入库列表
	@RequestMapping(value = "/biz/input/other/other_input_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject otherInputList(@RequestBody JSONObject json, CurrentUser cUser) {

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

			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);
			map.put("userId", cUser.getUserId());
			List<BizStockInputHead> shList = rkglService.getInputHeadList(map);
			if (shList != null && shList.size() > 0) {
				total = shList.get(0).getTotalCount();
				for (BizStockInputHead sh : shList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
					itemMap.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
					itemMap.put("moveTypeCode", UtilString.getStringOrEmptyForObject(sh.getMoveTypeCode()));
					itemMap.put("moveTypeName", UtilString.getStringOrEmptyForObject(sh.getMoveTypeName()));
					itemMap.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
					itemMap.put("receiptTypeName",
							UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
					itemMap.put("createTime", UtilString.getLongStringForDate(sh.getCreateTime()));
					itemMap.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
					itemMap.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
					itemMap.put("statusName",
							UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));

					itemMap.put("ftyId", UtilString.getStringOrEmptyForObject(sh.getFtyId()));
					itemMap.put("ftyCode", UtilString.getStringOrEmptyForObject(sh.getFtyCode()));
					itemMap.put("ftyName", UtilString.getStringOrEmptyForObject(sh.getFtyName()));
					itemMap.put("locationId", UtilString.getStringOrEmptyForObject(sh.getLocationId()));
					itemMap.put("locationCode", UtilString.getStringOrEmptyForObject(sh.getLocationCode()));
					itemMap.put("locationName", UtilString.getStringOrEmptyForObject(sh.getLocationName()));
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

	// 其他入库-1.8 其他入库详情
	@RequestMapping(value = "/biz/input/other/other_input_info/{stock_input_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject otherInputInfo(@PathVariable("stock_input_id") Integer stock_input_id,
			CurrentUser cUser) {

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
				// input.put("checkAccount",
				// UtilString.getStringOrEmptyForObject(sh.getCheckAccount()));
				input.put("stockInputId", UtilString.getStringOrEmptyForObject(sh.getStockInputId()));
				input.put("stockInputCode", UtilString.getStringOrEmptyForObject(sh.getStockInputCode()));
				input.put("receiptType", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
				input.put("receiptTypeName",
						UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
				input.put("createTime", UtilString.getLongStringForDate(sh.getCreateTime()));
				input.put("createUserName", UtilString.getStringOrEmptyForObject(sh.getCreateUserName()));
				input.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
				input.put("statusName",
						UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
				input.put("remark", UtilString.getStringOrEmptyForObject(sh.getRemark()));
				input.put("moveTypeName", UtilString.getStringOrEmptyForObject(sh.getMoveTypeName()));
				input.put("moveTypeCode", UtilString.getStringOrEmptyForObject(sh.getMoveTypeCode()));
				input.put("specStock", UtilString.getStringOrEmptyForObject(sh.getSpecStock()));

				input.put("moveTypeId", UtilString.getStringOrEmptyForObject(sh.getMoveTypeId()));
				input.put("ftyId", UtilString.getStringOrEmptyForObject(sh.getFtyId()));
				input.put("ftyCode", UtilString.getStringOrEmptyForObject(sh.getFtyCode()));
				input.put("ftyName", UtilString.getStringOrEmptyForObject(sh.getFtyName()));
				input.put("locationId", UtilString.getStringOrEmptyForObject(sh.getLocationId()));
				input.put("locationCode", UtilString.getStringOrEmptyForObject(sh.getLocationCode()));
				input.put("locationName", UtilString.getStringOrEmptyForObject(sh.getLocationName()));
				input.put("classTypeName", UtilString.getStringOrEmptyForObject(sh.getClassTypeName()));
				input.put("installationId", UtilString.getStringOrEmptyForObject(sh.getInstallationId()));
				input.put("installationName", UtilString.getStringOrEmptyForObject(sh.getInstallationName()));
				input.put("productLineId", UtilString.getStringOrEmptyForObject(sh.getProductLineId()));
				input.put("productLineName", UtilString.getStringOrEmptyForObject(sh.getProductLineName()));
				List<BizStockInputItem> itemList = rkglService.getInputItemListByID(stock_input_id);
				input.put("itemList", itemList);
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

	// 其他入库-1.7 其他入库列表
	@RequestMapping(value = "/biz/input/other/get_erp_batch", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getERPBatchList(String mat_id, Integer fty_id, CurrentUser cUser) {


		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> erp_batch_list = new ArrayList<Map<String, Object>>();
		try {

			erp_batch_list = iInputProduceTransportService.selectErpList(mat_id,fty_id);
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, erp_batch_list);
		return obj;

	}
	
	
	// 其他入库-1.7 其他入库列表
		@RequestMapping(value = "/biz/input/other/syn_erp_batch", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
		public @ResponseBody JSONObject synERPBatchList() {


			Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
			Integer pageSize = EnumPage.PAGE_SIZE.getValue();
			Integer total = 0;

			Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			boolean status = true;
			try {

				commonService.bufferERPBatch();
			} catch (Exception e) {
				status = false;
				errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
				logger.error("调拨单配送单列表（创建调拨入库单时查询列表）", e);
			}
			JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, new JSONArray());
			return obj;

		}
	
}
