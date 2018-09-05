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

import com.inossem.wms.constant.Const;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizAllocateDeliveryHead;
import com.inossem.wms.model.enums.EnumAllocateStatus;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.BizAllocateHeadVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.StockBatchVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IAllocService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 调拨管理
 * @author wang.ligang
 *
 */
@Controller
@RequestMapping("/biz/alloc")
public class AllocController {
	private static final Logger logger = LoggerFactory.getLogger(AllocController.class);

//	@Autowired
//	private IAllocService allocService;
//	
//	@Autowired
//	private ICommonService commonService;
//	
//	/**
//	 * 15.1.1 调拨单列表
//	 * @param cuser
//	 * @param json {"condition":"检索条件","pageIndex":1,"pageSize":10,"total":-1}
//	 * @return
//	 */
//	@RequestMapping(value = "/alloc/get_alloc_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getAllocList(CurrentUser cuser, @RequestBody JSONObject json) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":[{\"demand_date\":\"2018-01-25\",\"allocate_id\":2,\"allocate_code\":\"11111112\",\"location_output_name\":\"大地中心供应站\",\"status_name\":\"已提交\",\"fty_output_name\":\"伊泰股份工厂\",\"status\":10,\"delivery_id_list\":[\"4\"]},{\"demand_date\":\"2018-01-25\",\"allocate_id\":3,\"allocate_code\":\"11111113\",\"location_output_name\":\"大地中心供应站\",\"status_name\":\"草稿\",\"fty_output_name\":\"伊泰股份工厂\",\"status\":0,\"delivery_id_list\":[\"4\",\"4\"]},{\"demand_date\":\"2018-01-25\",\"allocate_id\":4,\"allocate_code\":\"50000001\",\"location_output_name\":\"塔拉壕中心供应站\",\"status_name\":\"草稿\",\"fty_output_name\":\"伊泰股份工厂\",\"status\":0,\"delivery_id_list\":[]},{\"demand_date\":\"2018-01-25\",\"allocate_id\":1,\"allocate_code\":\"11111111\",\"location_output_name\":\"大地中心供应站\",\"status_name\":\"草稿\",\"fty_output_name\":\"伊泰股份工厂\",\"status\":0,\"delivery_id_list\":[\"1\"]}]}";
//		return JSONObject.fromObject(str);
//	}
//	
//	/**
//	 * 15.1.3 查询物料列表(创建调拨单对应的添加button)
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/alloc/get_materials", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getMaterials(CurrentUser cuser, @RequestBody JSONObject json) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":[{\"mat_code\":\"60000094\",\"mat_name\":\"镀锌扁钢/40*4/Q235B\",\"unit_name\":\"千克\",\"mat_id\":94,\"qty\":2,\"unit_code\":\"KG\",\"unit_id\":63}]}";
//		return JSONObject.fromObject(str);
//	}
//	
//	/**
//	 * 2.2	发出接收工厂库存地点
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/alloc/get_out_and_in_fty_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getOutAndInFtyList(CurrentUser cuser) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":{\"base_data\":{\"corp_name\":\"公司名\",\"corp_id\":123,\"org_name\":\"部门名\",\"org_id\":123,\"create_time\":\"2012-01-01\",\"create_user_name\":\"用户名\"},\"fty_input_list\":[{\"fty_id\":\"2000\",\"fty_name\":\"伊泰股份工厂\",\"location_list\":[{\"location_name\":\"酸刺沟中心供应站\",\"location_code\":\"0003\",\"location_id\":20000003},{\"location_name\":\"大地中心供应站\",\"location_code\":\"0004\",\"location_id\":20000004},{\"location_name\":\"宏一中心供应站\",\"location_code\":\"0023\",\"location_id\":20000023}],\"fty_code\":\"2000\"}],\"fty_output_list\":[{\"fty_id\":\"2000\",\"fty_name\":\"伊泰股份工厂\",\"location_list\":[{\"location_name\":\"纳林庙中心供应站\",\"location_code\":\"0002\",\"location_id\":20000002},{\"location_name\":\"酸刺沟中心供应站\",\"location_code\":\"0003\",\"location_id\":20000003}],\"fty_code\":\"2000\"}]}}";
//		return JSONObject.fromObject(str);		
//	}
//	
//	/**
//	 * 15.1.4 保存提交调拨单
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/alloc/save_alloc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject saveOrUpdateAlloc(CurrentUser cuser, @RequestBody JSONObject json) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":{\"allocate_id\":\"ZCD100001\"}}";
//		return JSONObject.fromObject(str);
//	}
//	
//	/**
//	 * 15.1.6删除调拨单
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/alloc/alloc_delete/{allocate_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject deleteAlloc(CurrentUser cuser,
//			@PathVariable("allocate_id") Integer allocate_id) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":{}}";
//		return JSONObject.fromObject(str);
//	}
//	
//	
//	/**
//	 * 15.2.1 调拨单所有行项目(配送单管理列表)
//	 * 
//	 * @param cuser
//	 * @param json
//	 * @return
//	 */
//	@RequestMapping(value = "/delivery/alloc_item_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getAllocItemList(CurrentUser cuser, @RequestBody JSONObject json) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":[{\"unit_name\":\"千克\",\"fty_input\":2000,\"fty_input_name\":\"伊泰股份工厂\",\"corp_code\":\"2000\",\"allocate_qty\":20,\"location_input_name\":\"酸刺沟中心供应站\",\"fty_output_code\":\"2000\",\"location_input_code\":\"0003\",\"mat_code\":\"60000011\",\"mat_id\":11,\"allocate_id\":1,\"location_output_code\":\"0003\",\"fty_output\":2000,\"demand_date\":\"2018-01-25\",\"fty_input_code\":\"2000\",\"item_id\":1,\"allocate_code\":\"11111111\",\"fty_output_name\":\"伊泰股份工厂\",\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"location_output\":20000003,\"mat_name\":\"热轧碳素结构钢板/δ8*1500*6000/Q235B\",\"allocate_rid\":0,\"location_input\":20000003,\"corp_id\":2000,\"location_output_name\":\"酸刺沟中心供应站\",\"unit_en\":\"KG\"},{\"unit_name\":\"千克\",\"fty_input\":2000,\"fty_input_name\":\"伊泰股份工厂\",\"corp_code\":\"2000\",\"allocate_qty\":20,\"location_input_name\":\"大地中心供应站\",\"fty_output_code\":\"2000\",\"location_input_code\":\"0004\",\"mat_code\":\"60000011\",\"mat_id\":11,\"allocate_id\":2,\"location_output_code\":\"0004\",\"fty_output\":2000,\"demand_date\":\"2018-01-25\",\"fty_input_code\":\"2000\",\"item_id\":2,\"allocate_code\":\"11111112\",\"fty_output_name\":\"伊泰股份工厂\",\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"location_output\":20000004,\"mat_name\":\"热轧碳素结构钢板/δ8*1500*6000/Q235B\",\"allocate_rid\":1,\"location_input\":20000004,\"corp_id\":2000,\"location_output_name\":\"大地中心供应站\",\"unit_en\":\"KG\"}]}";
//		return JSONObject.fromObject(str);
//	}
//	
//	
//	/**
//	 * 15.2.2 调拨单行项目id获取详情(创建配送单按钮)
//	 * 
//	 * @param cuser
//	 * @param json
//	 * @return
//	 */
//	@RequestMapping(value = "/delivery/alloc_item_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getAllocItemInfo(CurrentUser cuser, @RequestBody JSONObject json) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":{\"item_list\":[{\"fty_input\":2000,\"fty_input_name\":\"伊泰股份工厂\",\"corp_code\":\"2000\",\"allocate_qty\":2,\"location_input_name\":\"大地中心供应站\",\"delivery_item_id\":2,\"fty_output_code\":\"2000\",\"location_input_code\":\"0004\",\"mat_code\":\"60000011\",\"allocate_delivery_id\":4,\"mat_id\":11,\"allocate_id\":3,\"location_output_code\":\"0004\",\"fty_output\":2000,\"demand_date\":\"2018-01-25\",\"fty_input_code\":\"2000\",\"item_id\":4,\"allocate_code\":\"11111113\",\"fty_output_name\":\"伊泰股份工厂\",\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"location_output\":20000004,\"unit_name\":\"千克\",\"mat_name\":\"热轧碳素结构钢板/δ8*1500*6000/Q235B\",\"allocate_rid\":2,\"location_input\":20000004,\"corp_id\":2000,\"location_output_name\":\"大地中心供应站\",\"name_en\":\"KG\"}],\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"create_user\":\"佳佳\",\"create_time\":\"2018-02-27\"}}";
//		return JSONObject.fromObject(str);
//	}
//	
//	
//	/**
//	 * 15.2.3 提交配送单
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/delivery/save_delivery", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject saveDelivery(CurrentUser cuser, @RequestBody JSONObject json) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":{\"allocate_delivery_id\":\"ZCD100001\"}}";
//		return JSONObject.fromObject(str);
//	}
//	
//	
//	/**
//	 * 15.3.3删除配送单
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/delivery_query/delivery_info/{allocate_delivery_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject deleteDelivery(CurrentUser cuser,
//			@PathVariable("allocate_delivery_id") Integer allocate_delivery_id) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":1,\"total\":20},\"body\":{}}";
//		return JSONObject.fromObject(str);
//	}
//	
//	/**
//	 * 15.3.1 配送单列表(配送单查询)
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/delivery_query/delivery_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getDeliveryList(CurrentUser cuser, @RequestBody JSONObject json) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":20,\"total\":20},\"body\":[{\"allocate_delivery_code\":\"10000003\",\"delivery_date\":\"2018-02-28\",\"allocate_delivery_id\":2,\"delivery_vehicle\":\"1111a\",\"status_name\":\"待配送\",\"status\":15},{\"allocate_delivery_code\":\"10000002\",\"delivery_date\":\"2018-02-05\",\"allocate_delivery_id\":1,\"delivery_vehicle\":\"11111\",\"status_name\":\"待配送\",\"status\":15}]}";
//		return JSONObject.fromObject(str);
//	}
//	
//	
//	/**
//	 * 15.3.2 配送单详情
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = "/delivery_query/delivery_info/{allocate_delivery_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getDeliveryInfo(CurrentUser cuser,
//			@PathVariable("allocate_delivery_id") Integer allocate_delivery_id) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":20,\"total\":20},\"body\":{\"delivery_driver\":\"2222\",\"create_time\":\"2018-02-05\",\"corp_code\":\"2000\",\"status_name\":\"待配送\",\"remark\":\"aaaa\",\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"allocate_delivery_code\":\"10000002\",\"delivery_date\":\"2018-02-05\",\"allocate_delivery_id\":1,\"delivery_vehicle\":\"11111\",\"item_list\":[{\"unit_name\":\"安时\",\"fty_input\":2000,\"fty_input_name\":\"伊泰股份工厂\",\"allocate_qty\":20,\"location_input_name\":\"凯达供应站\",\"delivery_item_id\":0,\"fty_output_code\":\"2000\",\"allocate_delivery_rid\":1,\"allocate_item_id\":1,\"location_input_code\":\"0024\",\"allocate_delivery_id\":1,\"mat_code\":\"60000001\",\"allocate_id\":1,\"location_output_code\":\"0024\",\"unit_id\":1,\"fty_output\":2000,\"fty_input_code\":\"2000\",\"allocate_code\":\"11111111\",\"fty_output_name\":\"伊泰股份工厂\",\"location_output\":20000024,\"mat_name\":\"热轧碳素结构钢板/δ3*1500*6000/Q235B\",\"allocate_rid\":1,\"location_input\":20000024,\"location_output_name\":\"凯达供应站\",\"unit_en\":\"Ah\"}],\"create_user\":\"a123457\",\"delivery_phone\":\"3333\",\"status\":15}}";
//		return JSONObject.fromObject(str);
//	}
//	/*
//	 * 15.1.2 调拨单详情
//	 * 
//	 * @param cuser
//	 * @return
//	 */
//	@RequestMapping(value = { "/alloc/alloc_info/{allocate_id}",
//			"/biz/myreceipt/alloc/alloc_info/{allocate_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//	public @ResponseBody Object getAllocInfo(CurrentUser cuser, @PathVariable("allocate_id") Integer allocate_id) {
//		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"\",\"page_index\":1,\"page_size\":20,\"total\":-1},\"body\":{\"fty_input\":2000,\"fty_output\":\"2000\",\"demand_date\":\"2018-01-25\",\"fty_input_name\":\"伊泰股份工厂\",\"create_time\":\"2018-01-25\",\"create_user_name\":\"佳佳\",\"status_name\":\"已提交\",\"location_input_name\":\"酸刺沟中心供应站\",\"distributed\":10,\"remark\":\"\",\"fty_output_name\":\"伊泰股份工厂\",\"corp_name\":\"内蒙古伊泰煤炭股份有限公司\",\"location_output\":\"20000003\",\"item_list\":[{\"unit_name\":\"米\",\"fty_output\":2000,\"fty_output_name\":\"发出工厂名\",\"create_time\":\"2018-01-25\",\"item_id\":1,\"allocate_qty\":100,\"modify_time\":\"2018-02-24\",\"allocate_delivery_item_id\":\"1\",\"delivery_item_id\":1,\"location_output\":20000004,\"location_output_name\":\"发出库存地点名\",\"mat_code\":\"60000650\",\"allocate_delivery_id\":\"1\",\"mat_name\":\"普通钢丝绳/6*37-Φ15/FC\",\"mat_id\":650,\"stock_qty\":20,\"allocate_id\":1,\"allocate_rid\":1,\"unit_id\":92,\"decimal_place\":2,\"name_en\":\"M\",\"status\":10},{\"unit_name\":\"千克\",\"fty_output\":2000,\"fty_output_name\":\"发出工厂名\",\"create_time\":\"2018-01-25\",\"item_id\":2,\"allocate_qty\":9.1,\"modify_time\":\"2018-02-24\",\"allocate_delivery_item_id\":\"2\",\"delivery_item_id\":2,\"location_output\":20000004,\"location_output_name\":\"发出库存地点名\",\"mat_code\":\"60000011\",\"allocate_delivery_id\":\"4\",\"mat_name\":\"热轧碳素结构钢板/δ8*1500*6000/Q235B\",\"mat_id\":11,\"stock_qty\":10,\"allocate_id\":2,\"allocate_rid\":1,\"unit_id\":63,\"decimal_place\":3,\"name_en\":\"KG\",\"status\":10}],\"allocate_id\":1,\"allocate_code\":\"123123code\",\"location_input\":20000003,\"location_output_name\":\"酸刺沟中心供应站\",\"org_name\":\"煤炭生产事业部\",\"status\":0,\"user_list\":[{\"role_id\":111,\"role_name\":\"角色1\",\"user_id\":\"user_id\",\"user_name\":\"user_name\",\"mobile\":\"1388888888\",\"corp_name\":\"公司名\",\"org_name\":\"部门名称\"}]}}";
//		return JSONObject.fromObject(str);
//	 }
//	@RequestMapping(value = "/delivery_query/test", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject getTest(CurrentUser cuser, @RequestBody JSONObject json) {
//		System.out.println(json);
//		JSONArray ar = json.getJSONArray("stocktake_users");
//		ar.size();
//		ar.get(0).getClass();
//		String str = "{\"stocktake_users\":[\"用户userId4\",\"用户userId5\"]}";
//		return JSONObject.fromObject(str);
//	}
	
}
