package com.inossem.wms.service.dic.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicWarehousePositionMapper;
import com.inossem.wms.exception.ExcelCellTypeException;
import com.inossem.wms.model.ExcelCellType;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.dic.IPositionService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("positionService")
public class PositionServiceImpl implements IPositionService {
	@Autowired
	private DicWarehousePositionMapper dicWarehousePositionDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Override
	public List<Map<String, Object>> listPositionOnPaging(Map<String, Object> map) throws Exception {	
		
		return dicWarehousePositionDao.selectWarehousePositionOnPaging(map);
	}

	@Override
	public JSONArray listWarsehouseAndArea(List<DicWarehouse> warehouses, List<DicWarehouseArea> areas)
			throws Exception {
		JSONArray body = new JSONArray();
		JSONArray areasJosn = null;
		for (DicWarehouse warehouse : warehouses) {
			JSONObject warehouseJSon = new JSONObject();
			warehouseJSon.put("wh_id", warehouse.getWhId());// 仓库id
			warehouseJSon.put("wh_code", warehouse.getWhCode());// 仓库编码
			warehouseJSon.put("wh_name", warehouse.getWhName());// 仓库描述
			areasJosn = new JSONArray();
			for (DicWarehouseArea area : areas) {
				if (warehouse.getWhId().intValue() == area.getWhId().intValue()) {
					JSONObject areaJSon = new JSONObject();
					areaJSon.put("area_id", area.getAreaId());// 储存区id
					areaJSon.put("area_code", area.getAreaCode());// 储存区编码
					areaJSon.put("area_name", area.getAreaName());// 储存区描述
					areaJSon.put("wh_id", warehouse.getWhId());// 仓库id
					areasJosn.add(areaJSon);
				}
			}
			warehouseJSon.put("area_info", areasJosn);
			body.add(warehouseJSon);
		}
		return body;
	}

