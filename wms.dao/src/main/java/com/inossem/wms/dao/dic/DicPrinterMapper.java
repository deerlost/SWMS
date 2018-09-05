package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicPrinter;

public interface DicPrinterMapper {
    int deleteByPrimaryKey(Integer printerId);

    int insert(DicPrinter record);

    int insertSelective(DicPrinter record);

    DicPrinter selectByPrimaryKey(Integer printerId);

    int updateByPrimaryKeySelective(DicPrinter record);

    int updateByPrimaryKey(DicPrinter record);
    
    List<Map<String,Object>> selectPrinterOnpaging(Map<String, Object> param);
   
    int deletePrinterById(Integer printerId);
    
    DicPrinter getPrinterByUserAndType(@Param("userId")String userId,@Param("type")Byte type);
    
    int insertPrinterRelUser(@Param("userId")String userId,@Param("printerId")Integer printerId,@Param("type")Byte type);
    
    int deletePrinterRelUser(@Param("userId")String userId,@Param("type")Byte type);
}