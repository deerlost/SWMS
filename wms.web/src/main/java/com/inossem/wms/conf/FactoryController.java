package com.inossem.wms.conf;

import java.util.Date;
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
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.dic.IDicCorpService;
import com.inossem.wms.service.dic.IDicFactoryService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 系统管理下组织架构下工厂管理
 * 
 * 
 * @author 刘宇 2018.02.27
 *
 */
@Controller
@RequestMapping("/conf/fty")
public class FactoryController {

	private static final Logger logger = LoggerFactory.getLogger(FactoryController.class);

	@Autowired
	private IDicFactoryService dicFactoryService;

	@Autowired
	private IDicCorpService dicCorpService;

	@Autowired
	private IDictionaryService dictionaryService;

	/**
	 * 工厂分页列表查询
	 * 
	 * @param 刘宇
	 *            2018.02.11
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/factory_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachFactory(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			String condition = json.getString("condition");// 查询条件
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			List<Map<String, Object>> factoryMaps = dicFactoryService.listFactoryOnPaging(condition, pageIndex,
					pageSize, total, sortAscend, sortColumn);
			if (factoryMaps.size() > 0) {
				total = Integer.parseInt(factoryMaps.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> factoryMap : factoryMaps) {

				JSONObject factoryMapsJSon = new JSONObject();
				factoryMapsJSon.put("fty_id", factoryMap.get("fty_id"));// 工厂id
				factoryMapsJSon.put("fty_code", factoryMap.get("fty_code"));// 工厂编码
				factoryMapsJSon.put("fty_name", factoryMap.get("fty_name"));// 工厂描述
				factoryMapsJSon.put("address", factoryMap.get("address"));// 工厂地址
				factoryMapsJSon.put("create_time",
						UtilString.getShortStringForDate((Date) factoryMap.get("create_time")));// 创建日期
				factoryMapsJSon.put("corp_id", factoryMap.get("corp_id"));// 公司id
				factoryMapsJSon.put("corp_code", factoryMap.get("corp_code"));// 公司编码
				factoryMapsJSon.put("corp_name", factoryMap.get("corp_name"));// 公司描述
				body.add(factoryMapsJSon);

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
	 * 添加或者修改工厂
	 * 
	 * @param 刘宇
	 *            2018.02.11
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/add_or_update_factory", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdateFactory(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = true;
		String errorString = "成功";
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		String body = UtilString.STRING_EMPTY;
		try {
			boolean isAdd = json.getBoolean("is_add");// 是否添加 true = 添加 false =
														// 修改
			Integer ftyId = json.getInt("fty_id");// 工厂id
			String ftyCode = json.getString("fty_code");// 工厂编码
			String ftyName = json.getString("fty_name");// 工厂描述
			String address = json.getString("address");// 地址
			Integer corpId = json.getInt("corp_id");// 公司id

			Map<String, Object> errorMap = dicFactoryService.addOrUpdateFactory(errorCode, errorString, errorStatus,
					isAdd, ftyId, ftyCode, ftyName, address, corpId);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");

			dictionaryService.refreshFactory();// 刷新工厂缓存数据
		} catch (Exception e) {
			errorString = "失败";
			errorStatus = false;
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 查询所有公司id和公司描述
	 * 
	 * @param 刘宇
	 *            2018.02.28
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/corp_id_and_corp_name_of_crop_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listAllCorpIdAndCorpNameOfCorp(CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			List<DicCorp> objs = dicCorpService.listAllCorpIdAndCorpNameOfCorp();
			for (DicCorp obj : objs) {
				JSONObject dicCorpsJSon = new JSONObject();
				dicCorpsJSon.put("corp_id", obj.getCorpId());// 公司id
				dicCorpsJSon.put("corp_code", obj.getCorpCode());// 公司编号
				dicCorpsJSon.put("corp_name", obj.getCorpName());// 公司描述
				body.add(dicCorpsJSon);
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

	@RequestMapping(value = "/get_fty_by_id/{fty_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseAreaById(@PathVariable("fty_id") Integer fty_id, CurrentUser cUser) {

		Map<String, Object> body = dicFactoryService.getFtyById(fty_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;

		if (body.get("fty_id") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, 0, 1, 1, body);
	}

}
