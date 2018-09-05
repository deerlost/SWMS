package com.inossem.wms.portable.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.BizUrgenceReqVo;
import com.inossem.wms.model.vo.BizUrgenceResVo;
import com.inossem.wms.service.biz.IUrgenceService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
public class UrgenceController {

	@Autowired
	private IUrgenceService urgenceInAndOutStockService;
	
	
	
	private static final Logger logger = LoggerFactory.getLogger(UrgenceController.class);
	
	/**
	 * 获取列表
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/get_urgence_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getUrgenceList(String condition, CurrentUser cUser) {
		
	
		List<BizUrgenceResVo> list = new ArrayList<BizUrgenceResVo>();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		
		try {
			
			BizUrgenceReqVo paramVo = new BizUrgenceReqVo();
			paramVo.setCondition(condition);
			list = urgenceInAndOutStockService.listBizUrgenceForApp(paramVo);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("紧急出入库列表", e);
		}
		
		
		return UtilResult.getResultToJson(status,error_code, list);
	}
	
	
	/**
	 * 根据单号获取紧急出入库明细
	 * 
	 * @param urgenceId
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/get_urgence_by_id/{urgence_id}" , method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getUrgenceById(@PathVariable("urgence_id") Integer urgence_id, CurrentUser cUser) {

		Map<String, Object> body = urgenceInAndOutStockService.getUrgenceById(urgence_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if(body.get("item_list")!=null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			 status = true;
		}
		return UtilResult.getResultToJson(status,error_code, body);
	}
	
	
	/**
	 * 提交单据
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/sub_order", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject subOrder(@RequestBody JSONObject json, CurrentUser user) {
		

		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if (json.containsKey("urgence_id")) {
			body = urgenceInAndOutStockService.subOrderApp(json, user);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} else {
			body.put("is_success", 1);
			body.put("message", "传入参数为空!");
		}

		return UtilResult.getResultToJson(status,error_code,body);
	}
	
	/**
	 * 删除文件
	 * @author sangjl
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/delete_file", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject removeFile(@RequestBody JSONObject json) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fileId", json.getString("file_id"));
		param.put("receiptId", json.getString("urgence_id"));
		param.put("receiptType", json.getString("receipt_type"));
		
		//urgenceInAndOutStockService.deleteByUUID(param);
		JSONObject body = 	urgenceInAndOutStockService.deleteByUUID(param);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if(body.getInt("is_success")==0) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}
		
		
		return UtilResult.getResultToJson(status,error_code,body);
		
	}
}
