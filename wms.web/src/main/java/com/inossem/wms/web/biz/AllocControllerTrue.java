package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizAllocateDeliveryHead;
import com.inossem.wms.model.enums.EnumAllocateStatus;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.vo.BizAllocateHeadVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.StockBatchVo;
import com.inossem.wms.service.biz.IAllocService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 调拨管理
 * 
 * @author wang.ligang
 *
 */
@Controller
@RequestMapping("/biz/alloc")
public class AllocControllerTrue {
	private static final Logger logger = LoggerFactory.getLogger(AllocControllerTrue.class);

	@Autowired
	private IAllocService allocService;

	@Autowired
	private ICommonService commonService;

	/**
	 * 15.1.1 调拨单列表
	 * 
	 * @param cuser
	 * @param json
	 *            {"condition":"检索条件","pageIndex":1,"pageSize":10,"total":-1}
	 * @return
	 */
	@RequestMapping(value = "/alloc/get_alloc_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAllocList(CurrentUser cuser, @RequestBody JSONObject json) {

		JSONArray body = new JSONArray();

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = 0;
		boolean sortAscend = true;
		String sortColumn = "";
		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			if (json.containsKey("sort_ascend")) {
				sortAscend = json.getBoolean("sort_ascend");
			}
			if (json.containsKey("sort_column")) {
				sortColumn = json.getString("sort_column");
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("paging", true);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", total);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
			// long allocateId = 0;

			paramMap.put("createUser", cuser.getUserId());
			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			if (StringUtils.hasLength(condition)) {
				try {
					// allocateId = Long.parseLong(condition);
					paramMap.put("allocateCode", condition);
				} catch (Exception e) {
					logger.error("", e);
				}
			}

			List<BizAllocateHeadVo> resList = allocService.getAllocList(paramMap);
			if (resList != null && resList.size() > 0) {
				total = resList.get(0).getTotalCount();
				for (BizAllocateHeadVo vo : resList) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("demand_date", UtilString.getShortStringForDate(vo.getDemandDate()));
					jsonObj.put("allocate_id", vo.getAllocateId());
					jsonObj.put("allocate_code", vo.getAllocateCode());
					jsonObj.put("location_output_name", vo.getLocationOutputName());
					jsonObj.put("status_name", EnumAllocateStatus.getDescByValue(vo.getStatus()));
					jsonObj.put("fty_output_name", vo.getFtyOutputName());
					jsonObj.put("status", vo.getStatus());
					List<String> list = allocService.getDeliveryCodeByAllocateId(vo.getAllocateId());

					jsonObj.put("delivery_id_list", list);
					body.add(jsonObj);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), pageIndex, pageSize,
					total, body);
		}

