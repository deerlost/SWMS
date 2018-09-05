package com.inossem.wms.web.auth;

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
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.auth.IApproveService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/auth/approve")
public class ApproveController {
	private static final Logger logger = LoggerFactory.getLogger(ApproveController.class);

	@Resource
	private IApproveService approveService;

	/**
	 * 审批管理列表/关键字查询
	 * 
	 * @author 刘宇 2018.03.12
	 * @return
	 */
	@RequestMapping(value = "/list_appr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listApprove(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			String condition = json.getString("condition");
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			List<Map<String, Object>> approveMaps = approveService.listApproveOnPaging(condition, pageIndex, pageSize,
					total, sortAscend, sortColumn);
			if (approveMaps.size() > 0) {
				total = Integer.parseInt(approveMaps.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> approveMap : approveMaps) {
				JSONObject approveMapsJson = new JSONObject();
				approveMapsJson.put("id", approveMap.get("id")); // 表id
				approveMapsJson.put("user_id", approveMap.get("user_id")); // 用户id
				approveMapsJson.put("user_name", approveMap.get("user_name")); // 用户名称
				approveMapsJson.put("receipt_type", approveMap.get("receipt_type")); // 类型id
				approveMapsJson.put("receipt_type_name", approveMap.get("receipt_type_name")); // 类型描述
				approveMapsJson.put("group", approveMap.get("group")); // 部门id
				approveMapsJson.put("code", approveMap.get("code")); // 部门编码
				approveMapsJson.put("name", approveMap.get("name")); // 部门描述
				approveMapsJson.put("fty_id", approveMap.get("fty_id")); // 工厂id
				approveMapsJson.put("fty_code", approveMap.get("fty_code")); // 工厂编码
				approveMapsJson.put("fty_name", approveMap.get("fty_name")); // 工厂描述
				approveMapsJson.put("node", approveMap.get("node")); // 节点名称
				body.add(approveMapsJson);
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
	 * 添加或者修改审批管理
	 *
	 * @param 刘宇
	 *            2018.03.12
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/add_or_update_appr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdateApprove(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = true;
		String errorString = "成功";
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		String body = UtilString.STRING_EMPTY;
		try {
			String id = json.getString("id");// 表id
			String userId = json.getString("user_id");// 用户id
			String receiptType = json.getString("receipt_type");// 类型id
			String group = json.getString("group");// 组id
			String node = json.getString("node");// 节点名称
			Map<String, Object> errorMap = approveService.addOrUpdateApprove(errorString, errorStatus, id, userId,
					receiptType, group, node);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
		} catch (Exception e) {
			errorString = "失败";
			errorStatus = false;
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 获取一个审批管理
	 * 
	 * @author 刘宇 2018.03.13
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/get_appr", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getApprove(Integer id, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONObject body = new JSONObject();
		try {
			Map<String, Object> approveMap = approveService.getApproveByUserId(id);
			if (approveMap != null) {
				JSONObject approveMapJson = new JSONObject();
				approveMapJson.put("id", approveMap.get("id")); // 表id
				approveMapJson.put("user_id", approveMap.get("user_id")); // 用户id
				approveMapJson.put("user_name", approveMap.get("user_name")); // 用户名称
				approveMapJson.put("receipt_type", approveMap.get("receipt_type")); // 类型id
				approveMapJson.put("receipt_type_name", approveMap.get("receipt_type_name")); // 类型描述
				approveMapJson.put("group", approveMap.get("group")); // 部门id
				approveMapJson.put("code", approveMap.get("code")); // 部门编码
				approveMapJson.put("name", approveMap.get("name")); // 部门描述
				approveMapJson.put("node", approveMap.get("node")); // 节点名称
				body = approveMapJson;
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				errorStatus = true;
				errorString = "成功";
			} else {
				errorStatus = false;
				errorString = "失败";
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 删除审批管理
	 *
	 * @param 刘宇
	 *            2018.03.12
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/delete_appr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteApprove(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = true;
		String errorString = "成功";
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		String body = UtilString.STRING_EMPTY;
		try {
			String id = json.getString("id");// 表id
			approveService.deleteApprove(id);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorString = "失败";
			errorStatus = false;
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

}
