
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
 *         &lt;element name="GetCurrentStockInfosResult" type="{http://www.mes30.com/wm_webservices}ArrayOfWmInvQueryResult" minOccurs="0"/>
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
    "getCurrentStockInfosResult"
})
@XmlRootElement(name = "GetCurrentStockInfosResponse")
public class GetCurrentStockInfosResponse {

    @XmlElement(name = "GetCurrentStockInfosResult")
    protected ArrayOfWmInvQueryResult getCurrentStockInfosResult;

    /**
     * ��ȡgetCurrentStockInfosResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWmInvQueryResult }
     *     
     */
    public ArrayOfWmInvQueryResult getGetCurrentStockInfosResult() {
        return getCurrentStockInfosResult;
    }

    /**
     * ����getCurrentStockInfosResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWmInvQueryResult }
     *     
     */
    public void setGetCurrentStockInfosResult(ArrayOfWmInvQueryResult value) {
        this.getCurrentStockInfosResult = value;
    }

}
