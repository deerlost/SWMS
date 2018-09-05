
package com.inossem.wms.wsdl.mes.update;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>WmMvRecInteropeDto complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡrecNum���Ե�ֵ��
     * 
     */
    public int getRecNum() {
        return recNum;
    }

    /**
     * ����recNum���Ե�ֵ��
     * 
     */
    public void setRecNum(int value) {
        this.recNum = value;
    }

    /**
     * ��ȡsrMtrlCode���Ե�ֵ��
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
     * ����srMtrlCode���Ե�ֵ��
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
     * ��ȡtgMtrlCode���Ե�ֵ��
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
     * ����tgMtrlCode���Ե�ֵ��
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
     * ��ȡwgtPerPack���Ե�ֵ��
     * 
     */
    public double getWgtPerPack() {
        return wgtPerPack;
    }

    /**
     * ����wgtPerPack���Ե�ֵ��
     * 
     */
    public void setWgtPerPack(double value) {
        this.wgtPerPack = value;
    }

    /**
     * ��ȡamnt���Ե�ֵ��
     * 
     */
    public int getAmnt() {
        return amnt;
    }

    /**
     * ����amnt���Ե�ֵ��
     * 
     */
    public void setAmnt(int value) {
        this.amnt = value;
    }

    /**
     * ��ȡwgt���Ե�ֵ��
     * 
     */
    public double getWgt() {
        return wgt;
    }

    /**
     * ����wgt���Ե�ֵ��
     * 
     */
    public void setWgt(double value) {
        this.wgt = value;
    }

    /**
     * ��ȡsrBch���Ե�ֵ��
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
     * ����srBch���Ե�ֵ��
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
     * ��ȡtgBch���Ե�ֵ��
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
     * ����tgBch���Ե�ֵ��
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
     * ��ȡsrRankCode���Ե�ֵ��
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
     * ����srRankCode���Ե�ֵ��
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
     * ��ȡtgRankCode���Ե�ֵ��
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
     * ����tgRankCode���Ե�ֵ��
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
     * ��ȡsrNodeCode���Ե�ֵ��
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
     * ����srNodeCode���Ե�ֵ��
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
     * ��ȡtgNodeCode���Ե�ֵ��
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
     * ����tgNodeCode���Ե�ֵ��
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
     * ��ȡdlvBillCode���Ե�ֵ��
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
     * ����dlvBillCode���Ե�ֵ��
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
     * ��ȡcustomer���Ե�ֵ��
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
     * ����customer���Ե�ֵ��
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
     * ��ȡtransTypeCode���Ե�ֵ��
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
     * ����transTypeCode���Ե�ֵ��
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
     * ��ȡvehiCode���Ե�ֵ��
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
     * ����vehiCode���Ե�ֵ��
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
     * ��ȡdes���Ե�ֵ��
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
     * ����des���Ե�ֵ��
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
     * ��ȡwgtDim���Ե�ֵ��
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
     * ����wgtDim���Ե�ֵ��
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
