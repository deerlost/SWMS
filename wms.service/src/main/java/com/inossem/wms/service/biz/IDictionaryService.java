package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

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

public interface IDictionaryService {

	void refreshCorp();

	/**
	 * 保存工厂时刷新工厂缓存数据
	 * 
	 * @author 刘宇 2018.04.08
	 */
	void refreshFactory();

	/**
	 * 保存库存地点时刷新库存地点缓存数据
	 * 
	 * @author 刘宇 2018.04.08
	 */
	void refreshLocation();

	/**
	 * 保存仓库时刷新仓库缓存数据
	 * 
	 * @author 刘宇 2018.04.08
	 */
	void refreshWarehouse();

	/**
	 * 保存移动类型时刷新移动类型缓存数据
	 * 
	 * @author 刘宇 2018.04.08
	 */
	void refreshMoveType();

	/**
	 * 保存计量单位时刷新计量单位缓存数据
	 * 
	 * @author 刘宇 2018.04.08
	 */
	void refreshUnit();

	/**
	 * 保存存储区时刷新存储区缓存数据
	 * 
	 * @author 刘宇 2018.04.08
	 */
	void refreshWarehouseArea();

	/**
	 * 保存分类表时刷新分类表缓存数据
	 * 
	 * @author 刘宇 2018.04.08
	 */
	void refreshBatchSpecClassify();

	Map<Integer, DicCorp> getCorpIdMap();

	Map<String, DicCorp> getCorpCodeMap();

	Map<Integer, DicFactoryVo> getFtyIdMap();

	Map<String, DicFactoryVo> getFtyCodeMap();

	Map<Integer, DicStockLocationVo> getLocationIdMap();

	Map<String, DicStockLocationVo> getLocationCodeMap();

	Map<Integer, DicWarehouse> getWarehouseIdMap();

	Map<String, DicWarehouse> getWarehouseCodeMap();

	Map<Integer, DicMoveType> getMoveTypeIdMap();

	Map<String, DicMoveType> getMoveTypeCodeMap();

	Map<Integer, DicUnit> getUnitIdMap();

	Map<String, DicUnit> getUnitCodeMap();

	String getCorpCodeByCorpId(int CorpId);

	int getCorpIdByCorpCode(String CorpCode);

	String getFtyCodeByFtyId(int ftyId);

	int getFtyIdByFtyCode(String ftyCode);

	String getLocCodeByLocId(int locationId);

	int getLocIdByFtyCodeAndLocCode(String ftyCode, String locationCode);

	String getFtyCodeAndLocCodeForLocMap(String ftyCode, String locationCode);

	String getWhCodeByWhId(int whId);

	int getWhIdByWhCode(String whCode);

	String getMoveTypeCodeByMoveTypeId(int moveTypeId);

	String getSpecStockByMoveTypeId(int moveTypeId);

	int getMoveTypeIdByMoveTypeCodeAndSpecStock(String moveType, String specStock);

	String getUnitCodeByUnitId(int unitId);

	int getUnitIdByUnitCode(String unitCode);

	Map<String, Integer> getMatMapByCodeList(List<String> list);

	Map<String, DicMaterial> getMatObjectMapByCodeList(List<String> list);

	Map<Integer, String> getMatMapByIdList(List<Integer> list);

	Map<Integer, DicMaterial> getMatObjectMapByIdList(List<Integer> list);

	int getMatIdByMatCode(String matCode);

	String getAreaCodeByAreaId(int areaId);

	int getAreaIdByWhCodeAndAreaCode(String whCode, String areaCode);

	Map<Integer, DicWarehouseArea> getAreaIdMap();

	Map<String, DicWarehouseArea> getAreaCodeMap();

	DicWarehouseArea getAreaByAreaId(int areaId);

	DicWarehousePosition getPositionByPositionId(int positionId);

	int getPositionIdByWhCodeAreaCodePositionCode(String whCode, String areaCode, String positionCode);

	Integer getMatGroupIdByCode(String matGroupCode);

	Map<Integer, DicBatchSpecClassify> getBatchSpecClassifyIdMap();

	Map<String, DicBatchSpecClassify> getBatchSpecClassifyCodeMap();

	Integer getBatchSpecClassifyIdByCode(String batchSpecClassifyCode);
	
	

}
