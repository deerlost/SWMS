
package com.inossem.wms.wsdl.mes.update;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfWmInvQueryResult complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWmInvQueryResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WmInvQueryResult" type="{http://www.mes30.com/wm_webservices}WmInvQueryResult" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWmInvQueryResult", propOrder = {
    "wmInvQueryResult"
})
public class ArrayOfWmInvQueryResult {

    @XmlElement(name = "WmInvQueryResult", nillable = true)
    protected List<WmInvQueryResult> wmInvQueryResult;

    /**
     * Gets the value of the wmInvQueryResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wmInvQueryResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWmInvQueryResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WmInvQueryResult }
     * 
     * 
     */
    public List<WmInvQueryResult> getWmInvQueryResult() {
        if (wmInvQueryResult == null) {
            wmInvQueryResult = new ArrayList<WmInvQueryResult>();
        }
        return this.wmInvQueryResult;
    }

}
