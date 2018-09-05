package com.inossem.wms.web.biz;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IPkgOperationService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

/**
 * 包装物扫描
 * 
 * @author sw
 *
 */
@Controller
@RequestMapping("/biz/package")
public class PackageScanController {
	
	private static final Logger logger = LoggerFactory.getLogger(PackageScanController.class);

	@Autowired
	private IPkgOperationService pkgService;
	
	
	@RequestMapping(value = { "/get_package_info" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getPackegeInfo(@RequestParam("code")String code) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = pkgService.selectPackegeInfo(code);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装物信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}

}
