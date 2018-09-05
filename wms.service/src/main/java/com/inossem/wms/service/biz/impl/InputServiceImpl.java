package com.inossem.wms.service.biz.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.config.BatchMaterialConfig;
import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.batch.BatchMaterialSpecMapper;
import com.inossem.wms.dao.biz.BizAllocateDeliveryHeadMapper;
import com.inossem.wms.dao.biz.BizAllocateDeliveryItemMapper;
import com.inossem.wms.dao.biz.BizAllocateHeadMapper;
import com.inossem.wms.dao.biz.BizAllocateItemMapper;
import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizMaterialDocItemMapper;
import com.inossem.wms.dao.biz.BizReceiptAttachmentMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockInputPackageItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskItemCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskPositionCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.dao.syn.SynMaterialDocHeadMapper;
import com.inossem.wms.dao.syn.SynMaterialDocItemMapper;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.exception.MaterialNotExistException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.FreezeCheck;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.batch.BatchMaterialSpec;
import com.inossem.wms.model.biz.BizAllocateDeliveryHead;
import com.inossem.wms.model.biz.BizAllocateDeliveryItem;
import com.inossem.wms.model.biz.BizAllocateHead;
import com.inossem.wms.model.biz.BizAllocateItem;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizPurchaseOrderItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputPackageItem;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.dic.DicBatchSpec;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.enums.EnumAllocateStatus;
import com.inossem.wms.model.enums.EnumBusinessType;
import com.inossem.wms.model.enums.EnumCheckAccount;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumLocationStatus;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.key.StockBatchKey;
import com.inossem.wms.model.stock.StockBatch;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.syn.SynMaterialDocHead;
import com.inossem.wms.model.vo.BizAllocateHeadVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.IAllocService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.service.intfc.IStockInput;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilMethod;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;

@Service("rkglService")
@Transactional
public class InputServiceImpl implements IInputService {

	@Autowired
	private IStockInput stockInputSap;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private SequenceDAO sequenceDAO;

	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;

	@Autowired
	private BizStockInputItemMapper stockInputItemDao;

	@Autowired
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;

	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;

	@Autowired
	private StockPositionMapper stockPositionDao;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Autowired
	private BatchMaterialSpecMapper batchMaterialSpecDao;

	@Autowired
	private SynMaterialDocHeadMapper synMDocHeadDao;

	@Autowired
	private SynMaterialDocItemMapper synMDocItemDao;

	@Autowired
	private DicMaterialMapper materialDao;

	@Autowired
	private BizAllocateItemMapper allocateItemDao;

	@Autowired
	private BizAllocateDeliveryItemMapper allocateDeliveryItemDao;

	@Autowired
	private BizMaterialDocItemMapper materialDocItemDao;

	@Autowired
	private StockBatchMapper stockBatchDao;

	@Autowired
	private BizAllocateHeadMapper allocateHeadDao;

	@Autowired
	private BizAllocateDeliveryHeadMapper allocateDeliveryHeadDao;

	@Autowired
	private IAllocService allocService;

	@Autowired
	private BizReceiptAttachmentMapper bizReceiptAttachmentDao;

	@Autowired
	private BizStockInputPackageItemMapper stockInputPackageItemDao;

	@Autowired
	private BizStockTaskItemCwMapper stockTaskItemCwDao;

	@Autowired
	private BizStockTaskPositionCwMapper stockTaskPositionCwDao;

	@Autowired
	private BizStockTaskHeadCwMapper stockTaskHeadCwDao;
	
	@Autowired
	private BizBusinessHistoryMapper businessHistoryDao;
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private BizStockTaskReqHeadMapper bizStockTaskReqHeadDao;
	
	
	@Override
	public List<BizPurchaseOrderHead> getContractList(Map<String, Object> map, CurrentUser user) throws Exception {

		return stockInputSap.getContractListFromSAP(map);
	}

	@Override
	public BizPurchaseOrderHead getContractInfo(String purchaseOrderCode, CurrentUser user) throws Exception {
		BizPurchaseOrderHead purchaseOrderHead = stockInputSap.getContractInfoFromSAP(purchaseOrderCode);
		List<BizPurchaseOrderItem> purchaseOrderItemList = new ArrayList<BizPurchaseOrderItem>();
		purchaseOrderHead.setCorpName(user.getCorpName());
		purchaseOrderHead.setCorpId(user.getCorpId());
		List<RelUserStockLocationVo> locationList = userService.listLocForUser(user.getUserId());
		List<Integer> locationIds = new ArrayList<Integer>();
		Map<Integer, Object> locationIdStatusMap = new HashMap<Integer, Object>();

		for (RelUserStockLocationVo innerObj : locationList) {
			locationIds.add(innerObj.getLocationId());
			locationIdStatusMap.put(innerObj.getLocationId(), innerObj.getStatus());
		}
		int i = 0;
		if (purchaseOrderHead != null && purchaseOrderHead.getItemList() != null) {
			purchaseOrderHead.getItemList();
			for (BizPurchaseOrderItem innerObj : purchaseOrderHead.getItemList()) {
				// 退货标识为空 & 订单数量>入库数量
				if (!StringUtils.hasText(innerObj.getReturnMarking()) && innerObj.getOrderQty() != null
						&& innerObj.getInputedQty() != null
						&& innerObj.getOrderQty().compareTo(innerObj.getInputedQty()) == 1) {
					// 当前登陆人库存地点下 并且 库存地点状态为虚拟上线库存 正式上线库存
					Integer locationId = innerObj.getLocationId();
					Byte locationStatus = UtilObject.getByteOrNull(locationIdStatusMap.get(locationId));

					if (locationIds.contains(locationId) && locationStatus != null
							&& (locationStatus.equals(EnumLocationStatus.LOCATION_STATUS_ON_LINE.getValue())
									|| locationStatus
											.equals(EnumLocationStatus.LOCATION_STATUS_ON_LINE_VIRTUAL.getValue()))) {
						innerObj.setStockInputRid(i + 1);
						purchaseOrderItemList.add(innerObj);
						i++;
					}

				}
			}
		}
		purchaseOrderHead.setItemList(purchaseOrderItemList);
		return purchaseOrderHead;
	}

