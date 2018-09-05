package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumMatReqBizType {
	MAT_REQ_BIZ_TYPE_MINOR_REPAIRS("小修/日常维修", (byte) 1), // 小修/日常维修
	MAT_REQ_BIZ_TYPE_OVERHAUL("大项修/技改", (byte) 2), // 大项修/技改
	MAT_REQ_BIZ_TYPE_OTHER("其他", (byte) 3), // 其他
	MAT_REQ_BIZ_TYPE_EQUIPMENT_ASSETS("设备/资产", (byte) 4);// 设备/资产

	/** 描述 */
	private String name;
	/** 枚举值 */
	private byte value;

	private EnumMatReqBizType(String name, byte value) {
		this.name = name;
		this.value = value;
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
			EnumMatReqBizType[] ary = EnumMatReqBizType.values();
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
			EnumMatReqBizType[] ary = EnumMatReqBizType.values();
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
