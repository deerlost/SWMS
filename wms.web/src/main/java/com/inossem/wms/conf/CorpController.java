package com.inossem.wms.conf;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.dic.IDicBoardService;
import com.inossem.wms.service.dic.IDicCorpService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 公司
 * 
 * @author 刘宇 2018.03.01
 *
 */
@Controller
@RequestMapping("/conf/corp")
public class CorpController {
	private static final Logger logger = LoggerFactory.getLogger(CorpController.class);

	@Resource
	private IDicCorpService dicCorpService;
	@Resource
	private IDictionaryService dictionaryService;
	@Resource
	private IDicBoardService dicBoardService;

	/**
	 * 公司列表/关键字查询
	 * 
	 * @author 刘宇 2018.02.28
	 * @return
	 */
	@RequestMapping(value = "/list_corp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listCorp(@RequestBody JSONObject json, CurrentUser user) {
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
			List<Map<String, Object>> corpMaps = dicCorpService.listCorpOnPaging(condition, pageIndex, pageSize, total,
					sortAscend, sortColumn);
			if (corpMaps.size() > 0) {
				total = Integer.parseInt(corpMaps.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> corpMap : corpMaps) {
				JSONObject corpMapsJson = new JSONObject();
				corpMapsJson.put("corp_id", corpMap.get("corp_id")); // 公司id
				corpMapsJson.put("corp_code", corpMap.get("corp_code")); // 公司编号
				corpMapsJson.put("corp_name", corpMap.get("corp_name")); // 公司描述
				corpMapsJson.put("city_id", corpMap.get("city_id")); // 城市id
				corpMapsJson.put("create_time", UtilString.getShortStringForDate((Date) corpMap.get("create_time"))); // 创建日期
				corpMapsJson.put("board_id", corpMap.get("board_id")); // 板块id
				corpMapsJson.put("board_name", corpMap.get("board_name")); // 板块描述
				body.add(corpMapsJson);
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
	 * 添加或者修改公司
	 * 
	 * @param 刘宇
	 *            2018.02.28
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/add_or_update_corp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdateCorp(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = true;
		String errorString = "成功";
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		String body = UtilString.STRING_EMPTY;
		try {
			String corpId = json.getString("corp_id");// 公司id
			String corpCode = json.getString("corp_code");// 公司编码
			String corpName = json.getString("corp_name");// 公司描述
			String boardId = json.getString("board_id");// 板块id
			Map<String, Object> errorMap = dicCorpService.addOrUpdateCorp(errorCode, errorString, errorStatus, corpId,
					corpCode, corpName, boardId);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");

			dictionaryService.refreshCorp();
		} catch (Exception e) {
			errorString = "失败";
			errorStatus = false;
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	@RequestMapping(value = "/get_corp_by_id/{corp_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseAreaById(@PathVariable("corp_id") Integer corp_id, CurrentUser cUser) {

		Map<String, Object> body = dicCorpService.getCorpById(corp_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;

		if (body.get("corp_id") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

	/**
	 * 查询所有板块id和描述
	 * 
	 * @author 刘宇 2018.03.02
	 * @return
	 */
	@RequestMapping(value = "/list_board", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listBoard(CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {

			List<DicBoard> boardobjs = dicBoardService.listBoard();
			for (DicBoard board : boardobjs) {
				JSONObject boardobjsJson = new JSONObject();
				boardobjsJson.put("board_id", board.getBoardId()); // 板块id
				boardobjsJson.put("board_name", board.getBoardName()); // 板块描述
				body.add(boardobjsJson);
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
	 * 查询所有板块id和描述
	 * 
	 * @author 刘宇 2018.03.02
	 * @return
	 */
	@RequestMapping(value = "/list_corp", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listCorp() {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = false;
		JSONArray body = new JSONArray();
		try {
			List<DicCorp> list = dicCorpService.getAllCorpList();
			for (DicCorp corp : list) {
				JSONObject boardobjsJson = new JSONObject();
				boardobjsJson.put("corp_id", corp.getCorpId());
				boardobjsJson.put("corp_code", corp.getCorpCode());
				boardobjsJson.put("corp_name", corp.getCorpName());
				body.add(boardobjsJson);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, body);
	}
}
