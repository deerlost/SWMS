
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
 *         &lt;element name="DeleteOpeBillResult" type="{http://www.mes30.com/wm_webservices}WmIopRetVal" minOccurs="0"/>
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
    "deleteOpeBillResult"
})
@XmlRootElement(name = "DeleteOpeBillResponse")
public class DeleteOpeBillResponse {

    @XmlElement(name = "DeleteOpeBillResult")
    protected WmIopRetVal deleteOpeBillResult;

    /**
     * ��ȡdeleteOpeBillResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link WmIopRetVal }
     *     
     */
    public WmIopRetVal getDeleteOpeBillResult() {
        return deleteOpeBillResult;
    }

    /**
     * ����deleteOpeBillResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link WmIopRetVal }
     *     
     */
    public void setDeleteOpeBillResult(WmIopRetVal value) {
        this.deleteOpeBillResult = value;
    }

}
