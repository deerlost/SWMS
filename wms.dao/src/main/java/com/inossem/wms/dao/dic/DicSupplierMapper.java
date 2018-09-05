package com.inossem.wms.dao.dic;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicSupplier;

public interface DicSupplierMapper {
    int deleteByPrimaryKey(Integer supplierId);

    int insert(DicSupplier record);

    int insertSelective(DicSupplier record);

    DicSupplier selectByPrimaryKey(Integer supplierId);

    int updateByPrimaryKeySelective(DicSupplier record);

    int updateByPrimaryKey(DicSupplier record);
    
    List<DicSupplier>  selectAll();
    
    DicSupplier getSupplierByCode(String supplierCode);
    
    int synSuppleFromSap(DicSupplier record);
    
    List<DicSupplier> selectAllByPackageTypeId(@Param("packageTypeId") Integer packageTypeId);
}