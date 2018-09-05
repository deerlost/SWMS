package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskHeadCw;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;

public interface BizStockTaskHeadCwMapper {
    int deleteByPrimaryKey(Integer stockTaskId);

    int insert(BizStockTaskHeadCw record);

    int insertSelective(BizStockTaskHeadCw record);

    BizStockTaskHeadCw selectByPrimaryKey(Integer stockTaskId);

    int updateByPrimaryKeySelective(BizStockTaskHeadCw record);

    int updateByPrimaryKey(BizStockTaskHeadCw record);
    
    //川维
    List<Map<String, Object> > getBizStockTaskHeadListForCWOnPaging (Map<String, Object> map) ;
    
    List<BizStockTaskHeadVo> getBizStockTaskHeadListInReq(BizStockTaskHeadVo record);
    
    List<BizStockTaskHeadVo> getBizStockTaskHeadListOnPaging (BizStockTaskHeadVo record) ;
    
    //获取销售出库下架作业单
    List<Map<String,Object>> selectTaskListBySale(Map<String,Object> param);

    //获取销售出库下架作业单 不分组
    List<Map<String,Object>> selectTaskListBySaleNoGroup(Map<String,Object> param);
    
    //获取出库单下架作业单
    List<Map<String,Object>> selectTaskListByOutput(Map<String,Object> param);
    
    /**
     * 更改作业单状态
     * @param map
     * @return
     */
    int updateStatusByReferReceiptIdAndType(Map<String, Object> map);
    
    int updateDeleteByReferReceiptIdAndType(Map<String, Object> map);
    
    Map<String, Object> selectByTaskIdForPrint(int stockTaskId);
    
}