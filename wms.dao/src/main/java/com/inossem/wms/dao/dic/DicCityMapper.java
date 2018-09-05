package com.inossem.wms.dao.dic;

import com.inossem.wms.model.dic.DicCity;

public interface DicCityMapper {
    int deleteByPrimaryKey(Integer cityId);

    int insert(DicCity record);

    int insertSelective(DicCity record);

    DicCity selectByPrimaryKey(Integer cityId);
    
    Integer selectByName(String cityName);

    int updateByPrimaryKeySelective(DicCity record);

    int updateByPrimaryKey(DicCity record);
}