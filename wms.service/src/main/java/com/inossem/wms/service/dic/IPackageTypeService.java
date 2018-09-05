package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicPackClassification;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicSupplier;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.vo.DicMaterialPackageTypeVo;
import com.inossem.wms.model.vo.DicPackageTypeViewVo;
import com.inossem.wms.model.vo.DicSupplierPackageTypeVo;

import net.sf.json.JSONObject;

public interface IPackageTypeService {

    List<DicPackageTypeViewVo>	getPackageTypeList(Map<String,Object> parameter);
	
    List<DicPackClassification> selectAll();
    
    boolean saveOrUpdate(JSONObject json,DicUnit matDicUnit,DicUnit dicUnit);
    
    List<DicMaterialPackageTypeVo>   getMatPackageTypeList (Map<String,Object> parameter);
	 
    public boolean 	savePackageTypeMaterial(JSONObject json,Map<String,Object> map) throws Exception;
    
    int updateFreezeById(JSONObject json) throws Exception;
    
    int updateFreezeByPackIds(String ids);
    
    Map<String,Object>  getMatUnitByMatCode(String mat_code);
    
    DicSupplier getSupplierByCode(String supplierCode);
    
    boolean updateOrInsertSupplierPackageType (JSONObject json) throws Exception;
    
    int updateFreezeBySupTypeids(String ids);
   
    List<DicSupplierPackageTypeVo> selectSupplierPackageTypeOnpaging(Map<String,Object> map);
   
    List<DicPackageType> getPackageTypeAll();
   
    Map<String,Object> searchMatCodeOrName(String search);
   
    List<DicPackageType>   getPackageTypeBySnKey(String snSerialKey);
   
    Integer getCountByCode(String code);
    
}
