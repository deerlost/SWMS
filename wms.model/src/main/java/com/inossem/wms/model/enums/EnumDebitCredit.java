package com.inossem.wms.model.enums;

import java.util.HashSet;
import java.util.Set;

public enum EnumDebitCredit {
	DEBIT_S_ADD("S"), CREDIT_H_SUBTRACT("H");

	/** 描述 */
	private String name;

	private EnumDebitCredit(String name) {
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
			EnumDebitCredit[] ary = EnumDebitCredit.values();
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
