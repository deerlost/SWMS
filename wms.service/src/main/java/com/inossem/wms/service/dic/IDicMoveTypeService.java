package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicMoveType;

/**
 * 移动类型功能接口
 * 
 * @author 刘宇 2018.01.24
 *
 */
public interface IDicMoveTypeService {

	/**
	 * 移动类型列表页查询and关键字查询
	 * 
	 * @author 刘宇 2018.01.24
	 * @param keyword
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 */
	List<DicMoveType> listMoveType(String keyword, int pageIndex, int pageSize, int total, boolean sortAscend,
			String sortColumn);

	/**
	 * 添加移动类型 刘宇
	 * 
	 * @author 刘宇 2018.01.24
	 * @param addOrUpdate
	 * @param moveType
	 * @param errorStatus
	 * @param specStock
	 * @param moveTypeName
	 * @param moveTypeName2
	 * @param bizType
	 * @return
	 */
	public Map<String, Object> addOrUpdatMoveType(int errorCode, Boolean addOrUpdate, String errorString,
			boolean errorStatus, String moveTypeCode, String specStock, String moveTypeName, Byte bizType);

	/**
	 * 
	 * 查询移动类型-用于添加判断 刘宇
	 * 
	 * @author 刘宇 2018.01.24
	 * @param moveType
	 * @param specStock
	 * @return
	 */
	public DicMoveType getMoveType(String moveTypeCode, String specStock);

	/**
	 * 根据单据类型查询 rel_move_type 表查询移动类型
	 * 
	 * @param receiptType
	 * @return
	 */
	public List<DicMoveType> getMoveTypeByReceiptType(int receiptType);

	/**
	 * 根据业务类型查询dic_move_type 表查询移动类型
	 * 
	 * @param bizType
	 * @return
	 */
	public List<DicMoveType> getMoveTypeByBizType(int bizType);
}