	@Override
	public Map<String, Object> addOrUpdatePosition(int errorCode, String errorString, boolean errorStatus,
			String positionId, String whId, String areaId, String positionCode, String freezeInput, String freezeOutput,String storageTypeId)
			throws Exception {
		// String freezeId,
		DicWarehousePosition isUpdateOrAddObj = null;
		if (UtilString.isNullOrEmpty(positionId) || UtilString.isNullOrEmpty(whId) || UtilString.isNullOrEmpty(areaId)
				|| UtilString.isNullOrEmpty(positionCode)) {
			errorStatus = false;
			errorString = "不可以有空值";
		} else {
			isUpdateOrAddObj = getPositionIdByWhIdAndAreaIdAndPositionCode(whId, areaId, positionCode);
			Map<String, Object> positionIndexsMap = cutPositionCode(positionCode);
			if (isUpdateOrAddObj == null) {
				if (UtilString.getIntForString(positionId) == 0) {

					DicWarehousePosition obj = new DicWarehousePosition();
					obj.setWhId(UtilString.getIntForString(whId)); // 仓库id
					obj.setAreaId(UtilString.getIntForString(areaId)); // 储存区id
					obj.setPositionCode(positionCode); // 仓位编码
					obj.setPositionIndex1(
							UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex1")));
					obj.setPositionIndex2(
							UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex2")));
					obj.setPositionIndex3(
							UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex3")));
					obj.setFreezeInput((byte) UtilString.getIntForString(freezeInput)); // 入库冻结---0-非冻结;1-冻结
					obj.setFreezeOutput((byte) UtilString.getIntForString(freezeOutput)); // 出库冻结---0-非冻结;1-冻结
					obj.setStorageTypeId(UtilString.getIntForString(storageTypeId));
					// obj.setFreezeId((byte)
					// UtilString.getIntForString(freezeId)); // 冻结原因id
					// obj.setUnitWeight(UtilString.getIntForString(unitWeight));
					// // 承重单位id
					// obj.setUnitVolume(UtilString.getIntForString(unitVolume));
					// // 容积单位id
					dicWarehousePositionDao.insertSelective(obj);
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				} else {
					DicWarehousePosition obj = new DicWarehousePosition();
					obj.setPositionId(UtilString.getIntForString(positionId)); // 仓位id
					obj.setWhId(UtilString.getIntForString(whId)); // 仓库id
					obj.setAreaId(UtilString.getIntForString(areaId)); // 储存区id
					obj.setPositionCode(positionCode); // 仓位编码
					obj.setPositionIndex1(
							UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex1")));
					obj.setPositionIndex2(
							UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex2")));
					obj.setPositionIndex3(
							UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex3")));
					obj.setFreezeInput((byte) UtilString.getIntForString(freezeInput)); // 入库冻结---0-非冻结;1-冻结
					obj.setFreezeOutput((byte) UtilString.getIntForString(freezeOutput)); // 出库冻结---0-非冻结;1-冻结
					obj.setStorageTypeId(UtilString.getIntForString(storageTypeId));
					// obj.setFreezeId((byte)
					// UtilString.getIntForString(freezeId)); // 冻结原因id
					// obj.setUnitWeight(UtilString.getIntForString(unitWeight));
					// // 承重单位id
					// obj.setUnitVolume(UtilString.getIntForString(unitVolume));
					// // 容积单位id
					dicWarehousePositionDao.updateByPrimaryKeySelective(obj);
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				}
			} else {
				if (UtilString.getIntForString(positionId) == 0) {
					errorStatus = false;
					errorString = "已经存在该仓库下的该储存区下的该仓位";
				} else {
					if (isUpdateOrAddObj.getPositionId().intValue() == UtilString.getIntForString(positionId)) {
						DicWarehousePosition obj = new DicWarehousePosition();
						obj.setPositionId(UtilString.getIntForString(positionId)); // 仓位id
						obj.setWhId(UtilString.getIntForString(whId)); // 仓库id
						obj.setAreaId(UtilString.getIntForString(areaId)); // 储存区id
						obj.setPositionCode(positionCode); // 仓位编码
						obj.setPositionIndex1(
								UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex1")));
						obj.setPositionIndex2(
								UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex2")));
						obj.setPositionIndex3(
								UtilString.getStringOrEmptyForObject(positionIndexsMap.get("positionIndex3")));
						obj.setFreezeInput((byte) UtilString.getIntForString(freezeInput)); // 入库冻结---0-非冻结;1-冻结
						obj.setFreezeOutput((byte) UtilString.getIntForString(freezeOutput)); // 出库冻结---0-非冻结;1-冻结
						obj.setStorageTypeId(UtilString.getIntForString(storageTypeId));
						// obj.setFreezeId((byte)
						// UtilString.getIntForString(freezeId)); // 冻结原因id
						// obj.setUnitWeight(UtilString.getIntForString(unitWeight));
						// // 承重单位id
						// obj.setUnitVolume(UtilString.getIntForString(unitVolume));
						// // 容积单位id
						dicWarehousePositionDao.updateByPrimaryKeySelective(obj);
						errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					} else {
						errorStatus = false;
						errorString = "已经存在该仓库下的该储存区下的该仓位";
					}
				}
			}

		}

		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	@Override
	public DicWarehousePosition getPositionIdByWhIdAndAreaIdAndPositionCode(String whId, String areaId,
			String positionCode) throws Exception {
		DicWarehousePosition obj = new DicWarehousePosition();
		obj.setWhId(UtilString.getIntForString(whId));
		obj.setAreaId(UtilString.getIntForString(areaId));
		obj.setPositionCode(positionCode);
		return dicWarehousePositionDao.selectPositionIdByWhIdAndAreaIdAndPositionCode(obj);
	}

	@Override
	public Map<String, Object> cutPositionCode(String positionCode) throws Exception {
		Map<String, Object> positionIndexMap = new HashMap<String, Object>();
		String positionIndex1 = "000000000000";
		String positionIndex2 = "0000000000";
		String positionIndex3 = "0000000000";
		String[] positionCodes = positionCode.split("-");
		if (positionCodes.length == 1) {
			positionIndex1 = "000000" + positionCodes[0];
		} else if (positionCodes.length == 2) {
			positionIndex1 = "000000" + positionCodes[0];
			positionIndex2 = "00000" + positionCodes[1];
		} else if (positionCodes.length == 3) {
			positionIndex1 = "000000" + positionCodes[0];
			positionIndex2 = "00000" + positionCodes[1];
			positionIndex3 = "00000" + positionCodes[2];
		}

		if (positionIndex1.length() < 12 || positionIndex1.length() == 12) {
			positionIndex1 = positionIndex1.substring((positionIndex1.length() - 6));
		} else {
			positionIndex1 = positionCodes[0].substring(0, 6);
		}

		if (positionIndex2.length() < 10 || positionIndex2.length() == 10) {
			positionIndex2 = positionIndex2.substring((positionIndex2.length() - 5));
		} else {
			positionIndex2 = positionCodes[1].substring(0, 5);
		}

		if (positionIndex3.length() < 10 || positionIndex3.length() == 10) {
			positionIndex3 = positionIndex3.substring((positionIndex3.length() - 5));
		} else {
			positionIndex3 = positionCodes[2].substring(0, 5);

		}
		positionIndexMap.put("positionIndex1", positionIndex1);
		positionIndexMap.put("positionIndex2", positionIndex2);
		positionIndexMap.put("positionIndex3", positionIndex3);
		return positionIndexMap;
	}

	@Override
	public Map<String, Object> getWarehousePositionById(Integer positionId, CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			DicWarehousePosition dw = null;

			dw = dicWarehousePositionDao.selectPositionByPrimaryKey(positionId);
			if (null == dw) {
				dw = new DicWarehousePosition();
			}

			body.put("position_id", dw.getPositionId());
			body.put("wh_id", dw.getWhId());
			body.put("wh_code", dw.getWhCode());
			body.put("area_id", dw.getAreaId());
			body.put("area_code", dw.getAreaCode());
			body.put("position_code", dw.getPositionCode());
			body.put("freeze_input", dw.getFreezeInput());
			body.put("freeze_output", dw.getFreezeOutput());
			body.put("wh_name", dw.getWhName());
			body.put("area_name", dw.getAreaName());
			body.put("storage_type_id", dw.getStorageTypeId());
			body.put("storage_type_code", dw.getStorageTypeCode());
			body.put("storage_type_name", dw.getStorageTypeName());
			
			// body.put("unit_weight", dw.getUnitWeight());
			// body.put("unit_volume", dw.getUnitVolume());

		} catch (Exception e) {

		}

		return body;

	}

	public Map<String, String> getTitleMapping() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("仓库号", "whCode");
		map.put("存储区", "areaCode");
		map.put("仓位", "positionCode");
		// map.put("仓储类型", "LGBER");
		// map.put("仓位类型", "LPTYP");
		// map.put("仓位的最大重量", "LGEWI");
		map.put("重量单位", "unitWeightCode");
		// map.put("仓位的最大体积", "VOLUM");
		map.put("体积单位", "unitVolumeCode");
		map.put("出库冻结标识", "freezeOutput");
		map.put("入库冻结标识", "freezeInput");
		// map.put("冻结原因", "SPGRU");
		// map.put("允许的最大托盘数", "MAXLE");
		// map.put("标识: 动态仓位 ", "KZDYN");
		// map.put("X 坐标", "XCORD");
		// map.put("Z 坐标", "ZCORD");
		// map.put("Y 坐标", "YCORD");

		return map;
	}

	public Map<String, ExcelCellType> getCheckData() {

		Map<String, ExcelCellType> checkData = new HashMap<String, ExcelCellType>();
		checkData.put("whCode", new ExcelCellType(true, 0, UtilExcel.CELL_TYPE_NUM));
		checkData.put("areaCode", new ExcelCellType(true, 0, UtilExcel.CELL_TYPE_NUM));
		checkData.put("positionCode", new ExcelCellType(true));
		return checkData;
	}

	public Map<String, List<String>> getMultValidation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = dicWarehousePositionDao.selectPositionForImport();
		list.add(UtilExcel.VALIDATION_DATA_NOTEXISTS);
		map.put("LGNUM-LGTYP-LGPLA", list);

		return map;
	}

	@Override
	public int getExcelData(String realPath, String fileName, String folder) throws Exception {

		// 文件全路径
		String path = realPath + folder + File.separator;

		List<Map<String, Object>> dataList = UtilExcel.getExcelDataList(path + fileName, null, getTitleMapping(),
				getCheckData(), getMultValidation());
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		Map<String, String> keyMap = new HashMap<String, String>();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> innerMap = dataList.get(i);
			String positionCode = UtilString.getStringOrEmptyForObject(innerMap.get("positionCode"));
			Map<String, Object> cutMap = this.cutPositionCode(positionCode);
			innerMap.putAll(cutMap);
			String whCode = UtilString.getStringOrEmptyForObject(innerMap.get("whCode"));
			String areaCode = UtilString.getStringOrEmptyForObject(innerMap.get("whCode"));
			Integer whId = dictionaryService.getWhIdByWhCode(whCode);
			Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
			String unitWeightCode = UtilString.getStringOrEmptyForObject(innerMap.get("whCode"));
			String unitVolumeCode = UtilString.getStringOrEmptyForObject(innerMap.get("whCode"));
			Integer unitWeight = dictionaryService.getUnitIdByUnitCode(unitWeightCode);
			Integer unitVolume = dictionaryService.getUnitIdByUnitCode(unitVolumeCode);
			innerMap.put("whId", whId);
			innerMap.put("areaId", areaId);
			innerMap.put("unitWeight", unitWeight);
			innerMap.put("unitVolume", unitVolume);
			innerMap.put("freezeInput", UtilObject.getIntOrZero(innerMap.get("freezeInput")));
			innerMap.put("freezeOutput", UtilObject.getIntOrZero(innerMap.get("freezeOutput")));
			String key = whCode + "~" + areaCode + "~" + positionCode;
			if (!keyMap.containsKey(key)) {
				keyMap.put(key, key);
				newList.add(innerMap);
			}

		}
		int count = 0;

		if (newList.size() > 0) {

			count = dicWarehousePositionDao.insertPositionBatch(newList);
		} else {
			throw new ExcelCellTypeException("Excel文档中没有找到数据!");
		}

		return count;
	}

	@Override
	public List<Map<String, Object>> selectAllStorageType() {
		return dicWarehousePositionDao.selectAllStorageType();
	}
}
