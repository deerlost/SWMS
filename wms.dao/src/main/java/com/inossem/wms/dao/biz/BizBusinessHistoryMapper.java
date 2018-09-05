package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.vo.BizBusinessHistoryVo;

public interface BizBusinessHistoryMapper {
	
    int insertSelective(BizBusinessHistory record);

    BizBusinessHistory selectByPrimaryKey(Integer businessHistoryId);

    //int updateByPrimaryKeySelective(BizBusinessHistory record);
    
    int countByPackageIds(List<Integer> list);
    
    BizBusinessHistory selectNewestByParams(Map<String, Object> map);
    
    List<BizBusinessHistoryVo> selectHistoryListByDispatcherOnPaging(Map<String, Object> param);
    
    Map<String,Object> getPackageExpressInfo(@Param("packageId") Integer packageId);
}