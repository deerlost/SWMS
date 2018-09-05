package com.inossem.wms.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/warring")
public class StockWarringController {
	private static final Logger logger = LoggerFactory.getLogger(StockWarringController.class);
	@Autowired
	private IStockQueryService stockQueryService;
	@Autowired
	private ServerConfig constantConfig;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IDicWarehouseService dicWarehouseService;
	
	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/to_coming/get_fty_location_for_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForUser(CurrentUser cUser, String fty_type) {
		String userId = cUser.getUserId();
		JSONArray ftyAry = new JSONArray();
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		try {
			ftyAry = commonService.listFtyLocationForUser(userId, fty_type);
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
			errorCode=EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			
		}

		return UtilResult.getResultToJson(true, errorCode,
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry);
	}
	

	@RequestMapping(value = "/select_warranty_date_onpaging", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectWarrantyDateOnpaging(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = true;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			param.put("pageSize", pageSize);
			param.put("pageIndex", pageIndex);
			param.put("paging", true);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
			param.put("ftyId", UtilObject.getIntegerOrNull(json.get("fty_id")));
			param.put("locationId", UtilObject.getIntegerOrNull(json.get("location_id")));
			param.put("positionCode", UtilObject.getStringOrEmpty(json.get("position_code")));
			param.put("areaCode", UtilObject.getStringOrEmpty(json.get("area_code")));
			if (json.has("mat_code")) {
				param.put("matCode", json.getString("mat_code"));// 物料编码
				List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(json.getString("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
				param.put("utilMatCodes", utilMatCodes);// 物料编码集合
			}
			// param.put("matCode", UtilObject.getStringOrEmpty(json.get("mat_code")));
			param.put("matName", UtilObject.getStringOrEmpty(json.get("mat_name")));
			if (json.has("day_start")) {
				param.put("dayStart",UtilObject.getStringOrEmpty(json.get("day_start")));
			}
			if (json.has("day_end")) {
				param.put("dayEnd",UtilObject.getStringOrEmpty(json.get("day_end")));
			}
			if (json.has("warranty_date")) {
				param.put("warrantyDate", UtilObject.getIntegerOrNull(json.get("warranty_date")));
			}
			if (json.has("wh_id")) {
				param.put("whId", UtilObject.getIntegerOrNull(json.get("wh_id")));
			}
			if (json.has("area_id")) {
				param.put("areaId", UtilObject.getIntegerOrNull(json.get("area_id")));
			}
			if (json.has("stock_type_id")) {
				if(json.getString("stock_type_id").equals("5")){
					param.put("isUrgent", json.getString("stock_type_id"));
				}else{
					param.put("stockTypeId", json.getString("stock_type_id"));
				}
				
			}
			
			body = stockQueryService.selectWarrantyDateOnpaging(param);
			if(body.size()>0) {				
				total = UtilObject.getIntOrZero(body.get(0).get("totalCount"));
			}else {
				total=0;
			}
		} catch (InterfaceCallException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			errorStatus = false;
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}


	@RequestMapping(value = "/download_warranty_date", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadWarrantyDate(HttpServletRequest request, HttpServletResponse response) throws Exception {

		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;

		List<Map<String, Object>> warrantyDateList = null;
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("ftyId", request.getParameter("fty_id"));
			map.put("locationId", request.getParameter("location_id"));
			map.put("positionCode", request.getParameter("position_code"));
			//map.put("areaCode", request.getParameter("area_code"));
			map.put("matCode", request.getParameter("mat_code"));// 物料编码
			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(request.getParameter("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
			map.put("utilMatCodes", utilMatCodes);// 物料编码集合
			// param.put("matCode", UtilObject.getStringOrEmpty(json.get("mat_code")));
			map.put("matName", request.getParameter("mat_name"));
			map.put("dayStart",request.getParameter("day_start"));
			map.put("dayEnd",request.getParameter("day_end"));
			map.put("warrantyDate",  request.getParameter("warranty_date"));
			map.put("whId",  request.getParameter("wh_id"));
			map.put("areaId",  request.getParameter("area_id"));
			if(request.getParameter("stock_type_id").equals("5")){
				map.put("isUrgent", request.getParameter("stock_type_id"));
			}else{
				map.put("stockTypeId", request.getParameter("stock_type_id"));
			}
			map.put("paging", false);

			warrantyDateList = stockQueryService.selectWarrantyDateOnpaging(map);

			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("fty_name", "工厂");
			relationMap.put("location_name", "库存地点");
			relationMap.put("area_name", "存储区");
			relationMap.put("position_code", "仓位");
			relationMap.put("stock_qty", "库存数量");
			relationMap.put("unit_name", "计量单位");
			relationMap.put("qty", "公斤数量");
			relationMap.put("warranty_date", "临期天数");
			relationMap.put("create_time", "入库日期");
			relationMap.put("product_batch", "生产批次");
			relationMap.put("package_type_code", "包装类型");
			relationMap.put("stock_type_name", "库存类型");
			List<String> orderList = new ArrayList<String>();
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("工厂");
			orderList.add("库存地点");
			orderList.add("存储区");
			orderList.add("仓位");
			orderList.add("库存数量");
			orderList.add("计量单位");
			orderList.add("公斤数量");
			orderList.add("临期天数");
			orderList.add("入库日期");
			orderList.add("生产批次");
			orderList.add("包装类型");
			orderList.add("库存类型");
			String root = constantConfig.getTempFilePath();

			String download_file_name = "warrantyDate";// 仓位库存查询

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, warrantyDateList, relationMap, orderList,
					root);

			fileInServer = new File(filePath);
			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(fileInServer));

			// 转码，免得文件名中文乱码
			String fileNameForUTF8 = URLEncoder.encode(download_file_name + ".xls", "UTF-8");
			// 设置文件下载头
			response.addHeader("Content-Disposition", "attachment;filename=" + fileNameForUTF8);

			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			out = new BufferedOutputStream(response.getOutputStream());
			int len = 0;
			while ((len = bis.read()) != -1) {
				out.write(len);
				out.flush();
			}

		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
	/**
	 * 查询sn信息
	 * @param cUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/to_coming/select_warranty_date_sn_massage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectWarrantyDateSnMassage(CurrentUser cUser, @RequestBody JSONObject json) {
		String userId = cUser.getUserId();
		List<Map<String,Object>> maplist=new ArrayList<Map<String,Object>>();
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean errorStatus = false;
		String msg="";
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			map.put("ftyId", json.get("fty_id"));
			//map.put("areaCode", json.get("area_code"));
			map.put("areaId", json.get("area_id"));
			map.put("locationId", json.get("location_id"));
			map.put("palletId", json.get("pallet_id"));
			map.put("stockTypeId", json.get("stock_type_id"));
			map.put("matId", json.get("mat_id"));
			map.put("positionCode", json.get("position_code"));
			maplist=stockQueryService.selectWarrantyDateSnMassage(map);
			errorStatus=true;
		} catch (Exception e) {
			logger.error("---临期库存预警查询sn信息--", e);
			msg="查询sn信息失败！";
			errorCode=EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(errorStatus, errorCode,msg, maplist);
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
	
	
	@RequestMapping(value = "/get_warehouse_area", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWarehouseArea(CurrentUser cUser, Integer wh_id ) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list=stockQueryService.selectWarehouseListByWhId(wh_id);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString,list);
	}
	
	
	
}
