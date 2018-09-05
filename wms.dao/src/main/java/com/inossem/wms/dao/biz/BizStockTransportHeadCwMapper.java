package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTransportHeadCw;
import com.inossem.wms.model.dic.DicFactory;

public interface BizStockTransportHeadCwMapper {
    int deleteByPrimaryKey(Integer stockTransportId);
    List<BizStockTransportHeadCw> selectByParamsOnpaging(Map<String, Object> map);
    int insert(BizStockTransportHeadCw record);

    int insertSelective(BizStockTransportHeadCw record);

    BizStockTransportHeadCw selectByPrimaryKey(Integer stockTransportId);

    int updateByPrimaryKeySelective(BizStockTransportHeadCw record);

    int updateByPrimaryKey(BizStockTransportHeadCw record);
    
    List<Map<String, Object>> selectMoveType();

	List<Map<String, Object>> selectDumpListOnPaging(Map<String, Object> param);

	
	List<Map<String, Object>> getClassMap();
	
	void InsertDump(BizStockTransportHeadCw stHead);
	
	
	Map<String, Object> selectHeadById(Integer stockTransportId);
	
	void InsertStockTransportHeadCw(BizStockTransportHeadCw stHeadCw);
	
	int selectReceipt(Integer moveTypeId);
	
	Map<String, Object> queryStockTransForPrint(Integer stockTransportId);
}