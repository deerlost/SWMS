package com.inossem.wms.portable.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.service.biz.IPrintService;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;
@Controller
public class PrintController {
	
	private static final Logger logger = LoggerFactory.getLogger(PrintController.class);
	@Autowired
	private IPrintService printService;
	
	
	@RequestMapping(value = "/biz/print/print_protrans_portable/{stock_transport_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printProductTrans(@PathVariable("stock_transport_id") int stockTransportId,CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printProductTrans(stockTransportId,cUser.getUserId());

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/table/"
						+ fileName.substring(0, fileName.length() - 4) + ".xls");
				body.put("fileNameUrl", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/table/"
						+ fileName);
			}

		} catch (Exception e) {
			logger.error("=============", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, errorCode, body);
	}
	
	/**
	* @Description: TODO(打印下架)</br>
	   @Title: printDownTask </br>
	* @param @param stockTaskId
	* @param @return    设定文件</br>
	* @return Object    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = "/biz/print/print_down_task_portable/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printDownTask(@PathVariable("stock_task_id") int stockTaskId,CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printTask(stockTaskId, EnumReceiptType.STOCK_TASK_REMOVAL.getValue(),cUser.getUserId());

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/table/"
						+ fileName.substring(0, fileName.length() - 4) + ".xls");
				body.put("fileNameUrl", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/table/"
						+ fileName);
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, errorCode, body);
	}
	
	/**
	* @Description: TODO(上架)</br>
	   @Title: printUpTask </br>
	* @param @param stockTaskId
	* @param @param cUser
	* @param @return    设定文件</br>
	* @return Object    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = "/biz/print/print_up_task_portable/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printUpTask(@PathVariable("stock_task_id") int stockTaskId,CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printTask(stockTaskId, EnumReceiptType.STOCK_TASK_LISTING.getValue(), cUser.getUserId());

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/table/"
						+ fileName.substring(0, fileName.length() - 4) + ".xls");
				body.put("fileNameUrl",
						UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/table/" + fileName);
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, errorCode, body);
	}
}
