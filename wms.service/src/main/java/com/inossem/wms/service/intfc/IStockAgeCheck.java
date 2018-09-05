package com.inossem.wms.service.intfc;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 库龄考核查询连接SAP获取移动平均价接口
 * 
 * @author 刘宇 2018.02.26
 *
 */
public interface IStockAgeCheck {
	/**
	 * 库龄考核查询连接SAP获取移动平均价
	 * 
	 * @author 刘宇 2018.02.26
	 * @param list
	 * @param ftyId
	 * @return
	 * @throws Exception
	 */
	JSONObject getVerprBySap(List<Map<String, Object>> list, String ftyId) throws Exception;
}
