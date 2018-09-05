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
import com.inossem.wms.dao.biz.BizAssetCardMapper;
import com.inossem.wms.dao.biz.BizMatReqHeadMapper;
import com.inossem.wms.dao.biz.BizMatReqItemMapper;
import com.inossem.wms.dao.biz.BizReceiptAttachmentMapper;
import com.inossem.wms.dao.biz.BizStockOutputHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputPositionMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.dao.stock.StockSnMapper;
import com.inossem.wms.dao.syn.SynMaterialDocItemMapper;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.model.FreezeCheck;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.batch.BatchMaterialSpec;
import com.inossem.wms.model.biz.BizAssetCard;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.biz.BizReserveOrderItem;
import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.biz.BizStockOutputItem;
import com.inossem.wms.model.biz.BizStockOutputPosition;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.dic.DicStockLocation;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockOutputStatus;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.key.BatchMaterialSpecKey;
import com.inossem.wms.model.key.StockSnKey;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.syn.SynMaterialDocItem;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.BizStockOutputHeadVo;
import com.inossem.wms.model.vo.BizStockOutputItemVo;
import com.inossem.wms.model.vo.BizStockOutputPositionVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.IOutPutService;
import com.inossem.wms.service.intfc.IStockOutput;
import com.inossem.wms.service.intfc.IStockOutputReturn;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilNumber;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

/**
 * 出库单Service
 * 
 * @author 高海涛
 * @date 2018年1月19日
 */
@Service
public class OutPutServiceImpl implements IOutPutService {

	@Resource
	private BizStockOutputHeadMapper outPutHeadDao;
	@Resource
	private BizStockOutputItemMapper outPutItemDao;
	@Resource
	private BizStockOutputPositionMapper outPutPositionDao;
	@Resource
	private BizAssetCardMapper assetCartDao;
	@Resource
	private SequenceDAO sequenceDao;
	@Resource
	private ICommonService commonService;
	@Resource
	private IStockOutput stockOutputImpl;
	@Resource
	private BizMatReqHeadMapper matReqHeadDao;
	@Resource
	private BizMatReqItemMapper matReqItemDao;
	@Resource
	private SynMaterialDocItemMapper synMDocItemDao;
	@Resource
	private StockPositionMapper stockPositionDao;
	@Resource
	private IFileService fileService;
	@Resource
	private BizReceiptAttachmentMapper bizReceiptAttachmentDao;
	@Resource
	private IDictionaryService dictionaryService;
	@Resource
	private BatchMaterialMapper batchMstDao;
	@Resource
	private BatchMaterialSpecMapper batchMstSDao;
	@Resource
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;
	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;
	@Resource
	private DicStockLocationMapper stockLocationDao;
	@Resource
	private BizAssetCardMapper assetCardDao;
	@Resource
	private StockSnMapper stockSnDao;
	@Resource
	private IStockOutputReturn stockOutputReturnSap;

	// MyTask 通用功能
	/**
	 * 查询出库列表
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrderList(Map<String, Object> param) {
		return outPutHeadDao.selectOrderListOnPaging(param);
	}

	/**
	 * 获取出库视图
	 * 
	 * @param stock_output_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public BizStockOutputHeadVo getOrderView(String stock_output_id) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stock_output_id);

		BizStockOutputHeadVo head = outPutHeadDao.selectHeadByOrderId(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);

		for (int i = 0; i < itemList.size(); i++) {
			List<BizStockOutputPositionVo> positionList = new ArrayList<BizStockOutputPositionVo>();

			param.put("stockOutputRid", itemList.get(i).getStockOutputRid());
			positionList = outPutPositionDao.selectPostionByOrderId(param);

			Map<Integer, BizStockOutputPositionVo> positionMap = new HashMap<Integer, BizStockOutputPositionVo>();
			for (int j = 0; j < positionList.size(); j++) {
				BizStockOutputPositionVo position = positionList.get(j);

//				Map<String, Object> sn = new HashMap<String, Object>();
//				sn.put("sn_id", UtilNumber.getIntOrZeroForInteger(position.getSnId()));
//				sn.put("sn_code", UtilString.getStringOrEmptyForObject(position.getSnCode()));
//				sn.put("package_code", UtilString.getStringOrEmptyForObject(position.getPackageCode()));
//
//				if (positionMap.containsKey(position.getStockId())) {
//					positionMap.get(position.getStockId()).getSnList().add(sn);
//				} else {
//					List<Map<String, Object>> snList = new ArrayList<Map<String, Object>>();
//					snList.add(sn);
//
//					position.setSnList(snList);
//					positionMap.put(position.getStockId(), position);
//				}
			}

			List<BizStockOutputPositionVo> positionListReal = new ArrayList<BizStockOutputPositionVo>();
			Set<Integer> set = positionMap.keySet(); // 得到所有key的集合

			for (Integer key : set) {
				BizStockOutputPositionVo position = positionMap.get(key);
				positionListReal.add(position);
			}

			itemList.get(i).setPositionList(positionListReal);
		}
		List<ApproveUserVo> userList = commonService.getReceiptUser(head.getStockOutputId(), head.getReceiptType());
		List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(head.getStockOutputId(),
				head.getReceiptType());

		head.setUserList(userList);
		head.setItemList(itemList);
		head.setFileList(fileList);

		return head;
	}

	/**
	 * 获取出库新主键
	 */
	private long getNewOrderID() {
		return sequenceDao.selectNextVal("stock_output");
	}

	/**
	 * 保存出库单数据
	 */
	private int saveOrderInfo(boolean isAdd, BizStockOutputHead head) {
		if (isAdd) {
			return outPutHeadDao.insertSelective(head);
		} else {
			return outPutHeadDao.updateByPrimaryKeySelective(head);
		}
	}

	/**
	 * 保存出库主表对象
	 */
	private BizStockOutputHead saveHeadData(boolean isAdd, Map<String, Object> headData, BizStockOutputHead head,
			CurrentUser user, byte receiptType, boolean isPDA) {
		if (isAdd) {
			head = new BizStockOutputHead();
			head.setStockOutputCode(String.valueOf(this.getNewOrderID()));// 新出库单号
			head.setReceiptType(receiptType); // 出库单类型
			head.setStatus(EnumStockOutputStatus.STOCK_OUTPUT_UNFINISH.getValue());
			head.setIsDelete(EnumBoolean.FALSE.getValue());
			head.setCheckAccount(EnumBoolean.FALSE.getValue());
			head.setCreateUser(user.getUserId());
		}
		head.setCorpId(user.getCorpId()); // 公司ID
		head.setReferReceiptCode(UtilObject.getStringOrEmpty(headData.get("refer_receipt_code"))); // 前置单据ID
		head.setPreorderId(UtilObject.getStringOrEmpty(headData.get("preorder_id"))); // 前置关联号
		head.setReceiveCode(UtilObject.getStringOrEmpty(headData.get("receive_code"))); // 接收人编码
		head.setReceiveName(UtilObject.getStringOrEmpty(headData.get("receive_name"))); // 接收人名称
		head.setOrderType(UtilObject.getStringOrEmpty(headData.get("order_type"))); // 订单类型
		head.setOrderTypeName(UtilObject.getStringOrEmpty(headData.get("order_type_name"))); // 订单类型名称
		head.setOrgCode(UtilObject.getStringOrEmpty(headData.get("org_code")));// 销售组织
		head.setOrgName(UtilObject.getStringOrEmpty(headData.get("org_name")));// 销售组织名称
		head.setGroupCode(UtilObject.getStringOrEmpty(headData.get("group_code")));// 销售组
		head.setRemark(UtilObject.getStringOrEmpty(headData.get("remark")));// 备注

		head.setModifyUser(user.getUserId()); // 最后修改人

//		if (receiptType == EnumReceiptType.STOCK_OUTPUT_RESERVE.getValue()) {
//			head.setReserveCostObjCode(UtilObject.getStringOrEmpty(headData.get("reserve_cost_obj_code")));// 成本对象
//			head.setReserveCostObjName(UtilObject.getStringOrEmpty(headData.get("reserve_cost_obj_name")));// 成本对象描述
//			head.setReserveCreateUser(UtilObject.getStringOrEmpty(headData.get("reserve_create_user")));// 预留单:创建人
//			head.setReserveCreateTime(UtilObject.getStringOrEmpty(headData.get("reserve_create_time")));// 预留单:创建日期
//		}

		if (isPDA) {
			head.setRequestSource(EnumBoolean.TRUE.getValue());
		} else {
			head.setRequestSource(EnumBoolean.FALSE.getValue());
		}

		this.saveOrderInfo(isAdd, head);

		return head;
	}

