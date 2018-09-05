package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.IOutPutCWService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import net.sf.json.JSONObject;

/**
 * 销售出库量统计查询
 * @author sw
 *
 */
@Controller
@RequestMapping("/biz/query")
public class CountSaleOutPutController {
	
	private static final Logger logger = LoggerFactory.getLogger(CountSaleOutPutController.class);
	
	@Autowired
	private IOutPutCWService outPutService;
	
	@Autowired
	private ServerConfig constantConfig;
	

	/**
	 * 查询销售chart需要的数据
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	@RequestMapping(value = { "/get_sale_chart_data" }, method = { RequestMethod.GET }, produces = {"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getList(String start_date,String end_date) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = outPutService.selectChartData(start_date,end_date);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("查询销售chart需要的数据", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	} 
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param product_line_id
	 * @param date
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_sale_data", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void download(HttpServletRequest request,HttpServletResponse response,Integer product_line_id,String date) throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			param.put("date", date);
			param.put("productLineId", product_line_id);
			returnList = outPutService.selectSaleDataListForExcel(param);
			
			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("stock_output_code", "出库单号");
			relationMap.put("refer_receipt_code", "交货单号");
			relationMap.put("sale_order_code", "销售订单号");
			relationMap.put("preorder_id", "合同编号");
			relationMap.put("receive_name", "客户名称");
			relationMap.put("user_name", "创建人");
			relationMap.put("create_time", "创建日期");
			relationMap.put("class_type_name", "班次");
			relationMap.put("product_line_name", "产品线");
			relationMap.put("remark1", "备注一");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("unit_name", "单位");
			relationMap.put("stock_qty", "交货数量");
			relationMap.put("location_name", "库存地点");
			relationMap.put("remark2", "备注二");
			
			
			List<String> orderList = new ArrayList<String>();
			orderList.add("出库单号");
			orderList.add("交货单号");
			orderList.add("销售订单号");
			orderList.add("合同编号");
			orderList.add("客户名称");
			orderList.add("创建人");
			orderList.add("创建日期");
			orderList.add("班次");
			orderList.add("产品线");
			orderList.add("备注一");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("单位");
			orderList.add("交货数量");
			orderList.add("库存地点");
			orderList.add("备注二");
			
			String root = constantConfig.getTempFilePath();

			String download_file_name = "销售出库统计量";

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
	 * 交货单配货情况报表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/select_cargo_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectCargoInfo(@RequestBody JSONObject json) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		try {
			pageIndex = UtilObject.getIntOrZero(json.getInt("page_index"));
			pageSize = UtilObject.getIntOrZero(json.getInt("page_size"));
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = UtilObject.getStringOrNull(json.getString("sort_column"));
			param.put("referReceiptCode", UtilObject.getStringOrNull(json.getString("keyword")));
			param.put("createTimeS", UtilObject.getStringOrNull(json.get("create_time_s")));
			param.put("createTimeE", UtilObject.getStringOrNull(json.get("create_time_e")));
			param.put("paging", true);
			param.put("pageIndex", pageIndex);
			param.put("pageSize", pageSize);
			param.put("totalCount", totalCount);
			param.put("sortAscend", sortAscend);
			param.put("sortColumn", sortColumn);
			list = outPutService.selectCargoInfoOnPaging(param);
			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("交货单配货情况报表 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex,pageSize, total, list);
	}
	
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param keyword
	 * @param create_time_s
	 * @param create_time_e
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_cargo_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadCargoInfo(HttpServletRequest request,HttpServletResponse response,String keyword,String create_time_s,String create_time_e) throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			param.put("keyword", keyword);
			param.put("createTimeS",create_time_s);
			param.put("createTimeE",create_time_e);
			param.put("paging", false);
			returnList = outPutService.selectCargoInfoOnPaging(param);
			
			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("allocate_cargo_code", "配货单号");
			relationMap.put("refer_receipt_code", "交货单号");
			relationMap.put("receive_name", "客户名称");
			relationMap.put("create_user", "创建人");
			relationMap.put("create_time", "创建日期");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("unit_name", "单位");
			relationMap.put("erp_batch", "ERP批次");
			relationMap.put("package_standard_ch", "包装规格");
			relationMap.put("order_qty", "交货单数量");
			relationMap.put("cargo_qty", "配货单数量");
			relationMap.put("task_qty", "已下架数量");
			relationMap.put("output_qty", "已出库数量");
			relationMap.put("do_qty", "未出库数量");
			
			List<String> orderList = new ArrayList<String>();
			orderList.add("配货单号");
			orderList.add("交货单号");
			orderList.add("客户名称");
			orderList.add("创建人");
			orderList.add("创建日期");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("单位");
			orderList.add("ERP批次");
			orderList.add("包装规格");
			orderList.add("交货单数量");
			orderList.add("配货单数量");
			orderList.add("已下架数量");
			orderList.add("已出库数量");
			orderList.add("未出库数量");
			
			String root = constantConfig.getTempFilePath();

			String download_file_name = "交货单配货情况报表";

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
	
}
