
package com.inossem.wms.wsdl.mes.update;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>WmMvRecInteropeDto complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="WmMvRecInteropeDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RecNum" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SrMtrlCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TgMtrlCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WgtPerPack" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Amnt" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Wgt" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="SrBch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TgBch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SrRankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TgRankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SrNodeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TgNodeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DlvBillCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Customer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VehiCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Des" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WgtDim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WmMvRecInteropeDto", propOrder = {
    "recNum",
    "srMtrlCode",
    "tgMtrlCode",
    "wgtPerPack",
    "amnt",
    "wgt",
    "srBch",
    "tgBch",
    "srRankCode",
    "tgRankCode",
    "srNodeCode",
    "tgNodeCode",
    "dlvBillCode",
    "customer",
    "transTypeCode",
    "vehiCode",
    "des",
    "wgtDim"
})
public class WmMvRecInteropeDto {

    @XmlElement(name = "RecNum")
    protected int recNum;
    @XmlElement(name = "SrMtrlCode")
    protected String srMtrlCode;
    @XmlElement(name = "TgMtrlCode")
    protected String tgMtrlCode;
    @XmlElement(name = "WgtPerPack")
    protected double wgtPerPack;
    @XmlElement(name = "Amnt")
    protected int amnt;
    @XmlElement(name = "Wgt")
    protected double wgt;
    @XmlElement(name = "SrBch")
    protected String srBch;
    @XmlElement(name = "TgBch")
    protected String tgBch;
    @XmlElement(name = "SrRankCode")
    protected String srRankCode;
    @XmlElement(name = "TgRankCode")
    protected String tgRankCode;
    @XmlElement(name = "SrNodeCode")
    protected String srNodeCode;
    @XmlElement(name = "TgNodeCode")
    protected String tgNodeCode;
    @XmlElement(name = "DlvBillCode")
    protected String dlvBillCode;
    @XmlElement(name = "Customer")
    protected String customer;
    @XmlElement(name = "TransTypeCode")
    protected String transTypeCode;
    @XmlElement(name = "VehiCode")
    protected String vehiCode;
    @XmlElement(name = "Des")
    protected String des;
    @XmlElement(name = "WgtDim")
    protected String wgtDim;

    /**
     * 获取recNum属性的值。
     * 
     */
    public int getRecNum() {
        return recNum;
    }

    /**
     * 设置recNum属性的值。
     * 
     */
    public void setRecNum(int value) {
        this.recNum = value;
    }

    /**
     * 获取srMtrlCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrMtrlCode() {
        return srMtrlCode;
    }

    /**
     * 设置srMtrlCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrMtrlCode(String value) {
        this.srMtrlCode = value;
    }

    /**
     * 获取tgMtrlCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTgMtrlCode() {
        return tgMtrlCode;
    }

    /**
     * 设置tgMtrlCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTgMtrlCode(String value) {
        this.tgMtrlCode = value;
    }

    /**
     * 获取wgtPerPack属性的值。
     * 
     */
    public double getWgtPerPack() {
        return wgtPerPack;
    }

    /**
     * 设置wgtPerPack属性的值。
     * 
     */
    public void setWgtPerPack(double value) {
        this.wgtPerPack = value;
    }

    /**
     * 获取amnt属性的值。
     * 
     */
    public int getAmnt() {
        return amnt;
    }

    /**
     * 设置amnt属性的值。
     * 
     */
    public void setAmnt(int value) {
        this.amnt = value;
    }

    /**
     * 获取wgt属性的值。
     * 
     */
    public double getWgt() {
        return wgt;
    }

    /**
     * 设置wgt属性的值。
     * 
     */
    public void setWgt(double value) {
        this.wgt = value;
    }

    /**
     * 获取srBch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrBch() {
        return srBch;
    }

    /**
     * 设置srBch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrBch(String value) {
        this.srBch = value;
    }

    /**
     * 获取tgBch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTgBch() {
        return tgBch;
    }

    /**
     * 设置tgBch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTgBch(String value) {
        this.tgBch = value;
    }

    /**
     * 获取srRankCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrRankCode() {
        return srRankCode;
    }

    /**
     * 设置srRankCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrRankCode(String value) {
        this.srRankCode = value;
    }

    /**
     * 获取tgRankCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTgRankCode() {
        return tgRankCode;
    }

    /**
     * 设置tgRankCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTgRankCode(String value) {
        this.tgRankCode = value;
    }

    /**
     * 获取srNodeCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrNodeCode() {
        return srNodeCode;
    }

    /**
     * 设置srNodeCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrNodeCode(String value) {
        this.srNodeCode = value;
    }

    /**
     * 获取tgNodeCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTgNodeCode() {
        return tgNodeCode;
    }

    /**
     * 设置tgNodeCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTgNodeCode(String value) {
        this.tgNodeCode = value;
    }

    /**
     * 获取dlvBillCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDlvBillCode() {
        return dlvBillCode;
    }

    /**
     * 设置dlvBillCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDlvBillCode(String value) {
        this.dlvBillCode = value;
    }

    /**
     * 获取customer属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * 设置customer属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomer(String value) {
        this.customer = value;
    }

    /**
     * 获取transTypeCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransTypeCode() {
        return transTypeCode;
    }

    /**
     * 设置transTypeCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransTypeCode(String value) {
        this.transTypeCode = value;
    }

    /**
     * 获取vehiCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehiCode() {
        return vehiCode;
    }

    /**
     * 设置vehiCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehiCode(String value) {
        this.vehiCode = value;
    }

    /**
     * 获取des属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDes() {
        return des;
    }

    /**
     * 设置des属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDes(String value) {
        this.des = value;
    }

    /**
     * 获取wgtDim属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWgtDim() {
        return wgtDim;
    }

    /**
     * 设置wgtDim属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWgtDim(String value) {
        this.wgtDim = value;
    }

}
