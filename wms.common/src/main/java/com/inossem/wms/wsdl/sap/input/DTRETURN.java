
package com.inossem.wms.wsdl.sap.input;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 名道智能仓储管理系统RETURN数据结构定义
 * 
 * <p>DT_RETURN complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取logno属性的值。
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
     * 设置logno属性的值。
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
     * 获取logmsgno属性的值。
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
     * 设置logmsgno属性的值。
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
     * 获取messagev1属性的值。
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
     * 设置messagev1属性的值。
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
     * 获取messagev2属性的值。
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
     * 设置messagev2属性的值。
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
     * 获取messagev3属性的值。
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
     * 设置messagev3属性的值。
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
     * 获取messagev4属性的值。
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
     * 设置messagev4属性的值。
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
     * 获取parameter属性的值。
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
     * 设置parameter属性的值。
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
     * 获取row属性的值。
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
     * 设置row属性的值。
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
     * 获取field属性的值。
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
     * 设置field属性的值。
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
     * 获取system属性的值。
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
     * 设置system属性的值。
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
