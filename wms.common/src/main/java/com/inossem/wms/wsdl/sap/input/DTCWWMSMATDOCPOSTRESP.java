
package com.inossem.wms.wsdl.sap.input;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳ����ƾ֤���˷������ݶ��壨���ã�
 * 
 * <p>DT_CW_WMS_MATDOC_POST_RESP complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_MATDOC_POST_RESP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ES_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD" minOccurs="0"/>
 *         &lt;element name="ET_LOG" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_YEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MAT_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MAT_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ET_RETURN" type="{urn:sinopec:ecc:iwms:cw}DT_RETURN" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_CW_WMS_MATDOC_POST_RESP", propOrder = {
    "esmsghead",
    "etlog",
    "etreturn"
})
public class DTCWWMSMATDOCPOSTRESP {

    @XmlElement(name = "ES_MSG_HEAD")
    protected MSGHEAD esmsghead;
    @XmlElement(name = "ET_LOG")
    protected List<DTCWWMSMATDOCPOSTRESP.ETLOG> etlog;
    @XmlElement(name = "ET_RETURN")
    protected List<DTRETURN> etreturn;

    /**
     * ��ȡesmsghead���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link MSGHEAD }
     *     
     */
    public MSGHEAD getESMSGHEAD() {
        return esmsghead;
    }

    /**
     * ����esmsghead���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link MSGHEAD }
     *     
     */
    public void setESMSGHEAD(MSGHEAD value) {
        this.esmsghead = value;
    }

    /**
     * Gets the value of the etlog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etlog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getETLOG().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSMATDOCPOSTRESP.ETLOG }
     * 
     * 
     */
    public List<DTCWWMSMATDOCPOSTRESP.ETLOG> getETLOG() {
        if (etlog == null) {
            etlog = new ArrayList<DTCWWMSMATDOCPOSTRESP.ETLOG>();
        }
        return this.etlog;
    }

    /**
     * Gets the value of the etreturn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etreturn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getETRETURN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTRETURN }
     * 
     * 
     */
    public List<DTRETURN> getETRETURN() {
        if (etreturn == null) {
            etreturn = new ArrayList<DTRETURN>();
        }
        return this.etreturn;
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
     *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_YEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MAT_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MAT_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "type",
        "docyear",
        "matdoc",
        "matdocitem",
        "docnum",
        "docitemnum",
        "message",
        "number",
        "iwmserpsid"
    })
    public static class ETLOG {

        @XmlElement(name = "TYPE")
        protected String type;
        @XmlElement(name = "DOC_YEAR")
        protected String docyear;
        @XmlElement(name = "MAT_DOC")
        protected String matdoc;
        @XmlElement(name = "MAT_DOC_ITEM")
        protected String matdocitem;
        @XmlElement(name = "DOC_NUM")
        protected String docnum;
        @XmlElement(name = "DOC_ITEM_NUM")
        protected String docitemnum;
        @XmlElement(name = "MESSAGE")
        protected String message;
        @XmlElement(name = "NUMBER")
        protected String number;
        @XmlElement(name = "IWMS_ERP_SID")
        protected String iwmserpsid;

        /**
         * ��ȡtype���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTYPE() {
            return type;
        }

        /**
         * ����type���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTYPE(String value) {
            this.type = value;
        }

        /**
         * ��ȡdocyear���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDOCYEAR() {
            return docyear;
        }

        /**
         * ����docyear���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDOCYEAR(String value) {
            this.docyear = value;
        }

        /**
         * ��ȡmatdoc���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATDOC() {
            return matdoc;
        }

        /**
         * ����matdoc���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATDOC(String value) {
            this.matdoc = value;
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
         * ��ȡmessage���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMESSAGE() {
            return message;
        }

        /**
         * ����message���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMESSAGE(String value) {
            this.message = value;
        }

        /**
         * ��ȡnumber���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMBER() {
            return number;
        }

        /**
         * ����number���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMBER(String value) {
            this.number = value;
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
