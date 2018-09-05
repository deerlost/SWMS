package com.inossem.wms.web.biz;

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
	@RequestMapping(value = "/biz/urgence/get_urgence_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getUrgenceList(@RequestBody JSONObject json, CurrentUser cUser) {

		List<BizUrgenceResVo> list = new ArrayList<BizUrgenceResVo>();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		boolean status = false;

		int total = 0;

		try {
			String condition = json.getString("condition").trim();
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			BizUrgenceReqVo paramVo = new BizUrgenceReqVo();

			paramVo.setCondition(condition);
			paramVo.setReceiptType((byte) json.getInt("receipt_type"));
			paramVo.setStatus((byte) json.getInt("status"));
			paramVo.setPaging(true);
			paramVo.setPageIndex(pageIndex);
			paramVo.setPageSize(pageSize);
			paramVo.setTotalCount(totalCount);
			paramVo.setSortAscend(sortAscend);
			paramVo.setSortColumn(sortColumn);

			list = urgenceInAndOutStockService.listBizUrgenceHead(paramVo);

			if (list != null && list.size() > 0) {
				total = list.get(0).getTotalCount();

			}

			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error("紧急出入库列表", e);
		}

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, list);
	}

	/**
	 * 根据单号获取紧急出入库明细
	 * 
	 * @param urgenceId
	 * @return
	 */
	@RequestMapping(value = { "/biz/urgence/get_urgence_by_id/{urgence_id}",
			"/biz/myreceipt/get_urgence_by_id/{urgence_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getUrgenceById(@PathVariable("urgence_id") Integer urgence_id, CurrentUser cUser) {

		Map<String, Object> body = urgenceInAndOutStockService.getUrgenceById(urgence_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		/*
		 * JSONObject head = new JSONObject(); head.put("error_code", 0);
		 * head.put("error_string", "Success"); head.put("page_index", 0);
		 * head.put("page_size", 1); head.put("total", 1);
		 * 
		 * Map<String, Object> obj = new HashMap<String, Object>();
		 * obj.put("head", head); obj.put("body", body);
		 */
		if (body.get("item_list") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	/**
	 * 暂存单据
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/save_order", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveOrder(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if (json.containsKey("item_list")) {
			body = urgenceInAndOutStockService.saveOrder(json, user);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} else {
			body.put("is_success", 1);
			body.put("message", "传入参数为空!");
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);

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
		if (json.containsKey("item_list")) {
			body = urgenceInAndOutStockService.subOrder(json, user);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} else {
			body.put("is_success", 1);
			body.put("message", "传入参数为空!");
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	/**
	 * 删除单据
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/delete_order/{urgence_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteOrder(@PathVariable("urgence_id") Integer urgence_id, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = urgenceInAndOutStockService.deleteOrder(urgence_id, user);

		// if ((Integer.parseInt("0")).equals(body.get("is_success"))) {
		if (body.getInt("is_success") == 0) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}
		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	/**
	 * 处理单据
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/posting_order", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject postingOrder(@RequestBody JSONObject json, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONObject body = new JSONObject();
		if (null != json) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
			body = urgenceInAndOutStockService.postingOrder(json, user);
		} else {

			body.put("is_success", 1);
			body.put("message", "传入参数为空!");
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	/**
	 * 查询关联单据
	 * 
	 * @date 2017年11月21日
	 * @author sangjl
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/biz/urgence/get_inner_order", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getInnerOrder(@RequestBody JSONObject json) {
		String condition = json.getString("condition").trim();

		String receipttype = String.valueOf(json.getInt("receipt_type"));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("condition", condition);
		param.put("locationId", json.getInt("location_id"));
		param.put("receiptType", receipttype);
		List<Map<String, Object>> list = urgenceInAndOutStockService.getInnerOrder(param);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if (list != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}
		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, list);
	}

	@RequestMapping(value = "/biz/urgence/delete_file", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject removeFile(@RequestBody JSONObject json) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fileId", json.getString("file_id"));
		param.put("receiptId", json.getString("urgence_id"));
		param.put("receiptType", json.getString("receipt_type"));

		// urgenceInAndOutStockService.deleteByUUID(param);
		JSONObject body = urgenceInAndOutStockService.deleteByUUID(param);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		if (body.getInt("is_success") == 0) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, body);

	}

}
