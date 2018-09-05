package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTakeHead;

public interface BizStockTakeHeadMapper {
    int deleteByPrimaryKey(Integer stockTakeId);

    int insert(BizStockTakeHead record);

    int insertSelective(BizStockTakeHead record);

    BizStockTakeHead selectByPrimaryKey(Integer stockTakeId);

    int updateByPrimaryKeySelective(BizStockTakeHead record);

    int updateByPrimaryKey(BizStockTakeHead record);

	List<Map<String, Object>> listOnPaging(Map<String, Object> paramMap);

	BizStockTakeHead selectInfoByPrimaryKey(Integer stockTakeId);
}