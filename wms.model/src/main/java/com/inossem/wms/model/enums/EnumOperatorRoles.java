package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumOperatorRoles {

	// 成功
	OPERATOR_WAREHOUSE_SUPERVISOR("库房主管", 14),
	// 失败
	OPERATOR_PICKER("领料人", 35),
	// 异常
	OPERATOR_QUALITY_ACCEPTANCE("质量验收人", 36);

	/** 描述 */
	private String roleName;
	/** 枚举值 */
	private Integer roleId;

	private EnumOperatorRoles(String roleName, Integer roleId) {
		this.roleName = roleName;
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	private static List<Map<String, String>> list;
	private static Map<Integer, String> map;

	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumOperatorRoles[] ary = EnumOperatorRoles.values();
			List<Map<String, String>> listTmp = new ArrayList<Map<String, String>>();
			for (int i = 0; i < ary.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("roleId", String.valueOf(ary[i].getRoleId()));
				map.put("roleName", ary[i].getRoleName());
				listTmp.add(map);
			}
			list = listTmp;
		}
		return list;
	}

	public static Map<Integer, String> toMap() {
		if (map == null) {
			EnumOperatorRoles[] ary = EnumOperatorRoles.values();
			Map<Integer, String> enumMap = new HashMap<Integer, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getRoleId(), ary[num].getRoleName());
			}
			map = enumMap;
		}
		return map;
	}

	public static String getRoleNameByRoleId(Integer value) {
		return toMap().get(value);
	}

}
