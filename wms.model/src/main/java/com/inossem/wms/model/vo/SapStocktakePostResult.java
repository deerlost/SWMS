package com.inossem.wms.model.vo;

public class SapStocktakePostResult {
	/**
	 * SAP盘点凭证
	 */
	public static final String RETURN_KEY_E_PDPZ = "E_PDPZ";
	/**
	 * 物料凭证
	 */
	public static final String RETURN_KEY_E_MSEG = "E_MSEG";
	
	private String ZWMSBH; // WMS单据编号
	private String ZWMSXM; // WMS单据行项目
	private String ZDJBH;  // 过账参考单据号
	private String ZDJXM;  // 过账参考单据项目
	private String MBLNR;  // 物料凭证号
	private String ZEILE;  // 物料凭证行项目
	private String MJAHR;  // 物料凭证年度
	private String BWART;  // 移动类型
	private String MATNR;  // 物料编码
	private String TXZ01;  // 物料描述
	private String WERKS;  // 工厂
	private String LGORT;  // 库存地点
	private String CHARG;  // 批号
	private String SOBKZ;  // 特殊库存类型
	private String ZTSKC;  // 特殊库存代码
	private String ZTSMS;  // 特殊库存描述
	private String LIFNR;  // 供应商编码
	private String VNAME1; // 供应商名称
	private String KUNNR;  // 客户编码
	private String CNAME1; // 客户名名称
	private String VBELN;  // 销售订单号
	private String KDPOS;  // 销售订单项目编号
	private String KDEIN;  // 销售订单交货计划
	private String DMBTR;  // 金额
	private String MENGE;  // 数量
	private String MEINS;  // 基本计量单位
	private String SHKZG;  // 借贷标识  ： H贷方  -数量; S借方  +数量
	//	非限制
	//	X	质量检查 
	//	S	冻结的
	//	T      在途的: INSMK 取MSEG- BWART= 313/S或
	//	MSEG- BWART=/315/H时，库存状态INSMK=T
	private String INSMK;  // 库存状态      
	
	private String ZIVNUM;   // WMS盘点凭证号
	private String ZIVNUM_N; // WMS盘点分组号
	private String IBLNR;    // 盘点凭证号
	
	public SapStocktakePostResult() {
	}

	/**
	 * @Description: WMS单据编号
	 */
	public String getZWMSBH() {
		return ZWMSBH;
	}

	/**
	 * @Description: WMS单据编号
	 */
	public void setZWMSBH(String ZWMSBH) {
		this.ZWMSBH = ZWMSBH;
	}

	/**
	 * @Description: WMS单据行项目
	 */
	public String getZWMSXM() {
		return ZWMSXM;
	}

	/**
	 * @Description: WMS单据行项目
	 */
	public void setZWMSXM(String ZWMSXM) {
		this.ZWMSXM = ZWMSXM;
	}

	/**
	 * @Description: 过账参考单据号
	 */
	public String getZDJBH() {
		return ZDJBH;
	}

	/**
	 * @Description: 过账参考单据号
	 */
	public void setZDJBH(String ZDJBH) {
		this.ZDJBH = ZDJBH;
	}

	/**
	 * @Description: 过账参考单据项目
	 */
	public String getZDJXM() {
		return ZDJXM;
	}

	/**
	 * @Description: 过账参考单据项目
	 */
	public void setZDJXM(String ZDJXM) {
		this.ZDJXM = ZDJXM;
	}

	/**
	 * @Description: 物料凭证号
	 */
	public String getMBLNR() {
		return MBLNR;
	}

	/**
	 * @Description: 物料凭证号
	 */
	public void setMBLNR(String MBLNR) {
		this.MBLNR = MBLNR;
	}

	/**
	 * @Description: 物料凭证行项目
	 */
	public String getZEILE() {
		return ZEILE;
	}

	/**
	 * @Description: 物料凭证行项目
	 */
	public void setZEILE(String ZEILE) {
		this.ZEILE = ZEILE;
	}

