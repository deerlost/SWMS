package com.inossem.wms.service.biz.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.dao.biz.BizMatReqHeadMapper;
import com.inossem.wms.dao.biz.BizMatReqItemMapper;
import com.inossem.wms.dao.biz.BizReceiptAttachmentMapper;
import com.inossem.wms.dao.biz.BizStockOutputHeadMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialReqBizTypeMapper;
import com.inossem.wms.dao.dic.DicMaterialReqMatTypeMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.dao.wf.FlowableMapper;
import com.inossem.wms.dao.wf.WfReceiptHistoryMapper;
import com.inossem.wms.dao.wf.WfReceiptMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.biz.BizMatReqItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.dic.DicMaterialReqBizType;
import com.inossem.wms.model.dic.DicMaterialReqMatType;
import com.inossem.wms.model.enums.EnumMatReqBizType;
import com.inossem.wms.model.enums.EnumMatReqDeviceType;
import com.inossem.wms.model.enums.EnumMatReqStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumTemplateType;
import com.inossem.wms.model.page.PageParameter;
import com.inossem.wms.model.vo.BizMatReqHeadVo;
import com.inossem.wms.model.vo.BizMatReqItemVo;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.model.vo.StockBatchVo;
import com.inossem.wms.model.wf.WfReceipt;
import com.inossem.wms.model.wf.WfReceiptHistory;
import com.inossem.wms.model.wf.WfReceiptKey;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IMatReqService;
import com.inossem.wms.service.intfc.IMatReq;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("matReqService")
public class MatReqServiceImpl implements IMatReqService {

	private static Logger logger = LoggerFactory.getLogger(MatReqServiceImpl.class);

	@Autowired
	private DicFactoryMapper dicFactorylDao;

	@Autowired
	private BizMatReqHeadMapper bizMatReqHeadDao;

	@Autowired
	private UserMapper userDao;

	@Autowired
	private BizMatReqItemMapper bizMatReqItemDao;

	@Autowired
	private BizReceiptAttachmentMapper bizReceiptAttachmentDao;

	@Autowired
	private DicMaterialReqBizTypeMapper dicMaterialReqBizTypeDao;

	@Autowired
	private DicMaterialReqMatTypeMapper dicMaterialReqMatTypeDao;

	@Autowired
	private FlowableMapper flowableDao;

/*	@Autowired
	private RuntimeService runtimeService;*/

	/*@Autowired
	private TaskService taskService;*/

	/*@Autowired
	private FormService formService;*/

	@Autowired
	private StockBatchMapper stockBatchDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private IMatReq matReqImpl;

	@Autowired
	private WfReceiptMapper wfReceiptDao;

	@Autowired
	private WfReceiptHistoryMapper wfReceiptHistoryDao;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private BizStockOutputHeadMapper bizStockOutputHeadDao;

	@Override
	// 查询领料工厂,根据工厂所在板块2
	public List<DicFactoryVo> getMatReqFactoryList() {
		return dicFactorylDao.selectMatReqFactoryList();
	}

	@Override
	// 查询领料工厂,根据公司
	public List<DicFactory> getMatReqFactoryListByCorpId(int corpId) {
		return dicFactorylDao.selectByCorpId(corpId);
	}

