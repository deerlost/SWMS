package com.inossem.wms.service.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.vo.BizStockOutputHeadVo;
import net.sf.json.JSONObject;

/**
 * 出库
 * @author sw
 *
 */
public interface IOutPutCWService {
	
	/**
	 * 获取列表数据
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getOrderList(Map<String, Object> param);
	
	/**
	 * 获取列表数据
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getOrderListAll(Map<String, Object> param);
	
	/**
	 * 查询详情
	 * @param stock_output_id
	 * @return
	 * @throws Exception
	 */
	BizStockOutputHeadVo getOrderView(Integer stock_output_id) throws Exception;

	/**
	 * 详情格式化
	 * @param head
	 * @return
	 * @throws Exception
     */
	Map<String,Object> getOrderViewFormat(BizStockOutputHeadVo head)throws Exception;
	
	/**
	 * 查询交货单详情
	 * @param code
	 * @param type
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getDeliverOrderView(String code,Byte type)throws Exception;
	
	/**
	 * 删除单据
	 * @param stockOutputId
	 * @param userId
	 * @return
	 */
	String deleteOutputOrder(int stockOutputId, String userId)throws Exception;
	
	/**
	 * 保存单据信息
	 * @param jsonData
	 * @param user
	 * @param isPDA
	 * @param orderType
	 * @return
	 * @throws Exception
	 */
	JSONObject saveOutputOrderData(JSONObject jsonData, CurrentUser user, boolean isPDA, byte orderType)throws Exception;

	/**
	 * 修改wms单据数据与sap数据
	 * @param stockOutputId
	 * @param postingDate
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String udateWmsAndSapData(Integer stockOutputId,String postingDate,String userId)throws Exception;
	
	/**
	 * 冲销并且修改单据
	 * @param stockOutputId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String writeOffAndUpdate(Integer stockOutputId, String userId) throws Exception;
	
	/**
	 * 查询班次集合
	 * @return
	 */
	Map<String, Object> selectClassTypeList()throws Exception;
	
	/**
	 * 查询移动类型
	 * @param type
	 * @return
	 */
	List<DicMoveType> selectMoveTypeList(Byte type)throws Exception;
	
	/**
	 * 查询出库物料库存信息
	 * @param param
	 * @return
	 */
	List<Map<String,Object>> selectMatListPastion(Map<String,Object> param)throws Exception;
	
	/**
	 * 完成出库
	 * @param stockOutputId
	 * @param postingDate
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	JSONObject saveOrderOutput(Integer stockOutputId,String postingDate,String createUserId)throws Exception;
	
	/**
	 * 修改出库单状态
	 * @param stockOutputId
	 * @param userId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	int updateOutPutHead(Integer stockOutputId,String userId,Byte status)throws Exception;
	
	/**
	 * 批量修改出库单状态
	 * @param stockOutputIds
	 * @param userId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	int updateOutPutHead(List<Integer> stockOutputIds,String userId,Byte status)throws Exception;
	
	/**
	 * (通用)出库sap过账成功后修改库存与单据状态 
	 * @param sapReturn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String updateOutPutOrder(Map<String,Object> sapReturn,String userId)throws Exception;

	/**
	 * (通用)出库mes同步信息
	 * @param stockOutputId
	 * @param userId
	 */
	void saveOutPutToMes(Integer stockOutputId,String userId);
	
	/**
	 * 直发出库进行SAP转储过账
	 * @param stockOutputId
	 * @param postingDate
	 * @param userId
     * @return
     */
	Map<String,Object> saveSapTranSport(Integer stockOutputId,String postingDate,String userId)throws Exception;
	
	/**
	 * SAP转储过账后修改
	 * @param sapRetrun
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String saveToTranSport(Map<String,Object> sapRetrun,String userId)throws Exception;
	
	/**
	 * 转储mes同步信息
	 * @param stockOutputId
	 * @param userId
	 */
	void saveTranSportToMes(Integer stockOutputId,String userId);
	
	/**
	 * 获取物料
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> selectMaterialList(Map<String, Object> param);
	
	/**
	 * 校验紧急出库交货单数据是否与交货单一致
	 * @param referReceiptCode
	 * @param stockOutputId
	 * @return
	 * @throws Exception
	 */
	Boolean checkOrderBySapOrder(String referReceiptCode,Integer stockOutputId)throws Exception;
	
	/**
	 * 查询出库销量chart的数据
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectChartData(String startDate,String endDate)throws Exception;
	
	/**
	 *  销售出库销量统计产品线
	 * @param param
	 * @return
	 */
	List<Map<String,Object>> selectChartDataline(Map<String,Object> param);
	
	/**
	 * 销售出库y轴最大值
	 * @param param
	 * @return
	 */
	BigDecimal selectChartYmax(Map<String,Object> param);
	
	/**
	 * 查询出库销量导出的数据
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> selectSaleDataListForExcel(Map<String,Object> param);
	
	/**
	 * 查询车信息
	 * @param userId
	 * @return
	 */
	Map<String,Object> selectDriverInfo(String userId);
	
	/**
	 * 测试冲销
	 * @param code
	 * @return
	 */
	String testWriteOff(String code)throws Exception;
	
	/**
	 * 同步mes
	 * @param stockOutputId
	 * @param businessType
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	JSONObject saveMesAgain(Integer stockOutputId,String businessType,String userId)throws Exception;
	
	/**
	 * mes冲销
	 * @param stockOutputId
	 * @param userId
	 * @return
	 */
	String mesWriteOff(Integer stockOutputId,String userId);
	
	/**
	 * 查询带出库配货单
	 * @param code
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getCargoOrderList(String code,String userId);

	/**
	 * 交货单配货情况报表
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> selectCargoInfoOnPaging(Map<String,Object> param);
	
	void test1() throws Exception;

}
