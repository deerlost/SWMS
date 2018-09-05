package com.inossem.wms.web.biz;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/task/false")
public class DisTaskControllerFalse {
	// 下架作业请求--列表

	@RequestMapping(value = "/removal/biz_stock_task_req_head_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqHeadList(CurrentUser cUser, String condition) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":[{\"stock_task_req_id\":\"231\",\"stock_task_code\":\"3500000151\",\"stock_task_id\":\"231\",\"stock_task_req_code\":\"3500000151\",\"location_name\":\"大地中心供应站\",\"type_name\":\"领料出库单\",\"fty_name\":\"工厂\",\"refer_receipt_code\":\"1212121212\",\"create_time\":\"2018-04-10\",\"create_user\":\"aaa\",\"status_name\":\"fff\"}]}";
		return JSONObject.fromObject(jsonStr);
	}

	// 下架作业-申请单详情
	@RequestMapping(value = "/removal/biz_stock_task_req_details", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqDetails(String stock_task_req_id, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"stock_task_req_id\":\"51\",\"stock_task_req_code\":\"3500000020\",\"create_user_name\":\"佳佳\",\"receipt_type\":\"22\",\"refer_receipt_code\":\"1212121212\",\"create_time\":\"2018-03-02\",\"wh_id\":\"4\",\"type_name\":\"销售出库单\",\"class_type_list\":[{\"class_type_id\":\"1\",\"class_type_name\":\"白班\",\"class_type_start\":\"08:00\",\"class_type_end\":\"16:00\"},{\"class_type_id\":\"2\",\"class_type_name\":\"晚班\",\"class_type_start\":\"17:00\",\"class_type_end\":\"24:00\"},{\"class_type_id\":\"3\",\"class_type_name\":\"夜班\",\"class_type_start\":\"01:00\",\"class_type_end\":\"8:00\"}],\"item_list\":[{\"stock_task_req_rid\":\"1\",\"mat_code\":\"41000000001\",\"mat_name\":\"转向拉杆03069391-0020\",\"unit_zh\":\"件\",\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":\"12\",\"unstock_task_qty\":\"100\",\"decimal_place\":\"0\",\"production_batch\":\"100000001\",\"mat_store_type\":\"2\",\"work_model\":\"1\",\"package_type_name\":\"PAC100\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"pallet_package_list\":[{\"pallet_id\":\"1\",\"max_payload\":\"1000\",\"have_payload\":\"100\",\"package_num\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"1\",\"package_code\":\"1\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"batch\":\"100024445\"}]}],\"task_list\":[{\"stock_task_id\":15,\"stock_task_code\":\"3600000018\",\"create_time\":\"2018-02-06\",\"location_name\":\"0049-红庆河中心供应站\",\"user_name\":\"佳佳\",\"status\":1,\"status_name\":\"已上架\"},{\"stock_task_id\":36,\"stock_task_code\":\"3600000035\",\"create_time\":\"2018-02-08\",\"location_name\":\"0049-红庆河中心供应站\",\"user_name\":\"佳佳\",\"status\":1,\"status_name\":\"已上架\"}]}}";
		return JSONObject.fromObject(jsonStr);

	}
	
	
	

	// 下架作业-申请单详情
	@RequestMapping(value = "/removal/check_code_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer item_id = null;// 上架作业请求行项目id
		Integer location_id = null; // 库存地点id
		String condition = ""; // 查询条件

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"1\",\"pallet_id\":\"5\",\"pallet_code\":\"1112\",\"max_payload\":\"1\",\"have_payload\":\"1\",\"package_num\":\"2\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"\",\"area_id\":\"40048001\",\"position_id\":\"111\",\"package_list\":[{\"package_id\":\"6\",\"package_code\":\"4500036466-0000002\"}]}}";

		return JSONObject.fromObject(jsonStr);

	}

	// 下架作业--下架
	@RequestMapping(value = "/removal/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject submit(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"上架成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"0\"},\"body\":{}}";
		return JSONObject.fromObject(jsonStr);
	}

	// 下架作业-1.1.8 获取新托盘
	@RequestMapping(value = "/removal/get_new_pallet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getNewPallet(Integer pallet_type_id, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":20,\"total\":5},\"body\":{\"pallet_id\":\"40048001\",\"pallet_code\":\"D01\",\"max_payload\":\"D01大件区\"}}";

		return JSONObject.fromObject(jsonStr);

	}

	// 下架作业-1.1.3 根据库存地点获取存储区
	@RequestMapping(value = "/removal/area_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getAreaList(Integer location_id, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":20,\"total\":5},\"body\":[{\"wh_id\":\"1\",\"area_id\":\"40048001\",\"area_code\":\"D01\",\"area_name\":\"D01大件区\",\"position_list\":[{\"position_id\":\"111\",\"position_code\":\"aaa\",\"position_name\":\"bbb\"}]}]}";

		return JSONObject.fromObject(jsonStr);

	}

	

	// 下架作业-1.1.7 获取下架单详情
	@RequestMapping(value = "/removal/get_inner_task_details", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getInnerTaskDetails(String stock_task_req_id, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"stock_task_id\":\"51\",\"stock_task_code\":\"3500000020\",\"stock_task_req_id\":\"51\",\"stock_task_req_code\":\"3500000020\",\"create_user_name\":\"佳佳\",\"receipt_type\":\"22\",\"refer_receipt_code\":\"1212121212\",\"create_time\":\"2018-03-02\",\"wh_id\":\"4\",\"type_name\":\"销售出库单\",\"class_type_id\":\"1\",\"class_type_list\":[{\"class_type_id\":\"1\",\"class_type_name\":\"白班\",\"class_type_start\":\"08:00\",\"class_type_end\":\"16:00\"},{\"class_type_id\":\"2\",\"class_type_name\":\"晚班\",\"class_type_start\":\"17:00\",\"class_type_end\":\"24:00\"},{\"class_type_id\":\"3\",\"class_type_name\":\"夜班\",\"class_type_start\":\"01:00\",\"class_type_end\":\"8:00\"}],\"item_list\":[{\"item_id\":\"1\",\"stock_task_rid\":\"1\",\"stock_task_req_rid\":\"1\",\"mat_code\":\"41000000001\",\"mat_name\":\"转向拉杆03069391-0020\",\"unit_zh\":\"件\",\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":\"12\",\"unstock_task_qty\":\"100\",\"decimal_place\":\"0\",\"production_batch\":\"100000001\",\"mat_store_type\":\"1\",\"work_model\":\"1\",\"package_type_name\":\"PAC100\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"pallet_package_list\":[{\"pallet_id\":\"1\",\"max_payload\":\"1000\",\"have_payload\":\"100\",\"package_num\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"1\",\"package_code\":\"1\",\"wh_id\":\"1\",\"area_id\":\"40048001\",\"position_id\":\"111\",\"wh_code\":\"1\",\"area_code\":\"1\",\"position_code\":\"1\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"batch\":\"100024445\"}]},{\"item_id\":\"2\",\"stock_task_rid\":\"2\",\"stock_task_req_rid\":\"2\",\"mat_code\":\"41000000002\",\"mat_name\":\"转向拉杆203069391-0020\",\"unit_zh\":\"件\",\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":\"12\",\"unstock_task_qty\":\"100\",\"decimal_place\":\"0\",\"production_batch\":\"100000001\",\"mat_store_type\":\"1\",\"work_model\":\"2\",\"package_type_name\":\"PAC100\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"pallet_package_list\":[{\"pallet_id\":\"\",\"max_payload\":\"\",\"have_payload\":\"\",\"package_num\":\"1\",\"pallet_code\":\"\",\"package_id\":\"1\",\"package_code\":\"1\",\"wh_id\":\"1\",\"area_id\":\"40048001\",\"position_id\":\"111\",\"wh_code\":\"1\",\"area_code\":\"1\",\"position_code\":\"1\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"batch\":\"100024445\"}]},{\"item_id\":\"1\",\"stock_task_rid\":\"1\",\"stock_task_req_rid\":\"1\",\"mat_code\":\"41000000001\",\"mat_name\":\"转向拉杆03069391-0020\",\"unit_zh\":\"件\",\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":\"12\",\"unstock_task_qty\":\"100\",\"qty\":\"100\",\"decimal_place\":\"0\",\"production_batch\":\"100000001\",\"mat_store_type\":\"2\",\"work_model\":\"3\",\"package_type_name\":\"PAC100\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"pallet_package_list\":[{\"pallet_id\":\"\",\"max_payload\":\"\",\"have_payload\":\"\",\"package_num\":\"\",\"pallet_code\":\"\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"1\",\"area_id\":\"40048001\",\"position_id\":\"111\",\"wh_code\":\"1\",\"area_code\":\"1\",\"position_code\":\"1\",\"package_type_code\":\"\",\"package_type_id\":\"\",\"qty\":\"100\",\"batch\":\"100024445\"}]}]}}";
		return JSONObject.fromObject(jsonStr);

	}

	@RequestMapping(value = {"/removal/position_list" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject positionList(@RequestBody JSONObject json) {

		int position_id = json.getInt("position_id");
		String jsonStr = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":20,\"total\":5},\"body\":{\"position_id\":\"1\",\"haven_material\":\"41000000009\\\\油嘴00901257-0008\\\\-1.000\\\\149,41000000037\\\\油泵电机00600128-0670\\\\11.000\\\\149,60000648\\\\普通钢丝绳/6*19-Φ18.5/FC\\\\1.000\\\\92,60000648\\\\普通钢丝绳/6*19-Φ18.5/FC\\\\25.000\\\\92,60027169\\\\乙炔气/≥2.5KGMPa/40L/瓶\\\\10.000\\\\5,60074324\\\\销冲/6*150mm/捷科/123106\\\\95.000\\\\149,60074416\\\\包胶美工刀/18mm手锁式/捷科/130153\\\\100.000\\\\149\"}}";
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		jsonObj.getJSONObject("body").put("position_id", position_id);
		return jsonObj;
	}
	
	
}
