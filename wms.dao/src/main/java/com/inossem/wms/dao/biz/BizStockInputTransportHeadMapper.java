package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.biz.BizStockInputTransportHead;

public interface BizStockInputTransportHeadMapper {

	int deleteByPrimaryKey(Integer stockTransportId);

	int insert(BizStockInputTransportHead record);

	int insertSelective(BizStockInputTransportHead record);

	BizStockInputTransportHead selectByPrimaryKey(Integer stockTransportId);

	int updateByPrimaryKeySelective(BizStockInputTransportHead record);

	int updateByPrimaryKey(BizStockInputTransportHead record);

	List<Map<String, Object>> getTransportInputListOnPaging(Map<String, Object> map);

	Map<String, Object> getTransportInputInfo(Map<String, Object> map);

	List<Map<String, Object>> getInputTransportItemListByID(Integer stock_input_id);

	Integer selectNowClassType();

	List<Map<String, Object>> select_stock_taskOnPaging(Map<String, Object> map);

	List<Map<String, Object>> getStockMatListByTaskId(Map<String, Object> map);

	int insertStockHead(Map<String, Object> headMap);

	void insertStockItem(Map<String, Object> headMap);

	List<Map<String, Object>> getMatListByID(Map<String, Object> map);

	String selectAreaId(Map<String, Object> paramMap);

	String selectPositonId(Map<String, Object> paramMap);

	Map<String, Object> getInputHeadMap(Map<String, Object> map);

	Map<String, Object> getItemMap(Map<String, Object> map);

	List<Map<String, Object>> getPackageList(Map<String, Object> itemMap);

	List<Map<String, Object>> selectStockTransportHeadOnPaging(Map<String, Object> param);

	Map<String, Object> selectStockTransportHeadbyID(@Param("stockTransportId") Integer stockTransportId);

	String selectTotalTransportNum(Map<String, Object> map);

	String getTransportSum(Map<String, Object> map);

	void updateTransportStatus(Map<String, Object> map);

	
	List<Map<String, Object>> selectMoveType();


	String getTaskDownId(String string);

	String getOtherId(String stock_task_id);

	List<Map<String, Object>> selectItemList(Integer stockTransportId);

	List<Map<String, Object>> selectItemListRefer(Integer stock_transport_id);

	List<String> selectItemStatus(String string);

	void updateItemStatusStatus(String other_id);

	void updateStatusByCode(String string);

	List<Map<String, Object>> select_stock_taskOnPagingUrgent(Map<String, Object> map);

	List<Map<String, Object>> getViItemInfo(Integer stock_input_id);

	List<Map<String, Object>> selectItemIdAndRid(Integer stock_input_id);

	void setBeforeStatus(Map<String, Object> map);

	void updateStockInputIdIsNull(Map<String,Object> param);

	void updateDownStaskStatus(Map<String, Object> param);
	
	List<Map<String, Object>> selectTransItemForPrint(Integer stockTransportId);

}