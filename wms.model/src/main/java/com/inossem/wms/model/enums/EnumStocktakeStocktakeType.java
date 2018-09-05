package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点 盘点方法：1-明盘，2-盲盘
 * @author wang.ligang
 *
 */
public enum EnumStocktakeStocktakeType {
	/** 明盘 **/
	OPEN_STOCKTAKE("明盘", "0"),
	
	/** 盲盘 **/
	BLACK_STOCKTAKE("盲盘", "10");

	/** 描述 */
	private String desc;
	/** 枚举值 */
	private String value;

	private EnumStocktakeStocktakeType(String desc, String value) {
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
	
	public static EnumStocktakeStocktakeType getEnum(String value){
		EnumStocktakeStocktakeType resultEnum = null;
		EnumStocktakeStocktakeType[] enumAry = EnumStocktakeStocktakeType.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue().equals(value)){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumStocktakeStocktakeType[] ary = EnumStocktakeStocktakeType.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", ary[num].getValue());
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		EnumStocktakeStocktakeType[] ary = EnumStocktakeStocktakeType.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value",ary[i].getValue());
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static String getNameByValue(String value) {
		String ret = "";
		List<Map<String,String>> list = EnumStocktakeStocktakeType.toList();
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
