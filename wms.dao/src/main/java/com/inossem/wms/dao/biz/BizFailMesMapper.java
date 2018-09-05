package com.inossem.wms.dao.biz;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.biz.BizFailMes;

public interface BizFailMesMapper {
    int deleteByPrimaryKey(Integer failId);

    int insert(BizFailMes record);

    int insertSelective(BizFailMes record);

    BizFailMes selectByPrimaryKey(Integer failId);

    int updateByPrimaryKeySelective(BizFailMes record);

    int updateByPrimaryKeyWithBLOBs(BizFailMes record);

    int updateByPrimaryKey(BizFailMes record);
    
    int deleteDateByDays(@Param("days") Integer days);
}