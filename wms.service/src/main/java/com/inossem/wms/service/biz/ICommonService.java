package com.inossem.wms.service.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.FreezeCheck;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.dic.*;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.SupplierVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface ICommonService {

	List<DicWarehouse> selectAllWarehouse();

	/**
	 * 
	 * @param ftyId
	 *            工厂Id
	 * @return
	 * @throws Exception
	 */
	String getPostDate(Integer ftyId) throws Exception;

	/**
	 * 
	 * 获取移动类型
	 * 
	 * @return
	 * @throws Exception
	 */
	List<DicMoveType> getMoveTypeByReceiptType(Byte receiptType) throws Exception;

	/**
	 * 移动原因
	 * 
	 * @param moveTypeId
	 * @return
	 * @throws Exception
	 */
	List<DicMoveReason> getReasonByMoveTypeId(Integer moveTypeId) throws Exception;

	/**
	 * 查询供应商
	 * @param supplier
	 * @return
	 * @throws Exception
     */
	List<SupplierVo> getSupplierList(SupplierVo supplier) throws Exception;

	FreezeCheck checkWhPosAndStockLoc(List<Map<String, String>> listParam);

	/**
	 * 修改批次库存通用方法
	 * @param userId
	 * @param bizMaterialDocHead
	 * @param list
	 * @return
     * @throws Exception
     */
	boolean modifyStockBatchByMaterialDoc(String userId, BizMaterialDocHead bizMaterialDocHead,
			List<BizMaterialDocItem> list) throws Exception;

	/**
	 * 修改仓位库存SN库存通用方法
	 * @param stockPosition
	 * @param list
	 * @return
	 * @throws Exception
     */
	boolean modifyStockPositionAndSn(StockPosition stockPosition, List<StockSn> list) throws Exception;

	/**
	 * 根据用户id获取库存地点
	 * 
	 * @param userId
	 * @return
	 */
	List<RelUserStockLocationVo> getStockLocationForUser(String userId);

	/**
	 * 获取新的单据code
	 * 
	 * @param seqName
	 * @return
	 * @throws Exception
	 */
	String getNextReceiptCode(String seqName) throws Exception;

	/**
	 * 获取批次
	 * @return
	 * @throws Exception
     */
	Long getBatch() throws Exception;

	int oaPushSendTodo(Map<String, Object> param);

	int addReceiptUser(int receiptId, int receiptType, String userId);

	int addReceiptUser(int receiptId, int receiptType, String userId, int roleId);

	int removeReceiptUser(int receiptId, int receiptType);

	JSONArray getReceiptUserForMatReq(int matReqId, int receiptType);

	List<ApproveUserVo> getReceiptUser(int receiptId, int receiptType);

	boolean taskDefaultApprove(int receiptId, byte receiptType, String processInstanceId, String approvePerson);

	/**
	 * 是否角色审批流程的最后一个审批人
	 * 
	 * @param receipt_id
	 * @param processInstanceId
	 * @return
	 */
	boolean lastRoleTask(int receipt_id, String processInstanceId, String approve_person);

	/**
	 * 获取经办人角色
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, String>> getOperatorRoles() throws Exception;

	/**
	 * 获取经办类型
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, String>> getOperatorTypes() throws Exception;

	/**
	 * 获取经办人
	 * 
	 * @return
	 * @throws Exception
	 */
	List<User> getOperatorUsers(User user) throws Exception;

	/**
	 * 获取人员
	 * 
	 * @return
	 * @throws Exception
	 */
	List<User> getUsers(User user) throws Exception;

	/**
	 * 代办完成
	 * 
	 * @date 2017年10月24日
	 * @author 高海涛
	 */
	int oaPushSetTodoDone(Map<String, Object> param);

	/**
	 * 查询批次信息及特性
	 * 
	 * @param batch
	 * @param ftyId
	 * @param matId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBatchSpecList(Long batch, Integer ftyId, Integer matId) throws Exception;

	JSONArray listFtyLocationForUser(String userId,String ftyType) throws Exception;

	JSONArray listFtyLocationForBoardId(int boardId) throws Exception;

	JSONArray listLocationForUser(String userId,String ftyType) throws Exception;

	JSONObject listBoardFtyAndLocationForUser(String userId) throws Exception;

	List<DicBatchSpec> getFixedBatchMaterialSpec(BatchMaterial batchMaterial, List<DicBatchSpec> batchSpecList)
			throws Exception;

	List<DicMoveType> listMovTypeAndReason(Byte receiptType) throws Exception;

	/**
	 * 初始化审批流程
	 * 
	 * @param receiptId
	 * @param receiptType
	 * @param receiptCode
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	String initDefaultWf(int receiptId, byte receiptType, String receiptCode, String createUserId) throws Exception;

	/**
	 * 初始化角色审批流程
	 * 
	 * @param receiptId
	 * @param receiptType
	 * @param receiptCode
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	String initRoleWf(int receiptId, byte receiptType, String receiptCode, String createUserId) throws Exception;

	/**
	 * 
	 * @param batch
	 * @param matId
	 * @param ftyId
	 * @param locationId
	 * @param updateQty
	 * @param areaId
	 * @param positionId
	 * @throws Exception
	 */
	public void updateStockTaskReq(Long batch, Integer matId, Integer ftyId, Integer locationId, BigDecimal updateQty,
			Integer areaId, Integer positionId) throws Exception;

	/**
	 * by wang.ligang 库存盘点初始化角色审批流程，
	 * 
	 * @param receiptId
	 * @param receiptType
	 * @param receiptCode
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	String initStocktakeWf(int receiptId, byte receiptType, String receiptCode, String createUserId) throws Exception;

	void bufferPrice() throws Exception;

	void bufferERPBatch() throws Exception;
	
	int taskRoleApprove1(int receiptId, byte receiptType, String processInstanceId, String approvePerson)
			throws Exception;

	/**
	 * 通过单据id和单据类型查找物料凭证 刘宇 2018.04.18
	 * 
	 * @author ly 2018.04.18
	 * @param referReceiptId
	 * @param matDocType
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listBizMaterialDocHeadByRefereceiptIdAndMatDocType(int referReceiptId, int matDocType)
			throws Exception;
	
	List<DicPackageType> listPackageTypeByMatId(Integer matId) throws Exception;
	
	/**
	 * 
	 * @param map 
	 * matId 
	 * locationId 
	 * batch 
	 * status 
	 * createUserId 
	 * debitCredit (H减 S加)
	 * qty
	 * @throws Exception
	 */
	void modifyStockBatch(Map<String, Object> map) throws Exception;
	
	void modifyStockBatchCanMinus(Map<String, Object> map) throws Exception;
	
	void modifyStockBatchCanNull(Map<String, Object> map) throws Exception;
	
	
	void modifyStockBatchOnSyn(Map<String, Object> map) throws Exception;

	/**
	 * 修改仓位库存
	 * @param param
	 * @return
	 * @throws Exception
     */
	boolean modifyStockPosition(StockPosition param) throws Exception ;
	
	/**
	 * 修改仓位库存 (不出现负库存)
	 * @param param
	 * @return
	 * @throws Exception
     */
	boolean modifyStockPositionNotNull(StockPosition param) throws Exception ;
	
	/**
	 * 修改仓位库存
	 * @param param
	 * @return
	 * @throws Exception
     */
	boolean modifyStockPositionOnSyn(StockPosition param) throws Exception ;
	
	/**
	 * 修改仓位库存
	 * @param param
	 * @return
	 * @throws Exception
     */
	boolean modifyStockPositionOnSynByDelete(StockPosition param) throws Exception ;
	
	/**
	 * 根据作业单修改仓位库存
	 * @param taskItemList
	 *  作业单行项目
	 * @param positionList
	 *  作业单仓位list
	 * @throws Exception
	 */
	void updateStockPositionByTask(List<BizStockTaskItemCw> taskItemList, List<BizStockTaskPositionCw> positionList,Byte stockTypeId,boolean isUrgent) throws Exception;
	
	/**
	 * @param receiptType
	 * 请求单类型 下架  上架
	 * 根据作业请求单修改仓位库存
	 * @param taskReqItemList
	 *  作业单行项目
	 * @param positionList
	 *  作业单仓位list
	 * @throws Exception
	 */
	void updateStockPositionByTaskReq(Byte receiptType,List<BizStockTaskReqItem> taskReqItemList, List<BizStockTaskPositionCw> positionList,Byte stockTypeId,boolean isUrgent) throws Exception;
	
	
	DicWarehousePallet newPallet(String userId) throws Exception;
	
	/**
	 * 获取班次
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getClassTypeList() throws Exception;
	
	/**
	 * 查询仓位库存 （根据 matId packageTypeId productionBatch erpBatch 分组）
	 * @param map
	 * matCode 
	 * matName
	 * productionBatch
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getStockGroupByCondition(Map<String, Object> map) throws Exception;

	/**
	 * 查询仓位库存物料
	 * 
	 * ftyId:工厂id 必填
	 * loactionId:库存地点id
	 * stockTypeId: 1.erp库存2.instock库存
	 * keyword1:物料code或描述
	 * keyword2: erq 或product批次
	 * 
	 * @param map      
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getMatListByBatch(Map<String, Object> map) throws Exception;
	
	
	
	/**
	 * 查询生产订单详细信息
	 * 
	 * 
	 * @param purchase_order_code      
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectProduceOrderInfo(String purchase_order_code);

	/**
	 * 获取产品线集合以及装置集合
	 * [
	 * 	{
	 *  	 productLine:[installation,installation]
	 * 	}
	 * ]
	 * @param userId
	 * @return
     */
	List<DicProductLine> selectProductLineListAndDicInstallationList(String userId);

	/**
	 * 获取当前班次
	 *
	 * @return
	 */
	Integer selectNowClassType();
	
	/**
	 * 查询仓位库存物料
	 * 
	 * ftyId:工厂id 必填
	 * loactionId:库存地点id
	 * stockTypeId: 1.erp库存2.instock库存
	 * keyword1:物料code或描述
	 * keyword2: erq 或product批次
	 * isDefault 0
	 * @param map      
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getMatListByPosition(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getTaskMatListByPosition(Map<String, Object> map) throws Exception;

	void modifyStockOccupancy(StockOccupancy stockOccupancy, String type) throws WMSException;
	
	/**
	 * 查询物料erp批次集合
	 * [{
	 * 	erp_batch:1sb15,erp_batch_name:1sb15,mat_id:4
	 * }]
	 * @param matId
	 * @param ftyId
	 * @return
	 */
	List<Map<String,Object>> getErpBatchList(Integer matId,Integer ftyId);
	
	List<Map<String, Object>> getDataFromOracle(String sqlStr) throws Exception;
	
	/**
	 * 插入初期批次库存
	 * @throws Exception
	 */
	void insertStockBatchBegin() throws Exception;
	
	JSONArray getMatPriceFromOracle(List<String> matCodeList, String ftyCode) throws Exception;

	List<DicSupplier> synSupplier(String keyword) throws Exception;
	
	int synErpBatch(List<String> matCodeList) throws Exception;

	void synStockPositionOnUrgent(List<BizStockTaskItemCw> taskItemList, List<BizStockTaskPositionCw> positionList,Byte stockTypeId) throws Exception;
	
	List<Map<String, Object>> getSynStockFromOracle(Integer ftyId,Integer locationId) throws Exception;
	
	List<Map<String, String>> getShippingTypeList() throws Exception;
	//根据用户查询车辆信息
    List<Map<String,Object>> selectVehicleByProductId(String userId);
    //根据用户查询司机信息
    List<Map<String,Object>> selectDriverByProductlineId(String userId);
    
    
    /**
     * 上传文件
     * @param referReceitpId
     * @param referReceiptType
     * @param createUser
     * @param fileList
     * @throws Exception
     */
    public void saveFileVoList(Integer referReceitpId,Byte referReceiptType,String createUser,List<BizReceiptAttachmentVo> fileList) throws Exception;
    
    
    /**
     * 
     * @param referReceitpId
     * @param referReceiptType
     * @param createUser
     * @param fileList
     * @throws Exception
     */
     public void saveFileAry(Integer referReceitpId,Byte referReceiptType,String createUser,JSONArray fileList) throws Exception;
    
    /**
     * 上传文件
     * @param referReceitpId
     * @param referReceiptType
     * @param createUser
     * @param fileList
     * @throws Exception
     */
    public void saveFileList(Integer referReceitpId,Byte referReceiptType,String createUser,List<BizReceiptAttachment> fileList) throws Exception;
    
    /**
     * 删除mes同步记录
     * @param days
     */
    void deleteDateByDays(Integer days);

    List<Map<String, Object>> selectProduceOrderInfo2(String purchase_order_code);
    
   
    List<DicBoard> getBordList();
    
}
