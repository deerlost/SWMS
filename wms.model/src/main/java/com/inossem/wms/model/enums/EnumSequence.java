package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumSequence {

	ALLOCATE("调拨单", "allocate"), 
	ALLOCATE_DELIVERY("调拨配送单", "allocate_delivery"), 
	BATCH("批次", "batch"),
	DELIVERY_NOTICE("送货通知单", "delivery_notice"),
	INSPECT_NOTICE("验收通知单", "inspect_notice"),
	MAT_REQ("领料单", "mat_req"),
	OUTPUT_RETURN("退货单", "output_return"),
	STOCKTAKE("库存盘点单", "stocktake"),
	STOCK_INPUT("入库单", "stock_input"),
	STOCK_OUPUT("出库单", "stock_ouput"),
	STOCK_TASK("仓储作业单", "stock_task"),
	STOCK_TASK_REQ("作业请求单", "stock_task_req"),
	STOCK_TRANSPORT("转储单", "stock_transport"),
	URGENCE("紧急出入库单", "urgence");
	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;

	private EnumSequence(String name, String value) {
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
			EnumSequence[] ary = EnumSequence.values();
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
			EnumSequence[] ary = EnumSequence.values();
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
