package com.inossem.wms.dao.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.vo.BizStockOutputHeadVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BizStockOutputHeadMapper {

	// 根据ID获取出库头信息
	BizStockOutputHead selectByPrimaryKey(Map<String, Object> param);

	// 根据ID获取出库头信息扩展
	BizStockOutputHeadVo selectHeadByOrderId(Map<String, Object> param);

	// 新增出库头信息
	int insertSelective(BizStockOutputHead record);

	// 修改出库头信息
	int updateByPrimaryKeySelective(BizStockOutputHead record);

	/****************************************************
	 * 领料单相关
	 ****************************************************/
	// 查询领料单列表
	List<Map<String, Object>> selectMatReqListOnPaging(Map<String, Object> param);

	// 查询领料出库单列表
	List<Map<String, Object>> selectMatReqOutPutListOnPaging(Map<String, Object> param);

	// 根据领料单号和出库单号获取领料单头信息
	Map<String, Object> selectMatReqHeadById(Map<String, Object> param);

	// 根据领料单号获取领料明细信息
	List<Map<String, Object>> selectMatReqItemById(Map<String, Object> param);

	// 领料单相关联的出库单查询
	List<Map<String, Object>> selectMatReqRelevancyByid(Map<String, Object> param);

	// 根据出库单号获取领料出库单明细
	List<Map<String, Object>> selectOutPutOrderItemForMatReq(Map<String, Object> param);

	/****************************************************
	 * 共用方法
	 ****************************************************/

	// 查询出库列表
	List<Map<String, Object>> selectOrderListOnPaging(Map<String, Object> param);
	
	// 查询出库列表不去重
	List<Map<String, Object>> selectOrderListAllOnPaging(Map<String, Object> param);

	// 查询库存
	List<Map<String, Object>> selectStockData(Map<String, Object> param);

	/**
	 * 料单查询下的出库数量查询出库单信息
	 * 
	 * @author 刘宇 2018.02.03
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> selectOutputForMatReq(Map<String, Object> param);
	
	List<Map<String,Object>> selectOutputListByOrderCode(Map<String, Object> param);
	
	
	/****************************************************
	 * 调拨出库相关
	 ****************************************************/
	//查询调拨出库列表
	List<Map<String, Object>> selectAllocateOrderListOnPaging(Map<String, Object> param);
	
	//查询关联单据
	List<Map<String,Object>> selectInnerOrderForDBCK(Map<String, Object> param);
	
	//查询前置头信息
	Map<String,Object> selectInnerOrderMstById(Map<String, Object> param);
	
	//查询明细
	List<Map<String,Object>> selectInnerOrderDtlByAllot(Map<String, Object> param);
	
	//查询明细
	List<Map<String,Object>> selectInnerOrderDtlByDelivery(Map<String, Object> param);
	
	Map<String,Object> selectDbckMstByStockOutputId(String stockOutputId);
    
    List<Map<String,Object>> selectDbckDtlByStockOutputId(String stockOutputId);

    int selectAllotCount(Map<String, Object> paramMap);
    
    int selectDeliveryItemId(Map<String, Object> paramMap);
    
    int selectDeliveryCount(Map<String, Object> paramMap);
    
    int updateAllotMstById(Map<String, Object> paramMap);
    
    int updateAllotDtlById(Map<String, Object> paramMap);
    
    int updateDeliveryMstById(Map<String, Object> paramMap);
    
    int updateDeliveryDtlById(Map<String, Object> paramMap);
    
    String selectPostDateById(int matDocId);
    
    int selectCountForPostDate(Map<String, String> paramMap);
    
    // 根据领料单号出库单号获取领料出库单概览（领料退库）
 	List<Map<String, Object>> selectOutPutInfoByOutPutId(Map<String, Object> param);
 	
 	// 查询出库单库存地点是否是唯一
 	int selectCountByLocation(@Param("stockOutputId") Integer stockOutputId);
 	
 	//插入凭证日期与过账日期
 	int insertPostingDateAndDocTime(Map<String,Object> param);
 	
 	//销售出库销量统计
 	List<Map<String,Object>> selectChartData(Map<String,Object> param);
 	
 	//导出excel的数据
 	List<Map<String,Object>> selectSaleDataListForExcel(Map<String,Object> param);

 	BigDecimal selectChartYmax(Map<String,Object> param);

	List<Map<String,Object>> selectChartDataline(Map<String,Object> param);
 	
 	Integer getCountBySaleOrder(@Param("referReceiptCode") String referReceiptCode);
 	
 	List<Map<String,Object>> selectCargoInfoOnPaging(Map<String,Object> param);
 	
 	@Select("select \r\n" + 
 			"	sale.receive_code,\r\n" + 
 			"	sale.erp_batch,\r\n" + 
 			"	sale.qty,\r\n" + 
 			"	mat.mat_id,\r\n" + 
 			"	fty.fty_id,\r\n" + 
 			"	location.location_id,\r\n" + 
 			"	mat.unit_id\r\n" + 
 			" from tmp_sale sale \r\n" + 
 			"INNER JOIN dic_material mat on mat.mat_code=sale.mat_code\r\n" + 
 			"INNER JOIN dic_factory fty on fty.fty_code=sale.fty_code\r\n" + 
 			"INNER JOIN dic_stock_location location on location.location_code=sale.location_code")
 	List<Map<String,Object>> getTmpSaleList();
}