package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicStockLocation;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;

public interface DicStockLocationMapper {
	int deleteByPrimaryKey(Integer locationId);

	int insert(DicStockLocation record);

	int insertSelective(DicStockLocation record);

	DicStockLocation selectByPrimaryKey(Integer locationId);

	List<RelUserStockLocationVo> selectByFtyId(int ftyId);

	RelUserStockLocationVo selectByFtyCodeAndLocationCode(@Param("ftyCode")String ftyCode,@Param("locationCode") String locationCode);

	List<RelUserStockLocationVo> selectLocationForUser(String userId);

	List<DicStockLocation> selectAllList();

	int updateByPrimaryKeySelective(DicStockLocation record);

	int updateByPrimaryKey(DicStockLocation record);

	List<DicStockLocationVo> selectAll();

	List<DicStockLocationVo> selectLocationsByCorpId(Integer corpId);

	List<DicStockLocationVo> selectLocationsByFtyId(Integer ftyId);

	DicStockLocation selectLocationCodeByPositionId(Integer positionId);

	String selectPlanByLocation(Integer locationId);

	/**
	 * 分页查询库存地点
	 * 
	 * @author 刘宇 2018.02.28
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectLocationOnPaging(Map<String, Object> map);

	/**
	 * 通过库存地点编码和工厂id获取库存地点id
	 * 
	 * @author 刘宇 2018.02.28
	 * @param record
	 * @return
	 */
	DicStockLocation selectLocationIDByLocationCodeAndFtyId(DicStockLocation record);

	
	//根据板块找公司
	List<Map<String,Object>> selectCorpListByBoardId(int boardId);
	//根据公司找工厂
	List<Map<String,Object>> selectFactoryListByCorpId(int corpId);
	//根据工厂找库存地点
	List<Map<String,Object>> selectLocationListByFtyId(int ftyId);
	//根获取所有物料组
	List<Map<String, Object>> selectMatGroup(Map<String, Object> paramMap);
	//获取当前库存金额
	List<Map<String,Object>> selectNowAmountByParam(Map<String, Object> paramMap);
	
	List<Map<String,Object>> selectLocationIdCodeByFtyCode(Map<String,Object> parameter);

}