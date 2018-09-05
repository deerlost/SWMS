package com.inossem.wms.service.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqPositionMapper;
import com.inossem.wms.dao.biz.BizStockTransportHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTransportItemCwMapper;
import com.inossem.wms.dao.biz.BizStockTransportPositionCwMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BizStockTaskReqPosition;
import com.inossem.wms.model.biz.BizStockTransportHeadCw;
import com.inossem.wms.model.biz.BizStockTransportItemCw;
import com.inossem.wms.model.biz.BizStockTransportPositionCw;
import com.inossem.wms.model.enums.EnumBusinessType;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumIsUrgent;
import com.inossem.wms.model.enums.EnumMatStoreType;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IProductionTransportService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.util.UtilProperties;

@Service("productionTransportService")
@Transactional
public class ProductionTransportServiceImpl implements IProductionTransportService {

	@Autowired
	private BizStockTransportHeadCwMapper stockTransportHeadCwDao;

	@Autowired
	private BizStockTransportItemCwMapper stockTransportItemCwDao;

	@Autowired
	private BizStockTransportPositionCwMapper stockTransportPositionCwDao;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;

	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;

	@Autowired
	private BizStockTaskReqPositionMapper StockTaskReqPositionDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;
	@Autowired
	private BizBusinessHistoryMapper businessHistoryDao;

	@Autowired
	private ITaskService taskService;

	@Override
	public Map<String, Object> addProductionTransport(BizStockTransportHeadCw stockTransportHead) throws Exception {

		String stockTransportCode = commonService.getNextReceiptCode("stock_transport");
		stockTransportHead.setStockTransportCode(stockTransportCode);
		stockTransportHead.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
		// 移动类型 工厂转储
		Integer moveTypeId = dictionaryService.getMoveTypeIdByMoveTypeCodeAndSpecStock("301", "");
		stockTransportHead.setMoveTypeId(moveTypeId);
		stockTransportHeadCwDao.insertSelective(stockTransportHead);
		commonService.saveFileList(stockTransportHead.getStockTransportId(), stockTransportHead.getReceiptType(),
				stockTransportHead.getCreateUser(), stockTransportHead.getFileList());
		if (stockTransportHead.getItemList() != null && stockTransportHead.getItemList().size() > 0) {
			int i = 1;
			for (BizStockTransportItemCw item : stockTransportHead.getItemList()) {
				item.setStockTransportId(stockTransportHead.getStockTransportId());
				item.setStockTransportRid(i);
				i++;
				item.setFtyOutput(stockTransportHead.getFtyId());
				item.setLocationOutput(stockTransportHead.getLocationId());
				item.setMatInput(item.getMatId());
				item.setProductionBatchInput(item.getProductionBatch());
				item.setErpBatchInput(item.getErpBatch());
				item.setQualityBatchInput(item.getQualityBatch());
				// 冻结
				// StockOccupancy stockOccupancy = new StockOccupancy();
				// stockOccupancy.setBatch(item.getBatch());
				//
				// stockOccupancy.setFtyId(item.getFtyOutput());
				// stockOccupancy.setLocationId(item.getLocationOutput());
				// stockOccupancy.setMatId(item.getMatId());
				// stockOccupancy.setQty(item.getTransportQty());
				// stockOccupancy.setPackageTypeId(item.getPackageTypeId());
				// stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				// if(stockTransportHead.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())){
				//
				// stockOccupancy.setIsUrgent(EnumIsUrgent.URGENT.getValue());
				// }
				//
				// commonService.modifyStockOccupancy(stockOccupancy,
				// EnumDebitCredit.DEBIT_S_ADD.getName());

				stockTransportItemCwDao.insertSelective(item);
				if (!item.getFtyInput().equals(item.getFtyOutput())) {
					// 批次信息
					BatchMaterial param = new BatchMaterial();
					param.setBatch(item.getBatch());
					param.setMatId(item.getMatId());
					param.setFtyId(item.getFtyOutput());
					BatchMaterial batchMaterial = new BatchMaterial();
					BatchMaterial find = batchMaterialDao.selectByUniqueKey(param);
					if (find == null) {
						throw new WMSException("无生产批次信息");
					}

					batchMaterial.setBatch(item.getBatch());
					batchMaterial.setMatId(item.getMatInput());
					batchMaterial.setFtyId(item.getFtyInput());
					batchMaterial.setProductLineId(find.getProductLineId());
					batchMaterial.setInstallationId(find.getInstallationId());
					batchMaterial.setClassTypeId(stockTransportHead.getClassTypeId());
					batchMaterial.setPurchaseOrderCode(find.getPurchaseOrderCode());
					batchMaterial.setCreateUser(stockTransportHead.getCreateUser());
					batchMaterial.setPackageTypeId(item.getPackageTypeId());
					batchMaterial.setErpBatch(item.getErpBatch());
					batchMaterial.setQualityBatch(item.getQualityBatch());
					batchMaterial.setProductionBatch(item.getProductionBatch());
					batchMaterialDao.insertSelective(batchMaterial);

				}

				if (item.getPositionList() != null && item.getPositionList().size() > 0) {
					for (BizStockTransportPositionCw pItem : item.getPositionList()) {
						pItem.setStockTransportId(item.getStockTransportId());
						pItem.setStockTransportRid(item.getStockTransportRid());
						pItem.setBatch(item.getBatch());
						stockTransportPositionCwDao.insertSelective(pItem);
					}
				}
			}
			// 生成上架请求
			this.addStockTaskReqHeadAndItem(stockTransportHead);
			// 修改仓位库存
			this.modifyStockPosition(stockTransportHead);

		} else {
			throw new WMSException("缺少行项目");
		}
		return null;
	}

