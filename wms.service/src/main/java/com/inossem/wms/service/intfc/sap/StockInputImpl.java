package com.inossem.wms.service.intfc.sap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.biz.BizMaterialDocHeadMapper;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IStockInput;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizPurchaseOrderItem;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.dic.DicBatchSpec;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.vo.SupplierVo;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 库存盘点SAP Service
 * @author wang.ligang
 *
 */
@Service("stockInputSap")
public class StockInputImpl implements IStockInput {

	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private BizMaterialDocHeadMapper materialDocHeadDao;

	/**
	 * SAP 03-采购订单信息下载接口
	 */
	@Override
	public List<BizPurchaseOrderHead> getContractListFromSAP(Map<String, Object> map) throws Exception {

		List<BizPurchaseOrderHead> purOrderList = new ArrayList<BizPurchaseOrderHead>();
		String purchaseOrderCode = ""; // 采购订单号
		String contractCode = ""; // 合同号
		String supplierCode = ""; // 供应商号
		if (map.containsKey("purchaseOrderCode")) {
			purchaseOrderCode = UtilString.getStringOrEmptyForObject(map.get("purchaseOrderCode"));
		}
		if (map.containsKey("contractCode")) {
			contractCode = UtilString.getStringOrEmptyForObject(map.get("contractCode"));
		}
		if (map.containsKey("supplierCode")) {
			supplierCode = UtilString.getStringOrEmptyForObject(map.get("supplierCode"));
		}

		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();
		I_IMPORT.put("ZTYPE", "03");
		I_IMPORT.put("ZERNAM", "");
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", "");
		I_IMPORT.put("ZTIME", "");

		JSONArray I_EBELN = new JSONArray();
		if (StringUtils.hasText(purchaseOrderCode)) {
			JSONObject ebelnObj = new JSONObject();
			ebelnObj.put("SIGN", "I");
			ebelnObj.put("OPTION", "EQ");
			ebelnObj.put("LOW", purchaseOrderCode);
			ebelnObj.put("HIGH", "");
			I_EBELN.add(ebelnObj);
			params.put("I_EBELN", I_EBELN);
		}
		if (StringUtils.hasText(contractCode)) {
			params.put("I_ZHTBH", contractCode);
		}
		if (StringUtils.hasText(supplierCode)) {
			params.put("I_LIFNR", supplierCode);
		}

		params.put("I_IMPORT", I_IMPORT);

		params.put("i_state", "1");
		JSONObject returnObj = new JSONObject();

		returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_03?sap-client=" + Const.SAP_API_CLIENT,
				params, 3);

		JSONArray contractAry = null;

		contractAry = returnObj.getJSONArray("ET_HEAD");

		if (contractAry != null) {
			for (int i = 0; i < contractAry.size(); i++) {
				JSONObject contractObj = contractAry.getJSONObject(i);
				BizPurchaseOrderHead purchaseOrderHead = new BizPurchaseOrderHead();
				this.setBizPurchaseOrderHeadFromSap(contractObj, purchaseOrderHead);

				purOrderList.add(purchaseOrderHead);
			}
		}

