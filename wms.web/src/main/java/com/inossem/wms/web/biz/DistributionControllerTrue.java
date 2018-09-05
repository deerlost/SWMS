package com.inossem.wms.web.biz;

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

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.sap.SapDeliveryOrderHead;
import com.inossem.wms.service.biz.DistributionService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.dic.IDicClassTypeService;
import com.inossem.wms.service.intfc.ICwErpWs;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 配货单业务Controller
 * @author liguoming
 *
 */
@Controller
@RequestMapping("/biz/distribution")
public class DistributionControllerTrue {
	
private static final Logger logger = LoggerFactory.getLogger(DistributionControllerTrue.class);
   @Autowired
   private DistributionService distributionService;
   @Autowired
   private IDicClassTypeService  dicClassTypeService;
   @Autowired
   private ICommonService commonService ;
	@Autowired
	private ICwErpWs CwErpWsImpl;
	
	
	
	/**
	 * 配货单列表查询
	 * @param cuser
	 * @param json
	 * @return Json
	 */
	@RequestMapping(value = "/get_distrib_select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String,Object> getDistribSelect(@RequestBody JSONObject json,CurrentUser cUser) {
		JSONArray stateArray=json.getJSONArray("status");
		List<Object> state=new ArrayList<Object>();
		String massage="";
		int total=0;
		for (Object object : stateArray) {
			state.add(object.toString());
		}
		String refer_receipt_code=UtilObject.getStringOrEmpty(json.getString("search_code"));
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		Map<String ,Object> parameter=getParamMapToPageing(json);
		parameter.put("state", state);
		parameter.put("refer_receipt_code", refer_receipt_code.trim());
		parameter.put("userId", cUser.getUserId());
		List<Map<String,Object>> listMap=null;
		try{
			listMap =  distributionService.selectCargo(parameter);
			if (listMap != null && listMap.size() > 0) {
				Long totalCount=(Long)listMap.get(0).get("totalCount");
				total= totalCount.intValue();
			}
		}catch(Exception e){
			logger.error("配货单列表查询 --getDistribSelect", e);
			status=false;
			massage="查询失败";
		}          
		return 	UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(parameter.get("pageIndex")),
						UtilObject.getIntOrZero(parameter.get("pageSize")), total, listMap);
	}
	
	/**
	 * 配货单详细
	 * @param json
	 * @param cUser
	 * @return 
	 */
	@RequestMapping(value = "/get_distrib_view", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String,Object> getDistribView(@RequestBody JSONObject json,CurrentUser cUser) {
		String allocate_cargo_id=json.getString("allocate_cargo_id");
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String,Object> listMap=null;
		try{
		 listMap =distributionService.selectCargoItem(Integer.parseInt(allocate_cargo_id));
		}catch(Exception e){
			logger.error("配货单详细--getDistribSelect", e);
			status=false;
		}		
		return UtilResult.getResultToMap(status, error_code, listMap);
	}

	/**
	 * 根据物料编码和库存地点获取不同批次物料
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_mat_pro_batch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatProBatch(@RequestBody JSONObject json,CurrentUser cUser) {
		String msg="";
		String mat_id =json.getString("mat_id");
		String location_id =json.getString("location_id");
		String erp_batch=null;
		if(json.containsKey("erp_batch")) {			
	      erp_batch=json.getString("erp_batch");
		}
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("matId", mat_id);
		map.put("locationId", location_id);
		map.put("erpBatch", erp_batch);
		map.put("isDefault", 0);
		map.put("stockTypeId", 1);
		map.put("ftyId", json.getString("fty_id"));
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String,Object>> dicClassTypeList=null;
		try{
			dicClassTypeList=	commonService.getMatListByPosition(map);
			//dicClassTypeList =distributionService.selectByCondition(map);
		}catch(Exception e){
			logger.error("根据物料编码和库存地点获取不同批次物料查询 --", e);
			status = false;
			msg="查询失败！";
			
		}
		JSONObject obj=UtilResult.getResultToJson(status, error_code, msg,dicClassTypeList);		
		return obj;			
	}
	
	/**
	 * 保存配货单中对应物资库存地点的物料数量
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject submit(@RequestBody JSONObject json,CurrentUser cUser) {
	
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg="保存交货单成功";
	    int allocateCargoId=-1;
		try{
			allocateCargoId= distributionService.saveCargo(json,cUser);
		}catch(Exception e){
			logger.error("保存配货单中对应物资库存地点的物料数量 --", e);
			status = false;
			msg="提交失败！";
		}		
		Map<String,String> map=new HashMap<String,String>();
		map.put("allocate_cargo_id", allocateCargoId+"");
		return UtilResult.getResultToJson(status, error_code,msg,map);
	}
	
	@RequestMapping(value = "/delete_cargo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteCargo(Integer allocate_cargo_id) {
	
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg="删除配货单成功";
	  
		try{
			status= distributionService.deleteCargo(allocate_cargo_id);
		}catch(WMSException wmse){
			logger.error("删除配货单 --", wmse);
			status = false;
			msg=wmse.getMessage(); 
	 	}			
		catch(Exception e){
			logger.error("删除配货单 --", e);
			status = false;
			msg="删除配货单失败！"; 
		}		
	
		return UtilResult.getResultToJson(status, error_code,msg,"");
	}
	
	/**
	 * 
	 * @param json
	 * @param cUser
	 * @return 
	 */
	@RequestMapping(value = "/get_class_type", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getClassType(CurrentUser cUser){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String masage="";
		List<Map<String,Object>> dicClassTypeList=null;
		try{
			dicClassTypeList =dicClassTypeService.selectAll();
		}catch(Exception e){
			logger.error("班次信息查询 --", e);
			status = false;
			masage="班次信息查询失败";
		}
		JSONObject obj=UtilResult.getResultToJson(status, error_code,masage, dicClassTypeList);
		return obj;		
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
		param.put("paging", true);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		param.put("sortAscend", sortAscend);
		param.put("sortColumn", sortColumn);
		return param;
	}

	
	/**
	 * 从SAP获取销售订单(概要)
	 * 
	 * @return
	 */
	@RequestMapping(value = {
			"/get_sale_masseg" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getSaleMasseg(@RequestBody JSONObject json, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String masage="获取交货单成功";
		Map<String, Object> list = new HashMap<String,Object>();  
		String refer_receipt_code= json.getString("refer_receipt_code");
		SapDeliveryOrderHead sapDeliveryOrderHead=null;
		Integer count=1;
		if (!"".equals(refer_receipt_code)) {
			while (refer_receipt_code.length() < 10) { // 不够十位数字，自动补0
				refer_receipt_code = "0" + refer_receipt_code;
			}
			try {	
				//判断这个交货单是否被其他业务使用
//				count=distributionService.getCountBySaleOrder(refer_receipt_code);
//				if(count>0) {
//					masage="此交货单已被使用，不能配货！";	
//					list.put("", "");
//					status = false;
//				}
				//sap 返回来的交货单数据
				sapDeliveryOrderHead=CwErpWsImpl.getDoOrder(refer_receipt_code);				
				//转换成页面所需得信息
				list = distributionService.getSaleMasseg(sapDeliveryOrderHead, user);
				
			} catch (Exception e) {
				logger.error("获取交货单订单 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
				masage="获取交货单信息失败！";
			}
		} else {
			logger.error("获取交货单订单 --", "请输入查询条件");
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		}
		return UtilResult.getResultToMap(status, error_code, masage, list);

	}


	
}
