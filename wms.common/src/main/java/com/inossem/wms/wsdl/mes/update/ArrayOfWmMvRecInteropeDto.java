
package com.inossem.wms.wsdl.mes.update;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfWmMvRecInteropeDto complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWmMvRecInteropeDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WmMvRecInteropeDto" type="{http://www.mes30.com/wm_webservices}WmMvRecInteropeDto" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWmMvRecInteropeDto", propOrder = {
    "wmMvRecInteropeDto"
})
public class ArrayOfWmMvRecInteropeDto {

    @XmlElement(name = "WmMvRecInteropeDto", nillable = true)
    protected List<WmMvRecInteropeDto> wmMvRecInteropeDto;

    /**
     * Gets the value of the wmMvRecInteropeDto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wmMvRecInteropeDto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWmMvRecInteropeDto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WmMvRecInteropeDto }
     * 
     * 
     */
    public List<WmMvRecInteropeDto> getWmMvRecInteropeDto() {
        if (wmMvRecInteropeDto == null) {
            wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
        }
        return this.wmMvRecInteropeDto;
    }

	public void setWmMvRecInteropeDto(List<WmMvRecInteropeDto> wmMvRecInteropeDto) {
		this.wmMvRecInteropeDto = wmMvRecInteropeDto;
	}
    
}
