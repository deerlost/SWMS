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
import com.inossem.wms.model.dic.DicInstallation;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IInstallationService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/installation")
public class InstallationController {
	private static final Logger logger = LoggerFactory.getLogger(ProductLineController.class);
	 @Autowired
	 private IInstallationService installationService;
	/**
	 * 装置列表查询
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_dic_installation_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDicInstallationList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;
		int pageIndex = 0;
		int pageSize = 0;
		List<DicInstallation> list = new ArrayList<DicInstallation>();
		Map<String, Object> param = this.getParamMapToPageing(json);
		param.put("condition", UtilObject.getStringOrEmpty(json.getString("condition"))); // 关键字
		try {
			list = installationService.getDicInstallationList(param);
			if (list != null && list.size() > 0) {
				Long totalCountLong = (long) list.get(0).getTotalCount();
				total = totalCountLong.intValue();
				pageSize = list.get(0).getPageSize();
				pageIndex = list.get(0).getPageIndex();
			}
		} catch (Exception e) {
			logger.error("装置列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, pageSize, pageIndex, total, list);
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
	 * 装置新增或修改
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/save_or_update_installation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveOrUpdateInstallation(@RequestBody JSONObject json, CurrentUser cUser) {
			
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String massage = "";
		try {
			status  = installationService.saveOrUpdateProductLine(json);
		} catch (Exception e) {
			logger.error("装置新增或修改--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			massage = "操作失败";
		}

		return UtilResult.getResultToJson(status, error_code, massage, "");
	}
	
	
	@RequestMapping(value = "/delete_installation", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteInstallation( Integer installation_id, CurrentUser cUser) {
		
		
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String massage = "";
		try {
			int i = installationService.deleteInstallation(installation_id);
			if(i<=0) {
				status=false;
			}
		} catch (Exception e) {
			logger.error("删除生产线 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			massage = "操作失败";
		}
		return UtilResult.getResultToJson(status, error_code, massage, "");
	}
	
	/**
	 * 装置
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_dic_installation_list1", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDicInstallationList1(@RequestBody JSONObject json, CurrentUser cUser) {

		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":5,\"total\":21},\"body\":[{\"installation_id\":\"1\",\"installation_name\":\"xxx装置\"}]}";
		return JSONObject.fromObject(str);
	}

	@RequestMapping(value = "/save_or_update_installation1", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveOrUpdateInstallation1(@RequestBody JSONObject json, CurrentUser cUser) {

		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":5,\"total\":21}}";
		return JSONObject.fromObject(str);
	}

	@RequestMapping(value = "/delete_installation1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteInstallation1(Integer installation_id, CurrentUser cUser) {

		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":5,\"total\":21}}";
		return JSONObject.fromObject(str);
	}
}
