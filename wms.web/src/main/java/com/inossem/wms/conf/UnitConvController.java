package com.inossem.wms.conf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.unit.UnitConv;
import com.inossem.wms.service.dic.IUnitConvService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/unitconv")
public class UnitConvController {

	private static final Logger logger = LoggerFactory.getLogger(UnitConvController.class);

	@Resource
	private IUnitConvService unitConvService;

	@RequestMapping(value = "/list_unitconv", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listCorp(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<Map<String, Object>> unitConvMaps = new ArrayList<Map<String, Object>>();
		try {
			String matGroupCode = json.getString("mat_group_code");
			String matCode = json.getString("mat_code");
			String matName = json.getString("mat_name");
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("matGroupCode", matGroupCode.trim());// 查询条件
			map.put("matCode", matCode.trim());// 查询条件
			map.put("matName", matName.trim());// 查询条件
			map.put("pageSize", pageSize);
			map.put("pageIndex", pageIndex);
			map.put("paging", true);

			unitConvMaps = unitConvService.listUnitOnPaging(map);
			if (unitConvMaps != null && unitConvMaps.size() > 0) {
				total = Integer.parseInt(unitConvMaps.get(0).get("totalCount").toString());
			}

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total,
				unitConvMaps);
	}

	@RequestMapping(value = "/add_or_update_unit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdateUnit(CurrentUser cUser, @RequestBody JSONObject json) {
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		try {
			String unitConvId = json.getString("unit_conv_id");
			UnitConv unitConv = new UnitConv();
			String matGroupCode = json.getString("mat_group_code");

			unitConv.setMatId(Integer.parseInt(json.getString("mat_id")));
			unitConv.setUnitId(Integer.parseInt(json.getString("unit_id")));
			unitConv.setConstName(json.getString("const_name"));
			unitConv.setConstValue(new BigDecimal(json.getString("const_value")));

			JSONArray unitArray = json.getJSONArray("unit_array");

			body = unitConvService.addOrUpdateUnit(matGroupCode, unitConvId, unitArray, unitConv);
			if (body.getInt("is_success") == 0) {
				error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			}

		} catch (Exception e) {

			logger.error("", e);
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/delete_unit_conv", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteOrder(CurrentUser cUser, @RequestBody JSONObject json) {
		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		try {

			JSONArray unitArray = json.getJSONArray("unit_array");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("unitArray", unitArray);

			body = unitConvService.deleteOrder(param);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;

		} catch (Exception e) {

			logger.error("", e);
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/get_mat/{mat_code}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatByCode(@PathVariable("mat_code") String mat_code) throws Exception {
		Map<String, Object> body = unitConvService.getMatByCode(mat_code);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if (body.get("matCode") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}
		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/get_unitconv_by_id/{unit_conv_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getUnitConvById(@PathVariable("unit_conv_id") Integer unit_conv_id,
			CurrentUser cUser) {

		Map<String, Object> body = unitConvService.getUnitConvById(unit_conv_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;

		if (body.get("unit_conv_id") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	@RequestMapping(value = "/get_unit_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getUnitList() throws Exception {
		List<DicUnit> list = unitConvService.selectAllUnit();

		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if (list != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}
		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, list);
	}
}
