package com.inossem.wms.service.dic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.dao.dic.DicWarehousePalletMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IDicWarehousePalletService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 托盘管理功能实现类
 * 
 * @author 刘宇 2018.01.23
 *
 */

@Transactional
@Service("palletService")
public class DicWarehousePalletServiceImpl implements IDicWarehousePalletService {

	private String url = UtilProperties.getInstance().getPrintUrl() + "/wms/print/print/ytLabel";
	// private String url = "http://127.0.0.1:8080/PrintServer/print/ytLabel";

	@Autowired
	private DicWarehousePalletMapper dicWarehousePalletMapper;

	@Autowired
	private DicWarehouseMapper dicWarehouseMapper;

	BigDecimal maxWeight = null; // 最大承重
	Byte freeze = 0; // 冻结标示 0 非冻结 1冻结
	Byte freezeId = 0; // 默认0
	Byte status = 0; // 空托盘标识,0-空
	Long palletCodeBegin = null; // 起始托盘号
	Long palletCodeEnd = null; // 结束托盘号
	String minWeightString = "0"; // 承重最小值
	String maxWeightString = "999999"; // 承重最大值
	BigDecimal minWeightValue = new BigDecimal(minWeightString); // 承重最小值
	BigDecimal maxWeightValue = new BigDecimal(maxWeightString); // 承重最大值
	String maxPalletCodeString = "9999999999"; // 托盘号最大值
	Long maxPalletCodeValue = Long.parseLong(maxPalletCodeString); // 托盘号最大值
	String[] manyPalletId = {}; // 托盘表自增主键数组
	Integer palletId = 0; // 托盘表自增主键

