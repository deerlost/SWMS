package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumStockStatus {

	STOCK_BATCH_STATUS_UNRESTRICTED("非限制库存", "", (byte) 1),
	// 在途库存 SAP 对应T
	STOCK_BATCH_STATUS_ON_THE_WAY("在途库存", "T", (byte) 2),
	// 质量检验库存 SAP 对应X
	STOCK_BATCH_STATUS_QUALITY_INSPECTION("质量检验库存", "X", (byte) 3),
	// 冻结库存 SAP 对应S
	STOCK_BATCH_STATUS_FREEZE("冻结库存", "S", (byte) 4),
	// 已售未提
	STOCK_BATCH_STATUS_NO_MENTION("已售未提","",(byte) 5); 

	/** 描述 */
	private String name;

	private String code;

	/** 枚举值 */
	private Byte value;

	private EnumStockStatus(String name, String code, Byte value) {
		this.name = name;
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	private static List<Map<String, String>> list;
	private static Map<Byte, String> map;

	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumStockStatus[] ary = EnumStockStatus.values();
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

	public static Map<Byte, String> toMap() {
		if (map == null) {
			EnumStockStatus[] ary = EnumStockStatus.values();
			Map<Byte, String> enumMap = new HashMap<Byte, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getNameByValue(byte value) {
		return toMap().get(value);
	}
}
