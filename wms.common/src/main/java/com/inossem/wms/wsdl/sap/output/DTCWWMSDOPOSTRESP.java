
package com.inossem.wms.wsdl.sap.output;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 名道智能仓储管理系统外向交货单过账反馈结果数据定义
 * 
 * <p>DT_CW_WMS_DO_POST_RESP complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_DO_POST_RESP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ES_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD" minOccurs="0"/>
 *         &lt;element name="ET_BAPIRET2" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DOCUMENT_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MBLNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MAT_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DT_CW_WMS_DO_POST_RESP", propOrder = {
    "esmsghead",
    "etbapiret2"
})
public class DTCWWMSDOPOSTRESP {

    @XmlElement(name = "ES_MSG_HEAD")
    protected MSGHEAD esmsghead;
    @XmlElement(name = "ET_BAPIRET2")
    protected List<DTCWWMSDOPOSTRESP.ETBAPIRET2> etbapiret2;

    /**
     * 获取esmsghead属性的值。
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
     * 设置esmsghead属性的值。
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
     * Gets the value of the etbapiret2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etbapiret2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getETBAPIRET2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSDOPOSTRESP.ETBAPIRET2 }
     * 
     * 
     */
    public List<DTCWWMSDOPOSTRESP.ETBAPIRET2> getETBAPIRET2() {
        if (etbapiret2 == null) {
            etbapiret2 = new ArrayList<DTCWWMSDOPOSTRESP.ETBAPIRET2>();
        }
        return this.etbapiret2;
    }


    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DOCUMENT_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MBLNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MAT_DOC_ITEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DOC_ITEM_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "documentnum",
        "mblnr",
        "matdocitem",
        "docnum",
        "docitemnum",
        "docyear",
        "wadatist",
        "ernam",
        "type",
        "id",
        "number",
        "message",
        "iwmserpsid"
    })
    public static class ETBAPIRET2 {

        @XmlElement(name = "DOCUMENT_NUM")
        protected String documentnum;
        @XmlElement(name = "MBLNR")
        protected String mblnr;
        @XmlElement(name = "MAT_DOC_ITEM")
        protected String matdocitem;
        @XmlElement(name = "DOC_NUM")
        protected String docnum;
        @XmlElement(name = "DOC_ITEM_NUM")
        protected String docitemnum;
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
         * 获取documentnum属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDOCUMENTNUM() {
            return documentnum;
        }

        /**
         * 设置documentnum属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDOCUMENTNUM(String value) {
            this.documentnum = value;
        }

        /**
         * 获取mblnr属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMBLNR() {
            return mblnr;
        }

        /**
         * 设置mblnr属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMBLNR(String value) {
            this.mblnr = value;
        }

        /**
         * 获取matdocitem属性的值。
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
         * 设置matdocitem属性的值。
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
         * 获取docnum属性的值。
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
         * 设置docnum属性的值。
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
         * 获取docitemnum属性的值。
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
         * 设置docitemnum属性的值。
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
         * 获取docyear属性的值。
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
         * 设置docyear属性的值。
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
         * 获取wadatist属性的值。
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
         * 设置wadatist属性的值。
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
         * 获取ernam属性的值。
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
         * 设置ernam属性的值。
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
         * 获取type属性的值。
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
         * 设置type属性的值。
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
         * 获取id属性的值。
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
         * 设置id属性的值。
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
         * 获取number属性的值。
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
         * 设置number属性的值。
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
         * 获取message属性的值。
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
         * 设置message属性的值。
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
         * 获取iwmserpsid属性的值。
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
         * 设置iwmserpsid属性的值。
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
