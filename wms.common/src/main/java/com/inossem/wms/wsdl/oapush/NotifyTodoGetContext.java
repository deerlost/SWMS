
package com.inossem.wms.wsdl.oapush;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>notifyTodoGetContext complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="notifyTodoGetContext">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="otherCond" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pageNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rowSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="targets" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notifyTodoGetContext", propOrder = {
    "otherCond",
    "pageNo",
    "rowSize",
    "targets",
    "type"
})
public class NotifyTodoGetContext {

    protected String otherCond;
    protected int pageNo;
    protected int rowSize;
    protected String targets;
    protected int type;

    /**
     * 获取otherCond属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherCond() {
        return otherCond;
    }

    /**
     * 设置otherCond属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherCond(String value) {
        this.otherCond = value;
    }

    /**
     * 获取pageNo属性的值。
     * 
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置pageNo属性的值。
     * 
     */
    public void setPageNo(int value) {
        this.pageNo = value;
    }

    /**
     * 获取rowSize属性的值。
     * 
     */
    public int getRowSize() {
        return rowSize;
    }

    /**
     * 设置rowSize属性的值。
     * 
     */
    public void setRowSize(int value) {
        this.rowSize = value;
    }

    /**
     * 获取targets属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargets() {
        return targets;
    }

    /**
     * 设置targets属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargets(String value) {
        this.targets = value;
    }

    /**
     * 获取type属性的值。
     * 
     */
    public int getType() {
        return type;
    }

    /**
     * 设置type属性的值。
     * 
     */
    public void setType(int value) {
        this.type = value;
    }

}
