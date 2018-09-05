package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumOperatorTypes {

	// 入库
	OPERATOR_INPUT(EnumReceiptType.STOCK_INPUT.getName(), EnumReceiptType.STOCK_INPUT.getValue()),
	// 出库
	OPERATOR_OUTPUT(EnumReceiptType.STOCK_OUTPUT.getName(), EnumReceiptType.STOCK_OUTPUT.getValue()),
	// 转储
	OPERATOR_TRANSPORT(EnumReceiptType.STOCK_TRANSPORT.getName(), EnumReceiptType.STOCK_TRANSPORT.getValue());

	/** 描述 */
	private String typeName;
	/** 枚举值 */
	private Byte type;

	private EnumOperatorTypes(String typeName, Byte type) {
		this.typeName = typeName;
		this.type = type;
	}

	

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}



	private static List<Map<String, String>> list;
	private static Map<Byte, String> map;

	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumOperatorTypes[] ary = EnumOperatorTypes.values();
			List<Map<String, String>> listTmp = new ArrayList<Map<String, String>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("type", String.valueOf(ary[i].getType()));
				map.put("typeName", ary[i].getTypeName());
				listTmp.add(map);
			}
			list = listTmp;
		}
		return list;
	}

	public static Map<Byte, String> toMap() {
		if (map == null) {
			EnumOperatorTypes[] ary = EnumOperatorTypes.values();
			Map<Byte, String> enumMap = new HashMap<Byte, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getType(), ary[num].getTypeName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getTypeNameByTypeId(Byte value) {
		return toMap().get(value);
	}

}
