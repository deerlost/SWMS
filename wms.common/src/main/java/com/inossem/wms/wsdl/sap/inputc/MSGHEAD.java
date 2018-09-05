
package com.inossem.wms.wsdl.sap.inputc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 名道智能仓储管理系统集成通用抬头数据定义
 * 
 * <p>MSG_HEAD complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MSG_HEAD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SENDTIME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RECIVER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SENDER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INTERFACE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SPRAS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SYSTEM_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROXY_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MANDT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSG_HEAD", propOrder = {
    "sendtime",
    "reciver",
    "sender",
    "interfaceid",
    "spras",
    "operator",
    "systemid",
    "proxyid",
    "guid",
    "mandt"
})
public class MSGHEAD {

    @XmlElement(name = "SENDTIME")
    protected String sendtime;
    @XmlElement(name = "RECIVER")
    protected String reciver;
    @XmlElement(name = "SENDER")
    protected String sender;
    @XmlElement(name = "INTERFACE_ID")
    protected String interfaceid;
    @XmlElement(name = "SPRAS")
    protected String spras;
    @XmlElement(name = "OPERATOR")
    protected String operator;
    @XmlElement(name = "SYSTEM_ID")
    protected String systemid;
    @XmlElement(name = "PROXY_ID")
    protected String proxyid;
    @XmlElement(name = "GUID")
    protected String guid;
    @XmlElement(name = "MANDT")
    protected String mandt;

    /**
     * 获取sendtime属性的值。
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
     * 设置sendtime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSENDTIME(String value) {
        this.sendtime = value;
    }

    /**
     * 获取reciver属性的值。
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
     * 设置reciver属性的值。
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
     * 获取sender属性的值。
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
     * 设置sender属性的值。
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
     * 获取interfaceid属性的值。
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
     * 设置interfaceid属性的值。
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
     * 获取spras属性的值。
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
     * 设置spras属性的值。
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
     * 获取operator属性的值。
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
     * 设置operator属性的值。
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
     * 获取systemid属性的值。
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
     * 设置systemid属性的值。
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
     * 获取proxyid属性的值。
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
     * 设置proxyid属性的值。
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
     * 获取guid属性的值。
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
     * 设置guid属性的值。
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
     * 获取mandt属性的值。
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
     * 设置mandt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMANDT(String value) {
        this.mandt = value;
    }

}
