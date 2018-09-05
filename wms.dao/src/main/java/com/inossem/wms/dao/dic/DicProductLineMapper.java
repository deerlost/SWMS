package com.inossem.wms.dao.dic;

import com.inossem.wms.model.dic.DicProductLine;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DicProductLineMapper {
    int deleteByPrimaryKey(Integer productLineId);

    int insert(DicProductLine dicProductLine);

    int insertSelective(DicProductLine dicProductLine);

    DicProductLine selectByPrimaryKey(Integer productLineId);

    int updateByPrimaryKeySelective(DicProductLine dicProductLine);

    int updateByPrimaryKey(DicProductLine dicProductLine);

    List<DicProductLine> selectDicProductLineList(@Param("userId")String userId);
    
    List<DicProductLine>  selectProductLineOnpaging(Map<String,Object> parameter);
    
    int updateIsDeleteByKey(Integer productLineId);
    
    List<DicProductLine> selectProductInstallationList();
    
    int  updateRelUserProductLine( Map<String,Object> param);
    
    int insertRelUserProductLine(Map<String,Object> param);
    
    List<DicProductLine> selectAllProductLine();
    
    Map<String,Object>  getRelUserProductLine(String userId);
    
}
