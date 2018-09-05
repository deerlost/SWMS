package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicBoard;

public interface DicBoardMapper {
	int deleteByPrimaryKey(Integer boardId);

	int insert(DicBoard record);

	int insertSelective(DicBoard record);

	DicBoard selectByPrimaryKey(Integer boardId);

	int updateByPrimaryKeySelective(DicBoard record);

	int updateByPrimaryKey(DicBoard record);

	/**
	 * 查询全部板块id和板块描述
	 *
	 * @author 刘宇 2018.03.02
	 * @return
	 */
	List<DicBoard> selectAllBoard();

}