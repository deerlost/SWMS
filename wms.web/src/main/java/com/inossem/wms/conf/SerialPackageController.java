package com.inossem.wms.conf;

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
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicPackClassification;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.SerialPackageVo;
import com.inossem.wms.service.biz.ISerialPackageService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 包装物SN管理
 * @author sw
 *
 */
@Controller
@RequestMapping("/conf/serial_package")
public class SerialPackageController {

	
	private static final Logger logger = LoggerFactory.getLogger(SerialPackageController.class);
	
	@Autowired
	private ISerialPackageService packageService;
	
	@Autowired
	private ServerConfig constantConfig;
	
	/**
	 * 获取列表页
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = { "/get_serial_package_list" }, method = { RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getSerialPackageList(@RequestBody JSONObject json) {
		boolean status = true;
		List<SerialPackageVo> list = new ArrayList<SerialPackageVo>();
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
			param.put("startCreateTime", UtilObject.getStringOrNull(json.get("start_create_time")));
			param.put("endCreateTime", UtilObject.getStringOrNull(json.get("end_create_time")));
			param.put("packageCode", UtilObject.getStringOrNull(json.get("package_code")));
			param.put("classificatId", UtilObject.getIntegerOrNull(json.get("classificat_id")));
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			param.put("paging", Boolean.valueOf(true));
			param.put("pageIndex", Integer.valueOf(pageIndex));
			param.put("pageSize", Integer.valueOf(pageSize));
			param.put("totalCount", Integer.valueOf(totalCount));
			param.put("sortColumn", sortColumn);
			param.put("sortAscend", sortAscend);
			list = this.packageService.selectSerialPackageList(param);
			if(list!=null && !list.isEmpty()) {
				total = list.get(0).getTotalCount();
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装SN序列管理列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, list);
	}
	
	/**
	 * 保存包装物SN
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/save_serial_package" }, method = { RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody Object saveSerialPackage(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		List<Integer> ids = new ArrayList<Integer>();
		try {
			ids = this.packageService.saveSerialPackage(jsonData,user.getUserId());
			msg = "提交成功";
		} catch (Exception e) {
			logger.error("保存包装物SN", e);
			msg = "程序异常";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, msg, ids);
	}
	
	/**
	 * 删除包装物SN
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/delete_serial_package" }, method = { RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody Object delete(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		try {
			JSONArray array = jsonData.getJSONArray("ids");
			this.packageService.deleteSerialPackageByIds(array,user.getUserId());
			msg = "操作成功";
		} catch (Exception e) {
			logger.error(" 删除或修改包装物SN --", e);
			msg = "程序异常";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, msg, "");
	}
	
	/**
	 * 包装物分类，包装物类型，包装物供应商
	 * @param json
	 * @return
	 */
	@RequestMapping(value = { "/get_dic_data_list" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getDicDataList() {
		boolean status = true;
		List<DicPackClassification> list = new ArrayList<DicPackClassification>();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			list = this.packageService.selectClassList();
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装SN序列管理", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code,list);
	}
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param start_create_time
	 * @param end_create_time
	 * @param start_package_code
	 * @param end_package_code
	 * @param classificat_id
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_package_data", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void download(HttpServletRequest request,HttpServletResponse response,String start_create_time,
			String end_create_time,String package_code,Integer classificat_id) throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			param.put("startCreateTime",UtilObject.getStringOrNull(start_create_time) );
			param.put("endCreateTime", UtilObject.getStringOrNull(end_create_time));
			param.put("packageCode", UtilObject.getStringOrNull(package_code));
			param.put("classificatId", UtilObject.getIntegerOrNull(classificat_id));
			param.put("paging", false);
			List<SerialPackageVo> list = this.packageService.selectSerialPackageList(param);
			
			for(SerialPackageVo serialPackageVo : list) {
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("packageCode", UtilProperties.getInstance().getInfoUrl()+"?SN=" + serialPackageVo.getPackageCode());
				data.put("packageTypeName", serialPackageVo.getPackageTypeName());
				data.put("supplierName", serialPackageVo.getSupplierName());
				returnList.add(data);
			}
			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("packageCode", "SN序号");
			relationMap.put("packageTypeName", "包装物类型");
			relationMap.put("supplierName", "供应商名称");
			
			List<String> orderList = new ArrayList<String>();
			orderList.add("SN序号");
			orderList.add("包装物类型");
			orderList.add("供应商名称");
			
			String root = constantConfig.getTempFilePath();

			String download_file_name = "包装物SN";

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
