package com.inossem.wms.dao.buf;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.buf.BufUserLocationMatStock;

public interface BufUserLocationMatStockMapper {
    int deleteByPrimaryKey(Integer buffId);

    int insert(BufUserLocationMatStock record);

    int insertSelective(BufUserLocationMatStock record);

    BufUserLocationMatStock selectByPrimaryKey(Integer buffId);

    int updateByPrimaryKeySelective(BufUserLocationMatStock record);

    int updateByPrimaryKey(BufUserLocationMatStock record);
    
    int insertStartQty(Map<String, Object> map);
    int insertInputQty(Map<String, Object> map);
    int insertOutputQty(Map<String, Object> map);
   
    
    List<Map<String, Object>> selectByParamOnPaging(Map<String, Object> map);
    
    int deleteByUserId(String userId);
}