		return UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, body);
	}

	/**
	 * 15.1.3 查询物料列表(创建调拨单对应的添加button)
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/alloc/get_materials", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterials(CurrentUser cuser, @RequestBody JSONObject json) {
		JSONArray body = new JSONArray();
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = 0;

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			String ftyId = json.getString("fty_id");
			String locationId = json.getString("location_id");
			String condition = null;
			if (json.containsKey("condition")) {
				condition = json.getString("condition");
			}

			// 参数设置
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("ftyId", ftyId);
			paramMap.put("locationId", locationId);
			if (StringUtils.hasText(condition)) {
				paramMap.put("condition", condition);
			}
			paramMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());

			List<StockBatchVo> findList = allocService.getMaterials(paramMap);
			if (findList != null && findList.size() > 0) {
				total = findList.get(0).getTotalCount();
				for (StockBatchVo innerObj : findList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("mat_id", innerObj.getMatId());
					map.put("mat_code", innerObj.getMatCode());
					map.put("qty", innerObj.getQty());
					map.put("mat_name", innerObj.getMatName());
					map.put("unit_id", innerObj.getUnitId());
					map.put("unit_code", innerObj.getUnitCode());
					map.put("unit_name", innerObj.getUnitName());
					body.add(map);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), pageIndex, pageSize,
					total, body);
		}

		return UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, body);
	}

	/**
	 * 15.1.2 获取发出接收库存地点
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/alloc/get_out_and_in_fty_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOutAndInFtyList(CurrentUser cuser) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Map<String, Object> body = new HashMap<String, Object>();
		List<Map<String, Object>> ftyOutPutList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> ftyInPutList = new ArrayList<Map<String, Object>>();
		try {
			List<DicStockLocationVo> findwerksOutPutList = allocService.getLocationsByCorpId(cuser.getCorpId());
			List<RelUserStockLocationVo> findwerksInPutList = commonService.getStockLocationForUser(cuser.getUserId());

			Map<String, Object> ftyMap = new LinkedHashMap<String, Object>();

			// 处理接收工厂的信息
			if (findwerksInPutList != null) {
				for (RelUserStockLocationVo inneerObj : findwerksInPutList) {

					// 用工厂ID_CODE作为key，处理库存地点结构
					String ftyIdAndCode = inneerObj.getFtyId() + "_" + inneerObj.getFtyCode();
					if (ftyMap.containsKey(ftyIdAndCode)) {
						List<Map<String, Object>> locationMapList = new ArrayList<Map<String, Object>>();
						locationMapList = (List<Map<String, Object>>) ftyMap.get(ftyIdAndCode);
						HashMap<String, Object> locationObj = new HashMap<String, Object>();
						locationObj.put("location_id", inneerObj.getLocationId());
						locationObj.put("location_code", inneerObj.getLocationCode());
						locationObj.put("fty_name", inneerObj.getFtyName());
						locationObj.put("location_name", inneerObj.getLocationName());
						locationMapList.add(locationObj);
						ftyMap.put(ftyIdAndCode, locationMapList);
					} else {
						ArrayList<HashMap<String, Object>> locationMapList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> locationObj = new HashMap<String, Object>();
						locationObj.put("location_id", inneerObj.getLocationId());
						locationObj.put("location_code", inneerObj.getLocationCode());
						locationObj.put("fty_name", inneerObj.getFtyName());
						locationObj.put("location_name", inneerObj.getLocationName());
						locationMapList.add(locationObj);
						ftyMap.put(ftyIdAndCode, locationMapList);
					}
				}
				Set<Entry<String, Object>> entries = ftyMap.entrySet();
				for (Entry<String, Object> entry : entries) {
					HashMap<String, Object> ftyObj = new HashMap<String, Object>();

					List<Map<String, Object>> inventoryAddressList = (List<Map<String, Object>>) entry.getValue();
					String idAndCode = entry.getKey();
					String[] arrIdAndCode = idAndCode.split("_");
					ftyObj.put("fty_id", arrIdAndCode[0]);
					ftyObj.put("fty_code", arrIdAndCode[1]);
					ftyObj.put("fty_name", inventoryAddressList.get(0).get("fty_name"));
					ftyObj.put("location_list", inventoryAddressList);
					ftyInPutList.add(ftyObj);
				}
			}

			ftyMap.clear();
			// 处理接收工厂的信息
			if (findwerksOutPutList != null) {
				for (DicStockLocationVo inneerObj : findwerksOutPutList) {

					// 用工厂ID_CODE作为key，处理库存地点结构
					String ftyIdAndCode = inneerObj.getFtyId() + "_" + inneerObj.getFtyCode();
					if (ftyMap.containsKey(ftyIdAndCode)) {
						List<Map<String, Object>> locationMapList = new ArrayList<Map<String, Object>>();
						locationMapList = (List<Map<String, Object>>) ftyMap.get(ftyIdAndCode);
						HashMap<String, Object> locationObj = new HashMap<String, Object>();
						locationObj.put("location_id", inneerObj.getLocationId());
						locationObj.put("location_code", inneerObj.getLocationCode());
						locationObj.put("fty_name", inneerObj.getFtyName());
						locationObj.put("location_name", inneerObj.getLocationName());
						locationMapList.add(locationObj);
						ftyMap.put(ftyIdAndCode, locationMapList);
					} else {
						ArrayList<HashMap<String, Object>> locationMapList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> locationObj = new HashMap<String, Object>();
						locationObj.put("location_id", inneerObj.getLocationId());
						locationObj.put("location_code", inneerObj.getLocationCode());
						locationObj.put("fty_name", inneerObj.getFtyName());
						locationObj.put("location_name", inneerObj.getLocationName());
						locationMapList.add(locationObj);
						ftyMap.put(ftyIdAndCode, locationMapList);
					}
				}
				Set<Entry<String, Object>> entries = ftyMap.entrySet();
				for (Entry<String, Object> entry : entries) {
					HashMap<String, Object> ftyObj = new HashMap<String, Object>();

					List<Map<String, Object>> inventoryAddressList = (List<Map<String, Object>>) entry.getValue();
					String idAndCode = entry.getKey();
					String[] arrIdAndCode = idAndCode.split("_");
					ftyObj.put("fty_id", arrIdAndCode[0]);
					ftyObj.put("fty_code", arrIdAndCode[1]);
					ftyObj.put("fty_name", inventoryAddressList.get(0).get("fty_name"));
					ftyObj.put("location_list", inventoryAddressList);
					ftyOutPutList.add(ftyObj);
				}
			}
			body.put("fty_input_list", ftyInPutList);
			body.put("fty_output_list", ftyOutPutList);

			HashMap<String, Object> baseData = new HashMap<String, Object>();
			baseData.put("corp_name", cuser.getCorpName());
			baseData.put("corp_id", cuser.getCorpName());
			baseData.put("org_name", cuser.getOrgName());
			baseData.put("org_id", cuser.getOrgId());
			baseData.put("create_time", UtilString.getShortStringForDate(new Date()));
			baseData.put("create_user_name", cuser.getDisplayUsername());
			body.put("base_data", baseData);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}
		return UtilResult.getResultToJson(true, errorCode, body);
	}

	/**
	 * 15.1.4 保存提交调拨单
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/alloc/save_alloc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveOrUpdateAlloc(CurrentUser cuser, @RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		Map<String, Object> returnMap = null;
		try {
			Map<String, Object> mapAllocate = this.allocService.createAllocateParams(cuser, json);
			returnMap = allocService.save(mapAllocate);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), returnMap);
		}
		return UtilResult.getResultToJson(true, errorCode, returnMap);
	}

	/**
	 * 15.1.6删除调拨单
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/alloc/alloc_delete/{allocate_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteAlloc(CurrentUser cuser, @PathVariable("allocate_id") Integer allocate_id) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		try {
			if (!allocService.allocateDel(allocate_id)) {
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			}

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, errorCode, "");
		}
		return UtilResult.getResultToJson(true, errorCode, "");
	}

	/**
	 * 15.2.1 调拨单所有行项目(配送单管理列表)
	 * 
	 * @param cuser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/delivery/alloc_item_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAllocItemList(CurrentUser cuser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = 0;
		boolean sortAscend = true;
		String sortColumn = "";
		List<Map<String, Object>> body = null;
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

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("paging", true);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			// paramMap.put("createUser",cuser.getUserId());

			paramMap.put("distributed", 10); // 配送
			paramMap.put("status", 10); // 提交
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);

			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			if (StringUtils.hasText(condition)) {
				paramMap.put("condition", condition);
			}

			paramMap.put("ftyLocation", cuser.getLocationList());

			body = allocService.getAllotItemsOnCreateDelivery(paramMap);

			if (body != null && body.size() > 0) {
				Object object = body.get(0).get("totalCount");
				if (object != null) {
					total = Integer.parseInt(object.toString());
				}
				for (Map<String, Object> innerMap : body) {
					Date date = (Date) innerMap.get("demand_date");
					innerMap.remove("demand_date");
					innerMap.put("demand_date", UtilString.getShortStringForDate(date));
				}
				// HashMap<String, Object> allot_item = new HashMap<String,
				// Object>();
				//
				// allot_item.put("item_id", allotItem.getItemId());
				// allot_item.put("allot_id", allotItem.getAllotId());
				// allot_item.put("allot_sn", allotItem.getAllotSn());
				// allot_item.put("matnr", allotItem.getMatnr());
				// allot_item.put("maktx", allotItem.getMaktx());
				// allot_item.put("meins", allotItem.getMeins());
				// allot_item.put("mseht", allotItem.getMseht());
				// allot_item.put("werks_output", allotItem.getWerksOutput());
				// allot_item.put("lgort_output", allotItem.getLgortOutput());
				// allot_item.put("werks_output_name",
				// allotItem.getWerks_output_name());
				// allot_item.put("lgort_output_name",
				// allotItem.getLgort_output_name());
				//
				//
				// allot_item.put("menge", allotItem.getMenge());
				//
				// allot_item.put("butxt", allotItem.getButxt());
				// allot_item.put("lgort_input_name",
				// allotItem.getLgort_input_name());
				// allot_item.put("werks_input_name",
				// allotItem.getWerks_input_name());
				// allot_item.put("xqrq",
				// StringUtil.dateToString(allotItem.getXqrq()));
				// allot_item_list.add(allot_item);
				// }

			}

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			logger.error("调拨单所有行项目(创建配送单)", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), pageIndex, pageSize,
					total, body);
		}

		return UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, body);
	}

	/**
	 * 15.2.2 调拨单行项目id获取详情(创建配送单按钮)
	 * 
	 * @param cuser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/delivery/alloc_item_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAllocItemInfo(CurrentUser cuser, @RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		int total = 0;
		JSONObject body = new JSONObject();
		try {
			JSONArray item_ids = json.getJSONArray("item_ids");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("distributed", 10); // 配送
			map.put("status", 10); // 提交
			map.put("itemIds", item_ids);

			List<Map<String, Object>> findList = allocService.getAllotItemsOnCreateDelivery(map);
			if (findList != null && findList.size() > 0) {
				Object object = findList.get(0).get("totalCount");
				if (object != null) {
					total = Integer.parseInt(object.toString());
				}
				for (Map<String, Object> innerMap : findList) {
					Date date = (Date) innerMap.get("demand_date");
					innerMap.remove("demand_date");
					innerMap.put("demand_date", UtilString.getShortStringForDate(date));
				}
			}
			body.put("item_list", findList);
			body.put("corp_name", cuser.getCorpName());
			body.put("create_user", cuser.getUserName());
			body.put("create_time", UtilString.getShortStringForDate(new Date()));
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			logger.error("调拨单所有行项目(创建配送单)", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}
		return UtilResult.getResultToJson(true, errorCode, body);
	}

	/**
	 * 15.2.3 提交配送单
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/delivery/save_delivery", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveDelivery(CurrentUser cuser, @RequestBody JSONObject json) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = false;
		JSONArray item_ids = json.getJSONArray("item_ids");
		String allocateDeliveryId = "";
		try {
			List<Integer> allotItemIds = new ArrayList<Integer>();
			allotItemIds.addAll(item_ids);

			BizAllocateDeliveryHead delivery = new BizAllocateDeliveryHead();

			delivery.setCorpId(cuser.getCorpId());
			delivery.setCreateUser(cuser.getUserId());

			this.createDeliveryParams(json, delivery);
			Map<String, Object> returnMap = allocService.saveDeliveryByAllotItemIds(delivery, allotItemIds);
			errorCode = (Integer) returnMap.get("errorCode");
			if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
				status = true;
			}
			allocateDeliveryId = returnMap.get("allocateDeliveryId") != null
					? returnMap.get("allocateDeliveryId").toString() : "";

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(" 调拨单行项目id获取详情(创建配送单)", e);
		}
		return UtilResult.getResultToJson(status, errorCode, allocateDeliveryId);
	}

	private void createDeliveryParams(JSONObject json, BizAllocateDeliveryHead delivery) {

		if (json.containsKey("delivery_date")) {
			delivery.setDeliveryDate(UtilString.getDateForString(json.getString("delivery_date"), null));
		} else {
			delivery.setDeliveryDate(new Date());
		}
		if (json.containsKey("delivery_vehicle")) {
			delivery.setDeliveryVehicle(json.getString("delivery_vehicle"));
		} else {
			delivery.setDeliveryVehicle("");
		}
		if (json.containsKey("delivery_driver")) {
			delivery.setDeliveryDriver(json.getString("delivery_driver"));
		} else {
			delivery.setDeliveryDriver("");
		}
		if (json.containsKey("delivery_phone")) {
			delivery.setDeliveryPhone(json.getString("delivery_phone"));
		} else {
			delivery.setDeliveryPhone("");
		}
		if (json.containsKey("remark")) {
			delivery.setRemark(json.getString("remark"));
		}

	}

	/**
	 * 15.3.3删除配送单
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/delivery_query/delivery_info/{allocate_delivery_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteDelivery(CurrentUser cuser,
			@PathVariable("allocate_delivery_id") Integer allocate_delivery_id) {

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		boolean status = false;

		try {
			BizAllocateDeliveryHead delivery = allocService.getDeliveryById(allocate_delivery_id);
			if (delivery != null && delivery.getStatus() <= 10) {
				status = allocService.deleteDeliveryById(allocate_delivery_id);
			} else {
				status = false;
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			logger.error("调拨单详情", e);
			return UtilResult.getResultToJson(false, errorCode, "");
		}
		return UtilResult.getResultToJson(status, errorCode, "");
	}

	/**
	 * 15.3.1 配送单列表(配送单查询)
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/delivery_query/delivery_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDeliveryList(CurrentUser cuser, @RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = 0;
		boolean sortAscend = true;
		String sortColumn = "";
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();

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

			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("totalCount", totalCount);
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);
			String condition = json.getString("condition");

			long deliveryId = 0;
			try {
				deliveryId = Long.parseLong(condition);
				map.put("allocateDeliveryId", deliveryId);
			} catch (Exception e) {

			}

			map.put("createUser", cuser.getUserId());
			List<Map<String, Object>> deliveryList = allocService.getDeliverieListByCondition(map);
			if (deliveryList != null && deliveryList.size() > 0) {
				if (deliveryList.get(0).get("totalCount") != null) {
					total = Integer.parseInt(deliveryList.get(0).get("totalCount").toString());
				}

				for (Map<String, Object> delivery : deliveryList) {
					Map<String, Object> mapDeliviry = new HashMap<String, Object>();
					mapDeliviry.put("allocate_delivery_id", delivery.get("allocate_delivery_id"));
					mapDeliviry.put("allocate_delivery_code", delivery.get("allocate_delivery_code"));

					mapDeliviry.put("delivery_vehicle", delivery.get("delivery_vehicle"));
					mapDeliviry.put("delivery_date",
							UtilString.getShortStringForDate((Date) delivery.get("delivery_date")));
					mapDeliviry.put("status", delivery.get("status"));
					mapDeliviry.put("status_name",
							EnumAllocateStatus.getDescByValue(Byte.parseByte(delivery.get("status").toString())));
					body.add(mapDeliviry);
				}
			}
		} catch (Exception e) {
			errorCode = 1;
			logger.error("配送单列表", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), pageIndex, pageSize,
					total, body);
		}

		return UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, body);
	}

	/**
	 * 15.3.2 配送单详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/delivery_query/delivery_info/{allocate_delivery_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDeliveryInfo(CurrentUser cuser,
			@PathVariable("allocate_delivery_id") Integer allocate_delivery_id) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		List<Map<String, Object>> deliveryItemList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> delivery = new HashMap<String, Object>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("allocateDeliveryId", allocate_delivery_id);
			map.put("createUser", cuser.getUserId());

			List<Map<String, Object>> deliveryList = allocService.getDeliverieListByCondition(map);
			if (deliveryList != null && deliveryList.size() == 1) {
				Map<String, Object> deliveryObj = deliveryList.get(0);
				delivery.put("allocate_delivery_id", deliveryObj.get("allocate_delivery_id"));
				delivery.put("allocate_delivery_code", deliveryObj.get("allocate_delivery_code"));
				delivery.put("corp_code", deliveryObj.get("corp_code"));
				delivery.put("corp_name", deliveryObj.get("corp_name"));

				delivery.put("create_time", UtilString.getShortStringForDate((Date) deliveryObj.get("create_time")));
				delivery.put("create_user", deliveryObj.get("create_user"));
				delivery.put("delivery_vehicle", deliveryObj.get("delivery_vehicle"));
				delivery.put("delivery_date",
						UtilString.getShortStringForDate((Date) deliveryObj.get("delivery_date")));
				delivery.put("delivery_driver", deliveryObj.get("delivery_driver"));
				delivery.put("delivery_phone", deliveryObj.get("delivery_phone"));
				delivery.put("status", deliveryObj.get("status"));
				delivery.put("status_name",
						EnumAllocateStatus.getDescByValue(UtilObject.getByteOrNull(delivery.get("status"))));
				delivery.put("remark", deliveryObj.get("remark"));

				Map<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("allocateDeliveryId", deliveryObj.get("allocate_delivery_id"));

				List<Map<String, Object>> deliveryItems = allocService.getDeliveryItemsByConditions(itemMap);
				if (deliveryItems != null && deliveryItems.size() > 0) {
					for (int i = 0; i < deliveryItems.size(); i++) {
						Map<String, Object> deliveryItem = deliveryItems.get(i);
						HashMap<String, Object> delivery_item = new HashMap<String, Object>();
						// 调拨单行项目ID
						delivery_item.put("allocate_item_id", deliveryItem.get("allocate_item_id"));

						delivery_item.put("allocate_id", deliveryItem.get("allocate_id"));
						delivery_item.put("allocate_code", deliveryItem.get("allocate_code"));
						// 调拨行项目
						delivery_item.put("allocate_rid", deliveryItem.get("allocate_rid"));
						delivery_item.put("delivery_item_id", deliveryItem.get("delivery_item_id"));
						// 配送单ID
						delivery_item.put("allocate_delivery_id", deliveryItem.get("allocate_delivery_id"));
						delivery_item.put("allocate_delivery_rid", deliveryItem.get("allocate_delivery_rid"));

						delivery_item.put("mat_code", deliveryItem.get("mat_code"));
						delivery_item.put("mat_name", deliveryItem.get("mat_name"));
						delivery_item.put("unit_id", deliveryItem.get("unit_id"));
						delivery_item.put("name_zh", deliveryItem.get("name_zh"));
						delivery_item.put("name_en", deliveryItem.get("name_en"));

						delivery_item.put("fty_output", deliveryItem.get("fty_output"));
						delivery_item.put("fty_output_name", deliveryItem.get("fty_output_name"));
						delivery_item.put("fty_output_code", deliveryItem.get("fty_output_code"));
						delivery_item.put("location_output", deliveryItem.get("location_output"));
						delivery_item.put("location_output_name", deliveryItem.get("location_output_name"));
						delivery_item.put("location_output_code", deliveryItem.get("location_output_code"));

						delivery_item.put("fty_input", deliveryItem.get("fty_input"));
						delivery_item.put("fty_input_name", deliveryItem.get("fty_input_name"));
						delivery_item.put("fty_input_code", deliveryItem.get("fty_input_code"));
						delivery_item.put("location_input", deliveryItem.get("location_input"));
						delivery_item.put("location_input_name", deliveryItem.get("location_input_name"));
						delivery_item.put("location_input_code", deliveryItem.get("location_input_code"));
						delivery_item.put("allocate_qty", deliveryItem.get("allocate_qty"));
						delivery_item.put("status_name", EnumAllocateStatus
								.getDescByValue(UtilObject.getByteOrNull(deliveryItem.get("status"))));
						deliveryItemList.add(delivery_item);
					}
				}

				delivery.put("item_list", deliveryItemList);

			} else {
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			logger.error("调拨单详情", e);
			return UtilResult.getResultToJson(false, errorCode, delivery);
		}

		return UtilResult.getResultToJson(true, errorCode, delivery);
	}

	//
	// @RequestMapping(value = "/batchList", method = RequestMethod.POST,
	// produces = "application/json;charset=UTF-8")
	// public @ResponseBody JSONObject findBatchList(@RequestBody JSONObject
	// linv) throws Exception {
	//
	// Map<String, Object> map = commonService.getBatchSpecList(null, 2000,
	// 241206);
	// return UtilResult.getResultToJson(true, 1, map);
	// }
}
