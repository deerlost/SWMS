package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicMoveReason;

public interface DicMoveReasonMapper {
	int deleteByPrimaryKey(Integer reasonId);

	int insert(DicMoveReason record);

	int insertSelective(DicMoveReason record);

	DicMoveReason selectByPrimaryKey(Integer reasonId);

	int updateByPrimaryKeySelective(DicMoveReason record);

	int updateByPrimaryKey(DicMoveReason record);

	List<DicMoveReason> selectByMoveTypeId(int moveTypeId);
}