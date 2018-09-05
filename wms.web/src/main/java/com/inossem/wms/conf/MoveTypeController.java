package com.inossem.wms.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.dic.IDicMoveTypeService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 移动类型数据传输控制类
 * 
 * @author 刘宇 2018.01.24
 *
 */
@Controller
@RequestMapping("/conf/movetype")
public class MoveTypeController {
	private static final Logger logger = LoggerFactory.getLogger(MoveTypeController.class);

	@Autowired
	private IDicMoveTypeService dicMoveTypeService;

	@Autowired
	private IDictionaryService dictionaryService;

	/**
	 * 移动类型列表页and关键字查询
	 * 
	 * @author 刘宇 2018.01.24
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/list_move_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listMoveType(@RequestBody JSONObject json, CurrentUser cUser) {
		List<DicMoveType> ydlxList = new ArrayList<DicMoveType>();
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();

		JSONArray body = new JSONArray();
		try {
			String keyword = json.getString("keyword");

			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			ydlxList = dicMoveTypeService.listMoveType(keyword, pageIndex, pageSize, total, sortAscend, sortColumn);

			if (ydlxList != null && ydlxList.size() > 0) {
				total = ydlxList.get(0).getTotalCount();
			}
			for (DicMoveType dicMoveType : ydlxList) {
				JSONObject dicMoveTypeObject = new JSONObject();
				dicMoveTypeObject.put("move_type_id", dicMoveType.getMoveTypeId()); // 移动类型表自增主键
				dicMoveTypeObject.put("move_type_code", dicMoveType.getMoveTypeCode()); // 移动类型
				dicMoveTypeObject.put("spec_stock", dicMoveType.getSpecStock()); // 特殊库存标识
				dicMoveTypeObject.put("move_type_name", dicMoveType.getMoveTypeName()); // 移动类型描述
				dicMoveTypeObject.put("biz_type", dicMoveType.getBizType()); // 业务类型
				dicMoveTypeObject.put("biz_type_name", dicMoveType.getBizTypeName()); // 业务类型描述
				body.add(dicMoveTypeObject);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 修改或者添加移动类型
	 * 
	 * @author 刘宇 2018.01.24
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add_or_update_move_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addAndUpdateMoveType(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		try {
			boolean addOrUpdate = json.getBoolean("add_or_update"); // 判断添加or修改移动类型
			// true---添加
			// false---修改
			String moveType = json.getString("move_type_code");// 移动类型
			//String specStock = json.getString("spec_stock");// 特殊库存标识
			String moveTypeName = json.getString("move_type_name");// 移动类型描述
			String bizTypeString = json.getString("biz_type");// 业务类型
			Byte bizType = 0;
			if (bizTypeString != null && !bizTypeString.equals("")) {
				bizType = Byte.parseByte(bizTypeString);
			}
			Map<String, Object> errorMap = dicMoveTypeService.addOrUpdatMoveType(errorCode, addOrUpdate, errorString,
					errorStatus, moveType, "", moveTypeName, bizType);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");

			dictionaryService.refreshMoveType();// 保存移动类型时刷新移动类型缓存数据
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
	 * 查找唯一一个移动类型
	 * 
	 * @author 刘宇 2018.01.24
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/get_move_type", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMoveType(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		String moveTypeCode = json.getString("move_type_code");// 移动类型
		//String specStock = json.getString("spec_stock");// 特殊库存标识
		JSONObject jsonObject = new JSONObject();
		try {
			DicMoveType objYdlxUpdate = dicMoveTypeService.getMoveType(moveTypeCode, "");
			jsonObject.put("move_type_id", objYdlxUpdate.getMoveTypeId()); // 移动类型表自增主键
			jsonObject.put("move_type_code", objYdlxUpdate.getMoveTypeCode());// 移动类型
			jsonObject.put("spec_stock", objYdlxUpdate.getSpecStock());// 特殊库存标识
			jsonObject.put("move_type_name", objYdlxUpdate.getMoveTypeName());// 移动类型描述
			jsonObject.put("biz_type", objYdlxUpdate.getBizType());// 业务类型
			jsonObject.put("biz_type_name", objYdlxUpdate.getBizTypeName()); // 业务类型描述

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), jsonObject);

	}
}
