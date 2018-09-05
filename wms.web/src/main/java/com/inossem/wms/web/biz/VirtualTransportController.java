package com.inossem.wms.web.biz;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.BizStockInputTransportItem;
import com.inossem.wms.model.enums.*;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputTransportService;
import com.inossem.wms.service.biz.VirtualTransportService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package com.inossem.wms.web.biz
 * @ClassName：VirtualTransportController
 * @Description :虚拟转储入库单
 * @anthor：王洋
 * @date：2018年5月16日 下午5:41:23 @版本：V1.0
 */
@Controller
public class VirtualTransportController {

	private static final Logger logger = LoggerFactory.getLogger(VirtualTransportController.class);

	@Resource
	ICommonService commonService;

	@Resource
	VirtualTransportService vtransportService;

	@Resource
	VirtualTransportService inputService;
	@Autowired
	private SequenceDAO sequenceDAO;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Autowired
	private IInputTransportService inputTransportService;

	/**
	 * @Title: getParamMapToPageing </br>
	 * @Description: 分页</br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws</br>
	 */
	private Map<String, Object> getParamMapToPageing(JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();

		int pageIndex = json.getInt("page_index");
		int pageSize = json.getInt("page_size");
		int totalCount = EnumPage.TOTAL_COUNT.getValue();
		JSONArray statusList = null;
		boolean sortAscend = true;
		String sortColumn = "";
		if (json.containsKey("total")) {
			totalCount = json.getInt("total");
		}
		if (json.containsKey("sort_ascend")) {
			sortAscend = json.getBoolean("sort_ascend");
		}
		if (json.containsKey("sort_column")) {
			sortColumn = json.getString("sort_column");
		}
		if (json.containsKey("status_list")) {
			statusList = json.getJSONArray("status_list");
		}

		param.put("paging", true);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		param.put("sortAscend", sortAscend);
		param.put("sortColumn", sortColumn);
		param.put("statusList", statusList);

		return param;
	}

