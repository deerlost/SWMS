package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.biz.BizMatReqItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.dic.DicMaterialReqBizType;
import com.inossem.wms.model.dic.DicMaterialReqMatType;
import com.inossem.wms.model.page.PageParameter;
import com.inossem.wms.model.vo.BizMatReqHeadVo;
import com.inossem.wms.model.vo.BizMatReqItemVo;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.model.vo.StockBatchVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IMatReqService {

	// 查询领料工厂【根据相同板块【仅煤炭】下的公司的工厂】
	List<DicFactoryVo> getMatReqFactoryList() throws Exception;

	// 查询领料工厂
	List<DicFactory> getMatReqFactoryListByCorpId(int corpId) throws Exception;

	// 获取领料单列表
	List<BizMatReqHeadVo> getMatReqList(String userId, int receiptType, String[] status, String query, int boardId,
			PageParameter parameter) throws Exception;

	// 查询领料单业务类型
	List<DicMaterialReqBizType> getMatReqBizTypeByReceiptTypeAndBoardId(int boardId) throws Exception;

	// 查询领料单物料类型
	List<DicMaterialReqMatType> getMatReqMatTypeByBoardId(int receiptType, int boardId) throws Exception;

	BizMatReqHeadVo getMatReqHead(int matReqId, byte receiptType) throws Exception;

	List<BizMatReqItemVo> getMatReqItemList(int matReqId) throws Exception;

	boolean lastTask(int receiptId, String processInstanceId, String approvePerson) throws Exception;

	/**
	 * 创建预留单,在最后审批人审批之后
	 * 
	 * @param receiptId
	 * @param approvePerson
	 * @return
	 * @throws Exception
	 */
	String createReserve(int receiptId, String approvePerson) throws Exception;

	List<StockBatchVo> getStockBatch(int locationId, String condition, String specStock, String matType)
			throws Exception;

	JSONArray getReferencePrice(String[] matCodes, int ftyId, String userId) throws Exception;

	int updateStatus(BizMatReqHead bizMatReqHead) throws Exception;

	int saveMatReq(boolean newMatReq, BizMatReqHead bizMatReqHead, List<BizMatReqItem> bizMatReqItemList,
			List<BizReceiptAttachment> bizReceiptAttachmentList) throws Exception;

	String initWF(BizMatReqHead head, int boardId, String url) throws Exception;

	boolean removeMatReq(int matReqId) throws Exception;

	boolean closeMatReq(int matReqId) throws Exception;

	/**
	 * 料单查询下的出库数量查询出库单信息
	 * 
	 * @author 刘宇 2018.02.03
	 * @param matReqId
	 * @param matReqRid
	 * @return
	 */
	List<Map<String, Object>> listOutputForMatReq(String matReqId, String matReqRid);

	/**
	 * 领料单分页查询
	 * 
	 * @author 刘宇 2018.02.01
	 * @param utilMatCodes
	 * @param matReqFtyId
	 * @param matReqMatTypeId
	 * @param receiveFtyId
	 * @param useDeptCode
	 * @param matReqBizTypeId
	 * @param createUser
	 * @param createTimeBegin
	 * @param createTimeEnd
	 * @param matCode
	 * @param locationId
	 * @param matName
	 * @param receiptType
	 * @param boardId
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @param paging
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listBizMatReqHeadOnPaging(List<MatCodeVo> utilMatCodes, String matReqFtyId,
			String matReqMatTypeId, String receiveFtyId, String useDeptCode, String matReqBizTypeId, String createUser,
			String createTimeBegin, String createTimeEnd, String matCode, String locationId, String matName,

			String matReqCode, int receiptType, int boardId, int pageIndex, int pageSize, int total, boolean paging)
			throws Exception;

	String upLoadExcel(MultipartFile fileInClient, String realPath) throws Exception;

	JSONObject getExcelData(String realPath, String fileName, String ftyCode, CurrentUser user) throws Exception;

}
