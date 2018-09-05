package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockTransportHead;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.vo.BizStockTransportHeadVo;

import net.sf.json.JSONObject;

public interface ITransportService {

	List<Map<String, Object>> getOrderList(Map<String, Object> param);

	BizStockTransportHeadVo getOrderView(String stockTransportId) throws Exception;

	String deleteTransportOrder(int stockTransportId, int receiptType);

	List<Map<String, Object>> getMoveList();

	List<DicMaterial> getMaterialList(DicMaterial material);

	JSONObject saveTransportData(JSONObject jsonData, CurrentUser user, boolean b) throws Exception;

	String saveOrderToFinish(JSONObject result, String userId) throws Exception;

	/**
	 * 根据转储单id查询转储单头信息
	 * 
	 * @author 刘宇 2018.04.18
	 * @param stockTransportId
	 * @return
	 * @throws Exception
	 */
	BizStockTransportHead getBizStockTransportHeadByStockTransportId(int stockTransportId) throws Exception;

}
