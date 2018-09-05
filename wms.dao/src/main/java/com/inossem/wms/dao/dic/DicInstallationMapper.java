package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicInstallation;

public interface DicInstallationMapper {
    int deleteByPrimaryKey(Integer installationId);

    int insert(DicInstallation dicInstallation);

    int insertSelective(DicInstallation dicInstallation);

    DicInstallation selectByPrimaryKey(Integer installationId);

    int updateByPrimaryKeySelective(DicInstallation dicInstallation);

    int updateByPrimaryKey(DicInstallation dicInstallation);

    List<DicInstallation> selectDicInstallationList(@Param("productLineId")Integer productLineId);
    
    List<DicInstallation> selectInstallationOnpaging(Map<String,Object> parameter);
    
   int  updateIsDeleteByKey(Integer productLineId);
}
