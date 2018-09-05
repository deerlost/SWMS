package com.inossem.wms.portable.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.inossem.wms.util.UtilExcel;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IStockCheckService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

/**
 * 库存对账Controller
 * @author 高海涛
 * @date 2018年2月26日
 */
@Controller
@RequestMapping("/biz/checkstock")
public class StockCheckController {

	private static final Logger logger = LoggerFactory.getLogger(StockCheckController.class);

	@Resource
	private IStockCheckService stockCheckService; 
	
	@Autowired
	private ServerConfig constantConfig;
	
	
	/**
	 * 同步sap数据并修改状态
	 * @date 2017年10月18日
	 * @author 高海涛
	 * @return
	 */
	@RequestMapping(value = "/sync_sap_mat_doc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject syncSapMatDoc(@RequestBody JSONObject json,CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		try {
			stockCheckService.syncSapMatDoc(json.getString("fty_id"),
						                    json.getString("location_id"),
						                    json.getString("s_date"),
						                    json.getString("e_date"),cUser.getUserId());
		} catch (Exception e) {
			logger.error("同步sap数据 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		
		return UtilResult.getResultToJson(status,error_code,UtilString.STRING_EMPTY);
		
	}
	
	/**
	 * 获取凭证列表
	 * @date 2017年10月18日
	 * @author 高海涛
	 * @return
	 */
	@RequestMapping(value = "/get_mat_item_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatItemList(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> list = null;
		
		try {
			list = stockCheckService.getMatItemList(json.getString("fty_id"),
													 json.getString("location_id"),
													 json.getString("s_date"),
													 json.getString("e_date"), 
												     json.getString("status1"),
													 json.getString("status2"),
													 json.getString("mat_id"));
		} catch (Exception e) {
			logger.error("获取凭证列表 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code,list);
		
	}
	
	/**
	 * 获取对冲数据
	 * @date 2017年10月18日
	 * @author 高海涛
	 * @return
	 */
	@RequestMapping(value = "/get_mat_item_neutralize_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatItemNeutralizeList() {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> list = null;
		
		try {
			list = stockCheckService.getMatItemNeutralizeList();
			
		} catch (Exception e) {
			logger.error("获取凭证列表 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		
		return UtilResult.getResultToJson(status,error_code,list);
	}
	
	/**
	 * 保存对冲数据
	 * @date 2017年10月18日
	 * @author 高海涛
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/save_wms_mat_doc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveToWmsMatDoc(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		int count = 0 ;
		
		try {
			count = stockCheckService.saveToWmsMatDoc((List)json.getJSONArray("list"));
		
		} catch (Exception e) {
			logger.error("保存对冲数据 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code,count);
		
	}
	
	/**
	 * 处理数据
	 * @date 2017年10月18日
	 * @author 高海涛
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/save_order", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> saveOrder(@RequestBody JSONObject json,CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		int count = 0 ;
		
		try {
			count =stockCheckService.saveOrder((List)json.getJSONArray("list"),cUser);
		
		} catch (Exception e) {
			logger.error("保存对冲数据 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code,count);
		
	}
	
	/**
	 * 获取库存列表
	 * @date 2017年10月18日
	 * @author 高海涛
	 * @return
	 */
	@RequestMapping(value = "/get_storage_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getStorageList(String fty_id,String location_id,CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> list = null;
		if ("null".equals(fty_id)) {
			fty_id = "";
		}
		if ("null".equals(location_id)) {
			location_id = "";
		}
		try {
			list = stockCheckService.getStorageList(fty_id,location_id,cUser.getUserId());
			
		} catch (Exception e) {
			logger.error("获取库存列表 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		
		return UtilResult.getResultToJson(status,error_code,list);
		
	}
	
	
	/**
	 * 下载库存列表
	 * @date 2018年6月13日
	 * @author 兰英爽
	 * @return
	 */
	@RequestMapping(value = "/download_storage_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadStorageList(CurrentUser cUser, HttpServletRequest request, HttpServletResponse response) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> list = null;		
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		ArrayList<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		
		try {
			String ftyId = request.getParameter("fty_id");// 工厂
			String locationId = request.getParameter("location_id");// 库存地点
			list = stockCheckService.getStorageList(ftyId,locationId,cUser.getUserId());
			if (list!= null && list.size()>0) {
				for (Map<String, Object> map : list) {
					Map<String, Object> mapDown = new HashMap<>();
					mapDown.put("fty_code", UtilString.getStringOrEmptyForObject(map.get("fty_code")));//工厂
					mapDown.put("fty_name", UtilString.getStringOrEmptyForObject(map.get("fty_name")));//工厂描述
					mapDown.put("location_code", UtilString.getStringOrEmptyForObject(map.get("location_code")));//库存地点
					mapDown.put("location_name", UtilString.getStringOrEmptyForObject(map.get("location_name")));//库存地点描述
					mapDown.put("mat_code", UtilString.getStringOrEmptyForObject(map.get("mat_code")));//物料
					mapDown.put("mat_name", UtilString.getStringOrEmptyForObject(map.get("mat_name")));//物料描述
					mapDown.put("unit_code", UtilString.getStringOrEmptyForObject(map.get("unit_code")));//单位
					mapDown.put("unit_name", UtilString.getStringOrEmptyForObject(map.get("unit_name")));//单位描述
					mapDown.put("spec_stock", UtilString.getStringOrEmptyForObject(map.get("spec_stock")));//特殊库存标识
					mapDown.put("spec_stock_code", UtilString.getStringOrEmptyForObject(map.get("spec_stock_code")));//特殊库存代码
					mapDown.put("spec_stock_name", UtilString.getStringOrEmptyForObject(map.get("spec_stock_name")));//特殊库存描述
					mapDown.put("sap_qty", UtilString.getStringOrEmptyForObject(map.get("sap_qty")));//SAP库存数量 
					mapDown.put("wms_qty", UtilString.getStringOrEmptyForObject(map.get("wms_qty")));//仓储系统库存数量 
					mapDown.put("num", UtilString.getStringOrEmptyForObject(map.get("num")));//差异
					returnList.add(mapDown);
				}
			}
			
			// 字段对应汉语意思
			Map<String, String> relationMap = new HashMap<String, String>();
			relationMap.put("fty_code", "工厂");
			relationMap.put("fty_name", "工厂描述");
			relationMap.put("location_code", "库存地点");
			relationMap.put("location_name", "库存地点描述");
			relationMap.put("mat_code", "物料");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("unit_code", "单位");
			relationMap.put("unit_name", "单位描述");
			relationMap.put("spec_stock", "特殊库存标识");
			relationMap.put("spec_stock_code", "特殊库存代码");
			relationMap.put("spec_stock_name", "特殊库存描述");
			relationMap.put("sap_qty", "SAP库存数量");
			relationMap.put("wms_qty", "仓储系统库存数量");
			relationMap.put("num", "差异");
			
			// 表格排序顺序
			List<String> orderList = new ArrayList<String>();
			orderList.add("工厂");
			orderList.add("工厂描述");
			orderList.add("库存地点");
			orderList.add("库存地点描述");
			orderList.add("物料");
			orderList.add("物料描述");
			orderList.add("单位");
			orderList.add("单位描述");
			orderList.add("特殊库存标识");
			orderList.add("特殊库存代码");
			orderList.add("特殊库存描述");
			orderList.add("SAP库存数量");
			orderList.add("仓储系统库存数量");
			orderList.add("差异");
			
			String root = constantConfig.getTempFilePath();

			String download_file_name = "库存对账查询";// 库存对账查

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
			logger.error("库存对账下载 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		
	}
}
