package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.constant.Const;
import com.inossem.wms.service.biz.IDeliveryNoticeService;
import com.inossem.wms.util.UtilREST;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
@Service("deliveryNoticeService")
public class DeliveryNoticeServiceImpl implements IDeliveryNoticeService {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryNoticeServiceImpl.class);

	@Override
	public ArrayList<HashMap<String, Object>> getContract(JSONObject findObj) throws Exception {
		ArrayList<HashMap<String, Object>> contractList = new ArrayList<HashMap<String, Object>>();
		String ebeln = "";
		String lifnr = "";
		String zhtbh = "";
		try {
			ebeln = findObj.getString("ebeln");
		} catch (Exception e) {
		}
		try {
			lifnr = findObj.getString("lifnr");
		} catch (Exception e) {
		}
		try {
			zhtbh = findObj.getString("zhtbh");
		} catch (Exception e) {
		}

		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();
		I_IMPORT.put("ZTYPE", "03");
		I_IMPORT.put("ZERNAM", "");
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", "");
		I_IMPORT.put("ZTIME", "");

		JSONArray I_EBELN = new JSONArray();
		if (StringUtils.hasText(ebeln)) {
			JSONObject ebelnObj = new JSONObject();
			ebelnObj.put("SIGN", "I");
			ebelnObj.put("OPTION", "EQ");
			ebelnObj.put("LOW", ebeln.trim());
			ebelnObj.put("HIGH", "");
			I_EBELN.add(ebelnObj);
			params.put("I_EBELN", I_EBELN);
		}
		if (StringUtils.hasText(zhtbh)) {
			params.put("I_ZHTBH", zhtbh.trim());
		}
		if (StringUtils.hasText(lifnr)) {
			params.put("I_LIFNR", lifnr.trim());
		}

		params.put("I_IMPORT", I_IMPORT);

		params.put("i_state", "1");
		JSONObject returnObj = new JSONObject();
		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_03?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		} catch (Exception e) {
			logger.error("", e);
		}
		JSONArray contractAry = null;
		try {
			contractAry = returnObj.getJSONArray("ET_HEAD");
		} catch (Exception e) {
			logger.error("", e);
		}
		if (contractAry != null) {
			for (int i = 0; i < contractAry.size(); i++) {
				JSONObject contractObj = (JSONObject) contractAry.get(i);
				HashMap<String, Object> contract = new HashMap<String, Object>();
				contract.put("lifnr", contractObj.getString("LIFNR"));
				contract.put("aedat", contractObj.getString("AEDAT"));// 创建日期
				contract.put("bukrs", contractObj.getString("BUKRS"));// 使用单位
				contract.put("butxt", contractObj.getString("BUTXT"));
				contract.put("bsart", contractObj.getString("BSART"));// 订单类型
				contract.put("batxt", contractObj.getString("BATXT"));

				contract.put("ekorg", contractObj.getString("EKORG"));
				contract.put("ekgrp", contractObj.getString("EKGRP"));
				contract.put("name1", contractObj.getString("NAME1"));// 供应商名称
				contract.put("zhtbh", contractObj.getString("ZHTBH"));
				contract.put("ekotx", contractObj.getString("EKOTX"));
				contract.put("eknam", contractObj.getString("EKNAM"));
				contract.put("ebeln", contractObj.getString("EBELN"));

				contractList.add(contract);
			}
		}

		return contractList;
	}

	@Override
	public HashMap<String, Object> getContractInfo(String ebeln) throws Exception {
		HashMap<String, Object> contractInfo = new HashMap<String, Object>();

		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();
		I_IMPORT.put("ZTYPE", "03");
		I_IMPORT.put("ZERNAM", "");
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", "");
		I_IMPORT.put("ZTIME", "");

		JSONArray I_EBELN = new JSONArray();
		if (StringUtils.hasText(ebeln)) {
			JSONObject ebelnObj = new JSONObject();
			ebelnObj.put("SIGN", "I");
			ebelnObj.put("OPTION", "EQ");
			ebelnObj.put("LOW", ebeln);
			ebelnObj.put("HIGH", "");
			I_EBELN.add(ebelnObj);
			params.put("I_EBELN", I_EBELN);
		}

		params.put("I_IMPORT", I_IMPORT);

		params.put("i_state", "3");
		JSONObject returnObj = new JSONObject();
		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_03?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		} catch (Exception e) {
			logger.error("", e);
			// e.printStackTrace();
		}
		JSONArray contractAry = null;
		JSONArray contractItemAry = null;
		try {
			contractAry = returnObj.getJSONArray("ET_HEAD");
		} catch (Exception e) {

		}
		try {
			contractItemAry = returnObj.getJSONArray("ET_ITEM");
		} catch (Exception e) {

		}
		if (contractAry != null && contractAry.size() == 1) {
			JSONObject contractObj = (JSONObject) contractAry.get(0);
			contractInfo.put("lifnr", contractObj.getString("LIFNR"));
			contractInfo.put("aedat", contractObj.getString("AEDAT"));// 创建日期
			contractInfo.put("bukrs", contractObj.getString("BUKRS"));// 使用单位
			contractInfo.put("butxt", contractObj.getString("BUTXT"));
			contractInfo.put("bsart", contractObj.getString("BSART"));// 订单类型
			contractInfo.put("batxt", contractObj.getString("BATXT"));
			contractInfo.put("ekorg", contractObj.getString("EKORG"));
			contractInfo.put("ekgrp", contractObj.getString("EKGRP"));
			contractInfo.put("name1", contractObj.getString("NAME1"));// 供应商名称
			contractInfo.put("zhtbh", contractObj.getString("ZHTBH"));
			contractInfo.put("zhtms", contractObj.getString("ZHTMC"));
			contractInfo.put("ekotx", contractObj.getString("EKOTX"));
			contractInfo.put("eknam", contractObj.getString("EKNAM"));
			contractInfo.put("ebeln", contractObj.getString("EBELN"));
			contractInfo.put("zhtlx", contractObj.getString("ZHTLX"));
		}
		ArrayList<HashMap<String, Object>> shdiList = new ArrayList<HashMap<String, Object>>();
		if (contractItemAry != null) {
			for (int i = 0; i < contractItemAry.size(); i++) {
				JSONObject contractObj = (JSONObject) contractItemAry.get(i);
				HashMap<String, Object> contractItem = new HashMap<String, Object>();
				contractItem.put("ebeln", contractObj.getString("EBELN"));// 订单类型
				contractItem.put("zhtbh", contractInfo.get("zhtbh"));
				contractItem.put("zhtms", contractInfo.get("zhtms"));
				contractItem.put("ebelp", contractObj.getString("EBELP"));
				contractItem.put("werks", contractObj.getString("WERKS"));
				contractItem.put("lgort", contractObj.getString("LGORT"));
				contractItem.put("lgobe", contractObj.getString("LGOBE"));
				contractItem.put("matnr", contractObj.getString("MATNR"));
				contractItem.put("txz01", contractObj.getString("TXZ01"));
				contractItem.put("meins", contractObj.getString("MEINS"));
				contractItem.put("msehl", contractObj.getString("MSEHL"));
				contractItem.put("andec", Byte.parseByte(contractObj.getString(("ANDEC"))));
				contractItem.put("zddsl", new BigDecimal(contractObj.getString("MENGE")));
				contractItem.put("zrksl", new BigDecimal(contractObj.getString("SJNUM")));
				contractItem.put("zsccj", contractObj.getString("AFNAM"));
				contractItem.put("zposid", contractObj.getString("POSID"));
				contractItem.put("zpost1", contractObj.getString("ZPOST1"));
				contractItem.put("sobkz", contractObj.getString("SOBKZ"));
				contractItem.put("zxqbu", contractObj.getString("TXT50"));
				contractItem.put("ztskc", contractObj.getString("ZTSKC"));
				contractItem.put("maktl", contractObj.getString("MATKL"));// 物料组
				contractItem.put("ztsms", contractObj.getString("ZTSMS"));
				contractItem.put("eindt", contractObj.getString("EINDT"));
				contractItem.put("bednr", contractObj.getString("BEDNR"));
				BigDecimal zddsl = new BigDecimal(contractObj.getString("MENGE"));
				BigDecimal zrksl = new BigDecimal(contractObj.getString("SJNUM"));

				String RETPO = contractObj.getString("RETPO");
				if (!StringUtils.hasText(RETPO) && zddsl.compareTo(zrksl) == 1) {
					shdiList.add(contractItem);
				}
			}
		}
		contractInfo.put("shdiList", shdiList);

		return contractInfo;
	}

}
