package com.inossem.wms.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumErrorCode {
	// 成功
	ERROR_CODE_SUCESS("成功", 0),
	// 失败
	ERROR_CODE_FAILURE("失败", 1),
	// 异常
	ERROR_CODE_EXCEPTION("程序异常", 2),
	// 工作流错误
	ERROR_CODE_WORKFLOW_FAILURE("工作流异常", 3),

	// ERROR_CODE_POST_FAILURE("过账失败", 4),

	ERROR_CODE_INTERFACE_CALL_FAILURE("接口调用失败", 5),
	
	ERROR_CODE_PRIMARY_KEY_EXISTED("主键已经存在", 6),
	//物料不存在
	ERROR_CODE_MATERIAL_NOT_EXIST("物料不存在", 7),
	// 只能删除草稿
	ERROR_CODE_ONLY_DELETE_DRAFT("只能删除草稿", 101),

	ERROR_CODE_EMPTY_ITEM("行项目为空", 102),

	ERROR_CODE_EMPTY_ORDER_ITEM("采购订单行项目为空", 103),

	ERROR_CODE_QTY_OVER(" 超过订单数量", 104),

	ERROR_CODE_LOCKED("库存地点冻结", 105),

	// 需求数量必须填写
	ERROR_CODE_DEMAND_QTY_FAILURE("必须填写需求数量", 106),

	ERROR_CODE_SUBMIT_FAILURE("提交失败", 107),

	ERROR_CODE_ALLOCATEID_EMPTY("调拨单号为空", 108),

	ERROR_CODE_STOCK_EXCEPTION("库存异常", 109),

	ERROR_CODE_EXCEL_CELL_TYPE("Excel文档中存在格式错误的数据!", 110),

	ERROR_CODE_DEBIT_CREDIT("借贷异常", 111),

	ERROR_CODE_QTY_UN_EQUAL(" 行项目入库数量与批次库存数量不等", 112),
	// 只能删除未完成入库单
	ERROR_CODE_ONLY_DELETE_UNFINISH("只能删除未完成单据", 113),
	// 
	ERROR_CODE_NO_PARAM("参数为空", 114),
	// 创建配送单
	ERROR_CODE_OUT_NOT_EQUAL_IN_LOCATION("发出工厂库存地点不一致", 115),
	// 账期管理月份已存在
	ERROR_CODE_MONTH_EXIST("月份已存在", 116),
	// 账期管理时间段冲突
	ERROR_CODE_TIME_BEGIN_END_EXIST("时间段冲突", 117),
	
	ERROR_CODE_VIRTUAL_INPUT_TRANED("该虚拟生产入库单已转储", 118),
	;
	
	/** 描述 */
	private String name;
	/** 枚举值 */
	private Integer value;

	private EnumErrorCode(String name, Integer value) {
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
			EnumErrorCode[] ary = EnumErrorCode.values();
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
			EnumErrorCode[] ary = EnumErrorCode.values();
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
