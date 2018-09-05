
package com.inossem.wms.wsdl.sap.input;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳ��������ƾ֤�����������ݶ��壨���ã�
 * 
 * <p>DT_CW_WMS_MATDOC_POST_REQ complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_MATDOC_POST_REQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IS_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD"/>
 *         &lt;element name="IV_BUKRS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IV_TESTRUN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IT_DOC_HEAD" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="GOODSMVT_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REF_DOC_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BILL_OF_LADING" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PR_UNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZGCWZ_FLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZBATCH_FLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZDOC_FLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMSGID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="HEADER_TXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REF_DOC_NO_LONG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IT_DOC_ITEM" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATERIAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PLANT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="STGE_LOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BATCH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MOVE_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="STCK_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SPEC_STOCK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VENDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CUSTOMER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VAL_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ENTRY_QNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ENTRY_UOM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PO_PR_QNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ORDERPR_UN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="COSTCENTER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TR_PART_BA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PAR_COMPCO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="GL_ACCOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PROFIT_CTR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="FUND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="FUNDS_CTR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CMMT_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="FUNC_AREA_LONG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="RESERV_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="RES_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PO_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PO_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NO_MORE_GR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WITHDRAWN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ITEM_TEXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="UNLOAD_PT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="GR_RCPT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MOVE_MAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MOVE_PLANT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MOVE_STLOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MOVE_BATCH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MOVE_VAL_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NB_SLIPS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WBS_ELEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VAL_WBS_ELEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ORDERID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ORDER_ITNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MVT_IND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MOVE_REAS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REF_DOC_YR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REF_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REF_DOC_IT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_CW_WMS_MATDOC_POST_REQ", propOrder = {
    "ismsghead",
    "ivbukrs",
    "ivtestrun",
    "itdochead",
    "itdocitem"
})
public class DTCWWMSMATDOCPOSTREQ {

    @XmlElement(name = "IS_MSG_HEAD", required = true)
    protected MSGHEAD ismsghead;
    @XmlElement(name = "IV_BUKRS")
    protected String ivbukrs;
    @XmlElement(name = "IV_TESTRUN")
    protected String ivtestrun;
    @XmlElement(name = "IT_DOC_HEAD")
    protected List<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD> itdochead;
    @XmlElement(name = "IT_DOC_ITEM")
    protected List<DTCWWMSMATDOCPOSTREQ.ITDOCITEM> itdocitem;

    public MSGHEAD getIsmsghead() {
		return ismsghead;
	}

	public void setIsmsghead(MSGHEAD ismsghead) {
		this.ismsghead = ismsghead;
	}

	public String getIvbukrs() {
		return ivbukrs;
	}

	public void setIvbukrs(String ivbukrs) {
		this.ivbukrs = ivbukrs;
	}

	public String getIvtestrun() {
		return ivtestrun;
	}

	public void setIvtestrun(String ivtestrun) {
		this.ivtestrun = ivtestrun;
	}

	public List<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD> getItdochead() {
		return itdochead;
	}

	public void setItdochead(List<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD> itdochead) {
		this.itdochead = itdochead;
	}

	public List<DTCWWMSMATDOCPOSTREQ.ITDOCITEM> getItdocitem() {
		return itdocitem;
	}

	public void setItdocitem(List<DTCWWMSMATDOCPOSTREQ.ITDOCITEM> itdocitem) {
		this.itdocitem = itdocitem;
	}

	/**
     * ��ȡismsghead���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link MSGHEAD }
     *     
     */
    public MSGHEAD getISMSGHEAD() {
        return ismsghead;
    }

    /**
     * ����ismsghead���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link MSGHEAD }
     *     
     */
    public void setISMSGHEAD(MSGHEAD value) {
        this.ismsghead = value;
    }

