package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicStockLocation;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;

public interface IDicStockLocationService {

	List<DicStockLocation> listAll();

	int saveLocation(boolean isAdd, DicStockLocation location);

	DicStockLocation getByPrimaryKey(int locationId);

	RelUserStockLocationVo getByFtyCodeAndLocationCode(String ftyCode, String locationCode);

	List<RelUserStockLocationVo> listByFtyId(int ftyId);

	List<RelUserStockLocationVo> listLocationForUser(String userId);

	List<DicStockLocation> listLocationAll();

	List<DicStockLocationVo> getLocationsByFtyId(int ftyId);

	/**
	 * 分页查询库存地点
	 * 
	 * @author 刘宇 2018.02.27
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listLocationOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception;

	/**
	 * 添加或者修改库存地点
	 * 
	 * @param errorString
	 * @param errorStatus
	 * @param isAdd
	 * @param locationId
	 * @param locationCode
	 * @param locationName
	 * @param cityId
	 * @param address
	 * @param createTime
	 * @param ftyId
	 * @param status
	 * @param freezeInput
	 * @param freezeOutput
	 * @param enabled
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addOrUpdateLocation(int errorCode, String errorString, boolean errorStatus, boolean isAdd,
			String locationId, String locationCode, String locationName, String cityId, String address,
			String createTime, String ftyId, String status, String freezeInput, String freezeOutput, String enabled)
			throws Exception;

	/**
	 * 通过库存地点编码和工厂id获取库存地点id
	 * 
	 * @author 刘宇 2018.02.28
	 * @param locationCode
	 * @param ftyId
	 * @return
	 */
	DicStockLocation getLocationIdByLocationCodeAndFtyId(String locationCode, String ftyId);

	Map<String, Object> getLocationById(Integer ftyId, CurrentUser user);
}
