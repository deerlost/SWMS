package com.inossem.wms.service.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inossem.wms.dao.dic.DicBatchSpecClassifyMapper;
import com.inossem.wms.dao.dic.DicCorpMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialGroupMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicMoveTypeMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.dic.DicWarehouseAreaMapper;
import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.dao.dic.DicWarehousePositionMapper;
import com.inossem.wms.model.dic.DicBatchSpecClassify;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.IDictionaryService;

@Service("dictionaryService")
public class DictionaryServiceImpl implements IDictionaryService {

	private static Logger logger = LoggerFactory.getLogger(DictionaryServiceImpl.class);

	private final String DOUBLE_CODE = "%s-%s";

	@Autowired
	private DicCorpMapper corpDao;

	@Autowired
	private DicFactoryMapper ftyDao;

	@Autowired
	private DicStockLocationMapper locationDao;

	@Autowired
	private DicWarehouseMapper warehouseDao;

	@Autowired
	private DicMoveTypeMapper moveTypeDao;

	@Autowired
	private DicUnitMapper unitDao;

	@Autowired
	private DicMaterialMapper materialDao;

	@Autowired
	private DicWarehouseAreaMapper warehouseAreaDao;

	// @Autowired
	// private DicWarehouseAreaMapper areaDao;

	@Autowired
	private DicMaterialGroupMapper materialGroupDao;

	@Autowired
	private DicWarehousePositionMapper positionDao;

	@Autowired
	private DicBatchSpecClassifyMapper batchSpecClassifyDao;

	private Map<Integer, DicCorp> corpIdMap;
	private Map<String, DicCorp> corpCodeMap;
	private Map<Integer, DicFactoryVo> ftyIdMap;
	private Map<String, DicFactoryVo> ftyCodeMap;
	private Map<Integer, DicStockLocationVo> locationIdMap;
	private Map<String, DicStockLocationVo> locationCodeMap;
	private Map<Integer, DicWarehouse> warehouseIdMap;
	private Map<String, DicWarehouse> warehouseCodeMap;
	private Map<Integer, DicMoveType> moveTypeIdMap;
	private Map<String, DicMoveType> moveTypeCodeMap;
	private Map<Integer, DicUnit> unitIdMap;
	private Map<String, DicUnit> unitCodeMap;
	private Map<Integer, DicWarehouseArea> areaIdMap;
	private Map<String, DicWarehouseArea> areaCodeMap;
	private Map<String, DicBatchSpecClassify> batchSpecClassifyCodeMap;
	private Map<Integer, DicBatchSpecClassify> batchSpecClassifyIdMap;

