package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumFtyType;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IInputProduceTransportService;
import com.inossem.wms.service.biz.MaterialTransportService;
import com.inossem.wms.service.biz.impl.InputTransportServiceImpl;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.web.auth.UserController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Package com.inossem.wms.web.biz
 * @ClassName：MaterialTransportController
 * @Description :物料转储
 * @anthor：王洋
 * @date：2018年5月9日 下午5:37:35 @版本：V1.0
 */
@Controller
public class MaterialTransportController {

	private static final Logger logger = LoggerFactory.getLogger(MaterialTransportController.class);
	@Resource
	private MaterialTransportService mtransportService;

	@Resource
	UserController userController;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Resource
	InputTransportServiceImpl inputTransportService;
	
	@Autowired
	private IInputProduceTransportService iInputProduceTransportService;

	@Resource
	ICommonService commonService;

	@Autowired
	IDictionaryService dictionaryService;

	/**
	 * @Title: getParamMapToPageing </br>
	 * @Description: 分页</br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws</br>
	 */
	private Map<String, Object> getParamMapToPageing(JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();

		int pageIndex = json.getInt("page_index");
		int pageSize = json.getInt("page_size");
		int totalCount = EnumPage.TOTAL_COUNT.getValue();
		JSONArray statusList = null;
		boolean sortAscend = true;
		String sortColumn = "";
		if (json.containsKey("total")) {
			totalCount = json.getInt("total");
		}
		if (json.containsKey("sort_ascend")) {
			sortAscend = json.getBoolean("sort_ascend");
		}
		if (json.containsKey("sort_column")) {
			sortColumn = json.getString("sort_column");
		}
		if (json.containsKey("status_list")) {
			statusList = json.getJSONArray("status_list");
		}

		param.put("paging", true);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		param.put("sortAscend", sortAscend);
		param.put("sortColumn", sortColumn);
		param.put("statusList", statusList);

		return param;
	}

