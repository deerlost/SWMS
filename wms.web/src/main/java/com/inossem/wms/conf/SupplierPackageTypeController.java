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
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicSupplier;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.DicSupplierPackageTypeVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.dic.IPackageTypeService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/supplier_package")
public class SupplierPackageTypeController {
	
	private static final Logger logger = LoggerFactory.getLogger(SupplierPackageTypeController.class);
	
	@Autowired
	private IPackageTypeService packageTypeService;
	
	@Autowired
	private ICommonService commonService;

	/**
	 * 获得包装物类型对应供应商列表
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_supplier_package_type_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getSupplierPackageTypeList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;
		int pageIndex = 0;
		int pageSize = 0;
		List<DicSupplierPackageTypeVo> list = new ArrayList<DicSupplierPackageTypeVo>();

		Map<String, Object> param = this.getParamMapToPageing(json);
		param.put("condition", UtilObject.getStringOrEmpty(json.getString("condition"))); // 关键字
		try {
			list = packageTypeService.selectSupplierPackageTypeOnpaging(param);
			pageSize = json.getInt("page_size");
			pageIndex=json.getInt("page_index");
			if (list != null && list.size() > 0) {
				Long totalCountLong = (long) list.get(0).getTotalCount();
				total = totalCountLong.intValue();			
			}
		} catch (Exception e) {
			logger.error("包装物类型对应供应商列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, list);
	}

	/**
	 * 新增或修改包装物类型对应供应商列表
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/update_or_insert_supplier", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateOrInsertSupplier(@RequestBody JSONObject json, CurrentUser cUser) {
		String massage = "";
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		try {
			status = packageTypeService.updateOrInsertSupplierPackageType(json);
		} catch (Exception e) {
			logger.error("新增或修改包装物类型对应供应商 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, massage,"");
	}

	/**
	 * 批量冻结供应商和包装物关系
	 * 
	 * @return
	 */
	@RequestMapping(value = "/update_freeze_by_sup_type_ids", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateFreezeBySupTypeIds(@RequestBody JSONObject json) {
		String massage = "";
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String ids = (String) json.get("dic_supplier_package_type_id");
		try {
			packageTypeService.updateFreezeBySupTypeids(ids);

		} catch (Exception e) {
			logger.error("保存包装物对应物料 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, massage,"");

	}
	

	@RequestMapping(value = "/get_package_type_all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getPackageTypeAll(CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
        String msg="";
		List<DicPackageType> list = new ArrayList<DicPackageType>();

		try {
			list = packageTypeService.getPackageTypeAll();
			
		} catch (Exception e) {
			logger.error("包装物管理查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg="包装物查询失败！";
		}

		return UtilResult.getResultToJson(status, error_code, msg,list);
	}
	
	/**
	 * 获取供应商列表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/get_supplier_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getSupplierList(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
        String msg="";
		List<DicSupplier> list = new ArrayList<DicSupplier>();
		try {
			list = commonService.synSupplier(UtilObject.getStringOrEmpty(json.get("keyword")));
		} catch (Exception e) {
			logger.error("供应商列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg="供应商列表";
		}
		return UtilResult.getResultToJson(status, error_code, msg,list);
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

}
