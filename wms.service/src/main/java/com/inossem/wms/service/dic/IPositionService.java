package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.dic.DicWarehousePosition;

import net.sf.json.JSONArray;

/**
 * 查询仓位主数据
 * 
 * @author 刘宇 2018.02.27
 *
 */
public interface IPositionService {
	/**
	 * 分页查询仓位主数据
	 * 
	 * @author 刘宇 2018.02.27
	 * @param whId
	 * @param positionCode
	 * @param areaCode
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listPositionOnPaging(Map<String, Object> map) throws Exception;

	/**
	 * 查询所有仓库下的所有储存区
	 * 
	 * @author 刘宇 2018.03.01
	 * @return
	 * @throws Exception
	 */
	JSONArray listWarsehouseAndArea(List<DicWarehouse> warehouses, List<DicWarehouseArea> areas) throws Exception;

	/**
	 * 
	 * 添加或者修改仓位
	 * 
	 * @author 刘宇 2018.03.01
	 * @param errorCode
	 * @param errorString
	 * @param errorStatus
	 * @param positionId
	 * @param whId
	 * @param areaId
	 * @param positionCode
	 * @param freezeInput
	 * @param freezeOutput
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addOrUpdatePosition(int errorCode, String errorString, boolean errorStatus, String positionId,
			String whId, String areaId, String positionCode, String freezeInput, String freezeOutput,String storageTypeId) throws Exception;

	/**
	 * 通过 仓库id---wh_id 和 储存区id---area_id 和 仓位编号position_code
	 * 查询仓位id---position_id
	 * 
	 * @author 刘宇 2018.03.01
	 * @param whId
	 * @param areaId
	 * @param positionCode
	 * @return
	 * @throws Exception
	 */
	DicWarehousePosition getPositionIdByWhIdAndAreaIdAndPositionCode(String whId, String areaId, String positionCode)
			throws Exception;

	/**
	 * 截取仓位编码
	 * 
	 * @author 刘宇 2018.03.01
	 * @param positionCode
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> cutPositionCode(String positionCode) throws Exception;

	Map<String, Object> getWarehousePositionById(Integer positionId, CurrentUser user);

	int getExcelData(String realPath, String fileName, String folder) throws Exception;
	
	List<Map<String,Object>> selectAllStorageType();

}
