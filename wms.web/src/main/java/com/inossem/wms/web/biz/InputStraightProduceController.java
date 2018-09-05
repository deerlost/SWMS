package com.inossem.wms.web.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumMesErrorInfo;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumProductOrderType;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputAccountProduceService;
import com.inossem.wms.service.biz.IInputProduceTransportService;
import com.inossem.wms.service.biz.IInputStraightProduceService;
import com.inossem.wms.service.biz.IInputTransportService;
import com.inossem.wms.service.biz.IInputVirtualProduceService;
import com.inossem.wms.service.biz.IPkgOperationService;
import com.inossem.wms.service.intfc.sap.CwMesWsImpl;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmInvQueryNewResult;
import com.inossem.wms.wsdl.mes.update.WmInvQueryNewResult;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 生产入库
 * @author libo
 *
 */
@Controller
@RequestMapping("/biz/input/straight/production")
public class InputStraightProduceController {
	
	private static final Logger logger = LoggerFactory.getLogger(InputStraightProduceController.class);
	 
	
	@Autowired
	private CwMesWsImpl iCwMesWs;
	
	@Autowired
	private IInputStraightProduceService iInputProduceTransportService;
	
	@Autowired
	private IInputProduceTransportService iInputProduceService;
	
	@Autowired
	private IPkgOperationService pkgService;
	
