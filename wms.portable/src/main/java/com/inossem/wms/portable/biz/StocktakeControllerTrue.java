package com.inossem.wms.portable.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumStocktakeStatus;
import com.inossem.wms.service.biz.IStocktakeService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/stocktake")
public class StocktakeControllerTrue {
	private static final Logger logger = LoggerFactory.getLogger(StocktakeControllerTrue.class);

	/**
	 * 库存盘点服务接口
	 */
	@Autowired
	private IStocktakeService stocktakeService;

	/**
	 * 2.4 查询盘点列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> list(CurrentUser cuser, @RequestBody JSONObject json) {
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 盘点状态
			String[] statusArray;
			// 只查询已提交的状态
			String status = EnumStocktakeStatus.INCOMPLETE.getValue();
			if (StringUtils.hasLength(status)) {
				statusArray = status.split("-");
				paramMap.put("statusArray", statusArray);
			}
			if (!UtilString.isNullOrEmpty(json.getString("condition"))) {
				paramMap.put("condition", json.getString("condition"));
			}

			body = stocktakeService.listOnPaging(paramMap);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToMap(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.5 查询盘点详情
	 */
	@RequestMapping(value = "/info/{stock_take_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject info(@PathVariable("stock_take_id") Integer stock_take_id) {
		JSONObject body = new JSONObject();
		try {
			body = stocktakeService.infoForPDA(stock_take_id);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.7 盘点数量暂存/完成
	 */
	@RequestMapping(value = "/take", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject take(CurrentUser cuser, @RequestBody JSONObject json) {
		JSONObject body = new JSONObject();
		try {
			if (json.has("status") && "1".equals(json.getString("status"))) {
				json.put("status", EnumStocktakeStatus.COMPLETED.getValue());
			}
			json.put("request_source", EnumRequestSource.PDA.getValue());
			stocktakeService.take(cuser, json);
			body.put("stock_take_id", json.get("stock_take_id"));

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

}
