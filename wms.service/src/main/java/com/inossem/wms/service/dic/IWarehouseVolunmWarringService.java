package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

public interface IWarehouseVolunmWarringService {
	List<Map<String,Object>>   selectWareHouseWarring(Map<String,Object> param);

	List<Map<String, Object>> selectWareHouseWarringForPortal(Map<String, Object> param);

	List<Map<String, Object>> selectWareHouseWarringByAreaId(int area_id);

}
