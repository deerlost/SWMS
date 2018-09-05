package com.inossem.wms.portable.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockTransportHeadCw;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IProductionTransportService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class ProductionTransportControllerCW {

	private static final Logger logger = LoggerFactory.getLogger(ProductionTransportControllerCW.class);
	@Autowired
	private ICommonService commonService;

	@Autowired
	private IProductionTransportService productionTransportService;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private ITaskService taskServiceImpl;

	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/biz/product/transport/base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForUser(CurrentUser cUser) {
		String userId = cUser.getUserId();
		JSONArray ftyListOut = new JSONArray();
		JSONArray ftyListIn = new JSONArray();
		List<Map<String, Object>> classTypeList = new ArrayList<Map<String, Object>>();
		List<Map<String, String>> shiipingTypeList = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> driverList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> VehicleList = new ArrayList<Map<String, Object>>();
		Integer classTypeId = 0;
		try {
			ftyListIn = commonService.listFtyLocationForUser(userId, "2");
			ftyListOut = commonService.listFtyLocationForUser(userId, "1");
			classTypeList = commonService.getClassTypeList();
			shiipingTypeList = commonService.getShippingTypeList();
			driverList = commonService.selectDriverByProductlineId(cUser.getUserId());
			VehicleList = commonService.selectVehicleByProductId(cUser.getUserId());
			classTypeId = commonService.selectNowClassType();
			if (classTypeId == null) {
				classTypeId = 0;
			}
			
			
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
		}
		JSONObject body = new JSONObject();
		body.put("ftyListOut", ftyListOut);
		body.put("ftyListIn", ftyListIn);
		body.put("classTypeList", classTypeList);
		body.put("classTypeId", classTypeId);
		body.put("shipping_type_list", shiipingTypeList);
		body.put("driverList", driverList);
		body.put("VehicleList", VehicleList);
		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/biz/product/transport/material_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialList(@RequestBody JSONObject json, CurrentUser cUser) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		List<Map<String, Object>> materialList1 = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("keyword1", UtilJSON.getStringOrEmptyFromJSON("material_condition", json));
			map.put("productBatchLike", UtilJSON.getStringOrEmptyFromJSON("production_batch", json));
			map.put("locationId", UtilJSON.getIntFromJSON("location_id", json));
			Integer locationId = UtilObject.getIntegerOrNull(map.get("locationId"));
			Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
			DicStockLocationVo locationObj = locationMap.get(locationId);

			Integer ftyId = locationObj.getFtyId();
			map.put("ftyId", ftyId);
			map.put("stockTypeId", EnumStockType.STOCK_TYPE_ERP.getValue());
			// materialList = commonService.getMatListByBatch(map);
			// if (materialList != null && materialList.size() > 0) {
			// for (Map<String, Object> innerMap : materialList) {
			// innerMap.put("production_batch", innerMap.get("product_batch"));
			// }
			// }
			map.put("isDefault", 1);
			map.put("status",EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			materialList1 = commonService.getMatListByPosition(map);

			if (materialList1 != null && materialList1.size() > 0) {
				for (Map<String, Object> innerMap : materialList1) {
					innerMap.put("production_batch", innerMap.get("product_batch"));
					innerMap.put("id", innerMap.get("unique_id"));
					innerMap.put("mat_store_type", innerMap.get("sn_used"));
				}
			} else {
				materialList1 = new ArrayList<Map<String, Object>>();
			}
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, materialList1);
		return obj;

	}

	// 提交生产转运单
	@RequestMapping(value = "/biz/product/transport/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject submit(@RequestBody JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		BizStockTransportHeadCw transportHead = new BizStockTransportHeadCw();
		try {

			transportHead = UtilJSONConvert.fromJsonToHump(json, BizStockTransportHeadCw.class);
			transportHead.setReceiptType(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue());
			transportHead.setCreateUser(user.getUserId());

			transportHead.setRequestSource(EnumRequestSource.WEB.getValue());
			productionTransportService.addProductionTransport(transportHead);

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增其他入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}
		body.put("stockTransportId", UtilObject.getIntOrZero(transportHead.getStockTransportId()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;

	}

	// 校验packageCode
	@RequestMapping(value = {
			"/biz/product/transport/check_code_type" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject checkCodeType(@RequestBody JSONObject json, CurrentUser cUser) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			body = taskServiceImpl.checkCodeTypeByProductionTransportNew(json);
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
			logger.error("", e);
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;

	}

	// 上架作业-1.1.8 获取新托盘
	@RequestMapping(value = {
			"/biz/product/transport/get_new_pallet" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getNewPallet(Integer pallet_type_id, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			DicWarehousePallet pallet = commonService.newPallet(cUser.getUserId());
			if (pallet != null && pallet.getPalletId() != null) {
				body.put("pallet_id", pallet.getPalletId());
				body.put("pallet_code", pallet.getPalletCode());
				body.put("max_payload", pallet.getMaxWeight());
			} else {
				throw new WMSException("新增托盘出错");
			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;

	}

	/**
	 * 生产转运列表
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/biz/product/transport/head_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getHeadList(CurrentUser cuser, @RequestBody JSONObject json) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		try {

			

			String condition = json.getString("condition");
			
			Map<String, Object> map = new HashMap<String, Object>();

		//	map.put("statusList", statusList);
			map.put("condition", UtilString.getStringOrEmptyForObject(condition).trim());
		//	map.put("paging", true);
		//	map.put("pageIndex", pageIndex);
		//	map.put("pageSize", pageSize);
		//	map.put("totalCount", totalCount);
			map.put("receiptType", EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue());
			map.put("currentUser", cuser.getUserId());
		//	map.put("sortAscend", sortAscend);
		//	map.put("sortColumn", sortColumn);
			List<BizStockTransportHeadCw> shList = productionTransportService.getHeadList(map);
			if (shList != null && shList.size() > 0) {
				total = shList.get(0).getTotalCount();
				for (BizStockTransportHeadCw sh : shList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("stock_transport_id", UtilString.getStringOrEmptyForObject(sh.getStockTransportId()));
					itemMap.put("stock_transport_code",
							UtilString.getStringOrEmptyForObject(sh.getStockTransportCode()));
					itemMap.put("receipt_type", UtilString.getStringOrEmptyForObject(sh.getReceiptType()));
					itemMap.put("receipt_type_name",
							UtilString.getStringOrEmptyForObject(EnumReceiptType.getNameByValue(sh.getReceiptType())));
					itemMap.put("location_name", UtilString.getStringOrEmptyForObject(sh.getLocationName()));
					itemMap.put("fty_name", UtilString.getStringOrEmptyForObject(sh.getFtyName()));
					itemMap.put("create_time", UtilString.getLongStringForDate(sh.getCreateTime()));
					itemMap.put("user_name", UtilString.getStringOrEmptyForObject(sh.getUserName()));
					itemMap.put("vehicle", UtilString.getStringOrEmptyForObject(sh.getVehicle()));
					itemMap.put("status", UtilString.getStringOrEmptyForObject(sh.getStatus()));
					itemMap.put("status_name",
							UtilString.getStringOrEmptyForObject(EnumReceiptStatus.getNameByValue(sh.getStatus())));
					returnList.add(itemMap);
				}
			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}

	// 转运单详情

	@RequestMapping(value = "/biz/product/transport/details/{stock_transport_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDetails(@PathVariable("stock_transport_id") int stockTransportId,
			CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean returnStatus = false;
		JSONObject body = new JSONObject();
		JSONArray ItemAry = new JSONArray();
		List<BizStockTransportHeadCw> list = new ArrayList<BizStockTransportHeadCw>();
		try {
			if (stockTransportId > 0) {

				Map<String, Object> map = new HashMap<>();
				map.put("stockTransportId", stockTransportId);
				// 获取作业单抬头
				list = productionTransportService.getHeadList(map);
				if (list != null && list.size() > 0) {
					BizStockTransportHeadCw head = list.get(0);
					body.put("receipt_type", head.getReceiptType());
					body.put("receipt_type_name", EnumReceiptType.getNameByValue(head.getReceiptType()));
					body.put("create_time", UtilString.getLongStringForDate(head.getCreateTime()));
					body.put("class_type_id", head.getClassTypeId());
					body.put("class_type_name", head.getClassTypeName());

					body.put("status_name", EnumReceiptStatus.getNameByValue(head.getStatus()));
					body.put("status", head.getStatus());
					body.put("stock_transport_id", head.getStockTransportId());
					body.put("stock_transport_code", head.getStockTransportCode());

					body.put("user_name", head.getUserName());
					body.put("remark", head.getRemark());
					body.put("fty_name", head.getFtyName());
					body.put("fty_code", head.getFtyCode());
					body.put("fty_id", head.getFtyId());
					body.put("location_id", head.getLocationId());
					body.put("location_code", head.getLocationCode());
					body.put("location_name", head.getLocationName());
					body.put("driver", head.getDriver());
					body.put("vehicle", head.getVehicle());

				}
				List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
				getList = productionTransportService.getItemListByHeadId(stockTransportId);

				if (getList != null && getList.size() > 0) {
					for (Map<String, Object> itemVo : getList) {
						JSONObject item = new JSONObject();
						item.put("item_id", itemVo.get("item_id"));// 作业单
						item.put("stock_transport_id", itemVo.get("stock_transport_id"));// 作业单行号
						item.put("stock_transport_rid", itemVo.get("stock_transport_rid"));// 作业单行号
						item.put("mat_id", itemVo.get("mat_id"));// 物料编码
						item.put("mat_code", itemVo.get("mat_code"));// 物料编码
						item.put("mat_name", itemVo.get("mat_name"));// 物料描述
						item.put("batch", itemVo.get("batch"));// 物料描述
						item.put("unit_name", itemVo.get("unit_name"));// 单位
						item.put("fty_id", itemVo.get("fty_input"));
						item.put("fty_code", itemVo.get("fty_code"));
						item.put("fty_name", itemVo.get("fty_name"));// 库存地点
						item.put("location_id", itemVo.get("location_input"));
						item.put("location_code", itemVo.get("location_code"));
						item.put("location_name", itemVo.get("location_name"));// 库存地点
						item.put("transport_qty", itemVo.get("transport_qty"));// 本次上架数量
						item.put("stock_qty", itemVo.get("stock_qty"));// 本次上架数量
						item.put("production_batch", itemVo.get("product_batch"));// 生产批次
						item.put("erp_batch", itemVo.get("erp_batch"));// 生产批次
						item.put("package_type_id", itemVo.get("package_type_id"));// 包装类型
						item.put("package_type_code", itemVo.get("package_type_code"));
						item.put("package_type_name", itemVo.get("package_type_name"));
						item.put("package_standard", itemVo.get("package_standard"));
						item.put("package_standard_ch", itemVo.get("package_standard_ch"));
						item.put("work_model", itemVo.get("work_model"));
						item.put("remark", itemVo.get("remark"));
						item.put("transport_pack_num", itemVo.get("transport_pack_num"));

						item.put("pack_num", itemVo.get("pack_num"));
						item.put("mat_store_type", itemVo.get("sn_used"));
						Map<String, Object> positionFindMap = new HashMap<String, Object>();
						positionFindMap.put("stockTransportRid", item.get("stock_transport_rid"));
						positionFindMap.put("stockTransportId", item.get("stock_transport_id"));

						List<Map<String, Object>> positionList = productionTransportService
								.getPositionItemListByParams(positionFindMap);
						if (positionList != null && positionList.size() > 0) {
							BigDecimal havePayload = new BigDecimal(0);
							int packageNum = 0;
							for (Map<String, Object> p : positionList) {
								Integer palletId = UtilObject.getIntegerOrNull(p.get("pallet_id"));
								if (palletId != null && palletId > 0) {
									// 有托盘

									havePayload = havePayload.add(UtilObject.getBigDecimalOrZero(p.get("qty")));
									packageNum++;
									p.put("max_payload", p.get("max_weight"));
									p.put("have_payload", havePayload);
									p.put("package_num", packageNum);
								}

							}
						} else {
							positionList = new ArrayList<Map<String, Object>>();
						}
						item.put("position_list", positionList);

						ItemAry.add(item);
					}
					body.put("item_list", ItemAry);
//					List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(
//							body.getInt("stock_transport_id"), UtilObject.getByteOrNull(body.get("receipt_type")));
//					body.put("fileList", fileList);
				}
			}
			returnStatus = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("", e);
		}
		JSONObject json = UtilResult.getResultToJson(returnStatus, error_code, body);
		UtilJSONConvert.setNullToEmpty(json);
		return json;
	}

	// 转运单详情

	@RequestMapping(value = "/biz/product/transport/delete/{stock_transport_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject delete(@PathVariable("stock_transport_id") int stockTransportId,
			CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean returnStatus = true;
		JSONObject body = new JSONObject();
		try {
			productionTransportService.deleteById(stockTransportId,
					EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue());

		} catch (WMSException e) {
			error_code = e.getErrorCode();
			error_string = e.getMessage();
			returnStatus = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增其他入库", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			returnStatus = false;
		}
		JSONObject json = UtilResult.getResultToJson(returnStatus, error_code, error_string, body);
		UtilJSONConvert.setNullToEmpty(json);
		return json;
	}

}
