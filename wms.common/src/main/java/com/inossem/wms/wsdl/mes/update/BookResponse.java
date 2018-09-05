
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
 *         &lt;element name="BookResult" type="{http://www.mes30.com/wm_webservices}WmIopRetVal" minOccurs="0"/>
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
    "bookResult"
})
@XmlRootElement(name = "BookResponse")
public class BookResponse {

    @XmlElement(name = "BookResult")
    protected WmIopRetVal bookResult;

    /**
     * 获取bookResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link WmIopRetVal }
     *     
     */
    public WmIopRetVal getBookResult() {
        return bookResult;
    }

    /**
     * 设置bookResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link WmIopRetVal }
     *     
     */
    public void setBookResult(WmIopRetVal value) {
        this.bookResult = value;
    }

}
