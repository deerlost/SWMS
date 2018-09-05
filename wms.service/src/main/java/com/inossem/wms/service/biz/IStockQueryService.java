package com.inossem.wms.service.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.vo.MatCodeVo;

import net.sf.json.JSONObject;

/**
 * 库存分页查询功能接口
 * 
 * @author 刘宇 2018.02.09
 *
 */
public interface IStockQueryService {
	/**
	 * 仓位库存分页查询
	 * 
	 * @author 刘宇 2018.02.009
	 * @param utilMatCodes
	 * @param matCode
	 * @param matName
	 * @param specStockCode
	 * @param inputDateBegin
	 * @param inputDateEnd
	 * @param positionCodeBegin
	 * @param positionCodeEnd
	 * @param specStockName
	 * @param areaCodeBegin
	 * @param areaCodeEnd
	 * @param specStock
	 * @param status
	 * @param ftyId
	 * @param pageIndex
	 * @param locationId
	 * @param pageSize
	 * @param total
	 * @return
	 */
	/*List<Map<String, Object>> listStockPositionOnPaging(List<MatCodeVo> utilMatCodes, String matCode, String matName,
			String specStockCode, String inputDateBegin, String inputDateEnd, String positionCodeBegin,
			String positionCodeEnd, String specStockName, String areaCodeBegin, String areaCodeEnd, String specStock,
			String status, String ftyId, int pageIndex, String locationId, int pageSize, int total, boolean paging)
			throws Exception;
*/
	
	List<Map<String, Object>> listStockPositionOnPaging(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 * 批次库存分页查询
	 * 
	 * @author 刘宇 2018.02.01
	 * @param utilMatCodes
	 * @param matCode
	 * @param matGroupCodeBegin
	 * @param matGroupCodeEnd
	 * @param specStockCode
	 * @param inputDateBegin
	 * @param inputDateEnd
	 * @param matName
	 * @param specStockName
	 * @param ftyId
	 * @param locationId
	 * @param specStock
	 * @param status
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	/*List<Map<String, Object>> listStockBatchOnPaging(List<MatCodeVo> utilMatCodes, String matCode,
			String matGroupCodeBegin, String matGroupCodeEnd, String specStockCode, String inputDateBegin,
			String inputDateEnd, String matName, String specStockName, String ftyId, String locationId,
			String specStock, String status, int pageIndex, int pageSize, int total, boolean paging ,List<Integer> locationIds) throws Exception;
	*/
	
	List<Map<String, Object>> listStockBatchOnPaging(Map<String, Object> map) throws Exception;

	/**
	 * 出入库分页查询
	 * 
	 * @author 刘宇 2018.02.24
	 * @param createTimeBegin
	 * @param createTimeEnd
	 * @param createUser
	 * @param moveTypeCode
	 * @param ftyId
	 * @param locationId
	 * @param specStockCode
	 * @param matCode
	 * @param matName
	 * @param bizTypes
	 * @param utilMatCodes
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listOutAndInStockOnPaging(String createTimeBegin, String createTimeEnd, String createUser,
			String moveTypeCode, String ftyId, String locationId, String specStockCode, String matCode, String matName,
			String bizTypes, List<MatCodeVo> utilMatCodes, String referReceiptCode, int pageIndex, int pageSize,
			int total, boolean paging) throws Exception;

	/**
	 * 库龄考核分页查询
	 * 
	 * @author 刘宇 2018.02.25
	 * @param dateBegin
	 * @param dateEnd
	 * @param checkDate
	 * @param ftyId
	 * @param locationId
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listStockDaysOnPaging(String dateBegin, String dateEnd, String checkDate, String ftyId,
			String locationId, int pageIndex, int pageSize, int total, boolean paging) throws Exception;

	/**
	 * 库龄考核集合里面加上移动平均价和库存金额
	 * 
	 * @param re
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getNewMaps(JSONObject re, List<Map<String, Object>> list) throws Exception;

	JSONObject getStockDaysChart(Map<String, Object> param);

	/**
	 * 查询库存积压
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getStockDaysList(Map<String, Object> param);

	int getExcelData(String realPath, String fileName, String folder) throws Exception;

	public BatchMaterial getBatchMaterial(BatchMaterial bm) throws Exception;
	
	public  Map<String,Object> getBaseInfo(CurrentUser cUser) ;
	
	public  List<Map<String,Object>> selectWarrantyDateOnpaging(Map<String,Object> param) ;

	List<Map<String, Object>> excelStockPositionOnPaging(Map<String, Object> map) throws Exception;
	
	
	List<Map<String,Object>> selectWarrantyDateSnMassage(Map<String,Object> map);

	int updateStatus(Map<String, Object> map) throws Exception;
	
	JSONObject pdaProductPicture();
	 
	List<Map<String,Object>> selectPersontByProduct(Integer product_line_id) throws Exception;
	 

    List<Map<String ,Object>> selectWarehouseListByWhId(Integer whId);
	 
    Map<String,Object> selectFirstView();
		
    Map<String,Object> selectFirstReport();	
		
    List<Map<String,Object>> selectDetail(Integer searchType);
		 
    List<Map<String,Object>> selectDetailMat(Map<String, Object> map);
	 
	BigDecimal selectALLPositionQty(Map<String,Object> param);

	BigDecimal selectALLBatchQty(Map<String, Object> param);

	List<Map<String, Object>> selectFirstViewStockDays(Map<String, Object> map);

	List<Map<String, Object>>  selectSecondViewStockDays(Map<String, Object> map);
			
	List<Map<String, Object>> selectSecondViewTotalAmount(Map<String, Object> map);
			
	List<Map<String, Object>>   selectThirdViewStockDays(Map<String, Object> map);
	
	Map<String, Object> listStockPositionOnPagingCw(Map<String, Object> map) throws Exception;
}
