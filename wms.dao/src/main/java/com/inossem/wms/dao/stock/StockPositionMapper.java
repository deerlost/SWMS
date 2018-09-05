package com.inossem.wms.dao.stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.key.StockPositionKey;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.vo.StockPositionExpandVo;
import com.inossem.wms.model.vo.StockPositionVo;

public interface StockPositionMapper {
	int deleteByPrimaryKey(Integer id);
	
	int deleteByPrimaryKeyUrgent(Integer id);

	int insert(StockPosition record);

	int insertSelective(StockPosition record);

	StockPosition selectByUniqueKey(StockPositionKey key);

	int deleteByUniqueKey(StockPositionKey key);

	StockPosition selectByPrimaryKey(Integer id);
	
	// 查询 紧急仓位库存表
	StockPosition selectByPrimaryKeyOnUrgent(Integer id);

	int updateByPrimaryKeySelective(StockPosition record);

	int updateByPrimaryKey(StockPosition record);

	String getHavenMaterial(StockPosition record);

	// 基于仓位调整,插入新增物料到目标仓位，数量0
	int insertIgnoreStockPosition(Map<String, Object> map);

	// 基于仓位调整,修改物料数量到目标仓位
	int updateStockPosition(Map<String, Object> map);

	// 基于仓位调整,删除源仓位所有物料
	int deleteStockPosition(Map<String, Object> map);

	List<StockPositionVo> getStockPositionListOnPaging(Map<String, Object> map);

	List<StockPositionVo> selectMaterialInPosition(StockPosition record);

	int selectCntForSLZG(StockPositionVo record);

	/**
	 * 物料调整时，目标仓位库存新增或添加的物料数量
	 */
	int dml(StockPositionVo record);

	// 根据map条件查询库存对象
	StockPosition selectStockObjectByParam(StockPosition record);

	/**
	 * 仓位库存分页查询
	 * 
	 * @author 刘宇 2018.02.09
	 */
	List<Map<String, Object>> selectStockPositionOnPaging(Map<String, Object> map);

	List<Map<String, Object>> selectStockPositionGroupOnPaging(Map<String, Object> map);
	
	/**
	 * @Description: 库存盘点模块，根据存储区，库存仓位范围， 库存状态，仓位状态，查询仓位库存信息
	 * @param biz_stocktake_item
	 *            查询条件 ,详细参数如下
	 * @param ftyId
	 *            工厂
	 * @param locationId
	 *            库存地点
	 * @param positionStatus
	 *            仓位状态
	 * @param stockStatus
	 *            库存状态
	 * @param specStock
	 *            特殊库存标识
	 * @param specStockCode
	 *            特殊库存代码【库存状态in(4,5,6)时，必须传递此值】
	 * @param areaId
	 *            仓储区
	 * @param positionIndex1Min
	 *            库存仓位开始
	 * @param positionIndex1Max
	 *            库存仓位结束
	 * @param positionIndex2Min
	 *            库存仓位开始
	 * @param positionIndex2Max
	 *            库存仓位结束
	 * @param positionIndex3Min
	 *            库存仓位开始
	 * @param positionIndex3Max
	 *            库存仓位结束
	 * @return
	 * @throws Exception
	 * @author wang.ligang
	 * @date 2017年8月24日
	 */
	List<Map<String, Object>> findStockPositionList(Map<String, String> map) throws Exception;

	int deleteByStockInputItemIDs(List<Integer> itemIds);

	/**
	 * 根据入库单行项目id查询仓位库存
	 * 
	 * @param itemId
	 * @return
	 */
	List<StockPosition> selectByInputItemId(Integer itemId);

	/**
	 * @Description: 批量添加仓位库存数据
	 * @param needInsertDataList
	 */
	void batchAddLQUA(@Param("list") List<StockPosition> needInsertDataList) throws Exception;