	void modifyStockPosition(BizStockTransportHeadCw stockTransportHead) throws Exception {
		Map<Integer, Map<String, Integer>> whIdMap = new HashMap<Integer, Map<String, Integer>>();

		if (stockTransportHead.getItemList() != null && stockTransportHead.getItemList().size() > 0) {
			for (BizStockTransportItemCw item : stockTransportHead.getItemList()) {
				if (item.getPositionList() == null || item.getPositionList().size() == 0) {
					BizStockTransportPositionCw p = new BizStockTransportPositionCw();
					p.setQty(item.getTransportQty());
					List<BizStockTransportPositionCw> pList = new ArrayList<BizStockTransportPositionCw>();
					pList.add(p);
					item.setPositionList(pList);
				}

				if (item.getPositionList() != null && item.getPositionList().size() > 0) {
					for (BizStockTransportPositionCw pItem : item.getPositionList()) {

						StockPosition stockPosition = new StockPosition();

						Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
						DicStockLocationVo locationObj = locationMap.get(item.getLocationOutput());

						Integer whId = locationObj.getWhId();
						Integer areaId = 0;
						Integer positionId = 0;
						if (whIdMap.containsKey(whId)) {
							Map<String, Integer> whObjMap = whIdMap.get(whId);
							areaId = whObjMap.get("areaId");
							positionId = whObjMap.get("positionId");
						} else {
							String whCode = dictionaryService.getWhCodeByWhId(whId);
							String areaCode = UtilProperties.getInstance().getDefaultCCQ();
							areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
							String positionCode = UtilProperties.getInstance().getDefaultCW();
							positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
									positionCode);
							Map<String, Integer> whObjMap = new HashMap<String, Integer>();
							whObjMap.put("areaId", areaId);
							whObjMap.put("positionId", positionId);

							whIdMap.put(whId, whObjMap);
						}
						stockPosition.setWhId(whId);

						stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
						stockPosition.setAreaId(areaId);
						stockPosition.setPositionId(positionId);

						stockPosition.setMatId(item.getMatId());
						stockPosition.setFtyId(item.getFtyOutput());
						stockPosition.setLocationId(item.getLocationOutput());
						stockPosition.setBatch(item.getBatch());

						stockPosition.setInputDate(new Date());
						stockPosition.setQty(pItem.getQty());
						stockPosition.setUnitId(item.getUnitId());
						stockPosition.setInputDate(new Date());
						if (item.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
							// 启用包
							if (pItem.getPackageId() == null) {
								throw new WMSException("包装类型出错");
							}
							stockPosition.setPackageId(pItem.getPackageId());
							if (pItem.getPalletId() == null) {
								stockPosition.setPalletId(0);
							} else {
								stockPosition.setPalletId(pItem.getPalletId());
							}

						} else {
							// 不启用包
							stockPosition.setPackageId(0);
							stockPosition.setPalletId(0);
						}
						stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
						stockPosition.setPackageTypeId(item.getPackageTypeId());
						if (stockTransportHead.getReceiptType()
								.equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())) {
							// 紧急记账转运
							stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
						}
						commonService.modifyStockPosition(stockPosition);
						// 历史记录
						BizBusinessHistory bh = new BizBusinessHistory();
						bh.setReferReceiptId(item.getStockTransportId());
						bh.setReferReceiptRid(item.getStockTransportRid());
						bh.setReferReceiptCode(stockTransportHead.getStockTransportCode());

						Byte businessType = EnumBusinessType.STOCK_TASK.getValue();
						String debitCredit = EnumDebitCredit.CREDIT_H_SUBTRACT.getName();

						bh.setBusinessType(businessType);
						bh.setReceiptType(stockTransportHead.getReceiptType());
						bh.setMatId(item.getMatId());
						bh.setBatch(item.getBatch());
						bh.setFtyId(item.getFtyOutput());
						bh.setLocationId(item.getLocationOutput());
						bh.setAreaId(areaId);
						bh.setPositionId(positionId);
						bh.setPalletId(pItem.getPalletId());
						bh.setPackageId(pItem.getPackageId());
						bh.setQty(pItem.getQty());
						bh.setDebitCredit(debitCredit);
						bh.setCreateUser(stockTransportHead.getCreateUser());
						businessHistoryDao.insertSelective(bh);

					}
				}
			}
		} else {
			throw new WMSException("缺少行项目");
		}
	}

	void modifyStockPositionByDelete(BizStockTransportHeadCw stockTransportHead) throws Exception {
		Map<Integer, Map<String, Integer>> whIdMap = new HashMap<Integer, Map<String, Integer>>();

		if (stockTransportHead.getItemList() != null && stockTransportHead.getItemList().size() > 0) {
			for (BizStockTransportItemCw item : stockTransportHead.getItemList()) {
				if (item.getPositionList() == null || item.getPositionList().size() == 0) {
					BizStockTransportPositionCw p = new BizStockTransportPositionCw();
					p.setQty(item.getTransportQty());
					List<BizStockTransportPositionCw> pList = new ArrayList<BizStockTransportPositionCw>();
					pList.add(p);
					item.setPositionList(pList);
				}

				if (item.getPositionList() != null && item.getPositionList().size() > 0) {
					for (BizStockTransportPositionCw pItem : item.getPositionList()) {

						StockPosition stockPosition = new StockPosition();

						Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
						DicStockLocationVo locationObj = locationMap.get(item.getLocationOutput());

						Integer whId = locationObj.getWhId();
						Integer areaId = 0;
						Integer positionId = 0;
						if (whIdMap.containsKey(whId)) {
							Map<String, Integer> whObjMap = whIdMap.get(whId);
							areaId = whObjMap.get("areaId");
							positionId = whObjMap.get("positionId");
						} else {
							String whCode = dictionaryService.getWhCodeByWhId(whId);
							String areaCode = UtilProperties.getInstance().getDefaultCCQ();
							areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
							String positionCode = UtilProperties.getInstance().getDefaultCW();
							positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
									positionCode);
							Map<String, Integer> whObjMap = new HashMap<String, Integer>();
							whObjMap.put("areaId", areaId);
							whObjMap.put("positionId", positionId);

							whIdMap.put(whId, whObjMap);
						}
						stockPosition.setWhId(whId);

						stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
						stockPosition.setAreaId(areaId);
						stockPosition.setPositionId(positionId);

						stockPosition.setMatId(item.getMatId());
						stockPosition.setFtyId(item.getFtyOutput());
						stockPosition.setLocationId(item.getLocationOutput());
						stockPosition.setBatch(item.getBatch());

						stockPosition.setInputDate(new Date());
						stockPosition.setQty(pItem.getQty());
						stockPosition.setUnitId(item.getUnitId());
						stockPosition.setInputDate(new Date());
						if (item.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
							// 启用包
							if (pItem.getPackageId() == null) {
								throw new WMSException("包装类型出错");
							}
							stockPosition.setPackageId(pItem.getPackageId());
							if (pItem.getPalletId() == null) {
								stockPosition.setPalletId(0);
							} else {
								stockPosition.setPalletId(pItem.getPalletId());
							}

						} else {
							// 不启用包
							stockPosition.setPackageId(0);
							stockPosition.setPalletId(0);
						}
						stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
						stockPosition.setPackageTypeId(item.getPackageTypeId());
						if (stockTransportHead.getReceiptType()
								.equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())) {
							// 紧急记账转运
							stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
							commonService.modifyStockPositionOnSynByDelete(stockPosition);
						}else{
							commonService.modifyStockPosition(stockPosition);
						}
						
						// 历史记录
						BizBusinessHistory bh = new BizBusinessHistory();
						bh.setReferReceiptId(item.getStockTransportId());
						bh.setReferReceiptRid(item.getStockTransportRid());
						bh.setReferReceiptCode(stockTransportHead.getStockTransportCode());

						Byte businessType = EnumBusinessType.STOCK_TASK.getValue();
						String debitCredit = EnumDebitCredit.DEBIT_S_ADD.getName();

						bh.setBusinessType(businessType);
						bh.setReceiptType(stockTransportHead.getReceiptType());
						bh.setMatId(item.getMatId());
						bh.setBatch(item.getBatch());
						bh.setFtyId(item.getFtyOutput());
						bh.setLocationId(item.getLocationOutput());
						bh.setAreaId(areaId);
						bh.setPositionId(positionId);
						bh.setPalletId(pItem.getPalletId());
						bh.setPackageId(pItem.getPackageId());
						bh.setQty(pItem.getQty());
						bh.setDebitCredit(debitCredit);
						bh.setCreateUser(stockTransportHead.getCreateUser());
						businessHistoryDao.insertSelective(bh);

					}
				}
			}
		} else {
			throw new WMSException("缺少行项目");
		}
	}

	@SuppressWarnings("unchecked")
	public void addStockTaskReqHeadAndItem(BizStockTransportHeadCw stockTransportHead) throws Exception {
		// 作业请求 库存地点 map
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		Map<Integer, Integer> reqIdMap = new HashMap<Integer, Integer>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();
		for (BizStockTransportItemCw item : stockTransportHead.getItemList()) {

			Integer locationId = item.getLocationInput();

			// -------生成作业请求单-----相同的库存地点生成一张作业单----------------

			if (locationIdMap.containsKey(locationId)) {
				// 含有相同的库存地
				Map<String, Object> headInfo = new HashMap<String, Object>();

				headInfo = (Map<String, Object>) locationIdMap.get(locationId);

				int stockTaskReqRid = (int) headInfo.get("stockTaskReqRid");
				stockTaskReqRid = stockTaskReqRid + 1;
				headInfo.put("stockTaskReqRid", stockTaskReqRid);

				BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

				this.addStockTaskReqItem(headInfo, stockTransportHead, item, reqItem);

				addItemList.add(reqItem);

			} else {
				// --------------------- 插入抬头

				Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
				DicStockLocationVo locationObj = locationMap.get(item.getLocationInput());

				Integer whId = locationObj.getWhId();

				BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
				String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
				reqhead.setStockTaskReqCode(stockTaskReqCode);
				reqhead.setFtyId(item.getFtyInput());
				reqhead.setLocationId(item.getLocationInput());
				reqhead.setWhId(whId);
				reqhead.setCreateUser(stockTransportHead.getCreateUser());
				reqhead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
				reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
				reqhead.setReferReceiptCode(stockTransportHead.getStockTransportCode());
				reqhead.setReferReceiptId(stockTransportHead.getStockTransportId());
				reqhead.setReferReceiptType(stockTransportHead.getReceiptType());
				reqhead.setRemark(stockTransportHead.getRemark());
				reqhead.setDeliveryDriver(stockTransportHead.getDriver());
				reqhead.setDeliveryVehicle(stockTransportHead.getVehicle());
				StockTaskReqHeadDao.insertSelective(reqhead);
				commonService.saveFileList(reqhead.getStockTaskReqId(), reqhead.getReceiptType(),
						reqhead.getCreateUser(), stockTransportHead.getFileList());
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

				this.addStockTaskReqItem(headInfo, stockTransportHead, item, reqItem);

				addItemList.add(reqItem);

			}

		}
		StockTaskReqItemDao.insertReqItems(addItemList);

		if(!reqIdMap.isEmpty()) {
			for(Integer key:reqIdMap.keySet()) {
				Integer stockTaskReqId = reqIdMap.get(key);
				taskService.transportByProduct(stockTaskReqId);
			}
		}
		
		
	}

	public void addStockTaskReqItem(Map<String, Object> headMap, BizStockTransportHeadCw stockTransportHead,
			BizStockTransportItemCw tItem, BizStockTaskReqItem reqItem) throws Exception {

		int stockTaskReqId = (int) headMap.get("stockTaskReqId");
		int stockTaskReqRid = (int) headMap.get("stockTaskReqRid");
		int whId = (int) headMap.get("whId");
		reqItem.setBatch(tItem.getBatch());
		reqItem.setWhId(whId);
		reqItem.setFtyId(tItem.getFtyInput());
		reqItem.setLocationId(tItem.getLocationInput());
		reqItem.setMatId(tItem.getMatInput());

		reqItem.setUnitId(tItem.getUnitId());
		reqItem.setQty(tItem.getTransportQty());
		reqItem.setPackageTypeId(tItem.getPackageTypeId());
		reqItem.setStockTaskReqId(stockTaskReqId);
		reqItem.setStockTaskReqRid(stockTaskReqRid);
		reqItem.setProductionBatch(tItem.getProductionBatchInput());
		reqItem.setErpBatch(tItem.getErpBatchInput());
		reqItem.setQualityBatch(tItem.getQualityBatchInput());

		reqItem.setReferReceiptCode(stockTransportHead.getStockTransportCode());
		reqItem.setReferReceiptId(tItem.getStockTransportId());
		reqItem.setReferReceiptType(stockTransportHead.getReceiptType());
		reqItem.setReferReceiptRid(tItem.getStockTransportRid());
		reqItem.setRemark(tItem.getRemark());
		if (tItem.getPositionList() != null && tItem.getPositionList().size() > 0) {
			int positionRid = 1;
			for (BizStockTransportPositionCw p : tItem.getPositionList()) {
				BizStockTaskReqPosition record = new BizStockTaskReqPosition();
				record.setStockTaskReqId(stockTaskReqId);
				record.setStockTaskReqRid(stockTaskReqRid);
				record.setStockTaskReqPositionId(positionRid);
				positionRid++;
				record.setBatch(p.getBatch());
				record.setPalletId(p.getPalletId());
				record.setPackageId(p.getPackageId());
				record.setQty(p.getQty());
				StockTaskReqPositionDao.insertSelective(record);
			}
		}
	}

	@Override
	public List<BizStockTransportHeadCw> getHeadList(Map<String, Object> map) throws Exception {

		return stockTransportHeadCwDao.selectByParamsOnpaging(map);
	}

	@Override
	public List<Map<String, Object>> getItemListByHeadId(Integer stockTransportId) throws Exception {
		return stockTransportItemCwDao.selectItemById(stockTransportId);
	}

	@Override
	public List<Map<String, Object>> getPositionItemListByParams(Map<String, Object> map) throws Exception {

		return stockTransportPositionCwDao.selectByParams(map);
	}

	@Override
	public void deleteById(Integer stockTransportId, Byte receiptType) throws Exception {
		taskService.deleteTaskReqAndTaskByReferIdAndType(stockTransportId, receiptType);
		// 还原生产库 库存
		List<BizStockTransportHeadCw> list = new ArrayList<BizStockTransportHeadCw>();

		BizStockTransportHeadCw head = null;
		Map<String, Object> map = new HashMap<>();
		map.put("stockTransportId", stockTransportId);
		// 获取作业单抬头
		list = this.getHeadList(map);
		if (list != null && list.size() > 0) {
			head = list.get(0);
		}
		List<BizStockTransportItemCw> getList = new ArrayList<BizStockTransportItemCw>();
		getList = stockTransportItemCwDao.selectItemVoById(stockTransportId);

		if (getList != null && getList.size() > 0) {
			for (BizStockTransportItemCw item : getList) {

				Map<String, Object> positionFindMap = new HashMap<String, Object>();
				positionFindMap.put("stockTransportId", item.getStockTransportId());
				positionFindMap.put("stockTransportRid", item.getStockTransportRid());

				List<BizStockTransportPositionCw> positionList = stockTransportPositionCwDao
						.selectVoByParams(positionFindMap);

				item.setPositionList(positionList);

			}
		}
		if (head == null) {
			throw new WMSException("未查到单据");
		}
		head.setItemList(getList);
		this.modifyStockPositionByDelete(head);
		// 删除单据
		BizStockTransportHeadCw recordHead = new BizStockTransportHeadCw();
		recordHead.setStockTransportId(stockTransportId);
		recordHead.setIsDelete((byte) 1);
		stockTransportHeadCwDao.updateByPrimaryKeySelective(recordHead);
		BizStockTransportItemCw recordItem = new BizStockTransportItemCw();
		recordItem.setStockTransportId(stockTransportId);
		recordItem.setIsDelete((byte) 1);
		stockTransportItemCwDao.updateDeleteByPrimaryKey(recordItem);
		BizStockTransportPositionCw recordPtem = new BizStockTransportPositionCw();
		recordPtem.setStockTransportId(stockTransportId);
		recordPtem.setIsDelete((byte) 1);
		stockTransportPositionCwDao.updateDeleteByPrimaryKey(recordPtem);

	}

}
