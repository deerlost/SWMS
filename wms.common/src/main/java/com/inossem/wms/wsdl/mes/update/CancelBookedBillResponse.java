
package com.inossem.wms.wsdl.mes.update;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="CancelBookedBillResult" type="{http://www.mes30.com/wm_webservices}ArrayOfString" minOccurs="0"/>
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
    "cancelBookedBillResult"
})
@XmlRootElement(name = "CancelBookedBillResponse")
public class CancelBookedBillResponse {

    @XmlElement(name = "CancelBookedBillResult")
    protected ArrayOfString cancelBookedBillResult;

    /**
     * ��ȡcancelBookedBillResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getCancelBookedBillResult() {
        return cancelBookedBillResult;
    }

    /**
     * ����cancelBookedBillResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setCancelBookedBillResult(ArrayOfString value) {
        this.cancelBookedBillResult = value;
    }

}
