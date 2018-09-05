
package com.inossem.wms.wsdl.mes.update;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
     * ��ȡuserId���Ե�ֵ��
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
     * ����userId���Ե�ֵ��
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
     * ��ȡchkTime���Ե�ֵ��
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
     * ����chkTime���Ե�ֵ��
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
     * ��ȡchkShiftId���Ե�ֵ��
     * 
     */
    public int getChkShiftId() {
        return chkShiftId;
    }

    /**
     * ����chkShiftId���Ե�ֵ��
     * 
     */
    public void setChkShiftId(int value) {
        this.chkShiftId = value;
    }

    /**
     * ��ȡipAddr���Ե�ֵ��
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
     * ����ipAddr���Ե�ֵ��
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
     * ��ȡchkItems���Ե�ֵ��
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
     * ����chkItems���Ե�ֵ��
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