		return purOrderList;

	}

	private void setBizPurchaseOrderHeadFromSap(JSONObject contractObj, BizPurchaseOrderHead purchaseOrderHead) {

		purchaseOrderHead.setSupplierCode(contractObj.getString("LIFNR"));
		purchaseOrderHead.setSupplierName(contractObj.getString("NAME1"));
		purchaseOrderHead.setCreateTime(contractObj.getString("AEDAT"));
		purchaseOrderHead.setCorpCode(contractObj.getString("BUKRS"));
		Integer corpId = dictionaryService.getCorpIdByCorpCode(purchaseOrderHead.getCorpCode());
		purchaseOrderHead.setCorpId(corpId);
		purchaseOrderHead.setContractName(contractObj.getString("BUTXT"));
		purchaseOrderHead.setPurchaseOrderType(contractObj.getString("BSART"));
		purchaseOrderHead.setPurchaseOrderTypeName(contractObj.getString("BATXT"));
		purchaseOrderHead.setPurchaseOrg(contractObj.getString("EKORG"));
		purchaseOrderHead.setPurchaseOrgName(contractObj.getString("EKOTX"));
		purchaseOrderHead.setPurchaseGroup(contractObj.getString("EKGRP"));
		purchaseOrderHead.setPurchaseGroupName(contractObj.getString("EKNAM"));
		purchaseOrderHead.setContractCode(contractObj.getString("ZHTBH"));
		purchaseOrderHead.setContractName(contractObj.getString("ZHTMC"));
		purchaseOrderHead.setPurchaseOrderCode(contractObj.getString("EBELN"));

	}

	@SuppressWarnings("unchecked")
	private void setBizPurchaseOrderItemFromSap(JSONObject contractObj, BizPurchaseOrderItem purchaseOrderItem,
			BizPurchaseOrderHead purchaseOrderHead) throws Exception {
		purchaseOrderItem.setPurchaseOrderCode(contractObj.getString("EBELN"));
		purchaseOrderItem.setContractCode(purchaseOrderHead.getContractCode());
		purchaseOrderItem.setContractName(purchaseOrderHead.getContractName());
		purchaseOrderItem.setPurchaseOrderRid(contractObj.getString("EBELP"));
		purchaseOrderItem.setFtyCode(contractObj.getString("WERKS"));
		purchaseOrderItem.setLocationCode(contractObj.getString("LGORT"));

		Integer ftyId = dictionaryService.getFtyIdByFtyCode(purchaseOrderItem.getFtyCode());
		Integer locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(purchaseOrderItem.getFtyCode(),
				purchaseOrderItem.getLocationCode());
		purchaseOrderItem.setLocationId(locationId);
		purchaseOrderItem.setFtyId(ftyId);
		String ftyName = dictionaryService.getFtyIdMap().get(ftyId).getFtyName();
		purchaseOrderItem.setFtyName(ftyName);
		purchaseOrderItem.setLocationName(contractObj.getString("LGOBE"));
		purchaseOrderItem.setMatCode(contractObj.getString("MATNR"));
		purchaseOrderItem.setMatName(contractObj.getString("TXZ01"));
		purchaseOrderItem.setUnitCode(contractObj.getString("MEINS"));
		Integer unitId = dictionaryService.getUnitIdByUnitCode(purchaseOrderItem.getUnitCode());
		purchaseOrderItem.setUnitId(unitId);
		purchaseOrderItem.setNameZh(contractObj.getString("MSEHL"));
		purchaseOrderItem.setDecimalPlace(Byte.parseByte(contractObj.getString(("ANDEC"))));
		purchaseOrderItem.setOrderQty(new BigDecimal(contractObj.getString("MENGE")));
		purchaseOrderItem.setInputedQty(new BigDecimal(contractObj.getString("SJNUM")));
		purchaseOrderItem.setManufacturer(contractObj.getString("AFNAM"));
		purchaseOrderItem.setCostObjCode(contractObj.getString("POSID"));
		purchaseOrderItem.setCostObjName(contractObj.getString("ZPOST1"));
		purchaseOrderItem.setSpecStock(contractObj.getString("SOBKZ"));
		purchaseOrderItem.setDemandDept(contractObj.getString("TXT50"));
		purchaseOrderItem.setSpecStockCode(contractObj.getString("ZTSKC"));
		purchaseOrderItem.setSpecStockName(contractObj.getString("ZTSMS"));
		purchaseOrderItem.setMatGroupCode(contractObj.getString("MATKL"));
		purchaseOrderItem.setReturnMarking(contractObj.getString("RETPO"));
		Integer matId = dictionaryService.getMatIdByMatCode(purchaseOrderItem.getMatCode());
		purchaseOrderItem.setMatId(matId);
		Map<String, Object> getBatchSpecMap = commonService.getBatchSpecList(null, null, matId);
		List<DicBatchSpec> batchSpecList = (List<DicBatchSpec>) getBatchSpecMap.get("batchSpecList");
		
		//List<DicBatchSpec> batchMaterialSpecList = 	(List<DicBatchSpec>) getBatchSpecMap.get("batchMaterialSpecList");	
		purchaseOrderItem.setBatchSpecList(batchSpecList);
		BatchMaterial batchMaterial = new BatchMaterial();
		batchMaterial.setContractCode(purchaseOrderItem.getContractCode());
		batchMaterial.setContractName(purchaseOrderItem.getContractName());
		batchMaterial.setPurchaseOrderCode(purchaseOrderItem.getPurchaseOrderCode());
		batchMaterial.setPurchaseOrderRid(purchaseOrderItem.getPurchaseOrderRid());
		batchMaterial.setDemandDept(purchaseOrderItem.getDemandDept());
		List<DicBatchSpec> batchMaterialSpecListNew = commonService.getFixedBatchMaterialSpec(batchMaterial, batchSpecList);
		purchaseOrderItem.setBatchMaterialSpecList(batchMaterialSpecListNew);
	
	}

	
	
	private void setBizMaterialDocHeadFromSAP(JSONObject json, BizMaterialDocHead materialDocHead,
			BizStockInputHead stockInputHead) {
		JSONArray e_msegs = json.getJSONArray("E_MSEG");
		String mblnr = ((JSONObject) e_msegs.get(0)).getString("MBLNR");// 物料凭证号
		materialDocHead.setMatDocCode(mblnr);// 物料凭证号
		materialDocHead.setMatDocType(stockInputHead.getReceiptType()); // 业务类型

		materialDocHead.setDocTime(stockInputHead.getDocTime()); // 凭证日期
		materialDocHead.setPostingDate(stockInputHead.getPostingDate());

		materialDocHead.setOrgCode(stockInputHead.getPurchaseOrg());
		materialDocHead.setOrgName(stockInputHead.getPurchaseOrgName());
		materialDocHead.setGroupCode(stockInputHead.getPurchaseGroup());
		materialDocHead.setGroupName(stockInputHead.getPurchaseGroupName());
		materialDocHead.setCreateUser(stockInputHead.getCreateUser());

	}

	private void setBizMaterialDocItemFromSAP(JSONObject e_mseg, BizMaterialDocItem mDocItem,
			BizStockInputItem stockInputItem, String stockInputCode) throws Exception{

		// 保存凭证详细表数据

		mDocItem.setMatDocRid(e_mseg.getInt("ZEILE")); // 物料凭证行项目
		mDocItem.setMatDocYear(e_mseg.getInt("MJAHR"));
		//mDocItem.setBatch(stockInputItem.getBatch());// 批次
		mDocItem.setMoveTypeCode(e_mseg.getString("BWART"));
		
		mDocItem.setMatId(dictionaryService.getMatIdByMatCode(e_mseg.getString("MATNR")));// 物料编码
		mDocItem.setFtyId(dictionaryService.getFtyIdByFtyCode(e_mseg.getString("WERKS"))); // 工厂
		mDocItem.setLocationId(
				dictionaryService.getLocIdByFtyCodeAndLocCode(e_mseg.getString("WERKS"), e_mseg.getString("LGORT"))); // 库存地点

		mDocItem.setSpecStock(e_mseg.getString("SOBKZ"));
		
		Integer moveTypeId = dictionaryService.getMoveTypeIdByMoveTypeCodeAndSpecStock(mDocItem.getMoveTypeCode(), mDocItem.getSpecStock());
		mDocItem.setMoveTypeId(moveTypeId);
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
		mDocItem.setReferReceiptId(stockInputItem.getStockInputId());
		mDocItem.setReferReceiptCode(stockInputCode); // 参考单据号
		mDocItem.setReferReceiptRid(stockInputItem.getStockInputRid()); // 参考单行号
		mDocItem.setReferReceiptItemId(stockInputItem.getItemId());
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

		mDocItem.setPurchaseOrderCode(stockInputItem.getPurchaseOrderCode());
		mDocItem.setPurchaseOrderRid(stockInputItem.getPurchaseOrderRid());
		mDocItem.setContractCode(stockInputItem.getContractCode());
		mDocItem.setBatchMaterialSpecList(stockInputItem.getBatchMaterialSpecList());
		mDocItem.setBatchSpecList(stockInputItem.getBatchSpecList());
		String batchString = e_mseg.getString("CHARG");
		Long batch = null;
		if(StringUtils.hasText(batchString)){
			batch = UtilObject.getLongOrNull(batchString);
		}
		if(batch==null||batch==0){
			batch = commonService.getBatch();
		}
		mDocItem.setBatch(batch);
		stockInputItem.setBatch(batch);
	
	}
	
	private void setBizMaterialDocHeadFromSAPAndOld(JSONObject json, BizMaterialDocHead materialDocHead,
			BizStockInputHead stockInputHead,BizMaterialDocHead oMaterialDocHead) {
		String mblnr = json.getString("MBLNR");// 物料凭证号
		materialDocHead.setMatDocCode(mblnr);// 物料凭证号
		materialDocHead.setMatDocType(oMaterialDocHead.getMatDocType()); // 业务类型

		materialDocHead.setDocTime(stockInputHead.getDocTime()); // 凭证日期
		materialDocHead.setPostingDate(stockInputHead.getPostingDate());

		materialDocHead.setOrgCode(oMaterialDocHead.getOrgCode());
		materialDocHead.setOrgName(oMaterialDocHead.getOrgName());
		materialDocHead.setGroupCode(oMaterialDocHead.getGroupCode());
		materialDocHead.setGroupName(oMaterialDocHead.getGroupName());
		materialDocHead.setCreateUser(stockInputHead.getCreateUser());

	}

	private void setBizMaterialDocItemFromSAPAndOld(JSONObject e_mseg, BizMaterialDocItem mDocItem,
			BizStockInputItem stockInputItem, String stockInputCode , BizMaterialDocItem omDocItem) {

		// 保存凭证详细表数据

		mDocItem.setMatDocRid(e_mseg.getInt("ZEILE")); // 物料凭证行项目
		mDocItem.setMatDocYear(e_mseg.getInt("MJAHR"));
		mDocItem.setBatch(omDocItem.getBatch());// 批次
		mDocItem.setMoveTypeCode(e_mseg.getString("BWART"));
		
		mDocItem.setMatId(dictionaryService.getMatIdByMatCode(e_mseg.getString("MATNR")));// 物料编码
		mDocItem.setFtyId(dictionaryService.getFtyIdByFtyCode(e_mseg.getString("WERKS"))); // 工厂
		mDocItem.setLocationId(
				dictionaryService.getLocIdByFtyCodeAndLocCode(e_mseg.getString("WERKS"), e_mseg.getString("LGORT"))); // 库存地点

		mDocItem.setSpecStock(e_mseg.getString("SOBKZ"));
		
		Integer moveTypeId = dictionaryService.getMoveTypeIdByMoveTypeCodeAndSpecStock(mDocItem.getMoveTypeCode(), mDocItem.getSpecStock());
		mDocItem.setMoveTypeId(moveTypeId);
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
		mDocItem.setReferReceiptId(omDocItem.getReferReceiptId());
		mDocItem.setReferReceiptCode(omDocItem.getReferReceiptCode()); // 参考单据号
		mDocItem.setReferReceiptRid(omDocItem.getReferReceiptRid()); // 参考单行号
		mDocItem.setReferReceiptItemId(stockInputItem.getItemId());
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

	}
	

	@Override
	public BizPurchaseOrderHead getContractInfoFromSAP(String purchaseOrderCode) throws Exception {

		BizPurchaseOrderHead purchaseOrderHead = new BizPurchaseOrderHead();
		List<BizPurchaseOrderItem> purchaseOrderItemList = new ArrayList<BizPurchaseOrderItem>();

		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();
		I_IMPORT.put("ZTYPE", "03");
		I_IMPORT.put("ZERNAM", "");
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", "");
		I_IMPORT.put("ZTIME", "");

		JSONArray I_EBELN = new JSONArray();
		if (StringUtils.hasText(purchaseOrderCode)) {
			JSONObject ebelnObj = new JSONObject();
			ebelnObj.put("SIGN", "I");
			ebelnObj.put("OPTION", "EQ");
			ebelnObj.put("LOW", purchaseOrderCode);
			ebelnObj.put("HIGH", "");
			I_EBELN.add(ebelnObj);
			params.put("I_EBELN", I_EBELN);
		}

		params.put("I_IMPORT", I_IMPORT);

		params.put("i_state", "3");
		JSONObject returnObj = new JSONObject();

		returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_03?sap-client=" + Const.SAP_API_CLIENT,
				params, 3);

		JSONArray contractAry = null;
		JSONArray contractItemAry = null;

		contractAry = returnObj.getJSONArray("ET_HEAD");

		contractItemAry = returnObj.getJSONArray("ET_ITEM");

		if (contractAry != null && contractAry.size() == 1) {
			JSONObject contractObj = contractAry.getJSONObject(0);
			this.setBizPurchaseOrderHeadFromSap(contractObj, purchaseOrderHead);
		}

		if (contractItemAry != null) {
			for (int i = 0; i < contractItemAry.size(); i++) {
				JSONObject contractObj = contractItemAry.getJSONObject(i);

				BigDecimal zddsl = new BigDecimal(contractObj.getString("MENGE"));
				BigDecimal zrksl = new BigDecimal(contractObj.getString("SJNUM"));

				String RETPO = contractObj.getString("RETPO");
				if (!StringUtils.hasText(RETPO) && zddsl.compareTo(zrksl) == 1) {

					BizPurchaseOrderItem purchaseOrderItem = new BizPurchaseOrderItem();
					this.setBizPurchaseOrderItemFromSap(contractObj, purchaseOrderItem, purchaseOrderHead);

					purchaseOrderItemList.add(purchaseOrderItem);

				}
			}
		}
		purchaseOrderHead.setItemList(purchaseOrderItemList);

		return purchaseOrderHead;

	}

	@Override
	public Map<String, Object> mjrkOrZcrkPosting(BizStockInputHead stockInputHead) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();

		JSONObject posting = new JSONObject();
		JSONArray I_LIST = new JSONArray();

		Map<String, DicCorp> dicCorpMap = dictionaryService.getCorpCodeMap();
		String coryCode = dictionaryService.getCorpCodeByCorpId(stockInputHead.getCorpId());
		int boardId = dicCorpMap.get(coryCode).getBoardId();

		for (BizStockInputItem item : stockInputHead.getItemList()) {
			JSONObject postingItem = new JSONObject();
			postingItem.put("ZDJBH", stockInputHead.getStockInputCode());
			postingItem.put("ZDJXM", item.getStockInputRid());
			postingItem.put("EBELN", item.getPurchaseOrderCode());
			postingItem.put("EBELP", item.getPurchaseOrderRid());
			postingItem.put("BCNUM", item.getQty());// 入库数量
			String locationCode = dictionaryService.getLocCodeByLocId(item.getLocationId());
			postingItem.put("STGE_LOC", locationCode);// 库存地点
			postingItem.put("DOC_DATE", UtilString.getShortStringForDate(stockInputHead.getDocTime()));
			postingItem.put("PSTNG_DATE", UtilString.getShortStringForDate(stockInputHead.getPostingDate()));
			// 招采入库
			if (stockInputHead.getReceiptType().equals(EnumReceiptType.STOCK_INPUT_PLATFORM.getValue())) {
				postingItem.put("EXTRA1", UtilString.getStringOrEmptyForObject(boardId));
			}

			I_LIST.add(postingItem);
		}
		posting.put("I_LIST", I_LIST);
		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);
		I_IMPORT.put("ZTYPE", "04");
		I_IMPORT.put("ZERNAM", stockInputHead.getCreateUser());
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		I_IMPORT.put("ZDJBH", stockInputHead.getStockInputCode());
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_ZDJBH", stockInputHead.getStockInputCode());
		params.put("I_LIST", I_LIST);

		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_04?sap-client=" + Const.SAP_API_CLIENT, params, 3);

		BizMaterialDocHead maDocHead = new BizMaterialDocHead();

		String isSuccess = returnObj.getString("RETURNVALUE");
		if ("S".equals(isSuccess)) {
			this.setBizMaterialDocHeadFromSAP(returnObj, maDocHead, stockInputHead);
			JSONArray e_msegs = returnObj.getJSONArray("E_MSEG");
			List<BizMaterialDocItem> mDocItems = new ArrayList<BizMaterialDocItem>();
			for (int i = 0; i < e_msegs.size(); i++) {
				JSONObject e_mseg = e_msegs.getJSONObject(i);
				Integer stockInputRid = e_mseg.getInt("ZWMSXM");
				String stockInputCode = e_mseg.getString("ZWMSBH");
				BizMaterialDocItem mDocItem = new BizMaterialDocItem();
				BizStockInputItem stockInputItem = stockInputHead.getItemList().stream()
						.filter(z -> z.getStockInputRid().equals(stockInputRid)
								&& stockInputCode.equals(stockInputHead.getStockInputCode()))
						.findFirst().get();
				this.setBizMaterialDocItemFromSAP(e_mseg, mDocItem, stockInputItem, stockInputCode);
				mDocItems.add(mDocItem);

			}
			maDocHead.setMaterialDocItemList(mDocItems);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = returnObj.getString("RETURN");

			returnMap.put("materialDocHead", maDocHead);
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			errorString = returnObj.getString("RETURN");
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;
	}

	@Override
	public Map<String, Object> qtrkPosting(BizStockInputHead stockInputHead) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);

		JSONObject params = new JSONObject();
		JSONObject i_import = new JSONObject();
		i_import.put("ZTYPE", "06");
		i_import.put("ZERNAM", stockInputHead.getCreateUser());
		i_import.put("ZIMENO", "----");
		i_import.put("ZDATE", ZDATE);
		i_import.put("ZTIME", ZTIME);
		i_import.put("ZDJBH", stockInputHead.getStockInputCode());
		params.put("I_IMPORT", i_import);

		JSONArray i_headAry = new JSONArray();
		JSONObject i_head = new JSONObject();
		i_head.put("ZNO", stockInputHead.getStockInputCode());// 入库单号"test00001"

		String ftyCode = dictionaryService.getFtyCodeByFtyId(stockInputHead.getFtyId());
		String locationCode = dictionaryService.getLocCodeByLocId(stockInputHead.getLocationId());
		String moveTypeCode = dictionaryService.getMoveTypeCodeByMoveTypeId(stockInputHead.getMoveTypeId());
		String specStock = dictionaryService.getSpecStockByMoveTypeId(stockInputHead.getMoveTypeId());
		i_head.put("PLANT", ftyCode);// 工厂"2000"
		i_head.put("STGE_LOC", locationCode);// 库存地点 "0004"
		i_head.put("MOVE_TYPE", moveTypeCode);// 移动类型 "511"
		i_head.put("SPEC_STOCK", specStock);// 特殊库存标识
		i_head.put("VENDOR", stockInputHead.getSupplierCode());// 供应商代码
																// "0020003377"
		i_head.put("VAL_WBS_ELEM", stockInputHead.getSpecStockCode());// 特殊库存代码
																		// ""
		i_head.put("BUDAT", UtilString.getShortStringForDate(stockInputHead.getPostingDate()));// 过账日期"2017-07-08"
		i_head.put("BLDAT", UtilString.getShortStringForDate(stockInputHead.getDocTime()));// 凭证日期"2017-07-08"

		List<Integer> matIdList = new ArrayList<Integer>();
		for (BizStockInputItem item : stockInputHead.getItemList()) {
			matIdList.add(item.getMatId());
		}
		Map<Integer, String> matMap = dictionaryService.getMatMapByIdList(matIdList);
		i_headAry.add(i_head);
		params.put("I_HEAD", i_headAry);

		JSONArray i_itemAry = new JSONArray();
		JSONObject i_item = new JSONObject();
		for (BizStockInputItem item : stockInputHead.getItemList()) {

			i_item.put("ZDJBH", stockInputHead.getStockInputCode());// 入库单号"test00001"
			i_item.put("ZDJXM", item.getStockInputRid());// 行项目
			i_item.put("MATERIAL", matMap.get(item.getMatId()));// 物料编码
																// "60000002"
			i_item.put("PLANT", ftyCode);/// 工厂 "2000"
			i_item.put("STGE_LOC", locationCode);// 库存地点"0004"
			i_item.put("MOVE_TYPE", moveTypeCode);// 移动类型"511"
			i_item.put("SPEC_STOCK", specStock);// 特殊库存标识 ""
			i_item.put("VENDOR", stockInputHead.getSupplierCode());// 供应商代码
																	// "0020003377"
			i_item.put("ENTRY_QNT", item.getQty());// 入库数量"30"
			String unitCode = dictionaryService.getUnitCodeByUnitId(item.getUnitId());
			i_item.put("ENTRY_UOM", unitCode);// 计量单位"KG"
			i_item.put("MOVE_MAT", "");// 目标物料
			i_item.put("MOVE_PLANT", "");// 目标工厂
			i_item.put("MOVE_STLOC", "");// 目标库存地点
			i_item.put("WBS_ELEM", "");// 特殊库存代码""
			i_item.put("VAL_WBS_ELEM", "");// 目标特殊库存代码
			i_itemAry.add(i_item);
		}

		params.put("I_ITEM", i_itemAry);

		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_06?sap-client=" + Const.SAP_API_CLIENT, params, 10);

		BizMaterialDocHead maDocHead = new BizMaterialDocHead();

		String isSuccess = returnObj.getString("RETURNVALUE");
		if ("S".equals(isSuccess)) {
			this.setBizMaterialDocHeadFromSAP(returnObj, maDocHead, stockInputHead);
			JSONArray e_msegs = returnObj.getJSONArray("E_MSEG");
			List<BizMaterialDocItem> mDocItems = new ArrayList<BizMaterialDocItem>();
			for (int i = 0; i < e_msegs.size(); i++) {
				JSONObject e_mseg = e_msegs.getJSONObject(i);
				Integer stockInputRid = e_mseg.getInt("ZWMSXM");
				String stockInputCode = e_mseg.getString("ZWMSBH");
				BizMaterialDocItem mDocItem = new BizMaterialDocItem();
				BizStockInputItem stockInputItem = stockInputHead.getItemList().stream()
						.filter(z -> z.getStockInputRid().equals(stockInputRid)
								&& stockInputCode.equals(stockInputHead.getStockInputCode()))
						.findFirst().get();
				this.setBizMaterialDocItemFromSAP(e_mseg, mDocItem, stockInputItem, stockInputCode);
				mDocItems.add(mDocItem);

			}
			maDocHead.setMaterialDocItemList(mDocItems);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = returnObj.getString("RETURN");

			returnMap.put("materialDocHead", maDocHead);
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			errorString = returnObj.getString("RETURN");
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;
	}

	@Override
	public List<SupplierVo> getSupplierList(SupplierVo supplier) throws Exception {

		List<SupplierVo> supplierList = new ArrayList<SupplierVo>();

		JSONObject params = new JSONObject();
		JSONArray i_lifnr = new JSONArray();
		JSONObject _lifnr = new JSONObject();
		_lifnr.put("SIGN", "I");
		_lifnr.put("OPTION", "CP");
		_lifnr.put("LOW", UtilString.getStringOrEmptyForObject(supplier.getSupplierCode()) + "*");
		i_lifnr.add(_lifnr);
		params.put("I_LIFNR", i_lifnr);
		if (StringUtils.hasText(supplier.getSupplierName())) {
			JSONArray i_lifnm = new JSONArray();
			JSONObject _lifnm = new JSONObject();
			_lifnm.put("SIGN", "I");
			_lifnm.put("OPTION", "CP");
			_lifnm.put("LOW", "*" + supplier.getSupplierName() + "*");
			i_lifnm.add(_lifnm);
			params.put("I_LIFNM", i_lifnm);
		}
		params.put("I_MOUNT", "10");
		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_20?sap-client=" + Const.SAP_API_CLIENT, params, 10);
		JSONArray e_lfa1 = result.getJSONArray("E_LFA1");
		for (Object object : e_lfa1) {
			JSONObject obj = (JSONObject) object;
			SupplierVo s = new SupplierVo();
			s.setSupplierCode(obj.getString("LIFNR"));
			s.setSupplierName(obj.getString("NAME1"));
			supplierList.add(s);
		}
		return supplierList;
	}

	@Override
	public Map<String, Object> dbrkPosting(BizStockInputHead stockInputHead) throws Exception {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		

		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);
		I_IMPORT.put("ZTYPE", "15");
		I_IMPORT.put("ZERNAM", stockInputHead.getCreateUser());
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		I_IMPORT.put("ZDJBH", stockInputHead.getStockInputCode());
		params.put("I_IMPORT", I_IMPORT);

		JSONArray headAry = new JSONArray();
		JSONObject headObj = new JSONObject();
		headObj.put("PSTNG_DATE", UtilString.getShortStringForDate(stockInputHead.getPostingDate())); // 凭证日期
		headObj.put("DOC_DATE", UtilString.getShortStringForDate(stockInputHead.getDocTime())); // 过账日期
		headObj.put("ZDJBH", stockInputHead.getStockInputCode());
		headAry.add(headObj);
		params.put("HEAD", headAry);

		List<Integer> matIdList = new ArrayList<Integer>();
		for (BizStockInputItem item : stockInputHead.getItemList()) {
			matIdList.add(item.getMatId());
		}
		Map<Integer, String> matMap = dictionaryService.getMatMapByIdList(matIdList);
		
		JSONArray I_LIST = new JSONArray();
		if(stockInputHead!=null&&stockInputHead.getItemList()!=null){
			for(BizStockInputItem inputItem : stockInputHead.getItemList()){
				JSONObject item = new JSONObject();
				item.put("ZDJBH", stockInputHead.getStockInputCode());
				item.put("ZDJXM", inputItem.getStockInputRid());
				item.put("MATERIAL", matMap.get(inputItem.getMatId()));
				String ftyCode = dictionaryService.getFtyCodeByFtyId(inputItem.getFtyId());
				String locationCode = dictionaryService.getLocCodeByLocId(inputItem.getLocationId());
				item.put("PLANT", ftyCode);
				item.put("STGE_LOC", locationCode);
				item.put("MOVE_TYPE", "315");
				item.put("SPEC_STOCK", inputItem.getSpecStock());// 原特殊库存标识
				item.put("ZTSKC1", inputItem.getSpecStockCode());// 原特殊库存编号
				item.put("ENTRY_QNT", inputItem.getQty());
				item.put("MOVE_MAT", matMap.get(inputItem.getMatId()));
				item.put("MOVE_PLANT", ftyCode);
				item.put("MOVE_STLOC", locationCode);
				item.put("ZKCBS", inputItem.getSpecStock());
				item.put("ZTSKC2", inputItem.getSpecStockCode());
				I_LIST.add(item);
			}
		}
		
		
		params.put("ITEM", I_LIST);

		JSONObject returnObj =  UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_15?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);

		BizMaterialDocHead maDocHead = new BizMaterialDocHead();

		String isSuccess = returnObj.getString("RETURNVALUE");
		if ("S".equals(isSuccess)) {
			this.setBizMaterialDocHeadFromSAP(returnObj, maDocHead, stockInputHead);
			JSONArray e_msegs = returnObj.getJSONArray("E_MSEG");
			List<BizMaterialDocItem> mDocItems = new ArrayList<BizMaterialDocItem>();
			for (int i = 0; i < e_msegs.size(); i++) {
				JSONObject e_mseg = e_msegs.getJSONObject(i);
				Integer stockInputRid = e_mseg.getInt("ZWMSXM");
				String stockInputCode = e_mseg.getString("ZWMSBH");
				BizMaterialDocItem mDocItem = new BizMaterialDocItem();
				BizStockInputItem stockInputItem = stockInputHead.getItemList().stream()
						.filter(z -> z.getStockInputRid().equals(stockInputRid)
								&& stockInputCode.equals(stockInputHead.getStockInputCode()))
						.findFirst().get();
				this.setBizMaterialDocItemFromSAP(e_mseg, mDocItem, stockInputItem, stockInputCode);
				mDocItems.add(mDocItem);
			}
			maDocHead.setMaterialDocItemList(mDocItems);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = returnObj.getJSONObject("RETURN").getString("MESSAGE");

			returnMap.put("materialDocHead", maDocHead);
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			errorString = returnObj.getJSONObject("RETURN").getString("MESSAGE");
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;
	}

	@Override
	public Map<String, Object> writeOff(BizStockInputHead stockInputHead,List<BizMaterialDocItem> omDocItemList) throws Exception {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		

		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);
		I_IMPORT.put("ZTYPE", "16");
		I_IMPORT.put("ZERNAM", stockInputHead.getCreateUser());
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		I_IMPORT.put("ZDJBH", stockInputHead.getStockInputCode());
		params.put("I_IMPORT", I_IMPORT);

		Integer matDocId = stockInputHead.getItemList().get(0).getMatDocId();
		String matDocCode = stockInputHead.getItemList().get(0).getMatDocCode();
		params.put("I_MBLNR", matDocCode);
		params.put("I_MJAHR", "");
		

		List<Integer> matIdList = new ArrayList<Integer>();
		for (BizStockInputItem item : stockInputHead.getItemList()) {
			matIdList.add(item.getMatId());
		}
		
		JSONArray I_LIST = new JSONArray();
		if(stockInputHead!=null&&stockInputHead.getItemList()!=null){
			for(BizStockInputItem inputItem : stockInputHead.getItemList()){
				JSONObject item = new JSONObject();
				item.put("MBLPO", inputItem.getMatDocRid());
				item.put("ZDJBH", stockInputHead.getStockInputCode());
				item.put("ZDJXM",inputItem.getStockInputRid());
				I_LIST.add(item);
			}
		}
		params.put("I_ITEM", I_LIST);
		
		

		JSONObject returnObj =  UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_16?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);

		BizMaterialDocHead maDocHead = new BizMaterialDocHead();

		String isSuccess = returnObj.getString("RETURNVALUE");
		if ("S".equals(isSuccess)) {
			BizMaterialDocHead oMaterialDocHead = materialDocHeadDao.selectByPrimaryKey(matDocId);
			this.setBizMaterialDocHeadFromSAPAndOld(returnObj, maDocHead, stockInputHead,oMaterialDocHead);
			JSONArray e_msegs = returnObj.getJSONArray("E_MSEG");
			List<BizMaterialDocItem> mDocItems = new ArrayList<BizMaterialDocItem>();
			for (int i = 0; i < e_msegs.size(); i++) {
				JSONObject e_mseg = e_msegs.getJSONObject(i);
				Integer stockInputRid = e_mseg.getInt("ZWMSXM");
				String stockInputCode = e_mseg.getString("ZWMSBH");
				BizMaterialDocItem mDocItem = new BizMaterialDocItem();
				BizStockInputItem stockInputItem = stockInputHead.getItemList().stream()
						.filter(z -> z.getStockInputRid().equals(stockInputRid)
								&& stockInputCode.equals(stockInputHead.getStockInputCode()))
						.findFirst().get();
				BizMaterialDocItem omDocItem = omDocItemList.stream()
						.filter(z -> z.getReferReceiptRid().equals(stockInputRid)
								&& z.getReferReceiptId().equals(stockInputHead.getStockInputId()))
						.findFirst().get();
				
				this.setBizMaterialDocItemFromSAPAndOld(e_mseg, mDocItem, stockInputItem, stockInputCode,omDocItem);
				mDocItems.add(mDocItem);
			}
			maDocHead.setMaterialDocItemList(mDocItems);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = returnObj.getJSONObject("RETURN").getString("MESSAGE");

			returnMap.put("materialDocHead", maDocHead);
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
			errorString = returnObj.getJSONObject("RETURN").getString("MESSAGE");
		}
		returnMap.put("errorCode", errorCode);
		returnMap.put("errorString", errorString);

		return returnMap;
	}

}
