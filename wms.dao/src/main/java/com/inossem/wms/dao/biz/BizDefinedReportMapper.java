package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.inossem.wms.model.biz.BizDefinedReport;
import com.inossem.wms.model.vo.BizDefinedReportVo;

public interface BizDefinedReportMapper {
    int deleteByPrimaryKey(Integer definedId);

    int insert(BizDefinedReport record);

    int insertSelective(BizDefinedReport record);

    BizDefinedReportVo selectByPrimaryKey(Integer definedId);

    int updateByPrimaryKeySelective(BizDefinedReport record);

    int updateByPrimaryKey(BizDefinedReport record);
    
    List<BizDefinedReportVo> getDefinedReportListOnPaging(Map<String,Object> param);
    
    List<Map<String,Object>> getDefinedField(@Param("type") Byte type,@Param("type1")String type1);
    
    int deleteRelReportUser(@Param("definedId")Integer definedId);
    
    int insertRelReportUser(Map<String,Object> param);
    
    int deleteRelReportField(@Param("definedId")Integer definedId);
    
    int insertRelReportField(Map<String,Object> param);
    
    List<Map<String,Object>> getDefinedFieldBySearch(@Param("type") Byte type,@Param("definedId") Integer definedId);
    
    List<Map<String,Object>> getDefinedFieldByQuery(@Param("type") Byte type,@Param("definedId") Integer definedId);
    
    @Select("${sql}")
    List<Map<String,Object>> selectDataListBySql(@Param("sql") String sql);
    
    @Select("${sql}")
    Integer selectCountBySql(@Param("sql") String sql);
}