	/**
	 * @Description: TODO(获取移 动类型)</br>
	 * @Title: getMoveList </br>
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/get_move_info" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMoveList(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<DicMoveType> move = null;
		try {
			move = mtransportService.getMoveList();
		} catch (Exception e) {
			logger.error("转储移动类型查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, move);
	}

	/**
	 * @Description: TODO(获取发出工厂及库存信息)</br>
	 * @Title: getOutFactoryInfo </br>
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/get_out_factory_info/{move_type_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOutFactoryInfo(@PathVariable("move_type_id") int move_type_id,
			CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String userId = cUser.getUserId();
		JSONArray ftyAry = new JSONArray();
		try {
			/*
			 * if (move_type_id == 15) { ftyAry =
			 * commonService.listFtyLocationForUser(userId, String.valueOf(2));
			 * } else { ftyAry = commonService.listFtyLocationForUser(userId,
			 * null); }
			 */
			ftyAry = commonService.listFtyLocationForUser(userId, null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取发出工厂及库存信息--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, ftyAry);
	}

	/**
	 * @Description: TODO(获取接收工厂及库存信息)</br>
	 * @Title: getInFactoryInfo </br>
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = {
			"/biz/transport/get_in_factory_info/{move_type_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getInFactoryInfo(CurrentUser cUser,
			@PathVariable("move_type_id") int move_type_id) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String userId = cUser.getUserId();
		List<DicStockLocationVo> locationList = new ArrayList<DicStockLocationVo>();
		JSONArray body = new JSONArray();
		try {
			if (move_type_id == 15) {
				body = commonService.listFtyLocationForUser(userId, null);
			} else if (move_type_id == 19) {
				body = commonService.listFtyLocationForUser(userId, null);
			} else {
				body = commonService.listFtyLocationForUser(userId, null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取接受工厂及库存--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}

	/**
	 * @Description: TODO 获取物料转储单列表</br>
	 * @Title: getDumpListInfo </br>
	 * @param @param
	 *            json
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/get_dumplist_info" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDumpListInfo(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;

		List<Map<String, Object>> dumplist = new ArrayList<Map<String, Object>>();
		Map<String, Object> param = this.getParamMapToPageing(json);

		// JSONArray array = json.getJSONArray("status_list");

		String condition = json.getString("condition").trim();
		if (null != condition && !condition.equals("")) {

			if (condition.equals("工厂转储")) {
				param.put("condition", "15");
			} else if (condition.equals("TF转储采购物料到物料")) {
				param.put("condition", "17");
			} else if (condition.equals("TF厂内移储")) {
				param.put("condition", "19");
			} else {
				param.put("condition", condition);
			}
		}
		param.put("receiptType", EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue());
		param.put("currentUser", cUser.getUserId());
		try {

			// param.put("locationId", cUser.getLocationList());
			dumplist = mtransportService.getDumpList(param);

			if (dumplist != null && dumplist.size() > 0) {
				Long totalCountLong = (Long) dumplist.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
		} catch (Exception e) {
			logger.error("物料转储单列表查询", e);
			// TODO: handle exception
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, dumplist);

	}

	/**
	 * @Description: TODO(获取物料转储明细)</br>
	 * @Title: getDumpItem </br>
	 * @param @param
	 *            stock_transport_id
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/get_dump_items" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDumpItemc(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int i = UtilObject.getIntegerOrNull(json.get("stock_transport_id"));
		Map<String, Object> head = new HashMap<String, Object>();
		try {
			head = mtransportService.getDumpByTransportID(i);
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(status, error_code, head);

	}

	/**
	 * @Description: TODO(查询物料)</br>
	 * @Title: getMaterialInfo </br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/get_material_info" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialInfo(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		/*
		 * int fty_id = json.getInt("fty_id"); int loc_id =
		 * json.getInt("loc_id");
		 */
		Map<String, Object> mat = new HashMap<String, Object>();
		mat.put("keyword1", json.get("mat_code"));
		mat.put("keyword2", json.get("batch"));
		mat.put("ftyId", json.get("fty_id"));
		mat.put("locationId", json.get("location_id"));
		mat.put("isDefault", 0);
		mat.put("stockTypeId", EnumStockType.STOCK_TYPE_ERP.getValue());
		List<Map<String, Object>> item_list = null;
		try {

			Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
			DicStockLocationVo locationObj = locationMap.get(UtilObject.getIntegerOrNull(mat.get("locationId")));
			if (locationObj.getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
				mat.put("isDefault", 1);
			}
			item_list = commonService.getMatListByPosition(mat);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("获取物料信息--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		// Map<String, Object> mat_batch_map = null;
		return UtilResult.getResultToJson(status, error_code, item_list);
	}

	/**
	 * @Description: TODO(创建转储表)</br>
	 * @Title: createDump </br>
	 * @param @param
	 *            json
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/create_dump" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject createDump(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int stock_transport_id = 0;
		try {
			stock_transport_id = mtransportService.InsertStockTransportHeadCw(json, cUser.getUserId());

		} catch (Exception e) {
			logger.error("创建转储单----", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("stock_transport_id", UtilObject.getIntOrZero(stock_transport_id));

		return UtilResult.getResultToJson(status, error_code, body);
	}

	/**
	 * @Description: TODO(获取班次及同步类型 车次及司机信息)</br>
	 * @Title: getClass </br>
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/get_class" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getClass(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> json = new HashMap<String, Object>();
		try {
			json.put("create_user", cUser.getUserName());
			String time = mtransportService.getNowTime();
			json.put("create_time", time);
			json.put("syn_type_id", "2");
			json.put("class_type_id", inputTransportService.selectNowClassType());

			List<Map<String, Object>> list = inputTransportService.selectAllclassType();
			json.put("class_type", list);

			JSONArray myJsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("syn_type_name", EnumSynType.MES_SAP_SYN.getName());
			jsonObject.put("syn_type", EnumSynType.MES_SAP_SYN.getValue());
			myJsonArray.add(jsonObject);

			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("syn_type_name", EnumSynType.MES_SAP_SYN.getName());
			jsonObject1.put("syn_type", EnumSynType.MES_SAP_SYN.getValue());
			myJsonArray.add(jsonObject1);

			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("syn_type_name", EnumSynType.NO_SYN.getName());
			jsonObject2.put("syn_type", EnumSynType.NO_SYN.getValue());
			myJsonArray.add(jsonObject2);
			json.put("syn_type", myJsonArray);

			List<Map<String, Object>> vehArray = commonService.selectVehicleByProductId(cUser.getUserId());
			json.put("vehicle_list", vehArray);
			List<Map<String, Object>> driverArray = commonService.selectDriverByProductlineId(cUser.getUserId());
			json.put("driver_list", driverArray);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取班次及同步类型----", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, json);
	}

	@RequestMapping(value = {
			"/biz/transport/is_move" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject isMove(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> body = new HashMap<String, Object>();
		List<Map<String, Object>> mat = mtransportService
				.getMaterial(UtilObject.getStringOrNull(json.get("mat_input")));
		if (UtilObject.getIntegerOrNull(json.get("move_type_id")) == 17 && mat != null && mat.size() > 0) {
			body.put("keyword", true);
		} else {
			body.put("keyword", false);
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}

	@RequestMapping(value = {
			"/biz/transport/getMaterial" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterial(String matcode) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> inputmat = new ArrayList<Map<String, Object>>();
		try {
			inputmat = mtransportService.getMaterial(matcode);
		} catch (Exception e) {
			logger.error("=================", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, error_code, inputmat);
	}

	/**
	 * @Description: TODO(删除)</br>
	 * @Title: delectDump </br>
	 * @param @param
	 *            stockTransportId
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/transport/delete_dump/{stockTransportId}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject delectDump(@PathVariable("stockTransportId") Integer stockTransportId) {
		int error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		boolean status = false;
		String msg = "";
		try {
			mtransportService.deleteDump(stockTransportId);
			msg = "删除成功";
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			status = false;
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error("=================", e);
			status = false;
			msg = "删除失败";
		}
		return UtilResult.getResultToJson(status, error_code, msg, "");
	}

	/**
	* @Description: TODO(获得目标ERP批次)</br>
	   @Title: getERP </br>
	* @param @param mat_id
	* @param @param fty_id
	* @param @param package_id
	* @param @return    设定文件</br>
	* @return JSONObject    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = {
			"/biz/transport/get_erp" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getERP(String mat_id,Integer fty_id,Integer package_id  ) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> erplist = new ArrayList<Map<String, Object>>();
		try {
			erplist =mtransportService.selectERP(mat_id,fty_id,package_id);	
		} catch (Exception e) {
			logger.error("=================", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, error_code, erplist);
	}

}
