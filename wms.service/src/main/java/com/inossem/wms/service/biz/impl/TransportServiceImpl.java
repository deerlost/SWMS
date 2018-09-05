package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.batch.BatchMaterialSpecMapper;
import com.inossem.wms.dao.biz.BizReceiptAttachmentMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.BizStockTransportHeadMapper;
import com.inossem.wms.dao.biz.BizStockTransportItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.dao.stock.StockSnMapper;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.model.FreezeCheck;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.batch.BatchMaterialSpec;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BizStockTransportHead;
import com.inossem.wms.model.biz.BizStockTransportItem;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockOutputStatus;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.key.BatchMaterialSpecKey;
import com.inossem.wms.model.key.StockSnKey;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.BizStockTransportHeadVo;
import com.inossem.wms.model.vo.BizStockTransportItemVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.ITransportService;
import com.inossem.wms.service.intfc.IStockTransport;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilNumber;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

/**
 * 转储Service
 * 
 * @author 高海涛
 * @date 2018年2月5日
 */
@Service
public class TransportServiceImpl implements ITransportService {

	@Resource
	private BizStockTransportHeadMapper transportHeadDao;
	@Resource
	private BizStockTransportItemMapper transportItemDao;
	@Resource
	private SequenceDAO sequenceDao;
	@Resource
	private ICommonService commonService;
	@Resource
	private StockPositionMapper stockPositionDao;
	@Resource
	private IStockTransport stockTransportImpl;
	@Resource
	private DicMaterialMapper materialDao;
	@Resource
	private IFileService fileService;
	@Resource
	private BizReceiptAttachmentMapper bizReceiptAttachmentDao;
	@Resource
	private IDictionaryService dictionaryService;
	@Resource
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;
	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;
	@Resource
	private BatchMaterialMapper batchMstDao;
	@Resource
	private BatchMaterialSpecMapper batchMstSDao;
	@Resource
	private DicStockLocationMapper stockLocationDao;
	@Resource
	private StockSnMapper stockSnDao;

	/**
	 * 获取移动类型
	 */
	@Override
	public List<Map<String, Object>> getMoveList() {
		return transportHeadDao.selectMoveType();
	}

	/**
	 * 获取目标物料
	 */
	@Override
	public List<DicMaterial> getMaterialList(DicMaterial material) {
		return materialDao.selectByCondition(material);
	}

	/**
	 * 查询出库列表
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrderList(Map<String, Object> param) {
		return transportHeadDao.selectOrderListOnPaging(param);
	}

	/**
	 * 获取出库视图
	 * 
	 * @param stock_output_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public BizStockTransportHeadVo getOrderView(String stockTransportId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockTransportId", stockTransportId);

		BizStockTransportHeadVo head = transportHeadDao.selectHeadById(param);
		List<BizStockTransportItemVo> itemList = transportItemDao.selectItemById(param);

		Map<Integer, BizStockTransportItemVo> itemMap = new HashMap<Integer, BizStockTransportItemVo>();
		for (int j = 0; j < itemList.size(); j++) {
			BizStockTransportItemVo item = itemList.get(j);

			Map<String, Object> sn = new HashMap<String, Object>();
			sn.put("sn_id", UtilNumber.getIntOrZeroForInteger(item.getSnId()));
			sn.put("sn_code", UtilString.getStringOrEmptyForObject(item.getSnCode()));
			sn.put("package_code", UtilString.getStringOrEmptyForObject(item.getPackageCode()));

			if (itemMap.containsKey(item.getStockPositionId())) {
				itemMap.get(item.getStockPositionId()).getSnList().add(sn);
			} else {
				List<Map<String, Object>> snList = new ArrayList<Map<String, Object>>();
				snList.add(sn);

				item.setSnList(snList);
				itemMap.put(item.getStockPositionId(), item);
			}
		}

		List<BizStockTransportItemVo> itemListReal = new ArrayList<BizStockTransportItemVo>();
		Set<Integer> set = itemMap.keySet(); // 得到所有key的集合

		for (Integer key : set) {
			BizStockTransportItemVo item = itemMap.get(key);
			itemListReal.add(item);
		}

		head.setItemList(itemList);
		head.setSpecStock(itemList.get(0).getSpecStockOutput());
		List<ApproveUserVo> userList = commonService.getReceiptUser(head.getStockTransportId(), head.getReceiptType());
		head.setUserList(userList);

		List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(head.getStockTransportId(),
				head.getReceiptType());
		head.setFileList(fileList);
		return head;
	}

	/**
	 * 删除转储单
	 */
	@Override
	@Transactional
	public String deleteTransportOrder(int stockTransportId, int receiptType) {

		BizStockTransportHead transportHead = new BizStockTransportHead();
		transportHead.setStockTransportId(stockTransportId);
		transportHead.setIsDelete((byte) 1);
		transportHead.setModifyTime(new Date());

		BizStockTransportItem transportItem = new BizStockTransportItem();

		transportItem.setStockTransportId(stockTransportId);
		transportItem.setIsDelete((byte) 1);
		transportItem.setModifyTime(new Date());

		transportHeadDao.updateByPrimaryKeySelective(transportHead);
		transportItemDao.updateByPrimaryKeySelective(transportItem);
		commonService.removeReceiptUser(stockTransportId, receiptType);
		bizReceiptAttachmentDao.deleteByReceiptId(stockTransportId);

		return "删除成功";
	}