	@Autowired
	private IInputTransportService inputTransportService;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private IInputAccountProduceService iInputAccountProduceTransportService;
	
	
	@RequestMapping(value="/contract_info/{purchase_order_code}",method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject func6(@PathVariable("purchase_order_code") String purchase_order_code,CurrentUser cuser) {
		String msg="";
		Map<String, Object> map = new HashMap<String, Object>();
	
		boolean status = true;
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
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
		map.put("syn_type_list", myJsonArray);
		try {
			Map<String,Object> order_map=commonService.selectProduceOrderInfo(purchase_order_code);
			if(order_map!=null && order_map.size()>0) {
				String mat_code=order_map.get("PLNBEZ").toString();
				Map<String,Object> matMap=iInputProduceTransportService.getMatInfoByCode(mat_code.trim());
				if(matMap!=null) {
					String mat_id=matMap.get("mat_id").toString();
					DicFactory dicFactory=iInputProduceTransportService.selectFtyNameById(order_map.get("WERKS").toString());
//					List<Map<String,Object>> erp_batch_list=iInputProduceService.selectErpList(mat_id,dicFactory.getFtyId());
					List<Map<String,Object>> packageTypelist=iInputAccountProduceTransportService.selectPackageTypeList(mat_id,dicFactory.getFtyId());		
					List<Map<String,Object>> classTypeList=inputTransportService.selectAllclassType();
					
					Map<String, Object> productLineMap=pkgService.selectPkgClassLineInstallationList(cuser.getUserId());
					map.put("class_type_id", inputTransportService.selectNowClassType()==null?0:inputTransportService.selectNowClassType());
					map.put("syn_type_id", EnumSynType.MES_SAP_SYN.getValue());
					map.put("mat_id", mat_id);
					map.put("class_type_list", classTypeList);
//					map.put("erp_batch_list", erp_batch_list);				
					map.put("package_type_list", packageTypelist);
					map.put("product_line_list", productLineMap.get("productLineList"));					
					map.put("mat_code", order_map.get("PLNBEZ").toString().trim());
					map.put("produce_order_code", order_map.get("AUFNR"));
					map.put("mat_name", matMap.get("mat_name"));
					map.put("fty_name", dicFactory.getFtyName());
					map.put("fty_id", dicFactory.getFtyId());
					map.put("fty_code", dicFactory.getFtyCode());
					List<Map<String,Object>> locationlist=iInputProduceService.selectLocationList(dicFactory.getFtyId(),cuser.getUserId());
					map.put("location_list", locationlist);
					map.put("business_type_name", EnumProductOrderType.getNameByValue(order_map.get("AUART").toString()));
					map.put("business_type_id", order_map.get("AUART"));
					map.put("input_stock_num", order_map.get("GAMNG"));
//					Map<String,Object> unit_map=iInputProduceService.getUnitIdByCode(order_map.get("GMEIN").toString().trim());
					map.put("unit_id", matMap.get("unit_id"));	
					map.put("unit_name", matMap.get("name_zh"));
					map.put("wms_unit_code", matMap.get("unit_code"));
					if(UtilObject.getStringOrEmpty(matMap.get("unit_code")).equals("KG")) {
						map.put("relation", 1);
					}else {
						map.put("relation", 1000);
					}
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("sap_unit_code", order_map.get("GMEIN").toString().trim());
					param.put("wms_unit_code", matMap.get("unit_code"));
					String rel=iInputProduceService.getUnitRelSapAndWms(param);
					map.put("input_stock_num", new BigDecimal(order_map.get("GAMNG").toString()).multiply(new BigDecimal(rel.toString())));					
					map.put("create_user", cuser.getDisplayUsername());
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
					java.util.Date date=new java.util.Date();  				
					map.put("create_time", sdf.format(date));
				}else {
					msg="未找到该物料信息";
					status = false;
				}
				
			}else {
				msg="获取生产订单失败";
				map.put("msg", msg);
				status = false;
			}
		}catch (Exception e) {
			logger.error("新增生产入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}	
		
		JSONObject obj = UtilResult.getResultToJson(status, errorCode,msg, map);		
		return obj;
	}
	
	
	@RequestMapping(value ="/production_input_list",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject production_input_list(CurrentUser cuser, @RequestBody JSONObject json) {
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
			String stock_transport_code=null;
			JSONArray statusList=null;

			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			if (json.containsKey("condition")) {
				stock_transport_code = json.getString("condition");
			}
			if (json.containsKey("status_list")) {
				statusList = json.getJSONArray("status_list");
			}

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("statusList", statusList);
			map.put("stock_transport_code", stock_transport_code);
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("sortAscend", sortAscend);
			map.put("sortColumn", sortColumn);						
			map.put("totalCount", totalCount);
			map.put("receiptType", EnumReceiptType.STOCK_INPUT_STRAIGTH.getValue());
			map.put("createUser", cuser.getUserId());
			List<Map<String, Object>> shList = iInputProduceTransportService.production_input_list(map);			
			if (shList != null && shList.size() > 0) {
			Long totalCountLong = (Long) shList.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
			returnList=shList;				
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("生产入库列表", e);
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		return obj;
	}
	
	
	@RequestMapping("/production_input_info/{stock_input_id}")
	@ResponseBody
	public JSONObject production_input_info(@PathVariable("stock_input_id") String stock_input_id,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
//			while (stock_input_id.length() < 10) { // 不够十位数字，自动补0
//				stock_input_id = "0" + stock_input_id;
//			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("stockInputId", stock_input_id);
			result  = iInputProduceTransportService.getInputHeadMap(map);
			List<Map<String, Object>> itemList  = iInputProduceTransportService.getItemMap(result);
			List<String>  list=new ArrayList<String>();
			List<String>  docYearlist=new ArrayList<String>();
			if(itemList.get(0)!=null) {
				Set<String> keys = itemList.get(0).keySet();
				for(String key :keys){
					result.put(key, itemList.get(0).get(key));
				}
			}			
			for(int i=0;i<itemList.size();i++) {
				list.add(itemList.get(i).get("item_id").toString());
				if(itemList.get(i).get("doc_year")!=null) {
					docYearlist.add(itemList.get(i).get("doc_year").toString());
				}
				
			}
			List<Map<String, Object>> packageList=iInputProduceTransportService.getPackageList(list);			
			result.put("item_list", packageList);
			result.put("item_id_list", list);
			result.put("doc_year_list", docYearlist);
			result.put("mes_doc_code", itemList.get(0).get("mes_doc_code")==null?"":itemList.get(0).get("mes_doc_code"));
			result.put("mat_doc_code", itemList.get(0).get("mat_doc_code")==null?"":itemList.get(0).get("mat_doc_code"));
			result.put("mat_off_code", itemList.get(0).get("mat_off_code")==null?"":itemList.get(0).get("mat_off_code"));
//			if(result.get("status").toString().equals("20")) {
				result.put("mes_off_code", itemList.get(0).get("mes_fail_code")==null?"":itemList.get(0).get("mes_fail_code"));
//			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
//		return JSONObject.fromObject("{\"head\":{\"status\":\"true\",\"error_code\":\"0\",\"msg\":\"Success\",\"page_index\":\"1\",\"page_size\":\"20\",\"total\":\"-1\"},\"body\":{\"produce_order_code\":\"8500036466\",\"mat_code\":\"60000648\",\"mat_name\":\"普通钢丝绳/6*19-Φ18.5/FC\",\"fty_name\":\"工厂\",\"class_type_name\":\"1\",\"syn_type_name\":\"1\",\"business_type_name\":\"单据类型\",\"order_num\":\"1\",\"unit_name\":\"1\",\"remark\":\"备注\",\"create_user\":\"创建人\",\"create_time\":\"2018-12-02\",\"product_line_name\":\"产品线\",\"package_type_list\":[{\"package_type_name\":\"11\"},{\"package_standard\":\"pv\"},{\"package_num\":\"1\"},{\"input_stock_num\":\"6\"},{\"production_batch\":\"1\"},{\"erp_batch\":\"11\"},{\"quality_batch\":\"11\"},{\"location_id\":\"11\"},{\"rec_package_id\":\"11\"},{\"remark\":\"11\"}]}}");
	}
	
	
	
	@RequestMapping(value = "/production", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateOtherInput(CurrentUser user,@RequestBody JSONObject json) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = false;
		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			if("".equals(json.get("stock_input_id"))) {
				Map<String,Object> map=iInputProduceTransportService.insertProduction(user,json);
				JSONArray batchArray=(JSONArray) map.get("batchArray");
				stockInputHead=(BizStockInputHead) map.get("bizStockInputHead");			
//				ErpReturn er=iInputProduceTransportService.takeSap(bih,json);
				JSONArray itemArray=(JSONArray) map.get("itemArray");
				
//				if("S".equals(er.getType())) {
					
					iInputProduceTransportService.modifyNum(stockInputHead,user,json,batchArray,itemArray);	
					try {
						WmIopRetVal wmIopRetVal=iInputProduceTransportService.takeMes(stockInputHead,json,user,itemArray);
					} catch (Exception e) {
						logger.error("新增生产入库", e);						
					}
//					iInputProduceTransportService.updateMatDocCode(er,itemArray);
//					iInputProduceService.setHistory(bih,user,itemArray,json,batchArray);
//				}else {
//					errorString=er.getMessage();
//				}
				status=true;	
			}else {
				
				Map<String,Object> map=iInputProduceTransportService.getstockInputHeadAndBatch(json.get("stock_input_id").toString());
				stockInputHead=(BizStockInputHead)map.get("bizStockInputHead");
//				ErpReturn er=iInputProduceTransportService.takeSap(stockInputHead,json);
				JSONArray itemArray=(JSONArray) map.get("itemArray");
				
//				if("S".equals(er.getType())) {
					iInputProduceTransportService.modifyNum2(stockInputHead,user,json,JSONArray.fromObject(map.get("list")),itemArray);
					try {
						WmIopRetVal wmIopRetVal=iInputProduceTransportService.takeMes2(stockInputHead,json,user,itemArray);
					} catch (Exception e) {
						logger.error("新增生产入库", e);						
					}
//					iInputProduceTransportService.updateMatDocCode(er,itemArray);				
//					iInputProduceService.setHistory2(stockInputHead,user,itemArray,json,JSONArray.fromObject(map.get("list")));
//				}else {
//					errorString=er.getMessage();
//				}
				status=true;
			}
			
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增生产入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
			status = false;
		}
		body.put("stockInputId", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
		body.put("stockInputCode", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputCode()));
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}
	
	
	@RequestMapping(value = "/takeMes/{stock_input_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object takeMes(CurrentUser user,@PathVariable("stock_input_id") String stock_input_id) {

		JSONObject body = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		JSONObject json=new JSONObject();
		boolean status = false;
		BizStockInputHead stockInputHead = new BizStockInputHead();
		try {
			Map<String,Object> map=iInputProduceTransportService.getstockInputHeadAndBatch(stock_input_id);
			stockInputHead=(BizStockInputHead)map.get("bizStockInputHead");
			JSONArray itemArray=(JSONArray) map.get("itemArray");
			if(stockInputHead.getStatus().toString().equals("20")) {
				String result=iInputProduceService.takeMesFailAgain(stockInputHead,json,user,itemArray);
				errorString=result;
				body.put("msg", result);
			}else {
				WmIopRetVal wmIopRetVal=iInputProduceTransportService.takeMesAgain(stockInputHead,json,user,itemArray);
				if(wmIopRetVal!=null) {
					if(!"".equals(UtilObject.getStringOrEmpty(wmIopRetVal.getErrmsg()))){
						String msg = wmIopRetVal.getErrmsg();
						msg = EnumMesErrorInfo.check(msg);
						errorString = msg;
						body.put("msg",msg);
					}
				}
			}
			body.put("stockInputId", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputId()));
			body.put("stockInputCode", UtilString.getStringOrEmptyForObject(stockInputHead.getStockInputCode()));
			status=true;
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("新增生产入库", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}
		
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;
	}
	
	
	
	
	@RequestMapping(value = "/get_package_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object get_package_list(@RequestBody JSONObject json) {
		boolean status = true;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		try {		
			param.put("package_time", UtilObject.getStringOrEmpty(json.getString("package_time")));
			param.put("package_code", UtilObject.getStringOrEmpty(json.getString("package_code")));
			param.put("production_batch", UtilObject.getStringOrEmpty(json.getString("production_batch")));
			param.put("mat_id", UtilObject.getStringOrEmpty(json.getString("mat_id")));
			param.put("package_type_id", UtilObject.getStringOrEmpty(json.getString("package_type_id")));
			
			result = this.iInputProduceTransportService.selectPkgList(param);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("包装管理列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, result);
	}
	
	
//	@RequestMapping(value = "/write_off", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody Object getMaterialBatchInfo(@RequestBody JSONObject json,CurrentUser cuser) {
//		boolean status = true;
//		Map<String, Object> body = new HashMap<String, Object>();
//		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
//		try {
//			JSONArray item_id=(JSONArray) json.get("item_id");
//			JSONArray doc_year=(JSONArray) json.get("doc_year");
//			ErpReturn er=iInputProduceTransportService.takeSapWriteOff(item_id,doc_year);
//			int  stock_input=Integer.valueOf(json.get("stock_input_id").toString());
//			if(er.getType().equals("S")) {
//				iInputProduceService.takeMesWriteOff(item_id,cuser);
//				iInputProduceTransportService.updateStockToWriteOff(stock_input,cuser,json);
//				iInputProduceService.updateWriteOffStatus(er,item_id,stock_input);
//			}else {
//				errorString="冲销失败";
//			}			
//		} catch (Exception e) {
//			errorString="冲销失败";
//			status = false;
//			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
//			logger.error("冲销", e);
//		}
//		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
//		return obj;
//	}
	
	@RequestMapping("/delete/{stock_input_id}")
	@ResponseBody
	public JSONObject delete(@PathVariable("stock_input_id") String stock_input_id,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			iInputProduceService.delete(stock_input_id);	
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("生产单信息", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}
	
	
	@RequestMapping(value="/select_mat_info_mes",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject select_mat_info_mes(CurrentUser cuser,@RequestBody JSONObject json) {
		String msg="";
		Map<String, Object> map = new HashMap<String, Object>();
		boolean status = true;
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();		
		try {
			String fty_code=json.getString("fty_code");		
			String product_line_name=json.getString("product_line_name");
			String unit_code=json.getString("wms_unit_code");
			String location_id="";
			String mat_code = "";
			if(product_line_name.equals("纤维产品线") ||
					product_line_name.equals("乳液产品线")) {
				mat_code=json.getString("mat_code");
			}
			List<Map<String, Object>> locationIds = iInputProduceService.getFtyLineLocationList(fty_code, product_line_name);    
			List<Map<String,Object>> mesMatList=new ArrayList<Map<String,Object>>();
			if(locationIds!=null && !locationIds.isEmpty()) {
				for(int i=0; i< locationIds.size(); i++) {
					ArrayOfWmInvQueryNewResult result = iCwMesWs.searchMatStock(mat_code,UtilObject.getStringOrEmpty(locationIds.get(i).get("mes_location_id")));
					List<WmInvQueryNewResult> list=result.getWmInvQueryNewResult();
					for(WmInvQueryNewResult wmInvQueryResult:list) {
						Map<String, Object> m = new HashMap<String, Object>();						
						m.put("qty",wmInvQueryResult.getWgtAftBook());	
						String mesName = iInputProduceService.selectMatName(wmInvQueryResult.getMtrlCode());
						m.put("mes_name",UtilObject.getStringOrEmpty(mesName) );		
						m.put("mes_code",wmInvQueryResult.getMtrlCode());	
						m.put("mes_package_standard", wmInvQueryResult.getWgtPerPack());
						m.put("mes_rank", wmInvQueryResult.getRankId());
						m.put("mes_bch", wmInvQueryResult.getBch());
						m.put("mes_location_id", wmInvQueryResult.getLocationId());
						m.put("mes_location_name", wmInvQueryResult.getLocation());
						Map<String,Object> matMap=iInputProduceTransportService.getMatInfoByCode(wmInvQueryResult.getMtrlCode().trim());
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("mes_unit_code", wmInvQueryResult.getWgtDim().toString());
						param.put("wms_unit_code", unit_code);
						String rel=iInputProduceService.getUnitRelMesAndWms(param);
						m.put("qty",wmInvQueryResult.getWgtAftBook().multiply(new BigDecimal(rel.toString())));
						mesMatList.add(m);
					}
				}
			}	
			map.put("mesMatList", mesMatList);	
			map.put("mes_location_id", location_id);
		}catch (Exception e) {
			logger.error("查询物料mes库存", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}			
		JSONObject obj = UtilResult.getResultToJson(status, errorCode,msg, map);		
		return obj;
	}
	
	
	

	
}