	/**
	 * @Description: TODO(获得班次，通过user获得接收工厂及库存信息)</br>
	 * @Title: getBaseInfo </br>
	 * @param @param
	 *            json
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/input/transport/baseinfo_byuser" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getBaseInfo(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int fty_type = 2;
		String userId = cUser.getUserId();
		JSONArray ftyAry = new JSONArray();
		JSONObject body = new JSONObject();
		JSONArray synAry = new JSONArray();
		try {
			ftyAry = commonService.listFtyLocationForUser(userId, null);
			synAry = vtransportService.getSynType();
			body.put("fty_list", ftyAry);
			body.put("class_type_list", commonService.getClassTypeList());
			body.put("class_type_id", inputTransportService.selectNowClassType());
			body.put("syn_list", synAry);
			body.put("syn_type_id",EnumSynType.SAP_SYN.getValue());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取发出工厂及库存信息--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}

	/**
	 * @Description: TODO(获取虚拟生产单)</br>
	 * @Title: selectStockInputByID </br>
	 * @param @param
	 *            stock_input_code
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = {
			"/biz/input/transport/stockinput_bycode/{stock_input_code}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectStockInputByID(@PathVariable("stock_input_code") String stock_input_code) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = null;

		JSONObject body = new JSONObject();

		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stockInputCode", stock_input_code);
			param.put("receiptType", EnumReceiptType.STOCK_INPUT_VIRTUAL.getValue());
			BizStockInputHead inputHead = vtransportService.selectInputHeadByVirtualInputCode(param);
			body.put("stock_input_code", UtilObject.getStringOrNull(inputHead.getStockInputCode()));
			body.put("stock_input_id", UtilObject.getIntegerOrNull(inputHead.getStockInputId()));
			body.put("create_user", UtilObject.getStringOrNull(inputHead.getCreateUserName()));
			body.put("create_time", new SimpleDateFormat("yyyy-MM-dd").format(inputHead.getCreateTime()));
			body.put("syn_type_id", EnumSynType.SAP_SYN.getValue());
			JSONArray myJsonArray = new JSONArray();				
			JSONObject jsonObject = new JSONObject();										
			jsonObject.put("syn_type_name", EnumSynType.MES_SAP_SYN.getName());
			jsonObject.put("syn_type_id", EnumSynType.MES_SAP_SYN.getValue());
			myJsonArray.add(jsonObject);			
			JSONObject jsonObject1 = new JSONObject();										
			jsonObject1.put("syn_type_name", EnumSynType.SAP_SYN.getName());
			jsonObject1.put("syn_type_id", EnumSynType.SAP_SYN.getValue());
			myJsonArray.add(jsonObject1);			
			JSONObject jsonObject2 = new JSONObject();										
			jsonObject2.put("syn_type_name", EnumSynType.NO_SYN.getName());
			jsonObject2.put("syn_type_id", EnumSynType.NO_SYN.getValue());
			myJsonArray.add(jsonObject2);			
			body.put("syn_type", myJsonArray);

			if (null == inputHead && "".equals(inputHead)) {

				logger.error("虚拟生产入库单为空---------------");
				error_code = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getValue();
				status = false;
			} else if (inputHead.getStatus() == (byte) 5) {
				error_code = EnumErrorCode.ERROR_CODE_VIRTUAL_INPUT_TRANED.getValue();
				status = false;
			} else {
				List<Map<String, Object>> item_list = new ArrayList<Map<String, Object>>();

				List<Map<String, Object>> inputItemList = vtransportService
						.selectInputItemByStockInputId(UtilObject.getIntegerOrNull(inputHead.getStockInputId()));
				for (Map<String, Object> map : inputItemList) {
					Map<String, Object> itemmap = new HashMap<String, Object>();
					int num = 0;
					body.put("mat_id", UtilObject.getIntegerOrNull(map.get("mat_id")));
					body.put("mat_code", UtilObject.getStringOrNull(map.get("mat_code")));
					body.put("mat_name", UtilObject.getStringOrNull(map.get("mat_name")));

					itemmap.put("package_type_code", UtilObject.getStringOrNull(map.get("package_type_code")));
					itemmap.put("package_standard_ch", UtilObject.getStringOrNull(map.get("package_standard_ch")));
					itemmap.put("package_standard", UtilObject.getIntegerOrNull(map.get("package_standard")));
					itemmap.put("qty", UtilObject.getBigDecimalOrZero(map.get("qty")));
					if ((UtilObject.getIntegerOrNull(map.get("qty"))
							% UtilObject.getIntegerOrNull(map.get("package_standard"))) > 0) {
						num = UtilObject.getIntegerOrNull(map.get("qty"))
								/ UtilObject.getIntegerOrNull(map.get("package_standard")) + 1;
					} else {
						num = UtilObject.getIntegerOrNull(map.get("qty"))
								/ UtilObject.getIntegerOrNull(map.get("package_standard"));
					}

					itemmap.put("num", num);
					itemmap.put("production_batch", UtilObject.getStringOrNull(map.get("production_batch")));
					itemmap.put("erp_batch", UtilObject.getStringOrNull(map.get("erp_batch")));
					itemmap.put("quality_batch", UtilObject.getStringOrNull(map.get("quality_batch")));
					itemmap.put("fty_output_name", UtilObject.getStringOrNull(map.get("fty_output_name")));
					itemmap.put("fty_output_code", UtilObject.getStringOrNull(map.get("fty_output_code")));
					itemmap.put("location_output_code", UtilObject.getStringOrNull(map.get("location_output_code")));
					itemmap.put("location_output_name", UtilObject.getStringOrNull(map.get("location_output_name")));

					item_list.add(itemmap);
					body.put("item_list", item_list);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

			logger.error("获取虚拟生产入库单-------------异常-----------------", e);
			status = true;
			msg = "虚拟生产入库单不存在";
			body.put("msg", msg);
		}

		return UtilResult.getResultToJson(status, error_code, body);

	}

	/**
	 * @Description: TODO(创建虚拟转储入库单)</br>
	 * @Title: createVirtualDump </br>
	 * @param @param
	 *            json
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/input/transport/create_virtualdump" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject createVirtualDump(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		String stock_input_code = UtilObject.getStringOrNull(json.get("stock_input_code"));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockInputCode", stock_input_code);
		param.put("receiptType", EnumReceiptType.STOCK_INPUT_VIRTUAL.getValue());
		// param.put("stockInputId",
		// UtilObject.getStringOrNull(json.get("stock_transport_id")));
		BizStockInputHead inputHead = vtransportService.selectInputHeadByVirtualInputCode(param);

		List<Map<String, Object>> inputItemList = vtransportService
				.selectInputItemByStockInputId(inputHead.getStockInputId());

		BizStockInputTransportHead transHead = new BizStockInputTransportHead();
		try {
			if (UtilObject.getStringOrNull(json.get("stock_transport_id")) == null) {
				transHead.setStockTransportCode(
						UtilObject.getStringOrEmpty(String.valueOf(sequenceDAO.selectNextVal("stock_input"))));
				transHead.setReceiptType(EnumReceiptType.STOCK_INPUT_TRANSPORT_VIRTUAL.getValue());
				transHead.setStockInputId(UtilObject.getIntegerOrNull(inputHead.getStockInputId()));
				transHead.setFtyId(UtilObject.getIntegerOrNull(json.get("fty_id")));
				transHead.setLocationId(UtilObject.getIntegerOrNull(json.get("location_id")));
				transHead.setClassTypeId(UtilObject.getIntegerOrNull(json.get("class_type_id")));
				transHead.setSynType(UtilObject.getIntegerOrNull(json.get("syn_type")));
				transHead.setRemark(UtilObject.getStringOrEmpty(json.get("remark")));
				transHead.setCreateUser(UtilObject.getStringOrNull(cUser.getUserId()));
				transHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				transHead.setIsDelete(EnumBoolean.FALSE.getValue());
				vtransportService.insertSelectiveStockInputTransHead(transHead);
			} else {
				transHead = vtransportService.getTransHead(json.get("stock_transport_id").toString());
			}
			int num = 0;
			for (Map<String, Object> map : inputItemList) {
				JSONArray item_list = json.getJSONArray("item_list");
				JSONObject remark = item_list.getJSONObject(num);
				num += 1;
				BizStockInputTransportItem transItem = new BizStockInputTransportItem();

				if (UtilObject.getStringOrNull(json.get("stock_transport_id")) == null) {
					transItem.setStockTransportId(transHead.getStockTransportId());
					transItem.setReferReceiptCode(UtilObject.getStringOrNull(map.get("stock_input_code")));
					transItem.setReferReceiptRid(UtilObject.getIntegerOrNull(map.get("item_id")));
					transItem.setReferReceiptId(UtilObject.getIntegerOrNull(map.get("stock_input_id")));
					transItem.setStockTransportRid(UtilObject.getIntegerOrNull(num));
					transItem.setMatId(UtilObject.getIntegerOrNull(map.get("mat_id")));
					transItem.setFtyOutputId(UtilObject.getIntegerOrNull(map.get("fty_id")));
					transItem.setLocationOutputId(UtilObject.getIntegerOrNull(map.get("location_id")));
					transItem.setFtyId(UtilObject.getIntegerOrNull(map.get("fty_id")));
					transItem.setFtyInput(transHead.getFtyId());
					transItem.setLocationId(UtilObject.getIntegerOrNull(map.get("location_id")));
					transItem.setLocationInput(transHead.getLocationId());
					transItem.setRemark(remark.getString("remark"));
					transItem.setBatch(UtilObject.getLongOrNull(map.get("batch")));
					transItem.setErpBatch(UtilObject.getStringOrNull(map.get("erp_batch")));
					transItem.setProductionBatch(UtilObject.getStringOrNull(map.get("production_batch")));
					transItem.setTransportQty(UtilObject.getBigDecimalOrZero(map.get("qty")));
					transItem.setBusinessType((int) EnumReceiptType.STOCK_INPUT_TRANSPORT_VIRTUAL.getValue());
					transItem.setUnitId(UtilObject.getIntegerOrNull(map.get("unit_id")));
					transItem.setPackageTypeId(UtilObject.getIntegerOrNull(map.get("package_type_id")));
					transItem.setMatInput(UtilObject.getIntegerOrNull(map.get("mat_id")));
					vtransportService.insertSelectiveStockInputTransItem(transItem);

					// 批次信息
					BatchMaterial b = new BatchMaterial();
					b.setBatch(UtilObject.getLongOrNull(map.get("batch")));
					b.setMatId(UtilObject.getIntegerOrNull(map.get("mat_id")));
					b.setFtyId(UtilObject.getIntegerOrNull(map.get("fty_id")));

					BatchMaterial find = batchMaterialDao.selectByUniqueKey(b);
					if (find == null) {
						throw new WMSException("无生产批次信息");
					}

					BatchMaterial batchMaterial = new BatchMaterial();

					batchMaterial.setBatch(UtilObject.getLongOrNull(map.get("batch")));
					batchMaterial.setMatId(UtilObject.getIntegerOrNull(map.get("mat_id")));
					batchMaterial.setSupplierCode("");
					batchMaterial.setSupplierName("");
					batchMaterial.setFtyId(UtilObject.getIntOrZero(transHead.getFtyId()));
					batchMaterial.setSpecStock("");
					batchMaterial.setSpecStockCode("");
					batchMaterial.setSpecStockName("");
					batchMaterial.setCreateUser(cUser.getUserId());
					batchMaterial.setPackageTypeId(UtilObject.getIntOrZero(map.get("package_type_id")));
					batchMaterial.setErpBatch(UtilObject.getStringOrNull(map.get("erp_batch")));
					batchMaterial.setQualityBatch(UtilObject.getStringOrNull(map.get("production_batch")));
					batchMaterial.setProductionBatch(UtilObject.getStringOrNull(map.get("production_batch")));
					batchMaterial.setPurchaseOrderCode(find.getPurchaseOrderCode());
					batchMaterial.setProductLineId(find.getProductLineId());
					batchMaterial.setInstallationId(find.getInstallationId());
					batchMaterial.setClassTypeId(UtilObject.getIntegerOrNull(json.get("class_type_id")));
					batchMaterial.setPackageTypeId(UtilObject.getIntegerOrNull(find.getPackageTypeId()));
					batchMaterialDao.insertSelective(batchMaterial);
				}

				/*
				 * BizStockInputPackageItem pack = new
				 * BizStockInputPackageItem();
				 * pack.setStockInputItemId(UtilObject.getIntegerOrNull(
				 * transItem.getItemId()));
				 * pack.setPackageTypeId(UtilObject.getIntegerOrNull(map.get(
				 * "package_type_id")));
				 * pack.setPackageNum(UtilObject.getIntegerOrNull(map.get(
				 * "package_num")));
				 * pack.setQty(UtilObject.getBigDecimalOrZero(map.get("qty")));
				 * pack.setProductionBatch(UtilObject.getStringOrNull(map.get(
				 * "production_batch")));
				 * pack.setErpBatch(UtilObject.getStringOrNull(map.get(
				 * "erp_batch")));
				 * pack.setQualityBatch(UtilObject.getStringOrNull(map.get(
				 * "quality_batch")));
				 * pack.setBatch(UtilObject.getLongOrNull(map.get("batch")));
				 * vtransportService.insertSelectiveStockInputPackItem(pack);
				 */
			}
