package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.dic.DicWarehouse;

import net.sf.json.JSONObject;

public interface IPersonnelService {

	
	List<Map<String, Object>> queryPersonnel(Map<String, Object> param) throws Exception;
	
	int insertPersonnel (JSONObject json) throws Exception;
	
	void deletePersonnel(int typeId ,int id) throws Exception;

	 List<DicProductLine> selectAllProductLine();
	 
	 List<DicWarehouse> selectAllWhIdAndWhCodeAndWhName();
}
