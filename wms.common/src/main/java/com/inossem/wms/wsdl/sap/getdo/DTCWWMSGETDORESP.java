package com.inossem.wms.wsdl.sap.getdo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ��ά���ִܲ�����ϵͳ��ѯ���򽻻����ӿ�
 * 
 * <p>DT_CW_WMS_GET_DO_RESP complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_CW_WMS_GET_DO_RESP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ES_MSG_HEAD" type="{urn:sinopec:ecc:iwms:cw}MSG_HEAD" minOccurs="0"/>
 *         &lt;element name="ET_RETURN" type="{urn:sinopec:ecc:iwms:cw}DT_RETURN" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ET_LIKP" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LFART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ERZET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ERDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ERNAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BZIRK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VSTEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VKORG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="AUTLF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="KZAZU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WADAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WADAT_IST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LFDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="KODAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ABLAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="INCO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="INCO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VBTYP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VSBED" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="KUNWE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="NAME1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="KUNAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ET_LIPS" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="VBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PSTYV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ERZET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ERDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATKL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CHARG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LICHN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="KDMAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LFIMG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VRKME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="UMVKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="UMVKN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VGBEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VGPOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VTWEG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SPART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BSTNK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="AUART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BEZEI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DT_CW_WMS_GET_DO_RESP", propOrder = {
    "esmsghead",
    "etreturn",
    "etlikp",
    "etlips"
})
public class DTCWWMSGETDORESP {