//			ErpReturn er = vtransportService.takeSap(transHead, json, inputItemList);
//			if ("S".equals(er.getType())) {
				vtransportService.modifyNum(json, inputItemList, cUser, transHead);
//				vtransportService.updateMatDocCode(er, transHead);
//				vtransportService.setHistory(transHead, cUser, json, inputItemList);
//			} else {
				// errorString=er.getMessage();
//			}

		} catch(WMSException e) {
			error_string = e.getMessage();
			logger.error("创建虚拟生产入库单-------------异常-----------------", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("创建虚拟生产入库单-------------异常-----------------", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("stock_transport_id", UtilObject.getIntOrZero(transHead.getStockTransportId()));
		return UtilResult.getResultToJson(status, error_code, error_string,body);
	}

	/**
	 * @Description: TODO(获取虚拟转储详情)</br>
	 * @Title: selectVirtualDumpByID </br>
	 * @param @param
	 *            stock_transport_id
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/input/transport/virtualdump_byid/{stock_transport_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectVirtualDumpByID(@PathVariable("stock_transport_id") int stock_transport_id) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		Map<String, Object> transHead = vtransportService
				.selectStockTransportHeadbyID(UtilObject.getIntegerOrNull(stock_transport_id));
		try {
			body.put("stock_transport_id", transHead.get("stock_transport_id"));
			body.put("stock_transport_code", transHead.get("stock_transport_code"));
			body.put("stock_input_code", transHead.get("stock_input_code"));
			body.put("create_user", transHead.get("user_name"));
			body.put("fty_input_code", transHead.get("fty_input_code"));
			body.put("fty_id", transHead.get("fty_id"));
			body.put("fty_input_name", transHead.get("fty_input_name"));
			body.put("class_name", transHead.get("class_name"));
			body.put("syn_type", transHead.get("syn_type"));
			body.put("syn_type_name", transHead.get("syn_type_name"));
			body.put("create_time", transHead.get("create_time"));
			body.put("status", transHead.get("status"));
			body.put("status_name", transHead.get("status_name"));
			body.put("location_input_code", transHead.get("location_input_code"));
			body.put("location_id", transHead.get("location_id"));
			body.put("location_input_name", transHead.get("location_input_name"));
			body.put("remark", transHead.get("remark"));

			List<Map<String, Object>> item_list = new ArrayList<Map<String, Object>>();

			List<Map<String, Object>> inputItemList = vtransportService
					.selectByStockTransportId(UtilObject.getIntegerOrNull(stock_transport_id));

			for (Map<String, Object> map : inputItemList) {
				Map<String, Object> itemmap = new HashMap<String, Object>();
				long num = 0;
				//BigDecimal num = new BigDecimal(0);
				body.put("mat_code", map.get("mat_code"));
				body.put("mat_name", map.get("mat_name"));
				itemmap.put("package_type_code", UtilObject.getStringOrNull(map.get("package_type_code")));
				itemmap.put("package_standard_ch", UtilObject.getStringOrNull(map.get("package_standard_ch")));
				itemmap.put("package_standard", UtilObject.getIntegerOrNull(map.get("package_standard")));
				itemmap.put("qty", UtilObject.getBigDecimalOrZero(map.get("transport_qty")));
				itemmap.put("production_batch", UtilObject.getStringOrNull(map.get("production_batch")));
				itemmap.put("erp_batch", UtilObject.getStringOrNull(map.get("erp_batch")));
				itemmap.put("quality_batch", UtilObject.getStringOrNull(map.get("production_batch")));
				itemmap.put("fty_output_name", UtilObject.getStringOrNull(map.get("fty_output_name")));
				itemmap.put("fty_output_code", UtilObject.getStringOrNull(map.get("fty_output_code")));
				itemmap.put("location_output_code", UtilObject.getStringOrNull(map.get("location_output_code")));
				itemmap.put("location_output_name", UtilObject.getStringOrNull(map.get("location_output_name")));
				/*if ((UtilObject.getIntegerOrNull(map.get("transport_qty"))
						% UtilObject.getIntegerOrNull(map.get("package_standard"))) > 0) {
					num = UtilObject.getIntegerOrNull(map.get("transport_qty"))
							/ UtilObject.getIntegerOrNull(map.get("package_standard")) + 1;
				} else {
					num = UtilObject.getIntegerOrNull(map.get("transport_qty"))
							/ UtilObject.getIntegerOrNull(map.get("package_standard"));
				}
				*/
				
				double standard = Double.parseDouble(String.valueOf(map.get("package_standard")));
				itemmap.put("num",UtilObject.getBigDecimalOrZero(map.get("transport_qty")).divide(new BigDecimal(standard),0,BigDecimal.ROUND_UP));
				itemmap.put("remark", map.get("remark"));

				item_list.add(itemmap);
				body.put("item_list", item_list);
			}
			body.put("mes_doc_code",
					inputItemList.get(0).get("mes_doc_code") == null ? "" : inputItemList.get(0).get("mes_doc_code"));
			body.put("mat_doc_code",
					inputItemList.get(0).get("mat_doc_code") == null ? "" : inputItemList.get(0).get("mat_doc_code"));
			body.put("mat_off_code", inputItemList.get(0).get("mat_off_code")==null?"":inputItemList.get(0).get("mat_off_code"));
			body.put("mes_off_code", inputItemList.get(0).get("mes_fail_code")==null?"":inputItemList.get(0).get("mes_fail_code"));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取虚拟转储单-------------异常-----------------", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}

	/**
	 * @Description: TODO(获取虚拟转储单列表)</br>
	 * @Title: selectVirtualDumplist </br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = {
			"/biz/input/transport/virtualdumplist_info" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectVirtualDumplist(CurrentUser user,@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> param = this.getParamMapToPageing(json);
		try {

			String key = json.getString("condition").trim(); // 编号

			if (null != key && !key.equals(null)) {
				param.put("keyword", UtilObject.getStringOrNull(key));
				param.put("userId", user.getUserId());
				param.put("receiptType", EnumReceiptType.STOCK_INPUT_TRANSPORT_VIRTUAL.getValue());
				list = vtransportService.selectVirtualDumplist(param);

				if (null != list && list.size() > 0) {
					Long totalCountLong = (Long) list.get(0).get("totalCount");
					total = totalCountLong.intValue();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception

			logger.error("转储列表查询 --------------", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, list);
	}

	@RequestMapping(value = {
			"/biz/input/transport/virtual/write_off" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject write_off(@RequestBody JSONObject json, CurrentUser cUser) {

		JSONObject obj = new JSONObject();
		Map<String, Object> body = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		try {
			BizStockInputTransportHead bizStockInputTransportHead = new BizStockInputTransportHead();
			bizStockInputTransportHead.setStockTransportId(Integer.valueOf(json.get("stock_task_id").toString()));
			bizStockInputTransportHead = inputTransportService.selectEntity(bizStockInputTransportHead);
			List<Map<String, Object>> list = inputTransportService
					.getViItemInfo(Integer.valueOf(json.get("stock_task_id").toString()));
//			ErpReturn er = vtransportService.takeSapWriteOff(bizStockInputTransportHead, list);
//			if ("S".equals(er.getType())) {
				vtransportService.updateStockToWriteOff(Integer.valueOf(json.get("stock_task_id").toString()),
						cUser,bizStockInputTransportHead, list);
				body.put("stock_input_code", bizStockInputTransportHead.getStockTransportCode());
				body.put("stock_input_id", bizStockInputTransportHead.getStockTransportId());
//			} else {
//				errorString = er.getMessage();
//				body.put("stock_input_code", bizStockInputTransportHead.getStockTransportCode());
//				body.put("stock_input_id", bizStockInputTransportHead.getStockTransportId());
//			}
			status = true;
		}catch (WMSException e) {
			e.printStackTrace();
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("虚拟转储入库冲销", e);
			errorString = e.getMessage();
		}catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("虚拟转储入库冲销", e);
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}
		obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}
	
	@RequestMapping("/biz/input/transport/virtual/delete/{stock_input_id}")
	@ResponseBody
	public JSONObject delete(@PathVariable("stock_input_id") String stock_input_id,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			vtransportService.delete(stock_input_id);	
//			inputTransportService.dealBeforeStatus(stock_input_id);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch(WMSException e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = e.getMessage();
		} catch (Exception e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}
		return UtilResult.getResultToJson(status, error_code,errorString, result);
	}

}
