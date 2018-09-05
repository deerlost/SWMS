package com.inossem.wms.conf;

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

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/wh")
public class WarehouseController {

	@Autowired
	private IDicWarehouseService dicWarehouseService;

	@Autowired
	private IDictionaryService dictionaryService;

	private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

	@RequestMapping(value = "/warehouse_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String condition = json.getString("condition");

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("condition", condition);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);

			list = dicWarehouseService.queryWarehouseList(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("仓库列表", e);
		}

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, list);
	}

	@RequestMapping(value = "/add_or_update_warehouse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdateWarehouse(CurrentUser cUser, @RequestBody JSONObject json) {
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		try {
			String whId = json.getString("wh_id");// 公司id
			String whCode = json.getString("wh_code");// 公司编码
			String whName = json.getString("wh_name");// 公司描述
			String address = json.getString("address");// 板块id
			DicWarehouse dw = new DicWarehouse();
			dw.setWhCode(whCode);
			dw.setWhName(whName);
			dw.setAddress(address);

			JSONArray locationArray = json.getJSONArray("location_id");

			body = dicWarehouseService.addOrUpdateWarehouse(whId, locationArray, dw);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
			dictionaryService.refreshWarehouse();// 保存仓库时刷新仓库缓存数据
			dictionaryService.refreshLocation();// 刷新库存地点
		} catch (Exception e) {

			logger.error("", e);
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/get_warehouse_by_id/{wh_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseById(@PathVariable("wh_id") Integer wh_id, CurrentUser cUser) {

		Map<String, Object> body = dicWarehouseService.getWarehouseById(wh_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;

		if (body.get("wh_id") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/location_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getLocationList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}

			String condition = json.getString("condition");

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("condition", condition);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", totalCount);
			paramMap.put("paging", true);

			list = dicWarehouseService.queryLoationList(paramMap);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("库存列表", e);
		}

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, list);
	}
}
