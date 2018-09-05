package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumReceiptStatus {
	// 未完成
	RECEIPT_UNFINISH("未完成", (byte) 0),
	// 已完成
	RECEIPT_FINISH("已完成", (byte) 10),
	// 草稿
	RECEIPT_DRAFT("草稿", (byte) 1),
	// 已提交
	RECEIPT_SUBMIT("已提交", (byte) 2),
	// 已作业
	RECEIPT_WORK("已作业", (byte) 3),
	// 已关联
	RECEIPT_ISUSED("已关联",(byte)4),
	// 已转储
	RECEIPT_TRANSPORT("已转储",(byte) 5),
	// 已拣货 
	RECEIPT_SORTING("已拣货",(byte)6),
	// 待出库
	RECEIPT_BEOUTPUT("待出库",(byte)7),
	// 已开票
	RECEIPT_INVOICED("已开票",(byte)8),
	// 已冲销
	RECEIPT_WRITEOFF("已冲销", (byte) 20);
	/** 描述 */
	private String name;
	/** 枚举值 */
	private Byte value;


	private EnumReceiptStatus(String name, Byte value) {
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
			EnumReceiptStatus[] ary = EnumReceiptStatus.values();
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
			EnumReceiptStatus[] ary = EnumReceiptStatus.values();
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
