package com.inossem.wms.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicPrinter;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IClassTypeService;
import com.inossem.wms.service.dic.IPrinterService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/printer")
public class PrinterController {
	private static final Logger logger = LoggerFactory.getLogger(PrinterController.class);
	@Autowired
	private IClassTypeService classTypeService;

	@Autowired
    private 	IPrinterService printerService;
	/**
	 *打印机管理列表
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_dic_printer_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDicClassTypeList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;
		int pageIndex = 0;
		int pageSize = 0;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

		Map<String, Object> param = this.getParamMapToPageing(json);
		param.put("condition", UtilObject.getStringOrEmpty(json.getString("condition"))); // 关键字
		try {
			list = printerService.getPrinterList(param);

			if (list != null && list.size() > 0) {
				Long totalCountLong = (long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
				pageSize = UtilObject.getIntOrZero(json.get("page_size"));
				pageIndex =UtilObject.getIntOrZero(json.get("page_index")); 
			}
		} catch (Exception e) {
			logger.error("打印机列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code,pageIndex, pageSize,  total, list);
	}

	/**
	 * 分页参数
	 * 
	 * @param json
	 * @return
	 */
	private Map<String, Object> getParamMapToPageing(JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();

		int pageIndex = json.getInt("page_index");
		int pageSize = json.getInt("page_size");
		int totalCount = EnumPage.TOTAL_COUNT.getValue();
		if (json.containsKey("total")) {
			totalCount = json.getInt("total");
		}
		boolean sortAscend = json.getBoolean("sort_ascend");
		String sortColumn = json.getString("sort_column");
		if (json.containsKey("paging")) {
			param.put("paging", json.getBoolean("paging"));
		} else {
			param.put("paging", true);
		}
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		param.put("sortAscend", sortAscend);
		param.put("sortColumn", sortColumn);

		return param;
	}

	/**
	 * 班次管理新增或修改
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/save_or_update_printer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveOrUpdateClassType(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String massage = "修改成功";
		try {
			status = printerService.saveOrUpdateClassType(json);
		} catch (Exception e) {
			logger.error("打印管理新增或修改 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			massage = "操作失败";
		}

		return UtilResult.getResultToJson(status, error_code, massage,"");

	}
	
	@RequestMapping(value = "/delete_printer", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deletePrinter(Integer printer_id) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String massage = "删除成功";
		try {
			status = printerService.deletePrinterById(printer_id);
		} catch (Exception e) {
			logger.error("班次管理新增或修改 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			massage = "操作失败";
		}

		return UtilResult.getResultToJson(status, error_code, massage,"");

	}

}