	@Override
	// 获取领料单列表
	public List<BizMatReqHeadVo> getMatReqList(String userId, int receiptType, String[] status, String query,
			int boardId, PageParameter parameter) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("receipt_type", receiptType);
		map.put("statusAry", status);
		map.put("query", query);
		map.put("paging", true);
		map.put("pageIndex", parameter.getPageIndex());
		map.put("pageSize", parameter.getPageSize());
		map.put("totalCount", parameter.getTotalCount());
		map.put("sortAscend", parameter.isSortAscend());
		map.put("sortColumn", parameter.getSortColumn());
		// 在最新情况下在建期只有煤制油板块 2017-09-21
		if (receiptType == EnumReceiptType.MAT_REQ_PRODUCTION.getValue()) {
			map.put("boardId", boardId);
			// map.put("pageNo", pageNo);
			// map.put("pageSize", pageSize);

			return bizMatReqHeadDao.selectMatReqListOnPaging(map);
		} else {
			return bizMatReqHeadDao.selectMatReqListOnPaging(map);
		}
	}

	@Override
	// 查询领料业务类型
	public List<DicMaterialReqBizType> getMatReqBizTypeByReceiptTypeAndBoardId(int boardId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("receiptType", receiptType);
		map.put("boardId", boardId);
		return dicMaterialReqBizTypeDao.selectByReceiptTypeAndBoardId(map);
	}

	@Override
	// 查询领料物料类型
	public List<DicMaterialReqMatType> getMatReqMatTypeByBoardId(int receiptType, int boardId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("receiptType", receiptType);
		map.put("boardId", boardId);
		return dicMaterialReqMatTypeDao.selectByReceiptTypeAndBoardId(map);
	}

	@Override
	// 查询领料单抬头信息
	public BizMatReqHeadVo getMatReqHead(int matReqId, byte receiptType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matReqId", matReqId);
		map.put("receiptType", receiptType);
		return bizMatReqHeadDao.selectBizMatReqHeadVo(map);
	}

	@Override
	public List<BizMatReqItemVo> getMatReqItemList(int matReqId) {
		return bizMatReqItemDao.selectBizMatReqItemVo(matReqId);
	}

	@Override
	// 是否最后审批人,由于伊泰领料单最后审批人是财务,需要财务审批人添加一些特殊信息,如移动类型等
	public boolean lastTask(int receiptId, String processInstanceId, String approvePerson) {
		// 判断是否最后审批任务
		BizMatReqHead bizMatReqHead = bizMatReqHeadDao.selectByPrimaryKey(receiptId);
		// 没有该领料单单
		if (bizMatReqHead == null) {
			return false;
		}

		// 获取当前任务
		/*Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskCandidateUser(approvePerson)
				.singleResult();*/
		if (true) {
			// throw new NullPointerException("没有这个任务");
			return false;
		} else {

			//List<FormProperty> list = formService.getTaskFormData(task.getId()).getFormProperties();
			/*if (list != null && list.size() > 0) {
				for (FormProperty formProperty : list) {
					if ("lastuser".equalsIgnoreCase(formProperty.getId())
							&& "true".equalsIgnoreCase(formProperty.getValue())) {
						return true;
					}
				}
			}*/
		}
		return false;
	}

	@Override
	// 创建预留单,在最后一人审批之后
	public String createReserve(int receiptId, String approvePerson) throws Exception {
		// 审批通过,则需要添加 使用部门 移动类型 成本对象
		BizMatReqHeadVo bizMatReqHead = this.getMatReqHead(receiptId, EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
		if (EnumMatReqStatus.MAT_REQ_STATUS_APPROVED.getValue() != bizMatReqHead.getStatus()) {
			return "未审批通过无法创建预留单";
		}
		List<BizMatReqItemVo> list = this.getMatReqItemList(receiptId);
		List<Map<String, Object>> mapListForCheckWorkReceiptCode = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapForCheckWorkReceiptCode;

		for (BizMatReqItemVo itemVo : list) {
			mapForCheckWorkReceiptCode = new HashMap<String, Object>();
			mapForCheckWorkReceiptCode.put("mat_req_rid", String.valueOf(itemVo.getMatReqRid()));
			mapForCheckWorkReceiptCode.put("mat_code", itemVo.getMatCode());
			mapForCheckWorkReceiptCode.put("mat_req_fty_code",
					dictionaryService.getFtyCodeByFtyId(bizMatReqHead.getMatReqFtyId()));
			mapForCheckWorkReceiptCode.put("work_receipt_code", itemVo.getWorkReceiptCode());
			mapListForCheckWorkReceiptCode.add(mapForCheckWorkReceiptCode);
		}

		boolean sapLLD = true;// 存储是否可以调用sap领料单存储接口
		// 当"业务类型"选择为"大修/技改"时，先调用第29接口检查物料和工单号，
		if (EnumMatReqBizType.MAT_REQ_BIZ_TYPE_OVERHAUL.getValue() == bizMatReqHead.getMatReqBizTypeId()) {
			if (!UtilString
					.isNullOrEmpty(matReqImpl.checkWorkReceiptCode(mapListForCheckWorkReceiptCode, approvePerson))) {
				sapLLD = false;
			}
		}

		if (sapLLD) {
			// 提交领料单到sap,在sap创建对应的领料单
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("receiptId", bizMatReqHead.getMatReqId());
			map.put("receiptType", EnumReceiptType.MAT_REQ_PRODUCTION.getValue());

			User createUser = userDao.selectByPrimaryKey(bizMatReqHead.getCreateUser());
			User firstApproveUser = userDao.selectFirstApproveUserByReceiptIDAndType(map);
			// User lastApproveUser = userDao.selectByPrimaryKey(approvePerson);
			User lastApproveUser = userDao.selectLastApproveUserByReceiptIDAndType(map);
			String returnString = matReqImpl.submitMatReq(bizMatReqHead, list, createUser, firstApproveUser,
					lastApproveUser);

			// 需要调用sap接口,在sap创建对应的领料单
			if (UtilString.isNullOrEmpty(returnString)) {
				// 1010030032成本

				for (BizMatReqItemVo item : list) {
					// 添加sap的领料单编号和行号
					bizMatReqItemDao.updateReserve(item);
					// 这里只判断空,代表着没有这个返回值
					if (null != item.getPurchaseOrderCode() && null != item.getPurchaseOrderRid()) {
						// 添加sap对应的采购订单编号和行号
						bizMatReqItemDao.updatePurchaseOrder(item);
					}
				}

				// 肯定是最后一个审批人,没有判断返回值,直接修改的状态
				bizMatReqHead.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_COMPLETED.getValue());
				bizMatReqHeadDao.updateStatus(bizMatReqHead);
			}
			return returnString;
		} else {
			return null;
		}
	}

	@Override
	// 查询批次库存
	public List<StockBatchVo> getStockBatch(int locationId, String condition, String specStock, String matType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("werks", werks);
		map.put("locationId", locationId);
		map.put("condition", condition);
		if (specStock != null) {
			map.put("specStock", specStock);
		}
		if (matType != null) {
			map.put("matType", matType);
		}
		return stockBatchDao.selectStockBatchByCondition(map);
	}

	@Override
	// 参考价格获取
	public JSONArray getReferencePrice(String[] matCodes, int ftyId, String userId) throws Exception {
		// Map<Integer, String> map =
		// dictionaryService.getMatMapByIdList(matIdList);
		// List<String> matCodeList = new ArrayList<String>();
		// for (Integer matId : matIdList) {
		// String matCode = map.get(matId);
		// if (!UtilString.isNullOrEmpty(matCode)) {
		// matCodeList.add(matCode);
		// }
		// }
		String ftyCode = dictionaryService.getFtyCodeByFtyId(ftyId);

		return matReqImpl.getReferencePrice(matCodes, ftyCode, userId);
	}

	@Override
	// 修改状态
	public int updateStatus(BizMatReqHead bizMatReqHead) throws Exception {
		// 修改领料单状态
		return bizMatReqHeadDao.updateStatus(bizMatReqHead);
	}

	@Override
	// 保存领料单
	public int saveMatReq(boolean newMatReq, BizMatReqHead bizMatReqHead, List<BizMatReqItem> bizMatReqItemList,
			List<BizReceiptAttachment> bizReceiptAttachmentList) throws Exception {
		// 暂存领料单，根据领料单号判断是否新建
		if (newMatReq) {
			bizMatReqHeadDao.insertSelective(bizMatReqHead);
		} else {
			bizMatReqHeadDao.updateByPrimaryKeySelective(bizMatReqHead);
			bizMatReqItemDao.deleteByMatReqId(bizMatReqHead.getMatReqId());
			bizReceiptAttachmentDao.deleteByReceiptId(bizMatReqHead.getMatReqId());
		}

		for (BizMatReqItem item : bizMatReqItemList) {
			item.setMatReqId(bizMatReqHead.getMatReqId());
			bizMatReqItemDao.insertSelective(item);
		}

		for (BizReceiptAttachment bizReceiptAttachment : bizReceiptAttachmentList) {
			bizReceiptAttachment.setReceiptId(bizMatReqHead.getMatReqId());
			bizReceiptAttachmentDao.insertSelective(bizReceiptAttachment);
		}
		return bizMatReqHead.getMatReqId();
	}

	@Override
	// 初始化领料单审批
	public String initWF(BizMatReqHead head, int boardId, String url) throws Exception {
		String piid = "";

		User user = userDao.selectCurrentUserByPrimaryKey(head.getCreateUser());
		if (user == null) {
			return piid;
		}

		Map<String, Object> variables = new HashMap<String, Object>();
		// variables.put("type", EnumReceiptType.MAT_REQ_PRODUCTION);
		// variables.put("receipt_type", head.getReceiptType());
		// variables.put("mat_type", head.getMatReqMatTypeId());
		// variables.put("board_id", boardId);
		// variables.put("apply_fty", head.getApplyFtyId());
		// variables.put("receive_fty", head.getReceiveFtyId());
		// variables.put("mat_req_fty", head.getMatReqFtyId());
		// variables.put("portable", head.getIsPortable() ==
		// EnumMatReqDeviceType.MOBILE_DEVICE.getValue());
		// variables.put("group", user.getDepartment());
		variables.put("type", EnumReceiptType.MAT_REQ_PRODUCTION.getValue().byteValue());
		// variables.put("zllfl", head.getReceiptType() ==
		// EnumReceiptType.MAT_REQ_PRODUCTION.getValue() ? 1 : 2);
		variables.put("zwllx", head.getMatReqMatTypeId());
		variables.put("zgsbk", boardId);
		// variables.put("sqgc", head.getApplyFtyId());
		// variables.put("jsgc", head.getReceiveFtyId());
		// variables.put("llgc", head.getMatReqFtyId());
		variables.put("sqgc", dictionaryService.getFtyCodeByFtyId(head.getApplyFtyId()));
		variables.put("jsgc", dictionaryService.getFtyCodeByFtyId(head.getReceiveFtyId()));
		variables.put("llgc", dictionaryService.getFtyCodeByFtyId(head.getMatReqFtyId()));
		variables.put("portable", head.getIsPortable() == EnumMatReqDeviceType.MOBILE_DEVICE.getValue());
		variables.put("group", user.getDepartment());

		// 启动验收流程
		//ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("materialRequisition", variables);
		//piid = processInstance.getId();

		// 修改验收流程ID到验收单表
		head.setPiid(piid);
		bizMatReqHeadDao.updatePiid(head);

		WfReceiptKey key = new WfReceiptKey();
		key.setReceiptId(head.getMatReqId());
		key.setReceiptType(head.getReceiptType());
		WfReceipt wfReceipt = wfReceiptDao.selectByPrimaryKey(key);

		if (wfReceipt != null) {
			// 已经存在,移到历史记录中
			WfReceiptHistory wfReceiptHistory = new WfReceiptHistory();
			wfReceiptHistory.setReceiptId(wfReceipt.getReceiptId());
			wfReceiptHistory.setReceiptType(wfReceipt.getReceiptType());
			wfReceiptHistory.setReceiptCode(wfReceipt.getReceiptCode());
			wfReceiptHistory.setPiid(wfReceipt.getPiid());
			wfReceiptHistory.setReceiptUserId(wfReceipt.getReceiptUserId());
			wfReceiptHistory.setCreateTime(wfReceipt.getCreateTime());
			wfReceiptHistory.setModifyTime(wfReceipt.getModifyTime());
			// 添加历史,删除原数据
			wfReceiptHistoryDao.insertSelective(wfReceiptHistory);

			wfReceiptDao.deleteByPrimaryKey(key);
		}

		// 新建原数据
		wfReceipt = new WfReceipt();
		wfReceipt.setReceiptId(head.getMatReqId());
		wfReceipt.setReceiptType(head.getReceiptType());
		wfReceipt.setReceiptCode(head.getMatReqCode());
		wfReceipt.setPiid(piid);
		wfReceipt.setReceiptUserId(head.getCreateUser());
		wfReceiptDao.insertSelective(wfReceipt);

		this.pushSendTodo(head, piid, url);

		return piid;

	}

	private void pushSendTodo(BizMatReqHead head, String piid, String url) {

		List<String> spEmployerNumberlist = flowableDao.selectEmployerNumberForApproveUserByPiid(piid);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("modelId", head.getMatReqId()); // 领料单号
		param.put("modelName", "领料单审批");// 模块名
		param.put("subject", "领料单审批"); // 标题
		param.put("link", url);// 路径
		param.put("type", 1); // 固定 1
		JSONArray jsonList = new JSONArray(); // 人员信息 json
		for (String user_id : spEmployerNumberlist) {
			JSONObject js = new JSONObject();
			js.put("PersonNo", user_id);
			jsonList.add(js);
		}

		param.put("targets", jsonList.toString());
		commonService.oaPushSendTodo(param);
	}

	@Override
	// 逻辑删除领料单
	public boolean removeMatReq(int matReqId) throws Exception {
		bizMatReqHeadDao.deleteLogicalByPrimaryKey(matReqId);
		bizMatReqItemDao.deleteLogicalByMatReqId(matReqId);
		bizReceiptAttachmentDao.deleteLogicalByReceiptId(matReqId);
		return true;
	}

	@Override
	// 关闭领料单
	public boolean closeMatReq(int matReqId) throws Exception {
		BizMatReqHead head = new BizMatReqHead();
		head.setMatReqId(matReqId);
		head.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_CLOSED.getValue());
		bizMatReqHeadDao.updateStatus(head);
		return true;
	}

	/**
	 * 领料单查询 刘宇 2018.02.01
	 */
	@Override
	public List<Map<String, Object>> listBizMatReqHeadOnPaging(List<MatCodeVo> utilMatCodes, String matReqFtyId,
			String matReqMatTypeId, String receiveFtyId, String useDeptCode, String matReqBizTypeId, String createUser,
			String createTimeBegin, String createTimeEnd, String matCode, String locationId, String matName,
			String matReqCode, int receiptType, int boardId, int pageIndex, int pageSize, int total, boolean paging)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matReqFtyId", matReqFtyId);// 领料工厂
		map.put("matReqMatTypeId", matReqMatTypeId);// 物料类型
		map.put("receiveFtyId", receiveFtyId);// 接收工厂
		map.put("useDeptCode", useDeptCode);// 使用部门
		map.put("matReqBizTypeId", matReqBizTypeId);// 业务类型
		map.put("createUser", createUser);// 领料申请人
		SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");

		if (StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
			map.put("createTimeBegin", clsFormat.parse(createTimeBegin));// 创建日期起
			map.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));// 创建日期止
		} else if (StringUtils.hasText(createTimeBegin) && !StringUtils.hasText(createTimeEnd)) {
			map.put("createTimeBegin", clsFormat.parse(createTimeBegin));
			map.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeBegin), 1));
		} else if (!StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
			map.put("createTimeBegin", clsFormat.parse(createTimeEnd));
			map.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));
		}
		map.put("utilMatCodes", utilMatCodes);// 物料编码集合
		map.put("matCode", matCode);// 物料编码（判断用）
		map.put("locationId", locationId);// 库存地点
		map.put("matName", matName);// 物料描述
		map.put("matReqCode", matReqCode);// 领料单编号
		map.put("receiptType", receiptType);// 领料单类型 receiptType =
		// 33 ,33生产期 34在建期
		map.put("boardId", boardId);// 板块 boardId=2
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", paging);
		return bizMatReqHeadDao.selectBizMatReqHeadOnPaging(map);
	}

	/**
	 * 料单查询下的出库数量查询出库单信息 刘宇 2018.02.03
	 */
	@Override
	public List<Map<String, Object>> listOutputForMatReq(String matReqId, String matReqRid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matReqId", matReqId);// 领料单号
		map.put("matReqRid", matReqRid);// 领料单行号
		return bizStockOutputHeadDao.selectOutputForMatReq(map);
	}

	@Override
	public String upLoadExcel(MultipartFile fileInClient, String realPath) throws Exception {

		// 文件全路径
		String path = realPath + EnumTemplateType.TEMPLATE_MAT_REQ_MATERIAL_LIST.getFolder() + File.separator;
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = sdf.format(new Date()) + ".xls";

		File fileInServer = new File(path, fileName);

		if (fileInServer != null) {
			if (!fileInServer.exists()) {
				fileInServer.mkdirs();
			}

			// MultipartFile自带的解析方法
			try {
				fileInClient.transferTo(fileInServer);
			} catch (IllegalStateException e) {
				logger.error("", e);
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		return fileName;

	}

	@Override
	public JSONObject getExcelData(String realPath, String fileName, String ftyCode, CurrentUser user)
			throws Exception {

		// 文件全路径
		String path = realPath + EnumTemplateType.TEMPLATE_MAT_REQ_MATERIAL_LIST.getFolder() + File.separator;

		List<Map<String, Object>> dataList = UtilExcel.getExcelDataList(path + fileName, getDataChangeTitle(),
				getTitleMapping());

		List<String> userLocationIds = user.getLocationList();
		String singleLocationCode = "";
		int success = 0;
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map = dataList.get(i);

			Boolean flag = true;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				format.setLenient(false);
				format.parse(UtilString.getStringOrEmptyForObject(map.get("demandDate")));
			} catch (ParseException e) {
				flag = false;
			}

			if (!flag) {
				dataList.get(i).put("demandDate", "");
			}

			if ("".equals(map.get("matCode").toString()) || "".equals(map.get("locationCode").toString())) {
				dataList.get(i).put("flag", false);
				dataList.get(i).put("returnMsg", "没有输入物料编码或库存地点");
			} else {
				Integer ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
				String locationCode = UtilString.getStringOrEmptyForObject(map.get("locationCode"));
				Integer locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
				if (userLocationIds.contains(ftyId + "-" + locationId)) {
					if ("".equals(singleLocationCode)) {
						singleLocationCode = locationCode;
					}

					if (singleLocationCode.equals(locationCode)) {
						dataList.get(i).put("locationId", locationId);

						Map<String, Object> findMap = new HashMap<String, Object>();
						String matCode = UtilString.getStringOrEmptyForObject(map.get("matCode"));
						int matId = dictionaryService.getMatIdByMatCode(matCode);
						if (matId > 0) {

							findMap.put("matCode", matCode);
							findMap.put("locationId", locationId);
							List<StockBatchVo> stockBatchList = stockBatchDao.selectStockBatchByCondition(findMap);

							if (stockBatchList == null || stockBatchList.isEmpty()) {
								dataList.get(i).put("flag", false);
								dataList.get(i).put("returnMsg", "没有查到物料编码相关库存.");
							} else {
								dataList.get(i).put("matId", stockBatchList.get(0).getMatId());
								dataList.get(i).put("matName", stockBatchList.get(0).getMatName());
								dataList.get(i).put("unitId", stockBatchList.get(0).getUnitId());
								dataList.get(i).put("unitCode", stockBatchList.get(0).getUnitCode());
								dataList.get(i).put("unitName", stockBatchList.get(0).getUnitName());
								dataList.get(i).put("locationName", stockBatchList.get(0).getLocationName());
								dataList.get(i).put("qty", stockBatchList.get(0).getQty());
								dataList.get(i).put("assetAttr", stockBatchList.get(0).getAssetAttr());
								dataList.get(i).put("decimalPrice", stockBatchList.get(0).getDecimalPlace());
								dataList.get(i).put("flag", true);
								dataList.get(i).put("returnMsg", "");
								success++;
							}
						} else {
							dataList.get(i).put("flag", false);
							dataList.get(i).put("returnMsg", "物料不存在");
						}

					} else {
						dataList.get(i).put("flag", false);
						dataList.get(i).put("returnMsg", "库存地点不一致");
					}
				} else {
					dataList.get(i).put("flag", false);
					dataList.get(i).put("returnMsg", "没有库存地点权限");
				}
			}
		}

		JSONObject body = new JSONObject();
		body.put("dataList", dataList);
		body.put("total", dataList.size());
		body.put("success", success);

		return body;

	}

	public List<String> getDataChangeTitle() {
		List<String> list = new ArrayList<String>();

		list.add("matCode");

		return list;
	}

	public Map<String, String> getTitleMapping() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("物料编码", "matCode");
		map.put("库存地点", "locationCode");
		map.put("需求数量", "demandQty");
		map.put("需求日期", "demandDate");
		map.put("工单号", "workReceiptCode");
		map.put("设备号", "deviceCode");
		map.put("备注描述", "remark");

		return map;

	}

}
