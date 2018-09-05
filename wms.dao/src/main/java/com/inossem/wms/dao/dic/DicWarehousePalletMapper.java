package com.inossem.wms.dao.dic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicWarehousePallet;

public interface DicWarehousePalletMapper {
	int deleteByPrimaryKey(Integer palletId);

	int insert(DicWarehousePallet record);

	/**
	 * 添加托盘
	 * 
	 * @author 刘宇2018.01.19
	 * @param record
	 * @return
	 */
	int insertPalletSelective(DicWarehousePallet record);

	DicWarehousePallet selectByPrimaryKey(Integer palletId);

	/**
	 * 修改单个托盘信息
	 * 
	 * @author 刘宇2018.01.19
	 * @param record
	 * @return
	 */

	int updatePalletByPrimaryKeySelective(DicWarehousePallet record);

	/**
	 * 批量修改托盘信息
	 * 
	 * @author 刘宇2018.01.19
	 * @param record
	 * @return
	 */
	int updateManyOfPalletByPrimaryKeysSelective(HashMap<String, Object> map);

	/**
	 * 托盘列表/关键字查询
	 * 
	 * @author 刘宇 2018.01.19
	 * @param record
	 * @return
	 */
	List<DicWarehousePallet> selectPalletOnPaging(DicWarehousePallet record);

	/**
	 * 查找区间仓库号
	 * 
	 * @author 刘宇2018.01.19
	 * @return
	 */
	List<DicWarehousePallet> selectPalletCodesByTwoPalletCode(DicWarehousePallet record);

	/**
	 * 批量添加托盘
	 * 
	 * @author 刘宇 2018.01.19
	 * @param pallets
	 * @return
	 */
	int insertPallets(List<DicWarehousePallet> pallets);

	/**
	 * 打印托盘信息
	 * 
	 * @author 刘宇 2018.01.19
	 * @param map
	 * @return
	 */
	List<DicWarehousePallet> selectPalletsForPrintByPalletId(HashMap<String, Object> map);

	int updateByPrimaryKey(DicWarehousePallet record);
	
	List<DicWarehousePallet> selectDicWarehousePalletList(Map<String, Object> param);
	
	int checkPalletIsUsed(@Param("palletId")Integer palletId);
}