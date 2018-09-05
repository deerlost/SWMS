package com.inossem.wms.dao.biz;


import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockInputItem;

public interface BizStockInputItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizStockInputItem record);

    int insertSelective(BizStockInputItem record);

    BizStockInputItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizStockInputItem record);

    int updateByPrimaryKey(BizStockInputItem record);
    
    List<BizStockInputItem> selectByStockInputId(Integer stockInputId);
    
    List<Map<String, Object>> selectAllocateInputItemByStockInputId(Integer stockInputId);
   
    List<BizStockInputItem> selectInputItemByStockInputId(Integer stockInputId);
    
    int deleteByStockInputId(Integer stockInputId);
    
    List<BizStockInputItem> selectByItemIds(List<Integer> itemIds);
    
    int deleteLogicallyByStockInputId(Integer stockInputId);
    
   List<Map<String, Object>> selectInputItemByStockInputID(Integer stockinputid);
   
   List<Map<String, Object>> selectStockInputItemByStockInputID(Integer stockinputid);
   
   List<Map<String, Object>> queryInputStockOutAndInOnPaging(Map<String, Object> param);
   
   List<Map<String, Object>>  getInputAmount(Map<String, Object> param);
   
   List<Map<String, Object>>   downloadTurnover(Map<String, Object> param);
   
   List<Map<String, Object>> queryFailMESOnPaging(Map<String, Object> param);
   
   List<Map<String, Object>> getAllInput(Map<String, Object> param);
   
}