package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizStockInputMesItem;

public interface BizStockInputMesItemMapper {
    int deleteByPrimaryKey(Integer mesItemId);

    int insert(BizStockInputMesItem record);

    int insertSelective(BizStockInputMesItem record);

    BizStockInputMesItem selectByPrimaryKey(Integer mesItemId);

    int updateByPrimaryKeySelective(BizStockInputMesItem record);

    int updateByPrimaryKey(BizStockInputMesItem record);
}