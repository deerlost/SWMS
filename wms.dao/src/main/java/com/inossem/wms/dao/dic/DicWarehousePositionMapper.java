package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.key.DicWarehousePositionKey;

public interface DicWarehousePositionMapper {
	int deleteByPrimaryKey(Integer positionId);

	int insert(DicWarehousePosition record);

	int insertSelective(DicWarehousePosition record);

	DicWarehousePosition selectByPrimaryKey(Integer positionId);

	int updateByPrimaryKeySelective(DicWarehousePosition record);

	int updateByPrimaryKey(DicWarehousePosition record);

	DicWarehousePosition selectDicWarehousePositionByKey(DicWarehousePositionKey key);

	DicWarehousePosition selectDicWarehousePositionByPositionIndex(DicWarehousePosition record);
	List<DicWarehousePosition> selectIdAndCodeBySql(String value);

	/**
	 * @Description: 移动端-根据工厂编码和库存地址编码来查询 校验仓位信息列表
	 * @param linkMobile
	 *            包含工厂编码和库存地点编码信息
	 */
	public List<Map<String, Object>> findPositionListByFactoryCodeAndInventoryAddressCode(Map<String, Object> map)
			throws Exception;

	/**
	 * @Description: 根据工厂，库存地点，查询存储区列表数据接口 【07-01-3.jpg,
	 *               库存盘点-库存盘点列表-创建盘点-盘点清单页面，查询存储区】
	 *               根据工厂，库存地点，到库存地点主数据[dic_stock_location]表查询仓库号，根据仓库号到仓储类型主数据[s_t301t]查询存储区列表数据接口
	 * @param map
	 *            工厂和库存地点
	 */
	List<Map<String, Object>> findStorageAreaListByFactoryCodeAndInventoryAddressCode(Map<String, Object> map)
			throws Exception;

	/**
	 * 
	 * @Description: 根据工厂编码和库存地址编码来查询和存储区编码来查询仓位信息列表
	 * @param map
	 *            包含工厂编码和库存地点编码和存储区编码 和 查询条件
	 */
	public List<Map<String, Object>> findPositionListByFactoryCodeAndInventoryAddressCodeAndStorageArea(
			Map<String, String> map) throws Exception;

	/**
	 * 仓位主数据分页查询
	 * 
	 * @author 刘宇 2018.02.27
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectWarehousePositionOnPaging(Map<String, Object> map) throws Exception;

	/**
	 * 通过 仓库id---wh_id 和 储存区id---area_id 和 仓位编号position_code
	 * 查询仓位id---position_id
	 * 
	 * @author 刘宇 2018.03.01
	 * @param record
	 * @return
	 */
	DicWarehousePosition selectPositionIdByWhIdAndAreaIdAndPositionCode(DicWarehousePosition record);

	/**
	 * @Description: 查询仓位表所有数据列表
	 * @return
	 */
	List<DicWarehousePosition> findLAGPList(DicWarehousePosition lagp) throws Exception;
	
	/**
	 * @Description: 批量根据仓库号，存储区，仓位，更新仓位的锁定状态
	 * @param lagps
	 *            对象集合
	 */
	public void batchUpdatePdlock(List<DicWarehousePosition> records) throws Exception;
	
	
	DicWarehousePosition selectPositionByPrimaryKey(Integer positionId);

	List<String> selectPositionForImport();
	
	int insertPositionBatch(List<Map<String, Object>> list);
	
	
	/**
	 * @Description: 组织架构 存储区管理  打印仓位
	 * @return
	 */
	List<DicWarehousePosition> selectPositionForPrint(Map<String, Object> map) throws Exception;
	
	 List<Map<String,Object>> selectAllStorageType();
	
}