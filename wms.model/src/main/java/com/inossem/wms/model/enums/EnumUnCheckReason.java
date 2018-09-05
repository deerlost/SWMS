package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 免检原因
 * @author 666
 *
 */
public enum EnumUnCheckReason {

	
	UN_CHECK_REASON_OFF_LINE_CHECKED("线下已验收", 0),
	
	UN_CHECK_REASON_LONG_COOPERATION("长协合作", 1),
	
	UN_CHECK_REASON_OUT_FACTORY_CEHCKED("出厂已质检", 2),

	UN_CHECK_REASON_THIRD_PARTY_CHECK("第三方质检", 3),
	
	UN_CHECK_REASON_DAILY_USE_UP("日常消耗品", 4),
	
	UN_CHECK_REASON_OTHER("其他原因", 5);

	/** 描述 */
	private String name;
	/** 枚举值 */
	private Integer value;

	private EnumUnCheckReason(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private static List<Map<String, String>> list;
	private static Map<Integer, String> map;

	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumUnCheckReason[] ary = EnumUnCheckReason.values();
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

	public static Map<Integer, String> toMap() {
		if (map == null) {
			EnumUnCheckReason[] ary = EnumUnCheckReason.values();
			Map<Integer, String> enumMap = new HashMap<Integer, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getNameByValue(Integer value) {
		return toMap().get(value);
	}

}
