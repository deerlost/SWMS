
package com.inossem.wms.wsdl.sap.output;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳ���򽻻��������������ݶ���
 * 
 * <p>DT_CW_WMS_DO_POST_REQ complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_DO_POST_REQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IS_MSG_HEAD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="MANDT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="GUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PROXY_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SYSTEM_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SPRAS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="INTERFACE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SENDER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="RECIVER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SENDTIME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IV_BUKRS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IV_TESTRUN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IS_SPLIT_BATCH" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZLT_SD_BATCH_CONTENT" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="LFIMG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="CHARG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IT_LIKP" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WADAT_IST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BTGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NTGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="GEWEI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VOLUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VOLEH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LGTOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="INCO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="INCO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ROUTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VSBED" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LPRIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ABLAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LIFSK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LFDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="KODAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WADAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LGMNG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="UECHA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IT_LIPS" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CHARG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LFIMG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="UMREF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BRGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NTGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="GEWEI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VOLEH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VRKME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="INSMK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BWART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="QPLOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VOLUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="UMVKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="UMVKN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IT_PARTNER" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ZLT_SD_PARTNER_CONTENT" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="PARVW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="KUNNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
@XmlType(name = "DT_CW_WMS_DO_POST_REQ", propOrder = {
    "ismsghead",
    "ivbukrs",
    "ivtestrun",
    "issplitbatch",
    "itlikp",
    "itlips",
    "itpartner"
})
public class DTCWWMSDOPOSTREQ {

    @XmlElement(name = "IS_MSG_HEAD")
    protected DTCWWMSDOPOSTREQ.ISMSGHEAD ismsghead;
    @XmlElement(name = "IV_BUKRS")
    protected String ivbukrs;
    @XmlElement(name = "IV_TESTRUN")
    protected String ivtestrun;
    @XmlElement(name = "IS_SPLIT_BATCH")
    protected List<DTCWWMSDOPOSTREQ.ISSPLITBATCH> issplitbatch;
    @XmlElement(name = "IT_LIKP")
    protected List<DTCWWMSDOPOSTREQ.ITLIKP> itlikp;
    @XmlElement(name = "IT_LIPS")
    protected List<DTCWWMSDOPOSTREQ.ITLIPS> itlips;
    @XmlElement(name = "IT_PARTNER")
    protected List<DTCWWMSDOPOSTREQ.ITPARTNER> itpartner;

    
    
    
    public DTCWWMSDOPOSTREQ.ISMSGHEAD getIsmsghead() {
		return ismsghead;
	}

