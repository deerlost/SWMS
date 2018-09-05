package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.BizBusinessHistoryVo;
import com.inossem.wms.service.biz.IBusinessHistoryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import net.sf.json.JSONObject;

/**
 * 收发存查询
 * @author sw
 *
 */
@Controller
@RequestMapping("/biz/query")
public class DispatcherController {
	
	private static final Logger logger = LoggerFactory.getLogger(DispatcherController.class);
	
	@Autowired
	private IBusinessHistoryService historyService;
	
	@Autowired
	private IDicWarehouseService warehouseService;
	
	@Autowired
	private ServerConfig constantConfig;
	
	/**
	 * 获取列表页
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = { "/get_dispatcher_list" }, method = { RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getList(@RequestBody JSONObject json,CurrentUser cUser) {
		boolean status = true;
	//	List<BizBusinessHistoryVo> list = new ArrayList<BizBusinessHistoryVo>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			param.put("ftyId", UtilObject.getIntegerOrNull(json.get("fty_id")));
			param.put("locationId", UtilObject.getIntegerOrNull(json.get("location_id")));
			param.put("matCode",  UtilObject.getStringOrNull(json.get("mat_code")));
			param.put("whId", UtilObject.getIntegerOrNull(json.get("wh_id")));
			param.put("createTimeStart", UtilObject.getStringOrNull(json.get("create_time_start")));
			param.put("createTimeEnd", UtilObject.getStringOrNull(json.get("create_time_end")));
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			param.put("paging", true);
			param.put("pageIndex", Integer.valueOf(pageIndex));
			param.put("pageSize", Integer.valueOf(pageSize));
			param.put("totalCount", Integer.valueOf(totalCount));
			param.put("sortColumn", sortColumn);
			param.put("sortAscend", sortAscend);
			param.put("userId", cUser.getUserId());
			param.put("refresh", json.getInt("refresh"));// 刷新
		//	list = this.historyService.selectHistoryList(param);
			list = historyService.selectInAndOut(param);
			if (list != null && list.size() > 0) {
				total = UtilObject.getIntOrZero(list.get(0).get("totalCount"));
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装管理列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		JSONObject obj = UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, list);
		UtilJSONConvert.setNullToEmpty(obj);
		return obj;
	} 
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_dispatcher_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void download(CurrentUser cuser, HttpServletRequest request,HttpServletResponse response) throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<BizBusinessHistoryVo> list = new ArrayList<BizBusinessHistoryVo>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			String ftyId = request.getParameter("fty_id");
			String locationId = request.getParameter("location_id");
			String createTimeStart = request.getParameter("create_time_start");
			String createTimeEnd = request.getParameter("create_time_end");
			String matCode = request.getParameter("mat_code");
			String whId = request.getParameter("wh_id");
			
			if (StringUtils.hasText(ftyId)) {
				param.put("ftyId", ftyId);
			}
			if (StringUtils.hasText(locationId)) {
				param.put("locationId", locationId);
			}
			if (StringUtils.hasText(createTimeStart)) {
				param.put("createTimeStart", createTimeStart);
			}
			if (StringUtils.hasText(createTimeEnd)) {
				param.put("createTimeEnd", createTimeEnd);
			}
			if (StringUtils.hasText(matCode)) {
				param.put("matCode", matCode);
			}
			if (StringUtils.hasText(whId)) {
				param.put("whId", whId);
			}
			param.put("paging", false);
			param.put("userId", cuser.getUserId());
			param.put("refresh", 1);// 刷新
//			list = this.historyService.selectHistoryList(param);
//			for(BizBusinessHistoryVo historyVo : list) {
//				Map<String,Object> map = this.objectToMap(historyVo);
//				map.put("createTimeStart", createTimeStart);
//				map.put("createTimeEnd", createTimeEnd);
//				returnList.add(map);
//			}
			returnList = historyService.selectInAndOut(param);
			
			
			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("matCode", "物料编码");
			relationMap.put("matName", "物料描述");
			relationMap.put("unitName", "计量单位");
			relationMap.put("beginQty", "初期库存");
			relationMap.put("inputQty", "入库数量");
			relationMap.put("outputQty", "出库数量");
			relationMap.put("endQty", "结余库存");
			relationMap.put("erpBatch", "ERP批次");
			relationMap.put("ftyName", "工厂");
			relationMap.put("locationName", "库存地点");
		//	relationMap.put("packageTypeCode", "包装类型");
			relationMap.put("createTimeStart", "开始日期");
			relationMap.put("createTimeEnd", "结束日期");

			List<String> orderList = new ArrayList<String>();
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("计量单位");
			orderList.add("初期库存");
			orderList.add("入库数量");
			orderList.add("出库数量");
			orderList.add("结余库存");
			orderList.add("ERP批次");
			orderList.add("工厂");
			orderList.add("库存地点");
		//	orderList.add("包装类型");
			orderList.add("开始日期");
			orderList.add("结束日期");
			
			String root = constantConfig.getTempFilePath();

			String download_file_name = "收发存查询";

			
			
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
	
	private Map<String, Object> objectToMap(Object obj)throws Exception {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
	      field.setAccessible(true);
	      String fieldName = field.getName();
	      Object value = field.get(obj);
	      map.put(fieldName, value);
        }
        return map;
    }
	
	
	/**
	 * 获取列表页
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = { "/get_warehouse_list" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody Object getWarehouseList() {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Object result = new Object();
		try {
			result = warehouseService.queryWarehouseList();
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装管理列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	} 
	

}