	private void initBatchSpecClassify() {
		try {
			Map<Integer, DicBatchSpecClassify> idMap;
			Map<String, DicBatchSpecClassify> codeMap;

			idMap = new HashMap<Integer, DicBatchSpecClassify>();
			codeMap = new HashMap<String, DicBatchSpecClassify>();

			List<DicBatchSpecClassify> batchSpecClassifyList = batchSpecClassifyDao.selectAll();
			for (DicBatchSpecClassify classify : batchSpecClassifyList) {
				idMap.put(classify.getBatchSpecClassifyId(), classify);
				codeMap.put(classify.getBatchSpecClassifyCode(), classify);
			}
			batchSpecClassifyIdMap = idMap;
			batchSpecClassifyCodeMap = codeMap;

			idMap = null;
			codeMap = null;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshBatchSpecClassify() {
		initBatchSpecClassify();
	}

	private void initCorp() {
		try {
			Map<Integer, DicCorp> idMap;
			Map<String, DicCorp> codeMap;

			idMap = new HashMap<Integer, DicCorp>();
			codeMap = new HashMap<String, DicCorp>();

			List<DicCorp> corpList = corpDao.selectAllCorpList();
			for (DicCorp corp : corpList) {
				idMap.put(corp.getCorpId(), corp);
				codeMap.put(corp.getCorpCode(), corp);
			}

			corpIdMap = idMap;
			corpCodeMap = codeMap;

			idMap = null;
			codeMap = null;

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshCorp() {
		initCorp();
	}

	private void initFty() {
		try {
			Map<Integer, DicFactoryVo> idMap;
			Map<String, DicFactoryVo> codeMap;

			idMap = new HashMap<Integer, DicFactoryVo>();
			codeMap = new HashMap<String, DicFactoryVo>();
			List<DicFactoryVo> ftyList = ftyDao.selectAllFactory();
			for (DicFactoryVo fty : ftyList) {
				idMap.put(fty.getFtyId(), fty);
				codeMap.put(fty.getFtyCode(), fty);
			}
			ftyIdMap = idMap;
			ftyCodeMap = codeMap;

			idMap = null;
			codeMap = null;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshFactory() {
		initFty();
	}

	private void initLocation() {
		try {
			Map<Integer, DicStockLocationVo> idMap;
			Map<String, DicStockLocationVo> codeMap;

			idMap = new HashMap<Integer, DicStockLocationVo>();
			codeMap = new HashMap<String, DicStockLocationVo>();
			List<DicStockLocationVo> locationList = locationDao.selectAll();
			for (DicStockLocationVo loc : locationList) {
				idMap.put(loc.getLocationId(), loc);
				codeMap.put(String.format(DOUBLE_CODE, loc.getFtyCode(), loc.getLocationCode()), loc);
			}
			locationIdMap = idMap;
			locationCodeMap = codeMap;

			idMap = null;
			codeMap = null;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshLocation() {
		initLocation();
	}

	private void initWarehouse() {
		try {
			Map<Integer, DicWarehouse> idMap;
			Map<String, DicWarehouse> codeMap;

			idMap = new HashMap<Integer, DicWarehouse>();
			codeMap = new HashMap<String, DicWarehouse>();
			List<DicWarehouse> whList = warehouseDao.selectAll();
			for (DicWarehouse wh : whList) {
				idMap.put(wh.getWhId(), wh);
				codeMap.put(wh.getWhCode(), wh);
			}
			warehouseIdMap = idMap;
			warehouseCodeMap = codeMap;

			idMap = null;
			codeMap = null;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshWarehouse() {
		initWarehouse();
	}

	private void initMoveType() {
		try {
			Map<Integer, DicMoveType> idMap;
			Map<String, DicMoveType> codeMap;

			idMap = new HashMap<Integer, DicMoveType>();
			codeMap = new HashMap<String, DicMoveType>();
			List<DicMoveType> moveTypeList = moveTypeDao.selectAll();
			for (DicMoveType moveType : moveTypeList) {
				idMap.put(moveType.getMoveTypeId(), moveType);
				codeMap.put(String.format(DOUBLE_CODE, moveType.getMoveTypeCode(), moveType.getSpecStock()), moveType);
			}
			moveTypeIdMap = idMap;
			moveTypeCodeMap = codeMap;

			idMap = null;
			codeMap = null;

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshMoveType() {
		initMoveType();
	}

	private void initUnit() {
		try {
			Map<Integer, DicUnit> idMap;
			Map<String, DicUnit> codeMap;

			idMap = new HashMap<Integer, DicUnit>();
			codeMap = new HashMap<String, DicUnit>();
			List<DicUnit> unitList = unitDao.selectAll();
			for (DicUnit unit : unitList) {
				idMap.put(unit.getUnitId(), unit);
				codeMap.put(unit.getUnitCode(), unit);
			}
			unitIdMap = idMap;
			unitCodeMap = codeMap;

			idMap = null;
			codeMap = null;

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshUnit() {
		initUnit();
	}

	private void initWarehouseArea() {
		try {
			Map<Integer, DicWarehouseArea> idMap;
			Map<String, DicWarehouseArea> codeMap;

			idMap = new HashMap<Integer, DicWarehouseArea>();
			codeMap = new HashMap<String, DicWarehouseArea>();
			List<DicWarehouseArea> whAreaList = warehouseAreaDao.selectAll();
			for (DicWarehouseArea wh : whAreaList) {
				idMap.put(wh.getAreaId(), wh);
				codeMap.put(String.format(this.DOUBLE_CODE, wh.getWhCode(), wh.getAreaCode()), wh);
			}
			areaIdMap = idMap;
			areaCodeMap = codeMap;

			idMap = null;
			codeMap = null;

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void refreshWarehouseArea() {
		initWarehouseArea();
	}

	@Override
	public Map<Integer, DicCorp> getCorpIdMap() {
		if (corpIdMap == null || corpCodeMap == null) {
			initCorp();
		}
		return corpIdMap;
	}

	@Override
	public Map<String, DicCorp> getCorpCodeMap() {
		if (corpIdMap == null || corpCodeMap == null) {
			initCorp();
		}
		return corpCodeMap;
	}

	@Override
	public Map<Integer, DicFactoryVo> getFtyIdMap() {
		if (ftyIdMap == null || ftyCodeMap == null) {
			initFty();
		}
		return ftyIdMap;
	}

	@Override
	public Map<String, DicFactoryVo> getFtyCodeMap() {
		if (ftyIdMap == null || ftyCodeMap == null) {
			initFty();
		}
		return ftyCodeMap;
	}

	@Override
	public Map<Integer, DicStockLocationVo> getLocationIdMap() {
		if (locationIdMap == null || locationCodeMap == null) {
			initLocation();
		}
		return locationIdMap;
	}

	@Override
	public Map<String, DicStockLocationVo> getLocationCodeMap() {

		if (locationIdMap == null || locationCodeMap == null) {
			initLocation();
		}
		return locationCodeMap;
	}

	@Override
	public Map<Integer, DicWarehouse> getWarehouseIdMap() {
		if (warehouseIdMap == null || warehouseCodeMap == null) {
			initWarehouse();
		}
		return warehouseIdMap;
	}

	@Override
	public Map<String, DicWarehouse> getWarehouseCodeMap() {
		if (warehouseIdMap == null || warehouseCodeMap == null) {
			initWarehouse();
		}
		return warehouseCodeMap;
	}

	@Override
	public Map<Integer, DicMoveType> getMoveTypeIdMap() {
		if (moveTypeIdMap == null || moveTypeCodeMap == null) {
			initMoveType();
		}
		return moveTypeIdMap;
	}

	@Override
	public Map<String, DicMoveType> getMoveTypeCodeMap() {
		if (moveTypeIdMap == null || moveTypeCodeMap == null) {
			initMoveType();
		}
		return moveTypeCodeMap;
	}

	@Override
	public Map<Integer, DicUnit> getUnitIdMap() {
		if (unitIdMap == null || unitCodeMap == null) {
			initUnit();
		}
		return unitIdMap;
	}

	@Override
	public Map<String, DicUnit> getUnitCodeMap() {
		if (unitIdMap == null || unitCodeMap == null) {
			initUnit();
		}
		return unitCodeMap;
	}

	@Override
	public String getCorpCodeByCorpId(int CorpId) {
		DicCorp corp = this.getCorpIdMap().get(CorpId);
		if (corp == null) {
			return null;
		} else {
			return corp.getCorpCode();
		}
	}

	@Override
	public int getCorpIdByCorpCode(String CorpCode) {
		DicCorp corp = this.getCorpCodeMap().get(CorpCode);
		if (corp == null) {
			return 0;
		} else {
			return corp.getCorpId();
		}
	}

	@Override
	public String getFtyCodeByFtyId(int ftyId) {
		DicFactoryVo fty = this.getFtyIdMap().get(ftyId);
		if (fty == null) {
			return null;
		} else {
			return fty.getFtyCode();
		}
	}

	@Override
	public int getFtyIdByFtyCode(String ftyCode) {
		DicFactoryVo fty = this.getFtyCodeMap().get(ftyCode);
		if (fty == null) {
			return 0;
		} else {
			return fty.getFtyId();
		}
	}

	@Override
	public String getLocCodeByLocId(int locationId) {
		DicStockLocationVo loc = this.getLocationIdMap().get(locationId);
		if (loc == null) {
			return null;
		} else {
			return loc.getLocationCode();
		}
	}

	@Override
	public int getLocIdByFtyCodeAndLocCode(String ftyCode, String locationCode) {
		DicStockLocationVo loc = this.getLocationCodeMap().get(String.format(DOUBLE_CODE, ftyCode, locationCode));
		if (loc == null) {
			return 0;
		} else {
			return loc.getLocationId();
		}
	}

	@Override
	public String getFtyCodeAndLocCodeForLocMap(String ftyCode, String locationCode) {
		return String.format(DOUBLE_CODE, ftyCode, locationCode);
	}

	@Override
	public String getWhCodeByWhId(int whId) {
		DicWarehouse wh = this.getWarehouseIdMap().get(whId);
		if (wh == null) {
			return null;
		} else {
			return wh.getWhCode();
		}
	}

	@Override
	public int getWhIdByWhCode(String whCode) {
		DicWarehouse wh = this.getWarehouseCodeMap().get(whCode);
		if (wh == null) {
			return 0;
		} else {
			return wh.getWhId();
		}
	}

	@Override
	public String getMoveTypeCodeByMoveTypeId(int moveTypeId) {
		DicMoveType fty = this.getMoveTypeIdMap().get(moveTypeId);
		if (fty == null) {
			return null;
		} else {
			return fty.getMoveTypeCode();
		}
	}

	@Override
	public String getSpecStockByMoveTypeId(int moveTypeId) {
		DicMoveType fty = this.getMoveTypeIdMap().get(moveTypeId);
		if (fty == null) {
			return null;
		} else {
			return fty.getSpecStock();
		}
	}

	@Override
	public int getMoveTypeIdByMoveTypeCodeAndSpecStock(String moveType, String specStock) {
		DicMoveType fty = this.getMoveTypeCodeMap().get(String.format(DOUBLE_CODE, moveType, specStock));
		if (fty == null) {
			return 0;
		} else {
			return fty.getMoveTypeId();
		}
	}

	@Override
	public String getUnitCodeByUnitId(int unitId) {
		DicUnit unit = this.getUnitIdMap().get(unitId);
		if (unit == null) {
			return null;
		} else {
			return unit.getUnitCode();
		}
	}

	@Override
	public int getUnitIdByUnitCode(String unitCode) {
		DicUnit unit = this.getUnitCodeMap().get(unitCode);
		if (unit == null) {
			return 0;
		} else {
			return unit.getUnitId();
		}
	}

	@Override
	public Map<String, Integer> getMatMapByCodeList(List<String> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		StringBuffer sqlSB = new StringBuffer();
		if (list == null || list.size() == 0) {
			return map;
		} else if (list.size() == 1) {
			sqlSB.append("SELECT mat_id matId,mat_code matCode FROM dic_material WHERE mat_code = '")
					.append(list.get(0).replace("'", "''")).append("';");
		} else {
			sqlSB.append("SELECT b.mat_id matId,b.mat_code matCode FROM (SELECT '")
					.append(list.get(0).replace("'", "''")).append("' mat_code");
			for (int i = 1; i < list.size(); i++) {
				sqlSB.append(" UNION ALL SELECT '").append(list.get(i).replace("'", "''")).append("'");
			}
			sqlSB.append(") a INNER JOIN dic_material b ON a.mat_code = b.mat_code;");
		}

		List<DicMaterial> matList = materialDao.selectIdAndCodeBySql(sqlSB.toString());
		for (DicMaterial dicMaterial : matList) {
			map.put(dicMaterial.getMatCode(), dicMaterial.getMatId());
		}
		return map;
	}

	@Override
	public Map<String, DicMaterial> getMatObjectMapByCodeList(List<String> list) {
		Map<String, DicMaterial> map = new HashMap<String, DicMaterial>();
		StringBuffer sqlSB = new StringBuffer();
		if (list == null || list.size() == 0) {
			return map;
		} else if (list.size() == 1) {
			sqlSB.append(
					"SELECT mat_id matId,mat_code matCode,mat_name matName,unit_id unitId FROM dic_material WHERE mat_code = '")
					.append(list.get(0).replace("'", "''")).append("';");
		} else {
			sqlSB.append("SELECT b.mat_id matId,b.mat_code matCode,b.mat_name matName,b.unit_id unitId FROM (SELECT '")
					.append(list.get(0).replace("'", "''")).append("' mat_code");
			for (int i = 1; i < list.size(); i++) {
				sqlSB.append(" UNION ALL SELECT '").append(list.get(i).replace("'", "''")).append("'");
			}
			sqlSB.append(") a INNER JOIN dic_material b ON a.mat_code = b.mat_code;");
		}

		List<DicMaterial> matList = materialDao.selectIdAndCodeBySql(sqlSB.toString());
		for (DicMaterial dicMaterial : matList) {
			map.put(dicMaterial.getMatCode(), dicMaterial);
		}
		return map;
	}

	@Override
	public Map<Integer, String> getMatMapByIdList(List<Integer> list) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		StringBuffer sqlSB = new StringBuffer();
		if (list == null || list.size() == 0) {
			return map;
		} else if (list.size() == 1) {
			sqlSB.append("SELECT mat_id matId,mat_code matCode FROM dic_material WHERE mat_id = '").append(list.get(0))
					.append("';");
		} else {
			sqlSB.append("SELECT b.mat_id matId,b.mat_code matCode FROM (SELECT '").append(list.get(0))
					.append("' mat_id");
			for (int i = 1; i < list.size(); i++) {
				sqlSB.append(" UNION ALL SELECT '").append(list.get(i)).append("'");
			}
			sqlSB.append(") a INNER JOIN dic_material b ON a.mat_id = b.mat_id;");
		}

		List<DicMaterial> matList = materialDao.selectIdAndCodeBySql(sqlSB.toString());
		for (DicMaterial dicMaterial : matList) {
			map.put(dicMaterial.getMatId(), dicMaterial.getMatCode());
		}
		return map;
	}

	@Override
	public Map<Integer, DicMaterial> getMatObjectMapByIdList(List<Integer> list) {
		Map<Integer, DicMaterial> map = new HashMap<Integer, DicMaterial>();
		StringBuffer sqlSB = new StringBuffer();
		if (list == null || list.size() == 0) {
			return map;
		} else if (list.size() == 1) {
			sqlSB.append(
					"SELECT mat_id matId,mat_code matCode,mat_name matName,unit_id unitId FROM dic_material WHERE mat_id = '")
					.append(list.get(0)).append("';");
		} else {
			sqlSB.append("SELECT b.mat_id matId,b.mat_code matCode,b.mat_name matName,b.unit_id unitId FROM (SELECT '")
					.append(list.get(0)).append("' mat_id");
			for (int i = 1; i < list.size(); i++) {
				sqlSB.append(" UNION ALL SELECT '").append(list.get(i)).append("'");
			}
			sqlSB.append(") a INNER JOIN dic_material b ON a.mat_id = b.mat_id;");
		}

		List<DicMaterial> matList = materialDao.selectIdAndCodeBySql(sqlSB.toString());
		for (DicMaterial dicMaterial : matList) {
			map.put(dicMaterial.getMatId(), dicMaterial);
		}
		return map;
	}

	@Override
	public int getMatIdByMatCode(String matCode) {
		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append("SELECT mat_id matId,mat_code matCode FROM dic_material WHERE mat_code = '")
				.append(matCode.replace("'", "''")).append("';");

		List<DicMaterial> matList = materialDao.selectIdAndCodeBySql(sqlSB.toString());
		if (matList == null || matList.size() == 0) {
			return 0;
		} else {
			return matList.get(0).getMatId();
		}
	}

	@Override
	public Map<Integer, DicWarehouseArea> getAreaIdMap() {
		if (areaCodeMap == null || areaIdMap == null) {
			initWarehouseArea();
		}
		return areaIdMap;
	}

	@Override
	public Map<String, DicWarehouseArea> getAreaCodeMap() {
		if (areaCodeMap == null || areaIdMap == null) {
			initWarehouseArea();
		}
		return areaCodeMap;
	}

	@Override
	public String getAreaCodeByAreaId(int areaId) {
		DicWarehouseArea area = this.getAreaIdMap().get(areaId);
		if (area == null) {
			return null;
		} else {
			return area.getAreaCode();
		}
	}

	@Override
	public int getAreaIdByWhCodeAndAreaCode(String whCode, String areaCode) {
		DicWarehouseArea area = this.getAreaCodeMap().get(String.format(DOUBLE_CODE, whCode, areaCode));
		if (area == null) {
			return 0;
		} else {
			return area.getAreaId();
		}
	}

	@Override
	public DicWarehouseArea getAreaByAreaId(int areaId) {
		return warehouseAreaDao.selectByPrimaryKey(areaId);
	}

	@Override
	public DicWarehousePosition getPositionByPositionId(int positionId) {

		return positionDao.selectByPrimaryKey(positionId);
	}

	@Override
	public int getPositionIdByWhCodeAreaCodePositionCode(String whCode, String areaCode, String positionCode) {
		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append(
				"SELECT wh.wh_id whId,a.area_id areaId,p.position_id positionId FROM dic_warehouse wh INNER JOIN dic_warehouse_area a ON wh.wh_id = a.wh_id AND wh.wh_code = '")
				.append(whCode.replace("'", "''")).append("' AND a.area_code = '").append(areaCode.replace("'", "''"))
				.append("' INNER JOIN dic_warehouse_position p ON a.area_id = p.area_id AND p.position_code = '")
				.append(positionCode.replace("'", "''")).append("';");

		List<DicWarehousePosition> positionList = positionDao.selectIdAndCodeBySql(sqlSB.toString());
		if (positionList == null || positionList.size() == 0) {
			return 0;
		} else {
			return positionList.get(0).getPositionId();
		}
	}

	@Override
	public Integer getMatGroupIdByCode(String matGroupCode) {

		return materialGroupDao.selectIdByCode(matGroupCode);
	}

	@Override
	public Integer getBatchSpecClassifyIdByCode(String batchSpecClassifyCode) {
		DicBatchSpecClassify classify = this.getBatchSpecClassifyCodeMap().get(batchSpecClassifyCode);
		if (classify == null) {
			return 0;
		} else {
			return classify.getBatchSpecClassifyId();
		}

	}

	@Override
	public Map<Integer, DicBatchSpecClassify> getBatchSpecClassifyIdMap() {
		if (batchSpecClassifyIdMap == null || batchSpecClassifyCodeMap == null) {
			initBatchSpecClassify();
		}
		return batchSpecClassifyIdMap;
	}

	@Override
	public Map<String, DicBatchSpecClassify> getBatchSpecClassifyCodeMap() {
		if (batchSpecClassifyIdMap == null || batchSpecClassifyCodeMap == null) {
			initBatchSpecClassify();
		}
		return batchSpecClassifyCodeMap;
	}

}
