
package com.inossem.wms.wsdl.mes.update;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>WmPhChkItem complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="WmPhChkItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InvId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MvRecId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MtrlId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Bch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WgtDimId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="WgtPerPack" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="CurInvWgt" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="CurInvAmnt" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ConfirmedWgt" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ConfirmedAmnt" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DiffWgt" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="DiffAmnt" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RankId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Mtrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WgtDim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Rank" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Des2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Des1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WmPhChkItem", propOrder = {
    "invId",
    "mvRecId",
    "location",
    "mtrlId",
    "bch",
    "wgtDimId",
    "wgtPerPack",
    "curInvWgt",
    "curInvAmnt",
    "confirmedWgt",
    "confirmedAmnt",
    "diffWgt",
    "diffAmnt",
    "rankId",
    "mtrl",
    "wgtDim",
    "rank",
    "locationId",
    "des2",
    "des1"
})
public class WmPhChkItem {

    @XmlElement(name = "InvId", required = true, type = Integer.class, nillable = true)
    protected Integer invId;
    @XmlElement(name = "MvRecId", required = true, type = Integer.class, nillable = true)
    protected Integer mvRecId;
    @XmlElement(name = "Location")
    protected String location;
    @XmlElement(name = "MtrlId", required = true, type = Integer.class, nillable = true)
    protected Integer mtrlId;
    @XmlElement(name = "Bch")
    protected String bch;
    @XmlElement(name = "WgtDimId", required = true, type = Integer.class, nillable = true)
    protected Integer wgtDimId;
    @XmlElement(name = "WgtPerPack", required = true, nillable = true)
    protected BigDecimal wgtPerPack;
    @XmlElement(name = "CurInvWgt", required = true, nillable = true)
    protected BigDecimal curInvWgt;
    @XmlElement(name = "CurInvAmnt", required = true, type = Integer.class, nillable = true)
    protected Integer curInvAmnt;
    @XmlElement(name = "ConfirmedWgt", required = true, nillable = true)
    protected BigDecimal confirmedWgt;
    @XmlElement(name = "ConfirmedAmnt", required = true, type = Integer.class, nillable = true)
    protected Integer confirmedAmnt;
    @XmlElement(name = "DiffWgt", required = true, nillable = true)
    protected BigDecimal diffWgt;
    @XmlElement(name = "DiffAmnt", required = true, type = Integer.class, nillable = true)
    protected Integer diffAmnt;
    @XmlElement(name = "RankId", required = true, type = Integer.class, nillable = true)
    protected Integer rankId;
    @XmlElement(name = "Mtrl")
    protected String mtrl;
    @XmlElement(name = "WgtDim")
    protected String wgtDim;
    @XmlElement(name = "Rank")
    protected String rank;
    @XmlElement(name = "LocationId", required = true, type = Integer.class, nillable = true)
    protected Integer locationId;
    @XmlElement(name = "Des2")
    protected String des2;
    @XmlElement(name = "Des1")
    protected String des1;

    /**
     * ��ȡinvId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInvId() {
        return invId;
    }

    /**
     * ����invId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInvId(Integer value) {
        this.invId = value;
    }

    /**
     * ��ȡmvRecId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMvRecId() {
        return mvRecId;
    }

    /**
     * ����mvRecId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMvRecId(Integer value) {
        this.mvRecId = value;
    }

    /**
     * ��ȡlocation���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * ����location���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * ��ȡmtrlId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMtrlId() {
        return mtrlId;
    }

    /**
     * ����mtrlId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMtrlId(Integer value) {
        this.mtrlId = value;
    }

    /**
     * ��ȡbch���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBch() {
        return bch;
    }

    /**
     * ����bch���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBch(String value) {
        this.bch = value;
    }

    /**
     * ��ȡwgtDimId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWgtDimId() {
        return wgtDimId;
    }

    /**
     * ����wgtDimId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWgtDimId(Integer value) {
        this.wgtDimId = value;
    }

    /**
     * ��ȡwgtPerPack���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWgtPerPack() {
        return wgtPerPack;
    }

    /**
     * ����wgtPerPack���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWgtPerPack(BigDecimal value) {
        this.wgtPerPack = value;
    }

    /**
     * ��ȡcurInvWgt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCurInvWgt() {
        return curInvWgt;
    }

    /**
     * ����curInvWgt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCurInvWgt(BigDecimal value) {
        this.curInvWgt = value;
    }

    /**
     * ��ȡcurInvAmnt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCurInvAmnt() {
        return curInvAmnt;
    }

    /**
     * ����curInvAmnt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCurInvAmnt(Integer value) {
        this.curInvAmnt = value;
    }

    /**
     * ��ȡconfirmedWgt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConfirmedWgt() {
        return confirmedWgt;
    }

    /**
     * ����confirmedWgt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConfirmedWgt(BigDecimal value) {
        this.confirmedWgt = value;
    }

    /**
     * ��ȡconfirmedAmnt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getConfirmedAmnt() {
        return confirmedAmnt;
    }

    /**
     * ����confirmedAmnt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setConfirmedAmnt(Integer value) {
        this.confirmedAmnt = value;
    }

    /**
     * ��ȡdiffWgt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDiffWgt() {
        return diffWgt;
    }

    /**
     * ����diffWgt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDiffWgt(BigDecimal value) {
        this.diffWgt = value;
    }

    /**
     * ��ȡdiffAmnt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDiffAmnt() {
        return diffAmnt;
    }

    /**
     * ����diffAmnt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDiffAmnt(Integer value) {
        this.diffAmnt = value;
    }

    /**
     * ��ȡrankId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRankId() {
        return rankId;
    }

    /**
     * ����rankId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRankId(Integer value) {
        this.rankId = value;
    }

    /**
     * ��ȡmtrl���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMtrl() {
        return mtrl;
    }

    /**
     * ����mtrl���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMtrl(String value) {
        this.mtrl = value;
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

    /**
     * ��ȡrank���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRank() {
        return rank;
    }

    /**
     * ����rank���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRank(String value) {
        this.rank = value;
    }

    /**
     * ��ȡlocationId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLocationId() {
        return locationId;
    }

    /**
     * ����locationId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLocationId(Integer value) {
        this.locationId = value;
    }

    /**
     * ��ȡdes2���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDes2() {
        return des2;
    }

    /**
     * ����des2���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDes2(String value) {
        this.des2 = value;
    }

    /**
     * ��ȡdes1���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDes1() {
        return des1;
    }

    /**
     * ����des1���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDes1(String value) {
        this.des1 = value;
    }

}
