package com.inossem.wms.service.dic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicStockLocation;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.service.dic.IDicStockLocationService;
import com.inossem.wms.util.UtilString;

@Transactional
@Service("dicStockLocationServiceImpl")
public class DicStockLocationServiceImpl implements IDicStockLocationService {
	@Autowired
	private DicStockLocationMapper locationDao;

	// @Autowired
	// private DicFactoryMapper ftyDao;
	@Override
	public List<DicStockLocation> listAll() {
		return locationDao.selectAllList();
	}

	@Override
	public int saveLocation(boolean isAdd, DicStockLocation location) {
		if (isAdd) {
			return locationDao.insertSelective(location);
		} else {
			return locationDao.updateByPrimaryKey(location);
		}
	}

	@Override
	public DicStockLocation getByPrimaryKey(int locationId) {
		return locationDao.selectByPrimaryKey(locationId);
	}

	@Override
	public RelUserStockLocationVo getByFtyCodeAndLocationCode(String ftyCode, String locationCode) {
		return locationDao.selectByFtyCodeAndLocationCode(ftyCode, locationCode);
	}

	@Override
	public List<RelUserStockLocationVo> listByFtyId(int ftyId) {
		return locationDao.selectByFtyId(ftyId);
	}

	@Override
	public List<RelUserStockLocationVo> listLocationForUser(String userId) {
		return locationDao.selectLocationForUser(userId);
	}

	@Override
	public List<DicStockLocation> listLocationAll() {
		return locationDao.selectAllList();
	}

	@Override
	public List<DicStockLocationVo> getLocationsByFtyId(int ftyId) {
		return locationDao.selectLocationsByFtyId(ftyId);
	}

	@Override
	public List<Map<String, Object>> listLocationOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", condition);// 查询条件
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", true);
		map.put("sortAscend", sortAscend);
		map.put("sortColumn", sortColumn);
		return locationDao.selectLocationOnPaging(map);
	}

	@Override
	public Map<String, Object> addOrUpdateLocation(int errorCode, String errorString, boolean errorStatus,
			boolean isAdd, String locationId, String locationCode, String locationName, String cityId, String address,
			String createTime, String ftyId, String status, String freezeInput, String freezeOutput, String enabled)
			throws Exception {
		DicStockLocation isUpdateOrAddObj = null;
		//|| UtilString.isNullOrEmpty(address)
		if (UtilString.isNullOrEmpty(locationId) || UtilString.isNullOrEmpty(locationCode)
				|| UtilString.isNullOrEmpty(locationName) || UtilString.isNullOrEmpty(cityId)
				|| UtilString.isNullOrEmpty(createTime)
				|| UtilString.isNullOrEmpty(status) || UtilString.isNullOrEmpty(freezeInput)
				|| UtilString.isNullOrEmpty(freezeOutput) || UtilString.isNullOrEmpty(enabled)) {
			errorStatus = false;
			errorString = "不可以有空值";
		} else {
			isUpdateOrAddObj = getLocationIdByLocationCodeAndFtyId(locationCode, ftyId);
			if (isUpdateOrAddObj == null) {
				if (isAdd) {
					DicStockLocation obj = new DicStockLocation();
					obj.setLocationCode(locationCode);// 库存地点编码
					obj.setLocationName(locationName);// 库存地点描述
					obj.setCityId(UtilString.getIntForString(cityId));// 城市id
					obj.setAddress(address);// 详细地址
					obj.setCreateTime(UtilString.getDateTimeForString(createTime));// 上线日期
					obj.setFtyId(UtilString.getIntForString(ftyId));// 工厂id
					obj.setStatus((byte) UtilString.getIntForString(status));// 状态
					obj.setWhId(0);
					obj.setFreezeInput((byte) UtilString.getIntForString(freezeInput));// 入库冻结
					obj.setFreezeOutput((byte) UtilString.getIntForString(freezeOutput));// 出库冻结
					obj.setEnabled((byte) UtilString.getIntForString(enabled));// 不可用
					locationDao.insertSelective(obj);
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				} else {
					DicStockLocation obj = new DicStockLocation();
					obj.setLocationId(UtilString.getIntForString(locationId));// 库存地点id
					obj.setLocationCode(locationCode);// 库存地点编码
					obj.setLocationName(locationName);// 库存地点描述
					obj.setCityId(UtilString.getIntForString(cityId));// 城市id
					obj.setAddress(address);// 详细地址
					obj.setCreateTime(UtilString.getDateTimeForString(createTime));// 上线日期
					obj.setFtyId(UtilString.getIntForString(ftyId));// 工厂id
					obj.setStatus((byte) UtilString.getIntForString(status));// 状态
					obj.setFreezeInput((byte) UtilString.getIntForString(freezeInput));// 入库冻结
					obj.setFreezeOutput((byte) UtilString.getIntForString(freezeOutput));// 出库冻结
					obj.setEnabled((byte) UtilString.getIntForString(enabled));// 不可用
					locationDao.updateByPrimaryKeySelective(obj);
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				}
			} else {
				if (isAdd) {
					errorStatus = false;
					errorString = "已经存在该工厂和该库存地点的组合";
				} else {
					if (isUpdateOrAddObj.getLocationId().intValue() == UtilString.getIntForString(locationId)) {
						DicStockLocation obj = new DicStockLocation();
						obj.setLocationId(UtilString.getIntForString(locationId));// 库存地点id
						obj.setLocationCode(locationCode);// 库存地点编码
						obj.setLocationName(locationName);// 库存地点描述
						obj.setCityId(UtilString.getIntForString(cityId));// 城市id
						obj.setAddress(address);// 详细地址
						obj.setCreateTime(UtilString.getDateTimeForString(createTime));// 上线日期
						obj.setFtyId(UtilString.getIntForString(ftyId));// 工厂id
						obj.setStatus((byte) UtilString.getIntForString(status));// 状态
						obj.setFreezeInput((byte) UtilString.getIntForString(freezeInput));// 入库冻结
						obj.setFreezeOutput((byte) UtilString.getIntForString(freezeOutput));// 出库冻结
						obj.setEnabled((byte) UtilString.getIntForString(enabled));// 不可用
						locationDao.updateByPrimaryKeySelective(obj);
						errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					} else {
						errorStatus = false;
						errorString = "已经存在该工厂和该库存地点的组合";
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
	public DicStockLocation getLocationIdByLocationCodeAndFtyId(String locationCode, String ftyId) {
		DicStockLocation obj = new DicStockLocation();
		obj.setLocationCode(locationCode);
		obj.setFtyId(UtilString.getIntForString(ftyId));
		return locationDao.selectLocationIDByLocationCodeAndFtyId(obj);
	}

	@Override
	public Map<String, Object> getLocationById(Integer locationId, CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			DicStockLocation dw = null;

			dw = locationDao.selectByPrimaryKey(locationId);
			if (null == dw) {
				dw = new DicStockLocation();
			}

			body.put("location_id", dw.getLocationId());
			body.put("location_code", dw.getLocationCode());
			body.put("location_name", dw.getLocationName());
			body.put("address", dw.getAddress());
			body.put("fty_id", dw.getFtyId());
			body.put("status", dw.getStatus());
			body.put("freeze_input", dw.getFreezeInput());
			body.put("freeze_output", dw.getFreezeOutput());
			body.put("enabled", dw.getEnabled());
			body.put("create_time", UtilString.getShortStringForDate(dw.getCreateTime()));

		} catch (Exception e) {

		}

		return body;

	}
}