	@Override
	public Map<String, Object> exemptInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user)
			throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		Map<String, Object> lockedCheck = this.checkLocation(stockInputHead);

		if (!lockedCheck.get("errorCode").equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
			errorCode = (Integer) lockedCheck.get("errorCode");
			errorString = (String) lockedCheck.get("errorString");
			returnMap.put("errorCode", errorCode);
			returnMap.put("errorString", errorString);
			return returnMap;
		}

		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	@Override
	public Map<String, Object> exemptInputPosting(BizStockInputHead stockInputHead) throws Exception {

		return stockInputSap.mjrkOrZcrkPosting(stockInputHead);
	}

	@Override
	public Map<String, Object> exemptInputUpdateStock(BizMaterialDocHead mDocHead) throws Exception {
		// 批次库存
		commonService.modifyStockBatchByMaterialDoc(mDocHead.getCreateUser(), mDocHead,
				mDocHead.getMaterialDocItemList());
		// 上架请求
		this.addStockTaskReqHeadAndItem(mDocHead);
		// 仓位库存
		this.addStockPosition(mDocHead);
		// 批次信息
		this.addBatchMaterialInfo(mDocHead);
		// 发起审批

		Integer receiptTypeId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		String receiptTypeCode = mDocHead.getMaterialDocItemList().get(0).getReferReceiptCode();
		commonService.initRoleWf(receiptTypeId, mDocHead.getMatDocType(), receiptTypeCode, mDocHead.getCreateUser());

		BizStockInputHead updateObj = new BizStockInputHead();
		Integer stockInputId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		updateObj.setStockInputId(stockInputId);
		updateObj.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		stockInputHeadDao.updateByPrimaryKeySelective(updateObj);
		this.updateInputItemMatDocInfo(mDocHead.getMaterialDocItemList());
		return null;
	}

	@Override
	public Map<String, Object> platformInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user)
			throws Exception {
		return this.exemptInputAddOrUpDate(stockInputHead, user);
	}

	@Override
	public Map<String, Object> platformInputPosting(BizStockInputHead stockInputHead) throws Exception {
		return this.exemptInputPosting(stockInputHead);
	}

	@Override
	public Map<String, Object> platformInputUpdateStock(BizMaterialDocHead mDocHead) throws Exception {
		return this.exemptInputUpdateStock(mDocHead);
	}

	@Override
	public Map<String, Object> otherInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user)
			throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		for (int i = 0; i < stockInputHead.getItemList().size(); i++) {
			BizStockInputItem item = stockInputHead.getItemList().get(i);
			item.setFtyId(stockInputHead.getFtyId());
			item.setLocationId(stockInputHead.getLocationId());
			item.setMoveTypeId(stockInputHead.getMoveTypeId());
			item.setSupplierCode(stockInputHead.getSupplierCode());
			item.setSupplierName(stockInputHead.getSupplierName());
		}

		Map<String, Object> lockedCheck = this.checkLocation(stockInputHead);

		if (!lockedCheck.get("errorCode").equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
			errorCode = (Integer) lockedCheck.get("errorCode");
			errorString = (String) lockedCheck.get("errorString");
			returnMap.put("errorCode", errorCode);
			returnMap.put("errorString", errorString);
			return returnMap;
		}

		this.addInput(stockInputHead, user);

		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	@Override
	public Map<String, Object> otherInputPosting(BizStockInputHead stockInputHead) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		if (EnumCheckAccount.CHECK_ACCOUNT_TRUE.getValue().equals(stockInputHead.getCheckAccount())) {
			// 核对账目
			returnMap = this.setMatDocHeadFromCheckAccount(stockInputHead);
		} else {
			returnMap = stockInputSap.qtrkPosting(stockInputHead);
		}

		return returnMap;
	}

	@Override
	public Map<String, Object> otherInputUpdateStock(BizMaterialDocHead mDocHead) throws Exception {
		// 批次库存
		commonService.modifyStockBatchByMaterialDoc(mDocHead.getCreateUser(), mDocHead,
				mDocHead.getMaterialDocItemList());
		// 上架请求
		this.addStockTaskReqHeadAndItem(mDocHead);
		// 仓位库存
		this.addStockPosition(mDocHead);
		// 批次信息
		this.addBatchMaterialInfo(mDocHead);
		// 发起审批

		Integer receiptTypeId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		String receiptTypeCode = mDocHead.getMaterialDocItemList().get(0).getReferReceiptCode();
		commonService.initRoleWf(receiptTypeId, mDocHead.getMatDocType(), receiptTypeCode, mDocHead.getCreateUser());

		BizStockInputHead updateObj = new BizStockInputHead();
		Integer stockInputId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		updateObj.setStockInputId(stockInputId);
		updateObj.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		stockInputHeadDao.updateByPrimaryKeySelective(updateObj);
		this.updateInputItemMatDocInfo(mDocHead.getMaterialDocItemList());
		return null;
	}

	@Override
	public Map<String, Object> checkLocation(BizStockInputHead stockInputHead) throws Exception {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, String>> checkMaps = new ArrayList<Map<String, String>>();
		if (stockInputHead != null && stockInputHead.getItemList() != null && stockInputHead.getItemList().size() > 0) {
			for (BizStockInputItem item : stockInputHead.getItemList()) {
				HashMap<String, String> checkMap = new HashMap<String, String>();
				checkMap.put("location_id", UtilString.getStringOrEmptyForObject(item.getLocationId()));
				checkMaps.add(checkMap);
			}
			FreezeCheck fCheck = commonService.checkWhPosAndStockLoc(checkMaps);
			if (fCheck != null && fCheck.isLockedInput()) {
				// 锁定
				errorCode = EnumErrorCode.ERROR_CODE_LOCKED.getValue();
				errorString = fCheck.getResultMsgInput();
			}

		} else {
			// emptyError
			errorCode = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getName();
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	/**
	 * 核对账目返回物料凭证
	 */
	private Map<String, Object> setMatDocHeadFromCheckAccount(BizStockInputHead stockInputHead) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		SynMaterialDocHead head = synMDocHeadDao.selectByPrimaryKey(stockInputHead.getItemList().get(0).getMatDocId());

		Map<String, Object> key = new HashMap<String, Object>();
		key.put("matDocId", head.getMatDocId());
		List<Map<String, Object>> msegList = synMDocItemDao.selectListById(key);

		BizMaterialDocHead maDocHead = new BizMaterialDocHead();
		List<BizMaterialDocItem> itemList = new ArrayList<BizMaterialDocItem>();
		if (msegList.size() > 0) {
			maDocHead.setMatDocCode(head.getMatDocCode());
			maDocHead.setMatDocType(stockInputHead.getReceiptType()); // 业务类型
			maDocHead.setDocTime(head.getMatDocTime()); // 凭证日期
			maDocHead.setPostingDate(head.getPostingTime());

			maDocHead.setOrgCode(stockInputHead.getPurchaseOrg());
			maDocHead.setOrgName(stockInputHead.getPurchaseOrgName());
			maDocHead.setGroupCode(stockInputHead.getPurchaseGroup());
			maDocHead.setGroupName(stockInputHead.getPurchaseGroupName());
			maDocHead.setCreateUser(stockInputHead.getCreateUser());

			for (Map<String, Object> item : msegList) {

				Integer matDocId = UtilObject.getIntegerOrNull(item.get("mat_doc_id"));
				Integer matDocRid = UtilObject.getIntegerOrNull(item.get("mat_doc_rid"));

				BizStockInputItem inputItem = stockInputHead.getItemList().stream()
						.filter(z -> z.getMatDocId().equals(matDocId) && z.getMatDocRid().equals(matDocRid)).findFirst()
						.get();
				BizMaterialDocItem matItem = new BizMaterialDocItem();
				matItem.setBatch(inputItem.getBatch());
				matItem.setCustomerCode(UtilString.getStringOrEmptyForObject(item.get("customer_code")));
				matItem.setCustomerName(UtilString.getStringOrEmptyForObject(item.get("customer_name")));
				matItem.setDebitCredit(UtilString.getStringOrEmptyForObject(item.get("debit_credit")));
				matItem.setFtyId(UtilObject.getIntOrZero(item.get("fty_id")));
				matItem.setLocationId(UtilObject.getIntOrZero(item.get("location_id")));
				matItem.setMatDocRid(UtilObject.getIntOrZero(item.get("mat_doc_rid")));
				matItem.setMatDocYear(head.getMatDocYear());
				matItem.setMatId(UtilObject.getIntOrZero(item.get("mat_id")));
				matItem.setMoveTypeId(UtilObject.getIntOrZero(item.get("move_type_id")));
				String moveTypeCode = dictionaryService.getMoveTypeCodeByMoveTypeId(matItem.getMoveTypeId());
				matItem.setMoveTypeCode(moveTypeCode);
				matItem.setQty(UtilObject.getBigDecimalOrZero(item.get("qty")));
				matItem.setReferReceiptCode(stockInputHead.getStockInputCode());
				matItem.setReferReceiptId(stockInputHead.getStockInputId());
				matItem.setReferReceiptRid(inputItem.getStockInputRid());
				matItem.setReferReceiptItemId(inputItem.getItemId());
				matItem.setReserveId(UtilString.getStringOrEmptyForObject(item.get("reserve_id")));
				matItem.setReserveRid(UtilString.getStringOrEmptyForObject(item.get("reserve_rid")));
				matItem.setSaleOrderCode(UtilString.getStringOrEmptyForObject(item.get("sale_order_code")));
				matItem.setSaleOrderProjectCode(
						UtilString.getStringOrEmptyForObject(item.get("sale_order_prject_code")));
				matItem.setSpecStock(UtilString.getStringOrEmptyForObject(item.get("spec_stock")));
				matItem.setSpecStockCode(UtilString.getStringOrEmptyForObject(item.get("spec_stock_code")));
				matItem.setSpecStockName(UtilString.getStringOrEmptyForObject(item.get("spec_stock_name")));
				matItem.setSupplierCode(UtilString.getStringOrEmptyForObject(item.get("supplier_code")));
				matItem.setSupplierName(UtilString.getStringOrEmptyForObject(item.get("supplier_name")));
				String stockStatus = UtilString.getStringOrEmptyForObject(item.get("stock_status"));

				if (stockStatus.equals(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getCode())) {
					matItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				} else if (stockStatus.equals(EnumStockStatus.STOCK_BATCH_STATUS_ON_THE_WAY.getCode())) {
					matItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_ON_THE_WAY.getValue());
				} else if (stockStatus.equals(EnumStockStatus.STOCK_BATCH_STATUS_QUALITY_INSPECTION.getCode())) {
					matItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_QUALITY_INSPECTION.getValue());
				} else if (stockStatus.equals(EnumStockStatus.STOCK_BATCH_STATUS_FREEZE.getCode())) {
					matItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_FREEZE.getValue());
				}

				matItem.setUnitId(UtilObject.getIntOrZero("unit_id"));
				matItem.setBatchMaterialSpecList(inputItem.getBatchMaterialSpecList());
				matItem.setBatchSpecList(inputItem.getBatchSpecList());
				String batchString = UtilString.getStringOrEmptyForObject(item.get("batch"));
				Long batch = null;
				if (StringUtils.hasText(batchString)) {
					batch = UtilObject.getLongOrNull(batchString);
				}
				if (batch == null || batch == 0) {
					batch = commonService.getBatch();
				}
				matItem.setBatch(batch);
				inputItem.setBatch(batch);

				itemList.add(matItem);
			}
			maDocHead.setMaterialDocItemList(itemList);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = "凭证生成成功";
			returnMap.put("materialDocHead", maDocHead);
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			errorString = "凭证生成失败";
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;
	}

	private void addStockHeadFromPurchaseOrder(BizStockInputHead stockInputHead,
			BizPurchaseOrderHead purchaseOrderHead) {

		stockInputHead.setPurchaseOrderType(purchaseOrderHead.getPurchaseOrderType());
		stockInputHead.setPurchaseOrderTypeName(purchaseOrderHead.getPurchaseOrderTypeName());
		stockInputHead.setCorpId(purchaseOrderHead.getCorpId());
		stockInputHead.setPurchaseOrderCode(purchaseOrderHead.getPurchaseOrderCode());
		stockInputHead.setPurchaseGroup(purchaseOrderHead.getPurchaseGroup());
		stockInputHead.setPurchaseGroupName(purchaseOrderHead.getPurchaseGroupName());
		stockInputHead.setPurchaseOrg(purchaseOrderHead.getPurchaseOrg());
		stockInputHead.setPurchaseOrgName(purchaseOrderHead.getPurchaseOrgName());
		stockInputHead.setContractCode(purchaseOrderHead.getContractCode());
		stockInputHead.setSupplierCode(purchaseOrderHead.getSupplierCode());
		stockInputHead.setSupplierName(purchaseOrderHead.getSupplierName());
	}

	private void addStockListFromPurchaseOrderList(List<BizStockInputItem> stockInputItemList,
			List<BizPurchaseOrderItem> purchaseOrderItemList) throws Exception {

		for (BizStockInputItem stockItem : stockInputItemList) {
			for (BizPurchaseOrderItem purItem : purchaseOrderItemList) {
				if (stockItem.getPurchaseOrderRid().equals(purItem.getPurchaseOrderRid())) {
					stockItem.setPurchaseOrderCode(purItem.getPurchaseOrderCode());
					stockItem.setPurchaseOrderRid(purItem.getPurchaseOrderRid());
					Integer matId = purItem.getMatId();
					if (matId != null && matId > 0) {
						stockItem.setMatId(matId);
					} else {
						throw new MaterialNotExistException();
					}
					stockItem.setUnitId(purItem.getUnitId());
					stockItem.setOrderQty(purItem.getOrderQty());
					stockItem.setContractCode(purItem.getContractCode());
					stockItem.setInputedQty(purItem.getInputedQty());

					stockItem.setSpecStock(purItem.getSpecStock());
					stockItem.setSupplierName(purItem.getSupplierName());
					stockItem.setSupplierCode(purItem.getSupplierCode());
					stockItem.setSpecStockCode(purItem.getSpecStockCode());
					stockItem.setSpecStockName(purItem.getSpecStockName());
					stockItem.setDemandDept(purItem.getDemandDept());
					stockItem.setDecimalPlace(purItem.getDecimalPlace());
					stockItem.setContractName(purItem.getContractName());
					stockItem.setManufacturer(purItem.getManufacturer());

				}

			}
		}

	}

	private void addStockListFromAllocateItem(List<BizStockInputItem> stockInputItemList,
			List<Map<String, Object>> allocateItemList) {

		for (BizStockInputItem stockItem : stockInputItemList) {
			for (Map<String, Object> allocateItem : allocateItemList) {
				// Integer stockInputRid=
				// UtilObject.getIntegerOrNull(allocateItem.get("stock_input_rid"));
				Long batch = UtilObject.getLongOrNull(allocateItem.get("batch"));
				Integer allocateItemId = UtilObject.getIntegerOrNull(allocateItem.get("item_id"));
				if (stockItem.getAllocateItemId().equals(allocateItemId) && batch != null
						&& batch.equals(stockItem.getBatch())) {

					Integer matId = UtilObject.getIntegerOrNull(allocateItem.get("mat_id"));
					stockItem.setMatId(matId);
					Integer unitId = UtilObject.getIntegerOrNull(allocateItem.get("unit_id"));
					stockItem.setUnitId(unitId);
					stockItem.setQty(UtilObject.getBigDecimalOrZero(allocateItem.get("qty")));
					stockItem.setSpecStock(UtilString.getStringOrEmptyForObject(allocateItem.get("spec_stock")));
					stockItem.setSpecStockCode(
							UtilString.getStringOrEmptyForObject(allocateItem.get("spec_stock_code")));
					stockItem.setSpecStockName(
							UtilString.getStringOrEmptyForObject(allocateItem.get("spec_stock_name")));
					Byte decimalPlace = UtilObject.getByteOrNull(allocateItem.get("decimal_place"));
					stockItem.setDecimalPlace(decimalPlace);

					stockItem.setBatchOld(batch);
					stockItem.setFtyId(UtilObject.getIntegerOrNull(allocateItem.get("fty_id")));
					stockItem.setLocationId(UtilObject.getIntegerOrNull(allocateItem.get("location_id")));

				}

			}
		}

	}

	/**
	 * 校验数量
	 * 
	 * @param stockInputItemList
	 * @return
	 */
	private Map<String, Object> checkQty(List<BizStockInputItem> stockInputItemList) {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		for (BizStockInputItem item : stockInputItemList) {

			BigDecimal orderQty = item.getOrderQty();
			BigDecimal inputedQty = item.getInputedQty();
			BigDecimal qty = item.getQty();
			// 入库数量+已入库数量>订单数量

			if (orderQty == null || inputedQty == null || qty.add(inputedQty).compareTo(orderQty) == 1) {
				if (errorCode.equals(EnumErrorCode.ERROR_CODE_QTY_OVER.getValue())) {
					errorString = item.getPurchaseOrderRid() + "," + errorString;
				} else {
					errorCode = EnumErrorCode.ERROR_CODE_QTY_OVER.getValue();
					errorString = item.getPurchaseOrderRid() + EnumErrorCode.ERROR_CODE_QTY_OVER.getName();
				}
			}

		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	@Override
	public Long getStockInputCode() throws Exception {

		return sequenceDAO.selectNextVal("stock_input");
	}

	@Override
	public List<BizStockInputItem> trimZeroOrNullQtyObj(List<BizStockInputItem> stockInputItemList) throws Exception {
		List<BizStockInputItem> itemList = new ArrayList<BizStockInputItem>();
		if (stockInputItemList != null) {
			for (BizStockInputItem item : stockInputItemList) {
				if (item.getQty() != null && item.getQty().compareTo(BigDecimal.ZERO) == 1) {
					itemList.add(item);
				}
			}
		}
		return itemList;
	}

	private void deleteStockPositionByWriteOff(List<BizStockInputItem> itemList) {
		if (itemList != null && itemList.size() > 0) {
			List<Integer> itemIds = new ArrayList<Integer>();
			for (BizStockInputItem item : itemList) {
				itemIds.add(item.getItemId());
			}
			stockPositionDao.deleteByStockInputItemIDs(itemIds);
		}
	}

	private void updateStockWriteOffStatus(List<BizStockInputItem> itemList, String writeOffReamrk) {
		if (itemList != null && itemList.size() > 0) {
			for (BizStockInputItem item : itemList) {
				BizStockInputItem siItem = new BizStockInputItem();
				siItem.setItemId(item.getItemId());
				siItem.setWriteOff((byte) 1);
				siItem.setRemark(writeOffReamrk);
				stockInputItemDao.updateByPrimaryKeySelective(siItem);
			}
		}
	}

	private void updateMatDocWriteOffStatus(List<BizStockInputItem> itemList) {
		if (itemList != null && itemList.size() > 0) {
			for (BizStockInputItem item : itemList) {
				BizMaterialDocItem mdItem = new BizMaterialDocItem();
				mdItem.setItemId(item.getMatDocItemId());
				mdItem.setWriteOff((byte) 1);
				materialDocItemDao.updateByPrimaryKeySelective(mdItem);
			}
		}
	}

	private void deleteStockTaskReq(List<BizStockInputItem> itemList) {
		if (itemList != null && itemList.size() > 0) {
			List<Integer> itemIds = new ArrayList<Integer>();
			for (BizStockInputItem item : itemList) {
				itemIds.add(item.getItemId());
			}
			StockTaskReqItemDao.deleteItemByStockInputItemIds(itemIds);
		}
	}

	private void deleteStockTaskReqHead(Integer matDocId) {
		StockTaskReqHeadDao.deleteByMatDocIdIfItemIsNull(matDocId);
	}

	private void updateStockBatchByAllocateInputWriteOff(List<BizMaterialDocItem> mDocItemList,
			List<BizMaterialDocItem> omDocItemList, String userId) {
		if (mDocItemList != null && mDocItemList.size() > 0) {
			for (BizMaterialDocItem item : mDocItemList) {
				if ("H".equals(item.getDebitCredit())) {
					BizMaterialDocItem oItem = omDocItemList.stream()
							.filter(z -> z.getReferReceiptId().equals(item.getReferReceiptId())
									&& z.getReferReceiptRid().equals(item.getReferReceiptRid()))
							.findFirst().get();

					// 在该工厂-库存地点不存在该批次的物料
					StockBatch stockBatch = new StockBatch();
					stockBatch.setMatId(oItem.getMatId()); // '物料编号',
					stockBatch.setLocationId(oItem.getLocationId());// '库存地点',
					stockBatch.setBatch(oItem.getBatch()); // '批号',

					stockBatch.setCreateUser(userId); // '创建人',
					stockBatch.setQty(item.getQty());
					stockBatch.setSpecStock(item.getSpecStock());// 特殊库存标识
					stockBatch.setSpecStockCode(item.getSpecStockCode());// 特殊库存代码
					stockBatch.setSpecStockName(item.getSpecStockName());// 特殊库存描述
					stockBatch.setStatus(item.getStatus());

					stockBatchDao.insertSelective(stockBatch);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addStockTaskReqHeadAndItem(BizMaterialDocHead materialDocHead) throws Exception {

		// 作业请求 库存地点 map
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();
		for (BizMaterialDocItem object : materialDocHead.getMaterialDocItemList()) {
			// "S" 时生成上架作业请求 "H" 下架
			if (EnumDebitCredit.DEBIT_S_ADD.getName().equals(object.getDebitCredit())) {

				Integer locationId = object.getLocationId();

				// -------生成作业请求单-----相同的库存地点生成一张作业单----------------

				if (locationIdMap.containsKey(locationId)) {
					// 含有相同的库存地
					Map<String, Object> headInfo = new HashMap<String, Object>();
					headInfo = (Map<String, Object>) locationIdMap.get(locationId);

					int stockTaskReqRid = (int) headInfo.get("stockTaskReqRid");
					stockTaskReqRid = stockTaskReqRid + 1;
					headInfo.put("stockTaskReqRid", stockTaskReqRid);

					BizStockTaskReqItem item = new BizStockTaskReqItem();

					this.addStockTaskReqItemFromMaterialDocItem(headInfo, object, item);

					addItemList.add(item);

				} else {
					// --------------------- 插入抬头

					Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
					DicStockLocationVo locationObj = locationMap.get(object.getLocationId());

					Integer whId = locationObj.getWhId();

					BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
					String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
					reqhead.setStockTaskReqCode(stockTaskReqCode);
					reqhead.setFtyId(object.getFtyId());
					reqhead.setLocationId(object.getLocationId());
					reqhead.setWhId(whId);
					reqhead.setMatDocId(object.getMatDocId());
					reqhead.setMatDocYear(object.getMatDocYear());
					reqhead.setCreateUser(materialDocHead.getCreateUser());
					reqhead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());

					StockTaskReqHeadDao.insertSelective(reqhead);

					// --------------end
					int stockTaskReqRid = 1;
					int stockTaskReqId = reqhead.getStockTaskReqId();

					Map<String, Object> headInfo = new HashMap<String, Object>();
					headInfo.put("stockTaskReqId", stockTaskReqId);// 仓库号
					headInfo.put("whId", whId);// 作业申请号
					headInfo.put("stockTaskReqRid", stockTaskReqRid);// 作业申请序号
					locationIdMap.put(locationId, headInfo);

					BizStockTaskReqItem item = new BizStockTaskReqItem();

					this.addStockTaskReqItemFromMaterialDocItem(headInfo, object, item);

					addItemList.add(item);

				}

			}

		}
		StockTaskReqItemDao.insertReqItems(addItemList);

	}

	@Override
	public void addStockTaskReqItemFromMaterialDocItem(Map<String, Object> headMap, BizMaterialDocItem materialDocItem,
			BizStockTaskReqItem reqItem) throws Exception {

		int stockTaskReqId = (int) headMap.get("stockTaskReqId");
		int stockTaskReqRid = (int) headMap.get("stockTaskReqRid");
		int whId = (int) headMap.get("whId");

		reqItem.setBatch(materialDocItem.getBatch());
		reqItem.setWhId(whId);
		reqItem.setFtyId(materialDocItem.getFtyId());
		reqItem.setLocationId(materialDocItem.getLocationId());
		reqItem.setMatId(materialDocItem.getMatId());
		reqItem.setMatDocId(materialDocItem.getMatDocId());
		reqItem.setMatDocRid(materialDocItem.getMatDocRid());
		reqItem.setUnitId(materialDocItem.getUnitId());
		reqItem.setQty(materialDocItem.getQty());
		reqItem.setSpecStock(materialDocItem.getSpecStock());
		reqItem.setSpecStockCode(materialDocItem.getSpecStockCode());
		reqItem.setSpecStockName(materialDocItem.getSpecStockName());
		reqItem.setStockTaskReqId(stockTaskReqId);
		reqItem.setStockTaskReqRid(stockTaskReqRid);

	}

	@Override
	public void addStockPosition(BizMaterialDocHead materialDocHead) throws Exception {

		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();

		for (BizMaterialDocItem item : materialDocHead.getMaterialDocItemList()) {
			StockPosition stockPosition = new StockPosition();

			DicStockLocationVo locationObj = locationIdMap.get(item.getLocationId());

			Integer whId = locationObj.getWhId();
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			String areaCode = UtilProperties.getInstance().getDefaultCCQ();
			Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
			String positionCode = UtilProperties.getInstance().getDefaultCW();
			// TODO 没有仓库号的库存地点
			Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
					positionCode);
			stockPosition.setWhId(whId);
			stockPosition.setAreaId(areaId);
			stockPosition.setPositionId(positionId);
			stockPosition.setMatId(item.getMatId());
			stockPosition.setFtyId(item.getFtyId());
			stockPosition.setLocationId(item.getLocationId());
			stockPosition.setBatch(item.getBatch());
			stockPosition.setSpecStock(item.getSpecStock());
			stockPosition.setSpecStockCode(item.getSpecStockCode());
			stockPosition.setSpecStockName(item.getSpecStockName());
			stockPosition.setInputDate(item.getCreateTime());
			stockPosition.setQty(item.getQty());
			stockPosition.setUnitId(item.getUnitId());
			stockPosition.setInputDate(new Date());
			stockPosition.setPackageId(0);
			stockPosition.setPalletId(0);
			stockPosition.setDebitOrCredit(item.getDebitCredit());
			stockPosition.setStatus(item.getStatus());
			List<StockSn> snList = new ArrayList<StockSn>();
			StockSn sn = new StockSn();
			sn.setSnId(0);
			sn.setQty(stockPosition.getQty());
			sn.setMatId(stockPosition.getMatId());
			sn.setDebitOrCredit(item.getDebitCredit());
			snList.add(sn);
			commonService.modifyStockPositionAndSn(stockPosition, snList);
			// stockPositionDao.insertSelective(stockPosition);
		}

	}

	@Override
	public void addBatchMaterialInfo(BizMaterialDocHead materialDocHead) throws Exception {

		List<BatchMaterialSpec> bmsList = new ArrayList<BatchMaterialSpec>();

		List<BatchMaterialConfig> bmcList = UtilProperties.getInstance().getBatchMaterialConfigList();

		List<String> batchSpecCodeList = new ArrayList<String>();
		for (BatchMaterialConfig item : bmcList) {
			if (StringUtils.hasText(item.getBatchSpecCode()))
				batchSpecCodeList.add(item.getBatchSpecCode());
		}

		List<String> declaredFields = new ArrayList<String>();

		for (Field f : BatchMaterial.class.getDeclaredFields()) {
			declaredFields.add(f.getName());
		}

		for (BizMaterialDocItem item : materialDocHead.getMaterialDocItemList()) {
			if ("S".equals(item.getDebitCredit())) {
				// 批次信息
				BatchMaterial batchMaterial = new BatchMaterial();
				batchMaterial.setBatch(item.getBatch());
				batchMaterial.setMatId(item.getMatId());
				batchMaterial.setSupplierCode(item.getSupplierCode());
				batchMaterial.setSupplierName(item.getSupplierName());
				batchMaterial.setFtyId(item.getFtyId());
				batchMaterial.setSpecStock(item.getSpecStock());
				batchMaterial.setSpecStockCode(item.getSpecStockCode());
				batchMaterial.setSpecStockName(item.getSpecStockName());
				batchMaterial.setCreateUser(materialDocHead.getCreateUser());

				// 批次信息补充
				for (DicBatchSpec dbs : item.getBatchMaterialSpecList()) {
					String code = dbs.getBatchSpecCode();
					String fieldName = UtilJSONConvert.humpName(code);
					if (declaredFields.contains(fieldName)) {
						Method method = UtilMethod.getSetMethod(batchMaterial.getClass(), fieldName);

						if (method != null) {
							Object value = dbs.getBatchSpecValue();
							if (BatchMaterial.class.getDeclaredField(fieldName).getGenericType().toString()
									.equals("class java.util.Date")) {
								value = UtilString.getDateForString(dbs.getBatchSpecValue());
							}

							method.invoke(batchMaterial, new Object[] { value });
						}
					}
				}
				batchMaterialDao.insertSelective(batchMaterial);

				for (DicBatchSpec dbs : item.getBatchSpecList()) {
					BatchMaterialSpec bms = new BatchMaterialSpec();
					bms.setMatId(item.getMatId());
					bms.setBatch(item.getBatch());
					bms.setFtyId(item.getFtyId());
					bms.setBatchSpecCode(dbs.getBatchSpecCode());
					bms.setBatchSpecValue(dbs.getBatchSpecValue());
					bmsList.add(bms);
				}
			}
		}
		if (bmsList.size() > 0) {
			batchMaterialSpecDao.insertList(bmsList);
		}

	}

	@Override
	public void deleteRkInfo(BizStockInputHead stockInputHead) throws Exception {

		Integer stockInputId = stockInputHead.getStockInputId();
		if (stockInputId != null) {

			// 删除批次信息
			batchMaterialDao.deleteByStockInputId(stockInputId);

			// 删除批次特性
			batchMaterialSpecDao.deleteByStockInputId(stockInputId);

			// 删除入库单抬头
			stockInputHeadDao.deleteByStockInputId(stockInputId);
			// 删除入库单行项目
			stockInputItemDao.deleteByStockInputId(stockInputId);
			// 删除经办人
			commonService.removeReceiptUser(stockInputId, stockInputHead.getReceiptType());
			// 删除附件
			bizReceiptAttachmentDao.deleteByReceiptId(stockInputId);

		}

	}

	@Override
	public List<DicMaterial> getMaterialList(DicMaterial material) throws Exception {
		return materialDao.selectByCondition(material);
	}

	@Override
	public String getReceiptTypeNameByInPutCode(String stockInputCode) {
		BizStockInputHead head = stockInputHeadDao.selectByInputCode(stockInputCode);
		return EnumReceiptType.getNameByValue(head.getReceiptType());
	}

	@Override
	public List<Map<String, Object>> getAllotItemsOnCreateDBRK(Map<String, Object> map) throws Exception {

		return allocateItemDao.selectAllotItemsOnCreateDBRKOnPagIng(map);
	}

	@Override
	public List<Map<String, Object>> getDBRKItemByAllotIdAndDeliveryId(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> items = materialDocItemDao.selectDBRKItemByAllotIdAndDeliveryId(map);
		int i = 0;
		if (items != null) {
			for (Map<String, Object> mseg : items) {
				mseg.put("stock_input_rid", i + 1);
				i++;
			}
		}
		return items;

	}

	@Override
	public Map<String, Object> allocateInputAddOrUpDate(BizStockInputHead stockInputHead, CurrentUser user)
			throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		// 生成入库单抬头
		Byte findstatus = EnumAllocateStatus.SENDING.getValue();
		if (stockInputHead.getStockInputId() != null) {
			findstatus = EnumAllocateStatus.WAIT_INPUT.getValue();
		}

		Integer allocateId = stockInputHead.getAllocateId();
		Integer allocateDeliveryId = stockInputHead.getAllocateDeliveryId();

		HashMap<String, Object> findMSEGMap = new HashMap<String, Object>();
		findMSEGMap.put("allocateId", allocateId);
		findMSEGMap.put("allocateDeliveryId", allocateDeliveryId);
		findMSEGMap.put("status", findstatus);
		List<Map<String, Object>> items = this.getDBRKItemByAllotIdAndDeliveryId(findMSEGMap);

		this.addStockListFromAllocateItem(stockInputHead.getItemList(), items);
		Integer ftyId = null;
		Integer locationId = null;
		if (items != null && items.size() > 0) {
			ftyId = UtilObject.getIntegerOrNull(items.get(0).get("fty_id"));
			locationId = UtilObject.getIntegerOrNull(items.get(0).get("location_id"));
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getName();

			returnMap.put("errorCode", errorCode);
			returnMap.put("errorString", errorString);
			return returnMap;
		}
		Map<String, Object> lockedCheck = this.checkLocation(stockInputHead);

		if (!lockedCheck.get("errorCode").equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
			errorCode = (Integer) lockedCheck.get("errorCode");
			errorString = (String) lockedCheck.get("errorString");
			returnMap.put("errorCode", errorCode);
			returnMap.put("errorString", errorString);
			return returnMap;
		}

		stockInputHead.setFtyId(ftyId);
		stockInputHead.setLocationId(locationId);

		this.addInput(stockInputHead, user);
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	@Override
	public Map<String, Object> allocateInputPosting(BizStockInputHead stockInputHead) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap = stockInputSap.dbrkPosting(stockInputHead);
		return returnMap;
	}

	@Override
	public Map<String, Object> allocateInputUpdateStock(BizMaterialDocHead mDocHead, BizStockInputHead stockInputHead)
			throws Exception {

		// 删掉在途库存
		this.deleteMchbOnWayByDBRK(stockInputHead);
		// 批次库存
		commonService.modifyStockBatchByMaterialDoc(mDocHead.getCreateUser(), mDocHead,
				mDocHead.getMaterialDocItemList());
		// 上架请求
		this.addStockTaskReqHeadAndItem(mDocHead);
		// 仓位库存
		this.addStockPosition(mDocHead);
		// 修改批次信息
		this.updateBatchMaterialInfo(mDocHead);
		// 修改调拨单配送单信息
		this.updateAllotItemAndDeliveryItem(stockInputHead, EnumAllocateStatus.COMPLETED.getValue(),
				stockInputHead.getAllocateId(), stockInputHead.getAllocateDeliveryId());

		// 发起审批

		Integer receiptTypeId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		String receiptTypeCode = mDocHead.getMaterialDocItemList().get(0).getReferReceiptCode();
		commonService.initRoleWf(receiptTypeId, mDocHead.getMatDocType(), receiptTypeCode, mDocHead.getCreateUser());

		BizStockInputHead updateObj = new BizStockInputHead();
		Integer stockInputId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		updateObj.setStockInputId(stockInputId);
		updateObj.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		stockInputHeadDao.updateByPrimaryKeySelective(updateObj);
		this.updateInputItemMatDocInfo(mDocHead.getMaterialDocItemList());
		return null;
	}

	public void deleteMchbOnWayByDBRK(BizStockInputHead stockInputHead) {
		if (stockInputHead != null && stockInputHead.getItemList().size() > 0) {
			for (BizStockInputItem item : stockInputHead.getItemList()) {

				StockBatchKey key = new StockBatchKey();
				key.setMatId(item.getMatId()); // '物料编号',
				key.setLocationId(item.getLocationId());// '库存地点',
				key.setBatch(item.getBatchOld()); // '批号',
				key.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_ON_THE_WAY.getValue());
				stockBatchDao.deleteByByUniqueKey(key);
			}
		}
	}

	@Override
	public void updateAllotItemAndDeliveryItem(BizStockInputHead stockInputHead, byte status, Integer allotId,
			Integer deliveryId) {
		if (stockInputHead != null && stockInputHead.getItemList() != null) {
			for (BizStockInputItem item : stockInputHead.getItemList()) {
				BizAllocateItem allotItem = new BizAllocateItem();
				allotItem.setItemId(item.getAllocateItemId());
				if (status == EnumAllocateStatus.COMPLETED.getValue()
						|| status == EnumAllocateStatus.WAIT_INPUT.getValue()) {
					allotItem.setStockInputId(item.getStockInputId());
					allotItem.setStockInputRid(item.getStockInputRid());
				} else if (status == EnumAllocateStatus.SENDING.getValue()) {
					allotItem.setStockInputId(0);
					allotItem.setStockInputRid(0);
				}

				allotItem.setStatus(status);
				allocateItemDao.updateByPrimaryKeySelective(allotItem);
				BizAllocateDeliveryItem deliveryItem = new BizAllocateDeliveryItem();
				deliveryItem.setAllocateItemId(item.getAllocateItemId());
				deliveryItem.setStatus(status);
				allocateDeliveryItemDao.updateByAllotItemIdSelective(deliveryItem);
			}
			// 更改抬头信息
			if (allotId != null) {
				Byte minStatusAllot = allocateItemDao.selectMinStatusByAllotId(allotId);
				BizAllocateHead allot = new BizAllocateHead();
				allot.setAllocateId(allotId);
				allot.setStatus(minStatusAllot);
				allocateHeadDao.updateByPrimaryKeySelective(allot);
				if (deliveryId != null) {
					Byte minStatus_delivery = allocateDeliveryItemDao.selectMinStatusByDeliveryId(deliveryId);
					BizAllocateDeliveryHead delivery = new BizAllocateDeliveryHead();
					delivery.setAllocateDeliveryId(deliveryId);
					delivery.setStatus(minStatus_delivery);
					allocateDeliveryHeadDao.updateByPrimaryKeySelective(delivery);
				}

			}
		}
	}

	public void updateBatchMaterialInfo(BizMaterialDocHead materialDocHead) throws Exception {

		List<BatchMaterialConfig> bmcList = UtilProperties.getInstance().getBatchMaterialConfigList();

		List<String> batchSpecCodeList = new ArrayList<String>();
		for (BatchMaterialConfig item : bmcList) {
			if (StringUtils.hasText(item.getBatchSpecCode()))
				batchSpecCodeList.add(item.getBatchSpecCode());
		}

		List<String> declaredFields = new ArrayList<String>();

		for (Field f : BatchMaterial.class.getDeclaredFields()) {
			declaredFields.add(f.getName());
		}

		for (BizMaterialDocItem item : materialDocHead.getMaterialDocItemList()) {
			// 批次信息
			BatchMaterial batchMaterial = new BatchMaterial();
			batchMaterial.setBatch(item.getBatch());
			batchMaterial.setMatId(item.getMatId());
			batchMaterial.setSupplierCode(item.getSupplierCode());
			batchMaterial.setSupplierName(item.getSupplierName());
			batchMaterial.setFtyId(item.getFtyId());
			batchMaterial.setSpecStock(item.getSpecStock());
			batchMaterial.setSpecStockCode(item.getSpecStockCode());
			batchMaterial.setSpecStockName(item.getSpecStockName());
			batchMaterial.setCreateUser(materialDocHead.getCreateUser());

			// 批次信息补充
			for (DicBatchSpec dbs : item.getBatchMaterialSpecList()) {
				String code = dbs.getBatchSpecCode();
				String fieldName = UtilJSONConvert.humpName(code);
				if (declaredFields.contains(fieldName)) {
					Method method = UtilMethod.getSetMethod(batchMaterial.getClass(), fieldName);

					if (method != null) {
						Object value = dbs.getBatchSpecValue();
						if (BatchMaterial.class.getDeclaredField(fieldName).getGenericType().toString()
								.equals("class java.util.Date")) {
							value = UtilString.getDateForString(dbs.getBatchSpecValue());
						}

						method.invoke(batchMaterial, new Object[] { value });
					}
				}
			}
			batchMaterialDao.updateByUniqueKeySelective(batchMaterial);

			for (DicBatchSpec dbs : item.getBatchSpecList()) {
				BatchMaterialSpec bms = new BatchMaterialSpec();
				bms.setMatId(item.getMatId());
				bms.setBatch(item.getBatch());
				bms.setFtyId(item.getFtyId());
				bms.setBatchSpecCode(dbs.getBatchSpecCode());
				bms.setBatchSpecValue(dbs.getBatchSpecValue());
				batchMaterialSpecDao.updateByUniqueKeySelective(bms);
			}

		}

	}

	@Override
	public Map<String, Object> getAllocateItem(CurrentUser cuser, Integer allocate_id, Integer allocate_delivery_id)
			throws Exception {

		Map<String, Object> input = new HashMap<String, Object>();
		Map<String, Object> delivery_info = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("allocateId", allocate_id);
		List<BizAllocateHeadVo> resList = allocService.getAllocList(paramMap);
		if (resList != null && resList.size() == 1) {

			BizAllocateHeadVo allot = resList.get(0);
			input.put("allocate_id", allot.getAllocateId());
			input.put("allocate_code", allot.getAllocateCode());
			input.put("allocate_delivery_id", UtilString.getStringOrEmptyForObject(allocate_delivery_id));
			input.put("allocate_delivery_code", "");
			input.put("org_name", allot.getOrgName());
			input.put("applicant", allot.getUserName());
			input.put("demand_date", UtilString.getShortStringForDate(allot.getDemandDate()));
			input.put("fty_input_code", allot.getFtyInputCode());
			input.put("fty_input_name", allot.getFtyName());
			input.put("location_input_code", allot.getLocationInputCode());
			input.put("location_input_name", allot.getLocationName());

		}

		if (allocate_delivery_id != null) {
			// 配送单
			BizAllocateDeliveryHead delivery = allocService.getDeliveryById(allocate_delivery_id);
			if (delivery != null) {
				delivery_info.put("is_show", 1);
				delivery_info.put("delivery_date", UtilString.getShortStringForDate(delivery.getDeliveryDate()));
				delivery_info.put("delivery_vehicle", delivery.getDeliveryVehicle());
				delivery_info.put("delivery_driver", delivery.getDeliveryDriver());
				delivery_info.put("delivery_phone", delivery.getDeliveryPhone());
				delivery_info.put("remark", delivery.getRemark());
				input.put("allocate_delivery_code", delivery.getAllocateDeliveryCode());
				input.put("delivery_info", delivery_info);
			} else {
				delivery_info.put("is_show", 0);
			}

		} else {

			delivery_info.put("is_show", 0);
		}
		input.put("delivery_info", delivery_info);
		HashMap<String, Object> findMSEGMap = new HashMap<String, Object>();
		findMSEGMap.put("allocateId", allocate_id);
		findMSEGMap.put("allocateDeliveryId", allocate_delivery_id);
		findMSEGMap.put("status", EnumAllocateStatus.SENDING.getValue());// 配送中
		List<Map<String, Object>> items = this.getDBRKItemByAllotIdAndDeliveryId(findMSEGMap);
		int i = 0;
		for (Map<String, Object> mseg : items) {
			mseg.put("stock_input_rid", i + 1);
			i++;
			Integer allocate_item_id = UtilObject.getIntegerOrNull(mseg.get("item_id"));
			mseg.put("allocate_item_id", allocate_item_id);
			mseg.remove("item_id");
			Long batch = UtilObject.getLongOrNull(mseg.get("batch"));
			Integer ftyId = UtilObject.getIntegerOrNull(mseg.get("fty_id"));
			Integer matId = UtilObject.getIntegerOrNull(mseg.get("mat_id"));
			Map<String, Object> batchSpecMap = commonService.getBatchSpecList(batch, ftyId, matId);
			mseg.put("batch_spec_list", batchSpecMap.get("batchSpecList"));
			mseg.put("batch_material_spec_list", batchSpecMap.get("batchMaterialSpecList"));
		}
		input.put("item_list", items);

		return input;
	}

	@Override
	public List<BizStockInputHead> getInputHeadList(Map<String, Object> map) throws Exception {

		List<BizStockInputHead> shList = stockInputHeadDao.selectByConditionOnPagIng(map);

		return shList;
	}

	@Override
	public List<Map<String, Object>> getAllocateInputHeadList(Map<String, Object> map) throws Exception {

		List<Map<String, Object>> shList = stockInputHeadDao.selectAllocateInputByConditionOnPagIng(map);

		return shList;
	}

	@Override
	public List<BizStockInputItem> getInputItemListByID(Integer stockInputId) throws Exception {

		List<BizStockInputItem> returnList = stockInputItemDao.selectByStockInputId(stockInputId);

		if (returnList != null) {
			for (BizStockInputItem item : returnList) {
				List<BizStockInputPackageItem> packageList = stockInputPackageItemDao.selectByItemId(item.getItemId());
				item.setPackageItemList(packageList);
				// Map<String, Object> batchInfo =
				// commonService.getBatchSpecList(item.getBatch(),
				// item.getFtyId(),
				// item.getMatId());
				// item.setBatchMaterialSpecList((List<DicBatchSpec>)
				// batchInfo.get("batchMaterialSpecList"));
				// item.setBatchSpecList((List<DicBatchSpec>)
				// batchInfo.get("batchSpecList"));
			}
		}

		return returnList;
	}

	@Override
	public List<Map<String, Object>> getInputAllocateItemById(Integer stockInputId) throws Exception {
		List<Map<String, Object>> returnList = stockInputItemDao.selectAllocateInputItemByStockInputId(stockInputId);

		String fty_output_code = "";
		String fty_output_name = "";
		String location_output_code = "";
		String location_output_name = "";
		Map<Integer, Integer> matIdAllocateItemIdMap = new HashMap<Integer, Integer>();
		if (returnList != null) {
			for (Map<String, Object> item : returnList) {
				Long batch = UtilObject.getLongOrNull(item.get("batch"));
				Integer matId = UtilObject.getIntegerOrNull(item.get("mat_id"));
				Integer ftyId = UtilObject.getIntegerOrNull(item.get("fty_id"));
				String ftyOutputCode = UtilObject.getStringOrEmpty(item.get("fty_output_code"));
				if (StringUtils.hasText(ftyOutputCode) && !StringUtils.hasText(fty_output_code)) {
					fty_output_code = ftyOutputCode;
					fty_output_name = UtilObject.getStringOrEmpty(item.get("fty_output_name"));
					location_output_code = UtilObject.getStringOrEmpty(item.get("location_output_code"));
					location_output_name = UtilObject.getStringOrEmpty(item.get("location_output_name"));
				}
				Integer allcateItemId = UtilObject.getIntegerOrNull(item.get("allocate_item_id"));
				if (allcateItemId != null) {
					matIdAllocateItemIdMap.put(matId, allcateItemId);
				}
				Map<String, Object> batchInfo = commonService.getBatchSpecList(batch, ftyId, matId);
				item.put("batchMaterialSpecList", batchInfo.get("batchMaterialSpecList"));
				item.put("batchSpecList", batchInfo.get("batchSpecList"));
			}
			for (Map<String, Object> item : returnList) {

				Integer matId = UtilObject.getIntegerOrNull(item.get("mat_id"));
				Integer allcateItemId = matIdAllocateItemIdMap.get(matId);
				item.put("allocate_item_id", allcateItemId);
				item.put("fty_output_code", fty_output_code);
				item.put("fty_output_name", fty_output_name);
				item.put("location_output_code", location_output_code);
				item.put("location_output_name", location_output_name);
			}
		}

		return returnList;
	}

	@Override
	public Map<String, Object> writeOff(BizStockInputHead stockInputHead, CurrentUser user) throws Exception {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		String userId = user.getUserId();
		Integer newOtherInput = stockInputHead.getNewOtherInput();
		Map<String, Object> returnMap = new HashMap<String, Object>();

		List<Integer> itemIds = new ArrayList<Integer>();
		if (stockInputHead.getItemList() != null && stockInputHead.getItemList().size() > 0) {
			for (BizStockInputItem item : stockInputHead.getItemList()) {
				itemIds.add(item.getItemId());
			}
		}
		if (itemIds.size() == 0) {

			errorCode = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getName();
			returnMap.put("errorCode", errorCode);
			returnMap.put("errorString", errorString);
			return returnMap;
		}
		List<BizStockInputItem> itemList = stockInputItemDao.selectByItemIds(itemIds);
		List<StockBatch> keys = new ArrayList<StockBatch>();
		List<Integer> omDocItemIds = new ArrayList<Integer>();
		if (itemList != null && itemList.size() > 0) {
			for (int i = 0; i < itemList.size(); i++) {
				BizStockInputItem item = itemList.get(i);
				StockBatch sb = new StockBatch();
				sb.setMatId(item.getMatId());
				sb.setLocationId(item.getLocationId());
				sb.setBatch(item.getBatch());
				keys.add(sb);
				omDocItemIds.add(item.getMatDocItemId());
			}

		}
		List<Integer> badList = new ArrayList<Integer>();

		List<StockBatch> stockBatchList = stockBatchDao.selectByUniqueKeys(keys);

		for (BizStockInputItem inputItem : itemList) {
			boolean find = false;
			for (StockBatch stockBatch : stockBatchList) {
				if (inputItem.getMatId().equals(stockBatch.getMatId())
						&& inputItem.getLocationId().equals(stockBatch.getLocationId())
						&& inputItem.getBatch().equals(stockBatch.getBatch())) {
					find = true;
					if (inputItem.getQty().compareTo(stockBatch.getQty()) != 0) {
						// 批次库存与入库数量不等
						badList.add(inputItem.getStockInputRid());
					}
				}

			}
			if (!find) {
				badList.add(inputItem.getStockInputRid());
			}
		}

		if (badList != null && badList.size() > 0) {
			String msg = "";
			for (Integer i : badList) {
				msg += i + ",";
			}
			msg = msg.substring(0, msg.length() - 1);

			errorCode = EnumErrorCode.ERROR_CODE_QTY_UN_EQUAL.getValue();
			errorString = EnumErrorCode.ERROR_CODE_QTY_UN_EQUAL.getName();
			returnMap.put("errorCode", errorCode);
			returnMap.put("errorString", msg + errorString);
			return returnMap;
		}
		// 过账
		List<BizMaterialDocItem> omDocItemList = materialDocItemDao.selectByItemIds(omDocItemIds);
		BizStockInputHead oStockInputHead = stockInputHeadDao.selectByPrimaryKey(stockInputHead.getStockInputId());
		oStockInputHead.setItemList(itemList);
		oStockInputHead.setCreateUser(userId);
		Integer ftyId = itemList.get(0).getFtyId();
		String postDate = commonService.getPostDate(ftyId);

		oStockInputHead.setDocTime(UtilString.getDateForString(postDate));
		oStockInputHead.setPostingDate(UtilString.getDateForString(postDate));
		Map<String, Object> postMap = stockInputSap.writeOff(oStockInputHead, omDocItemList);
		errorCode = (Integer) postMap.get("errorCode");
		errorString = (String) postMap.get("errorString");
		if (errorCode.equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {

			BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
			// 添加物料凭证修改批次库存
			commonService.modifyStockBatchByMaterialDoc(userId, mDocHead, mDocHead.getMaterialDocItemList());

			// 删除仓位库存
			this.deleteStockPositionByWriteOff(itemList);
			// 修改入库单冲销状态
			this.updateStockWriteOffStatus(itemList, stockInputHead.getWriteOffRemark());
			// 修改物料凭证冲销状态
			this.updateMatDocWriteOffStatus(itemList);
			// 删除上架请求行项目 抬头
			this.deleteStockTaskReq(itemList);
			Integer matDocId = omDocItemList.get(0).getMatDocId();
			this.deleteStockTaskReqHead(matDocId);

			// 调拨入库冲销
			if (oStockInputHead.getReceiptType().equals(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue())) {

				// 生成在途库存
				this.updateStockBatchByAllocateInputWriteOff(mDocHead.getMaterialDocItemList(), omDocItemList, userId);
				// 更改调拨单状态
				oStockInputHead.setItemList(itemList);
				this.updateAllotItemAndDeliveryItem(oStockInputHead, EnumAllocateStatus.SENDING.getValue(),
						oStockInputHead.getAllocateId(), oStockInputHead.getAllocateDeliveryId());

			}

			if (newOtherInput == 1) {
				// 生成新的入库单
				oStockInputHead.setCreateUser(userId);
				oStockInputHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				oStockInputHead.setStockInputId(null);
				oStockInputHead.setCreateTime(null);
				oStockInputHead.setModifyTime(null);
				String stockInputCode = commonService.getNextReceiptCode(EnumSequence.STOCK_INPUT.getValue());

				// 生成入库单
				oStockInputHead.setStockInputCode(stockInputCode);
				stockInputHeadDao.insert(oStockInputHead);
				for (int i = 0; i < itemList.size(); i++) {
					BizStockInputItem item = itemList.get(i);
					item.setItemId(null);
					item.setCreateTime(null);
					item.setModifyTime(null);
					item.setWriteOff((byte) 0);
					item.setStockInputId(oStockInputHead.getStockInputId());
					item.setStockInputRid(i + 1);
					item.setBatch((long) 0);
					item.setMatDocId(0);
					item.setMatDocRid(0);
					stockInputItemDao.insert(item);
				}
			}
		} else {
			throw new InterfaceCallException(errorString);
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	@Override
	public void updateInputItemMatDocInfo(List<BizMaterialDocItem> mDocItemList) throws Exception {

		for (BizMaterialDocItem mDocItem : mDocItemList) {
			BizStockInputItem updateItem = new BizStockInputItem();
			updateItem.setItemId(mDocItem.getReferReceiptItemId());
			updateItem.setMatDocId(mDocItem.getMatDocId());
			updateItem.setBatch(mDocItem.getBatch());
			updateItem.setMatDocRid(mDocItem.getMatDocRid());
			stockInputItemDao.updateByPrimaryKeySelective(updateItem);

		}

	}

	@Override
	public Map<String, Object> deleteUnFinishedInputInfo(Integer stockInputId) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		if (stockInputId != null) {
			BizStockInputHead stockInputHead = stockInputHeadDao.selectByPrimaryKey(stockInputId);

			if (stockInputHead != null
					&& stockInputHead.getStatus().equals(EnumReceiptStatus.RECEIPT_UNFINISH.getValue())) {

				if (stockInputHead.getReceiptType().equals(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue())) {
					// 调拨入更新调拨单和配送单状态
					List<BizStockInputItem> stockInputItemList = stockInputItemDao
							.selectInputItemByStockInputId(stockInputId);

					stockInputHead.setItemList(stockInputItemList);
					this.updateAllotItemAndDeliveryItem(stockInputHead, EnumAllocateStatus.SENDING.getValue(), null,
							null);

				} else {
					// 删除批次信息
					batchMaterialDao.deleteByStockInputId(stockInputId);

					// 删除批次特性
					batchMaterialSpecDao.deleteByStockInputId(stockInputId);

				}
				// 删除入库单抬头
				stockInputHeadDao.deleteByStockInputId(stockInputId);
				// 删除入库单行项目
				stockInputItemDao.deleteByStockInputId(stockInputId);
				// 经办人
				// commonService.removeReceiptApprove(zdjbh, zmkpf.getZtype());
				result.put("success", 1);
				result.put("msg", "删除成功");
			} else {
				errorCode = EnumErrorCode.ERROR_CODE_ONLY_DELETE_UNFINISH.getValue();
				errorString = EnumErrorCode.ERROR_CODE_ONLY_DELETE_UNFINISH.getName();
			}
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_NO_PARAM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_NO_PARAM.getName();
		}
		result.put("errorCode", errorCode);
		result.put("errorString", errorString);
		return result;
	}

	@Override
	public List<StockPosition> getStockPositionByItemId(Integer itemId) throws Exception {

		return stockPositionDao.selectByInputItemId(itemId);
	}

	@Override
	public void saveOperatorList(BizStockInputHead stockInputhead) throws Exception {
		if (stockInputhead.getUserList() != null) {
			for (User user : stockInputhead.getUserList()) {

				commonService.addReceiptUser(stockInputhead.getStockInputId(), stockInputhead.getReceiptType(),
						UtilObject.getStringOrEmpty(user.getUserId()), UtilObject.getIntegerOrNull(user.getRoleId()));
			}
		}

	}

	@Override
	public void saveFileList(BizStockInputHead stockInputhead) throws Exception {
		if (stockInputhead.getFileList() != null) {
			for (BizReceiptAttachment object : stockInputhead.getFileList()) {
				object.setReceiptId(stockInputhead.getStockInputId());
				object.setReceiptType(stockInputhead.getReceiptType());
				object.setUserId(stockInputhead.getCreateUser());
				object.setSize(object.getFileSize());
				String ext = UtilString.STRING_EMPTY;
				int extIndex = UtilString.getStringOrEmptyForObject(object.getFileName()).lastIndexOf('.');
				if (object.getFileName().length() - extIndex < 10) {
					ext = object.getFileName().substring(extIndex + 1);
				}
				object.setExt(ext);
				bizReceiptAttachmentDao.insertSelective(object);
			}
		}
	}

	@Override
	public Map<String, Object> addInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		if (stockInputHead.getItemList() == null || stockInputHead.getItemList().size() == 0) {
			errorCode = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getName();
			returnMap.put("errorCode", errorCode);
			returnMap.put("errorString", errorString);
			return returnMap;
		}

		// 数据初始化
		Map<String, Object> initMap = this.initInput(stockInputHead, user);
		if (!initMap.get("errorCode").equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
			return initMap;
		}
		// 库存地点锁定
		Map<String, Object> lockedCheck = this.checkLocation(stockInputHead);
		if (!lockedCheck.get("errorCode").equals(EnumErrorCode.ERROR_CODE_SUCESS.getValue())) {
			return lockedCheck;
		}

		// 过账日期
		Integer ftyId = stockInputHead.getItemList().get(0).getFtyId();
		String postDate = commonService.getPostDate(ftyId);
		stockInputHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
		stockInputHead.setCreateUser(user.getUserId());
		stockInputHead.setDocTime(UtilString.getDateForString(postDate));
		stockInputHead.setPostingDate(UtilString.getDateForString(postDate));

		// 是否新增
		if (stockInputHead.getStockInputId() != null) {
			// 是否核对账目
			if (EnumCheckAccount.CHECK_ACCOUNT_TRUE.getValue().equals(stockInputHead.getCheckAccount())) {
				BizStockInputHead oldStockInputHead = stockInputHeadDao
						.selectByPrimaryKey(stockInputHead.getStockInputId());

				stockInputHead.setDocTime(oldStockInputHead.getDocTime());
				stockInputHead.setPostingDate(oldStockInputHead.getPostingDate());

				List<BizStockInputItem> oldStockInputItems = stockInputItemDao
						.selectByStockInputId(stockInputHead.getStockInputId());
				for (BizStockInputItem oitem : oldStockInputItems) {
					for (BizStockInputItem item : stockInputHead.getItemList()) {
						if (oitem.getItemId().equals(item.getItemId())) {
							item.setStockInputRid(oitem.getStockInputRid());
							item.setMatDocId(oitem.getMatDocId());
							item.setMatDocRid(oitem.getMatDocRid());
						}
					}
				}
			}

			// 刪除旧数据
			this.deleteRkInfo(stockInputHead);
			stockInputHead.setStockInputId(null);
		} else {
			String stockInputCode = commonService.getNextReceiptCode(EnumSequence.STOCK_INPUT.getValue());
			stockInputHead.setStockInputCode(stockInputCode);
		}

		stockInputHeadDao.insertSelective(stockInputHead);
		// 添加经办人
		this.saveOperatorList(stockInputHead);
		// 添加文件
		this.saveFileList(stockInputHead);

		for (int i = 0; i < stockInputHead.getItemList().size(); i++) {
			BizStockInputItem item = stockInputHead.getItemList().get(i);

			item.setStockInputId(stockInputHead.getStockInputId());
			if (!EnumCheckAccount.CHECK_ACCOUNT_TRUE.getValue().equals(stockInputHead.getCheckAccount())
					&& item.getStockInputRid() == null) {
				item.setStockInputRid(i + 1);
			}

			item.setItemId(null);

			stockInputItemDao.insertSelective(item);

		}
		if (stockInputHead.getReceiptType().equals(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue())) {
			// 更新调拨单配送单状态
			this.updateAllotItemAndDeliveryItem(stockInputHead, EnumAllocateStatus.WAIT_INPUT.getValue(),
					stockInputHead.getAllocateId(), stockInputHead.getAllocateDeliveryId());
		}

		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	@Override
	public Map<String, Object> updateStock(BizMaterialDocHead mDocHead, BizStockInputHead stockInputHead)
			throws Exception {

		if (mDocHead.getMatDocType().equals(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue())) {
			// 调拨入库 删掉在途库存
			this.deleteMchbOnWayByDBRK(stockInputHead);
			// 修改调拨单配送单信息
			this.updateAllotItemAndDeliveryItem(stockInputHead, EnumAllocateStatus.COMPLETED.getValue(),
					stockInputHead.getAllocateId(), stockInputHead.getAllocateDeliveryId());
		}

		// 批次库存
		commonService.modifyStockBatchByMaterialDoc(mDocHead.getCreateUser(), mDocHead,
				mDocHead.getMaterialDocItemList());
		// 上架请求
		this.addStockTaskReqHeadAndItem(mDocHead);
		// 仓位库存
		this.addStockPosition(mDocHead);
		// 批次信息
		this.addBatchMaterialInfo(mDocHead);
		// 发起审批

		Integer receiptTypeId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		String receiptTypeCode = mDocHead.getMaterialDocItemList().get(0).getReferReceiptCode();
		commonService.initRoleWf(receiptTypeId, mDocHead.getMatDocType(), receiptTypeCode, mDocHead.getCreateUser());

		BizStockInputHead updateObj = new BizStockInputHead();
		Integer stockInputId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		updateObj.setStockInputId(stockInputId);
		updateObj.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		stockInputHeadDao.updateByPrimaryKeySelective(updateObj);
		this.updateInputItemMatDocInfo(mDocHead.getMaterialDocItemList());
		return null;
	}

	private Map<String, Object> initExemptInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		String purchaseOrderCode = stockInputHead.getPurchaseOrderCode();

		BizPurchaseOrderHead purchaseOrder = this.getContractInfo(purchaseOrderCode, user);

		if (purchaseOrder != null && purchaseOrder.getItemList() != null && purchaseOrder.getItemList().size() > 0) {

			// 校验 入库数量+已入库数量 <= 订单数量
			this.addStockHeadFromPurchaseOrder(stockInputHead, purchaseOrder);
			this.addStockListFromPurchaseOrderList(stockInputHead.getItemList(), purchaseOrder.getItemList());

			Map<String, Object> qtyCheckMap = this.checkQty(stockInputHead.getItemList());

			errorCode = (Integer) qtyCheckMap.get("errorCode");
			errorString = (String) qtyCheckMap.get("errorString");
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_EMPTY_ORDER_ITEM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EMPTY_ORDER_ITEM.getName();
		}

		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;

	}

	private Map<String, Object> initPlatformInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		String purchaseOrderCode = stockInputHead.getPurchaseOrderCode();

		BizPurchaseOrderHead purchaseOrder = this.getContractInfo(purchaseOrderCode, user);

		if (purchaseOrder != null && purchaseOrder.getItemList() != null && purchaseOrder.getItemList().size() > 0) {

			// 校验 入库数量+已入库数量 <= 订单数量
			this.addStockHeadFromPurchaseOrder(stockInputHead, purchaseOrder);
			this.addStockListFromPurchaseOrderList(stockInputHead.getItemList(), purchaseOrder.getItemList());

			Map<String, Object> qtyCheckMap = this.checkQty(stockInputHead.getItemList());

			errorCode = (Integer) qtyCheckMap.get("errorCode");
			errorString = (String) qtyCheckMap.get("errorString");
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_EMPTY_ORDER_ITEM.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EMPTY_ORDER_ITEM.getName();
		}

		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	private Map<String, Object> initOtherInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		for (int i = 0; i < stockInputHead.getItemList().size(); i++) {
			BizStockInputItem item = stockInputHead.getItemList().get(i);
			item.setFtyId(stockInputHead.getFtyId());
			item.setLocationId(stockInputHead.getLocationId());
			item.setMoveTypeId(stockInputHead.getMoveTypeId());
			item.setSupplierCode(stockInputHead.getSupplierCode());
			item.setSupplierName(stockInputHead.getSupplierName());
		}

		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;

	}

	private Map<String, Object> initAllocateInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		// 生成入库单抬头
		Byte findstatus = EnumAllocateStatus.SENDING.getValue();
		if (stockInputHead.getStockInputId() != null) {
			findstatus = EnumAllocateStatus.WAIT_INPUT.getValue();
		}

		Integer allocateId = stockInputHead.getAllocateId();
		Integer allocateDeliveryId = stockInputHead.getAllocateDeliveryId();

		HashMap<String, Object> findMSEGMap = new HashMap<String, Object>();
		findMSEGMap.put("allocateId", allocateId);
		findMSEGMap.put("allocateDeliveryId", allocateDeliveryId);
		findMSEGMap.put("status", findstatus);
		List<Map<String, Object>> items = this.getDBRKItemByAllotIdAndDeliveryId(findMSEGMap);

		this.addStockListFromAllocateItem(stockInputHead.getItemList(), items);
		Integer ftyId = null;
		Integer locationId = null;

		ftyId = UtilObject.getIntegerOrNull(items.get(0).get("fty_id"));
		locationId = UtilObject.getIntegerOrNull(items.get(0).get("location_id"));

		stockInputHead.setFtyId(ftyId);
		stockInputHead.setLocationId(locationId);
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);
		return returnMap;
	}

	@Override
	public Map<String, Object> initInput(BizStockInputHead stockInputHead, CurrentUser user) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		Byte type = stockInputHead.getReceiptType();
		if (type.equals(EnumReceiptType.STOCK_INPUT_EXEMPT_INSPECT.getValue())) {
			result = initExemptInput(stockInputHead, user);
		} else if (type.equals(EnumReceiptType.STOCK_INPUT_PLATFORM.getValue())) {
			result = initPlatformInput(stockInputHead, user);
		} else if (type.equals(EnumReceiptType.STOCK_INPUT_OTHER.getValue())) {
			result = initOtherInput(stockInputHead, user);
		} else if (type.equals(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue())) {
			result = initAllocateInput(stockInputHead, user);
		}

		return result;
	}

	@Override
	public BizStockInputHead getBizStockInputHeadByStockInputId(int stockInputId) throws Exception {

		return stockInputHeadDao.selectByPrimaryKey(stockInputId);
	}

	@Override
	public Map<String, Object> addOtherInput(BizStockInputHead stockInputHead) throws Exception {

		String stockInputCode = commonService.getNextReceiptCode(EnumSequence.STOCK_INPUT.getValue());
		stockInputHead.setStockInputCode(stockInputCode);
		stockInputHead.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());

		stockInputHeadDao.insertSelective(stockInputHead);
		if (stockInputHead.getItemList() != null && stockInputHead.getItemList().size() > 0) {
			int i = 1;
			for (BizStockInputItem item : stockInputHead.getItemList()) {
				item.setStockInputRid(i);
				item.setStockInputId(stockInputHead.getStockInputId());
				item.setFtyId(stockInputHead.getFtyId());

				item.setLocationId(stockInputHead.getLocationId());
				item.setMoveTypeId(stockInputHead.getMoveTypeId());
				stockInputItemDao.insertSelective(item);

				if (item.getPackageItemList() != null && item.getPackageItemList().size() > 0) {
					for (BizStockInputPackageItem packageItem : item.getPackageItemList()) {
						long batch = commonService.getBatch();
						packageItem.setBatch(batch);
						packageItem.setStockInputItemId(item.getItemId());
						stockInputPackageItemDao.insertSelective(packageItem);
					}
				} else {
					throw new WMSException("缺少包装类型");
				}

			}
		} else {
			throw new WMSException("缺少行项目");
		}
		// 生成上架请求
		addStockTaskReqHeadAndItem(stockInputHead);
		
		
		
		
		
		// 添加批次信息
		addBatchMaterialInfo(stockInputHead);
		if(stockInputHead.getStatus().equals(EnumReceiptStatus.RECEIPT_WORK.getValue())){
			this.postOtherInput(stockInputHead.getStockInputId(), stockInputHead.getCreateUser());
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addStockTaskReqHeadAndItem(BizStockInputHead stockInputHead) throws Exception {
		// 作业请求 库存地点 map
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		Map<Integer, Integer> reqIdMap = new HashMap<Integer, Integer>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();
		for (BizStockInputItem item : stockInputHead.getItemList()) {
			for (BizStockInputPackageItem packageItem : item.getPackageItemList()) {

				Integer locationId = item.getLocationId();

				// -------生成作业请求单-----相同的库存地点生成一张作业单----------------

				if (locationIdMap.containsKey(locationId)) {
					// 含有相同的库存地
					Map<String, Object> headInfo = new HashMap<String, Object>();

					headInfo = (Map<String, Object>) locationIdMap.get(locationId);

					int stockTaskReqRid = (int) headInfo.get("stockTaskReqRid");
					stockTaskReqRid = stockTaskReqRid + 1;
					headInfo.put("stockTaskReqRid", stockTaskReqRid);

					BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

					this.addStockTaskReqItem(headInfo, stockInputHead, item, packageItem, reqItem);

					addItemList.add(reqItem);

				} else {
					// --------------------- 插入抬头

					Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
					DicStockLocationVo locationObj = locationMap.get(item.getLocationId());

					Integer whId = locationObj.getWhId();

					BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
					String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
					reqhead.setStockTaskReqCode(stockTaskReqCode);
					reqhead.setFtyId(item.getFtyId());
					reqhead.setLocationId(item.getLocationId());
					reqhead.setWhId(whId);
					reqhead.setCreateUser(stockInputHead.getCreateUser());
					reqhead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
					reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
					reqhead.setReferReceiptCode(stockInputHead.getStockInputCode());
					reqhead.setReferReceiptId(stockInputHead.getStockInputId());
					reqhead.setReferReceiptType(stockInputHead.getReceiptType());
					StockTaskReqHeadDao.insertSelective(reqhead);
					
					// --------------end
					int stockTaskReqRid = 1;
					int stockTaskReqId = reqhead.getStockTaskReqId();

					Map<String, Object> headInfo = new HashMap<String, Object>();
					headInfo.put("stockTaskReqId", stockTaskReqId);// 仓库号
					headInfo.put("whId", whId);// 作业申请号
					headInfo.put("stockTaskReqRid", stockTaskReqRid);// 作业申请序号
					locationIdMap.put(locationId, headInfo);
					reqIdMap.put(stockTaskReqId,stockTaskReqId);
					BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

					this.addStockTaskReqItem(headInfo, stockInputHead, item, packageItem, reqItem);

					addItemList.add(reqItem);

				}
			}

		}
		StockTaskReqItemDao.insertReqItems(addItemList);

		if(!reqIdMap.isEmpty()) {
			for(Integer key:reqIdMap.keySet()) {
				Integer stockTaskReqId = reqIdMap.get(key);
				taskService.transportByProduct(stockTaskReqId);
			}
		}
		
		// 其他入库
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("referReceiptId", stockInputHead.getStockInputId());
		map.put("referReceiptType", stockInputHead.getReceiptType());
		Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
		if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
			// 所有请求已完成 更新入库单状态 已作业
			BizStockInputHead stockInputHeadU = new BizStockInputHead();
			stockInputHeadU.setStockInputId(stockInputHead.getStockInputId());
			stockInputHeadU.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
			stockInputHeadDao.updateByPrimaryKeySelective(stockInputHeadU);
			stockInputHead.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
		}
		
		
	}

	@Override
	public void addStockTaskReqItem(Map<String, Object> headMap, BizStockInputHead stockInputHead,
			BizStockInputItem stockInputItem, BizStockInputPackageItem packageItem, BizStockTaskReqItem reqItem)
			throws Exception {

		int stockTaskReqId = (int) headMap.get("stockTaskReqId");
		int stockTaskReqRid = (int) headMap.get("stockTaskReqRid");
		int whId = (int) headMap.get("whId");
		reqItem.setBatch(packageItem.getBatch());
		reqItem.setWhId(whId);
		reqItem.setFtyId(stockInputItem.getFtyId());
		reqItem.setLocationId(stockInputItem.getLocationId());
		reqItem.setMatId(stockInputItem.getMatId());

		reqItem.setUnitId(stockInputItem.getUnitId());
		reqItem.setQty(packageItem.getQty());
		reqItem.setPackageTypeId(packageItem.getPackageTypeId());
		reqItem.setStockTaskReqId(stockTaskReqId);
		reqItem.setStockTaskReqRid(stockTaskReqRid);
		reqItem.setProductionBatch(packageItem.getProductionBatch());
		reqItem.setErpBatch(packageItem.getErpBatch());
		reqItem.setQualityBatch(packageItem.getQualityBatch());

		reqItem.setReferReceiptCode(stockInputHead.getStockInputCode());
		reqItem.setReferReceiptId(stockInputHead.getStockInputId());
		reqItem.setReferReceiptType(stockInputHead.getReceiptType());
		reqItem.setReferReceiptRid(stockInputItem.getStockInputRid());
		reqItem.setRemark(packageItem.getRemark());
	}

	@Override
	public void addStockPosition(BizStockInputHead stockInputHead) throws Exception {
		if (stockInputHead.getItemList() != null && stockInputHead.getItemList().size() > 0) {

			for (BizStockInputItem item : stockInputHead.getItemList()) {

				if (item.getPackageItemList() != null && item.getPackageItemList().size() > 0) {
					Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
					for (BizStockInputPackageItem packageItem : item.getPackageItemList()) {

						StockPosition stockPosition = new StockPosition();

						DicStockLocationVo locationObj = locationIdMap.get(item.getLocationId());

						Integer whId = locationObj.getWhId();
						String whCode = dictionaryService.getWhCodeByWhId(whId);
						String areaCode = UtilProperties.getInstance().getDefaultCCQ();
						Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
						String positionCode = UtilProperties.getInstance().getDefaultCW();
						// TODO 没有仓库号的库存地点
						Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
								areaCode, positionCode);
						stockPosition.setWhId(whId);
						stockPosition.setAreaId(areaId);
						stockPosition.setPositionId(positionId);
						stockPosition.setMatId(item.getMatId());
						stockPosition.setFtyId(item.getFtyId());
						stockPosition.setLocationId(item.getLocationId());
						stockPosition.setBatch(packageItem.getBatch());
						stockPosition.setSpecStock(item.getSpecStock());
						stockPosition.setSpecStockCode(item.getSpecStockCode());
						stockPosition.setSpecStockName(item.getSpecStockName());

						stockPosition.setQty(packageItem.getQty());
						stockPosition.setUnitId(item.getUnitId());
						stockPosition.setInputDate(new Date());
						stockPosition.setPackageId(0);
						stockPosition.setPalletId(0);
						// 库存增加
						stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
						// 非限制库存
						stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
						List<StockSn> snList = new ArrayList<StockSn>();
						StockSn sn = new StockSn();
						sn.setSnId(0);
						sn.setQty(stockPosition.getQty());
						sn.setMatId(stockPosition.getMatId());
						sn.setDebitOrCredit(stockPosition.getDebitOrCredit());
						snList.add(sn);
						commonService.modifyStockPositionAndSn(stockPosition, snList);

					}
				} else {
					throw new WMSException("缺少包装类型");
				}

			}
		} else {
			throw new WMSException("缺少行项目");
		}

	}

	@Override
	public void addBatchMaterialInfo(BizStockInputHead stockInputHead) throws Exception {

		if (stockInputHead.getItemList() != null && stockInputHead.getItemList().size() > 0) {

			for (BizStockInputItem item : stockInputHead.getItemList()) {

				if (item.getPackageItemList() != null && item.getPackageItemList().size() > 0) {
					for (BizStockInputPackageItem packageItem : item.getPackageItemList()) {

						// 批次信息
						BatchMaterial batchMaterial = new BatchMaterial();
						batchMaterial.setBatch(packageItem.getBatch());
						batchMaterial.setMatId(item.getMatId());
						batchMaterial.setSupplierCode(item.getSupplierCode());
						batchMaterial.setSupplierName(item.getSupplierName());
						batchMaterial.setFtyId(item.getFtyId());
						batchMaterial.setSpecStock(item.getSpecStock());
						batchMaterial.setSpecStockCode(item.getSpecStockCode());
						batchMaterial.setSpecStockName(item.getSpecStockName());
						batchMaterial.setCreateUser(stockInputHead.getCreateUser());
						batchMaterial.setPackageTypeId(packageItem.getPackageTypeId());
						batchMaterial.setErpBatch(packageItem.getErpBatch());
						batchMaterial.setQualityBatch(packageItem.getQualityBatch());
						batchMaterial.setProductionBatch(packageItem.getProductionBatch());
						batchMaterial.setClassTypeId(UtilObject.getIntegerOrNull(stockInputHead.getClassTypeId()));
						batchMaterial.setProductLineId(UtilObject.getIntegerOrNull(stockInputHead.getProductLineId()));
						batchMaterial.setInstallationId(UtilObject.getIntegerOrNull(stockInputHead.getInstallationId()));
						batchMaterialDao.insertSelective(batchMaterial);

					}
				}
			}
		}

	}

	@Override
	public void modifyBatchMaterialInfo(BizStockInputHead stockInputHead) throws Exception {

		Date createTiem = new Date();
		if (stockInputHead.getItemList() != null && stockInputHead.getItemList().size() > 0) {

			for (BizStockInputItem item : stockInputHead.getItemList()) {

				if (item.getPackageItemList() != null && item.getPackageItemList().size() > 0) {
					for (BizStockInputPackageItem packageItem : item.getPackageItemList()) {

						// 批次信息
						BatchMaterial batchMaterial = new BatchMaterial();
						batchMaterial.setBatch(packageItem.getBatch());
						batchMaterial.setMatId(item.getMatId());
						batchMaterial.setFtyId(item.getFtyId());
						batchMaterial.setCreateTime(createTiem);
						batchMaterialDao.updateByUniqueKeySelective(batchMaterial);

					}
				}
			}
		}

	}
	
	void addStockBatch(BizStockInputHead stockInputHead, List<BizStockInputItem> itemList,
			List<BizStockInputPackageItem> packageList, String userId) throws Exception {

		if (packageList != null && packageList.size() > 0) {
			Map<Integer, BizStockInputItem> itemMap = new HashMap<Integer, BizStockInputItem>();
			if (itemList != null && itemList.size() > 0) {
				for (BizStockInputItem item : itemList) {
					itemMap.put(item.getItemId(), item);
				}
			} else {
				throw new WMSException("缺少行项目");
			}

			for (BizStockInputPackageItem packageItem : packageList) {
				BizStockInputItem item = itemMap.get(packageItem.getStockInputItemId());

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("matId", item.getMatId());
				map.put("locationId", item.getLocationId());
				map.put("batch", packageItem.getBatch());
				map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				map.put("createUserId", userId);
				map.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
				map.put("qty", packageItem.getQty());
				commonService.modifyStockBatch(map);
			}
		} else {
			throw new WMSException("缺少包装类型");
		}

	}

	

	@Override
	public Map<String, Object> postOtherInput(Integer stockInputId, String userId) throws Exception {
		BizStockInputHead stockInputHead = stockInputHeadDao.selectByPrimaryKey(stockInputId);
		if (stockInputHead.getStatus().equals(EnumReceiptStatus.RECEIPT_WORK.getValue())) {
			// 已作业状态过账
			List<BizStockInputItem> itemList = stockInputItemDao.selectByStockInputId(stockInputId);

			List<BizStockInputPackageItem> packageList = stockInputPackageItemDao.selectByInputId(stockInputId);
			BizStockTaskItemCw ti = new BizStockTaskItemCw();
			ti.setReferReceiptId(stockInputId);
			ti.setReferReceiptType(stockInputHead.getReceiptType());
			List<BizStockTaskItemCw> taskItemList = stockTaskItemCwDao.selectByReferReceiptIdAndType(ti);
			BizStockTaskPositionCw tp = new BizStockTaskPositionCw();
			tp.setReferReceiptId(stockInputId);
			tp.setReferReceiptType(stockInputHead.getReceiptType());
			List<BizStockTaskPositionCw> positionList = stockTaskPositionCwDao.selectByReferReceiptIdAndType(tp);
			// 添加批次库存
			addStockBatch(stockInputHead, itemList, packageList, userId);
			// 修改入库日期
			modifyBatchMaterialInfo(stockInputHead);
			// 添加仓位库存 根据已完成的作业单入库修改仓位库存
			commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);
			// 保存历史
			this.addHistory(taskItemList, positionList, EnumSynType.NO_SYN.getValue(), userId);
			// 更改入库单状态
			BizStockInputHead record = new BizStockInputHead();
			record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			record.setStockInputId(stockInputId);
			stockInputHeadDao.updateByPrimaryKeySelective(record);
			
			// 更改上架作业状态
			Map<String,Object> recordMap = new HashMap<String, Object>();
			recordMap.put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
			recordMap.put("referReceiptId",stockInputHead.getStockInputId());
			recordMap.put("referReceiptType",stockInputHead.getReceiptType());
			stockTaskHeadCwDao.updateStatusByReferReceiptIdAndType(recordMap);
		}
		return null;
	}
	void addHistory(List<BizStockTaskItemCw> taskItemList,
			List<BizStockTaskPositionCw> positionList,Byte synType, String userId) throws Exception{


		if (positionList != null && positionList.size() > 0) {

			Map<String, BizStockTaskItemCw> taskItemMap = new HashMap<String, BizStockTaskItemCw>();
			if (taskItemList != null && taskItemList.size() > 0) {
				for (BizStockTaskItemCw item : taskItemList) {
					String key = item.getStockTaskId() + "-" + item.getStockTaskRid();
					taskItemMap.put(key, item);
				}
			} else {
				throw new WMSException("缺少行项目");
			}

			for (BizStockTaskPositionCw pItem : positionList) {
				// 原仓位

				String key = pItem.getStockTaskId() + "-" + pItem.getStockTaskRid();

				BizStockTaskItemCw stockTaskItem = taskItemMap.get(key);
			


				BizBusinessHistory bh = new BizBusinessHistory();
				bh.setReferReceiptId(stockTaskItem.getReferReceiptId());
				bh.setReferReceiptRid(stockTaskItem.getReferReceiptRid());
				bh.setReferReceiptCode(stockTaskItem.getReferReceiptCode());
				bh.setReferReceiptPid(pItem.getItemPositionId());
				Byte businessType = EnumBusinessType.STOCK_INPUT.getValue();
				String debitCredit = EnumDebitCredit.DEBIT_S_ADD.getName();
				
				bh.setBusinessType(businessType);
				bh.setReceiptType(stockTaskItem.getReceiptType());
				bh.setMatId(stockTaskItem.getMatId());
				bh.setBatch(stockTaskItem.getBatch());
				bh.setFtyId(stockTaskItem.getFtyId());
				bh.setLocationId(stockTaskItem.getLocationId());
				bh.setAreaId(pItem.getSourceAreaId());
				bh.setPositionId(pItem.getSourcePositionId());
				bh.setPalletId(pItem.getPalletId());
				bh.setPackageId(pItem.getPackageId());
				bh.setQty(pItem.getQty());
				bh.setDebitCredit(debitCredit);
				bh.setCreateUser(userId);
				if(EnumSynType.MES_SAP_SYN.getValue()==synType){
					bh.setSynMes(true);
					bh.setSynSap(true);
				}else if(EnumSynType.SAP_SYN.getValue()==synType){
					bh.setSynSap(true);
				}
				
				businessHistoryDao.insertSelective(bh);

			}

		} else {
			throw new WMSException("缺少数据");
		}

	
	}

	@Override
	public List<Map<String, Object>> getStockLocationByFiveKeys() throws Exception {
		return null;
	}

}
