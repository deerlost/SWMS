package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;

public interface IStockCheckService {

	boolean syncSapMatDoc(String ftyId,String locationId,String sDate,String eDate,String userId) throws Exception;
	
	List<Map<String,Object>> getMatItemList(String ftyId,String locationId,String sDate,String eDate,String status1,String status2,String matId) throws Exception;

	List<Map<String, Object>> getMatItemNeutralizeList();
	
	int saveToWmsMatDoc(List<Map<String,Object>> matDocList) throws Exception;
	
	int saveOrder(List<Map<String,Object>> matDocList,CurrentUser cUser) throws Exception;
	
	List<Map<String,Object>> getStorageList(String ftyId,String locationId,String userId) throws Exception;
}
