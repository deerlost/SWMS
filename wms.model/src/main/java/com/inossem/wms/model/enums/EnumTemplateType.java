package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板类型
 * @author 666
 *
 */
public enum EnumTemplateType {

	TEMPLATE_MAT_REQ_MATERIAL_LIST("领料单", "mat_req_material","MatReqMaterialList.xls","MatReqMaterialList"),
	
	TEMPLATE_MATERIAL("物料", "material","MaterialImport.xls","MaterialImport"),

	TEMPLATE_POSITION("仓位", "position","PositionImport.xls","PositionImport"),

	TEMPLATE_STOCK_POSITION("仓位库存", "stock_position","StockPositionImport.xls","StockPositionImport");



	/** 描述 */
	private String name;
	/** 枚举值 */
	private String value;
	
	private String downLoadName;
	
	private String folder;

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getDownLoadName() {
		return downLoadName;
	}

	public void setDownLoadName(String downLoadName) {
		this.downLoadName = downLoadName;
	}

	private EnumTemplateType(String name, String value,String downLoadName,String folder) {
		this.name = name;
		this.value = value;
		this.downLoadName = downLoadName;
		this.folder = folder;
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
	private static Map<String, String> downLoadNameMap;
	private static Map<String, String> folderNameMap;
	public static List<Map<String, String>> toList() {
		if (list == null) {
			EnumTemplateType[] ary = EnumTemplateType.values();
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
			EnumTemplateType[] ary = EnumTemplateType.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getName());
			}
			map = enumMap;
		}
		return map;
	}
	
	public static Map<String, String> toDownLoadNameMap() {
		if (downLoadNameMap == null) {
			EnumTemplateType[] ary = EnumTemplateType.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getDownLoadName());
			}
			downLoadNameMap = enumMap;
		}
		return downLoadNameMap;
	}
	
	public static Map<String, String> toFolderMap() {
		if (folderNameMap == null) {
			EnumTemplateType[] ary = EnumTemplateType.values();
			Map<String, String> enumMap = new HashMap<String, String>();
			for (int num = 0; num < ary.length; num++) {
				enumMap.put(ary[num].getValue(), ary[num].getFolder());
			}
			folderNameMap = enumMap;
		}
		return folderNameMap;
	}
	

	public static String getNameByValue(String value) {
		return toMap().get(value);
	}

	public static String getDownLoadNameByValue(String value) {
		return toDownLoadNameMap().get(value);
	}
	public static String getFolderByValue(String value) {
		return toFolderMap().get(value);
	}
	
	

}
