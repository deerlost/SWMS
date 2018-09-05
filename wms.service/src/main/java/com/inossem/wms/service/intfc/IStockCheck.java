package com.inossem.wms.service.intfc;

import java.util.List;
import java.util.Map;

public interface IStockCheck {

	boolean syncSapMatDoc(String ftyId,String locationId,String sDate,String eDate,String userId) throws Exception;
	
	List<Map<String,Object>> getStorageList(String ftyId,String locationId,String userId) throws Exception;
}
