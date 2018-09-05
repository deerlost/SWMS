package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicSupplierPackageType;
import com.inossem.wms.model.vo.DicSupplierPackageTypeVo;

public interface DicSupplierPackageTypeMapper {
	int deleteByPrimaryKey(Integer dicSupplierPackageTypeId);

	int insert(DicSupplierPackageType record);

	int insertSelective(DicSupplierPackageType record);

	DicSupplierPackageType selectByPrimaryKey(Integer dicSupplierPackageTypeId);

	int updateByPrimaryKeySelective(DicSupplierPackageType record);

	int updateByPrimaryKey(DicSupplierPackageType record);

	int updateDeleteByPramKey(Integer id);
	
	int updateFreezeByids(Integer [] ids);
	
   List<DicSupplierPackageTypeVo> selectSupplierPackageTypeOnpaging(Map<String,Object> map);
	
	
}