
package com.inossem.wms.wsdl.sap.inputc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳ��������ƾ֤�����������ݶ���(���ã�
 * 
 * <p>DT_CW_WMS_CANCEL_MATDOC_REQ complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_CANCEL_MATDOC_REQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IS_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD"/>
 *         &lt;element name="IV_BUKRS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IV_TESTRUN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IT_MATDOC" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ZLINEID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATERIALDOCUMENT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATDOCUMENTYEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATDOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="GRUND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PR_UNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZMROYL05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DT_CW_WMS_CANCEL_MATDOC_REQ", propOrder = {
    "ismsghead",
    "ivbukrs",
    "ivtestrun",
    "itmatdoc"
})
public class DTCWWMSCANCELMATDOCREQ {

    @XmlElement(name = "IS_MSG_HEAD", required = true)
    protected MSGHEAD ismsghead;
    @XmlElement(name = "IV_BUKRS")
    protected String ivbukrs;
    @XmlElement(name = "IV_TESTRUN")
    protected String ivtestrun;
    @XmlElement(name = "IT_MATDOC")
    protected List<DTCWWMSCANCELMATDOCREQ.ITMATDOC> itmatdoc;

    
    
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

	public List<DTCWWMSCANCELMATDOCREQ.ITMATDOC> getItmatdoc() {
		return itmatdoc;
	}

	public void setItmatdoc(List<DTCWWMSCANCELMATDOCREQ.ITMATDOC> itmatdoc) {
		this.itmatdoc = itmatdoc;
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
     * Gets the value of the itmatdoc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itmatdoc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITMATDOC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSCANCELMATDOCREQ.ITMATDOC }
     * 
     * 
     */
    public List<DTCWWMSCANCELMATDOCREQ.ITMATDOC> getITMATDOC() {
        if (itmatdoc == null) {
            itmatdoc = new ArrayList<DTCWWMSCANCELMATDOCREQ.ITMATDOC>();
        }
        return this.itmatdoc;
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
     *         &lt;element name="ZLINEID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATERIALDOCUMENT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATDOCUMENTYEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATDOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="GRUND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PR_UNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZMROYL05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "zlineid",
        "materialdocument",
        "matdocumentyear",
        "matdocitem",
        "pstngdate",
        "grund",
        "pruname",
        "iwmserpsid",
        "zmroyl01",
        "zmroyl02",
        "zmroyl03",
        "zmroyl04",
        "zmroyl05"
    })
    public static class ITMATDOC {

        @XmlElement(name = "ZLINEID")
        protected String zlineid;
        @XmlElement(name = "MATERIALDOCUMENT")
        protected String materialdocument;
        @XmlElement(name = "MATDOCUMENTYEAR")
        protected String matdocumentyear;
        @XmlElement(name = "MATDOC_ITEM")
        protected String matdocitem;
        @XmlElement(name = "PSTNG_DATE")
        protected String pstngdate;
        @XmlElement(name = "GRUND")
        protected String grund;
        @XmlElement(name = "PR_UNAME")
        protected String pruname;
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

        /**
         * ��ȡzlineid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZLINEID() {
            return zlineid;
        }

        /**
         * ����zlineid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZLINEID(String value) {
            this.zlineid = value;
        }

        /**
         * ��ȡmaterialdocument���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATERIALDOCUMENT() {
            return materialdocument;
        }

        /**
         * ����materialdocument���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATERIALDOCUMENT(String value) {
            this.materialdocument = value;
        }

        /**
         * ��ȡmatdocumentyear���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATDOCUMENTYEAR() {
            return matdocumentyear;
        }

        /**
         * ����matdocumentyear���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATDOCUMENTYEAR(String value) {
            this.matdocumentyear = value;
        }

        /**
         * ��ȡmatdocitem���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATDOCITEM() {
            return matdocitem;
        }

        /**
         * ����matdocitem���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATDOCITEM(String value) {
            this.matdocitem = value;
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
         * ��ȡgrund���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGRUND() {
            return grund;
        }

        /**
         * ����grund���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGRUND(String value) {
            this.grund = value;
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

    }

}
