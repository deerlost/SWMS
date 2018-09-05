package com.inossem.wms.util;

import java.util.HashMap;
import java.util.Map;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;

import net.sf.json.JSONObject;

/**
 * 接口端返回值
 * 
 * @author 高海涛
 */
public class UtilResult {

	private static JSONObject getJsonData(boolean status, int errorCode, String errorString, int pageIndex,
			int pageSize, int total, Object data) {
		JSONObject head = new JSONObject();
		head.put("status", status);
		if (status) {
			head.put("error_code", EnumErrorCode.ERROR_CODE_SUCESS.getValue());
			head.put("msg", errorString);
		} else {
			head.put("error_code", errorCode);
			if (UtilString.isNullOrEmpty(errorString)) {
				head.put("msg", EnumErrorCode.ERROR_CODE_FAILURE.getName());
			} else {
				head.put("msg", errorString);
			}

		}

		head.put("page_index", pageIndex);
		head.put("page_size", pageSize);
		head.put("total", total);

		JSONObject obj = new JSONObject();
		obj.put("head", head);
		obj.put("body", data);

		return obj;
	}

	private static Map<String, Object> getMapData(boolean status, int errorCode, String errorString, int pageIndex,
			int pageSize, int total, Object data) {
		Map<String, Object> head = new HashMap<String, Object>();
		head.put("status", status);
		if (status) {
			head.put("error_code", EnumErrorCode.ERROR_CODE_SUCESS.getValue());
			head.put("msg", errorString);
		} else {
			head.put("error_code", errorCode);
			if (UtilString.isNullOrEmpty(errorString)) {
				head.put("msg", EnumErrorCode.ERROR_CODE_FAILURE.getName());
			} else {
				head.put("msg", errorString);
			}

		}

		head.put("page_index", pageIndex);
		head.put("page_size", pageSize);
		head.put("total", total);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("head", head);
		obj.put("body", data);
		return obj;
	}

	/**
	 * 返回JSON
	 * 
	 * @param errorCode
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @param data
	 * @return
	 */
	public static JSONObject getResultToJson(boolean status, int errorCode, String errorString, int pageIndex,
			int pageSize, int total, Object data) {

		return getJsonData(status, errorCode, errorString, pageIndex, pageSize, total, data);
	}

	public static JSONObject getResultToJson(boolean status, int errorCode, int pageIndex, int pageSize, int total,
			Object data) {

		return getJsonData(status, errorCode, EnumErrorCode.getNameByValue(errorCode), pageIndex, pageSize, total,
				data);
	}

	public static JSONObject getResultToJson(boolean status, int errorCode, String errorString, Object data) {
		return getJsonData(status, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), data);
	}

	public static JSONObject getResultToJson(boolean status, int errorCode, Object data) {
		return getJsonData(status, errorCode, EnumErrorCode.getNameByValue(errorCode), EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), data);
	}

	/**
	 * 返回MAP
	 * 
	 * @param errorCode
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @param data
	 * @return
	 */
	public static Map<String, Object> getResultToMap(boolean status, int errorCode, String errorString, int pageIndex,
			int pageSize, int total, Object data) {

		return getMapData(status, errorCode, errorString, pageIndex, pageSize, total, data);
	}

	public static Map<String, Object> getResultToMap(boolean status, int errorCode, int pageIndex, int pageSize,
			int total, Object data) {

		return getMapData(status, errorCode, UtilString.STRING_EMPTY, pageIndex, pageSize, total, data);
	}

	public static Map<String, Object> getResultToMap(boolean status, int errorCode, String errorString, Object data) {
		return getMapData(status, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(),
				EnumPage.TOTAL_COUNT.getValue(), data);
	}

	public static Map<String, Object> getResultToMap(boolean status, int errorCode, Object data) {
		return getMapData(status, errorCode, UtilString.STRING_EMPTY, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), data);
	}
}