	/**
	 * 保存出库单详细信息
	 */
	private BizStockOutputItem saveItemData(Map<String, Object> itemData, BizStockOutputHead head, String userId) {

		BizStockOutputItem item = new BizStockOutputItem();

		item.setStockOutputId(head.getStockOutputId());
		item.setStockOutputRid(UtilObject.getIntegerOrNull(itemData.get("stock_output_rid")));
		item.setFtyId(UtilObject.getIntegerOrNull(itemData.get("fty_id")));// 工厂
		item.setLocationId(UtilObject.getIntegerOrNull(itemData.get("location_id"))); // 库存地点
		item.setMatId(UtilObject.getIntegerOrNull(itemData.get("mat_id"))); // 物料编码
		item.setUnitId(UtilObject.getIntegerOrNull(itemData.get("unit_id"))); // 计量单位
		item.setDecimalPlace(UtilObject.getByteOrNull(itemData.get("decimal_place"))); // 浮点型
		item.setSendQty(UtilObject.getBigDecimalOrZero(itemData.get("send_qty"))); // 已发货数量
		item.setOutputQty(UtilObject.getBigDecimalOrZero(itemData.get("output_qty")));// 出库数量
		item.setReturnQty(new BigDecimal("0"));
		item.setStockQty(UtilObject.getBigDecimalOrZero(itemData.get("stock_qty"))); // 订单数量
		item.setMoveTypeId(UtilObject.getIntegerOrNull(itemData.get("move_type_id")));// 移动类型
		item.setReferReceiptCode(head.getReferReceiptCode());// 领料单号/参考单据编号(表头销售订单编号)
		item.setReferReceiptRid(UtilObject.getStringOrEmpty(itemData.get("refer_receipt_rid"))); // 领料单行项目号/参考单据序号(销售订单项目)
		item.setReferPrice(UtilObject.getBigDecimalOrZero(itemData.get("refer_price")));// 参考价

		item.setIsDelete(EnumBoolean.FALSE.getValue());
		item.setWriteOff(EnumBoolean.FALSE.getValue());

		item.setCreateUser(userId);
		item.setModifyUser(userId);

		outPutItemDao.insertSelective(item);

		return item;
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
	private void formatPositionDataToSave(Map<String, Object> postionData, int stockOutputId, String userId, int rid)
			throws Exception {

		if (postionData.containsKey("sn_list")) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> snList = (List<Map<String, Object>>) postionData.get("sn_list");

			for (Map<String, Object> sn : snList) {
				StockSnKey key = new StockSnKey();
				key.setSnId(UtilObject.getIntegerOrNull(sn.get("sn_id")));
				if (postionData.containsKey("stock_id")) {
					key.setStockId(UtilObject.getIntegerOrNull(postionData.get("stock_id")));
				}
				List<StockSn> stockSnList = stockSnDao.selectListByUniqueKey(key);

				if (stockSnList == null || stockSnList.size() == 0) {
					throw new RuntimeException("没有查询到相关库存");
				}
				if (stockSnList.size() > 1) {
					throw new RuntimeException("序列号库存异常");
				}

				if (stockSnList.size() == 1) {
					postionData.put("stock_id", stockSnList.get(0).getStockId());
					if (stockSnList.get(0).getQty()
							.compareTo(UtilObject.getBigDecimalOrZero(postionData.get("qty"))) >= 0) {
						this.savePositionData(postionData, stockOutputId, userId, rid, stockSnList.get(0).getId(),
								UtilObject.getIntOrZero(sn.get("sn_id")));
					} else {
						throw new RuntimeException("库存已被占用");
					}
				}
			}
		} else {
			StockSnKey key = new StockSnKey();
			key.setSnId(0);
			key.setStockId(UtilObject.getIntegerOrNull(postionData.get("stock_id")));
			List<StockSn> stockSnList = stockSnDao.selectListByUniqueKey(key);

			if (stockSnList == null || stockSnList.size() == 0) {
				throw new RuntimeException("没有查询到相关库存");
			}
			if (stockSnList.size() > 1) {
				throw new RuntimeException("序列号库存异常");
			}

			this.savePositionData(postionData, stockOutputId, userId, rid, stockSnList.get(0).getId(), 0);
		}

	}

	/**
	 * 保存出库单出库仓位详细信息
	 */
	private BizStockOutputPosition savePositionData(Map<String, Object> postionData, int stockOutputId, String userId,
			int rid, int stockSnId, int snId) throws Exception {

		StockPosition stockPosition = stockPositionDao
				.selectByPrimaryKey(UtilObject.getIntegerOrNull(postionData.get("stock_id")));
		BigDecimal qty = UtilObject.getBigDecimalOrZero(postionData.get("qty"));
		BigDecimal stockQty = new BigDecimal("0");
		if (stockPosition != null) {
			stockQty = stockPosition.getQty();
		}
		if (stockQty.compareTo(qty) >= 0) {
			BizStockOutputPosition outputPosition = new BizStockOutputPosition();

			outputPosition.setStockOutputId(stockOutputId);
			outputPosition.setStockOutputRid(rid);
			outputPosition
					.setStockOutputPositionId(UtilObject.getIntegerOrNull(postionData.get("stock_output_position_id")));
			outputPosition.setStockId(UtilObject.getIntegerOrNull(postionData.get("stock_id")));
			outputPosition.setStockSnId(stockSnId);
			outputPosition.setBatch(stockPosition.getBatch());
			outputPosition.setSnId(snId);
			outputPosition.setPalletId(stockPosition.getPalletId());
			outputPosition.setPackageId(stockPosition.getPackageId());
			outputPosition.setQty(qty);
			outputPosition.setSpecStock(stockPosition.getSpecStock());
			outputPosition.setSpecStockCode(stockPosition.getSpecStockCode());
			outputPosition.setSpecStockName(stockPosition.getSpecStockName());
			outputPosition.setWhId(stockPosition.getWhId());
			outputPosition.setAreaId(stockPosition.getAreaId());
			outputPosition.setPositionId(stockPosition.getPositionId());
			outputPosition.setStockQty(stockPosition.getQty());
			outputPosition.setIsDelete(EnumBoolean.FALSE.getValue());

			outputPosition.setCreateUser(userId);
			outputPosition.setModifyUser(userId);

			outPutPositionDao.insertSelective(outputPosition);
			return outputPosition;
		} else {
			throw new RuntimeException("库存已被占用");
		}
	}

	/**
	 * 保存出库表信息
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public JSONObject saveOutputOrderData(JSONObject jsonData, CurrentUser user, boolean isPDA, byte orderType)
			throws Exception {
		boolean isAdd = false; // 新增or修改
		List<Map<String, String>> ftyListParam = new ArrayList<Map<String, String>>(); // 工厂状态校验参数

		/******************************************************
		 * HEAD信息处理
		 ******************************************************/
		@SuppressWarnings("unchecked")
		Map<String, Object> headData = (Map<String, Object>) jsonData;

		BizStockOutputHead outputHead = null; // 领料出库单对象
		int stockOutputId = 0;

