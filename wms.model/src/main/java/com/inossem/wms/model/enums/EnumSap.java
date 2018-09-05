package com.inossem.wms.model.enums;

import java.util.HashSet;
import java.util.Set;

public enum EnumSap {
	RETURN_VALUE("RETURNVALUE"), RETURN("RETURN"), MESSAGE("MESSAGE"), RETURN_SUCCESS("S");

	/** 描述 */
	private String name;

	private EnumSap(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private static Set<String> set;

	public static Set<String> toSet() {
		if (set == null) {
			EnumSap[] ary = EnumSap.values();
			Set<String> enumSet = new HashSet<String>();
			for (int num = 0; num < ary.length; num++) {
				enumSet.add(ary[num].getName());
			}
			set = enumSet;
		}
		return set;
	}

	public static boolean contains(String name) {
		return toSet().contains(name);
	}
}
