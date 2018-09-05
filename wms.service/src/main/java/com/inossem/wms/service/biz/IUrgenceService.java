package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.vo.BizUrgenceReqVo;
import com.inossem.wms.model.vo.BizUrgenceResVo;
import net.sf.json.JSONObject;

public interface IUrgenceService {
	
	List<BizUrgenceResVo> listBizUrgenceHead(BizUrgenceReqVo paramVo) ;
	
	Map<String,Object> getUrgenceById(Integer urgenceById,CurrentUser user);
	
	JSONObject saveOrder(JSONObject json, CurrentUser user);
	
	JSONObject subOrder(JSONObject json, CurrentUser user);
	
	JSONObject deleteOrder(Integer urgenceId,CurrentUser user);
	
	JSONObject postingOrder(JSONObject json,CurrentUser user);
	
	List<Map<String, Object>> getInnerOrder(Map<String, Object> paramMap);
	
	List<Map<String, Object>> getOutMat(Map<String, Object> paramMap);
	
	List<BizUrgenceResVo> listBizUrgenceForApp (BizUrgenceReqVo record) ;
	JSONObject subOrderApp(JSONObject json, CurrentUser user);
	
	JSONObject deleteByUUID(Map<String,Object> params);
	
	List<BizUrgenceResVo> listBizDocUrgenceHead(Map<String, Object> paramMap) ;
	
	List<Integer> listBizDocUrgenceType(Map<String, Object> param);
	
	List<Map<String, Object>> getDocinstockList(Map<String, Object> paramMap);
	
	List<Integer> listBizDocInstockType(Map<String, Object> param);
	
	List<Map<String, Object>> getDocOutstockList(Map<String, Object> paramMap);
	
	List<Integer> listBizDocOutstockType(Map<String, Object> param);
	
	List<Map<String, Object>> getDocStockTaskList(Map<String, Object> paramMap);
	
	 List<Map<String, Object>> getDocStockTaskReqList(Map<String, Object> paramMap);
	 
	 List<Map<String, Object>> getDocStockZlList(Map<String, Object> paramMap);
	 
	 
	 List<Map<String, Object>> listBizDocstockTransportOnPaging(Map<String, Object> paramMap) ;
		
		List<Integer> listBizDocTransportType(Map<String, Object> param);
		
		List<Map<String, Object>> listBizDocstockReturnOnPaging(Map<String, Object> paramMap);
		
		List<Integer> listBizDocReturnType(Map<String, Object> param);
		
		List<Map<String, Object>> listBizDocAllocateOnPaging(Map<String, Object> paramMap);

}
