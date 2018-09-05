package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumMatReqStatus {
	// 草稿
	MAT_REQ_STATUS_DRAFT("草稿", (byte) 0),
	// 已提交
	MAT_REQ_STATUS_SUBMITTED("已提交", (byte) 10),
	// 已审批
	MAT_REQ_STATUS_APPROVED("已通过", (byte) 20),
	// 驳回
	MAT_REQ_STATUS_REJECT("未通过", (byte) 30),
	// 已完成
	MAT_REQ_STATUS_COMPLETED("已完成", (byte) 40),
	// 已关闭
	MAT_REQ_STATUS_CLOSED("已关闭", (byte) 50);

	/** 描述 */
	private String name;
	/** 枚举值 */
	private byte value;

	private EnumMatReqStatus(String name, byte value) {
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
			EnumMatReqStatus[] ary = EnumMatReqStatus.values();
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
			EnumMatReqStatus[] ary = EnumMatReqStatus.values();
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
