package com.inossem.wms.service.dic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePallet;

/**
 * 托盘管理功能接口
 * 
 * @author 刘宇 2018.01.23
 */
public interface IDicWarehousePalletService {
	/**
	 * 添加托盘功能接口方法
	 * 
	 * @author 刘宇 2018.01.19
	 * @param errorString
	 * @param errorStatus
	 * @param palletCode
	 * @param palletName
	 * @param whId
	 * @param stringFreeze
	 * @param stringFreezeId
	 * @param stringStatus
	 * @param stringMaxWeight
	 * @param unitWeight
	 * @return
	 */
	Map<String, Object> addPallet(int errorCode, String errorString, boolean errorStatus, String palletCode,
			String palletName, String whId, String stringFreeze, String stringFreezeId, String stringStatus,
			String stringMaxWeight, String unitWeight,CurrentUser user);

	/**
	 * 查询托盘功能接口方法
	 * 
	 * @author 刘宇 2018.01.19s
	 * @param pageSize
	 * @param pageIndex
	 * @param total
	 * @param condition
	 * @param palletCode
	 * @param palletId
	 * @return
	 */
	List<DicWarehousePallet> listPallets(int pageSize, int pageIndex, int total, String condition, String palletCode,
			Integer palletId, boolean sortAscend, String sortColumn);

	/**
	 * 修改单个托盘功能接口方法
	 * 
	 * @author 刘宇 2018.01.19
	 * @param errorString
	 * @param errorStatus
	 * @param stringPalletId
	 * @param palletName
	 * @param stringFreeze
	 * @param stringFreezeId
	 * @param stringStatus
	 * @param stringMaxWeight
	 * @param unitWeight
	 * @return
	 */
	Map<String, Object> updatePallet(int errorCode, String errorString, boolean errorStatus, String stringPalletId,
			String palletName, String stringFreeze, String stringFreezeId, String stringStatus, String stringMaxWeight,
			String unitWeight);

	/**
	 * 批量修改托盘功能接口方法
	 * 
	 * @author 刘宇 2018.01.19
	 * @param errorString
	 * @param errorStatus
	 * @param palletName
	 * @param stringFreeze
	 * @param stringMaxWeight
	 * @param stringManyPalletId
	 * @return
	 */
	Map<String, Object> updatePalletS(int errorCode, String errorString, boolean errorStatus, String palletName,
			String stringFreeze, String stringMaxWeight, String stringManyPalletId);

	/**
	 * 批量添加托盘接口
	 * 
	 * @author 刘宇 2018.01.19
	 * @param errorString
	 * @param errorStatus
	 * @param palletCodeBeginAdd
	 * @param palletCodeEndAdd
	 * @param palletName
	 * @param whId
	 * @param stringFreeze
	 * @param stringFreezeId
	 * @param stringStatus
	 * @param stringMaxWeight
	 * @param unitWeight
	 * @return
	 */
	Map<String, Object> addPallets(int errorCode, String errorString, boolean errorStatus, String palletCodeBeginAdd,
			String palletCodeEndAdd, String palletName, String whId, String stringFreeze, String stringFreezeId,
			String stringStatus, String stringMaxWeight, String unitWeight);

	/**
	 * 区间查找托盘仓库号接口方法
	 * 
	 * @author 刘宇 2018.01.19
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public List<DicWarehousePallet> listPalletCodes(DicWarehousePallet obj);

	/**
	 * 打印托盘信息
	 * 
	 * @author 刘宇 2018.01.19
	 * @param lgtyp
	 * @param lgpla
	 * @return
	 * @throws Exception
	 */
	public String printPallet(String lgtyp) throws Exception;

	/**
	 * 打印查询托盘
	 * 
	 * @author 刘宇 2018.01.19
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public List<DicWarehousePallet> listPrintPallets(HashMap<String, Object> map) throws Exception;

	/**
	 * 查询厂库号和厂库描述
	 * 
	 * @author 刘宇 2018.01.19
	 * @return
	 */
	public List<Map<String, Object>> listWhIdAndWhNameForPalle();
	
	/**
	 * 查询托盘
	 * @author wangfz 2018.08.22
	 * @param obj
	 * @return
	 */
	public List<DicWarehousePallet> selectDicWarehousePalletList(HashMap<String, Object> map); 

}