		if (headData.containsKey("stock_output_id")) {
			stockOutputId = UtilObject.getIntOrZero(headData.get("stock_output_id"));
		}
		// 根据出库单号是否存在 判断新增还是修改
		if (stockOutputId == 0) {
			isAdd = true;
		} else {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stockOutputId", stockOutputId);

			outputHead = outPutHeadDao.selectByPrimaryKey(param);
			if (null == outputHead || "".equals(outputHead.getStockOutputCode()) || outputHead.getIsDelete() == 1) {
				isAdd = true;
			}
		}

		// 保存或修改出库单并返回出库单对象
		outputHead = this.saveHeadData(isAdd, headData, outputHead, user, orderType, isPDA);

		// 如果当前操作为修改,则删除经办人与出库单详细信息
		if (!isAdd) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("modifyUser", user.getUserId());
			param.put("stockOutputId", outputHead.getStockOutputId());

			commonService.removeReceiptUser(outputHead.getStockOutputId(), outputHead.getReceiptType());
			bizReceiptAttachmentDao.deleteByReceiptId(outputHead.getStockOutputId());
			outPutItemDao.deleteItemByOrderId(param);
			outPutPositionDao.deletePostionByOrderId(param);
		}

		/******************************************************
		 * ITEM信息处理
		 ******************************************************/

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) headData.get("item_list"); // 出库单明细list
		for (int i = 0; i < itemList.size(); i++) {
			Map<String, Object> itemDataTemp = itemList.get(i);

			this.saveItemData(itemDataTemp, outputHead, user.getUserId());

			if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue()) {
				if (isAdd) {
					// 修改resb表 已出库数量
					Map<String, Object> keyMap = new HashMap<String, Object>();
					keyMap.put("addQty", UtilObject.getBigDecimalOrZero(itemDataTemp.get("output_qty")));
					keyMap.put("matReqId", outputHead.getReferReceiptCode());
					keyMap.put("matReqRid", UtilObject.getStringOrEmpty(itemDataTemp.get("refer_receipt_rid")));

					matReqItemDao.updateTakeDeliveryQty(keyMap);
				}
			} else if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue()) {
				this.updateDbOrderStatus(outputHead.getStockOutputId(),
						UtilObject.getIntegerOrNull(itemDataTemp.get("stock_output_rid")),
						outputHead.getReferReceiptCode(),
						UtilObject.getStringOrEmpty(itemDataTemp.get("refer_receipt_rid")), outputHead.getPreorderId(),
						"17");
			}

			/******************************************************
			 * position信息处理
			 ******************************************************/

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> positionList = (List<Map<String, Object>>) itemDataTemp.get("position_list"); // 出库单明细list

			if (positionList != null) {
				for (int j = 0; j < positionList.size(); j++) {
					Map<String, Object> positionDataTemp = positionList.get(j);

					if (UtilObject.getBigDecimalOrZero(positionDataTemp.get("qty")).compareTo(BigDecimal.ZERO) == 1) {

						this.formatPositionDataToSave(positionDataTemp, outputHead.getStockOutputId(), user.getUserId(),
								UtilObject.getIntegerOrNull(itemDataTemp.get("stock_output_rid")));

						Map<String, String> ftyParam = new HashMap<String, String>();
						ftyParam.put("location_id", UtilObject.getStringOrEmpty(itemDataTemp.get("location_id")));
						ftyParam.put("position_id", UtilObject.getStringOrEmpty(positionDataTemp.get("position_id")));
						ftyListParam.add(ftyParam);
					}
				}

			}
		}
		// 添加经办人信息
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> userList = (List<Map<String, Object>>) headData.get("user_list");
		if (userList != null) {
			for (int i = 0; i < userList.size(); i++) {
				Map<String, Object> userTemp = userList.get(i);
				commonService.addReceiptUser(outputHead.getStockOutputId(), outputHead.getReceiptType(),
						UtilObject.getStringOrEmpty(userTemp.get("user_id")),
						UtilObject.getIntegerOrNull(userTemp.get("role_id")));
			}
		}
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> fileList = (List<Map<String, Object>>) headData.get("file_list");

		if (fileList != null) {
			for (Object object : fileList) {
				JSONObject attachmentObj = (JSONObject) object;
				BizReceiptAttachment bizReceiptAttachment = new BizReceiptAttachment();
				bizReceiptAttachment.setReceiptId(outputHead.getStockOutputId());
				bizReceiptAttachment.setReceiptType(outputHead.getReceiptType());
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
		FreezeCheck checkFalg = commonService.checkWhPosAndStockLoc(ftyListParam);

		JSONObject result = new JSONObject();
		result.put("flag", true);
		result.put("stock_output_id", outputHead.getStockOutputId());

		if (!"".equals(checkFalg.getResultMsgOutput())) {
			result.put("flag", false);
			result.put("message", checkFalg.getResultMsgOutput());
		}

		return result;
	}

	/**
	 * 过账
	 * 
	 * @param result
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public Map<String, Object> saveOrderToSap(JSONObject result, String userId) throws Exception {
		if (!result.getBoolean("flag")) {
			throw new InterfaceCallException(UtilObject.getStringOrEmpty(result.get("message")));
		}

		int stockOutputId = result.getInt("stock_output_id");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead head = outPutHeadDao.selectByPrimaryKey(param);

		Map<String, Object> sapReturn = new HashMap<String, Object>();
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue()) {
			sapReturn = stockOutputImpl.postingForMatReqOrder(stockOutputId, userId);
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
			sapReturn = stockOutputImpl.postingForSalesOrder(stockOutputId, userId);
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PURCHASE_RETURN.getValue()) {
			sapReturn = stockOutputImpl.postingForPurchaseOrder(stockOutputId, userId);
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
			if (head.getCheckAccount() == 1) {
				sapReturn = stockOutputImpl.postingForHdzm(stockOutputId);
			} else {
				sapReturn = stockOutputImpl.postingForOtherOrder(stockOutputId, userId);
			}
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue()) {
			sapReturn = stockOutputImpl.postingForAllocateOrder(stockOutputId, userId);
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_RESERVE.getValue()) {
			sapReturn = stockOutputImpl.postingForReserveOrder(stockOutputId, userId);
		}

		return sapReturn;
	}

	/**
	 * 更新表单状态
	 */
	@Override
	@Transactional
	public String saveOrderToFinish(Map<String, Object> sapReturn, int stockOutputId, String userId) throws Exception {

		if ("S".equals(sapReturn.get("isSuccess"))) {

			boolean isAnla = false;

			if (sapReturn.containsKey("assetList")) {
				isAnla = true;
			}

			@SuppressWarnings("unchecked")
			JSONObject mDocData = this.saveMatDocData((List<Map<String, String>>) sapReturn.get("sapList"),
					stockOutputId, UtilObject.getStringOrEmpty(sapReturn.get("postDate")), "Post", isAnla);

			JSONObject matDocHead = mDocData.getJSONObject("mDocHead");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> mDocItemList = (List<Map<String, Object>>) mDocData.get("mDocItem");

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stockOutputId", stockOutputId);

			BizStockOutputHead head = outPutHeadDao.selectByPrimaryKey(param);

			// 保存资产卡片
			if (sapReturn.containsKey("assetList")) {

				@SuppressWarnings("unchecked")
				List<Map<String, String>> assetList = (List<Map<String, String>>) sapReturn.get("assetList");

				for (int i = 0; i < assetList.size(); i++) {
					Map<String, String> assetSap = assetList.get(i);

					BizAssetCard asset = new BizAssetCard();

					asset.setAssetCardCode(assetSap.get("assetCardCode"));// 资产卡片号
					asset.setAssetCardName(assetSap.get("assetCardName"));// 资产卡片描述
					asset.setStockOutputId(stockOutputId);// 订单编号
					asset.setStockOutputRid(Integer.parseInt(assetSap.get("stockOutputRid")));// 订单行号
					asset.setStockOutputPositionId(Integer.parseInt(assetSap.get("stockOutputPid")));// 序号
					if (matDocHead != null) {
						asset.setMatDocId(matDocHead.getInt("matDocId"));// 凭证号
					}

					asset.setMatDocYear(Integer.parseInt(assetSap.get("matDocYear")));// 年度
					asset.setMatDocRid(Integer.parseInt(assetSap.get("matDocRid")));// 行项目
					// asset.setMatReqId(Integer.parseInt(assetSap.get("matReqCode")));//
					// 领料单号
					// asset.setMatReqRid(Integer.parseInt(assetSap.get("matDocRid")));//
					// 领料行号
					asset.setIsSend(assetSap.get("isSend"));// 已发货标示

					assetCardDao.insertSelective(asset);
				}
			}

			// 过账成功
			this.updateOutPutStatus(stockOutputId, mDocItemList, userId);
			if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue()) {
				List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
				for (BizStockOutputItemVo item : itemList) {
					this.updateDbOrderStatus(head.getStockOutputId(), item.getStockOutputRid(),
							head.getReferReceiptCode(), item.getReferReceiptRid(), head.getPreorderId(), "20");
				}

			}
			commonService.initRoleWf(head.getStockOutputId(), UtilObject.getByteOrNull(head.getReceiptType()),
					head.getStockOutputCode(), head.getCreateUser());

			return UtilObject.getStringOrEmpty(sapReturn.get("message"));
		} else {
			throw new InterfaceCallException(UtilObject.getStringOrEmpty(sapReturn.get("message")));
		}
	}

	/**
	 * SAP成功后update出库表信息
	 */
	private void updateOutPutStatus(int stockOutputId, List<Map<String, Object>> mDocItemList, String userId) {

		int index = mDocItemList.size() - 1;
		Map<String, Object> temp = mDocItemList.get(index);
		int matDocId = UtilObject.getIntOrZero(temp.get("matDocId"));

		BizStockOutputHead head = new BizStockOutputHead();
		head.setStockOutputId(stockOutputId);
		head.setStatus(EnumStockOutputStatus.STOCK_OUTPUT_FINISH.getValue());
		head.setModifyUser(userId);
		outPutHeadDao.updateByPrimaryKeySelective(head);

		for (Map<String, Object> mDocItem : mDocItemList) {
			if (UtilObject.getIntOrZero(mDocItem.get("matDocId")) == matDocId) {
				// 物料凭证编号、物料凭证行序号过账后反写
				BizStockOutputItem item = new BizStockOutputItem();
				item.setStockOutputId(UtilObject.getIntOrZero(mDocItem.get("referReceiptId")));
				item.setStockOutputRid(UtilObject.getIntOrZero(mDocItem.get("referReceiptRid")));
				item.setMatDocId(matDocId);
				item.setMatDocRid(UtilObject.getIntOrZero(mDocItem.get("matDocRid")));
				item.setMatDocYear(UtilObject.getIntOrZero(mDocItem.get("matDocYear")));

				item.setModifyUser(userId);

				outPutItemDao.updateByPrimaryKeySelective(item);
			}
		}
	}

	/**
	 * 获取仓位信息
	 */
	@Override
	public List<Map<String, Object>> getStockData(Map<String, Object> param) {
		return outPutHeadDao.selectStockData(param);
	}

	/**
	 * 获取仓位信息(强制)
	 */
	@Override
	public List<Map<String, Object>> getStockDataCommend(Map<String, Object> param) {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> stockList = outPutHeadDao.selectStockData(param);

		BigDecimal outQty = UtilObject.getBigDecimalOrZero(param.get("outQty"));
		BigDecimal total = BigDecimal.ZERO;

		for (Map<String, Object> stock : stockList) {
			BigDecimal stockQty = new BigDecimal(0);

			stockQty = UtilObject.getBigDecimalOrZero(stock.get("stock_qty"));
			total = total.add(stockQty);

			if (total.compareTo(outQty) >= 0) {
				BigDecimal lastNumber = total.subtract(stockQty);
				lastNumber = outQty.subtract(lastNumber);
				stock.put("qty", lastNumber);
				result.add(stock);
				break;
			} else {
				stock.put("qty", stockQty);
				result.add(stock);
			}

		}
		return result;
	}

	/**
	 * 删除出库单
	 * 
	 * @param stockOutputId
	 * @param userId
	 * @return
	 */
	@Override
	@Transactional
	public String deleteOutputOrder(int stockOutputId, String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);

		// 如果是领料单删除 需要修改领料单的已出库数量
		if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue()) {
			for (BizStockOutputItemVo item : itemList) {
				Map<String, Object> keyMap = new HashMap<String, Object>();
				keyMap.put("addQty", "-" + item.getOutputQty());
				keyMap.put("matReqId", item.getReferReceiptCode());
				keyMap.put("matReqRid", item.getReferReceiptRid());

				matReqItemDao.updateTakeDeliveryQty(keyMap);
			}
		} else if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue()) {
			for (BizStockOutputItemVo item : itemList) {
				String status = "";
				if ("".equals(outputHead.getPreorderId()) || outputHead.getPreorderId() == null) {
					status = "10";
				} else {
					status = "15";
				}

				this.updateDbOrderStatus(outputHead.getStockOutputId(), item.getStockOutputRid(),
						item.getReferReceiptCode(), item.getReferReceiptRid(), outputHead.getPreorderId(), status);
			}
		}
		if (outputHead.getCheckAccount() == 1) {
			if (itemList.size() > 0) {
				for (BizStockOutputItemVo item : itemList) {
					SynMaterialDocItem synMDocItem = new SynMaterialDocItem();
					synMDocItem.setMatDocId(item.getMatDocId());
					synMDocItem.setMatDocRid(item.getMatDocRid());
					synMDocItem.setIsNew((byte) 1);
					synMDocItemDao.updateByPrimaryKeySelective(synMDocItem);
				}
			}

		}

		outputHead.setIsDelete((byte) 1);
		outputHead.setModifyUser(userId);

		param.put("modifyUser", userId);

		outPutHeadDao.updateByPrimaryKeySelective(outputHead);
		outPutItemDao.deleteItemByOrderId(param);
		outPutPositionDao.deletePostionByOrderId(param);
		commonService.removeReceiptUser(outputHead.getStockOutputId(), outputHead.getReceiptType());
		bizReceiptAttachmentDao.deleteByReceiptId(outputHead.getStockOutputId());

		return "删除成功";
	}

	/**
	 * 修改冲销状态
	 */
	private void udpateStatusWriteOff(int stockOutputId, List<Integer> ridList, String remark, String userId) {
		BizStockOutputItemVo item = new BizStockOutputItemVo();

		item.setStockOutputId(stockOutputId);

		for (int rid : ridList) {
			item.setStockOutputRid(rid);
			item.setRemark(remark);
			item.setWriteOff(EnumBoolean.TRUE.getValue());
			item.setModifyUser(userId);

			outPutItemDao.updateByPrimaryKeySelective(item);
		}
	}

	/**
	 * 冲销功能
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String writeOff(JSONObject json, CurrentUser user) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", json.getInt("stock_output_id"));

		BizStockOutputHeadVo head = outPutHeadDao.selectHeadByOrderId(param);

		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue()) {
			BizMatReqHead matReqHead = matReqHeadDao.selectByPrimaryKey(Integer.parseInt(head.getReferReceiptCode()));

			List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
			BizStockOutputItemVo item = itemList.get(0);

			String postDateString = outPutHeadDao.selectPostDateById(item.getMatDocId());
			String dateString = commonService.getPostDate(matReqHead.getMatReqFtyId());
			Map<String, String> dateMap = new HashMap<String, String>();
			dateMap.put("postDate", postDateString);
			dateMap.put("nowDate", dateString);

			int count = 0;
			if (postDateString.substring(0, 7).equals(dateString.substring(0, 7))) {
				count = outPutHeadDao.selectCountForPostDate(dateMap);
			} else {
				count = 1;
			}

			if (count > 0) {
				throw new InterfaceCallException("不允许跨月冲销，请走退库功能");
			}
		}
		List<Integer> ridList = (List<Integer>) json.get("stock_output_rid"); // 出库单明细list

		Map<String, Object> sapReturn = stockOutputImpl.writeOff(param, ridList, user);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		if ("S".equals(sapReturn.get("isSuccess"))) {
			this.saveMatDocData((List<Map<String, String>>) sapReturn.get("sapList"), outputHead.getStockOutputId(),
					UtilObject.getStringOrEmpty(sapReturn.get("postDate")), "WriteOff", false);

			if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue()) {
				for (int i = 0; i < ridList.size(); i++) {
					param.put("stockOutputRid", ridList.get(i));

					List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
					BizStockOutputItemVo item = itemList.get(0);

					// 修改resb表 已出库数量
					Map<String, Object> keyMap = new HashMap<String, Object>();
					keyMap.put("addQty", "-" + item.getOutputQty());
					keyMap.put("matReqId", item.getReferReceiptCode());
					keyMap.put("matReqRid", item.getReferReceiptRid());

					matReqItemDao.updateTakeDeliveryQty(keyMap);

				}

			} else if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue()) {
				for (int i = 0; i < ridList.size(); i++) {
					param.put("stockOutputRid", ridList.get(i));

					List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
					BizStockOutputItemVo item = itemList.get(0);
					String status = "";
					if ("".equals(outputHead.getPreorderId()) || outputHead.getPreorderId() == null) {
						status = "10";
					} else {
						status = "15";
					}

					this.updateDbOrderStatus(outputHead.getStockOutputId(), item.getStockOutputRid(),
							item.getReferReceiptCode(), item.getReferReceiptRid(), outputHead.getPreorderId(), status);
				}

			}

			this.udpateStatusWriteOff(outputHead.getStockOutputId(), ridList, json.getString("remark"),
					user.getUserId());

			commonService.initRoleWf(head.getStockOutputId(), UtilObject.getByteOrNull(head.getReceiptType()),
					head.getStockOutputCode(), head.getCreateUser());
			return UtilObject.getStringOrEmpty(sapReturn.get("message"));
		} else {
			throw new InterfaceCallException(UtilObject.getStringOrEmpty(sapReturn.get("message")));
		}

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

	/**
	 * 保存物料凭证抬头
	 * 
	 * @date 2017年9月30日
	 * @author 高海涛
	 * @param sapReturnData
	 * @return
	 */
	private BizMaterialDocHead saveMaterialDocHead(Map<String, String> sapData, BizStockOutputHead head,
			String pstng_date) throws Exception {
		BizMaterialDocHead mDocHead = new BizMaterialDocHead();

		mDocHead.setMatDocCode(sapData.get("matDocCode"));// 物料凭证号
		mDocHead.setMatDocType(head.getReceiptType()); // 业务类型

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		mDocHead.setDocTime(sdf.parse(pstng_date)); // 凭证日期
		mDocHead.setPostingDate(sdf.parse(pstng_date));

		mDocHead.setOrgCode(head.getOrgCode());
		mDocHead.setOrgName(head.getOrgName());
		mDocHead.setGroupCode(head.getGroupCode());

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
	private BizMaterialDocItem saveMaterialDocItem(Map<String, String> sapData, BizStockOutputPositionVo postion,
			String stockOutputCode, boolean isAnla) {
		// 保存凭证详细表数据
		BizMaterialDocItem mDocItem = new BizMaterialDocItem();
		mDocItem.setMatDocRid(Integer.parseInt(sapData.get("matDocRid"))); // 物料凭证行项目
		mDocItem.setMatDocYear(Integer.parseInt(sapData.get("matDocYear")));
		mDocItem.setBatch(postion.getBatch());// 批次
		mDocItem.setMoveTypeId(dictionaryService.getMoveTypeIdByMoveTypeCodeAndSpecStock(sapData.get("moveTypeCode"),
				sapData.get("specStock"))); // 移动类型
		mDocItem.setMoveTypeCode(sapData.get("moveTypeCode"));
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

		BigDecimal sapQty = UtilObject.getBigDecimalOrZero(sapData.get("qty"));
		BigDecimal wmsQty = postion.getQty();
		if (wmsQty.compareTo(sapQty) == 1 && sapQty.compareTo(BigDecimal.ONE) == 0) {
			mDocItem.setQty(sapQty);
		} else {
			mDocItem.setQty(wmsQty);
		}

		mDocItem.setUnitId(dictionaryService.getUnitIdByUnitCode(sapData.get("unitCode")));// 基本计量单位
		mDocItem.setReferReceiptId(postion.getStockOutputId());
		mDocItem.setReferReceiptCode(stockOutputCode); // 参考单据号
		mDocItem.setReferReceiptRid(postion.getStockOutputRid()); // 参考单行号

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
	 * SAP成功后更新仓位批次库存表
	 */
	private JSONObject saveMatDocData(List<Map<String, String>> sapList, int stockOutputId, String post_date,
			String orderType, boolean isAnla) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead head = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
		List<BizStockOutputPositionVo> positionList = outPutPositionDao.selectPostionByOrderId(param);

		// 保存各个行项目的批次信息
		Map<String, Long> batchMap = new HashMap<String, Long>();
		for (BizStockOutputPositionVo position : positionList) {
			batchMap.put(position.getStockOutputRid() + "-" + position.getStockOutputPositionId(), position.getBatch());
		}

		// 物料凭证List
		List<String> matCodeList = new ArrayList<String>();
		List<BizMaterialDocHead> mDocHeadList = new ArrayList<BizMaterialDocHead>();
		Map<String, List<BizMaterialDocItem>> mDocItemMap = new HashMap<String, List<BizMaterialDocItem>>();

		for (int i = 0; i < sapList.size(); i++) {

			Map<String, String> sapData = sapList.get(i);

			List<BizMaterialDocItem> mDocItemList = new ArrayList<BizMaterialDocItem>();

			for (int j = 0; j < positionList.size(); j++) {
				BizStockOutputPositionVo position = positionList.get(j);

				String stockOutputCode = sapData.get("stockOutputCode"); // 出库单号
				String stockOutputRid = sapData.get("stockOutputRid"); // 行号
				String stockOutputPid = sapData.get("stockOutputPid"); // 仓位行号

				// 如果物料凭证与出库单号 行号相同
//				if (position.getStockOutputCode().equals(stockOutputCode)
//						&& position.getStockOutputRid().equals(Integer.parseInt(stockOutputRid))) {
//					// 仓位表行号相同 或其他几种特殊类型
//					if (position.getStockOutputPositionId() == Integer.parseInt(stockOutputPid)
//							|| "".equals(stockOutputPid) || "00000".equals(stockOutputPid)
//							|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()
//							|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PURCHASE_RETURN.getValue()
//							|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
//
//						// H 出库的情况 获取批次 并保存 但是并不一定会使用
//						if ("H".equals(sapData.get("debitCredit"))) {
//							String key = position.getStockOutputRid() + "-" + position.getStockOutputPositionId();
//							Long newBatch = null;
//							if (sapData.containsKey("batch") && !"".equals(sapData.get("batch"))) {
//								newBatch = Long.parseLong(sapData.get("batch"));
//							} else {
//								newBatch = batchMap.get(key);
//							}
//
//							position.setBatch(newBatch);
//
//							// S 入库的情况如果zlien存在 则匹配到zlien 只生成一条新批次
//							// 合并的情况zlien =""或者00000 这种情况根据仓位明细数量生成新批次
//						} else if ("S".equals(sapData.get("debitCredit"))) {
//							if (!"WriteOff".equals(orderType)) {
//								Long newBatch = null;
//								if (sapData.containsKey("batch") && !"".equals(sapData.get("batch"))) {
//									newBatch = Long.parseLong(sapData.get("batch"));
//								} else {
//									newBatch = sequenceDao.selectNextVal("batch");
//								}
//
//								this.saveMach(sapData, newBatch);
//
//								BatchMaterialSpecKey specKey = new BatchMaterialSpecKey();
//								specKey.setMatId(dictionaryService.getMatIdByMatCode(sapData.get("matCode")));
//								specKey.setFtyId(dictionaryService.getFtyIdByFtyCode(sapData.get("ftyCode")));
//								specKey.setBatch(position.getBatch());
//
//								List<BatchMaterialSpec> batchMstSList = batchMstSDao.selectByUniqueKey(specKey);
//
//								for (BatchMaterialSpec batchMstS : batchMstSList) {
//
//									batchMstS.setFtyId(dictionaryService.getFtyIdByFtyCode(sapData.get("ftyCode")));
//									batchMstS.setBatch(newBatch);
//									batchMstS.setId(null);
//
//									batchMstSDao.insertSelective(batchMstS);
//								}
//
//								String key = position.getStockOutputRid() + "-" + position.getStockOutputPositionId();
//								batchMap.put(key, newBatch);
//								position.setBatch(newBatch);
//							}
//
//						}
//
//						// 销售出库与采购退货 只允许存在同一种库存类型的出库所以返回值为 :
//						// 1-N个凭证,每种凭证按照行项目合并后各个行项目只会有一条
//						// 领料出库返回值: 限制与非限制出库按照所传仓位明细行项目返回 ,
//						// 不同工厂间领料生成的645与101是按照行项目合并后数据 ZNON值为""
//
//						BizMaterialDocItem mDocItem = saveMaterialDocItem(sapData, position, head.getStockOutputCode(),
//								isAnla);
//						mDocItemList.add(mDocItem);
//					}
				}
			}

//			String matDocCode = sapData.get("matDocCode");
//			if (!matCodeList.contains(matDocCode)) {
//				matCodeList.add(matDocCode);
//				BizMaterialDocHead mDocHead = this.saveMaterialDocHead(sapData, head, post_date);
//				mDocHeadList.add(mDocHead);
//
//				mDocItemMap.put(matDocCode, mDocItemList);
//
//			} else {
//				List<BizMaterialDocItem> tempList = mDocItemMap.get(matDocCode);
//				tempList.addAll(mDocItemList);
//
//				mDocItemMap.put(matDocCode, tempList);
//			}

//		}
//		for (int i = 0; i < mDocHeadList.size(); i++) {
//			List<BizMaterialDocItem> mDocItemList = new ArrayList<BizMaterialDocItem>();
//			Map<String, Integer> key = new HashMap<String, Integer>();
//			int count = 0;
//			for (BizMaterialDocItem mDocItem : mDocItemMap.get(mDocHeadList.get(i).getMatDocCode())) {
//				String keyValue = mDocItem.getMatDocRid() + "-" + mDocItem.getBatch();
//				if (!key.containsKey(keyValue)) {
//					mDocItemList.add(mDocItem);
//					key.put(keyValue, count);
//					count++;
//				} else {
//					BigDecimal oldQty = mDocItemList.get(key.get(keyValue)).getQty();
//					mDocItemList.get(key.get(keyValue)).setQty(oldQty.add(mDocItem.getQty()));
//				}
//			}
//
//			// 根据物料凭证修改批次库存
//			commonService.modifyStockBatchByMaterialDoc(head.getCreateUser(), mDocHeadList.get(i), mDocItemList);
//		}

		// 根据转储单修改仓位库存
		this.savePositionStock(stockOutputId, itemList, orderType);

		JSONObject re = new JSONObject();

		re.put("mDocHead", mDocHeadList.get(mDocHeadList.size() - 1));
		re.put("mDocItem", mDocItemMap.get(mDocHeadList.get(mDocHeadList.size() - 1).getMatDocCode()));

		return re;
	}

	/**
	 * 保存仓位库存数量
	 * 
	 * @param positionList
	 * @param itemList
	 * @param orderType
	 * @throws Exception
	 */
	private void savePositionStock(Integer stockOutputId, List<BizStockOutputItemVo> itemList, String orderType)
			throws Exception {
		Map<String, Map<String, Object>> fty_location = new HashMap<String, Map<String, Object>>();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		List<BizStockOutputPositionVo> positionList = outPutPositionDao.selectPostionByOrderId(param);

		if (positionList != null) {
			for (BizStockOutputItemVo item : itemList) {
				for (BizStockOutputPositionVo position : positionList) {
					if (position.getStockOutputId().equals(item.getStockOutputId())
							&& position.getStockOutputRid().equals(item.getStockOutputRid())) {

						DicStockLocation stockL = stockLocationDao.selectByPrimaryKey(item.getLocationId());

						StockPosition stockPosition = new StockPosition();
						stockPosition.setId(position.getStockId());
						stockPosition.setWhId(stockL.getWhId());// 仓库号
						stockPosition.setFtyId(item.getFtyId());
						stockPosition.setLocationId(item.getLocationId());
						stockPosition.setMatId(item.getMatId());
						stockPosition.setAreaId(position.getAreaId());
						stockPosition.setPositionId(position.getPositionId());
						stockPosition.setBatch(position.getBatch());
						// E 现有订单 K 寄售（供应商） O 供应商分包库存 Q 项目库存
						stockPosition.setSpecStock(position.getSpecStock());// 特殊库存标识
						stockPosition.setSpecStockCode(position.getSpecStockCode());// 特殊库存代码
						stockPosition.setSpecStockName(position.getSpecStockName());// 特殊库存描述
						stockPosition.setInputDate(new Date()); // TO入库日期
						stockPosition.setUnitId(item.getUnitId()); // 基本计量单位
						stockPosition.setPalletId(position.getPalletId());
						stockPosition.setPackageId(position.getPackageId());

						if (orderType.equals("Post")) {
							stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
						} else if (orderType.equals("WriteOff")) {
							stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
						}

						stockPosition.setQty(position.getQty());

						List<StockSn> snlist = new ArrayList<StockSn>();
						StockSn sn = new StockSn();
						sn.setId(position.getStockSnId());
						sn.setMatId(stockPosition.getMatId());
						sn.setSnId(position.getSnId());
						sn.setQty(position.getQty());
						sn.setDebitOrCredit(stockPosition.getDebitOrCredit());
						snlist.add(sn);

						commonService.modifyStockPositionAndSn(stockPosition, snlist);

						if (orderType.equals("Post")) {
							// 默认仓位出库时 - 删除上架请求处理
							commonService.updateStockTaskReq(position.getBatch(), item.getMatId(), item.getFtyId(),
									item.getLocationId(), position.getQty(), position.getAreaId(),
									position.getPositionId());
						} else if (orderType.equals("WriteOff")) {
							// 默认仓位冲销时 - 上架请求处理
//							if (UtilProperties.getInstance().getDefaultCCQ().equals(position.getAreaCode())
//									&& UtilProperties.getInstance().getDefaultCW().equals(position.getPositionCode())) {
//								// -------生成作业请求单-----相同的库存地点生成一张作业单----------------
//								Map<String, Object> map = new HashMap<String, Object>();
//								map.put("batch", position.getBatch());
//								map.put("matId", item.getMatId());
//								map.put("ftyId", item.getFtyId());
//								map.put("locationId", item.getLocationId());
//
//								BizStockTaskReqItem taskReqItemOld = StockTaskReqItemDao.selectByUniqueKey(map);
//
//								String ftyLocation = item.getFtyId() + "-" + item.getLocationId();
//								if (null == taskReqItemOld && !fty_location.containsKey(ftyLocation)) {
//									// --------------ltbk------- 插入抬头
//									BizStockTaskReqHead taskReqHead = this.saveTaskReqHead(item);
//									Map<String, Object> taskReqHeadInfo = new HashMap<String, Object>();
//
//									int rid = 1;
//
//									taskReqHeadInfo.put("stockTaskReqId", taskReqHead.getStockTaskReqId());// 作业申请号
//									taskReqHeadInfo.put("whId", taskReqHead.getWhId());// 仓库号
//									taskReqHeadInfo.put("stockTaskReqRid", rid);// 作业申请序号
//									fty_location.put(ftyLocation, taskReqHeadInfo);
//
//									// -------------ltbl------------
//									this.saveTaskReqItem(item, position, taskReqItemOld, taskReqHeadInfo);
//
//								} else {
//									// 含有相同的库存地
//									Map<String, Object> taskReqHeadInfo = (Map<String, Object>) fty_location
//											.get(ftyLocation);
//									if (null == taskReqItemOld) {
//										int rid = (int) taskReqHeadInfo.get("stockTaskReqRid");
//										rid = rid + 1;
//										taskReqHeadInfo.put("stockTaskReqRid", rid);
//									}
//
//									this.saveTaskReqItem(item, position, taskReqItemOld, taskReqHeadInfo);
//								}
//								// -------------end---生成上架作业请求----------------
//							}
						}
					}
				}
			}
		}
	}

	private BizStockTaskReqHead saveTaskReqHead(BizStockOutputItemVo item) throws Exception {

		String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());

		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(item.getLocationId());

		BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
		taskReqHead.setStockTaskReqCode(stockTaskReqCode);
		taskReqHead.setWhId(locationObj.getWhId());
		taskReqHead.setFtyId(item.getFtyId());
		taskReqHead.setLocationId(item.getLocationId());
		taskReqHead.setMatDocId(item.getMatDocId());
		taskReqHead.setMatDocYear(item.getMatDocYear());
		taskReqHead.setCreateUser(item.getCreateUser());
		taskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());

		StockTaskReqHeadDao.insertSelective(taskReqHead);

		return taskReqHead;
	}

	private void saveTaskReqItem(BizStockOutputItemVo item, BizStockOutputPositionVo position,
			BizStockTaskReqItem taskReqItemOld, Map<String, Object> taskReqHeadInfo) {
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
			taskReqItem.setQty(taskReqItemOld.getQty().add(position.getQty()));

			StockTaskReqItemDao.updateByPrimaryKeySelective(taskReqItem);
		} else {

			taskReqItem.setStockTaskReqId((int) taskReqHeadInfo.get("stockTaskReqId"));
			taskReqItem.setWhId((int) taskReqHeadInfo.get("whId"));
			taskReqItem.setStockTaskReqRid((int) taskReqHeadInfo.get("stockTaskReqRid"));

			taskReqItem.setBatch(position.getBatch());

			taskReqItem.setFtyId(item.getFtyId());
			taskReqItem.setLocationId(item.getLocationId());
			taskReqItem.setMatId(item.getMatId());
			taskReqItem.setMatDocId(item.getMatDocId());
			taskReqItem.setMatDocRid(item.getMatDocRid());
			taskReqItem.setUnitId(item.getUnitId());
			taskReqItem.setQty(position.getQty());
			taskReqItem.setSpecStock(position.getSpecStock());
			taskReqItem.setSpecStockCode(position.getSpecStockCode());
			taskReqItem.setSpecStockName(position.getSpecStockName());

			StockTaskReqItemDao.insertSelective(taskReqItem);
		}
	}

	// MyTask 出库Service

	/**
	 * 获取领料单列表
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getMatReqList(Map<String, Object> param) {

		List<Map<String, Object>> matReqList = null;

		if ("1".equals(param.get("chooseType")) || "0".equals(param.get("chooseType"))) {
			matReqList = outPutHeadDao.selectMatReqListOnPaging(param);
		} else if ("2".equals(param.get("chooseType")) || "3".equals(param.get("chooseType"))) {
			matReqList = outPutHeadDao.selectMatReqOutPutListOnPaging(param);
		}

		return matReqList;
	}

	/**
	 * 根据领料单号获取领料单概览
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> getMatReqViewById(Map<String, Object> param) {
		Map<String, Object> head = outPutHeadDao.selectMatReqHeadById(param);

		if (null == head) {
			head = new HashMap<String, Object>();
		}
		List<Map<String, Object>> item = outPutHeadDao.selectMatReqItemById(param);
		List<Map<String, Object>> relevancy = outPutHeadDao.selectMatReqRelevancyByid(param);
		head.put("item_list", item);
		head.put("relevancy_list", relevancy);
		return head;
	}

	/**
	 * 根据出库单号获取领料出库单
	 * 
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getOrderViewByMatReq(Map<String, Object> param) throws Exception {

		BizStockOutputHeadVo outputHead = outPutHeadDao.selectHeadByOrderId(param);

		param.put("matReqId", outputHead.getReferReceiptCode());

		Map<String, Object> head = outPutHeadDao.selectMatReqHeadById(param);
		List<Map<String, Object>> item = outPutHeadDao.selectOutPutOrderItemForMatReq(param);
		for (int i = 0; i < item.size(); i++) {
			List<BizStockOutputPositionVo> positionList = new ArrayList<BizStockOutputPositionVo>();
			param.put("stockOutputRid", item.get(i).get("stock_output_rid"));
			positionList = outPutPositionDao.selectPostionByOrderId(param);

			Map<Integer, BizStockOutputPositionVo> positionMap = new HashMap<Integer, BizStockOutputPositionVo>();
			for (int j = 0; j < positionList.size(); j++) {
				BizStockOutputPositionVo position = positionList.get(j);

				Map<String, Object> sn = new HashMap<String, Object>();
				sn.put("sn_id", position.getSnId());
//				sn.put("sn_code", position.getSnCode());
//				sn.put("package_code", position.getPackageCode());
//
//				if (positionMap.containsKey(position.getStockId())) {
//					positionMap.get(position.getStockId()).getSnList().add(sn);
//				} else {
//					List<Map<String, Object>> snList = new ArrayList<Map<String, Object>>();
//					snList.add(sn);
//
//					position.setSnList(snList);
//					positionMap.put(position.getStockId(), position);
//				}
			}

			List<BizStockOutputPositionVo> positionListReal = new ArrayList<BizStockOutputPositionVo>();
			Set<Integer> set = positionMap.keySet(); // 得到所有key的集合

			for (Integer key : set) {
				BizStockOutputPositionVo position = positionMap.get(key);
				positionListReal.add(position);
			}

			item.get(i).put("position_list", positionListReal);

		}

		List<ApproveUserVo> userList = commonService.getReceiptUser(
				UtilObject.getIntOrZero(head.get("stock_output_id")),
				UtilObject.getIntOrZero(head.get("receipt_type")));
		List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(outputHead.getStockOutputId(),
				outputHead.getReceiptType());

		List<BizAssetCard> assetCardList = assetCartDao
				.selectAssetCardByOrderId(UtilObject.getIntOrZero(param.get("stockOutputId")));

		head.put("item_list", item);
		head.put("user_list", userList);
		head.put("file_list", fileList);
		head.put("asset_card_list", assetCardList);

		return head;

	}

	/**
	 * 从SAP获取销售订单(概览)
	 */
	@Override
	public List<Map<String, Object>> getSalesOrderList(String salesOrderCode, String userId) throws Exception {
		return stockOutputImpl.getSalesOrderList(salesOrderCode, userId);
	}

	/**
	 * 从SAP获取销售订单(明细)
	 */
	@Override
	public Map<String, Object> getSalesViewBySap(String salesOrderCode, CurrentUser user) throws Exception {
		return stockOutputImpl.getSalesViewBySap(salesOrderCode, user);
	}

	/**
	 * 从SAP获取采购订单(概览)
	 */
	@Override
	public List<Map<String, Object>> getPurchaseOrderList(Map<String, Object> param) throws Exception {
		return stockOutputImpl.getPurchaseOrderList(param);
	}

	/**
	 * 从SAP获取采购订单(明细)
	 */
	@Override
	public Map<String, Object> getPurchaseViewBySap(Map<String, Object> param) throws Exception {
		return stockOutputImpl.getPurchaseViewBySap(param);
	}

	/**
	 * 获取调拨单前置单据列表
	 */
	@Override
	public List<Map<String, Object>> getAllocateList(Map<String, Object> param) {
		return outPutHeadDao.selectInnerOrderForDBCK(param);
	}

	/**
	 * 查询调拨单明细
	 */
	@Override
	public Map<String, Object> getAllocateById(Map<String, Object> param) {

		String allocateDeliveryId = UtilObject.getStringOrEmpty(param.get("allocateDeliveryId"));

		Map<String, Object> mst = outPutHeadDao.selectInnerOrderMstById(param);

		List<Map<String, Object>> dtlList = new ArrayList<Map<String, Object>>();
		if ("".equals(allocateDeliveryId)) {
			dtlList = outPutHeadDao.selectInnerOrderDtlByAllot(param);
		} else {
			dtlList = outPutHeadDao.selectInnerOrderDtlByDelivery(param);
		}

		mst.put("item_list", dtlList);

		return mst;
	}

	/**
	 * 根据出库单号返回调拨信息
	 * 
	 * @date 2017年12月4日
	 * @author 高海涛
	 * @param zdjbh
	 * @return
	 */
	@Override
	public Map<String, Object> getDbckByStockOutputId(String stockOutputId) throws Exception {

		Map<String, Object> mst = outPutHeadDao.selectDbckMstByStockOutputId(stockOutputId);

		List<Map<String, Object>> dtlList = new ArrayList<Map<String, Object>>();
		if (mst != null) {
			dtlList = outPutHeadDao.selectDbckDtlByStockOutputId(stockOutputId);

			for (int i = 0; i < dtlList.size(); i++) {
				List<BizStockOutputPositionVo> positionList = new ArrayList<BizStockOutputPositionVo>();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("stockOutputId", stockOutputId);
				param.put("stockOutputRid", dtlList.get(i).get("stock_output_rid"));
				positionList = outPutPositionDao.selectPostionByOrderId(param);

				Map<Integer, BizStockOutputPositionVo> positionMap = new HashMap<Integer, BizStockOutputPositionVo>();
				for (int j = 0; j < positionList.size(); j++) {
					BizStockOutputPositionVo position = positionList.get(i);

					Map<String, Object> sn = new HashMap<String, Object>();
//					sn.put("sn_id", position.getSnId());
//					sn.put("sn_code", position.getSnCode());
//					sn.put("package_code", position.getPackageCode());
//
//					if (positionMap.containsKey(position.getStockId())) {
//						positionMap.get(position.getStockId()).getSnList().add(sn);
//					} else {
//						List<Map<String, Object>> snList = new ArrayList<Map<String, Object>>();
//						snList.add(sn);
//
//						position.setSnList(snList);
//						positionMap.put(position.getStockId(), position);
//					}
				}

				List<BizStockOutputPositionVo> positionListReal = new ArrayList<BizStockOutputPositionVo>();
				Set<Integer> set = positionMap.keySet(); // 得到所有key的集合

				for (Integer key : set) {
					BizStockOutputPositionVo position = positionMap.get(key);
					positionListReal.add(position);
				}

				dtlList.get(i).put("position_list", positionListReal);
			}
		}

		List<ApproveUserVo> userList = commonService.getReceiptUser(Integer.parseInt(stockOutputId),
				EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue());
		List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(Integer.parseInt(stockOutputId),
				EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue());

		mst.put("item_list", dtlList);
		mst.put("user_list", userList);
		mst.put("file_list", fileList);

		return mst;
	}

	/**
	 * 查询调拨出库列表
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrderListAllocate(Map<String, Object> param) {
		return outPutHeadDao.selectAllocateOrderListOnPaging(param);
	}

	/**
	 * 更新调拨 配送单状态
	 * 
	 * @date 2017年12月5日
	 * @author 高海涛
	 * @param zckdjh
	 * @param zckdjxh
	 * @param bstnk
	 */
	private void updateDbOrderStatus(int stockOutputId, int stockOutputRid, String refer_receipt_code,
			String refer_receipt_rid, String preorder_id, String status) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("allocateId", refer_receipt_code);
		paramMap.put("allocateRid", refer_receipt_rid);
		paramMap.put("modifyTime", new Date());
		paramMap.put("status", status);
		paramMap.put("stockOutputId", stockOutputId);
		paramMap.put("stockOutputRid", stockOutputRid);

		outPutHeadDao.updateAllotDtlById(paramMap);

		int count = outPutHeadDao.selectAllotCount(paramMap);

		if (count == 0) {
			outPutHeadDao.updateAllotMstById(paramMap);
		}

		if (!"".equals(preorder_id)) {
			int deliveryItemId = outPutHeadDao.selectDeliveryItemId(paramMap);
			paramMap.put("deliveryItemId", deliveryItemId);
			outPutHeadDao.updateDeliveryDtlById(paramMap);

			paramMap.put("allocateDeliveryId", preorder_id);
			int count2 = outPutHeadDao.selectDeliveryCount(paramMap);
			if (count2 == 0) {
				outPutHeadDao.updateDeliveryMstById(paramMap);
			}
		}

	}

	/**
	 * 获取预留单明细
	 */
	@Override
	public JSONObject getReserveViewBySap(String referReserveCode, CurrentUser cUser) throws Exception {
		// 调用sap查询预留单抬头和明细

		JSONObject body = new JSONObject();
		JSONObject returnObj = stockOutputReturnSap.getReserveOrderListFromSap(referReserveCode, cUser.getUserId());
		List<BizReserveOrderHead> reserveHeadList = stockOutputReturnSap.getReserveHeadListFromSapReturnObj(returnObj);
		BizReserveOrderHead head = reserveHeadList.get(0);
		body.put("refer_receipt_code", head.getReferReceiptCode());
		body.put("reserve_cost_obj_code", head.getReserveCostObjCode());
		body.put("reserve_cost_obj_name", head.getReserveCostObjName());
		body.put("reserve_create_time", head.getReserveCreateTime());
		body.put("reserve_create_user", head.getReserveCreateUser());
		List<BizReserveOrderItem> reserveItemList = stockOutputReturnSap.getReserveItemListFromSapReturnObj(returnObj);
		body.put("item_list", this.getReserveViewScreened(reserveItemList, cUser));
		return body;
	}

	// 筛选出预留单明细的工厂库存地点在创建人配置的工厂库存地点中
	private List<BizReserveOrderItem> getReserveViewScreened(List<BizReserveOrderItem> reserveItemList,
			CurrentUser cUser) {
		List<BizReserveOrderItem> list = new ArrayList<BizReserveOrderItem>();
		int i = 1;
		String isFocusBatch = "0"; // 批次信息是否强控
		for (BizReserveOrderItem bizReserveOrderItem : reserveItemList) {
			int ftyId = bizReserveOrderItem.getFtyId();
			int locationId = bizReserveOrderItem.getLocationId();
			String locatinstr = ftyId + "-" + locationId;
			List<String> locationList = cUser.getLocationList();
			if (locationList.contains(locatinstr)) {
				isFocusBatch = stockLocationDao.selectPlanByLocation(locationId);
				bizReserveOrderItem.setIsFocusBatch(isFocusBatch == null ? "0" : isFocusBatch);
				bizReserveOrderItem.setRid(i++);
				list.add(bizReserveOrderItem);
			}
		}
		return list;
	}

	/**
	 * 获取出库单抬头
	 */
	@Override
	public BizStockOutputHead getBizStockOutputHeadByStockOutPutId(int stockOutputId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);
		return outPutHeadDao.selectByPrimaryKey(param);
	}

}
