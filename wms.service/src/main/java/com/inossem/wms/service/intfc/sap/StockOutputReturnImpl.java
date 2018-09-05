package com.inossem.wms.service.intfc.sap;

import java.math.BigDecimal;
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
import org.springframework.util.StringUtils;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.dao.biz.BizMatReqHeadMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.biz.BizDeliveryOrderHead;
import com.inossem.wms.model.biz.BizDeliveryOrderItem;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.biz.BizReserveOrderItem;
import com.inossem.wms.model.biz.BizSaleOrderHead;
import com.inossem.wms.model.biz.BizSaleOrderItem;
import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.biz.BizStockOutputReturnItem;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IStockOutputReturn;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("stockOutputReturnSap")
public class StockOutputReturnImpl implements IStockOutputReturn {

	private static final Logger logger = LoggerFactory.getLogger(StockOutputReturnImpl.class);
	@Autowired
	private UserMapper userDao;
	
	@Autowired
	private DicFactoryMapper factoryDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private DicStockLocationMapper locationDao;

	@Autowired
	private DicUnitMapper dicUnitDao;

	@Autowired
	private BizMatReqHeadMapper matReqHeadDao;

	@Autowired
	private DicMaterialMapper dicMaterialDao;

	@Override
	public JSONObject getSaleOrderListFromSAP(Map<String, Object> map) throws Exception {
		String saleNumber = UtilString.getStringOrEmptyForObject(map.get("referReceiptCode"));// 销售单号
		String contractNumber = UtilString.getStringOrEmptyForObject(map.get("preorderId"));// 合同号
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();
		I_IMPORT.put("ZTYPE", "10"); // 接口类型
		I_IMPORT.put("ZERNAM", ""); // 用户id
		I_IMPORT.put("ZIMENO", ""); // 设备IMENO
		I_IMPORT.put("ZDATE", ""); // 传输日期
		I_IMPORT.put("ZTIME", ""); // 传输时间

		JSONArray I_EBELN = new JSONArray();
		if (StringUtils.hasText(saleNumber)) {
			saleNumber = saleNumber.trim();
			while (saleNumber.length() < 10) { // 不够十位数字，自动补0
				saleNumber = "0" + saleNumber;
			}
			JSONObject ebelnObj = new JSONObject();
			ebelnObj.put("SIGN", "I");
			ebelnObj.put("OPTION", "EQ");
			ebelnObj.put("LOW", saleNumber);
			I_EBELN.add(ebelnObj);
			params.put("I_VBELN", I_EBELN);
		}
		// JSONArray I_BSTNK = new JSONArray();
		if (StringUtils.hasText(contractNumber)) {
			JSONObject bstnkObj = new JSONObject();
			bstnkObj.put("SIGN", "I");
			bstnkObj.put("OPTION", "EQ");
			bstnkObj.put("LOW", contractNumber.trim());
			I_EBELN.add(bstnkObj);
			params.put("I_BSTNK", I_EBELN);
		}

		params.put("I_IMPORT", I_IMPORT);

		JSONObject returnObj = new JSONObject();
		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_10?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		} catch (Exception e) {
			logger.error("", e);
		}
		return returnObj;
	}

	@Override
	public List<BizSaleOrderHead> getSaleHeadListFromSAP(Map<String, Object> map) throws Exception {
		List<BizSaleOrderHead> contractList = new ArrayList<BizSaleOrderHead>();
		JSONObject returnObj = getSaleOrderListFromSAP(map);
		JSONArray contractAry = returnObj.getJSONArray("I_HEAD");

		if (contractAry != null) {
			for (int i = 0; i < contractAry.size(); i++) {
				JSONObject contractObj = (JSONObject) contractAry.get(i);
				BizSaleOrderHead orderHead = new BizSaleOrderHead();

				orderHead.setCorpCode(contractObj.getString("BUKRS")); // 公司代码
				orderHead.setCorpName(contractObj.getString("BUTXT")); // 公司名称/客户名称
				orderHead.setReferReceiptCode(contractObj.getString("VBELN")); // 销售订单编号
				orderHead.setPreorderId(contractObj.getString("BSTNK"));// 合同号
				orderHead.setCorpCode(contractObj.getString("KUNNR"));// 客户编码
				orderHead.setSupplierName(contractObj.getString("NAME1"));// 供应商名称
				orderHead.setOrderType(contractObj.getString("AUART"));// 订单类型
				orderHead.setOrderTypeName(contractObj.getString("BEZEI"));// 订单类型名称
				// orderHead.setSupplierName(contractObj.getString("BUTXT"));
				orderHead.setSaleOrgCode(contractObj.getString("VKORG"));// 销售组织
				orderHead.setSaleOrgName(contractObj.getString("VTEXT"));// 销售组织名称
				orderHead.setSaleGroupName(contractObj.getString("VKGRP"));// 销售组
				orderHead.setCreateTime(contractObj.getString("ERDAT"));// 创建日期

				contractList.add(orderHead);
			}

		}
		return contractList;
	}

	@Override
	public List<BizSaleOrderItem> getSaleItemListFromSAP(Map<String, Object> map) throws Exception {
		JSONObject returnObj = getSaleOrderListFromSAP(map);
		JSONArray itemAry = returnObj.getJSONArray("I_ITEM");
		List<BizSaleOrderItem> itemList = new ArrayList<BizSaleOrderItem>();

		if (itemAry != null) {

			for (int i = 0; i < itemAry.size(); i++) {
				JSONObject itemTmp = (JSONObject) itemAry.get(i);
				BizSaleOrderItem bizSaleOrderItem = new BizSaleOrderItem();

				bizSaleOrderItem.setSaleOrderCode(itemTmp.getString("VBELN"));// 销售订单号
				bizSaleOrderItem.setBatchSpecCode(itemTmp.getString("ZTSKC"));
				bizSaleOrderItem.setBatchSpecType(itemTmp.getString("SOBKZ"));
				bizSaleOrderItem.setBatchSpecValue(itemTmp.getString("ZTSMS"));
				bizSaleOrderItem.setDecimalPlace(itemTmp.getString("ANDEC"));

				String ftyCode = itemTmp.getString("WERKS");
				bizSaleOrderItem.setFtyCode(itemTmp.getString("WERKS"));
				bizSaleOrderItem.setFtyId(dictionaryService.getFtyIdByFtyCode(ftyCode));
				bizSaleOrderItem.setFtyName(dictionaryService.getFtyCodeMap().get(ftyCode).getFtyName());

				String locationCode = itemTmp.getString("LGORT");
				int locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
				bizSaleOrderItem.setLocationCode(locationCode);
				bizSaleOrderItem.setLocationId(locationId);
				bizSaleOrderItem.setLocationName(locationDao.selectByPrimaryKey(locationId).getLocationName());

				String matCode = itemTmp.getString("MATNR");
				bizSaleOrderItem.setMatCode(matCode);
				bizSaleOrderItem.setMatId(dictionaryService.getMatIdByMatCode(matCode));
				bizSaleOrderItem.setMatName(itemTmp.getString("ARKTX"));

				bizSaleOrderItem.setOrderQty(itemTmp.getString("ZMENG"));
				bizSaleOrderItem.setSaleRid(itemTmp.getString("POSNR"));
				bizSaleOrderItem.setHaveReturnQty(itemTmp.getString("ZFHSL"));
				bizSaleOrderItem.setUnitEn(itemTmp.getString("MEINS"));
				bizSaleOrderItem.setUnitName(itemTmp.getString("MSEHL"));
				bizSaleOrderItem.setUnitId(dictionaryService.getUnitIdByUnitCode(itemTmp.getString("MEINS")));

				itemList.add(bizSaleOrderItem);
			}
		}

		return itemList;
	}

	@Override
	public JSONObject getReserveOrderListFromSap(String receiptCode, String createUser) throws Exception {
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		I_IMPORT.put("ZTYPE", "08"); // 接口类型
		I_IMPORT.put("ZERNAM", createUser); // 用户id
		I_IMPORT.put("ZIMENO", ""); // 设备IMENO
		I_IMPORT.put("ZDATE", ""); // 传输日期
		I_IMPORT.put("ZTIME", ""); // 传输时间
		I_IMPORT.put("ZDJBH", ""); // 传入单据编号

		JSONArray I_RSNUM = new JSONArray();
		if (StringUtils.hasText(receiptCode)) {
			String code = receiptCode.trim();
			while (code.length() < 10) { // 不够十位数字，自动补0
				code = "0" + code;
			}
			JSONObject ebelnObj = new JSONObject();
			ebelnObj.put("SIGN", "I");
			ebelnObj.put("OPTION", "EQ");
			ebelnObj.put("LOW", code);
			I_RSNUM.add(ebelnObj);
			params.put("I_RSNUM", I_RSNUM);
		}

		params.put("I_IMPORT", I_IMPORT);

		JSONObject returnObj = new JSONObject();
		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_08?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		} catch (Exception e) {
			logger.error("", e);
		}
		return returnObj;
	}

	@Override
	public List<BizReserveOrderHead> getReserveHeadListFromSapReturnObj(JSONObject returnObj) throws Exception {
		List<BizReserveOrderHead> contractList = new ArrayList<BizReserveOrderHead>();
		// JSONObject returnObj = getReserveOrderListFromSAP(map);
		JSONArray contractAry = null;
		try {
			contractAry = returnObj.getJSONArray("E_HEAD");
		} catch (Exception e) {
			logger.error("", e);
		}
		if (contractAry != null) {
			for (int i = 0; i < contractAry.size(); i++) {
				JSONObject contractObj = (JSONObject) contractAry.get(i);
				BizReserveOrderHead bizReserveOrderHead = new BizReserveOrderHead();
				bizReserveOrderHead.setReferReceiptCode(contractObj.getString("RSNUM"));// 预留单号
				bizReserveOrderHead.setReserveCostObjCode(contractObj.getString("ZPOSID"));// 成本对象
				bizReserveOrderHead.setReserveCostObjName(contractObj.getString("ZPOST1"));// 成本对象描述
				bizReserveOrderHead.setReserveCreateTime(contractObj.getString("RSDAT"));// 创建日期
				bizReserveOrderHead.setReserveCreateUser(contractObj.getString("USNAM"));// 创建人
				contractList.add(bizReserveOrderHead);
			}
		}

		return contractList;
	}

	@Override
	public List<BizReserveOrderItem> getReserveItemListFromSapReturnObj(JSONObject returnObj) throws Exception {
		List<BizReserveOrderItem> itemList = new ArrayList<BizReserveOrderItem>();
		// JSONObject returnObj = getReserveOrderListFromSAP(map);
		JSONArray itemAry = null;

		try {
			itemAry = returnObj.getJSONArray("E_ITEM");
		} catch (Exception e) {
			logger.error("", e);
		}

		if (itemAry != null) {
			for (int i = 0; i < itemAry.size(); i++) {

				JSONObject itemTmp = (JSONObject) itemAry.get(i);
				BizReserveOrderItem bizReserveOrderItem = new BizReserveOrderItem();
				bizReserveOrderItem.setReferReceiptCode(itemTmp.getString("RSNUM"));// 预留号
				bizReserveOrderItem.setReferReceiptRid(itemTmp.getString("RSPOS"));// 预留号行项目

				String ftyCode = itemTmp.getString("WERKS");// 工厂
				bizReserveOrderItem.setFtyCode(itemTmp.getString("WERKS"));
				bizReserveOrderItem.setFtyId(dictionaryService.getFtyIdByFtyCode(ftyCode));
				bizReserveOrderItem.setFtyName(dictionaryService.getFtyCodeMap().get(ftyCode).getFtyName());

				String locationCode = itemTmp.getString("LGORT");// 库存地点
				int locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
				if (locationId <= 0) {
					throw new WMSException("库存地点无效");
				}
				bizReserveOrderItem.setLocationCode(locationCode);
				bizReserveOrderItem.setLocationId(locationId);
				// bizReserveOrderItem.setLocationName(locationDao.selectByPrimaryKey(locationId).getLocationName());
				bizReserveOrderItem
						.setLocationName(dictionaryService.getLocationIdMap().get(locationId).getLocationName());
				String matCode = itemTmp.getString("MATNR");// 物料
				bizReserveOrderItem.setMatCode(matCode);
				bizReserveOrderItem.setMatId(dictionaryService.getMatIdByMatCode(matCode));
				bizReserveOrderItem.setMatName(itemTmp.getString("MAKTX"));

				bizReserveOrderItem.setMoveTypeCode(itemTmp.getString("BWART"));// 预留移动类型
				bizReserveOrderItem.setSpecStockCode(itemTmp.getString("SOBKZ"));// 特殊库存标识
				bizReserveOrderItem.setUnitName(itemTmp.getString("MSEHL"));// 单位描述
				bizReserveOrderItem.setDemandQty(itemTmp.getString("BDMNG"));// 需求数量
				bizReserveOrderItem.setHaveReturnQty(itemTmp.getString("ENMNG"));// 已提货数量
				bizReserveOrderItem.setStockQty(itemTmp.getString("LBKUM"));// 总库存数量
				bizReserveOrderItem.setCanRreturnQty(itemTmp.getString("SQLAB")); // 总库存数量
				bizReserveOrderItem.setReserveCostObjCode(itemTmp.getString("ZPOSID"));// 成本对象
				bizReserveOrderItem.setReserveCostObjName(itemTmp.getString("ZPOST1"));// 成本对象描述

				// 计量单位小数位
				String meins = itemTmp.getString("MEINS"); // 单位code
				// DicUnit unit =
				// dicUnitDao.selectByPrimaryKey(dictionaryService.getUnitIdByUnitCode(meins));
				DicUnit unit = dictionaryService.getUnitCodeMap().get(meins);
				bizReserveOrderItem.setDecimalPlace(unit.getDecimalPlace());
				bizReserveOrderItem.setUnitId(unit.getUnitId());
				itemList.add(bizReserveOrderItem);
			}

		}

		return itemList;
	}

	@Override
	public Map<String, Object> submitToSapForMatreqForOneCorp(BizStockOutputReturnHead head,
			List<BizStockOutputReturnItem> items) throws Exception {
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("receipt_id", head.getReturnCode());
		// map.put("type", EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue());

		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		JSONObject returnObj = this.submitToSapForMatreq(head, items);
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONObject returnObject = new JSONObject();

		BizMaterialDocHead maDocHead = new BizMaterialDocHead();

		String isSuccess = returnObj.getString("RETURNVALUE");
		returnObject = returnObj.getJSONObject("RETURN");
		if ("S".equals(isSuccess)) {
			this.setBizMaterialDocHeadFromSAP(returnObj, maDocHead, head);
			JSONArray e_msegs = returnObj.getJSONArray("E_MSEG");
			List<BizMaterialDocItem> mDocItems = new ArrayList<BizMaterialDocItem>();
			for (int i = 0; i < e_msegs.size(); i++) {
				JSONObject e_mseg = e_msegs.getJSONObject(i);
				Integer returnRid = e_mseg.getInt("ZWMSXM");// 退库单行项目
				String returnCode = e_mseg.getString("ZWMSBH");
				BizMaterialDocItem mDocItem = new BizMaterialDocItem();
				BizStockOutputReturnItem returnItem = items.stream()
						.filter(z -> z.getReturnRid().equals(returnRid) && returnCode.equals(head.getReturnCode()))
						.findFirst().get();
				this.setBizMaterialDocItemFromSAP(e_mseg, mDocItem, returnItem, returnCode);

				// mDocItem.setBatchMaterialSpecList(returnItem.getBatchMaterialSpecList());
				// mDocItem.setBatchSpecList(returnItem.getBatchSpecList());
				mDocItem.setRecentBatch(returnItem.isRecentBatch());
				mDocItem.setReferBatch(returnItem.getBatchOutput());

				mDocItems.add(mDocItem);

			}
			maDocHead.setMaterialDocItemList(mDocItems);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = returnObject.getString("MESSAGE");

			returnMap.put("materialDocHead", maDocHead);
		} else {
			// errorCode
			// =EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			// errorString = returnObject.getString("MESSAGE");
			throw new InterfaceCallException(returnObject.getString("MESSAGE"));
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;
	}

	@Override
	public Map<String, Object> submitToSapForMatreqForTwoCorp(BizStockOutputReturnHead head,
			List<BizStockOutputReturnItem> items) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		JSONObject returnObj = this.submitToSapForMatreq(head, items);
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONObject returnObject = new JSONObject();

		String isSuccess = returnObj.getString("RETURNVALUE");
		returnObject = returnObj.getJSONObject("RETURN");
		if ("S".equals(isSuccess)) {
			Long matDocCode = Long.valueOf(returnObj.getString("MBLNR"));
			// 跨公司过账会返回两个物料凭证，4900580012 4900580013
			BizMaterialDocHead maDocHeadStar = new BizMaterialDocHead();// 物料凭证小的
			BizMaterialDocHead maDocHeadEnd = new BizMaterialDocHead();// 物料凭证大的
			BizMaterialDocHead maDocHead = new BizMaterialDocHead();

			JSONArray eMseg = returnObj.getJSONArray("E_MSEG");
			List<BizMaterialDocItem> mDocItemsStar = new ArrayList<BizMaterialDocItem>();// 物料凭证小的
			List<BizMaterialDocItem> mDocItemsEnd = new ArrayList<BizMaterialDocItem>();// 物料凭证大的
			List<BizMaterialDocItem> mDocItems = new ArrayList<BizMaterialDocItem>();// 所有物料凭证明细

			JSONArray eMsegStar = new JSONArray();
			JSONArray eMsegEnd = new JSONArray();
			JSONArray eMsegAll = new JSONArray();

			// 把物料凭证明细拆分为两个
			for (int i = 0; i < eMseg.size(); i++) {
				JSONObject object = (JSONObject) eMseg.get(i);
				Long matDocCodeInMseg = Long.valueOf(object.getString("MBLNR"));// 明细中的物料凭证号
				eMsegAll.add(object);
				if (matDocCodeInMseg.equals(matDocCode)) {
					eMsegStar.add(object);
				} else {
					eMsegEnd.add(object);
				}
			}

			this.setBizMaterialDocHeadFromSAPForTwoCorp(eMsegStar, maDocHeadStar, head);
			this.setBizMaterialDocHeadFromSAPForTwoCorp(eMsegEnd, maDocHeadEnd, head);

			// 组装小的物料凭证及明细
			for (Object objectStar : eMsegStar) {
				JSONObject object = (JSONObject) objectStar;
				Integer returnRid = object.getInt("ZWMSXM");// 退库单行项目
				String returnCode = object.getString("ZWMSBH");
				BizMaterialDocItem mDocItemStar = new BizMaterialDocItem();
				BizStockOutputReturnItem returnItem = items.stream()
						.filter(z -> z.getReturnRid().equals(returnRid) && returnCode.equals(head.getReturnCode()))
						.findFirst().get();
				this.setBizMaterialDocItemFromSAP(object, mDocItemStar, returnItem, returnCode);

				// mDocItemStar.setBatchMaterialSpecList(returnItem.getBatchMaterialSpecList());
				// mDocItemStar.setBatchSpecList(returnItem.getBatchSpecList());
				mDocItemStar.setRecentBatch(returnItem.isRecentBatch());
				mDocItemStar.setReferBatch(returnItem.getBatchOutput());

				mDocItemsStar.add(mDocItemStar);
				mDocItems.add(mDocItemStar);
			}
			maDocHeadStar.setMaterialDocItemList(mDocItemsStar);

			// 组装大的物料凭证及明细
			for (Object objectEnd : eMsegEnd) {
				JSONObject object = (JSONObject) objectEnd;
				Integer returnRid = object.getInt("ZWMSXM");// 退库单行项目
				String returnCode = object.getString("ZWMSBH");
				BizMaterialDocItem mDocItemEnd = new BizMaterialDocItem();
				BizStockOutputReturnItem returnItem = items.stream()
						.filter(z -> z.getReturnRid().equals(returnRid) && returnCode.equals(head.getReturnCode()))
						.findFirst().get();
				this.setBizMaterialDocItemFromSAP(object, mDocItemEnd, returnItem, returnCode);

				// mDocItemStar.setBatchMaterialSpecList(returnItem.getBatchMaterialSpecList());
				// mDocItemStar.setBatchSpecList(returnItem.getBatchSpecList());
				mDocItemEnd.setRecentBatch(returnItem.isRecentBatch());
				mDocItemEnd.setReferBatch(returnItem.getBatchOutput());

				mDocItemsEnd.add(mDocItemEnd);
				mDocItems.add(mDocItemEnd);
			}
			maDocHeadEnd.setMaterialDocItemList(mDocItemsEnd);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = returnObject.getString("MESSAGE");

			returnMap.put("materialDocHeadStart", maDocHeadStar);
			returnMap.put("materialDocHeadEnd", maDocHeadEnd);
			returnMap.put("eMsegAll", eMsegAll);
		} else {
			// errorCode =
			// EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			// errorString = returnObject.getString("MESSAGE");
			throw new InterfaceCallException(returnObject.getString("MESSAGE"));
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;
	}

	private JSONObject submitToSapForMatreq(BizStockOutputReturnHead head, List<BizStockOutputReturnItem> items)
			throws Exception {
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("receipt_id", head.getReturnCode());
		// map.put("type", EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue());
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();

		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		String ZDATE = formatterDate.format(date);
		String ZTIME = formatterTime.format(date);

		I_IMPORT.put("ZTYPE", "14");
		I_IMPORT.put("ZERNAM", head.getCurrentUserId());
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		I_IMPORT.put("ZDJBH", head.getReturnCode());

		JSONArray E_HEADs = new JSONArray();
		try {
			JSONObject E_HEAD = new JSONObject();
			E_HEAD.put("ZDJBH", head.getReturnCode());// 退库单号
			E_HEAD.put("DOC_DATE", head.getVoucherDate());// 凭证日期
			E_HEAD.put("PSTNG_DATE", head.getPostDate());// 过帐日期
			E_HEADs.add(E_HEAD);
		} catch (Exception e) {
			logger.error("", e);

		}
		JSONArray E_ITEMs = new JSONArray();
		try {
			for (BizStockOutputReturnItem item : items) {
				JSONObject E_ITEM = new JSONObject();
				BizMatReqHead matReqHead = matReqHeadDao
						.selectByPrimaryKey(Integer.parseInt(item.getReferReceiptCode()));
				String zlldh;// 领料单号
				if (matReqHead != null) {
					zlldh = matReqHead.getMatReqCode();
				} else {
					zlldh = "";
				}
				E_ITEM.put("ZDJBH", String.valueOf(head.getReturnCode()));// 退库单号
				E_ITEM.put("ZDJXM", String.valueOf(item.getReturnRid()));// 退库行项目
				E_ITEM.put("MATNR", dicMaterialDao.selectByPrimaryKey(item.getMatId()).getMatCode());// 物料编码
				// E_ITEM.put("WERKS", zmsegoTk.getWerks());// 工厂
				E_ITEM.put("WERKS", head.getWerkToSap());// 接收工厂code
				E_ITEM.put("STGE_LOC", dictionaryService.getLocCodeByLocId(item.getLocationId()));// 库存地点code
				E_ITEM.put("RSNUM", item.getReserveId());// 预留编号
				E_ITEM.put("RSPOS", item.getReserveRid());// 预留行项目
				E_ITEM.put("ENTRY_QNT", item.getQty());// 退库数量
				E_ITEM.put("EBELN", item.getPurchaseOrderCode());// 采购订单
				E_ITEM.put("EBELP", item.getPurchaseOrderRid());// 采购订单项目
				E_ITEM.put("BWART", dictionaryService.getMoveTypeCodeByMoveTypeId(item.getMoveTypeId()));// 移动类型
				E_ITEM.put("SOBKZ", "");// 特殊库存标识
				E_ITEM.put("ANLN1", "");// 资产卡片号
				E_ITEM.put("ZLLDH", zlldh);// 领料单号
				E_ITEM.put("ZLLDXH", item.getReferReceiptRid());// 领料单行项目
				E_ITEMs.add(E_ITEM);
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_TKID", head.getReturnCode());
		params.put("HEAD", E_HEADs);
		params.put("ITEM", E_ITEMs);
		JSONObject returnObj = null;
		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_14?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);

		} catch (Exception e) {
			logger.error("", e);
		}

		return returnObj;
	}

	private void setBizMaterialDocHeadFromSAPForTwoCorp(JSONArray e_msegs, BizMaterialDocHead materialDocHead,
			BizStockOutputReturnHead stockOutputReturnHead) {
		String mblnr = ((JSONObject) e_msegs.get(0)).getString("MBLNR");// 物料凭证号
		materialDocHead.setMatDocCode(mblnr);// 物料凭证号
		materialDocHead.setMatDocType(stockOutputReturnHead.getReceiptType()); // 业务类型

		// materialDocHead.setDocTime(stockOutputReturnHead.getDocTime()); //
		// 凭证日期
		// materialDocHead.setPostingDate(stockInputHead.getPostingDate());//
		// 过账日期

		materialDocHead.setCreateUser(stockOutputReturnHead.getCreateUser());

	}

	private void setBizMaterialDocHeadFromSAP(JSONObject json, BizMaterialDocHead materialDocHead,
			BizStockOutputReturnHead stockOutputReturnHead) {
		JSONArray e_msegs = json.getJSONArray("E_MSEG");
		String mblnr = ((JSONObject) e_msegs.get(0)).getString("MBLNR");// 物料凭证号
		materialDocHead.setMatDocCode(mblnr);// 物料凭证号
		materialDocHead.setMatDocType(stockOutputReturnHead.getReceiptType()); // 业务类型

		// materialDocHead.setDocTime(stockOutputReturnHead.getDocTime()); //
		// 凭证日期
		// materialDocHead.setPostingDate(stockInputHead.getPostingDate());//
		// 过账日期

		materialDocHead.setCreateUser(stockOutputReturnHead.getCreateUser());

	}

	private void setBizMaterialDocItemFromSAP(JSONObject e_mseg, BizMaterialDocItem mDocItem,
			BizStockOutputReturnItem returnItem, String returnCode) {

		// 保存凭证详细表数据

		mDocItem.setMatDocRid(e_mseg.getInt("ZEILE")); // 物料凭证行项目
		mDocItem.setMatDocYear(e_mseg.getInt("MJAHR"));
		mDocItem.setBatch(returnItem.getBatch());// 批次
		mDocItem.setMoveTypeCode(e_mseg.getString("BWART"));
		mDocItem.setMatId(dictionaryService.getMatIdByMatCode(e_mseg.getString("MATNR")));// 物料编码
		mDocItem.setFtyId(dictionaryService.getFtyIdByFtyCode(e_mseg.getString("WERKS"))); // 工厂
		mDocItem.setLocationId(
				dictionaryService.getLocIdByFtyCodeAndLocCode(e_mseg.getString("WERKS"), e_mseg.getString("LGORT"))); // 库存地点

		mDocItem.setSpecStock(e_mseg.getString("SOBKZ"));
		mDocItem.setSpecStockCode(e_mseg.getString("ZTSKC"));
		mDocItem.setSpecStockName(e_mseg.getString("ZTSMS"));

		mDocItem.setSupplierCode(e_mseg.getString("LIFNR")); // 供应商编码
		mDocItem.setSupplierName(e_mseg.getString("VNAME1")); // 供应商名称

		mDocItem.setCustomerCode(e_mseg.getString("KUNNR")); // 客户编码
		mDocItem.setCustomerName(e_mseg.getString("CNAME1")); // 客户名名称

		mDocItem.setSaleOrderCode(e_mseg.getString("VBELN")); // 销售订单号
		mDocItem.setSaleOrderProjectCode(e_mseg.getString("KDPOS")); // 销售订单项目编号
		mDocItem.setSaleOrderDeliveredPlan(e_mseg.getString("KDEIN")); // 销售订单交货计划

		mDocItem.setStandardCurrencyMoney(UtilObject.getBigDecimalOrZero(e_mseg.getString("DMBTR"))); // 金额

		mDocItem.setQty(new BigDecimal(e_mseg.getString("MENGE")));
		mDocItem.setUnitId(dictionaryService.getUnitIdByUnitCode(e_mseg.getString("MEINS")));// 基本计量单位
		mDocItem.setReferReceiptId(returnItem.getReturnId());
		mDocItem.setReferReceiptCode(returnCode); // 参考单据号
		mDocItem.setReferReceiptRid(returnItem.getReturnRid()); // 参考单行号
		mDocItem.setReferReceiptItemId(returnItem.getItemId());
		mDocItem.setDebitCredit(e_mseg.getString("SHKZG"));// 借贷标识 "H：贷方 数量减
															// S：借方 数量加 "

		String insmk = e_mseg.getString("INSMK");// 库存状态

		if (insmk.equals(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getCode())) {
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
		} else if (insmk.equals(EnumStockStatus.STOCK_BATCH_STATUS_ON_THE_WAY.getCode())) {
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_ON_THE_WAY.getValue());

		} else if (insmk.equals(EnumStockStatus.STOCK_BATCH_STATUS_QUALITY_INSPECTION.getCode())) {
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_QUALITY_INSPECTION.getValue());

		} else if (insmk.equals(EnumStockStatus.STOCK_BATCH_STATUS_FREEZE.getCode())) {
			mDocItem.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_FREEZE.getValue());

		}

		mDocItem.setPurchaseOrderCode(returnItem.getPurchaseOrderCode());
		mDocItem.setPurchaseOrderRid(returnItem.getPurchaseOrderRid());
		// mDocItem.setContractCode(returnItem.getContractCode());
		mDocItem.setBatchMaterialSpecList(returnItem.getBatchMaterialSpecList());
		mDocItem.setBatchSpecList(returnItem.getBatchSpecList());
	}

	@Override
	public Map<String, Object> submitToSapForSale(BizStockOutputReturnHead returnHead,
			List<BizStockOutputReturnItem> returnItems) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		JSONObject returnObject = new JSONObject();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		// sap过账
		JSONObject I_IMPORT = new JSONObject();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(date);
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);
		I_IMPORT.put("ZTYPE", "11");
		I_IMPORT.put("ZERNAM", returnHead.getCreateUser());
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		I_IMPORT.put("I_DATBI", returnHead.getPostDate());
		I_IMPORT.put("ZDJBH", returnHead.getReturnCode());

		List<Map<String, Object>> I_VBELN = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> vbelnMap = new HashMap<String, Object>();
		vbelnMap.put("SIGN", "I");
		vbelnMap.put("OPTION", "EQ");
		vbelnMap.put("LOW", returnHead.getReferReceiptCode());
		vbelnMap.put("HIGH", "");
		I_VBELN.add(vbelnMap);

		JSONArray I_ITEMS = new JSONArray();// SAP参数(ITEM)
		JSONObject itemMap = null;
		for (BizStockOutputReturnItem returnItem : returnItems) {
			itemMap = new JSONObject();
			itemMap.put("ZDJBH", returnHead.getReturnCode()); // 退库单号
			itemMap.put("ZDJXM", returnItem.getReturnRid() + ""); // 行项目
			itemMap.put("VBELN", returnItem.getReferReceiptCode()); // 销售订单
			itemMap.put("POSNR", returnItem.getReferReceiptRid());// 销售凭证项目
																	// SAP10未返货
			itemMap.put("MATNR", dicMaterialDao.selectByPrimaryKey(returnItem.getMatId()).getMatCode());
			itemMap.put("LGORT", dictionaryService.getLocCodeByLocId(returnItem.getLocationId()));
			itemMap.put("DLV_QTY", returnItem.getQty()); // 发货数量
			I_ITEMS.add(itemMap);
		}

		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_VBELN", I_VBELN);
		params.put("I_ITEM", I_ITEMS);
		JSONObject returnObj = new JSONObject();
		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_11?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BizMaterialDocHead maDocHead = new BizMaterialDocHead();

		String isSuccess = returnObj.getString("RETURNVALUE");
		returnObject = returnObj.getJSONObject("RETURN");
		if ("S".equals(isSuccess)) {
			this.setBizMaterialDocHeadFromSAP(returnObj, maDocHead, returnHead);
			JSONArray e_msegs = returnObj.getJSONArray("E_MSEG");
			List<BizMaterialDocItem> mDocItems = new ArrayList<BizMaterialDocItem>();
			for (int i = 0; i < e_msegs.size(); i++) {
				JSONObject e_mseg = e_msegs.getJSONObject(i);
				Integer returnRid = e_mseg.getInt("ZWMSXM");// 退库单行项目
				String returnCode = e_mseg.getString("ZWMSBH");
				BizMaterialDocItem mDocItem = new BizMaterialDocItem();
				BizStockOutputReturnItem returnItem = returnItems.stream().filter(
						z -> z.getReturnRid().equals(returnRid) && returnCode.equals(returnHead.getReturnCode()))
						.findFirst().get();
				this.setBizMaterialDocItemFromSAP(e_mseg, mDocItem, returnItem, returnCode);
				mDocItem.setBatchMaterialSpecList(returnItem.getBatchMaterialSpecList());
				mDocItem.setBatchSpecList(returnItem.getBatchSpecList());
				mDocItem.setRecentBatch(returnItem.isRecentBatch());
				mDocItem.setReferBatch(returnItem.getBatchOutput());
				mDocItem.setReferReceiptId(returnItem.getReturnId());
				mDocItem.setReferReceiptRid(returnItem.getReturnRid());
				mDocItems.add(mDocItem);

			}
			maDocHead.setMaterialDocItemList(mDocItems);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = returnObject.getString("MESSAGE");

			returnMap.put("materialDocHead", maDocHead);
		} else {
			// errorCode =
			// EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			// errorString = returnObject.getString("MESSAGE");
			throw new InterfaceCallException(returnObject.getString("MESSAGE"));
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;

	}

	@Override
	public BizDeliveryOrderHead getDeliveryOrderInfo(Map<String, Object> map) throws Exception {
		String code = map.get("condition").toString();
		//List<Map<String, Object>> headList = elikpDao.selectByCode(code);
		List<Map<String, Object>> headList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		BizDeliveryOrderHead head = new BizDeliveryOrderHead();
		if (headList != null && headList.size() > 0) {
			Map<String, Object> headMap = headList.get(0);
			head.setCreateTime(UtilString.getStringOrEmptyForObject(headMap.get("ERDAT")));
			//head.setCreateTime(sdf.format(headMap.get("ERDAT")));// 创建日期
			String userId = UtilString.getStringOrEmptyForObject(headMap.get("ERNAM"));
			head.setCreateUserId(userId);// 创建人
			head.setCreateUserName(userDao.selectByPrimaryKey(userId).getUserName());
			head.setCustomerCode(UtilString.getStringOrEmptyForObject(headMap.get("KUNNR")));// 客户编号
			head.setCustomerName(UtilString.getStringOrEmptyForObject(headMap.get("NAME1")));// 客户名称
			head.setDeliveryCode(UtilString.getStringOrEmptyForObject(headMap.get("VBELN")));// 交货单号
			head.setDeliveryId(UtilObject.getIntOrZero(headMap.get("e_likp_id")));
			head.setDeliveryType(UtilString.getStringOrEmptyForObject(headMap.get("LFART")));// 交货单类型
			head.setDistributionChannels(UtilString.getStringOrEmptyForObject(headMap.get("VTWEG")));// 分销渠道
			head.setProductGroup(UtilString.getStringOrEmptyForObject(headMap.get("SPART")));// 产品组
			head.setReferPurchaseCode(UtilString.getStringOrEmptyForObject(headMap.get("BSTNK")));// 参考采购订单编号--合同号
			head.setSaleDocument(UtilString.getStringOrEmptyForObject(headMap.get("VBELN_s")));// 销售凭证
			head.setSaleDocumentType(UtilString.getStringOrEmptyForObject(headMap.get("AUART")));// 销售凭证类型
			head.setSaleDocumentTypeName(UtilString.getStringOrEmptyForObject(headMap.get("BEZEI")));// 类型描述
			head.setSaleOrgCode(UtilString.getStringOrEmptyForObject(headMap.get("VKORG")));// 销售组织
		}
		//List<Map<String, Object>> returnItemList = elipsDao.selectByCode(code);
		List<Map<String, Object>> returnItemList = new ArrayList<>();
		List<BizDeliveryOrderItem> itemList = new ArrayList<>();
		if (returnItemList != null && returnItemList.size() > 0) {
			int i = 1;
			for (Map<String, Object> itemMap : returnItemList) {
				BizDeliveryOrderItem item = new BizDeliveryOrderItem();
				item.setRid(i++);
				item.setItemId(UtilString.getStringOrEmptyForObject(itemMap.get("")));
				item.setCustomerMatCode(UtilString.getStringOrEmptyForObject(itemMap.get("KDMAT")));// 客户物料编号
				item.setDeliveryCode(UtilString.getStringOrEmptyForObject(itemMap.get("VBELN")));// 交货单号
				item.setDeliveryQty(new BigDecimal(UtilString.getStringOrEmptyForObject(itemMap.get("LFIMG"))));// 交货数量
				item.setDeliveryRid(UtilString.getStringOrEmptyForObject(itemMap.get("POSNR")));// 交货行项目
				item.setErpBatch(UtilString.getStringOrEmptyForObject(itemMap.get("CHARG")));// erp批号
				// 工厂
				String ftyCode = UtilString.getStringOrEmptyForObject(itemMap.get("WERKS"));// 工厂编码
				int ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
				item.setFtyCode(ftyCode);
				item.setFtyId(ftyId);
				item.setFtyName(factoryDao.selectByPrimaryKey(ftyId).getFtyName());
				// 库存地点
				String locationCode = UtilString.getStringOrEmptyForObject(itemMap.get("LGORT"));// 库存地点编码
				int locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
				item.setLocationCode(locationCode);
				item.setLocationId(locationId);
				item.setLocationName(locationDao.selectByPrimaryKey(locationId).getLocationName());
				// 物料
				String matCode = UtilString.getStringOrEmptyForObject(itemMap.get("MATNR"));// 物料编码
				int matId = dictionaryService.getMatIdByMatCode(matCode);
				item.setMatCode(matCode);
				item.setMatId(matId);
				item.setMatName(dicMaterialDao.selectByPrimaryKey(matId).getMatName());

				DicMaterial dicMaterial = dicMaterialDao.selectByPrimaryKey(matId);
				int unitId = dicMaterial.getUnitId();
				item.setUnitCode(dicUnitDao.selectByPrimaryKey(unitId).getNameEn());
				item.setUnitId(unitId);
				item.setUnitName(dicUnitDao.selectByPrimaryKey(unitId).getNameZh());

				item.setReferReceiptCode(UtilString.getStringOrEmptyForObject(itemMap.get("VGBEL")));// 参考单据的单据编号
				item.setReferReceiptRid(UtilString.getStringOrEmptyForObject(itemMap.get("VGPOS")));// 参考项目的项目号
				item.setCanReturnQty(item.getDeliveryQty());
				item.setErpRemark("这是ERP备注");//sap会给传

				itemList.add(item);
			}
		}
		head.setItemList(itemList);
		return head;
	}

}
