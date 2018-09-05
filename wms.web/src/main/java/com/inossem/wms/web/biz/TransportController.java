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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.BizStockTransportHeadVo;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.ITransportService;
import com.inossem.wms.service.biz.IUrgenceService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 转储管理
 * 
 * @author 高海涛
 * @date 2018年2月5日
 */
@Controller
public class TransportController {

	private static final Logger logger = LoggerFactory.getLogger(TransportController.class);

	@Resource
	private ITransportService transportService;
	@Resource
	private IDictionaryService dictionaryService;
	@Autowired
	private IUrgenceService urgenceInAndOutStockService;
	
	/**
	 * 分页参数
	 * 
	 * @param json
	 * @return
	 */
	private Map<String, Object> getParamMapToPageing(JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();

		int pageIndex = json.getInt("page_index");
		int pageSize = json.getInt("page_size");
		int totalCount = EnumPage.TOTAL_COUNT.getValue();
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

		param.put("paging", true);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		param.put("sortAscend", sortAscend);
		param.put("sortColumn", sortColumn);

		return param;
	}

	/**
	 * 查询出库物料
	 * 
	 * @date 2017年11月21日
	 * @author sangjl
	 * @param json
	 * @return
	 */
	@RequestMapping(value = { "/biz/transport/material_list_output" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

	public @ResponseBody JSONObject getOutMat(@RequestBody JSONObject json) {
		String condition = json.getString("condition").trim();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("condition", condition);
		param.put("locationId", json.get("location_id"));
		int moveType = UtilObject.getIntOrZero(json.get("move_type_code"));
		if(moveType%2!=0){
			if (json.has("spec_stock")) {
				param.put("specStock", UtilObject.getStringOrEmpty(json.get("spec_stock")));
			}
		}else{
			param.put("specStock", "");
		}
		if (json.has("receipt_type")) {
			param.put("receiptType", json.get("receipt_type"));
		}

		List<Map<String, Object>> list = urgenceInAndOutStockService.getOutMat(param);
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		return UtilResult.getResultToJson(true, error_code, 0, 10, 5, list);
	}
	
	/**
	 * 获取移动类型
	 * 
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/transport/get_move_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMoveList(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> move = null;
		try {
			move = transportService.getMoveList();
		} catch (Exception e) {
			logger.error("转储移动类型查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, move);
	}

	/**
	 * 获取目标物料
	 * 
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/transport/get_target_material" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getTargetMaterial(String mat_code, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		List<DicMaterial> materialReturn = null;
		Integer matId = null;
		DicMaterial material = new DicMaterial();
		material.setMatCode(mat_code);
		try {
			materialReturn = transportService.getMaterialList(material);
			if (materialReturn.size() == 1) {
				msg = "合法物料编码";
				matId = materialReturn.get(0).getMatId();
			} else {
				msg = "物料编码不存在";
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			}
		} catch (Exception e) {
			logger.error("转储物料编码查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, msg, matId);
	}

	/**
	 * 获取转储单列表
	 * 
	 * @param keyWord
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = {
			"/biz/transport/get_order_list" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderList(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> param = this.getParamMapToPageing(json);
		try {

			JSONArray array = json.getJSONArray("list");
			if (array != null && array.size() > 0) {
				param.put("keyword", UtilObject.getStringOrEmpty(json.getString("keyword"))); // 关键字
				param.put("receiptType", json.get("list"));
				param.put("locationId", cUser.getLocationList());
				list = transportService.getOrderList(param);

				if (list != null && list.size() > 0) {
					Long totalCountLong = (Long) list.get(0).get("totalCount");
					total = totalCountLong.intValue();
				}
			}

		} catch (Exception e) {
			logger.error("转储列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, list);
	}

	/**
	 * 获取转储明细
	 * 
	 * @param stock_output_id
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = { "/biz/transport/get_order_view",
			"/biz/myreceipt/transport/get_order_view" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderView(String stock_transport_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		BizStockTransportHeadVo head = null;
		try {
			head = transportService.getOrderView(stock_transport_id);
		} catch (Exception e) {
			logger.error("转储概览页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, head);
	}

	/**
	 * 删除转储单
	 * 
	 * @param stock_output_id
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/transport/delete_order" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> deleteOrder(int stock_transport_id, int receipt_type, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String msg = "";
		boolean status = true;

		try {
			msg = transportService.deleteTransportOrder(stock_transport_id, receipt_type);
		} catch (Exception e) {
			logger.error("删除转储单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "删除失败";
		}

		return UtilResult.getResultToMap(status, error_code, msg, null);
	}

	/**
	 * 转储单过账
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/transport/save_transport_sap" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveTransportToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String msg = "";
		boolean status = true;

		JSONObject result = new JSONObject();

		try {
			result = transportService.saveTransportData(jsonData, user, false);
			msg = transportService.saveOrderToFinish(result, user.getUserId());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("转储单过账 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result.get("stock_transport_id"));
	}

}
