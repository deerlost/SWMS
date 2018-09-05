package com.inossem.wms.portable.biz;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IOutPutCWService;
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
	
	
	
}
