
package com.inossem.wms.wsdl.sap.inputc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳ��������ƾ֤���˷������ݶ��壨���ã�
 * 
 * <p>DT_CW_WMS_CANCEL_MATDOC_RESP complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_CANCEL_MATDOC_RESP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ES_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD" minOccurs="0"/>
 *         &lt;element name="ET_LOG" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ZLINEID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MAT_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATDOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REVERSE_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REVERSE_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_YEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="IWMS_ERP_SID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ET_RETURN" type="{urn:sinopec:ecc:iwms:cw}DT_ZRETURN" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_CW_WMS_CANCEL_MATDOC_RESP", propOrder = {
    "esmsghead",
    "etlog",
    "etreturn"
})
public class DTCWWMSCANCELMATDOCRESP {

    @XmlElement(name = "ES_MSG_HEAD")
    protected MSGHEAD esmsghead;
    @XmlElement(name = "ET_LOG")
    protected List<DTCWWMSCANCELMATDOCRESP.ETLOG> etlog;
    @XmlElement(name = "ET_RETURN")
    protected List<DTZRETURN> etreturn;

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
     * {@link DTCWWMSCANCELMATDOCRESP.ETLOG }
     * 
     * 
     */
    public List<DTCWWMSCANCELMATDOCRESP.ETLOG> getETLOG() {
        if (etlog == null) {
            etlog = new ArrayList<DTCWWMSCANCELMATDOCRESP.ETLOG>();
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
     * {@link DTZRETURN }
     * 
     * 
     */
    public List<DTZRETURN> getETRETURN() {
        if (etreturn == null) {
            etreturn = new ArrayList<DTZRETURN>();
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
     *         &lt;element name="ZLINEID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MAT_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATDOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REVERSE_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REVERSE_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_YEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "zlineid",
        "number",
        "matdoc",
        "matdocitem",
        "reversedoc",
        "reversedocitem",
        "docyear",
        "type",
        "message",
        "iwmserpsid"
    })
    public static class ETLOG {

        @XmlElement(name = "ZLINEID")
        protected String zlineid;
        @XmlElement(name = "NUMBER")
        protected String number;
        @XmlElement(name = "MAT_DOC")
        protected String matdoc;
        @XmlElement(name = "MATDOC_ITEM")
        protected String matdocitem;
        @XmlElement(name = "REVERSE_DOC")
        protected String reversedoc;
        @XmlElement(name = "REVERSE_DOC_ITEM")
        protected String reversedocitem;
        @XmlElement(name = "DOC_YEAR")
        protected String docyear;
        @XmlElement(name = "TYPE")
        protected String type;
        @XmlElement(name = "MESSAGE")
        protected String message;
        @XmlElement(name = "IWMS_ERP_SID")
        protected String iwmserpsid;

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
         * ��ȡreversedoc���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREVERSEDOC() {
            return reversedoc;
        }

        /**
         * ����reversedoc���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREVERSEDOC(String value) {
            this.reversedoc = value;
        }

        /**
         * ��ȡreversedocitem���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREVERSEDOCITEM() {
            return reversedocitem;
        }

        /**
         * ����reversedocitem���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREVERSEDOCITEM(String value) {
            this.reversedocitem = value;
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
