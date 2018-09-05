package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.sap.SapDeliveryOrderHead;

import net.sf.json.JSONObject;

public interface DistributionService {
	// 按条件查询所有配货单
	public List<Map<String, Object>> selectCargo(Map<String, Object> map) throws Exception;

	// 查询配货单详细
	public Map<String, Object> selectCargoItem(Integer allocate_cargo_id);

	// 根据条件查询仓位库存
	List<Map<String, String>> selectByCondition(Map<String, String> map);

	// 保存配货单
	public int saveCargo(JSONObject json, User cUser) throws Exception;

	//根据交货单查询配货单  从sap
	public Map<String, Object> getSaleMasseg(SapDeliveryOrderHead sapDeliveryOrderHead, CurrentUser user)throws Exception ;
	
	Integer getCountBySaleOrder(String refer_receipt_code);
	
	boolean deleteCargo(Integer allocate_cargo_id) throws Exception;
}
