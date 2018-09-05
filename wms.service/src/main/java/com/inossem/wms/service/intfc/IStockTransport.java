package com.inossem.wms.service.intfc;

import java.util.Map;

public interface IStockTransport {

	Map<String,Object> postingForTransport(int stockTransportId , String userId) throws Exception;
	
}