	@Override
	/**
	 * 添加单个托盘 刘宇
	 */
	public Map<String, Object> addPallet(int errorCode, String errorString, boolean errorStatus, String palletCode,
			String palletName, String whId, String stringFreeze, String stringFreezeId, String stringStatus,
			String stringMaxWeight, String unitWeight,CurrentUser user) {
		Integer a = null;
		
		List<DicWarehousePallet> palletsAdd1 = listPallets(EnumPage.PAGE_SIZE.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY, palletCode, a,
				true, UtilString.STRING_EMPTY);

			DicWarehousePallet palletObj = new DicWarehousePallet();
			palletObj.setPalletCode(palletCode); // 托盘号
			palletObj.setPalletName(palletName); // 托盘类型
			palletObj.setWhId(whId); // 仓库号
			palletObj.setFreeze(UtilObject.getByteOrNull(freeze)); // 冻结标示 0 非冻结 1冻结
			palletObj.setFreezeId(UtilObject.getByteOrNull(stringFreezeId)); // 默认0
			palletObj.setStatus(status); // 空托盘标识,0-空
			palletObj.setMaxWeight(UtilObject.getBigDecimalOrZero(stringMaxWeight)); // 最大承重
			palletObj.setUnitWeight(unitWeight); // 最大承重单位
			palletObj.setPalletId(palletId);
			palletObj.setCreateUser(user.getUserId());
			dicWarehousePalletMapper.insertPalletSelective(palletObj);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	/**
	 * 获取托盘列表and关键字查询
	 */
	@Override
	public List<DicWarehousePallet> listPallets(int pageSize, int pageIndex, int total, String condition,
			String palletCode, Integer palletId, boolean sortAscend, String sortColumn) {
		DicWarehousePallet obj = new DicWarehousePallet();
		obj.setPaging(true);
		obj.setPageSize(pageSize);
		obj.setPageIndex(pageIndex);
		obj.setCondition(condition);
		obj.setTotalCount(total);
		obj.setPalletCode(palletCode);
		obj.setPalletId(palletId);
		obj.setSortAscend(sortAscend);
		obj.setSortColumn(sortColumn);

		return dicWarehousePalletMapper.selectPalletOnPaging(obj);
	}

	/**
	 * 修改单个托盘 刘宇
	 */
	@Override
	public Map<String, Object> updatePallet(int errorCode, String errorString, boolean errorStatus,

			String stringPalletId, String palletName, String stringFreeze, String stringFreezeId, String stringStatus,
			String stringMaxWeight, String unitWeight) {
		
		List<DicWarehousePallet> dicWarehousePallets = listPallets(EnumPage.PAGE_SIZE.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY,
				UtilString.STRING_EMPTY, palletId, true, UtilString.STRING_EMPTY);
	     if (dicWarehousePallets.size() == 0) {

			errorStatus = false;
			errorString = "托盘id不存在";
	 	} else {

			DicWarehousePallet palletObj = new DicWarehousePallet();
			palletObj.setPalletId(UtilObject.getIntegerOrNull(stringPalletId));
			palletObj.setPalletName(palletName); // 托盘类型
			palletObj.setFreeze(UtilObject.getByteOrNull(stringFreeze)); // 冻结标示 0 非冻结 1冻结
			palletObj.setFreezeId(freezeId); // 默认0
			palletObj.setStatus(status); // 空托盘标识,0-空
			palletObj.setMaxWeight(UtilObject.getBigDecimalOrZero(stringMaxWeight)); // 最大承重
			palletObj.setUnitWeight(unitWeight); // 最大承重单位

			dicWarehousePalletMapper.updatePalletByPrimaryKeySelective(palletObj);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	/**
	 * 批量修改托盘 刘宇
	 */
	@Override
	public Map<String, Object> updatePalletS(int errorCode, String errorString, boolean errorStatus,

			String palletName, String stringFreeze, String stringMaxWeight, String stringManyPalletId) {
	
		if (UtilString.isNullOrEmpty(stringMaxWeight) == false) {
			maxWeight = new BigDecimal(stringMaxWeight);

		}
		if (UtilString.isNullOrEmpty(stringManyPalletId) == false) {
			manyPalletId = stringManyPalletId.split(",");

		}

		if (UtilString.isNullOrEmpty(palletName) || UtilString.isNullOrEmpty(stringFreeze)
				|| UtilString.isNullOrEmpty(stringMaxWeight) || UtilString.isNullOrEmpty(stringManyPalletId)

		) {
			errorStatus = false;
			errorString = "不可以有空值";

		} else if (maxWeight.compareTo(minWeightValue) == -1 || maxWeight.compareTo(maxWeightValue) == 1) {
			errorStatus = false;
			errorString = "承重超出数据范围";
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mapManyOfValues", manyPalletId); // 多个托盘表自增主键
			map.put("palletName", palletName); // 托盘类型
			map.put("maxWeight", maxWeight); // 最大承重
			map.put("freeze", freeze); // 冻结标示 0 非冻结 1冻结
			dicWarehousePalletMapper.updateManyOfPalletByPrimaryKeysSelective(map);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	/**
	 * 批量添加托盘 刘宇
	 */
	@Override
	public Map<String, Object> addPallets(int errorCode, String errorString, boolean errorStatus,
			String palletCodeBeginAdd, String palletCodeEndAdd, String palletName, String whId, String stringFreeze,
			String stringFreezeId, String stringStatus, String stringMaxWeight, String unitWeight) {

		DicWarehousePallet palletPalletCode = new DicWarehousePallet();
		palletPalletCode.setPalletCode1(palletCodeBeginAdd);
		palletPalletCode.setPalletCode2(palletCodeEndAdd);
		List<DicWarehousePallet> palletCodesList = listPalletCodes(palletPalletCode);
		List<DicWarehousePallet> pallets = new ArrayList<DicWarehousePallet>();

		if (UtilString.isNullOrEmpty(stringFreeze) == false) {
			freeze = Byte.parseByte(stringFreeze);

		}
		if (UtilString.isNullOrEmpty(stringFreezeId) == false) {
			freezeId = Byte.parseByte(stringFreezeId);

		}
		if (UtilString.isNullOrEmpty(stringStatus) == false) {
			status = Byte.parseByte(stringStatus);

		}
		if (UtilString.isNullOrEmpty(stringMaxWeight) == false) {
			maxWeight = new BigDecimal(stringMaxWeight);

		}
		if (UtilString.isNullOrEmpty(palletCodeBeginAdd) == false) {
			palletCodeBegin = Long.parseLong(palletCodeBeginAdd);

		}
		if (UtilString.isNullOrEmpty(palletCodeEndAdd) == false) {
			palletCodeEnd = Long.parseLong(palletCodeEndAdd);

		}

		if (UtilString.isNullOrEmpty(palletName) || UtilString.isNullOrEmpty(whId)
				|| UtilString.isNullOrEmpty(stringFreeze) || UtilString.isNullOrEmpty(stringFreezeId)
				|| UtilString.isNullOrEmpty(stringStatus) || UtilString.isNullOrEmpty(unitWeight)
				|| UtilString.isNullOrEmpty(palletCodeBeginAdd) || UtilString.isNullOrEmpty(palletCodeEndAdd)
				|| UtilString.isNullOrEmpty(stringMaxWeight)

		) {
			errorStatus = false;
			errorString = "不可以有空值";

		} else if (palletCodeBegin < 1000000000 || palletCodeEnd < 1000000000 || palletCodeBegin > maxPalletCodeValue
				|| palletCodeEnd > maxPalletCodeValue) {
			errorStatus = false;
			errorString = "托盘号不是10位数字";
		} else if (palletCodeBegin >= palletCodeEnd) {
			errorStatus = false;
			errorString = "起始托盘号必须小于结束托盘号";
		} else if (maxWeight.compareTo(minWeightValue) == -1 || maxWeight.compareTo(maxWeightValue) == 1) {
			errorStatus = false;
			errorString = "承重超出数据范围";
		} else if (palletCodesList.size() != 0) {
			int i = -1;
			int arraysSize = palletCodesList.size(); // 声明托盘表自增主键数组的长度
			String[] arrays = new String[arraysSize]; // 新建托盘表自增主键数组
			for (DicWarehousePallet pallet : palletCodesList) {
				i = i + 1;
				String palletCodex = pallet.getPalletCode(); // 托盘号
				arrays[i] = palletCodex;
			}
			String arraysToString = Arrays.toString(arrays);
			errorStatus = false;
			errorString = "仓库号:" + arraysToString + ",已存在！";
		} else {
			long forInt = (palletCodeEnd - palletCodeBegin) + 1; // 声明添加添加托盘的数量
			long useInt = palletCodeBegin - 1; // 每次添加的托盘号
			for (int i = 0; i < forInt; i++) {
				useInt = useInt + 1;
				DicWarehousePallet palletObjs = new DicWarehousePallet();
				String strUseInt = String.valueOf(useInt); // 托盘号
				palletObjs.setPalletCode(strUseInt); // 托盘号
				palletObjs.setPalletName(palletName); // 托盘类型
				palletObjs.setWhId(whId); // 仓库号
				palletObjs.setFreeze(freeze); // 冻结标示 0 非冻结 1冻结
				palletObjs.setFreezeId(freezeId); // 默认0
				palletObjs.setStatus(status); // 空托盘标识,0-空
				palletObjs.setMaxWeight(maxWeight); // 最大承重
				palletObjs.setUnitWeight(unitWeight); // 最大承重单位
				pallets.add(palletObjs);
			}
			dicWarehousePalletMapper.insertPallets(pallets);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	/**
	 * 区间查找托盘仓库号 刘宇
	 */
	@Override
	public List<DicWarehousePallet> listPalletCodes(DicWarehousePallet obj) {
		// TODO Auto-generated method stub
		return dicWarehousePalletMapper.selectPalletCodesByTwoPalletCode(obj);
	}

	/**
	 * 打印托盘 刘宇
	 */
	@Override
	public String printPallet(String ManyOfPallet_id) throws Exception {
		// TODO Auto-generated method stub

		String[] ManyOfPallet_ids = ManyOfPallet_id.split(",");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ManyOfPallet_ids", ManyOfPallet_ids);

		List<DicWarehousePallet> list = listPrintPallets(map);
		JSONObject params = new JSONObject();

		JSONArray printdata = new JSONArray();
		for (DicWarehousePallet pallet : list) {
			JSONObject json = new JSONObject();
			json.put("lgtyp", pallet.getPalletCode()); // 条形码下的托盘号
			json.put("pallet_id", pallet.getPalletId()); // 托盘表自增主键
			json.put("lgpla", "TP"); // lgpla = “TP”
			json.put("pallet_code", pallet.getPalletCode()); // 托盘号
			json.put("pallet_name", pallet.getPalletName()); // 托盘类型
			json.put("wh_id", pallet.getWhId()); // 仓库号
			json.put("wh_name", pallet.getWh_name()); // 仓库描述
			json.put("freeze", pallet.getFreeze()); // 冻结标示 0 非冻结 1冻结
			json.put("freeze_id", pallet.getFreezeId()); // 默认0
			json.put("status", pallet.getStatus()); // 空托盘标识,0-空
			json.put("max_weight", pallet.getMaxWeight()); // 最大承重
			json.put("unit_weight", pallet.getUnitWeight()); // 最大承重单位
			json.put("is_delete", pallet.getIsDelete()); // 是否删除
			json.put("create_time", UtilString.getLongStringForDate(pallet.getCreateTime())); // 创建时间
			json.put("modify_time", pallet.getModifyTime()); // 修改时间
			json.put("type", "L3"); // type = "L3"
			printdata.add(json);
		}
		params.put("printdata", printdata);

		JSONObject jsonreturn = new JSONObject();
		if (printdata.size() > 0) {
			jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 20);
		}
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
		}
		return returnString;

	}

	/**
	 * 查询托盘信息给打印托盘信息使用 刘宇
	 */
	@Override
	public List<DicWarehousePallet> listPrintPallets(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return dicWarehousePalletMapper.selectPalletsForPrintByPalletId(map);

	}

	/**
	 * 查询所有仓库号和仓库描述 刘宇
	 */
	@Override
	public List<Map<String, Object>> listWhIdAndWhNameForPalle() {
		// TODO Auto-generated method stub
		return dicWarehouseMapper.selectWhIdAndWhNameForPallet();
	}
	

	/**
	 * 查询托盘
	 */
	@Override
	public List<DicWarehousePallet> selectDicWarehousePalletList(HashMap<String, Object> map) {
		return dicWarehousePalletMapper.selectDicWarehousePalletList(map);
	}
}
