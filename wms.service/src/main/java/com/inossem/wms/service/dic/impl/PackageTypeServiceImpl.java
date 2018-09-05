package com.inossem.wms.service.dic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicCityMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicMaterialPackageTypeMapper;
import com.inossem.wms.dao.dic.DicPackClassificationMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicSupplierMapper;
import com.inossem.wms.dao.dic.DicSupplierPackageTypeMapper;
import com.inossem.wms.model.dic.DicCity;
import com.inossem.wms.model.dic.DicMaterialPackageType;
import com.inossem.wms.model.dic.DicPackClassification;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicSupplier;
import com.inossem.wms.model.dic.DicSupplierPackageType;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.vo.DicMaterialPackageTypeVo;
import com.inossem.wms.model.vo.DicPackageTypeViewVo;
import com.inossem.wms.model.vo.DicSupplierPackageTypeVo;
import com.inossem.wms.service.dic.IPackageTypeService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;

@Transactional
@Service("packageTypeService")
public class PackageTypeServiceImpl implements IPackageTypeService {
	@Autowired
	private DicPackageTypeMapper dicPackageTypeMapper;
	@Autowired
	private DicPackClassificationMapper dicPackClassificationMapper;
	@Autowired
	private DicMaterialPackageTypeMapper dicMaterialPackageTypeMapper;
	@Autowired
	private DicMaterialMapper dicMaterialMapper;
	@Autowired
	private DicSupplierMapper dicSupplierMapper;
	@Autowired
	private DicSupplierPackageTypeMapper dicSupplierPackageTypeMapper;
	@Autowired
	private DicCityMapper cityDao;

	@Override
	public List<DicPackageTypeViewVo> getPackageTypeList(Map<String, Object> parameter) {

		return dicPackageTypeMapper.selectPackageOnpaging(parameter);
	}

	@Override
	public List<DicPackClassification> selectAll() {

		return dicPackClassificationMapper.selectAll();
	}

	@Override
	public boolean saveOrUpdate(JSONObject json, DicUnit matDicUnit,DicUnit dicUnit) {
		boolean isAdd = false;
		@SuppressWarnings("unchecked")
		Map<String, Object> map = json;
		if (json.containsKey("package_type_id")) {
			
		} else {
			isAdd = true;
		}
		saveOrUpdatePackage(isAdd, map, matDicUnit,dicUnit);
		return true;
	}

	private DicPackageType saveOrUpdatePackage(boolean isAdd, Map<String, Object> map, DicUnit matDicUnit, DicUnit dicUnit) {
		DicPackageType dicPackageType = new DicPackageType();
          if(!isAdd) {
        	 dicPackageType.setPackageTypeId(UtilObject.getIntegerOrNull(map.get("package_type_id")));
          }
		dicPackageType.setPackageTypeCode((String) map.getOrDefault("package_type_code", ""));
		dicPackageType.setPackageTypeName((String) map.getOrDefault("package_type_name", ""));
		dicPackageType.setClassificatId(UtilObject.getIntOrZero(map.get("classificat_id")));
		dicPackageType.setIsFreeze(UtilObject.getByteOrNull(map.get("is_freeze")));
		dicPackageType.setPackageStandard(UtilObject.getBigDecimalOrZero(map.get("package_standard")));
		dicPackageType.setPackageStandardCh((String) map.getOrDefault("package_standard_ch", 0));
		dicPackageType.setSnUsed(UtilObject.getByteOrNull(map.get("sn_used")));
		dicPackageType.setIsDelete(EnumBoolean.FALSE.getValue());
		dicPackageType.setUnitId(dicUnit.getUnitId());
		dicPackageType.setMatUnitId(matDicUnit.getUnitId());
		dicPackageType.setSnSerialKey((String) map.getOrDefault("sn_serial_key", null));
		dicPackageType.setErpBatchKey((String) map.getOrDefault("erp_batch_key", null));
		if (isAdd) {
			dicPackageTypeMapper.insertSelective(dicPackageType);
		} else {
			dicPackageTypeMapper.updateByPrimaryKeySelective(dicPackageType);
		}
		return dicPackageType;
	}

	@Override
	public List<DicMaterialPackageTypeVo> getMatPackageTypeList(Map<String, Object> parameter) {
		return dicMaterialPackageTypeMapper.selectPackageTypeOnpaging(parameter);
	}

