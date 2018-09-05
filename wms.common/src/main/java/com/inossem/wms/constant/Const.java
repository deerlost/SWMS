package com.inossem.wms.constant;

import java.math.BigDecimal;
import java.util.Date;

import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;

public class Const {

	public static final String DEFAULT_CODE = "2000";
	
	public static final Date BEGIN_DATE = UtilString.getDateForString("2018-08-01");

	public static final String SAP_API_MSG_PREFIX = "[sap]";
	public static final String SAP_API_MSG_FORMAT = "%s%s";

	public static final String FILE_SYSTEM_UPLOAD = "%s/file/download";

	public static final String OA_PUSH_URL = "%s/wms/web/html/invoice/approval/approvallist.html?key=20";

	// 审批人审批完成后 审批状态 默认
	public static final byte RECEIPT_STATUS_DEFAULT = 0;
	// 审批人审批完成后 审批状态 通过
	public static final byte RECEIPT_STATUS_APPROVE = 1;
	// 审批人审批完成后 审批状态 不通过
	public static final byte RECEIPT_STATUS_REJECT = 2;
	// 审批人审批完成后 审批状态 其他人不通过
	public static final byte RECEIPT_STATUS_OTHER_REJECT = 3;
	// 审批人审批完成后 审批状态 其他人通过
	public static final byte RECEIPT_STATUS_OTHER_APPROVE = 4;

	// 审批人审批完成后，审批情况 通过未完成
	public static final byte RECEIPT_APPROVE_INCOMPLETE = 1;
	// 审批人审批完成后，审批情况 不通过已完成
	public static final byte RECEIPT_REJECT_COMPLETE = 2;
	// 审批人审批完成后，审批情况 通过已完成
	public static final byte RECEIPT_APPROVE_COMPLETE = 3;

	// SAP接口
	// public static final String SAP_API_URL =
	// "http://erpyedci.yitaigroup.com:8000/sap/api/ewm/int_%s?sap-client=400";
	public static final String SAP_API_URL; // "http://erpyedci.yitaigroup.com:8000/sap/api/ewm/";
	public static final String SAP_API_CLIENT;
	// public static final String CKGL_CCQ_DEFAULT = "888";
	// public static final String CKGL_CW_DEFAULT = "00-00-00";

	static {
		SAP_API_URL = UtilProperties.getInstance().getSapApiUrl();
		SAP_API_CLIENT = UtilProperties.getInstance().getSapApiClient();
	}

//	public static final byte CORP_BOARD_COAL_OIL = 1;
//	public static final byte CORP_BOARD_COAL = 2;
//	public static final byte CORP_BOARD_RAILWAY = 3;
//	public static final byte CORP_BOARD_OTHER = 4;


	// 非限制库存 SAP 对应空字符窜
	//public static final byte MCHB_STATUS_UNRESTRICTED = 1;
	// 在途库存 SAP 对应T
	//public static final byte MCHB_STATUS_ON_THE_WAY = 2;
	// 质量检验库存 SAP 对应X
	//public static final byte MCHB_STATUS_QUALITY_INSPECTION = 3;
	// 冻结库存 SAP 对应S
	//public static final byte MCHB_STATUS_FREEZE = 4;

	// 验收单状态 草稿
	//public static final byte DHYS_YSD_DRAFT = 0;
	// 验收单状态 未验收
	//public static final byte DHYS_YSD_UNCHECKED = 1;
	// 验收单状态 未通过
	//public static final byte DHYS_YSD_UNPASS = 2;
	// 验收单状态 验收中
	//public static final byte DHYS_YSD_INCHECK = 3;
	// 验收单状态 已完成
	//public static final byte DHYS_YSD_PASSED = 4;

	// 入库单冲销标识 1 未冲销
	//public static final byte RKGL_NCX = 1;
	// 入库单冲销标识 2 已冲销
	//public static final byte RKGL_YCX = 2;

	// 退库单标识 1 未完成
	//public static final byte TKGL_UNCOMPLETED = 1;
	// 退库单标识 2 已完成
	//public static final byte TKGL_COMPLETED = 2;

	// 领料单业务类型
	//public static final int LLD_YWLX_MINOR_REPAIRS = 1;// 小修/日常维修
	//public static final int LLD_YWLX_OVERHAUL = 2;// 大项修/技改
	//public static final int LLD_YWLX_OTHER = 3;// 其他
	//public static final int LLD_YWLX_EQUIPMENT_ASSETS = 4;// 设备/资产

	// 库房主管
	//public static final int JBR_KFZG = 14;
	// 领料人
	//public static final int JBR_LLR = 35;
	// 质量验收人
	//public static final int JBR_ZLYSR = 36;

	// 库房主管
	//public static final String JBR_KFZG_NAME = "库房主管";
	// 领料人
	//public static final String JBR_LLR_NAME = "领料人";
	// 质量验收人
	//public static final String JBR_ZLYSR_NAME = "质量验收人";

	// 盘点->仓位分割后对应字段位数
	public static final int PD_LGPLA_SEGMENTATION_NUM = 5;
	// 盘点->仓位分割后对应字段位数
	public static final int PD_LGPLA_SEGMENTATION_NUM6 = 6;

	
	// 销售凭证类型字段 销售退库 【退库】
	public static final String SAP_TK_TYPE_TK_XSTK = "ZRE2";
	
	
//	// 退库单标识 1 未完成
//	public static final byte REQUEST_SOURCE_WEB = 0;
//	// 退库单标识 2 已完成
//	public static final byte REQUEST_SOURCE_PDA = 1;
	
	// 借贷标识H：贷方   数量减
	//public static final String DEBIT_CREDIT_H = "H";

	// 借贷标识S：借方    数量加
	//public static final String DEBIT_CREDIT_S = "S";
	
	/**
	 * 减号 -
	 */
	public final static String SEPARATOR_MINUS_SIGN = "\\-";
	
	public static final BigDecimal PALLET_MAX_WEIGHT = new BigDecimal(1000);
	
}