	/**
	 * @Description: 物料凭证年度
	 */
	public String getMJAHR() {
		return MJAHR;
	}

	/**
	 * @Description: 物料凭证年度
	 */
	public void setMJAHR(String MJAHR) {
		this.MJAHR = MJAHR;
	}

	/**
	 * @Description: 移动类型
	 */
	public String getBWART() {
		return BWART;
	}

	/**
	 * @Description: 移动类型
	 */
	public void setBWART(String BWART) {
		this.BWART = BWART;
	}

	/**
	 * @Description: 物料编码
	 */
	public String getMATNR() {
		return MATNR;
	}

	/**
	 * @Description: 物料编码
	 */
	public void setMATNR(String MATNR) {
		this.MATNR = MATNR;
	}

	/**
	 * @Description: 物料描述
	 */
	public String getTXZ01() {
		return TXZ01;
	}

	/**
	 * @Description: 物料描述
	 */
	public void setTXZ01(String TXZ01) {
		this.TXZ01 = TXZ01;
	}

	/**
	 * @Description: 工厂
	 */
	public String getWERKS() {
		return WERKS;
	}

	/**
	 * @Description: 工厂
	 */
	public void setWERKS(String WERKS) {
		this.WERKS = WERKS;
	}

	/**
	 * @Description: 库存地点
	 */
	public String getLGORT() {
		return LGORT;
	}

	/**
	 * @Description: 库存地点
	 */
	public void setLGORT(String LGORT) {
		this.LGORT = LGORT;
	}

	/**
	 * @Description: 批号
	 */
	public String getCHARG() {
		return CHARG;
	}

	/**
	 * @Description: 批号
	 */
	public void setCHARG(String CHARG) {
		this.CHARG = CHARG;
	}

	/**
	 * @Description: 特殊库存类型
	 */
	public String getSOBKZ() {
		return SOBKZ;
	}

	/**
	 * @Description: 特殊库存类型
	 */
	public void setSOBKZ(String SOBKZ) {
		this.SOBKZ = SOBKZ;
	}

	/**
	 * @Description: 特殊库存代码
	 */
	public String getZTSKC() {
		return ZTSKC;
	}

	/**
	 * @Description: 特殊库存代码
	 */
	public void setZTSKC(String ZTSKC) {
		this.ZTSKC = ZTSKC;
	}

	/**
	 * @Description: 特殊库存描述
	 */
	public String getZTSMS() {
		return ZTSMS;
	}

	/**
	 * @Description: 特殊库存描述
	 */
	public void setZTSMS(String ZTSMS) {
		this.ZTSMS = ZTSMS;
	}

	/**
	 * @Description: 供应商编码
	 */
	public String getLIFNR() {
		return LIFNR;
	}

	/**
	 * @Description: 供应商编码
	 */
	public void setLIFNR(String LIFNR) {
		this.LIFNR = LIFNR;
	}

	/**
	 * @Description: 供应商名称
	 */
	public String getVNAME1() {
		return VNAME1;
	}

	/**
	 * @Description: 供应商名称
	 */
	public void setVNAME1(String VNAME1) {
		this.VNAME1 = VNAME1;
	}

	/**
	 * @Description: 客户编码
	 */
	public String getKUNNR() {
		return KUNNR;
	}

	/**
	 * @Description: 客户编码
	 */
	public void setKUNNR(String KUNNR) {
		this.KUNNR = KUNNR;
	}

	/**
	 * @Description: 客户名名称
	 */
	public String getCNAME1() {
		return CNAME1;
	}

	/**
	 * @Description: 客户名名称
	 */
	public void setCNAME1(String CNAME1) {
		this.CNAME1 = CNAME1;
	}

	/**
	 * @Description: 销售订单号
	 */
	public String getVBELN() {
		return VBELN;
	}

	/**
	 * @Description: 销售订单号
	 */
	public void setVBELN(String VBELN) {
		this.VBELN = VBELN;
	}

	/**
	 * @Description: 销售订单项目编号
	 */
	public String getKDPOS() {
		return KDPOS;
	}