    @XmlElement(name = "ES_MSG_HEAD")
    protected MSGHEAD esmsghead;
    @XmlElement(name = "ET_RETURN")
    protected List<DTRETURN> etreturn;
    @XmlElement(name = "ET_LIKP")
    protected List<DTCWWMSGETDORESP.ETLIKP> etlikp;
    @XmlElement(name = "ET_LIPS")
    protected List<DTCWWMSGETDORESP.ETLIPS> etlips;

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
     * Gets the value of the etlikp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etlikp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getETLIKP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSGETDORESP.ETLIKP }
     * 
     * 
     */
    public List<DTCWWMSGETDORESP.ETLIKP> getETLIKP() {
        if (etlikp == null) {
            etlikp = new ArrayList<DTCWWMSGETDORESP.ETLIKP>();
        }
        return this.etlikp;
    }

    /**
     * Gets the value of the etlips property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etlips property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getETLIPS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTCWWMSGETDORESP.ETLIPS }
     * 
     * 
     */
    public List<DTCWWMSGETDORESP.ETLIPS> getETLIPS() {
        if (etlips == null) {
            etlips = new ArrayList<DTCWWMSGETDORESP.ETLIPS>();
        }
        return this.etlips;
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
     *         &lt;element name="LFART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ERZET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ERDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ERNAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BZIRK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VSTEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VKORG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="AUTLF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="KZAZU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WADAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WADAT_IST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TDDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LFDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="KODAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ABLAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="INCO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="INCO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VBTYP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VSBED" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="KUNWE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="NAME1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="KUNAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "lfart",
        "erzet",
        "erdat",
        "ernam",
        "bzirk",
        "vstel",
        "vkorg",
        "autlf",
        "kzazu",
        "wadat",
        "wadatist",
        "lddat",
        "tddat",
        "lfdat",
        "kodat",
        "ablad",
        "inco1",
        "inco2",
        "vbtyp",
        "vsbed",
        "kunwe",
        "name1",
        "kunag"
    })
    public static class ETLIKP {

        @XmlElement(name = "VBELN")
        protected String vbeln;
        @XmlElement(name = "LFART")
        protected String lfart;
        @XmlElement(name = "ERZET")
        protected String erzet;
        @XmlElement(name = "ERDAT")
        protected String erdat;
        @XmlElement(name = "ERNAM")
        protected String ernam;
        @XmlElement(name = "BZIRK")
        protected String bzirk;
        @XmlElement(name = "VSTEL")
        protected String vstel;
        @XmlElement(name = "VKORG")
        protected String vkorg;
        @XmlElement(name = "AUTLF")
        protected String autlf;
        @XmlElement(name = "KZAZU")
        protected String kzazu;
        @XmlElement(name = "WADAT")
        protected String wadat;
        @XmlElement(name = "WADAT_IST")
        protected String wadatist;
        @XmlElement(name = "LDDAT")
        protected String lddat;
        @XmlElement(name = "TDDAT")
        protected String tddat;
        @XmlElement(name = "LFDAT")
        protected String lfdat;
        @XmlElement(name = "KODAT")
        protected String kodat;
        @XmlElement(name = "ABLAD")
        protected String ablad;
        @XmlElement(name = "INCO1")
        protected String inco1;
        @XmlElement(name = "INCO2")
        protected String inco2;
        @XmlElement(name = "VBTYP")
        protected String vbtyp;
        @XmlElement(name = "VSBED")
        protected String vsbed;
        @XmlElement(name = "KUNWE")
        protected String kunwe;
        @XmlElement(name = "NAME1")
        protected String name1;
        @XmlElement(name = "KUNAG")
        protected String kunag;

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
         * ��ȡlfart���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLFART() {
            return lfart;
        }

        /**
         * ����lfart���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLFART(String value) {
            this.lfart = value;
        }

        /**
         * ��ȡerzet���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getERZET() {
            return erzet;
        }

        /**
         * ����erzet���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setERZET(String value) {
            this.erzet = value;
        }

        /**
         * ��ȡerdat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getERDAT() {
            return erdat;
        }

        /**
         * ����erdat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setERDAT(String value) {
            this.erdat = value;
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
         * ��ȡbzirk���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBZIRK() {
            return bzirk;
        }

        /**
         * ����bzirk���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBZIRK(String value) {
            this.bzirk = value;
        }

        /**
         * ��ȡvstel���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVSTEL() {
            return vstel;
        }

        /**
         * ����vstel���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVSTEL(String value) {
            this.vstel = value;
        }

        /**
         * ��ȡvkorg���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVKORG() {
            return vkorg;
        }

        /**
         * ����vkorg���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVKORG(String value) {
            this.vkorg = value;
        }

        /**
         * ��ȡautlf���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUTLF() {
            return autlf;
        }

        /**
         * ����autlf���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUTLF(String value) {
            this.autlf = value;
        }

        /**
         * ��ȡkzazu���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKZAZU() {
            return kzazu;
        }

        /**
         * ����kzazu���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKZAZU(String value) {
            this.kzazu = value;
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
         * ��ȡvbtyp���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVBTYP() {
            return vbtyp;
        }

        /**
         * ����vbtyp���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVBTYP(String value) {
            this.vbtyp = value;
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
         * ��ȡkunwe���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKUNWE() {
            return kunwe;
        }

        /**
         * ����kunwe���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKUNWE(String value) {
            this.kunwe = value;
        }

        /**
         * ��ȡname1���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNAME1() {
            return name1;
        }

        /**
         * ����name1���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNAME1(String value) {
            this.name1 = value;
        }

        /**
         * ��ȡkunag���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKUNAG() {
            return kunag;
        }

        /**
         * ����kunag���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKUNAG(String value) {
            this.kunag = value;
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
     *         &lt;element name="PSTYV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ERZET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ERDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATKL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CHARG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LICHN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="KDMAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LFIMG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VRKME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="UMVKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="UMVKN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VGBEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VGPOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VTWEG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SPART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BSTNK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="AUART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BEZEI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "pstyv",
        "erzet",
        "erdat",
        "matnr",
        "matkl",
        "werks",
        "lgort",
        "charg",
        "lichn",
        "kdmat",
        "lfimg",
        "meins",
        "vrkme",
        "umvkz",
        "umvkn",
        "vgbel",
        "vgpos",
        "vtweg",
        "spart",
        "bstnk",
        "auart",
        "bezei"
    })
    public static class ETLIPS {

        @XmlElement(name = "VBELN")
        protected String vbeln;
        @XmlElement(name = "POSNR")
        protected String posnr;
        @XmlElement(name = "PSTYV")
        protected String pstyv;
        @XmlElement(name = "ERZET")
        protected String erzet;
        @XmlElement(name = "ERDAT")
        protected String erdat;
        @XmlElement(name = "MATNR")
        protected String matnr;
        @XmlElement(name = "MATKL")
        protected String matkl;
        @XmlElement(name = "WERKS")
        protected String werks;
        @XmlElement(name = "LGORT")
        protected String lgort;
        @XmlElement(name = "CHARG")
        protected String charg;
        @XmlElement(name = "LICHN")
        protected String lichn;
        @XmlElement(name = "KDMAT")
        protected String kdmat;
        @XmlElement(name = "LFIMG")
        protected String lfimg;
        @XmlElement(name = "MEINS")
        protected String meins;
        @XmlElement(name = "VRKME")
        protected String vrkme;
        @XmlElement(name = "UMVKZ")
        protected String umvkz;
        @XmlElement(name = "UMVKN")
        protected String umvkn;
        @XmlElement(name = "VGBEL")
        protected String vgbel;
        @XmlElement(name = "VGPOS")
        protected String vgpos;
        @XmlElement(name = "VTWEG")
        protected String vtweg;
        @XmlElement(name = "SPART")
        protected String spart;
        @XmlElement(name = "BSTNK")
        protected String bstnk;
        @XmlElement(name = "AUART")
        protected String auart;
        @XmlElement(name = "BEZEI")
        protected String bezei;

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
         * ��ȡpstyv���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPSTYV() {
            return pstyv;
        }

        /**
         * ����pstyv���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPSTYV(String value) {
            this.pstyv = value;
        }

        /**
         * ��ȡerzet���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getERZET() {
            return erzet;
        }

        /**
         * ����erzet���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setERZET(String value) {
            this.erzet = value;
        }

        /**
         * ��ȡerdat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getERDAT() {
            return erdat;
        }

        /**
         * ����erdat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setERDAT(String value) {
            this.erdat = value;
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
         * ��ȡmatkl���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATKL() {
            return matkl;
        }

        /**
         * ����matkl���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATKL(String value) {
            this.matkl = value;
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
         * ��ȡlichn���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLICHN() {
            return lichn;
        }

        /**
         * ����lichn���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLICHN(String value) {
            this.lichn = value;
        }

        /**
         * ��ȡkdmat���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKDMAT() {
            return kdmat;
        }

        /**
         * ����kdmat���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKDMAT(String value) {
            this.kdmat = value;
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

        /**
         * ��ȡvgbel���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVGBEL() {
            return vgbel;
        }

        /**
         * ����vgbel���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVGBEL(String value) {
            this.vgbel = value;
        }

        /**
         * ��ȡvgpos���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVGPOS() {
            return vgpos;
        }

        /**
         * ����vgpos���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVGPOS(String value) {
            this.vgpos = value;
        }

        /**
         * ��ȡvtweg���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVTWEG() {
            return vtweg;
        }

        /**
         * ����vtweg���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVTWEG(String value) {
            this.vtweg = value;
        }

        /**
         * ��ȡspart���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPART() {
            return spart;
        }

        /**
         * ����spart���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPART(String value) {
            this.spart = value;
        }

        /**
         * ��ȡbstnk���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBSTNK() {
            return bstnk;
        }

        /**
         * ����bstnk���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBSTNK(String value) {
            this.bstnk = value;
        }

        /**
         * ��ȡauart���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUART() {
            return auart;
        }

        /**
         * ����auart���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUART(String value) {
            this.auart = value;
        }

        /**
         * ��ȡbezei���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBEZEI() {
            return bezei;
        }

        /**
         * ����bezei���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBEZEI(String value) {
            this.bezei = value;
        }

    }

}
