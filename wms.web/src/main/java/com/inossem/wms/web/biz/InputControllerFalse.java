package com.inossem.wms.web.biz;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/22")
public class InputControllerFalse {
	
	private static final Logger logger = LoggerFactory.getLogger(InputControllerFalse.class);

	@Autowired
	private IInputService rkglService;

	@Autowired
	private ICommonService commonService;

	// 其他入库-1.1 获取移动类型及库存地点
	@RequestMapping(value = "/biz/input/other/other_base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject otherBaseInfo(CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"fty_list\":[{\"fty_id\":\"2000\",\"fty_code\":\"2000\",\"fty_name\":\"伊泰股份工厂\",\"location_ary\":[{\"loc_id\":\"20000003\",\"loc_code\":\"0003\",\"loc_name\":\"酸刺沟中心供应站\"}]}],\"move_list\":[{\"move_type_code\":\"Z93\",\"move_type_id\":\"260093\",\"move_type_name\":\"供应商免费赠送\",\"spec_stock\":\"\"}]}}";
		return JSONObject.fromObject(jsonStr);

	}
	
	
	
	

	// 其他入库-1.3 其他入库查询物料列表
	@RequestMapping(value = "/biz/input/other/get_material_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialList(String condition, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"0\",\"page_size\":\"10\",\"total\":\"5\"},\"body\":[{\"mat_code\":\"60000648\",\"mat_group_code\":\"013202\",\"mat_group_id\":\"13202\",\"mat_group_name\":\"钢丝绳\",\"mat_id\":\"648\",\"mat_name\":\"普通钢丝绳/6*19-Φ18.5/FC\",\"mat_type\":\"ZWL\",\"name_zh\":\"米\",\"unit_code\":\"M\",\"unit_id\":\"92\",\"decimal_place\":\"2\"}]}";
		return JSONObject.fromObject(jsonStr);

	}

	// 其他入库-1.4 根据物料获取包装类型
	@RequestMapping(value = "/biz/input/other/get_package_type_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getPackageTypeList(String mat_id, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"0\",\"page_size\":\"10\",\"total\":\"5\"},\"body\":[{\"package_type_id\":\"1\",\"package_type_code\":\"PVA20\",\"package_type_name\":\"PVA-20\",\"package_standard\":\"20\"},{\"package_type_id\":\"2\",\"package_type_code\":\"PVA30\",\"package_type_name\":\"PVA-30\",\"package_standard\":\"30\"},{\"package_type_id\":\"3\",\"package_type_code\":\"PVA10\",\"package_type_name\":\"PVA-10\",\"package_standard\":\"10\"}]}";
		return JSONObject.fromObject(jsonStr);

	}

	// 其他入库-1.5 MES半成品数量查询接口
	@RequestMapping(value = "/biz/input/other/get_mes_qty", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMESQty(String mat_code, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"0\",\"page_size\":\"10\",\"total\":\"5\"},\"body\":[{\"mat_code\":\"60000648\",\"mat_name\":\"普通钢丝绳/6*19-Φ18.5/FC\",\"qty\":\"M\"}]}";
		return JSONObject.fromObject(jsonStr);

	}

	// 其他入库-1.6 其他入库提交
	@RequestMapping(value = "biz/input/other/other", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getInnerTaskDetails(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"0\",\"page_size\":\"10\",\"total\":\"5\"},\"body\":{\"stock_input_code\":\"33344444444\",\"stock_input_id\":\"1\"}}";
		return JSONObject.fromObject(jsonStr);

	}

	// 其他入库-1.7 其他入库列表
	@RequestMapping(value = "/biz/input/other/other_input_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject otherInputList(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":10,\"total\":8},\"body\":[{\"stock_input_code\":\"T3100000017\",\"fty_name\":\"工厂\",\"location_name\":\"库存地点\",\"create_time\":\"2018-02-11\",\"stock_input_id\":\"20\",\"move_type_name\":\"供应商免费赠送\",\"status_name\":\"未入库\",\"create_user_name\":\"佳佳\",\"status\":\"0\"}]}";
		return JSONObject.fromObject(jsonStr);

	}

	// 其他入库-1.8 其他入库详情
	@RequestMapping(value = "/biz/input/other/other_input_info/{stock_input_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject otherInputInfo(@PathVariable("stock_input_id") Integer stock_input_id,
			CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":20,\"total\":0},\"body\":{\"create_user_name\":\"佳佳\",\"remark\":\"666\",\"fty_code\":\"2000\",\"move_type_id\":\"511\",\"location_name\":\"大地中心供应站\",\"receipt_type\":\"25\",\"fty_name\":\"伊泰股份工厂\",\"stock_input_code\":\"3100000016\",\"create_time\":\"2018-02-06\",\"fty_id\":\"2000\",\"move_type_name\":\"供应商免费赠送\",\"location_code\":\"0004\",\"status\":\"10\",\"move_type_code\":\"511\",\"spec_stock\":\"\",\"location_id\":\"20000004\",\"stock_input_id\":\"19\",\"status_name\":\"已入库\",\"item_list\":[{\"decimal_place\":0,\"item_id\":30,\"stock_input_id\":19,\"stock_input_rid\":1,\"mat_code\":\"41000000009\",\"mat_id\":241213,\"mat_name\":\"油嘴00901257-0008\",\"name_zh\":\"件\",\"qty\":10,\"remark\":\"\",\"write_off\":0,\"package_item_list\":[{\"package_type_id\":\"1\",\"package_type_code\":\"1\",\"package_type_name\":\"1\",\"package_num\":\"1\",\"qty\":\"5\",\"remark\":\"111\",\"production_batch\":\"5\",\"erp_batch\":\"5\",\"quality_batch\":\"5\"},{\"package_type_id\":\"2\",\"package_type_code\":\"2\",\"package_type_name\":\"2\",\"package_num\":\"1\",\"qty\":\"5\",\"remark\":\"111\",\"production_batch\":\"5\",\"erp_batch\":\"5\",\"quality_batch\":\"5\"}]}]}}";
		return JSONObject.fromObject(jsonStr);

	}
}
