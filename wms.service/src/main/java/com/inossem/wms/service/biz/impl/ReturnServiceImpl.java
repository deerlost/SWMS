package com.inossem.wms.service.biz.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ws.rs.PUT;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.httpclient.util.DateUtil;
import org.flowable.engine.impl.transformer.StringToInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.config.BatchMaterialConfig;
import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.batch.BatchMaterialSpecMapper;
import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizFailMesMapper;
import com.inossem.wms.dao.biz.BizMatReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputPositionMapper;
import com.inossem.wms.dao.biz.BizStockOutputReturnHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputReturnItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputReturnPositionMapper;
import com.inossem.wms.dao.biz.BizStockTaskHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskItemCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskPositionCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqPositionMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.batch.BatchMaterialSpec;
import com.inossem.wms.model.biz.BizAssetCard;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizDeliveryOrderHead;
import com.inossem.wms.model.biz.BizDeliveryOrderItem;
import com.inossem.wms.model.biz.BizFailMes;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.biz.BizReserveOrderItem;
import com.inossem.wms.model.biz.BizSaleOrderHead;
import com.inossem.wms.model.biz.BizSaleOrderItem;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputPackageItem;
import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.biz.BizStockOutputItem;
import com.inossem.wms.model.biz.BizStockOutputPosition;
import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.biz.BizStockOutputReturnItem;
import com.inossem.wms.model.biz.BizStockOutputReturnPosition;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BizStockTaskReqPosition;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.dic.DicBatchSpec;
import com.inossem.wms.model.dic.DicClassType;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicStockLocation;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumBusinessType;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumRankLevel;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockOutputReturnStatus;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.key.BatchMaterialSpecKey;
import com.inossem.wms.model.sap.SapDeliveryOrderHead;
import com.inossem.wms.model.sap.SapDeliveryOrderItem;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.model.vo.BizStockOutputHeadVo;
import com.inossem.wms.model.vo.BizStockOutputItemVo;
import com.inossem.wms.model.vo.BizStockOutputPositionVo;
import com.inossem.wms.model.vo.BizStockOutputReturnHeadVo;
import com.inossem.wms.model.vo.BizStockOutputReturnItemVo;
import com.inossem.wms.model.vo.BizStockOutputReturnPositionVo;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.service.biz.IReturnService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.service.dic.IDicStockLocationService;
import com.inossem.wms.service.intfc.ICwErpWs;
import com.inossem.wms.service.intfc.ICwMesWs;
import com.inossem.wms.service.intfc.IStockOutputReturn;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilMethod;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmMvRecInteropeDto;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;
import com.inossem.wms.wsdl.mes.update.WmMvRecInteropeDto;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ISMSGHEAD;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ISSPLITBATCH;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ITLIPS;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSEREQ;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSEREQ.ITDATA;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSERESP;
import com.inossem.wms.wsdl.sap.outputc.MSGHEAD;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ITLIKP;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Transactional
@Service("returnService")
public class ReturnServiceImpl implements IReturnService {
	private static final Logger logger = LoggerFactory.getLogger(ReturnServiceImpl.class);
	@Autowired
	private DicFactoryMapper factoryDao;

	@Autowired
	private DicUnitMapper dicUnitDao;

	@Autowired
	private DicMaterialMapper dicMaterialDao;
	
	@Autowired
	private BizStockTaskPositionCwMapper positionCwDao;

	@Autowired
	private ICwErpWs cwErpWsImpl;

	@Autowired
	private IFileService fileService;
	@Autowired
	private BizStockOutputReturnHeadMapper returnHeadDao;

	@Autowired
	private BizStockOutputReturnItemMapper returnItemDao;

	@Autowired
	private BizStockOutputHeadMapper outputHeadDao;

	@Autowired
	private BizStockOutputItemMapper outputItemDao;

	@Resource
	private ICommonService commonService;

	@Resource
	private BizStockOutputPositionMapper outPutPositionDao;

	@Autowired
	private BatchMaterialSpecMapper batchMaterialSpecDao;

	@Resource
	private IUserService userService;

	@Resource
	private IStockOutputReturn stockOutputReturnSap;

	@Resource
	private BatchMaterialMapper batchMaterialDao;

	@Resource
	private SequenceDAO sequenceDAO;

	@Autowired
	private BizMatReqHeadMapper matReqHeadDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private DicFactoryMapper dicFactoryDao;

	@Autowired
	private IInputService rkglService;

	@Autowired
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;

	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;

	@Autowired
	private BizStockTaskReqPositionMapper StockTaskReqPositionDao;

	@Autowired
	private DicStockLocationMapper locationDao;

	@Autowired
	private DicWarehouseMapper warehouseDao;

	@Autowired
	private StockPositionMapper stockPositionDao;

	@Autowired
	private BizStockOutputPositionMapper outputPositionDao;

	@Autowired
	private BizStockOutputReturnPositionMapper returnPositionDao;

	@Resource
	private SequenceDAO sequenceDao;

	@Resource
	private DicClassTypeMapper classTypeDao;

	@Autowired
	private BizStockTaskItemCwMapper stockTaskItemCwDao;

	@Autowired
	private BizStockTaskPositionCwMapper stockTaskPositionCwDao;

	@Autowired
	private BizStockTaskHeadCwMapper stockTaskHeadCwDao;
	
	@Autowired
	private BizBusinessHistoryMapper historyDao;
	
	@Autowired
	private DicPackageTypeMapper packageTypeDao;
	
	@Autowired
	private ICwMesWs CwMesWsImpl;
	
	@Autowired
	private BizFailMesMapper failMesDao;
	
	@Resource
	private IDicStockLocationService dicStockLocationService;
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;

	@Override
	public List<BizStockOutputReturnHeadVo> getReturnList(BizStockOutputReturnHeadVo paramVo) {

		return returnHeadDao.selectReturnHeadListForCWOnPaging(paramVo);
	}

	@Override
	public List<Map<String, Object>> getOutputListByOrderCode(Map<String, Object> param) {

		return outputHeadDao.selectOutputListByOrderCode(param);
	}

	@Override
	public Map<String, Object> getOutputInfoByOutputId(Map<String, Object> param) {

		BizStockOutputHeadVo outputHead = outputHeadDao.selectHeadByOrderId(param);

		param.put("matReqId", outputHead.getReferReceiptCode());

		// 领料出库单抬头
		Map<String, Object> head = outputHeadDao.selectMatReqHeadById(param);

		// 领料出库单明细
		List<Map<String, Object>> itemList = outputHeadDao.selectOutPutInfoByOutPutId(param);
		int i = 1;
		for (Map<String, Object> map : itemList) {
			map.put("rid", i++);
		}

		// 添加批次特性
		if (itemList.size() > 0) {
			Integer ftyId = UtilObject.getIntegerOrNull(itemList.get(0).get("fty_id"));
			itemList = this.addBatchMaterialSpecForItemList(ftyId, itemList);
		}

		// 出库单经办人
		List<ApproveUserVo> list = commonService.getReceiptUser(UtilObject.getIntOrZero(head.get("stock_output_id")),
				UtilObject.getIntOrZero(head.get("receipt_type")));
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		for (ApproveUserVo approveUserVo : list) {
			Map<String, Object> innerMap = new HashMap<String, Object>();
			innerMap.put("user_id", approveUserVo.getUserId());
			innerMap.put("corp_name", approveUserVo.getCorpName());
			innerMap.put("org_name", approveUserVo.getOrgName());
			innerMap.put("role_id", approveUserVo.getRoleId());
			innerMap.put("role_name", approveUserVo.getRoleName());
			innerMap.put("user_name", approveUserVo.getUserName());
			innerMap.put("phone", approveUserVo.getPhone());
			userList.add(innerMap);
		}
		head.put("item_list", itemList);
		head.put("user_list", userList);

		return head;

	}

	@Override
	public List<Map<String, Object>> addBatchMaterialSpecForItemList(Integer ftyId,
			List<Map<String, Object>> itemList) {
		List<DicBatchSpec> batchSpecList = new ArrayList<DicBatchSpec>();
		List<DicBatchSpec> pctxList = new ArrayList<DicBatchSpec>();
		for (Map itemMap : itemList) {
			List<Map<String, Object>> batchList = new ArrayList<Map<String, Object>>();
			Integer matId = UtilObject.getIntegerOrNull(itemMap.get("mat_id"));
			String batchStr = UtilString.getStringOrEmptyForObject(itemMap.get("batch"));
			String[] batchArray = batchStr.split(",");
			for (int i = 0; i < batchArray.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Long batch = UtilObject.getLongOrNull(batchArray[i]);
				if (batch != null && ftyId != null && matId != null) {
					BatchMaterialSpecKey find = new BatchMaterialSpecKey();
					find.setMatId(matId);
					find.setFtyId(ftyId);
					find.setBatch(batch);
					batchSpecList = batchMaterialSpecDao.selectBatchSpecByUniqueKey(find);

					// 获取新批次列表
					BatchMaterial batchMaterial = new BatchMaterial();
					batchMaterial.setMatId(matId);
					batchMaterial.setFtyId(ftyId);
					batchMaterial.setBatch(batch);
					batchMaterial = batchMaterialDao.selectByUniqueKey(batchMaterial);
					String supplierName = "";
					String createTime = "";
					if (batchMaterial != null) {
						supplierName = batchMaterial.getSupplierName();
						createTime = UtilString.getShortStringForDate(batchMaterial.getProductionTime());
					}
					List<Map<String, Object>> innerList = new ArrayList<Map<String, Object>>();
					if (batchSpecList != null && batchSpecList.size() > 0) {
						for (DicBatchSpec dicBatchSpec : batchSpecList) {
							Map<String, Object> innerMap = new HashMap<String, Object>();
							innerMap.put("batch_spec_value",
									UtilString.getStringOrEmptyForObject(dicBatchSpec.getBatchSpecValue()));
							innerMap.put("batch_spec_name",
									UtilString.getStringOrEmptyForObject(dicBatchSpec.getBatchSpecName()));
							innerList.add(innerMap);
						}
					}
					map.put("batch", batch);
					map.put("supplier_name", supplierName);// 供应商
					map.put("create_time", createTime);// 创建时间
					map.put("batch_spec_list", innerList);
					batchList.add(map);
				}
			}
			itemMap.put("batch_list", batchList);
		}
		return itemList;
	}

	@Override
	public Map<String, Object> getMatreqReturnInfo(Map<String, Object> param) throws Exception {
		// 退库单抬头
		int returnId = UtilObject.getIntOrZero(param.get("returnId"));
		BizStockOutputReturnHead returnHead = returnHeadDao.selectByPrimaryKey(returnId);
		int stockOutputId = returnHead.getStockOutputId();
		String returnCode = returnHead.getReturnCode();
		String returnRemark = returnHead.getRemark();

		param.put("stockOutputId", stockOutputId);
		// BizStockOutputHeadVo outputHead =
		// outputHeadDao.selectHeadByOrderId(param);
		param.put("matReqId", returnHead.getReferReceiptCode());

		String returnCreateUser = "";
		try {
			returnCreateUser = userService.getUserById(returnHead.getCreateUser()).getUserName();
		} catch (Exception e) {
			logger.error("退库单详情 ", e);
		}

		// 领料出库单抬头
		Map<String, Object> head = outputHeadDao.selectMatReqHeadById(param);

		// 退库单明细
		List<Map<String, Object>> itemList = returnHeadDao.selectMatreqReturnInfoByReturnId(param);
		if (itemList != null && itemList.size() > 0) {
			// 添加批次特性
			Integer ftyId = UtilObject.getIntegerOrNull(itemList.get(0).get("fty_id"));
			itemList = this.addBatchMaterialSpecForItemList(ftyId, itemList);
		}
		int i = 1;
		for (Map<String, Object> map : itemList) {
			map.put("rid", i++);
		}

		// 出库单经办人
		// List<ApproveUserVo> list =
		// commonService.getReceiptUser(UtilObject.getIntOrZero(returnHead.getReturnId()),
		// UtilObject.getIntOrZero(returnHead.getReceiptType()));
		// List<Map<String, Object>> userList = new ArrayList<Map<String,
		// Object>>();
		// for (ApproveUserVo approveUserVo : list) {
		// Map<String, Object> innerMap = new HashMap<String, Object>();
		// innerMap.put("user_id", approveUserVo.getUserId());
		// innerMap.put("corp_name", approveUserVo.getCorpName());
		// innerMap.put("org_name", approveUserVo.getOrgName());
		// innerMap.put("role_id", approveUserVo.getRoleId());
		// innerMap.put("role_name", approveUserVo.getRoleName());
		// innerMap.put("user_name", approveUserVo.getUserName());
		// innerMap.put("phone", approveUserVo.getPhone());
		// userList.add(innerMap);
		// }
		head.put("item_list", itemList);
		// head.put("user_list", userList);
		head.put("user_list", commonService.getReceiptUser(returnId, EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue()));
		head.put("file_list",
				fileService.getReceiptAttachments(returnId, EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue()));

		// 补充退库单其他信息
		head.put("return_code", returnCode);// 退库单号
		head.put("return_remark", returnRemark);// 退库备注
		head.put("return_create_user", returnCreateUser);// 退库单创建人
		head.put("status", returnHead.getStatus());// 状态
		return head;
	}

	@Override
	public List<BizSaleOrderHead> getSaleOrderList(Map<String, Object> map) throws Exception {

		return stockOutputReturnSap.getSaleHeadListFromSAP(map);

	}

	@Override
	public Map<String, Object> getSaleOrderInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<BizSaleOrderHead> headList = stockOutputReturnSap.getSaleHeadListFromSAP(map);
		List<BizSaleOrderItem> itemList = stockOutputReturnSap.getSaleItemListFromSAP(map);
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		// 为销售单明细添加批次---start-----

		int i = 1;
		if (headList != null && itemList != null) {
			BizSaleOrderHead bizSaleOrderHead = headList.get(0);
			for (BizSaleOrderItem bizSaleOrderItem : itemList) {
				bizSaleOrderItem.setRid(i++);
				int ftyId = bizSaleOrderItem.getFtyId();
				int matId = bizSaleOrderItem.getMatId();

				Map<String, Object> batchInfo = commonService.getBatchSpecList(null, ftyId, matId);
				List<DicBatchSpec> batchSpecList = (List<DicBatchSpec>) batchInfo.get("batchSpecList");
				List<DicBatchSpec> batchMaterialSpecList = (List<DicBatchSpec>) batchInfo.get("batchMaterialSpecList");
				bizSaleOrderItem.setBatchMaterialSpecList(batchMaterialSpecList);
				bizSaleOrderItem.setBatchSpecList(batchSpecList);
				bizSaleOrderItem.setBatch("");
			}

			// 抬头
			returnMap.put("refer_receipt_code", bizSaleOrderHead.getReferReceiptCode());// 销售单号
			returnMap.put("preorder_id", bizSaleOrderHead.getPreorderId());// 合同编号
			returnMap.put("customer_name", bizSaleOrderHead.getCorpName());
			// 客户名称
			returnMap.put("order_type_name", bizSaleOrderHead.getOrderTypeName());// 销售订单类型
			returnMap.put("sale_org_name", bizSaleOrderHead.getSaleOrgName());// 销售组织
			returnMap.put("sale_group_name", UtilString.getStringOrEmptyForObject(bizSaleOrderHead.getSaleGroupName()));// 销售组

			// 添加一个空的经办人
			returnMap.put("user_list", userList);

			// 明细
			returnMap.put("item_list", itemList);
		}

		// 为销售单明细添加批次---end-----

