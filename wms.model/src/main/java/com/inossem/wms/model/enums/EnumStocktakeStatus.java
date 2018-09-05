package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点 盘点表状态:0-草稿,10-已提交,20-已完成
 */
public enum EnumStocktakeStatus {

	DRAFT("草稿", "0"),
	
	INCOMPLETE("已提交", "10"),
	
	COMPLETED("已完成", "20");

	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;

	private EnumStocktakeStatus(String name, String value) {
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
	
	public static EnumStocktakeStatus getEnum(String value){
		EnumStocktakeStatus resultEnum = null;
		EnumStocktakeStatus[] enumAry = EnumStocktakeStatus.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue().equals(value)){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumStocktakeStatus[] ary = EnumStocktakeStatus.values();
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
		EnumStocktakeStatus[] ary = EnumStocktakeStatus.values();
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
		List<Map<String,String>> list = EnumStocktakeStatus.toList();
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