	@Override
	public int updateFreezeByPackIds(String ids) {
		String[] array = ids.split(",");
		Integer[] idsInt = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			idsInt[i] = Integer.parseInt(array[i]);
		}
		return dicPackageTypeMapper.updateFreezeByPackIds(idsInt);
	}

	public boolean savePackageTypeMaterial(JSONObject json, Map<String, Object> map) throws Exception {
		Integer count=0;
		@SuppressWarnings("unchecked")
		Map<String, Object> listMap = (Map<String, Object>) json;
		DicMaterialPackageType dicMaterialPackageType = new DicMaterialPackageType();
	
		//换算关系为包装类型里的最大重量		
		dicMaterialPackageType.setConversRelation(UtilObject.getBigDecimalOrZero(listMap.get("package_standard")));
		dicMaterialPackageType.setIsFreeze(UtilObject.getByteOrNull(listMap.get("is_freeze")));
		dicMaterialPackageType.setMatId(UtilObject.getIntegerOrNull(map.get("mat_id")));
		dicMaterialPackageType.setPackageTypeId(UtilObject.getIntegerOrNull(listMap.get("package_type_id")));
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("matId", dicMaterialPackageType.getMatId());
		param.put("packageTypeId", dicMaterialPackageType.getPackageTypeId());
		
		count= dicMaterialPackageTypeMapper.getMatPackageFreezeCount(param);
		if(count>0) {			
		throw new DuplicateKeyException("物料和包装类型关系已经存在！");				
		}		
		dicMaterialPackageTypeMapper.insertSelective(dicMaterialPackageType);
		return true;
	}

	/**
	 * 批量冻结包装物对应物料
	 */
	@Override
	public int updateFreezeById(JSONObject json) throws Exception {
		String ids = (String) json.get("dic_mat_package_type_id");
		String[] array = ids.split(",");
		Integer[] idsInt = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			idsInt[i] = Integer.parseInt(array[i]);
		}
		return dicMaterialPackageTypeMapper.updateFreezeById(idsInt);
	}

	@Override
	public Map<String, Object> getMatUnitByMatCode(String mat_code) {

		return dicMaterialMapper.getMatUnitByMatCode(mat_code);
	}

	
	
	@Override
	public DicSupplier getSupplierByCode(String supplierCode) {

		return dicSupplierMapper.getSupplierByCode(supplierCode);
	}
	
	

	@Override
	public boolean updateOrInsertSupplierPackageType(JSONObject json) throws Exception {
		boolean idAdd = true;
		@SuppressWarnings("unchecked")
		Map<String, Object> listMap = (Map<String, Object>) json;
		DicSupplierPackageType dicSupplierPackageType = new DicSupplierPackageType();
		if (listMap.containsKey("dic_supplier_package_type_id")) {
			idAdd = false;
		}
		DicSupplier sup = new DicSupplier();
		sup = dicSupplierMapper.getSupplierByCode(UtilObject.getStringOrEmpty(listMap.get("supplier_code")));
		if(sup == null) {
			sup = new DicSupplier();
			sup.setSupplierCode(UtilObject.getStringOrEmpty(listMap.get("supplier_code")));
			sup.setSupplierName(UtilObject.getStringOrEmpty(listMap.get("supplier_name")));
			Integer cityId = cityDao.selectByName(UtilObject.getStringOrEmpty(listMap.get("city_name")));
			if (cityId == null) {
				DicCity city = new DicCity();
				city.setCityName(UtilObject.getStringOrEmpty(listMap.get("city_name")));
				cityDao.insertSelective(city);
				cityId = city.getCityId();
			}
			sup.setCityId(cityId);
			sup.setIdDelete(EnumBoolean.FALSE.getValue());
			dicSupplierMapper.insertSelective(sup);
		}
		dicSupplierPackageType.setDicSupplierId(sup.getSupplierId());
		dicSupplierPackageType.setIsDelete(EnumBoolean.FALSE.getValue());
		dicSupplierPackageType.setIsFreeze(UtilObject.getByteOrNull(listMap.get("is_freeze")));
		dicSupplierPackageType.setPackageTypeId(UtilObject.getIntegerOrNull(listMap.get("package_type_id")));
		dicSupplierPackageType.setSupplierTimeStar((String) listMap.getOrDefault("supplier_time_star", ""));
		dicSupplierPackageType.setSupplierTimeEnd((String) listMap.getOrDefault("supplier_time_end", ""));
		if (idAdd) {
			dicSupplierPackageTypeMapper.insertSelective(dicSupplierPackageType);
		} else {
			dicSupplierPackageTypeMapper.updateDeleteByPramKey(UtilObject.getIntegerOrNull(listMap.get("dic_supplier_package_type_id")));
			dicSupplierPackageTypeMapper.insertSelective(dicSupplierPackageType);
		}
		return true;
	}

	@Override
	public int updateFreezeBySupTypeids(String ids) {
		String[] array = ids.split(",");
		Integer[] idsInt = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			idsInt[i] = Integer.parseInt(array[i]);
		}
		return dicSupplierPackageTypeMapper.updateFreezeByids(idsInt);
	}

	public List<DicSupplierPackageTypeVo> selectSupplierPackageTypeOnpaging(Map<String, Object> map) {
		return dicSupplierPackageTypeMapper.selectSupplierPackageTypeOnpaging(map);

	}
	
	public List<DicPackageType> getPackageTypeAll(){
	 return	dicPackageTypeMapper.getPackageTypeAll();		
	}

	@Override
	public Map<String, Object> searchMatCodeOrName(String search) {
		Map<String, Object> map =dicMaterialPackageTypeMapper.searchMatCodeOrName(search);
		List<Map<String,Object>> listPackageType=new ArrayList<Map<String,Object>>();
//		if(map!=null&&map.size()>0) {
//	    listPackageType=dicPackageTypeMapper.getPackageTypeByMatUnitId(UtilObject.getIntegerOrNull(map.get("unit_id")));
//		}
		//改动 2018/9/4 邵文说： 包装物不用和物料单位一置
		 listPackageType=dicPackageTypeMapper.getPackageTypeByMatUnitId(null);
		map.put("package_type_list", listPackageType);
		return map;
	}

	@Override
	public List<DicPackageType> getPackageTypeBySnKey(String snSerialKey) {
		return dicPackageTypeMapper.getPackageTypeBySnKey(snSerialKey);
	}

	@Override
	public Integer getCountByCode(String code) {
		return dicPackageTypeMapper.getCountByCode(code);
	}

}
