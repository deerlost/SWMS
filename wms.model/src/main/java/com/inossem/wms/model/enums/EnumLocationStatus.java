package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumLocationStatus {

	// 草稿
	LOCATION_STATUS_OFF_LINE("未上线库", (byte)0),
	// 已提交
	LOCATION_STATUS_ON_LINE("上线库", (byte)1),
	// 审批通过
	LOCATION_STATUS_ON_LINE_VIRTUAL ("上线虚拟库", (byte)2),
	// 驳回
	LOCATION_STATUS_OFF_LINE_VIRTUAL("未上线虚拟库", (byte)3);

	/** 描述 */
	private String name;
	/** 枚举值 */
	private Byte value;

	private EnumLocationStatus(String name, Byte value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getValue() {
		return value;
	}

	public void setValue(Byte value) {
		this.value = value;
	}

	private static List<Map<String, String>> list;
	private static Map<Byte, String> map;

	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumLocationStatus[] ary = EnumLocationStatus.values();
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
			EnumLocationStatus[] ary = EnumLocationStatus.values();
			Map<Byte, String> enumMap = new HashMap<Byte, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getNameByValue(Byte value) {
		return toMap().get(value);
	}
}
