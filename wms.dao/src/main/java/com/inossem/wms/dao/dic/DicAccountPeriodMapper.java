package com.inossem.wms.dao.dic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicAccountPeriod;
import com.inossem.wms.model.vo.DicAccountPeriodVo;

public interface DicAccountPeriodMapper {
    int deleteByPrimaryKey(Integer periodId);

    int insert(DicAccountPeriod record);

    int insertSelective(DicAccountPeriod record);

    DicAccountPeriod selectByPrimaryKey(Integer periodId);

    int updateByPrimaryKeySelective(DicAccountPeriod record);

    int updateByPrimaryKey(DicAccountPeriod record);
    
    List<DicAccountPeriodVo> selectPostDateByFtyId(HashMap<String, Object> map);

    List<DicAccountPeriodVo> selectByConditionOnPaging(Map<String, Object> map);
    
    int insertAccountPeriods(List<DicAccountPeriod> apList);
    
    int updateAccountPeriods(List<DicAccountPeriod> apList);
    
    int countAccountPeriods(Map<String, Object> map);
    
    int deleteByIds(List<Integer> ids);
    
}