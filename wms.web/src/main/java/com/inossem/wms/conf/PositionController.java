package com.inossem.wms.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IAreaService;
import com.inossem.wms.service.dic.IDicCorpService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.service.dic.IPositionService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 仓位主数据
 * 
 * @author 刘宇 2018.02.27
 *
 */
@Controller
@RequestMapping("/conf/position")
public class PositionController {

	private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

	@Autowired
	private IPositionService positionService;
	@Autowired
	private IDicWarehouseService dicWarehouseService;
	@Autowired
	private IAreaService areaService;

	@Autowired
	private IDicCorpService dicCorpService;

	/**
	 * 仓位数据分页查询
	 * 
	 * @param 刘宇
	 *            2018.02.11
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/position_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachPosition(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray body = new JSONArray();
		try {
			//String whCode = json.getString("wh_code");// 仓库编号
			String areaCode = json.getString("area_code");// 储存区编号
			String positionCode = json.getString("position_code");// 仓位编号
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			String storageTypeId = "";
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			if (json.containsKey("storage_type_id")) {
				storageTypeId = json.getString("storage_type_id");
			}		
			if(json.containsKey("fty_id")) {				
				map.put("ftyId", json.getString("fty_id"));
			}
			if(json.containsKey("location_id")) {				
				map.put("locationId", json.getString("location_id"));
			}
			if(json.containsKey("wh_id")) {				
				map.put("whId", json.getString("wh_id"));
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");			
			
			//map.put("whCode", whCode);// 仓库编号
			map.put("areaCode", areaCode);// 储存区编号
			map.put("positionCode", positionCode);// 仓位编号
			map.put("pageSize", pageSize);
			map.put("pageIndex", pageIndex);
			map.put("paging", true);
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);
			map.put("storageTypeId", storageTypeId);
		
			List<Map<String, Object>> positionMaps = positionService.listPositionOnPaging(map);
			
			
			if (positionMaps.size() > 0) {
				total = Integer.parseInt(positionMaps.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> positionMapsMap : positionMaps) {

				JSONObject positionMapsJSon = new JSONObject();
				positionMapsJSon.put("position_id", positionMapsMap.get("position_id"));// 仓位id
				positionMapsJSon.put("position_code", positionMapsMap.get("position_code"));// 仓位编码
				positionMapsJSon.put("wh_id", positionMapsMap.get("wh_id"));// 仓库id
				positionMapsJSon.put("wh_code", positionMapsMap.get("wh_code"));// 仓库编码
				positionMapsJSon.put("wh_name", positionMapsMap.get("wh_name"));// 仓库描述
				positionMapsJSon.put("area_id", positionMapsMap.get("area_id"));// 储存区id
				positionMapsJSon.put("area_code", positionMapsMap.get("area_code"));// 储存区编码
				positionMapsJSon.put("area_name", positionMapsMap.get("area_name"));// 储存区描述
				positionMapsJSon.put("freeze_output", positionMapsMap.get("freeze_output"));// 出库冻结标识
				positionMapsJSon.put("freeze_input", positionMapsMap.get("freeze_input"));// 入库冻结标识
				
				positionMapsJSon.put("storage_type_id", positionMapsMap.get("storage_type_id"));
				positionMapsJSon.put("storage_type_code", positionMapsMap.get("storage_type_code"));
				positionMapsJSon.put("storage_type_name", positionMapsMap.get("storage_type_name"));
				// positionMapsJSon.put("freeze_id",
				// positionMapsMap.get("freeze_id"));// 冻结原因
				// positionMapsJSon.put("unit_weight",
				// positionMapsMap.get("unit_weight"));// 重量单位
				// positionMapsJSon.put("unit_code_weight",
				// positionMapsMap.get("unit_code_weight"));// 重量单位编码
				// positionMapsJSon.put("name_zh_weight",
				// positionMapsMap.get("name_zh_weight"));// 重量单位中文描述
				// positionMapsJSon.put("unit_volume",
				// positionMapsMap.get("unit_volume"));// 容积单位
				// positionMapsJSon.put("unit_code_volume",
				// positionMapsMap.get("unit_code_volume"));// 容积单位编码
				// positionMapsJSon.put("name_zh_volume",
				// positionMapsMap.get("name_zh_volume"));// 容积单位中文描述
				body.add(positionMapsJSon);

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
	 * 添加或者修改仓位主数据
	 * 
	 * @param 刘宇
	 *            2018.02.28
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/add_or_update_position", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addOrUpdatePosition(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean errorStatus = true;
		String errorString = "成功";
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		String body = UtilString.STRING_EMPTY;
		try {
			String positionId = json.getString("position_id");// 仓位id
			String whId = json.getString("wh_id");// 仓库id
			String areaId = json.getString("area_id");// 储存区id
			String positionCode = json.getString("position_code");// 仓位编码
			String freezeInput = json.getString("freeze_input");// 入库冻结---0-非冻结;1-冻结
			String freezeOutput = json.getString("freeze_output");// 出库冻结---0-非冻结;1-冻结
			String storageTypeId=json.getString("storage_type_id");
			// String freezeId = json.getString("freeze_id");// 冻结原因id
			// String unitWeight = json.getString("unit_weight");// 承重单位id
			// String unitVolume = json.getString("unit_volume");// 容积单位id

			Map<String, Object> errorMap = positionService.addOrUpdatePosition(errorCode, errorString, errorStatus,
					positionId, whId, areaId, positionCode, freezeInput, freezeOutput,storageTypeId);
			/*
			 * Map<String, Object> errorMap =
			 * positionService.addOrUpdatePosition(errorString, errorStatus,
			 * positionId, whId, areaId, positionCode, freezeInput,
			 * freezeOutput, freezeId, unitWeight, unitVolume);
			 */
			errorCode = (int) errorMap.get("errorCode");
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
	 * 查询所有仓库下的所有储存区
	 * 
	 * @param 刘宇
	 *            2018.03.01
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/warehouse_and_area_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachWarsehouseAndArea(CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			List<DicWarehouse> warehouses = dicWarehouseService.listWhIdAndWhCodeAndWhName();
			List<DicWarehouseArea> areas = areaService.listAreaIdAndAreaCodeAndAreaName();

			body = positionService.listWarsehouseAndArea(warehouses, areas);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	@RequestMapping(value = "/get_position_by_id/{position_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseAreaById(@PathVariable("position_id") Integer position_id,
			CurrentUser cUser) {

		Map<String, Object> body = positionService.getWarehousePositionById(position_id, cUser);
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;

		if (body.get("position_id") != null) {
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		}

		return UtilResult.getResultToJson(status, error_code, "", body);
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
	
	@RequestMapping(value = "/select_storage_type_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectStorageTypeList(CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		JSONArray body = new JSONArray();
		List<Map<String,Object>> objs=null;
		try {
		    objs = positionService.selectAllStorageType();			
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			logger.error("--查询所有的存储类型", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, objs);
	}
	
	
	@RequestMapping(value = "/get_warehouse_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseList(CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;	
		List<Map<String,Object>>   wareHouseList=new ArrayList<Map<String,Object>>();
		try {
		   wareHouseList=	dicWarehouseService.queryWarehouseList();
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
		}

		JSONObject object = UtilResult.getResultToJson(errorStatus, errorCode, errorString,wareHouseList);

		UtilJSONConvert.deleteNull(object);

		return object;
	}
	
}
