package com.inossem.wms.dao.biz;

import java.util.List;

import com.inossem.wms.model.biz.BizAssetCard;

public interface BizAssetCardMapper {

    int insertSelective(BizAssetCard record);

    BizAssetCard selectByPrimaryKey(Integer assetCardId);

    int updateByPrimaryKeySelective(BizAssetCard record);
    
    List<BizAssetCard> selectAssetCardByOrderId(int stockOutputId);
}