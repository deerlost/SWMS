package com.inossem.wms.wsdl.sap.getdo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������ִܲ�����ϵͳRETURN���ݽṹ����
 * 
 * <p>DT_RETURN complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_RETURN">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MESSAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LOG_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LOG_MSG_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MESSAGE_V1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MESSAGE_V2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MESSAGE_V3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MESSAGE_V4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PARAMETER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ROW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FIELD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SYSTEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_RETURN", propOrder = {
    "type",
    "id",
    "number",
    "message",
    "logno",
    "logmsgno",
    "messagev1",
    "messagev2",
    "messagev3",
    "messagev4",
    "parameter",
    "row",
    "field",
    "system"
})
public class DTRETURN {

    @XmlElement(name = "TYPE")
    protected String type;
    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "NUMBER")
    protected String number;
    @XmlElement(name = "MESSAGE")
    protected String message;
    @XmlElement(name = "LOG_NO")
    protected String logno;
    @XmlElement(name = "LOG_MSG_NO")
    protected String logmsgno;
    @XmlElement(name = "MESSAGE_V1")
    protected String messagev1;
    @XmlElement(name = "MESSAGE_V2")
    protected String messagev2;
    @XmlElement(name = "MESSAGE_V3")
    protected String messagev3;
    @XmlElement(name = "MESSAGE_V4")
    protected String messagev4;
    @XmlElement(name = "PARAMETER")
    protected String parameter;
    @XmlElement(name = "ROW")
    protected String row;
    @XmlElement(name = "FIELD")
    protected String field;
    @XmlElement(name = "SYSTEM")
    protected String system;

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
     * ��ȡlogno���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGNO() {
        return logno;
    }

    /**
     * ����logno���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGNO(String value) {
        this.logno = value;
    }

    /**
     * ��ȡlogmsgno���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGMSGNO() {
        return logmsgno;
    }

    /**
     * ����logmsgno���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGMSGNO(String value) {
        this.logmsgno = value;
    }

    /**
     * ��ȡmessagev1���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSAGEV1() {
        return messagev1;
    }

    /**
     * ����messagev1���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESSAGEV1(String value) {
        this.messagev1 = value;
    }

    /**
     * ��ȡmessagev2���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSAGEV2() {
        return messagev2;
    }

    /**
     * ����messagev2���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESSAGEV2(String value) {
        this.messagev2 = value;
    }

    /**
     * ��ȡmessagev3���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSAGEV3() {
        return messagev3;
    }

    /**
     * ����messagev3���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESSAGEV3(String value) {
        this.messagev3 = value;
    }

    /**
     * ��ȡmessagev4���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSAGEV4() {
        return messagev4;
    }

    /**
     * ����messagev4���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESSAGEV4(String value) {
        this.messagev4 = value;
    }

    /**
     * ��ȡparameter���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARAMETER() {
        return parameter;
    }

    /**
     * ����parameter���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARAMETER(String value) {
        this.parameter = value;
    }

    /**
     * ��ȡrow���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROW() {
        return row;
    }

    /**
     * ����row���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROW(String value) {
        this.row = value;
    }

    /**
     * ��ȡfield���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFIELD() {
        return field;
    }

    /**
     * ����field���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFIELD(String value) {
        this.field = value;
    }

    /**
     * ��ȡsystem���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSYSTEM() {
        return system;
    }

    /**
     * ����system���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSYSTEM(String value) {
        this.system = value;
    }

}
