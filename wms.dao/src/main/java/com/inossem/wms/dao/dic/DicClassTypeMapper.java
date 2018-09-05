package com.inossem.wms.dao.dic;

import com.inossem.wms.model.dic.DicClassType;
import java.util.List;
import java.util.Map;

public interface DicClassTypeMapper {
    int deleteByPrimaryKey(Integer classTypeId);

    int insert(DicClassType dicClassType);

    int insertSelective(DicClassType dicClassType);

    DicClassType selectByPrimaryKey(Integer classTypeId);

    int updateByPrimaryKeySelective(DicClassType dicClassType);

    int updateByPrimaryKey(DicClassType dicClassType);

    List<Map<String,Object>> selectAll();
    
   List<Map<String,Object>> selectClassTypeOnpaging(Map<String,Object> parameter);
   
    int updateIsDeleteByKey(Integer classTypeId);
    
    Map<String,Object>  selectCurrentClassName(Integer classTypeId);

}
