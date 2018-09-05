
package com.inossem.wms.wsdl.sap.outputc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳ���򽻻��������������ݶ���
 * 
 * <p>DT_CW_WMS_DO_REVERSE_REQ complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_DO_REVERSE_REQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IS_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD"/>
 *         &lt;element name="IV_BUKRS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IV_TESTRUN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IT_DATA" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="IBUDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ERNAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DT_CW_WMS_DO_REVERSE_REQ", propOrder = {
    "ismsghead",
    "ivbukrs",
    "ivtestrun",
    "itdata"
})
public class DTCWWMSDOREVERSEREQ {

    @XmlElement(name = "IS_MSG_HEAD", required = true)
    protected MSGHEAD ismsghead;
    @XmlElement(name = "IV_BUKRS")
    protected String ivbukrs;
    @XmlElement(name = "IV_TESTRUN")
    protected String ivtestrun;
    @XmlElement(name = "IT_DATA")
    protected List<DTCWWMSDOREVERSEREQ.ITDATA> itdata;
    
    

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

	public List<DTCWWMSDOREVERSEREQ.ITDATA> getItdata() {
		return itdata;
	}

	public void setItdata(List<DTCWWMSDOREVERSEREQ.ITDATA> itdata) {
		this.itdata = itdata;
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
     * Gets the value of the itdata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itdata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITDATA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSDOREVERSEREQ.ITDATA }
     * 
     * 
     */
    public List<DTCWWMSDOREVERSEREQ.ITDATA> getITDATA() {
        if (itdata == null) {
            itdata = new ArrayList<DTCWWMSDOREVERSEREQ.ITDATA>();
        }
        return this.itdata;
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
     *         &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="IBUDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ERNAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "vbeln",
        "ibudat",
        "zflag",
        "ernam",
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
    public static class ITDATA {

        @XmlElement(name = "VBELN")
        protected String vbeln;
        @XmlElement(name = "IBUDAT")
        protected String ibudat;
        @XmlElement(name = "ZFLAG")
        protected String zflag;
        @XmlElement(name = "ERNAM")
        protected String ernam;
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
         * ��ȡvbeln���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVBELN() {
            return vbeln;
        }

        /**
         * ����vbeln���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVBELN(String value) {
            this.vbeln = value;
        }

        /**
         * ��ȡibudat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIBUDAT() {
            return ibudat;
        }

        /**
         * ����ibudat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIBUDAT(String value) {
            this.ibudat = value;
        }

        /**
         * ��ȡzflag���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZFLAG() {
            return zflag;
        }

        /**
         * ����zflag���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZFLAG(String value) {
            this.zflag = value;
        }

        /**
         * ��ȡernam���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getERNAM() {
            return ernam;
        }

        /**
         * ����ernam���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setERNAM(String value) {
            this.ernam = value;
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
