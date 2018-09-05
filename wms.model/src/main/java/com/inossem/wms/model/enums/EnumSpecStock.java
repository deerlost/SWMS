package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 特殊库存
 * 
 * @author 刘宇 2018.02.07
 *
 */
public enum EnumSpecStock {

	STOCK_BATCH_STATUS_PROJECT("项目库存", "Q"),

	STOCK_BATCH_STATUS_CONSIGNMENT("寄售库存", "K"),

	STOCK_BATCH_STATUS_SALE("销售库存", "E"),

	STOCK_BATCH_STATUS_NORMAL("非特殊库存", ""),

	STOCK_BATCH_STATUS_SUBPACKAGE("分包库存", "O");

	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;

	private EnumSpecStock(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private static List<Map<String, String>> list;
	private static Map<String, String> map;

	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumSpecStock[] ary = EnumSpecStock.values();
			List<Map<String, String>> listTmp = new ArrayList<Map<String, String>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", String.valueOf(ary[i].getValue()));
				map.put("name", ary[i].getName());
				listTmp.add(map);
			}
			list = listTmp;
		}
		return list;
	}

	public static Map<String, String> toMap() {
		if (map == null) {
			EnumSpecStock[] ary = EnumSpecStock.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getNameByValue(String value) {
		return toMap().get(value);
	}
}
