package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;

public interface DicWarehouseAreaMapper {
	int deleteByPrimaryKey(Integer areaId);

	int insert(DicWarehouseArea record);

	int insertSelective(DicWarehouseArea record);

	DicWarehouseArea selectByPrimaryKey(Integer areaId);

	int updateByPrimaryKeySelective(DicWarehouseArea record);

	int updateByPrimaryKey(DicWarehouseArea record);

	List<DicWarehouseArea> selectByKey(Integer whId);

	List<DicWarehouseArea> selectAll();

	/**
	 * 查询全部储存区id，编码，描述
	 * 
	 * @author 刘宇 2018.03.01
	 * @return
	 */
	List<DicWarehouseArea> selectAllAreaIdAndAreaCodeAndAreaName();
	
	List<Map<String, Object>> listAreaOnPaging(Map<String, Object> paramMap);
	
	
	DicWarehouseArea selectByPrimaryCode(Map<String, Object> map);
	//
	List<Map<String,Object>>   selectWareHouseWarring(Map<String,Object> param);

	List<Map<String, Object>> selectWareHouseWarringForPortal(Map<String, Object> param);

	List<Map<String, Object>> selectWareHouseWarringByAreaId(int area_id);

}