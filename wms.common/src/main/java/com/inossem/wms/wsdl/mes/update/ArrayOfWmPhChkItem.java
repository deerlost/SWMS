
package com.inossem.wms.wsdl.mes.update;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfWmPhChkItem complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWmPhChkItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WmPhChkItem" type="{http://www.mes30.com/wm_webservices}WmPhChkItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWmPhChkItem", propOrder = {
    "wmPhChkItem"
})
public class ArrayOfWmPhChkItem {

    @XmlElement(name = "WmPhChkItem", nillable = true)
    protected List<WmPhChkItem> wmPhChkItem;

    /**
     * Gets the value of the wmPhChkItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wmPhChkItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWmPhChkItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WmPhChkItem }
     * 
     * 
     */
    public List<WmPhChkItem> getWmPhChkItem() {
        if (wmPhChkItem == null) {
            wmPhChkItem = new ArrayList<WmPhChkItem>();
        }
        return this.wmPhChkItem;
    }

}
