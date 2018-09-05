package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumStocktakeSpecStock {
	/** 现有订单E **/
	XYDD("现有订单", "E"),
	
	/** 寄售(供应商)K **/
	JS("寄售(供应商)", "K"),
	
	/** 供应商分包库存 O **/
	FBKC("供应商分包库存", "O"),
	
	/** 项目库存 Q **/
	XMKC("项目库存", "Q");

	/** 描述 */
	private String desc;
	/** 枚举值 */
	private String value;

	private EnumStocktakeSpecStock(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static EnumStocktakeSpecStock getEnum(String value){
		EnumStocktakeSpecStock resultEnum = null;
		EnumStocktakeSpecStock[] enumAry = EnumStocktakeSpecStock.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue().equals(value)){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		return toMap(null, null);
	}
	
	public static Map<String, Map<String, Object>> toMap(String keyKey, String valueKey) {
		if (keyKey == null || keyKey.length() == 0) {
			keyKey = "value";
		}
		if (valueKey == null || valueKey.length() == 0) {
			valueKey = "desc";
		}
		EnumStocktakeSpecStock[] ary = EnumStocktakeSpecStock.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put(keyKey, ary[num].getValue());
			map.put(valueKey, ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}
	
	@SuppressWarnings({ "rawtypes"})
	public static List toList(){
		return toList(null, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(String keyKey, String valueKey){
		if (keyKey == null || keyKey.length() == 0) {
			keyKey = "value";
		}
		if (valueKey == null || valueKey.length() == 0) {
			valueKey = "desc";
		}
		EnumStocktakeSpecStock[] ary = EnumStocktakeSpecStock.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put(keyKey,ary[i].getValue());
			map.put(valueKey, ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static String getDescByValue(String value) {
		String ret = "";
		List<Map<String,String>> list = EnumStocktakeSpecStock.toList();
		for (Map<String,String> temp : list) {
			String key = temp.get("value");
			if (value != null && value.equals(key)) {
				ret = temp.get("desc");
				break;
			}
		}
		return ret;
	}

}
