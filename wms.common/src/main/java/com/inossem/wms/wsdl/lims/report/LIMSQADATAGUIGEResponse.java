
package com.inossem.wms.wsdl.lims.report;

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
 *         &lt;element name="LIMS_QADATA_GUIGEResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "limsqadataguigeResult"
})
@XmlRootElement(name = "LIMS_QADATA_GUIGEResponse")
public class LIMSQADATAGUIGEResponse {

    @XmlElement(name = "LIMS_QADATA_GUIGEResult")
    protected String limsqadataguigeResult;

    /**
     * ��ȡlimsqadataguigeResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIMSQADATAGUIGEResult() {
        return limsqadataguigeResult;
    }

    /**
     * ����limsqadataguigeResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIMSQADATAGUIGEResult(String value) {
        this.limsqadataguigeResult = value;
    }

}
