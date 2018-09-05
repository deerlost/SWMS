package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.BizStockInputTransportItem;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputTransportService;
import com.inossem.wms.service.biz.IUrgentTransportService;
import com.inossem.wms.service.biz.VirtualTransportService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Package com.inossem.wms.web.biz
 * @ClassName：UrgentTransportController
 * @Description :紧急转储记账
 * @anthor：王洋
 * @date：2018年5月22日 下午5:37:00 @版本：V1.0
 */
@Controller
public class UrgentTransportController {

	private static final Logger logger = LoggerFactory.getLogger(UrgentTransportController.class);
	@Autowired
	private ICommonService commonService;

	@Resource
	private IUrgentTransportService utransportSerivce;

	@Resource
	private VirtualTransportService vtransportService;
	
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

	/**_
	 * @Title: getBaseInfo </br>
	 * @Description: TODO(获取发出工厂库存、接收工厂库存、班次、移动类型以及同步类型)</br>
	 * @param @param
	 *            cUser
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/biz/transport/baseinfo_byuser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getBaseInfo(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String userId = cUser.getUserId();
		JSONArray ftyListIn = new JSONArray();
		JSONArray ftyListOut = new JSONArray();
		JSONObject body = new JSONObject();
		JSONArray synAry = new JSONArray();
		try {
			ftyListIn = commonService.listFtyLocationForUser(userId, "2");
			ftyListOut = commonService.listFtyLocationForUser(userId, "1");
			synAry = utransportSerivce.getSynType();
			body.put("fty_in_list", ftyListIn);
			body.put("fty_out_list", ftyListOut);
			body.put("class_type_list", commonService.getClassTypeList());
			body.put("class_type_id", inputTransportService.selectNowClassType()==null?0:inputTransportService.selectNowClassType());
			body.put("move_list", commonService.getMoveTypeByReceiptType(EnumReceiptType.STOCK_INPUT_TRANSPORT_BOOK_URGENT.getValue()));
			body.put("syn_list", synAry);
			body.put("syn_type_id", EnumSynType.SAP_SYN.getValue());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取发出工厂及库存信息--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}

	/**
	 * @Title: selectUrgentDumplist </br>
	 * @Description: TODO( 紧急转储记账列表)</br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/biz/transport/urgentdumplist_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectUrgentDumplist(CurrentUser user,@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int total = 0;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list_item = new ArrayList<Map<String, Object>>();
		Map<String, Object> param = this.getParamMapToPageing(json);
		try {

			String key = json.getString("condition").trim();

			if (null != key && !key.equals(null)) {
				param.put("keyword", UtilObject.getStringOrNull(key));
				param.put("userId", user.getUserId());
				param.put("receiptType", EnumReceiptType.STOCK_INPUT_TRANSPORT_BOOK_URGENT.getValue());
				list = vtransportService.selectVirtualDumplist(param);
				
				for(int i = 0 ; i<list.size();i++){
					Map<String, Object> obj = list.get(i);
					
					List<Map<String, Object>> inputItemList = vtransportService
							.selectByStockTransportId(UtilObject.getIntegerOrNull(obj.get("stock_transport_id")));
					for (Map<String, Object> map : inputItemList) {
						obj.put("fty_input_id", map.get("fty_input_id"));
						obj.put("fty_input_code", map.get("fty_input_code"));
						obj.put("fty_input_name", map.get("fty_input_name"));
						obj.put("location_input_id", map.get("location_input_id"));
						obj.put("loc_input_code", map.get("location_input_code"));
						obj.put("loc_input_name", map.get("location_input_name"));
						obj.put("fty_output_id", map.get("fty_output_id"));
						obj.put("fty_output_code", map.get("fty_output_code"));
						obj.put("fty_output_name", map.get("fty_output_name"));
						obj.put("location_output_id", map.get("location_output_id"));
						obj.put("loc_output_code", map.get("location_output_code"));
						obj.put("loc_output_name", map.get("location_output_name"));
					}
					
					list_item.add(obj);
				}
				

				if (null != list && list.size() > 0) {
					Long totalCountLong = (Long) list.get(0).get("totalCount");
					total = totalCountLong.intValue();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception

			logger.error("紧急转储记账列表查询 --------------", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, list_item);
	}

	/**
	* @Description: TODO(创建紧急记账)</br>
	   @Title: createUrgentDump </br>
	* @param @param json
	* @param @param cUser
	* @param @return    设定文件</br>
	* @return JSONObject    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = {
			"/biz/transport/create_urgentdump" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject createUrgentDump(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg="";
		JSONArray inputItemList = json.getJSONArray("item_list");
		BizStockInputTransportHead transHead = new BizStockInputTransportHead();
		try {
			transHead.setStockTransportCode(
					UtilObject.getStringOrEmpty(String.valueOf(sequenceDAO.selectNextVal("stock_input"))));
			transHead.setReceiptType(EnumReceiptType.STOCK_INPUT_TRANSPORT_BOOK_URGENT.getValue());
			transHead.setFtyId(UtilObject.getIntegerOrNull(json.get("fty_output_id")));
			transHead.setLocationId(UtilObject.getIntegerOrNull(json.get("loc_output_id")));
			transHead.setClassTypeId(UtilObject.getIntegerOrNull(json.get("class_type_id")));
			transHead.setSynType(UtilObject.getIntegerOrNull(json.get("syn_type")));
			transHead.setMoveTypeId(UtilObject.getIntegerOrNull(json.get("move_type_id")));
			transHead.setRemark(UtilObject.getStringOrEmpty(json.get("remark")));
			transHead.setCreateUser(UtilObject.getStringOrNull(cUser.getUserId()));
			transHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			transHead.setIsDelete(EnumBoolean.FALSE.getValue());
			if(UtilObject.getStringOrNull(json.get("stock_transport_id"))==null) {
				vtransportService.insertSelectiveStockInputTransHead(transHead);
			}else {
				transHead=vtransportService.getTransHead(json.get("stock_transport_id").toString());
			}

			if (inputItemList.size() > 0) {

				for (int num = 0; num < inputItemList.size(); num++) {

					JSONObject item = inputItemList.getJSONObject(num);
					BizStockInputTransportItem transItem = new BizStockInputTransportItem();

					transItem.setStockTransportId(transHead.getStockTransportId());
					transItem.setStockTransportRid(UtilObject.getIntegerOrNull(num + 1));
					transItem.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
					transItem.setFtyOutputId(UtilObject.getIntegerOrNull(json.get("fty_output_id")));
					transItem.setLocationOutputId(UtilObject.getIntegerOrNull(json.get("loc_output_id")));
					transItem.setFtyId(UtilObject.getIntegerOrNull(json.get("fty_input_id")));
					transItem.setFtyInput(UtilObject.getIntegerOrNull(json.get("fty_input_id")));
					transItem.setLocationId(UtilObject.getIntegerOrNull(json.get("loc_input_id")));
					transItem.setLocationInput(UtilObject.getIntegerOrNull(json.get("loc_input_id")));
					transItem.setRemark(UtilObject.getStringOrEmpty(item.get("remark")));
					transItem.setBatch(UtilObject.getLongOrNull(item.get("batch")));
					transItem.setErpBatch(UtilObject.getStringOrNull(item.get("erp_batch")));
					transItem.setProductionBatch(UtilObject.getStringOrNull(item.get("product_batch")));
					transItem.setTransportQty(UtilObject.getBigDecimalOrZero(item.get("transport_qty")));
					transItem.setStockQty(UtilObject.getBigDecimalOrZero(item.get("stock_qty")));
					transItem.setWhId(UtilObject.getIntegerOrNull(item.get("wh_id")));
					transItem.setUnitId(UtilObject.getIntegerOrNull(item.get("unit_id")));
					transItem.setPackageTypeId(UtilObject.getIntegerOrNull(item.get("package_type_id")));
					transItem.setBusinessType((int) EnumReceiptType.STOCK_INPUT_TRANSPORT_BOOK_URGENT.getValue());
					if(UtilObject.getStringOrNull(json.get("stock_transport_id"))==null) {
						vtransportService.insertSelectiveStockInputTransItem(transItem);
					}
					
					
					
					// 批次信息
					BatchMaterial param = new BatchMaterial();
					param.setBatch(UtilObject.getLongOrNull(item.get("batch")));
					param.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
					param.setFtyId(UtilObject.getIntegerOrNull(json.get("fty_output_id")));
				
					BatchMaterial find = batchMaterialDao.selectByUniqueKey(param);
					if(find == null){
						throw new WMSException("无生产批次信息");
					}
					
					
					
					BatchMaterial batchMaterial = new BatchMaterial();
					batchMaterial.setBatch(UtilObject.getLongOrNull(item.get("batch")));
					batchMaterial.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
					batchMaterial.setSupplierCode("");
					batchMaterial.setSupplierName("");
					batchMaterial.setFtyId(UtilObject.getIntOrZero(json.get("fty_input_id")));
					batchMaterial.setSpecStock("");
					batchMaterial.setSpecStockCode("");
					batchMaterial.setSpecStockName("");
					batchMaterial.setCreateUser(cUser.getUserId());
					batchMaterial.setPackageTypeId(UtilObject.getIntOrZero(item.get("package_type_id")));
					batchMaterial.setErpBatch(UtilObject.getStringOrNull(item.get("erp_batch")));
					batchMaterial.setQualityBatch(UtilObject.getStringOrNull(item.get("erp_batch")));
					batchMaterial.setProductionBatch(UtilObject.getStringOrNull(item.get("product_batch")));					
					batchMaterial.setPurchaseOrderCode(find.getPurchaseOrderCode());
					batchMaterial.setProductLineId(find.getProductLineId());
					batchMaterial.setInstallationId(find.getInstallationId());
					batchMaterial.setClassTypeId(UtilObject.getIntegerOrNull(json.get("class_type_id")));
					batchMaterial.setPackageTypeId(UtilObject.getIntegerOrNull(item.get("package_type_id")));
					batchMaterialDao.insertSelective(batchMaterial);

				}

			}
//			ErpReturn er=utransportSerivce.takeSap(transHead,json,inputItemList);
//			if("S".equals(er.getType())) {
				utransportSerivce.modifyNum(json,inputItemList,cUser,transHead);	
//				utransportSerivce.updateMatDocCode(er,transHead);
//				utransportSerivce.setHistory(transHead,cUser,json,inputItemList);
//			}else {
//				errorString=er.getMessage();
//			}
		} catch (WMSException e) {
			msg = e.getMessage();
			error_code = e.getErrorCode();
			status = false;
			logger.error("", e);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("创建紧急转储记账单-------------异常-----------------", e);
			status = false;
			msg = "失败";
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("stock_transport_id", UtilObject.getIntOrZero(transHead.getStockTransportId()));
		return UtilResult.getResultToJson(status, error_code,msg, body);
	}
	
	
	@RequestMapping(value = {"/biz/transport/urgent/get_material_info" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMaterialInfo(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		/*
		 * int fty_id = json.getInt("fty_id"); int loc_id =
		 * json.getInt("loc_id");
		 */
		Map<String, Object> mat = new HashMap<String, Object>();
		mat.put("keyword1", json.get("mat_code"));
		mat.put("keyword2", json.get("batch"));
		mat.put("ftyId", json.get("fty_id"));
		mat.put("locationId", json.get("location_id"));
		mat.put("isDefault", 1);
		mat.put("stockTypeId", EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
		List<Map<String, Object>> item_list = null;
		try {
			item_list = commonService.getMatListByPosition(mat);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("获取物料信息--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		// Map<String, Object> mat_batch_map = null;
		return UtilResult.getResultToJson(status, error_code, item_list);
}
	
	/**
	 * @Description: TODO(详情)</br>
	* @Title: selectUrgentDumpByID </br>
	* @param @param stock_transport_id
	* @param @return    设定文件</br>
	* @return JSONObject    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = {
			"/biz/transport/urgentdump_byid/{stock_transport_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectUrgentDumpByID(@PathVariable("stock_transport_id") int stock_transport_id) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		Map<String, Object> transHead = vtransportService
				.selectStockTransportHeadbyID(UtilObject.getIntegerOrNull(stock_transport_id));
		try {
			body.put("stock_transport_id", transHead.get("stock_transport_id"));
			body.put("stock_transport_code", transHead.get("stock_transport_code"));
			
			body.put("move_type_name",transHead.get("move_type_name"));
			body.put("move_type_id",transHead.get("move_type_id"));
			body.put("move_type_code",transHead.get("move_type_code"));
			body.put("create_user", transHead.get("user_name"));
			body.put("class_name", transHead.get("class_name"));
			body.put("syn_type_name", transHead.get("syn_type_name"));
	
			//body.put("syn_type", transHead.get("syn_type"));
			body.put("create_time", transHead.get("create_time"));
			body.put("status", transHead.get("status"));
			body.put("status_name", transHead.get("status_name"));
			body.put("remark", transHead.get("remark"));

			List<Map<String, Object>> item_list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> inputItemList = vtransportService
					.selectByStockTransportId(UtilObject.getIntegerOrNull(stock_transport_id));
			for (Map<String, Object> map : inputItemList) {
				Map<String, Object> itemmap = new HashMap<String, Object>();
				
				body.put("fty_input_id", map.get("fty_input_id"));
				body.put("fty_input_code", map.get("fty_input_code"));
				body.put("fty_input_name", map.get("fty_input_name"));
				body.put("location_input_id", map.get("location_input_id"));
				body.put("loc_input_code", map.get("location_input_code"));
				body.put("loc_input_name", map.get("location_input_name"));
				body.put("fty_output_id", map.get("fty_output_id"));
				body.put("fty_output_code", map.get("fty_output_code"));
				body.put("fty_output_name", map.get("fty_output_name"));
				body.put("location_output_id", map.get("location_output_id"));
				body.put("loc_output_code", map.get("location_output_code"));
				body.put("loc_output_name", map.get("location_output_name"));
				
				itemmap.put("mat_code", map.get("mat_code"));
				itemmap.put("mat_name", map.get("mat_name"));
				itemmap.put("mat_id", map.get("mat_id"));
				itemmap.put("batch", map.get("batch"));
				itemmap.put("package_type_id", map.get("package_type_id"));
				itemmap.put("stock_qty", UtilObject.getBigDecimalOrZero(map.get("stock_qty")));
				itemmap.put("product_batch", UtilObject.getStringOrNull(map.get("production_batch")));
				itemmap.put("erp_batch", UtilObject.getStringOrNull(map.get("erp_batch")));
				itemmap.put("package_type_name", UtilObject.getStringOrNull(map.get("package_type_name")));
				itemmap.put("package_type_code", UtilObject.getStringOrNull(map.get("package_type_code")));
				itemmap.put("unit_code", UtilObject.getStringOrNull(map.get("unit_code")));
				itemmap.put("unit_name", UtilObject.getStringOrNull(map.get("unit_name")));
				itemmap.put("transport_qty", UtilObject.getIntegerOrNull(map.get("transport_qty")));
				itemmap.put("remark", UtilObject.getStringOrEmpty(map.get("remark")));

				item_list.add(itemmap);
				body.put("item_list", item_list);
			}
			body.put("mes_doc_code", inputItemList.get(0).get("mes_doc_code")==null?"":inputItemList.get(0).get("mes_doc_code"));
			body.put("mat_doc_code", inputItemList.get(0).get("mat_doc_code")==null?"":inputItemList.get(0).get("mat_doc_code"));
			body.put("mat_off_code", inputItemList.get(0).get("mat_off_code")==null?"":inputItemList.get(0).get("mat_off_code"));
			body.put("mes_off_code", inputItemList.get(0).get("mes_fail_code")==null?"":inputItemList.get(0).get("mes_fail_code"));
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取紧急转储记账单-------------异常-----------------", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);
	}
	
	@RequestMapping("/biz/transport/urgent/delete/{stock_input_id}")
	@ResponseBody
	public JSONObject delete(@PathVariable("stock_input_id") String stock_input_id,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			utransportSerivce.delete(stock_input_id);	
//			inputTransportService.dealBeforeStatus(stock_input_id);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}
	
	
	@RequestMapping(value = {
	"/biz/transport/urgent/write_off/{stock_input_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject write_off(@PathVariable("stock_input_id") String stock_input_id,CurrentUser cUser) {
	
		JSONObject obj = new JSONObject();
		Map<String, Object> body = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		try {
			BizStockInputTransportHead bizStockInputTransportHead = new BizStockInputTransportHead();
			bizStockInputTransportHead.setStockTransportId(Integer.valueOf(stock_input_id));
			bizStockInputTransportHead = inputTransportService.selectEntity(bizStockInputTransportHead);
			List<Map<String, Object>> list = inputTransportService
					.getViItemInfo(Integer.valueOf(stock_input_id));
//			ErpReturn er = utransportSerivce.takeSapWriteOff(bizStockInputTransportHead, list);
//			if ("S".equals(er.getType())) {
				utransportSerivce.updateStockToWriteOff(Integer.valueOf(stock_input_id),bizStockInputTransportHead, list,
						cUser);
				body.put("stock_input_code", bizStockInputTransportHead.getStockTransportCode());
				body.put("stock_input_id", bizStockInputTransportHead.getStockTransportId());
//			} else {
//				errorString = er.getMessage();
//				body.put("stock_input_code", bizStockInputTransportHead.getStockTransportCode());
//				body.put("stock_input_id", bizStockInputTransportHead.getStockTransportId());
//			}
			status = true;
		} catch (WMSException e) {
			errorString=e.getMessage();
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("冲销", e);
	    } catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("虚拟转储入库冲销", e);
		
		}
		obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

}
