package com.inossem.wms.web.biz;

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

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.service.biz.IPrintService;
import com.inossem.wms.service.intfc.ICwLimsWs;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 打印使用的controller
 * 
 * @author ebola
 *
 */
@Controller
@RequestMapping("/biz/print")
public class PrintController {

	private static final Logger logger = LoggerFactory.getLogger(PrintController.class);
	@Autowired
	private IPrintService printService;

	/**
	 * 打印标签
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/cwLabelZpl", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject print(@RequestBody JSONObject json, CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		String msg = "";
		try {
			body = printService.printZpl(json, cUser.getUserId());
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, errorCode, msg,body);
	}

	@RequestMapping(value = "/print_input_for_batch/{stockInputId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printInputForBatch(@PathVariable("stockInputId") Integer stockInputId,CurrentUser user) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();

		boolean status = false;

		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printInputBatch(stockInputId);

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + fileName
						+ ".xls");
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	@RequestMapping(value = "/print_stock_batch_for_batch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printStockBatchForBatch(@RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = new JSONObject();
		try {
			String fileName = printService.printStockBatchForBatch(json);
			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + fileName
						+ ".xls");
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, body);

	}

	@RequestMapping(value = "/print_for_position", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printForPosition(@RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printForPosition(json);

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + fileName
						+ ".xls");
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	@RequestMapping(value = "/print_stock_position_for_batch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printStockPositionForBatch(@RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printStockPosition(json);

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + fileName
						+ ".xls");
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	@RequestMapping(value = "/print_urgence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printUrgence(@RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printUrgence(json);

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + fileName
						+ ".xls");
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	@RequestMapping(value = "/printCGDDForMatnr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printCGDDForMatnr(@RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printCGDDForMatnr(json);

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				// java.net.URI uri = new
				// java.net.URI(PropertiesUtil.getInstance().getPringUrl() +
				// "/wms/print/outputfiles/label/" + fileName + ".xls");
				// java.awt.Desktop.getDesktop().browse(uri);
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + fileName
						+ ".xls");
				status = true;
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	// 存储区管理 打印仓位
	@RequestMapping(value = "/print_for_area_and_position", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printForAreaAndPosition(@RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int areaId = json.getInt("area_id");// 存储区
			String areaCode = json.getString("area_code");// 存储区code
			String minPositionCode = json.getString("min_position_code");// 从仓位code开始
			String maxPositionCode = json.getString("max_position_code");// 到仓位code
			int minCount = json.getInt("min_count");// 从多少条开始
			int maxCount = json.getInt("max_count");// 到多少条
			map.put("areaId", areaId);
			map.put("minPositionCode", minPositionCode);
			map.put("maxPositionCode", maxPositionCode);
			map.put("minCount", minCount);
			map.put("maxCount", maxCount);
			List<DicWarehousePosition> positionList = printService.selectPositionForPrint(map);

			String fileName = "";
			JSONObject jsonData = new JSONObject();
			JSONArray ary = new JSONArray();
			if (positionList != null && positionList.size() > 0) {
				for (DicWarehousePosition dicWarehousePosition : positionList) {
					JSONObject object = new JSONObject();
					object.put("area_code", areaCode);
					object.put("position_code", dicWarehousePosition.getPositionCode());
					ary.add(object);
				}
				jsonData.put("area_position_list", ary);
				fileName = printService.printForPosition(jsonData);
			}

			body.put("fileName", fileName);

			if (!"".equals(fileName)) {
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + fileName
						+ ".xls");
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				status = true;
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	/**
	 * @Description: TODO(打印生产转运单)</br>
	 * @Title: printProductTrans </br>
	 * @param @param
	 *            stockTransportId
	 * @param @return
	 *            设定文件</br>
	 * @return Object 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/print_product_trans/{stock_transport_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printProductTrans(@PathVariable("stock_transport_id") int stockTransportId,
			CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printProductTrans(stockTransportId, null);

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

	/**
	 * @Description: TODO(打印erp库存)</br>
	 * @Title: printErpStock </br>
	 * @param @param
	 *            fty_id
	 * @param @param
	 *            location_id
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return Object 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/print_erp_stock", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printErpStock(String fty_id, String location_id, CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printErpStock(fty_id, location_id, cUser);

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

	/**
	 * @Description: TODO(打印发货单)</br>
	 * @Title: printOutput </br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return Object 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/print_output/{allocate_cargo_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printOutput(@PathVariable("allocate_cargo_id") Integer allocate_cargo_id) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printOutput(allocate_cargo_id);

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

	/**
	 * @Description: TODO(打印质检单)</br>
	 * @Title: printLims </br>
	 * @param @param
	 *            code
	 * @param @return
	 *            设定文件</br>
	 * @return Object 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/print_lims_web", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printLimsWeb(@RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();

		try {
			String samp_name = json.getString("samp_name");
			String batch_name = json.getString("product_batch");
			String specification = json.getString("specification");
			/*
			 * if(json.containsKey("product_batch")){ batch_array =
			 * json.getString("product_batch").split(","); } for(int
			 * i=0;i<batch_array.length;i++){ batch_name=batch_array[i]; }
			 */
			String fileName = printService.printLims(samp_name, batch_name, specification);
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

	@RequestMapping(value = "/print_lims", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printLims(@RequestBody JSONObject jsona) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		
		String[] batch_array ={};
		String batch_name = "";
		String fileName = "";
		JSONArray jsonarray  = jsona.getJSONArray("param");
		try {
			
			for(int i=0;i<jsonarray.size();i++){
				JSONObject json = jsonarray.getJSONObject(i);
				String samp_name = json.getString("samp_name");
				String specification = json.getString("specification");
				batch_array = json.getString("product_batch").split(",");
				for (int j = 0; j < batch_array.length; j++) {
					batch_name = batch_array[j];
					fileName = printService.printLims(samp_name, batch_name, specification);
				}
			}
			// String batch_name = json.getString("product_batch");
		
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

	/**
	 * @Description: TODO(打印下架)</br>
	 * @Title: printDownTask </br>
	 * @param @param
	 *            stockTaskId
	 * @param @return
	 *            设定文件</br>
	 * @return Object 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/print_down_task/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printDownTask(@PathVariable("stock_task_id") int stockTaskId) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printTask(stockTaskId, EnumReceiptType.STOCK_TASK_REMOVAL.getValue(), null);

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

	/**
	 * @Description: TODO(打印上架)</br>
	 * @Title: printUpTask </br>
	 * @param @param
	 *            stockTaskId
	 * @param @return
	 *            设定文件</br>
	 * @return Object 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/print_up_task/{stock_task_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printUpTask(@PathVariable("stock_task_id") int stockTaskId) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printTask(stockTaskId, EnumReceiptType.STOCK_TASK_LISTING.getValue(), null);

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

	/**
	 * @Description: TODO(打印转储单)</br>
	 * @Title: printTransport </br>
	 * @param @param
	 *            stockTransportId
	 * @param @return
	 *            设定文件</br>
	 * @return Object 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/print_stock_transport/{stock_transport_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printTransport(@PathVariable("stock_transport_id") int stockTransportId) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {

			String fileName = printService.printTransport(stockTransportId);

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
