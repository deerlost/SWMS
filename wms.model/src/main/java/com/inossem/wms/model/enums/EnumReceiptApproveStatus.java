package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumReceiptApproveStatus {

	// 审批人审批完成后 审批状态 默认
	RECEIPT_STATUS_DEFAULT("默认", (byte) 0),
	// 审批人审批完成后 审批状态 通过
	RECEIPT_STATUS_APPROVE("通过", (byte) 1),
	// 审批人审批完成后 审批状态 不通过
	RECEIPT_STATUS_REJECT("不通过", (byte) 2),
	// 审批人审批完成后 审批状态 其他人不通过
	RECEIPT_STATUS_OTHER_REJECT("其他人不通过", (byte) 3),
	// 审批人审批完成后 审批状态 其他人通过
	RECEIPT_STATUS_OTHER_APPROVE("其他人通过", (byte) 4);

	/** 描述 */
	private String name;
	/** 枚举值 */
	private Byte value;

	private EnumReceiptApproveStatus(String name, Byte value) {
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
			EnumReceiptApproveStatus[] ary = EnumReceiptApproveStatus.values();
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
			EnumReceiptApproveStatus[] ary = EnumReceiptApproveStatus.values();
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
