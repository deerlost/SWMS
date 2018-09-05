package com.inossem.wms.service.intfc.sap;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.inossem.wms.constant.Const;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSap;
import com.inossem.wms.model.vo.BizMatReqHeadVo;
import com.inossem.wms.model.vo.BizMatReqItemVo;
import com.inossem.wms.service.intfc.IMatReq;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("matReqImpl")
public class MatReqImpl implements IMatReq {

	@Override
	public JSONArray getReferencePrice(String[] matCodes, String ftyCode, String userId) throws Exception {
		JSONArray ary = null;
		JSONObject mat = null;
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		Date date = new Date();
		String dateStr = UtilString.getLongStringForDate(date);
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);

		I_IMPORT.put("ZTYPE", "19");
		I_IMPORT.put("ZERNAM", userId);
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);

		JSONArray matAry = new JSONArray();

		for (String matCode : matCodes) {
			if (!UtilString.isNullOrEmpty(matCode)) {
				JSONObject matJson = new JSONObject();
				matJson.put("SIGN", "I");
				matJson.put("OPTION", "EQ");
				matJson.put("LOW", matCode);
				matAry.add(matJson);
			}
		}

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_MATNR", matAry);
		params.put("I_WERKS", ftyCode);
		params.put("I_MOUNT", "");

		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_19?sap-client" + Const.SAP_API_CLIENT, params, 10);
		if (returnObj != null && returnObj.getString("RETURNVALUE").equalsIgnoreCase("S")) {
			JSONArray materAry = returnObj.getJSONArray("E_MATERIAL");
			ary = new JSONArray();
			for (Object object : materAry) {
				JSONObject json = (JSONObject) object;
				mat = new JSONObject();
				mat.put("mat_code", json.getString("MATNR"));
				mat.put("refer_price", UtilJSON.getBigDecimalFromJSON("VERPR", json));
				ary.add(mat);
			}
		} else {
			ary = new JSONArray();
		}
		// [{"WERKS":"2000","MATNR":"60000022","MAKTX":"热轧碳素结构钢板/δ3/Q235B","MEINS":"KG","VERPR":3.32}]

		return ary;
	}

	@Override
	public String chechMatForOther(JSONArray ary, String user_id) throws Exception {
		// JSONArray lldiAry = null;
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);

		I_IMPORT.put("ZTYPE", "29");
		I_IMPORT.put("ZERNAM", user_id);
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_LLDXM", ary);

		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_29?sap-client=" + Const.SAP_API_CLIENT, params, 10);
		// s时数据符合要求
		// if (returnObj != null &&
		// returnObj.getString("RETURNVALUE").equalsIgnoreCase("S")) {
		// lldiAry = returnObj.getJSONArray("E_LLDXM");
		// }

		if (returnObj == null) {
			return String.format(Const.SAP_API_MSG_FORMAT, Const.SAP_API_MSG_PREFIX, "接口返回值为空");
		} else if (returnObj.getString(EnumSap.RETURN_VALUE.getName())
				.equalsIgnoreCase(EnumSap.RETURN_SUCCESS.getName())) {
			return null;
		} else {
			JSONObject jsonobj = returnObj.getJSONObject("RETURN");
			return String.format(Const.SAP_API_MSG_FORMAT, Const.SAP_API_MSG_PREFIX,
					jsonobj.getString(EnumSap.MESSAGE.getName()));
		}
	}

	@Override
	public JSONArray getCostCenter(String ftyCode, String costObjCode, String costObjname, String matCode,
			int costObjType, String userId) throws Exception {
		JSONArray ret = new JSONArray();
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);

		I_IMPORT.put("ZTYPE", "21");
		I_IMPORT.put("ZERNAM", userId);
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_WERKS", ftyCode.trim());
		params.put("I_CBBH", costObjCode.trim());
		params.put("I_KTEXT", costObjname.trim());
		params.put("I_MATNR", matCode.trim());
		params.put("I_CBLX", String.valueOf(costObjType));

		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_21?sap-client=" + Const.SAP_API_CLIENT, params, 10);
		if (returnObj != null && returnObj.getString(EnumSap.RETURN_VALUE.getName())
				.equalsIgnoreCase(EnumSap.RETURN_SUCCESS.getName())) {
			JSONArray csksAry = returnObj.getJSONArray("E_CSKS");
			for (Object object : csksAry) {
				JSONObject csks = (JSONObject) object;
				JSONObject retObj = new JSONObject();
				retObj.put("cost_obj_code", csks.get("ZCBBH"));
				retObj.put("cost_obj_name", csks.get("NAME1"));
				retObj.put("fty_code", csks.get("WERKS"));
				ret.add(retObj);
			}
		}
		// [{"ZCBBH":"2000010007","WERKS":"2000","NAME1":"财务部机关2"},{"ZCBBH":"2000010088","WERKS":"2000","NAME1":"工会2"},{"ZCBBH":"2000900003","WERKS":"2000","NAME1":"蒙KYT025"},{"ZCBBH":"2000900004","WERKS":"2000","NAME1":"蒙KYT026"},{"ZCBBH":"2000900005","WERKS":"2000","NAME1":"蒙KYT027"},{"ZCBBH":"2000900006","WERKS":"2000","NAME1":"蒙KYT028"},{"ZCBBH":"2000900007","WERKS":"2000","NAME1":"蒙KYT029"},{"ZCBBH":"2000900010","WERKS":"2000","NAME1":"蒙KYT032"},{"ZCBBH":"2000900013","WERKS":"2000","NAME1":"蒙KYT062"},{"ZCBBH":"2000900021","WERKS":"2000","NAME1":"蒙KJ5562"},{"ZCBBH":"2000900022","WERKS":"2000","NAME1":"蒙K99072"},{"ZCBBH":"2000900025","WERKS":"2000","NAME1":"蒙KD9255"},{"ZCBBH":"2000900026","WERKS":"2000","NAME1":"蒙KD9266"},{"ZCBBH":"2000900027","WERKS":"2000","NAME1":"蒙KD9267"},{"ZCBBH":"2000900029","WERKS":"2000","NAME1":"蒙KZ9288"},{"ZCBBH":"2000900032","WERKS":"2000","NAME1":"蒙K60002"},{"ZCBBH":"2000900033","WERKS":"2000","NAME1":"蒙KV8205"},{"ZCBBH":"2000900035","WERKS":"2000","NAME1":"蒙K82863"},{"ZCBBH":"2000900038","WERKS":"2000","NAME1":"蒙K00032"},{"ZCBBH":"2000900041","WERKS":"2000","NAME1":"蒙K99032"},{"ZCBBH":"2000900042","WERKS":"2000","NAME1":"蒙KZ0032"},{"ZCBBH":"2000900043","WERKS":"2000","NAME1":"蒙KYX626"},{"ZCBBH":"2000900045","WERKS":"2000","NAME1":"西营子计划20899"},{"ZCBBH":"2000900046","WERKS":"2000","NAME1":"西营子站台20897"},{"ZCBBH":"2000900047","WERKS":"2000","NAME1":"虎石站台20089"},{"ZCBBH":"2000900048","WERKS":"2000","NAME1":"化验室26206"},{"ZCBBH":"2000900049","WERKS":"2000","NAME1":"联防队26207"},{"ZCBBH":"2000900053","WERKS":"2000","NAME1":"蒙K.XZ002"},{"ZCBBH":"2000900055","WERKS":"2000","NAME1":"蒙K.X3217"},{"ZCBBH":"2000900056","WERKS":"2000","NAME1":"蒙K.6F292"},{"ZCBBH":"2000900058","WERKS":"2000","NAME1":"蒙K.G2863"},{"ZCBBH":"2000900059","WERKS":"2000","NAME1":"蒙K.H5432"},{"ZCBBH":"2000900062","WERKS":"2000","NAME1":"蒙K.6F237"},{"ZCBBH":"2000900072","WERKS":"2000","NAME1":"蒙KJ5562"},{"ZCBBH":"2000900078","WERKS":"2000","NAME1":"蒙K2F095"},{"ZCBBH":"2000900084","WERKS":"2000","NAME1":"蒙KYT289"},{"ZCBBH":"2000900098","WERKS":"2000","NAME1":"蒙K28M80"},{"ZCBBH":"2000900099","WERKS":"2000","NAME1":"蒙KYT289"}]
		return ret;
	}

	@Override
	public JSONArray getCostObject(String moveType, String ftyCode, String costObjCode, String costObjName,
			String userId) throws Exception {
		JSONArray cbdxAry = null;
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);

		I_IMPORT.put("ZTYPE", "25");
		I_IMPORT.put("ZERNAM", userId);
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_BWART", moveType);
		params.put("I_WERKS", ftyCode);
		params.put("I_CBBH", costObjCode);
		params.put("I_KTEXT", costObjName);

		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_25?sap-client=" + Const.SAP_API_CLIENT, params, 10);
		if (returnObj != null && returnObj.getString("RETURNVALUE").equalsIgnoreCase("S")) {
			cbdxAry = returnObj.getJSONArray("E_CBDX");
		}

		return cbdxAry;
	}

	@Override
	public String checkWorkReceiptCode(List<Map<String, Object>> list, String userId) throws Exception {
		JSONArray ary = new JSONArray();
		JSONObject obj;
		for (Map<String, Object> map : list) {
			obj = new JSONObject();
			obj.put("ZLLDXH", map.get("mat_req_rid"));
			obj.put("MATNR", map.get("mat_code"));
			obj.put("WERKS", map.get("mat_req_fty_code"));
			obj.put("AUFNR", map.get("work_receipt_code"));
			ary.add(obj);
		}

		// JSONArray lldiAry = null;
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);

		I_IMPORT.put("ZTYPE", "29");
		I_IMPORT.put("ZERNAM", userId);
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_LLDXM", ary);

		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_29?sap-client=" + Const.SAP_API_CLIENT, params, 10);
		// s时数据符合要求
		// if (returnObj != null &&
		// returnObj.getString("RETURNVALUE").equalsIgnoreCase("S")) {
		// lldiAry = returnObj.getJSONArray("E_LLDXM");
		// }
		if (returnObj == null) {
			return String.format(Const.SAP_API_MSG_FORMAT, Const.SAP_API_MSG_PREFIX, "接口返回值为空");
		} else if (returnObj.getString(EnumSap.RETURN_VALUE.getName())
				.equalsIgnoreCase(EnumSap.RETURN_SUCCESS.getName())) {
			return null;
		} else {
			JSONObject jsonobj = returnObj.getJSONObject("RETURN");
			return String.format(Const.SAP_API_MSG_FORMAT, Const.SAP_API_MSG_PREFIX, jsonobj.getString("MESSAGE"));
		}
	}

	@Override
	public String submitMatReq(BizMatReqHeadVo bizMatReqHeadVo, List<BizMatReqItemVo> itemList, User createUser,
			User firstApproveUser, User lastApproveUser) throws Exception {

		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();
		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);

		I_IMPORT.put("ZTYPE", "07");
		I_IMPORT.put("ZERNAM", bizMatReqHeadVo.getCreateUser());
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		I_IMPORT.put("ZDJBH", bizMatReqHeadVo.getMatReqCode());

		JSONArray E_HEADs = new JSONArray();
		try {
			JSONObject E_HEAD = new JSONObject();
			E_HEAD.put("ZLLDH", String.valueOf(bizMatReqHeadVo.getMatReqCode()));
			E_HEAD.put("ZLLFL",
					bizMatReqHeadVo.getReceiptType() == EnumReceiptType.MAT_REQ_PRODUCTION.getValue() ? "1" : "2");
			// 5）原领料单的提交接口07中的字段PLANT取“接收方工厂”；
			E_HEAD.put("PLANT", bizMatReqHeadVo.getReceiveFtyCode());
			E_HEAD.put("WERKS", bizMatReqHeadVo.getMatReqFtyCode());

			E_HEAD.put("RES_DATE", UtilString.getShortStringForDate(bizMatReqHeadVo.getCreateTime()));
			E_HEAD.put("CREATED_BY", bizMatReqHeadVo.getCreateUser());
			E_HEAD.put("ZCJBM", bizMatReqHeadVo.getDeptCode());
			E_HEAD.put("ZWLLX", String.valueOf(bizMatReqHeadVo.getMatReqMatTypeId()));
			if (itemList.size() > 0) {
				E_HEAD.put("MOVE_TYPE", itemList.get(0).getMoveTypeCode());
			} else {
				E_HEAD.put("MOVE_TYPE", "201");
			}
			E_HEAD.put("ZNBDD", bizMatReqHeadVo.getInternalOrderCode());
			E_HEAD.put("ZYWLX", String.valueOf(bizMatReqHeadVo.getMatReqBizTypeId()));
			E_HEAD.put("ZZJBS", bizMatReqHeadVo.getIsBuildingProject());// 在建工程标识
			E_HEAD.put("ZZBZ", bizMatReqHeadVo.getRemark());// 备注
			//E_HEAD.put("ZCBS", bizMatReqHeadVo.getContractor());// 承包商
			//2018-04-14添加
			//旧有的承包商一直没有传值,一直是""
			//旧有的领用区队/部门一直没有传给sap
			//暂定把领用区队/部门使用承包商字段传输
			E_HEAD.put("ZCBS", bizMatReqHeadVo.getUseDeptCode());// 领用区队/部门
			E_HEAD.put("ZPOSID", bizMatReqHeadVo.getInternalOrderCode());
			E_HEAD.put("ZORDERANUM", createUser.getUserId()); // 提交人帐号”,
			E_HEAD.put("ZORDERANAME", createUser.getUserName());// 提交人名称”,
			E_HEAD.put("ZUUNUM", firstApproveUser.getUserId()); // 审核人帐号”,
			E_HEAD.put("ZUUNAME", firstApproveUser.getUserName()); // 审核人名称”,
			E_HEAD.put("ZFNUM", lastApproveUser.getUserId()); // 财务审核人帐号”,
			E_HEAD.put("ZFNAME", lastApproveUser.getUserName());// 财务审核人名称”

			E_HEADs.add(E_HEAD);
		} catch (Exception e) {
			throw e;
		}
		JSONArray E_ITEMs = new JSONArray();
		try {
			for (BizMatReqItemVo bizMatReqItemVo : itemList) {
				JSONObject E_ITEM = new JSONObject();
				E_ITEM.put("ZLLDH", String.valueOf(bizMatReqHeadVo.getMatReqCode()));
				E_ITEM.put("ZLLDXH", String.valueOf(bizMatReqItemVo.getMatReqRid()));
				E_ITEM.put("PLANT", bizMatReqHeadVo.getReceiveFtyCode());
				E_ITEM.put("WERKS", bizMatReqHeadVo.getMatReqFtyCode());
				E_ITEM.put("STORE_LOC", bizMatReqItemVo.getLocationCode());
				E_ITEM.put("BWART", bizMatReqItemVo.getMoveTypeCode());// 移动类型
				E_ITEM.put("SOBKZ", bizMatReqItemVo.getSpecStock());// 特殊库存标识
				E_ITEM.put("MATERIAL", bizMatReqItemVo.getMatCode());// 物料编码
				E_ITEM.put("QUANTITY", bizMatReqItemVo.getDemandQty());// 需求数量
				E_ITEM.put("REQ_DATE", UtilString.getShortStringForDate(bizMatReqItemVo.getDemandDate()));// 需求日期
				E_ITEM.put("POSID", "");//
				E_ITEM.put("SAKNR", bizMatReqItemVo.getGeneralLedgerSubject());// 总账科目
				E_ITEM.put("AUFNR", bizMatReqItemVo.getWorkReceiptCode());// 工单号
				E_ITEM.put("EQUNR", bizMatReqItemVo.getDeviceCode());// 设备号
				E_ITEM.put("ANLKL", String.valueOf(bizMatReqItemVo.getAssetAttr()));// 资产管控分类
				E_ITEM.put("SHORT_TEXT", bizMatReqItemVo.getRemark());// 备注/使用用途
				E_ITEM.put("ZPOSID", bizMatReqItemVo.getCostObjCode());// 成本对象
				E_ITEMs.add(E_ITEM);
			}

		} catch (Exception e) {
			throw e;
		}
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_ZLLDH", bizMatReqHeadVo.getMatReqCode());
		params.put("E_HEAD", E_HEADs);
		params.put("E_ITEM", E_ITEMs);
		JSONObject returnObj = new JSONObject();
		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_07?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
			if (returnObj == null) {
				// return String.format(Const.SAP_API_MSG_FORMAT,
				// Const.SAP_API_MSG_PREFIX, "接口返回值为空");
				throw new InterfaceCallException("接口返回值为空");
			} else if (returnObj.getString(EnumSap.RETURN_VALUE.getName())
					.equalsIgnoreCase(EnumSap.RETURN_SUCCESS.getName())) {
				JSONArray E_STOs = returnObj.getJSONArray("E_STO");
				if (E_STOs != null) {
					for (Object stoObj : E_STOs) {
						JSONObject E_STO = (JSONObject) stoObj;
						// String matReqCode = E_STO.getString("ZLLDH");
						int rid = Integer.parseInt(E_STO.getString("ZLLDXH"));
						String reserveCode = E_STO.getString("RSNUM");
						String reserveRid = E_STO.getString("RSPOS");
						// 忽略掉matReqCode条件.因为肯定是一个领料单
						// 直接方法对象中,该对象是参数list内的一个对象,在方法外面可以直接访问
						BizMatReqItemVo item = itemList.stream().filter(itemTmp -> itemTmp.getMatReqRid() == rid)
								.findFirst().get();
						// 添加sap的领料单编号和行号
						item.setReserveCode(reserveCode);
						item.setReserveRid(reserveRid);
						if (E_STO.containsKey("EBELN") && E_STO.containsKey("EBELP")) {
							// 添加sap对应的采购订单编号和行号
							String purchaseOrderCode = E_STO.getString("EBELN");
							String purchaseOrderRid = E_STO.getString("EBELP");
							item.setPurchaseOrderCode(purchaseOrderCode);
							item.setPurchaseOrderRid(purchaseOrderRid);
						}

					}
					return null;
				} else {
					// return String.format(Const.SAP_API_MSG_FORMAT,
					// Const.SAP_API_MSG_PREFIX, "接口返回预留单采购订单为空");
					throw new InterfaceCallException("接口返回预留单采购订单为空");
				}

			} else {
				JSONObject jsonobj = returnObj.getJSONObject("RETURN");
				// return String.format(Const.SAP_API_MSG_FORMAT,
				// Const.SAP_API_MSG_PREFIX, jsonobj.getString("MESSAGE"));
				throw new InterfaceCallException(String.format(Const.SAP_API_MSG_FORMAT, Const.SAP_API_MSG_PREFIX,
						jsonobj.getString("MESSAGE")));
			}
		} catch (Exception e) {
			throw e;
		}

		// return null;
	}
}
