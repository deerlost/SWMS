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
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IPrintService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/area")
public class WareHouseAreaController {	
	@Autowired
	private IDicWarehouseService dicWarehouseService;

	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IPrintService printService;

	private static final Logger logger = LoggerFactory.getLogger(WareHouseAreaController.class);

	@RequestMapping(value = "/warehouse_area_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseAreaList(@RequestBody JSONObject json, CurrentUser cUser) {

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
			list = dicWarehouseService.queryWarehouseAreaList(paramMap);

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

	@RequestMapping(value = "/add_or_update_warehouse_area", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdateCorp(CurrentUser cUser, @RequestBody JSONObject json) {
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		try {
			String areaId = json.getString("area_id");// 公司id
			String areaCode = json.getString("area_code");// 公司编码
			String areaName = json.getString("area_name");// 公司描述
			String whId = json.getString("wh_id");// 板块id

			String checkMethod = json.getString("check_method");// 公司id
			String typeInput = json.getString("type_input");// 公司编码
			String typeOutput = json.getString("type_output");// 公司描述
			String mix = json.getString("mix");// 公司描述
			DicWarehouseArea dw = new DicWarehouseArea();
			dw.setAreaCode(areaCode);
			dw.setAreaName(areaName);
			dw.setWhId(Integer.parseInt(whId));
			dw.setCheckMethod(Byte.parseByte(checkMethod));
			dw.setTypeInput(Byte.parseByte(typeInput));
			dw.setTypeOutput(Byte.parseByte(typeOutput));
			dw.setMix(Byte.parseByte(mix));

			body = dicWarehouseService.addOrUpdateWarehouseAreas(areaId, dw);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
			dictionaryService.refreshWarehouseArea();// 保存存储区时刷新存储区缓存数据
		} catch (Exception e) {

			logger.error("", e);
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/get_warehouse_area_by_id/{area_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseAreaById(@PathVariable("area_id") Integer area_id, CurrentUser cUser) {

		Map<String, Object> body = dicWarehouseService.getWarehouseAreaById(area_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;

		if (body.get("area_id") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/warehouse_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseList(CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			list = dicWarehouseService.queryWarehouseList();
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("仓库列表", e);
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, list);
	}


}
