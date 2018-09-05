package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点 库存状态：0-全部,10-只盘点非限制库存,20-只盘点冻结库存,30-只盘点寄售库存,40-只盘点项目库存,50-只盘点客户库存
 * @author wang.ligang
 *
 */
public enum EnumStocktakeStockStatus {
//	ALL("全部", "0"),
	
	ONLY_LIMIT_STOCK("只盘点非限制库存", "10");
	
//	ONLY_LOCKED_STOCK("只盘点冻结库存", "20"),
//	
//	ONLY_CONSIGNMENT("只盘点寄售库存", "30"),
//	
//	ONLY_PROJECT_STOCK("只盘点项目库存", "40"),
//	
//	ONLY_CUSTOMER_STOCK("只盘点客户库存", "50");

	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;

	private EnumStocktakeStockStatus(String name, String value) {
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
	
	public static EnumStocktakeStockStatus getEnum(String value){
		EnumStocktakeStockStatus resultEnum = null;
		EnumStocktakeStockStatus[] enumAry = EnumStocktakeStockStatus.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue().equals(value)){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumStocktakeStockStatus[] ary = EnumStocktakeStockStatus.values();
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
		EnumStocktakeStockStatus[] ary = EnumStocktakeStockStatus.values();
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
		List<Map<String,String>> list = EnumStocktakeStockStatus.toList();
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
