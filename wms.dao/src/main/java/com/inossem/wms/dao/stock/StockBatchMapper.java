package com.inossem.wms.dao.stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.key.StockBatchKey;
import com.inossem.wms.model.stock.StockBatch;
import com.inossem.wms.model.vo.BufFactoryMatVo;
import com.inossem.wms.model.vo.StockBatchVo;

public interface StockBatchMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(StockBatch record);

	int insertSelective(StockBatch record);

	StockBatch selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(StockBatch record);

	int updateByPrimaryKey(StockBatch record);

	StockBatch selectByUniqueKey(StockBatchKey key);

	int deleteByByUniqueKey(StockBatchKey key);

	// int deleteByByUniqueKeyAndStatus(StockBatch key);

	int updateQty(StockBatch record);

	public List<StockBatchVo> selectStockBatchByCondition(Map<String, Object> map);

	public List<StockBatchVo> getMatNumGroupByMatOnPaging(Map<String, Object> map);

	/**
	 * 批次库存分页查询
	 * 
	 * @author 刘宇 2018.02.06
	 */
	List<Map<String, Object>> selectStockBatchOnPaging(Map<String, Object> map);

	/**
	 * 库龄考核分页查询
	 * 
	 * @author 刘宇 2018.02.25
	 */
	List<Map<String, Object>> selectStockDaysOnPaging(Map<String, Object> map);

	List<Map<String, Object>> queryStockDaysOnPaging(Map<String, Object> map);

	List<StockBatch> selectByUniqueKeys(List<StockBatch> keys);

	// 查询库存积压列表
	List<Map<String, Object>> getStockDaysList(Map<String, Object> map);
	
	//积压 新需求   start   ligm
	List<Map<String, Object>> selectFirstViewStockDays(Map<String, Object> map);
	
	List<Map<String, Object>> selectSecondViewTotalAmount(Map<String, Object> map);

	List<Map<String, Object>>  selectSecondViewStockDays(Map<String, Object> map);
	
	
	List<Map<String, Object>>   selectThirdViewStockDays(Map<String, Object> map);
	//end
	
	// 查询所有工厂的已有库存物料
	List<BufFactoryMatVo> getFtyCodeMatCode();

	List<Map<String, Object>> selectMatListByBatch(Map<String, Object> map);

	List<Map<String, Object>> selectMatListByPosition(Map<String, Object> map);

	List<Map<String, Object>> selectTaskMatListByPositionParam(Map<String, Object> map);
	
	// 查询初期批次库存
	List<StockBatch> selectBatchListToBegin();
	
	List<Map<String,Object>> selectDataForHdzmStorageNum(Map<String,Object> param);

	public List<Map<String, Object>> queryLocPrice(Map<String, Object> param);

	public List<Map<String, Object>> queryLocPriceForCity(Map<String, Object> param);

	List<Map<String,Object>> queryCityNumberForLocPrice(Map<String, Object> param);

	Integer queryLocNumberForLocPrice(Map<String, Object> param);

	public List<Map<String, Object>> downloadLocPrice(Map<String, Object> params);
	
	int updateStatus(Map<String, Object> params);
	
	int updatePositionStatus(Map<String, Object> params);
	
	List<StockBatch> selectByUniqueList(@Param("uniqueList")List<String> uniqueList);
	//出入库查询
	List<Map<String, Object>> queryStockOutAndInOnPaging(Map<String, Object> param);

	Map<String,Object> queryBaseInfo(String userId);

	BigDecimal selectAllQty(Map<String,Object> param);
}