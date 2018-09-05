
package com.inossem.wms.wsdl.mes.update;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="credentialCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opeTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shiftBeginTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="shiftEndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="dtoSet" type="{http://www.mes30.com/wm_webservices}ArrayOfWmMvRecInteropeDto" minOccurs="0"/>
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
    "userId",
    "passwd",
    "credentialCode",
    "opeTypeCode",
    "shiftBeginTime",
    "shiftEndTime",
    "dtoSet"
})
@XmlRootElement(name = "CreateOpeBillAndBook")
public class CreateOpeBillAndBook {

    protected String userId;
    protected String passwd;
    protected String credentialCode;
    protected String opeTypeCode;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar shiftBeginTime;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar shiftEndTime;
    protected ArrayOfWmMvRecInteropeDto dtoSet;

    /**
     * 获取userId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置userId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * 获取passwd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * 设置passwd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswd(String value) {
        this.passwd = value;
    }

    /**
     * 获取credentialCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredentialCode() {
        return credentialCode;
    }

    /**
     * 设置credentialCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredentialCode(String value) {
        this.credentialCode = value;
    }

    /**
     * 获取opeTypeCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpeTypeCode() {
        return opeTypeCode;
    }

    /**
     * 设置opeTypeCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpeTypeCode(String value) {
        this.opeTypeCode = value;
    }

    /**
     * 获取shiftBeginTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getShiftBeginTime() {
        return shiftBeginTime;
    }

    /**
     * 设置shiftBeginTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setShiftBeginTime(XMLGregorianCalendar value) {
        this.shiftBeginTime = value;
    }

    /**
     * 获取shiftEndTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getShiftEndTime() {
        return shiftEndTime;
    }

    /**
     * 设置shiftEndTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setShiftEndTime(XMLGregorianCalendar value) {
        this.shiftEndTime = value;
    }

    /**
     * 获取dtoSet属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWmMvRecInteropeDto }
     *     
     */
    public ArrayOfWmMvRecInteropeDto getDtoSet() {
        return dtoSet;
    }

    /**
     * 设置dtoSet属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWmMvRecInteropeDto }
     *     
     */
    public void setDtoSet(ArrayOfWmMvRecInteropeDto value) {
        this.dtoSet = value;
    }

}
