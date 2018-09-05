package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumTaskReqShippingType {

	STOCK_INPUT("入库", "E"), STOCK_OUTPUT("出库", "A"), ACCOUNTS_CHANGE("记账变更", "U"), WAREHOUSE_MONITORING("仓库监控", "X");

	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;

	private EnumTaskReqShippingType(String name, String value) {
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
			EnumTaskReqShippingType[] ary = EnumTaskReqShippingType.values();
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
			EnumTaskReqShippingType[] ary = EnumTaskReqShippingType.values();
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
