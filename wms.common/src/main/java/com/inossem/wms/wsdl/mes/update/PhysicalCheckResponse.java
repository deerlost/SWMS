
package com.inossem.wms.wsdl.mes.update;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="PhysicalCheckResult" type="{http://www.mes30.com/wm_webservices}ArrayOfString" minOccurs="0"/>
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
    "physicalCheckResult"
})
@XmlRootElement(name = "PhysicalCheckResponse")
public class PhysicalCheckResponse {

    @XmlElement(name = "PhysicalCheckResult")
    protected ArrayOfString physicalCheckResult;

    /**
     * 获取physicalCheckResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getPhysicalCheckResult() {
        return physicalCheckResult;
    }

    /**
     * 设置physicalCheckResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setPhysicalCheckResult(ArrayOfString value) {
        this.physicalCheckResult = value;
    }

}