	public void setIsmsghead(DTCWWMSDOPOSTREQ.ISMSGHEAD ismsghead) {
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

	public List<DTCWWMSDOPOSTREQ.ISSPLITBATCH> getIssplitbatch() {
		return issplitbatch;
	}

	public void setIssplitbatch(List<DTCWWMSDOPOSTREQ.ISSPLITBATCH> issplitbatch) {
		this.issplitbatch = issplitbatch;
	}

	public List<DTCWWMSDOPOSTREQ.ITLIKP> getItlikp() {
		return itlikp;
	}

	public void setItlikp(List<DTCWWMSDOPOSTREQ.ITLIKP> itlikp) {
		this.itlikp = itlikp;
	}

	public List<DTCWWMSDOPOSTREQ.ITLIPS> getItlips() {
		return itlips;
	}

	public void setItlips(List<DTCWWMSDOPOSTREQ.ITLIPS> itlips) {
		this.itlips = itlips;
	}

	public List<DTCWWMSDOPOSTREQ.ITPARTNER> getItpartner() {
		return itpartner;
	}

	public void setItpartner(List<DTCWWMSDOPOSTREQ.ITPARTNER> itpartner) {
		this.itpartner = itpartner;
	}

	/**
     * ��ȡismsghead���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link DTCWWMSDOPOSTREQ.ISMSGHEAD }
     *     
     */
    public DTCWWMSDOPOSTREQ.ISMSGHEAD getISMSGHEAD() {
        return ismsghead;
    }

    /**
     * ����ismsghead���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link DTCWWMSDOPOSTREQ.ISMSGHEAD }
     *     
     */
    public void setISMSGHEAD(DTCWWMSDOPOSTREQ.ISMSGHEAD value) {
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
     * Gets the value of the issplitbatch property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the issplitbatch property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getISSPLITBATCH().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSDOPOSTREQ.ISSPLITBATCH }
     * 
     * 
     */
    public List<DTCWWMSDOPOSTREQ.ISSPLITBATCH> getISSPLITBATCH() {
        if (issplitbatch == null) {
            issplitbatch = new ArrayList<DTCWWMSDOPOSTREQ.ISSPLITBATCH>();
        }
        return this.issplitbatch;
    }

    /**
     * Gets the value of the itlikp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itlikp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITLIKP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSDOPOSTREQ.ITLIKP }
     * 
     * 
     */
    public List<DTCWWMSDOPOSTREQ.ITLIKP> getITLIKP() {
        if (itlikp == null) {
            itlikp = new ArrayList<DTCWWMSDOPOSTREQ.ITLIKP>();
        }
        return this.itlikp;
    }

    /**
     * Gets the value of the itlips property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itlips property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITLIPS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSDOPOSTREQ.ITLIPS }
     * 
     * 
     */
    public List<DTCWWMSDOPOSTREQ.ITLIPS> getITLIPS() {
        if (itlips == null) {
            itlips = new ArrayList<DTCWWMSDOPOSTREQ.ITLIPS>();
        }
        return this.itlips;
    }

    /**
     * Gets the value of the itpartner property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itpartner property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITPARTNER().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSDOPOSTREQ.ITPARTNER }
     * 
     * 
     */
    public List<DTCWWMSDOPOSTREQ.ITPARTNER> getITPARTNER() {
        if (itpartner == null) {
            itpartner = new ArrayList<DTCWWMSDOPOSTREQ.ITPARTNER>();
        }
        return this.itpartner;
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
     *         &lt;element name="MANDT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="GUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PROXY_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SYSTEM_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SPRAS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="INTERFACE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SENDER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="RECIVER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SENDTIME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "mandt",
        "guid",
        "proxyid",
        "systemid",
        "operator",
        "spras",
        "interfaceid",
        "sender",
        "reciver",
        "sendtime"
    })
    public static class ISMSGHEAD {

        @XmlElement(name = "MANDT")
        protected String mandt;
        @XmlElement(name = "GUID")
        protected String guid;
        @XmlElement(name = "PROXY_ID")
        protected String proxyid;
        @XmlElement(name = "SYSTEM_ID")
        protected String systemid;
        @XmlElement(name = "OPERATOR")
        protected String operator;
        @XmlElement(name = "SPRAS")
        protected String spras;
        @XmlElement(name = "INTERFACE_ID")
        protected String interfaceid;
        @XmlElement(name = "SENDER")
        protected String sender;
        @XmlElement(name = "RECIVER")
        protected String reciver;
        @XmlElement(name = "SENDTIME")
        protected String sendtime;

        
        
        public String getMandt() {
			return mandt;
		}

		public void setMandt(String mandt) {
			this.mandt = mandt;
		}

		public String getGuid() {
			return guid;
		}

		public void setGuid(String guid) {
			this.guid = guid;
		}

		public String getProxyid() {
			return proxyid;
		}

		public void setProxyid(String proxyid) {
			this.proxyid = proxyid;
		}

		public String getSystemid() {
			return systemid;
		}

