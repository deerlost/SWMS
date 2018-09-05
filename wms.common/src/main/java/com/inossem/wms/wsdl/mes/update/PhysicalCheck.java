
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
 *         &lt;element name="chkTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="chkShiftId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ipAddr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chkItems" type="{http://www.mes30.com/wm_webservices}ArrayOfWmPhChkItem" minOccurs="0"/>
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
    "chkTime",
    "chkShiftId",
    "ipAddr",
    "chkItems"
})
@XmlRootElement(name = "PhysicalCheck")
public class PhysicalCheck {

    protected String userId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar chkTime;
    protected int chkShiftId;
    protected String ipAddr;
    protected ArrayOfWmPhChkItem chkItems;

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
     * 获取chkTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getChkTime() {
        return chkTime;
    }

    /**
     * 设置chkTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setChkTime(XMLGregorianCalendar value) {
        this.chkTime = value;
    }

    /**
     * 获取chkShiftId属性的值。
     * 
     */
    public int getChkShiftId() {
        return chkShiftId;
    }

    /**
     * 设置chkShiftId属性的值。
     * 
     */
    public void setChkShiftId(int value) {
        this.chkShiftId = value;
    }

    /**
     * 获取ipAddr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * 设置ipAddr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAddr(String value) {
        this.ipAddr = value;
    }

    /**
     * 获取chkItems属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWmPhChkItem }
     *     
     */
    public ArrayOfWmPhChkItem getChkItems() {
        return chkItems;
    }

    /**
     * 设置chkItems属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWmPhChkItem }
     *     
     */
    public void setChkItems(ArrayOfWmPhChkItem value) {
        this.chkItems = value;
    }

}
