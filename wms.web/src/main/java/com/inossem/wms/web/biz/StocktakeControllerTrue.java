package com.inossem.wms.web.biz;

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
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumStocktakeStatus;
import com.inossem.wms.model.enums.EnumStocktakeStockStatus;
import com.inossem.wms.model.enums.EnumWorkShiftStatus;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputTransportService;
import com.inossem.wms.service.biz.IStocktakeService;
import com.inossem.wms.util.UtilJSON;
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
	
	@Autowired
    private ICommonService commonService;
	
	@Autowired
	private IInputTransportService inputTransportService;

	/**
	 * 2.1 班次和库存状态列表
	 */
	@RequestMapping(value = "/select_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectList() {
		JSONObject body = new JSONObject();
		try {
			body.put("work_shift_list", inputTransportService.selectAllclassType());
			body.put("stock_take_status_list", EnumStocktakeStockStatus.toList());
			body.put("work_shift_id", commonService.selectNowClassType());
		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.2 新增时查询盘点行项目信息列表
	 */
	@RequestMapping(value = "/query_item_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> queryItemList(CurrentUser cuser, @RequestBody JSONObject json) {
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		try {
			body = stocktakeService.queryItemListOnPaging(json);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToMap(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.3 盘点信息暂存/提交(创建时)
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject save(CurrentUser cuser, @RequestBody JSONObject json) {
		JSONObject body = new JSONObject();
		try {
			json.put("request_source", EnumRequestSource.WEB.getValue());
			body = stocktakeService.save(cuser, json);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.4 查询盘点列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> list(CurrentUser cuser, @RequestBody JSONObject json) {
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = 0;
		try {
			if (json.containsKey("page_index")) {
				pageIndex = json.getInt("page_index");
			}
			if (json.containsKey("page_size")) {
				pageSize = json.getInt("page_size");
			}
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}

			boolean sortAscend = true;
			if (json.containsKey("sort_ascend")) {
				sortAscend = json.getBoolean("sort_ascend");
			}
			String sortColumn = "";
			if (json.containsKey("sort_column")) {
				sortColumn = json.getString("sort_column");
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			// json转驼峰参数出现问题,int转成了double
			paramMap.put("paging", true);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("totalCount", total);
			paramMap.put("sortAscend", sortAscend);
			paramMap.put("sortColumn", sortColumn);
			// 盘点状态
			String[] statusArray;
			String status = UtilJSON.getStringOrEmptyFromJSON("status", json);
			if (StringUtils.hasLength(status)) {
				statusArray = status.split("-");
				paramMap.put("statusArray", statusArray);
			}
			if (!UtilString.isNullOrEmpty(json.getString("condition"))) {
				paramMap.put("condition", json.getString("condition"));
			}

			body = stocktakeService.listOnPaging(paramMap);

			// 赋值总数量
			if (body != null && body.size() > 0) {
				Long totalCountLong = (Long) body.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToMap(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), pageIndex, pageSize,
					total, body);
		}

		return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), pageIndex, pageSize, total,
				body);
	}

	/**
	 * 2.5 查询盘点详情
	 */
	@RequestMapping(value = "/info/{stock_take_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject info(@PathVariable("stock_take_id") Integer stock_take_id) {
		JSONObject body = new JSONObject();
		try {
			body = stocktakeService.info(stock_take_id);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.6 删除盘点单
	 */
	@RequestMapping(value = "/delete/{stock_take_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject delete(@PathVariable("stock_take_id") Integer stock_take_id) {
		JSONObject body = new JSONObject();
		try {
			stocktakeService.delete(stock_take_id);
			body.put("stock_take_id", stock_take_id);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.7 盘点数量暂存/完成(盘点时)
	 */
	@RequestMapping(value = "/take", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject take(CurrentUser cuser, @RequestBody JSONObject json) {
		JSONObject body = new JSONObject();
		try {
			if (json.has("status") && "1".equals(json.getString("status"))) {
				json.put("status", EnumStocktakeStatus.COMPLETED.getValue());
			}
			json.put("request_source", EnumRequestSource.WEB.getValue());
			stocktakeService.take(cuser, json);
			body.put("stock_take_id", json.get("stock_take_id"));

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	/**
	 * 2.8 重盘
	 */
	@RequestMapping(value = "/reset/{stock_take_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject reset(@PathVariable("stock_take_id") Integer stock_take_id) {
		JSONObject body = new JSONObject();
		try {
			stocktakeService.reset(stock_take_id);
			body.put("stock_take_id", stock_take_id);

		} catch (Exception e) {
			logger.error("", e);
			return UtilResult.getResultToJson(false, EnumErrorCode.ERROR_CODE_FAILURE.getValue(), body);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

}
