package com.inossem.wms.dao.biz;

import java.util.ArrayList;

import com.inossem.wms.model.biz.BufFactoryMatPrice;

public interface BufFactoryMatPriceMapper {
	int deleteByPrimaryKey(Integer priceId);

	int insert(BufFactoryMatPrice record);

	int insertSelective(BufFactoryMatPrice record);

	BufFactoryMatPrice selectByPrimaryKey(Integer priceId);

	int updateByPrimaryKeySelective(BufFactoryMatPrice record);

	int updateByPrimaryKey(BufFactoryMatPrice record);

	int insertOrUpdatePrices(ArrayList<BufFactoryMatPrice> list);
}