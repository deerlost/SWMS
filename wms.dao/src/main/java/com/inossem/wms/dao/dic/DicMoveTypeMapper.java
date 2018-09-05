package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicMoveType; 
import com.inossem.wms.model.vo.DicMoveTypeVo;

public interface DicMoveTypeMapper {
	int deleteByPrimaryKey(Integer moveTypeId);

	int insert(DicMoveType record);

	int insertSelective(DicMoveType record);

	DicMoveType selectByPrimaryKey(Integer moveTypeId);

	int updateByPrimaryKeySelective(DicMoveType record);

	int updateByPrimaryKey(DicMoveType record);

	/**
	 * 添加移动类型
	 * 
	 * @author 刘宇 2018.01.22
	 * @param record
	 * @return
	 */
	int insertMoveType(DicMoveType record);

	/**
	 * 修改移动类型
	 * 
	 * @author 刘宇 2018.01.22
	 * @param record
	 * @return
	 */
	int updateMoveTypeByPrimaryKeySelective(DicMoveType record);

	/**
	 * 移动类型列表and关键字查询
	 * 
	 * @author 刘宇 2018.01.24
	 * @param map
	 * @return
	 */
	List<DicMoveType> listMoveTypeOnPaging(Map<String, Object> map);

	/**
	 * 查询移动类型-用于添加判断
	 * 
	 * @author 刘宇 2018.01.24
	 * @param record
	 * @return
	 */
	DicMoveType getMoveTypeByMoveTypeAndSpecStock(DicMoveType record);

	List<DicMoveType> selectAll();

	/**
	 * 根据单据类型查询 rel_move_type 表查询移动类型
	 * 
	 * @author
	 * @return
	 */
	List<DicMoveType> selectByReceiptType(int receiptType);

	/**
	 * 根据业务类型查询dic_move_type 表查询移动类型
	 * 
	 * @param bizType
	 * @return
	 */
	List<DicMoveType> selectByBizType(int bizType);

	/**
	 * 查询移动类型原因
	 * 
	 * @param receiptType
	 * @return
	 */
	List<DicMoveTypeVo> selectVoByReceiptType(Byte receiptType);

}