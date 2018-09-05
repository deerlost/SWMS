package com.inossem.wms.conf;

import java.util.ArrayList;
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
import com.inossem.wms.model.auth.Department;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IDepartmentService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 审批部门数据传输控制类
 * 
 * @author 刘宇 2018.01.24
 *
 */
@Controller
@RequestMapping("/conf/approvedept")
public class DepartmentController {

	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	@Resource
	private IDepartmentService departmentService;

	/**
	 * 审批部门列表and关键字查询
	 * 
	 * @author 刘宇 2018.01.24
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/list_approve_dept", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listDepartment(@RequestBody JSONObject json, CurrentUser user) {
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
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			String condition = json.getString("condition");
			List<Map<String, Object>> objDepartments = departmentService.listDepartment(pageIndex, pageSize, total,
					condition, UtilString.STRING_EMPTY, 0, 0, sortAscend, sortColumn);
			if (objDepartments.size() > 0) {
				total = Integer.parseInt(objDepartments.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> mapSPBM : objDepartments) {

				JSONObject spbmJSon = new JSONObject();
				spbmJSon.put("department_id", mapSPBM.get("department_id")); // 审批部门表主键
				spbmJSon.put("code", mapSPBM.get("code")); // 审批部门编号
				spbmJSon.put("name", mapSPBM.get("name")); // 审批部门描述
				spbmJSon.put("fty_id", mapSPBM.get("fty_id")); // 工厂id
				spbmJSon.put("fty_name", mapSPBM.get("fty_name")); // 工厂描述
				body.add(spbmJSon);
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

	@RequestMapping(value = "/list_department_by_ftyid", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listDepartmentByFtyId(int fty_id, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = false;
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		try {
			body = departmentService.listDepartmentByFtyId(fty_id);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
		} catch (Exception e) {
			// TODO: handle exception
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, body);

	}

	/**
	 * 获取所有工厂
	 * 
	 * @author 刘宇 2018.01.24
	 * @return
	 */
	@RequestMapping(value = "/get_factory", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFactory() {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		JSONArray body = new JSONArray();
		try {
			List<DicFactory> objsList = departmentService.listFactory();
			for (DicFactory t001w : objsList) {
				JSONObject zwempfJSon = new JSONObject();
				zwempfJSon.put("fty_id", t001w.getFtyId()); // 工厂id
				zwempfJSon.put("fty_name", t001w.getFtyName()); // 工厂描述
				body.add(zwempfJSon);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);

	}

	/**
	 * 添加审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add_approve_dept", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addDepartment(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		try {
			String code = json.getString("code");
			String name = json.getString("name");
			Integer ftyId = json.getInt("fty_id");
			Map<String, Object> errorMap = departmentService.addDepartment(errorCode, errorString, errorStatus, code,
					name, ftyId);
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
	 * 修改审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/update_approve_dept", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateDepartment(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		try {
			int departmentId = json.getInt("department_id"); // 审批部门表主键id
			String code = json.getString("code"); // 审批部门编号
			String name = json.getString("name"); // 审批部门描述
			Integer ftyId = json.getInt("fty_id"); // 工厂编号
			Map<String, Object> errorMap = departmentService.updateDepartment(errorCode, errorString, errorStatus,
					departmentId, code, name, ftyId);
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
	 * 查找所有部门
	 * 
	 * @author 刘宇 2018.03.12
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/list_all_dept", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listAllDepartment(CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			List<Department> objDepartments = departmentService.listAlldepartment();
			for (Department objDepartment : objDepartments) {
				JSONObject spbmJSon = new JSONObject();
				spbmJSon.put("department_id", objDepartment.getDepartmentId()); // 审批部门表id
				spbmJSon.put("code", objDepartment.getCode()); // 审批部门编号
				spbmJSon.put("name", objDepartment.getName()); // 审批部门描述
				body.add(spbmJSon);
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

}
