package com.inossem.wms.portable.biz;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/22")
public class ProductionTransportControllerFalse {

	// 发出工厂 接收工厂 库存地点

	@RequestMapping(value = "/biz/product/transport/base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject baseInfo(CurrentUser cUser, String condition) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"fty_list_out\":[{\"fty_id\":\"2000\",\"fty_code\":\"2000\",\"fty_name\":\"伊泰股份工厂\",\"location_ary\":[{\"loc_id\":\"20000003\",\"loc_code\":\"0003\",\"loc_name\":\"2000003酸刺沟中心供应站\"},{\"loc_id\":\"20000004\",\"loc_code\":\"0004\",\"loc_name\":\"20000004酸刺沟中心供应站\"}]},{\"fty_id\":\"1000\",\"fty_code\":\"1000\",\"fty_name\":\"伊泰股份工厂\",\"location_ary\":[{\"loc_id\":\"10000003\",\"loc_code\":\"0003\",\"loc_name\":\"10000003酸刺沟中心供应站\"},{\"loc_id\":\"10000004\",\"loc_code\":\"0004\",\"loc_name\":\"10000004酸刺沟中心供应站\"}]}],\"fty_list_in\":[{\"fty_id\":\"2000\",\"fty_code\":\"2000\",\"fty_name\":\"伊泰股份工厂\",\"location_ary\":[{\"loc_id\":\"20000003\",\"loc_code\":\"0003\",\"loc_name\":\"2000003酸刺沟中心供应站\"},{\"loc_id\":\"20000004\",\"loc_code\":\"0004\",\"loc_name\":\"20000004酸刺沟中心供应站\"}]},{\"fty_id\":\"1000\",\"fty_code\":\"1000\",\"fty_name\":\"伊泰股份工厂\",\"location_ary\":[{\"loc_id\":\"10000003\",\"loc_code\":\"0003\",\"loc_name\":\"10000003酸刺沟中心供应站\"},{\"loc_id\":\"10000004\",\"loc_code\":\"0004\",\"loc_name\":\"10000004酸刺沟中心供应站\"}]}],\"class_type_list\":[{\"class_type_id\":\"1\",\"class_type_name\":\"白班\",\"class_type_start\":\"8:00\",\"class_type_end\":\"17:00\"},{\"class_type_id\":\"2\",\"class_type_name\":\"夜班\",\"class_type_start\":\"18:00\",\"class_type_end\":\"24:00\"},{\"class_type_id\":\"3\",\"class_type_name\":\"晚班\",\"class_type_start\":\"00:00\",\"class_type_end\":\"8:00\"}]}}";
		return JSONObject.fromObject(jsonStr);
	}

	// 物料--列表

	@RequestMapping(value = "/biz/product/transport/material_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialList(@RequestBody JSONObject json, CurrentUser cUser) {
		// 仓位库存连接 批次库存
		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"0\",\"page_size\":\"10\",\"total\":\"5\"},\"body\":[{\"mat_code\":\"60000648\",\"mat_id\":\"648\",\"mat_name\":\"普通钢丝绳/6*19-Φ18.5/FC\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC100\",\"package_type_name\":\"PVC100\",\"production_batch\":\"12345678\",\"erp_batch\":\"21232131\",\"qty\":\"3000\",\"package_num\":\"30\",\"name_zh\":\"米\",\"unit_code\":\"M\",\"unit_id\":\"92\",\"decimal_place\":\"2\",\"mat_store_type\":\"2\"},{\"mat_code\":\"60000648\",\"mat_id\":\"648\",\"mat_name\":\"普通钢丝绳/6*19-Φ18.5/FC\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC100\",\"package_type_name\":\"PVC100\",\"production_batch\":\"12345677\",\"erp_batch\":\"21232132\",\"qty\":\"1500\",\"package_num\":\"15\",\"name_zh\":\"米\",\"unit_code\":\"M\",\"unit_id\":\"92\",\"decimal_place\":\"2\",\"mat_store_type\":\"2\"}]}";
		return JSONObject.fromObject(jsonStr);
	}

	// 物料--详情
	@RequestMapping(value = "/biz/product/transport/material_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialInfo(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"0\",\"page_size\":\"10\",\"total\":\"5\"},\"body\":{\"mat_code\":\"60000648\",\"mat_id\":\"648\",\"mat_name\":\"普通钢丝绳/6*19-Φ18.5/FC\",\"package_type_id\":\"1\",\"package_type_code\":\"PVC100\",\"package_type_name\":\"PVC100\",\"production_batch\":\"12345678\",\"erp_batch\":\"21232131\",\"qty\":\"300\",\"package_num\":\"3\",\"name_zh\":\"米\",\"unit_code\":\"M\",\"unit_id\":\"92\",\"decimal_place\":\"2\",\"pallet_package_list\":[{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"1\",\"package_code\":\"1111111\",\"package_type_code\":\"PVC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"batch\":\"1245457742\"},{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"2\",\"package_code\":\"222222\",\"package_type_code\":\"PVC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"batch\":\"1245457742\"},{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"3\",\"package_code\":\"3333333\",\"package_type_code\":\"PAC10\",\"package_type_id\":\"1\",\"qty\":\"100\",\"batch\":\"1245457742\"}]}}";
		return JSONObject.fromObject(jsonStr);
	}

	// 校验code类型
	@RequestMapping(value = "/biz/product/transport/check_code_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"1\",\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"\",\"area_id\":\"\",\"position_id\":\"\",\"package_list\":[{\"package_id\":\"1\",\"package_code\":\"1111111\"},{\"package_id\":\"2\",\"package_code\":\"222222\"},{\"package_id\":\"3\",\"package_code\":\"3333333\"},{\"package_id\":\"4\",\"package_code\":\"444444444\"}]}}";
		int test = json.getInt("test");
		if (test == 1) {

		} else if (test == 2) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"2\",\"pallet_id\":\"\",\"pallet_code\":\"\",\"package_id\":\"4\",\"package_code\":\"444444444\",\"wh_id\":\"\",\"area_id\":\"\",\"position_id\":\"\",\"package_list\":[{}]}}";
		} else if (test == 3) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"3\",\"pallet_id\":\"\",\"pallet_code\":\"\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"1\",\"area_id\":\"2\",\"position_id\":\"33\",\"package_list\":[{}]}}";
		} else if (test == 4) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"1\",\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"1\",\"area_id\":\"1\",\"position_id\":\"1\",\"package_list\":[]}}";
		} else if (test == 5) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"1\",\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"1\",\"area_id\":\"2\",\"position_id\":\"33\",\"package_list\":[]}}";
		}

		return JSONObject.fromObject(jsonStr);

	}

	// 新增托盘
	@RequestMapping(value = "/biz/product/transport/get_new_pallet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getNewPallet(CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":20,\"total\":5},\"body\":{\"pallet_id\":\"40048001\",\"pallet_code\":\"D01\",\"max_payload\":\"D01大件区\"}}";

		return JSONObject.fromObject(jsonStr);

	}

	// 生成转运单
	@RequestMapping(value = "/biz/product/transport/productTransport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addProductTransport(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"上架成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"0\"},\"body\":{}}";
		return JSONObject.fromObject(jsonStr);
	}

}
