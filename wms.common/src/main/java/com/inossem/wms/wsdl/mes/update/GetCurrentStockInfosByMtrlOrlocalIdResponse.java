
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
 *         &lt;element name="GetCurrentStockInfosByMtrlOrlocalIdResult" type="{http://www.mes30.com/wm_webservices}ArrayOfWmInvQueryNewResult" minOccurs="0"/>
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
    "getCurrentStockInfosByMtrlOrlocalIdResult"
})
@XmlRootElement(name = "GetCurrentStockInfosByMtrlOrlocalIdResponse")
public class GetCurrentStockInfosByMtrlOrlocalIdResponse {

    @XmlElement(name = "GetCurrentStockInfosByMtrlOrlocalIdResult")
    protected ArrayOfWmInvQueryNewResult getCurrentStockInfosByMtrlOrlocalIdResult;

    /**
     * ��ȡgetCurrentStockInfosByMtrlOrlocalIdResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWmInvQueryNewResult }
     *     
     */
    public ArrayOfWmInvQueryNewResult getGetCurrentStockInfosByMtrlOrlocalIdResult() {
        return getCurrentStockInfosByMtrlOrlocalIdResult;
    }

    /**
     * ����getCurrentStockInfosByMtrlOrlocalIdResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWmInvQueryNewResult }
     *     
     */
    public void setGetCurrentStockInfosByMtrlOrlocalIdResult(ArrayOfWmInvQueryNewResult value) {
        this.getCurrentStockInfosByMtrlOrlocalIdResult = value;
    }

}
