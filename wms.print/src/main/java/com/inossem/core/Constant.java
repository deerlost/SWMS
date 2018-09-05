package com.inossem.core;
/** 
* @author 高海涛 
* @version 2017年9月22日
* 常量 
*/
public class Constant {
	
	// 路径
	public static final String OUTPUTFILE_LABEL_PATH = "/outputfiles/label/";	//标签输出路径
	public static final String OUTPUTFILE_TABLE_PATH = "/outputfiles/table/";	//单据输出路径
	public static final String OUTPUTFILE_QRCODE_PATH = "/outputfiles/QRcode/";	//二维码输出路径
	
	// 生成文件类型
	public static final String OUTPUTFILE_TYPE_XLS = ".xls";
	public static final String OUTPUTFILE_TYPE_PDF = ".pdf";
	public static final String OUTPUTFILE_TYPE_PNG = ".png";
	
	//模版路径
	public static final String TEMPLATE_LABEL_CHAGR = "/template/yt/label/label_zebra.xls";
	public static final String TEMPLATE_LABEL_MATERIEL = "/template/yt/label/materiel_zebra.xls";
	public static final String TEMPLATE_LABEL_JJCRK = "/template/yt/label/jjcrk_zebra.xls";
	
	//川维模板
	public static final String TEMPLATE_LABEL_PROTRANS = "/template/cw/protrans_zebra.xls";
	public static final String TEMPLATE_LABEL_ERPSTOCK = "/template/cw/erp_zebra.xls";
	public static final String TEMPLATE_LABEL_OUTPUT = "/template/cw/output_zebra.xls";
	public static final String TEMPLATE_LABEL_LIMS = "/template/cw/lims_zebra.xls";
	public static final String TEMPLATE_LABEL_DOWNTASK = "/template/cw/down_task_zebra.xls";
	public static final String TEMPLATE_LABEL_UPTASK = "/template/cw/up_task_zebra.xls";
	public static final String TEMPLATE_LABEL_TRANSINPUT = "/template/cw/stock_transport_zebra.xls";
	public static final String TEMPLATE_LABEL_POSITION = "/template/cw/position_zebra.xls";
	public static final String TEMPLATE_LABEL_PACKAGESN = "/template/cw/package_sn_zebra.xls";
	//评定等级
	public static final String IMG_EXCELLENT="/img/u.png";
	public static final String IMG_FIRST="/img/1.png";
	public static final String IMG_QUALIFIED="/img/h.png";
	
	//员工
	public static final String IMG_ZHOU="/img/zhou.png";
	public static final String IMG_QIU="/img/qiu.png";
	
	
	/** 托盘套膜标签打印*/
	public static final String TYPE_1 = "L1";
	/** 包装物标签打印*/
	public static final String TYPE_2 = "L2";
	/** 仓位标签打印 */
	public static final String TYPE_3 = "L3";
	
	//安卓标签识别码
	/** 托盘套膜标签打印*/
	public static final String ANDROID_TYPE_1 = "INS-11";
	/** 包装物标签打印*/
	public static final String ANDROID_TYPE_2 = "INS-02";
	/** 仓位标签打印 */
	public static final String ANDROID_TYPE_3 = "INS-10";
	/** 单据打印*/
	public static final String ANDROID_TYPE_6 = "INS-12";
	
	
	/** 紧急出入库标签打印 */
	public static final String TYPE_4 = "L4";

	//二维码颜色  
	public static final int BLACK = 0xFF000000;  
    //二维码颜色  
	public static final int WHITE = 0xFFFFFFFF;  
	
	
	
	
	public static final String ANDROID_TYPE_4 = "INS-04";//仓位
	public static final String ANDROID_TYPE_5 = "INS-05";//紧急出入库
	
	public static final String ZPL_BEGIN = "^XA^SEE:GB18030.DAT^CW1,E:SIMSUN.FNT"; // 标签格式以^XA开始，字符集设置为GB18030，字体为宋体
	public static final String ZPL_END = "^XZ"; // 标签格式以^XZ结束
}
