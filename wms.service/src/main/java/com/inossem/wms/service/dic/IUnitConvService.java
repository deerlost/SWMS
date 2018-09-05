package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.unit.UnitConv;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IUnitConvService {

	
	List<Map<String, Object>> listUnitOnPaging(Map<String,Object> map);
	
	JSONObject addOrUpdateUnit(String matGroupCode, String unitConvId, JSONArray unitArray,UnitConv unitConv) throws Exception;
	
	JSONObject deleteOrder(Map<String,Object> map) throws Exception;
	
	Map<String,Object> getMatByCode(String matCode);
	
	Map<String,Object> getUnitConvById(Integer unitConvId,CurrentUser user);
	
	List<DicUnit> selectAllUnit();
	
	DicUnit getUnitByCode(String unitCode);
}
