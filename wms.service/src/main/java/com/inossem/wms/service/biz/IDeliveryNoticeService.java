package com.inossem.wms.service.biz;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONObject;

public interface IDeliveryNoticeService {

	ArrayList<HashMap<String, Object>> getContract(JSONObject findObj) throws Exception;

	/**
	 * 根据采购订单号查询 采购订单详细信息
	 * 
	 * @param ebeln
	 *            采购订单
	 * @return
	 * @throws Exception
	 */
	HashMap<String, Object> getContractInfo(String ebeln) throws Exception;
}