		public void setSystemid(String systemid) {
			this.systemid = systemid;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

		public String getSpras() {
			return spras;
		}

		public void setSpras(String spras) {
			this.spras = spras;
		}

		public String getInterfaceid() {
			return interfaceid;
		}

		public void setInterfaceid(String interfaceid) {
			this.interfaceid = interfaceid;
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}

		public String getReciver() {
			return reciver;
		}

		public void setReciver(String reciver) {
			this.reciver = reciver;
		}

		public String getSendtime() {
			return sendtime;
		}

		public void setSendtime(String sendtime) {
			this.sendtime = sendtime;
		}

		/**
         * ��ȡmandt���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMANDT() {
            return mandt;
        }

        /**
         * ����mandt���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMANDT(String value) {
            this.mandt = value;
        }

        /**
         * ��ȡguid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGUID() {
            return guid;
        }

        /**
         * ����guid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGUID(String value) {
            this.guid = value;
        }

        /**
         * ��ȡproxyid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPROXYID() {
            return proxyid;
        }

        /**
         * ����proxyid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPROXYID(String value) {
            this.proxyid = value;
        }

        /**
         * ��ȡsystemid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSYSTEMID() {
            return systemid;
        }

        /**
         * ����systemid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSYSTEMID(String value) {
            this.systemid = value;
        }

        /**
         * ��ȡoperator���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOPERATOR() {
            return operator;
        }

        /**
         * ����operator���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOPERATOR(String value) {
            this.operator = value;
        }

        /**
         * ��ȡspras���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPRAS() {
            return spras;
        }

        /**
         * ����spras���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPRAS(String value) {
            this.spras = value;
        }

        /**
         * ��ȡinterfaceid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINTERFACEID() {
            return interfaceid;
        }

        /**
         * ����interfaceid���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINTERFACEID(String value) {
            this.interfaceid = value;
        }

        /**
         * ��ȡsender���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSENDER() {
            return sender;
        }

        /**
         * ����sender���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSENDER(String value) {
            this.sender = value;
        }

        /**
         * ��ȡreciver���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRECIVER() {
            return reciver;
        }

        /**
         * ����reciver���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRECIVER(String value) {
            this.reciver = value;
        }

        /**
         * ��ȡsendtime���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSENDTIME() {
            return sendtime;
        }

        /**
         * ����sendtime���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSENDTIME(String value) {
            this.sendtime = value;
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
     *         &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZLT_SD_BATCH_CONTENT" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="LFIMG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="CHARG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    @XmlType(name = "", propOrder = {
        "vbeln",
        "posnr",
        "matnr",
        "zltsdbatchcontent"
    })
    public static class ISSPLITBATCH {

        @XmlElement(name = "VBELN")
        protected String vbeln;
        @XmlElement(name = "POSNR")
        protected String posnr;
        @XmlElement(name = "MATNR")
        protected String matnr;
        @XmlElement(name = "ZLT_SD_BATCH_CONTENT")
        protected List<DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT> zltsdbatchcontent;

        
        
        public String getVbeln() {
			return vbeln;
		}

		public void setVbeln(String vbeln) {
			this.vbeln = vbeln;
		}

		public String getPosnr() {
			return posnr;
		}

		public void setPosnr(String posnr) {
			this.posnr = posnr;
		}

		public String getMatnr() {
			return matnr;
		}

		public void setMatnr(String matnr) {
			this.matnr = matnr;
		}

		public List<DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT> getZltsdbatchcontent() {
			return zltsdbatchcontent;
		}

		public void setZltsdbatchcontent(List<DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT> zltsdbatchcontent) {
			this.zltsdbatchcontent = zltsdbatchcontent;
		}

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
         * ��ȡposnr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPOSNR() {
            return posnr;
        }

        /**
         * ����posnr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPOSNR(String value) {
            this.posnr = value;
        }

        /**
         * ��ȡmatnr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATNR() {
            return matnr;
        }

        /**
         * ����matnr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATNR(String value) {
            this.matnr = value;
        }

        /**
         * Gets the value of the zltsdbatchcontent property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the zltsdbatchcontent property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getZLTSDBATCHCONTENT().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT }
         * 
         * 
         */
        public List<DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT> getZLTSDBATCHCONTENT() {
            if (zltsdbatchcontent == null) {
                zltsdbatchcontent = new ArrayList<DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT>();
            }
            return this.zltsdbatchcontent;
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
         *         &lt;element name="LFIMG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="CHARG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "lfimg",
            "lgort",
            "charg",
            "docnum",
            "docitemnum",
            "iwmserpsid"
        })
        public static class ZLTSDBATCHCONTENT {

