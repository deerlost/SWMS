package com.inossem.wms.conf;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicReceiver;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IDicReceiverService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 接收方数据传输控制类
 * 
 * @author 刘宇 2018.01.23
 *
 */
@Controller
@RequestMapping("/conf/receiver")
public class ReceiverController {
	private static final Logger logger = LoggerFactory.getLogger(ReceiverController.class);

	@Resource
	private IDicReceiverService dicReceiverService;

	/**
	 * 接收方列表and条件查询
	 * 
	 * @author 刘宇 2018.01.23
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/list_receiver", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listDicReceiver(@RequestBody JSONObject json, CurrentUser user) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();

		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			String condition = json.getString("condition");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			List<DicReceiver> receivers = dicReceiverService.listDicReceiver(condition, pageIndex, pageSize, total,
					UtilString.STRING_EMPTY, 0, sortAscend, sortColumn);
			if (receivers.size() > 0) {
				total = receivers.get(0).getTotalCount();
			}
			for (DicReceiver receiver : receivers) {
				JSONObject receiverJSon = new JSONObject();
				receiverJSon.put("id", receiver.getId());// 接收方表主键
				receiverJSon.put("receive_code", receiver.getReceiveCode());// 接收方代码
				receiverJSon.put("receive_name", receiver.getReceiveName());// 接收方描述
				body.add(receiverJSon);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 获取所有接收方
	 * 
	 * @author 刘宇 2018.02.01
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/list_receiver_on_paging", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listAllDicReceiver(CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			List<DicReceiver> receivers = dicReceiverService.listAllDicReceiver();
			for (DicReceiver receiver : receivers) {
				JSONObject receiverJSon = new JSONObject();
				receiverJSon.put("id", receiver.getId());// 接收方表主键
				receiverJSon.put("receive_code", receiver.getReceiveCode());// 接收方代码
				receiverJSon.put("receive_name", receiver.getReceiveName());// 接收方描述
				body.add(receiverJSon);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 添加接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add_receiver", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addDicReceiver(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		try {
			String receiveCode = json.getString("receive_code");// 接收方代码
			String receiveName = json.getString("receive_name");// 接收方描述
			Map<String, Object> errorMap = dicReceiverService.addDicReceiver(errorCode, errorString, errorStatus,
					receiveCode, receiveName);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			errorString = "程序异常";
			errorStatus = false;
		}
		String body = UtilString.STRING_EMPTY;
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);

	}

	/**
	 * 按id刪除一個接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/delete_receiver", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteJDicReceiver(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		try {
			int id = json.getInt("id");// 接收方表主键
			Map<String, Object> errorMap = dicReceiverService.deleteDicReceiver(errorString, errorStatus, id);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);

			errorString = "程序异常";
			errorStatus = false;
		}

		String body = UtilString.STRING_EMPTY;

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);

	}

	/**
	 * 按照id修改一個接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/update_receiver", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateDicReceiver(@RequestBody JSONObject json, CurrentUser user) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;

		try {

			int id = json.getInt("id");// 接收方表主键
			String receiveCode = json.getString("receive_code");// 接收方代码
			String receiveName = json.getString("receive_name");// 接收方描述
			Map<String, Object> errorMap = dicReceiverService.updateDicReceiver(errorCode, errorString, errorStatus, id,
					receiveCode, receiveName);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			errorString = "程序异常";
			errorStatus = false;
		}
		String body = UtilString.STRING_EMPTY;
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

}
