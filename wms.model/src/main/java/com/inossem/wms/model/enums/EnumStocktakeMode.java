package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点方法
 */
public enum EnumStocktakeMode {

	POSITION("存储区", "0"),
	
	MAT("物料", "10");

	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;

	private EnumStocktakeMode(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static EnumStocktakeMode getEnum(String value){
		EnumStocktakeMode resultEnum = null;
		EnumStocktakeMode[] enumAry = EnumStocktakeMode.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue().equals(value)){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumStocktakeMode[] ary = EnumStocktakeMode.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", ary[num].getValue());
			map.put("name", ary[num].getName());
			enumMap.put(key, map);
		}
		return enumMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		EnumStocktakeMode[] ary = EnumStocktakeMode.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value",ary[i].getValue());
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static String getNameByValue(String value) {
		String ret = "";
		List<Map<String,String>> list = EnumStocktakeMode.toList();
		for (Map<String,String> temp : list) {
			String key = temp.get("value");
			if (value != null && value.equals(key)) {
				ret = temp.get("name");
				break;
			}
		}
		return ret;
	}
}
