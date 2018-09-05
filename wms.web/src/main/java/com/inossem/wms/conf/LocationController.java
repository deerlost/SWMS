package com.inossem.wms.conf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.dic.IDicFactoryService;
import com.inossem.wms.service.dic.IDicStockLocationService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/loc")
public class LocationController {
	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

	@Resource
	private IDicStockLocationService dicStockLocationService;

	@Autowired
	private IDicFactoryService dicFactoryService;

	@Autowired
	private IDictionaryService dictionaryService;

	/**
	 * 库存地点列表/关键字查询
	 * 
	 * @author 刘宇 2018.02.28
	 * @return
	 */
	@RequestMapping(value = "/list_loc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listLocation(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			String condition = json.getString("condition");
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			List<Map<String, Object>> locationMaps = dicStockLocationService.listLocationOnPaging(condition, pageIndex,
					pageSize, total, sortAscend, sortColumn);
			if (locationMaps.size() > 0) {
				total = Integer.parseInt(locationMaps.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> locationMap : locationMaps) {
				JSONObject locationMapsJson = new JSONObject();
				locationMapsJson.put("location_id", locationMap.get("location_id")); // 库存地点id
				locationMapsJson.put("location_code", locationMap.get("location_code")); // 库存地点编号
				locationMapsJson.put("location_name", locationMap.get("location_name")); // 库存地点描述
				locationMapsJson.put("address_location", locationMap.get("address_location")); // 库存地点地址
				locationMapsJson.put("fty_id", locationMap.get("fty_id")); // 工厂id
				locationMapsJson.put("fty_code", locationMap.get("fty_code")); // 工厂编号
				locationMapsJson.put("fty_name", locationMap.get("fty_name")); // 工厂描述
				locationMapsJson.put("address_factory", locationMap.get("address_factory")); // 工厂地址
				locationMapsJson.put("create_time",
						UtilString.getShortStringForDate((Date) locationMap.get("create_time"))); // 创建日期
				locationMapsJson.put("corp_id", locationMap.get("corp_id")); // 公司id
				locationMapsJson.put("corp_code", locationMap.get("corp_code")); // 公司编号
				locationMapsJson.put("corp_name", locationMap.get("corp_name")); // 公司描述
				body.add(locationMapsJson);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 添加或者修改库存地点
	 * 
	 * @param 刘宇
	 *            2018.02.28
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/add_or_update_loc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdateLocation(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = true;
		String errorString = "成功";
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		String body = UtilString.STRING_EMPTY;
		try {
			boolean isAdd = json.getBoolean("is_add");// 是否添加 true = 添加 false =
														// // 修改
			String locationId = json.getString("location_id");// 库存地点id
			String locationCode = json.getString("location_code");// 库存地点编码
			String locationName = json.getString("location_name");// 库存地点描述
			String cityId = json.getString("city_id");// 城市id
			String address = json.getString("address");// 详细地址
			String createTime = json.getString("create_time");// 上线日期
			String ftyId = null;// 工厂id
			if (json.containsKey("fty_id")) {
				ftyId = json.getString("fty_id");
			}
			String status = json.getString("status");// 状态
			String freezeInput = json.getString("freeze_input");// 入库冻结
			String freezeOutput = json.getString("freeze_output");// 出库冻结出库冻结
			String enabled = json.getString("enabled");// 不可用
			Map<String, Object> errorMap = dicStockLocationService.addOrUpdateLocation(errorCode, errorString,
					errorStatus, isAdd, locationId, locationCode, locationName, cityId, address, createTime, ftyId,
					status, freezeInput, freezeOutput, enabled);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");

			dictionaryService.refreshLocation();// 保存库存地点的时候刷新库存地点的缓存数据
		} catch (Exception e) {
			errorString = "失败";
			errorStatus = false;
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	@RequestMapping(value = "/fty_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyList(CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			list = dicFactoryService.listFtyIdAndName();
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("仓库列表", e);
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, list);
	}

	@RequestMapping(value = "/get_location_by_id/{location_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseAreaById(@PathVariable("location_id") Integer location_id,
			CurrentUser cUser) {

		Map<String, Object> body = dicStockLocationService.getLocationById(location_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;

		if (body.get("location_id") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}
}
