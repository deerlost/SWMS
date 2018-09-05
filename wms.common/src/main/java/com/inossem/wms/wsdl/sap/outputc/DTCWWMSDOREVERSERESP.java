
package com.inossem.wms.wsdl.sap.outputc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳ���򽻻�����������������ݶ���
 * 
 * <p>DT_CW_WMS_DO_REVERSE_RESP complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_DO_REVERSE_RESP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ES_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD" minOccurs="0"/>
 *         &lt;element name="ET_DATA" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MAT_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATDOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REVERSE_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="REVERSE_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_YEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WADAT_IST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ERNAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DT_CW_WMS_DO_REVERSE_RESP", propOrder = {
    "esmsghead",
    "etdata"
})
public class DTCWWMSDOREVERSERESP {

    @XmlElement(name = "ES_MSG_HEAD")
    protected MSGHEAD esmsghead;
    @XmlElement(name = "ET_DATA")
    protected List<DTCWWMSDOREVERSERESP.ETDATA> etdata;

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
     * Gets the value of the etdata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etdata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getETDATA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSDOREVERSERESP.ETDATA }
     * 
     * 
     */
    public List<DTCWWMSDOREVERSERESP.ETDATA> getETDATA() {
        if (etdata == null) {
            etdata = new ArrayList<DTCWWMSDOREVERSERESP.ETDATA>();
        }
        return this.etdata;
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
     *         &lt;element name="MAT_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATDOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REVERSE_DOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="REVERSE_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_YEAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WADAT_IST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ERNAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "vbeln",
        "matdoc",
        "matdocitem",
        "reversedoc",
        "reversedocitem",
        "docyear",
        "wadatist",
        "ernam",
        "type",
        "id",
        "number",
        "message",
        "iwmserpsid"
    })
    public static class ETDATA {

        @XmlElement(name = "VBELN")
        protected String vbeln;
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
        @XmlElement(name = "WADAT_IST")
        protected String wadatist;
        @XmlElement(name = "ERNAM")
        protected String ernam;
        @XmlElement(name = "TYPE")
        protected String type;
        @XmlElement(name = "ID")
        protected String id;
        @XmlElement(name = "NUMBER")
        protected String number;
        @XmlElement(name = "MESSAGE")
        protected String message;
        @XmlElement(name = "IWMS_ERP_SID")
        protected String iwmserpsid;

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
         * ��ȡid���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * ����id���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
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