	/**
	 * 保存表信息
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public JSONObject saveTransportData(JSONObject jsonData, CurrentUser user, boolean isPDA) throws Exception {
		boolean isAdd = false; // 新增or修改

		/******************************************************
		 * HEAD信息处理
		 ******************************************************/
		@SuppressWarnings("unchecked")
		Map<String, Object> headData = (Map<String, Object>) jsonData;

		BizStockTransportHead transportHead = null;

		int stockTransportId = UtilObject.getIntOrZero(headData.get("stock_transport_id"));

		// 根据单号是否存在 判断新增还是修改
		if (stockTransportId == 0) {
			isAdd = true;
		} else {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stockTransportId", stockTransportId);

			transportHead = transportHeadDao.selectByPrimaryKey(param);
			if (null == transportHead || "".equals(transportHead.getStockTransportCode())
					|| transportHead.getIsDelete() == 1) {
				isAdd = true;
			}
		}

		// 保存或修改并返回对象
		transportHead = this.saveHeadData(isAdd, headData, transportHead, user, isPDA);

		// 如果当前操作为修改,则删除经办人与出库单详细信息
		if (!isAdd) {
			commonService.removeReceiptUser(transportHead.getStockTransportId(), transportHead.getReceiptType());
			bizReceiptAttachmentDao.deleteByReceiptId(transportHead.getStockTransportId());
			BizStockTransportItem itemDel = new BizStockTransportItem();
			itemDel.setStockTransportId(transportHead.getStockTransportId());
			itemDel.setIsDelete((byte) 1);
			itemDel.setModifyTime(new Date());
			transportItemDao.updateByPrimaryKeySelective(itemDel);
		}

