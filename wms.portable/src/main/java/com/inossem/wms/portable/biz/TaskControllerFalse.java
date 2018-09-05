package com.inossem.wms.portable.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.InventoryException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.key.DicWarehousePositionKey;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;
import com.inossem.wms.model.vo.BizStockTaskReqHeadVo;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/task/false")
public class TaskControllerFalse {

	// 上架作业请求--列表

	@RequestMapping(value = "/shelves/biz_stock_task_req_head_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqHeadList(CurrentUser cUser, String condition) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":[{\"stock_task_req_id\":\"231\",\"stock_task_req_code\":\"3500000151\",\"location_name\":\"大地中心供应站\",\"type_name\":\"领料出库单\",\"fty_name\":\"工厂\",\"refer_receipt_code\":\"1212121212\",\"create_time\":\"2018-04-10\"}]}";
		return JSONObject.fromObject(jsonStr);
	}

	// 上架作业-申请单详情
	@RequestMapping(value = "/shelves/biz_stock_task_req_details", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject bizStockTaskReqDetails(String stock_task_req_id, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"stock_task_req_id\":\"51\",\"stock_task_req_code\":\"3500000020\",\"create_user_name\":\"佳佳\",\"receipt_type\":\"41\",\"refer_receipt_code\":\"1212121212\",\"create_time\":\"2018-03-02\",\"wh_id\":\"4\",\"type_name\":\"销售出库单\",\"class_type_list\":[{\"class_type_id\":\"1\",\"class_type_name\":\"白班\",\"class_type_start\":\"08:00\",\"class_type_end\":\"17:00\"},{\"class_type_id\":\"2\",\"class_type_name\":\"晚班\",\"class_type_start\":\"17:00\",\"class_type_end\":\"24:00\"},{\"class_type_id\":\"3\",\"class_type_name\":\"夜班\",\"class_type_start\":\"00:00\",\"class_type_end\":\"08:00\"}],\"item_list\":[{\"item_id\":\"1\",\"stock_task_req_rid\":\"1\",\"mat_code\":\"41000000001\",\"mat_name\":\"转向拉杆03069391-0020\",\"unit_zh\":\"件\",\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":\"12\",\"unstock_task_qty\":\"100\",\"decimal_place\":\"0\",\"production_batch\":\"100000001\",\"mat_store_type\":\"1\",\"work_model\":\"1\",\"batch\":\"1245457742\",\"package_type_name\":\"PAC10\",\"package_type_code\":\"PAC10\",\"package_type_id\":\"1\",\"pallet_package_list\":[{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"1\",\"package_code\":\"1111111\",\"package_type_code\":\"PAC10\",\"package_type_id\":\"1\",\"qty\":\"10\",\"batch\":\"1245457742\"},{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"2\",\"package_code\":\"222222\",\"package_type_code\":\"PAC10\",\"package_type_id\":\"1\",\"qty\":\"10\",\"batch\":\"1245457742\"},{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"3\",\"package_code\":\"3333333\",\"package_type_code\":\"PAC10\",\"package_type_id\":\"1\",\"qty\":\"10\",\"batch\":\"1245457742\"},{\"pallet_id\":\"1\",\"pallet_code\":\"1\",\"package_id\":\"4\",\"package_code\":\"444444444\",\"package_type_code\":\"PAC10\",\"package_type_id\":\"1\",\"qty\":\"10\",\"batch\":\"1245457742\"},{\"pallet_id\":\"2\",\"pallet_code\":\"2\",\"package_id\":\"5\",\"package_code\":\"5\",\"package_type_code\":\"PAC30\",\"package_type_id\":\"3\",\"qty\":\"30\",\"batch\":\"1245457742\"},{\"pallet_id\":\"2\",\"pallet_code\":\"2\",\"package_id\":\"6\",\"package_code\":\"6\",\"package_type_code\":\"PAC30\",\"package_type_id\":\"3\",\"qty\":\"30\",\"batch\":\"1245457742\"}]},{\"item_id\":\"2\",\"stock_task_req_rid\":\"2\",\"mat_code\":\"41000000021\",\"mat_name\":\"222222转向拉杆03069391-0020\",\"unit_zh\":\"件\",\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":\"12\",\"unstock_task_qty\":\"88\",\"decimal_place\":\"0\",\"production_batch\":\"100000001\",\"mat_store_type\":\"2\",\"work_model\":\"1\",\"batch\":\"1245457743\",\"package_type_name\":\"PAC10\",\"package_type_code\":\"PAC10\",\"package_type_id\":\"1\",\"pallet_package_list\":[]}]}}";
		return JSONObject.fromObject(jsonStr);

	}

	// 上架作业-校验code类型
	@RequestMapping(value = "/shelves/check_code_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {

		Integer item_id = null;// 上架作业请求行项目id
		Integer location_id = null; // 库存地点id
		String condition = ""; // 查询条件

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

	// 上架作业--上架
	@RequestMapping(value = "/shelves/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject submit(@RequestBody JSONObject json, CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"上架成功\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"0\"},\"body\":{}}";
		return JSONObject.fromObject(jsonStr);
	}

	// 上架作业-新增托盘
	@RequestMapping(value = "/shelves/get_new_pallet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getNewPallet(CurrentUser cUser) {

		String jsonStr = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":20,\"total\":5},\"body\":{\"pallet_id\":\"40048001\",\"pallet_code\":\"D01\",\"max_payload\":\"D01大件区\"}}";

		return JSONObject.fromObject(jsonStr);

	}

	

}