            @XmlElement(name = "LFIMG")
            protected String lfimg;
            @XmlElement(name = "LGORT")
            protected String lgort;
            @XmlElement(name = "CHARG")
            protected String charg;
            @XmlElement(name = "DOC_NUM")
            protected String docnum;
            @XmlElement(name = "DOC_ITEM_NUM")
            protected String docitemnum;
            @XmlElement(name = "IWMS_ERP_SID")
            protected String iwmserpsid;

            
            
            
            public String getLfimg() {
				return lfimg;
			}

			public void setLfimg(String lfimg) {
				this.lfimg = lfimg;
			}

			public String getLgort() {
				return lgort;
			}

			public void setLgort(String lgort) {
				this.lgort = lgort;
			}

			public String getCharg() {
				return charg;
			}

			public void setCharg(String charg) {
				this.charg = charg;
			}

			public String getDocnum() {
				return docnum;
			}

			public void setDocnum(String docnum) {
				this.docnum = docnum;
			}

			public String getDocitemnum() {
				return docitemnum;
			}

			public void setDocitemnum(String docitemnum) {
				this.docitemnum = docitemnum;
			}

			public String getIwmserpsid() {
				return iwmserpsid;
			}

			public void setIwmserpsid(String iwmserpsid) {
				this.iwmserpsid = iwmserpsid;
			}

