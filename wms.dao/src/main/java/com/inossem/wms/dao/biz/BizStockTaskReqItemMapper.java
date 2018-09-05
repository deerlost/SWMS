package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;

public interface BizStockTaskReqItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizStockTaskReqItem record);

    int insertSelective(BizStockTaskReqItem record);

    BizStockTaskReqItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizStockTaskReqItem record);

    int updateByPrimaryKey(BizStockTaskReqItem record);
    
    List<BizStockTaskReqItemVo> getReqItemsByTaskReqId(Integer stockTaskReqId);
    
    Byte selectMinStatus(Integer stockTaskReqId);
    
    int insertReqItems(List<BizStockTaskReqItem> items);
    
    int updateItemQty(BizStockTaskReqItem record);
    
    int updateItemQtyByDelete(Integer stockTaskId);
    
    
    int deleteItemByStockInputItemIds(List<Integer> itemIds);
    
    int deleteReqItemsByTaskReqIdAndRid(BizStockTaskReqItem record);
    
    BizStockTaskReqItem selectByUniqueKey(Map<String, Object> map);
    
    List<BizStockTaskReqItem> selectItemById(Integer stockTaskReqId);
    List<BizStockTaskReqItem> selectItemByItemIds(List<Integer> itemIds);
    List<BizStockTaskReqItem> getBizStockTaskReqItemListByReferReipt(Map<String,Object> param);
    
    List<BizStockTaskReqItemVo> selectItemsByParams(Map<String, Object> map);
    
    //川维销售退库冲销生成下架请求明细
    //int insertReqItemForWriteoff (Map<String,Object> param);
    
    int updateDeleteByReferReceiptIdAndType(Map<String, Object> map);
    
}