package com.inossem.wms.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.inossem.wms.exception.ExcelCellTypeException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumTemplateType;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IMatMajorDataService;
import com.inossem.wms.service.dic.IPositionService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/data")
public class DataController {
	private static final Logger logger = LoggerFactory.getLogger(DataController.class);

	@Autowired
	private IMatMajorDataService matMajorDataService;

	@Autowired
	IPositionService positionService;

	@Autowired
	private IStockQueryService stockQueryService;

	@RequestMapping(value = "/upload_excel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void upLoadExcel(@RequestParam("file") MultipartFile fileInClient, HttpServletRequest request,
			HttpServletResponse response) {
		String type = request.getParameter("type");
		try {

			String fileName = matMajorDataService.upLoadExcel(fileInClient,
					request.getSession().getServletContext().getRealPath(""), EnumTemplateType.getFolderByValue(type));
			response.sendRedirect(String.format("/wms/web/html/conf/initialize/uploadresult.html?type=%s&fileName=%s",
					type, fileName));
		} catch (IOException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	@RequestMapping(value = "/get_excel_data", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getExcelData(String file_name, String type, HttpServletRequest request,
			CurrentUser user) {

		JSONObject body = new JSONObject();
		body = new JSONObject();
		int count = 0;
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		try {
			if (EnumTemplateType.TEMPLATE_MATERIAL.getValue().equalsIgnoreCase(type)) {
				count = matMajorDataService.getExcelData(request.getSession().getServletContext().getRealPath(""),
						file_name, EnumTemplateType.getFolderByValue(type));
			} else if (EnumTemplateType.TEMPLATE_POSITION.getValue().equalsIgnoreCase(type)) {
				count = positionService.getExcelData(request.getSession().getServletContext().getRealPath(""), file_name,
						EnumTemplateType.getFolderByValue(type));
			} else if (EnumTemplateType.TEMPLATE_STOCK_POSITION.getValue().equalsIgnoreCase(type)) {
				count = stockQueryService.getExcelData(request.getSession().getServletContext().getRealPath(""),
						file_name, EnumTemplateType.getFolderByValue(type));
			}
		} catch (ExcelCellTypeException ec) {
			logger.error("", ec);

			errorCode = ec.getErrorCode();
			errorString = ec.getMessage();
			status = false;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
			logger.error("", e);
		}
		body.put("count", count);
		return UtilResult.getResultToJson(status, errorCode, errorString, body);
	}

	@RequestMapping(value = "/down_template", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BufferedOutputStream out = null;
		InputStream bis = null;

		try {
			String type = request.getParameter("type");

			// 服务器端文件全路径
			String fileName;
			String downLoadName = EnumTemplateType.getDownLoadNameByValue(type);
			if (EnumTemplateType.toMap().containsKey(type)) {
				fileName = request.getSession().getServletContext().getRealPath("") + "template" + File.separator
						+ downLoadName;
			} else {
				fileName = "";
			}

			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(new File(fileName)));

			// 设置文件下载头

			response.addHeader("Content-Disposition", "attachment;filename=" + downLoadName);
			// 设置文件ContentType类型，这样设置，会自动判断下载文件类型
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