    /**
     * ��ȡivbukrs���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIVBUKRS() {
        return ivbukrs;
    }

    /**
     * ����ivbukrs���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIVBUKRS(String value) {
        this.ivbukrs = value;
    }

    /**
     * ��ȡivtestrun���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIVTESTRUN() {
        return ivtestrun;
    }

    /**
     * ����ivtestrun���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIVTESTRUN(String value) {
        this.ivtestrun = value;
    }

    /**
     * Gets the value of the itdochead property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itdochead property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITDOCHEAD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSMATDOCPOSTREQ.ITDOCHEAD }
     * 
     * 
     */
    public List<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD> getITDOCHEAD() {
        if (itdochead == null) {
            itdochead = new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD>();
        }
        return this.itdochead;
    }

    /**
     * Gets the value of the itdocitem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itdocitem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITDOCITEM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSMATDOCPOSTREQ.ITDOCITEM }
     * 
     * 
     */
    public List<DTCWWMSMATDOCPOSTREQ.ITDOCITEM> getITDOCITEM() {
        if (itdocitem == null) {
            itdocitem = new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCITEM>();
        }
        return this.itdocitem;
    }


    /**
     * <p>anonymous complex type�� Java �ࡣ
     * 
     * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="GOODSMVT_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REF_DOC_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BILL_OF_LADING" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PR_UNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZGCWZ_FLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZBATCH_FLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZDOC_FLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMSGID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="HEADER_TXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REF_DOC_NO_LONG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "docnum",
        "pstngdate",
        "docdate",
        "goodsmvtcode",
        "refdocno",
        "billoflading",
        "pruname",
        "zgcwzflag",
        "zbatchflag",
        "zdocflag",
        "zmsgid",
        "headertxt",
        "refdocnolong",
        "zmroyl01",
        "zmroyl02",
        "zmroyl03",
        "zmroyl04",
        "zmroyl05",
        "zmroyl06",
        "zmroyl07",
        "zmroyl08",
        "zmroyl09",
        "zmroyl10"
    })
    public static class ITDOCHEAD {

        @XmlElement(name = "DOC_NUM")
        protected String docnum;
        @XmlElement(name = "PSTNG_DATE")
        protected String pstngdate;
        @XmlElement(name = "DOC_DATE")
        protected String docdate;
        @XmlElement(name = "GOODSMVT_CODE")
        protected String goodsmvtcode;
        @XmlElement(name = "REF_DOC_NO")
        protected String refdocno;
        @XmlElement(name = "BILL_OF_LADING")
        protected String billoflading;
        @XmlElement(name = "PR_UNAME")
        protected String pruname;
        @XmlElement(name = "ZGCWZ_FLAG")
        protected String zgcwzflag;
        @XmlElement(name = "ZBATCH_FLAG")
        protected String zbatchflag;
        @XmlElement(name = "ZDOC_FLAG")
        protected String zdocflag;
        @XmlElement(name = "ZMSGID")
        protected String zmsgid;
        @XmlElement(name = "HEADER_TXT")
        protected String headertxt;
        @XmlElement(name = "REF_DOC_NO_LONG")
        protected String refdocnolong;
        @XmlElement(name = "ZMROYL01")
        protected String zmroyl01;
        @XmlElement(name = "ZMROYL02")
        protected String zmroyl02;
        @XmlElement(name = "ZMROYL03")
        protected String zmroyl03;
        @XmlElement(name = "ZMROYL04")
        protected String zmroyl04;
        @XmlElement(name = "ZMROYL05")
        protected String zmroyl05;
        @XmlElement(name = "ZMROYL06")
        protected String zmroyl06;
        @XmlElement(name = "ZMROYL07")
        protected String zmroyl07;
        @XmlElement(name = "ZMROYL08")
        protected String zmroyl08;
        @XmlElement(name = "ZMROYL09")
        protected String zmroyl09;
        @XmlElement(name = "ZMROYL10")
        protected String zmroyl10;

        /**
         * ��ȡdocnum���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDOCNUM() {
            return docnum;
        }

        /**
         * ����docnum���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDOCNUM(String value) {
            this.docnum = value;
        }

        /**
         * ��ȡpstngdate���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPSTNGDATE() {
            return pstngdate;
        }

        /**
         * ����pstngdate���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPSTNGDATE(String value) {
            this.pstngdate = value;
        }

        /**
         * ��ȡdocdate���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDOCDATE() {
            return docdate;
        }

        /**
         * ����docdate���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDOCDATE(String value) {
            this.docdate = value;
        }

        /**
         * ��ȡgoodsmvtcode���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGOODSMVTCODE() {
            return goodsmvtcode;
        }

        /**
         * ����goodsmvtcode���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGOODSMVTCODE(String value) {
            this.goodsmvtcode = value;
        }

        /**
         * ��ȡrefdocno���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREFDOCNO() {
            return refdocno;
        }

        /**
         * ����refdocno���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREFDOCNO(String value) {
            this.refdocno = value;
        }

        /**
         * ��ȡbilloflading���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBILLOFLADING() {
            return billoflading;
        }

        /**
         * ����billoflading���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBILLOFLADING(String value) {
            this.billoflading = value;
        }

        /**
         * ��ȡpruname���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPRUNAME() {
            return pruname;
        }

        /**
         * ����pruname���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPRUNAME(String value) {
            this.pruname = value;
        }

        /**
         * ��ȡzgcwzflag���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZGCWZFLAG() {
            return zgcwzflag;
        }

        /**
         * ����zgcwzflag���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZGCWZFLAG(String value) {
            this.zgcwzflag = value;
        }

        /**
         * ��ȡzbatchflag���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZBATCHFLAG() {
            return zbatchflag;
        }

        /**
         * ����zbatchflag���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZBATCHFLAG(String value) {
            this.zbatchflag = value;
        }

        /**
         * ��ȡzdocflag���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZDOCFLAG() {
            return zdocflag;
        }

        /**
         * ����zdocflag���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZDOCFLAG(String value) {
            this.zdocflag = value;
        }

        /**
         * ��ȡzmsgid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMSGID() {
            return zmsgid;
        }

        /**
         * ����zmsgid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMSGID(String value) {
            this.zmsgid = value;
        }

        /**
         * ��ȡheadertxt���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHEADERTXT() {
            return headertxt;
        }

        /**
         * ����headertxt���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHEADERTXT(String value) {
            this.headertxt = value;
        }

        /**
         * ��ȡrefdocnolong���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREFDOCNOLONG() {
            return refdocnolong;
        }

        /**
         * ����refdocnolong���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREFDOCNOLONG(String value) {
            this.refdocnolong = value;
        }

        /**
         * ��ȡzmroyl01���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL01() {
            return zmroyl01;
        }

        /**
         * ����zmroyl01���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL01(String value) {
            this.zmroyl01 = value;
        }

        /**
         * ��ȡzmroyl02���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL02() {
            return zmroyl02;
        }

        /**
         * ����zmroyl02���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL02(String value) {
            this.zmroyl02 = value;
        }

        /**
         * ��ȡzmroyl03���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL03() {
            return zmroyl03;
        }

        /**
         * ����zmroyl03���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL03(String value) {
            this.zmroyl03 = value;
        }

        /**
         * ��ȡzmroyl04���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL04() {
            return zmroyl04;
        }

        /**
         * ����zmroyl04���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL04(String value) {
            this.zmroyl04 = value;
        }

        /**
         * ��ȡzmroyl05���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL05() {
            return zmroyl05;
        }

        /**
         * ����zmroyl05���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL05(String value) {
            this.zmroyl05 = value;
        }

        /**
         * ��ȡzmroyl06���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL06() {
            return zmroyl06;
        }

        /**
         * ����zmroyl06���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL06(String value) {
            this.zmroyl06 = value;
        }

        /**
         * ��ȡzmroyl07���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL07() {
            return zmroyl07;
        }

        /**
         * ����zmroyl07���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL07(String value) {
            this.zmroyl07 = value;
        }

        /**
         * ��ȡzmroyl08���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL08() {
            return zmroyl08;
        }

        /**
         * ����zmroyl08���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL08(String value) {
            this.zmroyl08 = value;
        }

        /**
         * ��ȡzmroyl09���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL09() {
            return zmroyl09;
        }

        /**
         * ����zmroyl09���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL09(String value) {
            this.zmroyl09 = value;
        }

        /**
         * ��ȡzmroyl10���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL10() {
            return zmroyl10;
        }

        /**
         * ����zmroyl10���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL10(String value) {
            this.zmroyl10 = value;
        }

    }


    /**
     * <p>anonymous complex type�� Java �ࡣ
     * 
     * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATERIAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PLANT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="STGE_LOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BATCH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MOVE_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="STCK_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SPEC_STOCK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VENDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CUSTOMER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VAL_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ENTRY_QNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ENTRY_UOM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PO_PR_QNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ORDERPR_UN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="COSTCENTER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TR_PART_BA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PAR_COMPCO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="GL_ACCOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PROFIT_CTR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="FUND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="FUNDS_CTR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CMMT_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="FUNC_AREA_LONG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="RESERV_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="RES_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PO_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PO_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NO_MORE_GR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WITHDRAWN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ITEM_TEXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="UNLOAD_PT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="GR_RCPT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MOVE_MAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MOVE_PLANT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MOVE_STLOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MOVE_BATCH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MOVE_VAL_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NB_SLIPS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WBS_ELEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VAL_WBS_ELEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ORDERID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ORDER_ITNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MVT_IND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MOVE_REAS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REF_DOC_YR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REF_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REF_DOC_IT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL06" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL07" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL08" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL09" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "docnum",
        "docitemnum",
        "material",
        "plant",
        "stgeloc",
        "batch",
        "movetype",
        "stcktype",
        "specstock",
        "vendor",
        "customer",
        "valtype",
        "entryqnt",
        "entryuom",
        "poprqnt",
        "orderprun",
        "costcenter",
        "trpartba",
        "parcompco",
        "glaccount",
        "profitctr",
        "fund",
        "fundsctr",
        "cmmtitem",
        "funcarealong",
        "reservno",
        "resitem",
        "ponumber",
        "poitem",
        "nomoregr",
        "withdrawn",
        "itemtext",
        "unloadpt",
        "grrcpt",
        "movemat",
        "moveplant",
        "movestloc",
        "movebatch",
        "movevaltype",
        "nbslips",
        "wbselem",
        "valwbselem",
        "orderid",
        "orderitno",
        "mvtind",
        "movereas",
        "refdocyr",
        "refdoc",
        "refdocit",
        "iwmserpsid",
        "zmroyl01",
        "zmroyl02",
        "zmroyl03",
        "zmroyl04",
        "zmroyl05",
        "zmroyl06",
        "zmroyl07",
        "zmroyl08",
        "zmroyl09",
        "zmroyl10"
    })
    public static class ITDOCITEM {

        @XmlElement(name = "DOC_NUM")
        protected String docnum;
        @XmlElement(name = "DOC_ITEM_NUM")
        protected String docitemnum;
        @XmlElement(name = "MATERIAL")
        protected String material;
        @XmlElement(name = "PLANT")
        protected String plant;
        @XmlElement(name = "STGE_LOC")
        protected String stgeloc;
        @XmlElement(name = "BATCH")
        protected String batch;
        @XmlElement(name = "MOVE_TYPE")
        protected String movetype;
        @XmlElement(name = "STCK_TYPE")
        protected String stcktype;
        @XmlElement(name = "SPEC_STOCK")
        protected String specstock;
        @XmlElement(name = "VENDOR")
        protected String vendor;
        @XmlElement(name = "CUSTOMER")
        protected String customer;
        @XmlElement(name = "VAL_TYPE")
        protected String valtype;
        @XmlElement(name = "ENTRY_QNT")
        protected String entryqnt;
        @XmlElement(name = "ENTRY_UOM")
        protected String entryuom;
        @XmlElement(name = "PO_PR_QNT")
        protected String poprqnt;
        @XmlElement(name = "ORDERPR_UN")
        protected String orderprun;
        @XmlElement(name = "COSTCENTER")
        protected String costcenter;
        @XmlElement(name = "TR_PART_BA")
        protected String trpartba;
        @XmlElement(name = "PAR_COMPCO")
        protected String parcompco;
        @XmlElement(name = "GL_ACCOUNT")
        protected String glaccount;
        @XmlElement(name = "PROFIT_CTR")
        protected String profitctr;
        @XmlElement(name = "FUND")
        protected String fund;
        @XmlElement(name = "FUNDS_CTR")
        protected String fundsctr;
        @XmlElement(name = "CMMT_ITEM")
        protected String cmmtitem;
        @XmlElement(name = "FUNC_AREA_LONG")
        protected String funcarealong;
        @XmlElement(name = "RESERV_NO")
        protected String reservno;
        @XmlElement(name = "RES_ITEM")
        protected String resitem;
        @XmlElement(name = "PO_NUMBER")
        protected String ponumber;
        @XmlElement(name = "PO_ITEM")
        protected String poitem;
        @XmlElement(name = "NO_MORE_GR")
        protected String nomoregr;
        @XmlElement(name = "WITHDRAWN")
        protected String withdrawn;
        @XmlElement(name = "ITEM_TEXT")
        protected String itemtext;
        @XmlElement(name = "UNLOAD_PT")
        protected String unloadpt;
        @XmlElement(name = "GR_RCPT")
        protected String grrcpt;
        @XmlElement(name = "MOVE_MAT")
        protected String movemat;
        @XmlElement(name = "MOVE_PLANT")
        protected String moveplant;
        @XmlElement(name = "MOVE_STLOC")
        protected String movestloc;
        @XmlElement(name = "MOVE_BATCH")
        protected String movebatch;
        @XmlElement(name = "MOVE_VAL_TYPE")
        protected String movevaltype;
        @XmlElement(name = "NB_SLIPS")
        protected String nbslips;
        @XmlElement(name = "WBS_ELEM")
        protected String wbselem;
        @XmlElement(name = "VAL_WBS_ELEM")
        protected String valwbselem;
        @XmlElement(name = "ORDERID")
        protected String orderid;
        @XmlElement(name = "ORDER_ITNO")
        protected String orderitno;
        @XmlElement(name = "MVT_IND")
        protected String mvtind;
        @XmlElement(name = "MOVE_REAS")
        protected String movereas;
        @XmlElement(name = "REF_DOC_YR")
        protected String refdocyr;
        @XmlElement(name = "REF_DOC")
        protected String refdoc;
        @XmlElement(name = "REF_DOC_IT")
        protected String refdocit;
        @XmlElement(name = "IWMS_ERP_SID")
        protected String iwmserpsid;
        @XmlElement(name = "ZMROYL01")
        protected String zmroyl01;
        @XmlElement(name = "ZMROYL02")
        protected String zmroyl02;
        @XmlElement(name = "ZMROYL03")
        protected String zmroyl03;
        @XmlElement(name = "ZMROYL04")
        protected String zmroyl04;
        @XmlElement(name = "ZMROYL05")
        protected String zmroyl05;
        @XmlElement(name = "ZMROYL06")
        protected String zmroyl06;
        @XmlElement(name = "ZMROYL07")
        protected String zmroyl07;
        @XmlElement(name = "ZMROYL08")
        protected String zmroyl08;
        @XmlElement(name = "ZMROYL09")
        protected String zmroyl09;
        @XmlElement(name = "ZMROYL10")
        protected String zmroyl10;

        /**
         * ��ȡdocnum���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDOCNUM() {
            return docnum;
        }

        /**
         * ����docnum���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDOCNUM(String value) {
            this.docnum = value;
        }

        /**
         * ��ȡdocitemnum���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDOCITEMNUM() {
            return docitemnum;
        }

        /**
         * ����docitemnum���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDOCITEMNUM(String value) {
            this.docitemnum = value;
        }

        /**
         * ��ȡmaterial���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATERIAL() {
            return material;
        }

        /**
         * ����material���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATERIAL(String value) {
            this.material = value;
        }

        /**
         * ��ȡplant���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPLANT() {
            return plant;
        }

        /**
         * ����plant���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPLANT(String value) {
            this.plant = value;
        }

        /**
         * ��ȡstgeloc���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSTGELOC() {
            return stgeloc;
        }

        /**
         * ����stgeloc���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSTGELOC(String value) {
            this.stgeloc = value;
        }

        /**
         * ��ȡbatch���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBATCH() {
            return batch;
        }

        /**
         * ����batch���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBATCH(String value) {
            this.batch = value;
        }

        /**
         * ��ȡmovetype���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOVETYPE() {
            return movetype;
        }

        /**
         * ����movetype���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOVETYPE(String value) {
            this.movetype = value;
        }

        /**
         * ��ȡstcktype���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSTCKTYPE() {
            return stcktype;
        }

        /**
         * ����stcktype���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSTCKTYPE(String value) {
            this.stcktype = value;
        }

        /**
         * ��ȡspecstock���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECSTOCK() {
            return specstock;
        }

        /**
         * ����specstock���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECSTOCK(String value) {
            this.specstock = value;
        }

        /**
         * ��ȡvendor���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVENDOR() {
            return vendor;
        }

        /**
         * ����vendor���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVENDOR(String value) {
            this.vendor = value;
        }

        /**
         * ��ȡcustomer���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUSTOMER() {
            return customer;
        }

        /**
         * ����customer���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCUSTOMER(String value) {
            this.customer = value;
        }

        /**
         * ��ȡvaltype���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVALTYPE() {
            return valtype;
        }

        /**
         * ����valtype���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVALTYPE(String value) {
            this.valtype = value;
        }

        /**
         * ��ȡentryqnt���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getENTRYQNT() {
            return entryqnt;
        }

        /**
         * ����entryqnt���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setENTRYQNT(String value) {
            this.entryqnt = value;
        }

        /**
         * ��ȡentryuom���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getENTRYUOM() {
            return entryuom;
        }

        /**
         * ����entryuom���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setENTRYUOM(String value) {
            this.entryuom = value;
        }

        /**
         * ��ȡpoprqnt���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPOPRQNT() {
            return poprqnt;
        }

        /**
         * ����poprqnt���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPOPRQNT(String value) {
            this.poprqnt = value;
        }

        /**
         * ��ȡorderprun���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getORDERPRUN() {
            return orderprun;
        }

        /**
         * ����orderprun���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setORDERPRUN(String value) {
            this.orderprun = value;
        }

        /**
         * ��ȡcostcenter���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOSTCENTER() {
            return costcenter;
        }

        /**
         * ����costcenter���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOSTCENTER(String value) {
            this.costcenter = value;
        }

        /**
         * ��ȡtrpartba���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTRPARTBA() {
            return trpartba;
        }

        /**
         * ����trpartba���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTRPARTBA(String value) {
            this.trpartba = value;
        }

        /**
         * ��ȡparcompco���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPARCOMPCO() {
            return parcompco;
        }

        /**
         * ����parcompco���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPARCOMPCO(String value) {
            this.parcompco = value;
        }

        /**
         * ��ȡglaccount���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGLACCOUNT() {
            return glaccount;
        }

        /**
         * ����glaccount���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGLACCOUNT(String value) {
            this.glaccount = value;
        }

        /**
         * ��ȡprofitctr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPROFITCTR() {
            return profitctr;
        }

        /**
         * ����profitctr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPROFITCTR(String value) {
            this.profitctr = value;
        }

        /**
         * ��ȡfund���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFUND() {
            return fund;
        }

        /**
         * ����fund���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFUND(String value) {
            this.fund = value;
        }

        /**
         * ��ȡfundsctr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFUNDSCTR() {
            return fundsctr;
        }

        /**
         * ����fundsctr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFUNDSCTR(String value) {
            this.fundsctr = value;
        }

        /**
         * ��ȡcmmtitem���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCMMTITEM() {
            return cmmtitem;
        }

        /**
         * ����cmmtitem���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCMMTITEM(String value) {
            this.cmmtitem = value;
        }

        /**
         * ��ȡfuncarealong���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFUNCAREALONG() {
            return funcarealong;
        }

        /**
         * ����funcarealong���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFUNCAREALONG(String value) {
            this.funcarealong = value;
        }

        /**
         * ��ȡreservno���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRESERVNO() {
            return reservno;
        }

        /**
         * ����reservno���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRESERVNO(String value) {
            this.reservno = value;
        }

        /**
         * ��ȡresitem���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRESITEM() {
            return resitem;
        }

        /**
         * ����resitem���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRESITEM(String value) {
            this.resitem = value;
        }

        /**
         * ��ȡponumber���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPONUMBER() {
            return ponumber;
        }

        /**
         * ����ponumber���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPONUMBER(String value) {
            this.ponumber = value;
        }

        /**
         * ��ȡpoitem���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPOITEM() {
            return poitem;
        }

        /**
         * ����poitem���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPOITEM(String value) {
            this.poitem = value;
        }

        /**
         * ��ȡnomoregr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNOMOREGR() {
            return nomoregr;
        }

        /**
         * ����nomoregr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNOMOREGR(String value) {
            this.nomoregr = value;
        }

        /**
         * ��ȡwithdrawn���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWITHDRAWN() {
            return withdrawn;
        }

        /**
         * ����withdrawn���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWITHDRAWN(String value) {
            this.withdrawn = value;
        }

        /**
         * ��ȡitemtext���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getITEMTEXT() {
            return itemtext;
        }

        /**
         * ����itemtext���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setITEMTEXT(String value) {
            this.itemtext = value;
        }

        /**
         * ��ȡunloadpt���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUNLOADPT() {
            return unloadpt;
        }

        /**
         * ����unloadpt���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUNLOADPT(String value) {
            this.unloadpt = value;
        }

        /**
         * ��ȡgrrcpt���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGRRCPT() {
            return grrcpt;
        }

        /**
         * ����grrcpt���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGRRCPT(String value) {
            this.grrcpt = value;
        }

        /**
         * ��ȡmovemat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOVEMAT() {
            return movemat;
        }

        /**
         * ����movemat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOVEMAT(String value) {
            this.movemat = value;
        }

        /**
         * ��ȡmoveplant���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOVEPLANT() {
            return moveplant;
        }

        /**
         * ����moveplant���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOVEPLANT(String value) {
            this.moveplant = value;
        }

        /**
         * ��ȡmovestloc���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOVESTLOC() {
            return movestloc;
        }

        /**
         * ����movestloc���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOVESTLOC(String value) {
            this.movestloc = value;
        }

        /**
         * ��ȡmovebatch���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOVEBATCH() {
            return movebatch;
        }

        /**
         * ����movebatch���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOVEBATCH(String value) {
            this.movebatch = value;
        }

        /**
         * ��ȡmovevaltype���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOVEVALTYPE() {
            return movevaltype;
        }

        /**
         * ����movevaltype���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOVEVALTYPE(String value) {
            this.movevaltype = value;
        }

        /**
         * ��ȡnbslips���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNBSLIPS() {
            return nbslips;
        }

        /**
         * ����nbslips���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNBSLIPS(String value) {
            this.nbslips = value;
        }

        /**
         * ��ȡwbselem���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWBSELEM() {
            return wbselem;
        }

        /**
         * ����wbselem���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWBSELEM(String value) {
            this.wbselem = value;
        }

        /**
         * ��ȡvalwbselem���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVALWBSELEM() {
            return valwbselem;
        }

        /**
         * ����valwbselem���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVALWBSELEM(String value) {
            this.valwbselem = value;
        }

        /**
         * ��ȡorderid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getORDERID() {
            return orderid;
        }

        /**
         * ����orderid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setORDERID(String value) {
            this.orderid = value;
        }

        /**
         * ��ȡorderitno���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getORDERITNO() {
            return orderitno;
        }

        /**
         * ����orderitno���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setORDERITNO(String value) {
            this.orderitno = value;
        }

        /**
         * ��ȡmvtind���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMVTIND() {
            return mvtind;
        }

        /**
         * ����mvtind���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMVTIND(String value) {
            this.mvtind = value;
        }

        /**
         * ��ȡmovereas���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOVEREAS() {
            return movereas;
        }

        /**
         * ����movereas���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOVEREAS(String value) {
            this.movereas = value;
        }

        /**
         * ��ȡrefdocyr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREFDOCYR() {
            return refdocyr;
        }

        /**
         * ����refdocyr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREFDOCYR(String value) {
            this.refdocyr = value;
        }

        /**
         * ��ȡrefdoc���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREFDOC() {
            return refdoc;
        }

        /**
         * ����refdoc���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREFDOC(String value) {
            this.refdoc = value;
        }

        /**
         * ��ȡrefdocit���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREFDOCIT() {
            return refdocit;
        }

        /**
         * ����refdocit���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREFDOCIT(String value) {
            this.refdocit = value;
        }

        /**
         * ��ȡiwmserpsid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIWMSERPSID() {
            return iwmserpsid;
        }

        /**
         * ����iwmserpsid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIWMSERPSID(String value) {
            this.iwmserpsid = value;
        }

        /**
         * ��ȡzmroyl01���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL01() {
            return zmroyl01;
        }

        /**
         * ����zmroyl01���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL01(String value) {
            this.zmroyl01 = value;
        }

        /**
         * ��ȡzmroyl02���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL02() {
            return zmroyl02;
        }

        /**
         * ����zmroyl02���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL02(String value) {
            this.zmroyl02 = value;
        }

        /**
         * ��ȡzmroyl03���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL03() {
            return zmroyl03;
        }

        /**
         * ����zmroyl03���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL03(String value) {
            this.zmroyl03 = value;
        }

        /**
         * ��ȡzmroyl04���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL04() {
            return zmroyl04;
        }

        /**
         * ����zmroyl04���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL04(String value) {
            this.zmroyl04 = value;
        }

        /**
         * ��ȡzmroyl05���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL05() {
            return zmroyl05;
        }

        /**
         * ����zmroyl05���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL05(String value) {
            this.zmroyl05 = value;
        }

        /**
         * ��ȡzmroyl06���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL06() {
            return zmroyl06;
        }

        /**
         * ����zmroyl06���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL06(String value) {
            this.zmroyl06 = value;
        }

        /**
         * ��ȡzmroyl07���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL07() {
            return zmroyl07;
        }

        /**
         * ����zmroyl07���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL07(String value) {
            this.zmroyl07 = value;
        }

        /**
         * ��ȡzmroyl08���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL08() {
            return zmroyl08;
        }

        /**
         * ����zmroyl08���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL08(String value) {
            this.zmroyl08 = value;
        }

        /**
         * ��ȡzmroyl09���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL09() {
            return zmroyl09;
        }

        /**
         * ����zmroyl09���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL09(String value) {
            this.zmroyl09 = value;
        }

        /**
         * ��ȡzmroyl10���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZMROYL10() {
            return zmroyl10;
        }

        /**
         * ����zmroyl10���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZMROYL10(String value) {
            this.zmroyl10 = value;
        }

    }

}
