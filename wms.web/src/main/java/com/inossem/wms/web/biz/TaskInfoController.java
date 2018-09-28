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
import com.inossem.wms.service.biz.impl.TaskServiceImpl;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作业单报表
 * @author sw
 *
 */
@Controller
@RequestMapping("/biz/query")
public class TaskInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskInfoController.class);
	
	@Autowired
	private TaskServiceImpl  taskService;
	
	@Autowired
	private ServerConfig constantConfig;
	
	/**
	 * 作业单报表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/select_task_info_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectTaskInfoList(@RequestBody JSONObject json) {
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
			String sortColumn = UtilObject.getStringOrNull(json.get("sort_column"));
			param.put("matCode", UtilObject.getStringOrNull(json.get("mat_code")));
			param.put("matName", UtilObject.getStringOrNull(json.get("mat_name")));
			param.put("ftyId", UtilObject.getIntegerOrNull(json.get("fty_id")));
			param.put("locationId", UtilObject.getIntegerOrNull(json.get("location_id")));
			param.put("whId", UtilObject.getIntegerOrNull(json.get("wh_id")));
			param.put("createTimeS", UtilObject.getStringOrNull(json.get("create_time_s")));
			param.put("createTimeE", UtilObject.getStringOrNull(json.get("create_time_e")));
			param.put("erpBatch", UtilObject.getStringOrNull(json.get("erp_batch")));
			param.put("productBatch", UtilObject.getStringOrNull(json.get("product_batch")));
			param.put("paging", true);
			param.put("pageIndex", pageIndex);
			param.put("pageSize", pageSize);
			param.put("totalCount", totalCount);
			param.put("sortAscend", sortAscend);
			param.put("sortColumn", sortColumn);
			JSONArray receiptTypeList = json.getJSONArray("receipt_type_list");
			param.put("receiptTypeList", receiptTypeList);
			list = taskService.selectTaskInfoOnPaging(param);
			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("作业单报表 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex,pageSize, total, list);
	}
	
	
	/** 
	 * 导出作业单列表
	 * @param request
	 * @param response
	 * @param wh_id
	 * @param fty_id
	 * @param location_id
	 * @param product_batch
	 * @param mat_code
	 * @param mat_name
	 * @param create_time_s
	 * @param erp_batch
	 * @param create_time_e
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_task_info_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void downloadTaskInfoList(HttpServletRequest request,HttpServletResponse response,Integer wh_id,Integer fty_id,Integer location_id,String product_batch,String mat_code,String mat_name,String create_time_s,String erp_batch,String create_time_e) throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			param.put("matCode", UtilObject.getStringOrNull(mat_code));
			param.put("matName", UtilObject.getStringOrNull(mat_name));
			param.put("productBatch", UtilObject.getStringOrNull(product_batch));
			param.put("erpBatch", UtilObject.getStringOrNull(erp_batch));
			param.put("ftyId", UtilObject.getIntegerOrNull(fty_id));
			param.put("locationId", UtilObject.getIntegerOrNull(location_id));
			param.put("whId", UtilObject.getIntegerOrNull(wh_id));
			param.put("createTimeS", UtilObject.getStringOrNull(create_time_s));
			param.put("createTimeE", UtilObject.getStringOrNull(create_time_e));
			param.put("paging", false);
			returnList = taskService.selectTaskInfoOnPaging(param);
			
			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("create_user", "创建人");
			relationMap.put("create_time", "创建时间");
			relationMap.put("status_name", "状态");
			relationMap.put("stock_task_req_code", "作业请求号");
			relationMap.put("stock_task_code", "作业单号");
			relationMap.put("receipt_type_name", "作业单类型");
			relationMap.put("refer_receipt_code", "业务单号");
			relationMap.put("refer_receipt_type_name", "业务单类型");
			relationMap.put("delivery_driver", "司机");
			relationMap.put("delivery_vehicle", "车辆");
			relationMap.put("tally_clerk_name", "理货员");
			relationMap.put("forklift_worker_name", "叉车工");
			relationMap.put("remover_name", "搬运工");
			relationMap.put("head_remark", "作业单抬头备注");
			relationMap.put("stock_task_rid", "作业单行号");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("unit_name", "单位");
			relationMap.put("qty", "作业量");
			relationMap.put("qty_kg", "作业量公斤数");
			relationMap.put("package_type_name", "包装类型");
			relationMap.put("erp_batch", "ERP批次");
			relationMap.put("product_batch", "生产批次");
			relationMap.put("item_remark", "作业单行备注");
			List<String> orderList = new ArrayList<String>();
			orderList.add("创建人");
			orderList.add("创建时间");
			orderList.add("状态");
			orderList.add("作业请求号");
			orderList.add("作业单号");
			orderList.add("作业单类型");
			orderList.add("业务单号");
			orderList.add("业务单类型");
			orderList.add("司机");
			orderList.add("车辆");
			orderList.add("理货员");
			orderList.add("叉车工");
			orderList.add("搬运工");
			orderList.add("作业单抬头备注");
			orderList.add("作业单行号");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("单位");
			orderList.add("作业量");
			orderList.add("作业量公斤数");
			orderList.add("包装类型");
			orderList.add("ERP批次");
			orderList.add("生产批次");
			orderList.add("作业单行备注");
			
			String root = constantConfig.getTempFilePath();

			String download_file_name = "作业单报表";

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
			logger.error("作业单报表", e);
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
