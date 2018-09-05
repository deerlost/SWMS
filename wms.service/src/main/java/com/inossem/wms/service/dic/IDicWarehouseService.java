package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 仓库
 * 
 * @author 刘宇 2018.03.01
 *
 */
public interface IDicWarehouseService {
	/**
	 * 查询所有仓库id，编码，描述
	 * 
	 * @author 刘宇 2018.03.01
	 * @return
	 */
	List<DicWarehouse> listWhIdAndWhCodeAndWhName() throws Exception;
	List<Map<String, Object>> queryWarehouseList(Map<String, Object> paramMap);
	
	JSONObject addOrUpdateWarehouse( String whId, JSONArray locationArray,DicWarehouse dw) throws Exception;
	
	Map<String,Object> getWarehouseById(Integer whId,CurrentUser user);
	
	List<Map<String, Object>> queryLoationList(Map<String, Object> paramMap);
	
	
	List<Map<String, Object>> queryWarehouseAreaList(Map<String, Object> paramMap);
	
	JSONObject addOrUpdateWarehouseAreas( String areaId,DicWarehouseArea dw) throws Exception;
	
	Map<String,Object> getWarehouseAreaById(Integer areaId,CurrentUser user);
	
	List<Map<String, Object>> queryWarehouseList();
	
	
}
