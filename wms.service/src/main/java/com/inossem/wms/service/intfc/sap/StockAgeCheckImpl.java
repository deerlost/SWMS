package com.inossem.wms.service.intfc.sap;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.inossem.wms.constant.Const;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.model.enums.EnumSap;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IStockAgeCheck;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilREST;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 库龄考核查询连接SAP获取移动平均价功能实现
 * 
 * @author 刘宇 2018.02.26
 *
 */
@Transactional
@Service("StockAgeCheckService")
public class StockAgeCheckImpl implements IStockAgeCheck {
	private static final Logger logger = LoggerFactory.getLogger(StockAgeCheckImpl.class);

	@Autowired
	private IDictionaryService dictionaryService;
	
	@Override
	public JSONObject getVerprBySap(List<Map<String, Object>> list, String ftyId) throws Exception {
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = new JSONObject();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(date);
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);
		I_IMPORT.put("ZTYPE", "19");
		I_IMPORT.put("ZERNAM", "MMGW01");
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		params.put("I_IMPORT", I_IMPORT);

		JSONArray matnrAry = new JSONArray();
		String ftyCode = "";
		if(StringUtils.hasText(ftyId)){
			ftyCode = dictionaryService.getFtyCodeByFtyId(Integer.parseInt(ftyId));
		}
		
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> temp = list.get(i);

			JSONObject matnrJson = new JSONObject();
			matnrJson.put("SIGN", "I");
			matnrJson.put("OPTION", "EQ");
			matnrJson.put("LOW", temp.get("mat_code"));
			matnrAry.add(matnrJson);
		}

		params.put("I_MATNR", matnrAry);
		if (!"".equals(ftyId)) {
			
			params.put("I_WERKS", ftyCode);
		}
		JSONObject returnObj = new JSONObject();

		try {
			returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_19?sap-client=" + Const.SAP_API_CLIENT,
					params, 300);
		} catch (Exception e) {
			logger.error("库存积压价格", e);
		}

		JSONObject body = new JSONObject();
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();

		if (returnObj == null) {
			throw new InterfaceCallException();
		} else if (returnObj.getString("RETURNVALUE").equalsIgnoreCase("S")) {
			JSONArray material = returnObj.getJSONArray("E_MATERIAL");
			for (int i = 0; i < material.size(); i++) {
				JSONObject m = material.getJSONObject(i);
				map.put(m.getString("MATNR") + "-" + m.getString("WERKS"), UtilJSON.getBigDecimalFromJSON("VERPR", m));
			}
		} else {
			throw new InterfaceCallException(
					returnObj.getJSONObject(EnumSap.RETURN.getName()).getString(EnumSap.MESSAGE.getName()));
		}

		body.put("RETURNVALUE", returnObj.getString("RETURNVALUE"));
		body.put("map", map);
		return body;
	}

}
