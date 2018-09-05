package com.inossem.wms.web.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInAndOutService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

@Controller
public class InAndOutController {

	private static final Logger logger = LoggerFactory.getLogger(InAndOutController.class);

	@Autowired
	private IInAndOutService inAndOutService;

	@Autowired
	private ICommonService commonService;

	@RequestMapping(value = "/biz/query/stock_analyse/over_view", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject overView(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String corpId = json.getString("corp_id");// 板块id
			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
			}
			String date  = json.getString("date");// 日期
			int type = json.getInt("type");
			returnMap = inAndOutService.getOverview(boardId, corpId, whId, type,date);

		} catch (WMSException e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = e.getErrorCode();
			error_string = e.getMessage();
		}catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		return UtilResult.getResultToJson(status, error_code, error_string, returnMap);
	}

	@RequestMapping(value = "/biz/query/stock_analyse/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject Info(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String corpId = json.getString("corp_id");// 板块id
			String date  = json.getString("date");// 日期
			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
				if(!StringUtils.hasText(whId)){
					whId = null;
				}
			}
			int type = json.getInt("type");
			returnMap = inAndOutService.getInfo(boardId, corpId, whId, type, 12,date);

		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, "", returnMap);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/biz/query/stock_analyse/get_mat_detial", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatDetial(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String date = json.getString("date");// 板块id

			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
				if(!StringUtils.hasText(whId)){
					whId = null;
				}
			}
			int type = json.getInt("type");
			returnMap = inAndOutService.getMatDetial(boardId, null, whId, type, date);
			returnList = (List<Map<String, Object>>) returnMap.get("returnList");
		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, "", returnList);
	}

	@RequestMapping(value = "/biz/query/stock_analyse/get_wh_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWhInfo(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String date = json.getString("date");// 板块id

			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
				if(!StringUtils.hasText(whId)){
					whId = null;
				}
			}
			int type = json.getInt("type");
			returnMap = inAndOutService.getWhDetial(boardId, null, whId, type, date);

		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, "", returnMap);
	}

	// 根据板块得到公司、工厂、库存地点联动
	@RequestMapping(value = {"/biz/query/stock_analyse/base_info","/biz/turnover/base_info"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object baseInfo(String board_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		JSONObject body = new JSONObject();
		try {

			int defaultCorpId = cUser.getCorpId();
			List<DicBoard> bList = new ArrayList<DicBoard>();
			bList = commonService.getBordList();

			body.put("deaultBoardId", cUser.getBoardId());
			body.put("defaultCorpId", defaultCorpId);
			body.put("boardList", bList);
			// 计算当前时间的上一个月的月初日期和月末日期
			Calendar calendar1 = Calendar.getInstance();
			calendar1.add(Calendar.MONTH, 0);
			calendar1.set(Calendar.DAY_OF_MONTH, 1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			body.put("currentDate", format.format(calendar1.getTime()));

		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		JSONObject obj = UtilResult.getResultToJson(status, error_code, body);
		UtilJSONConvert.setNullToEmpty(obj);
		return obj;

	}

}
