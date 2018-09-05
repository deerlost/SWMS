package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.service.intfc.IStockAgeCheck;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 库存查询数据控制传输类
 * 
 * @author 刘宇 2018.02.07
 *
 */
@Controller
@RequestMapping("/biz/stockquery")
public class StockController {

	private static final Logger logger = LoggerFactory.getLogger(StockController.class);
	@Autowired
	private IStockQueryService stockQueryService;

	@Autowired
	private ServerConfig constantConfig;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IStockAgeCheck stockAgeCheckService;

	@Autowired
	private IDicWarehouseService dicWarehouseService;
	/**
	 * 批次库存查询
	 * 
	 * @param 刘宇
	 *            2018.02.07
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_stoch_batch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStochBatch(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;

		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<Map<String, Object>> stockBatchMaps = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();

			if (json.has("mat_code")) {
				map.put("matCode", json.getString("mat_code"));// 物料编码
				List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(json.getString("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
				map.put("utilMatCodes", utilMatCodes);// 物料编码集合
			}
			if (json.has("mat_name")) {
				map.put("matName", json.getString("mat_name"));// 物料描述
			}
			if (json.has("mat_group_code")) {
				map.put("matGroupCode", json.getString("mat_group_code"));// 物料组编码始
			}

			if (json.has("fty_id")) {
				map.put("ftyId", json.getString("fty_id"));// 工厂id
			}
			if (json.has("location_id")) {
				map.put("locationId", json.getString("location_id"));// 库存地点编码
			}
			
			if (json.has("status")) {
				map.put("status", json.getString("status"));
			}
			if (json.has("product_batch")) {
				map.put("productBatch", json.getString("product_batch"));
			}
			if (json.has("erp_batch")) {
				map.put("erpBatch", json.getString("erp_batch"));
			}
			if (json.has("wh_id")) {
				map.put("whId", json.getString("wh_id"));
			}
			
			SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");

			if (!json.getString("input_date_begin").equals("") && !json.getString("input_date_end").equals("")) {
				map.put("inputDateBegin", clsFormat.parse(json.getString("input_date_begin")));// 入库日期起
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(json.getString("input_date_end")), 1));// 入库日期止
			} else if (!json.getString("input_date_begin").equals("")
					&& json.getString("input_date_end").equals("")) {
				map.put("inputDateBegin", clsFormat.parse(json.getString("input_date_begin")));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(json.getString("input_date_begin")), 1));
			} else if (json.getString("input_date_begin").equals("")
					&& !json.getString("input_date_end").equals("")) {
				map.put("inputDateBegin", clsFormat.parse(json.getString("input_date_end")));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(json.getString("input_date_end")), 1));
			}

			if (json.has("stock_type_id")) {
				if(json.getString("stock_type_id").equals("5")){
					map.put("isUrgent", json.getString("stock_type_id"));
				}else{
					map.put("stockTypeId", json.getString("stock_type_id"));
				}
				
			}
			
			if (json.has("page_index")) {
				pageIndex = json.getInt("page_index");
			}
			if (json.has("page_size")) {
				pageSize = json.getInt("page_size");
			}
			if (json.has("total")) {
				total = json.getInt("total");
			}

			map.put("pageSize", pageSize);
			map.put("pageIndex", pageIndex);
			map.put("paging", true);

			stockBatchMaps = stockQueryService.listStockBatchOnPaging(map);
			if (stockBatchMaps.size() > 0) {
				total = Integer.parseInt(stockBatchMaps.get(0).get("totalCount").toString());
				stockBatchMaps.get(0).put("sum_qty", stockQueryService.selectALLBatchQty(map));
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total,
				stockBatchMaps);
	}

	@RequestMapping(value = "/update_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateStatus(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", json.get("id"));
			map.put("reason", json.get("reason"));
			map.put("status", json.get("status"));
			
			
			if (json.has("stock_type_id")) {
				if(json.getString("stock_type_id").equals("5")){
					map.put("isUrgent", json.getString("stock_type_id"));
				}
			}

			stockQueryService.updateStatus(map);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, "");
	}
	
	/**
	 * 批次库存批量导出
	 * 
	 * @author 刘宇 2018.02.07
	 * @param cUser
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_stocd_batch", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadStocdBatch(CurrentUser cUser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;

		List<Map<String, Object>> stockBatchMaps = null;
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("matCode", request.getParameter("mat_code"));// 物料编码
			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(request.getParameter("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
			map.put("utilMatCodes", utilMatCodes);// 物料编码集合
			map.put("matName", request.getParameter("mat_name"));// 物料描述
			map.put("matGroupCode", request.getParameter("mat_group_code"));// 物料组编码始

			map.put("ftyId", request.getParameter("fty_id"));// 工厂id
			map.put("locationId", request.getParameter("location_id"));// 库存地点编码

			SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
			String inputDateBegin = request.getParameter("input_date_begin");// 入库日期开始
			String inputDateEnd = request.getParameter("input_date_end");// 入库日期开始结束

			if (StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));// 入库日期起
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));// 入库日期止
			} else if (StringUtils.hasText(inputDateBegin) && !StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateBegin), 1));
			} else if (!StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateEnd));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));
			}

			if(request.getParameter("stock_type_id").equals("5")){
				map.put("isUrgent",request.getParameter("stock_type_id"));
			}else{
				map.put("stockTypeId", request.getParameter("stock_type_id"));
			}
			map.put("erpBatch", UtilObject.getStringOrNull(request.getParameter("erp_batch")));
			
	       map.put("whId", UtilObject.getStringOrNull(request.getParameter("wh_id")));
				
			map.put("status", request.getParameter("status"));
			map.put("productBatch", UtilObject.getStringOrNull(request.getParameter("product_batch")));
			
			map.put("paging", false);

			stockBatchMaps = stockQueryService.listStockBatchOnPaging(map);

			// 字段对应汉语意思
			Map<String, String> relationMap = new HashMap<String, String>();
			relationMap.put("mat_group_code", "物料组");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("package_type_code", "包装类型");
			relationMap.put("package_standard_ch", "包装规格");
			relationMap.put("stock_qty", "库存数量");
			relationMap.put("unit_name", "计量单位");
			relationMap.put("qty", "公斤数量");
			relationMap.put("erp_batch", "ERP批次");
			relationMap.put("product_batch", "生产批次");
			relationMap.put("quality_batch", "质检批次");
			relationMap.put("batch", "批次号");
			relationMap.put("fty_name", "工厂描述");
			relationMap.put("location_name", "库存地点描述");
			relationMap.put("unit_name", "计量单位");
			relationMap.put("input_date", "入库时间");
			relationMap.put("stock_type_name", "库存类型");

			// 表格排序顺序
			List<String> orderList = new ArrayList<String>();
			orderList.add("物料组");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("包装类型");
			orderList.add("包装规格");
			orderList.add("库存数量");
			orderList.add("计量单位");
			orderList.add("库存数量");
			orderList.add("计量单位");
			orderList.add("工厂描述");
			orderList.add("库存地点描述");
			orderList.add("ERP批次");
			orderList.add("生产批次");
			orderList.add("质检批次");
			orderList.add("批次号");
			orderList.add("入库时间");
			orderList.add("库存类型");

			String root = constantConfig.getTempFilePath();

			String download_file_name = "批次库存查询";// 批次库存查询

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, stockBatchMaps, relationMap, orderList,
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

	@RequestMapping(value = "/serach_stock_position_group", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStochPositionGroup(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();

		List<Map<String, Object>> stockPositionMaps = null;

		try {
			Map<String, Object> map = new HashMap<String, Object>();

			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(json.getString("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
			map.put("utilMatCodes", utilMatCodes);// 物料编码集合
			map.put("matCode", json.getString("mat_code"));// 物料编码
			map.put("matName", json.getString("mat_name"));// 物料描述
			map.put("status", json.getString("status"));// 物料描述
			String inputDateBegin = json.getString("input_date_begin");// 入库日期开始
			String inputDateEnd = json.getString("input_date_end");// 入库日期开始结束
			SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));// 入库日期起
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));// 入库日期止
			} else if (StringUtils.hasText(inputDateBegin) && !StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateBegin), 1));
			} else if (!StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateEnd));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));
			}

			//map.put("areaCode", json.getString("area_code"));// 储存区
			map.put("positionCode", json.getString("position_code"));// 仓位
			map.put("palletCode", json.getString("pallet_code"));// 托盘

			map.put("ftyId", json.getString("fty_id"));// 工厂编号
			map.put("locationId", json.getString("location_id"));// 库存地点编号

			if (json.has("stock_type_id")) {
				if(json.getString("stock_type_id").equals("5")){
					map.put("isUrgent", json.getString("stock_type_id"));
				}else{
					map.put("stockTypeId", json.getString("stock_type_id"));
				}
				
			}
			if(json.has("erp_batch")) {
				map.put("erpBatch", json.getString("erp_batch"));
			}
			if(json.has("area_id")) {
				map.put("areaId", json.getString("area_id"));
			}
            if(json.has("product_batch")) {
            	map.put("productBatch", json.getString("product_batch"));
			}
            if (json.has("wh_id")) {
				map.put("whId", json.getString("wh_id"));
			}
			if (json.has("page_index")) {
				pageIndex = json.getInt("page_index");
			}
			if (json.has("page_size")) {
				pageSize = json.getInt("page_size");
			}
			if (json.has("total")) {
				total = json.getInt("total");
			}

			map.put("pageSize", pageSize);
			map.put("pageIndex", pageIndex);
			map.put("paging", true);

			stockPositionMaps = stockQueryService.listStockPositionOnPaging(map);

			if (stockPositionMaps.size() > 0) {
				total = Integer.parseInt(stockPositionMaps.get(0).get("totalCount").toString());
				stockPositionMaps.get(0).put("sum_qty", stockQueryService.selectALLPositionQty(map));
			}

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total,
				stockPositionMaps);
	}
	
	
	/**
	 * 仓位库存查询
	 * 
	 * @param 刘宇
	 *            2018.02.09
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_stock_position", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStochPosition(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();

		List<Map<String, Object>> stockPositionMaps = null;

		try {
			Map<String, Object> map = new HashMap<String, Object>();

			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(json.getString("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
			map.put("utilMatCodes", utilMatCodes);// 物料编码集合
			map.put("matCode", json.getString("mat_code"));// 物料编码
			map.put("matName", json.getString("mat_name"));// 物料描述
			map.put("matId", json.getString("mat_id"));// 物料描述
			map.put("palletId", json.getString("pallet_id"));// 物料描述
			
			String inputDateBegin = json.getString("input_date_begin");// 入库日期开始
			String inputDateEnd = json.getString("input_date_end");// 入库日期开始结束
			SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));// 入库日期起
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));// 入库日期止
			} else if (StringUtils.hasText(inputDateBegin) && !StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateBegin), 1));
			} else if (!StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateEnd));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));
			}

			map.put("areaId", json.getString("area_id"));// 储存区
			map.put("positionId", json.getString("position_id"));// 仓位
			map.put("palletCode", json.getString("pallet_code"));// 托盘

			map.put("ftyId", json.getString("fty_id"));// 工厂编号
			map.put("locationId", json.getString("location_id"));// 库存地点编号

			if (json.has("stock_type_id")) {
				if(json.getString("stock_type_id").equals("5")){
					map.put("isUrgent", json.getString("stock_type_id"));
				}else{
					map.put("stockTypeId", json.getString("stock_type_id"));
				}
				
			}
			if (json.has("wh_id")) {
				map.put("whId", json.getString("wh_id"));
			}
			if (json.has("page_index")) {
				pageIndex = json.getInt("page_index");
			}
			if (json.has("page_size")) {
				pageSize = json.getInt("page_size");
			}
			if (json.has("total")) {
				total = json.getInt("total");
			}

			map.put("pageSize", pageSize);
			map.put("pageIndex", pageIndex);
			map.put("paging", false);
			if(json.has("erp_batch")) {
				map.put("erpBatch", json.getString("erp_batch"));
			}
            if(json.has("product_batch")) {
            	map.put("productBatch", json.getString("product_batch"));
			}
			stockPositionMaps = stockQueryService.excelStockPositionOnPaging(map);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total,
				stockPositionMaps);
	}

	@RequestMapping(value = "/download_stock_position", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadStochPosition(HttpServletRequest request, HttpServletResponse response) throws Exception {

		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;

		List<Map<String, Object>> stockPositionMaps = null;
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("matCode", request.getParameter("mat_code"));// 物料编码
			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(request.getParameter("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
			map.put("utilMatCodes", utilMatCodes);// 物料编码集合
			map.put("matName", request.getParameter("mat_name"));// 物料描述
			SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
			String inputDateBegin = request.getParameter("input_date_begin");// 入库日期开始
			String inputDateEnd = request.getParameter("input_date_end");// 入库日期开始结束

			if (StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));// 入库日期起
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));// 入库日期止
			} else if (StringUtils.hasText(inputDateBegin) && !StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateBegin));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateBegin), 1));
			} else if (!StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
				map.put("inputDateBegin", clsFormat.parse(inputDateEnd));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));
			}

			//map.put("areaCode", request.getParameter("area_code"));// 储存区
			map.put("positionCode", request.getParameter("position_code"));// 仓位
			map.put("palletCode", request.getParameter("pallet_code"));// 托盘

			map.put("ftyId", request.getParameter("fty_id"));// 工厂id
			map.put("locationId", request.getParameter("location_id"));// 库存地点编码
			map.put("whId", request.getParameter("wh_id"));
			if(request.getParameter("stock_type_id").equals("5")){
				map.put("isUrgent", request.getParameter("stock_type_id"));
			}else{
				map.put("stockTypeId", request.getParameter("stock_type_id"));
			}
			map.put("areaId", request.getParameter("area_id"));
			map.put("erpBatch", request.getParameter("erp_batch"));
            map.put("productBatch", request.getParameter("product_batch"));
            map.put("wh_id", request.getParameter("product_batch"));
			map.put("paging", false);

			stockPositionMaps = stockQueryService.excelStockPositionOnPaging(map);

			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("area_name", "存储区");
			relationMap.put("position_code", "仓位");
			relationMap.put("pallet_code", "托盘");
			relationMap.put("package_code", "包装号");
			relationMap.put("package_type_code", "包装类型");
			relationMap.put("package_standard_ch", "包装规格");
			
			relationMap.put("erp_batch", "ERP批次");
			relationMap.put("product_batch", "生产批次");
			relationMap.put("quality_batch", "质检批次");
			relationMap.put("batch", "批次号");
			relationMap.put("stock_qty", "库存数量");
			relationMap.put("unit_name", "计量单位");
			relationMap.put("qty", "公斤数量");
			relationMap.put("purchase_order_code", "生产订单号");
			relationMap.put("shelf_life", "保质期");
			relationMap.put("installation_name", "装置");
			relationMap.put("product_line_name", "生产线");
			relationMap.put("class_type_name", "班次");
			relationMap.put("fty_name", "工厂描述");
			relationMap.put("location_name", "库存地点描述");
			relationMap.put("unit_name", "计量单位");
			relationMap.put("input_date", "入库时间");
			relationMap.put("stock_type_name", "库存类型");

			List<String> orderList = new ArrayList<String>();

			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("存储区");
			orderList.add("仓位");
			orderList.add("托盘");
			orderList.add("包装号");
			orderList.add("包装类型");
			orderList.add("包装规格");
			orderList.add("计量单位");
			orderList.add("工厂描述");
			orderList.add("库存地点描述");
			orderList.add("ERP批次");
			orderList.add("生产批次");
			orderList.add("质检批次");
			orderList.add("批次号");
			orderList.add("库存数量");
			orderList.add("计量单位");
			orderList.add("公斤数量");
			orderList.add("入库时间");
			orderList.add("库存类型");
			orderList.add("生产订单号");
			orderList.add("保质期");
			orderList.add("装置");
			orderList.add("生产线");
			orderList.add("班次");
			String root = constantConfig.getTempFilePath();

			String download_file_name = "position";// 仓位库存查询

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, stockPositionMaps, relationMap, orderList,
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
	 * 出入存查询
	 * 
	 * @param 刘宇
	 *            2018.02.11
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_stock_out_and_in", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStockOutAndIn(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			String createTimeBegin = json.getString("create_time_begin");// 创建日期开始
			String createTimeEnd = json.getString("create_time_end");// 创建日期开始结束
			String createUser = json.getString("create_user");// 创建人
			String moveTypeCode = json.getString("move_type_code");// 移动类型编码
			String ftyId = json.getString("fty_id");// 工厂id
			String locationId = json.getString("location_id");// 库存地点id
			String specStockCode = json.getString("spec_stock_code");// 特殊库存代码
			String matCode = json.getString("mat_code");// 物料编码
			String matName = json.getString("mat_name");// 物料描述
			String bizTypes = json.getString("biz_types");// 业务类型
			String referReceiptCode = json.getString("refer_receipt_code").trim();// 单据编号
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}

			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(matCode);// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组

			List<Map<String, Object>> outAndInStockMaps = stockQueryService.listOutAndInStockOnPaging(createTimeBegin,
					createTimeEnd, createUser, moveTypeCode, ftyId, locationId, specStockCode, matCode, matName,
					bizTypes, utilMatCodes, referReceiptCode, pageIndex, pageSize, total, true);
			if (outAndInStockMaps.size() > 0) {
				total = Integer.parseInt(outAndInStockMaps.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> outAndInStockMap : outAndInStockMaps) {
				Integer biz_type = new Integer((int) outAndInStockMap.get("biz_type"));
				Integer mat_doc_type = new Integer((int) outAndInStockMap.get("mat_doc_type"));
				JSONObject stockOutAndInMapsJSon = new JSONObject();
				stockOutAndInMapsJSon.put("biz_type_name", EnumReceiptType.getNameByValue(biz_type.byteValue()));// 业务类型
				stockOutAndInMapsJSon.put("mat_id", outAndInStockMap.get("mat_id"));// 物料id
				stockOutAndInMapsJSon.put("mat_code", outAndInStockMap.get("mat_code"));// 物料编码
				stockOutAndInMapsJSon.put("mat_name", outAndInStockMap.get("mat_name"));// 物料描述
				stockOutAndInMapsJSon.put("fty_name", outAndInStockMap.get("fty_name"));// 工厂描述
				stockOutAndInMapsJSon.put("location_name", outAndInStockMap.get("location_name"));// 库存地点描述
				stockOutAndInMapsJSon.put("move_type_code", outAndInStockMap.get("move_type_code"));// 移动类型编码
				stockOutAndInMapsJSon.put("batch", outAndInStockMap.get("batch"));// 批次号
				stockOutAndInMapsJSon.put("spec_stock_code", outAndInStockMap.get("spec_stock_code"));// 特殊库存编码
				stockOutAndInMapsJSon.put("mat_doc_code", outAndInStockMap.get("mat_doc_code"));// 物料凭证
				stockOutAndInMapsJSon.put("mat_doc_rid", outAndInStockMap.get("mat_doc_rid"));// 物料凭证行项目
				stockOutAndInMapsJSon.put("mat_doc_type_name",
						EnumReceiptType.getNameByValue(mat_doc_type.byteValue()));// 仓储单据类型
				stockOutAndInMapsJSon.put("refer_receipt_id", outAndInStockMap.get("refer_receipt_id"));// 单据号id
				stockOutAndInMapsJSon.put("refer_receipt_code", outAndInStockMap.get("refer_receipt_code"));// 单据号
				stockOutAndInMapsJSon.put("refer_receipt_rid", outAndInStockMap.get("refer_receipt_rid"));// 单据行号
				stockOutAndInMapsJSon.put("name_zh", outAndInStockMap.get("name_zh"));// 计量单位
				stockOutAndInMapsJSon.put("qty", outAndInStockMap.get("qty"));// 数量
				stockOutAndInMapsJSon.put("create_time",
						UtilString.getShortStringForDate((Date) outAndInStockMap.get("create_time")));// 创建日期
				stockOutAndInMapsJSon.put("posting_date",
						UtilString.getShortStringForDate((Date) outAndInStockMap.get("posting_date")));// 入账日期
				stockOutAndInMapsJSon.put("user_name", outAndInStockMap.get("user_name"));// 创建人
				stockOutAndInMapsJSon.put("fty_id", outAndInStockMap.get("fty_id"));// 工厂id
				stockOutAndInMapsJSon.put("fty_code", outAndInStockMap.get("fty_code"));// 工厂编号
				stockOutAndInMapsJSon.put("receipt_type",
						UtilString.getStringOrEmptyForObject(outAndInStockMap.get("mat_doc_type")));// 业务单据类型
				body.add(stockOutAndInMapsJSon);

			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	@RequestMapping(value = "/download_stock_out_and_in", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadStockOutAndIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		ArrayList<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			String createTimeBegin = request.getParameter("create_time_begin");// 创建日期开始
			String createTimeEnd = request.getParameter("create_time_end");// 创建日期开始结束
			String createUser = request.getParameter("create_user");// 创建人
			String moveTypeCode = request.getParameter("move_type_code");// 移动类型编码
			String ftyId = request.getParameter("fty_id");// 工厂id
			String locationId = request.getParameter("location_id");// 库存地点id
			String specStockCode = request.getParameter("spec_stock_code");// 特殊库存代码
			String matCode = request.getParameter("mat_code");// 物料编码
			String matName = request.getParameter("mat_name");// 物料描述
			String bizTypes = request.getParameter("biz_types");// 业务类型
			String referReceiptCode = request.getParameter("refer_receipt_code");// 单据编号
			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(matCode);// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组

			List<Map<String, Object>> outAndInStockMaps = stockQueryService.listOutAndInStockOnPaging(createTimeBegin,
					createTimeEnd, createUser, moveTypeCode, ftyId, locationId, specStockCode, matCode, matName,
					bizTypes, utilMatCodes, referReceiptCode, pageIndex, pageSize, total, false);
			if (outAndInStockMaps != null && outAndInStockMaps.size() > 0) {

				for (Map<String, Object> innerMap : outAndInStockMaps) {
					Integer biz_type = new Integer((int) innerMap.get("biz_type"));
					Integer mat_doc_type = new Integer((int) innerMap.get("mat_doc_type"));
					Map<String, Object> stockOutAndInMapsJSon = new HashMap<String, Object>();
					stockOutAndInMapsJSon.put("biz_type_name", EnumReceiptType.getNameByValue(biz_type.byteValue()));// 业务类型
					stockOutAndInMapsJSon.put("mat_id", innerMap.get("mat_id"));// 物料id
					stockOutAndInMapsJSon.put("mat_code", innerMap.get("mat_code"));// 物料编码
					stockOutAndInMapsJSon.put("mat_name", innerMap.get("mat_name"));// 物料描述
					stockOutAndInMapsJSon.put("fty_name", innerMap.get("fty_name"));// 工厂描述
					stockOutAndInMapsJSon.put("location_name", innerMap.get("location_name"));// 库存地点描述
					stockOutAndInMapsJSon.put("move_type_code", innerMap.get("move_type_code"));// 移动类型编码
					stockOutAndInMapsJSon.put("batch", innerMap.get("batch"));// 批次号
					stockOutAndInMapsJSon.put("spec_stock_code", innerMap.get("spec_stock_code"));// 特殊库存编码
					stockOutAndInMapsJSon.put("mat_doc_code", innerMap.get("mat_doc_code"));// 物料凭证
					stockOutAndInMapsJSon.put("mat_doc_rid", innerMap.get("mat_doc_rid"));// 物料凭证行项目
					stockOutAndInMapsJSon.put("mat_doc_type_name",
							EnumReceiptType.getNameByValue(mat_doc_type.byteValue()));// 仓储单据类型
					stockOutAndInMapsJSon.put("refer_receipt_id", innerMap.get("refer_receipt_id"));// 单据号id
					stockOutAndInMapsJSon.put("refer_receipt_code", innerMap.get("refer_receipt_code"));// 单据号
					stockOutAndInMapsJSon.put("refer_receipt_rid", innerMap.get("refer_receipt_rid"));// 单据行号
					stockOutAndInMapsJSon.put("name_zh", innerMap.get("name_zh"));// 计量单位
					stockOutAndInMapsJSon.put("qty", innerMap.get("qty"));// 数量
					stockOutAndInMapsJSon.put("create_time",
							UtilString.getShortStringForDate((Date) innerMap.get("create_time")));// 创建日期
					stockOutAndInMapsJSon.put("posting_date",
							UtilString.getShortStringForDate((Date) innerMap.get("posting_date")));// 入账日期
					stockOutAndInMapsJSon.put("user_name", innerMap.get("user_name"));// 创建人
					stockOutAndInMapsJSon.put("fty_id", innerMap.get("fty_id"));// 工厂id
					stockOutAndInMapsJSon.put("fty_code", innerMap.get("fty_code"));// 工厂编号
					stockOutAndInMapsJSon.put("receipt_type",
							UtilString.getStringOrEmptyForObject(innerMap.get("mat_doc_type")));// 业务单据类型
					stockOutAndInMapsJSon.put("receipt_type_name",
							EnumReceiptType.getNameByValue(UtilObject.getByteOrNull(innerMap.get("mat_doc_type"))));// 业务单据类型
					returnList.add(stockOutAndInMapsJSon);
				}
			}

			Map<String, String> relationMap = new HashMap<String, String>();
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("fty_name", "工厂名称");
			relationMap.put("location_name", "库存地点");
			relationMap.put("move_type_code", "移动类型");
			relationMap.put("batch", "批次编号");
			relationMap.put("spec_stock_code", "特殊库存代码");
			relationMap.put("mat_doc_code", "物料凭证");
			relationMap.put("refer_receipt_rid", "物料凭证行项目");
			relationMap.put("receipt_type_name", "仓储单据类型");
			relationMap.put("refer_receipt_code", "单据号");
			relationMap.put("name_zh", "计量单位");
			relationMap.put("qty", "数量");
			relationMap.put("create_time", "创建时间");
			relationMap.put("posting_date", "过账日期");
			relationMap.put("user_name", "创建人");

			List<String> orderList = new ArrayList<String>();
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("工厂名称");
			orderList.add("库存地点");
			orderList.add("移动类型");
			orderList.add("批次编号");
			orderList.add("特殊库存代码");
			orderList.add("物料凭证");
			orderList.add("物料凭证行项目");
			orderList.add("仓储单据类型");
			orderList.add("单据号");
			orderList.add("计量单位");
			orderList.add("数量");
			orderList.add("创建时间");
			orderList.add("过账日期");
			orderList.add("创建人");

			String root = constantConfig.getTempFilePath();

			String download_file_name = "materiel";// 物料凭证查询

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, returnList, relationMap, orderList, root);

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
	 * 库龄考核查询
	 * 
	 * @param 刘宇
	 *            2018.02.25
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_stock_days", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStockDays(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONArray body = new JSONArray();
		try {
			String dateBegin = json.getString("date_begin");// 积压天数开始
			String dateEnd = json.getString("date_end");// 积压天数结束
			String checkDate = json.getString("check_date");// 考核日期
			String ftyId = json.getString("fty_id");// 工厂id
			String locationId = json.getString("location_id");// 库存地点id

			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			List<Map<String, Object>> stockDaysMaps = stockQueryService.listStockDaysOnPaging(dateBegin, dateEnd,
					checkDate, ftyId, locationId, pageIndex, pageSize, total, true);

			JSONObject re = new JSONObject();
			if (stockDaysMaps != null && stockDaysMaps.size() > 0) {
				total = Integer.parseInt(stockDaysMaps.get(0).get("totalCount").toString());

				if (UtilString.isNullOrEmpty(ftyId) == false) {
					re = stockAgeCheckService.getVerprBySap(stockDaysMaps, ftyId);
				} else {
					re = stockAgeCheckService.getVerprBySap(stockDaysMaps, "");
				}

			} else {
				re.put("RETURNVALUE", "E");
			}
			stockDaysMaps = stockQueryService.getNewMaps(re, stockDaysMaps);// 含有移动平均价和库存金额的库龄考核集合

			for (Map<String, Object> stockDaysMap : stockDaysMaps) {
				JSONObject stockDaysMapsJSon = new JSONObject();

				stockDaysMapsJSon.put("check_date", stockDaysMap.get("check_date"));// 考核日期
				stockDaysMapsJSon.put("mat_id", stockDaysMap.get("mat_id"));// 物料id
				stockDaysMapsJSon.put("mat_code", stockDaysMap.get("mat_code"));// 物料编码
				stockDaysMapsJSon.put("mat_name", stockDaysMap.get("mat_name"));// 物料描述
				stockDaysMapsJSon.put("name_zh", stockDaysMap.get("name_zh"));// 计量单位
				stockDaysMapsJSon.put("fty_id", stockDaysMap.get("fty_id"));// 工厂id
				stockDaysMapsJSon.put("fty_code", stockDaysMap.get("fty_code"));// 工厂编码
				stockDaysMapsJSon.put("fty_name", stockDaysMap.get("fty_name"));// 工厂描述
				stockDaysMapsJSon.put("location_id", stockDaysMap.get("location_id"));// 库存地点id
				stockDaysMapsJSon.put("location_code", stockDaysMap.get("location_code"));// 库存地点编码
				stockDaysMapsJSon.put("location_name", stockDaysMap.get("location_name"));// 库存地点描述
				stockDaysMapsJSon.put("batch", stockDaysMap.get("batch"));// 批次号
				stockDaysMapsJSon.put("in_date", stockDaysMap.get("in_date"));// 入库时间
				stockDaysMapsJSon.put("stcok_days", stockDaysMap.get("stcok_days"));// 库龄（天）
				stockDaysMapsJSon.put("qty", stockDaysMap.get("qty"));// 库存数量
				stockDaysMapsJSon.put("move_average_sum", stockDaysMap.get("move_average_sum"));// 移动平均价
				stockDaysMapsJSon.put("stock_sum", stockDaysMap.get("stock_sum"));// 库存金额
				stockDaysMapsJSon.put("spec_stock", stockDaysMap.get("spec_stock"));// 特殊库存标识
				stockDaysMapsJSon.put("spec_stock_name", stockDaysMap.get("spec_stock_name"));// 特殊库存描述
				stockDaysMapsJSon.put("batch_spec_value", stockDaysMap.get("batch_spec_value"));// 考核完成标示
				stockDaysMapsJSon.put("demand_dept", stockDaysMap.get("demand_dept"));// 使用部门
				body.add(stockDaysMapsJSon);

			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (InterfaceCallException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			errorStatus = false;
			// logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 导出Excel
	 * 
	 * @date 2017年11月17日
	 * @author 兰英爽
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_stock_days", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadStockDays(HttpServletRequest request, HttpServletResponse response) throws IOException {

		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {

			String dateBegin = request.getParameter("date_begin");// 积压天数开始
			String dateEnd = request.getParameter("date_end");// 积压天数结束
			String checkDate = request.getParameter("check_date");// 考核日期
			String ftyId = request.getParameter("fty_id");// 工厂id
			String locationId = request.getParameter("location_id");// 库存地点id

			List<Map<String, Object>> stockDaysMaps = stockQueryService.listStockDaysOnPaging(dateBegin, dateEnd,
					checkDate, ftyId, locationId, pageIndex, pageSize, total, false);

			JSONObject re = new JSONObject();
			if (stockDaysMaps != null && stockDaysMaps.size() > 0) {

				if (UtilString.isNullOrEmpty(ftyId) == false) {
					re = stockAgeCheckService.getVerprBySap(stockDaysMaps, ftyId);
				} else {
					re = stockAgeCheckService.getVerprBySap(stockDaysMaps, "");
				}

			} else {
				re.put("RETURNVALUE", "E");
			}
			stockDaysMaps = stockQueryService.getNewMaps(re, stockDaysMaps);// 含有移动平均价和库存金额的库龄考核集合

			for (Map<String, Object> stockDaysMap : stockDaysMaps) {
				Map<String, Object> stockDaysMapsJSon = new HashMap<String, Object>();

				stockDaysMapsJSon.put("check_date", stockDaysMap.get("check_date"));// 考核日期
				stockDaysMapsJSon.put("mat_id", stockDaysMap.get("mat_id"));// 物料id
				stockDaysMapsJSon.put("mat_code", stockDaysMap.get("mat_code"));// 物料编码
				stockDaysMapsJSon.put("mat_name", stockDaysMap.get("mat_name"));// 物料描述
				stockDaysMapsJSon.put("name_zh", stockDaysMap.get("name_zh"));// 计量单位
				stockDaysMapsJSon.put("fty_id", stockDaysMap.get("fty_id"));// 工厂id
				stockDaysMapsJSon.put("fty_code", stockDaysMap.get("fty_code"));// 工厂编码
				stockDaysMapsJSon.put("fty_name", stockDaysMap.get("fty_name"));// 工厂描述
				stockDaysMapsJSon.put("location_id", stockDaysMap.get("location_id"));// 库存地点id
				stockDaysMapsJSon.put("location_code", stockDaysMap.get("location_code"));// 库存地点编码
				stockDaysMapsJSon.put("location_name", stockDaysMap.get("location_name"));// 库存地点描述
				stockDaysMapsJSon.put("batch", stockDaysMap.get("batch"));// 批次号
				stockDaysMapsJSon.put("in_date", stockDaysMap.get("in_date"));// 入库时间
				stockDaysMapsJSon.put("stcok_days", stockDaysMap.get("stcok_days"));// 库龄（天）
				stockDaysMapsJSon.put("qty", stockDaysMap.get("qty"));// 库存数量
				stockDaysMapsJSon.put("move_average_sum", stockDaysMap.get("move_average_sum"));// 移动平均价
				stockDaysMapsJSon.put("stock_sum", stockDaysMap.get("stock_sum"));// 库存金额
				stockDaysMapsJSon.put("spec_stock", stockDaysMap.get("spec_stock"));// 特殊库存标识
				stockDaysMapsJSon.put("spec_stock_name", stockDaysMap.get("spec_stock_name"));// 特殊库存描述
				stockDaysMapsJSon.put("batch_spec_value", stockDaysMap.get("batch_spec_value"));// 考核完成标示
				stockDaysMapsJSon.put("demand_dept", stockDaysMap.get("demand_dept"));// 使用部门
				list.add(stockDaysMapsJSon);

			}

			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("check_date", "考核日期");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("name_zh", "计量单位");
			relationMap.put("fty_code", "工厂");
			relationMap.put("location_code", "库存地点");
			relationMap.put("batch", "批次号");
			relationMap.put("in_date", "入库时间");
			relationMap.put("stcok_days", "库龄(天)");
			relationMap.put("qty", "库存数量");
			relationMap.put("move_average_sum", "移动平均价");
			relationMap.put("stock_sum", "库存金额");
			relationMap.put("spec_stock", "特殊库存标识");
			relationMap.put("spec_stock_name", "特殊库存描述");
			relationMap.put("batch_spec_value", "考核完成标示");
			relationMap.put("zxqbm", "使用部门");

			List<String> orderList = new ArrayList<String>();
			orderList.add("考核日期");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("计量单位");
			orderList.add("工厂");
			orderList.add("库存地点");
			orderList.add("批次号");
			orderList.add("入库时间");
			orderList.add("库龄(天)");
			orderList.add("库存数量");
			orderList.add("移动平均价");
			orderList.add("库存金额");
			orderList.add("特殊库存标识");
			orderList.add("特殊库存描述");
			orderList.add("考核完成标示");
			orderList.add("使用部门");

			String root = constantConfig.getTempFilePath();

			String download_file_name = "overstock";// 库存积压

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, list, relationMap, orderList, root);

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
	 * 批次信息查询
	 * 
	 * @param 刘宇
	 *            2018.02.11
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_batch_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachBatchInfo(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Map<String, Object> batchSpecMap = new HashMap<String, Object>();
		try {
			String strBatch = json.getString("batch");// 批次号
			String strMatId = json.getString("mat_id");// 物料id
			String strFtyId = json.getString("fty_id");// 工厂id

			Integer ftyId = 0;
			Integer matId = 0;
			Long batch = null;
			if (UtilString.isNullOrEmpty(strBatch) == false) {
				batch = Long.parseLong(strBatch);

			}
			if (UtilString.isNullOrEmpty(strMatId) == false) {
				matId = Integer.parseInt(strMatId);

			}
			if (UtilString.isNullOrEmpty(strFtyId) == false) {
				ftyId = Integer.parseInt(strFtyId);

			}
			batchSpecMap = commonService.getBatchSpecList(batch, ftyId, matId);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
		}

		JSONObject object = UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total,
				batchSpecMap);

		UtilJSONConvert.deleteNull(object);

		return object;
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
			list=   stockQueryService.selectWarehouseListByWhId(wh_id);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, list);
	}

}
