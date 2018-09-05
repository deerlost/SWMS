package com.inossem.wms.web.biz;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.BizPkgOperationHeadVo;
import com.inossem.wms.model.vo.BizPkgOperationItemVo;
import com.inossem.wms.service.biz.IPkgOperationService;
import com.inossem.wms.util.UtilNumber;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 包装管理
 * 
 * @author sw
 *
 */
@Controller
@RequestMapping("/biz/pkg")
public class PkgOperationController {

	private static final Logger logger = LoggerFactory.getLogger(PkgOperationController.class);

	@Autowired
	private IPkgOperationService pkgService;

	/**
	 * 获取生产订单信息
	 * @param code
	 * @param user
     * @return
     */
	@RequestMapping(value = { "/get_refer_receipt" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getReferReceipt(@RequestParam("code")String code,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "";
		try {
			while (code.length() < 12) { // 不够十二位数字，自动补0
				code = "0" + code;
			}
			result = pkgService.selectOrderViewBySap(code,user.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}catch (WMSException e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = e.getMessage();
		}catch (Exception e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "获取生产订单失败";
		} 
		return UtilResult.getResultToJson(status, error_code,msg, result);
	}

	/**
	 * 获取列表页
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = { "/get_pkg_list" }, method = { RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getPkgList(@RequestBody JSONObject json) {
		boolean status = true;
		List<BizPkgOperationHeadVo> list = new ArrayList<BizPkgOperationHeadVo>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
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
			param.put("keyword", UtilObject.getStringOrEmpty(json.getString("keyword")));
			param.put("createTime", UtilObject.getStringOrEmpty(json.get("create_time")));
			JSONArray array = json.getJSONArray("status");
			param.put("statusList",array);
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			param.put("paging", Boolean.valueOf(true));
			param.put("pageIndex", Integer.valueOf(pageIndex));
			param.put("pageSize", Integer.valueOf(pageSize));
			param.put("totalCount", Integer.valueOf(totalCount));
			param.put("sortColumn", sortColumn);
			param.put("sortAscend", sortAscend);
			list = this.pkgService.selectPkgList(param);
			if (list != null && list.size() > 0) {
				total = list.get(0).getTotalCount();
				for (BizPkgOperationHeadVo headVo : list) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("pkg_operation_id", headVo.getPkgOperationId());// 包装单id
					map.put("pkg_operation_code", headVo.getPkgOperationCode());// 包装单code
					map.put("refer_receipt_code", headVo.getReferReceiptCode());// 订单code
					map.put("refer_receipt_type", headVo.getDocumentType());// 订单类型
					map.put("package_team", headVo.getPackageTeam());// 包装班组
					map.put("product_line_id", headVo.getProductLine());// 产品线id
					map.put("product_line_name", headVo.getProductLineName());// 产品线name
					map.put("class_type_id", headVo.getClassTypeId());// 班次id
					map.put("class_type_name", headVo.getClassTypeName());// 班次name
					map.put("installation_id", headVo.getInstallation());// 装置id
					map.put("installation_name", headVo.getInstallationName());// 装置name
					map.put("status", headVo.getStatus());// 状态
					map.put("status_name", headVo.getStatusName());// 状态中文
					map.put("create_time", headVo.getPkgCreateTime());// 创建时间
					map.put("modify_time", headVo.getPkgModifyTime());// 修改时间
					map.put("remark", headVo.getRemark());// 备注
					map.put("product_batch", headVo.getProductBatch());// 产品批次
					map.put("create_name", headVo.getCreateName());// 创建者id
					map.put("fty_name", headVo.getFtyName());// 工厂名字
					map.put("fty_id", headVo.getFtyId());// 工厂id
					result.add(map);
				}
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装管理列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, result);
	}

	/**
	 * 获取 班次 产品线 装置 集合
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/get_pkg_class_line_list" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getPkgDicList(CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = this.pkgService.selectPkgClassLineInstallationList(user.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("查询包装页班次 产品线 集合", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}

	/**
	 * 保存包装单
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/save_pkg_operation" }, method = { RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody Object savePkgOperation(@RequestBody JSONObject jsonData, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		JSONObject result = new JSONObject();
		try {
			result = this.pkgService.savePkgOperationData(jsonData, user);
			msg = "提交成功";
		} catch (WMSException e) {
			logger.error("保存包装单", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(" 保存包装单 --", e);
			msg = "程序异常";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToMap(status, error_code, msg, result);
	}

	/**
	 * 获取物料集合
	 * @param keyword
	 * @return
     */
	@RequestMapping(value = { "/get_pkg_mat_list" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getPkgMatList(String keyword) {
		boolean status = true;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			param.put("keyword", keyword);
			param.put("ftyType", 1);
			result = this.pkgService.selectMaterialList(param);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装单物料集合", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}

	/**
	 * 获取托盘或者包裹集合
	 * @param keyword
	 * @param package_type_id
     * @return
     */
	@RequestMapping(value = { "/get_pkg_pallet_list" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getPkgtypePalletList(@RequestParam("keyword")String keyword,@RequestParam("package_type_id")Integer package_type_id) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "";
		try {	
		    String type = "none";
			if(keyword.length() == 12) {
				String body = keyword.substring(1,keyword.length());
				if(!UtilNumber.isNumber(body)){
					msg = "托盘编号存在不合法字符";
				}else {
					type = "pallet";
				}
			}else if(keyword.length() == 18) {
				String body = keyword.substring(2,keyword.length());
				if(!UtilNumber.isNumber(body)){
					msg = "包装物SN存在不合法字符";
				}else {
					type = "pkgtype";
				}
			}else {
				msg = "输入托盘/包装物SN格式错误";
			}
			if(!type.equals("none")) {
				result = this.pkgService.selectPkgOrPalletList(package_type_id,keyword,type);
			}else {
				status = false;
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue() ;
		}catch (WMSException e) {
			logger.error("包装单", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error("包装单", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue() ;
			msg = "程序异常";
		}
		return UtilResult.getResultToJson(status, error_code,msg,result);
	}

	/**
	 * 获取包装单详情
	 * 
	 * @param pkgOperationId
	 * @return
	 */
	@RequestMapping(value = { "/get_pkg_detail/{pkgOperationId}" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject detail(@PathVariable("pkgOperationId") Integer pkgOperationId) {
		logger.info("获取包装单详情");
		logger.info("传进参数-----" + pkgOperationId);
		BizPkgOperationHeadVo headVo = null;
		Map<String, Object> result = new HashMap<String, Object>();
		boolean status = false;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			headVo = this.pkgService.selectPkgDetailById(pkgOperationId);
			if(headVo!=null) {
				result.put("pkg_operation_id",headVo.getPkgOperationId());// 包装单id
				result.put("pkg_operation_code", headVo.getPkgOperationCode());// 包装单code
				result.put("refer_receipt_code", headVo.getReferReceiptCode());// 订单code
				result.put("refer_receipt_type", headVo.getDocumentType());// 订单类型
				result.put("package_team", headVo.getPackageTeam());// 包装班组
				result.put("product_line_id", headVo.getProductLine());// 产品线id
				result.put("product_line_name", headVo.getProductLineName());// 产品线name
				result.put("class_type_id", headVo.getClassTypeId());// 班次id
				result.put("class_type_name", headVo.getClassTypeName());// 班次name
				result.put("installation_id", headVo.getInstallation());// 装置id
				result.put("installation_name", headVo.getInstallationName());// 装置name
				result.put("status", headVo.getStatus());// 状态
				result.put("status_name", headVo.getStatusName());// 状态中文
				result.put("create_time", headVo.getPkgCreateTime());// 创建时间
				result.put("modify_time", headVo.getPkgModifyTime());// 修改时间
				result.put("remark", headVo.getRemark());// 备注
				result.put("product_batch", headVo.getProductBatch());// 产品批次
				result.put("create_name", headVo.getCreateName());// 创建者id
				result.put("fty_id", headVo.getFtyId());// 工厂id
				result.put("fty_name", headVo.getFtyName());// 工厂名字
				result.put("is_pallet", headVo.getIsPallet());// 是否启用托盘
				result.put("class_type_list", headVo.getClassTypeList());//班次
				result.put("product_line_list", headVo.getProductLineList());//产品线
				List<BizPkgOperationItemVo> itemList = headVo.getItemList();
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
				for(BizPkgOperationItemVo item : itemList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("mat_id", item.getMatId());
					map.put("mat_code", item.getMatCode());
					map.put("mat_name", item.getMatName());
					map.put("unit_id", item.getUnitId());
					map.put("unit_code", item.getUnitCode());
					map.put("unit_name", item.getUnitName());
					map.put("name_en", item.getNameEn());
					map.put("name_zh", item.getNameZh());
					map.put("item_id", item.getItemId());
					map.put("pkg_operation_id", item.getPkgOperationId());
					map.put("pkg_operation_rid", item.getPkgOperationRid());
					map.put("fty_id", item.getFtyId());
					map.put("order_qty", item.getOrderQty().compareTo(BigDecimal.ZERO)==0?"--":item.getOrderQty());
					map.put("pkg_qty", item.getPkgQty());
					map.put("package_type_id", item.getPackageTypeId());
					map.put("package_type_code", item.getPackageTypeCode());
					map.put("package_type_name", item.getPackageTypeName());
					map.put("package_standard", item.getPackageStandard());
					map.put("package_standard_ch",item.getPackageStandardCh());
					map.put("package_type_list",item.getPackageTypeList());
					map.put("position_list",item.getPositionList());
					resultList.add(map);
				}
				result.put("item_list",resultList);
			}
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue() ;
		} catch (Exception e) {
			logger.error("获取包装单详情", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue() ;
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}

	/**
	 * 创建托盘
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/create_pkg_warehouse_pallet" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject createPkgWarehousePallet(CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue() ;
		boolean status = true;
		String msg = "";
		DicWarehousePallet pallet = new DicWarehousePallet();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			pallet = this.pkgService.createPkgWarehousePallet(user.getUserId());
			if (pallet != null) {
				result.put("pallet_id", pallet.getPalletId());
				result.put("pallet_code", pallet.getPalletCode());
				result.put("max_weight", pallet.getMaxWeight());
			}
			msg = "新建托盘成功";
		} catch (WMSException wmse) {
			error_code = wmse.getErrorCode();
			msg = wmse.getMessage();
			status = false;
			logger.error("", wmse);
		} catch (Exception e) {
			logger.error(" 新建托盘 --", e);
			msg = e.getMessage();
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue() ;
		}
		return UtilResult.getResultToJson(status, error_code, msg, result);
	}

	
	/**
	 * 删除包装单
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/delete_pkg_operation" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject deletePkgOperation(@RequestParam(value="pkg_operation_id",required=true)Integer pkg_operation_id) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue() ;
		boolean status = true;
		String msg = "";
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			msg = this.pkgService.deletePkgOperation(pkg_operation_id);
		} catch (Exception e) {
			logger.error(" 删除包装单 --", e);
			msg = e.getMessage();
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue() ;
		}
		return UtilResult.getResultToJson(status, error_code, msg, result);
	} 
}
