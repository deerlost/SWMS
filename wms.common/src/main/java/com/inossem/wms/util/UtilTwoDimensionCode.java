package com.inossem.wms.util;

import java.util.HashMap;

/**
 * 二维码解析
 * 
 * @author 666
 *
 */
public class UtilTwoDimensionCode {

	/**
	 * 解析二维码 success = true 解析成功 charg 批次编码 matnr 物料编码
	 * 
	 * @param condition
	 * @return
	 */
	public static HashMap<String, Object> getDate(String condition) {
		boolean success = false;
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		try {
			String conditions[] = condition.split("@@");

			if (conditions != null && conditions.length > 0) {
				for (int i = 0; i < conditions.length; i++) {
					String key_value = conditions[i];
					String key_values[] = key_value.split(":");
					String key = key_values[0];
					String value = key_values[1];
					conditionMap.put(key, value);
				}
				success = true;
			}

		} catch (Exception e) {
			success = false;
		}
		conditionMap.put("success", success);
		return conditionMap;
	}

	/**
	 * 存储区-仓位拆分
	 * lgtyp 存储区
	 * lgpla 仓位
	 * @param condition
	 * @return
	 */
	public static HashMap<String, Object> getLGTYPandLGPLA(String condition) {
		String lgtyp = "";
		String lgpla = "";
		String whCode = "";
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		try {
			String stringBuff = condition;
			whCode = stringBuff.substring(0, stringBuff.indexOf("-"));
			stringBuff = stringBuff.substring(stringBuff.indexOf("-") + 1, stringBuff.length());
			lgtyp = stringBuff.substring(0, stringBuff.indexOf("-"));
			lgpla = stringBuff.substring(stringBuff.indexOf("-") + 1, stringBuff.length());
		} catch (Exception e) {
		}
		conditionMap.put("whCode", whCode);
		conditionMap.put("lgtyp", lgtyp);
		conditionMap.put("lgpla", lgpla);
		return conditionMap;
	}

}
