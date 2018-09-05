
package com.inossem.wms.wsdl.mes.update;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfWmInvQueryNewResult complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWmInvQueryNewResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WmInvQueryNewResult" type="{http://www.mes30.com/wm_webservices}WmInvQueryNewResult" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWmInvQueryNewResult", propOrder = {
    "wmInvQueryNewResult"
})
public class ArrayOfWmInvQueryNewResult {

    @XmlElement(name = "WmInvQueryNewResult", nillable = true)
    protected List<WmInvQueryNewResult> wmInvQueryNewResult;

    /**
     * Gets the value of the wmInvQueryNewResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wmInvQueryNewResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWmInvQueryNewResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WmInvQueryNewResult }
     * 
     * 
     */
    public List<WmInvQueryNewResult> getWmInvQueryNewResult() {
        if (wmInvQueryNewResult == null) {
            wmInvQueryNewResult = new ArrayList<WmInvQueryNewResult>();
        }
        return this.wmInvQueryNewResult;
    }

}
