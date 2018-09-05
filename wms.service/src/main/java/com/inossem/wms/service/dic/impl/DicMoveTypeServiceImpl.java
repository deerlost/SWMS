package com.inossem.wms.service.dic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicMoveTypeMapper;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.dic.IDicMoveTypeService;
import com.inossem.wms.util.UtilString;

/**
 * 移动类型功能实现类
 * 
 * @author 刘宇 2018.01.24
 *
 */
@Transactional
@Service("moveTypeService")
public class DicMoveTypeServiceImpl implements IDicMoveTypeService {
	@Autowired
	private DicMoveTypeMapper dicMoveTypeDao;

	/**
	 * 移动类型列表and关键字查询 刘宇
	 */
	@Override
	public List<DicMoveType> listMoveType(String keyword, int pageIndex, int pageSize, int total, boolean sortAscend,
			String sortColumn) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keyword", keyword); // 关键字
		map.put("paging", true);
		map.put("pageIndex", pageIndex);
		map.put("pageSize", pageSize);
		map.put("totalCount", total);
		map.put("sortAscend", sortAscend);
		map.put("sortColumn", sortColumn);
		return dicMoveTypeDao.listMoveTypeOnPaging(map);
	}

	/**
	 * 查询移动类型-用于添加判断 刘宇
	 */
	@Override
	public DicMoveType getMoveType(String moveTypeCode, String specStock) {
		DicMoveType obj = new DicMoveType();
		obj.setMoveTypeCode(moveTypeCode);// 移动类型
		obj.setSpecStock(specStock);// 特殊库存标识
		return dicMoveTypeDao.getMoveTypeByMoveTypeAndSpecStock(obj);
	}

	/**
	 * 根据单据类型查询 rel_move_type 表查询移动类型
	 * 
	 * @param receiptType
	 * @return
	 */
	@Override
	public List<DicMoveType> getMoveTypeByReceiptType(int receiptType) {
		return dicMoveTypeDao.selectByReceiptType(receiptType);
	}

	/**
	 * 根据业务类型查询dic_move_type 表查询移动类型
	 * 
	 * @param bizType
	 * @return
	 */
	@Override
	public List<DicMoveType> getMoveTypeByBizType(int bizType) {
		return dicMoveTypeDao.selectByBizType(bizType);
	}

	@Override
	public Map<String, Object> addOrUpdatMoveType(int errorCode, Boolean addOrUpdate, String errorString,
			boolean errorStatus, String moveTypeCode, String specStock, String moveTypeName, Byte bizType) {
		DicMoveType objYdlxUpdate = getMoveType(moveTypeCode, specStock);
		DicMoveType obj = new DicMoveType();
		if (UtilString.isNullOrEmpty(moveTypeCode) || UtilString.isNullOrEmpty(moveTypeName) || bizType == null
				|| bizType == 0) {
			errorStatus = false;
			errorString = "值不可以为空";
		} else if (addOrUpdate) {
			if (objYdlxUpdate != null) {
				errorStatus = false;
				errorString = "该移动类型已经绑定该特殊库存标识";
			} else {
				obj.setMoveTypeCode(moveTypeCode);// 移动类型
				obj.setSpecStock(specStock);// 特殊库存标识
				obj.setMoveTypeName(moveTypeName);// 移动类型描述
				obj.setBizType(bizType);// 业务类型
				dicMoveTypeDao.insertMoveType(obj);
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			}
		} else {
			if (objYdlxUpdate == null) {
				errorStatus = false;
				errorString = "数据错误没有该移动类型";
			} else {
				obj.setMoveTypeCode(moveTypeCode);// 移动类型
				obj.setSpecStock(specStock);// 特殊库存标识
				obj.setMoveTypeName(moveTypeName);// 移动类型描述
				obj.setBizType(bizType);// 业务类型
				dicMoveTypeDao.updateMoveTypeByPrimaryKeySelective(obj);
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			}
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

}
