package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumReceiptType {

	/**********************************************
	 * 单据类型
	 **********************************************/

	STOCK_INPUT("入库", (byte) 10), 
	STOCK_INPUT_PKG_OPERATION("包装单管理", (byte) 11), 
	STOCK_INPUT_PRODUCE("生产入库单", (byte) 12), 
	STOCK_INPUT_OTHER("其它入库单", (byte) 13),
	STOCK_INPUT_VIRTUAL("虚拟生产入库", (byte) 14),
	STOCK_INPUT_STRAIGTH("直发生产入库", (byte) 15),
	STOCK_INPUT_PRODUCTION_URGENT("临时作业生产入库", (byte) 16),
	STOCK_INPUT_BOOK_URGENT("紧急记账生产入库", (byte) 17),
	
	
	STOCK_INPUT_TRANSPORT("入库(转储)", (byte) 40), 
	STOCK_INPUT_TRANSPORT_ERP("转储入库单", (byte) 41), 
	STOCK_INPUT_TRANSPORT_VIRTUAL("虚拟转储入库",(byte)42),
	STOCK_INPUT_TRANSPORT_PRODUCTION_URGENT("临时作业转储入库", (byte) 43),
	STOCK_INPUT_TRANSPORT_BOOK_URGENT("紧急记账转储入库", (byte) 44),
	
	STOCK_OUTPUT("出库", (byte) 20),
	STOCK_OUTPUT_ALLOCATE_CARGO("配货单", (byte) 21), 
	STOCK_OUTPUT_SALE("销售出库单", (byte) 22), 
	STOCK_OUTPUT_SCRAP("报废出库单", (byte) 23), 
	STOCK_OUTPUT_OTHER("其它出库单", (byte) 24),
	STOCK_OUTPUT_STRAIGTH("生产直发出库单", (byte) 25),
	STOCK_OUTPUT_PRODUCTION_URGENT_TRUE("临时作业销售出库", (byte) 26),
	STOCK_OUTPUT_PRODUCTION_URGENT_FALSE("临时作业销售出库", (byte) 27),
	STOCK_OUTPUT_BOOK_URGENT("紧急记账销售出库", (byte) 28),
	
	
	STOCK_TASK("仓库管理", (byte) 30),
	STOCK_TASK_LISTING_REQ("上架请求单", (byte) 31), 
	STOCK_TASK_REMOVAL_REQ("下架请求单", (byte) 32), 
	STOCK_TASK_LISTING("上架作业单", (byte) 33), 
	STOCK_TASK_REMOVAL("下架作业单", (byte) 34),
	STOCK_TASK_GROUP("组盘作业单", (byte) 35),
	STOCK_TASK_PCKAGE_CLEANUP("基于包装整理", (byte) 36),
	STOCK_TASK_PALLET_CLEANUP("基于托盘整理", (byte) 37),
	STOCK_TASK_POSITION_CLEANUP("基于仓位整理", (byte) 38),
	STOCK_TASK_MATERIAL_CLEANUP("基于物料整理", (byte) 39),
	
	
	STOCK_TRANSPORT("转储", (byte) 50),
	STOCK_TRANSPORT_FACTORY("工厂转储单", (byte) 51), 
	STOCK_TRANSPORT_STOCK("库存转储单", (byte) 52), 
	STOCK_TRANSPORT_MATERIAL("物料转储单", (byte) 53), 
	STOCK_TRANSPORT_PRODUCTION("生产转运", (byte) 54),
	STOCK_TRANSPORT_PRODUCTION_URGENT("临时作业生产转运", (byte) 55),
	STOCK_TRANSPORT_FACTORY_WRITEOFF("工厂转储冲销单", (byte) 56), 
	STOCK_TRANSPORT_STOCK_WRITEOFF("库存转储冲销单", (byte) 57), 
	STOCK_TRANSPORT_MATERIAL_WRITEOFF("物料转储冲销单", (byte) 58),
	STOCK_RETURN_SALE("销售退货单", (byte) 62),

	STOCKTAKE("库存盘点单", (byte) 71),
	

	/**********************************************
	 * 单据类型END
	 **********************************************/

	DELIVERY_NOTICE("送货通知单", (byte) 127),
	INSPECT_NOTICE("验收通知单", (byte) 127),
	STOCK_INPUT_INSPECT("验收入库单", (byte) 127),
	STOCK_INPUT_EXEMPT_INSPECT("免检入库单", (byte) 127),
	STOCK_INPUT_ALLOCATE("调拨入库单", (byte) 127),
	STOCK_INPUT_PLATFORM("招采入库单", (byte) 127), 
	MAT_REQ_PRODUCTION("生产期领料单", (byte) 127),
	MAT_REQ_BUILD("在建期领料单", (byte) 127),
	STOCK_OUTPUT_MAT_REQ("领料出库单", (byte) 127),
	STOCK_OUTPUT_PURCHASE_RETURN("采购退货单", (byte) 127),
	STOCK_OUTPUT_ALLCOTE("调拨出库单", (byte) 127),
	STOCK_OUTPUT_RESERVE("预留出库单", (byte) 127),
	STOCK_RETURN_MAT_REQ("领料退货单", (byte) 127),
	STOCK_RETURN_RESERVE("预留退货单", (byte) 127),
	ALLOCATE("调拨单", (byte) 127),
	ALLOCATE_DELIVERY("调拨配送单", (byte) 127),
	TYPE_SAP("SAP生成", (byte) 127),
	URGENCE_STOCK_INPUT("紧急入库单", (byte) 127),
	URGENCE_STOCK_OUTPUT("紧急出库单", (byte) 127),
	URGENCE_BORROW("紧急借用单", (byte) 127),
	STOCK_TASK_SN_CLEANUP("基于SN整理", (byte) 127);
	

	/** 描述 */
	private String name;
	/** 枚举值 */
	private Byte value;

	private EnumReceiptType(String name, Byte value) {
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
			EnumReceiptType[] ary = EnumReceiptType.values();
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
			EnumReceiptType[] ary = EnumReceiptType.values();
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
