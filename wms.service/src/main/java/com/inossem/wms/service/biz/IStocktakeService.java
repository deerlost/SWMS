package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;

import net.sf.json.JSONObject;

public interface IStocktakeService {

	/**
	 * 2.2 新增时查询盘点行项目信息列表
	 */
	List<Map<String, Object>> queryItemListOnPaging(JSONObject json);

	/**
	 * 2.3 盘点信息暂存/提交
	 */
	JSONObject save(CurrentUser cuser, JSONObject json) throws Exception;

	/**
	 * 2.4 查询盘点列表
	 */
	List<Map<String, Object>> listOnPaging(Map<String, Object> json);

	/**
	 * 2.5 查询盘点详情
	 */
	JSONObject info(Integer stockTakeId);

	/**
	 * 2.5 查询盘点详情(手持端)
	 */
	JSONObject infoForPDA(Integer stockTakeId);

	/**
	 * 2.6 删除盘点单
	 */
	void delete(Integer stockTakeId);

	/**
	 * 2.7 盘点数量暂存/完成
	 */
	void take(CurrentUser cuser, JSONObject json);

	/**
	 * 2.8 重盘
	 */
	void reset(Integer stock_take_id);

}
