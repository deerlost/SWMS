package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: EnumStockPositionTemplate</p>
 * <p>Description: 仓位模板，描述该仓储区下的仓位的格式</p>
 * <p>Company: www.inossem.com</p>
 * @author  wang.ligang
 * @version 1.0
 */
public enum EnumStockPositionTemplate {
	/**
     * 仓位模板，描述该仓储区下的仓位的格式：
     * 0-仓位号没有分隔的横杠 例：H0004，
     * 1-仓位号有一个分隔的横杠 例：ZJK-B01，JL-L01，Z01-34，
     * 2-仓位号有两个分隔的横杠 例：G34-05-02，03-03-71
     */
	
	/** 0-仓位号没有分隔的横杠  **/
	NO("仓位号没有分隔的横杠", (byte)0),
	
	/** 1-仓位号有一个分隔的横杠  **/
	ONE("仓位号有一个分隔的横杠", (byte)1),
	
	/** 2-仓位号有两个分隔的横杠 **/
	TWO("仓位号有两个分隔的横杠", (byte)2);

	/** 描述 */
	private String desc;
	/** 枚举值 */
	private byte value;

	private EnumStockPositionTemplate(String desc, byte value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public static EnumStockPositionTemplate getEnum(byte value){
		EnumStockPositionTemplate resultEnum = null;
		EnumStockPositionTemplate[] enumAry = EnumStockPositionTemplate.values();
		for(int i = 0;i<enumAry.length;i++){
			if(enumAry[i].getValue() == value){
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		EnumStockPositionTemplate[] ary = EnumStockPositionTemplate.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", ary[num].getValue());
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(){
		EnumStockPositionTemplate[] ary = EnumStockPositionTemplate.values();
		List list = new ArrayList();
		for(int i=0;i<ary.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value",ary[i].getValue());
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static String getDescByValue(String value) {
		String ret = "";
		List<Map<String,String>> list = EnumStockPositionTemplate.toList();
		for (Map<String,String> temp : list) {
			String key = temp.get("value");
			if (value != null && value.equals(key)) {
				ret = temp.get("desc");
				break;
			}
		}
		return ret;
	}
	
}
