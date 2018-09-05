package com.inossem.wms.web.biz;

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
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumMesErrorInfo;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.service.biz.IInputTransportService;
import com.inossem.wms.service.biz.IInputUrgentTransportService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;
import com.inossem.wms.wsdl.sap.input.MSGHEAD;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/input/urgent/transport/")
public class InputStockUrgentTransportController {

	private static final Logger logger = LoggerFactory.getLogger(InputStockUrgentTransportController.class);
	

	@Autowired
	private IInputUrgentTransportService inputTransportService;
	
	@Autowired
	private IInputTransportService inputService;



	
	/**
	 * 新增转储入库单页面跳转
	 * 
	 * @param cUser
	 *           
	 * @return
	 */
	@RequestMapping(value = "/to_transport_add", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object platformBaseInfo(CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();	
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		JSONObject json=new JSONObject();
		try {					
			json.put("create_user", cUser.getUserName());
			String time=inputTransportService.selectNowTime();
			json.put("create_time", time);
			json.put("syn_type_id", EnumSynType.MES_SAP_SYN.getValue());
			json.put("class_type_id", inputTransportService.selectNowClassType()==null?0:inputTransportService.selectNowClassType());	
			List<Map<String,Object>> list=inputTransportService.selectAllclassType();			
			json.put("class_type", list);			
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
			json.put("syn_type", myJsonArray);	
		} catch (Exception e) {
			logger.error("跳转到转储入库单页", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}		
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, errorString, json);
		return obj;
	}



	/**
	 * 新增转储入库单
	 * 
	 * @param json
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/transport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateOtherInput(@RequestBody JSONObject json, CurrentUser user) {
		

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		JSONObject obj=new JSONObject();
		BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
		try {			
			if(!"".equals(json.get("stock_task_id"))) {
				JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));
				List<Map<String,Object>> itemList = inputTransportService.getMatListByID(myJsonArray);				
				bizStockInputTransportHead.setStockTransportCode(json.get("stock_input_code").toString());
				bizStockInputTransportHead.setStockTransportId(Integer.valueOf(json.get("stock_task_id").toString()));
				
				
//				inputTransportService.selectIsWriteOff(Integer.valueOf(json.get("stock_task_id").toString()));
				
				
//				ErpReturn er=inputTransportService.takeSap(bizStockInputTransportHead,itemList);
				bizStockInputTransportHead=inputTransportService.selectEntity(bizStockInputTransportHead);
				
//				if("S".equals(er.getType())) {
//					inputTransportService.updateMatDocCode(er,bizStockInputTransportHead);				
					inputTransportService.modifyNum2(itemList,user,bizStockInputTransportHead);
					try {	
						WmIopRetVal wmIopRetVal=inputTransportService.takeMes(bizStockInputTransportHead,itemList,user);
					} catch (Exception e) {
						logger.error("新增转储入库", e);						
					}
//					inputTransportService.updateStockHeandStatus(itemList,bizStockInputTransportHead.getStockTransportId());
//					inputTransportService.updateTransportStatus(itemList,user);
//				}else {
//					status=false;
//					errorString=er.getMessage();
//				}
				status = true;
			}else {
//				JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));
//				List<Map<String,Object>> itemList = inputTransportService.getMatListByID(myJsonArray);		
//				bizStockInputTransportHead=inputTransportService.insertStock(itemList,user,json);
//				inputTransportService.modifyNum(itemList,user,bizStockInputTransportHead);
				bizStockInputTransportHead=inputTransportService.submitTransport(json,user,bizStockInputTransportHead);
				
//				inputTransportService.updateStockHeandStatus(itemList,bizStockInputTransportHead.getStockTransportId());
//				inputTransportService.updateTransportStatus(itemList,user);
//				inputTransportService.setHistory(bizStockInputTransportHead,user,json,itemList);				
				status = true;
			}
			
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增转储入库", e);
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		}
		body.put("stock_input_code", UtilObject.getStringOrEmpty(bizStockInputTransportHead.getStockTransportCode()));
		body.put("stock_input_id", UtilObject.getStringOrEmpty(bizStockInputTransportHead.getStockTransportId()));
		obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}
	
	
	
	@RequestMapping(value = "/takeMes/{stock_input_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object takeMes(CurrentUser user,@PathVariable("stock_input_id") String stock_input_id) {
		

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		JSONObject obj=new JSONObject(); 
		try {					
			BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
			bizStockInputTransportHead.setStockTransportId(Integer.valueOf(stock_input_id));
			bizStockInputTransportHead=inputTransportService.selectEntity(bizStockInputTransportHead);
			if(bizStockInputTransportHead.getStatus().toString().equals("20")) {
				String result=inputService.takeMesFailAgain(bizStockInputTransportHead,user);
			}else {
				WmIopRetVal wmIopRetVal=inputTransportService.takeMesAgain(bizStockInputTransportHead,user);
				if(!"".equals(wmIopRetVal.getErrmsg())){
					String msg = wmIopRetVal.getErrmsg();
					msg = EnumMesErrorInfo.check(msg);
					errorString = msg;
					body.put("msg",msg);
				}
			}
			status = true;
			body.put("stock_input_code", bizStockInputTransportHead.getStockTransportCode());
			body.put("stock_input_id", bizStockInputTransportHead.getStockTransportId());	
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增转储入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}
		obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}

	


	
	/**
	 * 1.5	添加作业单列表查询
	 * 
	 * @param task_id,receipt_type
	 * @return
	 */
	@RequestMapping(value = "/select_stock_task", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMoveReasonList(String  task_id,String receipt_type) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("stock_task_code", UtilString.getStringOrEmptyForObject(task_id).trim());		
			map.put("receiptType", receipt_type);
			returnList= inputTransportService.select_stock_task(map);			
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("添加作业单列表查询", e);
		}
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}
	
	
	/**
	 * 1.6	添加作业单
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/stock_task_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object stock_task_add(@RequestBody JSONObject json) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		try {			
			JSONArray myJsonArray =JSONArray.fromObject(json.get("task_id"));	
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("task_id", myJsonArray);
			returnList= inputTransportService.getStockMatListByTaskId(map);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("添加作业单", e);
		}
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}


	



	/**
	 * 转储入库列表
	 * 
	 * @param cuser，json
	 * @return
	 */
	@RequestMapping(value = "/transport_input_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getTransportInputList(CurrentUser cuser, @RequestBody JSONObject json) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		try {
			if (json.containsKey("page_index")) {
				pageIndex = json.getInt("page_index");
			}
			if (json.containsKey("page_size")) {
				pageSize = json.getInt("page_size");
			}
			Integer totalCount = -1;
			String stock_transport_code="";
			JSONArray statusList=null;

			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			if (json.containsKey("stock_input_code")) {
				stock_transport_code = json.getString("stock_input_code");
			}
			if (json.containsKey("status_list")) {
				statusList = json.getJSONArray("status_list");
			}

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("statusList", statusList);
			map.put("stock_transport_code", UtilString.getStringOrEmptyForObject(stock_transport_code).trim());
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);						
			map.put("totalCount", totalCount);
			map.put("receiptType", EnumReceiptType.STOCK_INPUT_TRANSPORT_PRODUCTION_URGENT.getValue());
			map.put("createUser", cuser.getUserId());

