package com.inossem.wms.portable.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.SerialPackageVo;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.ISerialPackageService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IDicWarehousePalletService;
import com.inossem.wms.service.dic.IMatMajorDataService;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.util.UtilTwoDimensionCode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/stockquery")
public class StockQuryController {

	private static final Logger logger = LoggerFactory.getLogger(StockQuryController.class);
	
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IStockQueryService stockQueryService;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private ISerialPackageService packageService;
	
	@Autowired
	private IDicWarehousePalletService palletService;
	
	@Autowired
	private IMatMajorDataService matMajorDataService;
	
	/**
	 * 仓库号 存储区
	 * 
	 * @param str
	 * @return
	 */
	@RequestMapping(value = "warehouseList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getKCDDAll(CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		
		ArrayList<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
		try {
			List<Map<String, Object>> lgnumList = userService.getCurrentUserWarehouse(cUser.getUserId());
			HashMap<String, Object> lgnumMap = new HashMap<String, Object>();
			HashMap<String, Object> lgnumNameMap = new HashMap<String, Object>();
			if (lgnumList != null) {
				for (Map<String, Object> hashMap : lgnumList) {
					String lgnum = UtilObject.getStringOrEmpty( hashMap.get("wh_code"));// 仓库号
					String lgtyp = UtilObject.getStringOrEmpty( hashMap.get("area_code"));// 存储区
					String ltypt = UtilObject.getStringOrEmpty( hashMap.get("position_code"));// 存储区描述
					String lnumt = UtilObject.getStringOrEmpty(  hashMap.get("wh_name"));// 仓库描述
					if (lgnumMap.containsKey(lgnum)) {

						ArrayList<HashMap<String, Object>> lgtypList = (ArrayList<HashMap<String, Object>>) lgnumMap
								.get(lgnum);
						HashMap<String, Object> lgtypMap = new HashMap<String, Object>();

						lgtypMap.put("areaNumber", lgtyp);
						lgtypMap.put("areaDescription", ltypt);
						lgtypList.add(lgtypMap);

						lgnumMap.put(lgnum, lgtypList);
					} else {
						ArrayList<HashMap<String, Object>> lgtypList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> lgtypMap = new HashMap<String, Object>();

						lgtypMap.put("areaNumber", lgtyp);
						lgtypMap.put("areaDescription", ltypt);
						lgtypList.add(lgtypMap);
						lgnumNameMap.put(lgnum, lnumt);
						lgnumMap.put(lgnum, lgtypList);
					}
				}
			}

			Set<Entry<String, Object>> entries = lgnumMap.entrySet();

			for (Entry<String, Object> entry : entries) {
				HashMap<String, Object> werksObj = new HashMap<String, Object>();

				ArrayList<HashMap<String, Object>> inventoryAddressList = (ArrayList<HashMap<String, Object>>) entry
						.getValue();
				werksObj.put("warehouseNumber", entry.getKey());
				werksObj.put("warehouseDescription", lgnumNameMap.get(entry.getKey()));
				werksObj.put("areaList", inventoryAddressList);
				returnList.add(werksObj);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("库存地点含名称", e);
		}
		
		JSONObject data = new JSONObject();
		data.put("warehouseList", returnList);
		
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, data);
	}
	
	
	/**
	 * 根据物料编码查询批次库存-仓位库存
	 * 
	 * @param str
	 * @return
	 */
	@RequestMapping(value = "materielBatchList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialBatchList(HttpServletRequest request, CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		
		String condition = request.getParameter("condition");

		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap = UtilTwoDimensionCode.getDate(condition);

		String matnr = "";
		if (conditionMap.containsKey("matnr")) {
			matnr = UtilObject.getStringOrEmpty(conditionMap.get("matnr"));
		} else {
			matnr = condition;
		}

		JSONObject data = new JSONObject();

		ArrayList<HashMap<String, Object>> batchList = new ArrayList<HashMap<String, Object>>();

		List<Map<String, Object>> mchbList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lquaList = new ArrayList<Map<String, Object>>();

		
		
		
		List<RelUserStockLocationVo> locationList = userService.listLocForUser(cUser.getUserId());
		List<Integer> locationIds = new ArrayList<Integer>();

		for (RelUserStockLocationVo innerObj : locationList) {
			locationIds.add(innerObj.getLocationId());
		}
		HashMap<String, Object> mchbMap = new HashMap<String, Object>();
		mchbMap.put("matCodeFind", matnr);
		mchbMap.put("locationIds", locationIds);
		
		HashMap<String, Object> lquaMap = new HashMap<String, Object>();
		lquaMap.put("matCodeFind", matnr);
		lquaMap.put("locationIds", locationIds);
		try {
			mchbList = stockQueryService.listStockBatchOnPaging(mchbMap);
			lquaList = stockQueryService.listStockPositionOnPaging(lquaMap);
			if (mchbList != null && mchbList.size() > 0) {
				data.put("materielCode", UtilObject.getStringOrEmpty(mchbList.get(0).get("mat_code")));
				data.put("materielDescription", UtilObject.getStringOrEmpty(mchbList.get(0).get("mat_name")));
				for (Map<String, Object> mchb : mchbList) {
					HashMap<String, Object> batch = new HashMap<String, Object>();
					batch.put("batchNumber", UtilObject.getBigDecimalOrZero(mchb.get("batch")));
					// ==批次库存
					batch.put("inventoryNumber", UtilObject.getBigDecimalOrZero(mchb.get("qty")) + "");
					batch.put("unit", UtilObject.getStringOrEmpty(mchb.get("name_zh")));
					batch.put("contract", UtilObject.getStringOrEmpty(mchb.get("contract_code")));
					batch.put("supplier", UtilObject.getStringOrEmpty(mchb.get("supplier_name")));
					batch.put("demandDepartment", UtilObject.getStringOrEmpty(mchb.get("demand_dept")));
					batch.put("purchaseOrder", UtilObject.getStringOrEmpty(mchb.get("purchase_order_code")));
					batch.put("projectNumber", UtilObject.getStringOrEmpty(mchb.get("purchase_order_rid")));
					batch.put("inStorageDate", UtilString.getShortStringForDate((Date)mchb.get("input_date")));
					batch.put("specialInventoryFlag", UtilObject.getStringOrEmpty(mchb.get("spec_stock")));
					batch.put("specialInventoryCode", UtilObject.getStringOrEmpty(mchb.get("spec_stock_code")));
					batch.put("specialInventoryDescription", UtilObject.getStringOrEmpty(mchb.get("spec_stock_name")));
					ArrayList<HashMap<String, Object>> positionList = new ArrayList<HashMap<String, Object>>();
					if (lquaList != null) {
						for (Map<String, Object> lqua : lquaList) {
							if (UtilObject.getLongOrNull(mchb.get("batch")).equals(UtilObject.getLongOrNull(lqua.get("batch")))  && UtilObject.getStringOrEmpty(mchb.get("fty_code")).equals(UtilObject.getStringOrEmpty(lqua.get("fty_code")))) {
								HashMap<String, Object> position = new HashMap<String, Object>();
								position.put("warehouseNumber", UtilObject.getStringOrEmpty(lqua.get("")));
								position.put("areaNumber", UtilObject.getStringOrEmpty(lqua.get("area_code")));
								position.put("positionNumber", UtilObject.getStringOrEmpty(lqua.get("position_code")));
								position.put("positionInventoryNumber", UtilObject.getBigDecimalOrZero(lqua.get("qty")));
								positionList.add(position);
							}

						}
					}

					batch.put("positionList", positionList);
					batchList.add(batch);
				}
				data.put("batchList", batchList);
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				errorStatus = true;
				errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
			}

		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, data);
	}
	
	
	/**
	 * 查询仓位库存
	 * 
	 * @param str
	 * @return
	 */
	@RequestMapping(value = "materielList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterielList(@RequestBody JSONObject json, CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		
		String lgnum = "";// 仓库号
		String lgtyp = "";// 存储区
		String lgpla = "";// 仓位号
		String matnr = "";// 物料编码
		String matxt = "";// 物料描述
		if (json.containsKey("warehouseNumber")) {
			lgnum = json.getString("warehouseNumber");// 仓库号
		}
		if (json.containsKey("areaNumber")) {
			lgtyp = json.getString("areaNumber");// 存储区
		}
		if (json.containsKey("positionNumber")) {
			lgpla = json.getString("positionNumber");// 仓位号
		}
		if (json.containsKey("materielCode")) {
			matnr = json.getString("materielCode");// 物料编码
		}
		if (json.containsKey("materielDescription")) {
			matxt = json.getString("materielDescription");// 物料描述
		}

		ArrayList<HashMap<String, Object>> materielList = new ArrayList<HashMap<String, Object>>();
		List<Map<String, Object>> lquaList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("whCode", lgnum);
		map.put("areaCode", lgtyp);
		map.put("positionCode", lgpla);
		map.put("matCodeFind", matnr);
		map.put("matName", matxt);
		try {
			lquaList = stockQueryService.listStockPositionOnPaging(map);
			for (Map<String, Object> lqua : lquaList) {
				HashMap<String, Object> batch = new HashMap<String, Object>();
				batch.put("id", UtilObject.getStringOrEmpty(lqua.get("id")));
				batch.put("materielCode", UtilObject.getStringOrEmpty(lqua.get("mat_code")));
				batch.put("materielDescription", UtilObject.getStringOrEmpty(lqua.get("mat_name")));
				batch.put("positionNumber", UtilObject.getStringOrEmpty(lqua.get("position_code")));
				batch.put("batchNumber", UtilObject.getStringOrEmpty(lqua.get("batch")));
				batch.put("specialInventoryFlag", UtilObject.getStringOrEmpty(lqua.get("spec_stock")));
				batch.put("inventoryNumber", UtilObject.getBigDecimalOrZero(lqua.get("qty")));
				batch.put("unit", UtilObject.getStringOrEmpty(lqua.get("name_zh")));

				materielList.add(batch);
			}
			
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		} catch (Exception e) {
			logger.error("", e);
		}
		JSONObject data = new JSONObject();
		data.put("materielList", materielList);
		
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, data);
	}
	
	/**
	 * 查询仓位库存
	 * 
	 * @param str
	 * @return
	 */
	@RequestMapping(value = "materielListCw", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterielListCw(HttpServletRequest request, CurrentUser cUser) {
		
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		//获取搜索请求数据
		String searchData = "";
		if (request.getParameterMap().containsKey("data")) {
			searchData = request.getParameter("data");// 仓库号
		}else {
			return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, "");
		}
		
		String lgnum = "";// 仓库号
		String lgtyp = "";// 存储区
		String lgpla = "";// 仓位编码
		String matnr = "";// 物料编码
		String matxt = "";// 物料描述
		String pallet = "";//托盘
		String packagenm ="";//包-sn
		
		//解析搜索数据
		//扫描 扫描格式为 存储区-仓库号-仓位编码
		if(searchData.indexOf("-")>1 && 3 < searchData.split("-").length) {
			lgtyp = searchData.substring(0,searchData.indexOf("-"));
			lgnum = searchData.substring(searchData.indexOf("-")+1,searchData.indexOf("-",searchData.indexOf("-")+1));
			lgpla = searchData.substring(searchData.indexOf("-",searchData.indexOf("-")+1)+1,searchData.length());
		}else {
			//区分 包 托盘  物料编码   物料描述描述//TODO
			
			//serial_package  包表 对应sn
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("packageCode", UtilObject.getStringOrNull(searchData));
			List<SerialPackageVo> seiallist = this.packageService.selectSerialPackageList(param);
			
			//dic_warehouse_pallet  托盘 
			param = new HashMap<String, Object>();
			param.put("keyword", UtilObject.getStringOrNull(searchData));
			List<DicWarehousePallet> palletlist  = palletService.selectDicWarehousePalletList(param);
			
			//dic_material 物料编码
			Map<String ,Object> matMajorSataMap = matMajorDataService.getMatUnitByMatCode(UtilObject.getStringOrNull(searchData));
			
			if(CollectionUtils.isNotEmpty(seiallist)) {
				packagenm = UtilObject.getStringOrNull(searchData);
			}else if(CollectionUtils.isNotEmpty(palletlist)) {
				pallet = UtilObject.getStringOrNull(searchData);
			}else	if(MapUtils.isNotEmpty(matMajorSataMap)) {
				matnr = UtilObject.getStringOrNull(searchData);
			}else {
				matxt ="!@#$%^";
			}
			//物料描述
//			matxt = UtilObject.getStringOrNull(searchData);
		}

		ArrayList<HashMap<String, Object>> materielList = new ArrayList<HashMap<String, Object>>();
//		List<Map<String, Object>> lquaList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("whCode", lgnum);
		map.put("areaCode", lgtyp);
		map.put("positionCode", lgpla);
		map.put("matCode", matnr);
		map.put("matName", matxt);
		map.put("palleCodet", pallet);
		map.put("packageCode", packagenm);
		HashMap<String, Object> batch = new HashMap<String, Object>();
		try {
				Map<String, Object> lqua =stockQueryService.listStockPositionOnPagingCw(map);
				batch.put("materielCode", UtilObject.getStringOrEmpty(lqua.get("mat_code")));//物料编码
				batch.put("materielDescription", UtilObject.getStringOrEmpty(lqua.get("mat_name")));//物料描述
				batch.put("ftyName", UtilObject.getStringOrEmpty(lqua.get("fty_name")));//工厂名称
				batch.put("locationName", UtilObject.getStringOrEmpty(lqua.get("location_code"))+UtilObject.getStringOrEmpty(lqua.get("location_name")));//库存地点描述
				batch.put("areaCode", UtilObject.getStringOrEmpty(lqua.get("area_code")));//存储区编码
				batch.put("areaName", UtilObject.getStringOrEmpty(lqua.get("area_name")));//存储区描述
				batch.put("positionNumber", UtilObject.getStringOrEmpty(lqua.get("position_code")));//仓位
				batch.put("palletCode", UtilObject.getStringOrEmpty(lqua.get("pallet_code")));//托盘标签
				batch.put("inventoryNumber", UtilObject.getBigDecimalOrZero(lqua.get("stock_qty")));//仓位库存数量
				batch.put("unitName", UtilObject.getStringOrEmpty(lqua.get("unit_name")));//计量单位
				batch.put("statusName", UtilObject.getStringOrEmpty(lqua.get("status_name")));//库存状态
				batch.put("stockTypeName", UtilObject.getStringOrEmpty(lqua.get("stock_type_name")));//库存类型
				//包装物信息
				batch.put("packageTypeCode", UtilObject.getStringOrEmpty(lqua.get("package_type_code")));//包装类型
				batch.put("packageStandardCh", UtilObject.getStringOrEmpty(lqua.get("package_standard_ch")));//包装规格
				batch.put("erpBatch", UtilObject.getStringOrEmpty(lqua.get("erp_batch")));//ERP批次
				batch.put("productBatch", UtilObject.getStringOrEmpty(lqua.get("product_batch")));//生产批次
				batch.put("batch", UtilObject.getStringOrEmpty(lqua.get("batch")));//批次号
				batch.put("stockQty", UtilObject.getStringOrEmpty(lqua.get("stock_qty")));//库存数量
				batch.put("shelfLife", UtilObject.getStringOrEmpty(lqua.get("shelf_life")));//保质期
				batch.put("inputDate", UtilObject.getStringOrEmpty(lqua.get("input_date")));//入库日期
			
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		} catch (Exception e) {
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, batch);
	}
	
	/**
	 * 根据物料编码查询批次库存-仓位库存
	 * 
	 * @param str
	 * @return
	 */
	@RequestMapping(value = "materielInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterielInfo(HttpServletRequest request, CurrentUser cUser) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		
		String lqnum = request.getParameter("id");

		Integer lqnum_int = Integer.parseInt(lqnum);

		JSONObject data = new JSONObject();

		ArrayList<HashMap<String, Object>> batchList = new ArrayList<HashMap<String, Object>>();

		List<Map<String, Object>> lquaList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", lqnum_int);

		try {
			lquaList = stockQueryService.listStockPositionOnPaging(map);
			
			if (lquaList != null && lquaList.size() > 0) {
				BatchMaterial key = new BatchMaterial();
				key.setBatch(UtilObject.getLongOrNull(lquaList.get(0).get("batch")));
				key.setMatId(UtilObject.getIntegerOrNull(lquaList.get(0).get("mat_id")));
				key.setFtyId(UtilObject.getIntegerOrNull(lquaList.get(0).get("fty_id")));
				BatchMaterial mcha = stockQueryService.getBatchMaterial(key);
				if (mcha == null) {
					mcha = new BatchMaterial();
				}
				data.put("materielCode", UtilObject.getStringOrEmpty(lquaList.get(0).get("mat_code")));
				data.put("materielDescription", UtilObject.getStringOrEmpty(lquaList.get(0).get("mat_name")));
				for (Map<String, Object> lqua : lquaList) {
					HashMap<String, Object> batch = new HashMap<String, Object>();
					batch.put("batchNumber", UtilObject.getLongOrNull(lquaList.get(0).get("batch")));
					batch.put("inventoryNumber", UtilObject.getBigDecimalOrZero(lquaList.get(0).get("qty")));
					batch.put("unit",  UtilObject.getStringOrEmpty(lqua.get("name_zh")));
					batch.put("contract", UtilObject.getStringOrEmpty(mcha.getContractCode()));
					batch.put("supplier", UtilObject.getStringOrEmpty(mcha.getSupplierName()));
					batch.put("demandDepartment", UtilObject.getStringOrEmpty(mcha.getDemandDept()));
					batch.put("purchaseOrder", UtilObject.getStringOrEmpty(mcha.getPurchaseOrderCode()));
					batch.put("projectNumber", UtilObject.getStringOrEmpty(mcha.getPurchaseOrderRid()));
					batch.put("inStorageDate", UtilString.getShortStringForDate(mcha.getCreateTime()));
					batch.put("specialInventoryFlag", UtilObject.getStringOrEmpty(mcha.getSpecStock()));
					batch.put("specialInventoryCode", UtilObject.getStringOrEmpty(mcha.getSpecStockCode()));
					batch.put("specialInventoryDescription", UtilObject.getStringOrEmpty(mcha.getSpecStockName()));
					ArrayList<HashMap<String, Object>> positionList = new ArrayList<HashMap<String, Object>>();

					HashMap<String, Object> position = new HashMap<String, Object>();
					position.put("warehouseNumber", UtilObject.getStringOrEmpty(lqua.get("wh_code")));
					position.put("areaNumber", UtilObject.getStringOrEmpty(lqua.get("area_code")));
					position.put("positionNumber", UtilObject.getStringOrEmpty(lqua.get("position_code")));
					position.put("positionInventoryNumber", UtilObject.getBigDecimalOrZero(lqua.get("qty")));
					positionList.add(position);

					batch.put("positionList", positionList);
					batchList.add(batch);
				}
				data.put("batchList", batchList);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, data);
	}
	
	
	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/get_fty_location_for_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForUser(CurrentUser cUser,String fty_type) {
		String userId = cUser.getUserId();
		JSONArray ftyAry = new JSONArray();
		try {
			ftyAry = commonService.listFtyLocationForUser(userId,fty_type);
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry);
	}
	
	/**
	 * 批次库存查询
	 * 
	 * @param 刘宇
	 *            2018.02.07
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_stoch_batch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStochBatch(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;

		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<Map<String, Object>> stockBatchMaps = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();

			if (json.has("mat_code")) {
				map.put("matCode", json.getString("mat_code"));// 物料编码
				List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(json.getString("mat_code"));// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
				map.put("utilMatCodes", utilMatCodes);// 物料编码集合
			}
			if (json.has("mat_name")) {
				map.put("matName", json.getString("mat_name"));// 物料描述
			}
			if (json.has("mat_group_code")) {
				map.put("matGroupCode", json.getString("mat_group_code"));// 物料组编码始
			}

			if (json.has("fty_id")) {
				map.put("ftyId", json.getString("fty_id"));// 工厂id
			}
			if (json.has("location_id")) {
				map.put("locationId", json.getString("location_id"));// 库存地点编码
			}
			
			if (json.has("status")) {
				map.put("status", json.getString("status"));
			}
			if (json.has("product_batch")) {
				map.put("productBatch", json.getString("product_batch"));
			}
			
			SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");

			if (!json.getString("input_date_begin").equals("") && !json.getString("input_date_end").equals("")) {
				map.put("inputDateBegin", clsFormat.parse(json.getString("input_date_begin")));// 入库日期起
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(json.getString("input_date_end")), 1));// 入库日期止
			} else if (!json.getString("input_date_begin").equals("")
					&& json.getString("input_date_end").equals("")) {
				map.put("inputDateBegin", clsFormat.parse(json.getString("input_date_begin")));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(json.getString("input_date_begin")), 1));
			} else if (json.getString("input_date_begin").equals("")
					&& !json.getString("input_date_end").equals("")) {
				map.put("inputDateBegin", clsFormat.parse(json.getString("input_date_end")));
				map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(json.getString("input_date_end")), 1));
			}

			if (json.has("stock_type_id")) {
				if(json.getString("stock_type_id").equals("5")){
					map.put("isUrgent", json.getString("stock_type_id"));
				}else{
					map.put("stockTypeId", json.getString("stock_type_id"));
				}
				
			}
			
			if (json.has("page_index")) {
				pageIndex = json.getInt("page_index");
			}
			if (json.has("page_size")) {
				pageSize = json.getInt("page_size");
			}
			if (json.has("total")) {
				total = json.getInt("total");
			}

			map.put("pageSize", pageSize);
			map.put("pageIndex", pageIndex);
			map.put("paging", false);

			stockBatchMaps = stockQueryService.listStockBatchOnPaging(map);
	
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode,errorString,stockBatchMaps);
	}
	
	
}
