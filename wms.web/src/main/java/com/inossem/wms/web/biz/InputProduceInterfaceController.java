package com.inossem.wms.web.biz;

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
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IInputProduceInterfaceService;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

/**
 * 生产入库接口
 * @author libo
 *
 */
@Controller
@RequestMapping("/wms/open/input/")
public class InputProduceInterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(InputProduceInterfaceController.class);
	 

	@Autowired
	private IInputProduceInterfaceService iInputProduceTransportService;
	
	
	@RequestMapping(value = "/production", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateOtherInput(CurrentUser user,@RequestBody JSONObject json) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		BizStockInputHead record=new BizStockInputHead();
		try {
			record=iInputProduceTransportService.insertProduction(user,json);			
			status=true;
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增生产入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}
		body.put("stock_input_id", UtilString.getStringOrEmptyForObject(record.getStockInputId()));
		body.put("stock_input_code", UtilString.getStringOrEmptyForObject(record.getStockInputCode()));
		body.put("status", status);
		body.put("code", errorCode);
		body.put("msg", errorString);
//		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return body;
	}
	
	
	
	
}
