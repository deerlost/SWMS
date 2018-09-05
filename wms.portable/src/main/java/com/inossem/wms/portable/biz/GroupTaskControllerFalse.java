package com.inossem.wms.portable.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/22")
public class GroupTaskControllerFalse {

	private static final Logger logger = LoggerFactory.getLogger(GroupTaskControllerFalse.class);

	@Autowired
	private ICommonService commonService;

	/**
	 * 免检原因,库存地点list
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/group_task/base_info" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getBaseInfo(CurrentUser user) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		JSONArray locationList = new JSONArray();
		try {
			locationList = commonService.listLocationForUser(user.getUserId(),"2");
		} catch (Exception e) {
			logger.error("库存地点list", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), locationList);
		return obj;
	}

	// 上架作业-校验code类型
	@RequestMapping(value = "/biz/group_task/check_code_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer item_id = null;// 上架作业请求行项目id
		Integer location_id = null; // 库存地点id
		String condition = ""; // 查询条件

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"1\",\"pallet_id\":\"5\",\"pallet_code\":\"1112\",\"max_payload\":\"1\",\"have_payload\":\"1\",\"wh_id\":\"\",\"wh_code\":\"\",\"wh_name\":\"\",\"area_name\":\"\",\"area_code\":\"\",\"area_id\":\"\",\"position_id\":\"\",\"position_code\":\"\",\"position_name\":\"\"}}";
		int test = json.getInt("test");
		if (test == 1) {

		} else if (test == 2) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"2\",\"pallet_id\":\"\",\"pallet_code\":\"\",\"package_id\":\"4\",\"package_code\":\"444444444\",\"wh_id\":\"\",\"area_id\":\"\",\"position_id\":\"\",\"package_list\":[{}]}}";
		} else if (test == 3) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"3\",\"pallet_id\":\"\",\"pallet_code\":\"\",\"max_payload\":\"\",\"have_payload\":\"\",\"wh_id\":\"1\",\"wh_code\":\"11\",\"wh_name\":\"111\",\"area_name\":\"222\",\"area_code\":\"22\",\"area_id\":\"2\",\"position_id\":\"3\",\"position_code\":\"33\",\"position_name\":\"333\"}}";
		} else if (test == 4) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"3\",\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"1\",\"area_id\":\"1\",\"position_id\":\"1\",\"package_list\":[]}}";
		} else if (test == 5) {
			jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"type\":\"1\",\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"\",\"package_code\":\"\",\"wh_id\":\"1\",\"area_id\":\"2\",\"position_id\":\"33\",\"package_list\":[]}}";
		}

		return JSONObject.fromObject(jsonStr);

	}

	// 上架作业-申请单详情
	@RequestMapping(value = "/biz/group_task/biz_stock_task_req_item_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqDetails(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":[{\"stock_task_req_item_id\":\"1\",\"stock_task_req_rid\":\"1\",\"mat_id\":\"1\",\"mat_code\":\"41000000001\",\"mat_name\":\"转向拉杆03069391-0020\",\"unit_zh\":\"件\",\"package_type_name\":\"PAC100\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":\"12\",\"unstock_task_qty\":\"100\",\"decimal_place\":\"0\",\"production_batch\":\"100000001\",\"erp_batch\":\"100000001\",\"mat_store_type\":\"1\",\"work_model\":\"1\",\"stock_task_req_id\":\"51\",\"stock_task_req_code\":\"3500000020\",\"refer_receipt_code\":\"1212121212\",\"refer_receipt_id\":\"1212121212\",\"refer_receipt_type\":\"41\",\"refer_type_name\":\"销售出库单\",\"pallet_package_list\":[{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"1\",\"package_code\":\"1\",\"package_type_code\":\"PAC100\",\"package_type_id\":\"1\",\"qty\":\"100\",\"batch\":\"100024445\"}]}]}";
		return JSONObject.fromObject(jsonStr);

	}
	
	
	// 上架作业-校验code类型
		@RequestMapping(value = "/biz/group_task/check_package_code_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
		public @ResponseBody JSONObject checkPackageCodeType(@RequestBody JSONObject json, CurrentUser cUser) {

			Integer item_id = null;// 上架作业请求行项目id
			Integer location_id = null; // 库存地点id
			String condition = ""; // 查询条件

			String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"package_id\":\"5\",\"package_code\":\"1112\",\"package_type_code\":\"1\",\"package_type_id\":\"1\",\"qty\":\"\"}}";
			

			return JSONObject.fromObject(jsonStr);

		}
	
}
