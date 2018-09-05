
package com.inossem.wms.wsdl.mes.update;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>WmPhChkItem complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取invId属性的值。
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
     * 设置invId属性的值。
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
     * 获取mvRecId属性的值。
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
     * 设置mvRecId属性的值。
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
     * 获取location属性的值。
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
     * 设置location属性的值。
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
     * 获取mtrlId属性的值。
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
     * 设置mtrlId属性的值。
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
     * 获取bch属性的值。
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
     * 设置bch属性的值。
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
     * 获取wgtDimId属性的值。
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
     * 设置wgtDimId属性的值。
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
     * 获取wgtPerPack属性的值。
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
     * 设置wgtPerPack属性的值。
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
     * 获取curInvWgt属性的值。
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
     * 设置curInvWgt属性的值。
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
     * 获取curInvAmnt属性的值。
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
     * 设置curInvAmnt属性的值。
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
     * 获取confirmedWgt属性的值。
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
     * 设置confirmedWgt属性的值。
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
     * 获取confirmedAmnt属性的值。
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
     * 设置confirmedAmnt属性的值。
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
     * 获取diffWgt属性的值。
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
     * 设置diffWgt属性的值。
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
     * 获取diffAmnt属性的值。
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
     * 设置diffAmnt属性的值。
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
     * 获取rankId属性的值。
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
     * 设置rankId属性的值。
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
     * 获取mtrl属性的值。
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
     * 设置mtrl属性的值。
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

    /**
     * 获取rank属性的值。
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
     * 设置rank属性的值。
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
     * 获取locationId属性的值。
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
     * 设置locationId属性的值。
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
     * 获取des2属性的值。
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
     * 设置des2属性的值。
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
     * 获取des1属性的值。
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
     * 设置des1属性的值。
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
