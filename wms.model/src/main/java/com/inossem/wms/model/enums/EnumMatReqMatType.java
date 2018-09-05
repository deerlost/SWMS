package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumMatReqMatType {
	MAT_REQ_MAT_TYPE_SPARE_PARTS_MATERIALS("备件、材料（除支护矿务工程）", (byte) 51), // 备件、材料（除支护矿务工程）
	MAT_REQ_MAT_TYPE_ENGINEERING_MATERIAL("支护、矿务工程材料+其他", (byte) 52), // 支护、矿务工程材料+其他
	MAT_REQ_MAT_TYPE_EQUIPMENT("设备", (byte) 53), // 设备
	MAT_REQ_MAT_TYPE_TAPE_CABLE_MONOMER("胶带/电缆/单体+设备资产", (byte) 54), // 胶带/电缆/单体+设备资产
	MAT_REQ_MAT_TYPE_LARGE_PART("大型部件", (byte) 55), // 大型部件
	MAT_REQ_MAT_TYPE_R_D_COST("研发类费用支出", (byte) 56);// 研发类费用支出

	/** 描述 */
	private String name;
	/** 枚举值 */
	private byte value;

	private EnumMatReqMatType(String name, byte value) {
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
			EnumMatReqMatType[] ary = EnumMatReqMatType.values();
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