		/******************************************************
		 * ITEM信息处理
		 ******************************************************/
		List<Map<String, String>> ftyOutList = new ArrayList<Map<String, String>>(); // 工厂状态校验参数
		List<Map<String, String>> ftyInList = new ArrayList<Map<String, String>>(); // 工厂状态校验参数
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) headData.get("item_list"); // 明细list
		for (int i = 0; i < itemList.size(); i++) {
			Map<String, Object> itemDataTemp = itemList.get(i);
			itemDataTemp.put("move_type_code", headData.get("move_type_code"));
			itemDataTemp.put("spec_stock", headData.get("spec_stock"));
			BizStockTransportItem item = this.formatPositionDataToSave(itemDataTemp, transportHead);
			Map<String, String> checkOutMap = new HashMap<String, String>();
			checkOutMap.put("location_id", String.valueOf(item.getLocationId()));
			checkOutMap.put("position_id", String.valueOf(item.getPositionId()));
			Map<String, String> checkInMap = new HashMap<String, String>();
			checkInMap.put("location_id", String.valueOf(item.getLocationInput()));

			ftyOutList.add(checkOutMap);
			ftyInList.add(checkInMap);
		}
		// 添加经办人信息
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> userList = (List<Map<String, Object>>) headData.get("user_list");
		for (int i = 0; i < userList.size(); i++) {
			Map<String, Object> userTemp = userList.get(i);
			commonService.addReceiptUser(transportHead.getStockTransportId(), transportHead.getReceiptType(),
					UtilObject.getStringOrEmpty(userTemp.get("user_id")),
					UtilObject.getIntegerOrNull(userTemp.get("role_id")));
		}

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> fileList = (List<Map<String, Object>>) headData.get("file_list");
		if (fileList != null) {
			for (Object object : fileList) {
				JSONObject attachmentObj = (JSONObject) object;
				BizReceiptAttachment bizReceiptAttachment = new BizReceiptAttachment();
				bizReceiptAttachment.setReceiptId(transportHead.getStockTransportId());
				bizReceiptAttachment.setReceiptType(transportHead.getReceiptType());
				bizReceiptAttachment.setUserId(user.getUserId());
				bizReceiptAttachment.setFileId(UtilJSON.getStringOrEmptyFromJSON("file_id", attachmentObj));
				bizReceiptAttachment.setFileName(UtilJSON.getStringOrEmptyFromJSON("file_name", attachmentObj));
				bizReceiptAttachment.setSize(attachmentObj.getInt("file_size"));
				String ext = UtilString.STRING_EMPTY;
				int extIndex = bizReceiptAttachment.getFileName().lastIndexOf('.');
				if (bizReceiptAttachment.getFileName().length() - extIndex < 10) {
					ext = bizReceiptAttachment.getFileName().substring(extIndex + 1);
				}

				bizReceiptAttachment.setExt(ext);

				bizReceiptAttachmentDao.insertSelective(bizReceiptAttachment);
			}
		}
		FreezeCheck checkFalg1 = commonService.checkWhPosAndStockLoc(ftyOutList);
		FreezeCheck checkFalg2 = commonService.checkWhPosAndStockLoc(ftyInList);

		JSONObject result = new JSONObject();
		result.put("flag", true);
		result.put("stock_transport_id", transportHead.getStockTransportId());

		if (!"".equals(checkFalg1.getResultMsgOutput())) {
			result.put("flag", false);
			result.put("message", checkFalg1.getResultMsgOutput());
		}
		if (!"".equals(checkFalg2.getResultMsgOutput())) {
			result.put("flag", false);
			result.put("message", checkFalg2.getResultMsgOutput());
		}

		return result;
	}

	/**
	 * 获取新主键
	 */
	private long getNewOrderID() {
		return sequenceDao.selectNextVal("stock_transport");
	}

	/**
	 * 保存数据
	 */
	private int saveOrderInfo(boolean isAdd, BizStockTransportHead head) {
		if (isAdd) {
			return transportHeadDao.insertSelective(head);
		} else {
			return transportHeadDao.updateByPrimaryKeySelective(head);
		}
	}

	/**
	 * 保存主表对象
	 */
	private BizStockTransportHead saveHeadData(boolean isAdd, Map<String, Object> headData, BizStockTransportHead head,
			CurrentUser user, boolean isPDA) {
		if (isAdd) {
			head = new BizStockTransportHead();
			head.setStockTransportCode(String.valueOf(this.getNewOrderID()));// 新单号

			head.setStatus(EnumStockOutputStatus.STOCK_OUTPUT_UNFINISH.getValue());
			head.setIsDelete(EnumBoolean.FALSE.getValue());
			head.setCreateUser(user.getUserId());
			head.setCreateTime(new Date());
		}

		head.setReceiptType(UtilObject.getByteOrNull(headData.get("receipt_type"))); // 类型
		head.setMoveTypeId(UtilObject.getIntegerOrNull(headData.get("move_type_id")));
		head.setFtyId(UtilObject.getIntegerOrNull(headData.get("fty_id")));
		head.setLocationId(UtilObject.getIntegerOrNull(headData.get("location_id")));
		head.setRemark(UtilObject.getStringOrEmpty(headData.get("remark")));// 备注

		head.setModifyTime(new Date()); // 最后修改时间

		if (isPDA) {
			head.setRequestSource(EnumBoolean.TRUE.getValue());
		} else {
			head.setRequestSource(EnumBoolean.FALSE.getValue());
		}

		this.saveOrderInfo(isAdd, head);

		return head;
	}

	/**
	 * 序列号表处理
	 * 
	 * @param postionData
	 * @param stockOutputId
	 * @param userId
	 * @param rid
	 * @throws Exception
	 */
	private BizStockTransportItem formatPositionDataToSave(Map<String, Object> itemData, BizStockTransportHead head)
			throws Exception {
		BizStockTransportItem item = new BizStockTransportItem();
		if (itemData.containsKey("sn_list")) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> snList = (List<Map<String, Object>>) itemData.get("sn_list");

			for (Map<String, Object> sn : snList) {
				StockSnKey key = new StockSnKey();
				key.setSnId(UtilObject.getIntegerOrNull(sn.get("sn_id")));
				if (itemData.containsKey("stock_position_id")) {
					key.setStockId(UtilObject.getIntegerOrNull(itemData.get("stock_position_id")));
				}
				List<StockSn> stockSnList = stockSnDao.selectListByUniqueKey(key);

				if (stockSnList == null || stockSnList.size() == 0) {
					throw new RuntimeException("没有查询到相关库存");
				}
				if (stockSnList.size() > 1) {
					throw new RuntimeException("序列号库存异常");
				}

				if (stockSnList.size() == 1) {
					itemData.put("stock_position_id", stockSnList.get(0).getStockId());
					if (stockSnList.get(0).getQty()
							.compareTo(UtilObject.getBigDecimalOrZero(itemData.get("transport_qty"))) >= 0) {
						item = this.saveItemData(itemData, head, stockSnList.get(0).getId(),
								UtilObject.getIntOrZero(sn.get("sn_id")));
					} else {
						throw new RuntimeException("库存已被占用");
					}
				}
			}
		} else {
			StockSnKey key = new StockSnKey();
			key.setSnId(0);
			key.setStockId(UtilObject.getIntegerOrNull(itemData.get("stock_position_id")));
			List<StockSn> stockSnList = stockSnDao.selectListByUniqueKey(key);

			if (stockSnList == null || stockSnList.size() == 0) {
				throw new RuntimeException("没有查询到相关库存");
			}
			if (stockSnList.size() > 1) {
				throw new RuntimeException("序列号库存异常");
			}

			item = this.saveItemData(itemData, head, stockSnList.get(0).getId(), 0);
		}

		return item;
	}

	/**
	 * 保存详细信息
	 */
	private BizStockTransportItem saveItemData(Map<String, Object> itemData, BizStockTransportHead head, int stockSnId,
			int snId) throws Exception {

		StockPosition stockPosition = stockPositionDao
				.selectByPrimaryKey(UtilObject.getIntegerOrNull(itemData.get("stock_position_id")));
		BigDecimal qty = UtilObject.getBigDecimalOrZero(itemData.get("transport_qty"));
		BigDecimal stockQty = new BigDecimal("0");
		if (stockPosition != null) {
			stockQty = stockPosition.getQty();
		}
		if (stockQty.compareTo(qty) >= 0) {
			BizStockTransportItem item = new BizStockTransportItem();

			item.setStockTransportId(head.getStockTransportId());
			item.setStockTransportRid(UtilObject.getIntegerOrNull(itemData.get("stock_transport_rid")));
			item.setStockPositionId(UtilObject.getIntegerOrNull(itemData.get("stock_position_id")));
			item.setStockSnId(stockSnId);
			item.setSnId(snId);
			item.setPalletId(stockPosition.getPalletId());
			item.setPackageId(stockPosition.getPackageId());
			item.setMatId(stockPosition.getMatId());
			item.setUnitId(stockPosition.getUnitId());
			item.setBatch(stockPosition.getBatch());
			item.setFtyId(stockPosition.getFtyId());
			item.setLocationId(stockPosition.getLocationId());
			item.setWhId(stockPosition.getWhId());
			item.setAreaId(stockPosition.getAreaId());
			item.setPositionId(stockPosition.getPositionId());
			item.setSpecStockCodeOutput(stockPosition.getSpecStockCode());
			item.setSpecStockNameOutput(stockPosition.getSpecStockName());
			item.setSpecStockOutput(stockPosition.getSpecStock());

			item.setTransportQty(qty);

			if (itemData.containsKey("fty_input")) {
				item.setFtyInput(UtilObject.getIntegerOrNull(itemData.get("fty_input")));
			} else {
				item.setFtyInput(stockPosition.getFtyId());
			}

			if (itemData.containsKey("location_input")) {
				item.setLocationInput(UtilObject.getIntegerOrNull(itemData.get("location_input")));
			} else {
				item.setLocationInput(stockPosition.getLocationId());
			}

			if (itemData.containsKey("mat_input_id")) {
				item.setMatInput(UtilObject.getIntegerOrNull(itemData.get("mat_input_id")));
			} else {
				item.setMatInput(stockPosition.getMatId());
			}
			
			String moveTypeCode = dictionaryService.getMoveTypeCodeByMoveTypeId(head.getMoveTypeId());
			String specStock = UtilString.getStringOrNullForObject(itemData.get("spec_stock"));

			if(moveTypeCode.equals("311")&& specStock.equals("K")){
				item.setSpecStockCodeInput(stockPosition.getSpecStockCode());
				item.setSpecStockInput("K");
			}else if (moveTypeCode.equals("412")) {
				item.setSpecStockCodeInput(UtilString.getStringOrNullForObject(itemData.get("spec_stock_code_input")));
				item.setSpecStockInput("K");
			}else{
				item.setSpecStockCodeInput("");
				item.setSpecStockInput("");
			}
			
			

			item.setCreateTime(new Date());
			item.setModifyTime(new Date());
			item.setIsDelete(EnumBoolean.FALSE.getValue());

			transportItemDao.insertSelective(item);

			return item;
		} else {
			throw new Exception("库存已被占用");
		}
	}

	/**
	 * 过账并更新表单状态
	 * 
	 * @param result
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String saveOrderToFinish(JSONObject result, String userId) throws Exception {
		if (!result.getBoolean("flag")) {
			throw new InterfaceCallException(UtilObject.getStringOrEmpty(result.get("message")));
		}

		int stockTransportId = result.getInt("stock_transport_id");

		Map<String, Object> sapReturn = stockTransportImpl.postingForTransport(stockTransportId, userId);

		if ("S".equals(sapReturn.get("isSuccess"))) {
			// 过账成功
			this.saveMatDocData((List<Map<String, String>>) sapReturn.get("sapList"), stockTransportId,
					UtilObject.getStringOrEmpty(sapReturn.get("postDate")), userId);

			this.updateTransportStatus(stockTransportId, userId);

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stockTransportId", stockTransportId);
			BizStockTransportHeadVo head = transportHeadDao.selectHeadById(param);

			commonService.initRoleWf(head.getStockTransportId(), UtilObject.getByteOrNull(head.getReceiptType()),
					head.getStockTransportCode(), head.getCreateUser());

			return UtilObject.getStringOrEmpty(sapReturn.get("message"));
		} else {
			throw new InterfaceCallException(UtilObject.getStringOrEmpty(sapReturn.get("message")));
		}

	}

	/**
	 * SAP成功后update出库表信息
	 */
	private void updateTransportStatus(int stockTransportId, String userId) {

		BizStockTransportHead head = new BizStockTransportHead();
		head.setStockTransportId(stockTransportId);
		head.setStatus(EnumStockOutputStatus.STOCK_OUTPUT_FINISH.getValue());
		head.setModifyTime(new Date());
		transportHeadDao.updateByPrimaryKeySelective(head);

	}

	/**
	 * SAP成功后更新仓位批次库存表
	 */
	private void saveMatDocData(List<Map<String, String>> sapList, int stockTransportId, String post_date,
			String userId) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockTransportId", stockTransportId);

		BizStockTransportHeadVo head = transportHeadDao.selectHeadById(param);
		List<BizStockTransportItemVo> itemList = transportItemDao.selectItemById(param);

		// 物料凭证List
		BizMaterialDocHead mDocHead = this.saveMaterialDocHead(sapList.get(0).get("matDocCode"), head, post_date);
		List<BizMaterialDocItem> mDocItemList = new ArrayList<BizMaterialDocItem>();

		for (int i = 0; i < sapList.size(); i++) {
			Map<String, String> sapData = sapList.get(i);
			for (int j = 0; j < itemList.size(); j++) {
				BizStockTransportItemVo item = itemList.get(j);

				String stockTransportCode = sapData.get("stockTransportCode"); // 出库单号
				String stockTransportRid = sapData.get("stockTransportRid"); // 行号

				if (item.getStockTransportCode().equals(stockTransportCode)
						&& item.getStockTransportRid().equals(Integer.parseInt(stockTransportRid))) {
					long newBatch = 0;
					if (sapData.containsKey("batch") && !"".equals(sapData.get("batch"))) {
						newBatch = Long.parseLong(sapData.get("batch"));
					}

					if ("S".equals(sapData.get("debitCredit"))) {

						if (newBatch == 0) {
							newBatch = sequenceDao.selectNextVal("batch");
						}

						this.saveMach(sapData, newBatch);

						BatchMaterialSpecKey specKey = new BatchMaterialSpecKey();
						specKey.setMatId(dictionaryService.getMatIdByMatCode(sapData.get("matCode")));
						specKey.setFtyId(dictionaryService.getFtyIdByFtyCode(sapData.get("ftyCode")));
						specKey.setBatch(item.getBatch());

						List<BatchMaterialSpec> batchMstSList = batchMstSDao.selectByUniqueKey(specKey);

						for (BatchMaterialSpec batchMstS : batchMstSList) {

							batchMstS.setFtyId(dictionaryService.getFtyIdByFtyCode(sapData.get("ftyCode")));
							batchMstS.setBatch(newBatch);
							batchMstS.setId(null);

							batchMstSDao.insertSelective(batchMstS);
						}
					}

					BizMaterialDocItem mDocItem = saveMaterialDocItem(sapData, head, item, newBatch);
					mDocItemList.add(mDocItem);
				}
			}
		}
		// 根据物料凭证修改批次库存
		commonService.modifyStockBatchByMaterialDoc(head.getCreateUser(), mDocHead, mDocItemList);
		// 根据转储单修改仓位库存
		this.savePositionStock(itemList, mDocItemList, userId);
	}

	/**
	 * 保存仓位库存数量
	 * 
	 * @param positionList
	 * @param itemList
	 * @param orderType
	 * @throws Exception
	 */
	private void savePositionStock(List<BizStockTransportItemVo> itemList, List<BizMaterialDocItem> mDocItemList,
			String userId) throws Exception {
		Map<String, Map<String, Object>> fty_location = new HashMap<String, Map<String, Object>>();
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		if (itemList != null) {
			for (BizMaterialDocItem mDocItem : mDocItemList) {
				for (BizStockTransportItemVo item : itemList) {
					if (mDocItem.getReferReceiptId().equals(item.getStockTransportId())
							&& mDocItem.getReferReceiptRid().equals(item.getStockTransportRid())) {
						DicStockLocationVo locationObj = locationIdMap.get(mDocItem.getLocationId());
						Integer whId = locationObj.getWhId();
						String whCode = dictionaryService.getWhCodeByWhId(whId);
						String areaCode = UtilProperties.getInstance().getDefaultCCQ();
						Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
						String positionCode = UtilProperties.getInstance().getDefaultCW();
						Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
								areaCode, positionCode);

						StockPosition stockPosition = new StockPosition();
						stockPosition.setWhId(whId);// 仓库号
						stockPosition.setFtyId(mDocItem.getFtyId());
						stockPosition.setLocationId(mDocItem.getLocationId());
						stockPosition.setMatId(mDocItem.getMatId());
						if (mDocItem.getDebitCredit().equals("H")) {
							stockPosition.setAreaId(item.getAreaId());
							stockPosition.setPositionId(item.getPositionId());
						} else if (mDocItem.getDebitCredit().equals("S")) {
							stockPosition.setAreaId(areaId);
							stockPosition.setPositionId(positionId);
						}
						stockPosition.setBatch(mDocItem.getBatch());

						// E 现有订单 K 寄售（供应商） O 供应商分包库存 Q 项目库存
						stockPosition.setSpecStock(mDocItem.getSpecStock());// 特殊库存标识
						stockPosition.setSpecStockCode(mDocItem.getSpecStockCode());// 特殊库存代码
						stockPosition.setSpecStockName(mDocItem.getSpecStockName());// 特殊库存描述
						stockPosition.setInputDate(new Date()); // TO入库日期
						stockPosition.setUnitId(item.getUnitId()); // 基本计量单位
						if (item.getPalletId() == null) {
							stockPosition.setPalletId(0);
						} else {
							stockPosition.setPalletId(item.getPalletId());
						}
						if (item.getPackageId() == null) {
							stockPosition.setPackageId(0);
						} else {
							stockPosition.setPackageId(item.getPackageId());
						}
						stockPosition.setDebitOrCredit(mDocItem.getDebitCredit());

						stockPosition.setQty(mDocItem.getQty());
						stockPosition.setStatus(mDocItem.getStatus());
						List<StockSn> snlist = new ArrayList<StockSn>();
						StockSn sn = new StockSn();

						sn.setMatId(stockPosition.getMatId());
						if (item.getSnId() == null) {
							sn.setSnId(0);
						} else {
							sn.setSnId(item.getSnId());
						}

						sn.setQty(item.getTransportQty());
						sn.setDebitOrCredit(stockPosition.getDebitOrCredit());
						snlist.add(sn);

						commonService.modifyStockPositionAndSn(stockPosition, snlist);

						if (mDocItem.getDebitCredit().equals("H")) {
							if (item.getAreaId() == areaId && item.getPositionId() == positionId) {
								// 默认仓位出库时 - 删除上架请求处理
								commonService.updateStockTaskReq(item.getBatch(), item.getMatId(), item.getFtyId(),
										item.getLocationId(), item.getTransportQty(), item.getAreaId(),
										item.getPositionId());
							}
						} else if (mDocItem.getDebitCredit().equals("S")) {
							// 默认仓位冲销时 - 上架请求处理
							// -------生成作业请求单-----相同的库存地点生成一张作业单----------------
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("batch", mDocItem.getBatch());
							map.put("matId", item.getMatId());
							map.put("locationId", item.getLocationId());
							map.put("ftyId", item.getFtyId());

							BizStockTaskReqItem taskReqItemOld = StockTaskReqItemDao.selectByUniqueKey(map);

							String ftyLocation = item.getFtyId() + "-" + item.getLocationId();
							if (null == taskReqItemOld && !fty_location.containsKey(ftyLocation)) {
								// --------------ltbk------- 插入抬头
								BizStockTaskReqHead taskReqHead = this.saveTaskReqHead(mDocItem, userId);
								Map<String, Object> taskReqHeadInfo = new HashMap<String, Object>();

								int rid = 1;

								taskReqHeadInfo.put("stockTaskReqId", taskReqHead.getStockTaskReqId());// 作业申请号
								taskReqHeadInfo.put("whId", taskReqHead.getWhId());// 仓库号
								taskReqHeadInfo.put("stockTaskReqRid", rid);// 作业申请序号
								fty_location.put(ftyLocation, taskReqHeadInfo);

								// -------------ltbl------------
								this.saveTaskReqItem(mDocItem, taskReqItemOld, taskReqHeadInfo);

							} else {
								// 含有相同的库存地
								Map<String, Object> taskReqHeadInfo = (Map<String, Object>) fty_location
										.get(ftyLocation);
								if (null == taskReqItemOld) {
									int rid = (int) taskReqHeadInfo.get("stockTaskReqRid");
									rid = rid + 1;
									taskReqHeadInfo.put("stockTaskReqRid", rid);
								}

								this.saveTaskReqItem(mDocItem, taskReqItemOld, taskReqHeadInfo);
							}
							// -------------end---生成上架作业请求----------------
						}
					}
				}
			}
		}
	}

	/**
	 * 保存物料凭证抬头
	 * 
	 * @date 2017年9月30日
	 * @author 高海涛
	 * @param sapReturnData
	 * @return
	 */
	private BizMaterialDocHead saveMaterialDocHead(String matDocCode, BizStockTransportHead head, String pstng_date)
			throws Exception {
		BizMaterialDocHead mDocHead = new BizMaterialDocHead();

		mDocHead.setMatDocCode(matDocCode);// 物料凭证号
		mDocHead.setMatDocType(head.getReceiptType()); // 业务类型

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		mDocHead.setDocTime(sdf.parse(pstng_date)); // 凭证日期
		mDocHead.setPostingDate(sdf.parse(pstng_date));

		mDocHead.setCreateUser(head.getCreateUser());

		return mDocHead;
	}

	/**
	 * 保存物料明细
	 * 
	 * @date 2017年9月30日
	 * @author 高海涛
	 * @param e_mseg
	 * @param postion
	 * @return
	 */
	private BizMaterialDocItem saveMaterialDocItem(Map<String, String> sapData, BizStockTransportHead head,
			BizStockTransportItem item, long newBatch) {
		// 保存凭证详细表数据
		BizMaterialDocItem mDocItem = new BizMaterialDocItem();
		mDocItem.setMatDocRid(Integer.parseInt(sapData.get("matDocRid"))); // 物料凭证行项目
		mDocItem.setMatDocYear(Integer.parseInt(sapData.get("matDocYear")));
		mDocItem.setMoveTypeId(head.getMoveTypeId());
		mDocItem.setMoveTypeCode(sapData.get("moveTypeCode"));
		if (newBatch == 0) {
			mDocItem.setBatch(item.getBatch());// 批次
		} else {
			mDocItem.setBatch(newBatch);// 批次
		}

		mDocItem.setMatId(dictionaryService.getMatIdByMatCode(sapData.get("matCode")));// 物料编码
		mDocItem.setFtyId(dictionaryService.getFtyIdByFtyCode(sapData.get("ftyCode"))); // 工厂
		mDocItem.setLocationId(
				dictionaryService.getLocIdByFtyCodeAndLocCode(sapData.get("ftyCode"), sapData.get("locationCode"))); // 库存地点

		mDocItem.setSpecStock(sapData.get("specStock"));
		mDocItem.setSpecStockCode(sapData.get("specStockCode"));
		mDocItem.setSpecStockName(sapData.get("specStockName"));

		mDocItem.setSupplierCode(sapData.get("supplierCode")); // 供应商编码
		mDocItem.setSupplierName(sapData.get("supplierName")); // 供应商名称

		mDocItem.setCustomerCode(sapData.get("customerCode")); // 客户编码
		mDocItem.setCustomerName(sapData.get("customerName")); // 客户名名称

		mDocItem.setSaleOrderCode(sapData.get("saleOrderCode")); // 销售订单号
		mDocItem.setSaleOrderProjectCode(sapData.get("saleOrderProjectCode")); // 销售订单项目编号
		mDocItem.setSaleOrderDeliveredPlan(sapData.get("saleOrderDeliveredPlan")); // 销售订单交货计划
		mDocItem.setStandardCurrencyMoney(UtilObject.getBigDecimalOrZero(sapData.get("standardCurrencyMoney"))); // 金额

		mDocItem.setQty(item.getTransportQty());
		mDocItem.setUnitId(dictionaryService.getUnitIdByUnitCode(sapData.get("unitCode")));// 基本计量单位
		mDocItem.setReferReceiptId(head.getStockTransportId());
		mDocItem.setReferReceiptCode(head.getStockTransportCode()); // 参考单据号
		mDocItem.setReferReceiptRid(item.getStockTransportRid()); // 参考单行号

		mDocItem.setCreateTime(new Date());
		mDocItem.setDebitCredit(sapData.get("debitCredit"));// 借贷标识 "H：贷方 数量减
															// S：借方 数量加 "

		String insmk = sapData.get("stockStatus");// 库存状态

		// 设置库存状态
		if ("".equals(insmk)) {
			// 非限制库存
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
		} else if ("X".equals(insmk)) {
			// 质量检验库存
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_QUALITY_INSPECTION.getValue());
		} else if ("S".equals(insmk)) {
			// 冻结库存
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_FREEZE.getValue());
		} else if ("T".equals(insmk)) {
			// 在途库存
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_ON_THE_WAY.getValue());
		}

		return mDocItem;
	}

	/**
	 * 保存新批次信息
	 * 
	 * @date 2017年10月27日
	 * @author 高海涛
	 * @param e_mseg
	 * @param newBatch
	 */
	private void saveMach(Map<String, String> sapData, Long newBatch) {
		BatchMaterial batchMst = new BatchMaterial();

		batchMst.setMatId(dictionaryService.getMatIdByMatCode(sapData.get("matCode")));
		batchMst.setFtyId(dictionaryService.getFtyIdByFtyCode(sapData.get("ftyCode")));
		batchMst.setBatch(newBatch);

		batchMst.setSupplierCode(sapData.get("supplierCode"));
		batchMst.setSupplierName(sapData.get("supplierName"));

		batchMst.setPurchaseOrderCode(sapData.get("matDocCode"));
		batchMst.setPurchaseOrderRid(sapData.get("matDocRid"));

		batchMst.setCreateUser("SAP");

		batchMstDao.insertSelective(batchMst);
	}

	private BizStockTaskReqHead saveTaskReqHead(BizMaterialDocItem mDocItem, String userId) throws Exception {

		String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());

		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(mDocItem.getLocationId());

		BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
		taskReqHead.setStockTaskReqCode(stockTaskReqCode);
		taskReqHead.setWhId(locationObj.getWhId());
		taskReqHead.setFtyId(mDocItem.getFtyId());
		taskReqHead.setLocationId(mDocItem.getLocationId());
		taskReqHead.setMatDocId(mDocItem.getMatDocId());
		taskReqHead.setMatDocYear(mDocItem.getMatDocYear());
		taskReqHead.setCreateUser(userId);
		taskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());

		StockTaskReqHeadDao.insertSelective(taskReqHead);

		return taskReqHead;
	}

	private void saveTaskReqItem(BizMaterialDocItem item, BizStockTaskReqItem taskReqItemOld,
			Map<String, Object> taskReqHeadInfo) {
		BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();

		if (taskReqItemOld != null) {
			taskReqItem.setStockTaskReqId(taskReqItemOld.getStockTaskReqId());
			taskReqItem.setStockTaskReqRid(taskReqItemOld.getStockTaskReqRid());

			if (taskReqItemOld.getStatus() == 1) {
				taskReqItem.setStatus((byte) 0);
				BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
				taskReqHead.setStockTaskReqId(taskReqItemOld.getStockTaskReqId());
				taskReqHead.setStatus((byte) 0);
				StockTaskReqHeadDao.updateByPrimaryKeySelective(taskReqHead);
			}
			taskReqItem.setQty(taskReqItemOld.getQty().add(item.getQty()));

			StockTaskReqItemDao.updateByPrimaryKeySelective(taskReqItem);
		} else {

			taskReqItem.setStockTaskReqId((int) taskReqHeadInfo.get("stockTaskReqId"));
			taskReqItem.setWhId((int) taskReqHeadInfo.get("whId"));
			taskReqItem.setStockTaskReqRid((int) taskReqHeadInfo.get("stockTaskReqRid"));

			taskReqItem.setBatch(item.getBatch());

			taskReqItem.setFtyId(item.getFtyId());
			taskReqItem.setLocationId(item.getLocationId());
			taskReqItem.setMatId(item.getMatId());
			taskReqItem.setMatDocId(item.getMatDocId());
			taskReqItem.setMatDocRid(item.getMatDocRid());
			taskReqItem.setUnitId(item.getUnitId());
			taskReqItem.setQty(item.getQty());
			taskReqItem.setSpecStock(item.getSpecStock());
			taskReqItem.setSpecStockCode(item.getSpecStockCode());
			taskReqItem.setSpecStockName(item.getSpecStockName());

			StockTaskReqItemDao.insertSelective(taskReqItem);
		}
	}

	/**
	 * 根据转储单id查询转储单头信息
	 */
	@Override
	public BizStockTransportHead getBizStockTransportHeadByStockTransportId(int stockTransportId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockTransportId", stockTransportId);
		return transportHeadDao.selectByPrimaryKey(param);
	}
}