			List<Map<String, Object>> shList = inputTransportService.getTransportInputList(map);
//			
			if (shList != null && shList.size() > 0) {
			Long totalCountLong = (Long) shList.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
			returnList=shList;
				
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("转储入库列表", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}

	

	/**
	 * 转储入库详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = {"/transport_input_info/{stock_input_id}"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getInputOtherInfo(CurrentUser cuser,@PathVariable("stock_input_id") Integer stock_input_id
			) {
		
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = 0;

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> input = new HashMap<String, Object>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("stockInputId", stock_input_id);
			Map<String,Object> headMap = inputTransportService.getTransportInputInfo2(map);
			input=headMap;				
			List<Map<String,Object>> itemList = inputTransportService.getInputTransportItemListByID(stock_input_id);
			input.put("item_list", itemList);
			input.put("mes_doc_code", itemList.get(0).get("mes_doc_code")==null?"":itemList.get(0).get("mes_doc_code"));
			input.put("mat_doc_code", itemList.get(0).get("mat_doc_code")==null?"":itemList.get(0).get("mat_doc_code"));
			input.put("mat_off_code", itemList.get(0).get("mat_off_code")==null?"":itemList.get(0).get("mat_off_code"));
//			if(input.get("status").toString().equals("20")) {
				input.put("mes_off_code", itemList.get(0).get("mes_fail_code")==null?"":itemList.get(0).get("mes_fail_code"));
//			}
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("转储入库详情", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, input);
		UtilJSONConvert.deleteNull(obj);
		return obj;
	}
	
	
	@RequestMapping(value = "/write_off", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object write_off(@RequestBody JSONObject json,CurrentUser cuser) {
		JSONObject obj=new JSONObject();
		Map<String, Object> body = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		try {
			BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
			bizStockInputTransportHead.setStockTransportCode(json.get("stock_input_code").toString());
			bizStockInputTransportHead.setStockTransportId(Integer.valueOf(json.get("stock_task_id").toString()));
			JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));
			List<Map<String,Object>> itemList = inputTransportService.getMatListByID(myJsonArray);		
			ErpReturn er=inputTransportService.takeSapWriteOff(bizStockInputTransportHead,itemList);
			if("S".equals(er.getType())) {
				String result=inputService.takeMesFail(bizStockInputTransportHead,itemList,cuser);
				inputTransportService.updateStockToWriteOff(Integer.valueOf(json.get("stock_input_id").toString()),cuser);	
				body.put("stock_input_code", bizStockInputTransportHead.getStockTransportCode());
				body.put("stock_input_id", bizStockInputTransportHead.getStockTransportId());
				if(result==null) {
					errorString=result;
				}
			}else {
				errorString=er.getMessage();
				body.put("stock_input_code", bizStockInputTransportHead.getStockTransportCode());
				body.put("stock_input_id", bizStockInputTransportHead.getStockTransportId());
			}
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("供应商列表", e);
			
		}
		obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}
	
	@RequestMapping("/delete/{stock_input_id}")
	@ResponseBody
	public JSONObject delete(@PathVariable("stock_input_id") String stock_input_id,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			inputTransportService.delete(stock_input_id);	
			inputTransportService.dealBeforeStatus(stock_input_id);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}

}
