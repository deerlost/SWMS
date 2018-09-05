package com.inossem.wms.dao.biz;

import java.util.List;

import com.inossem.wms.model.biz.BizStockInputPackageItem;

public interface BizStockInputPackageItemMapper {
    int deleteByPrimaryKey(Integer packageTypeItemId);

    int insert(BizStockInputPackageItem record);

    int insertSelective(BizStockInputPackageItem record);

    BizStockInputPackageItem selectByPrimaryKey(Integer packageTypeItemId);

    int updateByPrimaryKeySelective(BizStockInputPackageItem record);

    int updateByPrimaryKey(BizStockInputPackageItem record);

    List<BizStockInputPackageItem> selectByInputId(Integer stockInputId);
    
    List<BizStockInputPackageItem> selectByItemId(Integer stockInputItemId);
}