	/**
	 * @Description: 销售订单项目编号
	 */
	public void setKDPOS(String KDPOS) {
		this.KDPOS = KDPOS;
	}

	/**
	 * @Description: 销售订单交货计划
	 */
	public String getKDEIN() {
		return KDEIN;
	}

	/**
	 * @Description: 销售订单交货计划
	 */
	public void setKDEIN(String KDEIN) {
		this.KDEIN = KDEIN;
	}

	/**
	 * @Description: 金额
	 */
	public String getDMBTR() {
		return DMBTR;
	}

	/**
	 * @Description: 金额
	 */
	public void setDMBTR(String DMBTR) {
		this.DMBTR = DMBTR;
	}

	/**
	 * @Description: 数量
	 */
	public String getMENGE() {
		return MENGE;
	}

	/**
	 * @Description: 数量
	 */
	public void setMENGE(String MENGE) {
		this.MENGE = MENGE;
	}

	/**
	 * @Description: 基本计量单位
	 */
	public String getMEINS() {
		return MEINS;
	}

	/**
	 * @Description: 基本计量单位
	 */
	public void setMEINS(String MEINS) {
		this.MEINS = MEINS;
	}

	/**
	 * @Description: 借贷标识  ： H贷方  -数量; S借方  +数量
	 */
	public String getSHKZG() {
		return SHKZG;
	}

	/**
	 * @Description: 借贷标识  ： H贷方  -数量; S借方  +数量
	 */
	public void setSHKZG(String SHKZG) {
		this.SHKZG = SHKZG;
	}

	/**
	 * @Description: 库存状态
	 */
	public String getINSMK() {
		return INSMK;
	}

	/**
	 * @Description: 库存状态
	 */
	public void setINSMK(String INSMK) {
		this.INSMK = INSMK;
	}

	/**
	 * @Description: WMS盘点凭证号
	 */
	public String getZIVNUM() {
		return ZIVNUM;
	}

	/**
	 * @Description: WMS盘点凭证号
	 */
	public void setZIVNUM(String zIVNUM) {
		ZIVNUM = zIVNUM;
	}

	/**
	 * @Description: WMS盘点分组号
	 */
	public String getZIVNUM_N() {
		return ZIVNUM_N;
	}

	/**
	 * @Description: WMS盘点分组号
	 */
	public void setZIVNUM_N(String zIVNUM_N) {
		ZIVNUM_N = zIVNUM_N;
	}

	/**
	 * @Description: 盘点凭证号
	 */
	public String getIBLNR() {
		return IBLNR;
	}

	/**
	 * @Description: 盘点凭证号
	 */
	public void setIBLNR(String iBLNR) {
		IBLNR = iBLNR;
	}

	@Override
	public String toString() {
		return "POSTRESULT [ZWMSBH=" + ZWMSBH + ", ZWMSXM=" + ZWMSXM + ", ZDJBH=" + ZDJBH + ", ZDJXM=" + ZDJXM
				+ ", MBLNR=" + MBLNR + ", ZEILE=" + ZEILE + ", MJAHR=" + MJAHR + ", BWART=" + BWART + ", MATNR=" + MATNR
				+ ", TXZ01=" + TXZ01 + ", WERKS=" + WERKS + ", LGORT=" + LGORT + ", CHARG=" + CHARG + ", SOBKZ=" + SOBKZ
				+ ", ZTSKC=" + ZTSKC + ", ZTSMS=" + ZTSMS + ", LIFNR=" + LIFNR + ", VNAME1=" + VNAME1 + ", KUNNR="
				+ KUNNR + ", CNAME1=" + CNAME1 + ", VBELN=" + VBELN + ", KDPOS=" + KDPOS + ", KDEIN=" + KDEIN
				+ ", DMBTR=" + DMBTR + ", MENGE=" + MENGE + ", MEINS=" + MEINS + ", SHKZG=" + SHKZG + ", INSMK=" + INSMK
				+ "]";
	}

}
