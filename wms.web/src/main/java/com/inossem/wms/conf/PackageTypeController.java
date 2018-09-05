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
import com.inossem.wms.model.dic.DicPackClassification;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicSupplier;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.DicMaterialPackageTypeVo;
import com.inossem.wms.model.vo.DicPackageTypeViewVo;
import com.inossem.wms.model.vo.DicSupplierPackageTypeVo;
import com.inossem.wms.service.dic.IPackageTypeService;
import com.inossem.wms.service.dic.IUnitConvService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/package")
public class PackageTypeController {
	private static final Logger logger = LoggerFactory.getLogger(PackageTypeController.class);
	@Autowired
	private IPackageTypeService packageTypeService;
	@Autowired
	private IUnitConvService unitConvService;

	/**
	 * 获得包装物列表页
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_package_type_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getPackageTypeList(@RequestBody JSONObject json, CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<DicPackageTypeViewVo> list = new ArrayList<DicPackageTypeViewVo>();

		Map<String, Object> param = this.getParamMapToPageing(json);
		param.put("condition", UtilObject.getStringOrEmpty(json.getString("condition"))); // 关键字
		try {
			list = packageTypeService.getPackageTypeList(param);
			pageSize = json.getInt("page_size");
			pageIndex=json.getInt("page_index");
			if (list != null && list.size() > 0) {
				Long totalCountLong = (long) list.get(0).getTotalCount();
				total = totalCountLong.intValue();				
			}
		} catch (Exception e) {
			logger.error("包装物管理查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, pageIndex,pageSize, total, list);
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
	 * 修改或删除
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/update_or_insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updateOrInsert(@RequestBody JSONObject json, CurrentUser cUser) {
		String massage = "";
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<DicPackageType> list = new ArrayList<DicPackageType>();
		DicUnit matDicUnit = unitConvService.getUnitByCode(json.getString("mat_unit_code"));
		DicUnit dicUnit = unitConvService.getUnitByCode(json.getString("unit_code"));
		list = packageTypeService.getPackageTypeBySnKey(json.getString("sn_serial_key"));
	    Integer pakageTypeId = UtilObject.getIntegerOrNull(json.get("package_type_id"));
	    Integer resultCount=  packageTypeService.getCountByCode(json.getString("package_type_code"));
		if (dicUnit == null||matDicUnit==null) {
			massage = "您输入的单位编码不合法！  请重新输入";
			status = false;
		} else if(list!=null &&list.size()>0&& pakageTypeId==null) {
			massage = "您输入的sn序列号不合法！  请重新输入";
			status = false;
		} else if(resultCount>0&&pakageTypeId==null) {
			massage = "您输入的包装物编码已经存在！  请重新输入";
			status = false;		
		}
		else {
			try {
				status = packageTypeService.saveOrUpdate(json, matDicUnit,dicUnit);
			} catch (Exception e) {
				logger.error("包装物修改或新增 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
				massage = "新增或更新失败";
			}
		}
		return UtilResult.getResultToJson(status, error_code, massage,"");
	}

	/**
	 * 得到包装物类型列表
	 * 
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_package_classify", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getPackageClassify(CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		List<DicPackClassification> list = new ArrayList<DicPackClassification>();
		try {
			list = packageTypeService.selectAll();

		} catch (Exception e) {
			logger.error("得到所有的包装物分类 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, list);
	}
	
	
//	/**
//	 * 
//	 * 
//	 * @param cUser
//	 * @return
//	 */
//	@RequestMapping(value = "/sn_change", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//	public @ResponseBody JSONObject snChange(String sn_serial_key) {
//
//		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//		boolean status = true;
//        String msg="";
//		List<DicPackageType> list = new ArrayList<DicPackageType>();
//		try {
//			list = packageTypeService.getPackageTypeBySnKey(sn_serial_key);
//			if(list!=null && list.size()>0) {
//				status = false;
//				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
//				msg="SN序列号关键字已经存在！";
//			}
//
//		} catch (Exception e) {
//			logger.error("得到所有的包装物分类 --", e);
//			status = false;
//			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
//		}
//		return UtilResult.getResultToJson(status, error_code, msg,"");
//	}
//	

	/**
	 * 修改冻结状态
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/update_package_type_freeze", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updatePackageTypeFreeze(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String ids = (String) json.get("package_type_id");
        String msg="";
		try {
			packageTypeService.updateFreezeByPackIds(ids);
		} catch (Exception e) {
			logger.error("保存包装物对应物料 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg="修改失败！";
		}
		return UtilResult.getResultToJson(status, error_code,msg, "");
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
