
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
     * ��ȡpasswd���Ե�ֵ��
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
     * ����passwd���Ե�ֵ��
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
     * ��ȡcredentialCode���Ե�ֵ��
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
     * ����credentialCode���Ե�ֵ��
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
     * ��ȡopeTypeCode���Ե�ֵ��
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
     * ����opeTypeCode���Ե�ֵ��
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
     * ��ȡshiftBeginTime���Ե�ֵ��
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
     * ����shiftBeginTime���Ե�ֵ��
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
     * ��ȡshiftEndTime���Ե�ֵ��
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
     * ����shiftEndTime���Ե�ֵ��
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
     * ��ȡdtoSet���Ե�ֵ��
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
     * ����dtoSet���Ե�ֵ��
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
