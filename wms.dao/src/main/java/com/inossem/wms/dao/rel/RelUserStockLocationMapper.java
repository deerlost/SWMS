package com.inossem.wms.dao.rel;

import java.util.List;

import com.inossem.wms.model.rel.RelUserStockLocation;
import com.inossem.wms.model.vo.RelUserStockLocationVo;

public interface RelUserStockLocationMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(RelUserStockLocation record);

	int insertSelective(RelUserStockLocation record);

	RelUserStockLocation selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(RelUserStockLocation record);

	int updateByPrimaryKey(RelUserStockLocation record);

	List<RelUserStockLocationVo> selectStockLocationForUser(String userId);
	
	List<RelUserStockLocationVo> selectStockLocationForBoardId(int boardId);
	
	int deleteLocationByUserId(String userId);
}