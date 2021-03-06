package com.inossem.wms.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.DicMaterialPackageTypeVo;
import com.inossem.wms.service.dic.IPackageTypeService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/mat_package")
public class MatPackageTypeController {
	private static final Logger logger = LoggerFactory.getLogger(MatPackageTypeController.class);
	@Autowired
	private IPackageTypeService packageTypeService;
	/**
	 * 获得包装物类型对应物料列表
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_mat_package_type_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatPackageTypeList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;
		int pageIndex = 0;
		int pageSize = 0;
		List<DicMaterialPackageTypeVo> list = new ArrayList<DicMaterialPackageTypeVo>();

		Map<String, Object> param = this.getParamMapToPageing(json);
		param.put("condition", UtilObject.getStringOrEmpty(json.getString("condition"))); // 关键字
		try {
			list = packageTypeService.getMatPackageTypeList(param);
			pageSize = json.getInt("page_size");
			pageIndex=json.getInt("page_index");
			if (list != null && list.size() > 0) {
				Long totalCountLong = (long) list.get(0).getTotalCount();
				total = totalCountLong.intValue();
			
			}
		} catch (Exception e) {
			logger.error("包装物类型对应物料查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code,pageIndex, pageSize,  total, list);
	}

	/**
	 * 保存包装物对应物料
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/save_package_type_material", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject savePackageTypeMaterial(@RequestBody JSONObject json, CurrentUser cUser) {
		String massage = "";
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> map = packageTypeService.getMatUnitByMatCode((String) json.getString("mat_code"));
		
		
		if (map == null) {
			massage = "您输入的物料编码不合法，请重新输入";
			status = false;
		} else {
			try {			
				
				status = packageTypeService.savePackageTypeMaterial(json, map);				

			} catch (DuplicateKeyException de) {
				logger.error("保存包装物对应物料 --", de);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
				massage = "物料和包装物关系已经存在！";
			} catch (Exception e) {
				logger.error("保存包装物对应物料 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		}
		return UtilResult.getResultToJson(status, error_code, massage,"");
	}

	/**
	 * 批量冻结物料对应包装物类型
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/update_mat_package_type_freeze", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateMatPackageTypeFreeze(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String massage = "";
		try {
			packageTypeService.updateFreezeById(json);
		} catch (Exception e) {
			logger.error("保存包装物对应物料 --", e);
			status = false;
			massage = "执行失败";
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

	
	
	@RequestMapping(value = "/search_name_or_code", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject searchNameOrCode(String search, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
        String msg="";
        Map<String,Object> map =new HashMap<String,Object>();
		try {
			map= packageTypeService.searchMatCodeOrName(search);
			
			
		} catch (Exception e) {
			logger.error("查询物料的code或name--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg="查询物料的编码或描述失败！";
		}

		return UtilResult.getResultToJson(status, error_code, msg,map);
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
