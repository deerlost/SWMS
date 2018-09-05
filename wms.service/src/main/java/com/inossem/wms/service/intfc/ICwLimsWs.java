package com.inossem.wms.service.intfc;

import net.sf.json.JSONArray;

public interface ICwLimsWs {
	
	JSONArray searchReport(String sampName,String batchName,String Specification) throws Exception;
	
}
