package com.inossem.wms.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UtilConst {

//	
//
//	public static String getReceiptTypeName(int type) {
//		String name;
//		switch (type) {
//		case ConstReceiptType.RECEIPT_TYPE_DHYS:
//			name = "到货验收单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_RKGL_YSRK:
//			name = "验收入库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_RKGL_MJRK:
//			name = "免检入库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_RKGL_QTRK:
//			name = "其它入库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_RKGL_DBRK:
//			name = "调拨入库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_RKGL_ZCRK:
//			name = "招采入库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_CKGL_SJ:
//			name = "上架请求单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_CKGL_CKZL:
//			name = "上架作业单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_LLGL_SCQLL:
//			name = "生产期领料单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_LLGL_ZJQLL:
//			name = "在建期领料单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_CHKGL_LLCHK:
//			name = "领料出库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_CHKGL_XSCHK:
//			name = "销售出库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_CHKGL_CGTH:
//			name = "采购退货单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_CHKGL_QTCHK:
//			name = "其它出库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_CHKGL_DBCHK:
//			name = "调拨出库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_ZC_GCZC:
//			name = "工厂转储单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_ZC_KCZC:
//			name = "库存转储单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_ZC_WLZC:
//			name = "物料转储单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_TK_LLTK:
//			name = "领料退库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_TK_XSTK:
//			name = "销售退库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_TK_YLTK:
//			name = "预留退库单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_KCPD:
//			name = "库存盘点单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_DBGL:
//			name = "调拨管理单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_JYGL:
//			name = "借用管理单";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_JJCRK_I:
//			name = "紧急入库";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_JJCRK_O:
//			name = "紧急出库";
//			break;
//		case ConstReceiptType.RECEIPT_TYPE_JJCRK_Q:
//			name = "紧急借用";
//			break;
//
//		default:
//			name = "未知单据";
//			break;
//		}
//
//		return name;
//	}

	public static String getReceiptFileName(long receipt_id, String fileName) {
		return String.format("%d_%s", receipt_id, fileName);
	}

	public static ArrayList<HashMap<String, Object>> getMJYYList() {

		ArrayList<HashMap<String, Object>> mjyyList = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> mjyy0 = new HashMap<String, Object>();
		mjyy0.put("value", "0");
		mjyy0.put("text", "线下已验收");
		HashMap<String, Object> mjyy1 = new HashMap<String, Object>();
		mjyy1.put("value", "1");
		mjyy1.put("text", "长协合作");
		HashMap<String, Object> mjyy2 = new HashMap<String, Object>();
		mjyy2.put("value", "2");
		mjyy2.put("text", "出厂已质检");
		HashMap<String, Object> mjyy3 = new HashMap<String, Object>();
		mjyy3.put("value", "3");
		mjyy3.put("text", "第三方质检");
		HashMap<String, Object> mjyy4 = new HashMap<String, Object>();
		mjyy4.put("value", "4");
		mjyy4.put("text", "日常消耗品");
		HashMap<String, Object> mjyy5 = new HashMap<String, Object>();
		mjyy5.put("value", "5");
		mjyy5.put("text", "其他原因");

		mjyyList.add(mjyy0);
		mjyyList.add(mjyy1);
		mjyyList.add(mjyy2);
		mjyyList.add(mjyy3);
		mjyyList.add(mjyy4);
		mjyyList.add(mjyy5);

		return mjyyList;
	}
	
	public static String getStatusNameByAllocate(byte status) {
		String statusString = "";
		switch (status) {
		case 0:
			statusString = "草稿";
			break;
		case 10:
			statusString = "已提交";
			break;
		case 15:
			statusString = "待配送";
			break;
		case 17:
			statusString = "待出库";
			break;
		case 20:
			statusString = "配送中";
			break;
		case 25:
			statusString = "待入库";
			break;
		case 30:
			statusString = "已完成";
			break;

		default:
			break;
		}
		return statusString;
	}
	
	
	
	public static Date getFirstDay(){
		Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, -1);
        calendar1.set(Calendar.DAY_OF_MONTH,1);
        return calendar1.getTime();
	}
	
	public static Date getLastDay(){
		Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 0);
        return calendar2.getTime();
	}
	

}
