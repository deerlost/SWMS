package com.inossem.wms.portable.biz;

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
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.ITransportService;
import com.inossem.wms.service.biz.IUrgenceService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 转储管理
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
	@Resource
	private ICommonService commonService;
	@Autowired
	private IUrgenceService urgenceInAndOutStockService;
	
	/**
	 * 获取基础信息
	 */
	@RequestMapping(value = "/biz/transport/get_baseinfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getBaseinfo(CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		JSONObject body = new JSONObject();
		
		try {
			JSONArray fty_list = commonService.listFtyLocationForUser(user.getUserId(),"");
			List<Map<String,Object>> moveList = transportService.getMoveList();
			
			body.put("fty_list", fty_list);
			body.put("move_list", moveList);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false ;
			logger.error("获取基础信息 --", e);
		}
		return UtilResult.getResultToJson(status,errorCode, 1, 50, 50, body);
	}
	
	@RequestMapping(value = { "/biz/transport/material_list_output" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOutMat(@RequestBody JSONObject json) {
		String condition = json.getString("condition").trim();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("condition", condition);
		param.put("locationId", json.get("location_id"));
		int moveType = UtilObject.getIntOrZero(json.get("move_type_code"));
		if(moveType%2!=0){
			if(json.has("spec_stock")){
				param.put("specStock", json.get("spec_stock"));
			}
		}else{
			param.put("specStock","");
		}
		if(json.has("receipt_type")){
			param.put("receiptType", json.get("receipt_type"));
		}
		
		List<Map<String, Object>> list = urgenceInAndOutStockService.getOutMat(param);
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		return UtilResult.getResultToJson(true, error_code, 0, 10, 5, list);
	}
	
	/**
	 * 获取目标物料
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = {"/biz/transport/get_target_material"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getTargetMaterial(String mat_code ,CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		List<DicMaterial> materialReturn =null;
		
		DicMaterial material = new DicMaterial();
		material.setCondition(mat_code);
		try {
			materialReturn = transportService.getMaterialList(material);
		} catch (Exception e) {
			logger.error("转储物料编码查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		

		return UtilResult.getResultToJson(status,error_code,msg, materialReturn);
	}
	
	
	
	/**
	 * 转储单过账
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/transport/save_transport_sap"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveTransportToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.transportPost(jsonData, user);
	}
	@RequestMapping(value = { "/biz/transport/save_transport_sap"}, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object updateTransportToSAP(@RequestBody JSONObject jsonData, CurrentUser user) {
		return this.transportPost(jsonData, user);
	}
	private JSONObject transportPost(JSONObject jsonData, CurrentUser user){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String msg = "";
		boolean status =true;
		
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
		
		return UtilResult.getResultToJson(status,error_code, msg, result.get("stock_transport_id"));
	}
}