		return returnMap;
	}

	@Override
	public Map<String, Object> getSaleOrReserveReturnInfo(Integer returnId) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BizStockOutputReturnHead head = returnHeadDao.selectByPrimaryKey(returnId);
		List<BizStockOutputReturnItemVo> itemList = returnItemDao.selectSaleOrReserveReturnItem(returnId);
		if (head != null && itemList.size() > 0) {
			byte receiptType = head.getReceiptType();
			returnMap.put("return_id", returnId);// 退库单id
			returnMap.put("return_code", head.getReturnCode());// 退库单号
			returnMap.put("refer_receipt_code", head.getReferReceiptCode());// 销售单号
																			// -
																			// 预留单号
			returnMap.put("remark", head.getRemark());// 备注
			if (receiptType == EnumReceiptType.STOCK_RETURN_SALE.getValue()) {
				// 销售退库抬头
				returnMap.put("preorder_id", head.getPreorderId());// 合同单号
				returnMap.put("customer_name", head.getCustomerName());// 客户名称
				returnMap.put("order_type_name", head.getOrderTypeName());// 销售订单类型
				returnMap.put("sale_org_name", head.getSaleOrgName());// 销售组织
				returnMap.put("sale_group_name", head.getSaleGroupName());// 销售组
				returnMap.put("status", head.getStatus());// 状态， 1 未完成 2 已完成
			} else {
				// 预留退库抬头
				returnMap.put("reserve_cost_obj_code", head.getReserveCostObjCode());// 成本对象
				returnMap.put("reserve_cost_obj_name", head.getReserveCostObjName());// 成本对象描述
				returnMap.put("reserve_create_user", head.getReserveCreateUser());// 预留单创建人
				returnMap.put("reserve_create_time", UtilString.getShortStringForDate(head.getCreateTime()));// 实际是退库单的
																												// 创建日期
				returnMap.put("status", head.getStatus());// 状态，1 未完成 2 已完成
			}

			// 退库单经办人
			List<ApproveUserVo> userList = commonService.getReceiptUser(UtilObject.getIntOrZero(head.getReturnId()),
					UtilObject.getIntOrZero(head.getReceiptType()));

			// 退库单明细
			int i = 1;
			for (BizStockOutputReturnItemVo itemVo : itemList) {
				Integer ftyId = itemVo.getFtyId();
				Integer matId = itemVo.getMatId();
				Map<String, Object> batchInfo = commonService.getBatchSpecList(null, ftyId, matId);
				List<DicBatchSpec> batchSpecList = (List<DicBatchSpec>) batchInfo.get("batchSpecList");
				List<DicBatchSpec> batchMaterialSpecList = (List<DicBatchSpec>) batchInfo.get("batchMaterialSpecList");
				itemVo.setBatchMaterialSpecList(batchMaterialSpecList);
				itemVo.setBatchSpecList(batchSpecList);

				itemVo.setRid(i++);
			}
			returnMap.put("item_list", itemList);
			returnMap.put("user_list", userList);

			returnMap.put("file_list", fileService.getReceiptAttachments(UtilObject.getIntOrZero(head.getReturnId()),
					UtilObject.getIntOrZero(head.getReceiptType())));
		}

		return returnMap;
	}

	@Override
	public List<BizReserveOrderHead> getReserveOrderList(String receiptCode, String createUser) throws Exception {
		JSONObject returnObj = stockOutputReturnSap.getReserveOrderListFromSap(receiptCode, createUser);
		return stockOutputReturnSap.getReserveHeadListFromSapReturnObj(returnObj);
	}

	@Override
	public Map<String, Object> getReserveOrderInfo(String receiptCode, String createUser) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		JSONObject returnObj = stockOutputReturnSap.getReserveOrderListFromSap(receiptCode, createUser);
		List<BizReserveOrderHead> headList = stockOutputReturnSap.getReserveHeadListFromSapReturnObj(returnObj);
		List<BizReserveOrderItem> itemList = stockOutputReturnSap.getReserveItemListFromSapReturnObj(returnObj);

		// 为预留单明细添加批次---start-----
		// 退库需要批次
		int i = 1;
		if (headList != null && itemList != null) {
			BizReserveOrderHead bizReserveOrderHead = headList.get(0);
			for (BizReserveOrderItem bizReserveOrderItem : itemList) {
				bizReserveOrderItem.setRid(i++);
				int ftyId = bizReserveOrderItem.getFtyId();
				int matId = bizReserveOrderItem.getMatId();

				Map<String, Object> batchInfo = commonService.getBatchSpecList(null, ftyId, matId);
				List<DicBatchSpec> batchSpecList = (List<DicBatchSpec>) batchInfo.get("batchSpecList");
				List<DicBatchSpec> batchMaterialSpecList = (List<DicBatchSpec>) batchInfo.get("batchMaterialSpecList");
				bizReserveOrderItem.setBatchMaterialSpecList(batchMaterialSpecList);
				bizReserveOrderItem.setBatchSpecList(batchSpecList);
				bizReserveOrderItem.setBatch("");
			}

			// 抬头
			returnMap.put("refer_receipt_code", bizReserveOrderHead.getReferReceiptCode());// 预留单号
			returnMap.put("reserve_cost_obj_code", bizReserveOrderHead.getReserveCostObjCode());// 成本对象
			returnMap.put("reserve_cost_obj_name", bizReserveOrderHead.getReserveCostObjName());
			// 成本对象描述
			returnMap.put("reserve_create_user", bizReserveOrderHead.getReserveCreateUser());// 创建人
			returnMap.put("reserve_create_time", bizReserveOrderHead.getReserveCreateTime());// 创建日期

			// 添加一个空的经办人
			List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
			returnMap.put("user_list", userList);

			// 明细
			returnMap.put("item_list", itemList);
		}

		// 为预留单明细添加批次---end-----

		return returnMap;

	}

	@Override
	public List<BatchMaterialSpec> getRecentBatchList(Map<String, Object> param) {

		return batchMaterialSpecDao.selectRecentBatchList(param);
	}

	@Override
	public JSONObject matreqSaveAndPost(JSONObject json) throws Exception {
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		// JSONObject returnbody = new JSONObject();

		boolean isSameBukrs = false;
		boolean saveBool = false;// 暂存是否成功
		boolean submitBool = false;// 过账是否成功
		boolean newReturnOrder;
		boolean submit;

		int autoReturnId = 0;
		String returnCode = "";
		String outputCode = "";

		int stockOutputId = json.getInt("stock_output_id");// 出库单号
		int returnId = json.getInt("return_id");// 退库单号

		String voucherDate = "";// 审批日期
		String postDate = "";// 过账日期

		JSONArray userList = json.getJSONArray("user_list");// 经办人
		JSONArray itemList = json.getJSONArray("item_list");// 物料明细
		String remark = json.getString("remark");// 备注
		String cUserId = UtilString.getStringOrEmptyForObject(json.get("cUserId"));
		String createName = UtilString.getStringOrEmptyForObject(json.get("createName"));
		JSONArray locationAry = json.getJSONArray("locationId");

		List<String> locationList = new ArrayList<String>();
		for (Object object : locationAry) {
			String str = UtilString.getStringOrEmptyForObject(object);
			locationList.add(str);
		}

		try {
			// 先判断暂存还是过账
			if (UtilObject.getIntOrZero(json.get("status")) == 10) {
				submit = true;// false暂存 true过账
			} else {
				submit = false;
			}

			// 判断是否新建退库单 创建人
			String createUser = "";
			String outputCodeStr = "";
			long returnCodeLong = 0;
			BizStockOutputReturnHead returnHead = new BizStockOutputReturnHead();
			if (returnId == 0) {
				newReturnOrder = true;
				returnCodeLong = sequenceDAO.selectNextVal("output_return");// 退库单号
				returnCode = String.valueOf(returnCodeLong);
				createUser = cUserId;
			} else {
				newReturnOrder = false;
				returnHead = returnHeadDao.selectByPrimaryKey(returnId);
				String returnCodeStr = returnHead.getReturnCode();
				returnCode = String.valueOf(returnCodeStr);
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("stockOutputId", stockOutputId);
				BizStockOutputHead outputHead = outputHeadDao.selectByPrimaryKey(param);
				outputCode = outputHead.getStockOutputCode();// 出库单号
				createUser = returnHead.getCreateUser();
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("stockOutputId", stockOutputId); // 出库单id
			paramMap.put("locationId", locationList);
			paramMap.put("createName", createName);

			Map<String, Object> outputHeadMap = outputHeadDao.selectMatReqHeadById(paramMap);// 出库单抬头
			int applyFtyId = UtilObject.getIntOrZero((outputHeadMap.get("apply_fty_id")));// 申请工厂id
			String applyFtyCode = UtilString.getStringOrEmptyForObject(outputHeadMap.get("apply_fty_code"));// 申请工厂code
			int matReqId = UtilObject.getIntOrZero((outputHeadMap.get("refer_receipt_code")));// 领料单号id
			String matReqCode = UtilString.getStringOrEmptyForObject(outputHeadMap.get("mat_req_code"));// 领料单号
			int receiveId = UtilObject.getIntOrZero((outputHeadMap.get("receive_id")));// 接收工厂id
			String receiveCode = UtilString.getStringOrEmptyForObject(outputHeadMap.get("receive_code"));// 接收工厂code
			voucherDate = commonService.getPostDate(receiveId);// 传sap
			postDate = voucherDate;

			// 组装退库单抬头
			BizStockOutputReturnHead newReturnHead = new BizStockOutputReturnHead();
			newReturnHead.setReturnId(returnId);
			newReturnHead.setReturnCode(returnCode);
			newReturnHead.setStockOutputId(stockOutputId);
			newReturnHead.setCreateUser(createUser);
			newReturnHead.setRemark(remark);
			newReturnHead.setReferReceiptCode(matReqId + "");
			newReturnHead.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_UNFINISH.getValue());
			newReturnHead.setFtyId(applyFtyId);
			newReturnHead.setReceiptType(EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue());
			newReturnHead.setReserveCreateTime(new Date());
			if (!newReturnOrder) {
				returnHeadDao.deleteByPrimaryKey(returnId);
				returnItemDao.deleteItemsByReturnId(returnId);
			}

			List<Map<String, Object>> outputItemList = outputHeadDao.selectOutPutInfoByOutPutId(paramMap);
			List<BizStockOutputReturnItem> newReturnItems = new ArrayList<BizStockOutputReturnItem>();
			int i = 1;
			for (Object object : itemList) {
				Map<String, Object> materMap = (Map<String, Object>) object;
				int rid = UtilObject.getIntOrZero(materMap.get("rid"));
				int stockOutputRid = UtilObject.getIntOrZero(materMap.get("stock_output_rid"));// 出库单行项目
				BigDecimal qty = new BigDecimal(UtilString.getStringOrEmptyForObject(materMap.get("return_qty")));// 退库数量
				long outputBatch = Long.valueOf(UtilString.getStringOrEmptyForObject(materMap.get("batch")))
						.longValue();// 批次号
				for (Map<String, Object> map : outputItemList) {
					int stockOutputRidInner = Integer
							.parseInt(UtilString.getStringOrEmptyForObject(map.get("stock_output_rid")));
					if (stockOutputRidInner == stockOutputRid) {
						int referReceiptRid = UtilObject.getIntOrZero(map.get("refer_receipt_rid"));// 对应领料单行项目
						int matId = UtilObject.getIntOrZero(map.get("mat_id"));// 物料编码
						int moveTypeId = UtilObject.getIntOrZero(map.get("move_type_id"));// 移动类型
						int unitId = UtilObject.getIntOrZero(map.get("unit_id"));// 计量单位
						int locationId = UtilObject.getIntOrZero(map.get("location_id"));// 库存地点
						String saleQty = UtilString.getStringOrEmptyForObject(map.get("sale_qty"));// 销售数量
						String sendQty = UtilString.getStringOrEmptyForObject(map.get("send_qty"));// 已创建数量
						String reserveId = UtilString.getStringOrEmptyForObject(map.get("reserve_id"));// 预留号
						String reserveRrid = UtilString.getStringOrEmptyForObject(map.get("reserve_rid"));// 预留行项目
						String purchaseOrderCode = UtilString.getStringOrEmptyForObject(map.get("purchase_order_code"));// 采购订单号
						String purchaseOrderRid = UtilString.getStringOrEmptyForObject(map.get("purchase_order_rid"));// 采购订单行号

						// 2017-12-15 退库单明细存领料单的领料工厂和库存地点
						// 传SAP的是领料单的接收工厂和领料单的库存地点
						// String werks_output_return =
						// StringUtil.objToString(map.get("zjsgc")); // 原是领料工厂
						// ——2017-12-11
						// 汪帮宇改成领料单的接收工厂

						BizStockOutputReturnItem newReturnItem = new BizStockOutputReturnItem();
						if (submit) {
							Long newBatch = commonService.getBatch();
							newReturnItem.setBatch(newBatch);
						} else {
							newReturnItem.setBatch(outputBatch);
						}
						newReturnItem.setBatchOutput(outputBatch);
						newReturnItem.setFtyId(applyFtyId);
						newReturnItem.setIsDelete((byte) 0);
						newReturnItem.setLocationId(locationId);
						newReturnItem.setMatId(matId);
						newReturnItem.setMoveTypeId(moveTypeId);
						newReturnItem.setPurchaseOrderCode(purchaseOrderCode);
						newReturnItem.setPurchaseOrderRid(purchaseOrderRid);
						newReturnItem.setQty(qty);
						newReturnItem.setReferReceiptCode(matReqId + "");
						newReturnItem.setReferReceiptRid(referReceiptRid + "");
						newReturnItem.setReserveId(reserveId);
						newReturnItem.setReturnRid(i++);
						newReturnItem.setSaleQty(new BigDecimal(saleQty));
						newReturnItem.setSendQty(new BigDecimal(sendQty));
						newReturnItem.setStockOutputId(stockOutputId);
						newReturnItem.setStockOutputRid(stockOutputRid);
						newReturnItem.setUnitId(unitId);
						newReturnItem.setWriteOff((byte) 0);
						newReturnItems.add(newReturnItem);
					}

				}

			}

			// 向数据库插入抬头和明细，返回return_id
			autoReturnId = this.insertReturnHeadAndItems(newReturnHead, newReturnItems);
			BizStockOutputReturnHead currentReturnHead = returnHeadDao.selectByPrimaryKey(autoReturnId);

			// 添加经办人
			if (!newReturnOrder) {
				commonService.removeReceiptUser(currentReturnHead.getReturnId(), currentReturnHead.getReceiptType());
			}
			for (Object object : userList) {
				JSONObject obj = (JSONObject) object;
				String userId = UtilString.getStringOrEmptyForObject(obj.getString("user_id"));
				int roleId = UtilObject.getIntOrZero(obj.getString("role_id"));
				int receiptType = currentReturnHead.getReceiptType();
				commonService.addReceiptUser(currentReturnHead.getReturnId(), receiptType, userId, roleId);
			}

			saveBool = true; // 保存成功
			if (submit) {
				BizStockOutputReturnHead head = null;
				List<BizStockOutputReturnItem> items = new ArrayList<BizStockOutputReturnItem>();
				Map<String, Object> postMap = new HashMap<String, Object>();
				head = returnHeadDao.selectByPrimaryKey(autoReturnId);
				items = returnItemDao.selectItemsByReturnId(autoReturnId);

				voucherDate = commonService.getPostDate(receiveId);// 审批日期
				postDate = commonService.getPostDate(receiveId);// 过账日期

				head.setVoucherDate(voucherDate);
				head.setPostDate(postDate);
				// head.setVoucherDate("");
				// head.setPostDate("");
				head.setCurrentUserId(cUserId);
				head.setWerkToSap(receiveCode);

				isSameBukrs = this.isSameBukrs(matReqId);// 判断领料工厂与接收工厂是否是同一公司
				// SAP接口过账
				if (isSameBukrs) {
					postMap = stockOutputReturnSap.submitToSapForMatreqForOneCorp(head, items);// sap过账，组装返回值
				} else {
					postMap = stockOutputReturnSap.submitToSapForMatreqForTwoCorp(head, items);// sap过账，组装返回值
				}
				// returnMap.put("materialDocHead", maDocHead);同公司

				// returnMap.put("materialDocHeadStart", maDocHeadStar);不同公司
				// returnMap.put("materialDocHeadEnd", maDocHeadEnd);
				// returnMap.put("materialItemsAll", mDocItems);

				errorCode = (Integer) postMap.get("errorCode");
				errorString = (String) postMap.get("errorString");
				if (errorCode == EnumErrorCode.ERROR_CODE_SUCESS.getValue()) {
					// sap过账成功修改相关表数据
					if (isSameBukrs) {
						BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
						this.updateReturnStockForOneCorp(mDocHead);
					} else {
						BizMaterialDocHead mDocHeadStart = (BizMaterialDocHead) postMap.get("materialDocHeadStart");
						BizMaterialDocHead mDocHeadEnd = (BizMaterialDocHead) postMap.get("materialDocHeadEnd");
						JSONArray eMsegAll = (JSONArray) postMap.get("eMsegAll");
						this.updateReturnStockForTwoCorp(mDocHeadStart, mDocHeadEnd, eMsegAll);
					}
					status = true;
					submitBool = true;
					commonService.initRoleWf(autoReturnId, head.getReceiptType(), head.getReturnCode(),
							head.getCreateUser());

				} else {
					// 把新建得批次号更回原批次
					for (BizStockOutputReturnItem bizStockOutputReturnItem : items) {
						bizStockOutputReturnItem.setBatch(bizStockOutputReturnItem.getBatchOutput());
						returnItemDao.updateByPrimaryKeySelective(bizStockOutputReturnItem);
					}
					body.put("return_id", autoReturnId);
					status = false;
					throw new InterfaceCallException(errorString);
				}

			}

		} catch (Exception e) {
			// logger.error("", e);
			body.put("return_id", autoReturnId);
			throw e;
		}

		JSONObject object = new JSONObject();
		object.put("errorCode", errorCode);
		object.put("errorString", errorString);
		object.put("body", body);
		return object;
	}

	private boolean isSameBukrs(int matReqId) throws Exception {
		BizMatReqHead matReqHead = matReqHeadDao.selectByPrimaryKey(matReqId);
		matReqHead.getReceiveFtyId();
		int matReqFtyId = matReqHead.getMatReqFtyId();// 领料工厂
		int receiveFtyId = matReqHead.getReceiveFtyId();// 接收工厂
		int matReqCorpId = dicFactoryDao.selectByPrimaryKey(matReqFtyId).getCorpId();// 领料公司
		int receiveCorpId = dicFactoryDao.selectByPrimaryKey(receiveFtyId).getCorpId();// 接收公司

		if (matReqCorpId == receiveCorpId) {
			return true;
		} else {
			return false;
		}
	}

	private Map<String, Object> updateReturnStockForOneCorp(BizMaterialDocHead mDocHead) throws Exception {
		// 批次库存和凭证
		commonService.modifyStockBatchByMaterialDoc(mDocHead.getCreateUser(), mDocHead,
				mDocHead.getMaterialDocItemList());
		// 上架请求
		rkglService.addStockTaskReqHeadAndItem(mDocHead);
		// 仓位库存
		rkglService.addStockPosition(mDocHead);
		// 批次信息
		if (mDocHead.getMatDocType() == EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue()) {
			this.addBatchMaterialInfoForMatreqReturn(mDocHead);
			this.updateOutputQty(mDocHead.getMaterialDocItemList());
		} else {
			// rkglService.addBatchMaterialInfo(mDocHead);
			this.addeBatchMaterialInfoForReturnOrder(mDocHead);
			this.updateSaleOrReserveQty(mDocHead.getMaterialDocItemList());
		}

		BizStockOutputReturnHead updateObj = new BizStockOutputReturnHead();
		Integer returnId = mDocHead.getMaterialDocItemList().get(0).getReferReceiptId();
		updateObj.setReturnId(returnId);
		updateObj.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_FINISH.getValue());
		returnHeadDao.updateByPrimaryKeySelective(updateObj);
		this.updateReturnItemMatDocInfo(mDocHead.getMaterialDocItemList());
		return null;
	}

	private Map<String, Object> updateReturnStockForTwoCorp(BizMaterialDocHead mDocHeadStart,
			BizMaterialDocHead mDocHeadEnd, JSONArray eMsegAll) throws Exception {
		// 批次库存和凭证
		commonService.modifyStockBatchByMaterialDoc(mDocHeadStart.getCreateUser(), mDocHeadStart,
				mDocHeadStart.getMaterialDocItemList());
		commonService.modifyStockBatchByMaterialDoc(mDocHeadEnd.getCreateUser(), mDocHeadEnd,
				mDocHeadEnd.getMaterialDocItemList());
		// 上架请求
		rkglService.addStockTaskReqHeadAndItem(mDocHeadStart);// 只有s
		this.updateStockTaskReqHeadAndItem(mDocHeadEnd, mDocHeadStart);// 有S 有H
																		// end
		// 仓位库存
		// rkglService.addStockPosition(eMsegAll);
		this.editStockPosition(eMsegAll);// 更新仓位库存，对应ZMKPFOTKServiceImpl 中的
											// editLqua方法
		// 批次信息
		if (mDocHeadStart.getMatDocType() == EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue()) {
			this.addBatchMaterialInfoForMatreqReturn(mDocHeadStart);
			this.updateOutputQty(mDocHeadStart.getMaterialDocItemList());
		}

		BizStockOutputReturnHead updateObj = new BizStockOutputReturnHead();
		Integer returnId = mDocHeadStart.getMaterialDocItemList().get(0).getReferReceiptId();
		updateObj.setReturnId(returnId);
		updateObj.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_FINISH.getValue());
		returnHeadDao.updateByPrimaryKeySelective(updateObj);
		this.updateReturnItemMatDocInfo(mDocHeadStart.getMaterialDocItemList());
		// this.updateReturnItemMatDocInfo(mDocHeadEnd.getMaterialDocItemList());
		return null;
	}

	private void editStockPosition(JSONArray eMsegAll) {
		for (Object object : eMsegAll) {
			JSONObject msegJSON = (JSONObject) object;
			String debitCredit = msegJSON.getString("SHKZG");// 借贷标识
			String returnCode = msegJSON.getString("ZWMSBH");// 退库单号
			int returnRid = msegJSON.getInt("ZWMSXM");// 退库单行项目

			String matCode = msegJSON.getString("MATNR");// 物料编码
			int matId = dictionaryService.getMatIdByMatCode(matCode);
			String ftyCode = msegJSON.getString("WERKS");// 工厂
			int ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
			String locationCode = msegJSON.getString("LGORT");// 库存地点
			int locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);

			BizStockOutputReturnHead returnHead = returnHeadDao.selectByReturnCode(returnCode);
			int returnId = 0;
			if (returnHead != null) {
				returnId = returnHead.getReturnId();
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("returnId", returnId);
			paramMap.put("returnRid", returnRid);
			BizStockOutputReturnItem returnItem = returnItemDao.selectItemByReturnIdAndReturnRid(paramMap);

			BigDecimal qty = returnItem.getQty();
			int unitId = returnItem.getUnitId();

			Map<String, String> cangkuhaoMap = new HashMap<String, String>();
			String locationIdString = locationId + "";
			if (!cangkuhaoMap.containsKey(locationIdString)) {
				DicStockLocation location = locationDao.selectByPrimaryKey(locationId);
				// T301T t301t = t301tDao.selectByPrimaryKey(key)
				cangkuhaoMap.put(locationIdString, location.getWhId() + "");
			}
			int whId = Integer.parseInt(cangkuhaoMap.get(locationIdString));
			DicWarehouse dicWarehouse = warehouseDao.selectByPrimaryKey(whId);
			String whCode = dicWarehouse.getWhCode();

			StockPosition findPosition = new StockPosition();
			String defaultAreaCode = UtilProperties.getInstance().getDefaultCCQ();
			String defaultPositionCode = UtilProperties.getInstance().getDefaultCW();
			findPosition.setWhId(whId);// 仓库
			findPosition.setAreaId(dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, defaultAreaCode));// 存储区
			findPosition.setFtyId(ftyId);
			findPosition.setPositionId(dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
					defaultAreaCode, defaultPositionCode));// 仓位
			findPosition.setPackageId(0);
			findPosition.setPalletId(0);
			findPosition.setMatId(matId);
			findPosition.setLocationId(locationId);
			findPosition.setBatch(returnItem.getBatch());

			findPosition.setDebitOrCredit(debitCredit);

			findPosition.setUnitId(unitId);
			findPosition.setQty(qty);
			findPosition.setInputDate(new Date());
			findPosition.setStatus((byte) 1);

			List<StockSn> snList = new ArrayList<StockSn>();
			StockSn sn = new StockSn();
			sn.setSnId(0);
			sn.setQty(qty);
			sn.setMatId(matId);
			sn.setDebitOrCredit(debitCredit);
			snList.add(sn);

			try {
				commonService.modifyStockPositionAndSn(findPosition, snList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void updateStockTaskReqHeadAndItem(BizMaterialDocHead mDocHeadEnd, BizMaterialDocHead mDocHeadStart)
			throws Exception {

		// 作业请求 库存地点 map
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();
		for (BizMaterialDocItem object : mDocHeadEnd.getMaterialDocItemList()) {
			int matDocId = mDocHeadEnd.getMatDocId();
			int ftyId = object.getFtyId();
			int locationIdReq = object.getLocationId();
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

					rkglService.addStockTaskReqItemFromMaterialDocItem(headInfo, object, item);

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
					reqhead.setCreateUser(mDocHeadEnd.getCreateUser());
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

					rkglService.addStockTaskReqItemFromMaterialDocItem(headInfo, object, item);

					addItemList.add(item);

				}
				StockTaskReqItemDao.insertReqItems(addItemList);

			} else {

				BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
				reqhead.setMatDocId(mDocHeadStart.getMatDocId());
				reqhead.setFtyId(ftyId);
				reqhead.setLocationId(locationIdReq);
				BizStockTaskReqHead reqhead1 = StockTaskReqHeadDao.selectByBizStockTaskReqHead(reqhead);
				if (reqhead1 != null) {
					int stockTaskReqId = reqhead1.getStockTaskReqId();
					List<BizStockTaskReqItemVo> reqItems = StockTaskReqItemDao.getReqItemsByTaskReqId(stockTaskReqId);

					for (BizStockTaskReqItemVo item : reqItems) {
						int itemMatId = item.getMatId();// 物料编码
						if (itemMatId == (object.getMatId())) {
							BizStockTaskReqItem innerItem = new BizStockTaskReqItem();
							innerItem.setStockTaskReqId(item.getStockTaskReqId());
							innerItem.setStockTaskReqRid(item.getStockTaskReqRid());
							StockTaskReqItemDao.deleteReqItemsByTaskReqIdAndRid(innerItem);// 删除对应上架作业单明细
							reqItems.remove(item);
						}
						if (reqItems.size() == 0) {
							StockTaskReqHeadDao.deleteByPrimaryKey(reqhead1.getStockTaskReqId());// 删除对应上架作业单抬头
						}
						break;
					}

				}

			}

		}

	}

	public void updateReturnItemMatDocInfo(List<BizMaterialDocItem> mDocItemList) throws Exception {

		for (BizMaterialDocItem mDocItem : mDocItemList) {
			BizStockOutputReturnItem updateItem = new BizStockOutputReturnItem();
			// updateItem.setItemId(mDocItem.getReferReceiptItemId());
			updateItem.setMatDocId(mDocItem.getMatDocId());
			updateItem.setMatDocRid(mDocItem.getMatDocRid());
			updateItem.setReturnId(mDocItem.getReferReceiptId());
			updateItem.setReturnRid(mDocItem.getReferReceiptRid());
			returnItemDao.updateForMatDocIdAndMatDocRid(updateItem);

		}

	}

	public void updateOutputQty(List<BizMaterialDocItem> mDocItemList) throws Exception {

		for (BizMaterialDocItem mDocItem : mDocItemList) {
			BizStockOutputReturnItem updateItem = new BizStockOutputReturnItem();
			updateItem.setReturnId(mDocItem.getReferReceiptId());
			updateItem.setReturnRid(mDocItem.getReferReceiptRid());
			updateItem.setMatDocId(mDocItem.getMatDocId());
			updateItem.setMatDocRid(mDocItem.getMatDocRid());
			returnItemDao.updateByPrimaryKeySelective(updateItem);

		}

		for (BizMaterialDocItem docItem : mDocItemList) {
			int returnId = docItem.getReferReceiptId();// 退库id
			int returnRid = docItem.getReferReceiptRid();// 退库rid
			String returnCode = docItem.getReferReceiptCode();// 退库code
			BigDecimal returnQty = docItem.getQty();// 退库数量
			if (returnQty == null) {
				returnQty = BigDecimal.ZERO;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("returnId", returnId);
			map.put("returnRid", returnRid);
			BizStockOutputReturnItem returnItem = returnItemDao.selectItemByReturnIdAndReturnRid(map);
			int stockOutputId = returnItem.getStockOutputId();
			int stockOutputRid = returnItem.getStockOutputRid();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stockOutputId", stockOutputId);
			param.put("stockOutputRid", stockOutputRid);
			BizStockOutputItem outputItem = outputItemDao.selectByPrimaryKey(param);
			BigDecimal haveReturnQty = outputItem.getReturnQty();// 退库数量

			BigDecimal nowReturnQty = haveReturnQty.add(returnQty);

			outputItem.setReturnQty(nowReturnQty);

			outputItemDao.updateByPrimaryKeySelective(outputItem);
		}

	}

	public void updateSaleOrReserveQty(List<BizMaterialDocItem> mDocItemList) throws Exception {

		for (BizMaterialDocItem mDocItem : mDocItemList) {
			BizStockOutputReturnItem updateItem = new BizStockOutputReturnItem();
			updateItem.setReturnId(mDocItem.getReferReceiptId());
			updateItem.setReturnRid(mDocItem.getReferReceiptRid());
			updateItem.setMatDocId(mDocItem.getMatDocId());
			updateItem.setMatDocRid(mDocItem.getMatDocRid());
			returnItemDao.updateByPrimaryKeySelective(updateItem);

		}

		// 更改前置单据数量
		for (BizMaterialDocItem docItem : mDocItemList) {
			int returnId = docItem.getReferReceiptId();// 退库id
			int returnRid = docItem.getReferReceiptRid();// 退库rid
			String returnCode = docItem.getReferReceiptCode();// 退库code
			BigDecimal returnQty = docItem.getQty();// 退库数量
			if (returnQty == null) {
				returnQty = BigDecimal.ZERO;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("returnId", returnId);
			map.put("returnRid", returnRid);
			BizStockOutputReturnItem returnItem = returnItemDao.selectItemByReturnIdAndReturnRid(map);
			returnItem.setReturnId(returnId);
			returnItem.setReturnRid(returnRid);
			returnItem.setSendQty(returnItem.getQty().add(returnItem.getSendQty()));
			returnItemDao.updateByPrimaryKeySelective(returnItem);
		}

	}

	private void addBatchMaterialInfoForMatreqReturn(BizMaterialDocHead materialDocHead) throws Exception {
		List<BizMaterialDocItem> docItemList = materialDocHead.getMaterialDocItemList();
		for (BizMaterialDocItem item : docItemList) {
			int ftyId = item.getFtyId();
			int matId = item.getMatId();
			long batch = item.getBatch();
			Map<String, Object> innerMap = new HashMap<String, Object>();
			innerMap.put("returnId", item.getReferReceiptId());
			innerMap.put("returnRid", item.getReferReceiptRid());
			BizStockOutputReturnItem returnItem = returnItemDao.selectItemByReturnIdAndReturnRid(innerMap);

			// 批次信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("matId", ftyId);// 物料
			map.put("ftyId", matId);// 工厂
			map.put("batch", returnItem.getBatchOutput());// 原批次
			map.put("newBatch", batch);// 新批次
			map.put("createUser", materialDocHead.getCreateUser());// 新批次创建人
			batchMaterialDao.insertBatchMaterialForNewBatch(map);
			// 批次特性
			batchMaterialSpecDao.insertBatchMaterialSpecForNewBatch(map);

		}
	}

	@Override
	public JSONObject salePost(JSONObject json) throws Exception {
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();

		String saleOrderCode = json.getString("refer_receipt_code");// 销售单号
		int returnId = json.getInt("return_id");// 退库单号
		JSONArray userList = json.getJSONArray("user_list");// 经办人
		JSONArray itemList = json.getJSONArray("item_list");// 物料明细
		String remark = json.getString("remark");// 备注
		String cUserId = UtilString.getStringOrEmptyForObject(json.get("cUserId"));// 创建人

		String voucherDate = "";// 审批日期
		String postDate = "";// 过账日期

		boolean newReturnOrder;
		boolean submit;// false暂存 true过账 默认过账
		boolean saveBool = false;// 暂存是否成功
		boolean submitBool = false;// 过账是否成功

		// 判断是否新建退库单 创建人
		int autoReturnId = 0;
		String returnCode = "";
		String createUser = "";
		String outputCodeStr = "";
		long returnCodeLong = 0;
		BizStockOutputReturnHead returnHead = new BizStockOutputReturnHead();

		try {
			// 先判断暂存还是过账
			if (UtilObject.getIntOrZero(json.get("status")) == 10) {
				submit = true;// false暂存 true过账
			} else {
				submit = false;
			}

			if (returnId == 0) {
				newReturnOrder = true;
				returnCodeLong = sequenceDAO.selectNextVal("output_return");// 退库单号
				returnCode = String.valueOf(returnCodeLong);
				createUser = cUserId;
			} else {
				newReturnOrder = false;
				returnHead = returnHeadDao.selectByPrimaryKey(returnId);
				String returnCodeStr = returnHead.getReturnCode();
				returnCode = String.valueOf(returnCodeStr);
				createUser = returnHead.getCreateUser();
			}

			// 调用sap查询销售订单抬头和明细
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("referReceiptCode", saleOrderCode);
			param.put("preorderId", "");
			List<BizSaleOrderHead> saleHeadList = stockOutputReturnSap.getSaleHeadListFromSAP(param);
			List<BizSaleOrderItem> saleItemList = stockOutputReturnSap.getSaleItemListFromSAP(param);

			if (saleHeadList.size() > 0 && saleItemList.size() > 0) {
				BizSaleOrderHead bizSaleOrderHead = saleHeadList.get(0);
				BizStockOutputReturnHead newReturnHead = new BizStockOutputReturnHead();
				newReturnHead.setReturnId(returnId);
				newReturnHead.setReturnCode(returnCode);
				newReturnHead.setStockOutputId(0);
				newReturnHead.setCreateUser(createUser);
				newReturnHead.setRemark(remark);
				newReturnHead.setReferReceiptCode(saleOrderCode);
				newReturnHead.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_UNFINISH.getValue());
				newReturnHead.setFtyId(saleItemList.get(0).getFtyId());
				newReturnHead.setReceiptType(EnumReceiptType.STOCK_RETURN_SALE.getValue());
				newReturnHead.setReserveCreateTime(new Date());

				newReturnHead.setOrderType(bizSaleOrderHead.getOrderType());
				newReturnHead.setOrderTypeName(bizSaleOrderHead.getOrderTypeName());// 订单类型描述
				newReturnHead.setCustomerCode(bizSaleOrderHead.getSupplierCode());
				newReturnHead.setCustomerName(bizSaleOrderHead.getCorpName());// 客户描述
				newReturnHead.setSaleOrg(bizSaleOrderHead.getSaleOrgCode());
				newReturnHead.setSaleOrgName(bizSaleOrderHead.getSaleOrgName());// 销售组织描述
				newReturnHead.setSaleGroup(bizSaleOrderHead.getSaleGroup());
				newReturnHead.setSaleGroupName(bizSaleOrderHead.getSaleGroupName());// 销售组描述
				newReturnHead.setPreorderId(bizSaleOrderHead.getPreorderId());// 合同号

				if (!newReturnOrder) {
					returnHeadDao.deleteByPrimaryKey(returnId);
					returnItemDao.deleteItemsByReturnId(returnId);
				}

				int i = 1;
				List<BizStockOutputReturnItem> newReturnItems = new ArrayList<BizStockOutputReturnItem>();
				for (Object object : itemList) {
					BizStockOutputReturnItem newReturnItem = new BizStockOutputReturnItem();
					Map<String, Object> itemMap = (Map<String, Object>) object;
					String saleRid = itemMap.get("sale_rid").toString();// 销售单行项目
					BigDecimal qty = new BigDecimal(itemMap.get("qty").toString());// 退库数量

					String batchString = itemMap.get("batch").toString();
					Long referBatch;
					if (batchString.length() == 0) {
						referBatch = Long.valueOf("0");
					} else {
						referBatch = Long.valueOf(itemMap.get("batch").toString());// 参考批次号
																					// 最近的批次号
																					// 或
																					// ""
					}

					// BizSaleOrderItem bizSaleOrderItem = saleItemList.stream()
					// .filter(z -> z.getSaleRid().equals(saleRid)
					// &&
					// saleOrderCode.equals(bizSaleOrderHead.getReferReceiptCode()))
					// .findFirst().get();
					for (BizSaleOrderItem bizSaleOrderItem : saleItemList) {
						String sapSaleRid = bizSaleOrderItem.getSaleRid();
						String sapSaleOrderCode = bizSaleOrderItem.getSaleOrderCode();
						if (sapSaleRid.equals(saleRid)) {
							// newReturnItem.setBatchSpecList(batchSpecList);
							// newReturnItem.setBatchMaterialSpecList(batchMaterialSpecList);
							Long newBatch;

							if (submit) {
								newBatch = commonService.getBatch();
								newReturnItem.setBatch(newBatch);
							} else {
								newReturnItem.setBatch(referBatch);
							}
							// newReturnItem.setBatch(newBatch);
							newReturnItem.setBatchOutput(referBatch);// 参考批次号
							newReturnItem.setFtyId(bizSaleOrderItem.getFtyId());
							newReturnItem.setIsDelete((byte) 0);
							newReturnItem.setLocationId(bizSaleOrderItem.getLocationId());
							newReturnItem.setMatId(bizSaleOrderItem.getMatId());
							newReturnItem.setMoveTypeId(0);
							newReturnItem.setQty(qty);
							newReturnItem.setReferReceiptCode(bizSaleOrderHead.getReferReceiptCode());
							newReturnItem.setReferReceiptRid(saleRid + "");

							newReturnItem.setReturnRid(i++);
							newReturnItem.setSaleQty(new BigDecimal(bizSaleOrderItem.getOrderQty()));// 订单数量
							newReturnItem.setSendQty(new BigDecimal(bizSaleOrderItem.getHaveReturnQty()));// 已退库数量,先暂存，退库成功后更新已退库数量
							newReturnItem.setUnitId(bizSaleOrderItem.getUnitId());
							newReturnItem.setWriteOff((byte) 0);

							// 为其他字段设置默认值
							newReturnItem.setStockOutputId(0);
							newReturnItem.setStockOutputRid(0);
							newReturnItem.setPurchaseOrderCode("");
							newReturnItem.setPurchaseOrderRid("");
							newReturnItem.setReserveId("");
							newReturnItem.setReserveRid("");
							newReturnItems.add(newReturnItem);
						}
					}

				}

				autoReturnId = this.insertReturnHeadAndItems(newReturnHead, newReturnItems);
				BizStockOutputReturnHead currentReturnHead = returnHeadDao.selectByPrimaryKey(autoReturnId);

				// 添加经办人
				if (!newReturnOrder) {
					commonService.removeReceiptUser(currentReturnHead.getReturnId(),
							currentReturnHead.getReceiptType());
				}
				for (Object object : userList) {
					JSONObject obj = (JSONObject) object;
					String userId = UtilString.getStringOrEmptyForObject(obj.getString("user_id"));
					int roleId = UtilObject.getIntOrZero(obj.getString("role_id"));
					int receiptType = currentReturnHead.getReceiptType();
					commonService.addReceiptUser(currentReturnHead.getReturnId(), receiptType, userId, roleId);
				}

				saveBool = true; // 保存成功

				if (submit) {
					// sap接口过账
					BizStockOutputReturnHead head = null;
					List<BizStockOutputReturnItem> items = new ArrayList<BizStockOutputReturnItem>();
					Map<String, Object> postMap = new HashMap<String, Object>();
					head = returnHeadDao.selectByPrimaryKey(autoReturnId);
					items = returnItemDao.selectItemsByReturnId(autoReturnId);
					for (BizStockOutputReturnItem bizStockOutputReturnItem : items) {
						for (int j = 0; j < itemList.size(); j++) {
							JSONObject object = itemList.getJSONObject(j);
							String saleRid = object.getString("sale_rid");// 销售单行项目

							JSONObject humpObject = new JSONObject();
							humpObject.put("batch_spec_list", object.getJSONArray("batch_spec_list"));
							humpObject.put("batch_material_spec_list", object.getJSONArray("batch_material_spec_list"));
							UtilJSONConvert.convertToHump(humpObject);
							List<DicBatchSpec> batchSpecList = JSONArray.toList(
									humpObject.getJSONArray("batchSpecList"), new DicBatchSpec(), new JsonConfig());
							List<DicBatchSpec> batchMaterialSpecList = JSONArray.toList(
									humpObject.getJSONArray("batchMaterialSpecList"), new DicBatchSpec(),
									new JsonConfig());

							// 判断是获取最近的批次，还是 新建的批次
							String isRecentBatchStr = object.getString("batch");
							boolean isRecentBatch;
							if (isRecentBatchStr.length() > 0) {
								isRecentBatch = true;
							} else {
								isRecentBatch = false;
							}

							for (BizStockOutputReturnItem returnItem : items) {
								if (returnItem.getReferReceiptRid().equals(saleRid)) {
									returnItem.setBatchMaterialSpecList(batchMaterialSpecList);
									returnItem.setBatchSpecList(batchSpecList);
									returnItem.setRecentBatch(isRecentBatch);
								}
							}

						}
					}

					String receiveFtyCode = dictionaryService.getFtyCodeByFtyId(head.getFtyId());
					voucherDate = commonService.getPostDate(head.getFtyId());// 审批日期
					postDate = commonService.getPostDate(head.getFtyId());// 过账日期
					head.setVoucherDate(voucherDate);
					head.setPostDate(postDate);
					head.setCurrentUserId(cUserId);
					head.setWerkToSap(receiveFtyCode);

					postMap = stockOutputReturnSap.submitToSapForSale(head, items);// sap过账，组装返回值
					errorCode = (Integer) postMap.get("errorCode");
					errorString = (String) postMap.get("errorString");
					if (errorCode == EnumErrorCode.ERROR_CODE_SUCESS.getValue()) {
						BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
						this.updateReturnStockForOneCorp(mDocHead);// sap过账成功修改相关标数据
						commonService.initRoleWf(autoReturnId, head.getReceiptType(), head.getReturnCode(),
								head.getCreateUser());
						status = true;
						submitBool = true;
					} else {
						for (BizStockOutputReturnItem bizStockOutputReturnItem : items) {
							bizStockOutputReturnItem.setBatch(bizStockOutputReturnItem.getBatchOutput());
							returnItemDao.updateByPrimaryKeySelective(bizStockOutputReturnItem);
						}
						status = false;
						body.put("return_id", autoReturnId);
						throw new InterfaceCallException(errorString);
					}
				}

			}
		} catch (Exception e) {
			logger.error("", e);
			body.put("return_id", autoReturnId);
		}

		JSONObject object = new JSONObject();
		object.put("errorCode", errorCode);
		object.put("errorString", errorString);
		object.put("body", body);
		return object;
	}

	@Override
	public JSONObject reservePost(JSONObject json) throws Exception {
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();

		String reserveCode = json.getString("refer_receipt_code");// 预留单号
		int returnId = json.getInt("return_id");// 退库单号
		JSONArray userList = json.getJSONArray("user_list");// 经办人
		JSONArray itemList = json.getJSONArray("item_list");// 物料明细
		String remark = json.getString("remark");// 备注
		String cUserId = UtilString.getStringOrEmptyForObject(json.get("cUserId"));// 创建人

		String voucherDate = "";// 审批日期
		String postDate = "";// 过账日期

		boolean newReturnOrder;
		boolean saveBool = false;// 暂存是否成功
		boolean submitBool = false;// 过账是否成功
		boolean submit;// false暂存 true过账 默认过账

		// 判断是否新建退库单 创建人
		int autoReturnId = 0;
		String returnCode = "";
		String createUser = "";
		String outputCodeStr = "";
		long returnCodeLong = 0;
		BizStockOutputReturnHead returnHead = new BizStockOutputReturnHead();
		try {
			// 先判断暂存还是过账
			if (UtilObject.getIntOrZero(json.get("status")) == 10) {
				submit = true;// false暂存 true过账
			} else {
				submit = false;
			}

			if (returnId == 0) {
				newReturnOrder = true;
				returnCodeLong = sequenceDAO.selectNextVal("output_return");// 退库单号
				returnCode = String.valueOf(returnCodeLong);
				createUser = cUserId;
			} else {
				newReturnOrder = false;
				returnHead = returnHeadDao.selectByPrimaryKey(returnId);
				String returnCodeStr = returnHead.getReturnCode();
				returnCode = String.valueOf(returnCodeStr);
				createUser = returnHead.getCreateUser();
			}

			// 调用sap查询销售订单抬头和明细
			JSONObject returnObj = stockOutputReturnSap.getReserveOrderListFromSap(reserveCode, cUserId);
			List<BizReserveOrderHead> reserveHeadList = stockOutputReturnSap
					.getReserveHeadListFromSapReturnObj(returnObj);
			List<BizReserveOrderItem> reserveItemList = stockOutputReturnSap
					.getReserveItemListFromSapReturnObj(returnObj);

			if (reserveHeadList.size() > 0 && reserveItemList.size() > 0) {
				BizReserveOrderHead bizReserveOrderHead = reserveHeadList.get(0);
				BizStockOutputReturnHead newReturnHead = new BizStockOutputReturnHead();
				newReturnHead.setReturnId(returnId);
				newReturnHead.setReturnCode(returnCode);
				newReturnHead.setStockOutputId(0);
				newReturnHead.setCreateUser(createUser);
				newReturnHead.setRemark(remark);
				newReturnHead.setReferReceiptCode(reserveCode);
				newReturnHead.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_UNFINISH.getValue());
				newReturnHead.setFtyId(reserveItemList.get(0).getFtyId());
				newReturnHead.setReceiptType(EnumReceiptType.STOCK_RETURN_RESERVE.getValue());
				newReturnHead.setReserveCreateTime(new Date());

				newReturnHead.setReserveCostObjCode(bizReserveOrderHead.getReserveCostObjCode());
				newReturnHead.setReserveCostObjName(bizReserveOrderHead.getReserveCostObjName());
				newReturnHead
						.setReserveCreateTime(UtilString.getDateForString(bizReserveOrderHead.getReserveCreateTime()));
				newReturnHead.setReserveCreateUser(bizReserveOrderHead.getReserveCreateUser());

				if (!newReturnOrder) {
					returnHeadDao.deleteByPrimaryKey(returnId);
					returnItemDao.deleteItemsByReturnId(returnId);
				}

				int i = 1;
				List<BizStockOutputReturnItem> newReturnItems = new ArrayList<BizStockOutputReturnItem>();
				for (Object object : itemList) {
					BizStockOutputReturnItem newReturnItem = new BizStockOutputReturnItem();
					Map<String, Object> itemMap = (Map<String, Object>) object;
					String reserveRid = itemMap.get("refer_receipt_rid").toString();// 预留单行项目
					BigDecimal qty = new BigDecimal(itemMap.get("qty").toString());// 退库数量
					// List<DicBatchSpec> batchSpecList =
					// (List<DicBatchSpec>)itemMap.get("batch_spec_list");
					// List<DicBatchSpec> batchMaterialSpecList =
					// (List<DicBatchSpec>)itemMap.get("batch_material_spec_list");
					// newReturnItem.setBatchSpecList(batchSpecList);
					// newReturnItem.setBatchMaterialSpecList(batchMaterialSpecList);
					// BizReserveOrderItem bizReserveOrderItem =
					// reserveItemList.stream()
					// .filter(z -> z.getReferReceiptRid().equals(reserveRid))
					// .findFirst().get();

					String batchString = itemMap.get("batch").toString();
					Long referBatch;
					if (batchString.length() == 0) {
						referBatch = Long.valueOf("0");
					} else {
						referBatch = Long.valueOf(itemMap.get("batch").toString());// 参考批次号
																					// 最近的批次号或""
					}

					for (BizReserveOrderItem reserveOrderItem : reserveItemList) {
						String sapReserveRid = reserveOrderItem.getReferReceiptRid();
						String sapReserveCode = reserveOrderItem.getReferReceiptCode();
						if (sapReserveRid.equals(reserveRid)) {
							// Long newBatch = commonService.getBatch();
							Long newBatch;
							if (submit) {
								newBatch = commonService.getBatch();
								newReturnItem.setBatch(newBatch);
							} else {
								newReturnItem.setBatch(referBatch);
							}

							// newReturnItem.setBatch(newBatch);
							newReturnItem.setBatchOutput(referBatch);
							newReturnItem.setFtyId(reserveOrderItem.getFtyId());
							newReturnItem.setIsDelete((byte) 0);
							newReturnItem.setLocationId(reserveOrderItem.getLocationId());
							newReturnItem.setMatId(reserveOrderItem.getMatId());
							String moveTypeCode = reserveOrderItem.getMoveTypeCode();
							String specStockCode = reserveOrderItem.getSpecStockCode();
							newReturnItem.setMoveTypeId(dictionaryService
									.getMoveTypeIdByMoveTypeCodeAndSpecStock(moveTypeCode, specStockCode));
							newReturnItem.setQty(qty);
							newReturnItem.setReferReceiptCode(bizReserveOrderHead.getReferReceiptCode());
							newReturnItem.setReferReceiptRid(reserveRid + "");
							newReturnItem.setReserveId(bizReserveOrderHead.getReferReceiptCode());
							newReturnItem.setReserveRid(reserveRid + "");

							newReturnItem.setReturnRid(i++);
							newReturnItem.setSaleQty(new BigDecimal(reserveOrderItem.getDemandQty()));// 销售数量-对应预留单的需求数量
							newReturnItem.setSendQty(new BigDecimal(reserveOrderItem.getHaveReturnQty()));// 已退库数量
							newReturnItem.setUnitId(reserveOrderItem.getUnitId());
							newReturnItem.setWriteOff((byte) 0);
							newReturnItem.setReturnId(autoReturnId);

							// 为其他字段设置默认值
							newReturnItem.setStockOutputId(0);
							newReturnItem.setStockOutputRid(0);
							newReturnItem.setPurchaseOrderCode("");
							newReturnItem.setPurchaseOrderRid("");

							// 补充成本对象和描述字段
							newReturnItem.setReserveCostObjCode(reserveOrderItem.getReserveCostObjCode());
							newReturnItem.setReserveCostObjName(reserveOrderItem.getReserveCostObjName());
							newReturnItems.add(newReturnItem);
						}
					}

				}

				autoReturnId = this.insertReturnHeadAndItems(newReturnHead, newReturnItems);
				BizStockOutputReturnHead currentReturnHead = returnHeadDao.selectByPrimaryKey(autoReturnId);

				// 添加经办人
				if (!newReturnOrder) {
					commonService.removeReceiptUser(currentReturnHead.getReturnId(),
							currentReturnHead.getReceiptType());
				}
				for (Object object : userList) {
					JSONObject obj = (JSONObject) object;
					String userId = UtilString.getStringOrEmptyForObject(obj.getString("user_id"));
					int roleId = UtilObject.getIntOrZero(obj.getString("role_id"));
					int receiptType = currentReturnHead.getReceiptType();
					commonService.addReceiptUser(currentReturnHead.getReturnId(), receiptType, userId, roleId);
				}

				saveBool = true; // 保存成功

				if (submit) {
					// 准备过账
					BizStockOutputReturnHead head = null;
					List<BizStockOutputReturnItem> items = new ArrayList<BizStockOutputReturnItem>();
					Map<String, Object> postMap = new HashMap<String, Object>();
					head = returnHeadDao.selectByPrimaryKey(autoReturnId);
					items = returnItemDao.selectItemsByReturnId(autoReturnId);

					// 补充批次信息
					for (BizStockOutputReturnItem bizStockOutputReturnItem : items) {
						for (int j = 0; j < itemList.size(); j++) {
							JSONObject object = itemList.getJSONObject(j);
							String reserveRid = object.getString("refer_receipt_rid");// 预留单行项目

							JSONObject humpObject = new JSONObject();
							humpObject.put("batch_spec_list", object.getJSONArray("batch_spec_list"));
							humpObject.put("batch_material_spec_list", object.getJSONArray("batch_material_spec_list"));
							UtilJSONConvert.convertToHump(humpObject);
							List<DicBatchSpec> batchSpecList = JSONArray.toList(
									humpObject.getJSONArray("batchSpecList"), new DicBatchSpec(), new JsonConfig());
							List<DicBatchSpec> batchMaterialSpecList = JSONArray.toList(
									humpObject.getJSONArray("batchMaterialSpecList"), new DicBatchSpec(),
									new JsonConfig());

							// 判断是获取最近的批次，还是 新建的批次
							String isRecentBatchStr = object.getString("batch");
							boolean isRecentBatch;
							if (isRecentBatchStr.length() > 0) {
								isRecentBatch = true;
							} else {
								isRecentBatch = false;
							}

							for (BizStockOutputReturnItem returnItem : items) {
								if (returnItem.getReferReceiptRid().equals(reserveRid)) {
									returnItem.setBatchMaterialSpecList(batchMaterialSpecList);
									returnItem.setBatchSpecList(batchSpecList);
									returnItem.setRecentBatch(isRecentBatch);
								}
							}
						}
					}

					String receiveFtyCode = dictionaryService.getFtyCodeByFtyId(head.getFtyId());
					voucherDate = commonService.getPostDate(head.getFtyId());// 审批日期
					postDate = commonService.getPostDate(head.getFtyId());// 过账日期

					head.setVoucherDate(voucherDate);
					head.setPostDate(postDate);
					head.setCurrentUserId(cUserId);
					head.setWerkToSap(receiveFtyCode);

					// SAP接口过账
					postMap = stockOutputReturnSap.submitToSapForMatreqForOneCorp(head, items);// sap过账，组装返回值
					errorCode = (Integer) postMap.get("errorCode");
					errorString = (String) postMap.get("errorString");
					if (errorCode == EnumErrorCode.ERROR_CODE_SUCESS.getValue()) {
						BizMaterialDocHead mDocHead = (BizMaterialDocHead) postMap.get("materialDocHead");
						this.updateReturnStockForOneCorp(mDocHead);// sap过账成功修改相关标数据
						commonService.initRoleWf(autoReturnId, head.getReceiptType(), head.getReturnCode(),
								head.getCreateUser());
						status = true;
					} else {
						for (BizStockOutputReturnItem bizStockOutputReturnItem : items) {
							bizStockOutputReturnItem.setBatch(bizStockOutputReturnItem.getBatchOutput());
							returnItemDao.updateByPrimaryKeySelective(bizStockOutputReturnItem);
						}
						status = false;
						body.put("return_id", autoReturnId);
						throw new InterfaceCallException(errorString);
					}
				}

			}
		} catch (WMSException e) {
			logger.error("", e);
			status = false;
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			body.put("return_id", autoReturnId);
		} catch (Exception e) {
			logger.error("", e);
			status = false;
			body.put("return_id", autoReturnId);
		}

		JSONObject object = new JSONObject();
		object.put("errorCode", errorCode);
		object.put("errorString", errorString);
		object.put("body", body);
		return object;
	}

	@Override
	public void addeBatchMaterialInfoForReturnOrder(BizMaterialDocHead materialDocHead) throws Exception {
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
			if (item.isRecentBatch()) {
				int ftyId = item.getFtyId();
				int matId = item.getMatId();
				long batch = item.getBatch();
				Map<String, Object> innerMap = new HashMap<String, Object>();
				innerMap.put("returnId", item.getReferReceiptId());
				innerMap.put("returnRid", item.getReferReceiptRid());
				BizStockOutputReturnItem returnItem = returnItemDao.selectItemByReturnIdAndReturnRid(innerMap);

				// 批次信息
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("matId", matId);// 物料
				map.put("ftyId", ftyId);// 工厂
				map.put("batch", returnItem.getBatchOutput());// 原批次
				map.put("newBatch", batch);// 新批次
				map.put("createUser", materialDocHead.getCreateUser());// 新批次创建人
				batchMaterialDao.insertBatchMaterialForNewBatch(map);
				// 批次特性
				batchMaterialSpecDao.insertBatchMaterialSpecForNewBatch(map);
			} else {
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

		}
		if (bmsList.size() > 0) {
			batchMaterialSpecDao.insertList(bmsList);
		}
	}

	@Override
	public int insertReturnHeadAndItems(BizStockOutputReturnHead newReturnHead,
			List<BizStockOutputReturnItem> newReturnItems) {
		returnHeadDao.insertSelective(newReturnHead);
		int autoReturnId = newReturnHead.getReturnId();
		for (BizStockOutputReturnItem bizStockOutputReturnItem : newReturnItems) {
			bizStockOutputReturnItem.setReturnId(autoReturnId);
			returnItemDao.insertSelective(bizStockOutputReturnItem);
		}

		return autoReturnId;
	}

	/**
	 * 通过退库单id查询退库单头信息
	 */
	@Override
	public BizStockOutputReturnHead getBizStockOutputReturnHeadByReturnId(int returnId) throws Exception {

		return returnHeadDao.selectByPrimaryKey(returnId);
	}

	@Override
	public BizDeliveryOrderHead getDeliveryOrderInfo(Map<String, Object> map) throws Exception {
		String  referReceiptCode = UtilString.getStringOrEmptyForObject(map.get("condition"));	
		String  createUserId = UtilString.getStringOrEmptyForObject(map.get("cUserId"));
		String  createUserName = UtilString.getStringOrEmptyForObject(map.get("cUserName"));

		SapDeliveryOrderHead sapDeliveryOrderHead = cwErpWsImpl.getDoOrder(referReceiptCode);
		BizDeliveryOrderHead head = new BizDeliveryOrderHead();
		head.setCustomerCode(sapDeliveryOrderHead.getReceiveCode());//客户编号
		head.setCustomerName(sapDeliveryOrderHead.getReceiveName());//客户名称
		head.setDeliveryCode(sapDeliveryOrderHead.getDeliveryOrderCode());//交货单号
		head.setReferPurchaseCode(sapDeliveryOrderHead.getReferSaleOrderCode());//参考采购订单编号
		head.setSaleDocumentType(sapDeliveryOrderHead.getOrderType());//订单类型
		head.setSaleDocumentTypeName(sapDeliveryOrderHead.getOrderTypeName());//类型描述
		head.setSaleOrgCode(sapDeliveryOrderHead.getOrgCode());//销售组织
		head.setPreorderId(sapDeliveryOrderHead.getContractNumber());//合同编号
		head.setSaleOrderCode(sapDeliveryOrderHead.getSaleOrderCode());//销售订单号
		head.setCreateTime(UtilString.getShortStringForDate(new Date()));
		head.setCreateUserName(createUserName);
		head.setClassTypeList(commonService.getClassTypeList());
		
		//显示用户库存地点权限下的行项目
		List<RelUserStockLocationVo> locationList = userService.listLocForUser(createUserId);
		List<Integer> locationIds = new ArrayList<Integer>();
		for (RelUserStockLocationVo innerObj : locationList) {
			locationIds.add(innerObj.getLocationId());
		}
		List<BizDeliveryOrderItem> itemList = new ArrayList<>();
		List<SapDeliveryOrderItem> sapItemList = sapDeliveryOrderHead.getItemList();
		if (sapItemList != null && sapItemList.size() >0) {
			int i = 1;
			// 工厂
			String ftyCode = sapItemList.get(0).getFtyCode();				
			int ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
			if (ftyId <= 0) {
				throw new WMSException("工厂无效");
			}			
			List<RelUserStockLocationVo> allLocationList = dicStockLocationService.listByFtyId(ftyId);
			List<Map<String, Object> > locList = new ArrayList<>();
			for (RelUserStockLocationVo loc : allLocationList) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("location_id", loc.getLocationId());
				map2.put("location_code", loc.getLocationCode());
				map2.put("location_name", loc.getLocationName());
				locList.add(map2);
			}
			
			for (SapDeliveryOrderItem saptem : sapItemList) {
				BizDeliveryOrderItem bizItem = new BizDeliveryOrderItem();
				bizItem.setCanReturnQty(saptem.getQty());
				bizItem.setDeliveryCode(referReceiptCode);
				bizItem.setDeliveryQty(saptem.getQty());
				bizItem.setDeliveryRid(saptem.getDeliveryOrderRid());
				bizItem.setErpBatch(saptem.getErpBatch());
				bizItem.setHaveShelvesQty(BigDecimal.ZERO);
				bizItem.setReturnQty(BigDecimal.ZERO);
				bizItem.setRid(i++);
				
				bizItem.setFtyCode(ftyCode);
				bizItem.setFtyId(ftyId);
				bizItem.setFtyName(factoryDao.selectByPrimaryKey(ftyId).getFtyName());

				bizItem.setLocList(locList);
				
				// 库存地点
				String locationCode = saptem.getLocationCode();// 库存地点编码				
				int locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
				if (locationId <= 0) {
					bizItem.setLocationCode("");
					bizItem.setLocationId(0);
					bizItem.setLocationName("");
				}else {
					bizItem.setLocationCode(locationCode);
					bizItem.setLocationId(locationId);
					bizItem.setLocationName(locationDao.selectByPrimaryKey(locationId).getLocationName());
				}
				

				// 物料
				String matCode = saptem.getMatCode();// 物料编码
				int matId = dictionaryService.getMatIdByMatCode(matCode);
				if (matId <= 0) {
					throw new WMSException("物料无效");
				}
				bizItem.setMatCode(matCode);
				bizItem.setMatId(matId);
				bizItem.setMatName(dicMaterialDao.selectByPrimaryKey(matId).getMatName());
				List<Map<String, Object>> erpList = new ArrayList<>();
				erpList = commonService.getErpBatchList(matId, ftyId);
				if (erpList!= null && erpList.size()>0) {
					bizItem.setErpBatchList(erpList);
				}else {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("erp_batch", bizItem.getErpBatch());
					map2.put("erp_batch_name", bizItem.getErpBatch());
					map2.put("mat_id", bizItem.getMatId());
					erpList.add(map2);
					bizItem.setErpBatchList(erpList);
				}
				
				
				//单位
				String unitCode = saptem.getUnitCode();
				int unitId = dictionaryService.getUnitIdByUnitCode(unitCode);
				if (unitId <= 0) {
					throw new WMSException("单位无效");
				}
				bizItem.setUnitCode(unitCode);
				bizItem.setUnitId(unitId);
				bizItem.setUnitName(dicUnitDao.selectByPrimaryKey(unitId).getNameZh());
				
				if (locationId ==0 || locationIds.contains(locationId)){
					itemList.add(bizItem);
				}

			}
		}
		head.setItemList(itemList);
		return head;
	}

	@Override
	public List<Map<String, Object>> getSaleOutputItemList(Map<String, Object> map) throws Exception {

		return outputItemDao.getOutputItemList(map);
	}

	/**
	 * 获取出库新主键
	 */
	private long getNewOrderCode() {
		return sequenceDao.selectNextVal("output_return");
	}

	/**
	 * 保存退库单抬头信息
	 */
	private BizStockOutputReturnHead saveHeadData(boolean isAdd, JSONObject json, BizStockOutputReturnHead head,
			BizDeliveryOrderHead deliveryOrderHead) {

		String deliveryCode = UtilObject.getStringOrEmpty(json.get("delivery_code"));// 交货单号
		int returnId = json.getInt("return_id");// 退库单id
		String remark = json.getString("remark");// 备注
		String cUserId = UtilString.getStringOrEmptyForObject(json.get("cUserId"));// 创建人
		Long code = this.getNewOrderCode();
		String returnCode = code.toString();
		// head.setReturnId(returnId);
		head.setReturnCode(returnCode);
		head.setStockOutputId(0);
		head.setCreateUser(cUserId);
		head.setRemark(remark);
		head.setReferReceiptCode(deliveryCode);// 交货单号
		head.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_SUBMIT.getValue());
		head.setFtyId(deliveryOrderHead.getItemList().get(0).getFtyId());
		head.setReceiptType(EnumReceiptType.STOCK_RETURN_SALE.getValue());
		head.setReserveCreateTime(new Date());
		head.setOrderType(deliveryOrderHead.getSaleDocumentType());// 订单类型
		head.setOrderTypeName(deliveryOrderHead.getSaleDocumentTypeName());// 订单类型描述
		head.setCustomerCode(deliveryOrderHead.getCustomerCode());
		head.setCustomerName(deliveryOrderHead.getCustomerName());// 客户描述
		head.setSaleOrg(deliveryOrderHead.getSaleOrgCode());
		head.setPreorderId(deliveryOrderHead.getPreorderId());// 合同号
		head.setClassTypeId(UtilObject.getIntegerOrNull(json.get("class_type_id")));// 班次		
		head.setProductLineId(UtilObject.getIntegerOrNull(json.get("product_line_id")));//产品线
		head.setInstallationId(UtilObject.getIntegerOrNull(json.get("installation_id")));//装置
		
		int synType = UtilObject.getIntegerOrNull(json.get("syn_type"));
		head.setSynType((byte) synType);
		this.saveOrderInfo(isAdd, head);
		return head;
	}

	/**
	 * 保存退库单数据
	 */
	private int saveOrderInfo(boolean isAdd, BizStockOutputReturnHead head) {
		if (isAdd) {
			return returnHeadDao.insertSelective(head);
		} else {
			return returnHeadDao.updateByPrimaryKeySelective(head);
		}
	}

	/**
	 * 保存退库单详细信息
	 */
	private BizStockOutputReturnItem saveItemData(Map<String, Object> itemMap, BizStockOutputReturnItem item,
			BizDeliveryOrderItem bizDeliveryOrderItem) {
		item.setFtyId(bizDeliveryOrderItem.getFtyId());
		item.setIsDelete((byte) 0);
		//item.setLocationId(bizDeliveryOrderItem.getLocationId());
		item.setLocationId(UtilObject.getIntOrZero(itemMap.get("location_id")));
		item.setMatId(bizDeliveryOrderItem.getMatId());
		item.setMoveTypeId(0);
		item.setQty(new BigDecimal(UtilString.getStringOrEmptyForObject(itemMap.get("return_qty"))));// 退库数量
		item.setReferReceiptCode(bizDeliveryOrderItem.getDeliveryCode());
		item.setReferReceiptRid(bizDeliveryOrderItem.getDeliveryRid());
		item.setSaleQty(bizDeliveryOrderItem.getDeliveryQty());// 订单数量
		item.setUnitId(bizDeliveryOrderItem.getUnitId());
		item.setWriteOff((byte) 0);
		item.setRemark(UtilString.getStringOrEmptyForObject(itemMap.get("remark")));// 备注

		// 为其他字段设置默认值
		item.setStockOutputId(0);
		item.setStockOutputRid(0);
		item.setPurchaseOrderCode("");
		item.setPurchaseOrderRid("");
		item.setReserveId("");
		item.setReserveRid("");
		item.setErpBatch(bizDeliveryOrderItem.getErpBatch());
		item.setSendQty(BigDecimal.ZERO);
		item.setErpRemark(bizDeliveryOrderItem.getErpRemark());
		returnItemDao.insertSelective(item);
		return item;
	}

	private BizStockOutputReturnPosition savePositionData(int ftyId , int matId, BizStockOutputReturnHead returnHead,Map<String, Object> positionDataTemp,
			BizStockOutputReturnPosition returnPosition) {
		int packageTypeId = UtilObject.getIntOrZero(positionDataTemp.get("package_type_id"));//包装类型									
		String productBatch = UtilString.getStringOrEmptyForObject(positionDataTemp.get("product_batch"));//生产批次
		String qualityBatch = UtilString.getStringOrEmptyForObject(positionDataTemp.get("quality_batch"));//质检批次
		String outputBatchStr = UtilString.getStringOrEmptyForObject(positionDataTemp.get("output_batch"));//出库批次
		String erpBatch = UtilString.getStringOrEmptyForObject(positionDataTemp.get("erp_batch"));//erp批次
		BigDecimal qty = new BigDecimal(UtilObject.getStringOrEmpty(positionDataTemp.get("qty")));//退库数量
		String outputQtyStr = UtilObject.getStringOrEmpty(positionDataTemp.get("output_qty"));//出库数量
		BigDecimal outputQty = BigDecimal.ZERO;
		if (!outputQtyStr.equals("") && outputQtyStr.length() >0) {
			outputQty = new BigDecimal(outputQtyStr);
		}
		Long  outputBatch = Long.valueOf("0");
		if (!outputBatchStr.equals("") && outputBatchStr.length() >0) {
			outputBatch = Long.valueOf(outputBatchStr); 
		}
		returnPosition.setCreateUser(returnHead.getCreateUser());
		returnPosition.setPackageTypeId(packageTypeId);
		returnPosition.setProductBatch(productBatch);
		returnPosition.setQualityBatch(qualityBatch);
		returnPosition.setOutputBatch(Long.valueOf(outputBatch));
		returnPosition.setErpBatch(erpBatch);
		returnPosition.setQty(qty);
		returnPosition.setOutputQty(outputQty);
		returnPosition.setStockQty(BigDecimal.ZERO);
		returnPositionDao.insertSelective(returnPosition);

		// 修改原出库position已退库数量
		// int outputItemPositionId = outputPosition.getItemPositionId();
//		outputItem.setReturnQty(outputItem.getReturnQty().add(returnPosition.getQty()));
//		outputItemDao.updateByPrimaryKeySelective(outputItem);

		//生成批次信息
		BatchMaterial batchMaterial = new BatchMaterial();
		batchMaterial.setBatch(returnPosition.getBatch());
		batchMaterial.setProductionBatch(returnPosition.getProductBatch());
		batchMaterial.setQualityBatch(returnPosition.getQualityBatch());
		batchMaterial.setPackageTypeId(returnPosition.getPackageTypeId());
		batchMaterial.setFtyId(ftyId);
		batchMaterial.setMatId(matId);	
		batchMaterial.setErpBatch(returnPosition.getErpBatch());
		batchMaterial.setCreateUser(returnHead.getCreateUser());
		batchMaterial.setProductLineId(returnHead.getProductLineId());
		batchMaterial.setInstallationId(returnHead.getInstallationId());
		batchMaterial.setClassTypeId(returnHead.getClassTypeId());
		batchMaterialDao.insertSelective(batchMaterial);
		
		return returnPosition;
	}

	// 川维销售退库提交
	@Override
	public JSONObject saveSaleReturnOrder(JSONObject json) throws Exception {
		boolean isAdd = false; // 新增or修改
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		String deliveryCode = UtilObject.getStringOrEmpty(json.get("delivery_code"));// 交货单号

		// 调用sap查询交货单抬头和明细
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("condition", deliveryCode);
		param.put("cUserId", json.get("cUserId"));
		BizDeliveryOrderHead deliveryOrderHead = this.getDeliveryOrderInfo(param);

		BizStockOutputReturnHead returnHead = new BizStockOutputReturnHead(); // 退库单对象
		int returnId = 0;

		if (json.containsKey("return_id")) {
			returnId = UtilObject.getIntOrZero(json.get("return_id"));
		}

		// 根据出库单号是否存在 判断新增还是修改
		if (returnId == 0) {
			isAdd = true;
		} else {
			returnHead = returnHeadDao.selectByPrimaryKey(returnId);
			if (null == returnHead || "".equals(returnHead.getReturnCode()) || returnHead.getIsDelete() == 1) {
				isAdd = true;
			}
		}

		// 保存退库单抬头
		returnHead = this.saveHeadData(isAdd, json, returnHead, deliveryOrderHead);

		// 保存退库单明细
		JSONArray itemList = json.getJSONArray("item_list");
		List<BizDeliveryOrderItem> deliveryItemList = deliveryOrderHead.getItemList();

		List<BizStockOutputReturnItem> returnItemList = new ArrayList<>();
		List<BizStockOutputReturnPosition> returnPositionList = new ArrayList<>();
		if (itemList != null && itemList.size() > 0) {
			for (Object object : itemList) {
				Map<String, Object> itemMap = (Map<String, Object>) object;
				BizStockOutputReturnItem returnItem = new BizStockOutputReturnItem();
				String deliveryRid = itemMap.get("delivery_rid").toString();// 交货单行项目
				int i = 1;
				for (BizDeliveryOrderItem bizDeliveryOrderItem : deliveryItemList) {
					String referDeliveryCode = bizDeliveryOrderItem.getDeliveryCode();
					String referDeliveryRid = bizDeliveryOrderItem.getDeliveryRid();
					if (deliveryCode.equals(referDeliveryCode) && deliveryRid.equals(referDeliveryRid)) {
						returnItem.setReturnId(returnHead.getReturnId());
						returnItem.setReturnRid(i++);
						returnItem = this.saveItemData(itemMap, returnItem, bizDeliveryOrderItem);

						// 保存退库单仓位信息
						List<Map<String, Object>> positionList = (List<Map<String, Object>>) itemMap
								.get("position_list");
						int k = 1;
						if (positionList != null) {
							for (int j = 0; j < positionList.size(); j++) {
								Map<String, Object> positionDataTemp = positionList.get(j);

								if (UtilObject.getBigDecimalOrZero(positionDataTemp.get("qty"))
										.compareTo(BigDecimal.ZERO) == 1) {
//									Map<String, Object> innerParam = new HashMap<>();
//									innerParam.put("itemId",UtilObject.getIntOrZero(positionDataTemp.get("stock_output_item_id")));//出库单行项目item id
//									int itemPositionCwId = UtilObject.getIntOrZero(positionDataTemp.get("stock_task_position_item_id"));//下架作业单仓位主键
								
//									BizStockTaskPositionCw positionCw = positionCwDao.selectByPrimaryKey(itemPositionCwId);
//									BizStockOutputItem outputItem = outputItemDao.selectByPrimaryKey(innerParam);//出库单行项目

									BizStockOutputReturnPosition returnPosition = new BizStockOutputReturnPosition();
									returnPosition.setBatch(commonService.getBatch());									
									returnPosition.setReturnId(returnHead.getReturnId());
									returnPosition.setReturnRid(returnItem.getReturnRid());																	
									returnPosition.setReturnPositionId(k++);
									returnPosition = this.savePositionData(returnItem.getFtyId(), returnItem.getMatId(), returnHead,positionDataTemp,returnPosition);
									returnPositionList.add(returnPosition);
								}
							}
							returnItem.setPositionList(returnPositionList);
						}
						returnItemList.add(returnItem);

					}

				}

			}

		}
		returnHead.setItemList(returnItemList);
		// 生成上架请求
		//BizStockOutputReturnHead head = this.getReturnAllInfo(returnHead.getReturnId());
		this.addStockTaskReqHeadAndItem(returnHead,returnHead.getCreateUser());

		JSONObject object = new JSONObject();
		object.put("return_id", returnHead.getReturnId());
		return object;
	}

	/**
	 * 保存上架作业请求
	 * 
	 * @param result
	 * @param jsonData
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public void addStockTaskReqHeadAndItem(BizStockOutputReturnHead returnHead,String userId) throws Exception {
		// 作业请求 库存地点 map
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();
		for (BizStockOutputReturnItem item : returnHead.getItemList()) {
			for (BizStockOutputReturnPosition position : item.getPositionList()) {

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

					this.addStockTaskReqItem(headInfo, returnHead, item, position, reqItem);

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
					if (returnHead.getStatus().equals(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_WRITEOFF.getValue())) {
						reqhead.setCreateUser(userId);
						reqhead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
						reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
					}else {
						reqhead.setCreateUser(returnHead.getCreateUser());
						reqhead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
						reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
					}					
					reqhead.setReferReceiptCode(returnHead.getReturnCode());
					reqhead.setReferReceiptId(returnHead.getReturnId());
					reqhead.setReferReceiptType(returnHead.getReceiptType());
					StockTaskReqHeadDao.insertSelective(reqhead);

					// --------------end
					int stockTaskReqRid = 1;
					int stockTaskReqId = reqhead.getStockTaskReqId();

					Map<String, Object> headInfo = new HashMap<String, Object>();
					headInfo.put("stockTaskReqId", stockTaskReqId);// 仓库号
					headInfo.put("whId", whId);// 作业申请号
					headInfo.put("stockTaskReqRid", stockTaskReqRid);// 作业申请序号
					locationIdMap.put(locationId, headInfo);

					BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

					this.addStockTaskReqItem(headInfo, returnHead, item, position, reqItem);

					addItemList.add(reqItem);

				}
			}

		}
		StockTaskReqItemDao.insertReqItems(addItemList);

	}

	public void addStockTaskReqItem(Map<String, Object> headMap, BizStockOutputReturnHead returnHead,
			BizStockOutputReturnItem returnItem, BizStockOutputReturnPosition returnPosition,
			BizStockTaskReqItem reqItem) throws Exception {

		int stockTaskReqId = (int) headMap.get("stockTaskReqId");
		int stockTaskReqRid = (int) headMap.get("stockTaskReqRid");
		int whId = (int) headMap.get("whId");
		reqItem.setBatch(returnPosition.getBatch());
		reqItem.setWhId(whId);
		reqItem.setFtyId(returnItem.getFtyId());
		reqItem.setLocationId(returnItem.getLocationId());
		reqItem.setMatId(returnItem.getMatId());

		reqItem.setUnitId(returnItem.getUnitId());
		reqItem.setQty(returnPosition.getQty());
		reqItem.setPackageTypeId(returnPosition.getPackageTypeId());
		reqItem.setStockTaskReqId(stockTaskReqId);
		reqItem.setStockTaskReqRid(stockTaskReqRid);
		reqItem.setProductionBatch(returnPosition.getProductBatch());
		reqItem.setErpBatch(returnPosition.getErpBatch());
		reqItem.setQualityBatch(returnPosition.getQualityBatch());

		reqItem.setReferReceiptCode(returnHead.getReturnCode());
		reqItem.setReferReceiptId(returnHead.getReturnId());
		reqItem.setReferReceiptType(returnHead.getReceiptType());
		reqItem.setReferReceiptRid(returnItem.getReturnRid());

	}

	// /**
	// * 保存上架作业请求
	// *
	// * @param result
	// * @param jsonData
	// * @param user
	// * @return
	// * @throws Exception
	// */
	// private void saveStockTaskReq(BizStockOutputReturnHead returnHead) {
	// String stockTaskReqCode;
	// try {
	// stockTaskReqCode =
	// commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
	//
	// List<BizStockOutputReturnItem> itemList = returnHead.getItemList();
	// if (itemList.size() > 0) {
	// // BizStockOutputReturnItem retrunItem = itemList.get(0);
	// // 保存作业请求头部信息
	// BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
	// taskReqHead.setStockTaskReqCode(stockTaskReqCode);
	// taskReqHead.setWhId(0);
	// taskReqHead.setFtyId(UtilObject.getIntegerOrNull(itemList.get(0).getFtyId()));
	// taskReqHead.setLocationId(UtilObject.getIntegerOrNull(itemList.get(0).getLocationId()));
	// taskReqHead.setMatDocId(0);
	// taskReqHead.setMatDocYear(0);
	// taskReqHead.setShippingType("");
	// taskReqHead.setStatus(EnumStockTaskReqStatus.UN_FINISHED.getValue());
	// taskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
	// taskReqHead.setReferReceiptId(returnHead.getReturnId());
	// taskReqHead.setReferReceiptCode(returnHead.getReturnCode());
	// taskReqHead.setReferReceiptType(returnHead.getReceiptType());
	// taskReqHead.setCreateUser(returnHead.getCreateUser());
	// taskReqHead.setCreateTime(new Date());
	// taskReqHead.setModifyTime(new Date());
	// taskReqHead.setWhId(returnHead);
	// StockTaskReqHeadDao.insertSelective(taskReqHead);
	// BizStockOutputReturnItem item = new BizStockOutputReturnItem();
	// for (int i = 0; i < itemList.size(); i++) {
	// item = itemList.get(i);
	// List<BizStockOutputReturnPosition> positionList = item.getPositionList();
	// for (BizStockOutputReturnPosition returnPosition : positionList) {
	// if (positionList != null && positionList.size() > 0) {
	// BizStockTaskReqPosition taskReqPosition = new BizStockTaskReqPosition();
	// BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();
	// taskReqItem.setFtyId(item.getFtyId());
	// taskReqItem.setLocationId(item.getLocationId());
	// taskReqItem.setMatId(item.getMatId());
	// taskReqItem.setStockTaskQty(BigDecimal.ZERO);
	// taskReqItem.setQty(item.getQty());
	// taskReqItem.setErpBatch(item.getErpBatch().toString());
	// taskReqItem.setUnitId(item.getUnitId());
	// // taskReqItem.setPackageTypeId(item);
	//
	// taskReqItem.setReferReceiptType(returnHead.getReceiptType());
	// taskReqItem.setReferReceiptId(returnHead.getReturnId());
	// taskReqItem.setReferReceiptCode(returnHead.getReturnCode());
	// taskReqItem.setReferReceiptRid(item.getReturnRid());
	// taskReqItem.setStockTaskReqId(taskReqHead.getStockTaskReqId());
	// taskReqItem.setStockTaskReqRid(item.getReturnRid());
	// taskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
	// taskReqItem.setStatus(EnumBoolean.FALSE.getValue());
	// taskReqItem.setWhId(0);
	// taskReqItem.setCreateTime(new Date());
	// taskReqItem.setModifyTime(new Date());
	// taskReqItem.setPackageTypeId(returnPosition.);
	// taskReqItem.setBatch(returnPosition.getOutputBatch());
	//
	// StockTaskReqItemDao.insertSelective(taskReqItem);
	//
	// }
	//
	// }
	//
	// }
	// }
	//
	// } catch (Exception e) {
	//
	// }
	// }

	@Override
	public List<Map<String, Object>> selectReturnPositionList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> resultMap = returnPositionDao.selectPositionBatchInfo(map);
		if (resultMap != null && resultMap.size() > 0) {
			for (Map<String, Object> innerMap : resultMap) {
//				int stockStatus = UtilObject.getIntOrZero(innerMap.get("status"));// 库存状态
//				innerMap.put("stock_status_name", getStockStatusName(stockStatus));
				innerMap.put("stock_status_name", EnumStockType.STOCK_TYPE_ERP.getName());// 库存类型
			}
		}

		return resultMap;
	}

	private String getStockStatusName(int status) {
		String stockStatusName = "";
		if (status == 1) {
			stockStatusName = "非限制库存";
		} else if (status == 2) {
			stockStatusName = "在途库存";
		} else if (status == 3) {
			stockStatusName = "质量检验库存";
		} else if (status == 4) {
			stockStatusName = "冻结的库存";
		}
		return stockStatusName;
	}

	// 川维退库单概览
	@Override
	public Map<String, Object> getSaleReturnInfo(Integer returnId) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BizStockOutputReturnHead head = returnHeadDao.selectByReturnId(returnId);
		List<BizStockOutputReturnItemVo> itemList = returnItemDao.selectSaleReturnItems(returnId);
		if (head != null && itemList.size() > 0) {
			//byte receiptType = head.getReceiptType();
			returnMap.put("return_id", returnId);// 退库单id
			returnMap.put("return_code", head.getReturnCode());// 退库单号
			returnMap.put("delivery_code", head.getReferReceiptCode());// 交货单号
			returnMap.put("preorder_id", head.getPreorderId());// 合同编号
			returnMap.put("customer_code", head.getCustomerCode());// 客户编码
			returnMap.put("customer_name", head.getCustomerName());// 客户名称
			returnMap.put("class_name", head.getClassTypeName());// 班次
			returnMap.put("product_line_name", head.getProductLineName());// 产品线
			returnMap.put("installation_name", head.getInstallationName());// 装置
			returnMap.put("create_user_name", head.getCreateUser());// 创建人
			returnMap.put("create_time", UtilString.getLongStringForDate(head.getCreateTime()));// 创建日期
			returnMap.put("remark", head.getRemark());// 备注
			returnMap.put("status", head.getStatus());// 状态
			String statusName = EnumStockOutputReturnStatus.getNameByValue(head.getStatus());
			returnMap.put("status_name", statusName); // 状态名称
			returnMap.put("syn_type", head.getSynType()); // 同步
			returnMap.put("mat_doc_code", itemList.get(0).getMatDocCode()==null ? "":itemList.get(0).getMatDocCode()); // 物料凭证
			returnMap.put("mes_doc_code", itemList.get(0).getMesDocCode()==null ? "":itemList.get(0).getMesDocCode()); // MES凭证
			returnMap.put("mat_write_off_code", itemList.get(0).getMatWriteOffCode()==null ? "":itemList.get(0).getMatWriteOffCode()); // 物料冲销凭证
			returnMap.put("mes_write_off_code", itemList.get(0).getMesWriteOffCode()==null ? "":itemList.get(0).getMesWriteOffCode()); // MES冲销凭证
			// 退库单明细
			int i = 1;
			
			List<RelUserStockLocationVo> allLocationList = dicStockLocationService.listByFtyId(head.getFtyId());
			List<Map<String, Object> > locList = new ArrayList<>();
			for (RelUserStockLocationVo loc : allLocationList) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("location_id", loc.getLocationId());
				map2.put("location_code", loc.getLocationCode());
				map2.put("location_name", loc.getLocationName());
				locList.add(map2);
			}
			List<Map<String, Object>> list = new ArrayList<>();
			for (BizStockOutputReturnItemVo itemVo : itemList) {
				Map<String, Object> map = new HashMap<>();								
				map.put("rid", i++);
				map.put("mat_id", itemVo.getMatId());
				map.put("mat_code", itemVo.getMatCode());
				map.put("mat_name", itemVo.getMatName());
				map.put("fty_id", itemVo.getFtyId());
				map.put("fty_code", itemVo.getFtyCode());
				map.put("fty_name", itemVo.getFtyName());
				map.put("location_id", itemVo.getLocationId());
				map.put("location_code", itemVo.getLocationCode());
				map.put("location_name", itemVo.getLocationName());
				
				map.put("loc_list", locList);
				map.put("unit_id", itemVo.getUnitId());
				map.put("unit_code", itemVo.getUnitEn());
				map.put("unit_name", itemVo.getUnitName());
				map.put("erp_batch", itemVo.getErpBatch());
				
				map.put("remark", itemVo.getRemark());
				map.put("return_qty", itemVo.getQty());
				map.put("erp_remark", itemVo.getErpRemark());// erp备注
				map.put("delivery_qty", itemVo.getOrderQty());// 订单数量
				BigDecimal orderQty = itemVo.getOrderQty();
				BigDecimal returnQty = itemVo.getQty();
				map.put("can_return_qty", orderQty.subtract(returnQty));// 待退库数量
				Map<String, Object> selectMap = new HashMap<>();
				selectMap.put("referReceiptId", itemVo.getReturnId());
				selectMap.put("referReceiptRid", itemVo.getReturnRid());
				selectMap.put("referReceiptCode", head.getReturnCode());
				selectMap.put("referReceiptType", head.getReceiptType());
				selectMap.put("receiptType", EnumReceiptType.STOCK_TASK_LISTING.getValue());
				Map<String, Object> result = stockTaskItemCwDao.selectHaveShelvesQty(selectMap);
				if (result == null ) {
					map.put("have_shelves_qty", BigDecimal.ZERO);
				}else {
					String haveShelvesQty = UtilString.getStringOrEmptyForObject(result.get("have_shelves_qty"));
					map.put("have_shelves_qty", new BigDecimal(haveShelvesQty));
				}
				
				Map<String, Object> innerMap = new HashMap<>();
				innerMap.put("returnId", itemVo.getReturnId());
				innerMap.put("returnRid", itemVo.getReturnRid());
				innerMap.put("matId", itemVo.getMatId());
				innerMap.put("ftyId", itemVo.getFtyId());
				innerMap.put("erpBatch", itemVo.getErpBatch());
				innerMap.put("receiptType", EnumReceiptType.STOCK_RETURN_SALE.getValue());
				map.put("position_list", this.selectReturnPositionList(innerMap));
				List<Map<String, Object>> erpList = new ArrayList<>();
				erpList = commonService.getErpBatchList(itemVo.getMatId(), itemVo.getFtyId());
				if (erpList!= null && erpList.size()>0) {
					map.put("erp_batch_list", erpList);
				}else {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("erp_batch", itemVo.getErpBatch());
					map2.put("erp_batch_name", itemVo.getErpBatch());
					map2.put("mat_id", itemVo.getMatId());
					erpList.add(map2);
					map.put("erp_batch_list", erpList);
				}
				list.add(map);

			}
			returnMap.put("item_list", list);

		}

		return returnMap;
	}

	// 川维销售退库过账
	@Override
	public Map<String, Object> postOrderToSap(Integer returnId, String userId) throws Exception {
		BizStockOutputReturnHead returnHead = this.getReturnAllInfo(returnId);
		int locationNumber = returnItemDao.selectCountByLocation(returnId);
		Map<String, Object> sapReturn = new HashMap<>();
		if (locationNumber == 1) {
			sapReturn = this.saveSapSaleByOneLocation(returnHead);
		}else {
			sapReturn = this.saveSapSaleByMoreLocation(returnHead);
		}		
		sapReturn.put("return_id", returnId);
		sapReturn.put("syn_type", returnHead.getSynType());
		return sapReturn;
	}
	
	private void addReturnHistory(Integer returnId) {
		BizStockOutputReturnHead returnHead = this.getReturnAllInfo(returnId);
		List<BizStockOutputReturnItem>  itemList = returnHead.getItemList();
		BizBusinessHistory returnHistory = new BizBusinessHistory();
		for (BizStockOutputReturnItem item : itemList) {
			List<BizStockOutputReturnPosition> positionList =  item.getPositionList();
			for (BizStockOutputReturnPosition position : positionList) {
				returnHistory = new BizBusinessHistory();
				returnHistory.setAreaId(position.getAreaId());
				returnHistory.setBatch(position.getBatch());
				if (item.getWriteOff() == EnumReceiptStatus.RECEIPT_WRITEOFF.getValue()) {
					returnHistory.setBusinessType(EnumBusinessType.STOCK_OUTPUT.getValue());
				}else {
					returnHistory.setBusinessType(EnumBusinessType.STOCK_INPUT.getValue());
				}				
				returnHistory.setCreateUser(returnHead.getCreateUser());
				returnHistory.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
				returnHistory.setFtyId(item.getFtyId());
				returnHistory.setLocationId(item.getLocationId());
				returnHistory.setMatId(item.getMatId());
				returnHistory.setModifyUser(returnHead.getCreateUser());
				returnHistory.setPackageId(position.getPackageId());
				returnHistory.setPalletId(position.getPalletId());
				returnHistory.setPositionId(position.getPositionId());
				returnHistory.setQty(position.getQty());
				returnHistory.setReceiptType(returnHead.getReceiptType());
				returnHistory.setReferReceiptCode(returnHead.getReturnCode());
				returnHistory.setReferReceiptId(returnHead.getReturnId());
				returnHistory.setReferReceiptPid(position.getItemPositionId());
				returnHistory.setReferReceiptRid(position.getReturnRid());
				if (returnHead.getSynType()==EnumSynType.NO_SYN.getValue()) {
					returnHistory.setSynMes(false);					
				}else {
					returnHistory.setSynMes(true);
				}
				returnHistory.setSynSap(false);
				historyDao.insertSelective(returnHistory);
			}
			
		}
	}

	/**
	 * sap 销售出库过账 单库存地点
	 * @param headVo
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> saveSapSaleByOneLocation(BizStockOutputReturnHead returnHead)throws Exception{
		Map<String, Object> sapReturn = new HashMap<String,Object>();
		List<BizStockOutputReturnItem> items = returnHead.getItemList();
		DTCWWMSDOPOSTREQ mtCWWMSDOPOSTREQ = new DTCWWMSDOPOSTREQ();

		/****************************************
		 * 	IS_MSG_HEAD
		 * ************************************/
		ISMSGHEAD iSMSGHEAD = new ISMSGHEAD();
		String date=DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(returnHead.getReturnCode()+date);
		iSMSGHEAD.setSystemid("WMS");
		mtCWWMSDOPOSTREQ.setISMSGHEAD(iSMSGHEAD);

		/****************************************
		 * 	IT_LIKP
		 * ************************************/
		List<ITLIKP> IT_LIKP = new ArrayList<ITLIKP>();
		ITLIKP iTLIKP= new ITLIKP();
		iTLIKP.setZFLAG("UP");
		iTLIKP.setVBELN(returnHead.getReferReceiptCode());
		String date1=DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		iTLIKP.setLFDAT(date1);
		iTLIKP.setKODAT(date1);
		iTLIKP.setTDDAT(date1);
		iTLIKP.setLDDAT(date1);
		iTLIKP.setWADAT(date1);
		IT_LIKP.add(iTLIKP);
		mtCWWMSDOPOSTREQ.setItlikp(IT_LIKP);

		/****************************************
		 * 	IT_LIPS
		 * ************************************/
		List<ITLIPS> IT_LIPS = new ArrayList<ITLIPS>();
		for(int i=0;i<items.size();i++) {
			BizStockOutputReturnItem item = items.get(i);
			ITLIPS iTLIPS = new ITLIPS();
			iTLIPS.setZFLAG("UP");
			iTLIPS.setVBELN(item.getReferReceiptCode());
			iTLIPS.setPOSNR(item.getReferReceiptRid());
			iTLIPS.setWERKS(dictionaryService.getFtyCodeByFtyId(item.getFtyId()));
			iTLIPS.setLGORT(dictionaryService.getLocCodeByLocId(item.getLocationId()));
			iTLIPS.setCHARG(item.getErpBatch());
			IT_LIPS.add(iTLIPS);
		}
		mtCWWMSDOPOSTREQ.setItlips(IT_LIPS);

		ErpReturn erpReturn = cwErpWsImpl.outputPost(mtCWWMSDOPOSTREQ);
		sapReturn.put("mat_doc_code", erpReturn.getMatDocCode());
		sapReturn.put("message", erpReturn.getMessage());
		sapReturn.put("type", erpReturn.getType());
		return sapReturn;
	}

	/**
	 * sap 销售出库过账 多库存地点或多erp批次
	 * @param headVo
	 * @return
	 * @throws Exception
     */
	private Map<String,Object> saveSapSaleByMoreLocation(BizStockOutputReturnHead returnHead)throws  Exception{
		Map<String, Object> sapReturn = new HashMap<String,Object>();
		List<BizStockOutputReturnItem> items = returnHead.getItemList();
		DTCWWMSDOPOSTREQ mtCWWMSDOPOSTREQ = new DTCWWMSDOPOSTREQ();

		/****************************************
		 * 	IS_MSG_HEAD
		 * ************************************/
		ISMSGHEAD iSMSGHEAD = new ISMSGHEAD();
		String date=DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(returnHead.getReturnCode()+date);
		iSMSGHEAD.setSystemid("WMS");
		mtCWWMSDOPOSTREQ.setISMSGHEAD(iSMSGHEAD);

		/****************************************
		 * 	IS_SPLIT_BATCH
		 * ************************************/
		List<ISSPLITBATCH> IS_SPLIT_BATCH = new ArrayList<ISSPLITBATCH>();
		for(BizStockOutputReturnItem item : items) {
			ISSPLITBATCH iSSPLITBATCH = new ISSPLITBATCH();
			iSSPLITBATCH.setVBELN(item.getReferReceiptCode());
			iSSPLITBATCH.setPOSNR(item.getReferReceiptRid());
			/****************************************
			 * 	ZLT_SD_BATCH_CONTENT
			 * ************************************/
			List<ZLTSDBATCHCONTENT> ZLT_SD_BATCH_CONTENT =  new ArrayList<ZLTSDBATCHCONTENT>();
			ZLTSDBATCHCONTENT zLTSDBATCHCONTENT = new ZLTSDBATCHCONTENT();
			zLTSDBATCHCONTENT.setDOCNUM(returnHead.getReturnCode());
			zLTSDBATCHCONTENT.setDOCITEMNUM(item.getReturnRid().toString());
			zLTSDBATCHCONTENT.setLFIMG(item.getQty()+"");
			zLTSDBATCHCONTENT.setLGORT(dictionaryService.getLocCodeByLocId(item.getLocationId()));
			zLTSDBATCHCONTENT.setCHARG(item.getErpBatch());
			ZLT_SD_BATCH_CONTENT.add(zLTSDBATCHCONTENT);
			iSSPLITBATCH.setZltsdbatchcontent(ZLT_SD_BATCH_CONTENT);
			IS_SPLIT_BATCH.add(iSSPLITBATCH);
		}
		mtCWWMSDOPOSTREQ.setIssplitbatch(IS_SPLIT_BATCH);

		/****************************************
		 * 	IT_LIKP
		 * ************************************/
		List<ITLIKP> IT_LIKP = new ArrayList<ITLIKP>();
		ITLIKP iTLIKP= new ITLIKP();
		iTLIKP.setZFLAG("UP");
		iTLIKP.setVBELN(returnHead.getReferReceiptCode());
		String date1=DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		iTLIKP.setLFDAT(date1);
		iTLIKP.setKODAT(date1);
		iTLIKP.setTDDAT(date1);
		iTLIKP.setLDDAT(date1);
		iTLIKP.setWADAT(date1);
		IT_LIKP.add(iTLIKP);
		mtCWWMSDOPOSTREQ.setItlikp(IT_LIKP);

		/****************************************
		 * 	IT_LIPS
		 * ************************************/
		List<ITLIPS> IT_LIPS = new ArrayList<ITLIPS>();
		for(int i=0;i<items.size();i++) {
			BizStockOutputReturnItem item = items.get(i);
			ITLIPS iTLIPS = new ITLIPS();
			iTLIPS.setZFLAG("UP");
			iTLIPS.setVBELN(item.getReferReceiptCode());
			iTLIPS.setPOSNR(item.getReferReceiptRid());
			//iTLIPS.setWERKS(item.getFtyCode());
			//iTLIPS.setLGORT(item.getLocationCode());
			//iTLIPS.setCHARG(item.getErpBatch());
			IT_LIPS.add(iTLIPS);
		}
		mtCWWMSDOPOSTREQ.setItlips(IT_LIPS);

		ErpReturn erpReturn = cwErpWsImpl.outputPost(mtCWWMSDOPOSTREQ);
		sapReturn.put("mat_doc_code", erpReturn.getMatDocCode());
		sapReturn.put("message", erpReturn.getMessage());
		sapReturn.put("type", erpReturn.getType());
		return sapReturn;
	}

	
	private BizStockOutputReturnHead getReturnAllInfo(int returnId) {
		BizStockOutputReturnHead returnHead = returnHeadDao.selectByPrimaryKey(returnId);
		List<BizStockOutputReturnItem> itemList = returnItemDao.selectItemsByReturnId(returnId);
		for (BizStockOutputReturnItem returnItem : itemList) {
			BizStockOutputReturnPosition returnPosition = new BizStockOutputReturnPosition();
			returnPosition.setReturnId(returnItem.getReturnId());
			returnPosition.setReturnRid(returnItem.getReturnRid());
			List<BizStockOutputReturnPosition> positionList = returnPositionDao
					.selectPositionListByIdAndRid(returnPosition);
			returnItem.setPositionList(positionList);
		}
		returnHead.setItemList(itemList);
		return returnHead;
	}

	// sap过账后更新表数据
	@Override
	public String postOrderToFinish(Map<String, Object> sapResult, int returnId, String userId) throws Exception {
		// result.put("mat_doc_code", erpReturn.getMatDocCode());
		// result.put("message", erpReturn.getMessage());
		// result.put("type", erpReturn.getType());
		// result.put("return_id", returnHead.getReturnId());

		if ("S".equals(sapResult.get("type"))) {
			BizStockOutputReturnHead returnHead = returnHeadDao.selectByPrimaryKey(returnId);
			List<BizStockOutputReturnItem> itemList = returnItemDao.selectItemsByReturnId(returnId);

			BizStockTaskItemCw ti = new BizStockTaskItemCw();
			ti.setReferReceiptId(returnId);
			ti.setReferReceiptType(returnHead.getReceiptType());
			List<BizStockTaskItemCw> taskItemList = stockTaskItemCwDao.selectByReferReceiptIdAndType(ti);
			BizStockTaskPositionCw tp = new BizStockTaskPositionCw();
			tp.setReferReceiptId(returnId);
			tp.setReferReceiptType(returnHead.getReceiptType());
			List<BizStockTaskPositionCw> positionList = stockTaskPositionCwDao.selectByReferReceiptIdAndType(tp);
			// 添加批次库存
			addStockBatch(returnHead, itemList);
			// 修改入库日期
			modifyBatchMaterialInfo(returnHead);
			// 添加仓位库存 根据已完成的作业单入库修改仓位库存
			commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);//根据作业单 及 所有positionList
			// 更改入库单状态
			BizStockOutputReturnHead record = new BizStockOutputReturnHead();
			record.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_FINISH.getValue());
			record.setReturnId(returnId);
			returnHeadDao.updateByPrimaryKeySelective(record);

			// 更改上架作业状态
			Map<String, Object> recordMap = new HashMap<String, Object>();
			recordMap.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
			recordMap.put("referReceiptId", returnHead.getReturnId());
			recordMap.put("referReceiptType", returnHead.getReceiptType());
			stockTaskHeadCwDao.updateStatusByReferReceiptIdAndType(recordMap);

			//过账成功生成历史记录			
			this.addReturnHistory(returnId);			
			
			// 更新退库item表的物料凭证
			Map<String , Object> map = new HashMap<>();
			map.put("return_id", returnId);
			map.put("mat_doc_code", sapResult.get("mat_doc_code"));
			returnItemDao.updateMesDocCodeByReturnId(map);
			
			return UtilObject.getStringOrEmpty(sapResult.get("message"));
		} else {
			// 更改入库单状态
			BizStockOutputReturnHead record = new BizStockOutputReturnHead();
			record.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_UNFINISH.getValue());
			record.setReturnId(returnId);
			returnHeadDao.updateByPrimaryKeySelective(record);
			throw new InterfaceCallException(UtilObject.getStringOrEmpty(sapResult.get("message")));
		}

	}

	void addStockBatch(BizStockOutputReturnHead returnHead, List<BizStockOutputReturnItem> itemList) throws Exception {

		if (itemList != null && itemList.size() > 0) {
			for (BizStockOutputReturnItem item : itemList) {
				BizStockOutputReturnPosition position = new BizStockOutputReturnPosition();
				position.setReturnId(item.getReturnId());
				position.setReturnRid(item.getReturnRid());
				List<BizStockOutputReturnPosition> positionList = returnPositionDao
						.selectPositionListByIdAndRid(position);
				if (positionList != null && positionList.size() > 0) {
					for (BizStockOutputReturnPosition returnPosition : positionList) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("matId", item.getMatId());
						map.put("locationId", item.getLocationId());
						map.put("batch", returnPosition.getBatch());
						map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
						map.put("createUserId", returnHead.getCreateUser());
						map.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
						map.put("qty", returnPosition.getQty());
						commonService.modifyStockBatch(map);
					}
				} else {
					throw new WMSException("缺少仓位信息");
				}

			}
		} else {
			throw new WMSException("缺少行项目");
		}

	}
	
	void reduceStockBatch(BizStockOutputReturnHead returnHead, List<BizStockOutputReturnItem> itemList) throws Exception {

		if (itemList != null && itemList.size() > 0) {
			for (BizStockOutputReturnItem item : itemList) {
				BizStockOutputReturnPosition position = new BizStockOutputReturnPosition();
				position.setReturnId(item.getReturnId());
				position.setReturnRid(item.getReturnRid());
				List<BizStockOutputReturnPosition> positionList = returnPositionDao
						.selectPositionListByIdAndRid(position);
				if (positionList != null && positionList.size() > 0) {
					for (BizStockOutputReturnPosition returnPosition : positionList) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("matId", item.getMatId());
						map.put("locationId", item.getLocationId());
						map.put("batch", returnPosition.getBatch());
						map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
						map.put("createUserId", returnHead.getCreateUser());
						map.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
						map.put("qty", returnPosition.getQty());
						commonService.modifyStockBatch(map);
					}
				} else {
					throw new WMSException("缺少仓位信息");
				}

			}
		} else {
			throw new WMSException("缺少行项目");
		}

	}

	private void modifyBatchMaterialInfo(BizStockOutputReturnHead head) {
		BizStockOutputReturnHead returnHead = getReturnAllInfo(head.getReturnId());
		Date createTime = new Date();
		if (returnHead.getItemList() != null && returnHead.getItemList().size() > 0) {

			for (BizStockOutputReturnItem item : returnHead.getItemList()) {

				if (item.getPositionList() != null && item.getPositionList().size() > 0) {
					for (BizStockOutputReturnPosition position : item.getPositionList()) {

						// 批次信息
						BatchMaterial batchMaterial = new BatchMaterial();
						batchMaterial.setBatch(position.getBatch());
						batchMaterial.setMatId(item.getMatId());
						batchMaterial.setFtyId(item.getFtyId());
						batchMaterial.setCreateTime(createTime);
						batchMaterialDao.updateByUniqueKeySelective(batchMaterial);

					}
				}
			}
		}

	}

	@Override
	public Map<String, Object> writeoffOrderToSap(Integer returnId, String userId) throws Exception {
		BizStockOutputReturnHead  head = returnHeadDao.selectByPrimaryKey(returnId);
		Map<String, Object> result = new HashMap<String,Object>();
		DTCWWMSDOREVERSEREQ mtCWWMSDOREVERSERE =  new DTCWWMSDOREVERSEREQ();

		/***************************************************************
		 * 		IS_MSG_HEAD
		 **************************************************************/
		MSGHEAD iSMSGHEAD = new MSGHEAD();
		iSMSGHEAD.setSYSTEMID("WMS");
		String date= DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(head.getReturnCode()+date);
		mtCWWMSDOREVERSERE.setISMSGHEAD(iSMSGHEAD);

		/***************************************************************
		 * 		IT_DATA
		 **************************************************************/
		List<ITDATA> IT_DATA = new ArrayList<ITDATA>();
		ITDATA iTDATA = new ITDATA();
		iTDATA.setVBELN(head.getReferReceiptCode());
		iTDATA.setIBUDAT(UtilString.getShortStringForDate(head.getCreateTime()));
		IT_DATA.add(iTDATA);
		mtCWWMSDOREVERSERE.setItdata(IT_DATA);
		ErpReturn erpReturn = cwErpWsImpl.outputWriteOff(mtCWWMSDOREVERSERE);
		result.put("mat_write_off_code", erpReturn.getMatDocCode());
		result.put("message", erpReturn.getMessage());
		result.put("type", erpReturn.getType());
		return result;
	}

	@Override
	public String writeoffOrderToFinish(Map<String, Object> sapResult, int returnId, String userId) throws Exception {

		if ("S".equals(sapResult.get("type"))) {
			BizStockOutputReturnHead returnHead = returnHeadDao.selectByPrimaryKey(returnId);
			List<BizStockOutputReturnItem> itemList = returnItemDao.selectItemsByReturnId(returnId);
			
			// 更改退库单状态为已冲销
			returnHead.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_WRITEOFF.getValue());
			returnHeadDao.updateByPrimaryKeySelective(returnHead);
			
			BizStockOutputReturnItem item = new BizStockOutputReturnItem();
			item.setReturnId(returnId);
			item.setWriteOff(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_WRITEOFF.getValue());
			returnItemDao.updateForMatDocIdAndMatDocRid(item);	
			
			//根据下架请求行itemList , 上架单positionList 修改仓位库存
			BizStockTaskPositionCw tp = new BizStockTaskPositionCw();
			tp.setReferReceiptId(returnId);
			tp.setReferReceiptType(returnHead.getReceiptType());
			List<BizStockTaskPositionCw> positionList = stockTaskPositionCwDao.selectByReferReceiptIdAndType(tp);
			
			// 生成下架作业请求
			BizStockOutputReturnHead head = this.getReturnAllInfo(returnId);
			this.addStockTaskReqHeadAndItem(head,userId);
			Map<String, Object> param = new HashMap<>();
			param.put("referReceiptId", returnId);
			param.put("referReceiptCode", returnHead.getReturnCode());
			param.put("referReceiptType", returnHead.getReceiptType());
			param.put("receiptType", EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
			
			List<BizStockTaskReqHead>  reqHeads = StockTaskReqHeadDao.getBizStockTaskReqHeadListByReferReipt(param);
			List<BizStockTaskReqItem> taskReqItemList = null;
			if (reqHeads != null && reqHeads.size()>0) {
				int stockTaskReqId = reqHeads.get(0).getStockTaskReqId();
				taskReqItemList = StockTaskReqItemDao.selectItemById(stockTaskReqId);
			}else {
				throw new WMSException("找不到作业请求");
			}			
			commonService.updateStockPositionByTaskReq(EnumReceiptType.STOCK_TASK_REMOVAL.getValue(), taskReqItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);

			// 更改批次库存
			reduceStockBatch(returnHead, itemList);
			
			// 更新退库item表的物料凭证
			Map<String , Object> map = new HashMap<>();
			map.put("return_id", returnId);
			map.put("mat_write_off_code", sapResult.get("mat_write_off_code"));
			returnItemDao.updateMesDocCodeByReturnId(map);

			//冲销成功生成历史记录			
			this.addReturnHistory(returnId);
		}
		return null;
	}

	@Override
	public boolean canUseForDelivery(String condition) throws Exception {
		Map<String, Object> map = returnHeadDao.selectCountForDelivery(condition);
		int count = UtilObject.getIntOrZero(map.get("cnt"));
		if (count>0) {
			throw new WMSException ("此交货单已被使用");
		}else {
			return true;
		}
		
	}

	@Override
	public void saveReturnToMes(Map<String, Object> sapReturn, String userId) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(UtilObject.getByteOrNull(sapReturn.get("syn_type")) == EnumSynType.MES_SAP_SYN.getValue()) {
			try {
				int returnId = UtilObject.getIntOrZero(sapReturn.get("return_id"));
				BizStockOutputReturnHead head = this.getReturnAllInfo(returnId);
				param.put("refer_receipt_id", head.getReturnId());
				param.put("refer_receipt_code", head.getReturnCode());
				param.put("refer_receipt_type", head.getReceiptType());
				param.put("business_type", "106");
				WmIopRetVal val = this.saveMesReturn(head, "106");
				if(val.getValue()!=null&&val.getValue().equals("")) {
					param = new HashMap<String,Object>();
					param.put("return_id", UtilObject.getIntOrZero(sapReturn.get("return_id")));
					param.put("mes_doc_code", UtilObject.getStringOrEmpty(val.getValue()));
					//保存mes过账物料凭证
					returnItemDao.updateMesDocCodeByReturnId(param);
				}else {
					param.put("error_info",val.getErrmsg());
					this.saveFailMes(param,userId);
				}
			} catch (Exception e) {
				param.put("error_info", e.getMessage());
				try {
					this.saveFailMes(param,userId);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 保存mes同步失败记录
	 * @param param
	 * @param userId
	 */
	private void saveFailMes(Map<String,Object> param,String userId)throws Exception{
		BizFailMes mes = new BizFailMes();
		mes.setReferReceiptId(UtilObject.getIntOrZero(param.get("refer_receipt_id")));
		mes.setReferReceiptCode(UtilObject.getStringOrEmpty(param.get("refer_receipt_code")));
		mes.setReferReceiptType(UtilObject.getByteOrNull(param.get("refer_receipt_type")));
		mes.setBusinessType(UtilObject.getStringOrEmpty(param.get("business_type")));
		mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
		mes.setCreateTime(new Date());
		mes.setModifyTime(new Date());
		mes.setCreateUser(userId);
		mes.setModifyUser(userId);
		mes.setErrorInfo(UtilObject.getStringOrEmpty(param.get("error_info")));
		failMesDao.insert(mes);
	}
	
	/**
	 * 出库mes同步
	 * @param head
	 * @param opeTypeCode
	 * @return
	 * @throws Exception
	 */
	private WmIopRetVal saveMesReturn(BizStockOutputReturnHead head,String opeTypeCode) throws Exception{
		String credentialCode = "".equals(head.getReferReceiptCode())?head.getReturnCode():head.getReferReceiptCode();
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		List<BizStockOutputReturnItem> items = head.getItemList();		
		
		List<BizStockOutputReturnPosition> positionList = this.dataFormat(items);
		for(int j=0;j<positionList.size();j++){
			BizStockOutputReturnPosition position = positionList.get(j);
			WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
			String matCode = dicMaterialDao.selectByPrimaryKey(position.getMatId()).getMatCode();
			dto.setRecNum(position.getReturnPositionId());
			dto.setSrMtrlCode(matCode);
			dto.setTgMtrlCode(matCode);
			
			DicPackageType packageType = packageTypeDao.selectByPrimaryKey(position.getPackageTypeId());
			double wgtperpack = packageType.getPackageStandard().doubleValue();
			dto.setWgtPerPack(wgtperpack);
			dto.setAmnt(position.getQty().divide(new BigDecimal(wgtperpack)).intValue());
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mes_unit_code", "吨");
			param.put("wms_unit_code", dictionaryService.getUnitCodeByUnitId(position.getUnitId()));
			String rel=this.getUnitRelMesAndWms(param);
			dto.setWgt(position.getQty().divide(new BigDecimal(rel)).doubleValue());
			dto.setSrBch(position.getErpBatch());
			dto.setTgBch(position.getErpBatch());
			
			dto.setSrRankCode(EnumRankLevel.getRankByValue(position.getErpBatch()));
			dto.setTgRankCode(EnumRankLevel.getRankByValue(position.getErpBatch()));
			DicStockLocation location = locationDao.selectByPrimaryKey(position.getLocationId());
			dto.setSrNodeCode(location.getNodeCode());
			dto.setTgNodeCode(location.getNodeCode());

			dto.setDlvBillCode(credentialCode);
			dto.setCustomer(head.getCustomerName());
			dto.setTransTypeCode("1");
			dto.setVehiCode("1001");
			dto.setDes(head.getReturnCode());
			dto.setWgtDim("TO");
			wmMvRecInteropeDto.add(dto);
		}
		
		
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		Map<String,Object> classType = classTypeDao.selectCurrentClassName(head.getClassTypeId());
		String className = UtilString.getStringOrEmptyForObject(classType.get("class_name"));
		WmIopRetVal val =CwMesWsImpl.updateMesStock(credentialCode, opeTypeCode, getBeginDate(className), getEndDate(className), dtoSet);
		return val;
	}
	
	
	/**
	 * 按包装类型、ERP批次、库存地点汇总
	 * @param positionList
	 * @return
	 */
	private List<BizStockOutputReturnPosition> dataFormat(List<BizStockOutputReturnItem> items){
		List<BizStockOutputReturnPosition> result = new ArrayList<BizStockOutputReturnPosition>();
		Map<String,BizStockOutputReturnPosition> data = new HashMap<String,BizStockOutputReturnPosition>();
		
		for(BizStockOutputReturnItem item : items) {
			for(BizStockOutputReturnPosition positionVo : item.getPositionList()) {
				DicPackageType pack = packageTypeDao.selectByPrimaryKey(positionVo.getPackageTypeId());
				String key = item.getMatId()+"-"+pack.getPackageStandard()+"-"+positionVo.getErpBatch()+"-"+item.getLocationId();
				positionVo.setLocationId(item.getLocationId());
				positionVo.setMatId(item.getMatId());
				positionVo.setUnitId(item.getUnitId());				
				if(data.containsKey(key)) {
					BizStockOutputReturnPosition position = data.get(key);
					position.setQty(position.getQty().add(positionVo.getQty()));
					data.put(key, position);
				}else {
					data.put(key, positionVo);
				}
			}
			Iterator<Entry<String, BizStockOutputReturnPosition>> it = data.entrySet().iterator();
			while(it.hasNext()) {
				result.add(it.next().getValue());
			}
			
		}				
		return result;
	}
	
	
	
	
	private String getUnitRelMesAndWms(Map<String, Object> param) {
		return stockInputHeadDao.getUnitRelMesAndWms(param);
	}
	
	private String getBeginDate(String classTypeName) {
		classTypeName = classTypeName.substring(2, classTypeName.length());
		return classTypeName.replace("(", "").replace(")", "").split("-")[0];
	}
	
	private String getEndDate(String classTypeName) {
		classTypeName = classTypeName.substring(2, classTypeName.length());
		return classTypeName.replace("(", "").replace(")", "").split("-")[1];
	}

	@Override
	public String deleteReturnOrder(int returnId, String userId) throws Exception {
		//删除作业单
		BizStockOutputReturnHead returnHead = new BizStockOutputReturnHead();
		returnHead = returnHeadDao.selectByPrimaryKey(returnId);		
		taskService.deleteTaskReqAndTaskByReferIdAndType(returnId, returnHead.getReceiptType());
		
		//删除退库单
		returnHead.setIsDelete((byte)1);
		returnHeadDao.updateByPrimaryKey(returnHead);		
		returnItemDao.updateItemsToDelete(returnId);
		returnPositionDao.updatePositionsToDelete(returnId);
		
		return "删除成功";
	}

	@Override
	public String writeoffReturnToMes(int returnId, String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String result = null;
		BizStockOutputReturnHead head;
		try {
			head = this.getReturnAllInfo(returnId);
			if (head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()) {
				try {
					param.put("refer_receipt_id", head.getReturnId());
					param.put("refer_receipt_code", head.getReturnCode());
					param.put("refer_receipt_type", head.getReceiptType());
					param.put("business_type", "202");
					List<BizStockOutputReturnItem> itemVos = head.getItemList();
					if(itemVos.get(0).getMesDocCode()!=null && !itemVos.get(0).getMesDocCode().equals("")) {
						result =UtilObject.getStringOrNull(CwMesWsImpl.CancelBookedBillByBillCode(itemVos.get(0).getMesDocCode()));
						if (result != null && result.length() == 16) {
							param = new HashMap<String, Object>();
							param.put("return_id", head.getStockOutputId());
							param.put("mes_write_off_code",result);
							//保存mes冲销物料凭证
							returnItemDao.updateMesDocCodeByReturnId(param);
						} else {
							param.put("error_info", result);
							this.saveFailMes(param, userId);
						}
					}else {
						throw new WMSException("MES还未过账无法冲销");
					}
				} catch (Exception e) {
					param.put("error_info", e.getMessage());
					e.printStackTrace();
					try {
						this.saveFailMes(param, userId);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return result;
		
	}

	@Override
	public JSONObject saveAndwriteoffToMes(int returnId, String userId) {
		JSONObject returnObj = new JSONObject();
		Map<String, Object> param = new HashMap<String, Object>();
		String result = null;
		BizStockOutputReturnHead head;
		try {
			head = this.getReturnAllInfo(returnId);
			if (head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()) {
				try {
					param.put("refer_receipt_id", head.getReturnId());
					param.put("refer_receipt_code", head.getReturnCode());
					param.put("refer_receipt_type", head.getReceiptType());
					param.put("business_type", "202");
					List<BizStockOutputReturnItem> itemVos = head.getItemList();
					if(itemVos.get(0).getMesDocCode()!=null && !itemVos.get(0).getMesDocCode().equals("")) {
						result =UtilObject.getStringOrNull(CwMesWsImpl.CancelBookedBillByBillCode(itemVos.get(0).getMesDocCode()));
						if (result != null && result.length() == 16) {
							param = new HashMap<String, Object>();
							param.put("return_id", returnId);
							param.put("mes_write_off_code",result);
							returnItemDao.updateMesDocCodeByReturnId(param);
							returnObj.put("mesInfo", "成功");
						} else {
							param.put("error_info", result);
							this.saveFailMes(param, userId);
							returnObj.put("mesInfo", result);
						}						
					}else if (head.getStatus() != EnumReceiptStatus.RECEIPT_WRITEOFF.getValue()) {
						WmIopRetVal val = this.saveMesReturn(head, "106");
						if(val.getValue()!=null&& !val.getValue().equals("")) {
							param = new HashMap<String,Object>();
							param.put("return_id", returnId);
							param.put("mes_doc_code", UtilObject.getStringOrEmpty(val.getValue()));
							returnItemDao.updateMesDocCodeByReturnId(param);
							returnObj.put("mesInfo", "成功");
						}else {
							param.put("error_info",val.getErrmsg());
							this.saveFailMes(param,userId);
							returnObj.put("mesInfo", val.getErrmsg());
						}						
					}else {
						returnObj.put("mesInfo", "MES还未过账无法冲销");
						throw new WMSException("MES还未过账无法冲销");						
					}
				} catch (Exception e) {
					param.put("error_info", e.getMessage());
					returnObj.put("mesInfo", "服务器异常");
					e.printStackTrace();
					try {
						this.saveFailMes(param, userId);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return returnObj;
	}

}
