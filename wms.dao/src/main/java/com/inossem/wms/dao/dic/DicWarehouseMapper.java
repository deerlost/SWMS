package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicWarehouse;

public interface DicWarehouseMapper {
	int deleteByPrimaryKey(Integer whId);

	int insert(DicWarehouse record);

	int insertSelective(DicWarehouse record);

	DicWarehouse selectByPrimaryKey(Integer whId);

	int updateByPrimaryKeySelective(DicWarehouse record);

	int updateByPrimaryKey(DicWarehouse record);

	List<DicWarehouse> selectAll();

	/**
	 * 查询查询全部仓库id和仓库描述
	 * 
	 * @author 刘宇 2018.01.19
	 * 
	 * @return
	 */
	List<Map<String, Object>> selectWhIdAndWhNameForPallet();

	/**
	 * 查询所有仓库id，编码，描述
	 * 
	 * @author 刘宇 2018.03.01
	 * @return
	 */
	List<DicWarehouse> selectAllWhIdAndWhCodeAndWhName();
	
	List<Map<String, Object>> listWarehouseOnPaging(Map<String, Object> paramMap);
	
	
	List<Map<String, Object>>  listLocationOnPaging (Map<String, Object> paramMap);
	
	
	DicWarehouse selectByPrimaryCode(String whCode);
	
	int updateLocationByArray(Map<String, Object> paramMap);
	
	int updateLocationByWhId(Integer whId);
	
	List<Map<String, Object>> queryLocationByWhId(Integer whId);
	
	List<Map<String, Object>> listWarehouse();
	
	List<Map<String ,Object>> selectWarehouseListByWhId(Integer whId);
	
	List<DicWarehouse> selectAllWarehouseListWithCoryId();

}