			/**
             * ��ȡlfimg���Ե�ֵ��
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLFIMG() {
                return lfimg;
            }

            /**
             * ����lfimg���Ե�ֵ��
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLFIMG(String value) {
                this.lfimg = value;
            }

            /**
             * ��ȡlgort���Ե�ֵ��
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLGORT() {
                return lgort;
            }

            /**
             * ����lgort���Ե�ֵ��
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLGORT(String value) {
                this.lgort = value;
            }

            /**
             * ��ȡcharg���Ե�ֵ��
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCHARG() {
                return charg;
            }

            /**
             * ����charg���Ե�ֵ��
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCHARG(String value) {
                this.charg = value;
            }

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
     *         &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WADAT_IST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BTGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NTGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="GEWEI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VOLUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VOLEH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LGTOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="INCO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="INCO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ROUTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VSBED" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LPRIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ABLAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LIFSK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LFDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="KODAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WADAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LGMNG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="UECHA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "zflag",
        "vbeln",
        "wadatist",
        "btgew",
        "ntgew",
        "gewei",
        "volum",
        "voleh",
        "lgtor",
        "inco1",
        "inco2",
        "route",
        "vsbed",
        "lprio",
        "ablad",
        "lifsk",
        "lfdat",
        "kodat",
        "tddat",
        "lddat",
        "wadat",
        "lgmng",
        "uecha"
    })
    public static class ITLIKP {

        @XmlElement(name = "ZFLAG")
        protected String zflag;
        @XmlElement(name = "VBELN")
        protected String vbeln;
        @XmlElement(name = "WADAT_IST")
        protected String wadatist;
        @XmlElement(name = "BTGEW")
        protected String btgew;
        @XmlElement(name = "NTGEW")
        protected String ntgew;
        @XmlElement(name = "GEWEI")
        protected String gewei;
        @XmlElement(name = "VOLUM")
        protected String volum;
        @XmlElement(name = "VOLEH")
        protected String voleh;
        @XmlElement(name = "LGTOR")
        protected String lgtor;
        @XmlElement(name = "INCO1")
        protected String inco1;
        @XmlElement(name = "INCO2")
        protected String inco2;
        @XmlElement(name = "ROUTE")
        protected String route;
        @XmlElement(name = "VSBED")
        protected String vsbed;
        @XmlElement(name = "LPRIO")
        protected String lprio;
        @XmlElement(name = "ABLAD")
        protected String ablad;
        @XmlElement(name = "LIFSK")
        protected String lifsk;
        @XmlElement(name = "LFDAT")
        protected String lfdat;
        @XmlElement(name = "KODAT")
        protected String kodat;
        @XmlElement(name = "TDDAT")
        protected String tddat;
        @XmlElement(name = "LDDAT")
        protected String lddat;
        @XmlElement(name = "WADAT")
        protected String wadat;
        @XmlElement(name = "LGMNG")
        protected String lgmng;
        @XmlElement(name = "UECHA")
        protected String uecha;

        public String getZflag() {
			return zflag;
		}

		public void setZflag(String zflag) {
			this.zflag = zflag;
		}

		public String getVbeln() {
			return vbeln;
		}

		public void setVbeln(String vbeln) {
			this.vbeln = vbeln;
		}

		public String getWadatist() {
			return wadatist;
		}

		public void setWadatist(String wadatist) {
			this.wadatist = wadatist;
		}

		public String getBtgew() {
			return btgew;
		}

		public void setBtgew(String btgew) {
			this.btgew = btgew;
		}

		public String getNtgew() {
			return ntgew;
		}

		public void setNtgew(String ntgew) {
			this.ntgew = ntgew;
		}

		public String getGewei() {
			return gewei;
		}

		public void setGewei(String gewei) {
			this.gewei = gewei;
		}

		public String getVolum() {
			return volum;
		}

		public void setVolum(String volum) {
			this.volum = volum;
		}

		public String getVoleh() {
			return voleh;
		}

		public void setVoleh(String voleh) {
			this.voleh = voleh;
		}

		public String getLgtor() {
			return lgtor;
		}

		public void setLgtor(String lgtor) {
			this.lgtor = lgtor;
		}

		public String getInco1() {
			return inco1;
		}

		public void setInco1(String inco1) {
			this.inco1 = inco1;
		}

		public String getInco2() {
			return inco2;
		}

		public void setInco2(String inco2) {
			this.inco2 = inco2;
		}

		public String getRoute() {
			return route;
		}

		public void setRoute(String route) {
			this.route = route;
		}

		public String getVsbed() {
			return vsbed;
		}

		public void setVsbed(String vsbed) {
			this.vsbed = vsbed;
		}

		public String getLprio() {
			return lprio;
		}

		public void setLprio(String lprio) {
			this.lprio = lprio;
		}

		public String getAblad() {
			return ablad;
		}

		public void setAblad(String ablad) {
			this.ablad = ablad;
		}

		public String getLifsk() {
			return lifsk;
		}

		public void setLifsk(String lifsk) {
			this.lifsk = lifsk;
		}

		public String getLfdat() {
			return lfdat;
		}

		public void setLfdat(String lfdat) {
			this.lfdat = lfdat;
		}

		public String getKodat() {
			return kodat;
		}

		public void setKodat(String kodat) {
			this.kodat = kodat;
		}

		public String getTddat() {
			return tddat;
		}

		public void setTddat(String tddat) {
			this.tddat = tddat;
		}

		public String getLddat() {
			return lddat;
		}

		public void setLddat(String lddat) {
			this.lddat = lddat;
		}

		public String getWadat() {
			return wadat;
		}

		public void setWadat(String wadat) {
			this.wadat = wadat;
		}

		public String getLgmng() {
			return lgmng;
		}

		public void setLgmng(String lgmng) {
			this.lgmng = lgmng;
		}

		public String getUecha() {
			return uecha;
		}

		public void setUecha(String uecha) {
			this.uecha = uecha;
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
         * ��ȡwadatist���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWADATIST() {
            return wadatist;
        }

        /**
         * ����wadatist���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWADATIST(String value) {
            this.wadatist = value;
        }

        /**
         * ��ȡbtgew���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBTGEW() {
            return btgew;
        }

        /**
         * ����btgew���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBTGEW(String value) {
            this.btgew = value;
        }

        /**
         * ��ȡntgew���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNTGEW() {
            return ntgew;
        }

        /**
         * ����ntgew���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNTGEW(String value) {
            this.ntgew = value;
        }

        /**
         * ��ȡgewei���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGEWEI() {
            return gewei;
        }

        /**
         * ����gewei���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGEWEI(String value) {
            this.gewei = value;
        }

        /**
         * ��ȡvolum���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVOLUM() {
            return volum;
        }

        /**
         * ����volum���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVOLUM(String value) {
            this.volum = value;
        }

        /**
         * ��ȡvoleh���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVOLEH() {
            return voleh;
        }

        /**
         * ����voleh���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVOLEH(String value) {
            this.voleh = value;
        }

        /**
         * ��ȡlgtor���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLGTOR() {
            return lgtor;
        }

        /**
         * ����lgtor���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLGTOR(String value) {
            this.lgtor = value;
        }

        /**
         * ��ȡinco1���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINCO1() {
            return inco1;
        }

        /**
         * ����inco1���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINCO1(String value) {
            this.inco1 = value;
        }

        /**
         * ��ȡinco2���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINCO2() {
            return inco2;
        }

        /**
         * ����inco2���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINCO2(String value) {
            this.inco2 = value;
        }

        /**
         * ��ȡroute���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getROUTE() {
            return route;
        }

        /**
         * ����route���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setROUTE(String value) {
            this.route = value;
        }

        /**
         * ��ȡvsbed���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVSBED() {
            return vsbed;
        }

        /**
         * ����vsbed���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVSBED(String value) {
            this.vsbed = value;
        }

        /**
         * ��ȡlprio���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLPRIO() {
            return lprio;
        }

        /**
         * ����lprio���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLPRIO(String value) {
            this.lprio = value;
        }

        /**
         * ��ȡablad���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getABLAD() {
            return ablad;
        }

        /**
         * ����ablad���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setABLAD(String value) {
            this.ablad = value;
        }

        /**
         * ��ȡlifsk���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLIFSK() {
            return lifsk;
        }

        /**
         * ����lifsk���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLIFSK(String value) {
            this.lifsk = value;
        }

        /**
         * ��ȡlfdat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLFDAT() {
            return lfdat;
        }

        /**
         * ����lfdat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLFDAT(String value) {
            this.lfdat = value;
        }

        /**
         * ��ȡkodat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKODAT() {
            return kodat;
        }

        /**
         * ����kodat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKODAT(String value) {
            this.kodat = value;
        }

        /**
         * ��ȡtddat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTDDAT() {
            return tddat;
        }

        /**
         * ����tddat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTDDAT(String value) {
            this.tddat = value;
        }

        /**
         * ��ȡlddat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLDDAT() {
            return lddat;
        }

        /**
         * ����lddat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLDDAT(String value) {
            this.lddat = value;
        }

        /**
         * ��ȡwadat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWADAT() {
            return wadat;
        }

        /**
         * ����wadat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWADAT(String value) {
            this.wadat = value;
        }

        /**
         * ��ȡlgmng���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLGMNG() {
            return lgmng;
        }

        /**
         * ����lgmng���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLGMNG(String value) {
            this.lgmng = value;
        }

        /**
         * ��ȡuecha���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUECHA() {
            return uecha;
        }

        /**
         * ����uecha���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUECHA(String value) {
            this.uecha = value;
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
     *         &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CHARG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LFIMG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="UMREF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BRGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NTGEW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="GEWEI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VOLEH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VRKME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="INSMK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BWART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="QPLOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VOLUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="UMVKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="UMVKN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "zflag",
        "vbeln",
        "posnr",
        "matnr",
        "werks",
        "lgort",
        "meins",
        "charg",
        "lfimg",
        "umref",
        "brgew",
        "ntgew",
        "gewei",
        "voleh",
        "vrkme",
        "insmk",
        "bwart",
        "qplos",
        "volum",
        "umvkz",
        "umvkn"
    })
    public static class ITLIPS {

        @XmlElement(name = "ZFLAG")
        protected String zflag;
        @XmlElement(name = "VBELN")
        protected String vbeln;
        @XmlElement(name = "POSNR")
        protected String posnr;
        @XmlElement(name = "MATNR")
        protected String matnr;
        @XmlElement(name = "WERKS")
        protected String werks;
        @XmlElement(name = "LGORT")
        protected String lgort;
        @XmlElement(name = "MEINS")
        protected String meins;
        @XmlElement(name = "CHARG")
        protected String charg;
        @XmlElement(name = "LFIMG")
        protected String lfimg;
        @XmlElement(name = "UMREF")
        protected String umref;
        @XmlElement(name = "BRGEW")
        protected String brgew;
        @XmlElement(name = "NTGEW")
        protected String ntgew;
        @XmlElement(name = "GEWEI")
        protected String gewei;
        @XmlElement(name = "VOLEH")
        protected String voleh;
        @XmlElement(name = "VRKME")
        protected String vrkme;
        @XmlElement(name = "INSMK")
        protected String insmk;
        @XmlElement(name = "BWART")
        protected String bwart;
        @XmlElement(name = "QPLOS")
        protected String qplos;
        @XmlElement(name = "VOLUM")
        protected String volum;
        @XmlElement(name = "UMVKZ")
        protected String umvkz;
        @XmlElement(name = "UMVKN")
        protected String umvkn;

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
         * ��ȡposnr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPOSNR() {
            return posnr;
        }

        /**
         * ����posnr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPOSNR(String value) {
            this.posnr = value;
        }

        /**
         * ��ȡmatnr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATNR() {
            return matnr;
        }

        /**
         * ����matnr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATNR(String value) {
            this.matnr = value;
        }

        /**
         * ��ȡwerks���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWERKS() {
            return werks;
        }

        /**
         * ����werks���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWERKS(String value) {
            this.werks = value;
        }

        /**
         * ��ȡlgort���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLGORT() {
            return lgort;
        }

        /**
         * ����lgort���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLGORT(String value) {
            this.lgort = value;
        }

        /**
         * ��ȡmeins���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMEINS() {
            return meins;
        }

        /**
         * ����meins���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMEINS(String value) {
            this.meins = value;
        }

        /**
         * ��ȡcharg���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCHARG() {
            return charg;
        }

        /**
         * ����charg���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCHARG(String value) {
            this.charg = value;
        }

        /**
         * ��ȡlfimg���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLFIMG() {
            return lfimg;
        }

        /**
         * ����lfimg���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLFIMG(String value) {
            this.lfimg = value;
        }

        /**
         * ��ȡumref���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUMREF() {
            return umref;
        }

        /**
         * ����umref���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUMREF(String value) {
            this.umref = value;
        }

        /**
         * ��ȡbrgew���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBRGEW() {
            return brgew;
        }

        /**
         * ����brgew���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBRGEW(String value) {
            this.brgew = value;
        }

        /**
         * ��ȡntgew���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNTGEW() {
            return ntgew;
        }

        /**
         * ����ntgew���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNTGEW(String value) {
            this.ntgew = value;
        }

        /**
         * ��ȡgewei���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGEWEI() {
            return gewei;
        }

        /**
         * ����gewei���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGEWEI(String value) {
            this.gewei = value;
        }

        /**
         * ��ȡvoleh���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVOLEH() {
            return voleh;
        }

        /**
         * ����voleh���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVOLEH(String value) {
            this.voleh = value;
        }

        /**
         * ��ȡvrkme���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVRKME() {
            return vrkme;
        }

        /**
         * ����vrkme���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVRKME(String value) {
            this.vrkme = value;
        }

        /**
         * ��ȡinsmk���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINSMK() {
            return insmk;
        }

        /**
         * ����insmk���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINSMK(String value) {
            this.insmk = value;
        }

        /**
         * ��ȡbwart���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBWART() {
            return bwart;
        }

        /**
         * ����bwart���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBWART(String value) {
            this.bwart = value;
        }

        /**
         * ��ȡqplos���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQPLOS() {
            return qplos;
        }

        /**
         * ����qplos���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQPLOS(String value) {
            this.qplos = value;
        }

        /**
         * ��ȡvolum���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVOLUM() {
            return volum;
        }

        /**
         * ����volum���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVOLUM(String value) {
            this.volum = value;
        }

        /**
         * ��ȡumvkz���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUMVKZ() {
            return umvkz;
        }

        /**
         * ����umvkz���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUMVKZ(String value) {
            this.umvkz = value;
        }

        /**
         * ��ȡumvkn���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUMVKN() {
            return umvkn;
        }

        /**
         * ����umvkn���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUMVKN(String value) {
            this.umvkn = value;
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
     *         &lt;element name="ZFLAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ZLT_SD_PARTNER_CONTENT" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="PARVW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="KUNNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    @XmlType(name = "", propOrder = {
        "zflag",
        "vbeln",
        "posnr",
        "zltsdpartnercontent"
    })
    public static class ITPARTNER {

        @XmlElement(name = "ZFLAG")
        protected String zflag;
        @XmlElement(name = "VBELN")
        protected String vbeln;
        @XmlElement(name = "POSNR")
        protected String posnr;
        @XmlElement(name = "ZLT_SD_PARTNER_CONTENT")
        protected List<DTCWWMSDOPOSTREQ.ITPARTNER.ZLTSDPARTNERCONTENT> zltsdpartnercontent;

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
         * ��ȡposnr���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPOSNR() {
            return posnr;
        }

        /**
         * ����posnr���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPOSNR(String value) {
            this.posnr = value;
        }

        /**
         * Gets the value of the zltsdpartnercontent property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the zltsdpartnercontent property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getZLTSDPARTNERCONTENT().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DTCWWMSDOPOSTREQ.ITPARTNER.ZLTSDPARTNERCONTENT }
         * 
         * 
         */
        public List<DTCWWMSDOPOSTREQ.ITPARTNER.ZLTSDPARTNERCONTENT> getZLTSDPARTNERCONTENT() {
            if (zltsdpartnercontent == null) {
                zltsdpartnercontent = new ArrayList<DTCWWMSDOPOSTREQ.ITPARTNER.ZLTSDPARTNERCONTENT>();
            }
            return this.zltsdpartnercontent;
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
         *         &lt;element name="PARVW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="KUNNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "parvw",
            "kunnr",
            "lifnr"
        })
        public static class ZLTSDPARTNERCONTENT {

            @XmlElement(name = "PARVW")
            protected String parvw;
            @XmlElement(name = "KUNNR")
            protected String kunnr;
            @XmlElement(name = "LIFNR")
            protected String lifnr;

            /**
             * ��ȡparvw���Ե�ֵ��
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPARVW() {
                return parvw;
            }

            /**
             * ����parvw���Ե�ֵ��
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPARVW(String value) {
                this.parvw = value;
            }

            /**
             * ��ȡkunnr���Ե�ֵ��
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKUNNR() {
                return kunnr;
            }

            /**
             * ����kunnr���Ե�ֵ��
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKUNNR(String value) {
                this.kunnr = value;
            }

            /**
             * ��ȡlifnr���Ե�ֵ��
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLIFNR() {
                return lifnr;
            }

            /**
             * ����lifnr���Ե�ֵ��
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLIFNR(String value) {
                this.lifnr = value;
            }

        }

    }

}