	/**
	 * @Description: 根据主键批量更新库存数量
	 * @param needUpdateDataList
	 */
	void batchUpdateGesmeByPrimaryKey(@Param("list") List<StockPosition> needUpdateDataList) throws Exception;

	StockPosition selectByTargetUniqueKey(StockPositionExpandVo evo);

	int insertTargetSelective(StockPositionExpandVo record);

	int updateByStockQty(StockPosition record);
	
	int updatePalletIdToZeroByPalletIdAndPositionId(Map<String, Object> map);

	int deleteBySourceUniqueKey(StockPositionExpandVo evo);

	int insertHistorySelective(StockPositionExpandVo evo);

	List<String> selectStockPositionForImport();

	List<String> selectStockBatchForImport();

	List<String> selectBatchMaterialForImport();

	List<String> selectBatchMaterialSpecForImport();

	List<String> selectBatchSpecForImport();

	int insertStockPositionList(List<Map<String, Object>> list);

	int insertStockBatchList(List<Map<String, Object>> list);

	int insertBatchMaterialList(List<Map<String, Object>> list);

	int insertBatchMaterialSpecList(List<Map<String, Object>> list);

	int insertSNList(List<Map<String, Object>> list);

	StockPosition selectBySourceUniqueKey(StockPositionExpandVo evo);

	StockPosition selectBySourceStockUniqueKey(StockPosition evo);

	List<StockPosition> selectStockPositionListByPositionId(Integer positionId);
	
	/**
     * 根据托盘code 包code 查询
     * @param map
     * 	palletCode
     * 	packageCode
     * @return
     */
	List<StockPosition> selectByPalletCodeOrPackageCode(Map<String, Object> map);

	/**
	 * 查询仓位库存 （根据 matId packageTypeId productionBatch erpBatch 分组）
	 * @param map
	 * matCode 
	 * matName
	 * productionBatch
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectGroupByFourKeys(Map<String, Object> map);
	
	/**
	 * 根据条件查询仓位库存仓位
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectAreaPositionByParam(Map<String, Object> map);
	
	/**
	 * 根据条件查询建议仓位库存仓位
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectAdviseAreaPositionByParam(Map<String, Object> map);
	
	/**
	 * 根据条件查询仓位库存仓位
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectStockPositionByParam(Map<String, Object> map);
	
	List<StockPosition> selectUniqueExceptPackageId(StockPosition sPosition);
	
	List<Map<String,Object>>  selectWarrantyDateOnpaging(Map<String,Object> map);
	
	List<Map<String,Object>> selectWarrantyDateSnMassage(Map<String,Object> map);
	
	List<StockPosition> selectByUniqueList(@Param("uniqueList")List<String> uniqueList);
	
	
	//手机端临期库存查询 start 弃用
	List<Map<String,Object>> selectWarringPdaPictrueDay();
	
	List<Map<String,Object>> selectWarringPdaPictrueDayAll();
	
	List<Map<String,Object>> selectWarringPdaByProductLineIdLq(Integer productLineId);
	
	List<Map<String,Object>> selectWarringPdaByProductLineIdYj(Integer productLineId);
	
	List<Map<String,Object>> selectWarringPdaByProductLineIdGq(Integer productLineId);
	
	List<Map<String,Object>> selectWarringPdaByProductLineIdAll(Integer productLineId);
	//手机端临期库存查询 end
	
	//手机端临期库存查询  使用中。。。 start    liguoming
	Map<String,Object> selectFirstView();
	
	Map<String,Object> selectFirstReport();	
	
	 List<Map<String,Object>> selectDetail(Integer searchType);
	 
	 List<Map<String,Object>> selectDetailMat(Map<String, Object> map);
	//手机端临期库存查询  使用中。。。 end
	 
	// 已售未提 mat_id spec
	List<StockPosition> selectByMatIdAndSpec(Map<String, Object> map);
	
	BigDecimal selectAllQty(Map<String,Object> param);
	
	Map<String, Object> selectStockPositionGroupOnPagingCw(Map<String, Object> map);
}