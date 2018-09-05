package com.inossem.wms.service.dic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicDeliveryDriverMapper;
import com.inossem.wms.dao.dic.DicDeliveryVehicleMapper;
import com.inossem.wms.dao.dic.DicProductLineMapper;
import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.dao.rel.RelTaskUserWarehouseMapper;
import com.inossem.wms.model.dic.DicDeliveryDriver;
import com.inossem.wms.model.dic.DicDeliveryVehicle;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.rel.RelTaskUserWarehouse;
import com.inossem.wms.service.dic.IPersonnelService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;

@Service
@Transactional
public class PersonnelServiceImpl implements IPersonnelService {

	@Resource
	DicDeliveryDriverMapper driverDao;

	@Resource
	DicDeliveryVehicleMapper vehicleDao;

	@Resource
	RelTaskUserWarehouseMapper userwareDao;

	@Autowired
	DicWarehouseMapper dicWarehouseDao;

	@Autowired
	DicProductLineMapper dicProductLineDao;

	@Override
	public List<Map<String, Object>> queryPersonnel(Map<String, Object> param) throws Exception {
		int typeId = UtilObject.getIntegerOrNull(param.get("type"));
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (typeId == 4) {
			result = driverDao.queryAllDriverOnPaging(param);
		} else if (typeId == 5) {
			result = vehicleDao.queryAllVehicleOnPaging(param);
		} else {
			result = userwareDao.selectByTypeOnPaging(param);
		}

		return result;
	}

	@Override
	public int insertPersonnel(JSONObject json) throws Exception {
		int type = json.getInt("type");
		if (type == 4) {
			DicDeliveryDriver driver = new DicDeliveryDriver();
			Map<String, Object> map = new HashMap<String, Object>();
			driver.setProductLineId(json.getInt("product_line_id"));
			map.put("delivery_driver_name", json.getString("driver_name"));
			driver.setDeliveryDriverName(String.valueOf(map.get("delivery_driver_name")));
			driverDao.insertSelective(driver);
		} else if (type == 5) {
			DicDeliveryVehicle vehicle = new DicDeliveryVehicle();
			vehicle.setDeliveryVehicleName(json.getString("vehicle_name"));
			vehicle.setProductLineId(json.getInt("product_line_id"));
			vehicleDao.insertSelective(vehicle);
		} else {
			RelTaskUserWarehouse userware = new RelTaskUserWarehouse();
			userware.setName(json.getString("person_name"));
			userware.setType((byte) type);
			userware.setWhId(json.getInt("wh_id"));
			userwareDao.insertSelective(userware);
		}

		return 0;
	}

	@Override
	public void deletePersonnel(int type, int id) throws Exception {
		if (type == 4) {
			DicDeliveryDriver driver = new DicDeliveryDriver();
			driver.setDeliveryDriverId(id);
			driver.setIsDelete((byte) 1);
			driverDao.updateByPrimaryKeySelective(driver);
		} else if (type == 5) {
			DicDeliveryVehicle vehicle = new DicDeliveryVehicle();
			vehicle.setDeliveryVehicleId(id);
			vehicle.setIsDelete((byte) 1);
			vehicleDao.updateByPrimaryKeySelective(vehicle);
		} else {
			RelTaskUserWarehouse userware = new RelTaskUserWarehouse();
			userware.setId(id);
			userware.setIsDelete((byte) 1);
			userwareDao.updateByPrimaryKeySelective(userware);
		}
	}

	@Override
	public List<DicProductLine> selectAllProductLine() {
		// TODO Auto-generated method stub
		return dicProductLineDao.selectAllProductLine();
	}

	@Override
	public List<DicWarehouse> selectAllWhIdAndWhCodeAndWhName() {
		// TODO Auto-generated method stub
		return dicWarehouseDao.selectAllWhIdAndWhCodeAndWhName();
	}

}
