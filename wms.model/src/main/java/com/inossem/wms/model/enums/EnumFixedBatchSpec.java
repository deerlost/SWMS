package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumFixedBatchSpec {

	// 成功
	PRODUCTION_TIME("生产日期", "production_time", (byte)2),
	// 失败
	VALIDITY_TIME("有效期", "validity_time", (byte)2),
	// 异常
	CONTRACT_CODE("合同编号", "contract_code" , (byte)1),
	// 工作流错误
	CONTRACT_NAME("合同描述", "contract_name" ,(byte)1 ),

	PURCHASE_ORDER_CODE("采购订单号", "purchase_order_code",(byte)1),

	PURCHASE_ORDER_RID("采购订单行项目号", "purchase_order_rid",(byte)1),
	// 只能删除草稿
	DEMAND_DEPT("需求部门", "demand_dept",(byte)1);



	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;
	
	private Byte type;
	

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	private EnumFixedBatchSpec(String name, String value,Byte type) {
		this.name = name;
		this.value = value;
		this.type = type;
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
	private static Map<String, Byte> typeMap;
	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumFixedBatchSpec[] ary = EnumFixedBatchSpec.values();
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
			EnumFixedBatchSpec[] ary = EnumFixedBatchSpec.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}
	
	public static Map<String, Byte> toTypeMap() {
		if (typeMap == null) {
			EnumFixedBatchSpec[] ary = EnumFixedBatchSpec.values();
			Map<String, Byte> enumMap = new HashMap<String, Byte>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getType());
			}
			typeMap = enumMap;
		}
		return typeMap;
	}
	

	public static String getNameByValue(String value) {
		return toMap().get(value);
	}

	public static Byte getTypeByValue(String value) {
		return toTypeMap().get(value);
	}
}
